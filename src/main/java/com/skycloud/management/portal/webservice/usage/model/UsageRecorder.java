package com.skycloud.management.portal.webservice.usage.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="UsageRecord")
public class UsageRecorder {
	private String resourceType; //实例类型
	private String streamNumber;	//话单流水号
	private String userId;	//用户ID
	private String serviceId;	//业务ID
	private String orderId;		//订购关系ID
	private String resourceInstanceId;	//资源实例ID
	private String applicationid;	//订单ID
	private String userEmail;	//用户邮箱账号
	private String userImsi;	//用户手机号码
	private String payType;		//支付类型
	private String beginTime;		
	private String endTime;			
	private int duration;	//使用时长
	private int cost;
	private int fee;
	private int event;	//话单相关事件0：新申请资源；1：分割话单；2：资源取消。

	
	//----------------SETTER AND GETTER----------------------
	public String getResourceType() {
		return resourceType;
	}
	public void setResourceType(String resourceType) {
		this.resourceType = resourceType;
	}
	public String getStreamNumber() {
		return streamNumber;
	}	
	public void setStreamNumber(String streamNumber) {
		this.streamNumber = streamNumber;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getServiceId() {
		return serviceId;
	}
	public void setServiceId(String serviceId) {
		this.serviceId = serviceId;
	}
	public String getOrderId() {
		return orderId;
	}
	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
	public String getResourceInstanceId() {
		return resourceInstanceId;
	}
	public void setResourceInstanceId(String resourceInstanceId) {
		this.resourceInstanceId = resourceInstanceId;
	}
	public String getApplicationid() {
		return applicationid;
	}
	public void setApplicationid(String applicationid) {
		this.applicationid = applicationid;
	}
	public String getUserEmail() {
		return userEmail;
	}
	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}
	public String getUserImsi() {
		return userImsi;
	}
	public void setUserImsi(String userImsi) {
		this.userImsi = userImsi;
	}
	public String getPayType() {
		return payType;
	}
	public void setPayType(String payType) {
		this.payType = payType;
	}
	public String getBeginTime() {
		return beginTime;
	}
	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public int getCost() {
		return cost;
	}
	public void setCost(int cost) {
		this.cost = cost;
	}
	public int getFee() {
		return fee;
	}
	public void setFee(int fee) {
		this.fee = fee;
	}
	public int getEvent() {
		return event;
	}
	public void setEvent(int event) {
		this.event = event;
	}
}
