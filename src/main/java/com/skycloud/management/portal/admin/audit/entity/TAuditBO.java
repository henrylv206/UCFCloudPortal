package com.skycloud.management.portal.admin.audit.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 审批对象
 *
 * @author jiaoyz
 */
public class TAuditBO {

	private int orderId;

	private int type;
	private int vmOrStorage;

	private String account;

	private Date createDt;

	private int creatorUserId;
	private int state;

	private String orderCode;
	private int orderType;//订单表中的type标示
	private int templateType;//order表中的模板类型
	private int instanceInfoId; // 资源实例ID

	//页面传参数
	private int approveType;// 代表审批的类型，同意或者拒绝
	private String commit;
	private String email;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	private String createDtString;

	public String getCreateDtString() {
		return createDtString;
	}

	public void setCreateDtString(String createDtString) {
		this.createDtString = createDtString;
	}

	public void setCreateDt(Date createDt) {
		this.createDtString=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createDt);
		this.createDt = createDt;
	}

	public Date getCreateDt() {
		return createDt;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getType() {
		return type;
	}

	public void setVmOrStorage(int vmOrStorage) {
		this.vmOrStorage = vmOrStorage;
	}

	public int getVmOrStorage() {
		return vmOrStorage;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public String getCommit() {
		return commit;
	}

	public void setApproveType(int approveType) {
		this.approveType = approveType;
	}

	public int getApproveType() {
		return approveType;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setOrderType(int orderType) {
		this.orderType = orderType;
	}

	public int getOrderType() {
		return orderType;
	}

	public void setTemplateType(int templateType) {
		this.templateType = templateType;
	}

	public int getTemplateType() {
		return templateType;
	}

	public void setState(int state) {
		this.state = state;
	}

	public int getState() {
		return state;
	}


    public int getInstanceInfoId() {
    	return instanceInfoId;
    }


    public void setInstanceInfoId(int instanceInfoId) {
    	this.instanceInfoId = instanceInfoId;
    }


}
