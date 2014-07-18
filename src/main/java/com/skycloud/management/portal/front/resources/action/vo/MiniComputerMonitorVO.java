/**
 * 2012-2-2  下午04:38:11  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ 下午04:38:11
 */
public class MiniComputerMonitorVO implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = 5183426407900608423L;
  private String cpuused;// CPU利用率
  private String memused;// 内存利用率
  private String networkkbsread;// 网络入口带宽速度
  private String networkkbswrite;// 网络出口带宽速度
  private String rootdiskkbsread;// 系统磁盘读取速率
  private String rootdiskkbswrite;// 系统磁盘写入速率
  private String resCode;

  public String getResCode() {
    return resCode;
  }

  public void setResCode(String resCode) {
    this.resCode = resCode;
  }

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
