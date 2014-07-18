package com.skycloud.management.portal.front.instance.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TInstancePeriodInfo implements java.io.Serializable {

	/**
	 * 创建人：   张爽    
	 * 创建时间：2012-3-24  下午04:09:54
	 */
			
	private static final long serialVersionUID = 4406002294105128184L;
	
	private int id;
	private String resourceInfo;
	private Date expireDate;
	private String period;        //资源有效期限：资源的使用时间单位，按天/月/季/年等
	private BigDecimal price;
	private String unit;
	private String expireDateString;
	private String buyPeriod;//已经购买周期
	private String unitString;
	private Integer order_Id;
	
	public TInstancePeriodInfo() {
		super();
	}

	public TInstancePeriodInfo(int id, String resourceInfo, Date expireDate,
			String period, BigDecimal price, String unit) {
		super();
		this.id = id;
		this.resourceInfo = resourceInfo;
		this.expireDate = expireDate;
		this.period = period;
		this.price = price;
		this.unit = unit;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getResourceInfo() {
		return resourceInfo;
	}

	public void setResourceInfo(String resourceInfo) {
		this.resourceInfo = resourceInfo;
	}

	public Date getExpireDate() {
		return expireDate;
	}

	public void setExpireDate(Date expireDate) {
		this.expireDateString=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(expireDate);
		this.expireDate = expireDate;
	}

	public String getPeriod() {
		return period;
	}

	public void setPeriod(String period) {
		this.period = period;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public void setExpireDateString(String expireDateString) {
		this.expireDateString = expireDateString;
	}

	public String getExpireDateString() {
		return expireDateString;
	}

	public void setBuyPeriod(String buyPeriod) {
		this.buyPeriod = buyPeriod;
	}

	public String getBuyPeriod() {
		return buyPeriod;
	}

	public void setUnitString(String unitString) {
		this.unitString = unitString;
	}

	public String getUnitString() {
		return unitString;
	}

	public Integer getOrder_Id() {
		return order_Id;
	}

	public void setOrder_Id(Integer order_Id) {
		this.order_Id = order_Id;
	}

}
