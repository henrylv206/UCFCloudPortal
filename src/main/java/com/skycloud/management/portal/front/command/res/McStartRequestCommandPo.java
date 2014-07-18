package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class McStartRequestCommandPo extends BaseCommandPo {

	private static final String COMMAND = "mcstartReq" ;
	
	public McStartRequestCommandPo (){
		super (COMMAND);
	}
	/**
	 * The ID of the virtual machine
	 * Not Null
	 */
    private int vmId = 0 ;
    /**
	 * The ID of the Operator
	 * Not Null
	 */
	private int userId = 0;
	
	public int getVmId() {
		return vmId;
	}

	public void setVmId(int vmId) {
		this.vmId = vmId;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(vmId == 0 || userId == 0){
			throw new ServiceException("必填参数为空：vmId、userId");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		throw new ServiceException("McResumeRequetCommandPo方法fromJsonToOperatePo不可用");
	}
}
