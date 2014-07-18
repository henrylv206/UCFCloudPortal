package com.skycloud.management.portal.front.subaccount.entity;

public class TPortalMenuRelation implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2585510421686165146L;
	
	private int id; // 主键
	private int userId;
	private int menuId;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
}
