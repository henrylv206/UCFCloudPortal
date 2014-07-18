package com.skycloud.management.portal.front.sg.dao;

import com.skycloud.management.portal.front.sg.entity.SGCustomService;

/**
 * 自定义服务持久化接口
 * @author jiaoyz
 */
public interface ISGCustomServiceDao {

  /**
   * 创建自定义服务
   * @param service 自定义服务
   * @return 服务ID
   * @throws Exception
   */
  public int createCustomService(SGCustomService service) throws Exception;

  /**
   * 更新自定义服务
   * @param service 自定义服务
   * @throws Exception
   */
  public void updateCustomService(SGCustomService service) throws Exception;

  /**
   * 获取自定义服务
   * @param vdId 虚设备id
   * @param deviceId 设备id
   * @param protocol 协议
   * @param srcPort 源端口
   * @param dstPort 目的端口
   * @return 自定义服务
   * @throws Exception
   */
  public SGCustomService getSGCustomService(int vdId, int deviceId, String protocol, int srcPort, int dstPort) throws Exception;

  /**
   * 删除自定义服务
   * @param id 服务id
   * @throws Exception
   */
  public void deleteCustomService(int id) throws Exception;
}
