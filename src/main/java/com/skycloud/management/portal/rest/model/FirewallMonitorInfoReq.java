/**
 * 2012-1-18  下午02:59:45  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午02:59:45
 */
@XmlRootElement
public class FirewallMonitorInfoReq {
  private String fwID;// 防火墙ID
  private String userID; // 用户ID

  public String getFwID() {
    return fwID;
  }

  public void setFwID(String fwID) {
    this.fwID = fwID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }
}
