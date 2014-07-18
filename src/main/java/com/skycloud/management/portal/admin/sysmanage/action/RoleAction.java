package com.skycloud.management.portal.admin.sysmanage.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;
import com.skycloud.management.portal.admin.sysmanage.service.IRoleManageService;

public class RoleAction extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(RoleAction.class);
	private IRoleManageService roleService;
	private String returnJson; // 符合Json格式的语法的数组字符串，每条数组内容为一个TAudit对象
	private TRoleBO role;
	private List<TRoleBO> returnList;
	private int roleId;
	private String jsonStr;
	private String state;
	private String roleName;
	private QueryCriteria criteria;
	
	

	public QueryCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(QueryCriteria criteria) {
		this.criteria = criteria;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public IRoleManageService getRoleService() {
		return roleService;
	}

	public void setRoleService(IRoleManageService roleService) {
		this.roleService = roleService;
	}

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}

	public String getReturnJson() {
		return returnJson;
	}

	public TRoleBO getRole() {
		return role;
	}

	public void setRole(TRoleBO role) {
		this.role = role;
	}
	
	public void setReturnList(List<TRoleBO> returnList) {
		this.returnList = returnList;
	}

	public List<TRoleBO> getReturnList() {
		return returnList;
	}
	 
	
	public String findAllRole() throws Exception {
		try {
			returnList=roleService.findAllRole();			
			return SUCCESS;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			returnJson = "error : " + e.getMessage();
		}
		return "ERROR";
	}
	 
	  

}
