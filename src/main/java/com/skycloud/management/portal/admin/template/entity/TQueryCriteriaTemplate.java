package com.skycloud.management.portal.admin.template.entity;

public class TQueryCriteriaTemplate {
	private String templateName;
	private int CPUNum = -1;
	private int memorySize = -1;
	private int storageSize = -1;

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public int getCPUNum() {
		return CPUNum;
	}

	public void setCPUNum(int cPUNum) {
		CPUNum = cPUNum;
	}

	public int getMemorySize() {
		return memorySize;
	}

	public void setMemorySize(int memorySize) {
		this.memorySize = memorySize;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

}
