package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


public class ActVM  extends BaseCommandPo {

	public static final String COMMAND = "actVM";
	
	public static final String VM_ID = "VMID";
	public static final String ACT_TYPE = "ActType";
	/**
	 * The ID of the disk volume
	 * Not Null
	 */
	private String vMID;
	private String actType;
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


	public ActVM() {
		super(COMMAND);
	}


	public String getVMID() {
		return vMID;
	}
	
	public String getActType() {
		return actType;
	}


	public void setVMID(String VMID) {
		this.vMID = VMID;
		this.setParameter(VM_ID, VMID);
	}
	
	public void setActType(String ACTTYPE) {
		this.actType = ACTTYPE;
		this.setParameter(ACT_TYPE, ACTTYPE);
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
			throw new ServiceException("缺少必填参数：vmid");
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
			CreateSnapshot po = (CreateSnapshot)JsonUtil.getObject4JsonString(tempJson, CreateSnapshot.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
