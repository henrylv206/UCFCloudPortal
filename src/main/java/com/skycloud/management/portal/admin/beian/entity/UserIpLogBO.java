package com.skycloud.management.portal.admin.beian.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 广东VDC公司用户的IP使用记录表
 * @author 何军辉
 * 2012-11-10
 *
 */
public class UserIpLogBO implements Serializable {
	
	private static final long serialVersionUID = 3212529523203303395L;
	/**
	 * 0内网IP;1外网IP 
	 */
	public final static int Type_0=0;
	/**
	 * 0内网IP;1外网IP 
	 */
	public final static int Type_1=1;
	
	/**
	 * 0 绑定;1解绑;2变更
	 */
	public final static int Act_0=0;
	/**
	 * 0 绑定;1解绑;2变更
	 */
	public final static int Act_1=1;
	/**
	 * 0 绑定;1解绑;2变更
	 */
	public final static int Act_2=12;
	
	private int id;
	private int userId;
	private int instanceId;
	
	private String ip;
	private int ipType; 
	private Date createDt;
	private int act; 
	
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
}
