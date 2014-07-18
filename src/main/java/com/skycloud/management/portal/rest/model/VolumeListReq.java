package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.common.Constants;

/**
 * 块存储列表请求对象
 * @author jiaoyz
 */
@XmlRootElement
public class VolumeListReq {

  private int vmId = Constants.STATUS_COMMONS.IGNORE.getValue(); //虚机ID
  private int poolId;

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
