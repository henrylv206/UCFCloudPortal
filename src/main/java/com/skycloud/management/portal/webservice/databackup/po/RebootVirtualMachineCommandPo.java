package com.skycloud.management.portal.webservice.databackup.po;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class RebootVirtualMachineCommandPo extends BaseCommandPo {

	public static final String COMMAND = "rebootVirtualMachine";
	public static final String ID = "id";
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
	private String id = "";

	public RebootVirtualMachineCommandPo(String id) {
		super(COMMAND);
		this.setId(id);
	}

	public RebootVirtualMachineCommandPo() {
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
		return super.fromPoToJsonStrSupportValueIsNull(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			RebootVirtualMachineCommandPo po = (RebootVirtualMachineCommandPo)JsonUtil.getObject4JsonString(tempJson, RebootVirtualMachineCommandPo.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[RebootVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
