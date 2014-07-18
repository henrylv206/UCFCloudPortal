package com.skycloud.management.portal.front.order.entity;

import java.text.SimpleDateFormat;
import java.util.Date;
/**
 * TOrder entity. 
 * @author hefk
 */

@SuppressWarnings("serial")
public class TOrderLogBO implements java.io.Serializable {

	/**
	 * 订单日志信息实体对象
	 */
	private static long serialVersionUID = -1552371484813306221L;
	private int orderDetailId;    //主键
	private int userId;           //处理人
	private int orderId;          //订单ID	
	private Date createDt;      //创建时间
	private String createDtString;
	private int state;            //订单状态     1：申请状态；2：高级用户审核状态；3：管理员审核状态；
	                                  //4：高级管理员审核状态；5：拒绝状态
	private String commit;//原因
	private String auditUserName;
	private int roleApproveLevel;
	
	/** default constructor */
	public TOrderLogBO() {
		super();
	}

	public TOrderLogBO(int orderDetailId, int userId, int orderId, Date createDt,
			int state) {
		super();
		this.orderDetailId = orderDetailId;
		this.userId = userId;
		this.orderId = orderId;
		this.createDt = createDt;
		this.state = state;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static void setSerialversionuid(long serialversionuid) {
		serialVersionUID = serialversionuid;
	}

	public int getOrderDetailId() {
		return orderDetailId;
	}

	public void setOrderDetailId(int orderDetailId) {
		this.orderDetailId = orderDetailId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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
		this.createDtString=(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")).format(createDt);
		this.createDt = createDt;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setAuditUserName(String auditUserName) {
		this.auditUserName = auditUserName;
	}

	public String getAuditUserName() {
		return auditUserName;
	}

	public void setCommit(String commit) {
		this.commit = commit;
	}

	public String getCommit() {
		return commit;
	}

	public void setCreateDtString(String createDtString) {
		this.createDtString = createDtString;
	}

	public String getCreateDtString() {
		return createDtString;
	}

	public void setRoleApproveLevel(int roleApproveLevel) {
		this.roleApproveLevel = roleApproveLevel;
	}

	public int getRoleApproveLevel() {
		return roleApproveLevel;
	}
	
		
}