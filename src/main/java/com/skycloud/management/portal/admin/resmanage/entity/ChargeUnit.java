package com.skycloud.management.portal.admin.resmanage.entity;

/**
 * haolong@20120910 01 计费模式变更
 */
public class ChargeUnit {
	/**
	 * 主键编号
	 */
	private String id="";
	/**
	 * 价格单位：Y：年；M：月；W：周；D：日；H：小时；S：按流量
	 */
	private String unit="";
	/**
	 * 作为配置UI的Label
	 */
	private String description="";
	
	/**
	 * 计费代码00:按天计费 01：按月计费
	 */
	private String unitCode;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUnitCode() {
		return unitCode;
	}
	public void setUnitCode(String unitCode) {
		this.unitCode = unitCode;
	}
}
