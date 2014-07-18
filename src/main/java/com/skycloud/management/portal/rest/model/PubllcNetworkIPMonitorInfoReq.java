/**
 * 2012-1-18  下午03:00:14  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午03:00:14
 */
@XmlRootElement
public class PubllcNetworkIPMonitorInfoReq {
  private String pnIPID;// 公网IPID
  private String userID; // 用户ID

  public String getPnIPID() {
    return pnIPID;
  }

  public void setPnIPID(String pnIPID) {
    this.pnIPID = pnIPID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

}
