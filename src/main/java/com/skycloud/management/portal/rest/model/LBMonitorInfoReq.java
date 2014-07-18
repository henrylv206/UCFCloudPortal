/**
 * 2012-1-18  下午02:59:57  $ID:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author shixq
 * @version $Revision$ 下午02:59:57
 */
@XmlRootElement
public class LBMonitorInfoReq {
  private String lbID;// 负载均衡ID
  private String userID; // 用户ID

  public String getLbID() {
    return lbID;
  }

  public void setLbID(String lbID) {
    this.lbID = lbID;
  }

  public String getUserID() {
    return userID;
  }

  public void setUserID(String userID) {
    this.userID = userID;
  }

}
