package com.skycloud.management.portal.front.network.h3c.entity;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 调用H3C API日志
 * @author jiaoyz
 */
@XmlRootElement
public class H3CApiLog {

  private int id; //日志id

  private int userId; //用户id

  private int vlanId; //VLan id

  private Date createDate; //操作日期

  private int result; //操作结果 1：成功 0：失败

  private String series; //业务应用标识

  private String operate; //操作

  private String message; //操作返回信息

  private String srcChainType; //操作前链路类型

  private String dstChainType; //操作后链路类型

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public int getVlanId() {
    return vlanId;
  }

  public void setVlanId(int vlanId) {
    this.vlanId = vlanId;
  }

  public Date getCreateDate() {
    return createDate;
  }

  public void setCreateDate(Date createDate) {
    this.createDate = createDate;
  }

  public int getResult() {
    return result;
  }

  public void setResult(int result) {
    this.result = result;
  }

  public String getSeries() {
    return series;
  }

  public void setSeries(String series) {
    this.series = series;
  }

  public String getOperate() {
    return operate;
  }

  public void setOperate(String operate) {
    this.operate = operate;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public String getSrcChainType() {
    return srcChainType;
  }

  public void setSrcChainType(String srcChainType) {
    this.srcChainType = srcChainType;
  }

  public String getDstChainType() {
    return dstChainType;
  }

  public void setDstChainType(String dstChainType) {
    this.dstChainType = dstChainType;
  }
}
