package com.skycloud.management.portal.front.loadbalance.vo;

public class PortalLoadBalaneVO {

	private int instanceId;

	private String loadbalanceName;

	private String publicPort;

	private String privatePort;

	private String throughPut;

	private String conNumber;

	private String algorithm;

	private String virtualServiceName;

	private String serverGroupName;

	private String serverName;

	private String state;

	private String createDt;

	private String updateDt;

	private String lbProtocol;

	private String lbPolicy;

	private int lbPort;

	private int resourcePoolsId;

	private int zoneId;

	private int special;

	public String getLoadbalanceName() {
		return loadbalanceName;
	}

	public void setLoadbalanceName(String loadbalanceName) {
		this.loadbalanceName = loadbalanceName;
	}

	public String getPublicPort() {
		return publicPort;
	}

	public void setPublicPort(String publicPort) {
		this.publicPort = publicPort;
	}

	public String getPrivatePort() {
		return privatePort;
	}

	public void setPrivatePort(String privatePort) {
		this.privatePort = privatePort;
	}

	public String getThroughPut() {
		return throughPut;
	}

	public void setThroughPut(String throughPut) {
		this.throughPut = throughPut;
	}

	public String getConNumber() {
		return conNumber;
	}

	public void setConNumber(String conNumber) {
		this.conNumber = conNumber;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public String getVirtualServiceName() {
		return virtualServiceName;
	}

	public void setVirtualServiceName(String virtualServiceName) {
		this.virtualServiceName = virtualServiceName;
	}

	public String getServerGroupName() {
		return serverGroupName;
	}

	public void setServerGroupName(String serverGroupName) {
		this.serverGroupName = serverGroupName;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}

	public String getLbProtocol() {
		return lbProtocol;
	}

	public void setLbProtocol(String lbProtocol) {
		this.lbProtocol = lbProtocol;
	}

	public String getLbPolicy() {
		return lbPolicy;
	}

	public void setLbPolicy(String lbPolicy) {
		this.lbPolicy = lbPolicy;
	}

	public int getLbPort() {
		return lbPort;
	}

	public void setLbPort(int lbPort) {
		this.lbPort = lbPort;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

}
