package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class RestoreBackup  extends BaseCommandPo {

	public static final String COMMAND = "RestoreBackup";
	
	public static final String VMBackupId = "VMBackupId";
	/**
	 * The ID of the disk volume
	 * Not Null
	 */
	private String vMBackupId;
	/**
	 * The account of the snapshot. The account parameter must be used with the domainId parameter.
	 * Null
	 */
	private String account = "";
	/**
	 * The domain ID of the snapshot. If used with the account parameter, specifies a domain for the account associated with the disk volume
	 * Null
	 */
	private long domainid = 0;
	/**
	 * policy id of the snapshot, if this is null, then use MANUAL_POLICY
	 * Null
	 */
	private long policyid = 0;
	
	
	public String getAccount() {
		return account;
	}


	public void setAccount(String account) {
		this.account = account;
	}


	public long getDomainid() {
		return domainid;
	}


	public void setDomainid(long domainid) {
		this.domainid = domainid;
	}


	public long getPolicyid() {
		return policyid;
	}


	public void setPolicyid(long policyid) {
		this.policyid = policyid;
	}


	public RestoreBackup() {
		super(COMMAND);
	}


	public String getVMBackupId() {
		return vMBackupId;
	}


	public void setVMBackupId(String vMBackupId) {
		this.vMBackupId = vMBackupId;
		this.setParameter(VMBackupId, vMBackupId);
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if("".equals(vMBackupId)){
			throw new ServiceException("缺少必填参数：volumeid");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			RestoreBackup po = (RestoreBackup)JsonUtil.getObject4JsonString(tempJson, RestoreBackup.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
