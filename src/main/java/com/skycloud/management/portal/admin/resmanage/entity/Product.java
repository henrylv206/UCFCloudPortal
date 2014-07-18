package com.skycloud.management.portal.admin.resmanage.entity;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.template.entity.TTemplateMCBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;

public class Product {

	private int id;

	private String code;

	private String name;

	// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-已下线
	private int state;

	private String description;

	private String specification;

	private int quotaNum;

	private String period;

	private float price;

	private String unit;

	private String picture;

	private String doc;

	private int templateId;// 模板Id

	private Integer productItemId;

	private Date createDate;

	private Date modifyDate;

	private int isDefault; // 是否为默认产品:1:默认;2:自定义

	private int type; // 1：表示虚拟机；2：表示块存储;4：表示备份；5：表示监控；6：表示负载均衡；7：表示防火墙；8：表示带宽；9：表示公网IP；10：表示物理机；15:云备份

	private int bindProductItem; // 是否绑定了服务目录

	private String typeName; // 把type翻译成中文需用字段

	private String unitName;

	private int operateType; // 操作类型 1:添加，2:修改，3：删除

	private String productItemName;
	
	private String templateTypes;

	private String templateIds;

	private String templateNums;

	private List<TTemplateVMBO> templates; // 用于多虚机-非小型机模板
	
//	private List<TTemplateMCBO> mcTemplates;

	private String templateNames; //服务关联的所有模板名称-用于多虚机

	private float price2;

	private TTemplateVMBO template; // 用于不是多虚机其他服务类型

	private int resourcePoolsId;

	private BigInteger zoneId;

	private Integer special;// 1：特殊模板；0：标准模板
	
	//多定价模式支持 added by 何军辉 20130311
	private String priceList;

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

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
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

	public Integer getProductItemId() {
		return productItemId;
	}

	public void setProductItemId(Integer productItemId) {
		this.productItemId = productItemId;
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

	public int getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(int isDefault) {
		this.isDefault = isDefault;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getBindProductItem() {
		return bindProductItem;
	}

	public void setBindProductItem(int bindProductItem) {
		this.bindProductItem = bindProductItem;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getUnitName() {
		return unitName;
	}

	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}

	public int getOperateType() {
		return operateType;
	}

	public void setOperateType(int operateType) {
		this.operateType = operateType;
	}

	public String getProductItemName() {
		return productItemName;
	}

	public void setProductItemName(String productItemName) {
		this.productItemName = productItemName;
	}
	
	public String getTemplateTypes() {
		return templateTypes;
	}

	public void setTemplateTypes(String templateTypes) {
		this.templateTypes = templateTypes;
	}

	public String getTemplateIds() {
		return templateIds;
	}

	public void setTemplateIds(String templateIds) {
		this.templateIds = templateIds;
	}

	public String getTemplateNums() {
		return templateNums;
	}

	public void setTemplateNums(String templateNums) {
		this.templateNums = templateNums;
	}

	public List<TTemplateVMBO> getTemplates() {
		return templates;
	}

	public void setTemplates(List<TTemplateVMBO> templates) {
		this.templates = templates;
	}

	public String getTemplateNames() {
		return templateNames;
	}

	public void setTemplateNames(String templateNames) {
		this.templateNames = templateNames;
	}

	public float getPrice2() {
		return price2;
	}

	public void setPrice2(float price2) {
		this.price2 = price2;
	}

	public TTemplateVMBO getTemplate() {
		return template;
	}

	public void setTemplate(TTemplateVMBO template) {
		this.template = template;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public BigInteger getZoneId() {
		return zoneId;
	}

	public void setZoneId(BigInteger zoneId) {
		this.zoneId = zoneId;
	}

	public Integer getSpecial() {
		return special;
	}

	public void setSpecial(Integer special) {
		this.special = special;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}
	
//	public List<TTemplateMCBO> getMcTemplates() {
//		return mcTemplates;
//	}
//
//	public void setMcTemplates(List<TTemplateMCBO> mcTemplates) {
//		this.mcTemplates = mcTemplates;
//	}

	public String getPriceList() {
		return priceList;
	}

	public void setPriceList(String priceList) {
		this.priceList = priceList;
	}
}
