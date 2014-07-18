package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 卸载块存储请求对象
 * @author jiaoyz
 */
@XmlRootElement
public class VolumeDetachReq {

  private int id;

  private int vmId;

  private int poolId;

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getVmId() {
    return vmId;
  }

  public void setVmId(int vmId) {
    this.vmId = vmId;
  }

  public int getPoolId() {
    return poolId;
  }

  public void setPoolId(int poolId) {
    this.poolId = poolId;
  }
}
