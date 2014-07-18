package com.skycloud.management.portal.front.order.entity;

import java.util.Date;

/**
 * 服务模板实例 paas tableName: T_SCS_PRODUCT_INSTANCE_REF TProductInstanceInfoRefBO
 * entity.
 *
 * @author hefk
 */

public class TProductInstanceInfoRefBO implements java.io.Serializable {

	/**
	 * 创建人： 何福康 创建时间：2012-6-13 下午01:45:00
	 */

	private static final long serialVersionUID = 1L;

	private int piId; // 主键

	private String piName; // 组别名称(服务实例名称)

	private String piCode; // 组别编码:userid_创建时间

	private int templateId; // 模板ID

	private int serviceInstanceId; // 服务实例ID

	private int instanceInfoId; // 实例ID

	private int productId; // 服务模板ID

	private int orderId; // 订单ID

	private Date createDt; // 创建日期

	private String description; //服务描述
	//fix bug 3699 3559
	private float charge; //服务价格

	public TProductInstanceInfoRefBO() {
	}

	public int getPiId() {
		return piId;
	}

	public void setPiId(int piId) {
		this.piId = piId;
	}

	public String getPiName() {
		return piName;
	}

	public void setPiName(String piName) {
		this.piName = piName;
	}

	public String getPiCode() {
		return piCode;
	}

	public void setPiCode(String piCode) {
		this.piCode = piCode;
	}

	public int getTemplateId() {
		return templateId;
	}

	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}

	public int getServiceInstanceId() {
		return serviceInstanceId;
	}

	public void setServiceInstanceId(int serviceInstanceId) {
		this.serviceInstanceId = serviceInstanceId;
	}

	public int getInstanceInfoId() {
		return instanceInfoId;
	}

	public void setInstanceInfoId(int instanceInfoId) {
		this.instanceInfoId = instanceInfoId;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public float getCharge() {
		return charge;
	}

	public void setCharge(float charge) {
		this.charge = charge;
	}

	
}
