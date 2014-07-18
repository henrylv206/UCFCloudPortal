package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

public class VolumeResumeVO implements Serializable{

	
	/**
   * 
   */
  private static final long serialVersionUID = -3574264103139652400L;

  private String snapshotId;
	
	private String name; //快照名称
	
	private int instanceId;
	//2012.3.21添加ostypeid属性
	private String osTypeId;
	
	private String vmBackupID;
	
	private String resourcePoolsId;//增加资源池id
	
	public String getResourcePoolsId() {
    return resourcePoolsId;
  }

  public void setResourcePoolsId(String resourcePoolsId) {
    this.resourcePoolsId = resourcePoolsId;
  }

  public String getOsTypeId() {
		return osTypeId;
	}

	public void setOsTypeId(String osTypeId) {
		this.osTypeId = osTypeId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public String getSnapshotId() {
		return snapshotId;
	}

	public void setSnapshotId(String snapshotId) {
		this.snapshotId = snapshotId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVmBackupID() {
		return vmBackupID;
	}

	public void setVmBackupID(String vmBackupID) {
		this.vmBackupID = vmBackupID;
	}
	
	
}
