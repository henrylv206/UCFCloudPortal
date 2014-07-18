package com.skycloud.management.portal.front.mall.vo;

import java.io.Serializable;

/**
 * 我的拓扑图数据的资源VO
 * @author ninghao
 * @time 2012-09-05
 * @version 1.2
 */
public class ResourceVO implements Serializable 
{
	private static final long serialVersionUID = 5183426407900666111L;

	private int sid;// 资源编 号
	private int id;	// 资源ID
	private String name;// 资源名称
	private ResourceVO host;// 物理设备ID
	private String type;// 取 值： FIREWALL、VLAN、VM、LOADBANCE
	private int poolid;	// 资源池ID
	
	
	public int getSid() {
		return sid;
	}
	public void setSid(int sid) {
		this.sid = sid;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ResourceVO getHost() {
		return host;
	}
	public void setHost(ResourceVO host) {
		this.host = host;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getPoolid() {
		return poolid;
	}
	public void setPoolid(int poolid) {
		this.poolid = poolid;
	}
	
}
