package com.skycloud.management.portal.front.network.h3c.dao;

import com.skycloud.management.portal.front.network.h3c.entity.HLJVpnInstance;

/**
 * H3C网络VPN实例持久化接口
 * @author jiaoyz
 */
public interface IHLJVpnInstanceDao {

  /**
   * 创建vpn实例
   * @param instance vpn实例
   * @throws Exception
   */
  public void createInstance(HLJVpnInstance instance) throws Exception;

  /**
   * 根据vpn实例id获取vpn实例
   * @param vpnId vpn实例id
   * @return vpn实例
   * @throws Exception
   */
  public HLJVpnInstance getInstanceByVpnId(int vpnId) throws Exception;

  /**
   * 根据用户id获取vpn实例
   * @param userId 用户id
   * @return vpn实例
   * @throws Exception
   */
  public HLJVpnInstance getInstanceByUserId(int userId) throws Exception;

  /**
   * 根据用户vlan id获取vpn实例
   * @param vlanId 用户vlan id
   * @return vpn实例
   * @throws Exception
   */
  public HLJVpnInstance getInstanceByUserVlanId(int vlanId) throws Exception;

  /**
   * 更新vpn实例
   * @param instance vpn实例
   * @throws Exception
   */
  public void updateInstance(HLJVpnInstance instance) throws Exception;

  /**
   * 删除vpn实例
   * @param vpnId vpn实例id
   * @throws Exception
   */
  public void deleteInstance(int vpnId) throws Exception;
}