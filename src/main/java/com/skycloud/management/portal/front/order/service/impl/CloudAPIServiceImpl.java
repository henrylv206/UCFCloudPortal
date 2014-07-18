package com.skycloud.management.portal.front.order.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.res.ECluster;
import com.skycloud.management.portal.front.command.res.EListConfigurations;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EOsCategory;
import com.skycloud.management.portal.front.command.res.EOsType;
import com.skycloud.management.portal.front.command.res.EStoragePool;
import com.skycloud.management.portal.front.command.res.EVlanIpRange;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.command.res.ListHosts;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.command.res.ListVlanIpRanges;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;
import com.skycloud.management.portal.front.instance.service.impl.CommandServiceImpl;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;

public class CloudAPIServiceImpl implements ICloudAPISerivce {

	private final String ERROR_MESSAGE_getResourcePools = "查询资源池失败。失败原因：%s";

	private final String ERROR_MESSAGE_listNetworksId = "查询Network失败。失败原因：%s";

	private final String ERROR_MESSAGE_listNetworksZoneId = "查询Network失败。失败原因：%s";

	private final String ERROR_MESSAGE_listZones = "查询Zones失败。失败原因：%s";

	private final String ERROR_MESSAGE_listCluster = "查询Cluster失败。失败原因：%s";

	private final String ERROR_MESSAGE_listStoragePool = "查询StoragePool失败。失败原因：%s";

	private final String ERROR_MESSAGE_listOsCategory = "查询OsCategory失败。失败原因：%s";

	private final String ERROR_MESSAGE_listOsType = "查询OsType失败。失败原因：%s";

	private final String ERROR_MESSAGE_listVlan = "查询Vlane失败。失败原因：%s";

	private final String ERROR_MESSAGE_listConfigurations = "查询listConfigurations失败。失败原因：%s";

	private final String ERROR_MESSAGE_listIpAddressesByNetWork = "查询listIpAddressesByNetWork失败。失败原因：%s";

	private final String ERROR_MESSAGE_listHosts = "查询listHosts失败。失败原因：%s";

	private final String ERROR_MESSAGE_listTemplates = "查询listTemplates失败。失败原因：%s";

	CommandServiceImpl commandService;

	IResourcePoolsDao resourcePoolsDao;

	@Override
	public List<EVlanIpRange> listVlan(long id, int zoneid, int networkid,Integer resourcePoolId) throws Exception {
		List<EVlanIpRange> listVlans = new ArrayList<EVlanIpRange>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listVlans;
			}
			EVlanIpRange vlanIpRange = new EVlanIpRange();
			if (id != 0) {
				vlanIpRange.setId(id);
			}
			if (zoneid != 0) {
				vlanIpRange.setZoneid(String.valueOf(zoneid));
			}
			if (networkid != 0) {
				vlanIpRange.setNetworkid(String.valueOf(networkid));
			}
			Object getObjJson = commandService.executeAndJsonReturn(vlanIpRange, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> vlanMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object listvlaniprangesresponse = vlanMap.get("listvlaniprangesresponse");
				if (listvlaniprangesresponse != null) {
					Map<String, Object> vlanMap2 = JsonUtil.getMap4Json(listvlaniprangesresponse.toString());
					Object vlaniprange = vlanMap2.get("vlaniprange");
					if (vlaniprange != null) {
						listVlans = JsonUtil.getList4Json(vlaniprange.toString(), EVlanIpRange.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listVlan, e.getMessage()));
		}
		return listVlans;
	}

	@Override
	public List<EOsType> listOsType(long id,Integer resourcePoolId) throws Exception {
		List<EOsType> listValues = new ArrayList<EOsType>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			EOsType newObj = new EOsType();
			if (id != 0) {
				newObj.setId(id);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listostypesresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("ostype");
					if (contentObj != null) {
						listValues = JsonUtil.getList4Json(contentObj.toString(), EOsType.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listOsType, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<EOsCategory> listOsCategory(long id,Integer resourcePoolId) throws Exception {
		List<EOsCategory> listValues = new ArrayList<EOsCategory>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			EOsCategory newObj = new EOsCategory();
			if (id != 0) {
				newObj.setId(id);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listoscategoriesresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("oscategory");
					if (contentObj != null) {
						listValues = JsonUtil.getList4Json(contentObj.toString(), EOsCategory.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listOsCategory, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<EStoragePool> listStoragePool(long id,Integer resourcePoolId) throws Exception {
		List<EStoragePool> listValues = new ArrayList<EStoragePool>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			EStoragePool newObj = new EStoragePool();
			if (id != 0) {
				newObj.setId(id);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("liststoragepoolsresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("storagepool");
					if (contentObj != null) {
						listValues = JsonUtil.getList4Json(contentObj.toString(), EStoragePool.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listStoragePool, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<ECluster> listCluster(long id, int hypervisorIsBareMetal,Integer resourcePoolId) throws Exception {
		List<ECluster> listValues = new ArrayList<ECluster>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			ECluster newObj = new ECluster();
			if (id != 0) {
				newObj.setId(id);
			}
			if (hypervisorIsBareMetal == 1) {
				newObj.setHypervisortype("BareMetal");
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listclustersresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("cluster");
					if (contentObj != null) {
						listValues = JsonUtil.getList4Json(contentObj.toString(), ECluster.class);
					}
				}
			}

		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listCluster, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<EZone> listZones(long id,Integer resourcePoolId) throws Exception {
		List<EZone> listValues = new ArrayList<EZone>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			if(resourcePoolId < 1){
				return listValues;
			}
			EZone newObj = new EZone();
			if (id != 0) {
				newObj.setId(id);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				if(responseMap.containsKey("listzonesresponse")){
					Object responseObj = responseMap.get("listzonesresponse");
					if (responseObj != null) {
						Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
						if(contentMap.containsKey("zone")){
							Object contentObj = contentMap.get("zone");
							if (contentObj != null) {
								listValues = JsonUtil.getList4Json(contentObj.toString(), EZone.class);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listZones, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<ENetwork> listNetworks(long id,Integer resourcePoolId) throws Exception {
		List<ENetwork> listValues = new ArrayList<ENetwork>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			ENetwork newObj = new ENetwork();
			if (id != 0) {
				newObj.setId(id);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listnetworksresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("network");
					if (contentObj != null) {
						Object[] objlist = JsonUtil.getObjectArray4Json(contentObj.toString());
						int size = objlist.length;
						if (size != 0) {
							for (Object obj : objlist) {
								Map<String, String> objMap = JsonUtil.getMap4Json(obj.toString());
								ENetwork network = new ENetwork();
								long netid = Long.parseLong(String.valueOf(objMap.get("id")));
								network.setId(netid);
								network.setStartip(objMap.get("startip"));
								network.setEndip(objMap.get("endip"));
								network.setIsdefault(Boolean.parseBoolean(String.valueOf(objMap.get("isdefault"))));
								network.setName(objMap.get("name"));
								network.setState(objMap.get("state"));
								network.setZoneid(Integer.valueOf(String.valueOf(objMap.get("zoneid"))));
								if(objMap.get("vlan")!=null){
									network.setVlan(Integer.valueOf(objMap.get("vlan")));
								}
								listValues.add(network);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listNetworksId, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<ENetwork> listDefaultNetworksByZoneId(int zoneId,Integer resourcePoolId) throws Exception {
		List<ENetwork> listValues = new ArrayList<ENetwork>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			ENetwork newObj = new ENetwork();
			newObj.setIsdefault(true);
			// to fix bug:2286
			newObj.setType("Direct");
			if (zoneId != 0) {
				newObj.setZoneid(zoneId);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listnetworksresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("network");
					if (contentObj != null) {
						// listValues =
						// JsonUtil.getList4Json(contentObj.toString(),
						// ENetwork.class);
						String mytags = null;
						Object[] objlist = JsonUtil.getObjectArray4Json(contentObj.toString());
						int size = objlist.length;
						if (size != 0) {
							for (Object obj : objlist) {
								Map<String, String> objMap = JsonUtil.getMap4Json(obj.toString());
								mytags = objMap.get("tags");
								ENetwork network = new ENetwork();
								long netid = Long.parseLong(String.valueOf(objMap.get("id")));
								network.setId(netid);
								network.setStartip(objMap.get("startip"));
								network.setEndip(objMap.get("endip"));
								network.setIsdefault(Boolean.parseBoolean(String.valueOf(objMap.get("isdefault"))));
								network.setName(objMap.get("name"));
								network.setState(objMap.get("state"));
								if(objMap.get("vlan")!=null){
									network.setVlan(Integer.valueOf(objMap.get("vlan")));
								}
								network.setTags(mytags);
								listValues.add(network);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listNetworksZoneId, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public List<ENetwork> listOtherNetworksByZoneId(int zoneId,Integer resourcePoolId) throws Exception {
		List<ENetwork> listValues = new ArrayList<ENetwork>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			ENetwork newObj = new ENetwork();
			newObj.setIsdefault(false);
			// to fix bug:2286
			newObj.setType("Direct");
			if (zoneId != 0) {
				newObj.setZoneid(zoneId);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listnetworksresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("network");
					if (contentObj != null) {
						// listValues =
						// JsonUtil.getList4Json(contentObj.toString(),
						// ENetwork.class);
						String mytags = null;
						Object[] objlist = JsonUtil.getObjectArray4Json(contentObj.toString());
						int size = objlist.length;
						if (size != 0) {
							for (Object obj : objlist) {
								Map<String, String> objMap = JsonUtil.getMap4Json(obj.toString());
								mytags = objMap.get("tags");
								ENetwork network = new ENetwork();
								long netid = Long.parseLong(String.valueOf(objMap.get("id")));
								network.setId(netid);
								network.setStartip(objMap.get("startip"));
								network.setEndip(objMap.get("endip"));
								network.setIsdefault(Boolean.parseBoolean(String.valueOf(objMap.get("isdefault"))));
								network.setName(objMap.get("name"));
								network.setState(objMap.get("state"));
								if(objMap.get("vlan")!=null){
									network.setVlan(Integer.valueOf(objMap.get("vlan")));
								}
								network.setTags(mytags);
								listValues.add(network);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listNetworksZoneId, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public Map<String, List<EVlanIpRange>> getVlanIpRangesMap(Integer resourcePoolID){
		Map<String, List<EVlanIpRange>> iprangeMap = new HashMap<String, List<EVlanIpRange>>();
		try {
			List<EVlanIpRange>  listVlansAll = this.listVlan(0, 0, 0, resourcePoolID);
			List<EVlanIpRange> list  = new ArrayList<EVlanIpRange>();
			for (EVlanIpRange vlan:listVlansAll){
				String networkId = vlan.getNetworkid();
				if(iprangeMap.containsKey(networkId)){
					list = iprangeMap.get(networkId);
				}else{
					list = new ArrayList<EVlanIpRange>();
					iprangeMap.put(networkId, list);
				}
				list.add(vlan);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
		return iprangeMap;
	}

	@Override
	public List<ENetwork> listNetworksByZoneId(int zoneId,boolean isdefault,Integer resourcePoolId) throws Exception {
		List<ENetwork> listValues = new ArrayList<ENetwork>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			String paraTags = null;
			if (isdefault == true){
				paraTags = ConstDef.getFirstNetworkTag();
			}else{
				//				paraTags = ConstDef.getSecondNetworkTag();
			}
			ENetwork newObj = new ENetwork();
			newObj.setIsdefault(isdefault);
			// to fix bug:2286
			newObj.setType("Direct");
			if (zoneId != 0) {
				newObj.setZoneid(zoneId);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listnetworksresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("network");
					if (contentObj != null) {
						// listValues =
						// JsonUtil.getList4Json(contentObj.toString(),
						// ENetwork.class);
						Object[] objlist = JsonUtil.getObjectArray4Json(contentObj.toString());
						int size = objlist.length;
						if (size != 0) {
							String mytags = null;
							for (Object obj : objlist) {
								Map<String, String> objMap = JsonUtil.getMap4Json(obj.toString());
								ENetwork network = new ENetwork();
								//to fix bug:3938
								mytags = objMap.get("tags");
								if (paraTags!=null && !"".equals(paraTags)){//参数表设置有网卡标签时
									if (mytags!=null && !"".equals(mytags)){
										if (paraTags.equals(mytags)){
											network.setTags(mytags);
											long netid = Long.parseLong(String.valueOf(objMap.get("id")));
											network.setId(netid);
											network.setStartip(objMap.get("startip"));
											network.setEndip(objMap.get("endip"));
											network.setIsdefault(Boolean.valueOf(String.valueOf(objMap.get("isdefault"))));
											network.setName(objMap.get("name"));
											network.setState(objMap.get("state"));
											String vlanStr = objMap.get("vlan");
											if (vlanStr==null || "".equals(vlanStr)){
												vlanStr = "0";
											}
											network.setVlan(Integer.valueOf(vlanStr));
											network.setTags(mytags);
											listValues.add(network);
										}
									}
								}else{//参数表未设置网卡标签时
									long netid = Long.parseLong(String.valueOf(objMap.get("id")));
									network.setId(netid);
									network.setStartip(objMap.get("startip"));
									network.setEndip(objMap.get("endip"));
									network.setIsdefault(Boolean.valueOf(String.valueOf(objMap.get("isdefault"))));
									network.setName(objMap.get("name"));
									network.setState(objMap.get("state"));
									String vlanStr = objMap.get("vlan");
									if (vlanStr==null || "".equals(vlanStr)){
										vlanStr = "0";
									}
									network.setVlan(Integer.valueOf(vlanStr));
									network.setTags(mytags);
									listValues.add(network);
								}
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listNetworksZoneId, e.getMessage()));
		}
		return listValues;
	}

	@Override
	public String getResourcePoolNameById(int resourcePoolsId) throws SQLException {
		String return_value = "";
		try {
			TResourcePoolsBO resPool = resourcePoolsDao.searchResourcePoolsById(resourcePoolsId);
			if (null != resPool) {
				return_value = resPool.getPoolName();
			}
		}
		catch (Exception e) {
			throw new SQLException(String.format(ERROR_MESSAGE_getResourcePools, e.getMessage()));
		}
		return return_value;
	}

	public TResourcePoolsBO getResourcePoolById(int resourcePoolsId) throws SQLException {
		TResourcePoolsBO resPool = null;
		try {
			resPool = resourcePoolsDao.searchResourcePoolsById(resourcePoolsId);
		}
		catch (Exception e) {
			throw new SQLException(String.format(ERROR_MESSAGE_getResourcePools, e.getMessage()));
		}
		return resPool;
	}

	@Override
	public List<TResourcePoolsBO> listAllResourcePools() throws SQLException {
		try {
			return resourcePoolsDao.searchAllPools();
		}
		catch (Exception e) {
			throw new SQLException(String.format(ERROR_MESSAGE_getResourcePools, e.getMessage()));
		}
	}

	// to fix bug:0001910
	@Override
	public List<EListConfigurations> listConfigurations(String name,Integer resourcePoolId) throws SQLException {
		List<EListConfigurations> listValues = null;
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listValues;
			}
			EListConfigurations newObj = new EListConfigurations();
			if (name != null && !"".equals(name)) {
				newObj.setName(name);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listconfigurationsresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("configuration");
					if (contentObj != null) {
						listValues = JsonUtil.getList4Json(contentObj.toString(), EListConfigurations.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new SQLException(String.format(ERROR_MESSAGE_listConfigurations, e.getMessage()));
		}
		return listValues;
	}

	public CommandServiceImpl getCommandService() {
		return commandService;
	}

	public void setCommandService(CommandServiceImpl commandService) {
		this.commandService = commandService;
	}

	public IResourcePoolsDao getResourcePoolsDao() {
		return resourcePoolsDao;
	}

	public void setResourcePoolsDao(IResourcePoolsDao resourcePoolsDao) {
		this.resourcePoolsDao = resourcePoolsDao;
	}

	@Override
	public List<Map<String, String>> listVlanIpRanges(long networkId, int zoneId,Integer resourcePoolId)
	throws Exception {
		ListVlanIpRanges cmd = new ListVlanIpRanges();
		if(networkId >0 ) {
			cmd.setNetworkId(networkId);
			cmd.setZoneId(zoneId);
		}
		List<Map<String, String>> ipRanges = null;

		Object getObjJson = commandService.executeAndJsonReturn(cmd,resourcePoolId);
		if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
			JSONObject jo = JSONObject.fromObject(getObjJson);
			if (jo.containsKey("listvlaniprangesresponse")) {
				jo = jo.getJSONObject("listvlaniprangesresponse");
				if (jo.containsKey("vlaniprange")) {
					ipRanges = new ArrayList<Map<String, String>>();
					JSONArray arrays = jo.getJSONArray("vlaniprange");
					if (arrays != null && !arrays.isEmpty()) {
						for (int i = 0; i < arrays.size(); i++) {
							JSONObject jDomain = JSONObject.fromObject(arrays
							                                           .get(i));
							Map<String, String> ipRange = new HashMap<String, String>();
							ipRange.put("startip", jDomain.getString("startip"));
							ipRange.put("endip", jDomain.getString("endip"));
							ipRanges.add(ipRange);
						}
					}
				}
			}
		}
		return ipRanges;
	}

	@Override
	// bug 0006741
	public List<ListVirtualMachines> listVirtualMachines(int id, long networkId,
	                                                     int zoneId,Integer resourcePoolId) throws Exception {
		ListVirtualMachines cmd = new ListVirtualMachines();
		if (id > 0) {
			cmd.setId(id);
		}
		if(networkId >0 ) {
			cmd.setNetworkId(networkId);
		}
		if(zoneId>0) {
			cmd.setZoneId(zoneId);
		}
		List<ListVirtualMachines> vms = null;
		Object getObjJson = commandService.executeAndJsonReturn(cmd,resourcePoolId);
		if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
			JSONObject jo = JSONObject.fromObject(getObjJson);
			if (jo.containsKey("listvirtualmachinesresponse")) {
				jo = jo.getJSONObject("listvirtualmachinesresponse");
				if (jo.containsKey("virtualmachine")) {
					vms = new ArrayList<ListVirtualMachines>();
					JSONArray vmArray = jo.getJSONArray("virtualmachine");
					if (vmArray != null && !vmArray.isEmpty()) {
						for (int i = 0; i < vmArray.size(); i++) {
							JSONObject jVM = JSONObject.fromObject(vmArray.get(i));
							ListVirtualMachines vm = new ListVirtualMachines();
							vm.setId(Long.valueOf(jVM.getString("id")));
							vm.setName(jVM.getString("name"));
							vms.add(vm);
						}
					}
				}
			}
		}
		return vms;
	}

	@Override
	public List<listIpAddressesByNetWork> listIpAddressesByNetWork(long networkId,Integer resourcePoolId) throws Exception {
		List<listIpAddressesByNetWork> IpAddresses = null;
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return IpAddresses;
			}
			listIpAddressesByNetWork newObj = new listIpAddressesByNetWork();
			if (networkId != 0) {
				newObj.setNetworkid(networkId);
			}
			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				Object responseObj = responseMap.get("listipaddressesbynetworkresponse");
				if (responseObj != null) {
					Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
					Object contentObj = contentMap.get("ipaddresses");
					if (contentObj != null) {
						IpAddresses = JsonUtil.getList4Json(contentObj.toString(), listIpAddressesByNetWork.class);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listIpAddressesByNetWork, e.getMessage()));
		}
		return IpAddresses;
	}

	@Override
	public int  ipTotal4VlanIpRange(List<EVlanIpRange> listVlan){
		int iptotal = 0;
		for(EVlanIpRange vlan:listVlan){
			String ipstart =  vlan.getStartip();
			String ipend = vlan.getEndip();
			String[] ipstartarray1= ipstart.split("\\.");
			int ip1 = Integer.valueOf(ipstartarray1[ipstartarray1.length-1]);
			String[] ipendarray2= ipend.split("\\.");
			int ip2 = Integer.valueOf(ipendarray2[ipendarray2.length-1]);
			int ipcount = ip2-ip1+1;
			iptotal = iptotal + ipcount;
		}
		return iptotal;
	}

	@Override
	// bug 0006741
	public List<ListHosts> ListHosts(int id, int zoneId, int clusterId, Integer resourcePoolId) throws Exception {
		List<ListHosts> listHosts = new ArrayList<ListHosts>();
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listHosts;
			}
			ListHosts newObj = new ListHosts();
			if (id != 0) {
				newObj.setId(id);
			}
			if (zoneId != 0) {
				newObj.setZoneid(zoneId);
			}
			if(clusterId != 0){
				newObj.setClusterid(clusterId);
			}
			newObj.setType("Routing");
			newObj.setAllocationstate("Enabled");
			//fix bug:7644
			newObj.setState("Up");

			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);

			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				if(responseMap.containsKey("listhostsresponse")){
					Object responseObj = responseMap.get("listhostsresponse");
					if (responseObj != null) {
						Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
						if(contentMap.containsKey("host")){
							Object contentObj = contentMap.get("host");
							if (contentObj != null) {
								listHosts = JsonUtil.getList4Json(contentObj.toString(), ListHosts.class);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listHosts, e.getMessage()));
		}
		return listHosts;
	}

	@Override
	public List<ListTemplates> ListTemplates(int eOsId, int zoneId, Integer resourcePoolId)
	throws Exception {
		List<ListTemplates> listTemplates = null;
		try {
			if (ConstDef.getCurProjectId() == 2) {// 广东vdc
				return listTemplates;
			}
			ListTemplates newObj = new ListTemplates();
			if (eOsId != 0) {
				newObj.setId(String.valueOf(eOsId));
			}
			if (zoneId != 0) {
				newObj.setZoneId(zoneId);
			}
			newObj.setTemplateFilter("community");

			Object getObjJson = commandService.executeAndJsonReturn(newObj, resourcePoolId);
			if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
				Map<String, Object> responseMap = JsonUtil.getMap4Json(getObjJson.toString());
				if(responseMap.containsKey("listtemplatesresponse")){
					Object responseObj = responseMap.get("listtemplatesresponse");
					if (responseObj != null) {
						Map<String, Object> contentMap = JsonUtil.getMap4Json(responseObj.toString());
						if(contentMap.containsKey("template")){
							Object contentObj = contentMap.get("template");
							if (contentObj != null) {
								listTemplates = JsonUtil.getList4Json(contentObj.toString(), ListTemplates.class);
							}
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			throw new Exception(String.format(ERROR_MESSAGE_listTemplates, e.getMessage()));
		}
		return listTemplates;
	}




}
