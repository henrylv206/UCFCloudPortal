package com.skycloud.management.portal.admin.beian.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 广东VDC公司用户的IP使用记录表,运营管理平台使用的DTO对象
 * @author 何军辉
 * 2012-11-10
 *
 */
public class UserIpLogView implements Serializable {
	
	private static final long serialVersionUID = -1051431769113004065L;
	
	private int id;
	private int userId;
	private int instanceId;
	
	private String ip;
	private int ipType; 
	private Date createDt;
	private int act; 
	
	private String userName;
	
	private String companyName;
	
	private int companyId;
	
	private int vmId;
	
	private String vmName;
	
	private Date expiredDt;
	
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	public int getInstanceId() {
		return instanceId;
	}
	public void setIpType(int ipType) {
		this.ipType = ipType;
	}
	public int getIpType() {
		return ipType;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getIp() {
		return ip;
	}
	public void setAct(int act) {
		this.act = act;
	}
	public int getAct() {
		return act;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyId(int companyId) {
		this.companyId = companyId;
	}
	public int getCompanyId() {
		return companyId;
	}
	public void setVmId(int vmId) {
		this.vmId = vmId;
	}
	public int getVmId() {
		return vmId;
	}
	public void setVmName(String vmName) {
		this.vmName = vmName;
	}
	public String getVmName() {
		return vmName;
	}
	public void setExpiredDt(Date expiredDt) {
		this.expiredDt = expiredDt;
	}
	public Date getExpiredDt() {
		return expiredDt;
	}
}
