package com.skycloud.management.portal.front.instance.entity;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TServicePeriodInfo implements java.io.Serializable {

	/**
	 * 创建人：   张爽    
	 * 创建时间：2012-3-24  下午04:09:54
	 */
			
	private static final long serialVersionUID = 4406002294105128184L;
	
	private int id;
	private Date expiryDate;
	private String period;        //资源有效期限：资源的使用时间单位，按天/月/季/年等
	private BigDecimal price;
	private String unit;
	private String expiryDateString;
	private String buyPeriod;//已经购买周期
	private String unitString;
	private Integer order_Id;
	
	public TServicePeriodInfo() {
		super();
	}

	public TServicePeriodInfo(int id, String resourceInfo, Date expiryDate,
			String period, BigDecimal price, String unit) {
		super();
		this.id = id;
 
		this.expiryDate = expiryDate;
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

	public Date getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(Date expiryDate) {
		this.expiryDateString=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(expiryDate);
		this.expiryDate = expiryDate;
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

	public void setExpiryDateString(String expiryDateString) {
		this.expiryDateString = expiryDateString;
	}

	public String getExpiryDateString() {
		return expiryDateString;
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
