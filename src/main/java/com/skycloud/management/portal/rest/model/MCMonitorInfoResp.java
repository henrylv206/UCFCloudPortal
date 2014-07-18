/**
 * 2012-1-18  下午02:51:10  $Id:shixq
 */
package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;

/**
 * @author shixq
 * @version $Revision$ 下午02:51:10
 */
@XmlRootElement
public class MCMonitorInfoResp extends BaseResp {

  private String cpuused; // CPU利用率
  private String memused; // 内存利用率
  private String networkkbsread; // 网络入口带宽速度
  private String networkkbswrite; // 网络出口带宽速度
  private String rootdiskkbsread;// 系统磁盘读取速率
  private String rootdiskkbswrite; // 系统磁盘写入速率

  public String getCpuused() {
    return cpuused;
  }

  public void setCpuused(String cpuused) {
    this.cpuused = cpuused;
  }

  public String getMemused() {
    return memused;
  }

  public void setMemused(String memused) {
    this.memused = memused;
  }

  public String getNetworkkbsread() {
    return networkkbsread;
  }

  public void setNetworkkbsread(String networkkbsread) {
    this.networkkbsread = networkkbsread;
  }

  public String getNetworkkbswrite() {
    return networkkbswrite;
  }

  public void setNetworkkbswrite(String networkkbswrite) {
    this.networkkbswrite = networkkbswrite;
  }

  public String getRootdiskkbsread() {
    return rootdiskkbsread;
  }

  public void setRootdiskkbsread(String rootdiskkbsread) {
    this.rootdiskkbsread = rootdiskkbsread;
  }

  public String getRootdiskkbswrite() {
    return rootdiskkbswrite;
  }

  public void setRootdiskkbswrite(String rootdiskkbswrite) {
    this.rootdiskkbswrite = rootdiskkbswrite;
  }

}
