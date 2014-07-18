package com.skycloud.management.portal.admin.customer.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;



/**
 * TCompanyInfo entity. @author MyEclipse Persistence Tools
 */

public class CTBRI_TCompanyInfo implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1183045985884891839L;
	// Fields
	
	private Integer compId;
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
	private String cityId;
	private String compCategoryId;
	private String compPropertyId;
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
	private String compClassId;
	private Timestamp compRegTime;
	private String manager;
	private String compCreater;
	private Timestamp compCreateTime;
	private String compModifier;
	private Timestamp compModifyTime;
	private String firCheckId; 
	private Timestamp firCheckTime;
	private String name;
	private String email;
	private String categoryName;
	private String className;
	private String cityName;
	private String propertyName;
	private String zoneName;
	private String secChekId;
	private Timestamp secCheckTime;
	private String secCheckComment;
	private Integer checkState;
	private String checkStateEx;
	private String domain;
	private Integer dataCenter;
	private String firCheckComment;
	private String account;
	private int userId;
	private String createDt;
	private String blnStartTimeString;
	private String blnEndTimeString;
	private String newpwd;
	private String renewpwd;
	
	public CTBRI_TCompanyInfo(){
		super();
	}
	
	public CTBRI_TCompanyInfo(Integer compId, String compOrgCode,
			String compCnName, String compEnName, String compCnAbbreviation,
			String compEnAbbreviation, String busLicenseNum, Date blnStartTime,
			Date blnEndTime, String compLegalPerson, String compLegalPersonId,
			String cityId, String compCategoryId, String compPropertyId,
			String compAddress, String postCode, String compPhone,
			String compFax, String compEmail, String compBankName,
			String compBankAccount, String relationName, String relaDepName,
			String relaPhone, String relaMobile, String relaFax,
			String relaEmail, String compClassId, Timestamp compRegTime,
			String manager, String compCreater, Timestamp compCreateTime,
			String compModifier, Timestamp compModifyTime, String firCheckId,
			Timestamp firCheckTime, String name, String email,
			String categoryName, String className, String cityName,
			String propertyName, String zoneName, String secChekId,
			Timestamp secCheckTime, String secCheckComment, Integer checkState,
			String checkStateEx, String domain, Integer dataCenter,
			String firCheckComment, String account, int userId, String createDt) {
		super();
		this.compId = compId;
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
		this.cityId = cityId;
		this.compCategoryId = compCategoryId;
		this.compPropertyId = compPropertyId;
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
		this.compClassId = compClassId;
		this.compRegTime = compRegTime;
		this.manager = manager;
		this.compCreater = compCreater;
		this.compCreateTime = compCreateTime;
		this.compModifier = compModifier;
		this.compModifyTime = compModifyTime;
		this.firCheckId = firCheckId;
		this.firCheckTime = firCheckTime;
		this.name = name;
		this.email = email;
		this.categoryName = categoryName;
		this.className = className;
		this.cityName = cityName;
		this.propertyName = propertyName;
		this.zoneName = zoneName;
		this.secChekId = secChekId;
		this.secCheckTime = secCheckTime;
		this.secCheckComment = secCheckComment;
		this.checkState = checkState;
		this.checkStateEx = checkStateEx;
		this.domain = domain;
		this.dataCenter = dataCenter;
		this.firCheckComment = firCheckComment;
		this.account = account;
		this.userId = userId;
		this.createDt = createDt;
	}

	public Integer getCompId() {
		return compId;
	}

	public void setCompId(Integer compId) {
		this.compId = compId;
	}

	public String getCompOrgCode() {
		return compOrgCode;
	}

	public void setCompOrgCode(String compOrgCode) {
		this.compOrgCode = compOrgCode;
	}

	public String getCompCnName() {
		return compCnName;
	}

	public void setCompCnName(String compCnName) {
	    this.compCnName = compCnName;
	}

	public String getCompEnName() {
		return compEnName;
	}

	public void setCompEnName(String compEnName) {
		this.compEnName = compEnName;
	}

	public String getCompCnAbbreviation() {
		return compCnAbbreviation;
	}

	public void setCompCnAbbreviation(String compCnAbbreviation) {
		this.compCnAbbreviation = compCnAbbreviation;
	}

	public String getCompEnAbbreviation() {
		return compEnAbbreviation;
	}

	public void setCompEnAbbreviation(String compEnAbbreviation) {
		this.compEnAbbreviation = compEnAbbreviation;
	}

	public String getBusLicenseNum() {
		return busLicenseNum;
	}

	public void setBusLicenseNum(String busLicenseNum) {
		this.busLicenseNum = busLicenseNum;
	}

	public Date getBlnStartTime() {
		return blnStartTime;
	}

	public void setBlnStartTime(Date blnStartTime) {
		if(blnStartTime!=null){
			this.blnStartTimeString=(new SimpleDateFormat("yyyy-MM-dd")).format(blnStartTime);
		}
		this.blnStartTime = blnStartTime;
	}

	public Date getBlnEndTime() {
		return blnEndTime;
	}

	public void setBlnEndTime(Date blnEndTime) {
		if(blnEndTime!=null){
			this.blnEndTimeString=(new SimpleDateFormat("yyyy-MM-dd")).format(blnEndTime);
		}
		this.blnEndTime = blnEndTime;
	}

	public String getCompLegalPerson() {
		return compLegalPerson;
	}

	public void setCompLegalPerson(String compLegalPerson) {
		this.compLegalPerson = compLegalPerson;
	}

	public String getCompLegalPersonId() {
		return compLegalPersonId;
	}

	public void setCompLegalPersonId(String compLegalPersonId) {
		this.compLegalPersonId = compLegalPersonId;
	}

	public String getCityId() {
		return cityId;
	}

	public void setCityId(String cityId) {
		this.cityId = cityId;
	}

	public String getCompCategoryId() {
		return compCategoryId;
	}

	public void setCompCategoryId(String compCategoryId) {
		this.compCategoryId = compCategoryId;
	}

	public String getCompPropertyId() {
		return compPropertyId;
	}

	public void setCompPropertyId(String compPropertyId) {
		
		this.compPropertyId = compPropertyId;
	}

	public String getCompAddress() {
		return compAddress;
	}

	public void setCompAddress(String compAddress) {
		this.compAddress = compAddress;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getCompPhone() {
		return compPhone;
	}

	public void setCompPhone(String compPhone) {
		this.compPhone = compPhone;
	}

	public String getCompFax() {
		return compFax;
	}

	public void setCompFax(String compFax) {
		this.compFax = compFax;
	}

	public String getCompEmail() {
		return compEmail;
	}

	public void setCompEmail(String compEmail) {
		this.compEmail = compEmail;
	}

	public String getCompBankName() {
		return compBankName;
	}

	public void setCompBankName(String compBankName) {
		this.compBankName = compBankName;
	}

	public String getCompBankAccount() {
		return compBankAccount;
	}

	public void setCompBankAccount(String compBankAccount) {
		this.compBankAccount = compBankAccount;
	}

	public String getRelationName() {
		return relationName;
	}

	public void setRelationName(String relationName) {
		this.relationName = relationName;
	}

	public String getRelaDepName() {
		return relaDepName;
	}

	public void setRelaDepName(String relaDepName) {
		this.relaDepName = relaDepName;
	}

	public String getRelaPhone() {
		return relaPhone;
	}

	public void setRelaPhone(String relaPhone) {
		this.relaPhone = relaPhone;
	}

	public String getRelaMobile() {
		return relaMobile;
	}

	public void setRelaMobile(String relaMobile) {
		this.relaMobile = relaMobile;
	}

	public String getRelaFax() {
		return relaFax;
	}

	public void setRelaFax(String relaFax) {
		this.relaFax = relaFax;
	}

	public String getRelaEmail() {
		return relaEmail;
	}

	public void setRelaEmail(String relaEmail) {
		this.relaEmail = relaEmail;
	}

	public String getCompClassId() {
		return compClassId;
	}

	public void setCompClassId(String compClassId) {
		this.compClassId = compClassId;
	}

	public Timestamp getCompRegTime() {
		return compRegTime;
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
		return compCreater;
	}

	public void setCompCreater(String compCreater) {
		this.compCreater = compCreater;
	}

	public Timestamp getCompCreateTime() {
		return compCreateTime;
	}

	public void setCompCreateTime(Timestamp compCreateTime) {
		this.compCreateTime = compCreateTime;
	}

	public String getCompModifier() {
		return compModifier;
	}

	public void setCompModifier(String compModifier) {
		this.compModifier = compModifier;
	}

	public Timestamp getCompModifyTime() {
		return compModifyTime;
	}

	public void setCompModifyTime(Timestamp compModifyTime) {
		this.compModifyTime = compModifyTime;
	}

	public String getFirCheckId() {
		return firCheckId;
	}

	public void setFirCheckId(String firCheckId) {
		this.firCheckId = firCheckId;
	}

	public Timestamp getFirCheckTime() {
		return firCheckTime;
	}

	public void setFirCheckTime(Timestamp firCheckTime) {
		this.firCheckTime = firCheckTime;
	}

	public String getFirCheckComment() {
		return firCheckComment;
	}

	public void setFirCheckComment(String firCheckComment) {
		this.firCheckComment = firCheckComment;
	}

	public String getSecChekId() {
		return secChekId;
	}

	public void setSecChekId(String secChekId) {
		this.secChekId = secChekId;
	}


	public String getSecCheckComment() {
		return this.secCheckComment;
	}

	public void setSecCheckComment(String secCheckComment) {
		this.secCheckComment = secCheckComment;
	}

	public Integer getCheckState() {
		return checkState;
	}

	public void setCheckState(Integer checkState) {
		this.checkState = checkState;
		if((int)checkState==2){
		this.setCheckStateEx("待审核");
		}
        if((int)checkState==3){
        	this.setCheckStateEx("已审核-注销/失败");	
		}
        if((int)checkState==4){
        	this.setCheckStateEx("已审核-可用");
        }
        if((int)checkState==5){
        	this.setCheckStateEx("已审核-挂起");
        }
        if((int)checkState==6){
        	this.setCheckStateEx("已审核-待激活");
        }
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

	// Constructors

	public void setEmail(String email) {
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public String getCategoryName() {
		return categoryName;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getClassName() {
		return className;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getCityName() {
		return cityName;
	}


	public void setCheckStateEx(String checkStateEx) {
		this.checkStateEx = checkStateEx;
	}

	public String getCheckStateEx() {
		return checkStateEx;
	}

	public void setSecCheckTime(Timestamp secCheckTime) {
		this.secCheckTime = secCheckTime;
	}

	public Timestamp getSecCheckTime() {
		return secCheckTime;
	}

	public void setZoneName(String zoneName) {
		this.zoneName = zoneName;
	}

	public String getZoneName() {
		return zoneName;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getAccount() {
		return account;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public String getBlnStartTimeString() {
		return blnStartTimeString;
	}

	public void setBlnStartTimeString(String blnStartTimeString) {
		this.blnStartTimeString = blnStartTimeString;
	}

	public String getBlnEndTimeString() {
		return blnEndTimeString;
	}

	public void setBlnEndTimeString(String blnEndTimeString) {
		this.blnEndTimeString = blnEndTimeString;
	}

	public String getNewpwd() {
		return newpwd;
	}

	public void setNewpwd(String newpwd) {
		this.newpwd = newpwd;
	}

	public String getRenewpwd() {
		return renewpwd;
	}

	public void setRenewpwd(String renewpwd) {
		this.renewpwd = renewpwd;
	}



}