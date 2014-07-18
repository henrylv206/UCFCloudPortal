package com.skycloud.management.portal.front.resources.rest;


public class ListPhysicalVlanIP extends RestfulPath{

	
	public static final String PATH = "/rest/phyip/listip";

	
	public static final String ID = "id";
	public static final String IP = "ip";
	public static final String NAME = "name";
	public static final String DESC = "desc";
	public static final String STATE = "state";
	public static final String VLANID = "vlanid";
	public static final String VLANDBID = "vlandbid";
	public static final String ZONEID = "zoneid";
	public static final String PODID = "podid";
	public static final String DNS = "dns";
	public static final String NICID = "nicid";
	public static final String MASK = "mask";
	public static final String GATEWAY = "gateway";
	public static final String HOSTID = "hostid";
	
	private long id;//ID
	private String ip;
	private String state;//状态: Active/Inactive
	private Long vlanid;//VLANID
	private Long vlandbid;//VLANDBID
	private Long zoneid;//资源域
	private Long podid;//VLANID
	private String dns;//
	private String nicid;//
	private String mask;//子网掩码
	private String gateway;//VLAN网关
	private Long hostid;//物理机ID

	public ListPhysicalVlanIP() {
	    super(PATH);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
		this.setParameter(STATE, state);
	}

	public Long getVlanid() {
		return vlanid;
	}

	public void setVlanid(Long vlanId) {
		this.vlanid = vlanId;
		this.setParameter(VLANID, vlanId);
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
		this.setParameter(IP, ip);
	}

	public Long getVlandbid() {
		return vlandbid;
	}

	public void setVlandbid(Long vlandbid) {
		this.vlandbid = vlandbid;
		this.setParameter(VLANDBID, vlandbid);
	}

	public Long getZoneid() {
		return zoneid;
	}

	public void setZoneid(Long zoneid) {
		this.zoneid = zoneid;
		this.setParameter(ZONEID, zoneid);
	}

	public Long getPodid() {
		return podid;
	}

	public void setPodid(Long podid) {
		this.podid = podid;
		this.setParameter(PODID, podid);
	}

	public String getDns() {
		return dns;
	}

	public void setDns(String dns) {
		this.dns = dns;
		this.setParameter(DNS, dns);
	}

	public String getNicid() {
		return nicid;
	}

	public void setNicid(String nicid) {
		this.nicid = nicid;
		this.setParameter(NICID, nicid);
	}

	public String getMask() {
		return mask;
	}

	public void setMask(String mask) {
		this.mask = mask;
		this.setParameter(MASK, mask);
	}

	public String getGateway() {
		return gateway;
	}

	public void setGateway(String gateway) {
		this.gateway = gateway;
		this.setParameter(GATEWAY, gateway);
	}

	public Long getHostid() {
		return hostid;
	}

	public void setHostid(Long hostid) {
		this.hostid = hostid;
		this.setParameter(HOSTID, hostid);
	}
	
}
