package com.skycloud.management.portal.front.mall.action;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MediaType;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.skycloud.management.portal.admin.parameters.entity.Parameters;
import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.CTime;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ParameterManager;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.mall.entity.TemplateTypeBO;
import com.skycloud.management.portal.front.mall.entity.VMMonitorInfo;
import com.skycloud.management.portal.front.mall.service.CloudServiceMallService;
import com.skycloud.management.portal.front.mall.vo.ResourceTopoVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.service.ConfigurationLoader;
import com.skycloud.management.portal.service.IElasterSerivce;
import com.skycloud.management.portal.webservice.databackup.service.IDBAsycnJobService;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;
import com.sun.jersey.api.uri.UriComponent;

public class CloudServiceMallAction extends BaseAction {

	/**   */
	private static final long serialVersionUID = -8598517780605901562L;

	private final Logger logger = Logger.getLogger(CloudServiceMallAction.class);

	private CloudServiceMallService service;

	private List<Product> returnList;

	private List<TServiceInstanceBO> rsList;

	private List<TInstanceInfoBO> inslist;

	private List<TemplateTypeBO> templateTypeList;

	private Map<String, Object> listResp;

	private String returnJson;

	private int curPage = 1;// 当前页数

	private int pageSize = 5;// 每页显示多少条

	private int countTotal;

	private String serviceName;

	private String message;

	private int part;

	private int serviceId;

	private int serviceID;

	private String typeId;

	private String key;// 搜索词

	private String sales;// 销量次序

	private String price;// 价格次序

	// added by zhanghuizheng
	private Map<String, String> configParams; // 页面初始化参数

	private String queryJson;

	private String state;

	private String start;

	private String end;

	// added by zhanghuizheng
	private Product product;

	private String preUrl;

	private int resourceId;

	private int orderId;
	private String relationsJson;;
	private String resourceIDsJson;;
	private String topIDArray;

	//private IServiceInstanceService serviceInstanceService;

	private List<Parameters> parameterList;

	private ISysParametersService parametersService;
	
	private IDBAsycnJobService dbAsycnJobService;

	// 1.3功能，支持多资源池
	private IElasterSerivce elasterSerivce;
	//资源池业务逻辑
	//private IResourcePoolsService resourcePoolsService;

	// 全部服务
	public String showServiceMall() {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		try {
			countTotal = service.getAllServiceCnt(part, key, typeId, 0);
			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}
			returnList = service.getAllService(page, part, sales, price, key, typeId, 0);

			listResp.put("list", returnList);
			listResp.put("total", countTotal);
			logger.info("get all cloud mall service. ");
			return SUCCESS;
		}
		catch (Exception e) {
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	// 推荐服务
	public String showCommendService() {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		try {
			countTotal = service.getCommendServiceCount(0);
			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}
			returnList = service.getCommendService(page, 0);
			listResp.put("list", returnList);
			listResp.put("total", countTotal);
			logger.info("get all cloud mall service. ");
			return SUCCESS;
		}
		catch (Exception e) {
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	public String listTemplateType() {
		try {
			templateTypeList = service.getTemplateTypeList();
			logger.info("get TemplateType List. ");
			return SUCCESS;
		}
		catch (SQLException e) {
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	// 我的服务列表
	public String showServiceList() {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {
			countTotal = service.getAllServiceCount(user, key, typeId, state, start, end);
			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}
			List<TServiceInstanceBO> rs = service.getAllService(page, user, key, typeId, state, start, end);
			// fix bug 7635 服务状态与资源状态不一致，job未能维护正确（这里不应该转换状态）
			/*
			rsList = new ArrayList<TServiceInstanceBO>();
			// bug 0003097 0003205 0003193 0003094
			for (int i = 0, l = rs.size(); i < l; i++) {
				TServiceInstanceBO info = rs.get(i);
				info.setState(serviceInstanceService.checkServiceInstanceStateById(info.getId()));
				// bug 0003356
				if (!"0".equals(state) && StringUtils.isNotEmpty(state)) {
					if (info.getState() == Integer.valueOf(state)) {
						rsList.add(info);
					}
				} else {// bug 0003308 0003193
					rsList.add(info);
				}
			}
			//*/
			listResp.put("list", rs);// fix bug 7635
			listResp.put("total", countTotal);
			logger.info("get all cloud mall service. ");
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}


	public String showQuitInstance() {
		listResp = new HashMap<String, Object>();
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		String _fromOpt = ServletActionContext.getRequest().getParameter("fromOpt");
		String _userOwner = ServletActionContext.getRequest().getParameter("userOwner");
		try {
			if(null != _fromOpt){
				if(null != _userOwner){
					int realuserid = Integer.parseInt(_userOwner);

				}
			}
			rsList = service.getQuitInstance(user, serviceID);
			listResp.put("list", rsList);
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}



	// 资源列表
	public String showResourceList() {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {									
			countTotal = service.getAllReourceCount(serviceId);
			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}
			inslist = service.getAllReource(page, serviceId, user);
			
			String eID = "";			
			if (inslist.size()>0){
				TInstanceInfoBO tInstanceInfoBO = inslist.get(0);	
				eID = String.valueOf(tInstanceInfoBO.geteInstanceId());
			}		
			AsyncJobInfo asyncInfo = dbAsycnJobService.queryAsyncJobInfoByPara(eID);
			
			int initPassword = ConstDef.getInitPassword();
			listResp.put("list", inslist);
			listResp.put("total", countTotal);
			listResp.put("curProjectId", String.valueOf(ConstDef.curProjectId));
			listResp.put("initPassword", initPassword);
			if(asyncInfo!=null){
				listResp.put("ResId", asyncInfo.getRESID());				
			}else {
				listResp.put("ResId",0);
			}			
			logger.info("get all cloud mall service. ");
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	// 资源列表
	public String showResourceList2() {
		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {
			countTotal = service.getAllReourceCount(serviceId);
			PageVO page = null;
			if (countTotal > 0) {
				page = new PageVO();
				page.setCurPage(curPage);
				page.setPageSize(pageSize);
			}
			inslist = service.getAllReource2(page, serviceId, user, orderId);
			listResp.put("list", inslist);
			listResp.put("total", countTotal);
			listResp.put("curProjectId", String.valueOf(ConstDef.curProjectId));
			logger.info("get all cloud mall service. ");
			return SUCCESS;
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	private long vmid;

	private String resName;

	private String type;

	private long startTime;

	private long endTime;

	private List<VMMonitorInfo> vmmolist;

	private static Client client;

	public static void setup() {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);
		client.setFollowRedirects(true);
		client.setReadTimeout(190000);
		client.setConnectTimeout(5000);
	}

	public static void teardown() {
		client.destroy();
	}

	private String go(String url) throws Exception {
		HttpClient client = new HttpClient();
		GetMethod method = new GetMethod(url);
		int statusCode = client.executeMethod(method);
		if (statusCode != HttpStatus.SC_OK) {
			System.err.println("Method failed: " + method.getStatusLine());
		}
		byte[] responseBody = method.getResponseBody();
		String response = new String(responseBody);
		System.out.println("response : " + response);
		return response;
	}

	/**
	 * 云监控 1.3
	 * 
	 * @return 0004851 0004528 0005140
	 * @throws Exception
	 */
	public String getMonitorData() throws Exception {
		setup();

		// type: VM(虚拟机), HOST(物理机), MINPHY(小型机), HYPERVISORHOST(hyperviser)
		ResourcesVO res = service.getDeviceNameById(vmid, type);
		if (res != null) {
			resName = res.getInstance_name();
			resourceId = res.getResourcePoolsId();
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vmmolist = new ArrayList<VMMonitorInfo>();
		// TimeZone.setDefault(TimeZone.getTimeZone("Asia/Shanghai"));// GMT+8
		Date date2 = new Date();
		// Date date2 = df.parse("2012-08-31 00:00:00");
		// date2 = df.parse("2013-01-21 00:00:00");
		int step = Integer.valueOf(parametersService.getParameterValueByType("MONITOR_HOUR_STEP"));
		logger.info("step=" + step);
		// Date date1 = df.parse(CTime.addDay(df, df.format(date2), -1));
		Date date1 = df.parse(CTime.addHours(df, df.format(date2), -step));
		JSONObject o2 = new JSONObject();
		String params = "";
		//fix bug7417 监控数据获取，中文需要转换编码
		params += "deviceName=" + UriComponent.encode(resName, UriComponent.Type.PATH) + "&";
		params += "resourceID=" + resourceId + "&";
		params += "deviceType=" + type + "&";
		// 起始时间 形如 2013-01-10 16:50:11
		params += "fromTime=" + df.format(date1) + "&";
		// 结束时间 形如 2013-01-11 16:50:11
		params += "toTime=" + df.format(date2);

		try {
			String url = ConfigurationLoader.getInstance().getProperty("monitor.query.url");
			WebResource r = client.resource(url);
			url = url + "?" + params.replace(" ", "+");
			r = client.resource(url);
			logger.info(url);
			ClientResponse resp  = r.accept(MediaType.APPLICATION_JSON)
									.header("Content-Type", "application/json")
									.get(ClientResponse.class);

			// System.out.println(resp.toString());
			String str = resp.getEntity(String.class);
			//System.out.println(str);
			net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject("{infoList:" + str + "}");
			net.sf.json.JSONArray jsonArray = json.getJSONArray("infoList");

			for (int i = 0, l = jsonArray.size(); i < l; i++) {// bug 0004046
				net.sf.json.JSONObject js = jsonArray.getJSONObject(i);
				js = net.sf.json.JSONObject.fromObject(_toN(js.toString()));
				VMMonitorInfo info = (VMMonitorInfo) net.sf.json.JSONObject.toBean(js, VMMonitorInfo.class);
				String timeStamp = info.getTimeStamp();
				Date time = new Date(Long.valueOf(timeStamp));
				timeStamp = df.format(time);
				info.setTimeStamp(timeStamp);
				vmmolist.add(info);
				//System.out.println("cpu="+ info.getCpuUt()+",mem="+ info.getMemUt()+",net_r="+info.getNetworkRead()+",net_w="+info.getNetworkWrite()+",disk_r="+ info.getDiskRead()+",disk_w="+info.getDiskWrite());
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		teardown();
		return SUCCESS;
	}

	// 将形如VM_ID的变量转成形如vmId
	private String _toN(String str) {
		String rs = "";
		str = str.replace("{", "").replace("}", "");
		String[] strs = str.split(",");
		for (int i = 0, l = strs.length; i < l; i++) {
			strs[i] = _2N(strs[i]);
		}
		rs = "{" + StringUtils.join(strs, ",") + "}";
		return rs;
	}

	private String _2N(String _$param) {
		String rs = "";
		String[] _s = _$param.split("_");
		rs += _s[0].toLowerCase();
		for (int i = 1, l = _s.length; i < l; i++) {
			String first=_s[i].substring(0,1);
			String other=_s[i].substring(1);
			rs += first.toUpperCase() + other.toLowerCase().replace(":null", ":0");
		}
		return rs;
	}
	/**
	 * 云监控 1.2
	 * 
	 * @return
	 * @throws Exception
	 */
	public String getMonitorData_o() throws Exception {
		setup();

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vmmolist = new ArrayList<VMMonitorInfo>();
		Date date2 = new Date();
		// Date date2 = df.parse("2012-08-31 00:00:00");
		// Date date2 = df.parse("2012-09-01 00:00:00");
		// fix bug 4562 0005116
		int step = parametersService.getParameterByType("MONITOR_HOUR_STEP");
		//logger.info(step);
		Date date1 = df.parse(CTime.addHours(df, df.format(date2), -step));
		// Date date1 = df.parse(CTime.addDay(df, df.format(date2), -1));

		JSONObject o2 = new JSONObject();
		o2.put("id", vmid + "");
		o2.put("type", type);// type: VM(虚拟机), PHYSICAL(物理机), MINIPHY(小型机)
		o2.put("startTime", date1.getTime());
		o2.put("endTime", date2.getTime());
		List<JSONObject> l = new ArrayList<JSONObject>();
		l.add(o2);
		try {
			JSONArray ja = new JSONArray(l);
			JSONObject request = new JSONObject();
			request.put("vmidList", ja);
			// System.out.println(request.toString());
			String url = ConfigurationLoader.getInstance().getProperty("monitor.query.url");
			WebResource r = client.resource(url);
			ClientResponse resp = r.accept(MediaType.APPLICATION_JSON, MediaType.APPLICATION_ATOM_XML).header("Content-Type", "application/json")
			.post(ClientResponse.class, request.toString());
			// System.out.println(resp.getStatus());
			System.out.println(resp.getEntity(String.class));
			String str = resp.getEntity(String.class);
			net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(str);
			net.sf.json.JSONArray jsonArray = json.getJSONArray("infoList");
			for (int i = 0; i < jsonArray.size(); i++) {// bug 0004046
				// int k = i % 3;
				// if (k == 0) {
				net.sf.json.JSONObject js = jsonArray.getJSONObject(i);
				VMMonitorInfo info = (VMMonitorInfo) net.sf.json.JSONObject.toBean(js, VMMonitorInfo.class);
				vmmolist.add(info);
				// }
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		teardown();
		return SUCCESS;
	}

	public List<Product> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<Product> returnList) {
		this.returnList = returnList;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public String getReturnJson() {
		return returnJson;
	}

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}

	public int getCountTotal() {
		return countTotal;
	}

	public void setCountTotal(int countTotal) {
		this.countTotal = countTotal;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Logger getLogger() {
		return logger;
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

	 

	public int getPart() {
		return part;
	}

	public void setPart(int part) {
		this.part = part;
	}

	public CloudServiceMallService getService() {
		return service;
	}

	public void setService(CloudServiceMallService service) {
		this.service = service;
	}

	public void setTemplateTypeList(List<TemplateTypeBO> templateTypeList) {
		this.templateTypeList = templateTypeList;
	}

	public List<TemplateTypeBO> getTemplateTypeList() {
		return templateTypeList;
	}

	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getSales() {
		return sales;
	}

	public void setSales(String sales) {
		this.sales = sales;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public List<TServiceInstanceBO> getRsList() {
		return rsList;
	}

	public void setRsList(List<TServiceInstanceBO> rsList) {
		this.rsList = rsList;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getRelationsJson() {
		return relationsJson;
	}

	public void setRelationsJson(String relationsJson) {
		this.relationsJson = relationsJson;
	}

	public String getResourceIDsJson() {
		return resourceIDsJson;
	}

	public void setResourceIDsJson(String resourceIDsJson) {
		this.resourceIDsJson = resourceIDsJson;
	}

	public String getTopIDArray() {
		return topIDArray;
	}

	public void setTopIDArray(String topIDArray) {
		this.topIDArray = topIDArray;
	}

	// ---------------------------added by
	// zhanghuizheng-----------------------------
	public String getTemplateConfigParams() {
		try {
			// String strType =
			// ServletActionContext.getRequest().getParameter("type");
			// int type = -1;
			// if(null != strType && !strType.equals("")){
			// type = Integer.parseInt(strType);
			// }
			configParams = new HashMap<String, String>();
			// 内存大小
			configParams.put("memorysize", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_MEMORYSIZE));
			// 存储类别
			// to fix bug:3535
			this.getAllStoreType();
			// configParams.put("storetype",
			// ParameterManager.getInstance().getString("dropdownlist.storetype"));
			// cpu个数
			configParams.put("cpunum", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_CPUNUM));
			// 价格区间
			configParams.put("pricerange", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_PRICERANGE));

			// configParams.put("cpuhz",
			// ParameterManager.getInstance().getString("dropdownlist.cpuhz_ghz"));
			// configParams.put("osname",
			// ParameterManager.getInstance().getString("dropdownlist.osname"));
			// configParams.put("curprojectid",
			// String.valueOf(ConstDef.getCurProjectId()));
			// 磁盘大小
			configParams.put("storagesize", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_STORAGESIZE));
			// 负载均衡的并发数下拉列表
			configParams.put("concurrentnum", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_LOADBALANCE_CONCURRENTNUM));
			// 防火墙的规则条数下拉列表
			configParams.put("rulenum", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_FIREWALL_RULES));
			// 带宽的大小下拉列表
			configParams.put("bandwidth", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_BANDWIDTH));
			//网卡个数
			configParams.put("networknum", ParameterManager.getInstance().getValueForCombox(ConstDef.COMBOX_NETWORKNUM));
			// int curProjectId = ConstDef.getCurProjectId();

		}
		catch (Exception e) {
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	/**
	 * 自服务门户根据用户所在的用户组查询所有的存储类型
	 */
	public void getAllStoreType() {
		// 此处需要根据用户所在的用户组查询对应的资源池，然后循环调用getAllStoreType方法，
		// 把用户所有的存储类型查出来，等待用户组的绑定资源池完成后
		// ？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？？
		int resourcePoolsId = 1;
		List<Map> list = elasterSerivce.getAllStoreType(resourcePoolsId);
		List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
		Map<String, String> dinstinctmap = new HashMap<String, String>();
		for (Map _storage : list) {
			String key = String.valueOf(_storage.get("tags"));
			if (!dinstinctmap.containsKey(key)) {
				dinstinctmap.put(key, key);
			}
		}

		if (null != dinstinctmap && dinstinctmap.size() > 0) {
			Set<String> _set = dinstinctmap.keySet();
			for (String _key : _set) {

				if (!_key.equals("")) {
					Map<String, String> optionMap = new HashMap<String, String>();
					optionMap.put("text", _key);
					optionMap.put("value", _key);
					reList.add(optionMap);
				}

			}
		}

		configParams.put("storetype", net.sf.json.JSONArray.fromObject(reList).toString());
	}

	
	public String getProductById() {
		try {
			String id = ServletActionContext.getRequest().getParameter("id");
			if (null != id && !id.equals("")) {
				product = service.getProductById(Integer.parseInt(id));
			}
		}
		catch (Exception e) {
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;

	}

	public ISysParametersService getParametersService() {
		return parametersService;
	}

	public void setParametersService(ISysParametersService parametersService) {
		this.parametersService = parametersService;
	}

	public List<Parameters> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Parameters> parameterList) {
		this.parameterList = parameterList;
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

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public List<TInstanceInfoBO> getInslist() {
		return inslist;
	}

	public void setInslist(List<TInstanceInfoBO> inslist) {
		this.inslist = inslist;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public int getServiceId() {
		return serviceId;
	}

	public void setServiceId(int serviceId) {
		this.serviceId = serviceId;
	}

	public String getPreUrl() {
		return preUrl;
	}

	public void setPreUrl(String preUrl) {
		this.preUrl = preUrl;
	}

	public long getVmid() {
		return vmid;
	}

	public void setVmid(long vmid) {
		this.vmid = vmid;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getStartTime() {
		return startTime;
	}

	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}

	public long getEndTime() {
		return endTime;
	}

	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}

	public String getResName() {
		return resName;
	}

	public void setResName(String resName) {
		this.resName = resName;
	}

	public List<VMMonitorInfo> getVmmolist() {
		return vmmolist;
	}

	public void setVmmolist(List<VMMonitorInfo> vmmolist) {
		this.vmmolist = vmmolist;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public IElasterSerivce getElasterSerivce() {
		return elasterSerivce;
	}

	public void setElasterSerivce(IElasterSerivce elasterSerivce) {
		this.elasterSerivce = elasterSerivce;
	}
	
	public IDBAsycnJobService getDbAsycnJobService() {
		return dbAsycnJobService;
	}

	public void setDbAsycnJobService(IDBAsycnJobService dbAsycnJobService) {
		this.dbAsycnJobService = dbAsycnJobService;
	}	

	/**
	 * 查询我的拓扑图数据
	 */
	public String getMyTopoStructData() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("~~~~~getMyTopoStructData===start===");
		}
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

		try {
			// 查询我的拓扑图数据
			ResourceTopoVO topo = service.findMyTopoStructData(user);
			// 转换为json格式
			returnJson = net.sf.json.JSONObject.fromObject(topo).toString();

			// 替换fromId为from，替换toId为to
			String relationsJson = topo.getRelationsJson();
			String resourceIDsJson = topo.getResourceIDsJson();
			String topIDArray = topo.getTopIDArray();
			String resPlatformURL = topo.getResPlatformURL();
			String portalURL = topo.getTopoPortalURL();
			// 使用session传递拓扑图参数
			this.getSession().setAttribute("resPlatformURL", resPlatformURL);
			this.getSession().setAttribute("relationsJson", relationsJson);
			this.getSession().setAttribute("resourceIDsJson", resourceIDsJson);
			this.getSession().setAttribute("topIDArray", topIDArray);
			this.getSession().setAttribute("portalURL", portalURL);

			if (logger.isDebugEnabled()) {
				logger.debug("~~~~~getMyTopoStructData===returnJson=" + returnJson);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("~~~~~getMyTopoStructData===end===");
		}

		return SUCCESS;
	}

	/**
	 * 查询我的监控服务总购买有效数量
	 */
	public String getMyMonitorCount() throws Exception {
		if (logger.isDebugEnabled()) {
			logger.debug("~~~~~getMyMonitorCount===start===");
		}
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);

		try {
			// 查询我的拓扑图数据
			// if(StringUtils.isEmpty(key)){ key = "cnt"; }
			if (StringUtils.isEmpty(typeId)) {
				typeId = "5";
			}
			if(ConstDef.getCloudId() == 2){//私有云
				countTotal = 1;//fix bug 7404 私有云默认有监控服务
			}else{
				// if(StringUtils.isEmpty(state)){ state = "2"; }//??
				countTotal = service.getAllServiceCount(user, key, typeId, state, start, end);
				if (countTotal <= 0) {// 没有有效的云监控，判断是否存在正在退订 fix bug 3710
					countTotal = service.getAllServiceCount(user, key, typeId, "5", start, end);
				}
			}
			// listResp.put("total", countTotal);
			returnJson = "" + countTotal;
			if (logger.isDebugEnabled()) {
				logger.debug("~~~~~getMyMonitorCount===returnJson=" + returnJson);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		if (logger.isDebugEnabled()) {
			logger.debug("~~~~~getMyMonitorCount===end===");
		}

		return SUCCESS;
	}

}
