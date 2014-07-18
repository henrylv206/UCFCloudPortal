package com.skycloud.management.portal.admin.sysmanage.entity;

import java.util.Date;

public class TUserVlanBO {
	private int id;        //ID
	private int userId;   //USER ID
	private long vlanId;  //VLAN ID
	private int state; //状态 1：可用，2：不可用，3：已删除
	private int type; //类型 1：PrivateNetwork，2：StorageNetwork
	private int zoneId; //ZONE
	private Date createDt;   //创建时间
	private Date modifyDt;//修改时间
	private String account;
	private String vlanName;
	private String zoneName;
	private String typeName;
	private String stateName;
	private String ipRanges;
	private String companyName;
	private int resourcePoolsId;

    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }

    public int getUserId() {
    	return userId;
    }

    public void setUserId(int userId) {
    	this.userId = userId;
    }

    public long getVlanId() {
    	return vlanId;
    }

    public void setVlanId(long vlanId) {
    	this.vlanId = vlanId;
    }

    public int getState() {
    	return state;
    }

    public void setState(int state) {
    	this.state = state;
    }

    public int getType() {
    	return type;
    }

    public void setType(int type) {
    	this.type = type;
    }

    public int getZoneId() {
    	return zoneId;
    }

    public void setZoneId(int zoneId) {
    	this.zoneId = zoneId;
    }

    public Date getCreateDt() {
    	return createDt;
    }

    public void setCreateDt(Date createDt) {
    	this.createDt = createDt;
    }

    public Date getModifyDt() {
    	return modifyDt;
    }

    public void setModifyDt(Date modifyDt) {
    	this.modifyDt = modifyDt;
    }


    public String getAccount() {
    	return account;
    }


    public void setAccount(String account) {
    	this.account = account;
    }


    public String getVlanName() {
    	return vlanName;
    }


    public void setVlanName(String vlanName) {
    	this.vlanName = vlanName;
    }


    public String getZoneName() {
    	return zoneName;
    }


    public void setZoneName(String zoneName) {
    	this.zoneName = zoneName;
    }


    public String getTypeName() {
    	if (type==1){
        	typeName = "第一块网卡";
        }else  if (type==2){
        	typeName = "第二块网卡";
        }else  if (type==3){
        	typeName = "第三块网卡";
        }else  if (type==4){
        	typeName = "第四块网卡";
        }
    	return typeName;
    }


    public void setTypeName(String typeName) {
    	this.typeName = typeName;
    }


    public String getStateName() {
    	return stateName=(state==1)?"可用":"不可用";
    }


    public void setStateName(String stateName) {
    	this.stateName = stateName;
    }

	public String getIpRanges() {
		return ipRanges;
	}

	public void setIpRanges(String ipRanges) {
		this.ipRanges = ipRanges;
	}


    public String getCompanyName() {
    	return companyName;
    }


    public void setCompanyName(String companyName) {
    	this.companyName = companyName;
    }


    public int getResourcePoolsId() {
    	return resourcePoolsId;
    }


    public void setResourcePoolsId(int resourcePoolsId) {
    	this.resourcePoolsId = resourcePoolsId;
    }



}
