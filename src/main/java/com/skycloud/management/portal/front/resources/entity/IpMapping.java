package com.skycloud.management.portal.front.resources.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * H3C网络应用公网ip和虚服务ip或者虚机ip映射关系对象
 * @author jiaoyz
 */
@XmlRootElement
public class IpMapping {

  private String series; //应用标识

  private int userId; //用户id

  private String chainType; //链路类型

  private String[] publicIp; //公网IP

  private String privateIp; //虚服务IP或者VMIP

  private Date intoEffect; //生效日期

  private Date loseEffect; //失效日期

  private String[] vlanId; //此链路上的vlan

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String[] getPublicIp() {
    return publicIp;
  }

  public void setPublicIp(String[] publicIp) {
    this.publicIp = publicIp;
  }

  public String getPrivateIp() {
    return privateIp;
  }

  public void setPrivateIp(String privateIp) {
    this.privateIp = privateIp;
  }

  public String getChainType() {
    return chainType;
  }

  public void setChainType(String chainType) {
    this.chainType = chainType;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Date getIntoEffect() {
    return intoEffect;
  }

  public void setIntoEffect(Date intoEffect) {
    this.intoEffect = intoEffect;
  }

  public Date getLoseEffect() {
    return loseEffect;
  }

  public void setLoseEffect(Date loseEffect) {
    this.loseEffect = loseEffect;
  }

  public String[] getVlanId() {
    return vlanId;
  }

  public void setVlanId(String[] vlanId) {
    this.vlanId = vlanId;
  }
}
