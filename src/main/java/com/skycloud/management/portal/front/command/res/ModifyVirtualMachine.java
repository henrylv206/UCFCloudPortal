package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class ModifyVirtualMachine extends BaseCommandPo {

	public static final String COMMAND = "ModifyVM";
	
	public static final String VM_ID = "vmId";
	public static final String TempLateId = "templateId";
	
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
    private String vMID;
    private String templateId;
    /**
	 * Force stop the VM. The caller knows the VM is stopped
	 * Null
	 */
	
	public ModifyVirtualMachine(){
		 super(COMMAND);
	}
	
	public String getTemplateId() {
		return templateId;
	}

	public void setTemplateId(String templateId) {
		this.templateId = templateId;
		this.setParameter(TempLateId, templateId);
	}	
	
	public String getVMID() {
		return vMID;
	}

	public void setVMID(String VMID) {
		this.vMID = VMID;
		this.setParameter(VM_ID, VMID);
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if("".equals(vMID)){
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
			ModifyVirtualMachine po = (ModifyVirtualMachine)JsonUtil.getObject4JsonString(tempJson, ModifyVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[StopVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[StopVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
