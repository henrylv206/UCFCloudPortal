/**
 * 2012-1-18  下午02:58:26  $Id:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;

/**
 * @author shixq
 * @version $Revision$ 下午02:58:26
 */
@XmlRootElement
public class VolumeMonitorInfoResp extends BaseResp {

  private String spaceused; // 空间利用率
  private String datadiskkbsread; // 块存储读取速率
  private String datadiskkbswrite; // 块存储写入速率

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
