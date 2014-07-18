package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

/**
 * 挂接硬盘操作命令对象
 * 
 * @author fengyk
 */
public class AttachVolume extends BaseCommandPo {

	public static final String COMMAND = "attachVolume";
	public static final String ID = "id";
	public static final String VIRTUAL_MACHINE_ID = "virtualmachineid";
	public static final String DEVICE_ID = "deviceid";
	/**
	 * the ID of the disk volume
	 * Not Null
	 */
	private long id = 0;
	/**
	 * the ID of the virtual machine
	 * Not Null
	 */
	private long virtualmachineid = 0;
	/**
	 * the ID of the device to map the volume to within the guest OS.
	 * Null
	 */
	private long deviceid;

	public AttachVolume() {
		super(COMMAND);
	}

	public AttachVolume(long id, long virtualmachineid) {
		super(COMMAND);
		this.setId(id);
		this.setVirtualmachineid(virtualmachineid);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
		this.setParameter(ID, id);
	}

	public long getVirtualmachineid() {
		return virtualmachineid;
	}

	public void setVirtualmachineid(long virtualmachineid) {
		this.virtualmachineid = virtualmachineid;
		this.setParameter(VIRTUAL_MACHINE_ID, virtualmachineid);
	}

	public long getDeviceid() {
		return deviceid;
	}

	public void setDeviceid(long deviceid) {
		this.deviceid = deviceid;
		this.setParameter(DEVICE_ID, deviceid);
	}
	
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	
	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(id == 0 || virtualmachineid == 0){
			throw new ServiceException("缺少必填参数: id、virtualmachineid为必填");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new RuntimeException("解析Json传失败[AttachVolume]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			AttachVolume po = (AttachVolume)JsonUtil.getObject4JsonString(tempJson, AttachVolume.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[AttachVolume]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[AttachVolume]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
