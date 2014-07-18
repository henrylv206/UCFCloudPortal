package com.skycloud.management.portal.admin.customer.entity;

import java.util.TimeZone;

import com.skycloud.management.portal.front.command.impl.Command;


public class CTBRI_CreateAccount extends Command {
	public static final String COMMAND = "createAccount";
	
	public static final String ACCOUNT_TYPE = "accounttype";
	public static final String EMAIL = "email";
	public static final String FIRSTNAME = "firstname";
	public static final String LASTNAME = "lastname";
	public static final String PASSWORD = "password";
	public static final String USERNAME = "username";
	public static final String ACCOUNT = "account";
	public static final String DOMAIN_ID = "domainid";
	public static final String TIMEZONE = "timezone";
	
	private int accountType;
	private String email;
	private String firstname;
	private String lastname;
	private String password;
	private String username;
	private String account;
	private long domainId;
	private TimeZone timezone;
	/*
accounttype	Type of the account. Specify 0 for user, 1 for root admin, and 2 for domain admin	TRUE
email	email	TRUE
firstname	firstname	TRUE
lastname	lastname	TRUE
password	Hashed password (Default is MD5). If you wish to use any other hashing algorithm, you would need to write a custom authentication adapter See Docs section.	TRUE
username	Unique username.	TRUE
account	Creates the user under the specified account. If no account is specified, the username will be used as the account name.	FALSE
domainid	Creates the user under the specified domain.	FALSE
timezone	Specifies a timezone for this command. For more information on the timezone parameter, see Time Zone Format.	FALSE
	 */
	public CTBRI_CreateAccount(long domainId, String username, String password, String email, String firstname, String lastname) {
		this(2, domainId, username, password, email, firstname, lastname);
	}
	
	public CTBRI_CreateAccount(int accountType, long domainId, String username, String password, String email, String firstname, String lastname) {
		super(COMMAND);
		this.setDomainId(domainId);
		this.setAccountType(accountType);
		this.setUsername(username);
		this.setPassword(password);
		this.setEmail(email);
		this.setFirstname(firstname);
		this.setLastname(lastname);
	}
	
	
	public int getAccountType() {
		return accountType;
	}
	public void setAccountType(int accountType) {
		this.accountType = accountType;
		this.setParameter(ACCOUNT_TYPE, accountType);
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
		this.setParameter(ACCOUNT_TYPE, account);
	}
	public long getDomainId() {
		return domainId;
	}
	public void setDomainId(long domainId) {
		this.domainId = domainId;
		this.setParameter(DOMAIN_ID, domainId);
	}
	public TimeZone getTimezone() {
		return timezone;
	}
	public void setTimezone(TimeZone timezone) {
		this.timezone = timezone;
		this.setParameter(TIMEZONE, timezone);
	}

}
