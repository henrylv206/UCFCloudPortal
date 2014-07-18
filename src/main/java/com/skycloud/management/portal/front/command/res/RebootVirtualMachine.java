package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class RebootVirtualMachine extends BaseCommandPo {

	public static final String COMMAND = "rebootVirtualMachine";
	public static final String ID = "id";
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
	private long id = 0;

	public RebootVirtualMachine(long id) {
		super(COMMAND);
		this.setId(id);
	}

	public RebootVirtualMachine() {
		super(COMMAND);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
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
		if(id == 0){
			throw new ServiceException("缺少必填参数：id");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			RebootVirtualMachine po = (RebootVirtualMachine)JsonUtil.getObject4JsonString(tempJson, RebootVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
