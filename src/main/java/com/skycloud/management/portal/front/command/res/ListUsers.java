package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class ListUsers extends BaseCommandPo{
	public static final String COMMAND = "listUsers";
	public static final String ACCOUNT = "account";
	public static final String ACCOUNTTYPE = "accounttype";
	public static final String USERNAME = "username";
	
	private String account;
	private String id;
	private String domainid;
	private String username;
	private String accounttype;
	
	public ListUsers() {
		super(COMMAND);
	}
	
	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
		this.setParameter(ACCOUNT, account);
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;		
	}

	public String getDomainid() {
		return domainid;
	}

	public void setDomainid(String domainid) {
		this.domainid = domainid;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;	
		this.setParameter(USERNAME, username);
	}

	public String getAccounttype() {
		return accounttype;
	}

	public void setAccounttype(String accounttype) {
		this.accounttype = accounttype;
		this.setParameter(ACCOUNTTYPE, accounttype);
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
			throw new ServiceException("缺少必填参数：username");
		}
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json串失败[ListUsers]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			ListUsers po = (ListUsers)JsonUtil.getObject4JsonString(tempJson, ListUsers.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json串失败[ListUsers]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json串失败[ListUsers]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
