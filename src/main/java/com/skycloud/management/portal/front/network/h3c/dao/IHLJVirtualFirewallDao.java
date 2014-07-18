package com.skycloud.management.portal.front.network.h3c.dao;

import com.skycloud.management.portal.front.network.h3c.entity.HLJVirtualFirewall;

/**
 * H3C网络虚防火墙持久化接口
 * @author jiaoyz
 */
public interface IHLJVirtualFirewallDao {

  /**
   * 创建虚防火墙
   * @param firewall 虚防火墙
   * @throws Exception
   */
  public void createFirewall(HLJVirtualFirewall firewall) throws Exception;

  /**
   * 根据唯一索引获取虚防火墙
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @return 虚防火墙
   * @throws Exception
   */
  public HLJVirtualFirewall getFirewallByUniqueIndex(int virtualDeviceId, int deviceId) throws Exception;

  /**
   * 根据用户id获取虚防火墙
   * @param userId 用户id
   * @return 虚防火墙
   * @throws Exception
   */
  public HLJVirtualFirewall getFirewallByUserId(int userId) throws Exception;

  /**
   * 获取空闲虚防火墙
   * @return 虚防火墙
   * @throws Exception
   */
  public HLJVirtualFirewall getIdleFirewall() throws Exception;

  /**
   * 更新虚防火墙
   * @param firewall 虚防火墙
   * @throws Exception
   */
  public void updateFirewall(HLJVirtualFirewall firewall) throws Exception;

  /**
   * 删除虚防火墙
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @throws Exception
   */
  public void deleteFirewall(int virtualDeviceId, int deviceId) throws Exception;

  /**
   * 根据安全域vlan id获取虚防火墙
   * @param vlanId 安全域vlan id
   * @return 虚防火墙
   * @throws Exception
   */
  public HLJVirtualFirewall getFirewallByTrustZoneVlanId(int vlanId) throws Exception;
}