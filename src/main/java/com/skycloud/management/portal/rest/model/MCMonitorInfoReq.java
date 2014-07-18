/**
 * 2012-1-18  下午03:00:05  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午03:00:05
 */
@XmlRootElement
public class MCMonitorInfoReq {
  private String mcID;// 小型机ID
  private String userID; // 用户ID

  public String getMcID() {
    return mcID;
  }

  public void setMcID(String mcID) {
    this.mcID = mcID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }
}
