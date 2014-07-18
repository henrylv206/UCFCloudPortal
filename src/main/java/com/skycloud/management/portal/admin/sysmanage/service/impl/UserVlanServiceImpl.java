package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.sysmanage.dao.IUserVlanDao;
import com.skycloud.management.portal.admin.sysmanage.dao.impl.PermissionDaoImpl;
import com.skycloud.management.portal.admin.sysmanage.entity.TMenuBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserVlanService;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EVlanIpRange;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.network.h3c.dao.IHLJVpnInstanceDao;
import com.skycloud.management.portal.front.network.h3c.entity.HLJVpnInstance;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.resources.enumtype.InstanceState;
import com.skycloud.management.portal.front.resources.enumtype.MenuType;
import com.skycloud.management.portal.front.resources.enumtype.ServiceType;


public class UserVlanServiceImpl implements IUserVlanService{
	private final String ERROR_MESSAGE_getResourcePools = "查询资源池失败。失败原因：%s";

	private IUserVlanDao userVlanDao;
	private INicsDao nicsDao;
	private ICloudAPISerivce cloudAPIService;
	private IHLJVpnInstanceDao HLJVpnInstanceDao;
	private IInstanceInfoDao instanceInfoDao;
	private PermissionDaoImpl perDao;

	@Override
	public int save(TUserVlanBO userVlan) throws SCSException {
		int ret_val = 0;
		try {
			ret_val = userVlanDao.save(userVlan);
			//判断是否启用公网IP
			int menuIp = this.checkMenuPublicNetworkIp();
			if (menuIp==0){
				return ret_val;
			}
	        int userId = userVlan.getUserId();
	        int networkId = (int)userVlan.getVlanId();
	        if (userVlan.getType()==1){//企业私网需要绑定vpn
	        	HLJVpnInstance vpnInstance = HLJVpnInstanceDao.getInstanceByUserId(userId);
		        if(vpnInstance!=null ){
		        	vpnInstance.getVpnId();
		        	ret_val = -1;
		        	System.out.println("给管理员提示信息，通知管理员要去H3C设备上进行手动配置，把本次要分配给用户的vlan关联到用户已经占用的vpn实例上");
	        	}else{
	        		int resourcePoolsId = userVlan.getResourcePoolsId();
	        		 List<ENetwork> networkList = cloudAPIService.listNetworks(networkId,resourcePoolsId);
	        		 if(networkList!=null && networkList.size()>0){
		        			int vlanId =networkList.get(0).getVlan();
		        			HLJVpnInstance vpnInstance4vlan = HLJVpnInstanceDao.getInstanceByUserVlanId(vlanId);
		             		if (vpnInstance4vlan!=null){
		             			vpnInstance4vlan.setUserId(userId);
		                 		HLJVpnInstanceDao.updateInstance(vpnInstance4vlan);
		             		}else{
		             			ret_val = -2;
		             			System.out.println("VLAN未绑定VPN实例:接口出错 HLJVpnInstanceDao.getInstanceByUserVlanId(vlanId)，vlanId="+vlanId);
		             		}
	        		 }
	        	}
	        }

        }
        catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
        return ret_val;
	}

	@Override
	public int update(TUserVlanBO userVlan) throws SCSException {
		return userVlanDao.update(userVlan);
	}

	@Override
	public int delete(int id) throws Exception {
		int ret_val = -1;
		//判断是否启用公网IP
		int menuIp = this.checkMenuPublicNetworkIp();
		if (menuIp==0){
			return userVlanDao.delete(id);
		}
		//找到当前的分配关系
		TUserVlanBO userVlan = this.findById(id);
		//to fix bug:4269
		if(userVlan.getType() >= 2) {
			return userVlanDao.delete(id);
		}
		TUserVlanBO newUserVlan = new TUserVlanBO();
		newUserVlan.setUserId(userVlan.getUserId());
		newUserVlan.setType(1);
//		不用考虑zone
//		newUserVlan.setZoneId(userVlan.getZoneId());
		//根据当前用户查找该用户有几个VLAN
		List<TUserVlanBO> userVlans = this.findUserVlan(newUserVlan);
		if(userVlans!=null && !userVlans.isEmpty()) {
			//只有一个VLAN
			if(userVlans.size() == 1) {
				//检查负载均衡
				int lbNum = checkInstanceInfoLB(userVlan);
				if (lbNum>=1){
					//UI提示管理员由于用户已经申请了负载均衡，不能删除VLAN分配
					ret_val = 6;
					return ret_val;
				}
				//检查防火墙
				int fwNum = checkInstanceInfoFW(userVlan);
				if (fwNum>=1){
					//UI提示管理员由于用户已经申请了防火墙，不能删除VLAN分配
					ret_val = 7;
					return ret_val;
				}

				HLJVpnInstance vpnInstance = HLJVpnInstanceDao.getInstanceByUserId(userVlan.getUserId());
				if(vpnInstance!=null){
					//解除用户与vpn绑定关系成功
					vpnInstance.setUserId(0);
					HLJVpnInstanceDao.updateInstance(vpnInstance);
					ret_val = 8;
				}else{
					//解除用户与vpn绑定关系失败
					ret_val = 9;
				}

			} else {
				TUserVlanBO firVlanMapping = userVlans.get(userVlans.size()-1);//获得最初创建的VLAN1
				TUserVlanBO secVlanMapping = userVlans.get(userVlans.size()-2);//获得VLAN2
				//用户有多个VLAN
				if(id == firVlanMapping.getId()) {//判断是否删除VLAN1(主vlan)
					//检查负载均衡
					int lbNum = checkInstanceInfoLB(userVlan);
					if (lbNum>=1){
						//UI提示管理员由于用户已经申请了负载均衡，不能删除主vlan
						ret_val = 6;
						return ret_val;
					}
					//获取vpn实例对象
					HLJVpnInstance firVpnInstance = null;
					HLJVpnInstance secVpnInstance = null;
					int networkId1 = (int)firVlanMapping.getVlanId();
					List<ENetwork> networkList1 = cloudAPIService.listNetworks(networkId1, userVlan.getResourcePoolsId());
	        		 if(networkList1!=null && networkList1.size()>0){
		        			int vlanId1 =networkList1.get(0).getVlan();
		        			firVpnInstance = HLJVpnInstanceDao.getInstanceByUserVlanId(vlanId1);
	        		 }
	        		 int networkId2 = (int)secVlanMapping.getVlanId();
						List<ENetwork> networkList2 = cloudAPIService.listNetworks(networkId2, userVlan.getResourcePoolsId());
		        		 if(networkList1!=null && networkList1.size()>0){
			        			int vlanId2 =networkList2.get(0).getVlan();
			        			secVpnInstance = HLJVpnInstanceDao.getInstanceByUserVlanId(vlanId2);
		        		 }
					if (firVpnInstance!=null && secVpnInstance!=null){
						//获取vpn实例成员变量
						String[] vlan1 = firVpnInstance.getUserVlanId();
						String[] getway1 = firVpnInstance.getUserGateway();
						String linkVlanIpLb1 = firVpnInstance.getLinkVlanViIpLb();

						String[] vlan2 = secVpnInstance.getUserVlanId();
						String[] getway2 = secVpnInstance.getUserGateway();
						String linkVlanIpLb2 = secVpnInstance.getLinkVlanViIpLb();

						//交换vpn实例成员变量
						firVpnInstance.setUserVlanId(vlan2);
						firVpnInstance.setUserGateway(getway2);
						firVpnInstance.setLinkVlanViIpLb(linkVlanIpLb2);

						secVpnInstance.setUserVlanId(vlan1);
						secVpnInstance.setUserGateway(getway1);
						secVpnInstance.setLinkVlanViIpLb(linkVlanIpLb1);
						//更新表数据
						HLJVpnInstanceDao.updateInstance(firVpnInstance);
						HLJVpnInstanceDao.updateInstance(secVpnInstance);
						//提示管理员到H3C设备上手动操作
						ret_val = 10;
					} else {
						//VPN与VLAN对换失败
						ret_val = 9;
					}
				} else {
					//用户删除VLAN2
					//提示管理员手动操作
					ret_val = 11;
				}
			}

			int delnum = userVlanDao.delete(id);
			if (delnum <= 0) {
				ret_val = delnum;
			}
		}
		return ret_val;
	}

	private int checkInstanceInfoLB(TUserVlanBO userVlan){
		int ret_val = 0;
		try {
	        List<InstanceState> instateStates = new ArrayList<InstanceState>();
	        instateStates.add(InstanceState.Apply);
	        instateStates.add(InstanceState.ApplyInProcess);
	        instateStates.add(InstanceState.Running);
	        instateStates.add(InstanceState.Operating);
	        List<TInstanceInfoBO> instanceInfos = instanceInfoDao.searchInstanceInfoByUserId(userVlan.getUserId(), ServiceType.LB, instateStates, 0);
	        if(instanceInfos!=null&&!instanceInfos.isEmpty()) {
	        	//UI提示管理员由于用户已经申请了负载均衡，不能删除VLAN分配
	        	ret_val = instanceInfos.size();

	        }
        }
        catch (SCSException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return ret_val;
	}

	private int checkInstanceInfoFW(TUserVlanBO userVlan){
		int ret_val = 0;
		try {
	        List<InstanceState> instateStates = new ArrayList<InstanceState>();
	        instateStates.add(InstanceState.Apply);
	        instateStates.add(InstanceState.ApplyInProcess);
	        instateStates.add(InstanceState.Running);
	        instateStates.add(InstanceState.Operating);
	        List<TInstanceInfoBO> instanceInfos = instanceInfoDao.searchInstanceInfoByUserId(userVlan.getUserId(), ServiceType.FW, instateStates, 0);
	    	if(instanceInfos!=null&&!instanceInfos.isEmpty()) {
	    		//UI提示管理员由于用户已经申请了防火墙，不能删除VLAN分配
	    		ret_val = instanceInfos.size();
	    	}
        }
        catch (SCSException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return ret_val;
	}



	@Override
    public int checkbindVpnInstance(int userId, int vlanId) throws Exception {
		int ret_val = 0;
        	HLJVpnInstance vpnInstance = HLJVpnInstanceDao.getInstanceByUserId(userId);
        	int networkId = vlanId;
	        if(vpnInstance!=null ){
	        	int resourcePoolsId = 1;
	        	TUserVlanBO userVlanVO = new TUserVlanBO();
		        	userVlanVO.setUserId(userId);
		        	userVlanVO.setVlanId(vlanId);
		        	List<TUserVlanBO> userVlanList = userVlanDao.findUserVlan(userVlanVO);
		        	if (userVlanList!=null && !userVlanList.isEmpty()){
		        		resourcePoolsId = userVlanList.get(0).getResourcePoolsId();
		        	}
        		 List<ENetwork> networkList = cloudAPIService.listNetworks(networkId,resourcePoolsId);
        		 if(networkList!=null && networkList.size()>0){
	        			int myvlanId =networkList.get(0).getVlan();
	        			HLJVpnInstance vpnInstance4vlan = HLJVpnInstanceDao.getInstanceByUserVlanId(myvlanId);
	             		if (vpnInstance4vlan!=null){
	             			int myuserid = vpnInstance4vlan.getUserId();
	                 		if (userId == myuserid){
	                 			ret_val = 1;
	                 		}
	             		}
        		 }
        	}
	    return ret_val;
    }

	private void unbindVpnInstance(int userId) {
		try {

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
    public int deleteUserVlanByVlanId(long vlanId) throws SCSException {
	    // TODO Auto-generated method stub
	    return 0;
    }


	private List<Integer> findResourcePoolsList(List<TUserVlanBO> userVlanList) {
		List<Integer> resourcePoolsIdList = new ArrayList<Integer>();
		for (TUserVlanBO userVlan :userVlanList){
			if (!resourcePoolsIdList.contains(userVlan.getResourcePoolsId())){
				resourcePoolsIdList.add(userVlan.getResourcePoolsId());
			}
		}
		return resourcePoolsIdList;
	}

	private  List<EZone> findZoneList(List<Integer> resourcePoolsIdList) throws SCSException{
		List<EZone> zoneList = new ArrayList<EZone>();
		for (Integer resourcePoolsId :resourcePoolsIdList){
			 try {
				 List<EZone> zoneList4resPool = cloudAPIService.listZones(0,resourcePoolsId);
				 zoneList.addAll(zoneList4resPool);
            }
            catch (Exception e) {
	            e.printStackTrace();
            }
		}
		return zoneList;
	}

	private  List<ENetwork> findNetworkList(List<Integer> resourcePoolsIdList) throws SCSException{
		List<ENetwork> networkList = new ArrayList<ENetwork>();
		for (Integer resourcePoolsId :resourcePoolsIdList){
			 try {
				 List<ENetwork> networkList4resPool = cloudAPIService.listNetworks(0,resourcePoolsId);
				 networkList.addAll(networkList4resPool);
            }
            catch (Exception e) {
	            e.printStackTrace();
            }
		}
		return networkList;
	}

	@Override
	public List<TUserVlanBO> findAll(TUserVlanBO userV) throws SCSException {
		 List<TUserVlanBO> userVlanList = userVlanDao.findAll(userV);
		 List<EZone> zoneList;
        List<ENetwork> networkList;
        try {

//			     zoneList = cloudAPIService.listZones(0);
//			     networkList = cloudAPIService.listNetworks(0);

			     List<Integer> resPoolsList = findResourcePoolsList(userVlanList);
			     zoneList = findZoneList(resPoolsList);
			     networkList = findNetworkList(resPoolsList);

				 Map<String,String> zoneMap = new HashMap<String,String>();
				 Map<String,String> networkMap = new HashMap<String,String>();
				 for(EZone zone:zoneList){
					 zoneMap.put(String.valueOf(zone.getId()), zone.getName());
				 }
				 for(ENetwork network:networkList){
					 networkMap.put(String.valueOf(network.getId()), String.valueOf(network.getVlan()));
				 }

				 for(TUserVlanBO userVlan:userVlanList){
					 String zoneName = zoneMap.get(String.valueOf(userVlan.getZoneId()));
					 String vlanName = networkMap.get(String.valueOf(userVlan.getVlanId()));
					 userVlan.setVlanName(vlanName);
					 userVlan.setZoneName(zoneName);
				 }
        }
        catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		 return userVlanList;
	}

	@Override
	public TUserVlanBO findById(int id) throws SCSException {
		TUserVlanBO userVlan = userVlanDao.findById(id);
		List<EZone> zoneList;
        List<ENetwork> networkList;
        List<Map<String, String>> ipRanges;
        try {
        	zoneList = cloudAPIService.listZones(0, userVlan.getResourcePoolsId());
		    networkList = cloudAPIService.listNetworks(0, userVlan.getResourcePoolsId());

			Map<String,String> zoneMap = new HashMap<String,String>();
			Map<String,ENetwork> networkMap = new HashMap<String,ENetwork>();

			for(EZone zone:zoneList){
				zoneMap.put(String.valueOf(zone.getId()), zone.getName());
			}
			for(ENetwork network:networkList){
				networkMap.put(String.valueOf(network.getId()), network);
				ipRanges = cloudAPIService.listVlanIpRanges(network.getId(), network.getZoneid(), userVlan.getResourcePoolsId());
				this.setVlanIpRange(network, ipRanges);
			}

			String zoneName = zoneMap.get(String.valueOf(userVlan.getZoneId()));
			ENetwork network = networkMap.get(String.valueOf(userVlan.getVlanId()));
			userVlan.setVlanName(network!=null?String.valueOf(network.getVlan()):"N/A");
			userVlan.setZoneName(zoneName);
			if (network!=null){
				userVlan.setIpRanges(network.getDisplayIpRanges());
			}
        }
        catch (Exception e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
        }
		return userVlan;
	}

	private void setVlanIpRange(ENetwork network, List<Map<String, String>> ipRangeLists) {
		String displayVlanIpRange = "";
		if(ipRangeLists!=null&&!ipRangeLists.isEmpty()) {
			for(Map<String, String> ipRangeList: ipRangeLists) {
				displayVlanIpRange = displayVlanIpRange + ipRangeList.get("startip") + " - " + ipRangeList.get("endip") + " , ";
			}
			displayVlanIpRange = displayVlanIpRange.substring(0, displayVlanIpRange.lastIndexOf(','));
			network.setDisplayIpRanges(displayVlanIpRange);
		}
	}

	// 列出Network列表
	@Override
    public List<ENetwork> listNetworksIpRangeByZoneId(int zoneId,boolean isdefault,int resourcePoolsId) throws Exception  {
		try {
			Map<String, List<EVlanIpRange>> ipvlanMap = cloudAPIService.getVlanIpRangesMap(resourcePoolsId);
			List<ENetwork> listNetworkNew = new ArrayList<ENetwork>();
			List<ENetwork> listNetworkOld = cloudAPIService.listNetworksByZoneId(zoneId,isdefault,resourcePoolsId);
			for (ENetwork network : listNetworkOld) {
				if (network.getState() != null && network.getStartip() != null && !"Allocated".equals(network.getState())) {
					 List<EVlanIpRange> listVlan = ipvlanMap.get(String.valueOf(network.getId()));
					 if (listVlan!=null){
						 network.setListVlanIpRange(listVlan);
					 }
					listNetworkNew.add(network);
				}
			}
			return listNetworkNew;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//根据网络类型 和tag标签过滤网卡信息
	@Override
	public List<ENetwork> listNetworksByTypeTag(List<ENetwork> networkList,int type)throws Exception  {
		List<ENetwork> listValues = new ArrayList<ENetwork>();
		String paraTags = null;
		if (type == 1){
			paraTags = ConstDef.getFirstNetworkTag();
		}else if (type == 2){
			paraTags = ConstDef.getSecondNetworkTag();
		}else if (type == 3){
			paraTags = ConstDef.getThirdNetworkTag();
		}
		if (paraTags!=null && !"".equals(paraTags)){
			for (ENetwork network : networkList) {
				String mytags = network.getTags();
				if (mytags!=null && !"".equals(mytags) && paraTags.equals(mytags)){
					listValues.add(network);
				}
			}
		}else{
			listValues = networkList;
		}

		return listValues;
	}

	//检查菜单是否有公网IP
	@Override
	public int checkMenuPublicNetworkIp(){
        int netMenuNum = 0;
		try {
			//parent_menu_id=13:服务模板管理
			List<TMenuBO> list = perDao.queryAllMenus(13);
			if(null!=list&&!list.isEmpty()){
				for(TMenuBO menu : list){
					int menuId = menu.getMenuId();
					//to fix bug:4989 修改公网ip模板菜单ID
					if (menuId==MenuType.TEMPLATE_PUBLICNETWORKIP_PRODUCT.getValue()){
						netMenuNum = 1;
						break;
					}
				}
            }
		} catch (Exception e) {
			e.printStackTrace();
		}
		return netMenuNum;
	}

	@Override
	public List<TUserVlanBO> findUserVlan(TUserVlanBO userVlan) throws SCSException {
		// TODO Auto-generated method stub
		return userVlanDao.findUserVlan(userVlan);
	}

	@Override
    public List<TUserVlanBO> findUserZone(TUserVlanBO userVlan) throws SCSException {
		// TODO Auto-generated method stub
		return userVlanDao.findUserZone(userVlan);
	}

    @Override
    public int searchNicssCountByvlanId(int vlanId) throws SQLException {
	    return nicsDao.searchNicssCountByvlanId(vlanId);
    }

    @Override
    public List<TResourcePoolsBO> listAllResourcePools() throws SQLException {
    	try {
    		return  cloudAPIService.listAllResourcePools();
		}
		catch (Exception e) {
			throw new SQLException(String.format(ERROR_MESSAGE_getResourcePools, e.getMessage()));
		}
    }


	@Override
    public String getResourcePoolNameById(int resourcePoolsId) throws SQLException {
		String return_value = "";
		try {
			return_value = cloudAPIService.getResourcePoolNameById(resourcePoolsId);
		}
		catch (Exception e) {
			throw new SQLException(String.format(ERROR_MESSAGE_getResourcePools, e.getMessage()));
		}
		return return_value;
    }

	public IUserVlanDao getUserVlanDao() {
    	return userVlanDao;
    }


    public void setUserVlanDao(IUserVlanDao userVlanDao) {
    	this.userVlanDao = userVlanDao;
    }


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


    public IHLJVpnInstanceDao getHLJVpnInstanceDao() {
    	return HLJVpnInstanceDao;
    }


    public void setHLJVpnInstanceDao(IHLJVpnInstanceDao hLJVpnInstanceDao) {
    	HLJVpnInstanceDao = hLJVpnInstanceDao;
    }

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}


    public PermissionDaoImpl getPerDao() {
    	return perDao;
    }


    public void setPerDao(PermissionDaoImpl perDao) {
    	this.perDao = perDao;
    }

}
