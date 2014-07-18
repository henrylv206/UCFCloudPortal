package com.skycloud.management.portal.admin.audit.entity;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 产品对象
 * 
 * @author zs
 */
public class TProductBO {
	private int id;
	private String code;  //资源编码
	private String name;  //资源名称
	private int state;    //资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败。资源状态由系统后台自动生成，操作人员不可修改
	private String description; //资源内容描述
	private Date createDate;
	private Date modifyDate;
	private String specification; //资源规格：即资源的容量、设备性能、资源等级等不同标准
	private int quotaNum;         //资源购买数量限额：最大购买数
	private String period;        //资源有效期限：资源的使用时间单位，按天/月/季/年等
	private BigDecimal price;
	private String unit;          //价格单位：Y：年；M：月；W：周；D：日；H：小时；S：按流量
	private String picture;       //图片:图片格式可以是bmp、jpg等
	private String doc;           //资源的说明文档
	private int templateId;       //模板ID:关联T_SCS_TEMPLATE的主键ID
	private int productItemId;    //产品服务目录ID
	private int isDefault;        //是否为默认产品:1:默认;2:自定义
	
	public TProductBO() {
		super();
	}
	
	public TProductBO(int id, String code, String name, int state,
			String description, Date createDate, Date modifyDate,
			String specification, int quotaNum, String period,
			BigDecimal price, String unit, String picture, String doc,
			int templateId, int productItemId, int isDefault) {
		super();
		this.id = id;
		this.code = code;
		this.name = name;
		this.state = state;
		this.description = description;
		this.createDate = createDate;
		this.modifyDate = modifyDate;
		this.specification = specification;
		this.quotaNum = quotaNum;
		this.period = period;
		this.price = price;
		this.unit = unit;
		this.picture = picture;
		this.doc = doc;
		this.templateId = templateId;
		this.productItemId = productItemId;
		this.isDefault = isDefault;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getState() {
		return state;
	}
	public void setState(int state) {
		this.state = state;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getModifyDate() {
		return modifyDate;
	}
	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}
	public String getSpecification() {
		return specification;
	}
	public void setSpecification(String specification) {
		this.specification = specification;
	}
	public int getQuotaNum() {
		return quotaNum;
	}
	public void setQuotaNum(int quotaNum) {
		this.quotaNum = quotaNum;
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
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getDoc() {
		return doc;
	}
	public void setDoc(String doc) {
		this.doc = doc;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public int getProductItemId() {
		return productItemId;
	}
	public void setProductItemId(int productItemId) {
		this.productItemId = productItemId;
	}
	public int getIsDefault() {
		return isDefault;
	}
	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}
}
