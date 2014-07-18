package com.skycloud.management.portal.front.resources.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 广东VDC公司备案信息
 * @author 何军辉
 * 2012-10-15
 *
 */
public class BeianBO implements Serializable {
	private static final long serialVersionUID = -3805728237613427395L;
	

	/**
	 * 0 默认状态，1 待确认，2 确认可用，3 非法数据， 9 已删除' 
	 */
	public final static int State_0=0;
	/**
	 * 0 默认状态，1 待确认，2 确认可用，3 非法数据， 9 已删除' 
	 */
	public final static int State_1=1;
	/**
	 * 0 默认状态，1 待确认，2 确认可用，3 非法数据， 9 已删除' 
	 */
	public final static int State_2=2;
	/**
	 * 0 默认状态，1 待确认，2 确认可用，3 非法数据， 9 已删除' 
	 */
	public final static int State_3=3;
	/**
	 * 0 默认状态，1 待确认，2 确认可用，3 非法数据， 9 已删除' 
	 */
	public final static int State_9=9;
	
	private int id;
	private String companyCode;
	private int userId;
	private String beianCode;
	private int state;
	private Date createDt;
	private Date auditDt;
	public void setId(int id) {
		this.id = id;
	}
	public int getId() {
		return id;
	}
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}
	public String getCompanyCode() {
		return companyCode;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getUserId() {
		return userId;
	}
	public void setBeianCode(String beianCode) {
		this.beianCode = beianCode;
	}
	public String getBeianCode() {
		return beianCode;
	}
	public void setState(int state) {
		this.state = state;
	}
	public int getState() {
		return state;
	}
	public void setCreateDt(Date createDt) {
		this.createDt = createDt;
	}
	public Date getCreateDt() {
		return createDt;
	}
	public void setAuditDt(Date auditDt) {
		this.auditDt = auditDt;
	}
	public Date getAuditDt() {
		return auditDt;
	}
	
}
