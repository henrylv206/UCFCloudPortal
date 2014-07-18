package com.skycloud.management.portal.admin.audit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TSendInfoBO {
	private int id;
	private String receiveAddress;
	private int orderId;
	private String approveReason;
	private Date createDt;
	private String createDtString;
	private int state;
	private String orderCode;
	private int type;//查询结果集中添加 o.TYPE 字段;//bugid = 0001707

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getReceiveAddress() {
		return receiveAddress;
	}

	public void setReceiveAddress(String receiveAddress) {
		this.receiveAddress = receiveAddress;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getApproveReason() {
		return approveReason;
	}

	public void setApproveReason(String approveReason) {
		this.approveReason = approveReason;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setCreateDt(Date createDt) {
		this.createDtString=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createDt);
		this.createDt = createDt;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setCreateDtString(String createDtString) {
		this.createDtString = createDtString;
	}

	public String getCreateDtString() {
		return createDtString;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

}
