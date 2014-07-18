package com.skycloud.management.portal.front.instance.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.audit.dao.IAuditDao;
import com.skycloud.management.portal.admin.audit.entity.TAuditBO;
import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.DestroyVirtualMachine;
import com.skycloud.management.portal.front.command.res.DisableAccount;
import com.skycloud.management.portal.front.command.res.DisableUser;
import com.skycloud.management.portal.front.command.res.ListAccounts;
import com.skycloud.management.portal.front.command.res.ListDomains;
import com.skycloud.management.portal.front.command.res.ListUsers;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.instance.service.IJobInstanceInfoService;
import com.skycloud.management.portal.front.order.dao.IJobInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.action.vo.VMResourcesVO;
import com.skycloud.management.portal.front.resources.service.BackUpInstanceOperateService;
import com.skycloud.management.portal.front.resources.service.BandWidthInstanceOperateService;
import com.skycloud.management.portal.front.resources.service.IPublicIPInstanceService;
import com.skycloud.management.portal.front.resources.service.IVolumeOperateService;
import com.skycloud.management.portal.front.resources.service.VirtualMachineModifyService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.front.sg.entity.SGRule;
import com.skycloud.management.portal.front.sg.service.ISGRuleService;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.InstanceInfoStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.enumtype.OrderState;
import com.skycloud.management.portal.task.vdc.enumtype.OrderType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

public class JobInstanceInfoServiceImpl implements IJobInstanceInfoService {

	protected final Logger logger = Logger.getLogger(JobInstanceInfoServiceImpl.class);

	private IJobInstanceInfoDao jobInstanceDao;

	private IAsyncJobInfoDAO asyncJobDao;

	private VirtualMachineModifyService vmModifyService;

	private IPublicIPInstanceService publicIPService;

	private AsyncJobVDCService asyncJobVDCService;

	private BandWidthInstanceOperateService bandWidthService;

	private ISGRuleService SGRuleService;

	private IInstanceService instanceService;

	private BackUpInstanceOperateService backUpInstanceOperateService;

	private IVolumeOperateService volumeOperateService;

	private ICommandService commandService;

	private IServiceInstanceService serviceInstanceService;

	private IAuditDao auditDao;

	private IOrderDao orderDao;

	private IResourcePoolsDao resourcePoolsDao;

	// 4：已回收
	private final static int STATE_RELEASE = 4;

	private final static String USERID = "userid";

	private final static String ACCOUNTID = "accountid";

	private final static String DOMAINID = "domainid";

	public IAsyncJobInfoDAO getAsyncJobDao() {
		return asyncJobDao;
	}

	public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
		this.asyncJobDao = asyncJobDao;
	}

	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

	public IVolumeOperateService getVolumeOperateService() {
		return volumeOperateService;
	}

	public void setVolumeOperateService(IVolumeOperateService volumeOperateService) {
		this.volumeOperateService = volumeOperateService;
	}

	public IInstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	public BackUpInstanceOperateService getBackUpInstanceOperateService() {
		return backUpInstanceOperateService;
	}

	public void setBackUpInstanceOperateService(BackUpInstanceOperateService backUpInstanceOperateService) {
		this.backUpInstanceOperateService = backUpInstanceOperateService;
	}

	public ISGRuleService getSGRuleService() {
		return SGRuleService;
	}

	public void setSGRuleService(ISGRuleService sGRuleService) {
		SGRuleService = sGRuleService;
	}

	public IPublicIPInstanceService getPublicIPService() {
		return publicIPService;
	}

	public void setPublicIPService(IPublicIPInstanceService publicIPService) {
		this.publicIPService = publicIPService;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	public BandWidthInstanceOperateService getBandWidthService() {
		return bandWidthService;
	}

	public void setBandWidthService(BandWidthInstanceOperateService bandWidthService) {
		this.bandWidthService = bandWidthService;
	}

	public VirtualMachineModifyService getVmModifyService() {
		return vmModifyService;
	}

	public void setVmModifyService(VirtualMachineModifyService vmModifyService) {
		this.vmModifyService = vmModifyService;
	}

	public IJobInstanceInfoDao getJobInstanceDao() {
		return jobInstanceDao;
	}

	public void setJobInstanceDao(IJobInstanceInfoDao jobInstanceDao) {
		this.jobInstanceDao = jobInstanceDao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	@Override
	public List<ResourcesVO> queryInstanceByUser(TUserBO user) throws SCSException {
		return this.getJobInstanceDao().queryInstanceByUser(user);
	}

	@Override
	public void updateInstanceByUser(TUserBO user) {
		List<ResourcesVO> resourceVoList;
		try {
			// 用户挂起时仅对虚拟机作停机命令
			resourceVoList = this.getJobInstanceDao().queryVMForUpdateByUser(user);
			if (null != resourceVoList && !resourceVoList.isEmpty()) {
				for (ResourcesVO res : resourceVoList) {
					this.insertVMStop(res, user);
				}
			}
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void insertVMStop(ResourcesVO vo, TUserBO user) throws SCSException {
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.seteInstanceId(vo.getId());
		vmModifyVO.setId(vo.getId());
		vmModifyVO.setRes_code(vo.getRes_code());
		vmModifyService.insertVMStop(vmModifyVO);
	}
	@Override
	//fix bug 4561
	public void disabledVM(TInstanceInfoBO infoBO) throws SCSException{
		ResourcesVO resourceVO = new ResourcesVO();
		resourceVO.setE_instance_id(""+infoBO.geteInstanceId());
		resourceVO.setId(infoBO.getId());
		resourceVO.setRes_code(infoBO.getResCode());
		this.insertVMStop(resourceVO,null);
	}
	@Override
	public void disabledStorage(TInstanceInfoBO infoBO){
		Iri iri = new Iri();
		iri.setE_DISK_INSTANCE_INFO_ID((int)infoBO.geteInstanceId());
		iri.setDISK_INSTANCE_INFO_ID(infoBO.getId());
		iri.setCREATE_DT(new Timestamp(System.currentTimeMillis()));
		iri.setSTATE(5);
		logger.info("eid:"+infoBO.geteInstanceId()+",id:"+infoBO.getId());
		TInstanceInfoBO infobo;
		try {
			infobo = instanceService.queryInstanceInfoById((int)infoBO.geteInstanceId());
			System.out.println("infobo:"+infobo.getInstanceName()+":"+infobo.getId());
			volumeOperateService.insertDetachVolumeOperateAuto(iri, infobo);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}


	//过期,禁止使用
	@Override
	public void disabledBackup(TInstanceInfoBO infoBO){
		logger.info("eid:"+infoBO.geteInstanceId()+",id:"+infoBO.getId());
	}

	@Override
	public void deleteInstanceByDestoryUser() {
		try {
			List<TUserBO> userList = jobInstanceDao.queryUserByState(CompanyCheckStateEnum.FAILURE);
			logger.debug("---->注销的用户数目：" + userList.size());
			//1.3功能，支持多资源池，查询出所有的资源池
			List<TResourcePoolsBO> poollist = null;
			try {
				poollist = resourcePoolsDao.searchAllPools();
			}
			catch (SQLException e) {
				e.printStackTrace();
			}			
			if (null != userList && !userList.isEmpty()) {
				for (TUserBO user : userList) {
					logger.debug("---->正在处理的注销用户ID：" + user.getId());
					this.deleteInstanceByUser(user);
					//循环所有的资源池
					if(null != poollist && poollist.size()>0){
						for (TResourcePoolsBO pool : poollist) {
							this.disabledEAccount(user,pool.getId());
						}					
						
					}					
				}
			}
		}
		catch (SCSException e) {
			e.printStackTrace();
		}
	}

	// 取消订单操作
	@Override
	public void deleteInstanceByOrder(TOrderBO order, TUserBO user) {
		// update order state to invalid and update instance state to invalid
		// when the order not be processed
		if (order.getState() == OrderState.APPLY.getValue() || order.getState() == OrderState.LEVEL1.getValue()
		        || order.getState() == OrderState.LEVEL2.getValue()) {
			// update the order state to invalid
			updateOrderState(order);
			// update the instance state to delete when applying
			try {
				if (order.getType() == OrderType.APPLY.getValue()) {
					batchUpdateInstanceState(order, InstanceInfoStateVDC.DELETE);
					serviceInstanceService.updateServiceStateByOrder(InstanceInfoStateVDC.DELETE.getValue(), order);
				} else if (order.getType() == OrderType.DELETE.getValue()) {// 取消退订
					// batchUpdateInstanceState(order,
					// InstanceInfoStateVDC.AVAILABLE);
					// serviceInstanceService.updateServiceStateByOrder(InstanceInfoStateVDC.AVAILABLE.getValue(),
					// order);
					// 资源实例状态回滚
					batchRollbackInstanceState(order);
					// 服务实例状态回滚
					// to fix bug:3704
					serviceInstanceService.rollbackServiceStateByServiceId(order.getServiceInstanceId());
				} else {// //取消修改
					// batchUpdateInstanceState(order,
					// InstanceInfoStateVDC.AVAILABLE);
					// 资源实例状态回滚,服务不变
					batchRollbackInstanceState(order);
					//取消订单实例修改
					//to fix bug:6995
					serviceInstanceService.cancelUpdateServiceInstanceByOrderId(order.getOrderId());
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			return;
		}
		// 用户退订订单，对所有资源作释放
		List<ResourcesVO> resourceVoList;
		List<VMResourcesVO> vmResourceList;
		try {
			// 非虚拟机和块存储
			resourceVoList = this.getJobInstanceDao().queryInstanceByOrder(order);
			logger.debug("---->1退订注销资源数目：" + resourceVoList.size());
			if (null != resourceVoList && !resourceVoList.isEmpty()) {
				for (ResourcesVO vo : resourceVoList) {
					this.deleteInstanceSwitch(vo, user);
				}
			}
			// 虚拟机和块存储
			vmResourceList = this.getJobInstanceDao().queryVMByOrder(order);
			logger.debug("---->2退订注销资源数目：" + vmResourceList.size());
			if (null != vmResourceList && !vmResourceList.isEmpty()) {
				for (VMResourcesVO vo : vmResourceList) {
					this.deleteVMSwitch(vo, user);
				}
			}
			// update the order state to invalid
			updateOrderState(order);
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void batchUpdateInstanceState(TOrderBO order, InstanceInfoStateVDC state) {
		try {
			List<TInstanceInfoBO> instances = orderDao.findInstanceByOrderId(order.getOrderId(), order.getType());
			if (instances != null && !instances.isEmpty()) {
				for (TInstanceInfoBO instance : instances) {
					instanceService.updateTScsIntanceInfoStateById(instance.getId(), state.getValue());
					// to fix bug:0002802
					// 负载均衡和防火墙退订时候一起取消
					if (order.getType() == 3 && (instance.getTemplateType() == 6 || instance.getTemplateType() == 7)) {
						int templateType = 6;
						if (instance.getTemplateType() == 6) {
							templateType = 7;
						}
						this.cancelLoadBalanceAndFireWall(order, templateType);
					}
				}
			}
		}
		catch (ServiceException e) {
			logger.error("Batch update instance error: " + e);
		}
		catch (SQLException e) {
			logger.error("Batch update instance error: " + e);
		}
	}

	private void batchRollbackInstanceState(TOrderBO order) {
		try {
			List<TInstanceInfoBO> instances = orderDao.findInstanceByOrderId(order.getOrderId(), order.getType());
			if (instances != null && !instances.isEmpty()) {
				for (TInstanceInfoBO instance : instances) {
					instanceService.rollbackIntanceInfoStateById(instance.getId());
					// to fix bug:0002802
					// 负载均衡和防火墙退订时候一起取消
					if (order.getType() == 3 && (instance.getTemplateType() == 6 || instance.getTemplateType() == 7)) {
						int templateType = 6;
						if (instance.getTemplateType() == 6) {
							templateType = 7;
						}
						this.cancelLoadBalanceAndFireWall(order, templateType);
					}
				}
			}
		}
		catch (ServiceException e) {
			logger.error("Batch update instance error: " + e);
		}
		catch (SQLException e) {
			logger.error("Batch update instance error: " + e);
		}
	}

	// 负载均衡和防火墙退订时候一起取消
	private void cancelLoadBalanceAndFireWall(TOrderBO order, int templateType) {
		try {
			List<TAuditBO> orderList = auditDao.queryWaitApproveOrderByInstanceInfo(order.getOrderId(), templateType);
			if (orderList != null && orderList.size() > 0) {
				TOrderBO myorder = orderDao.searchOrderById(orderList.get(0).getOrderId());
				myorder.setState(OrderState.INVALID.getValue());
				// 订单作废
				this.updateOrderState(myorder);
				// 服务恢复可用
				serviceInstanceService.updateServiceStateByOrder(InstanceInfoStateVDC.AVAILABLE.getValue(), myorder);
				// 实例恢复为可用
				instanceService.updateTScsIntanceInfoStateById(orderList.get(0).getInstanceInfoId(), InstanceInfoStateVDC.AVAILABLE.getValue());
			}

		}
		catch (Exception e) {
			logger.error("Update order error: " + e);
		}
	}

	private void updateOrderState(TOrderBO order) {
		try {
			order.setState(OrderState.INVALID.getValue());
			orderDao.update(order);

		}
		catch (SQLException e) {
			logger.error("Update order error: " + e);
		}
	}

	@Override
	public void deleteInstanceByUser(TUserBO user) {
		// 用户注销，对所有资源作释放
		List<ResourcesVO> resourceVoList;
		List<VMResourcesVO> vmResourceList;
		try {
			// 非虚拟机和块存储
			resourceVoList = this.getJobInstanceDao().queryInstanceByUser(user);
			logger.debug("---->1正在注销资源数目：" + resourceVoList.size());
			if (null != resourceVoList && !resourceVoList.isEmpty()) {
				for (ResourcesVO vo : resourceVoList) {
					this.deleteInstanceSwitch(vo, user);
				}
			}
			// 虚拟机和块存储
			vmResourceList = this.getJobInstanceDao().queryVMByUser(user);
			logger.debug("---->2正在注销资源数目：" + vmResourceList.size());
			if (null != vmResourceList && !vmResourceList.isEmpty()) {
				for (VMResourcesVO vo : vmResourceList) {
					this.deleteVMSwitch(vo, user);
				}
			}
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void deleteInstanceSwitch(ResourcesVO vo, TUserBO user) throws Exception {
		logger.debug("---->正在注销资源ID：" + vo.getId());
		int type = Integer.parseInt(vo.getTemplate_type());
		// 1： 虚拟机和块存储；2：小型机；3：X86物理机(没有用)； //4:备份服务；5：监控服务；6:负载均衡服务；
		// 7:防火墙资源服务；8:带宽资源服务；9:公网IP资源服务；10:物理机: 15: 数据云备份',
		switch (type) {
			case 2:
				break;
			case 3:
				break;
			case 4:// 虚拟机备份
				this.deleteBakUpInstance(vo, user);
				break;
			case 5:// 监控服务
				this.deleMonitorInstance(vo, user);
				break;
			case 6:// 负载均衡服务
				this.deleLoadBalanceInstance(vo, user);
				break;
			case 7:// 防火墙资源服务
				this.deleteFwInstance(vo);
				break;
			case 8:// 带宽
				this.deleteBandWidthInstance(vo, user);
				break;
			case 9:// 公网IP
				publicIPService.writeBackForReleasePublicIP(vo.getId(), STATE_RELEASE);
				break;
			case 10:// 物理机
				break;
			case 15:// 数据云备份
				this.deleteDataBak(vo, user);
				break;
			default:
				break;
		}
	}

	private void deleLoadBalanceInstance(ResourcesVO vo, TUserBO user) {
		instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
	}

	private void deleMonitorInstance(ResourcesVO vo, TUserBO user) {
		instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
	}

	private void deleteBakUpInstance(ResourcesVO vo, TUserBO user) throws Exception {
		if (ConstDef.getCurProjectId() == 2) {
			instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
		} else if (ConstDef.getCurProjectId() == 1) {
			// 删除快照
			jobInstanceDao.deleteUserSnapshot(user.getId());
			ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
			vmModifyVO.setId(vo.getId());
			vmModifyVO.setApply_reason("用户注销");
			// backUpInstanceOperateService.insertDirtyReaddeleteBackUpInstance(vmModifyVO,user);
			instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
		}
	}

	private void deleteDataBak(ResourcesVO vo, TUserBO user) throws SCSException {
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
		TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
		if (null != info) {
			if (ConstDef.getCurProjectId() == 2) {
				AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
				asynJobVDCPO.setParameter(vo.getRes_code());
				asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
				asynJobVDCPO.setUser_id(user.getId());
				asynJobVDCPO.setTemplate_id(info.getTemplateId());
				asynJobVDCPO.setOperation(OperationType.DELETE_DATABAK);
				asynJobVDCPO.setInstance_info_id(info.getId());
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
				// 用户日志记录
				String para = "instanceId=" + vo.getId();
				ConstDef.saveLogInfo(ConstDef.USERLOG_DEL, "产品管理-数据云备份job", "回收数据云备份", this.getClass().getName(), "deleteDataBak()", para, "", "");
				// 用户日志结束
			}
		}
	}

	private void deleteFwInstance(ResourcesVO vo) throws Exception {
		// SGRule carrier = new SGRule();
		// carrier.setAccess(Constants.STATUS_COMMONS.IGNORE.getValue());
		// carrier.setDestinationPort(Constants.STATUS_COMMONS.IGNORE.getValue());
		// carrier.setOperate(Constants.STATUS_COMMONS.IGNORE.getValue());
		// carrier.setSourcePort(Constants.STATUS_COMMONS.IGNORE.getValue());
		// carrier.setStatus(Constants.STATUS_COMMONS.IGNORE.getValue());
		// carrier.setSgId(ISGRuleService.SGID_PREFIX + String.format("%08d",
		// vo.getId()));
		instanceService.updateTScsIntanceInfoStateById(vo.getId(), STATE_RELEASE);
		List<SGRule> ruleList = SGRuleService.getRuleListByInstanceId(ISGRuleService.SGID_PREFIX + String.format("%08d", vo.getId()));
		if (null != ruleList && !ruleList.isEmpty()) {
			for (SGRule rule : ruleList) {
				SGRuleService.deleteRuleForDestory(rule.getId());
			}
		}
	}

	private String deleteBandWidthInstance(ResourcesVO vo, TUserBO user) throws Exception {
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
		vmModifyVO.setUserID(user.getId());
		return bandWidthService.deleteBandWidthInstance(vmModifyVO);
	}

	private void deleteVMSwitch(VMResourcesVO vo, TUserBO user) throws Exception {
		logger.debug("---->正在注销资源ID：" + vo.getId());
		int type = vo.getInstance_type();
		switch (type) {
			case 1:// 虚拟机
				this.insertVMAapplyDestroy(vo, user);
				break;
			case 2:// 块存储
				this.insertVolumeDestroy(vo, user);
				break;
			default:
				break;
		}
	}

	private String insertVMAapplyDestroy(VMResourcesVO vo, TUserBO user) throws Exception {
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(vo.getId());
		TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
		int index = 0;
		String parameters = null;
		if (info != null) {
			if (ConstDef.getCurProjectId() == 1) {
				// 查询是否有公网IP绑定
				TInstanceInfoBO ip = publicIPService.searchInstanceInfoByID(user.getId(), ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP, vo.getId());
				if (null != ip) {
					String resourceInfo = ip.getResourceInfo(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
					JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
					String ipAddress = ""; // IP地址
					if (jsonObject.containsKey("ipAddress")) {
						ipAddress = jsonObject.getString("ipAddress");
					}
					// 生成解绑公网IP指令，再生成虚拟机销毁指令
					publicIPService.insertAsyncjobVdcForBind(user.getId(), ip.getId(), ipAddress, "" + vo.getId(), 1);
				}
				parameters = "" + info.geteInstanceId();
				this.destroyVirturlMachines(info);
			} else if (ConstDef.getCurProjectId() == 2) {
				// deleteVM
				// DeleteVM vmDelete = new DeleteVM();
				// vmDelete.setVMID(vo.getRes_code());
				// parameter = CommandCreateUtil.getJsonParameterStr(vmDelete);

				Map<String, Object> mapJob = new HashMap<String, Object>();
				mapJob.put("VMID", vo.getRes_code());
				parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
				// insert async job for vdc
				AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
				asynJobVDCPO.setUser_id(user.getId());
				asynJobVDCPO.setInstance_info_id(Integer.parseInt(vo.geteInstanceId()));
				asynJobVDCPO.setOperation(OperationType.DELETE_VM);
				asynJobVDCPO.setTemplate_id(info.getTemplateId());
				asynJobVDCPO.setParameter(parameters);
				asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
				index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
			}
			// 用户日志记录
			String para = "instanceId=" + vo.getId();
			String memo = parameters;
			ConstDef.saveLogInfo(ConstDef.USERLOG_DEL, "产品管理-虚拟机job", "回收虚拟机", this.getClass().getName(), "insertVMAapplyDestroy()", para, "用户注销",
			                     memo);
			// 用户日志结束
		}
		return ResourcesUtil.resultTOString(index);
	}

	private void insertVolumeDestroy(VMResourcesVO vo, TUserBO user) throws Exception {
		try {
			ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
			vmModifyVO.setId(vo.getId());
			TInstanceInfoBO info = vmModifyService.updateInstanceInfoState(vmModifyVO);
			if (null != info) {
				if (ConstDef.getCurProjectId() == 1) {
					// 查询是否有绑定VM
					Iri iri = this.getJobInstanceDao().queryIriByDiskId(vo.getId());
					if (null != iri) {
						iri.setE_DISK_INSTANCE_INFO_ID(Integer.parseInt(vo.getE_disk_id()));
						iri.setDISK_INSTANCE_INFO_ID(vo.getId());
						iri.setCREATE_DT(new Timestamp(System.currentTimeMillis()));
						iri.setSTATE(5);
						iri.setCREATE_USER_ID(user.getId());
						// 生成解绑定VM指令，再生成块存储的销毁指令
						volumeOperateService.insertDetachVolumeOperate(iri, info);
					}
					this.insertDestroyVolume(info, user);
				} else if (ConstDef.getCurProjectId() == 2) {
					// JOB信息封装开始
					AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
					String parameters = "";// 操作参数，调用api所需要的参数
					Map<String, Object> mapJob = new HashMap<String, Object>();
					mapJob.put("BSID", vo.getRes_code());
					parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
					asynJobVDCPO.setParameter(parameters);
					asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
					asynJobVDCPO.setUser_id(user.getId());
					asynJobVDCPO.setTemplate_id(info.getTemplateId());
					asynJobVDCPO.setOperation(OperationType.DELETE_EBS);
					asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
					// JOB信息封装结束

					// 用户日志记录
					String para = "templateid=" + info.getTemplateId() + ",res_code=" + info.getResourceInfo();
					String memo = parameters;
					ConstDef.saveLogInfo(1, "产品管理-块存储job", "退定job", "VolumeOperateServiceImpl.java", "insertDestroyVolume()", para, "用户注销", memo);
					// 用户日志结束
				}// vdc项目判断结束

			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}

	@Override
	public void disabledEAccount(TUserBO user,int resourcePoolsId) {
		List<TResourcePoolsBO> poollist = null;
		try {
			poollist = resourcePoolsDao.searchAllPools();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		Map<String, String> userMap = this.findEAccount(user,resourcePoolsId);
		if (null != userMap && !userMap.isEmpty()) {
			// 注销account
			if (userMap.containsKey(ACCOUNTID)) {
				DisableAccount cmd = new DisableAccount();
				cmd.setAccount(user.getAccount());
				cmd.setDomainid(userMap.get(DOMAINID));
				cmd.setLock("false");
				for (TResourcePoolsBO pool : poollist) {
					JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, pool.getId()));
				}
				// jo =
				// JSONObject.fromObject(jo.getString("createaccountresponse"));
				// logger.debug("createAccount:"+jo);
			}
			// 注销user
			if (userMap.containsKey(USERID)) {
				DisableUser cmd = new DisableUser();
				cmd.setId(userMap.get(USERID));
				for (TResourcePoolsBO pool : poollist) {
					JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, pool.getId()));
				}
			}
		}
	}

	@Override
	public Map<String, String> findEUser(TUserBO user) {
		List<TResourcePoolsBO> poollist = null;
		try {
			poollist = resourcePoolsDao.searchAllPools();
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		ListUsers cmd = new ListUsers();
		cmd.setUsername(user.getAccount());
		if (3 == user.getRoleApproveLevel() || 4 == user.getRoleApproveLevel()) {
			cmd.setAccounttype("1");
		} else {
			cmd.setAccounttype("2");
		}
		Map eUserMap = new HashMap<String, String>();
		try {
			for (TResourcePoolsBO pool : poollist) {
				JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, pool.getId()));
				if (jo.containsKey("listusersresponse")) {
					jo = jo.getJSONObject("listusersresponse");
					JSONArray array = jo.getJSONArray("user");
					if (null != array && array.size() > 0) {
						JSONObject jUser = JSONObject.fromObject(array.get(0));
						eUserMap.put(USERID, jUser.getString("id"));
						eUserMap.put(DOMAINID, jUser.getString("domainid"));
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return eUserMap;
	}

	@Override
	public Map<String, String> findEAccount(TUserBO user,int resourcePoolsId) {
		ListAccounts cmd = new ListAccounts();
		cmd.setName(user.getAccount());
		if (3 == user.getRoleApproveLevel() || 4 == user.getRoleApproveLevel()) {
			cmd.setAccounttype("1");
		} else {
			cmd.setAccounttype("2");
		}
		Map eUserMap = new HashMap<String, String>();
		try {
			
				JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, resourcePoolsId));
				if (jo.containsKey("listaccountsresponse")) {
					jo = jo.getJSONObject("listaccountsresponse");
					if (jo.containsKey("account")) {
						JSONArray array = jo.getJSONArray("account");
						if (null != array && array.size() > 0) {
							for (int i = 0; i < array.size(); i++) {
								JSONObject jaccount = JSONObject.fromObject(array.get(i));
								if(jaccount.getString("name").equals(user.getAccount())){
//									JSONObject jAccount = JSONObject.fromObject(array.get(0));
									eUserMap.put(ACCOUNTID, jaccount.getString("id"));
									eUserMap.put(DOMAINID, jaccount.getString("domainid"));
									JSONArray uarray = jaccount.getJSONArray("user");
									if (null != uarray && uarray.size() > 0) {
										JSONObject jUser = JSONObject.fromObject(uarray.get(0));
										eUserMap.put(USERID, jUser.getString("id"));
									}
									
								}
							}
						}
					}
				}
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return eUserMap;
	}

	/**
	 * 根据用户注册名称查询网域是否已存在
	 */
	@Override
	public Map<String, String> findEDomain(TUserBO user,int resourcePoolsId) {
//		List<TResourcePoolsBO> poollist = null;
//		try {
//			poollist = resourcePoolsDao.searchAllPools();
//		}
//		catch (SQLException e) {
//			e.printStackTrace();
//		}
		ListDomains cmd = new ListDomains();
		cmd.setDomainName(user.getAccount());

		Map eUserMap = new HashMap<String, String>();

//		for (TResourcePoolsBO pool : poollist) {
			JSONObject jo = JSONObject.fromObject(commandService.executeAndJsonReturn(cmd, resourcePoolsId));
			if (jo.containsKey("listdomainsresponse")) {
				jo = jo.getJSONObject("listdomainsresponse");
				if (jo.containsKey("domain")) {
					JSONArray array = jo.getJSONArray("domain");
					if (null != array && array.size() > 0) {
						for(int i=0;i<array.size();i++){
							JSONObject jDomain = JSONObject.fromObject(array.get(i));
							//fix bug 4852
							if(user.getAccount().equals(jDomain.getString("name"))){
								eUserMap.put(DOMAINID, jDomain.getString("id"));
								eUserMap.put("name", jDomain.getString("name"));
								eUserMap.put("level", jDomain.getString("level"));
								break;
							}							
						}						
					}
				}
			}
//		}
		return eUserMap;
	}

	/**
	 * 1.1平台无需审核直接删除块存储
	 */
	public void insertDestroyVolume(TInstanceInfoBO info, TUserBO user) throws Exception {
		try {
			String command = "deleteVolume";
			AsyncJobInfo ajInfo = new AsyncJobInfo();
			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
			Map<String, Object> subMap = new HashMap<String, Object>();
			subMap.put("id", info.geteInstanceId());
			map.put(command, subMap);
			String parameter = JsonUtil.getJsonString4JavaPOJO(map);
			// 1操作0创建销毁
			ajInfo.setAPPLY_ID(0);
			ajInfo.setINSTANCE_INFO_ID(info.getId());
			ajInfo.setOPERATION(command);
			ajInfo.setORDER_ID(0);
			ajInfo.setPARAMETER(parameter);
			// 1未完成;2已完成
			ajInfo.setJOBSTATE(1);
			asyncJobDao.insertAsyncJob(ajInfo);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}

	/**
	 * 生成销毁虚拟机任务,无须审核.
	 */
	public int destroyVirturlMachines(TInstanceInfoBO instanceInfoBO) {
		int ret = 0;
		try {
			AsyncJobInfo ajInfo = new AsyncJobInfo();
			DestroyVirtualMachine dvm = new DestroyVirtualMachine();
			dvm.setId((int) instanceInfoBO.geteInstanceId());
			String parameter = CommandCreateUtil.getJsonParameterStr(dvm);
			String operation = dvm.getCOMMAND();
			ajInfo.setINSTANCE_INFO_ID(instanceInfoBO.getId());
			ajInfo.setOPERATION(operation);
			ajInfo.setPARAMETER(parameter);
			ajInfo.setJOBSTATE(1);
			ajInfo.setAPPLY_ID(0);
			ret = asyncJobDao.insertAsyncJob(ajInfo);
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ret;
	}

	@Override
	public List<ResourcesVO> queryAllInstanceByUser(TUserBO user) throws SCSException {
		return jobInstanceDao.queryAllInstanceByUser(user);
	}

	public IServiceInstanceService getServiceInstanceService() {
		return serviceInstanceService;
	}

	public void setServiceInstanceService(IServiceInstanceService serviceInstanceService) {
		this.serviceInstanceService = serviceInstanceService;
	}

	public IAuditDao getAuditDao() {
		return auditDao;
	}

	public void setAuditDao(IAuditDao auditDao) {
		this.auditDao = auditDao;
	}

	public IResourcePoolsDao getResourcePoolsDao() {
		return resourcePoolsDao;
	}

	public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
		this.resourcePoolsDao = resourcePoolsDao;
	}

}
