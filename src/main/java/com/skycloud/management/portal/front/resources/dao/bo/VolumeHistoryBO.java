/**
 * 2011-11-21  下午02:44:00  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.dao.bo;

import java.io.Serializable;
import java.util.Date;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */

public class VolumeHistoryBO implements Serializable {

  
  /**
   * 创建人：   冯永凯    
   * 创建时间：2012-12-7  下午04:50:41
   */
  		
  private static final long serialVersionUID = -3083625717287856992L;
  /**
   * 
   */
  private int USER_ID;
  private String USER_NAME;
  private int E_DISK_ID;
  private Date REMOVE_DT;
  private String INSTANCE_ID;
  private String INSTANCE_NAME;
  private String SUCCESS;
  public int getUSER_ID() {
    return USER_ID;
  }
  public void setUSER_ID(int uSER_ID) {
    USER_ID = uSER_ID;
  }
  public String getUSER_NAME() {
    return USER_NAME;
  }
  public void setUSER_NAME(String uSER_NAME) {
    USER_NAME = uSER_NAME;
  }
  public int getE_DISK_ID() {
    return E_DISK_ID;
  }
  public void setE_DISK_ID(int e_DISK_ID) {
    E_DISK_ID = e_DISK_ID;
  }
  public Date getREMOVE_DT() {
    return REMOVE_DT;
  }
  public void setREMOVE_DT(Date rEMOVE_DT) {
    REMOVE_DT = rEMOVE_DT;
  }
  public String getINSTANCE_ID() {
    return INSTANCE_ID;
  }
  public void setINSTANCE_ID(String iNSTANCE_ID) {
    INSTANCE_ID = iNSTANCE_ID;
  }
  public String getINSTANCE_NAME() {
    return INSTANCE_NAME;
  }
  public void setINSTANCE_NAME(String iNSTANCE_NAME) {
    INSTANCE_NAME = iNSTANCE_NAME;
  }
  public String getSUCCESS() {
    return SUCCESS;
  }
  public void setSUCCESS(String sUCCESS) {
    SUCCESS = sUCCESS;
  }
  
}
