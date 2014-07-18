/**
 * 2011-12-22  下午01:12:05  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ 下午01:12:05
 */
public class VirtualMachineMonitorVO implements Serializable {
  /**
   * 
   */
  private static final long serialVersionUID = -1838061040400436173L;
  private String cpunumber;
  private String cpuused;// CPU利用率
  private String memused;// 内存利用率
  private String networkkbsread;// 网络入口带宽速度
  private String networkkbswrite;// 网络出口带宽速度
  private String rootdiskkbsread;// 系统磁盘读取速率
  private String rootdiskkbswrite;// 系统磁盘写入速率

  private String memoryInternalFree;

  private String hypervisor;

  public String getCpunumber() {
    return cpunumber;
  }

  public void setCpunumber(String cpunumber) {
    this.cpunumber = cpunumber;
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

  public String getMemoryInternalFree() {
    return memoryInternalFree;
  }

  public void setMemoryInternalFree(String memoryInternalFree) {
    this.memoryInternalFree = memoryInternalFree;
  }

  public String getHypervisor() {
    return hypervisor;
  }

  public void setHypervisor(String hypervisor) {
    this.hypervisor = hypervisor;
  }

}
