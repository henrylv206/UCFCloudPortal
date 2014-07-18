package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.skycloud.h3c.loadbalance.po.vservice.VServiceRowPO;
import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.network.h3c.dao.IHLJVpnInstanceDao;
import com.skycloud.management.portal.front.network.h3c.entity.HLJVpnInstance;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.FirewallVO;
import com.skycloud.management.portal.front.resources.action.vo.ObjectStorageVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.IFirewallDao;
import com.skycloud.management.portal.front.resources.dao.PublicIPInstanceDao;
import com.skycloud.management.portal.front.resources.service.IFirewallService;

/**
 * 防火墙资源使用相关业务实现
 *
 * @author jiaoyz
 */
public class FirewallServiceImpl implements IFirewallService {

	private IFirewallDao firewallDao;

	private IInstanceInfoDao instanceInfoDao;

	private IOrderDao orderDao;

	private IAuditSevice auditService;

	private PublicIPInstanceDao publicIPInstanceDao;

	private IHLJVpnInstanceDao hljVpnInstanceDao;

	@Override
	public TTemplateVMBO creatSpecalFirewallTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
		//
		template.setCode(null);// 模板编码
		// 特殊模板 1：用户定义的特殊模板
		template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);
		template.setCpufrequency(0);
		template.setCpuNum(0);
		template.setMemorySize(0);
		vminfo.setDisknumber(0);
		template.setStorageSize(vminfo.getStorageSize());
		template.setVethAdaptorNum(0);
		if (user != null) {
			template.setCreatorUserId(user.getId());
		}
		// template.setZoneId(vminfo.getZoneId());
		template.setResourcePoolsId(vminfo.getPoolId());
		template.setType(vminfo.getTemplateType());
		template.setOperType(1);
		Timestamp ts = new Timestamp(new Date().getTime());
		template.setCreateTime(ts.toString());
		template.setMeasureMode("Duration");// Duration：按时长计量
		template.setState(ConstDef.STATE_TWO);//可用状态modify by hfk 13-1-22
		template.seteOsId(0);
		// 根据物理机创建模板,操作系统信息组织要创建服务信息
		product.setName("FIREWALL");
		product.setCreateDate(ts);
		product.setType(template.getType());
		product.setState(ConstDef.STATE_THREE);// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线
		product.setDescription("防火墙自动创建服务");
		product.setSpecification(product.getDescription());
		product.setQuotaNum(1);// 待确定 TODO
		product.setPrice(0f);
		product.setUnit(vminfo.getUnit());
		product.setOperateType(1);
		product.setIsDefault(0);// 待确定 TODO
		return template;
	}

	@Override
	public List<ObjectStorageVO> getObjectStorage(int userId, int curPage, int pageSize) throws Exception {
		if (userId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return firewallDao.getObjectStorage(userId, curPage, pageSize);
	}

	@Override
	public List<FirewallVO> getFirewallList(int userId, int curPage, int pageSize) throws Exception {
		if (userId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return firewallDao.getFirewallList(userId, curPage, pageSize);
	}

	@Override
	public int getFirewallListCount(int userId) throws Exception {
		if (userId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return firewallDao.getFirewallListCount(userId);
	}

	@Override
	public List<FirewallVO> searchFirewall(String name, int userId, int curPage, int pageSize) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		if (userId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return firewallDao.searchFirewall(name, userId, curPage, pageSize);
	}

	@Override
	public int searchFirewallCount(String name, int userId) throws Exception {
		if (StringUtils.isBlank(name)) {
			throw new Exception("Paramater is missing");
		}
		if (userId < 1) {
			throw new Exception("Paramater is invalid");
		}
		return firewallDao.searchFirewallCount(name, userId);
	}

	@Override
	public void destroyObjectStorage(int instanceId, TUserBO user, String reason) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			info.setState(6);
			instanceInfoDao.update(info);
			TOrderBO order = new TOrderBO();
			order.setType(ConstDef.ORDER_DEL_TYPE);
			order.setState(1); // 申请状态
			order.setCreatorUserId(user.getId()); // 下单人ID
			order.setInstanceInfoId(instanceId);
			order.setZoneId(info.getZoneId());
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			// 日期+6位随机数
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
						                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
						                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				}
			}
		}
	}

	public void destroyObjectStorage(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			if (info.getState() == 7) {// bug 0003965
				info.setState(4);// 退订不可用的资源，直接作废，不生成订单
			} else {
				info.setState(3);
				TOrderBO order = new TOrderBO();
				order.setType(ConstDef.ORDER_DEL_TYPE);
				order.setState(1); // 申请状态
				order.setCreatorUserId(user.getId()); // 下单人ID
				// order.setInstanceInfoId(instanceId);
				order.setServiceInstanceId(serviceID);
				order.setZoneId(info.getZoneId());
				order.setClusterId(info.getState()); // clusterId中保存原instance状态
				order.setOrderApproveLevelState(user.getRoleApproveLevel());
				order.setState(user.getRoleApproveLevel());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
				order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
				// 日期+6位随机数
				order.setReason(reason);
				int saveOrderId = orderDao.save(order);
				if (saveOrderId > 0) {
					if (user.getRoleApproveLevel() == 4) {
						try {
							auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
							                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					} else if (user.getRoleApproveLevel() < 4) {
						try {
							auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
							                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					}
				}
			}
			instanceInfoDao.update(info);
		}
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
			if (info.getState() == 7) {// 不可用的退订时，直接作废
				index = instanceInfoDao.updateServiceState4(serviceID);
			} else if (info.getState() == 6) {
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
	public void destroyFirewall(int instanceId, TUserBO user, String reason) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);

		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			info.setState(6);
			instanceInfoDao.update(info);
			TOrderBO order = new TOrderBO();
			order.setType(ConstDef.ORDER_DEL_TYPE);
			order.setState(1); // 申请状态
			order.setCreatorUserId(user.getId()); // 下单人ID
			order.setInstanceInfoId(instanceId);
			order.setZoneId(info.getZoneId());
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			// 日期+6位随机数
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
						                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
						                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				}
			}
		}
	}

	public void destroyfw(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			if (info.getState() == 7) {// bug 0003965
				info.setState(4);// 退订不可用的直接作废，不生成订单
			} else {
				info.setState(3);
				TOrderBO order = new TOrderBO();
				order.setType(ConstDef.ORDER_DEL_TYPE);
				order.setState(1); // 申请状态
				order.setCreatorUserId(user.getId()); // 下单人ID
				// order.setInstanceInfoId(instanceId);
				order.setServiceInstanceId(serviceID);
				order.setZoneId(info.getZoneId());
				order.setClusterId(info.getState()); // clusterId中保存原instance状态
				order.setOrderApproveLevelState(user.getRoleApproveLevel());
				order.setState(user.getRoleApproveLevel());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
				order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
				// 日期+6位随机数
				order.setReason(reason);
				int saveOrderId = orderDao.save(order);
				if (saveOrderId > 0) {
					if (user.getRoleApproveLevel() == 4) {
						try {
							auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
							                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					} else if (user.getRoleApproveLevel() < 4) {
						try {
							auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
							                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					}
				}
			}
			instanceInfoDao.update(info);
		}
	}

	@Override
	public void insertDestroyFirewall(int instanceId, int bindId, TUserBO user, String reason) throws Exception {
		if (bindId > 0) {
			this.insertDestoryLoadBalance(bindId, user, reason);
		}
		this.destroyFirewall(instanceId, user, reason);
	}

	@Override
	public void insertDestroyfw(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		this.destroyfw(instanceId, user, reason, serviceID);
	}

	@Override
	public void insertDestroyOM(int instanceId, TUserBO user, String reason) throws Exception {
		this.destroyObjectStorage(instanceId, user, reason);
	}

	@Override
	public void insertDestroyOS(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		this.destroyObjectStorage(instanceId, user, reason, serviceID);
	}

	@Override
	public void insertDestroyLoadBalance(int instanceId, int bindId, TUserBO user, String reason) throws Exception {
		if (bindId > 0) {
			this.destroyFirewall(bindId, user, reason);
		}
		this.insertDestoryLoadBalance(instanceId, user, reason);
	}

	@Override
	public List<VServiceRowPO> queryVirtualServiceListByUser(TUserBO user) throws Exception{
		return firewallDao.queryVirtialServiceListByUser(user);
	}
	@Override
	public HLJVpnInstance getInstanceByUserId(int userId) throws Exception {
		return hljVpnInstanceDao.getInstanceByUserId(userId);
	}
	@Override
	public void insertDestroylb(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {

		this.insertDestorylb(instanceId, user, reason, serviceID);
	}

	@Override
	public List<TInstanceInfoBO> queryFWOrLBInstance(int userId) throws Exception {
		return firewallDao.getFWOrLBList(userId);
	}

	@Override
	public List<TInstanceInfoBO> queryBoughtOMInstance(int userId) throws Exception {
		return firewallDao.getBoughtOMList(userId);
	}

	public void setPublicIPInstanceDao(PublicIPInstanceDao publicIPInstanceDao) {
		this.publicIPInstanceDao = publicIPInstanceDao;
	}

	public void setFirewallDao(IFirewallDao firewallDao) {
		this.firewallDao = firewallDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}


	public IHLJVpnInstanceDao getHljVpnInstanceDao() {
		return hljVpnInstanceDao;
	}

	public void setHljVpnInstanceDao(IHLJVpnInstanceDao hljVpnInstanceDao) {
		this.hljVpnInstanceDao = hljVpnInstanceDao;
	}

	@Override
	public void insertDestoryLoadBalance(int instanceId, TUserBO user, String reason) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			info.setState(6);
			instanceInfoDao.update(info);
			TOrderBO order = new TOrderBO();
			order.setType(ConstDef.ORDER_DEL_TYPE);
			order.setState(1); // 申请状态
			order.setCreatorUserId(user.getId()); // 下单人ID
			order.setInstanceInfoId(instanceId);
			order.setZoneId(info.getZoneId());
			order.setClusterId(info.getState()); // clusterId中保存原instance状态
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
			// 日期+6位随机数
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				if (user.getRoleApproveLevel() == 4) {
					try {
						auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
						                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				} else if (user.getRoleApproveLevel() < 4) {
					try {
						auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
						                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
					}
					catch (SQLException e) {
						throw new SCSException("approveOrder error:" + e.getMessage());
					}
				}
			}
		}
	}

	public void insertDestorylb(int instanceId, TUserBO user, String reason, int serviceID) throws Exception {
		TInstanceInfoBO info = instanceInfoDao.searchInstanceInfoByID(instanceId);
		TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceID);
		if (info.getState() == 6) {
			return;
		} else {
			info.setClusterId(info.getState());
			if (info.getState() == 7) {// bug 0003965
				info.setState(4);// 退订不可用的资源，直接作废，不生成订单
			} else {
				info.setState(3);
				TOrderBO order = new TOrderBO();
				order.setType(ConstDef.ORDER_DEL_TYPE);
				order.setState(1); // 申请状态
				order.setCreatorUserId(user.getId()); // 下单人ID
				// order.setInstanceInfoId(instanceId);
				order.setServiceInstanceId(serviceID);
				order.setZoneId(info.getZoneId());
				order.setClusterId(info.getState()); // clusterId中保存原instance状态
				order.setOrderApproveLevelState(user.getRoleApproveLevel());
				order.setState(user.getRoleApproveLevel());
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); // 订单编码
				order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
				// 日期+6位随机数
				order.setReason(reason);
				int saveOrderId = orderDao.save(order);
				if (saveOrderId > 0) {
					if (user.getRoleApproveLevel() == 4) {
						try {
							auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(),
							                          ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					} else if (user.getRoleApproveLevel() < 4) {
						try {
							auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(),
							                               ConstDef.ORDER_DEL_TYPE, info.getTemplateType());
						}
						catch (SQLException e) {
							throw new SCSException("approveOrder error:" + e.getMessage());
						}
					}
				}
			}
			instanceInfoDao.update(info);
		}
	}

	@Override
	public long searchBindTipIdBylbId(TUserBO user, int lbId) throws Exception {
		ResourcesQueryVO rqvo = new ResourcesQueryVO();
		rqvo.setUser(user);
		rqvo.setOperateSqlType(ConstDef.RESOURCE_TYPE_PUBLICNETWORKIP);
		List<ResourcesVO> tiplist = publicIPInstanceDao.queryResouceServiceInstanceInfo(rqvo);
		for (int i = 0, l = tiplist.size(); i < l; i++) {
			ResourcesVO ipVo = tiplist.get(i);
			String ipResInfo = ipVo.getResource_info();
			JSONObject jsonObject = JSONObject.fromObject(ipResInfo);
			String ipAddressId = "";
			if (jsonObject.containsKey("ipAddressId")) {
				ipAddressId = jsonObject.getString("ipAddressId");
				long rsId = publicIPInstanceDao.searchBindRsIdByipId(Integer.valueOf(ipAddressId));
				if (rsId == -1) {
					return rsId;
				}
			}
		}
		return 0;
	}

	@Override
	// fix bug 2417
	public List queryBindedVSByIP(int userId) {
		return firewallDao.queryBindedVSByIP(userId);
	}
}
