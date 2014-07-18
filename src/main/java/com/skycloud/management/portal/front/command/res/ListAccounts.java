package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class ListAccounts extends BaseCommandPo{
	public static final String COMMAND = "listAccounts";
	public static final String ACCOUNT = "account";
	public static final String ACCOUNTTYPE = "accounttype";
	public static final String NAME = "name";
	
	private String account;
	private String id;
	private String domainid;
	private String name;
	private String accounttype;
	
	public ListAccounts() {
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;	
		this.setParameter(NAME, name);
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
		if(StringUtils.isBlank(name)){
			throw new ServiceException("缺少必填参数：name");
		}
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json串失败[ListAccounts]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			ListAccounts po = (ListAccounts)JsonUtil.getObject4JsonString(tempJson, ListAccounts.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json串失败[ListAccounts]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json串失败[ListAccounts]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
