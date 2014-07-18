package com.skycloud.management.portal.front.subaccount.entity;

public class TPortalMenu implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5001617417570000346L;

	private int id; // 主键
	private String menuId;
	private String menuName;
	private String param4;
	private String param5;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getMenuId() {
		return menuId;
	}

	public void setMenuId(String menuId) {
		this.menuId = menuId;
	}

	public String getMenuName() {
		return menuName;
	}

	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}

	public String getParam4() {
		return param4;
	}

	public void setParam4(String param4) {
		this.param4 = param4;
	}

	public String getParam5() {
		return param5;
	}

	public void setParam5(String param5) {
		this.param5 = param5;
	}


}
