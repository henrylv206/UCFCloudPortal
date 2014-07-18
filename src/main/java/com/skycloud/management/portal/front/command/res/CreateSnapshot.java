package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;

public class CreateSnapshot  extends BaseCommandPo {

	public static final String COMMAND = "createSnapshot";
	
	public static final String VOLUME_ID = "volumeid";
	/**
	 * The ID of the disk volume
	 * Not Null
	 */
	private long volumeid = 0;
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


	public CreateSnapshot() {
		super(COMMAND);
	}


	public long getVolumeid() {
		return volumeid;
	}


	public void setVolumeid(long volumeid) {
		this.volumeid = volumeid;
		this.setParameter(VOLUME_ID, volumeid);
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(volumeid == 0){
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
			CreateSnapshot po = (CreateSnapshot)JsonUtil.getObject4JsonString(tempJson, CreateSnapshot.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateSnapshot]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
