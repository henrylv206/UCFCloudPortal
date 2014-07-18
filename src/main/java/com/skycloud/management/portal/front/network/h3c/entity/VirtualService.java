package com.skycloud.management.portal.front.network.h3c.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络负载均衡虚服务对象
 * @author jiaoyz
 */
@XmlRootElement
public class VirtualService {

  private String vsName; //虚服务名称

  private int virtualDeviceId; //虚设备id

  private int deviceId; //所在设备id

  private String lbLayer; //负载均衡深度

  private int vpnInstanceId; //vpn实例id

  private String vpnInstanceName; //vpn实例名称

  private String vsIp; //虚服务ip

  private int subnetMask; //虚服务ip子网掩码

  private int protocolNumber; //协议类型

  private int portNumber; //端口号

  private String forwardMode; //转发模式代号

  private String forwardModeName; //转发模式名称

  private String snatEnable; //SNAT使能

  private String snatIpPoolFrom; //源地址池起始

  private String snatIpPoolTo; //源地址池结束

  private String persistenceMethod; //持续性方法

  private String persistenceMethodName; //持续性方法名称

  private int connectionLimit; //连接数限制

  private String realServiceGroupName; //实服务组名称

  private String vsEnable; //使能虚服务

  private int userId; //用户id
  
  private int state;//状态
  
  private int vsId;//虚服务id

  public String getVsName() {
    return vsName;
  }

  public void setVsName(String vsName) {
    this.vsName = vsName;
  }

  public int getVirtualDeviceId() {
    return virtualDeviceId;
  }

  public void setVirtualDeviceId(int virtualDeviceId) {
    this.virtualDeviceId = virtualDeviceId;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(int deviceId) {
    this.deviceId = deviceId;
  }

  public String getLbLayer() {
    return lbLayer;
  }

  public void setLbLayer(String lbLayer) {
    this.lbLayer = lbLayer;
  }

  public int getVpnInstanceId() {
    return vpnInstanceId;
  }

  public void setVpnInstanceId(int vpnInstanceId) {
    this.vpnInstanceId = vpnInstanceId;
  }

  public String getVpnInstanceName() {
    return vpnInstanceName;
  }

  public void setVpnInstanceName(String vpnInstanceName) {
    this.vpnInstanceName = vpnInstanceName;
  }

  public String getVsIp() {
    return vsIp;
  }

  public void setVsIp(String vsIp) {
    this.vsIp = vsIp;
  }

  public int getSubnetMask() {
    return subnetMask;
  }

  public void setSubnetMask(int subnetMask) {
    this.subnetMask = subnetMask;
  }

  public int getProtocolNumber() {
    return protocolNumber;
  }

  public void setProtocolNumber(int protocolNumber) {
    this.protocolNumber = protocolNumber;
  }

  public int getPortNumber() {
    return portNumber;
  }

  public void setPortNumber(int portNumber) {
    this.portNumber = portNumber;
  }

  public String getForwardMode() {
    return forwardMode;
  }

  public void setForwardMode(String forwardMode) {
    this.forwardMode = forwardMode;
  }

  public String getForwardModeName() {
    return forwardModeName;
  }

  public void setForwardModeName(String forwardModeName) {
    this.forwardModeName = forwardModeName;
  }

  public String getSnatEnable() {
    return snatEnable;
  }

  public void setSnatEnable(String snatEnable) {
    this.snatEnable = snatEnable;
  }

  public String getSnatIpPoolFrom() {
    return snatIpPoolFrom;
  }

  public void setSnatIpPoolFrom(String snatIpPoolFrom) {
    this.snatIpPoolFrom = snatIpPoolFrom;
  }

  public String getSnatIpPoolTo() {
    return snatIpPoolTo;
  }

  public void setSnatIpPoolTo(String snatIpPoolTo) {
    this.snatIpPoolTo = snatIpPoolTo;
  }

  public String getPersistenceMethod() {
    return persistenceMethod;
  }

  public void setPersistenceMethod(String persistenceMethod) {
    this.persistenceMethod = persistenceMethod;
  }

  public String getPersistenceMethodName() {
    return persistenceMethodName;
  }

  public void setPersistenceMethodName(String persistenceMethodName) {
    this.persistenceMethodName = persistenceMethodName;
  }

  public int getConnectionLimit() {
    return connectionLimit;
  }

  public void setConnectionLimit(int connectionLimit) {
    this.connectionLimit = connectionLimit;
  }

  public String getRealServiceGroupName() {
    return realServiceGroupName;
  }

  public void setRealServiceGroupName(String realServiceGroupName) {
    this.realServiceGroupName = realServiceGroupName;
  }

  public String getVsEnable() {
    return vsEnable;
  }

  public void setVsEnable(String vsEnable) {
    this.vsEnable = vsEnable;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

public int getState() {
	return state;
}

public void setState(int state) {
	this.state = state;
}

public int getVsId() {
	return vsId;
}

public void setVsId(int vsId) {
	this.vsId = vsId;
}
 
}