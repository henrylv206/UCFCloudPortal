package com.skycloud.management.portal.front.report.entity;

public class QueryVO {
	private String start;

	private String end;

	private int orderId;

	private long userid;

	private long typeid;

	private long productId;

	private int orderType;

	private int orderInstanceState;
		
	private int deptid;
	
	private String expireStart;
	
	private String expireEnd;

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public long getTypeid() {
		return typeid;
	}

	public void setTypeid(long typeid) {
		this.typeid = typeid;
	}

	public long getProductId() {
		return productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderInstanceState() {
		return orderInstanceState;
	}

	public void setOrderInstanceState(int orderInstanceState) {
		this.orderInstanceState = orderInstanceState;
	}

	public int getDeptid() {
		return deptid;
	}

	public void setDeptid(int deptid) {
		this.deptid = deptid;
	}

	public String getExpireStart() {
		return expireStart;
	}

	public void setExpireStart(String expireStart) {
		this.expireStart = expireStart;
	}

	public String getExpireEnd() {
		return expireEnd;
	}

	public void setExpireEnd(String expireEnd) {
		this.expireEnd = expireEnd;
	}

}
