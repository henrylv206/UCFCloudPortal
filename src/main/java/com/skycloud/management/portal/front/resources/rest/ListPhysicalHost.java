package com.skycloud.management.portal.front.resources.rest;


public class ListPhysicalHost extends RestfulPath{

	
	public static final String PATH = "/rest/phyhost/listHosts";

	
	public static final String ID = "id";
	public static final String NAME = "name";
	public static final String IPMIIP = "ipmiip";
	public static final String ZONEID = "zoneid";
	public static final String PODID = "podid";
	public static final String CLUSTERID = "clusterid";
	public static final String USERNAME = "username";
	public static final String STATE = "state";
	
	private long id;//物理机ID
	private int zoneId;//物理机资源域ID
	private String name;//物理机主机名称
	private long clusterId;//物理机集群ID
	private String ipmiip;//IPMI IP地址
	private long podId;//物理机机架ID
	private String username;//物理机所属的用户名称
	private String state;//操作系统状态

	public ListPhysicalHost() {
	    super(PATH);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
		this.setParameter(CLUSTERID, clusterId);
	}

	public String getIpmiip() {
		return ipmiip;
	}

	public void setIpmiip(String ipmiip) {
		this.ipmiip = ipmiip;
		this.setParameter(IPMIIP, ipmiip);
	}

	public long getPodId() {
		return podId;
	}

	public void setPodId(long podId) {
		this.podId = podId;
		this.setParameter(PODID, podId);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
		this.setParameter(USERNAME, username);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.setParameter(STATE, state);
	}
	
}
