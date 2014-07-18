package com.skycloud.management.portal.front.network.h3c.dao;

import com.skycloud.management.portal.front.network.h3c.entity.VirtualLoadBalance;

/**
 * H3C网络虚负载均衡持久化接口
 * @author jiaoyz
 */
public interface IVirtualLoadBalanceDao {

  /**
   * 创建虚负载均衡
   * @param loadBalance 虚负载均衡
   * @throws Exception
   */
  public void createLoadBalance(VirtualLoadBalance loadBalance) throws Exception;

  /**
   * 根据唯一索引获取虚负载均衡
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @return 虚负载均衡
   * @throws Exception
   */
  public VirtualLoadBalance getLoadBalanceByUniqueIndex(int virtualDeviceId, int deviceId) throws Exception;

  /**
   * 根据用户id获取虚负载均衡
   * @param userId 用户id
   * @return 虚负载均衡
   * @throws Exception
   */
  public VirtualLoadBalance getLoadBalanceByUserId(int userId) throws Exception;

  /**
   * 获取空闲虚负载均衡
   * @return 虚负载均衡
   * @throws Exception
   */
  public VirtualLoadBalance getIdleLoadBalance() throws Exception;

  /**
   * 更新虚负载均衡
   * @param loadBalance 虚负载均衡
   * @throws Exception
   */
  public void updateLoadBalance(VirtualLoadBalance loadBalance) throws Exception;

  /**
   * 删除虚负载均衡
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @throws Exception
   */
  public void deleteLoadBalance(int virtualDeviceId, int deviceId) throws Exception;
}