package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class StartVirtualMachine extends BaseCommandPo {
	
	public static final String COMMAND = "startVirtualMachine";
	
	public static final String ID = "id";
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
	private int id = 0;
	
	public StartVirtualMachine(){
		 super(COMMAND);
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
		this.setParameter(ID, id);
	}
	
	public String getCOMMAND() {
		return COMMAND;
	}
	
	public  String getParameter (BaseCommandPo po){
		if(id == 0){
			throw new ServiceException("缺少必填参数：id");
		}
		return super.fromPoToJsonStr(po);
	}
	
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[StartVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			StartVirtualMachine po = (StartVirtualMachine)JsonUtil.getObject4JsonString(tempJson, StartVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[StartVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[StartVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
