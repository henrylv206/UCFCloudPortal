/**
 * 2012-1-18  下午02:59:37  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午02:59:37
 */
@XmlRootElement
public class BWMonitorInfoReq {
  private String bwID;// 带宽ID
  private String userID; // 用户ID

  public String getBwID() {
    return bwID;
  }

  public void setBwID(String bwID) {
    this.bwID = bwID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

}
