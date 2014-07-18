package com.skycloud.management.portal.front.mall.entity;

import java.util.Date;

/**
 * 实例续费记录表 ：T_SCS_SERVICE_RENEW_LOG<br>
 * Description.
 * <p>
 * Copyright: Copyright (c) 2012-8-24 上午11:23:35
 * <p>
 * Company: 北京天云融创科技有限公司
 * <p>
 * Author: hefk@chinaskycloud.com
 * <p>
 * Version: 1.0
 * <p>
 */

public class TServiceRenewLogBO {

	private int id;

	private int serviceInstanceId;// 订单ID

	private int num;// 购买数量

	private int period;// 购买周期

	private String unit;// 计费单位

	private float price;// 服务价格

	private int state;// 服务状态

	private Date createDt;// 创建时间

	private Date expiryDate;// 过期时间

	private String createDtStr;// 创建时间字符串

	private String expiryDateStr;// 过期时间字符串

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(int serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDate = expiryDate;
	}

	public String getCreateDtStr() {
		return createDtStr;
	}

	public void setCreateDtStr(String createDtStr) {
		this.createDtStr = createDtStr;
	}

	public String getExpiryDateStr() {
		return expiryDateStr;
	}

	public void setExpiryDateStr(String expiryDateStr) {
		this.expiryDateStr = expiryDateStr;
	}

}
