package com.skycloud.management.portal.front.command.res;

import java.util.List;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * 申请虚机操作命令对象
 * @author fengyk
 */
public class DeployVirtualMachine extends QueryCommand {

  public static final String COMMAND = "deployVirtualMachine";
  public static final String SERVICE_OFFERING_ID = "serviceofferingid";
  public static final String TEMPLATE_ID = "templateid";
  public static final String ZONE_ID = "zoneid";
  public static final String ACCOUNT = "account";
  public static final String DISK_OFFERING_ID = "diskofferingid";
  public static final String DISPLAY_NAME = "displayname";
  public static final String DOMAIN_ID = "domainid";
  public static final String GROUP = "group";
  public static final String HOST_ID = "hostid";
  public static final String HYPERVISOR = "hypervisor";
  public static final String KEY_PAIR = "keypair";
  public static final String NAME = "name";
  public static final String NETWORK_IDS = "networkids";
  public static final String SECURITY_GROUP_IDS = "securitygroupids";
  public static final String SECURITY_GROUP_NAMES = "securitygroupnames";
  public static final String SIZE = "size";
  public static final String USER_DATA = "userdata";

  private long serviceofferingid;
  private long templateid;
  private long zoneid;
  private String account;
  private long diskofferingid;
  private String displayname;
  private long domainid;
  private String group;
  private long hostid;
  private String hypervisor;
  private String keypair;
  private String name;
  private List<Long> networkids;
  private List<Long> securitygroupids;
  private List<String> securitygroupnames;
  private long size;
  private String userdata;

  public DeployVirtualMachine() {
    super(COMMAND);
  }

  public DeployVirtualMachine(long serviceofferingid, long templateid, long zoneid) {
    super(COMMAND);
    this.setServiceofferingid(serviceofferingid);
    this.setTemplateid(templateid);
    this.setZoneid(zoneid);
  }

  public long getServiceofferingid() {
    return serviceofferingid;
  }

  public void setServiceofferingid(long serviceofferingid) {
    this.serviceofferingid = serviceofferingid;
    this.setParameter(SERVICE_OFFERING_ID, serviceofferingid);
  }

  public long getTemplateid() {
    return templateid;
  }

  public void setTemplateid(long templateid) {
    this.templateid = templateid;
    this.setParameter(TEMPLATE_ID, templateid);
  }

  public long getZoneid() {
    return zoneid;
  }

  public void setZoneid(long zoneid) {
    this.zoneid = zoneid;
    this.setParameter(ZONE_ID, zoneid);
  }

  public String getAccount() {
    return account;
  }

  public void setAccount(String account) {
    this.account = account;
    this.setParameter(ACCOUNT, account);
  }

  public long getDiskofferingid() {
    return diskofferingid;
  }

  public void setDiskofferingid(long diskofferingid) {
    this.diskofferingid = diskofferingid;
    this.setParameter(DISK_OFFERING_ID, diskofferingid);
  }

  public String getDisplayname() {
    return displayname;
  }

  public void setDisplayname(String displayname) {
    this.displayname = displayname;
    this.setParameter(DISPLAY_NAME, displayname);
  }

  public long getDomainid() {
    return domainid;
  }

  public void setDomainid(long domainid) {
    this.domainid = domainid;
    this.setParameter(DOMAIN_ID, domainid);
  }

  public String getGroup() {
    return group;
  }

  public void setGroup(String group) {
    this.group = group;
    this.setParameter(GROUP, group);
  }

  public long getHostid() {
    return hostid;
  }

  public void setHostid(long hostid) {
    this.hostid = hostid;
    this.setParameter(HOST_ID, hostid);
  }

  public String getHypervisor() {
    return hypervisor;
  }

  public void setHypervisor(String hypervisor) {
    this.hypervisor = hypervisor;
    this.setParameter(HYPERVISOR, hypervisor);
  }

  public String getKeypair() {
    return keypair;
  }

  public void setKeypair(String keypair) {
    this.keypair = keypair;
    this.setParameter(KEY_PAIR, keypair);
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
    this.setParameter(NAME, name);
  }

  public List<Long> getNetworkids() {
    return networkids;
  }

  public void setNetworkids(List<Long> networkids) {
    this.networkids = networkids;
    this.setParameter(NETWORK_IDS, networkids);
  }

  public List<Long> getSecuritygroupids() {
    return securitygroupids;
  }

  public void setSecuritygroupids(List<Long> securitygroupids) {
    this.securitygroupids = securitygroupids;
    this.setParameter(SECURITY_GROUP_IDS, securitygroupids);
  }

  public List<String> getSecuritygroupnames() {
    return securitygroupnames;
  }

  public void setSecuritygroupnames(List<String> securitygroupnames) {
    this.securitygroupnames = securitygroupnames;
    this.setParameter(SECURITY_GROUP_NAMES, securitygroupnames);
  }

  public long getSize() {
    return size;
  }

  public void setSize(long size) {
    this.size = size;
    this.setParameter(SIZE, size);
  }

  public String getUserdata() {
    return userdata;
  }

  public void setUserdata(String userdata) {
    this.userdata = userdata;
    this.setParameter(USER_DATA, userdata);
  }
}
