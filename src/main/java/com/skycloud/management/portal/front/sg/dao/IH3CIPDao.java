package com.skycloud.management.portal.front.sg.dao;

import java.util.List;

import com.skycloud.management.portal.front.sg.entity.H3CIP;

/**
 * 公网IP在H3C中的映射关系对象持久化接口
 * @author jiaoyz
 */
public interface IH3CIPDao {

  /**
   * 在H3C防火墙上创建IP与接口的映射关系，创建成功后将信息持久化到数据库
   * @param id 该IP在T_SCS_PUBLIC_IP表中的ID
   * @param ip 公网IP
   * @throws Exception
   */
  public void createH3CIP(H3CIP ip) throws Exception;

  /**
   * 根据IP获取该IP在H3C防火墙上与接口的映射关系对象
   * @param ip 公网IP
   * @return 映射关系对象
   * @throws Exception
   */
  public H3CIP getH3CIPByIp(String ip) throws Exception;

  /**
   * 获取用户可以用于设置域间策略的ip
   * @param userId 用户id
   * @return 对象列表
   * @throws Exception
   */
  public List<H3CIP> getPublicIpByUser(int userId) throws Exception;

  /**
   * 更新ip用途
   * @param ip ip对象
   * @throws Exception
   */
  public void updateH3CIP(H3CIP ip) throws Exception;

  /**
   * 删除在H3C防火墙上该IP与接口的映射关系
   * @param ip 公网IP
   * @throws Exception
   */
  public void removeH3CIP(String ip) throws Exception;

  /**
   * 根据私网ip获取映射关系对象
   * @param privateIp 私网ip
   * @return 映射关系对象列表
   * @throws Exception
   */
  public List<H3CIP> getH3CIPByPrivateIp(String privateIp) throws Exception;
}
