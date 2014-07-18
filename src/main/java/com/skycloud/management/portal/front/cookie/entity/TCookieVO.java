package com.skycloud.management.portal.front.cookie.entity;

import java.sql.Timestamp;

/**
 * TCookie entity. @author MyEclipse Persistence Tools
 */

public class TCookieVO implements java.io.Serializable {

	private static final long serialVersionUID = 5462096204651183346L;
	
	// Fields
	private String COOKIE_KEY;
	private String COOKIE_VALUE;
	private Integer COOKIE_TYPE;
	private Timestamp updateTime;

	// Constructors

	/** default constructor */
	public TCookieVO() {
	}

	/** minimal constructor */
	public TCookieVO(String COOKIE_KEY) {
		this.COOKIE_KEY = COOKIE_KEY;
	}

	/** full constructor */
	public TCookieVO(String COOKIE_KEY, String COOKIE_VALUE, Integer COOKIE_TYPE,Timestamp updateTime) {
		this.COOKIE_KEY = COOKIE_KEY;
		this.COOKIE_VALUE = COOKIE_VALUE;
		this.COOKIE_TYPE = COOKIE_TYPE;
		this.updateTime = updateTime;
	}
	

	public String getCOOKIE_KEY() {
		return COOKIE_KEY;
	}

	public void setCOOKIE_KEY(String cOOKIE_KEY) {
		COOKIE_KEY = cOOKIE_KEY;
	}

	public String getCOOKIE_VALUE() {
		return COOKIE_VALUE;
	}

	public void setCOOKIE_VALUE(String cOOKIE_VALUE) {
		COOKIE_VALUE = cOOKIE_VALUE;
	}

	public Integer getCOOKIE_TYPE() {
		return COOKIE_TYPE;
	}

	public void setCOOKIE_TYPE(Integer cOOKIE_TYPE) {
		COOKIE_TYPE = cOOKIE_TYPE;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	

}