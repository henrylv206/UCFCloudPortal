package com.skycloud.management.portal.front.sg.entity;

public class DeviceInfo {

  private int id;
  private String url;
  private String name;
  private String username;
  private String password;
  private DeviceType type;
  private String vldcode;
  private String model;
  private String vendor; //厂商
  private String virtualIp; //虚ip
  private int master = 1; //是否为主设备

  public int getId() {
    return id;
  }

  public String getUrl() {
    return url;
  }

  public String getUsername() {
    return username;
  }

  public String getPassword() {
    return password;
  }

  public DeviceType getType() {
    return type;
  }

  public void setId(int id) {
    this.id = id;
  }

  public void setUrl(String url) {
    this.url = url;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public void setType(DeviceType type) {
    this.type = type;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getVldcode() {
    return vldcode;
  }

  public void setVldcode(String vldcode) {
    this.vldcode = vldcode;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getVendor() {
    return vendor;
  }

  public void setVendor(String vendor) {
    this.vendor = vendor;
  }

  public String getVirtualIp() {
    return virtualIp;
  }

  public void setVirtualIp(String virtualIp) {
    this.virtualIp = virtualIp;
  }

  public int getMaster() {
    return master;
  }

  public void setMaster(int master) {
    this.master = master;
  }
}
