package com.skycloud.management.portal.admin.sysmanage.vo;

import java.io.Serializable;

/**
 * 部门与资源池的关系VO
 * @author ninghao@chinaskycloud.com
 * @time 2012-12-06
 * @version 1.3
 */
public class ResPoolDeptRelationVO implements Serializable 
{
	private static final long serialVersionUID = 5183426407900666223L;

	private int id; 	 	// 关系编号
	private int deptId;	 	// 部门ID
	private int poolId;  	// 资源池ID
	private String poolName;// 资源池名称
	private String check;	// 是否已分配
	//其他临时变量
	private int resourceId;//资源ID
	private String resourceName;//资源名称
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDeptId() {
		return deptId;
	}
	public void setDeptId(int deptId) {
		this.deptId = deptId;
	}
	public int getPoolId() {
		return poolId;
	}
	public void setPoolId(int poolId) {
		this.poolId = poolId;
	}
	public String getCheck() {
		return check;
	}
	public void setCheck(String check) {
		this.check = check;
	}
	public String getPoolName() {
		return poolName;
	}
	public void setPoolName(String poolName) {
		this.poolName = poolName;
	}

	public int getResourceId() {
		return resourceId;
	}

	public void setResourceId(int resourceId) {
		this.resourceId = resourceId;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}
	
}
