package com.skycloud.management.portal.admin.sysmanage.entity;

import java.util.List;

public class TMenuBO {

	private static final long serialVersionUID = 6905481693289889505L;
	private int menuId;      //菜单ID
	private String menuName; //菜单名称
	private String menuCode; //菜单编码
	private String menuDescr;//菜单描述
	private int parentScsMenuId;//父菜单ID
	private String actionUrl;//actionUrl
	private String imgUrl;   //imgUrl
	private char state;      //菜单状态
	private int menuOrder;   //菜单排序
	private List<TMenuBO> subMenuList;
	
	public List<TMenuBO> getSubMenuList() {
		return subMenuList;
	}
	public void setSubMenuList(List<TMenuBO> subMenuList) {
		this.subMenuList = subMenuList;
	}
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
	public String getMenuDescr() {
		return menuDescr;
	}
	public void setMenuDescr(String menuDescr) {
		this.menuDescr = menuDescr;
	}
	public int getParentScsMenuId() {
		return parentScsMenuId;
	}
	public void setParentScsMenuId(int parentScsMenuId) {
		this.parentScsMenuId = parentScsMenuId;
	}
	public String getActionUrl() {
		return actionUrl;
	}
	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}
	public String getImgUrl() {
		return imgUrl;
	}
	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}
	public char getState() {
		return state;
	}
	public void setState(char state) {
		this.state = state;
	}
	public int getMenuOrder() {
		return menuOrder;
	}
	public void setMenuOrder(int menuOrder) {
		this.menuOrder = menuOrder;
	}
 
	       

}
