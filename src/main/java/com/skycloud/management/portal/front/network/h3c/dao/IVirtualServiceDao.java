package com.skycloud.management.portal.front.network.h3c.dao;

import java.util.List;

import com.skycloud.management.portal.front.network.h3c.entity.VirtualService;

/**
 * H3C网络负载均衡虚服务对象持久化接口
 * @author jiaoyz
 */
public interface IVirtualServiceDao {

  /**
   * 创建虚服务
   * @param service 虚服务
   * @throws Exception
   */
  public void createVirtualService(VirtualService service) throws Exception;

  /**
   * 根据唯一索引获取虚服务
   * @param vsName 虚服务名称
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @return 虚服务
   * @throws Exception
   */
  public VirtualService getServiceByUniqueIndex(String vsName, int virtualDeviceId, int deviceId) throws Exception;

  /**
   * 根据虚设备id获取虚服务列表
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @return 虚服务列表
   * @throws Exception
   */
  public List<VirtualService> getServiceByVirtualDeviceId(int virtualDeviceId, int deviceId) throws Exception;

  /**
   * 根据设备id获取虚服务列表
   * @param deviceId 设备id
   * @return 虚服务列表
   * @throws Exception
   */
  public List<VirtualService> getServiceByDeviceId(int deviceId) throws Exception;

  /**
   * 根据用户id获取虚服务列表
   * @param userId 用户id
   * @return 虚服务列表
   * @throws Exception
   */
  public List<VirtualService> getServiceByUserId(int userId) throws Exception;

  /**
   * 更新虚服务
   * @param service 虚服务
   * @throws Exception
   */
  public void updateVirtualService(VirtualService service) throws Exception;

  /**
   * 删除虚服务
   * @param vsName 虚服务名称
   * @param virtualDeviceId 虚设备id
   * @param deviceId 设备id
   * @throws Exception
   */
  public void deleteVirtualService(String vsName, int virtualDeviceId, int deviceId) throws Exception;
}