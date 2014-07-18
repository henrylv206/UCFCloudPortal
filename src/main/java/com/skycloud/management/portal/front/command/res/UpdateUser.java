package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class UpdateUser extends BaseCommandPo {
	public static final String COMMAND = "updateUser";
	public static final String ID = "id";
	public static final String EMAIL = "email";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String TIMEZONE = "timezone";
	
	
	private String id;
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private String username;
	private String timezone;
	
	public UpdateUser() {
		super(COMMAND);
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		return null;
	}	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.setParameter(ID, id);
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
				throw new ServiceException("[UPdateUser].fromJsonToOperatePo：Json is null");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			CreateAccount po = (CreateAccount)JsonUtil.getObject4JsonString(tempJson, CreateAccount.class);
			return po;
		}catch(Exception e){
			logger.error("[UPdateUser].fromJsonToOperatePo：",e);
			throw new RuntimeException("[UPdateUser].fromJsonToOperatePo："+e.getMessage());
		}
	}
	
}
