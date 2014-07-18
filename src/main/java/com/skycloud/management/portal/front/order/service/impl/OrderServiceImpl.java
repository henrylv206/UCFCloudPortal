package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.admin.audit.dao.IAuditDao;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IMCTemplateService;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.dao.IOrderHistoryDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TOrderHistoryBO;
import com.skycloud.management.portal.front.order.entity.TOrderLogBO;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.order.service.IOrderSerivce;
import com.skycloud.management.portal.front.order.service.IProductInstanceRefService;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

public class OrderServiceImpl implements IOrderSerivce {

	public static final int STATE_APPLY = 1;// 1：申请状态

	public static final int INSTANCE_STATE_FAIL = 7;

	private IOrderDao orderDao;

	private IInstanceInfoDao instanceInfoDao;

	private IAuditDao auditDao;

	private ICloudAPISerivce cloudAPIService;

	private INicsDao nicsDao;

	private IAsyncJobInfoDAO asyncJobDao;

	private ISysParametersDao parametersDao;

	private ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao;

	private AsyncJobVDCService asyncJobVDCService;

	private IProductInstanceRefService productInstanceRefService;

	private IOrderHistoryDao orderHistoryDao;

	private IServiceInstanceService serviceInstanceService;

	private IProductService productService;

	private IVMTemplateService VMTemplateService;

	private IMCTemplateService MCTemplateService;//小机模板

	private PhysicalMachinesService physicalMachineService;//物理机服务

	@Override
	public int insertOrder(TOrderBO order, List<TInstanceInfoBO> instanceInfolist) throws Exception {
		int return_val = -1;
		if (order == null) {
			throw new Exception("Paramater TOrderBO is null");
		}
		if (instanceInfolist == null) {
			throw new Exception("Paramater List<TInstanceInfoBO> is null");
		}
		if (order.getOrderId() > 0) {
			throw new Exception("Paramater TOrderBO is invalid");
		}
		int newOrderId = this.saveOrder(order);
		if (newOrderId <= 0) {
			throw new Exception("saveOrder fails");
		}
		return_val = newOrderId;
		if (instanceInfolist.size() > 0) {
			// 服务实例设初值
			int serviceInstanceId = 0;
			for (TInstanceInfoBO instanceInfo : instanceInfolist) {
				instanceInfo.setOrderId(newOrderId);
				// 如果是虚拟机，判断vlanId==0，直接设置state为开通失败
				// if (1 == instanceInfo.getTemplateType() &&
				// instanceInfo.getNicsBOs().get(0).geteVlanId() == 0) {
				// instanceInfo.setState(INSTANCE_STATE_FAIL);
				// }
				int instanceId = this.saveInstanceInfo(instanceInfo);
				// 保存历史订单
				TOrderHistoryBO orderHistory = new TOrderHistoryBO();
				orderHistory.setInstanceInfoId(instanceId);
				orderHistory.setOrderId(newOrderId);
				orderHistory.setProductId(instanceInfo.getProductId());
				orderHistory.setTemplateId(instanceInfo.getTemplateId());
				this.orderHistoryDao.save(orderHistory);

				// 向job表里写webservice接口调用数据
				AsyncJobVDCPO vdcPo = instanceInfo.getAsynJobVDCPO();
				int project_switch = ConstDef.curProjectId;
				if (vdcPo != null && project_switch == 2) {
					vdcPo.setOrder_id(newOrderId);
					vdcPo.setInstance_info_id(instanceId);
					asyncJobVDCService.insterAsyncJobVDC(vdcPo);
				}

				// 用户操作日志提交
				TUserLogVO userlogvo = instanceInfo.getUserLogVO();
				if (userlogvo != null) {
					ConstDef.getUserLogService().saveLog(userlogvo);
				}

				// 如果是虚拟机或小型机，则添加网卡信息
				// fix bug 3396 物理机产品详情中缺少网卡-IP地址参数
				if ((instanceInfo.getTemplateType() == 1 && instanceInfo.getStorageSize() == 0) || instanceInfo.getTemplateType() == 3
						|| instanceInfo.getTemplateType() == 10) {
					for (TNicsBO nics : instanceInfo.getNicsBOs()) {
						//						//
						//						if (1 != instanceInfo.getTemplateType()) {
						nics.setVmInstanceInfoId(instanceId);
						nics.setState(ConstDef.STATE_ONE);
						// 保存网卡信息
						this.saveNics(nics);
						//						}
					}
				}

				// 服务实例关联关系
				if (instanceInfo.getProductInstanceInfoRefBO() != null) {
					TProductInstanceInfoRefBO piRef = instanceInfo.getProductInstanceInfoRefBO();
					// 服务实例保存(广东vdc没有服务实例:CurProjectId=2)
					if (ConstDef.getCurProjectId() != 2 && piRef.getPiId() >= 1) {
						Product product = productService.getProduct(instanceInfo.getProductId());
						TServiceInstanceBO serviceInstance = new TServiceInstanceBO();
						serviceInstance.setServiceName(instanceInfo.getInstanceName());
						serviceInstance.setServiceDesc(instanceInfo.getComment());
						serviceInstance.setOrderId(newOrderId);
						serviceInstance.setProductId(instanceInfo.getProductId());
						serviceInstance.setServiceType(instanceInfo.getTemplateType());
						serviceInstance.setExpiryDate(instanceInfo.getExpireDate());
						// 购买周期
						serviceInstance.setPeriod(instanceInfo.getPeriod());
						// 计费方式
						serviceInstance.setUnit(product.getUnit());
						serviceInstance.setPrice(product.getPrice());
						// serviceInstance.setNum(instanceInfo.getPeriod());
						serviceInstance.setFlag("iaas");
						// 如果是虚拟机，判断vlanId==0，直接设置state为开通失败
						// if (1 == instanceInfo.getTemplateType() &&
						// instanceInfo.getNicsBOs().get(0).geteVlanId() == 0) {
						// serviceInstance.setState(INSTANCE_STATE_FAIL);
						// } else {
						serviceInstance.setState(STATE_APPLY);
						// }
						if (piRef.getPiId() == 2) {
							serviceInstance.setServiceName(piRef.getPiName());
							serviceInstance.setServiceDesc(piRef.getDescription());
							serviceInstance.setFlag("paas");
							serviceInstance.setServiceType(50);
							// fix bug 3699
							serviceInstance.setPrice(piRef.getCharge());
						}
						serviceInstanceId = this.serviceInstanceService.save(serviceInstance);
					}
					if (ConstDef.getCurProjectId() == 2 && piRef.getPiId() == 1) {
						// 广东vdc中的iaas不写入服务实例关系表
					} else {
						piRef.setOrderId(newOrderId);
						piRef.setInstanceInfoId(instanceId);
						piRef.setServiceInstanceId(serviceInstanceId);
						productInstanceRefService.save(piRef);
					}

				}

				// 如果是虚机，而且有硬盘要挂载
				if (instanceInfo.getTemplateType() == 1
						&& (instanceInfo.getStorageInstances() != null && instanceInfo.getStorageInstances().size() > 0)) {
					for (TInstanceInfoBO storageInstance : instanceInfo.getStorageInstances()) {
						// 保存存储实例信息
						int disk_instanceId = this.saveInstanceInfo(storageInstance);
						if (disk_instanceId > 0) {
							Iri iri = new Iri();
							iri.setVM_INSTANCE_INFO_ID(instanceId);
							iri.setDISK_INSTANCE_INFO_ID(disk_instanceId);
							iri.setCREATE_USER_ID(order.getCreatorUserId());
							iri.setCREATE_DT(new Timestamp(System.currentTimeMillis()));
							iri.setSTATE(1);
							// 建立虚机与块存储的关联关系
							int id = asyncJobDao.updateiriInfoforVolume(iri);
						}
					}
				}
			}
		}
		return return_val;
	}

	@Override
	public int searchInstanceInfoByInstanceNameAndUserId(int createUserId, String instanceName) throws SCSException {
		try {
			return instanceInfoDao.searchInstanceInfoByInstanceNameAndUserId(createUserId, instanceName);
		}
		catch (SCSException e) {
			e.printStackTrace();
		}
		return 0;
	}

	@Override
	public List<TInstanceInfoBO> searchEBSInstanceInfosByVMInstanceId(int vmInstanceId) throws SCSException {
		if (vmInstanceId <= 0) {
			return null;
		}
		return instanceInfoDao.searchEBSInstanceInfosByVMInstanceId(vmInstanceId);
	}

	@Override
	public int saveOrder(TOrderBO order) throws Exception {
		if (order == null) {
			throw new Exception("Paramater TOrderBO is null");
		}
		return orderDao.save(order);
	}

	@Override
	public int saveInstanceInfo(TInstanceInfoBO instanceInfo) throws Exception {
		if (instanceInfo == null) {
			throw new Exception("Paramater TInstanceInfoBO is null");
		}
		if(instanceInfo.getZoneId() == null){
			//处理zongId为空的情况，数据库不允许为空 ninghao@chinaskycloud.com 2013-01-22
			instanceInfo.setZoneId(new Long(0));
		}
		return instanceInfoDao.save(instanceInfo);
	}

	@Override
	public int deleteNewOrderByid(int orderId, int orderType, int instanceInfoId) throws Exception {
		int ret_val = -1;
		if (orderId <= 0) {
			throw new Exception("Paramater orderId is invalid");
		}
		if (orderType == 1) {// 新订单
			ret_val = instanceInfoDao.deleteByOrderId(orderId);
		} else if (orderType == 2 || orderType == 3) {// 修改单或删除单
			instanceInfoDao.recoveryIntanceInfoStateById(instanceInfoId);
		}

		if (ret_val <= 0) {
			// throw new
			// Exception("instance_info table delete record EXCEPTION");
		}
		ret_val = orderDao.delete(orderId);
		return ret_val;
	}

	@Override
	public TOrderBO selectOrderByOrderId(int orderId) throws Exception {
		if (orderId <= 0) {
			throw new Exception("Paramater orderId is invalid");
		}
		return orderDao.searchOrderById(orderId);
	}

	@Override
	public int selectLastOrderId() throws Exception {
		return orderDao.searchLastId();
	}

	@Override
	public int selectLastInstanceInfoId() throws Exception {
		return instanceInfoDao.searchLastId();
	}

	@Override
	public int saveNics(final TNicsBO nics) throws Exception {
		if (nics == null) {
			throw new Exception("Paramater TNicsBO is null");
		}
		return nicsDao.save(nics);
	}

	private int updateNics(int infoId) throws SCSException {
		int saveFlag = 0;
		try {
			List<TNicsBO> nicsBOs = nicsDao.searchNicssByInstanceId(infoId);
			//保存新的NICS
			if (nicsBOs == null) {
				throw new SCSException("error","Paramater TNicsBO is null");
			}
			//删除原NICS 按照infoId删除其全部NICS TODO
//			nicsDao.deleteNicsByInfoId(infoId);
			//更新网卡信息
			for (TNicsBO nics : nicsBOs) {
				nics.setVmInstanceInfoId(infoId);
				nics.seteVlanId(0);
				nics.setIp("0");
				nics.setState(ConstDef.STATE_ONE);
				saveFlag = nicsDao.update(nics);
				if(saveFlag <= 0){
					throw new SCSException("error","update TNicsBO is error");
				}
			}
		} catch (SQLException e) {
			throw new SCSException(e.getMessage());
		}

		return saveFlag;
	}

	@Override
	public int searchParametersValueByType(String type) throws SCSException {
		if (StringUtils.isBlank(type)) {
			throw new SCSException("Paramater is missing");
		}
		try {
			//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
			int value = -1;
			String strValue = parametersDao.getParameter(type);
			if(null != strValue && !strValue.equals("")){
				value = Integer.parseInt(strValue);
			}
			return value;
		}
		catch (Exception e) {
			throw new SCSException("Paramaters table search error.");
		}
	}

	public ResouceServiceInstanceOperateDao getResouceServiceInstanceOperateDao() {
		return resouceServiceInstanceOperateDao;
	}

	public void setResouceServiceInstanceOperateDao(ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao) {
		this.resouceServiceInstanceOperateDao = resouceServiceInstanceOperateDao;
	}

	public IAuditDao getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(IAuditDao auditDao) {
		this.auditDao = auditDao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public INicsDao getNicsDao() {
		return nicsDao;
	}

	public void setNicsDao(INicsDao nicsDao) {
		this.nicsDao = nicsDao;
	}

	public IAsyncJobInfoDAO getAsyncJobDao() {
		return asyncJobDao;
	}

	public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
		this.asyncJobDao = asyncJobDao;
	}

	public ISysParametersDao getParametersDao() {
		return parametersDao;
	}

	public void setParametersDao(ISysParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	@Override
	public List<TOrderBO> searchOrders(PageVO vo, TUserBO user, QueryCriteria query) throws Exception {
		List<TOrderBO> orderBOs = this.orderDao.searchOrders(vo, user, query);
		for (TOrderBO tOrder : orderBOs) {
			List<TInstanceInfoBO> instanceInfos = null;
			instanceInfos = this.orderDao.findInstanceByOrderId(tOrder.getOrderId(), tOrder.getType());
			// if ((type == 1 && vOrs == 1) || type == 2) {//判断非存储，小型机和vm都会有vlan
			List<TNicsBO> nics = auditDao.vmVlanInfo(tOrder.getOrderId());
			List<TNicsBO> vlan = null;
			Map m = new HashMap();
			for (int k = 0, l = nics.size(); k < l; k++) {
				TNicsBO o = nics.get(k);
				m.put(o.getVmInstanceInfoId() + "", o);
			}
			for (TInstanceInfoBO infos : instanceInfos) {
				infos.setPool_name(cloudAPIService.getResourcePoolNameById(infos.getResourcePoolsId()));
				List<EZone> zones = cloudAPIService.listZones(infos.getZoneId(), infos.getResourcePoolsId());
				if (zones != null && zones.size() > 0) {
					infos.setClusterName(zones.get(0).getName());
				}
				// 获得申请虚拟机时所挂的硬盘
				infos.setStorageInstances(searchEBSInstanceInfosByVMInstanceId(infos.getId()));
				// 虚拟机或小型机所挂的vlan
				vlan = new ArrayList<TNicsBO>();
				TNicsBO tNicsBO = (TNicsBO) m.get(infos.getId() + "");
				if (tNicsBO != null) {
					List<ENetwork> vlans = cloudAPIService.listNetworks(tNicsBO.geteVlanId(), infos.getResourcePoolsId());
					if (vlans != null && vlans.size() > 0) {
						tNicsBO.setVlan(vlans.get(0).getName());
					}
					vlan.add(tNicsBO);
					infos.setNicsBOs(vlan);
				}
				// for (TNicsBO tNicsBO : nics) {
				// if (infos.getId() == tNicsBO.getVmInstanceInfoId()) {
				// List<ENetwork> vlans =
				// cloudAPIService.listNetworks(tNicsBO.geteVlanId());
				// if (vlans != null && vlans.size() > 0) {
				// tNicsBO.setVlan(vlans.get(0).getName());
				// }
				// vlan.add(tNicsBO);
				// }
				// }
				// infos.setNicsBOs(vlan);
			}
			// }
			tOrder.setInstanceInfos(instanceInfos);
		}
		return orderBOs;
	}

	@Override
	public List<TOrderBO> searchOrderServices(PageVO vo, TUserBO user, QueryCriteria query) throws Exception {
		List<TOrderBO> orderBOs = this.orderDao.searchOrders(vo, user, query);
		for (TOrderBO tOrder : orderBOs) {
			List<TServiceInstanceBO> serviceInstances = null;
			serviceInstances = serviceInstanceService.searchByOrderId(tOrder.getOrderId(), tOrder.getType());
			double totalprice = 0;
			for (TServiceInstanceBO sIns : serviceInstances) {
				totalprice = totalprice + sIns.getTotalPrice();
				int state = 0;
				// if (1==tOrder.getType() || 3==tOrder.getType() ){
				// state = searchServiceInstanceState(sIns.getId());
				// }else{
				// state = sIns.getInstanceState()*10;
				// }
				state = serviceInstanceService.checkServiceInstanceStateById(sIns.getId());
				sIns.setState(state);
			}
			tOrder.setPrice(totalprice);
			tOrder.setServiceInstances(serviceInstances);

			// 设置订单审核原因
			List<TOrderLogBO> listOrderLog = auditDao.orderLogList(tOrder.getOrderId());
			if (listOrderLog != null && listOrderLog.size() >= 1) {
				String reason = listOrderLog.get(listOrderLog.size() - 1).getCommit();
				tOrder.setReason(reason);
			}

		}
		return orderBOs;
	}

	@Override
	public List<TOrderBO> searchOrderServices2(PageVO vo, TUserBO user, QueryCriteria query, int orderType) throws Exception {
		List<TOrderBO> orderBOs = this.orderDao.searchOrders(null, user, query);
		for (TOrderBO tOrder : orderBOs) {
			List<TServiceInstanceBO> serviceInstances = null;
			serviceInstances = serviceInstanceService.searchByOrderId(vo, query.getOrderId(), orderType);
			double totalprice = 0;
			for (TServiceInstanceBO sIns : serviceInstances) {
				totalprice = totalprice + sIns.getTotalPrice();
				int state = 0;
				// if (1==tOrder.getType() || 3==tOrder.getType() ){
				// state = searchServiceInstanceState(sIns.getId());
				// }else{
				// state = sIns.getInstanceState()*10;
				// }
				state = serviceInstanceService.checkServiceInstanceStateById(sIns.getId());
				sIns.setState(state);
			}
			tOrder.setPrice(totalprice);
			tOrder.setServiceInstances(serviceInstances);
		}
		return orderBOs;
	}

	// 获取服务实例状态
	private int searchServiceInstanceState(int serviceInstanceId) throws SQLException {
		int return_state = -1;
		try {
			List<TInstanceInfoBO> instanceinfos = this.instanceInfoDao.searchInstanceInfoByServiceInstanceId(serviceInstanceId);
			int state = 0;
			int flag = 0;// flag： 0：状态一致，1：状态不一致，3：开通失败
			for (TInstanceInfoBO ins : instanceinfos) {
				int state1 = ins.getState();
				if (state == 0) {
					state = state1;
				}
				if (state1 == 7) {
					flag = 3;
					break;
				} else if (state != state1) {
					flag = 1;
				}
			}

			return_state = state * 10 + flag;
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return return_state;
	}

	@Override
	public List<TOrderBO> searchOrdersByInstanceId(int instanceId) throws SQLException {
		return this.orderDao.searchOrdersByInstanceId(instanceId);
	}

	@Override
	public int searchOrdersAmount(TUserBO user, QueryCriteria query) throws SQLException {
		return this.orderDao.searchOrdersAmount(user, query);
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	@Override
	public int deleteOrderByid(TOrderBO order) throws Exception {
		int ret_val = 0;
		order.setState(6);
		ret_val = orderDao.update(order);
		List<TInstanceInfoBO> list = order.getInstanceInfos();
		for (TInstanceInfoBO bo : list) {
			bo.setState(4);
			instanceInfoDao.update(bo);
		}
		return ret_val;
	}
	/**
	 * 修改订单
	 * @author ninghao@chinaskycloud.com 2012-12-19
	 * @param vminfos
	 * @return
	 * @throws Exception
	 */
	@Override
	public int updateOrder(List<TVmInfo> vminfos,TUserBO user) throws SCSException
	{
		int excuteUpdate = -1;

		int orderId = 0;
		int templateType = 0;
		int templateId = 0;
		TOrderBO order = null;

		if(vminfos == null || vminfos.size() == 0){
			throw new SCSException("error","订单服务信息为空");
		}else{
			orderId = vminfos.get(0).getOrderId();
		}

		try {
			order = this.orderDao.searchOrderById(orderId);
		} catch (SQLException e) {
			e.printStackTrace();

			throw new SCSException("error","订单信息查询SQL异常！");
		}

		if(order == null){

			throw new SCSException("error","订单信息不存在！");
		}

		List<TInstanceInfoBO> vms = new ArrayList<TInstanceInfoBO>();

		// 订单对象
		order.setLastupdateDt(new Timestamp(System.currentTimeMillis()));
		TTemplateVMBO templateOld = null;
		TTemplateMCBO templateMCOld = null;
		TTemplateVMBO templateNew = null;
		TInstanceInfoBO instanceInfoNew = null;
		int resPoolsId = 0;
		int zoneId = 0;

		for (TVmInfo vminfo : vminfos) {
			Map<String, Object> map = new HashMap<String, Object>();
			templateType = vminfo.getTemplateType();

			if(templateType == 3){//小机模板
				if(vminfo.getTemplateId() != templateId){
					templateId = vminfo.getTemplateId();

					try {
						//查询模板
						templateMCOld = this.MCTemplateService.getTemplateById(templateId);
						resPoolsId = templateMCOld.getResourcePoolsId();
						zoneId = templateMCOld.getZoneId();
					} catch (Exception e) {
						throw new SCSException("error","订单服务模板信息查询错误！");
					}

				}

				if (null == templateMCOld) {
					throw new SCSException("error","保存失败，模板不存在！");
				}
			}else{//除小机外的模板

				if(vminfo.getTemplateId() != templateId){
					templateId = vminfo.getTemplateId();

					try {
						//查询模板
						templateOld = this.VMTemplateService.getTemplateById(templateId);
						resPoolsId = templateOld.getResourcePoolsId();
						zoneId = templateOld.getZoneId();
					} catch (Exception e) {
						throw new SCSException("error","订单服务模板信息查询错误！");
					}

				}

				if (null == templateOld) {
					throw new SCSException("error","保存失败，模板不存在！");
				}
			}


			try {
				instanceInfoNew = instanceInfoDao.searchInstanceInfoByID(vminfo.getInstanceInfoId());
			}
			catch (Exception e) {
				e.printStackTrace();
				throw new SCSException("error", "查询资源实例信息失败！msg=" + e.getMessage());
			}
			if (null == instanceInfoNew) {
				throw new SCSException("error","保存失败，资源实例不存在！");
			}

			String account = "";
			String resourceInfo = instanceInfoNew.getResourceInfo();
			Map<String, String> mapres =JsonUtil.getMap4Json(resourceInfo);
			//bugid 0004997
			if (mapres!=null){
				//fix bug 5047 解析json串错误问题
				if(mapres.get("account") == null || mapres.get("account") == "null"){
					account = "";
				}else{

					try {//fix bug 5059 json对象为空异常处理
						account = mapres.get("account");
					} catch (Exception e) {
						account = "";
					}

				}
			}

			templateNew = new TTemplateVMBO();
			templateNew.setId(templateId);

			//修改订单、模板、服务定义 信息赋值逻辑
			String return_vel = null;
			if (templateType == 1) {
				// 1 : 虚机
				templateNew = templateOld;
				this.getOrderUpdateInfoByVM(templateNew,instanceInfoNew, vminfo, user);
			} else if (templateType == 2) {
				// 2 : 虚拟硬盘
				templateNew = templateOld;
				this.getOrderUpdateInfoByVdisk(templateNew, instanceInfoNew, vminfo, user);
			} else if (templateType == 3) {
				// 3 : 小机
				this.getOrderUpdateInfoByMC(templateMCOld, instanceInfoNew, vminfo);
			} else if (templateType == 4) {
				// 4:备份服务
				templateNew = templateOld;//fix bug8027 备份订单修改模板状态=0的问题
				this.getOrderUpdateInfoByBackup(templateNew, instanceInfoNew, vminfo, user);
			} else if (templateType == 5) {
				// 5：监控服务
				this.getOrderUpdateInfoByMonitor(templateNew, templateOld, vminfo, user);
			} else if (templateType == 6) {
				// 6：负载均衡服务
				this.getOrderUpdateInfoByLoadBalance(templateOld, instanceInfoNew, vminfo, user);
				templateNew = templateOld;
			} else if (templateType == 7) {
				// 7：防火墙服务
				this.getOrderUpdateInfoByFireWall(templateOld, instanceInfoNew, vminfo, user);
				templateNew = templateOld;
			} else if (templateType == 8) {
				// 8：带宽服务
				this.getOrderUpdateInfoByBandWidth(templateOld, instanceInfoNew, vminfo, user);
				templateNew = templateOld;
			} else if (templateType == 9) {
				// 9：公网IP服务
				this.getOrderUpdateInfoByPublicIP(templateNew, templateOld, vminfo, user);
			} else if (templateType == 10) {
				// 10:物理机
				templateNew = templateOld;
				this.getOrderUpdateInfoByPhyHost(templateNew, vminfo, user, instanceInfoNew);
			} else if (templateType == 11) {
				// 11:对象存储
				templateNew = templateOld;
				this.getOrderUpdateInfoByObjetStorage(templateNew, instanceInfoNew, vminfo, user);
			} else if (templateType == 12) {
				// 12 : 弹性块存储
				templateNew = templateOld;
				this.getOrderUpdateInfoByIpsan(templateNew, instanceInfoNew, vminfo, user);
			} else if (templateType == 13) {
				// 13:nas文件系统
				templateNew = templateOld;
				this.getOrderUpdateInfoByNAS(templateNew, instanceInfoNew, vminfo, user);
			} else if (templateType == 15) {
				// 10:云数据备份
				this.getOrderUpdateInfoByDataBackup(templateNew, templateOld, vminfo, user);
			} else if (templateType == 50) {
				// 50:多虚机服务
				this.getOrderUpdateInfoByVMS(templateNew, templateOld, vminfo, user);
			}

			//更新模板
			if(templateType == 3){//小机模板

				try {
					this.MCTemplateService.updateTemplate(templateMCOld);
					excuteUpdate = 1;
				} catch (Exception e) {
					e.printStackTrace();
					excuteUpdate = -1;
					throw new SCSException("error",e.getMessage());
				}

			}else{
				excuteUpdate = this.VMTemplateService.updateTemplateByOrderModify(templateNew);
			}

			//更新实例信息
			mapres =JsonUtil.getMap4Json(instanceInfoNew.getResourceInfo());
			if(account == null)
			 {
	            account="";//account为空是，存储空字符串，不能存储空值
            }
			mapres.put("account", account);
			String parameter = JsonUtil.getJsonString4JavaPOJO(mapres);
			instanceInfoNew.setResourceInfo(parameter);
			//fix bug:7443
			//如果是虚机，而且修改了资源池或资源域，则主机信息清0
			if (instanceInfoNew.getTemplateType() == 1 && (vminfo.getPoolId()!=resPoolsId || vminfo.getZoneId() != zoneId))
			{// 更新主机信息
				instanceInfoNew.seteHostId(0);
			}
			int excuteUpdateInstanceInfo = this.instanceInfoDao.update(instanceInfoNew);
			if (excuteUpdateInstanceInfo==0){
				excuteUpdate = excuteUpdateInstanceInfo;
			}

			//更新Nics网卡
			// 虚拟机，小型机，物理机，则添加网卡信息
			if (instanceInfoNew.getTemplateType() == 1) {
				//fix bug:7443
//				if(instanceInfoNew.getNicsBOs() != null && instanceInfoNew.getNicsBOs().size() > 0)
				if (vminfo.getPoolId()!=resPoolsId || vminfo.getZoneId() != zoneId)
				{// 更新网卡信息
					this.updateNics(instanceInfoNew.getId());
				}
			}
			if (instanceInfoNew.getTemplateType() == 10) {
				if(instanceInfoNew.getNicsBOs() != null && instanceInfoNew.getNicsBOs().size() > 0)
				{// 更新网卡信息 fix bug7702 订单修改时，物理机vlan和ip，删除，重新插入
					this.updateNicsForPhy(instanceInfoNew.getNicsBOs(), instanceInfoNew.getId());
				}
			}


			//判断是否更新失败
			if(excuteUpdate <= 0){
				throw new SCSException("error","修改订单失败，模板更新失败");
			}
		}
		return excuteUpdate;
	}

	/**
	 * 物理机修改订单，更新VLAN和IP信息
	 * @param nicsBOs
	 * @param infoId
	 * @return
	 * @throws SCSException
	 */
	private int updateNicsForPhy(List<TNicsBO> nicsBOs, int infoId) throws SCSException {
		int saveFlag = 0;

		//保存新的NICS
		if (nicsBOs == null) {
			throw new SCSException("error","Paramater TNicsBO is null");
		}
		//删除原NICS 按照infoId删除其全部NICS TODO
		try {
			nicsDao.deleteNicsByInfoId(infoId);
		} catch (SQLException e1) {
			throw new SCSException(e1.getMessage());
		}
		//更新网卡信息
		for (TNicsBO nics : nicsBOs) {
			nics.setVmInstanceInfoId(infoId);
			nics.setState(ConstDef.STATE_ONE);

			try {
				saveFlag = nicsDao.save(nics);
			} catch (SQLException e) {
				throw new SCSException("error",e.getMessage());
			}

			if(saveFlag <= 0){
				throw new SCSException("error","save TNicsBO is error");
			}
		}

		return saveFlag;
	}

	private void getOrderUpdateInfoByVM(TTemplateVMBO templateNew , TInstanceInfoBO instanceInfoNew, TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 1 : 虚机
		// 资源模板更新内容
		templateNew.setResourcePoolsId(vminfo.getPoolId());// 资源池
		templateNew.setZoneId(vminfo.getZoneId());// 资源域
		templateNew.setCpuNum(vminfo.getCpuNum());//CPU个数
		templateNew.setMemorySize(vminfo.getMemorySize());//内存大小
		templateNew.seteOsId(vminfo.getOsId());//系统盘OS选择
		templateNew.setOsDesc(vminfo.getVmos());//系统盘OS名称
		templateNew.setStoreType(vminfo.getStoreType());//存储类型
		templateNew.setExtendAttrJSON(vminfo.getExtendAttrJson());//带宽大小
		templateNew.setVethAdaptorNum(vminfo.getVethAdaptorNum());//网卡个数
		templateNew.setOperType(2);
		// 资源实例模板更新信息
		instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
		instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
		instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
		instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
		instanceInfoNew.setCpuNum(vminfo.getCpuNum());//CPU个数
		instanceInfoNew.setMemorySize(vminfo.getMemorySize());//内存大小
		instanceInfoNew.seteOsId(vminfo.getOsId());//系统盘OS选择
		//to fix bug:5174
		instanceInfoNew.setOsDesc(vminfo.getVmos());//系统盘OS名称
		instanceInfoNew.setStoreType(vminfo.getStoreType());//存储类型
		templateNew.setVethAdaptorNum(vminfo.getVethAdaptorNum());//网卡个数
		//JOB参数
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("zoneId", templateNew.getZoneId());
		map.put("account", user.getAccount());
		map.put("cpuNum", templateNew.getCpuNum());
		map.put("memorySize", templateNew.getMemorySize());
		map.put("period", Integer.valueOf(vminfo.getPeriod()) + vminfo.getUnit());
		String parameter = "";
		parameter = JsonUtil.getJsonString4JavaPOJO(map);
		instanceInfoNew.setResourceInfo(parameter);
	}

	private void getOrderUpdateInfoByVdisk(TTemplateVMBO template, TInstanceInfoBO instanceInfoNew, TVmInfo vminfo, TUserBO user)
	throws SCSException
	{
		// 2 : EBS 虚拟硬盘/虚拟磁盘
		//根据管理员修改信息更新模板信息
		if(vminfo != null && template != null){
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			if(vminfo.getZoneId() > 0){
				template.setZoneId(vminfo.getZoneId());
			}
			template.seteOsId(vminfo.getOsId());
			template.setStoreType(vminfo.getStoreType());
			template.setStorageSize(vminfo.getStorageSize());
			template.setOperType(2);
		}
		if(vminfo != null && template != null && instanceInfoNew != null){
			// 资源实例模板更新信息
			instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
			instanceInfoNew.seteOsId(template.geteOsId());
			instanceInfoNew.setStoreType(template.getStoreType());
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			//JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("zoneId", template.getZoneId());
			map.put("account", user.getAccount());
			map.put("eOsId", template.geteOsId());
			map.put("storeType", template.getStoreType());
			map.put("storageSize", template.getStorageSize());
			map.put("period", vminfo.getPeriod() + vminfo.getUnit());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByMC(TTemplateMCBO template , TInstanceInfoBO instanceInfoNew , TVmInfo vminfo)
	throws SCSException
	{
		// 3 : 小机
		// 资源模板更新内容
		//根据管理员修改信息更新模板信息
		if(vminfo != null && template != null){
			//			template.setCode(null);//模板编码
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			//			if(vminfo.getZoneId() > 0){
			//				template.setZoneId(vminfo.getZoneId());
			//			}
			template.setCputype(vminfo.getVstype());
			template.setCpuNum(vminfo.getCpuNum());
			template.setCpufrequency(vminfo.getCpufrequency());
			template.setMemorySize(vminfo.getMemorySize());
			template.setStorageSize(vminfo.getStorageSize());
			template.setOperType(2);
		}
		if(vminfo != null && template != null && instanceInfoNew != null){
			// 资源实例模板更新信息
			//			instanceInfoNew.setZoneId(vminfo.getZoneId());// 资源域
			instanceInfoNew.setCpuNum(vminfo.getCpuNum());
			instanceInfoNew.setCpufrequency(vminfo.getCpufrequency());
			instanceInfoNew.setMemorySize(vminfo.getMemorySize());
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			//JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("cpuNum", template.getCpuNum());
			map.put("cpuType", template.getCputype());
			map.put("memorySize", template.getMemorySize());
			map.put("storageSize", template.getStorageSize());
			map.put("period", vminfo.getPeriod() + vminfo.getUnit());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByBackup(TTemplateVMBO templateNew , TInstanceInfoBO instanceInfoNew , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 4 : 备份
		//根据管理员修改信息更新模板信息
		if(vminfo != null && templateNew != null){
			templateNew.setType(vminfo.getTemplateType());
			templateNew.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：用户定义的特殊模板
			templateNew.setResourcePoolsId(vminfo.getPoolId());
			//			if(vminfo.getZoneId() > 0){
			//				templateNew.setZoneId(vminfo.getZoneId());
			//			}
			//			templateNew.seteOsId(vminfo.getOsId());
			//			templateNew.setStoreType(vminfo.getStoreType());
			templateNew.setStorageSize(vminfo.getStorageSize());
			templateNew.setOperType(2);
		}
		if(vminfo != null && templateNew != null && instanceInfoNew != null){
			// 资源实例模板更新信息
			//			instanceInfoNew.setZoneId(templateNew.getZoneId());// 资源域
			//			instanceInfoNew.seteOsId(templateNew.geteOsId());
			//			instanceInfoNew.setStoreType(templateNew.getStoreType());
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			//JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			//			map.put("zoneId", templateNew.getZoneId());
			//			map.put("account", user.getAccount());
			//			map.put("eOsId", templateNew.geteOsId());
			//			map.put("storeType", templateNew.getStoreType());
			map.put("storageSize", templateNew.getStorageSize());
			map.put("period", vminfo.getPeriod() + vminfo.getUnit());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByMonitor(TTemplateVMBO templateNew , TTemplateVMBO templateOld , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 5 : 云监控
		//TODO 增加修改逻辑
	}

	private void getOrderUpdateInfoByLoadBalance(TTemplateVMBO templateNew , TInstanceInfoBO instanceInfoNew, TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 6 : 负载均衡
		// 资源模板更新内容
		templateNew.setResourcePoolsId(vminfo.getPoolId());// 资源池
		templateNew.setZoneId(vminfo.getZoneId());// 资源域
		templateNew.setStorageSize(vminfo.getConcurrentNum());// 规则条数
		templateNew.setProtocol(vminfo.getProtocol());//协议类型
		templateNew.setPolicy(vminfo.getPolicy());//策略
		templateNew.setPort(vminfo.getPort());//端口号
		templateNew.setOperType(2);
		// 资源实例模板更新信息
		// instanceInfoNew.setZoneId(vminfo.getZoneId());// 资源域
		instanceInfoNew.setStorageSize(vminfo.getConcurrentNum());// 最大连接数
		instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
		instanceInfoNew.setComment(vminfo.getDescription());// 实例描述

	}

	private void getOrderUpdateInfoByFireWall(TTemplateVMBO templateNew, TInstanceInfoBO instanceInfoNew, TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 7 : 防火墙
		// 资源模板更新内容
		templateNew.setResourcePoolsId(vminfo.getPoolId());// 资源池
		templateNew.setZoneId(vminfo.getZoneId());// 资源域
		templateNew.setStorageSize(vminfo.getStorageSize());// 规则条数
		templateNew.setOperType(2);
		// 资源实例模板更新信息
		// instanceInfoNew.setZoneId(vminfo.getZoneId());// 资源域
		instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
		instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
		instanceInfoNew.setComment(vminfo.getDescription());// 实例描述

	}

	private void getOrderUpdateInfoByBandWidth(TTemplateVMBO templateNew ,TInstanceInfoBO instanceInfoNew , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 8 : 带宽
		templateNew.setResourcePoolsId(vminfo.getPoolId());// 资源池
//		templateNew.setZoneId(vminfo.getZoneId());// 资源域
		templateNew.setStorageSize(vminfo.getStorageSize());// 规则条数
		templateNew.setOperType(2);
		instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 规则条数
		instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
		instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
	}

	private void getOrderUpdateInfoByPublicIP(TTemplateVMBO templateNew , TTemplateVMBO templateOld , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 9 : 公网IP
		//TODO 增加修改逻辑
	}

	/**
	 * 物理机修改信息获取
	 * @param templateNew
	 * @param templateOld
	 * @param vminfo
	 * @param user
	 * @throws SCSException
	 */
	private void getOrderUpdateInfoByPhyHost(TTemplateVMBO templateNew , TVmInfo vminfo,TUserBO user, TInstanceInfoBO instanceInfoNew)
	throws SCSException
	{
		// 10:物理机
		//		Product product = new Product();
		//获取修改后参数
		//		this.physicalMachineService.creatSpecalPhysicalTemplate(vminfo, user, templateNew, product);
		if(vminfo != null && templateNew != null && instanceInfoNew != null){
			templateNew.setOperType(2);
			templateNew.setCpuNum(vminfo.getCpuNum());
			templateNew.setCpufrequency(vminfo.getCpufrequency());
			templateNew.setMemorySize(vminfo.getMemorySize());
			templateNew.setStorageSize(vminfo.getDisknumber()*vminfo.getStorageSize());
			templateNew.setVethAdaptorNum(vminfo.getVethAdaptorNum());//网卡个数
			if(vminfo.getPoolId() > 0){
				templateNew.setResourcePoolsId(vminfo.getPoolId());
			}
			if(vminfo.getZoneId() > 0){
				templateNew.setZoneId(vminfo.getZoneId());
			}
			if(vminfo.getOsId() > 0){
				templateNew.seteOsId(vminfo.getOsId());
			}

			// 资源实例模板更新信息
			instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
			instanceInfoNew.setCpuNum(vminfo.getCpuNum());
			instanceInfoNew.setCpufrequency(vminfo.getCpufrequency());
			instanceInfoNew.setMemorySize(vminfo.getMemorySize());
			instanceInfoNew.setStorageSize(vminfo.getDisknumber()*vminfo.getStorageSize());
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			instanceInfoNew.setVethAdaptorNum(vminfo.getVethAdaptorNum());
			instanceInfoNew.seteOsId(templateNew.geteOsId());//fix bug 5495修改物理机订单，无法修改物理机操作系统
			//JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			String parameter = "";
			// 网卡信息
			List<TNicsBO> nicsList = new ArrayList<TNicsBO>();
			List<Map> mapList = new ArrayList<Map>();
			Map<String, Object> mapNicEach = new HashMap<String, Object>();
			TNicsBO eNics = null;
			int vlanId = 0;

//			int dhcpValue = this.searchParametersValueByType("NICS_DHCP_SWITCH");
			int dhcpValue = 0;//默认为IP手动 ninghao@chinaskycloud.com 2013-01-22
			if (dhcpValue == 0) {//1 ： 动态获取IP; 0 : 人工填写IP
				List<String> ipArray = vminfo.getIpArray();
				List<String> vlanArray = vminfo.getVlanArray();
				if (vminfo.getVethAdaptorNum() == vlanArray.size() && ipArray.size() == vlanArray.size())
				{
					for(int num =0 ;num < vminfo.getVethAdaptorNum() ; num++){
						eNics = new TNicsBO();
						if(vlanArray.get(num) != null) {
							vlanId = Integer.parseInt(vlanArray.get(num));
						}
						eNics.seteVlanId(vlanId);
						eNics.setIp(ipArray.get(num));

						mapNicEach = new HashMap<String, Object>();
						mapNicEach.put("vlanid", eNics.geteVlanId());
						mapNicEach.put("ip", eNics.getIp());
						mapList.add(mapNicEach);

						nicsList.add(eNics);
					}
				}
			} else {
				for(int num =0 ;num < vminfo.getVethAdaptorNum() ; num++){
					eNics = new TNicsBO();
					eNics.seteVlanId(vlanId);
					eNics.setIp("0");

					mapNicEach.put("vlanid", eNics.geteVlanId());
					mapNicEach.put("ip", eNics.getIp());
					mapList.add(mapNicEach);

					nicsList.add(eNics);
				}
			}
			instanceInfoNew.setNicsBOs(nicsList);//最新vlan信息

			map.put("cpunumber", templateNew.getCpuNum());
			map.put("cpuspeed", templateNew.getCpufrequency());
			map.put("memory", templateNew.getMemorySize());
			map.put("disknumber", vminfo.getDisknumber());
			map.put("disksize", vminfo.getStorageSize());
			map.put("ostype", templateNew.geteOsId());
			map.put("nics", JSONArray.fromObject(mapList));
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}

	}

	private void getOrderUpdateInfoByObjetStorage(TTemplateVMBO template, TInstanceInfoBO instanceInfoNew, TVmInfo vminfo, TUserBO user)
	throws SCSException
	{
		// 11 : 对象存储
		// 根据管理员修改信息更新模板信息
		if (vminfo != null && template != null) {
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);// 特殊模板
			// 1：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			if (vminfo.getZoneId() > 0) {
				template.setZoneId(vminfo.getZoneId());
			}
			template.setStorageSize(vminfo.getStorageSize());
			template.setOperType(2);
		}
		if (vminfo != null && template != null && instanceInfoNew != null) {
			// 资源实例模板更新信息
			instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 存储空间
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			// JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("period", vminfo.getPeriod());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByIpsan(TTemplateVMBO template, TInstanceInfoBO instanceInfoNew, TVmInfo vminfo, TUserBO user)
	throws SCSException
	{
		// 12 : 弹性块存储
		//根据管理员修改信息更新模板信息
		if(vminfo != null && template != null){
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);//特殊模板 1：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			if(vminfo.getZoneId() > 0){
				template.setZoneId(vminfo.getZoneId());
			}
			template.setStorageSize(vminfo.getStorageSize());
			template.setOperType(2);
			//修改raid级别
			String raid = vminfo.getExtendAttrJson();
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("raid", raid);
			String	parameter = JsonUtil.getJsonString4JavaPOJO(map);
			template.setExtendAttrJSON(parameter);

		}
		if(vminfo != null && template != null && instanceInfoNew != null){
			// 资源实例模板更新信息
			instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 存储空间
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			//JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("period", vminfo.getPeriod());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByNAS(TTemplateVMBO template, TInstanceInfoBO instanceInfoNew, TVmInfo vminfo, TUserBO user)
	throws SCSException
	{
		// 13 : NAS文件系统
		// 根据管理员修改信息更新模板信息
		if (vminfo != null && template != null) {
			template.setType(vminfo.getTemplateType());
			template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);// 特殊模板
			// 1：用户定义的特殊模板
			template.setResourcePoolsId(vminfo.getPoolId());
			if (vminfo.getZoneId() > 0) {
				template.setZoneId(vminfo.getZoneId());
			}
			template.setStorageSize(vminfo.getStorageSize());
			template.setOperType(2);
		}
		if (vminfo != null && template != null && instanceInfoNew != null) {
			// 资源实例模板更新信息
			instanceInfoNew.setZoneId(Long.valueOf(vminfo.getZoneId()));// 资源域
			instanceInfoNew.setStorageSize(vminfo.getStorageSize());// 存储空间
			instanceInfoNew.setInstanceName(vminfo.getInstanceName());// 实例名称
			instanceInfoNew.setComment(vminfo.getDescription());// 实例描述
			// JOB参数
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("period", vminfo.getPeriod());
			String parameter = "";
			parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoNew.setResourceInfo(parameter);
		}
	}

	private void getOrderUpdateInfoByDataBackup(TTemplateVMBO templateNew , TTemplateVMBO templateOld , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 15 : 云数据备份
		// TODO 增加修改逻辑
	}

	private void getOrderUpdateInfoByVMS(TTemplateVMBO templateNew , TTemplateVMBO templateOld , TVmInfo vminfo,TUserBO user)
	throws SCSException
	{
		// 50 : 多虚机
		//TODO 增加修改逻辑
	}

	public IProductInstanceRefService getProductInstanceRefService() {
		return productInstanceRefService;
	}

	public void setProductInstanceRefService(IProductInstanceRefService productInstanceRefService) {
		this.productInstanceRefService = productInstanceRefService;
	}

	public IOrderHistoryDao getOrderHistoryDao() {
		return orderHistoryDao;
	}

	public void setOrderHistoryDao(IOrderHistoryDao orderHistoryDao) {
		this.orderHistoryDao = orderHistoryDao;
	}

	public IServiceInstanceService getServiceInstanceService() {
		return serviceInstanceService;
	}

	public void setServiceInstanceService(IServiceInstanceService serviceInstanceService) {
		this.serviceInstanceService = serviceInstanceService;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public IVMTemplateService getVMTemplateService() {
		return VMTemplateService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public PhysicalMachinesService getPhysicalMachineService() {
		return physicalMachineService;
	}

	public void setPhysicalMachineService(
			PhysicalMachinesService physicalMachineService) {
		this.physicalMachineService = physicalMachineService;
	}

	public IMCTemplateService getMCTemplateService() {
		return MCTemplateService;
	}

	public void setMCTemplateService(IMCTemplateService mCTemplateService) {
		MCTemplateService = mCTemplateService;
	}

}
