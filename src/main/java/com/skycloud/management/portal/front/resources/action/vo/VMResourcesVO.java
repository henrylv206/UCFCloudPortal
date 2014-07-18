package com.skycloud.management.portal.front.resources.action.vo;

public class VMResourcesVO extends ResourcesVO {
	//1:虚拟机;2:块存储	
	private int instance_type;

	public int getInstance_type() {
		return instance_type;
	}

	public void setInstanceType(int instance_type) {
		this.instance_type = instance_type;
	}
	
}
