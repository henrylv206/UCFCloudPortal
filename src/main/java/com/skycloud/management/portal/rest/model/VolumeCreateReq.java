package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 创建块存储请求对象
 * @author jiaoyz
 */
@XmlRootElement
public class VolumeCreateReq {

  private String name;

  private long diskofferingid;

  private long size;

  private long zoneid;

  private int poolId;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public long getDiskofferingid() {
    return diskofferingid;
  }

  public void setDiskofferingid(long diskofferingid) {
    this.diskofferingid = diskofferingid;
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
  }

  public long getZoneid() {
    return zoneid;
  }

  public void setZoneid(long zoneid) {
    this.zoneid = zoneid;
  }

  public int getPoolId() {
    return poolId;
  }

  public void setPoolId(int poolId) {
    this.poolId = poolId;
  }
}
