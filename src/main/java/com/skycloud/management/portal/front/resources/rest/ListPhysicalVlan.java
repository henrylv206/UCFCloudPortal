package com.skycloud.management.portal.front.resources.rest;


public class ListPhysicalVlan extends RestfulPath{

	
	public static final String PATH = "/rest/phyvlan/listvlan";


	public static final String ID = "id";
	public static final String VLANID = "vlanid";
	public static final String DESC = "desc";
	public static final String BUSINESSTYPE = "businesstype";
	public static final String VLANGATEWAY = "vlangateway";
	public static final String VLANNETMASK = "vlannetmask";
	public static final String VLANTYPE = "vlantype";
	public static final String ZONEID = "zoneid";

	private Long id;//VLANID
	private Long vlanid;//VLANID
	private String desc;//VLAN描述
	//VLAN所属网络类型:business生产网，store存储网，management管理网
	private String businesstype;
	private String vlangateway;///VLAN网关
	private String vlannetmask;//子网掩码
	private String vlantype;//VLAN类型
	private Long zoneId;//资源域

	public ListPhysicalVlan() {
	    super(PATH);
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
		this.setParameter(DESC, desc);
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
		this.setParameter(ZONEID, zoneId);
	}

	public Long getVlanid() {
		return vlanid;
	}

	public void setVlanid(Long vlanid) {
		this.vlanid = vlanid;
		this.setParameter(VLANID, vlanid);
	}

	public String getBusinesstype() {
		return businesstype;
	}

	public void setBusinesstype(String businesstype) {
		this.businesstype = businesstype;
		this.setParameter(BUSINESSTYPE, businesstype);
	}

	public String getVlangateway() {
		return vlangateway;
	}

	public void setVlangateway(String vlangateway) {
		this.vlangateway = vlangateway;
		this.setParameter(VLANGATEWAY, vlangateway);
	}

	public String getVlannetmask() {
		return vlannetmask;
	}

	public void setVlannetmask(String vlannetmask) {
		this.vlannetmask = vlannetmask;
		this.setParameter(VLANNETMASK, vlannetmask);
	}

	public String getVlantype() {
		return vlantype;
	}

	public void setVlantype(String vlantype) {
		this.vlantype = vlantype;
		this.setParameter(VLANTYPE, vlantype);
	}
	
}
