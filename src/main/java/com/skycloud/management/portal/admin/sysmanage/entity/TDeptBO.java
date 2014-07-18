package com.skycloud.management.portal.admin.sysmanage.entity;

public class TDeptBO {
	private int deptId;//部门ID
	private String deptName;//部门名称
	private String deptDesc;//部门描述
	
	//	前台页面穿数据
	private String oldDeptName;

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public String getDeptDesc() {
		return deptDesc;
	}

	public void setDeptDesc(String deptDesc) {
		this.deptDesc = deptDesc;
	}



	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}



	public int getDeptId() {
		return deptId;
	}

	public void setOldDeptName(String oldDeptName) {
		this.oldDeptName = oldDeptName;
	}

	public String getOldDeptName() {
		return oldDeptName;
	}
	
	
}
