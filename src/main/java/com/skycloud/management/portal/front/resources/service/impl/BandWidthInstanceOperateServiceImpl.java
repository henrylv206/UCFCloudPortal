package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.service.BandWidthInstanceOperateService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;

public class BandWidthInstanceOperateServiceImpl implements BandWidthInstanceOperateService {

	private ResouceServiceInstanceOperateDao bandWidthInstanceOperateDao;

	private IOrderDao orderDao;

	private IInstanceInfoDao instanceInfoDao;

	private IAuditSevice auditService;

	private AsyncJobVDCService asyncJobVDCService;

	private static Log log = LogFactory.getLog(BandWidthInstanceOperateServiceImpl.class);

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
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

	public ResouceServiceInstanceOperateDao getBandWidthInstanceOperateDao() {
		return bandWidthInstanceOperateDao;
	}

	public void setBandWidthInstanceOperateDao(ResouceServiceInstanceOperateDao bandWidthInstanceOperateDao) {
		this.bandWidthInstanceOperateDao = bandWidthInstanceOperateDao;
	}

	@Override
	public List<ResourcesVO> queryBandWidthInstanceList(ResourcesQueryVO rqvo) throws Exception {
		List<ResourcesVO> resultList = new ArrayList<ResourcesVO>();
		try {
			rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BANDWIDTH);
			resultList = bandWidthInstanceOperateDao.queryResouceServiceInstanceInfo(rqvo);
		}
		catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return resultList;
	}

	@Override
	public int queryBandWidthInstanceListCount(ResourcesQueryVO rqvo) throws Exception {
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BANDWIDTH);
		return bandWidthInstanceOperateDao.queryResouceServiceInstanceInfoCount(rqvo);
	}

	@Override
	public String insertDirtyReadChangeBandWidthInstance(ResourcesModifyVO vmModifyVO, TUserBO user) throws Exception {
		// 退订不可用资源时，直接作废 // bug 0003965
		TInstanceInfoBO info = updateInstanceInfoState37(vmModifyVO);// bug
		                                                             // 0003873
		int index = 0;
		int project_switch = ConstDef.curProjectId;

		if (info != null) {
			if (info.getState() != 4) {
				index = approveOrder(user, info, index, vmModifyVO, ConstDef.ORDER_CHANGE_TYPE);
			} else {
				index = 1;
			}

		}
		if (project_switch == 1) {
			// elaster 没有带宽的相关接口

		} else if (project_switch == 2) {
			index = getAsyncJobString(index, vmModifyVO.getId(), OperationType.UPDATE_BANDWIDTH, user.getId(), AuditStateVDC.WAIT_AUDIT);
		}

		return ResourcesUtil.resultTOString(index);
	}

	@Override
	public String insertDirtyReaddeleteBandWidthInstance(ResourcesModifyVO vmModifyVO, TUserBO user) throws Exception {

		TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
		int index = 0;
		int project_switch = ConstDef.curProjectId;
		if (info != null) {
			index = approveOrder(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE);
		}
		if (project_switch == 1) {
			// elaster 没有带宽的相关接口

		} else if (project_switch == 2) {
			index = getAsyncJobString(index, vmModifyVO.getId(), OperationType.DELETE_BANDWIDTH, user.getId(), AuditStateVDC.WAIT_AUDIT);
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
			if (info.getState() == 7) { // 退订不可用资源时，直接作废
				index = instanceInfoDao.updateServiceState4(serviceID);
			}
			if (info.getState() == 6) {
				return null;
			} else {
				index = instanceInfoDao.updateServiceState(serviceID);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	}

	@Override
	public String insertBWInstance(ResourcesModifyVO vmModifyVO, TUserBO user, int serviceID) throws Exception {

		TInstanceInfoBO info = updateInstanceInfoState37(vmModifyVO);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		int index = 0;
		int project_switch = ConstDef.curProjectId;
		if (project_switch == 1) {

		} else if (project_switch == 2) {
			index = getAsyncJobString(index, vmModifyVO.getId(), OperationType.DELETE_BANDWIDTH, user.getId(), AuditStateVDC.WAIT_AUDIT);
		}
		if (info != null) {
			if (info.getState() != 4) {
				index = approveOrderService(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE, serviceID);
			} else {
				index = 1;
			}
		}
		return ResourcesUtil.resultTOString(index);

	}

	private int getAsyncJobString(int orderId, int instanceId, OperationType opertion, int userId, AuditStateVDC auditStateVDC) throws SQLException {
		int index = 0;
		TInstanceInfoBO instanceInfoBO = instanceInfoDao.findInstanceInfoById(instanceId);
		if (instanceInfoBO != null) {
			JSONObject jsonObject = JSONObject.fromObject(instanceInfoBO.getResourceInfo());
			if (jsonObject.containsKey("ipAddress")) {
				instanceInfoBO.setIpAddress(jsonObject.getString("ipAddress"));
			} else {
				instanceInfoBO.setIpAddress("error info");
				index = 0;
			}
			// JOB信息封装开始
			AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
			String parameters = "";// 操作参数，调用api所需要的参数
			String ResourceTemplateID = ConstDef.getVDCTemplateID(instanceInfoBO.getTemplateType(), instanceInfoBO.getTemplateId());
			Map<String, Object> mapJob = new HashMap<String, Object>();
			if (opertion.equals(OperationType.UPDATE_BANDWIDTH)) {
				mapJob.put("BWTemplateID", ResourceTemplateID);
			}
			mapJob.put("IP", instanceInfoBO.getIpAddress());
			parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
			asynJobVDCPO.setParameter(parameters);
			asynJobVDCPO.setAuditstate(auditStateVDC);
			asynJobVDCPO.setUser_id(userId);
			asynJobVDCPO.setTemplate_id(instanceInfoBO.getTemplateId());
			asynJobVDCPO.setInstance_info_id(instanceId);
			asynJobVDCPO.setTemplate_res_id(ResourceTemplateID);
			asynJobVDCPO.setOrder_id(orderId);
			asynJobVDCPO.setOperation(opertion);
			try {
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
			}
			catch (SCSException e) {
				e.printStackTrace();
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
	 *             创建人： 冯永凯 创建时间：2012-2-23 下午04:15:54
	 */
	private int approveOrder(TUserBO user, TInstanceInfoBO info, int index, ResourcesModifyVO vmModifyVO, int type) throws SCSException {

		TOrderBO order = new TOrderBO();
		if (type == ConstDef.ORDER_CHANGE_TYPE) {
			order.setStorageSize(vmModifyVO.getStorage_size()); // 带宽大小
		}
		order.setType(type);// 修改申请
		order.setState(1);// 申请状态
		order.setCreatorUserId(user.getId());// 下单人ID
		order.setInstanceInfoId(vmModifyVO.getId());
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int project_switch = ConstDef.curProjectId;
		if (project_switch == 2) {
			order.setMemorySize(Integer.parseInt(vmModifyVO.getVmtemplateId()));
		}

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
		if (type == ConstDef.ORDER_CHANGE_TYPE) {
			order.setStorageSize(vmModifyVO.getStorage_size()); // 带宽大小
		}
		order.setType(type);// 修改申请
		order.setState(1);// 申请状态
		order.setCreatorUserId(user.getId());// 下单人ID
		// order.setInstanceInfoId(vmModifyVO.getId());
		order.setServiceInstanceId(serviceID);
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int project_switch = ConstDef.curProjectId;
		if (project_switch == 2) {
			order.setMemorySize(Integer.parseInt(vmModifyVO.getVmtemplateId()));
		}

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
	 * 用于变更，退订等产生订单的操作
	 * 
	 * @param vmModifyVO
	 * @return
	 * @throws SCSException
	 */
	public TInstanceInfoBO updateInstanceInfoState37(ResourcesModifyVO vmModifyVO) throws SCSException {
		int index = 0;
		int id = vmModifyVO.getId();
		TInstanceInfoBO info = null;
		if (id != 0) {
			info = instanceInfoDao.searchInstanceInfoByID(id);
			if (info.getState() == 3) {
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

	/**
	 * 用于不产生订单的操作
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

	@Override
	public List<TInstanceInfoBO> findUsableIpInstance(int userId) throws SQLException {
		// return instanceInfoDao.findUsableIpInstance(userId);
		// modify by zhanghuizheng
		return instanceInfoDao.findUsableIpInstance(userId);
	}

	@Override
	/**
	 * 购买带宽时查询可用的公网ip,项目不同查询逻辑不同
	 * 1.3功能，对于产品来说，支持多资源池
	 */
	public List<TInstanceInfoBO> findUsableIpInstance2(int userId,int resourcePoolsId,int zoneId) throws SQLException {
		// return instanceInfoDao.findUsableIpInstance(userId);
		// modify by zhanghuizheng
		int project_switch = ConstDef.curProjectId;
		List<TInstanceInfoBO> result = null;
		//产品
		if (project_switch == 1) {
			result = instanceInfoDao.findUsableIpInstance2(userId,resourcePoolsId,zoneId);
		} else if (project_switch == 2) { //vdc项目
			result = instanceInfoDao.findUsableIpInstance(userId);
		}
		return result;
	}

	@Override
	public int findIPAddressExist(String ipAddress, int templateType, int userId) throws SQLException {
		return instanceInfoDao.findIPAddressExist(ipAddress, templateType, userId);
	}

	@Override
	public String deleteBandWidthInstance(ResourcesModifyVO vmModifyVO) throws Exception {
		TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
		int index = 0;
		int project_switch = ConstDef.curProjectId;
		if (project_switch == 1) {
			// elaster 没有带宽的相关接口
		} else if (project_switch == 2) {
			index = getAsyncJobString(index, vmModifyVO.getId(), OperationType.DELETE_BANDWIDTH, vmModifyVO.getUserID(), AuditStateVDC.NO_AUDIT);
		}
		return ResourcesUtil.resultTOString(index);

	}

	@Override
	public int update(TPublicIPBO publicIP) throws SCSException {
		return bandWidthInstanceOperateDao.update(publicIP);
	}
}
