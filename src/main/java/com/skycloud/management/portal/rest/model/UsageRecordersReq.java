package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author WangHaiDong
 * @version 2012-2-1 17:25
 */
@XmlRootElement
public class UsageRecordersReq {
	
	
	private Integer resourceId;	//资源ID	
	private Integer resourceType;//资源类型
	
	
	public Integer getResourceId() {
		return resourceId;
	}
	public void setResourceId(Integer resourceId) {
		this.resourceId = resourceId;
	}
	public Integer getResourceType() {
		return resourceType;
	}
	public void setResourceType(Integer resourceType) {
		this.resourceType = resourceType;
	}
	
}
