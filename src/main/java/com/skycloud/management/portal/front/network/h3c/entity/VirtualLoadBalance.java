package com.skycloud.management.portal.front.network.h3c.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络虚负载均衡
 * @author jiaoyz
 */
@XmlRootElement
public class VirtualLoadBalance {

  private int virtualDeviceId; //虚设备id

  private String virtualDeviceName; //虚设备名称

  private int sessionLimit; //最大会话数

  private int realServiceGroupLimit; //最大实服务组数

  private int realServiceLimit; //最大实服务数

  private int virtualServiceLimit; //最大虚服务数

  private String networkNumber; //虚服务ip网络号

  private int subnetMask; //虚服务ip子网掩码

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

  public int getSessionLimit() {
    return sessionLimit;
  }

  public void setSessionLimit(int sessionLimit) {
    this.sessionLimit = sessionLimit;
  }

  public int getRealServiceGroupLimit() {
    return realServiceGroupLimit;
  }

  public void setRealServiceGroupLimit(int realServiceGroupLimit) {
    this.realServiceGroupLimit = realServiceGroupLimit;
  }

  public int getRealServiceLimit() {
    return realServiceLimit;
  }

  public void setRealServiceLimit(int realServiceLimit) {
    this.realServiceLimit = realServiceLimit;
  }

  public int getVirtualServiceLimit() {
    return virtualServiceLimit;
  }

  public void setVirtualServiceLimit(int virtualServiceLimit) {
    this.virtualServiceLimit = virtualServiceLimit;
  }

  public String getNetworkNumber() {
    return networkNumber;
  }

  public void setNetworkNumber(String networkNumber) {
    this.networkNumber = networkNumber;
  }

  public int getSubnetMask() {
    return subnetMask;
  }

  public void setSubnetMask(int subnetMask) {
    this.subnetMask = subnetMask;
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