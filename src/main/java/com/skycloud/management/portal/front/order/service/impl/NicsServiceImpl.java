package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.dao.IUserVlanDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EVlanIpRange;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.order.service.INicsService;
import com.skycloud.management.portal.front.resources.enumtype.InstanceState;
import com.skycloud.management.portal.front.resources.enumtype.ServiceType;

import edu.emory.mathcs.backport.java.util.Arrays;

public class NicsServiceImpl implements INicsService {

	private final Logger logger = Logger.getLogger(NicsServiceImpl.class); // 日志
	private INicsDao nicsDao;
	private ICloudAPISerivce cloudAPIService;
	private IInstanceInfoDao instanceInfoDao;
	private IUserVlanDao userVlanDao;
	private final Logger log = Logger.getLogger(NicsServiceImpl.class);

	public INicsDao getNicsDao() {
		return nicsDao;
	}

	public void setNicsDao(INicsDao nicsDao) {
		this.nicsDao = nicsDao;
	}

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IUserVlanDao getUserVlanDao() {
		return userVlanDao;
	}

	public void setUserVlanDao(IUserVlanDao userVlanDao) {
		this.userVlanDao = userVlanDao;
	}

	@Override
	public int save(TNicsBO nics) throws SQLException {
		return nicsDao.save(nics);
	}

	@Override
	public int delete(int nicsId) throws SQLException {
		return nicsDao.delete(nicsId);
	}

	@Override
	public int update(TNicsBO nics) throws SQLException {
		return nicsDao.update(nics);
	}

	@Override
	public TNicsBO searchNicsById(int nicsId) throws SQLException {
		return nicsDao.searchNicsById(nicsId);
	}

	@Override
	public List<TNicsBO> searchAllNics() throws SQLException {
		return nicsDao.searchAllNics();
	}

	@Override
	public int searchLastId() throws SQLException {
		return nicsDao.searchLastId();
	}

	@Override
	public List<TNicsBO> searchNicssByInstanceId(int instanceId)
			throws SQLException {
		return nicsDao.searchNicssByInstanceId(instanceId);
	}

	@Override
	public List<TNicsBO> searchNicssByUserId(int userId) throws SQLException {
		return nicsDao.searchNicssByUserId(userId);
	}

	@Override
	public int searchVlanIdByUserId(int userId) throws SQLException {
		int vlanId = 0;
		List<TNicsBO> nicsList = searchNicssByUserId(userId);
		if (nicsList != null && nicsList.size() > 0) {
			vlanId = (int) nicsList.get(0).geteVlanId();
		}
		return vlanId;
	}

	@Override
	public int searchVlanDefaultFreeCount(int zoneId,Integer resourcePoolID) throws Exception {
		int vlanFree = -1;
		try {
			List<ENetwork> listNetwork = cloudAPIService
					.listDefaultNetworksByZoneId(zoneId, resourcePoolID);
			if (listNetwork != null && listNetwork.size() > 0) {
				int vlanAll = listNetwork.size();
				// to fix bug:2622
				int vlanDefaultCount = nicsDao.searchVlanDefaultCount(zoneId);
				vlanFree = vlanAll - vlanDefaultCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchVlanDefaultFreeCount(int zoneId) error:"
					+ e.getMessage());
		}
		return vlanFree;
	}

	@Override
	public int searchVlanOtherFreeCount(int zoneId,Integer resourcePoolID) throws Exception {
		int vlanFree = -1;
		try {
			List<ENetwork> listNetwork = cloudAPIService
					.listOtherNetworksByZoneId(zoneId, resourcePoolID);
			if (listNetwork != null && listNetwork.size() > 0) {
				int vlanAll = listNetwork.size();
				// to fix bug:2622
				// int vlanCount = nicsDao.searchVlanCount(zoneId);
				// int vlanDefaultCount =
				// nicsDao.searchVlanDefaultCount(zoneId);
				// vlanFree = vlanAll - (vlanCount - vlanDefaultCount);
				// to fix bug:3698 3705
				int vlanOtherCount = nicsDao.searchVlanOtherCount(zoneId);
				vlanFree = vlanAll - vlanOtherCount;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new Exception("searchVlanOtherFreeCount(int zoneId) error:"
					+ e.getMessage());
		}
		return vlanFree;
	}

	/**
	 * 为当前用户分配一个或多个VLAN
	 */
	@Override
	public List<Long> getAvailableNetworkByUserId(int userId,
			int vethAdaptorNum, int createNum, int resourceDomain, Integer resourcePoolID) {
		Long[] networks = new Long[vethAdaptorNum];
		try {
			// 优先获得状态为开机、关机或正在操作的VM
			List<TInstanceInfoBO> instanceInfos = this.getVMCreated(userId,
					resourceDomain);
			// 获得申请和正在开通或正在变更中的VM
			List<TInstanceInfoBO> applyVMs = this.getApplyingVM(userId,
					resourceDomain);
			if(instanceInfos!=null) {
				if(applyVMs!=null) {
					instanceInfos.addAll(applyVMs);
				}
			} else {
				instanceInfos = applyVMs;
			}
			boolean isFindFirNetwork = false;
			boolean isFindSecNetwork = false;
			boolean isFindThirNetwork = false;
			List<Long> notEnoughFirNetwork = null;
			List<Long> notEnoughSecNetwork = null;
			List<Long> notEnoughThirNetwork = null;
			int vmNumApplied = 0;
			if (instanceInfos != null && !instanceInfos.isEmpty()) {
				notEnoughFirNetwork = new ArrayList<Long>();
				notEnoughSecNetwork = new ArrayList<Long>();
				notEnoughThirNetwork = new ArrayList<Long>();
				for (TInstanceInfoBO instanceInfo : instanceInfos) {
					List<TNicsBO> nicses = searchNicssByInstanceId(instanceInfo
							.getId());
					if (nicses != null && !nicses.isEmpty()) {
						if (vethAdaptorNum == 1) {
							// 请求单网卡VM
							long networkId = nicses.get(0).geteVlanId();
							vmNumApplied = this.getVMNumApplied(networkId, resourceDomain);
							if (ipEnough(networkId, vmNumApplied, createNum,
									resourceDomain, resourcePoolID)) {
								networks[0] = networkId;
								isFindFirNetwork = true;// 单网卡虚拟机VLAN已经找到
								break;
							} else {
								notEnoughFirNetwork.add(networkId);
								continue;// 继续搜索下一下单网卡VM
							}
						} else if (vethAdaptorNum == 2) {
							// 请求多网卡VM
							if(nicses.size() != 2) {
								continue;// 继续搜索下一个双网卡VM
							} else {
								long firNetworkId = nicses.get(0).geteVlanId();
								long secNetworkId = nicses.get(1).geteVlanId();

								vmNumApplied = this.getVMNumApplied(firNetworkId, resourceDomain);
								// 检查第一块网卡VLAN地址是否足够
								if (ipEnough(firNetworkId, vmNumApplied,
										createNum, resourceDomain, resourcePoolID)) {
									networks[0] = firNetworkId;
									isFindFirNetwork = true;// 双网卡虚拟机第一个网卡已经找到
								} else {
									notEnoughFirNetwork.add(firNetworkId);
								}

								vmNumApplied = this.getVMNumApplied(secNetworkId, resourceDomain);
								// 检查第二块网卡VLAN地址是否足够
								if (ipEnough(secNetworkId, vmNumApplied,
										createNum, resourceDomain, resourcePoolID)) {
									networks[1] = secNetworkId;
									isFindSecNetwork = true;// 双网卡虚拟机第二个网卡已经找到
								} else {
									notEnoughSecNetwork.add(secNetworkId);
								}
								if (isFindFirNetwork && isFindSecNetwork) {
									break;
								} else {
									continue;// 继续搜索下一个双网卡VM
								}
							}
						} else if (vethAdaptorNum == 3) {
							// 请求多网卡VM
							if(nicses.size() != 3) {
								continue;// 继续搜索下一个三网卡VM
							} else {
								long firNetworkId = nicses.get(0).geteVlanId();
								long secNetworkId = nicses.get(1).geteVlanId();
								long thirNetworkId = nicses.get(2).geteVlanId();

								vmNumApplied = this.getVMNumApplied(firNetworkId, resourceDomain);
								// 检查第一块网卡VLAN地址是否足够
								if (ipEnough(firNetworkId, vmNumApplied,
										createNum, resourceDomain, resourcePoolID)) {
									networks[0] = firNetworkId;// 三网卡虚拟机第一个网卡已经找到
									isFindFirNetwork = true;
								} else {
									notEnoughFirNetwork.add(firNetworkId);
								}

								vmNumApplied = this.getVMNumApplied(secNetworkId, resourceDomain);
								// 检查第二块网卡VLAN地址是否足够
								if (ipEnough(secNetworkId, vmNumApplied,
										createNum, resourceDomain, resourcePoolID)) {
									networks[1] = secNetworkId;
									isFindSecNetwork = true;// 三网卡虚拟机第二个网卡已经找到
								} else {
									notEnoughSecNetwork.add(secNetworkId);
								}

								vmNumApplied = this.getVMNumApplied(thirNetworkId, resourceDomain);
								// 检查第三块网卡VLAN地址是否足够
								if (ipEnough(thirNetworkId, vmNumApplied,
										createNum, resourceDomain, resourcePoolID)) {
									networks[2] = thirNetworkId;
									isFindThirNetwork = true;// 三网卡虚拟机第三个网卡已经找到
								} else {
									notEnoughThirNetwork.add(thirNetworkId);
								}
								if (isFindFirNetwork && isFindSecNetwork
										&& isFindThirNetwork) {
									break;
								} else {
									continue;// 继续搜索下一个三网卡VM
								}

							}
						}
					} else {
						log.error("Does not find nic info by instance id "
								+ instanceInfo.getId());
					}
				}
			} else {
				log.info("User " + userId
						+ " didn't create virtual machine yet.");
			}
			// 该用户没有申请过VM或所有VM已经退订，从已分配列表中随机获得
			if (vethAdaptorNum == 1) {
				if (!isFindFirNetwork) {
					// 第一块网卡VLAN没有找到，从已分配列表中获得 //fix bug 3958
					long vlanId = this.getNetworkAssigned(userId, 1,
							notEnoughFirNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[0] = vlanId;
					} else {
						log.warn("Can not find free vlan of first nic for user "
								+ userId);
						return null;
					}
				}
			} else if (vethAdaptorNum == 2) {
				if (!isFindFirNetwork) {
					// fix bug 3958
					long vlanId = this.getNetworkAssigned(userId, 1,
							notEnoughFirNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[0] = vlanId;
					} else {
						log.warn("Can not find free vlan of first nic for user "
								+ userId);
						return null;
					}
				}
				if (!isFindSecNetwork) {
					long vlanId = this.getNetworkAssigned(userId, 2,
							notEnoughSecNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[1] = vlanId;
					} else {
						log.warn("Can not find free vlan of second nic for user "
								+ userId);
						return null;
					}
				}
			} else if (vethAdaptorNum == 3) {
				if (!isFindFirNetwork) {
					long vlanId = this.getNetworkAssigned(userId, 1,
							notEnoughFirNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[0] = vlanId;
					} else {
						log.warn("Can not find free vlan of first nic for user "
								+ userId);
						return null;
					}
				}
				if (!isFindSecNetwork) {
					long vlanId = this.getNetworkAssigned(userId, 2,
							notEnoughSecNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[1] = vlanId;
					} else {
						log.warn("Can not find free vlan of second nic for user "
								+ userId);
						return null;
					}
				}
				if (!isFindThirNetwork) {
					long vlanId = this.getNetworkAssigned(userId, 3,
							notEnoughThirNetwork, resourceDomain, createNum, resourcePoolID);
					if (vlanId > 0) {
						networks[2] = vlanId;
					} else {
						log.warn("Can not find free vlan of third nic for user "
								+ userId);
						return null;
					}
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
			log.error("Find free vlan for user " + userId + " error: " + e);
		}
		return checkNetwowrks(networks, resourcePoolID) ? Arrays.asList(networks) : null;
	}

	/**
	 * 获得已经创建的VM
	 *
	 * @param userId
	 * @param resourceDomain
	 * @return
	 */
	private List<TInstanceInfoBO> getVMCreated(int userId, int resourceDomain) {
		try {
			List<InstanceState> instanceStates = new ArrayList<InstanceState>();
			instanceStates.add(InstanceState.Running);
			instanceStates.add(InstanceState.Stopped);
			instanceStates.add(InstanceState.Operating);
			return instanceInfoDao.searchInstanceAppliedByUserId(userId,
					ServiceType.VM, instanceStates, resourceDomain);
		} catch (SCSException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获得正在创建的VM
	 *
	 * @param userId
	 * @param resourceDomain
	 * @return
	 */
	private List<TInstanceInfoBO> getApplyingVM(int userId, int resourceDomain) {
		try {
			List<InstanceState> instanceStates = new ArrayList<InstanceState>();
			instanceStates.add(InstanceState.Apply);
			instanceStates.add(InstanceState.ApplyInProcess);
			return instanceInfoDao.searchInstanceAppliedByUserId(userId,
					ServiceType.VM, instanceStates, resourceDomain);
		} catch (SCSException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 检查network是否有效，如果network无效则删除分配关系
	 *
	 * @param networks
	 * @return
	 */
	private boolean checkNetwowrks(Long[] networks,Integer resourcePoolID) {
		try {
			if (networks.length == 1) {
				if (isValid(networks[0], resourcePoolID)) {// 校验单网卡是否有效
					return true;
				} else {
					// 删除VLAN分配关系
					userVlanDao.deleteUserVlanByVlanId(networks[0]);
					log.error("Network " + networks[0]
							+ " has been deleted on Elaster");
				}
			} else if (networks.length == 2) {// 校验双网卡是否有效
				boolean firNicValid = isValid(networks[0], resourcePoolID);
				boolean secNicValid = isValid(networks[1], resourcePoolID);
				if (firNicValid && secNicValid) {
					return true;
				} else {
					if (!firNicValid) {
						// 删除网卡1VLAN分配关系
						userVlanDao.deleteUserVlanByVlanId(networks[0]);
						log.error("Network " + networks[0]
								+ " has been deleted on Elaster");
					}
					if (!secNicValid) {
						// 删除网卡2VLAN分配关系
						userVlanDao.deleteUserVlanByVlanId(networks[1]);
						log.error("Network " + networks[1]
								+ " has been deleted on Elaster");
					}
				}
			} else if (networks.length == 3) {// 校验三网卡是否有效
				boolean firNicValid = isValid(networks[0], resourcePoolID);
				boolean secNicValid = isValid(networks[1], resourcePoolID);
				boolean thirNicValid = isValid(networks[2], resourcePoolID);
				if (firNicValid && secNicValid && thirNicValid) {
					return true;
				} else {
					if (!firNicValid) {
						// 删除网卡1VLAN分配关系
						userVlanDao.deleteUserVlanByVlanId(networks[0]);
						log.error("Network " + networks[0]
								+ " has been deleted on Elaster");
					}
					if (!secNicValid) {
						// 删除网卡2VLAN分配关系
						userVlanDao.deleteUserVlanByVlanId(networks[1]);
						log.error("Network " + networks[1]
								+ " has been deleted on Elaster");
					}
					if (!thirNicValid) {
						// 删除网卡3VLAN分配关系
						userVlanDao.deleteUserVlanByVlanId(networks[2]);
						log.error("Network " + networks[2]
								+ " has been deleted on Elaster");
					}
				}
			}
		} catch (SCSException e) {
			e.printStackTrace();
			log.error("Check networks errror: " + e);
		}
		return false;
	}

	/**
	 * 检查network是否存在
	 *
	 * @param networkId
	 * @return
	 */
	private boolean isValid(long networkId,Integer resourcePoolID) {
		try {
			List<ENetwork> networks = cloudAPIService.listNetworks(networkId, resourcePoolID);
			if (networks != null && !networks.isEmpty()) {
				return true;
			}
		} catch (Exception e) {
			log.error("Call listNetworks api error: " + e);
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获得已分配的VLAN
	 *
	 * @param userId
	 * @param networkType
	 * @param filterNetwork
	 * @return
	 */
	private long getNetworkAssigned(int userId, int networkType,
			List<Long> filterNetworks, int resourceDomain, int createNum,Integer resourcePoolID) {
		try {
			TUserVlanBO userVlanParam = new TUserVlanBO();
			userVlanParam.setUserId(userId);
			userVlanParam.setType(networkType);// 获得企业私网
			userVlanParam.setZoneId(resourceDomain);
			List<TUserVlanBO> userVlans = userVlanDao
					.findUserVlan(userVlanParam);
			if (userVlans != null && !userVlans.isEmpty()) {
				for (TUserVlanBO userVlan : userVlans) {
					if (filterNetworks != null && !filterNetworks.isEmpty()) {
						if (!filterNetworks.contains(userVlan.getVlanId())) {
							if (ipEnough(userVlan.getVlanId(), 0, createNum,
									resourceDomain, resourcePoolID)) {
								return userVlan.getVlanId();
							}
						}
					} else {
						if (ipEnough(userVlan.getVlanId(), 0, createNum,
								resourceDomain, resourcePoolID)) {
							return userVlan.getVlanId();
						}
					}
					continue;
				}
			} else {
				log.info("Does not assign free vlan to user " + userId);
				return 0;
			}
		} catch (SCSException e) {
			e.printStackTrace();
			log.error("Find vlan assigned error: " + e);
		}
		log.warn("No available free vlan for user " + userId);
		return 0;
	}

	private int getVMNumApplied(long networkId, int resourceDomain) {
		try {
			List<TInstanceInfoBO> appliedVMs = instanceInfoDao.searchInstanceAppliedByNetworkId(networkId, resourceDomain);
			if(appliedVMs != null) {
				return appliedVMs.size();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 检查IP地址是否足够
	 *
	 * @param vlanId
	 * @param createdInstances
	 * @return
	 */
	private boolean ipEnough(long networkId, int createdNum, int createNum,
			int resourceDomain,Integer resourcePoolID) {
		// 调用Elaster得到VLAN的IP段
		try {
			List<Map<String, String>> ipRanges = cloudAPIService
					.listVlanIpRanges(networkId, resourceDomain, resourcePoolID);
			// 检查IP段是否足够
			int freeIPCount = 0;
			if (ipRanges != null && !ipRanges.isEmpty()) {
				for (Map<String, String> map : ipRanges) {
					String startip = map.get("startip");
					String endip = map.get("endip");
					int min = Integer.valueOf(startip.substring(startip
							.lastIndexOf(".") + 1));
					int max = Integer.valueOf(endip.substring(endip
							.lastIndexOf(".") + 1));
					freeIPCount = freeIPCount + (max - min) + 1;
				}
				// vRoute占一个VM
				if (freeIPCount > (createdNum + createNum)) {
					return true;
				}
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			log.error("Judge whether ip range is enough error: " + e);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Judge whether ip range is enough error: " + e);
		}
		return false;
	}

	@Override
	public List<listIpAddressesByNetWork> listIpAddressesByNetworkId(long networkId, Integer resourcePoolsId) throws Exception {
		List<TNicsBO> nicsList = this.searchNicssByNetworkId((int) networkId);
		Map<String, String> nicsMap = new HashMap<String, String>();
		for (TNicsBO nics : nicsList) {
			nicsMap.put(nics.getIp(), nics.getIp());
		}
		List<listIpAddressesByNetWork> ipAddressListNew = new ArrayList<listIpAddressesByNetWork>();
		List<listIpAddressesByNetWork> ipAddressList = cloudAPIService.listIpAddressesByNetWork(networkId, resourcePoolsId);
		if (ipAddressList != null) {
			for (listIpAddressesByNetWork ipAddress : ipAddressList) {
				String ip = ipAddress.getIpaddress();
				if (nicsMap.get(ip) == null) {
					ipAddressListNew.add(ipAddress);
				}
			}
		}

		return ipAddressListNew;
	}

	/**
	 *根据parameter参数表的Tag标签过滤网卡列表
	 * @param networkList
	 * @param networkType
	 * @return
	 * @throws Exception
	 */
	private List<ENetwork> filterNetworkListBynetworkType(List<ENetwork> networkList , int networkType) throws Exception {
		List<ENetwork> networkListReturn = new ArrayList<ENetwork>();
		String networkTagParas = null;
		if(networkType == 1){//第一块网卡
			networkTagParas = ConstDef.getFirstNetworkTag().trim();//to fix bug [7873]
		}else if(networkType == 2){//第二块网卡
			networkTagParas = ConstDef.getSecondNetworkTag().trim();
		}else if(networkType == 3){//第三块网卡
			networkTagParas = ConstDef.getThirdNetworkTag().trim();
		}else if(networkType == 4){//第四块网卡
			networkTagParas = ConstDef.getForthNetworkTag().trim();
		}
		if (networkTagParas != null  &&  !"".equals(networkTagParas)){
			for (ENetwork network:networkList){
				String netTags = network.getTags();
				if (netTags != null && !"".equals(netTags) && netTags.equals(networkTagParas)){
					networkListReturn.add(network);
				}
			}
		}else{//to fix bug [7618],7696
			networkListReturn = networkList;
		}
		
		return networkListReturn;
	}
	
	@Override
	public List<ENetwork> searchNetworkListDefault(int zoneId, Integer resourcePoolsId,int networkType) throws Exception {
		List<ENetwork> listNetworkNew = new ArrayList<ENetwork>();
		try {
			//获取vlan和账号关联关系
			//fix bug:7815
			TUserVlanBO userV = new TUserVlanBO();
			userV.setResourcePoolsId(resourcePoolsId);
			 List<TUserVlanBO>  userVlanList = userVlanDao.findAll(userV);
			 Map<String,String> mapVlan = new HashMap<String,String>();
			 for(TUserVlanBO userVlan : userVlanList){
				 mapVlan.put(String.valueOf(userVlan.getVlanId()), userVlan.getAccount());
			 }

			Map<String, List<EVlanIpRange>> ipvlanMap = cloudAPIService.getVlanIpRangesMap(resourcePoolsId);
			List<ENetwork> listNetworkOld = cloudAPIService.listDefaultNetworksByZoneId(zoneId, resourcePoolsId);
			for (ENetwork network : listNetworkOld) {
				if (network.getState() != null && network.getStartip() != null && !"Allocated".equals(network.getState())) {
					List<EVlanIpRange> listVlan = ipvlanMap.get(String.valueOf(network.getId()));
					int ipFreeNum = 0;
					if (listVlan != null) {
						// 剩余ip数量
						long networkId = network.getId();
						List<listIpAddressesByNetWork> listIpAddresses = this.listIpAddressesByNetworkId(networkId, resourcePoolsId);
						if (listIpAddresses != null) {
							ipFreeNum = listIpAddresses.size();
						}
						List<TNicsBO> listNicses = this.searchNicssByNetworkIdAndIp((int) networkId, "0");
						if (listNicses != null) {
							ipFreeNum = ipFreeNum - listNicses.size();
						}
						// ip总数
						int ipTotal = cloudAPIService.ipTotal4VlanIpRange(listVlan);
						network.setIpTotal(ipTotal);
						network.setIpFreeNum(ipFreeNum);
						network.setListVlanIpRange(listVlan);

						//通过vlan获取被分配的账号
						String account = mapVlan.get(String.valueOf(networkId));
						if (account!=null){
							network.setAccount(account);
						}

					}
					if (ipFreeNum >= 1) {// 剩余ip数量不为0的Vlan才显示
						listNetworkNew.add(network);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		//根据parameter参数表的Tag标签过滤网卡列表
		listNetworkNew = filterNetworkListBynetworkType(listNetworkNew,networkType);
		
		return listNetworkNew;
	}

	@Override
	public List<ENetwork> searchNetworkListOther(int zoneId, Integer resourcePoolsId,int networkType) throws Exception {
		List<ENetwork> listNetworkNew = new ArrayList<ENetwork>();
		try {
			//获取vlan和账号关联关系
			//fix bug:7815
			TUserVlanBO userV = new TUserVlanBO();
			userV.setResourcePoolsId(resourcePoolsId);
			 List<TUserVlanBO>  userVlanList = userVlanDao.findAll(userV);
			 Map<String,String> mapVlan = new HashMap<String,String>();
			 for(TUserVlanBO userVlan : userVlanList){
				 mapVlan.put(String.valueOf(userVlan.getVlanId()), userVlan.getAccount());
			 }

			Map<String, List<EVlanIpRange>> ipvlanMap = cloudAPIService.getVlanIpRangesMap(resourcePoolsId);
			List<ENetwork> listNetworkOld = cloudAPIService.listOtherNetworksByZoneId(zoneId, resourcePoolsId);
			for (ENetwork network : listNetworkOld) {
				if (network.getState() != null && network.getStartip() != null && !"Allocated".equals(network.getState())) {
					List<EVlanIpRange> listVlan = ipvlanMap.get(String.valueOf(network.getId()));
					int ipFreeNum = 0;
					if (listVlan != null) {
						// 剩余ip数量
						long networkId = network.getId();
						List<listIpAddressesByNetWork> listIpAddresses = this.listIpAddressesByNetworkId(networkId, resourcePoolsId);
						if (listIpAddresses != null) {
							ipFreeNum = listIpAddresses.size();
						}
						List<TNicsBO> listNicses = this.searchNicssByNetworkIdAndIp((int) networkId, "0");
						if (listNicses != null) {
							ipFreeNum = ipFreeNum - listNicses.size();
						}
						// ip总数
						int ipTotal = cloudAPIService.ipTotal4VlanIpRange(listVlan);
						network.setIpTotal(ipTotal);
						network.setIpFreeNum(ipFreeNum);
						network.setListVlanIpRange(listVlan);

						//通过vlan获取被分配的账号
						String account = mapVlan.get(String.valueOf(networkId));
						if (account!=null){
							network.setAccount(account);
						}

					}
					if (ipFreeNum >= 1) {// 剩余ip数量不为0的Vlan才显示
						listNetworkNew.add(network);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		
		//根据parameter参数表的Tag标签过滤网卡列表
		listNetworkNew = filterNetworkListBynetworkType(listNetworkNew,networkType);
		return listNetworkNew;
	}

	@Override
	public List<TNicsBO> searchNicssByNetworkId(int networkId) throws SQLException {
		// TODO Auto-generated method stub
		return nicsDao.searchNicssByNetworkId(networkId);
	}

	@Override
	public List<TNicsBO> searchNicssByNetworkIdAndIp(int networkId, String ipaddress) throws SQLException {
		// TODO Auto-generated method stub
		return nicsDao.searchNicssByNetworkIdAndIp(networkId, ipaddress);
	}

	@Override
	public List<TNicsBO> searchNicsDhcpByOrderId(int orderId) throws SQLException {
		// TODO Auto-generated method stub
		return nicsDao.searchNicsDhcpByOrderId(orderId);
	}

	@Override
    public List<TNicsBO> searchNicsRepeatVlanCountByorderId(int orderId) throws SQLException {
	    // TODO Auto-generated method stub
	    return nicsDao.searchNicsRepeatVlanCountByorderId(orderId);
    }

	@Override
    public List<TNicsBO> searchNicsRepeatIPCountByorderId(int orderId) throws SQLException {
		List<TNicsBO> ret_nicsList = new ArrayList<TNicsBO>();
		//查询所有有重复ip的网卡信息
	    List<TNicsBO> nicsList =  nicsDao.searchNicsRepeatIP();
	    if(nicsList!=null && nicsList.size()>=1){
	    	Map <String,TNicsBO> mapList = new HashMap<String,TNicsBO>();
	    	for (TNicsBO nics:nicsList){
	    		mapList.put(nics.getIp(),nics);
    		}
	    	//查询订单的网卡
	    	List<TNicsBO> orderNicsList  = nicsDao.searchNicsRepeatVlanCountByorderId(orderId);
	    	if (orderNicsList!=null && orderNicsList.size()>=1){
	    		for (TNicsBO orderNics:orderNicsList){
	    			String ip = orderNics.getIp();
	    			 if (mapList.get(ip)!=null){
	    				 orderNics.setIpCount(mapList.get(ip).getIpCount());
	    				 ret_nicsList.add(orderNics);
	    			 }
	    		}
	    	}
	    }
	    return ret_nicsList;
    }




}
