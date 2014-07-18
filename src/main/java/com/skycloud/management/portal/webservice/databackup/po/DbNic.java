package com.skycloud.management.portal.webservice.databackup.po;

public class DbNic {

	private String id;
	
	private String networkid;
	
	private String gateway;
	
	private String ipaddress;
	
	private String isolationuri;
	
	private String broadcasturi;
	
	private String traffictype;
	
	private String type;
	
	private String isdefault;
	
	private String macaddress;

	private String netmask;
	
	
	public String getNetmask() {
		return netmask;
	}

	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}

	public String getNetworkid() {
		return networkid;
	}

	public void setNetworkid(String networkid) {
		this.networkid = networkid;
	}

	public String getIpaddress() {
		return ipaddress;
	}

	public void setIpaddress(String ipaddress) {
		this.ipaddress = ipaddress;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
	}

	public String getIsolationuri() {
		return isolationuri;
	}

	public void setIsolationuri(String isolationuri) {
		this.isolationuri = isolationuri;
	}

	public String getBroadcasturi() {
		return broadcasturi;
	}

	public void setBroadcasturi(String broadcasturi) {
		this.broadcasturi = broadcasturi;
	}

	public String getTraffictype() {
		return traffictype;
	}

	public void setTraffictype(String traffictype) {
		this.traffictype = traffictype;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsdefault() {
		return isdefault;
	}

	public void setIsdefault(String isdefault) {
		this.isdefault = isdefault;
	}

	public String getMacaddress() {
		return macaddress;
	}

	public void setMacaddress(String macaddress) {
		this.macaddress = macaddress;
	}
		
}
