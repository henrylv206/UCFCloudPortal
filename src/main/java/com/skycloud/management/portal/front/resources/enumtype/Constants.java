package com.skycloud.management.portal.front.resources.enumtype;

/**
 * 常量类
 * @author jiaoyz
 */
public class Constants {

  //链路类型
  public static enum CHAINTYPE {
    NAT, FW, LB, FW_LB
  }

  public static final String PREFIX_VLAN_INTERFACE = "Vlan-interface";

  public static final int ID_ROOT_FIREWALL = 1;
  public static final String NAME_ROOT_FIREWALL = "Root";
  public static final int ID_ROOT_LOADBALANCE = 1;
  public static final String NAME_ROOT_LOADBALANCE = "Root";

  public static final String BANDWIDTH_ACL_INDEX = "3000";
  public static final String BANDWIDTH_QOS_NAME = "bandwidth";
  public static final String BANDWIDTH_PREFIX_BEHAVIOR_NAME = "bandwidth_";
  public static final String BANDWIDTH_PREFIX_POLICY_NAME = "bandwidth_";
}
