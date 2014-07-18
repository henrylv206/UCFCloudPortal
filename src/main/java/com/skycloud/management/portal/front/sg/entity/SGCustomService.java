package com.skycloud.management.portal.front.sg.entity;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 用户自定义服务，用于H3C
 * @author jiaoyz
 */
@XmlRootElement
public class SGCustomService {

  private int id; //系统内ID

  private String name; //服务名称

  private String protocol; //协议

  private int srcPort = 0; //源端口

  private int dstPort = 0; //目的端口

  private int deviceId; //设备id

  private int vdId; //虚设备id

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public int getSrcPort() {
    return srcPort;
  }

  public void setSrcPort(int srcPort) {
    this.srcPort = srcPort;
  }

  public int getDstPort() {
    return dstPort;
  }

  public void setDstPort(int dstPort) {
    this.dstPort = dstPort;
  }

  public int getDeviceId() {
    return deviceId;
  }

  public void setDeviceId(int deviceId) {
    this.deviceId = deviceId;
  }

  public int getVdId() {
    return vdId;
  }

  public void setVdId(int vdId) {
    this.vdId = vdId;
  }

}
