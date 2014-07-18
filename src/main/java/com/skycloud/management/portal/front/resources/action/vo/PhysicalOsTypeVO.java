package com.skycloud.management.portal.front.resources.action.vo;

import org.apache.struts2.json.annotations.JSON;

/**
 * 物理机的操作系统类型
 * @author ninghao@chinaskycloud.com
 * @time 2012-12-03
 */
public class PhysicalOsTypeVO  implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -438166602012120301L;
	private Long id;
	private String name;
	private String desc;
	private String state;
	private String errorcode;
	private String errordesc;

	@JSON(name="id")
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@JSON(name="name")
	public String getName() {
		return name;
	}

	public void setName(String sOS_NAME) {
		name = sOS_NAME;
	}

	@JSON(name="desc")
	public String getDesc() {
		return desc;
	}

	public void setDesc(String sOS_DESC) {
		desc = sOS_DESC;
	}

	@JSON(name="state")
	public String getState() {
		return state;
	}

	public void setState(String sTATE) {
		state = sTATE;
	}

	@JSON(name="errorcode")
	public String getErrorcode() {
		return errorcode;
	}

	public void setErrorcode(String errorcode) {
		this.errorcode = errorcode;
	}

	@JSON(name="errordesc")
	public String getErrordesc() {
		return errordesc;
	}

	public void setErrordesc(String errordesc) {
		this.errordesc = errordesc;
	}

}
