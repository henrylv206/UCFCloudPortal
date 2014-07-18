package com.skycloud.management.portal.front.network.h3c.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络虚防火墙
 * @author jiaoyz
 */
@XmlRootElement
public class HLJVirtualFirewall {

  private int virtualDeviceId; //虚设备id

  private String virtualDeviceName; //虚设备名称

  private int untrustZoneId; //非安全域id

  private String untrustZoneName; //非安全域名称

  private int untrustZoneVlanId; //非安全域对应vlan id

  private int trustZoneId; //安全域id

  private String trustZoneName; //安全域名称

  private int trustZoneVlanId; //安全域对应vlan id

  private int deviceId; //所在设备id

  private int userId; //用户id

  public int getVirtualDeviceId() {
    return virtualDeviceId;
  }

  public void setVirtualDeviceId(int virtualDeviceId) {
    this.virtualDeviceId = virtualDeviceId;
  }

  public String getVirtualDeviceName() {
    return virtualDeviceName;
  }

  public void setVirtualDeviceName(String virtualDeviceName) {
    this.virtualDeviceName = virtualDeviceName;
  }

  public int getUntrustZoneId() {
    return untrustZoneId;
  }

  public void setUntrustZoneId(int untrustZoneId) {
    this.untrustZoneId = untrustZoneId;
  }

  public String getUntrustZoneName() {
    return untrustZoneName;
  }

  public void setUntrustZoneName(String untrustZoneName) {
    this.untrustZoneName = untrustZoneName;
  }

  public int getUntrustZoneVlanId() {
    return untrustZoneVlanId;
  }

  public void setUntrustZoneVlanId(int untrustZoneVlanId) {
    this.untrustZoneVlanId = untrustZoneVlanId;
  }

  public int getTrustZoneId() {
    return trustZoneId;
  }

  public void setTrustZoneId(int trustZoneId) {
    this.trustZoneId = trustZoneId;
  }

  public String getTrustZoneName() {
    return trustZoneName;
  }

  public void setTrustZoneName(String trustZoneName) {
    this.trustZoneName = trustZoneName;
  }

  public int getTrustZoneVlanId() {
    return trustZoneVlanId;
  }

  public void setTrustZoneVlanId(int trustZoneVlanId) {
    this.trustZoneVlanId = trustZoneVlanId;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(int deviceId) {
    this.deviceId = deviceId;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }
}