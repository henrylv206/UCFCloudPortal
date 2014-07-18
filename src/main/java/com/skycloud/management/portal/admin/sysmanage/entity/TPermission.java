package com.skycloud.management.portal.admin.sysmanage.entity;

public class TPermission {
	private int menuId;	
	private String menuName;
	private String menuCode;
	private int parentId;
	private String satae;
	
	public int getMenuId() {
		return menuId;
	}
	public void setMenuId(int menuId) {
		this.menuId = menuId;
	}
	public String getMenuName() {
		return menuName;
	}
	public void setMenuName(String menuName) {
		this.menuName = menuName;
	}
	public String getMenuCode() {
		return menuCode;
	}
	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}
	public int getParentId() {
		return parentId;
	}
	public void setParentId(int parentId) {
		this.parentId = parentId;
	}
	public String getSatae() {
		return satae;
	}
	public void setSatae(String satae) {
		this.satae = satae;
	}
	
}
