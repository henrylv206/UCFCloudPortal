package com.skycloud.management.portal.common.action;


import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.common.utils.Page;
 
@SuppressWarnings("serial")
public class BaseAction extends ActionSupport {
  
  private static Log log = LogFactory.getLog(BaseAction.class);
  
	protected String TASK_FORM_URL = "taskFormURL";
	public static final String ILLEGAL_ARGUMENT = "illegalArgument";
	public static final String VM_RESPONSE = "vmResponse";
	public static final String MESSAGE = "message";
	public static final String PAGE_NO = "page";
	public static final String PAGE_SIZE = "pagesize";
	protected int page = 1;
	protected int pagesize = Page.PAGE_SIZE;
	//1.3功能，支持多资源池
//	private int resourcePoolsId;  //资源池
//	private int zoneId;			  //资源域
	
	/**
	 * 获取HttpSession对象
	 * @return
	 */
	public HttpSession getSession() {
		//ActionContext.getContext().getSession();
		return ServletActionContext.getRequest().getSession();
	}
	public HttpSession getSession(boolean arg) {
		//ActionContext.getContext().getSession();
		return ServletActionContext.getRequest().getSession(arg);
	}
	
	/**
	 * 获取HttpServletResponse对象
	 * @return
	 */
	public HttpServletResponse getResponse(){
		HttpServletResponse response = ServletActionContext.getResponse();
		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("Pragma","No-cache");//HTTP 1.1 
		response.setHeader("Cache-Control","no-cache");//HTTP 1.0
		response.setHeader("Expires","0");
		return response;
	}
	
	/**
	 * 获取HttpServletRequest对象
	 * @return
	 */
	public HttpServletRequest getRequest(){
		return ServletActionContext.getRequest();
	}
	  
	@SuppressWarnings("rawtypes")
	public Page getPageParameter(){
		return new Page(this.getPage(), this.getPagesize());
	}
	protected void printRequestParameters(){
		HttpServletRequest req = this.getRequest();
		Enumeration<String> enu = req.getParameterNames();
		String key = null;
		while(enu.hasMoreElements()){
			key = enu.nextElement();
			log.info(key + " \t" + req.getParameter(key));
		}
	} 
	public String save(){
		return SUCCESS;
	}
	public String update(){
		return SUCCESS;
	}
	public String delete(){
		return SUCCESS;
	}
	public String findById(){
		return SUCCESS;
	}
	public String findAll(){
		return SUCCESS;
	} 
	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}
//	public int getResourcePoolsId() {
//		return resourcePoolsId;
//	}
//	public void setResourcePoolsId(int resourcePoolsId) {
//		this.resourcePoolsId = resourcePoolsId;
//	}
//	public int getZoneId() {
//		return zoneId;
//	}
//	public void setZoneId(int zoneId) {
//		this.zoneId = zoneId;
//	}
	
}
