package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class DeleteDomain extends BaseCommandPo{
	public static final String COMMAND = "deleteDomain";
	public static final String ID = "id";
	
	
	private String id;

	public DeleteDomain() {
		super(COMMAND);
	}
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(id)){
			throw new ServiceException("缺少必填参数：id");
		}
		return super.fromPoToJsonStr(po);
	}

	@Override
	protected QueryCommand fromJsonToOperatePo(String jsonStr) {
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json串失败[DeleteDomain]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DeleteDomain po = (DeleteDomain)JsonUtil.getObject4JsonString(tempJson, DeleteDomain.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json串失败[DeleteDomain]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json串失败[DeleteDomain]方法fromJsonToOperatePo："+e.getMessage());
		}
	}

}