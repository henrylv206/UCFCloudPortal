package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class DestroyVirtualMachine  extends BaseCommandPo{
	
	public static final String COMMAND = "destroyVirtualMachine";
	
	public static final String ID = "id";
	
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
	private int id = 0;
	
	public DestroyVirtualMachine() {
		super(COMMAND);
	}
	
	public DestroyVirtualMachine(int id) {
		super(COMMAND);
		this.setParameter(ID, id);
	}

	public int getId() {
		return id;
	}


	public void setId(int id) {
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
				throw new ServiceException("解析Json传失败[DestroyVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			DestroyVirtualMachine po = (DestroyVirtualMachine)JsonUtil.getObject4JsonString(tempJson, DestroyVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[DestroyVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[DestroyVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
