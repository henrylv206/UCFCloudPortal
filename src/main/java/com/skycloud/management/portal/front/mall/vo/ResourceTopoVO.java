package com.skycloud.management.portal.front.mall.vo;

import java.io.Serializable;

/**
 * 我的拓扑图数据VO
 * @author ninghao
 * @time 2012-09-05
 * @version 1.2
 */
public class ResourceTopoVO implements Serializable 
{
	private static final long serialVersionUID = 5183426407900666888L;

	private String resPlatformURL; // 资源管理平台的地址
	private String resourceIDsJson;// 设备ID列表的json数据
	private String relationsJson;  // 关系的json数据
	private String topIDArray;     // 全部的连接顶点
	private String topoPortalURL;  // Portal的地址
	  
	public String getResPlatformURL() {
		return resPlatformURL;
	}
	public void setResPlatformURL(String resPlatformURL) {
		this.resPlatformURL = resPlatformURL;
	}
	public String getResourceIDsJson() {
		return resourceIDsJson;
	}
	public void setResourceIDsJson(String resourceIDsJson) {
		this.resourceIDsJson = resourceIDsJson;
	}
	public String getRelationsJson() {
		return relationsJson;
	}
	public void setRelationsJson(String relationsJson) {
		this.relationsJson = relationsJson;
	}
	public String getTopIDArray() {
		return topIDArray;
	}
	public void setTopIDArray(String topIDArray) {
		this.topIDArray = topIDArray;
	}
	public String getTopoPortalURL() {
		return topoPortalURL;
	}
	public void setTopoPortalURL(String topoPortalURL) {
		this.topoPortalURL = topoPortalURL;
	}
	  
}
