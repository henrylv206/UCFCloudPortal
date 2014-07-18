package com.skycloud.management.portal.front.log.entity;

import java.io.Serializable;
import java.util.Date;

public class TUserLogConfig implements Serializable {

	/**
	 * 用户操作日志配置
	 */
	private static final long serialVersionUID = 4181642385997791979L;

	private int id;

	private String name;//日志（类型、操作）名称

	private String keyName;//配置键值:类路径.类名称.方法名

	private int msgFlag;//是否记录消息:0:否,1:是

	private int mailFlag;//是否发送邮件:0:否,1:是

	private int smsFlag;//是否发送短信:0:否,1:是

	private int logFlag;//是否记录日志:0:否,1:是

	private int fileFlag;//是否记录文件:0:否,1:是

	private int bak1Flag;//备用标志1:0:否,1:是

	private int bak2Flag;//备用标志2:0:否,1:是

	private Date createDt; // 创建时间

	private Date lastupdateDt; // 最后修改时间

	private int status;//状态：1：正常；2：作废

	private String remark;//说明

	public TUserLogConfig() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getKeyName() {
		return keyName;
	}

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}

	public int getMsgFlag() {
		return msgFlag;
	}

	public void setMsgFlag(int msgFlag) {
		this.msgFlag = msgFlag;
	}

	public int getMailFlag() {
		return mailFlag;
	}

	public void setMailFlag(int mailFlag) {
		this.mailFlag = mailFlag;
	}

	public int getSmsFlag() {
		return smsFlag;
	}

	public void setSmsFlag(int smsFlag) {
		this.smsFlag = smsFlag;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getLastupdateDt() {
		return lastupdateDt;
	}

	public void setLastupdateDt(Date lastupdateDt) {
		this.lastupdateDt = lastupdateDt;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public int getLogFlag() {
		return logFlag;
	}

	public void setLogFlag(int logFlag) {
		this.logFlag = logFlag;
	}

	public int getFileFlag() {
		return fileFlag;
	}

	public void setFileFlag(int fileFlag) {
		this.fileFlag = fileFlag;
	}

	public int getBak1Flag() {
		return bak1Flag;
	}

	public void setBak1Flag(int bak1Flag) {
		this.bak1Flag = bak1Flag;
	}

	public int getBak2Flag() {
		return bak2Flag;
	}

	public void setBak2Flag(int bak2Flag) {
		this.bak2Flag = bak2Flag;
	}

}
