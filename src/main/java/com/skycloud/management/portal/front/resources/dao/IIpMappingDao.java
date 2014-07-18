package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.IpMapping;

/**
 * H3C网络应用公网ip和虚服务ip或者虚机ip映射关系对象持久化接口
 * @author jiaoyz
 */
public interface IIpMappingDao {

  /**
   * 创建映射
   * @param mapping 映射对象
   * @throws Exception
   */
  public void createIpMapping(IpMapping mapping) throws Exception;

  /**
   * 更新映射
   * @param mapping 映射对象
   * @throws Exception
   */
  public void updateIpMapping(IpMapping mapping) throws Exception;

  /**
   * 删除映射
   * @param series 应用系列标识
   * @throws Exception
   */
  public void deleteIpMapping(String series) throws Exception;

  /**
   * 根据公网ip获取对应私网ip
   * @param publicIp 公网ip
   * @return 虚服务ip或者虚机ip
   * @throws Exception
   */
  public String getPrivateIpByPublicIp(String publicIp) throws Exception;

  /**
   * 根据私网ip获取对应公网ip
   * @param privateIp 虚服务ip或者虚机ip
   * @return 公网ip
   * @throws Exception
   */
  public String[] getPublicIpByPrivateIp(String privateIp) throws Exception;

  /**
   * 根据用户id获取映射列表
   * @param userId 用户id
   * @return 映射列表
   * @throws Exception
   */
  public List<IpMapping> getMappingByUser(int userId) throws Exception;

  /**
   * 根据series获取映射对象
   * @param series 应用系列标识
   * @return 映射对象
   * @throws Exception
   */
  public IpMapping getMappingBySeries(String series) throws Exception;
  
  public void removeUnusedPublicIp(String publicIp) throws Exception;

public void updateIpMapping2(IpMapping ipMapping);
  
  
}
