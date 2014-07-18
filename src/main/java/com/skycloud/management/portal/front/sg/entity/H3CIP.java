package com.skycloud.management.portal.front.sg.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 公网IP在H3C中的映射关系对象
 * @author jiaoyz
 */
@XmlRootElement
public class H3CIP {

  private int id; // 公网IP ID，直接存T_SCS_PUBLIC_IP表中的ID

  private String publicIp; // H3C接口配置中的公网IP

  private String publicInterface; // H3C接口配置中公网IP对应的接口名称

  private String publicMask; // H3C接口配置中公网IP的网络掩码

  private String publicZone; // H3C接口配置中公网IP对应的安全域名称

  private int publicZoneId; // H3C接口配置中公网IP对应的安全域ID

  private String publicState; // H3C接口配置中公网IP对应的接口状态

  private String localIp; // 配对接口配置中的内网IP

  private String localInterface; // 配对接口配置中的接口名称

  private String localMask; // 配对接口配置中内网IP的网络掩码

  private String localZone; // 配对接口配置中的安全域名称

  private int localZoneId; // 配对接口配置中的安全域ID

  private String localState; // 配对接口配置的接口状态

  private int usedFor; //使用情况

  private int userId; //用户id

  private int vdId; //虚设备id

  private int deviceId; //设备id

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPublicIp() {
    return publicIp;
  }

  public void setPublicIp(String publicIp) {
    this.publicIp = publicIp;
  }

  public String getPublicInterface() {
    return publicInterface;
  }

  public void setPublicInterface(String publicInterface) {
    this.publicInterface = publicInterface;
  }

  public String getPublicMask() {
    return publicMask;
  }

  public void setPublicMask(String publicMask) {
    this.publicMask = publicMask;
  }

  public String getPublicZone() {
    return publicZone;
  }

  public void setPublicZone(String publicZone) {
    this.publicZone = publicZone;
  }

  public int getPublicZoneId() {
    return publicZoneId;
  }

  public void setPublicZoneId(int publicZoneId) {
    this.publicZoneId = publicZoneId;
  }

  public String getPublicState() {
    return publicState;
  }

  public void setPublicState(String publicState) {
    this.publicState = publicState;
  }

  public String getLocalIp() {
    return localIp;
  }

  public void setLocalIp(String localIp) {
    this.localIp = localIp;
  }

  public String getLocalInterface() {
    return localInterface;
  }

  public void setLocalInterface(String localInterface) {
    this.localInterface = localInterface;
  }

  public String getLocalMask() {
    return localMask;
  }

  public void setLocalMask(String localMask) {
    this.localMask = localMask;
  }

  public String getLocalZone() {
    return localZone;
  }

  public void setLocalZone(String localZone) {
    this.localZone = localZone;
  }

  public int getLocalZoneId() {
    return localZoneId;
  }

  public void setLocalZoneId(int localZoneId) {
    this.localZoneId = localZoneId;
  }

  public String getLocalState() {
    return localState;
  }

  public void setLocalState(String localState) {
    this.localState = localState;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getVdId() {
    return vdId;
  }

  public void setVdId(int vdId) {
    this.vdId = vdId;
  }

  public int getUsedFor() {
    return usedFor;
  }

  public void setUsedFor(int usedFor) {
    this.usedFor = usedFor;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(int deviceId) {
    this.deviceId = deviceId;
  }

}