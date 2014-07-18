package com.skycloud.management.portal.admin.sysmanage.entity;

import java.util.Date;

public class TDeptPoolBO {
	private int id;//部门与资源关系ID
	private int deptId;//部门ID
	private int resourcePoolId;//资源池ID
	private Date createDt;   //创建时间
	private Date lastupdateDt;//修改时间
	private String state;//状态，1：正常；2：作废


	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}

	public int getDeptId() {
		return deptId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getResourcePoolId() {
		return resourcePoolId;
	}

	public void setResourcePoolId(int resourcePoolId) {
		this.resourcePoolId = resourcePoolId;
	}

	public Date getLastupdateDt() {
		return lastupdateDt;
	}

	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

}
