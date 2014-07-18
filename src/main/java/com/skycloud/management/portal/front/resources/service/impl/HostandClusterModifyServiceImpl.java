/**
 * 2011-11-28  下午03:32:39  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.McStartRequestCommandPo;
import com.skycloud.management.portal.front.command.res.McStopRequestCommandPo;
import com.skycloud.management.portal.front.command.res.McrebootRequestCommandPo;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.service.HostandClusterModifyService;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */
public class HostandClusterModifyServiceImpl implements
		HostandClusterModifyService {

	 private static Log log =
	 LogFactory.getLog(HostandClusterModifyServiceImpl.class);

	private IAsyncJobInfoDAO asyncJobDao;
	private IInstanceInfoDao instanceInfoDao;
	private IOrderDao orderDao;
	private IAuditSevice auditService;

	private String parameter;//
	private String operation;//
	private int id;// 小型机ID

	private TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO)
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
				info.setState(6);
				index = instanceInfoDao.update(info);
				if (index == 0) {
					return null;
				}
			}
		}
		return info;
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

	private int insertAsyncJob(ResourcesModifyVO hcModifyVO)
			throws SCSException {
		AsyncJobInfo ajInfo = init(hcModifyVO);
		int index = 0;
		index = asyncJobDao.insertAsyncJob(ajInfo);
		return index;
	}

	private AsyncJobInfo init(ResourcesModifyVO hcModifyVO) {
		AsyncJobInfo ajInfo = new AsyncJobInfo();
		ajInfo.setAPPLY_ID(1);
		ajInfo.setINSTANCE_INFO_ID(hcModifyVO.getId());
		ajInfo.setOPERATION(operation);
		ajInfo.setPARAMETER(parameter);
		ajInfo.setJOBSTATE(1);
		return ajInfo;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.HostandClusterService
	 * #hcStart()
	 */
	@Override
	public String insertHCStart(ResourcesModifyVO hcModifyVO)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState(hcModifyVO);
		int index = 0;
		if (info != null) {
			McStartRequestCommandPo vmStart = new McStartRequestCommandPo();
			id = hcModifyVO.geteInstanceId();
			vmStart.setVmId(id);
			vmStart.setUserId(hcModifyVO.getUserID());
			parameter = CommandCreateUtil.getJsonParameterStr(vmStart);// 得到json命令串
			operation = vmStart.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段
			index = insertAsyncJob(hcModifyVO);
		}
		return ResourcesUtil.resultTOString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.HostandClusterService
	 * #hcReboot()
	 */
	@Override
	public String insertHCReboot(ResourcesModifyVO hcModifyVO)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState(hcModifyVO);
		int index = 0;
		if (info != null) {
			McrebootRequestCommandPo vmStart = new McrebootRequestCommandPo();
			id = hcModifyVO.geteInstanceId();
			vmStart.setVmId(id);
			vmStart.setUserId(hcModifyVO.getUserID());
			parameter = CommandCreateUtil.getJsonParameterStr(vmStart);// 得到json命令串
			operation = vmStart.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段
			index = insertAsyncJob(hcModifyVO);
		}
		return ResourcesUtil.resultTOString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.HostandClusterService
	 * #hcStop()
	 */
	@Override
	public String insertHCStop(ResourcesModifyVO hcModifyVO)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState(hcModifyVO);
		int index = 0;
		if (info != null) {
			McStopRequestCommandPo vmStart = new McStopRequestCommandPo();
			id = hcModifyVO.geteInstanceId();
			vmStart.setVmId(id);
			vmStart.setUserId(hcModifyVO.getUserID());
			parameter = CommandCreateUtil.getJsonParameterStr(vmStart);// 得到json命令串
			operation = vmStart.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段
			index = insertAsyncJob(hcModifyVO);
		}
		return ResourcesUtil.resultTOString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.HostandClusterService
	 * #hcApplyUpdate()
	 */
	@Override
	public String insertHCApplyUpdate(ResourcesModifyVO hcModifyVO,TUserBO user)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState(hcModifyVO);
		int index = 0;
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(2);// 修改申请
			order.setState(1);// 申请状态
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setClusterId(info.getState()); //clusterId中保存原instance状态
			order.setCpuNum(hcModifyVO.getCpu_num());
			order.setMemorySize(hcModifyVO.getMem_size());
			order.setInstanceInfoId(info.getId());
			order.setZoneId(info.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new java.util.Date())
					+ (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号 日期+6位随机数
			order.setState(user.getRoleApproveLevel());
			order.setReason(hcModifyVO.getApply_reason());
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				  index = saveOrderId;
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId,
								user.getRoleApproveLevel(), 0, "自动审批",
								user.getEmail(), 1,info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:"+e.getMessage());
						throw new SCSException("approveOrder error:"+e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(), saveOrderId,
								user.getRoleApproveLevel()+1, 0, "自动审批",
								user.getEmail(), 1,info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:"+e.getMessage());
						throw new SCSException("approveOrder error:"+e.getMessage());
					}
				}
			}
		}
		return ResourcesUtil.resultTOString(index);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.front.resources.service.HostandClusterService
	 * #hcAapplyDestroy()
	 */
	@Override
	public String insertHCAapplyDestroy(ResourcesModifyVO hcModifyVO,TUserBO user)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState(hcModifyVO);
		int index = 0;
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(3);// 修改申请
			order.setState(1);// 申请状态
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setClusterId(info.getState()); //clusterId中保存原instance状态
			order.setInstanceInfoId(info.getId());
			order.setZoneId(info.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new java.util.Date())
					+ (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号 日期+6位随机数
			order.setState(user.getRoleApproveLevel());
			order.setReason(hcModifyVO.getApply_reason());
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				  index = saveOrderId;
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId,
								user.getRoleApproveLevel(), 0, "自动审批",
								user.getEmail(), 1,info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:"+e.getMessage());
						throw new SCSException("approveOrder error:"+e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(), saveOrderId,
								user.getRoleApproveLevel()+1, 0, "自动审批",
								user.getEmail(), 1,info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:"+e.getMessage());
						throw new SCSException("approveOrder error:"+e.getMessage());
					}
				}
			}
		}
		return ResourcesUtil.resultTOString(index);
	}
	
	public String insertHCDestroy(ResourcesModifyVO hcModifyVO, TUserBO user, int serviceID)
			throws SCSException {
		TInstanceInfoBO info = updateInstanceInfoState3(hcModifyVO);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		int index = 0;
		if (info != null) {
			TOrderBO order = new TOrderBO();
			order.setType(3);// 修改申请
			order.setState(1);// 申请状态
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			//order.setInstanceInfoId(info.getId());
			order.setServiceInstanceId(serviceID);
			order.setZoneId(info.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000));  
																	 
			order.setState(user.getRoleApproveLevel());
			order.setReason(hcModifyVO.getApply_reason());
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				index = saveOrderId;
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId,
								user.getRoleApproveLevel(), 0, "自动审批", user
										.getEmail(), 1, info.getTemplateType());
					} catch (SQLException e) {
						log.error("approveOrder error:" + e.getMessage());
						throw new SCSException("approveOrder error:"
								+ e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(),
								saveOrderId, user.getRoleApproveLevel() + 1, 0,
								"自动审批", user.getEmail(), 1, info
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
	
	public IAsyncJobInfoDAO getAsyncJobDao() {
		return asyncJobDao;
	}

	public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
		this.asyncJobDao = asyncJobDao;
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

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}

}
