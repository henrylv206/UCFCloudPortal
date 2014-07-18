package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConstDef;
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
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;
import com.skycloud.management.portal.front.resources.service.BackUpInstanceOperateService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;

public class BackUpInstanceOperateServiceImpl implements
		BackUpInstanceOperateService {

	private static Log log = LogFactory
			.getLog(BackUpInstanceOperateServiceImpl.class);

	private ResouceServiceInstanceOperateDao backUpInstanceOperateDao;

	private IOrderDao orderDao;

	private IInstanceInfoDao instanceInfoDao;

	private IAuditSevice auditService;

	@Override
	public int queryBackUpInstanceListCount(ResourcesQueryVO rqvo)
			throws Exception {
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BACKUP);
		return backUpInstanceOperateDao
				.queryResouceServiceInstanceInfoCount(rqvo);
	}
	
	@Override
	public int queryBackUpInstanceListCountBeforApprove(ResourcesQueryVO rqvo) throws Exception {
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BACKUP);
		return backUpInstanceOperateDao.queryResouceServiceInstanceInfoCountBeforApprove(rqvo);
	}
	
	public int queryInstanceListCountBeforeApprove(ResourcesQueryVO rqvo) throws Exception {
		return backUpInstanceOperateDao.queryResouceServiceInstanceInfoCountBeforApprove(rqvo);
	}


	@Override
	public List<ResourcesVO> queryBackUpInstanceList(ResourcesQueryVO rqvo)
			throws Exception {
		List<ResourcesVO> resultList = new ArrayList<ResourcesVO>();
		try {
			rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BACKUP);
			resultList = backUpInstanceOperateDao
					.queryResouceServiceInstanceInfo(rqvo);
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
		return resultList;
	}

	@Override
	public String insertDirtyReadChangeBackUpInstance(
			ResourcesModifyVO vmModifyVO, TUserBO user) throws Exception {
		//to fix bug [3534]
		TInstanceInfoBO info = updateInstanceInfoState3(vmModifyVO);
		int index = 0;
		if (info != null) {
			index = approveOrder(user, info, index, vmModifyVO, ConstDef.ORDER_CHANGE_TYPE);
		}

		return ResourcesUtil.resultTOString(index);
	}

	@Override
	public String insertDirtyReaddeleteBackUpInstance(
			ResourcesModifyVO vmModifyVO, TUserBO user) throws Exception {
		TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
		int index = 0;
		if (info != null) {
			index = approveOrder(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE);
		}
		return ResourcesUtil.resultTOString(index);
	}
	
	public String insertBackUpDestroy(ResourcesModifyVO vmModifyVO, TUserBO user, int serviceID) throws Exception {
		TInstanceInfoBO info = updateInstanceInfoState3(vmModifyVO);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		int index = 0;
		if (info != null) {
			index = approveOrderService(user, info, index, vmModifyVO, ConstDef.ORDER_DEL_TYPE, serviceID);
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
			} catch (SQLException e) { 			 
				e.printStackTrace();
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
	
	/**
	 *  提交审核订单
	 * @param user
	 * @param info
	 * @param index
	 * @param vmModifyVO
	 * @param type
	 * @return
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-2-23  下午04:15:54
	 */
	private int approveOrder(TUserBO user, TInstanceInfoBO info, int index,
			ResourcesModifyVO vmModifyVO, int type) throws SCSException {

		TOrderBO order = new TOrderBO();
		if (type == ConstDef.ORDER_CHANGE_TYPE) {
			order.setStorageSize(vmModifyVO.getStorage_size()); // 备份空间大小
		}
		order.setType(type);
		order.setState(1);// 申请状态
		order.setCreatorUserId(user.getId());// 下单人ID
		order.setInstanceInfoId(vmModifyVO.getId());
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date())
				+ (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int saveOrderId = orderDao.save(order);

		if (saveOrderId > 0) {
			index = saveOrderId;
			if (user.getRoleApproveLevel() == 4) {
				try {
					auditService.approveOrder(user.getId(), saveOrderId,
							user.getRoleApproveLevel(), 0, "自动审批",
							user.getEmail(), type, info.getTemplateType());
				} catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:"
							+ e.getMessage());
				}
			} else if (user.getRoleApproveLevel() < 4) {
				try {
					auditService.isAutoApproveUser(user.getId(), saveOrderId,
							user.getRoleApproveLevel() + 1, 0, "自动审批",
							user.getEmail(), type, info.getTemplateType());
				} catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:"
							+ e.getMessage());
				}
			}
		}
		return index;
	}
	
	private int approveOrderService(TUserBO user, TInstanceInfoBO info, int index,
			ResourcesModifyVO vmModifyVO, int type, int serviceID) throws SCSException {

		TOrderBO order = new TOrderBO();
		if (type == ConstDef.ORDER_CHANGE_TYPE) {
			order.setStorageSize(vmModifyVO.getStorage_size()); // 备份空间大小
		}
		order.setType(type);
		order.setState(1);// 申请状态
		order.setCreatorUserId(user.getId());// 下单人ID
		//order.setInstanceInfoId(vmModifyVO.getId());
		order.setServiceInstanceId(serviceID);
		order.setZoneId(info.getZoneId());
		order.setClusterId(info.getState()); // clusterId中保存原instance状态
		order.setOrderApproveLevelState(user.getRoleApproveLevel());
		order.setState(user.getRoleApproveLevel());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
		order.setOrderCode(sdf.format(new java.util.Date())
				+ (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
		// 日期+6位随机数
		order.setReason(vmModifyVO.getApply_reason());
		int saveOrderId = orderDao.save(order);

		if (saveOrderId > 0) {
			index = saveOrderId;
			if (user.getRoleApproveLevel() == 4) {
				try {
					auditService.approveOrder(user.getId(), saveOrderId,
							user.getRoleApproveLevel(), 0, "自动审批",
							user.getEmail(), type, info.getTemplateType());
				} catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:"
							+ e.getMessage());
				}
			} else if (user.getRoleApproveLevel() < 4) {
				try {
					auditService.isAutoApproveUser(user.getId(), saveOrderId,
							user.getRoleApproveLevel() + 1, 0, "自动审批",
							user.getEmail(), type, info.getTemplateType());
				} catch (SQLException e) {
					log.error("approveOrder error:" + e.getMessage());
					throw new SCSException("approveOrder error:"
							+ e.getMessage());
				}
			}
		}
		return index;
	}	

	/**
	 * 更新实例信息
	 * 
	 * @param vmModifyVO
	 * @return
	 * @throws Exception
	 * 创建人： 冯永凯 创建时间：2012-2-23 下午03:58:05
	 */
	private TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO)
			throws Exception {
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
	
	private TInstanceInfoBO updateInstanceInfoState3(ResourcesModifyVO vmModifyVO)
			throws Exception {
		int index = 0;
		int id = vmModifyVO.getId();
		TInstanceInfoBO info = null;
		if (id != 0) {
			info = instanceInfoDao.searchInstanceInfoByID(id);
			if (info.getState() == 6) {
				return null;
			} else {
				info.setClusterId(info.getState());
				info.setState(3);
				index = instanceInfoDao.update(info);
				if (index == 0) {
					return null;
				}
			}
		}
		return info;
	}	

	@Override
	public List<TemplateVMBO> queryBackTemplateAvailableList(
			ResourcesQueryVO rqvo) throws Exception {
		List<TemplateVMBO> list = null;
		try {
			rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_BACKUP);
			list = backUpInstanceOperateDao
					.queryResouceTemplateAvailableList(rqvo);
		} catch (Exception e) {
			log.error("backUpInstanceOperateDao is =="
					+ backUpInstanceOperateDao);
			throw e;
		}
		return list;
	}

	public ResouceServiceInstanceOperateDao getBackUpInstanceOperateDao() {
		return backUpInstanceOperateDao;
	}

	public void setBackUpInstanceOperateDao(
			ResouceServiceInstanceOperateDao backUpInstanceOperateDao) {
		this.backUpInstanceOperateDao = backUpInstanceOperateDao;
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

	public IAuditSevice getAuditService() {
		return auditService;
	}

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}

}
