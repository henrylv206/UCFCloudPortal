package com.skycloud.management.portal.admin.audit.entity;

public class TNicsBO {
	private int id;
	private int vmInstanceInfoId;
	private long eVlanId;
	private String ip;
	private int state;
	private int vlanCount;//vlan资源在同一个虚机实例网卡中被指定的次数
	private int ipCount;//IP资源在有效虚机实例网卡中被指定的次数
	
	private String vlan;

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getVmInstanceInfoId() {
		return vmInstanceInfoId;
	}
	public void setVmInstanceInfoId(int vmInstanceInfoId) {
		this.vmInstanceInfoId = vmInstanceInfoId;
	}
	public long geteVlanId() {
		return eVlanId;
	}
	public void seteVlanId(long eVlanId) {
		this.eVlanId = eVlanId;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public void setVlan(String vlan) {
		this.vlan = vlan;
	}
	public String getVlan() {
		return vlan;
	}

    public int getVlanCount() {
    	return vlanCount;
    }

    public void setVlanCount(int vlanCount) {
    	this.vlanCount = vlanCount;
    }
	
    public int getIpCount() {
    	return ipCount;
    }
	
    public void setIpCount(int ipCount) {
    	this.ipCount = ipCount;
    }


}
