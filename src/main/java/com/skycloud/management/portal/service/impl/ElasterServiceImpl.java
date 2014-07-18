package com.skycloud.management.portal.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts2.ServletActionContext;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.res.ECluster;
import com.skycloud.management.portal.front.command.res.EListConfigurations;
import com.skycloud.management.portal.front.command.res.EStoragePool;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.service.IElasterSerivce;

/**
 * 调用elaster的接口实现集中写在此类
 * 
 * @author zhanghuizheng
 * 
 */
public class ElasterServiceImpl implements IElasterSerivce {
	ICommandService commandService;

	// IResourcePoolsDao resourcePoolsDao;

	/**
	 * 1.3功能，支持多资源池 调用elaster的listTemplates查询虚机的操作系统
	 * 
	 * @param type
	 */
	public List<Map<String, String>> listTemplates(Integer resourcePoolsId,int zoneId) throws Exception {
		ListTemplates temp = new ListTemplates();
		temp.setZoneId(zoneId);//to fix bug [7681]
		temp.setTemplateFilter("community");
		Map<String, String> map = JsonUtil.getMap4Json(String.valueOf(commandService.executeAndJsonReturn(temp,resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map.get("listtemplatesresponse")));
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if (map.containsKey("template")) {
			List<Map> list = JsonUtil.getList4Json(String.valueOf(map.get("template")), Map.class);
			for (Map template : list) {
				if (template.get("status").equals("Download Complete")) {
					// if (type == ConstDef.RESOURCE_TYPE_VM) {
					if (!template.get("hypervisor").equals("BareMetal")) {
						Map<String, String> optionMap = new HashMap<String, String>();
						optionMap.put("text", String.valueOf(template.get("name")));
						optionMap.put("value", String.valueOf(template.get("id")));
						reList.add(optionMap);
					}
					// }
				}
			}
		}
		return reList;
	}
	
	/**
	 * 查询所有资源池下的操作系统
	 */
	public List<Map<String, String>> listTemplatesForAllPools(List<TResourcePoolsBO> poollist){
	    ListTemplates temp = new ListTemplates();
	    temp.setTemplateFilter("community");
	    List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
	    if(null != poollist && poollist.size()>0){
	    	for(TResourcePoolsBO pool : poollist){
	    		Object getObjJson = commandService.executeAndJsonReturn(temp,pool.getId());
	    		if (getObjJson != null && String.valueOf(getObjJson).startsWith("{")) {
	    			JSONObject jo = JSONObject.fromObject(getObjJson);
	    			if(jo.containsKey("listtemplatesresponse")){
	    				jo = jo.getJSONObject("listtemplatesresponse");
	    				if(jo.containsKey("template")){
	    					JSONArray arrays = jo.getJSONArray("template");
	    					if (arrays != null && !arrays.isEmpty()) {
	    						for (int i = 0; i < arrays.size(); i++) {
	    							JSONObject template = JSONObject.fromObject(arrays.get(i));
	    					          if(template.get("status").equals("Download Complete")){
	    					        		if(!template.get("hypervisor").equals("BareMetal")){
	    					        			Map<String, String> optionMap = new HashMap<String, String>();
	    					                	optionMap.put("text", String.valueOf(template.get("name")));
	    					                	optionMap.put("value", String.valueOf(template.get("id"))+","+pool.getId());
	    					                	reList.add(optionMap);
	    					        		}
	    					          }    	  	    							
	    						}
	    					}
	    				}
	    			}
	    		}
//		    	Map<String, String> map = JsonUtil.getMap4Json(String.valueOf(commandService.executeAndJsonReturn(temp,pool.getId())));
//		    	map = JsonUtil.getMap4Json(String.valueOf(map.get("listtemplatesresponse")));
//			    if(map.containsKey("template")){
//			    	List<Map> list = JsonUtil.getList4Json(String.valueOf(map.get("template")), Map.class);
//			      	for(Map template : list) {
//			          if(template.get("status").equals("Download Complete")){
//			        		if(!template.get("hypervisor").equals("BareMetal")){
//			        			Map<String, String> optionMap = new HashMap<String, String>();
//			                	optionMap.put("text", String.valueOf(template.get("name")));
//			                	optionMap.put("value", String.valueOf(template.get("id"))+","+pool.getId());
//			                	reList.add(optionMap);
//			        		}
//			          }    	  
//			      	}
//			    } 	    	  
		    }	    		
	    }	
	    return reList;
	}

	/**
	 * 根据操作系统id调用elaster接口查询操作系统
	 * 
	 * @return
	 */
	public Map getTemplateById(int osId, Integer resourcePoolsId) {
		ListTemplates temp = new ListTemplates();
		temp.setTemplateFilter("community");
		Map<String, String> map = JsonUtil.getMap4Json(String
				.valueOf(commandService.executeAndJsonReturn(temp,
						resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map
				.get("listtemplatesresponse")));
		Map reTemp = null;
		if (map.containsKey("template")) {
			List<Map> list = JsonUtil.getList4Json(String.valueOf(map
					.get("template")), Map.class);
			for (Map template : list) {
				int id = Integer.parseInt(String.valueOf(template.get("id")));
				if (id == osId) {
					reTemp = template;
					break;
				}
			}
		}
		return reTemp;
	}

	/**
	 * 根据操作系统类型和resourcePoolsId查询某个资源池下的cluster
	 */
	public Map<String, String> listClusters(String hypervisortype,Integer resourcePoolsId) {
//		String hypervisortype = ServletActionContext.getRequest().getParameter(
//				"hypervisor");
		ECluster ecluster = new ECluster();
		ecluster.setHypervisortype(hypervisortype);
		Map<String, String> map = JsonUtil.getMap4Json(String
				.valueOf(commandService.executeAndJsonReturn(ecluster,
						resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map
				.get("listclustersresponse")));
		return map;
	}

	/**
	 * 查询elaster的listConfigurations接口
	 * 
	 * @return
	 */
	public Map<String, String> listConfigurations(Integer resourcePoolsId) {
		EListConfigurations config = new EListConfigurations();
		config.setName("use.local.storage");
		Map<String, String> map_config = JsonUtil.getMap4Json(String
				.valueOf(commandService.executeAndJsonReturn(config,
						resourcePoolsId)));
		map_config = JsonUtil.getMap4Json(String.valueOf(map_config
				.get("listconfigurationsresponse")));
		return map_config;
	}

	/**
	 * 综合一点的查询 
	 * 在某个资源池下根据操作系统id查询所有的存储类型
	 * 第1步：根据resourcePoolsId和osId查询出操作系统template
	 * 第2步：获取template的操作系统类型，例如是vmware还是xenserver
	 * 第3步：根据操作系统类型和resourcePoolsId查询某个资源池下的clusters
	 * 第4步：循环clusters，精确筛选出符合的操作系统，得到clusterid
	 * 第5步：根据resourcePoolsId和clusterid调用elaster的listStoragePools接口查询所有的存储类型
	 * 第6步：在查询出的结果中根据clusterid相等精确筛选出存储类型
	 * @param osId
	 * @param resourcePoolsId
	 * @return
	 */
	public String getStoreTypesByOsId(int osId, Integer resourcePoolsId) {
		//根据操作系统id调用elaster接口查询操作系统
		Map template = this.getTemplateById(osId, resourcePoolsId);
		String hypervisortype = String.valueOf(template.get("hypervisor"));
		// 根据操作系统的类型调用elaster接口查询存储类型
		String result = this.getStoreTypesByHypervisortype(hypervisortype,
				resourcePoolsId);
		return result;
	}

	/**
	 * 综合一点的查询 
	 * 在某个资源池下根据操作系统类型查询所有的存储类型
	 * 第一步：根据操作系统类型和resourcePoolsId查询某个资源池下的clusters
	 * 第二步：循环clusters，精确筛选出符合的操作系统，得到clusterid
	 * 第三步：在某个资源池下根据clusterid调用elaster的listStoragePools接口查询存储类型
	 * 	第1步：根据resourcePoolsId和clusterid调用elaster的listStoragePools接口查询所有的存储类型
	 * 	第2步：在查询出的结果中根据clusterid相等精确筛选出存储类型
	 * 
	 * @param resourcePoolsId
	 * @return
	 */
	public String getStoreTypesByHypervisortype(String hypervisortype,
			Integer resourcePoolsId) {
		Map<String, String> map = this.listClusters(hypervisortype,resourcePoolsId);
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		if (map.containsKey("cluster")) {
			List<Map> list = JsonUtil.getList4Json(String.valueOf(map
					.get("cluster")), Map.class);
			Map<String, String> dinstinctmap = new HashMap<String, String>();
			for (Map cluster : list) {
				String hypervisor = String.valueOf(cluster
						.get("hypervisortype"));
				if (hypervisortype.equals(hypervisor)) {
					int cluterId = Integer.parseInt(String.valueOf(cluster
							.get("id")));
					//在某个资源池下根据clusterid查询存储类型
					List<Map<String, String>> _storages = this
							.getStoreTypeByClusterId(cluterId, resourcePoolsId);
					for (Map storage2 : _storages) {
						String key = String.valueOf(storage2.get("tags"));
						if (!dinstinctmap.containsKey(key)) {
							dinstinctmap.put(key, key);
						}

					}
				}
			}
			if (null != dinstinctmap && dinstinctmap.size() > 0) {
				Set<String> _set = dinstinctmap.keySet();
				for (String _key : _set) {
					Map<String, String> optionMap = new HashMap<String, String>();
					if (_key.equals("")) {
						optionMap.put("text", "默认");
						optionMap.put("value", _key);
					} else {
						optionMap.put("text", _key);
						optionMap.put("value", _key);
					}
					reList.add(optionMap);
				}
			}

		}

		// 查询elaster的listConfigurations接口
		Map<String, String> map_config = this
				.listConfigurations(resourcePoolsId);
		if (map.containsKey("configuration")) {
			List<Map> list_config = JsonUtil.getList4Json(String
					.valueOf(map_config.get("configuration")), Map.class);
			for (Map _config : list_config) {
				String name = String.valueOf(_config.get("name"));
				if (name.equals("use.local.storage")) {
					String value = String.valueOf(_config.get("value"));
					if (value.equals("true")) {
						Map<String, String> _optionMap = new HashMap<String, String>();
						_optionMap.put("text", "LOCALSTORAGE");
						_optionMap.put("value", "LOCALSTORAGE");
						reList.add(_optionMap);
					}
				}
			}
		}
		return JSONArray.fromObject(reList).toString();
	}
	
	/**
	 * 在某个资源池下根据clusterid调用elaster的listStoragePools接口查询存储类型
	 * 第一步：根据resourcePoolsId和clusterid调用elaster的listStoragePools接口查询所有的存储类型
	 * 第二步：在查询出的结果中根据clusterid相等精确筛选出存储类型
	 * @param clusterid
	 * @return
	 */
	public List<Map<String, String>> getStoreTypeByClusterId(long clusterid,
			Integer resourcePoolsId) {
		EStoragePool storagePool = new EStoragePool();
		storagePool.setState("Up");
		storagePool.setClusterid(clusterid);
		Map<String, String> map = JsonUtil.getMap4Json(String
				.valueOf(commandService.executeAndJsonReturn(storagePool,
						resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map
				.get("liststoragepoolsresponse")));

		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		// to fix bug:3669
		if (map.containsKey("storagepool")) {
			List<Map> list = JsonUtil.getList4Json(String.valueOf(map
					.get("storagepool")), Map.class);
			for (Map _storage : list) {
				long _clusterid = Long.parseLong(String.valueOf(_storage
						.get("clusterid")));
				if (_clusterid == clusterid) {
					reList.add(_storage);
				}
			}

		}
		return reList;
	}
	
	/**
	 * 自服务门户查询存储类型
	 */
	public List<Map> getAllStoreType(Integer resourcePoolsId) {
		EStoragePool storagePool = new EStoragePool();
		storagePool.setState("Up");
		Map<String, String> map = JsonUtil.getMap4Json(String.valueOf(commandService.executeAndJsonReturn(storagePool,resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map.get("liststoragepoolsresponse")));
		List<Map> list = JsonUtil.getList4Json(String.valueOf(map.get("storagepool")), Map.class);
		return list;
	}
	
	/**
	 * 运营管理平台-资源模板管理-虚拟硬盘资源模板管理
	 */
	public List<Map> getAllStoreTypeByZoneId(Integer resourcePoolsId,int zoneId) {
		EStoragePool storagePool = new EStoragePool();
		storagePool.setState("Up");
		storagePool.setZoneid(zoneId);
		Map<String, String> map = JsonUtil.getMap4Json(String.valueOf(commandService.executeAndJsonReturn(storagePool,resourcePoolsId)));
		map = JsonUtil.getMap4Json(String.valueOf(map.get("liststoragepoolsresponse")));
		List<Map> list = JsonUtil.getList4Json(String.valueOf(map.get("storagepool")), Map.class);
		return list;
	}

	
	public ICommandService getCommandService() {
		return commandService;
	}

	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}

}
