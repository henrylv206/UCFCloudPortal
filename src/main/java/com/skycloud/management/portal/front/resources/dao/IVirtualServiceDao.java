package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.VirtualService;

/**
 * H3C网络规划虚服务对象持久化接口
 * @author jiaoyz
 */
public interface IVirtualServiceDao {

  /**
   * 创建虚服务
   * @param service 虚服务
   * @throws Exception
   */
  public void createService(VirtualService service) throws Exception;

  /**
   * 更新虚服务
   * @param service 虚服务
   * @throws Exception
   */
  public void updateService(VirtualService service) throws Exception;

  /**
   * 根据名称获取虚服务
   * @param vsName 虚服务名称
   * @return 虚服务
   * @throws Exception
   */
  public VirtualService getServiceByName(String vsName) throws Exception;

  /**
   * 根据用户id获取虚服务列表
   * @param userId 用户id
   * @return 虚服务列表
   * @throws Exception
   */
  public List<VirtualService> getServiceListByUser(int userId) throws Exception;

  /**
   * 获取空闲虚服务
   * @return 虚服务
   * @throws Exception
   */
  public VirtualService getIdleService() throws Exception;

  /**
   * 根据设备id获取虚服务列表
   * @param deviceId 设备id
   * @return 虚服务列表
   * @throws Exception
   */
  public List<VirtualService> getServiceListByDevice(int deviceId) throws Exception;

  /**
   * 根据虚服务ip获取虚服务
   * @param vsIp 虚服务ip
   * @return 虚服务
   * @throws Exception
   */
  public VirtualService getServiceByIp(String vsIp) throws Exception;
}
