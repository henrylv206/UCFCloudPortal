package com.skycloud.management.portal.front.command.res;

import org.apache.commons.lang.StringUtils;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;


/**
 * 申请硬盘操作命令对象
 * 
 * @author fengyk
 */
public class CreateVolume  extends BaseCommandPo {

	public static final String COMMAND = "createVolume";
	public static final String NAME = "name";
	public static final String ACCOUNT = "account";
	public static final String DISK_OFFERING_ID = "diskofferingid";
	public static final String DOMAIN_ID = "domainid";
	public static final String SIZE = "size";
	public static final String SNAPSHOT_ID = "snapshotid";
	public static final String ZONE_ID = "zoneid";
	/**
	 * the name of the disk volume 
	 * Not Null
	 */
	private String name;
	/**
	 * the account associated with the disk volume. Must be used with the
	 * domainId parameter 
	 * Null
	 */
	private String account;
	/**
	 * the ID of the disk offering. Either diskOfferingId or snapshotId must be passed in
	 * Null
	 */
	private long diskofferingid;
	/**
	 * the domain ID associated with the disk offering. If used with the account parameter returns the disk volume associated with the account for the specified domain
	 * Null
	 */
	private long domainid;
	/**
	 * Arbitrary volume size. Mutually exclusive with diskOfferingId
	 * Null
	 */
	private long size;
	/**
	 * the snapshot ID for the disk volume. Either diskOfferingId or snapshotId must be passed in
	 * Null
	 */
	private long snapshotid;
	/**
	 * the ID of the availability zone
	 * Null
	 */
	private long zoneid;

	public CreateVolume() {
		super(COMMAND);
	}

	public CreateVolume(String name) {
		super(COMMAND);
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
		this.setParameter(NAME, name);
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
		this.setParameter(ACCOUNT, account);
	}

	public long getDiskofferingid() {
		return diskofferingid;
	}

	public void setDiskofferingid(long diskofferingid) {
		this.diskofferingid = diskofferingid;
		this.setParameter(DISK_OFFERING_ID, diskofferingid);
	}

	public long getDomainid() {
		return domainid;
	}

	public void setDomainid(long domainid) {
		this.domainid = domainid;
		this.setParameter(DOMAIN_ID, domainid);
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
		this.setParameter(SIZE, size);
	}

	public long getSnapshotid() {
		return snapshotid;
	}

	public void setSnapshotid(long snapshotid) {
		this.snapshotid = snapshotid;
		this.setParameter(SNAPSHOT_ID, snapshotid);
	}

	public long getZoneid() {
		return zoneid;
	}

	public void setZoneid(long zoneid) {
		this.zoneid = zoneid;
		this.setParameter(ZONE_ID, zoneid);
	}
	@Override
	public String getCOMMAND() {
		// TODO Auto-generated method stub
		return COMMAND;
	}

	@Override
	public String getParameter(BaseCommandPo po) {
		// TODO Auto-generated method stub
		if(StringUtils.isBlank(name)){
			throw new ServiceException("缺少必填参数：name");
		}
		return super.fromPoToJsonStr(po);
	}
	protected  QueryCommand fromJsonToOperatePo (String jsonStr){
		try{
			if(StringUtils.isBlank(jsonStr)){
				throw new ServiceException("解析Json传失败[CreateVolume]方法fromJsonToOperatePo：传入参数为空");
			}
			String tempJson = "";
			tempJson = fromJsonToMap(jsonStr).get(COMMAND).toString();
			CreateVolume po = (CreateVolume)JsonUtil.getObject4JsonString(tempJson, CreateVolume.class);
			return po;
		}catch(Exception e){
			logger.error("解析Json传失败[CreateVolume]方法fromJsonToOperatePo：",e);
			throw new RuntimeException("解析Json传失败[CreateVolume]方法fromJsonToOperatePo："+e.getMessage());
		}
	}
}
