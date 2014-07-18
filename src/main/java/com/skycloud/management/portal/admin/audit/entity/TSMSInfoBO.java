package com.skycloud.management.portal.admin.audit.entity;

import java.util.Date;


public class TSMSInfoBO {

	private int id;
	private String mobile;
	private String content;
	private Date deadTime;
	private int status;
	private String eid;
	private String userId;
	private String password;
	private int usrport;
	
	public TSMSInfoBO() {
		super();
	}
	
	public TSMSInfoBO(int id, String mobile, String content, Date deadTime,
			int status, String eid, String userId, String password, int usrport) {
		super();
		this.id = id;
		this.mobile = mobile;
		this.content = content;
		this.deadTime = deadTime;
		this.status = status;
		this.eid = eid;
		this.userId = userId;
		this.password = password;
		this.usrport = usrport;
	}



	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getDeadTime() {
		return deadTime;
	}
	public void setDeadTime(Date deadTime) {
		this.deadTime = deadTime;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getEid() {
		return eid;
	}
	public void setEid(String eid) {
		this.eid = eid;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public int getUsrport() {
		return usrport;
	}
	public void setUsrport(int usrport) {
		this.usrport = usrport;
	}
	
	
}
