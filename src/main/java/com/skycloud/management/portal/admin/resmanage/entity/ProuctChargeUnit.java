package com.skycloud.management.portal.admin.resmanage.entity;

/**
 * haolong@20120910 01 计费模式变更
 */
public class ProuctChargeUnit extends ChargeUnit{
	/**
	 * 主键编号
	 */
	private  String id="";
	/**
	 * 产品ID
	 */
	private  String productId="";
	/**
	 * 计费单位ID
	 */
	private  String unitId="";
	/**
	 * 价格：单位元
	 */
	private  String price="";
	/**
	 * 此字段为广东VDC项目使用，其它项目请忽略
	 * 资源编码规则是ResourceID=serviced+ 8位数字
	 * 8位数字规范：1～2位为计费方式， 00为按日计费，01为按月计费
     * 3～8位为递增流水号
     */
	private  String resourceId="";
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getUnitId() {
		return unitId;
	}
	public void setUnitId(String unitId) {
		this.unitId = unitId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
	public String getResourceId() {
		return resourceId;
	}
	public void setResourceId(String resourceId) {
		this.resourceId = resourceId;
	}


}
