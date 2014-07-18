package com.skycloud.management.portal.front.customer.entity;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.struts2.json.annotations.JSON;


/**
 * TCompanyInfo entity. @author MyEclipse Persistence Tools
 */

public class TCompanyInfo implements java.io.Serializable {

	// Fields

	private Integer compId;
	private TClass TClass;
	private TProperty TProperty;
	private TCity TCity;
	private TCategory TCategory;
	private String compOrgCode;
	private String compCnName;
	private String compEnName;
	private String compCnAbbreviation;
	private String compEnAbbreviation;
	private String busLicenseNum;
	private Date blnStartTime;
	private Date blnEndTime;
	private String compLegalPerson;
	private String compLegalPersonId;
	private String compAddress;
	private String postCode;
	private String compPhone;
	private String compFax;
	private String compEmail;
	private String compBankName;
	private String compBankAccount;
	private String relationName;
	private String relaDepName;
	private String relaPhone;
	private String relaMobile;
	private String relaFax;
	private String relaEmail;
	private Timestamp compRegTime;
	private String manager;
	private String compCreater;
	private Timestamp compCreateTime;
	private String compModifier;
	private Timestamp compModifyTime;
	private Timestamp firCheckTime;
	private String firCheckComment;
	private Timestamp secCheckTime;
	private String secCheckComment;
	private Integer checkState;
	private String domain;
	private Integer dataCenter;
	private Set TCompanyAdmins = new HashSet(0);
	private Set TCompUgroups = new HashSet(0);
	private Set TCompanyUsers = new HashSet(0);
	private Set TOrders = new HashSet(0);
	private String jbpmTaskId;
	private String cityId;
	private String compPropertyId;
	private String compCategoryId;
	private String compClassId;

	// Constructors

	/** default constructor */
	public TCompanyInfo() {
	}

	/** minimal constructor */
	public TCompanyInfo(TClass TClass, TProperty TProperty, TCity TCity,
			TCategory TCategory, String compOrgCode, String compCnName,
			String compEnName, String compCnAbbreviation,
			String compEnAbbreviation, String busLicenseNum,
			Timestamp blnStartTime, Timestamp blnEndTime,
			String compLegalPerson, String compLegalPersonId,
			String compAddress, String postCode, String compPhone,
			String compFax, String compEmail, String compBankName,
			String compBankAccount, String relationName, String relaDepName,
			String relaPhone, String relaMobile, String relaEmail,
			Timestamp compRegTime, String compCreater, String manager,
			Timestamp compCreateTime, Timestamp compModifyTime,
			Timestamp firCheckTime, Timestamp secCheckTime, Integer checkState,String domain,Integer dataCenter) {
		this.TClass = TClass;
		this.TProperty = TProperty;
		this.TCity = TCity;
		this.TCategory = TCategory;
		this.compOrgCode = compOrgCode;
		this.compCnName = compCnName;
		this.compEnName = compEnName;
		this.compCnAbbreviation = compCnAbbreviation;
		this.compEnAbbreviation = compEnAbbreviation;
		this.busLicenseNum = busLicenseNum;
		this.blnStartTime = blnStartTime;
		this.blnEndTime = blnEndTime;
		this.compLegalPerson = compLegalPerson;
		this.compLegalPersonId = compLegalPersonId;
		this.compAddress = compAddress;
		this.postCode = postCode;
		this.compPhone = compPhone;
		this.compFax = compFax;
		this.compEmail = compEmail;
		this.compBankName = compBankName;
		this.compBankAccount = compBankAccount;
		this.relationName = relationName;
		this.relaDepName = relaDepName;
		this.relaPhone = relaPhone;
		this.relaMobile = relaMobile;
		this.relaEmail = relaEmail;
		this.compRegTime = compRegTime;
		this.manager=manager;
		this.compCreater = compCreater;
		this.compCreateTime = compCreateTime;
		this.compModifyTime = compModifyTime;
		this.firCheckTime = firCheckTime;
		this.secCheckTime = secCheckTime;
		this.checkState = checkState;
		this.domain = domain;
		this.dataCenter = dataCenter;
	}

	/** full constructor */
	public TCompanyInfo(TClass TClass, TProperty TProperty, TCity TCity,
			TCategory TCategory, String compOrgCode, String compCnName,
			String compEnName, String compCnAbbreviation,
			String compEnAbbreviation, String busLicenseNum,
			Timestamp blnStartTime, Timestamp blnEndTime,
			String compLegalPerson, String compLegalPersonId,
			String compAddress, String postCode, String compPhone,
			String compFax, String compEmail, String compBankName,
			String compBankAccount, String relationName, String relaDepName,
			String relaPhone, String relaMobile, String relaFax,
			String relaEmail, Timestamp compRegTime, String compCreater,String manager,
			Timestamp compCreateTime, String compModifier,
			Timestamp compModifyTime,
			Timestamp firCheckTime, String firCheckComment, 
			Timestamp secCheckTime, String secCheckComment, Integer checkState,String domain,Integer dataCenter,
			Set TCompanyAdmins, Set TCompUgroups, Set TCompanyUsers, Set TOrders) {
		this.TClass = TClass;
		this.TProperty = TProperty;
		this.TCity = TCity;
		this.TCategory = TCategory;
		this.compOrgCode = compOrgCode;
		this.compCnName = compCnName;
		this.compEnName = compEnName;
		this.compCnAbbreviation = compCnAbbreviation;
		this.compEnAbbreviation = compEnAbbreviation;
		this.busLicenseNum = busLicenseNum;
		this.blnStartTime = blnStartTime;
		this.blnEndTime = blnEndTime;
		this.compLegalPerson = compLegalPerson;
		this.compLegalPersonId = compLegalPersonId;
		this.compAddress = compAddress;
		this.postCode = postCode;
		this.compPhone = compPhone;
		this.compFax = compFax;
		this.compEmail = compEmail;
		this.compBankName = compBankName;
		this.compBankAccount = compBankAccount;
		this.relationName = relationName;
		this.relaDepName = relaDepName;
		this.relaPhone = relaPhone;
		this.relaMobile = relaMobile;
		this.relaFax = relaFax;
		this.relaEmail = relaEmail;
		this.compRegTime = compRegTime;
		this.manager = manager;
		this.compCreater = compCreater;
		this.compCreateTime = compCreateTime;
		this.compModifier = compModifier;
		this.compModifyTime = compModifyTime;
		this.firCheckTime = firCheckTime;
		this.firCheckComment = firCheckComment;
		this.secCheckTime = secCheckTime;
		this.secCheckComment = secCheckComment;
		this.checkState = checkState;
		this.domain = domain;
		this.dataCenter = dataCenter;
		this.TCompanyAdmins = TCompanyAdmins;
		this.TCompUgroups = TCompUgroups;
		this.TCompanyUsers = TCompanyUsers;
		this.TOrders = TOrders;
	}

	// Property accessors
	
	public Integer getCompId() {
		return this.compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public TClass getTClass() {
		return this.TClass;
	}

	public void setTClass(TClass TClass) {
		this.TClass = TClass;
	}

	public TProperty getTProperty() {
		return this.TProperty;
	}

	public void setTProperty(TProperty TProperty) {
		this.TProperty = TProperty;
	}

	public TCity getTCity() {
		return this.TCity;
	}

	public void setTCity(TCity TCity) {
		this.TCity = TCity;
	}

	public TCategory getTCategory() {
		return this.TCategory;
	}

	public void setTCategory(TCategory TCategory) {
		this.TCategory = TCategory;
	}

	public String getCompOrgCode() {
		return this.compOrgCode;
	}

	public void setCompOrgCode(String compOrgCode) {
		this.compOrgCode = compOrgCode;
	}

	public String getCompCnName() {
		return this.compCnName;
	}

	public void setCompCnName(String compCnName) {
		this.compCnName = compCnName;
	}

	public String getCompEnName() {
		return this.compEnName;
	}

	public void setCompEnName(String compEnName) {
		this.compEnName = compEnName;
	}

	public String getCompCnAbbreviation() {
		return this.compCnAbbreviation;
	}

	public void setCompCnAbbreviation(String compCnAbbreviation) {
		this.compCnAbbreviation = compCnAbbreviation;
	}

	public String getCompEnAbbreviation() {
		return this.compEnAbbreviation;
	}

	public void setCompEnAbbreviation(String compEnAbbreviation) {
		this.compEnAbbreviation = compEnAbbreviation;
	}

	public String getBusLicenseNum() {
		return this.busLicenseNum;
	}

	public void setBusLicenseNum(String busLicenseNum) {
		this.busLicenseNum = busLicenseNum;
	}

	@JSON(format="yyyy-MM-dd")
	public Date getBlnStartTime() {
		return this.blnStartTime;
	}
	@JSON(format="yyyy-MM-dd")
	public void setBlnStartTime(Date blnStartTime) {
		this.blnStartTime = blnStartTime;
	}
	
	@JSON(format="yyyy-MM-dd")
	public Date getBlnEndTime() {
		return this.blnEndTime;
	}
	@JSON(format="yyyy-MM-dd")
	public void setBlnEndTime(Date blnEndTime) {
		this.blnEndTime = blnEndTime;
	}

	public String getCompLegalPerson() {
		return this.compLegalPerson;
	}

	public void setCompLegalPerson(String compLegalPerson) {
		this.compLegalPerson = compLegalPerson;
	}

	public String getCompLegalPersonId() {
		return this.compLegalPersonId;
	}

	public void setCompLegalPersonId(String compLegalPersonId) {
		this.compLegalPersonId = compLegalPersonId;
	}

	public String getCompAddress() {
		return this.compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}

	public String getPostCode() {
		return this.postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCompPhone() {
		return this.compPhone;
	}

	public void setCompPhone(String compPhone) {
		this.compPhone = compPhone;
	}

	public String getCompFax() {
		return this.compFax;
	}

	public void setCompFax(String compFax) {
		this.compFax = compFax;
	}

	public String getCompEmail() {
		return this.compEmail;
	}

	public void setCompEmail(String compEmail) {
		this.compEmail = compEmail;
	}

	public String getCompBankName() {
		return this.compBankName;
	}

	public void setCompBankName(String compBankName) {
		this.compBankName = compBankName;
	}

	public String getCompBankAccount() {
		return this.compBankAccount;
	}

	public void setCompBankAccount(String compBankAccount) {
		this.compBankAccount = compBankAccount;
	}

	public String getRelationName() {
		return this.relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getRelaDepName() {
		return this.relaDepName;
	}

	public void setRelaDepName(String relaDepName) {
		this.relaDepName = relaDepName;
	}

	public String getRelaPhone() {
		return this.relaPhone;
	}

	public void setRelaPhone(String relaPhone) {
		this.relaPhone = relaPhone;
	}

	public String getRelaMobile() {
		return this.relaMobile;
	}

	public void setRelaMobile(String relaMobile) {
		this.relaMobile = relaMobile;
	}

	public String getRelaFax() {
		return this.relaFax;
	}

	public void setRelaFax(String relaFax) {
		this.relaFax = relaFax;
	}

	public String getRelaEmail() {
		return this.relaEmail;
	}

	public void setRelaEmail(String relaEmail) {
		this.relaEmail = relaEmail;
	}
	@JSON(format="yyyy-MM-dd")
	public Timestamp getCompRegTime() {
		return this.compRegTime;
	}

	public void setCompRegTime(Timestamp compRegTime) {
		this.compRegTime = compRegTime;
	}
	
	public String getManager() {
		return manager;
	}

	public void setManager(String manager) {
		this.manager = manager;
	}

	public String getCompCreater() {
		return this.compCreater;
	}

	public void setCompCreater(String compCreater) {
		this.compCreater = compCreater;
	}
	 @JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCompCreateTime() {
		return this.compCreateTime;
	}

	public void setCompCreateTime(Timestamp compCreateTime) {
		this.compCreateTime = compCreateTime;
	}

	public String getCompModifier() {
		return this.compModifier;
	}

	public void setCompModifier(String compModifier) {
		this.compModifier = compModifier;
	}
	@JSON(format="yyyy-MM-dd HH:mm:ss")
	public Timestamp getCompModifyTime() {
		return compModifyTime;
	}

	public void setCompModifyTime(Timestamp compModifyTime) {
		this.compModifyTime = compModifyTime;
	}

	public Timestamp getFirCheckTime() {
		return this.firCheckTime;
	}

	public void setFirCheckTime(Timestamp firCheckTime) {
		this.firCheckTime = firCheckTime;
	}

	public String getFirCheckComment() {
		return this.firCheckComment;
	}

	public void setFirCheckComment(String firCheckComment) {
		this.firCheckComment = firCheckComment;
	}

	public Timestamp getSecCheckTime() {
		return this.secCheckTime;
	}

	public void setSecCheckTime(Timestamp secCheckTime) {
		this.secCheckTime = secCheckTime;
	}

	public String getSecCheckComment() {
		return this.secCheckComment;
	}

	public void setSecCheckComment(String secCheckComment) {
		this.secCheckComment = secCheckComment;
	}

	public Integer getCheckState() {
		return this.checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	public Integer getDataCenter() {
		return dataCenter;
	}

	public void setDataCenter(Integer dataCenter) {
		this.dataCenter = dataCenter;
	}

	public Set getTCompanyAdmins() {
		return this.TCompanyAdmins;
	}

	public void setTCompanyAdmins(Set TCompanyAdmins) {
		this.TCompanyAdmins = TCompanyAdmins;
	}

	public Set getTCompUgroups() {
		return this.TCompUgroups;
	}

	public void setTCompUgroups(Set TCompUgroups) {
		this.TCompUgroups = TCompUgroups;
	}

	public Set getTCompanyUsers() {
		return this.TCompanyUsers;
	}

	public void setTCompanyUsers(Set TCompanyUsers) {
		this.TCompanyUsers = TCompanyUsers;
	}

	public Set getTOrders() {
		return this.TOrders;
	}

	public void setTOrders(Set TOrders) {
		this.TOrders = TOrders;
	}

	

	public String getJbpmTaskId() {
		return jbpmTaskId;
	}

	public void setJbpmTaskId(String jbpmTaskId) {
		this.jbpmTaskId = jbpmTaskId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCompPropertyId() {
		return compPropertyId;
	}

	public void setCompPropertyId(String compPropertyId) {
		this.compPropertyId = compPropertyId;
	}

	public String getCompCategoryId() {
		return compCategoryId;
	}

	public void setCompCategoryId(String compCategoryId) {
		this.compCategoryId = compCategoryId;
	}

	public String getCompClassId() {
		return compClassId;
	}

	public void setCompClassId(String compClassId) {
		this.compClassId = compClassId;
	}

	
	
	
}