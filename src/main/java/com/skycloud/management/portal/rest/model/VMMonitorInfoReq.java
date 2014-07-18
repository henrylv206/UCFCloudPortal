/**
 * 2012-1-18  下午03:00:23  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午03:00:23
 */
@XmlRootElement
public class VMMonitorInfoReq {
  private String vmID;// 虚拟机ID
  private String userID; // 用户ID

  public String getVmID() {
    return vmID;
  }

  public void setVmID(String vmID) {
    this.vmID = vmID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

}
