/**
 * 2012-2-2  上午10:29:01  $Id:shixq
 */
package com.skycloud.management.portal.rest.model;

/**
 * @author shixq
 * @version $Revision$ 上午10:29:01
 */
public class MonitorTemplateReq {

  private String id;
  private String name;
  private boolean vm;
  private boolean mc;
  private boolean volume;
  private boolean lb;
  private boolean firewall;
  private boolean publlcnetworkip;
  private boolean bw;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isVm() {
    return vm;
  }

  public void setVm(boolean vm) {
    this.vm = vm;
  }

  public boolean isMc() {
    return mc;
  }

  public void setMc(boolean mc) {
    this.mc = mc;
  }

  public boolean isVolume() {
    return volume;
  }

  public void setVolume(boolean volume) {
    this.volume = volume;
  }

  public boolean isLb() {
    return lb;
  }

  public void setLb(boolean lb) {
    this.lb = lb;
  }

  public boolean isFirewall() {
    return firewall;
  }

  public void setFirewall(boolean firewall) {
    this.firewall = firewall;
  }

  public boolean isPubllcnetworkip() {
    return publlcnetworkip;
  }

  public void setPubllcnetworkip(boolean publlcnetworkip) {
    this.publlcnetworkip = publlcnetworkip;
  }

  public boolean isBw() {
    return bw;
  }

  public void setBw(boolean bw) {
    this.bw = bw;
  }

}
