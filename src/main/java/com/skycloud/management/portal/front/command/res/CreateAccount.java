package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class CreateAccount extends BaseCommandPo{	

	public static final String COMMAND = "createAccount";
	public static final String ACCOUNTTYPE = "accounttype";
	public static final String EMAIL = "email";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String ACCOUNT = "account";
	public static final String DOMAINID = "domainid";
	public static final String TIMEZONE = "timezone";
	
	
	private String accounttype;
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private String username;
	private String account;
	private String domainid;
	private String timezone;
	
	
	public CreateAccount() {
		super(COMMAND);
	}
	public CreateAccount(String name) {
		super(COMMAND);
		// TODO Auto-generated constructor stub
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(username)){
			throw new ServiceException("缺少必填参数：name");
		}
		return super.fromPoToJsonStr(po);
	}
	
	public String getAccounttype() {
		return accounttype;
	}
	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
		this.setParameter(ACCOUNTTYPE, accounttype);
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
		this.setParameter(EMAIL, email);
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
		this.setParameter(FIRSTNAME, firstname);
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
		this.setParameter(LASTNAME, lastname);
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
		this.setParameter(PASSWORD, password);
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
		this.setParameter(USERNAME, username);
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
		this.setParameter(ACCOUNT, account);
	}
	public String getDomainid() {
		return domainid;
	}
	public void setDomainid(String domainid) {
		this.domainid = domainid;
		this.setParameter(DOMAINID, domainid);
	}
	public String getTimezone() {
		return timezone;
	}
	public void setTimezone(String timezone) {
		this.timezone = timezone;
		this.setParameter(TIMEZONE, timezone);
	}
	@Override	
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[CreateAccount]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			CreateAccount po = (CreateAccount)JsonUtil.getObject4JsonString(tempJson, CreateAccount.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateVolume]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateAccount]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
