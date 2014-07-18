package com.skycloud.management.portal.front.resources.entity;

/**
 * 广东VDC公司备案信息(View)
 * @author 何军辉
 * 2012-10-16
 *
 */
public class BeianView extends BeianBO {
	private static final long serialVersionUID = 1203223021475663862L;
	private String userName;
	private String compCnName;
	private String account;
	
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getUserName() {
		return userName;
	}
	public void setCompCnName(String compCnName) {
		this.compCnName = compCnName;
	}
	public String getCompCnName() {
		return compCnName;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getAccount() {
		return account;
	}
}
