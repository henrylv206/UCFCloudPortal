package com.skycloud.management.portal.admin.template.action;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ParameterManager;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.service.IElasterSerivce;

public class VMTemplateAction extends ActionSupport {

	private static final long serialVersionUID = -2228337046652929894L;

	private final Logger logger = Logger.getLogger(VMTemplateAction.class);

	private IVMTemplateService VMTemplateService;

	//private ICommandService commandService;

	private String message;

	private int curPage = 1;

	private int pageSize = 10;

	private Map<String, Object> listResp;

	private TTemplateVMBO template;

	private Map<String, String> configParams;

	// added by zhanghuizheng
	private String[] resourceType;

	private String queryJson;

	// added by zhanghuizheng
	private ICloudAPISerivce cloudAPIService;

	// private IInstanceInfoService instanceInfoService;
	//private IMCTemplateService MCTemplateService;

	// 1.3功能，支持多资源池
	private IElasterSerivce elasterSerivce;

	// 1.3功能，支持多资源池
	private int resourcePoolsId;
	
	private int zoneId;
	

	public String listDISKTemplateByType() {

		int storageSize = Integer.parseInt(ServletActionContext.getRequest().getParameter("storage_size"));

		listResp = new HashMap<String, Object>();
		try {
			listResp.put("list", VMTemplateService.listTemplateDISK(storageSize));
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}



	// @LogInfo(desc="type=1=>functionName=查询虚拟机模板,templateType=2=>functionName=查询虚拟硬盘模板,templateType=4=>functionName=查询备份服务模板,templateType=5=>functionName=查询监控服务模板,templateType=6=>functionName=查询负载均衡服务模板,templateType=7=>functionName=查询防火墙服务模板,templateType=8=>functionName=查询公网带宽服务模板,templateType=9=>functionName=查询公网IP服务模板,templateType=10=>functionName=查询物理机资源模板",operateType=4,moduleName="资源模板管理",functionName="查询模板",parameters="type")
	public String getVMTemplateById() {
		try {
			int id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
			template = VMTemplateService.getTemplateById(id);
			int _type = template.getType();
			if(_type == ConstDef.RESOURCE_TYPE_IPSAN){
				String raidjson = template.getExtendAttrJSON();
				if(null != raidjson && !raidjson.equals("")){
					JSONObject jo = JSONObject.fromObject(raidjson);
					if(jo.containsKey("raid")){
						String raid = jo.getString("raid");
						template.setExtendAttrJSON(raid);
					}
				}
			}
			//查询资源池名称
			String poolname = cloudAPIService.getResourcePoolNameById(template.getResourcePoolsId());
			template.setResourcePoolsName(poolname);
			if(template.getId() > 0 && template.getType() != 10 &&  template.getType() != 3)
			{
				//特殊模板不走查询zone逻辑，物理机、小机单独一套zone
				List<EZone> zones = this.cloudAPIService.listZones(template.getZoneId(), template.getResourcePoolsId());
				if(null != zones && !zones.isEmpty()){
					for(EZone zone : zones){
						if(zone.getId()==template.getZoneId()){
							template.setZoneName(zone.getName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}


	@SuppressWarnings("unchecked")
	public String getTemplateConfigParams() {
		try {
			String strType = ServletActionContext.getRequest().getParameter("type");
			int type = -1;
			if (null != strType && !strType.equals("")) {
				type = Integer.parseInt(strType);
			}
			configParams = new HashMap<String, String>();
			//查询资源池
			this.getPoolnameParams();
			configParams.put("cpuhz", ParameterManager.getInstance().getValue(ConstDef.COMBOX_CPUHZ_GHZ));
			int curProjectId = ConstDef.getCurProjectId();
			configParams.put("curprojectid", String.valueOf(curProjectId));
			
			// 操作系统下拉列表
			if (curProjectId == 1) { // 1:SkyCloud; 2:广东移动VDC;
				if(type == ConstDef.RESOURCE_TYPE_VM){
					this.getOsnameParams();
				}
				else if (type == ConstDef.RESOURCE_TYPE_PM) {// 增加物理机方法
					// 物理机操作系统信息表为T_SCS_PHYSICAL_OSTYPE，与虚拟机不同，需要新增查询方法 ninghao
					// 2012-08-28
					this.getPhysicalOsnameParams(type);
				}
			} else if (curProjectId == 2) {
				// 暂时从配置文件读取数据替代,等待广东移动接口?
				configParams.put("_vmos", ConfigManager.getInstance().getString("dropdownlist.vmos"));
			}
			
			configParams.put("publicOrPrivateCloud",String.valueOf(ConstDef.getCloudId()));
		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 1.3支持多资源池 操作系统下拉列表:调用elaster的listTemplates接口根据resourcePoolsId和zoneId查询虚机的操作系统
	 * 
	 * @return
	 */
	public String getOsesByResourcePoolId() {
		try {
			// 1.3功能，支持多资源池
			int curProjectId = ConstDef.getCurProjectId();
			List<Map<String, String>> reList = null;
			if (curProjectId == 1) { // 1:SkyCloud; 2:广东移动VDC;
				reList = elasterSerivce.listTemplates(this.resourcePoolsId,this.zoneId);
			}
			message = JSONArray.fromObject(reList).toString();
		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 1.3支持多资源池 资源域下拉列表:调用elaster的listZones接口根据resourcePoolsId查询资源域
	 * 
	 * @return
	 */
	public String getZonesByResourcePoolId() {
		try {
			// 1.3功能，支持多资源池
			int curProjectId = ConstDef.getCurProjectId();
			List<EZone> reList = null;
			if (curProjectId == 1) { // 1:SkyCloud; 2:广东移动VDC;
				reList = this.cloudAPIService.listZones(0, this.resourcePoolsId);
			}
			message = JSONArray.fromObject(reList).toString();
		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 物理机操作系统信息表为T_SCS_PHYSICAL_OSTYPE，与虚拟机不同， 需要新增查询物理机操作系统信息方法 ninghao
	 * 2012-08-28
	 * 
	 * @param type
	 */
	public void getPhysicalOsnameParams(int type) {

		List<Map<String, Object>> list = null;

		try {// 物理机操作系统列表
			list = this.VMTemplateService.getPhysicalTemplateOS(type);
		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}

		List<Map<String, String>> oslist = new ArrayList<Map<String, String>>();
		// 转换map信息（页面下拉列表）
		if (list != null) {
			for (Map os : list) {
				Map<String, String> optionMap = new HashMap<String, String>();
				optionMap.put("text", String.valueOf(os.get("name")));
				optionMap.put("value", String.valueOf(os.get("id")));
				oslist.add(optionMap);
			}
		}
		configParams.put("osname", JSONArray.fromObject(oslist).toString());
	}

	/**
	 * 在某个资源池下根据操作系统id查询所有的存储类型
	 * 
	 * @return
	 */
	public String getStoreTypesByOsId() {
		try {
			int osId = Integer.parseInt(ServletActionContext.getRequest().getParameter("osId"));
			message = this.elasterSerivce.getStoreTypesByOsId(osId, resourcePoolsId);
		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	
	public void getPoolnameParams() throws SQLException {
		List<TResourcePoolsBO> poollist = this.cloudAPIService.listAllResourcePools();
		List<Map<String, String>> poolMapList = new ArrayList<Map<String, String>>();
		for (TResourcePoolsBO pool : poollist) {
			Map<String, String> poolOptionMap = new HashMap<String, String>();
			poolOptionMap.put("text", pool.getPoolName());
			poolOptionMap.put("value", String.valueOf(pool.getId()));
			poolMapList.add(poolOptionMap);
		}
		configParams.put("poolname", JSONArray.fromObject(poolMapList).toString());
	}


	/**
	 * 查询所有资源池下的操作系统
	 * @throws SQLException
	 */
	public void getOsnameParams() throws SQLException{
		List<TResourcePoolsBO> poollist = this.cloudAPIService.listAllResourcePools();
		List<Map<String, String>> reList = this.elasterSerivce.listTemplatesForAllPools(poollist);
	    configParams.put("osname", JSONArray.fromObject(reList).toString());
	  }
	
	public IVMTemplateService getVMTemplateService() {
		return VMTemplateService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public TTemplateVMBO getTemplate() {
		return template;
	}

	public void setTemplate(TTemplateVMBO template) {
		this.template = template;
	}

	public Map<String, String> getConfigParams() {
		return configParams;
	}

	public void setConfigParams(Map<String, String> configParams) {
		this.configParams = configParams;
	}

	public String getQueryJson() {
		return queryJson;
	}

	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}

	

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public String[] getResourceType() {
		return resourceType;
	}

	public void setResourceType(String[] resourceType) {
		this.resourceType = resourceType;
	}

	// 1.3功能，支持多资源池
	public IElasterSerivce getElasterSerivce() {
		return elasterSerivce;
	}

	// 1.3功能，支持多资源池
	public void setElasterSerivce(IElasterSerivce elasterSerivce) {
		this.elasterSerivce = elasterSerivce;
	}

	// 1.3功能，支持多资源池
	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	// 1.3功能，支持多资源池
	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	// public IInstanceInfoService getInstanceInfoService() {
	// return instanceInfoService;
	// }
	//
	// public void setInstanceInfoService(IInstanceInfoService
	// instanceInfoService) {
	// this.instanceInfoService = instanceInfoService;
	// }
		
	/**
	 * 获取模板和产品
	 */
	public String getVMTemplateAndProductById() {
		try {
			int id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
			template = VMTemplateService.getTemplateById(id);
			if(template.getId() > 0 && template.getType() != 10 &&  template.getType() != 3)
			{//特殊模板不走查询zone逻辑，物理机、小机单独一套zone
				List<EZone> zones = this.cloudAPIService.listZones(template.getZoneId(), template.getResourcePoolsId());
				if(null != zones && !zones.isEmpty()){
					for(EZone zone : zones){
						if(zone.getId()==template.getZoneId()){
							template.setZoneName(zone.getName());
						}
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}
	

}
