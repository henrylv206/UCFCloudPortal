/**
 * 2012-1-18 下午03:04:01 $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午03:04:01
 */
@XmlRootElement
public class VolumeMonitorInfoReq {

	private String vmID;// 虚拟机ID

	private String volumeID;// 块存储ID

	private String userID; // 用户ID

	private int resourcePoolsId;

	public String getVmID() {
		return vmID;
	}

	public void setVmID(String vmID) {
		this.vmID = vmID;
	}

	public String getVolumeID() {
		return volumeID;
	}

	public void setVolumeID(String volumeID) {
		this.volumeID = volumeID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}
}
