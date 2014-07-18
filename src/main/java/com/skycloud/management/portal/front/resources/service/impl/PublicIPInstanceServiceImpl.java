package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.dao.IPublicIPDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.PublicIPInstanceDao;
import com.skycloud.management.portal.front.resources.service.IPublicIPInstanceService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

/**
 * @declare
 * @file_name PublicIPServiceImpl.java
 * @author gankb E-mail: gankb@chinaskycloud.com
 * @version 2012-3-7 下午02:24:31
 */
public class PublicIPInstanceServiceImpl implements IPublicIPInstanceService {

	private static Log log = LogFactory.getLog(PublicIPInstanceServiceImpl.class);

	private IPublicIPDao publicIPDao;

	private PublicIPInstanceDao publicIPInstanceDao;

	private IInstanceInfoDao instanceInfoDao;

	private IAuditSevice auditService;

	private IOrderDao orderDao;

	private IVMTemplateDao VMTemplateDao;

	private IInstanceService instanceService;

	private AsyncJobVDCService asyncJobVDCService;

	public IPublicIPDao getPublicIPDao() {
		return publicIPDao;
	}

	public void setPublicIPDao(IPublicIPDao publicIPDao) {
		this.publicIPDao = publicIPDao;
	}

	public PublicIPInstanceDao getPublicIPInstanceDao() {
		return publicIPInstanceDao;
	}

	public void setPublicIPInstanceDao(PublicIPInstanceDao publicIPInstanceDao) {
		this.publicIPInstanceDao = publicIPInstanceDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IAuditSevice getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public IVMTemplateDao getVMTemplateDao() {
		return VMTemplateDao;
	}

	public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
		VMTemplateDao = vMTemplateDao;
	}

	public IInstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

	@Override
	public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider) {
		return publicIPDao.listPublicIPByServiceProvider(serviceProvider);
	}

	@Override
	public int queryPublicIPInstanceListCount(ResourcesQueryVO rqvo) throws SCSException {
		try {
			return publicIPInstanceDao.queryResouceServiceInstanceInfoCount(rqvo);
		}
		catch (SCSException e) {
			e.printStackTrace();
			log.error("queryPublicIPInstanceListCount error:" + e.getMessage());
		}
		return -1;
	}

	@Override
	public ResourcesVO queryBindVMByIp(int ipid) throws SCSException {
		return publicIPInstanceDao.queryBindVMByIp(ipid);
	}

	@Override
	public List<ResourcesVO> queryPublicIPInstanceList(ResourcesQueryVO rqvo) throws SCSException {
		try {
			return publicIPInstanceDao.queryResouceServiceInstanceInfo(rqvo);
		}
		catch (SCSException e) {
			e.printStackTrace();
			log.error("queryPublicIPInstanceList error:" + e.getMessage());
		}
		return null;
	}

	@Override
	public String insertDirtyReaddeletePublicIpInstance(ResourcesModifyVO vmModifyVO, TUserBO user, String IP) throws Exception {
		TInstanceInfoBO info = this.updateInstanceInfoState(vmModifyVO);
		int index = 0;
		int saveOrderId = 0;
		/**
		 * 生成退订订单
		 */
		if (info != null) {
			index = this.approveOrder(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE); // 返回插入的订单id
			// saveOrderId = this.approveOrder(user, info, index, vmModifyVO,
			// ConstDef.ORDER_DEL_TYPE); //fix bug 2503 update by CQ
			saveOrderId = index;
		}
		/**
		 * 向T_SCS_ASYNCJOB_VDC表里写入退订信息
		 */
		// int project_switch = ConstDef.curProjectId;
		int project_switch = ConstDef.getCurProjectId();
		if (project_switch == 1) {
			// elaster 没有带宽的相关接口
		} else if (project_switch == 2) {
			index = this.getAsyncJobString(index, vmModifyVO.getId(), OperationType.DELETE_WANIP, user.getId(), IP, saveOrderId);
		}
		return ResourcesUtil.resultTOString(index);
	}

	public TServiceInstanceBO updateServiceInfoState(int serviceID) throws SCSException {
		int index = 0;
		int id = serviceID;
		TServiceInstanceBO info = null;
		if (id != 0) {
			try {
				info = instanceInfoDao.findServiceInstanceById(serviceID);
			}
			catch (SQLException e) {
				e.printStackTrace();
			}
			if (info.getState() == 6) {
				return null;
			} else {
				if (info.getState() == 7) {
					index = instanceInfoDao.updateServiceState4(serviceID);
				} else {
					index = instanceInfoDao.updateServiceState(serviceID);
				}
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	}

	@Override
	public String insertIpInstance(ResourcesModifyVO vmModifyVO, TUserBO user, String IP, int serviceID) throws Exception {
		// 退订不可用资源时，直接作废 // bug 0003965
		TInstanceInfoBO info = this.updateInstanceInfoState37(vmModifyVO);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		int index = 0;
		int saveOrderId = 0;
		/**
		 * 生成退订订单
		 */
		if (info != null) {
			if (info.getState() != 4) {
				index = this.approveOrderService(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE, serviceID);
				saveOrderId = index;
			} else {
				index = 1;
			}
		}

		int project_switch = ConstDef.getCurProjectId();
		if (project_switch == 1) {

		} else if (project_switch == 2) {
			index = this.getAsyncJobString(index, vmModifyVO.getId(), OperationType.DELETE_WANIP, user.getId(), IP, saveOrderId);
		}
		return ResourcesUtil.resultTOString(index);
	}

	/**
	 * 向T_SCS_ASYNCJOB_VDC写入退订公网IP信息
	 * 
	 * @param orderId
	 *            订单id
	 * @param instanceId
	 *            实例id
	 * @param opertion
	 *            请求命令类型
	 * @param userId
	 *            用户id
	 * @param IP
	 *            需要退订的公网ip地址
	 * @return
	 * @throws SQLException
	 *             创建人： 甘坤彪 创建时间：2012-3-16 下午04:38:25
	 */
	private int getAsyncJobString(int orderId, int instanceId, OperationType opertion, int userId, String IP, int saveOrderId) throws SQLException {
		int index = 0;
		TInstanceInfoBO instanceInfoBO = instanceInfoDao.findInstanceInfoById(instanceId);
		if (instanceInfoBO != null) {
			// JOB信息封装开始
			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
			String parameters = "";// 操作参数，调用api所需要的参数
			String ResourceTemplateID = ConstDef.getVDCTemplateID(instanceInfoBO.getTemplateType(), instanceInfoBO.getTemplateId());
			Map<String, Object> mapJob = new HashMap<String, Object>();
			mapJob.put("IP", IP);
			parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
			asynJobVDCPO.setParameter(parameters);
			asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
			asynJobVDCPO.setUser_id(userId);
			asynJobVDCPO.setTemplate_id(instanceInfoBO.getTemplateId());
			asynJobVDCPO.setInstance_info_id(instanceId);
			asynJobVDCPO.setTemplate_res_id(ResourceTemplateID);
			asynJobVDCPO.setOrder_id(saveOrderId);
			asynJobVDCPO.setOperation(opertion);
			try {
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
			}
			catch (SCSException e) {
				e.printStackTrace();
				log.error("getAsyncJobString error:" + e.getMessage());
			}
			// JOB信息封装结束
			index = orderId;
		} else {
			index = 0;
		}
		return index;
	}

	/**
	 * 提交审核订单
	 * 
	 * @param user
	 * @param info
	 * @param index
	 * @param vmModifyVO
	 * @param type
	 * @return
	 * @throws SCSException
	 */
	private int approveOrder(TUserBO user, TInstanceInfoBO info, int index, ResourcesModifyVO vmModifyVO, int type) throws SCSException {
		TOrderBO order = new TOrderBO();
		order.setType(type); // 修改申请
		order.setState(1); // 申请状态
		order.setCreatorUserId(user.getId()); // 下单人ID
		order.setInstanceInfoId(vmModifyVO.getId());
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int saveOrderId = orderDao.save(order);

		if (saveOrderId > 0) {
			index = saveOrderId;
			if (user.getRoleApproveLevel() == 4) {
				try {
					auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), type,
					                          info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			} else if (user.getRoleApproveLevel() < 4) {
				try {
					auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), type,
					                               info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			}
		}
		return index;
	}

	private int approveOrderService(TUserBO user, TInstanceInfoBO info, int index, ResourcesModifyVO vmModifyVO, int type, int serviceID)
	        throws SCSException {
		TOrderBO order = new TOrderBO();
		order.setType(type); // 修改申请
		order.setState(1); // 申请状态
		order.setCreatorUserId(user.getId());
		// order.setInstanceInfoId(vmModifyVO.getId());
		order.setServiceInstanceId(serviceID);
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		order.setCreateDt(new Date());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int saveOrderId = orderDao.save(order);

		if (saveOrderId > 0) {
			index = saveOrderId;
			if (user.getRoleApproveLevel() == 4) {
				try {
					auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), type,
					                          info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			} else if (user.getRoleApproveLevel() < 4) {
				try {
					auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), type,
					                               info.getTemplateType());
				}
				catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:" + e.getMessage());
				}
			}
		}
		return index;
	}

	/**
	 * 修改
	 * 
	 * @param vmModifyVO
	 * @return
	 * @throws Exception
	 */
	private TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO) throws Exception {
		int index = 0;
		int id = vmModifyVO.getId();
		TInstanceInfoBO info = null;
		if (id != 0) {
			info = instanceInfoDao.searchInstanceInfoByID(id);
			if (info.getState() == 6) {
				return null;
			} else {
				info.setClusterId(info.getState());
				info.setState(6);
				index = instanceInfoDao.update(info);
				if (index == 0) {
					return null;
				}
			}
		}
		return info;
	}

	public TInstanceInfoBO updateInstanceInfoState37(ResourcesModifyVO vmModifyVO) throws SCSException {
		int index = 0;
		int id = vmModifyVO.getId();
		TInstanceInfoBO info = null;
		if (id != 0) {
			info = instanceInfoDao.searchInstanceInfoByID(id);
			if (info.getState() == 6) {
				return null;
			} else {
				info.setClusterId(info.getState());
				if (info.getState() == 7) {
					info.setState(4);// 退订不可用资源时，直接作废
				} else {
					info.setState(3);
				}
				index = instanceInfoDao.update(info);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	}

	@Override
	public long searchBindRsIdByIpInstanceId(TUserBO user, int ipInstanceId) throws Exception {
		ResourcesQueryVO rqvo = new ResourcesQueryVO();
		rqvo.setUser(user);
		rqvo.setId(Integer.valueOf(ipInstanceId).toString());
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP);
		ResourcesVO ipVo = null;
		List<ResourcesVO> volist = publicIPInstanceDao.queryResouceServiceInstanceInfo(rqvo);
		if (volist == null || volist.size() == 0) {// bug 0003950
			return 0;
		}
		ipVo = volist.get(0);
		String resourceInfo = ipVo.getResource_info();
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		String ipAddressId = "";
		if (jsonObject.containsKey("ipAddressId")) {
			ipAddressId = jsonObject.getString("ipAddressId");
			return publicIPInstanceDao.searchBindRsIdByipId(Integer.valueOf(ipAddressId));
		}
		return 0;
	}

	@Override
	public TInstanceInfoBO searchInstanceInfoByID(int userId, int templateType, int instanceId) throws SCSException {
		try {
			return publicIPInstanceDao.searchInstanceInfoByID(userId, templateType, instanceId);
		}
		catch (SCSException e) {
			e.printStackTrace();
			log.error("searchInstanceInfoByID error:" + e.getMessage());
		}
		return null;
	}

	@Override
	public TInstanceInfoBO searchInstanceInfo2ByID(int userId, int templateType, int instanceId) throws SCSException {
		try {
			return publicIPInstanceDao.searchInstanceInfo2ByID(userId, templateType, instanceId);
		}
		catch (SCSException e) {
			e.printStackTrace();
			log.error("searchInstanceInfo2ByID error:" + e.getMessage());
		}
		return null;
	}

	@Override
	public TInstanceInfoBO searchInstanceInfoByID(int ID) throws SCSException {
		return instanceInfoDao.searchInstanceInfoByID(ID);
	}

	@Override
	public int update(TInstanceInfoBO instanceInfo) throws SCSException {
		return instanceInfoDao.update(instanceInfo);
	}

	@Override
	public int checkPullicIpAddressExists(int userId, String ipAddress) throws SQLException {
		return instanceInfoDao.findIPAddressExist(ipAddress, ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP, userId);
	}

	@Override
	public int checkPullicIpAddressExists(String ipAddress) throws SQLException {
		return instanceInfoDao.findIPAddressExist(ipAddress, ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP);
	}

	@Override
	public TTemplateVMBO getTemplateById(final int templateId) throws Exception {
		if (templateId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return VMTemplateDao.getTemplateById(templateId);
	}

	@Override
	public List<String> getAllPublicAddress(int userId) throws Exception {
		List<ResourcesVO> resourceList = null;
		List<String> ipAddressList = new ArrayList<String>();
		ResourcesQueryVO vo = new ResourcesQueryVO();
		TUserBO user = new TUserBO();
		user.setId(userId);
		vo.setUser(user); // 用户信息
		vo.setOperateSqlType(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP); // 类型type=9代表公网IP
		resourceList = publicIPInstanceDao.queryResouceServiceInstanceInfo(vo);
		for (ResourcesVO instance : resourceList) {
			String resourceInfo = instance.getResource_info(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
			JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
			String ipAddress = "";
			if (jsonObject.containsKey("ipAddress")) {
				ipAddress = jsonObject.getString("ipAddress");
			}
			ipAddressList.add(ipAddress);
		}
		return ipAddressList;
	}

	@Override
	public int insertAsyncjobVdcForBind(int userId, int ipId, String IP, String VMID, int operationType) throws Exception {
		int index = 0;
		TInstanceInfoBO instanceInfoBO = instanceInfoDao.findInstanceInfoById(Integer.valueOf(VMID));
		if (instanceInfoBO != null) {
			// JOB信息封装开始
			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
			String parameters = "";// 操作参数，调用api所需要的参数
			String ResourceTemplateID = ConstDef.getVDCTemplateID(instanceInfoBO.getTemplateType(), instanceInfoBO.getTemplateId());
			Map<String, Object> mapJob = new HashMap<String, Object>();
			mapJob.put("IP", IP);

			TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(Integer.valueOf(VMID));
			mapJob.put("VMID", resCodeInfo.getResCode());
			parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
			asynJobVDCPO.setParameter(parameters);
			asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
			asynJobVDCPO.setUser_id(userId);
			asynJobVDCPO.setTemplate_id(instanceInfoBO.getTemplateId());
			asynJobVDCPO.setInstance_info_id(ipId);
			asynJobVDCPO.setTemplate_res_id(ResourceTemplateID);
			asynJobVDCPO.setOrder_id(instanceInfoBO.getOrderId());
			if (operationType == 0) // 绑定
			{
				asynJobVDCPO.setOperation(OperationType.BIND_WANIP);
				/**
				 * 绑定操作---先向T_SCS_INSTANCE_INFO表中写入，绑定公网ip的虚拟机信息
				 * 如果调绑定接口成功，则只是修改状态为“可用”，如果调用失败，则将绑定公网ip的虚拟机信息删掉即可
				 */
				final int vmId = Integer.parseInt(VMID);
				this.writeBindInfo(ipId, vmId); // 本地方法---写入绑定虚拟机信息
			} else if (operationType == 1) // 解绑定
			{
				asynJobVDCPO.setOperation(OperationType.UNBIND_WANIP);
				/**
				 * 修改T_SCS_INSTANCE_INFO表状态为6---命令正在执行中
				 */
				final int state = 6;
				instanceService.updateTScsIntanceInfoStateById(ipId, state);
			}
			try {
				/**
				 * 插入T_SCS_ASYNCJOB_VDC表信息
				 */
				index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
			}
			catch (SCSException e) {
				e.printStackTrace();
				log.error("insertAsyncjobVdcForBind error:" + e.getMessage());
			}
		} else {
			index = 0;
		}
		return index;
	}

	/**
	 * 绑定操作，写入T_SCS_INSTANCE_INFO表信息： 1. 先向T_SCS_INSTANCE_INFO表中写入，绑定公网ip的虚拟机信息
	 * 2. 然后再将实例状态改为“6---命令正在执行中”
	 * 调用资源池绑定公网ip接口后，如果调绑定接口成功，则只是修改状态为“可用”，如果调用失败，则将绑定公网ip的虚拟机信息删掉即可
	 * 
	 * @param instanceId
	 *            实例id，代表一条公网ip实例记录
	 * @param vmId
	 *            绑定该公网ip的虚拟机id，代表一条虚拟机实例记录
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-30 下午02:34:00
	 */
	private void writeBindInfo(final int instanceId, final int vmId) throws Exception {
		/**
		 * 1.组织数据
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		TInstanceInfoBO instanceInfoBO = this.searchInstanceInfoByID(instanceId); // 取出T_SCS_INSTANCE_INFO表一条记录
		TInstanceInfoBO VMinstanceInfoBO = this.searchInstanceInfoByID(vmId); // 取出T_SCS_INSTANCE_INFO表一条记录
		String resourceInfo = instanceInfoBO.getResourceInfo(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		String serviceProvider = ""; // 运营商编码
		String ipAddress = ""; // 公网ip地址
		String ipType = ""; // 公网ip类型，IPV4或者IPV6
		String buyCycle = ""; // 购买周期
		if (jsonObject.containsKey("serviceProvider")) {
			serviceProvider = jsonObject.getString("serviceProvider");
		}
		if (jsonObject.containsKey("ipAddress")) {
			ipAddress = jsonObject.getString("ipAddress");
		}
		if (jsonObject.containsKey("ipType")) {
			ipType = jsonObject.getString("ipType");
		}
		if (jsonObject.containsKey("period")) {
			buyCycle = jsonObject.getString("period");
		}
		map.put("serviceProvider", serviceProvider); // 运营商编码
		map.put("ipType", ipType); // 公网ip类型，IPV4或者IPV6
		map.put("ipAddress", ipAddress); // 公网ip地址
		map.put("period", buyCycle); // 购买周期
		map.put("instanceId", vmId); // 绑定的虚机id
		map.put("instanceName", VMinstanceInfoBO.getInstanceName()); // 绑定的虚拟机名称
		String parameter = JsonUtil.getJsonString4JavaPOJO(map); // 符合JSON格式的字符串
		/**
		 * 2.写回本地数据表
		 */
		final int state = 6; // 6---代表状态为“命令正在处理中”
		instanceService.updateTScsInstanceInfoStateAndResourceInfoById(instanceId, state, parameter);
	}

	@Override
	public void writeBackForApplyPublicIP(int id, int state, String ipAddress) throws Exception {
		/**
		 * 1.组织数据
		 */
		Map<String, Object> map = new HashMap<String, Object>();
		TInstanceInfoBO instanceInfoBO = this.searchInstanceInfoByID(id); // 取出T_SCS_INSTANCE_INFO表一条记录
		String resourceInfo = instanceInfoBO.getResourceInfo(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		String serviceProvider = ""; // 运营商编码
		String ipType = ""; // 公网ip类型，IPV4或者IPV6
		String buyCycle = ""; // 购买周期
		if (jsonObject.containsKey("serviceProvider")) {
			serviceProvider = jsonObject.getString("serviceProvider");
		}
		if (jsonObject.containsKey("ipType")) {
			ipType = jsonObject.getString("ipType");
		}
		if (jsonObject.containsKey("period")) {
			buyCycle = jsonObject.getString("period");
		}
		map.put("serviceProvider", serviceProvider); // 运营商编码
		map.put("ipType", ipType); // 公网ip类型，IPV4或者IPV6
		map.put("ipAddress", ipAddress); // 公网ip地址
		map.put("period", buyCycle); // 购买周期
		String parameter = JsonUtil.getJsonString4JavaPOJO(map); // 符合JSON格式的字符串
		/**
		 * 2.写回本地数据表
		 */
		instanceService.updateTScsInstanceInfoStateAndResourceInfoById(id, state, parameter);
	}

	@Override
	public void writeBackForReleasePublicIP(final int instanceId, final int state) throws Exception {
		/**
		 * state = -1，代表调用资源池接口失败，则需要将实例状态改为“2--可用状态”，方便用户下次继续进行退订操作
		 */
		if (state == -1) {
			final int state2 = 2; // 2---可用状态
			instanceService.updateTScsIntanceInfoStateById(instanceId, state2);
		}
		/**
		 * state = 4，代表调用资源池接口成功，则需要将实例状态改为“4--已回收”
		 */
		if (state == 4) {
			instanceService.updateTScsIntanceInfoStateById(instanceId, state);
		}
	}

	@Override
	// public void writeBackForBindPublicIP(int instanceId, int vmId, int state)
	// throws Exception
	public void writeBackForBindPublicIP(final int instanceId, final int state) throws Exception {
		/**
		 * state = -1，代表调用资源池接口失败，则需要删掉绑定虚拟机的信息
		 */
		if (state == -1) {
			/**
			 * 1.组织数据
			 */
			Map<String, Object> map = new HashMap<String, Object>();
			TInstanceInfoBO instanceInfoBO = this.searchInstanceInfoByID(instanceId); // 取出T_SCS_INSTANCE_INFO表一条记录
			String resourceInfo = instanceInfoBO.getResourceInfo(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
			JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
			String serviceProvider = ""; // 运营商编码
			String ipAddress = ""; // 公网ip地址
			String ipType = ""; // 公网ip类型，IPV4或者IPV6
			String buyCycle = ""; // 购买周期
			if (jsonObject.containsKey("serviceProvider")) {
				serviceProvider = jsonObject.getString("serviceProvider");
			}
			if (jsonObject.containsKey("ipAddress")) {
				ipAddress = jsonObject.getString("ipAddress");
			}
			if (jsonObject.containsKey("ipType")) {
				ipType = jsonObject.getString("ipType");
			}
			if (jsonObject.containsKey("period")) {
				buyCycle = jsonObject.getString("period");
			}
			map.put("serviceProvider", serviceProvider); // 运营商编码
			map.put("ipType", ipType); // 公网ip类型，IPV4或者IPV6
			map.put("ipAddress", ipAddress); // 公网ip地址
			map.put("period", buyCycle); // 购买周期
			String parameter = JsonUtil.getJsonString4JavaPOJO(map); // 符合JSON格式的字符串
			/**
			 * 2.写回本地数据表 此时数据表实例状态改为“2--可用状态”，方便用户下次继续进行绑定操作
			 */
			final int state2 = 2; // 2---可用状态
			instanceService.updateTScsInstanceInfoStateAndResourceInfoById(instanceId, state2, parameter);
		}
		/**
		 * state = 2，代表调用资源池接口成功，则只需要将实例状态改为“2--可用状态”
		 */
		else if (state == 2) {
			instanceService.updateTScsIntanceInfoStateById(instanceId, state);
		}
	}

	@Override
	// public void writeBackForUnBindPublicIP(int instanceId, int vmId, int
	// state) throws Exception
	public void writeBackForUnBindPublicIP(final int instanceId, final int state) throws Exception {
		/**
		 * state = -1，代表调用资源池接口失败，则只需要将实例状态改为“2--可用状态”，方便用户下次继续进行解绑定操作
		 */
		if (state == -1) {
			final int state2 = 2; // 2--可用状态
			instanceService.updateTScsIntanceInfoStateById(instanceId, state2);
		}
		/**
		 * state = 2，代表调用资源池接口成功，则需要将实例状态改为“2--可用状态”，并且需要删掉绑定虚拟机的信息
		 */
		else if (state == 2) {
			/**
			 * 1.组织数据
			 */
			Map<String, Object> map = new HashMap<String, Object>();
			TInstanceInfoBO instanceInfoBO = this.searchInstanceInfoByID(instanceId); // 取出T_SCS_INSTANCE_INFO表一条记录
			String resourceInfo = instanceInfoBO.getResourceInfo(); // 此信息包含：运营商编码，公网ip地址，绑定的虚拟机id，绑定的虚拟机名称
			JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
			String serviceProvider = ""; // 运营商编码
			String ipAddress = ""; // 公网ip地址
			String ipType = ""; // 公网ip类型，IPV4或者IPV6
			String buyCycle = ""; // 购买周期
			if (jsonObject.containsKey("serviceProvider")) {
				serviceProvider = jsonObject.getString("serviceProvider");
			}
			if (jsonObject.containsKey("ipAddress")) {
				ipAddress = jsonObject.getString("ipAddress");
			}
			if (jsonObject.containsKey("ipType")) {
				ipType = jsonObject.getString("ipType");
			}
			if (jsonObject.containsKey("period")) {
				buyCycle = jsonObject.getString("period");
			}
			map.put("serviceProvider", serviceProvider); // 运营商编码
			map.put("ipType", ipType); // 公网ip类型，IPV4或者IPV6
			map.put("ipAddress", ipAddress); // 公网ip地址
			map.put("period", buyCycle); // 购买周期
			String parameter = JsonUtil.getJsonString4JavaPOJO(map);
			instanceInfoBO.setResourceInfo(parameter);
			/**
			 * 2.写回本地数据表
			 */
			instanceService.updateTScsInstanceInfoStateAndResourceInfoById(instanceId, state, parameter);
		}
	}

	// @Override
	// public void writeBackForRequestFailure(int instanceId, int state) throws
	// Exception
	// {
	// if (state == 0)
	// {
	// state = 2;
	// }
	// instanceService.updateTScsIntanceInfoStateById(instanceId, state);
	// }

}
