package com.skycloud.management.portal.admin.audit.sevice.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.skycloud.management.portal.admin.audit.dao.IAuditDao;
import com.skycloud.management.portal.admin.audit.dao.ISendInfoDao;
import com.skycloud.management.portal.admin.audit.entity.InstanceTypeBO;
import com.skycloud.management.portal.admin.audit.entity.TAuditBO;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.entity.TSendInfoBO;
import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.admin.sysmanage.service.IDeptManageService;
import com.skycloud.management.portal.admin.sysmanage.service.IUserVlanService;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EStoragePool;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.command.res.ListHosts;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;
import com.skycloud.management.portal.front.loadbalance.service.IoadBalanceH3CService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TOrderLogBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.order.service.INicsService;
import com.skycloud.management.portal.front.order.service.IOrderSerivce;
import com.skycloud.management.portal.front.order.service.IProductInstanceRefService;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalOsTypeVO;
import com.skycloud.management.portal.front.resources.rest.CheckPhysicalHost;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalOS;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalVlan;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalZone;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

/**
 * 订单管理业务实现
 * <dl>
 * <dt>类名：AuditSeviceImpl</dt>
 * <dd>描述:</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2012-2-8 下午03:06:47</dd>
 * <dd>创建人： 张爽</dd>
 * </dl>
 */
public class AuditSeviceImpl implements IAuditSevice {

	private IAuditDao auditDao;

	private ISendInfoDao sendInfoDao;

	private ICloudAPISerivce cloudAPIService;

	private IOrderSerivce orderService;

	private AsyncJobVDCService asyncJobVDCService;

	private IInstanceInfoDao instanceInfoDao;

	private ISysParametersDao sysParametersDao;

	private IoadBalanceH3CService loadBalanceH3CServiceImpl;

	private IVMTemplateService vmTemplateService;

	private IMCTemplateService mcTemplateService;

	private INicsService nicsService;

	private IServiceInstanceService serviceInstanceService;

	private IDeptManageService deptService;

	private PhysicalMachinesService physicalMachineService;

	private IUserVlanService userVlanService;

	private IProductInstanceRefService productInstanceRefService;

	@Override
	public List<TInstanceInfoBO> detailOrder(int orderId, int type, int vOrs,
	                                         int orderType) throws Exception {
		List<TInstanceInfoBO> instanceInfos = null;
		// orderType,订单类别:新申请、作废、修改
		if (orderType == 1) {// 1.新申请
			instanceInfos = auditDao.vmOrStorageOrSMDetailInfo(orderId, type,
			                                                   vOrs);
		} else {// 3.作废、2.修改
			if (orderType == 3 || orderType == 4) {// 作废
				instanceInfos = auditDao.detailServiceInfo(orderId, type, vOrs);
			}else
				// to fix bug 1495
				if (orderType == 2) {// 修改
					instanceInfos = auditDao.detailInfo(orderId, type, vOrs);
					TInstanceInfoBO insInfo = instanceInfos.get(0);
					int instanceId = insInfo.getId();
					int templateId = insInfo.getTemplateId();
					int templateType = insInfo.getTemplateType();
					int cpuNum = 0;
					int memorySize = 0;
					int storageSize = 0;
					List<TOrderBO> orders = orderService
					.searchOrdersByInstanceId(instanceId);
					// 查询上一订单信息
					for (TOrderBO order : orders) {
						if (orderId > order.getOrderId()) {
							cpuNum = order.getCpuNum();
							memorySize = order.getMemorySize();
							storageSize = order.getStorageSize();
							break;
						}
					}
					try {

						// 如果是第一次修改实例的订单，查询模板信息
						// to fix bug 0002236(订单详细信息中，第二次修改前的配置参数显示不正确)
						if (storageSize == 0 && cpuNum == 0) {
							if (templateType != 2) {// 虚机模板,ebs没有修改功能
								TTemplateVMBO templatevm = vmTemplateService
								.getTemplateById(templateId);
								if (templatevm != null) {
									cpuNum = templatevm.getCpuNum();
									memorySize = templatevm.getMemorySize();
									storageSize = templatevm.getStorageSize();
								}
							} else {// 小型机模板
								// to fix bug 0002193
								TTemplateMCBO templatemc = mcTemplateService
								.getTemplateById(templateId);
								if (templatemc != null) {
									cpuNum = templatemc.getCpuNum();
									memorySize = templatemc.getMemorySize();
									storageSize = templatemc.getStorageSize();
								}
							}
						}
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					;

					insInfo.setCpuNum(cpuNum);
					insInfo.setMemorySize(memorySize);
					insInfo.setStorageSize(storageSize);

				}
		}

		// if ((type == 1 && vOrs == 1) || type == 2) {//判断非存储，小型机和vm都会有vlan
		// to fix bug:4366
		int insOrderId = orderId;
		if (instanceInfos!=null && instanceInfos.size()>0){
			insOrderId = instanceInfos.get(0).getOrderId();
		}
		List<TNicsBO> nics = auditDao.vmVlanInfo(insOrderId);
		List<TNicsBO> vlan = null;
		for (TInstanceInfoBO infos : instanceInfos) {
			if (orderType == 1) {// 新申请
				// to fix bug:2279 (自定义大小的v-disk订单,不显示磁盘大小)
				// EBS存储
				if (infos.getTemplateType() == 1 && infos.getStorageSize() > 0) {
					// 1.EBS存储没有修改功能， 2.自定义模板大小为0, 3.故用实例中的存储大小
				} else {
					infos.setCpuNum(infos.getTemplateCpuNum());
					infos.setMemorySize(infos.getTemplateMemorySize());
					infos.setStorageSize(infos.getTemplateStorageSize());
				}
			}
			infos.setPool_name(cloudAPIService.getResourcePoolNameById(infos.getResourcePoolsId()));

			// 获得申请虚拟机时所挂的硬盘
			if (type == 1 && vOrs == 1) {
				infos.setStorageInstances(orderService.searchEBSInstanceInfosByVMInstanceId(infos.getId()));
			}

			//这里需要区分物理机与其他 TODO
			if(infos.getTemplateType() == 10){//物理机
				ListPhysicalZone listVO = new ListPhysicalZone();
				listVO.setId(infos.getZoneId());
				List<ListPhysicalZone> phyzones = physicalMachineService.findPhysicalHostZoneByREST(listVO,infos.getResourcePoolsId());
				if (phyzones != null && phyzones.size() > 0) {
					infos.setClusterName(phyzones.get(0).getName());
				}
				//根据操作系统ID，资源池ID，获取OS信息
				ListPhysicalOS listOSVO = new ListPhysicalOS();
				long osId = infos.geteOsId();
				if(osId > 0){
					listOSVO.setId(osId);
				}
				List<PhysicalOsTypeVO> oslist = physicalMachineService.findPhysicalHostOsTypeByREST(listOSVO, infos.getResourcePoolsId());
				PhysicalOsTypeVO osVO = null;
				if(oslist !=null && oslist.size() == 1){
					osVO = oslist.get(0);
					infos.setOsDesc(osVO.getName());
				}
				// 虚拟机或小型机所挂的vlan
				vlan = new ArrayList<TNicsBO>();
				for (TNicsBO tNicsBO : nics) {
					if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
						// to fix bug:0001858
						List<ListPhysicalVlan> vlans = null;
						if (tNicsBO.geteVlanId() != 0) {// id=0时会列出所有Vlan信息
							ListPhysicalVlan listVlan = new ListPhysicalVlan();
							listVlan.setId(tNicsBO.geteVlanId());
							vlans = physicalMachineService.findPhysicalHostVlanByREST(listVlan, infos.getResourcePoolsId());

						}
						if (vlans != null && vlans.size() > 0) {
							tNicsBO.setVlan(vlans.get(0).getBusinesstype()+"-"+vlans.get(0).getVlanid());
						}else{
							//to fix bug:5439
							tNicsBO.setVlan(String.valueOf(tNicsBO.geteVlanId()));
						}
						vlan.add(tNicsBO);
					}
				}
				infos.setNicsBOs(vlan);

			}else if(infos.getTemplateType() == 3){//小型机
				// 资源域等不需要处理
				// 小型机所挂的IP地址
				vlan = new ArrayList<TNicsBO>();
				for (TNicsBO tNicsBO : nics) {
					if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
						infos.setIpAddress(tNicsBO.getIp());
						vlan.add(tNicsBO);
					}
				}
				infos.setNicsBOs(vlan);
			}else{
				List<EZone> zones = cloudAPIService.listZones(infos.getZoneId(),infos.getResourcePoolsId());
				if (zones != null && zones.size() > 0) {
					infos.setClusterName(zones.get(0).getName());
				}
				// 虚拟机或小型机所挂的vlan
				vlan = new ArrayList<TNicsBO>();
				for (TNicsBO tNicsBO : nics) {
					if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
						// to fix bug:0001858
						List<ENetwork> vlans = null;
						if (tNicsBO.geteVlanId() != 0) {
							vlans = cloudAPIService.listNetworks(tNicsBO
							                                     .geteVlanId(),infos.getResourcePoolsId());// id=0时会列出所有Vlan信息
						}
						if (vlans != null && vlans.size() > 0) {
							tNicsBO.setVlan(vlans.get(0).getName());
						}
						vlan.add(tNicsBO);
					}
				}
				infos.setNicsBOs(vlan);
			}
			infos.setProductBO(auditDao.getProductByProId(infos.getProductId()));
			// 云数据备份数据组合
			if (type == 15) {
				JSONObject jsonObject = JSONObject.fromObject(infos
				                                              .getResourceInfo());
				if (jsonObject.containsKey("type")) {
					infos.setBkType(jsonObject.getString("type").equals("1") ? "虚拟机备份"
							: "块存储备份");
				}
				if (jsonObject.containsKey("insName")) {
					infos.setBkInstanceName(jsonObject.getString("insName"));
				}
				if (jsonObject.containsKey("pl")) {
					infos.setBkFrequency(jsonObject.getString("pl") + " Hz");
				}
				if (jsonObject.containsKey("url")) {
					infos.setBkVMUrl(jsonObject.getString("url"));
				} else {
					infos.setBkVMUrl("块存储没有路径");
				}
			}
		}
		// }
		return instanceInfos;
	}

	// 查询是否自动审核
	// 审批功能
	// type：0,同意。1,拒绝,email:下单人email，approveUserId：下单人Id，commit：审批原因，level：下单人roleLevel
	@Override
	public void isAutoApproveUser(int approveUserId, int orderId, int level,
			int type, String commit, String email, int orderType,
			int templateType) throws SQLException {
		if ("1".equals(auditDao.roleIsAutoCommit(level))) {
			TUserBO user = null;
			if (level == 2) {
				user = getFristUser(level, approveUserId);
			} else {
				user = getFristUser(level, 0);
			}
			if (user != null) {
				approveUserId = user.getId();
			} else {
				return;
			}
			approveOrder(approveUserId, orderId, level, type, commit, email,
			             orderType, templateType);
		} else {
			return;
		}
	}

	//获取到期日期(hefk 2012-05-10 add) //bugid=0001709 产品续订之后，已购买周期变为0，到期时间无变化
	private static Date getExpireDate(int period,String unit,Date expireDate){
		if (period<=0 || unit==null || unit.equals("")){
			return null;
		}

		Calendar calendar = Calendar.getInstance();
		calendar.setTime(expireDate);
		int day = 0;
		if (unit.equals("Y")){
			day = calendar.get(Calendar.YEAR);
			calendar.set(Calendar.YEAR, day + period);
		}else if (unit.equals("M")){
			day = calendar.get(Calendar.MONTH);
			calendar.set(Calendar.MONTH, day + period);
		}else if (unit.equals("W")){
			day = calendar.get(Calendar.WEEK_OF_YEAR);
			calendar.set(Calendar.WEEK_OF_YEAR, day + period);
		}else if (unit.equals("D")){
			day = calendar.get(Calendar.DAY_OF_YEAR);
			calendar.set(Calendar.DAY_OF_YEAR, day + period);
		}else if (unit.equals("H")){
			day = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.set(Calendar.HOUR_OF_DAY, day + period);
		}else{
			return null;
		}
		return calendar.getTime();
	}

	// 执行审核功能
	@Override
	public int approveOrder(int approveUserId, int orderId, int level,
			int type, String commit, String email, int orderType,
			int templateType) throws SQLException {
		TOrderLogBO log = new TOrderLogBO();
		log.setOrderId(orderId);
		log.setUserId(approveUserId);
		Date date = new Date(System.currentTimeMillis());
		log.setCreateDt(date);
		log.setCommit(commit);
		TSendInfoBO info = new TSendInfoBO();
		info.setApproveReason(commit);
		info.setOrderId(orderId);
		info.setReceiveAddress(email);
		info.setCreateDt(date);
		// 订单审核通过的做法
		try {
			int audit_project_switch = ConstDef.curProjectId;
			if (type == 0) {
				log.setState(level);

				//审核订单时提示管理员：用户A没有在资源池1上申请资源的权限，不能审核通过；
				/* 暂时不使用给用户组分配资源池的功能 TODO
			boolean poolCheckFlag = this.checkResourcePoolAssigned(orderId);
			//*/
				if (level == 4) {
					info.setState(2);

					if (audit_project_switch == 1) {//云平台
						// elaster
						// ...加上备份及服务的审批和监控及服务的审批的相关instance表的修改
						List<InstanceTypeBO> instanceTypeBOs = auditDao	.findTemplateTypeList(orderId, orderType);
						for (InstanceTypeBO instanceType : instanceTypeBOs) {
							// 备份服务等实例，修改状态为通过。
							// 6负载均衡，7安全组，8:带宽,  9公网IP, 15:数据云备份广东vdc专用
							// to fix bug:2308
							if (instanceType.getTemplateType() == 4 || instanceType.getTemplateType() == 5) {//备份和监控
								auditDao.updateInstance(instanceType.getId(),	 orderType,	0, orderId);// 0表示审核通过
							}else if (instanceType.getTemplateType() == 7){
								//判断是否给用户分配了vlan
							}else{
								//让job修改资源实例状态;
							}

							//to fix bug:3613
							if (orderType==1){//新申请
								int serviceState = 3;
								if (instanceType.getTemplateType()==4 || instanceType.getTemplateType() == 5){
									serviceState = 2;
								}
								serviceInstanceService.updateServiceStateByInstanceInfoId(serviceState, instanceType.getId());
							}else if (orderType==3){//退订申请
								serviceInstanceService.updateServiceStateByInstanceInfoId(6, instanceType.getId());
							}else if (orderType==2){//修改申请
								//fix bug:5359 增加参数orderId
								//fix bug:4996
								//从order表向instance表写入修改的参数
								auditDao.updateInstanceState_VDC(instanceType.getId(), orderType, orderId);
								//								//1.查询服务实例
								//								TServiceInstanceBO serviceInstanceBO = serviceInstanceService.searchServiceInstanceByInstanceInfoId(instanceType.getId());
								//								int oldOrderId = serviceInstanceBO.getOrderId();
								//								int historyId = serviceInstanceBO.getId();
								//								//2.修改老的服务实例
								//								serviceInstanceBO.setHistoryState(1);//1,历史服务
								//								serviceInstanceService.update(serviceInstanceBO);
								TOrderBO orderBO = orderService.selectOrderByOrderId(orderId);
								//								//3.创建新的服务实例
								//								serviceInstanceBO.setProductId(orderBO.getProductId());
								//								serviceInstanceBO.setOrderId(orderId);
								//								serviceInstanceBO.setHistoryId(historyId);//变革前的服务
								//								serviceInstanceBO.setHistoryState(0);//0,新服务
								//								serviceInstanceService.save(serviceInstanceBO);
								//								//4.创建新的服务资源实例关系内容
								//								int templateId4Edit = instanceType.getTemplateId();
								//								List<TProductInstanceInfoRefBO>  insRefList = productInstanceRefService.searchByOrderId(oldOrderId);
								//								for (TProductInstanceInfoRefBO piRef:insRefList){
								//									piRef.setOrderId(orderId);
								//									piRef.setProductId(orderBO.getProductId());
								//									if(piRef.getTemplateId()==templateId4Edit){
								//										piRef.setTemplateId(orderBO.getTemplateId());
								//									}
								//									productInstanceRefService.save(piRef);
								//								}
								//5.修改资源实例中的模板ID
								TInstanceInfoBO instanceInfo =  instanceInfoDao.findInstanceInfoById(instanceType.getId());
								instanceInfo.setTemplateId(orderBO.getTemplateId());
								instanceInfo.setProductId(orderBO.getProductId());
								instanceInfoDao.update(instanceInfo);
							}
						}//end for
						    //fix bug:7652
							//修改续订到期时间begin
							 if (orderType==4){ //续订申请
								TOrderBO orderInfo = null;
								//获取续订订单对象
								orderInfo = orderService.selectOrderByOrderId(orderId);
								if (orderInfo!=null){
									//续费周期
									int period = orderInfo.getStorageSize();
									//续费参数
									String resourceInfo = orderInfo.getResourceInfo();
									JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
									if (jsonObject!=null && jsonObject.containsKey("unit")) {
										//获取续费单位: Y=年,M=月
										String unit=jsonObject.getString("unit");
										//获取服务实例对象
//										TServiceInstanceBO  serviceInstance = serviceInstanceService.searchServiceInstanceByInstanceInfoId(instanceType.getId());
										TServiceInstanceBO  serviceInstance = serviceInstanceService.searchById(orderInfo.getServiceInstanceId());
										
										Date expireDateOld = serviceInstance.getExpiryDate();
										//获取续费后的过期时间
										Date expireDate = this.getExpireDate(period, unit, expireDateOld);
										if (serviceInstance!=null){
											//续费后过期时间
											serviceInstance.setExpiryDate(expireDate);
											//续费后总的周期
											serviceInstance.setPeriod(period+serviceInstance.getPeriod());
											serviceInstanceService.update(serviceInstance);
										}
									}
								}
							}
							//修改续订到期时间end

							//这个状态让job去维护
							//						if (orderType==3){//退订时修改服务实例状态为正在退订
							//	                        serviceInstanceService.updateServiceStateByInstanceInfoId(6, instanceType.getId());
							//						}
							// 负载均衡，调华山接口，王海东加的
							// 取消by CQ
							// if(instanceType.getTemplateType()==6){
							// String newVirtualServerName="";
							// List<VServiceRowPO> vServerList =
							// loadBalanceH3CServiceImpl.queryVirtualServiceList();
							// List<String> insVServerList =
							// this.getVServerForLoadBalaInstance();
							// if(insVServerList.size()>0){
							// vServerList.removeAll(insVServerList);
							// if(vServerList.size()>0){
							// newVirtualServerName = vServerList.get(0).getName();
							// }
							// }else{
							// newVirtualServerName = vServerList.get(0).getName();
							// }
							// auditDao.updateInstance(orderId,
							// instanceType.getTemplateType(), orderType,
							// newVirtualServerName);
							// }
						
					} else if (audit_project_switch == 2) {
						// ...加上备份及服务的审批和监控及服务的审批的相关instance表的修改
						List<InstanceTypeBO> instanceTypeBOs = auditDao	.findTemplateTypeList(orderId, orderType);
						for (InstanceTypeBO instanceType : instanceTypeBOs) {
							// 备份服务等实例，修改状态为通过。update by CQ 2012-05-04
							// 4：表示备份；5：表示监控；6：表示负载均衡；10：表示物理机'; 15:数据云备份
							if (instanceType.getTemplateType() == 4
									|| instanceType.getTemplateType() == 5
									|| instanceType.getTemplateType() == 15) {
								// || instanceType.getTemplateType() == 6
								// || instanceType.getTemplateType() == 10
								auditDao.updateInstance(instanceType.getId(), orderType,	0, orderId);// 0表示审核通过
							} else {
								auditDao.updateInstanceState_VDC(orderId, orderType, orderId);
								// 审核通过更新异步JOB表的状态
								updateAsyncJobString(AuditStateVDC.PASS_AUDIT,	orderId, approveUserId);
							}
						}
					}
					sendInfoDao.insertSendInfo(info);

				}
			} else if (type == 1) {// 订单审核拒绝的做法
				log.setState(5);
				info.setState(1);
				sendInfoDao.insertSendInfo(info);
				// to fix bug:0001973 新申请类型订单 审核拒绝， 实例需要改为作废状态:i.state = 4
				// if(orderType!=1){//新申请类型订单
				// auditDao.updateInstance(orderId, templateType, orderType, 1);
				// }

				// to fix bug:2400(门户虚机退订，订单审核不通过，虚机不能使用)
				List<InstanceTypeBO> instanceTypeBOs = auditDao	.findTemplateTypeList(orderId, orderType);
				for (InstanceTypeBO instanceType : instanceTypeBOs) {
					auditDao.updateInstance(instanceType.getId(), orderType, 1, orderId);// 1表示审核拒绝
					if (audit_project_switch == 1) {//云平台项目
						// elaster

						if (orderType==3){//退订的服务实例状态时修改为可用
							//	                    serviceInstanceService.updateServiceStateByInstanceInfoId(2, instanceType.getId());
							TServiceInstanceBO  serviceIns =serviceInstanceService.searchServiceInstanceByInstanceInfoId(instanceType.getId());
							if (serviceIns!=null ){
								int serviceId = serviceIns.getId();
								serviceInstanceService.rollbackServiceStateByServiceId(serviceId);
							}
						}else if (orderType==1){//新申请的服务实例状态修改为作废
							//	                    instanceInfoDao.updateServiceState(instanceType.getId(),4);
							serviceInstanceService.updateServiceStateByInstanceInfoId(4, instanceType.getId());
						}else if (orderType==2){//修改申请的服务实例,回滚到修改前的服务实例
							//取消订单实例的修改
							serviceInstanceService.cancelUpdateServiceInstanceByOrderId(orderId);
//							TOrderBO orderBO = orderService.selectOrderByOrderId(orderId);
//							if(orderBO!=null){
//								int serviceId = orderBO.getServiceInstanceId();
//								TServiceInstanceBO  serviceInstanceBo = serviceInstanceService.searchById(serviceId);
//								int serviceHistoryId = 0;
//								if(serviceInstanceBo!=null){
//									serviceHistoryId = serviceInstanceBo.getHistoryId();
//									TServiceInstanceBO  serviceInstanceHistoryBo = serviceInstanceService.searchById(serviceHistoryId);
//									//将被修改的服务实例变为当前服务实例
//									serviceInstanceHistoryBo.setHistoryState(0);
//									serviceInstanceService.update(serviceInstanceHistoryBo);
//									//将当前服务实例变为历史服务
//									serviceInstanceBo.setHistoryState(1);
//									serviceInstanceService.update(serviceInstanceBo);
//								}
//							}
						}
					}
				}
				if (audit_project_switch == 1) {
					// elaster
				} else if (audit_project_switch == 2) {
					// 审核不通过更新异步JOB表的状态
					updateAsyncJobString(AuditStateVDC.NOT_PASS_AUDIT, orderId,approveUserId);
				}
			}

		}catch (Exception e) {
			e.printStackTrace();
		}
		// 组织审批流程日志信息
		log.setRoleApproveLevel(level);
		List<TOrderLogBO> logs = new ArrayList<TOrderLogBO>();
		logs.add(log);
		int result = auditDao.updateOrder(level, type, orderId, date);// 更新订单状态
		if (result == 0) {
			return 0;
		}
		auditDao.insertOrderLogs(logs);// 插入订单审批流程信息
		// 审核通过后查看下一次审批人的状态（是否自动审批）
		level = level + 1;
		if (type == 0 && level < 5) {
			isAutoApproveUser(approveUserId, orderId, level, 0, "自动审批", email,orderType, templateType);
		}
		// to fix bug:2477
		// 负载均衡和安全组一起退订，模板类型(templateType): 6负载均衡，7安全组
		//to fix bug:6894 负载均衡和防火墙不再捆绑退订
//		if (orderType == 3 && (templateType == 6 || templateType == 7)) {
//			int templateType_tmp = 6;
//			if (templateType == 6) {
//				templateType_tmp = 7;
//			}
//			List<TAuditBO> orderList = auditDao
//			.queryWaitApproveOrderByInstanceInfo(orderId,
//			                                     templateType_tmp);
//			if (orderList != null && orderList.size() > 0) {
//				int orderId_tmp = orderList.get(0).getOrderId();
//				int level_tmp = orderList.get(0).getState();
//				// to fix bug:2697
//				if (level_tmp < level - 1) {
//					level_tmp = level_tmp + 1;
//					approveOrder(approveUserId, orderId_tmp, level_tmp, type,
//					             commit, email, orderType, templateType_tmp);
//				}
//			}
//		}


		return result;
	}

	@SuppressWarnings("unused")
	private List<String> getVServerForLoadBalaInstance() {
		return instanceInfoDao.queryLoadBalanceWithVirtualServer();
	}

	private void updateAsyncJobString(AuditStateVDC auditState, int orderId,
			int approveUserId) {
		// JOB信息封装开始
		try {
			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
			asynJobVDCPO.setAuditstate(auditState);
			asynJobVDCPO.setOrder_id(orderId);
			asynJobVDCPO.setUser_id(approveUserId);
			asyncJobVDCService.updateAsyncJobAuditStateVDC(asynJobVDCPO);
		} catch (SCSException e) {
			e.printStackTrace();
		}
	}

	// 自动审核时，自动获取上一级审核人
	private TUserBO getFristUser(int level, int approveUserId)
	throws SQLException {
		TUserBO user = auditDao.firstUserByLevel(level, approveUserId);
		if (user == null) {
			user = getFristUser(level + 1, 0);
		}
		return user;
	}

	public void setAuditDao(IAuditDao auditDao) {
		this.auditDao = auditDao;
	}

	public IAuditDao getAuditDao() {
		return auditDao;
	}

	// 查询待审核订单
	@Override
	public List<TAuditBO> pendingList(TUserBO loginUser,
	                                  QueryCriteria criteria, PageVO vo) throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.pendingList(loginUser, criteria, vo);
	}

	@Override
	public List<TAuditBO> finishList(TUserBO loginUser, QueryCriteria criteria,
	                                 PageVO vo) throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.finishList(loginUser, criteria, vo);
	}

	@Override
	public List<TOrderLogBO> orderLogList(int orderId) throws SQLException {
		return auditDao.orderLogList(orderId);
	}

	@Override
	public List<InstanceTypeBO> getIntanceType(int orderId, int orderType)
	throws SQLException {
		List<InstanceTypeBO> types = auditDao.getIntanceType(orderId, orderType);
		for (InstanceTypeBO instanceTypeBO : types) {
			if (instanceTypeBO != null) {
				int temType = instanceTypeBO.getTemplateType();
				int storSize = instanceTypeBO.getStorageSize();
				if (temType == 2) {
					instanceTypeBO.setInstanceType(2);
				} else if (temType == 1 && storSize == 0) {
					instanceTypeBO.setInstanceType(1);
				} else if (temType == 1 && storSize != 0) {
					instanceTypeBO.setInstanceType(2);//to fix bug:3045
				} else {
					instanceTypeBO.setInstanceType(temType);
				}
			}
		}
		return types;
	}

	@Override
	public List<TAuditBO> orderNewList(TUserBO loginUser, QueryCriteria criteria)
	throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.getOrderNewList(loginUser, criteria);
	}

	@Override
	public List<TAuditBO> orderCheckingList(TUserBO loginUser,
	                                        QueryCriteria criteria) throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.getOrderCheckingList(loginUser, criteria);
	}

	@Override
	public List<TAuditBO> orderCheckedList(TUserBO loginUser,
	                                       QueryCriteria criteria) throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.getOrderCheckedList(loginUser, criteria);
	}

	@Override
	public List<TAuditBO> orderRefuseList(TUserBO loginUser,
	                                      QueryCriteria criteria) throws SQLException {
		if (loginUser == null) {
			throw new SQLException("登录用户不存在，或者session已过期");
		}
		return auditDao.getOrderRefuseList(loginUser, criteria);
	}

	public ISendInfoDao getSendInfoDao() {
		return sendInfoDao;
	}

	public void setSendInfoDao(ISendInfoDao sendInfoDao) {
		this.sendInfoDao = sendInfoDao;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setOrderService(IOrderSerivce orderService) {
		this.orderService = orderService;
	}

	public IOrderSerivce getOrderService() {
		return orderService;
	}

	@Override
	public int getOrderState(int orderId) throws SQLException {

		return auditDao.getOrderState(orderId);
	}

	@Override
	public int getVlanFreeState(int orderId) throws SQLException {
		int state = 0;
		try {
			List<TInstanceInfoBO> listInstance = instanceInfoDao
			.queryInstanceInfoByOrderId(orderId);
			for (TInstanceInfoBO instance : listInstance) {
				if (instance.getTemplateType() == 1
						&& instance.getStorageSize() == 0) {
					int zoneId = instance.getZoneId().intValue();
					if (zoneId >= 1) {
						//to fix bug:3698,3728,3705
						//自动分配vlan时阀值为1，人工选择vlan是阀值为0
						int freeCount = 0;
						//网卡IP动态获取开关; 1 ： 动态获取IP; 0 : 人工填写IP
						if (ConstDef.getNicsDhcpSwitch()==1){
							freeCount = 1;
						}
						// 获取默认网卡vlan剩余资源
						int vlanDefaultFreeCount = nicsService.searchVlanDefaultFreeCount(zoneId,instance.getResourcePoolsId());
						// 获取其他网卡vlan剩余资源
						//to fix bug:3787
						int vlanOtherFreeCount = freeCount;
						int networkNum = 0;
						// to fix bug 2251
						TTemplateVMBO templateVM = vmTemplateService.getTemplateById(instance.getTemplateId());
						if (templateVM != null) {
							// 获取网卡个数
							networkNum = templateVM.getVethAdaptorNum();
							if (networkNum >= 2) {
								vlanOtherFreeCount = nicsService.searchVlanOtherFreeCount(zoneId,instance.getResourcePoolsId());
							}
						}

						// 获取状态
						//1.dhcp时，判断条件是1，2.人工获取ip是判断条件是0
						if (networkNum >= 2 && vlanDefaultFreeCount < freeCount && vlanOtherFreeCount < freeCount ) {
							state = 3;
						} else if (vlanDefaultFreeCount < freeCount) {
							state = 1;
						} else if (vlanOtherFreeCount < freeCount) {
							state = 2;
						}
						// 退出循环体
						if (state >= 1) {
							break;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return state;
	}

	@Override
	public int updateInstanceInfo(int eHostId,int id) throws SQLException{
		int ret_val = 0;
		try {
			TInstanceInfoBO instanceInfo = instanceInfoDao.searchInstanceInfoByID(id);
			instanceInfo.seteHostId(eHostId);
			ret_val = instanceInfoDao.update(instanceInfo);
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret_val;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IoadBalanceH3CService getLoadBalanceH3CServiceImpl() {
		return loadBalanceH3CServiceImpl;
	}

	public void setLoadBalanceH3CServiceImpl(
			IoadBalanceH3CService loadBalanceH3CServiceImpl) {
		this.loadBalanceH3CServiceImpl = loadBalanceH3CServiceImpl;
	}

	@Override
	public int orderAcount(QueryCriteria criteria, int type, TUserBO user)
	throws SQLException {
		return auditDao.orderAcount(criteria, type, user);
	}

	@Override
	public int queryOrderLogCountByUser(int userId) throws SQLException {
		// fix bug 2204
		return auditDao.queryUnAuditOrderCountByUser(userId);

	}

	@Override
	public String getResourceState(int templateType) {
		String fieldName = ""; // T_SCS_PARAMETERS表中的type字段的值
		int type = -1; // T_SCS_RESOURCE_STATE表中type字段的值
		if (templateType == 1) { // 虚拟机
			fieldName = "RESOURCE_THRESHOLD_HOST";
			type = 0;
		} else if (templateType == 12) { // 块存储
			fieldName = "RESOURCE_THRESHOLD_STORAGE";
			type = 1;
		} else { // 公网IP
			fieldName = "RESOURCE_THRESHOLD_EIP";
			type = 3;
		}

		// 获取对应资源的阀值
		int thresholds = sysParametersDao.findThresholdsByFieldName(fieldName);
		// 获取对应资源的剩余量
		int surplus = auditDao.findSurplusByType(type);

		if (surplus < thresholds) {
			if (templateType == 1) { // 虚拟机资源不足
				return "0";
			} else if (templateType == 12) { // 块存储资源不足
				return "1";
			} else { // 公网IP资源不足
				return "2";
			}
		} else {
			return "ok"; // 资源充足
		}
	}

	/**
	 * 判断用户所属用户组是否分配了订单含有的资源池的使用权限
	 *
	 * @return
	 */
	private boolean checkResourcePoolAssigned(int orderId) {
		boolean  checkOK   = false;//本次效验标志
		TOrderBO orderInfo = null;//订单对象

		//异常MSG
		StringBuffer exceptionMSG = new StringBuffer();

		//审核订单时提示管理员用户A没有在资源池1上申请资源的权限，不能审核通过；
		//检查订单所属用户（是否已分配）使用订单下资源所属资源池申请资源的权限

		try {
			//获取续订订单对象
			orderInfo = orderService.selectOrderByOrderId(orderId);
			if(orderInfo != null){
				//获取订单全部资源的资源池列表信息
				List<ResPoolDeptRelationVO> orderResPoolList = deptService.findOrderResPoolByOrderId(orderId);

				//获取已分配给用户（所属用户组）资源池列表
				List<ResPoolDeptRelationVO> userPoolList = null;
				int userId = orderInfo.getCreatorUserId();
				if(userId > 0){//查询部门相关资源池服务类
					userPoolList = deptService.findUserResPoolByUserId(userId);
				}else{
					exceptionMSG.append("订单编号[").append(orderId).append("]的用户编号为空。");
				}
				//判断订单全部资源池在用户资源池是否存在
				List<ResPoolDeptRelationVO> notValidatPoolList = new ArrayList<ResPoolDeptRelationVO>();
				if(orderResPoolList != null && orderResPoolList.size() > 0
						&& userPoolList != null && userPoolList.size() > 0)
				{
					boolean isOutUserPool = false;
					boolean isNotAssigned = true;
					boolean isNotInValidate = true;
					ResPoolDeptRelationVO orderPBO = null;
					ResPoolDeptRelationVO userPBO = null;
					ResPoolDeptRelationVO valiPBO = null;
					for(int i=0;i<orderResPoolList.size();i++){
						isNotAssigned = true;
						orderPBO = orderResPoolList.get(i);
						//检查是否资源池已分配给订单所属的用户
						for(int j=0;j<userPoolList.size();j++){
							userPBO = orderResPoolList.get(j);
							if(userPBO.getPoolId() == orderPBO.getPoolId()){
								isNotAssigned = false;
								break;
							}
						}
						if(isNotAssigned){
							isOutUserPool = true;
							isNotInValidate = true;
							if(notValidatPoolList != null && notValidatPoolList.size() > 0){
								for(int k=0;k<notValidatPoolList.size();k++){
									valiPBO = notValidatPoolList.get(k);
									if(valiPBO.getPoolId() == orderPBO.getPoolId()){
										isNotInValidate = false;
										break;
									}
								}
							}
							if(isNotInValidate){
								notValidatPoolList.add(orderPBO);
							}
						}

					}
					if(isOutUserPool){
						if(notValidatPoolList != null && notValidatPoolList.size() > 0){
							for(int k=0;k<notValidatPoolList.size();k++){
								valiPBO = notValidatPoolList.get(k);
								exceptionMSG.append("订单编号[").append(orderId).append("]的")
								.append("资源编号[").append(  valiPBO.getResourceId()).append("]")
								.append("资源名称[").append(  valiPBO.getResourceName()).append("]")
								.append("所属资源池[").append(valiPBO.getPoolName()).append("] \n\r")
								;
							}
							exceptionMSG.append("本订单所属用户无权使用，请联系系统管理员。");
						}
					}
				}else{
					if(orderResPoolList == null || orderResPoolList.size() ==0){
						exceptionMSG.append("订单编号[").append(orderId).append("]下资源无对应的资源池，或订单无对应资源。");
					}
					if(userPoolList == null || userPoolList.size() ==0){
						exceptionMSG.append("订单编号[").append(orderId).append("]对应用户没有分配资源池。");
					}
				}
				if(exceptionMSG.length() > 0){
					throw new SQLException(exceptionMSG.toString());
				}

				checkOK = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			//			logger.error(e.getMessage());
			return false;
		}

		return checkOK;
	}

	@Override
	public TNicsBO searchNicsById(int nicsId) throws SQLException {
		// TODO Auto-generated method stub
		return nicsService.searchNicsById(nicsId);
	}

	@Override
	public int updateNics(TNicsBO nics) throws SQLException {
		// TODO Auto-generated method stub
		int networkId = (int)nics.geteVlanId();
		String ipaddress = nics.getIp();
		if (ipaddress!=null &&  !"".equals(ipaddress) && !"0".equals(ipaddress)){
			List<TNicsBO>  listNics = nicsService.searchNicssByNetworkIdAndIp(networkId, ipaddress);
			if (listNics!=null && listNics.size()>=1){
				return 2;//ip已经被使用
			}
		}

		return nicsService.update(nics);
	}

	@Override
	public List<listIpAddressesByNetWork> listIpAddressesByNetWork(long networkId, Integer resourcePoolsId) throws Exception {
		List<listIpAddressesByNetWork> ipAddressListNew = nicsService.listIpAddressesByNetworkId(networkId, resourcePoolsId);
		return ipAddressListNew;
	}

	@Override
	public List<ListHosts> ListHosts(int templateId, int zoneId, Integer resourcePoolsId) throws Exception {
		//获取操作系统名称
		String hypervisor = null;
		int clusterId = 0;
		List<Integer> clusterIdList = new ArrayList<Integer>();
		//获取模板对象
		TTemplateVMBO template = vmTemplateService.getTemplateById(templateId);
		if (template!=null){
			//获取hypervisor
			//列出操作系统信息,
			List<ListTemplates> listTemplate = cloudAPIService.ListTemplates(template.geteOsId(), zoneId, resourcePoolsId);
			if (listTemplate!=null){
				hypervisor = listTemplate.get(0).getHypervisor();
			}
			//to fix bug:5119
			//获取clusterId
			String storeType = template.getStoreType();
			if (storeType!=null && !"".equals(storeType)){
				List<EStoragePool> listStroagePools = cloudAPIService.listStoragePool(0, resourcePoolsId);
				for(EStoragePool storagePool : listStroagePools){
					if (storagePool.getTags()!=null && storagePool.getTags().equals(storeType)){
//						clusterId = (int)storagePool.getClusterid();
						clusterIdList.add((int)storagePool.getClusterid());
					}
				}
			}
		}

		//获取相同操作系统主机列表,模板所在操作系统cluster
		List<ListHosts> listHostsReturnList = new ArrayList<ListHosts>();
		List<ListHosts> listHosts = cloudAPIService.ListHosts(0, zoneId, clusterId, resourcePoolsId);
		for(ListHosts host:listHosts){
			String hypervisorHost = host.getHypervisor();
			if (hypervisor !=null && hypervisorHost!=null && hypervisor.equals(hypervisorHost) ){
				if(clusterIdList.size()==0){
					listHostsReturnList.add(host);
				}else if(clusterIdList.contains(host.getClusterid())){
					listHostsReturnList.add(host);
				}
			}
		}
		return listHostsReturnList;
	}


	@Override
	public List<ENetwork> searchNetworkListDefault(int zoneId, Integer resourcePoolsId, int orderId) throws Exception {
		// 返回列表
		List<ENetwork> networkListReturn = new ArrayList<ENetwork>();
		// 获取默认网卡
		List<ENetwork> networkList = nicsService.searchNetworkListDefault(zoneId, resourcePoolsId,1);
		if (networkList == null) {
			return null;
		}

		// 更加订单ID获取用户ID
		int userId = -1;
		TOrderBO order = orderService.selectOrderByOrderId(orderId);
		if (order != null) {
			userId = order.getCreatorUserId();
		} else {
			return null;
		}
		// 获取用户分配表中的网卡信息
		TUserVlanBO userVlan = new TUserVlanBO();
		userVlan.setUserId(userId);
		userVlan.setType(1);
		//fix bug:7630
		userVlan.setResourcePoolsId(resourcePoolsId);
		List<TUserVlanBO> userVlanList = userVlanService.findUserVlan(userVlan);
		// 用户分配表的网卡信息不为空
		if (userVlanList != null && userVlanList.size() > 0) {
			Map<Long, TUserVlanBO> userVlanMap = new HashMap<Long, TUserVlanBO>();
			for (TUserVlanBO uVlan : userVlanList) {
				userVlanMap.put(uVlan.getVlanId(), uVlan);
			}
			for (ENetwork network : networkList) {
				long networkId = network.getId();
				if (networkId != 0) {
					TUserVlanBO userVlanObj = userVlanMap.get(networkId);
					if (userVlanObj != null) {
						networkListReturn.add(network);
					}
				}
			}
		} else {// 没给用户分配网卡vlan时,取所有空隙资源
			networkListReturn = networkList;
		}
		return networkListReturn;
	}


	@Override
	public List<ENetwork> searchNetworkListOther(int zoneId, Integer resourcePoolsId, int orderId, int networkType) throws Exception {
		// 返回列表
		List<ENetwork> networkListReturn = new ArrayList<ENetwork>();
		// 获取默认网卡
		List<ENetwork> networkList = nicsService.searchNetworkListOther(zoneId, resourcePoolsId,networkType);
		if (networkList == null) {
			return null;
		}
		
		// 更加订单ID获取用户ID
		int userId = -1;
		TOrderBO order = orderService.selectOrderByOrderId(orderId);
		if (order != null) {
			userId = order.getCreatorUserId();
		} else {
			return null;
		}
		// 获取用户分配表中的网卡信息
		TUserVlanBO userVlan = new TUserVlanBO();
		userVlan.setUserId(userId);
		userVlan.setType(networkType);
		//fix bug:7630 7518
		userVlan.setResourcePoolsId(resourcePoolsId);
		List<TUserVlanBO> userVlanList = userVlanService.findUserVlan(userVlan);
		// 用户分配表的网卡信息不为空
		if (userVlanList != null && userVlanList.size() > 0) {
			Map<Long, TUserVlanBO> userVlanMap = new HashMap<Long, TUserVlanBO>();
			for (TUserVlanBO uVlan : userVlanList) {
				userVlanMap.put(uVlan.getVlanId(), uVlan);
			}
			for (ENetwork network : networkList) {
				long networkId = network.getId();
				if (networkId != 0) {
					TUserVlanBO userVlanObj = userVlanMap.get(networkId);
					if (userVlanObj != null) {
						networkListReturn.add(network);
					}
				}
			}
		} else {// 没给用户分配网卡vlan时,取所有空隙资源
			networkListReturn = networkList;
		}
		return networkListReturn;
	}

	@Override
	public List<TNicsBO> searchNicsDhcpByOrderId(int orderId) throws SQLException {
		return nicsService.searchNicsDhcpByOrderId(orderId);
	}



	@Override
	public List<TNicsBO> searchNicsRepeatVlanCountByorderId(int orderId) throws SQLException {
		return nicsService.searchNicsRepeatVlanCountByorderId(orderId);
	}
	
	@Override
    public List<TNicsBO> searchNicsRepeatIPCountByorderId(int orderId) throws SQLException {
	    return nicsService.searchNicsRepeatIPCountByorderId(orderId);
    }
	
	

	@Override
    public int CheckPhysicalHost(int orderId) throws Exception {
		int availableNum = 1;
		List<TInstanceInfoBO> instanceInfos = instanceInfoDao.queryInstanceInfoByOrderId(orderId);
		for(TInstanceInfoBO insInfo:instanceInfos){
			if(insInfo.getTemplateType()==10){
				String response =insInfo.getResourceInfo();
				JSONObject jsonObject = JSONObject.fromObject(response);
//				System.out.println(jsonObject.toString());
//				CheckPhysicalHost resultHost = (CheckPhysicalHost)JSONObject.toBean(jsonObject, CheckPhysicalHost.class);
//				JSONObject.toBean(jsonObject, CheckPhysicalHost.class);
				
//				{"disknumber":8,"ostype":7,"disksize":8,"account":""
//					,"nics":[{"vlanid":1001,"ip":"192.168.101.49"}]
//					         ,"cpunumber":1,"cpuspeed":2.4,"memory":32}
				//fix bug 7834 对象转换错误，对象不存在nics属性
				CheckPhysicalHost resultHost = new CheckPhysicalHost();
				resultHost.setCpunumber(jsonObject.getLong("cpunumber"));
				resultHost.setDisknumber(jsonObject.getLong("disknumber"));
				resultHost.setMemory(jsonObject.getLong("memory"));
				resultHost.setCpuspeed(jsonObject.getDouble("cpuspeed"));
				resultHost.setDisksize(jsonObject.getLong("disksize"));
				resultHost.setZoneId(insInfo.getZoneId());
				availableNum = physicalMachineService.CheckPhysicalHost(resultHost, insInfo.getResourcePoolsId());
			    if(availableNum <=0){
			    	break;
			    }
			}
		}
	    return availableNum;
    }

	public IVMTemplateService getVmTemplateService() {
		return vmTemplateService;
	}

	public void setVmTemplateService(IVMTemplateService vmTemplateService) {
		this.vmTemplateService = vmTemplateService;
	}

	public IMCTemplateService getMcTemplateService() {
		return mcTemplateService;
	}

	public void setMcTemplateService(IMCTemplateService mcTemplateService) {
		this.mcTemplateService = mcTemplateService;
	}

	public INicsService getNicsService() {
		return nicsService;
	}

	public void setNicsService(INicsService nicsService) {
		this.nicsService = nicsService;
	}

	public void setSysParametersDao(ISysParametersDao sysParametersDao) {
		this.sysParametersDao = sysParametersDao;
	}


	public IServiceInstanceService getServiceInstanceService() {
		return serviceInstanceService;
	}


	public void setServiceInstanceService(IServiceInstanceService serviceInstanceService) {
		this.serviceInstanceService = serviceInstanceService;
	}

	public IDeptManageService getDeptService() {
		return deptService;
	}

	public void setDeptService(IDeptManageService deptService) {
		this.deptService = deptService;
	}

	public PhysicalMachinesService getPhysicalMachineService() {
		return physicalMachineService;
	}

	public void setPhysicalMachineService(
			PhysicalMachinesService physicalMachineService) {
		this.physicalMachineService = physicalMachineService;
	}


	public IUserVlanService getUserVlanService() {
		return userVlanService;
	}


	public void setUserVlanService(IUserVlanService userVlanService) {
		this.userVlanService = userVlanService;
	}


	public IProductInstanceRefService getProductInstanceRefService() {
		return productInstanceRefService;
	}


	public void setProductInstanceRefService(IProductInstanceRefService productInstanceRefService) {
		this.productInstanceRefService = productInstanceRefService;
	}



}
