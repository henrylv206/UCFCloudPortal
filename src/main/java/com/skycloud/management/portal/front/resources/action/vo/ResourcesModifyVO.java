/**
 * 2011-11-29  上午11:21:59  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

import com.skycloud.management.portal.front.task.util.BaseCommandPo;

/**
 * @author shixq
 * @version $Revision$ 上午11:21:59
 */
public class ResourcesModifyVO implements Serializable {
	/**
   * 
   */
	private static final long serialVersionUID = -3491555576000269754L;
	private int cpu_num;// CPU个数
	private int mem_size;// 内存大小
	
	private int storage_size; //存储大小 2012.1.11 备份修改存储大小
	private String apply_reason;// 申请理由
	private int eInstanceId;
	private long clusterId;
	private int id;
	private int userID;
	private BaseCommandPo commandType;
	private int isoId;
	private String resInfo;
	private String res_code;
	private String actType;
	private String vmtemplateId;
	private String productId;
	private int diskType;
	private String account;

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getVmtemplateId() {
		return vmtemplateId;
	}

	public void setVmtemplateId(String vmtemplateId) {
		this.vmtemplateId = vmtemplateId;
	}

	public String getActType() {
		return actType;
	}

	public void setActType(String actType) {
		this.actType = actType;
	}

	public String getRes_code() {
		return res_code;
	}

	public void setRes_code(String resCode) {
		res_code = resCode;
	}

	public String getResInfo() {
		return resInfo;
	}

	public void setResInfo(String resInfo) {
		this.resInfo = resInfo;
	}

	public int getIsoId() {
		return isoId;
	}

	public void setIsoId(int isoId) {
		this.isoId = isoId;
	}

	public int getCpu_num() {
		return cpu_num;
	}

	public void setCpu_num(int cpu_num) {
		this.cpu_num = cpu_num;
	}

	public int getMem_size() {
		return mem_size;
	}

	public void setMem_size(int mem_size) {
		this.mem_size = mem_size;
	}

	public String getApply_reason() {
		return apply_reason;
	}

	public void setApply_reason(String apply_reason) {
		this.apply_reason = apply_reason;
	}

	public int geteInstanceId() {
		return eInstanceId;
	}

	public void seteInstanceId(int eInstanceId) {
		this.eInstanceId = eInstanceId;
	}

	public long getClusterId() {
		return clusterId;
	}

	public void setClusterId(long clusterId) {
		this.clusterId = clusterId;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public BaseCommandPo getCommandType() {
		return commandType;
	}

	public void setCommandType(BaseCommandPo commandType) {
		this.commandType = commandType;
	}

	public int getStorage_size() {
		return storage_size;
	}

	public void setStorage_size(int storage_size) {
		this.storage_size = storage_size;
	}

	public int getDiskType() {
		return diskType;
	}

	public void setDiskType(int diskType) {
		this.diskType = diskType;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
	
}
