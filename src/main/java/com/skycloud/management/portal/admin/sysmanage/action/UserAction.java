package com.skycloud.management.portal.admin.sysmanage.action;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.front.log.aop.LogInfo;

public class UserAction extends ActionSupport{

	
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(UserAction.class);
	private IUserManageService userService;
	private String returnJson; // 符合Json格式的语法的数组字符串
	private List<TUserBO> returnList;
	private TUserBO user;
	private String jsonStr;
	private String state;
	private int userId;
	private String account;
	private int resPoolId;
	private String startLastUpdate;
	private String endLastUpdate;
	private String email;
	private List<Map<String, Object>> listResp;
	
	@LogInfo(desc="显示指定用户详细信息",moduleName="系统管理-用户管理",functionName="用户详情",operateType=4,parameters="userId")
	public String findUserById() throws Exception {
		try {
			user=userService.getUserById(userId);
			returnList=new ArrayList<TUserBO>();
			returnList.add(user);
			logger.info("query the user by userId ,userId="+userId);
			return SUCCESS;
		} catch (Exception e) {
			returnJson="error : "+e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}
 
	@LogInfo(desc="检查用户登录名称是否重复",moduleName="系统管理-用户管理",functionName="检查用户登录名",operateType=4,parameters="account")
	public String findUserByAccout()throws Exception {
		try {
			if(account!=null){
				account=account.trim();
			}
			user=userService.getUserByAccout(account);
			return SUCCESS;
		} catch (Exception e) {
			returnJson="error : "+e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}
	 
	  
	public void setUserService(IUserManageService userService) {
		this.userService = userService;
	}
	public IUserManageService getUserService() {
		return userService;
	}
	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}
	public String getReturnJson() {
		return returnJson;
	}
	
	public List<TUserBO> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<TUserBO> returnList) {
		this.returnList = returnList;
	}

	public void setUser(TUserBO user) {
		this.user = user;
	}

	public TUserBO getUser() {
		return user;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public int getResPoolId() {
		return resPoolId;
	}

	public void setResPoolId(int resPoolId) {
		this.resPoolId = resPoolId;
	}

	public String getStartLastUpdate() {
		return startLastUpdate;
	}

	public void setStartLastUpdate(String startLastUpdate) {
		this.startLastUpdate = startLastUpdate;
	}

	public String getEndLastUpdate() {
		return endLastUpdate;
	}

	public void setEndLastUpdate(String endLastUpdate) {
		this.endLastUpdate = endLastUpdate;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Map<String, Object>> getListResp() {
		return listResp;
	}

	public void setListResp(List<Map<String, Object>> listResp) {
		this.listResp = listResp;
	}
	
	
}
