package com.skycloud.management.portal.admin.productitem.action;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.productitem.entity.ListProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemBO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemFrontBO;
import com.skycloud.management.portal.admin.productitem.service.IProductItemService;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.log.aop.LogInfo;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;


public class ProductItemAction extends ActionSupport {
	private Logger logger = Logger.getLogger(ProductItemAction.class);
	private IProductItemService productItemService;
	private String queryJson;
	private String message;
	private Map<String, Object> listResp;
	private String jsonResult;
	private int parentNodeId;
	private TProductItemBO productItem;
	private int pId;
	private String itemName;
	private int level;
	private String code;
	private int itemId;
	private int nodeType;
	private String releasedIds;
	private JSONObject jsonRoot;
	//private IProductService productService;
	private int curPage = 1;
	private int pageSize = 10;
	private String jsonStr;
	private File upload;
	private int state;
//	private String serviceTypes;
	private List<TProductItemFrontBO> productItemList;
	//private ISysParametersService parametersService;
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}
   
	  
	
	/**
	 * 
	 * 方法描述：组织前台根节点的json数据
	 * 
	 * @param root
	 * @return
	 * @throws Exception
	 *             创建人： 张慧征 创建时间：Feb 19, 2012 4:53:22 PM
	 */
	public JSONObject getRootJSONFront(TProductItemFrontBO root) throws Exception {
		if (root == null) {
			return new JSONObject();
		}
		JSONObject jsonObj = null;
		JSONObject jsonAttr = null;
		jsonObj = new JSONObject();
		jsonAttr = new JSONObject();
		int nodeId = root.getId();
		jsonObj.put("data", root.getName());
		jsonAttr.put("id", nodeId);
		jsonAttr.put("parentId", root.getParentId());
		jsonAttr.put("code", root.getCode());
		jsonAttr.put("level", root.getLevel());
		jsonAttr.put("isFolder", 0);
		jsonAttr.put("rel", "drive");
//		jsonObj.put("class", "jstree-checked");
		jsonObj.put("attr", jsonAttr);
		jsonObj.put("state", "closed");
		return jsonObj;
	}

	/**
	 * 
	 * 方法描述：组织根节点的json数据
	 * 
	 * @param root
	 * @return
	 * @throws Exception
	 *             创建人： 张慧征 创建时间：Feb 19, 2012 4:53:22 PM
	 */
	public JSONObject getRootJSON(TProductItemBO root) throws Exception {
		if (root == null) {
			return new JSONObject();
		}
		JSONObject jsonObj = null;
		JSONObject jsonAttr = null;
		jsonObj = new JSONObject();
		jsonAttr = new JSONObject();
		int nodeId = root.getId();
		jsonObj.put("data", root.getName());
		jsonAttr.put("id", nodeId);
		jsonAttr.put("parentId", root.getParentId());
		jsonAttr.put("code", root.getCode());
		jsonAttr.put("level", root.getLevel());
		jsonAttr.put("isFolder", 0);
		jsonAttr.put("rel", "drive");//folder
//		jsonObj.put("class", "jstree-checked");
		jsonObj.put("attr", jsonAttr);
		jsonObj.put("state", "closed");
		return jsonObj;
	}
  
	
	/**
	 * 前台用 获取节点的子节点
	 * @return
	 */
	@LogInfo(desc="前台查询服务目录的子目录",operateType=1,moduleName="服务目录查询",functionName="前台查询子目录",parameters="parentNodeId")
	public String loadFrontSubTree() {
		try {
			List<TProductItemFrontBO> list = this.productItemService.listProductItemFront(parentNodeId);
			jsonResult = this.getJSONArrayFront(list).toString();
			
		} catch (Exception e) {
			e.printStackTrace();
//			message = "error : " + e.getMessage();
//			logger.error(e.getMessage());
//			return ERROR;
		}
		return this.outputJsonData(jsonResult);
	}
	
	public String outputJsonData(String jsonData) {
		HttpServletResponse response = ServletActionContext.getResponse();
		this.renderText(response, jsonData);
		return null;
	}

/**
 * 
 * 方法描述：通过相应对象向JSP页面输出数据
 * @param response
 * @param text
 * 创建人：   张慧征    
 * 创建时间：Feb 19, 2012  5:26:42 PM
 */
	public static void renderText(HttpServletResponse response, String text)
	{
		render(response, text, "text/plain;charset=UTF-8");
	}
	
/**
 * 
 * 方法描述：通过相应对象向JSP页面输出数据
 * @param response
 * @param text
 * @param contentType
 * 创建人：   张慧征    
 * 创建时间：Feb 19, 2012  5:26:25 PM
 */
	private static void render(HttpServletResponse response, String text,
			String contentType)
	{
		try
		{
			response.setContentType(contentType);
			response.getWriter().write(text);
		} catch (IOException e)
		{
		}
	}

	/**
	 * 
	 * 方法描述：组织节点的json数据
	 * 
	 * @param resList
	 * @return 
	 *  创建人： 张慧征 创建时间：Feb 19, 2012 5:15:00 PM
	 */
	public JSONArray getJSONArray(List<TProductItemBO> resList) {
		if (resList == null) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		JSONObject jsonAttr = null;
		for (int i = 0; i < resList.size(); i++) {
			TProductItemBO item = resList.get(i);
			jsonObj = new JSONObject();
			jsonAttr = new JSONObject();
			int nodeId = item.getId();
			jsonObj.put("data", item.getName());
			jsonAttr.put("id", nodeId);
			jsonAttr.put("parentId", item.getParentId());
			jsonAttr.put("code", item.getCode());
			jsonAttr.put("level", item.getLevel());
//			if(item.getReleaseOrNot()==1){
//				jsonObj.put("class", "jstree-checked");
//			}else{
//				jsonObj.put("class", "jstree-unchecked jstree-undetermined");
//			}

			// 如果是2,表示该节点是一个子节点
			if (item.getNodeType() == 1) {
				jsonAttr.put("isFolder", 1);
				jsonAttr.put("rel", "default");
				jsonObj.put("state", "opened");
			} else if (item.getNodeType() == 0) {
				jsonAttr.put("isFolder", 0);
				jsonAttr.put("rel", "folder");
				jsonObj.put("state", "closed");
			}
			jsonObj.put("attr", jsonAttr);
			jsonArray.add(jsonObj);
		}
		return jsonArray;
	}
	
	/**
	 * 
	 * 方法描述：组织前台节点的json数据
	 * 
	 * @param resList
	 * @return 
	 *  创建人： 张慧征 创建时间：Feb 19, 2012 5:15:00 PM
	 */
	public JSONArray getJSONArrayFront(List<TProductItemFrontBO> resList) {
		if (resList == null) {
			return new JSONArray();
		}
		JSONArray jsonArray = new JSONArray();
		JSONObject jsonObj = null;
		JSONObject jsonAttr = null;
		for (int i = 0; i < resList.size(); i++) {
			TProductItemFrontBO item = resList.get(i);
			jsonObj = new JSONObject();
			jsonAttr = new JSONObject();
			int nodeId = item.getId();
			jsonObj.put("data", item.getName());
			jsonAttr.put("id", nodeId);
			jsonAttr.put("parentId", item.getParentId());
			jsonAttr.put("code", item.getCode());
			jsonAttr.put("level", item.getLevel());
//			if(item.getReleaseOrNot()==1){
//				jsonObj.put("class", "jstree-checked");
//			}else{
//				jsonObj.put("class", "jstree-unchecked jstree-undetermined");
//			}

			// 如果是2,表示该节点是一个子节点
			if (item.getNodeType() == 1) {
				jsonAttr.put("isFolder", 1);
				jsonAttr.put("rel", "default");
				jsonObj.put("state", "opened");
			} else if (item.getNodeType() == 0) {
				jsonAttr.put("isFolder", 0);
				jsonAttr.put("rel", "folder");
				jsonObj.put("state", "closed");
			}
			jsonObj.put("attr", jsonAttr);
			jsonArray.add(jsonObj);
		}
		return jsonArray;
	}
    
	 
	
	@LogInfo(operateType=3,moduleName="服务目录管理",functionName="服务绑定",parameters="productIds")
	public String bindProduct(){
		 Map<String, String> map = null;
		if(StringUtils.isNotBlank(getQueryJson())) {
			map = JsonUtil.getMap4Json(getQueryJson());
			if(map != null && map.size() > 0) {
				int productItemId = Integer.parseInt(map.get("productItemId"));
				String productIds = map.containsKey("productIds") ? String.valueOf(map.get("productIds")) : "";
				String currentAllProductIds = map.containsKey("currentAllProductIds") ? String.valueOf(map.get("currentAllProductIds")) : "";
				try {
					this.productItemService.updateProductItemRelation(Integer.parseInt(map.get("productItemId")), productIds,currentAllProductIds);
				} catch (Exception e) {
					message = "error : " + e.getMessage();
			        logger.error(e.getMessage());
					e.printStackTrace();
					return ERROR;
				}
				message = "ok : create succeed";
			}else{
				message = "error : no parameters";
		        return ERROR;
			}
		}else{
			message = "error : no parameters";
	        return ERROR;
		}
		return SUCCESS;
	}
   
	
	@LogInfo(operateType=1,moduleName="服务目录管理",functionName="导入服务目录",parameters="")
	public String createXmlProductItems(){
		XStream xstream = new XStream(new DomDriver());
		xstream.alias("service", ListProductItemXmlPO.class);
	    xstream.alias("item", ProductItemXmlPO.class);
		xstream.aliasField("itemList", ListProductItemXmlPO.class, "itemList");
		try {
//	    xstream.addImplicitCollection(ListProductItemXmlPO.class, "productItemXmlPOList");
			/*
			 * to fix bug:1531
			 */
			StringBuffer sbf = new StringBuffer();
			File f = getUpload();
			InputStreamReader isr=new InputStreamReader(new FileInputStream(f),"UTF-8");
			BufferedReader bReader = new BufferedReader(isr);
//			BufferedReader bReader = new BufferedReader(new FileReader(f));

			String line = null;
			while((line=bReader.readLine())!=null){
//				sbf.append(new String(line.getBytes("GBK"), "UTF-8"));
				sbf.append(line);
				sbf.append("\n");
				logger.info(line);
			}
			ListProductItemXmlPO listProductItemXmlPO = (ListProductItemXmlPO) xstream.fromXML(sbf.toString());
			List<ProductItemXmlPO> list = listProductItemXmlPO.getItemList();	
			this.productItemService.insertXmlProductItems(list);
			
			ServletActionContext.getResponse().setContentType("text/plain");
			ServletActionContext.getResponse().getWriter().write("success");
//			message = "ok : import succeed";
		} catch (Exception e) {
			e.printStackTrace();
//			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
//			return ERROR;
		}
		return null;	
	}
	
	@LogInfo(operateType=4,moduleName="服务目录管理",functionName="导出服务目录",parameters="")
	public InputStream getXmlProductItems(){
		List<ProductItemXmlPO> list;
		String xmlstr = "";
		ByteArrayInputStream inputStream = null;
		try {
			list = this.productItemService.listProductItemForExport();
			if(null != list && list.size()>0){
			    XStream xstream = new XStream(new  DomDriver("UTF-8"));
			    ListProductItemXmlPO productItemList = new ListProductItemXmlPO();
			    productItemList.setItemList(list);
			    xstream.alias("item", ProductItemXmlPO.class);
			    xstream.alias("service", ListProductItemXmlPO.class);
			    xmlstr = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
			    	+"<!-- 系统默认的根节点id为1,不需要导入导出,不在导出文件中,导入文件中也不需要设置," 
			    	+"因此请不要在导入文件中设置任何一个节点的id的值为1,可以设置节点的父节点(parentId)为1," 
			    	+"表示该节点是根节点的子节点,如果不设置任何节点的父节点(parentId)为1,那么服务目录将导入失败," 
			    	+"不能按期望的显示为一棵树,只显示树的根节点。请不要对一级目录（例如弹性计算服务）和二级（例如虚拟机）目录做修改或删除，"
			    	+"即id小于23的目录都不要修改，删除，因为这些一级和二级目录是系统初始化数据，修改或者删除会引起导入失败。 -->\n"
			    	+xstream.toXML(productItemList);
			    
//			    message = "ok : export succeed";
			}else{
//				message = "error : 0";
			}
			inputStream = new ByteArrayInputStream(xmlstr.getBytes("UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
//			message = "error : " + e.getMessage();
//			logger.error(e.getMessage());
//			return ERROR;
		}

		return inputStream;
	}
	
	/**
	 * 服务目录审核
	 * @return
	 */
	@LogInfo(operateType=3,moduleName="服务目录管理",functionName="审核服务目录",parameters="")
	public String aduitProductItem(){
		int state = Integer.parseInt(ServletActionContext.getRequest().getParameter("state"));
		try {
			this.productItemService.updateProductItem(state);
			message = "ok : update succeed";
		} catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	public IProductItemService getProductItemService() {
		return productItemService;
	}

	public void setProductItemService(IProductItemService productItemService) {
		this.productItemService = productItemService;
	}

	public String getQueryJson() {
		return queryJson;
	}

	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public String getJsonResult() {
		return jsonResult;
	}

	public void setJsonResult(String jsonResult) {
		this.jsonResult = jsonResult;
	}

	public int getParentNodeId() {
		return parentNodeId;
	}

	public void setParentNodeId(int parentNodeId) {
		this.parentNodeId = parentNodeId;
	}

	public TProductItemBO getProductItem() {
		return productItem;
	}

	public void setProductItem(TProductItemBO productItem) {
		this.productItem = productItem;
	}

	public int getPId() {
		return pId;
	}

	public void setPId(int id) {
		pId = id;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public int getNodeType() {
		return nodeType;
	}

	public void setNodeType(int nodeType) {
		this.nodeType = nodeType;
	}

	public String getReleasedIds() {
		return releasedIds;
	}

	public void setReleasedIds(String releasedIds) {
		this.releasedIds = releasedIds;
	}

	public JSONObject getJsonRoot() {
		return jsonRoot;
	}

	public void setJsonRoot(JSONObject jsonRoot) {
		this.jsonRoot = jsonRoot;
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

	public File getUpload() {
		return upload;
	}

	public void setUpload(File upload) {
		this.upload = upload;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

//	public String getServiceTypes() {
//		return serviceTypes;
//	}
//
//	public void setServiceTypes(String serviceTypes) {
//		this.serviceTypes = serviceTypes;
//	}

	 
	public List<TProductItemFrontBO> getProductItemList() {
		return productItemList;
	}

	public void setProductItemList(List<TProductItemFrontBO> productItemList) {
		this.productItemList = productItemList;
	}	

	public String loadFrontAllItems(){
		try {
			productItemList = this.productItemService.listProductItemFront(-1);
			for (TProductItemFrontBO _menu : productItemList) {
				int parentid = _menu.getParentId();
				if(parentid > 0){
					for (TProductItemFrontBO _menu2 : productItemList) {
						int menuId = _menu2.getId();
						if (menuId == parentid) {
							_menu2.getChildren().add(_menu);
						}
					}					
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
			
		}
		return SUCCESS;
	}

}
