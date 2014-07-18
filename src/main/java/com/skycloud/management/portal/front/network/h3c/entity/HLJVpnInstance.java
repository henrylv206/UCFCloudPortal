package com.skycloud.management.portal.front.network.h3c.entity;

import java.util.Map;

import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.front.network.h3c.commons.Constants;

/**
 * H3C网络VPN实例
 * @author jiaoyz
 */
@XmlRootElement
public class HLJVpnInstance {

  private int vpnId; //vpn实例id

  private String vpnName; //vpn实例名称

  private String[] userVlanId = null; //用户vlan id

  private String[] userGateway = null; //网关ip地址

  private int linkVlanId; //链路vlan id

  private String linkVlanViName; //链路vlan虚接口名称

  private String linkVlanViIpFw; //链路vlan在防火墙上的ip

  private String linkVlanViIpLb; //链路vlan在负载均衡上的ip

  private String linkVlanViIpSw; //链路vlan在交换机上的ip

  private Map<String, String> linkVlanViIpRouter; //链路vlan在路由器上的ip

  private int userId; //用户id

  public int getVpnId() {
    return vpnId;
  }

  public void setVpnId(int vpnId) {
    this.vpnId = vpnId;
  }

  public String getVpnName() {
    return vpnName;
  }

  public void setVpnName(String vpnName) {
    this.vpnName = vpnName;
  }

  public String[] getUserVlanId() {
    return userVlanId;
  }

  public void setUserVlanId(String[] userVlanId) {
    this.userVlanId = userVlanId;
  }

  public String[] getUserGateway() {
    return userGateway;
  }

  public void setUserGateway(String[] userGateway) {
    this.userGateway = userGateway;
  }

  public int getLinkVlanId() {
    return linkVlanId;
  }

  public void setLinkVlanId(int linkVlanId) {
    this.linkVlanId = linkVlanId;
  }

  public String getLinkVlanViName() {
    if(StringUtils.isBlank(linkVlanViName)) {
      if(linkVlanId > 0) {
        linkVlanViName = Constants.PREFIX_VLAN_INTERFACE + linkVlanId;
      }
    }
    return linkVlanViName;
  }

  public void setLinkVlanViName(String linkVlanViName) {
    this.linkVlanViName = linkVlanViName;
  }

  public String getLinkVlanViIpFw() {
    return linkVlanViIpFw;
  }

  public void setLinkVlanViIpFw(String linkVlanViIpFw) {
    this.linkVlanViIpFw = linkVlanViIpFw;
  }

  public String getLinkVlanViIpLb() {
    return linkVlanViIpLb;
  }

  public void setLinkVlanViIpLb(String linkVlanViIpLb) {
    this.linkVlanViIpLb = linkVlanViIpLb;
  }

  public String getLinkVlanViIpSw() {
    return linkVlanViIpSw;
  }

  public void setLinkVlanViIpSw(String linkVlanViIpSw) {
    this.linkVlanViIpSw = linkVlanViIpSw;
  }

  public int getUserId() {
    return userId;
  }

  public void setUserId(int userId) {
    this.userId = userId;
  }

  public Map<String, String> getLinkVlanViIpRouter() {
    return linkVlanViIpRouter;
  }

  public void setLinkVlanViIpRouter(Map<String, String> linkVlanViIpRouter) {
    this.linkVlanViIpRouter = linkVlanViIpRouter;
  }

}