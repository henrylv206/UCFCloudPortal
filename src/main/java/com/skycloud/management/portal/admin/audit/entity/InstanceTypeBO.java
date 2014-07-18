package com.skycloud.management.portal.admin.audit.entity;

public class InstanceTypeBO {
	private String name;//下订单人
	private String deptName;//下订单人所在部门
	private int templateType;//模板类型
	private int storageSize;//存储大小
	private String email;
	private String resourceInfo;//实例表中Json数据，业务扩充用
	private int instanceType;//标记实例类型，1为虚拟机，2为小型机，3为磁盘
	private int id;
	private int templateId;//模板Id //hfk add 13-1-12 修改资源实例时要修改模板ID
	private int userId;
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDeptName() {
		return deptName;
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public int getInstanceType() {
		return instanceType;
	}

	public void setInstanceType(int instanceType) {
		this.instanceType = instanceType;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setResourceInfo(String resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public String getResourceInfo() {
		return resourceInfo;
	}

    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }


    public int getTemplateId() {
    	return templateId;
    }


    public void setTemplateId(int templateId) {
    	this.templateId = templateId;
    }


    public int getUserId() {
    	return userId;
    }


    public void setUserId(int userId) {
    	this.userId = userId;
    }



}
