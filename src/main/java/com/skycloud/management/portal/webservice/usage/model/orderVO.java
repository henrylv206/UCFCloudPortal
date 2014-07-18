package com.skycloud.management.portal.webservice.usage.model;

public class orderVO {
	private String orderId;
	private String orderCode;
	private String creatorUserId;
	private int type;
	private String elsterId;
	
	
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getOrderCode() {
		return orderCode;
	}
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}
	public String getCreatorUserId() {
		return creatorUserId;
	}
	public void setCreatorUserId(String creatorUserId) {
		this.creatorUserId = creatorUserId;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public String getElsterId() {
		return elsterId;
	}
	public void setElsterId(String elsterId) {
		this.elsterId = elsterId;
	}
	
	
}
