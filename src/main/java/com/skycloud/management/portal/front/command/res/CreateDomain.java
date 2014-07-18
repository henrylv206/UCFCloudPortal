package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class CreateDomain extends BaseCommandPo{
	public static final String COMMAND = "createDomain";
	public static final String NAME = "name";
	private String name;
	private String PARENT_DOMAIN_ID = "parentdomainid";
	private String parentDomainId;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}

	public CreateDomain() {
		super(COMMAND);
	}

	public CreateDomain(String name) {
		super(COMMAND);
		this.setName(name);
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}
	
	public String getParentDomainId() {
		return parentDomainId;
	}

	public void setParentDomainId(String parentDomainId) {
		this.parentDomainId = parentDomainId;
		this.setParameter(PARENT_DOMAIN_ID, parentDomainId);
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(name)){
			throw new ServiceException("缺少必填参数：name");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[CreateVolume]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			CreateDomain po = (CreateDomain)JsonUtil.getObject4JsonString(tempJson, CreateDomain.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateDomain]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateDomain]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}
