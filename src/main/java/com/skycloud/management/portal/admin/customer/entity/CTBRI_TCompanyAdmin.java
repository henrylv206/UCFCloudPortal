package com.skycloud.management.portal.admin.customer.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;


public class CTBRI_TCompanyAdmin {


	// Fields

	private Integer compAdminId;
	private TCompanyInfo TCompanyInfo;
	private String compAdminAccount;
	private String compAdminPwd;
	private String compAdminName;
	private String compAdminEmail;
	private String compAdminPhone;
	private String compAdminMobile;
	private String compAdminFax;
	private String compAdminCreater;
	private Timestamp compAdminCtime;
	private String compModifyName;
	private Timestamp compModifyTime;
	private Integer activeState;
	private String checkCode;
	private Set TProductSales = new HashSet(0);
	private Set TOrders = new HashSet(0);

	// Constructors

	/** default constructor */
	public CTBRI_TCompanyAdmin() {
	}

	/** minimal constructor */
	public CTBRI_TCompanyAdmin(TCompanyInfo TCompanyInfo, String compAdminAccount,
			String compAdminPwd, String compAdminName, String compAdminEmail,
			String compAdminPhone, String compAdminMobile,
			String compAdminCreater, Timestamp compAdminCtime,
			Timestamp compModifyTime, Integer activeState) {
		this.TCompanyInfo = TCompanyInfo;
		this.compAdminAccount = compAdminAccount;
		this.compAdminPwd = compAdminPwd;
		this.compAdminName = compAdminName;
		this.compAdminEmail = compAdminEmail;
		this.compAdminPhone = compAdminPhone;
		this.compAdminMobile = compAdminMobile;
		this.compAdminCreater = compAdminCreater;
		this.compAdminCtime = compAdminCtime;
		this.compModifyTime = compModifyTime;
		this.activeState = activeState;
	}

	/** full constructor */
	public CTBRI_TCompanyAdmin(TCompanyInfo TCompanyInfo, String compAdminAccount,
			String compAdminPwd, String compAdminName, String compAdminEmail,
			String compAdminPhone, String compAdminMobile, String compAdminFax,
			String compAdminCreater, Timestamp compAdminCtime,
			String compModifyName, Timestamp compModifyTime, Integer activeState,
			String checkCode, Set TProductSales, Set TOrders) {
		this.TCompanyInfo = TCompanyInfo;
		this.compAdminAccount = compAdminAccount;
		this.compAdminPwd = compAdminPwd;
		this.compAdminName = compAdminName;
		this.compAdminEmail = compAdminEmail;
		this.compAdminPhone = compAdminPhone;
		this.compAdminMobile = compAdminMobile;
		this.compAdminFax = compAdminFax;
		this.compAdminCreater = compAdminCreater;
		this.compAdminCtime = compAdminCtime;
		this.compModifyName = compModifyName;
		this.compModifyTime = compModifyTime;
		this.activeState = activeState;
		this.checkCode = checkCode;
		this.TProductSales = TProductSales;
		this.TOrders = TOrders;
	}

	// Property accessors

	public Integer getCompAdminId() {
		return this.compAdminId;
	}

	public void setCompAdminId(Integer compAdminId) {
		this.compAdminId = compAdminId;
	}

	public TCompanyInfo getTCompanyInfo() {
		return this.TCompanyInfo;
	}

	public void setTCompanyInfo(TCompanyInfo TCompanyInfo) {
		this.TCompanyInfo = TCompanyInfo;
	}

	public String getCompAdminAccount() {
		return this.compAdminAccount;
	}

	public void setCompAdminAccount(String compAdminAccount) {
		this.compAdminAccount = compAdminAccount;
	}

	public String getCompAdminPwd() {
		return this.compAdminPwd;
	}

	public void setCompAdminPwd(String compAdminPwd) {
		this.compAdminPwd = compAdminPwd;
	}

	public String getCompAdminName() {
		return this.compAdminName;
	}

	public void setCompAdminName(String compAdminName) {
		this.compAdminName = compAdminName;
	}

	public String getCompAdminEmail() {
		return this.compAdminEmail;
	}

	public void setCompAdminEmail(String compAdminEmail) {
		this.compAdminEmail = compAdminEmail;
	}

	public String getCompAdminPhone() {
		return this.compAdminPhone;
	}

	public void setCompAdminPhone(String compAdminPhone) {
		this.compAdminPhone = compAdminPhone;
	}

	public String getCompAdminMobile() {
		return this.compAdminMobile;
	}

	public void setCompAdminMobile(String compAdminMobile) {
		this.compAdminMobile = compAdminMobile;
	}

	public String getCompAdminFax() {
		return this.compAdminFax;
	}

	public void setCompAdminFax(String compAdminFax) {
		this.compAdminFax = compAdminFax;
	}

	public String getCompAdminCreater() {
		return this.compAdminCreater;
	}

	public void setCompAdminCreater(String compAdminCreater) {
		this.compAdminCreater = compAdminCreater;
	}

	public Timestamp getCompAdminCtime() {
		return this.compAdminCtime;
	}

	public void setCompAdminCtime(Timestamp compAdminCtime) {
		this.compAdminCtime = compAdminCtime;
	}

	public String getCompModifyName() {
		return this.compModifyName;
	}

	public void setCompModifyName(String compModifyName) {
		this.compModifyName = compModifyName;
	}

	public Timestamp getCompModifyTime() {
		return this.compModifyTime;
	}

	public void setCompModifyTime(Timestamp compModifyTime) {
		this.compModifyTime = compModifyTime;
	}

	public Integer getActiveState() {
		return this.activeState;
	}

	public void setActiveState(Integer activeState) {
		this.activeState = activeState;
	}

	public String getCheckCode() {
		return this.checkCode;
	}

	public void setCheckCode(String checkCode) {
		this.checkCode = checkCode;
	}

	public Set getTProductSales() {
		return this.TProductSales;
	}

	public void setTProductSales(Set TProductSales) {
		this.TProductSales = TProductSales;
	}

	public Set getTOrders() {
		return this.TOrders;
	}

	public void setTOrders(Set TOrders) {
		this.TOrders = TOrders;
	}


}
