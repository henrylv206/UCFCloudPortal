/**
 * 2012-1-31  上午11:00:24  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ 上午11:00:24
 */
public class StorageMonitorVO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -6289770501916384216L;
  private String spaceused;// 空间利用率
  private String datadiskkbsread;// 块存储读取速率
  private String datadiskkbswrite;// 块存储写入速率

  public String getSpaceused() {
    return spaceused;
  }

  public void setSpaceused(String spaceused) {
    this.spaceused = spaceused;
  }

  public String getDatadiskkbsread() {
    return datadiskkbsread;
  }

  public void setDatadiskkbsread(String datadiskkbsread) {
    this.datadiskkbsread = datadiskkbsread;
  }

  public String getDatadiskkbswrite() {
    return datadiskkbswrite;
  }

  public void setDatadiskkbswrite(String datadiskkbswrite) {
    this.datadiskkbswrite = datadiskkbswrite;
  }

}
