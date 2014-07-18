package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


/**
 * 变更CPU操作命令对象
 * 
 * @author fengyk
 */
public class ChangeServiceForVirtualMachine extends BaseCommandPo {

	public static final String COMMAND = "changeServiceForVirtualMachine";
	
	public static final String ID = "id";
	
	public static final String SERVICE_OFFERING_ID = "serviceofferingid";
	
	/**
	 * The ID of the virtual machine 
	 * Not null
	 */
	private long id = 0;
	/**
	 * the service offering ID to apply to the virtual machine 
	 * Not null
	 */
	private long serviceofferingid = 0;

	public ChangeServiceForVirtualMachine() {
		super(COMMAND);
	}

	public ChangeServiceForVirtualMachine(long id, long serviceofferingid) {
		super(COMMAND);
		this.setId(id);
		this.setServiceofferingid(serviceofferingid);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public long getServiceofferingid() {
		return serviceofferingid;
	}

	public void setServiceofferingid(long serviceofferingid) {
		this.serviceofferingid = serviceofferingid;
		this.setParameter(SERVICE_OFFERING_ID, serviceofferingid);
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(id == 0 || serviceofferingid == 0){
			throw new ServiceException("缺少必填参数:id、serviceofferingid");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[ChangeServiceForVirtualMachine]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			ChangeServiceForVirtualMachine po = (ChangeServiceForVirtualMachine)JsonUtil.getObject4JsonString(tempJson, ChangeServiceForVirtualMachine.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[ChangeServiceForVirtualMachine]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[ChangeServiceForVirtualMachine]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
