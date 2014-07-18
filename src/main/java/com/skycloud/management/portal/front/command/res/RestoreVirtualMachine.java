package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class RestoreVirtualMachine extends BaseCommandPo {

	public static final String COMMAND = "restoreVirtualMachine";
	
	public static final String ID = "id";
	
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
    private int id = 0;
    /**
	 * Force stop the VM. The caller knows the VM is stopped
	 * Null
	 */
	
	public RestoreVirtualMachine(){
		 super(COMMAND);
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
				throw new ServiceException("解析Json传失败[StopVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			StopVirtualMachine po = (StopVirtualMachine)JsonUtil.getObject4JsonString(tempJson, StopVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[StopVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[StopVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
