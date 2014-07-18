package com.skycloud.management.portal.admin.sysmanage.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IDeptManageService;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;
import com.skycloud.management.portal.front.log.aop.LogInfo;

public class DeptAction extends ActionSupport{
	private static final long serialVersionUID = -5324795640045682682L;
	private Logger logger=Logger.getLogger(DeptAction.class);
	private IDeptManageService deptService;
	private List<TDeptBO> returnList;
	private List<TUserBO> userList;
	private QueryCriteria criteria;
	private TDeptBO dept;
	private String state;
	private String jsonStr;
	private String returnJson; // 符合Json格式的语法的数组字符串
	private int deptId;
	private List<ResPoolDeptRelationVO> deptPoolList;
	
	public List<ResPoolDeptRelationVO> getDeptPoolList() {
		return deptPoolList;
	}

	public void setDeptPoolList(List<ResPoolDeptRelationVO> deptPoolList) {
		this.deptPoolList = deptPoolList;
	}

	public QueryCriteria getCriteria() {
		return criteria;
	}

	public void setCriteria(QueryCriteria criteria) {
		this.criteria = criteria;
	}
	
	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public TDeptBO getDept() {
		return dept;
	}

	public void setDept(TDeptBO dept) {
		this.dept = dept;
	}

	public int getDeptId() {
		return deptId;
	}

	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	@LogInfo(desc="部门管理列表",moduleName="系统管理-部门管理",functionName="部门列表",operateType=4)
	public String findAllDept() throws Exception {
		try {
			setReturnList(deptService.getAllDept());
			logger.info("list all departments"); 
			return SUCCESS;
		} catch (Exception e) {
			returnJson="error : "+e.getMessage();
			logger.error(e.getMessage());
		}
		return ERROR;
	}
     

	public void setReturnList(List<TDeptBO> returnList) {
		this.returnList = returnList;
	}

	public List<TDeptBO> getReturnList() {
		return returnList;
	}

	public IDeptManageService getDeptService() {
		return deptService;
	}

	public void setDeptService(IDeptManageService deptService) {
		this.deptService = deptService;
	}

	public String getReturnJson() {
		return returnJson;
	}

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}

	public void setUserList(List<TUserBO> userList) {
		this.userList = userList;
	}

	public List<TUserBO> getUserList() {
		return userList;
	}
	
	
}
