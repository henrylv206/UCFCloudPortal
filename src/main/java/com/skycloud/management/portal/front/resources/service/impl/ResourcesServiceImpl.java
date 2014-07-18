/**
 * 2012-1-16  下午04:16:27  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.dao.ResourcesDao;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateMCBO;
import com.skycloud.management.portal.front.resources.service.ResourcesService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;

/**
 * @author shixq
 * @version $Revision$ 下午04:16:27
 */
public class ResourcesServiceImpl implements ResourcesService {

	private static Log log = LogFactory.getLog(ResourcesServiceImpl.class);

	private IOrderDao orderDao;

	private IInstanceInfoDao instanceInfoDao;

	private IAuditSevice auditService;

	private ResourcesDao resourcesDao;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.ResourcesService
	 * #queryHCTemplateMonitorList
	 * (com.skycloud.management.portal.front.resources
	 * .action.vo.ResourcesQueryVO)
	 */
	@Override
	public boolean queryTemplateService(ResourcesQueryVO vo)
			throws SCSException {
		List<TemplateMCBO> list = resourcesDao.queryTemplateService(vo);
		if (list != null && list.size() > 0)
			return true;
		return false;
	}
	
	public TInstanceInfoBO updateInstanceInfoState3(ResourcesModifyVO vmModifyVO)
			throws SCSException {
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.ResourcesService
	 * #insertDirtyReadChangeBackUpInstance
	 * (com.skycloud.management.portal.front.resources
	 * .action.vo.ResourcesModifyVO,
	 * com.skycloud.management.portal.admin.sysuser.entity.TUserBO)
	 */
	@Override
	public String insertDirtyReadChangeInstance(ResourcesModifyVO vmModifyVO,
			TUserBO user) throws Exception {
		TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
		int index = 0;
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(3);// 作废申请
			order.setState(1);// 申请状态
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setInstanceInfoId(vmModifyVO.getId());
			order.setZoneId(info.getZoneId());
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
								user.getRoleApproveLevel(), 0, "自动审批", user
										.getEmail(), 3, info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:" + e.getMessage());
						throw new SCSException("approveOrder error:"
								+ e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(),
								saveOrderId, user.getRoleApproveLevel() + 1, 0,
								"自动审批", user.getEmail(), 3, info
										.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:" + e.getMessage());
						throw new SCSException("approveOrder error:"
								+ e.getMessage());
					}
				}
			}
		}
		return ResourcesUtil.resultTOString(index);
	}

	public TServiceInstanceBO updateServiceInfoState(int serviceID)
			throws SCSException {
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

	public String insertMOInstance(ResourcesModifyVO vmModifyVO, TUserBO user,
			int serviceID) throws Exception {
		TInstanceInfoBO info = updateInstanceInfoState3(vmModifyVO);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		int index = 0;
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(3);// 作废申请
			order.setState(1);// 申请状态
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setCreatorUserId(user.getId());// 下单人ID
			//order.setInstanceInfoId(vmModifyVO.getId());
			order.setServiceInstanceId(serviceID);
			order.setZoneId(info.getZoneId());
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
								user.getRoleApproveLevel(), 0, "自动审批", user
										.getEmail(), 3, info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:" + e.getMessage());
						throw new SCSException("approveOrder error:"
								+ e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(),
								saveOrderId, user.getRoleApproveLevel() + 1, 0,
								"自动审批", user.getEmail(), 3, info
										.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:" + e.getMessage());
						throw new SCSException("approveOrder error:"
								+ e.getMessage());
					}
				}
			}
		}
		return ResourcesUtil.resultTOString(index);
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

	public ResourcesDao getResourcesDao() {
		return resourcesDao;
	}

	public void setResourcesDao(ResourcesDao resourcesDao) {
		this.resourcesDao = resourcesDao;
	}

	@Override
	public TInstanceInfoBO searchInstanceInfoByID(int ID) throws SCSException {
		// TODO Auto-generated method stub
		return this.instanceInfoDao.searchInstanceInfoByID(ID);
	}

	@Override
	public void updateInstanceInfo(TInstanceInfoBO bo) throws SCSException {
		// TODO Auto-generated method stub
		this.instanceInfoDao.update(bo);
	}

}
