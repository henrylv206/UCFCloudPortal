package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class DisableAccount extends BaseCommandPo{
	public static final String COMMAND = "disableAccount";
	public static final String ACCOUNT = "account";
	public static final String DOMAINID = "domainid";
	public static final String LOCK = "lock";
	
	
	private String account;
	private String domainid;
	private String lock;

	public DisableAccount() {
		super(COMMAND);
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


	public String getLock() {
		return lock;
	}


	public void setLock(String lock) {
		this.lock = lock;
		this.setParameter(LOCK, lock);
	}


	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(account)){
			throw new ServiceException("缺少必填参数：account");
		}
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json串失败[DisableAccount]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DisableAccount po = (DisableAccount)JsonUtil.getObject4JsonString(tempJson, DisableAccount.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json串失败[DisableAccount]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json串失败[DisableAccount]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
