package com.skycloud.management.portal.front.sg.entity;

import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.common.utils.JsonUtil;

/**
 * 安全组规则对象
 * @author jiaoyz
 */
@XmlRootElement
public class SGRule {

  private int id; //系统内规则ID

  private String sgId; //安全组编码

  private String sourceIp; //源IP地址

  private int sourcePort; //源端口

  private String destinationIp; //目的IP地址

  private int destinationPort; //目的端口

  private String protocol; //协议类型

  private int access; //访问权限：1-PERMIT 0-DENY

  private int sgRulesId; //安全规则编码

  private int status; //系统内规则状态：0-处理中 1-生效 2-失败

  private int operate; //最后一次操作类型：1-创建 0-删除

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSgId() {
    return sgId;
  }

  public void setSgId(String sgId) {
    this.sgId = sgId;
  }

  public String getSourceIp() {
    return sourceIp;
  }

  public void setSourceIp(String sourceIp) {
    this.sourceIp = sourceIp;
  }

  public int getSourcePort() {
    return sourcePort;
  }

  public void setSourcePort(int sourcePort) {
    this.sourcePort = sourcePort;
  }

  public String getDestinationIp() {
    return destinationIp;
  }

  public void setDestinationIp(String destinationIp) {
    this.destinationIp = destinationIp;
  }

  public int getDestinationPort() {
    return destinationPort;
  }

  public void setDestinationPort(int destinationPort) {
    this.destinationPort = destinationPort;
  }

  public String getProtocol() {
    return protocol;
  }

  public void setProtocol(String protocol) {
    this.protocol = protocol;
  }

  public int getAccess() {
    return access;
  }

  public void setAccess(int access) {
    this.access = access;
  }

  public int getSgRulesId() {
    return sgRulesId;
  }

  public void setSgRulesId(int sgRulesId) {
    this.sgRulesId = sgRulesId;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public int getOperate() {
    return operate;
  }

  public void setOperate(int operate) {
    this.operate = operate;
  }

  public String getAddSGRulesReq() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("SGID", this.sgId);
    map.put("SIP", this.sourceIp);
    map.put("SPort", String.valueOf(this.sourcePort));
    map.put("DIP", this.destinationIp);
    map.put("DPort", String.valueOf(this.destinationPort));
    map.put("Protocol", this.protocol);
    map.put("Access", String.valueOf(this.access));
    return JsonUtil.getJsonString4JavaPOJO(map);
  }

  public String getDeleteSGRulesReq() {
    Map<String, String> map = new HashMap<String, String>();
    map.put("SGID", this.sgId);
    map.put("SGRulesID", String.valueOf(this.sgRulesId));
    return JsonUtil.getJsonString4JavaPOJO(map);
  }
}
