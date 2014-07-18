package com.skycloud.management.portal.front.network.h3c.service;

/**
 * H3C带宽service
 * @author jiaoyz
 *   acl和qos预先建好并绑定，qos名称与公网ip相关，_i的配置为匹配公网ip，_o的动态配置为匹配私网ip
 */
public interface IBandwidthService {

  /**
   * 带宽设定
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @param cir 带宽限速值 单位为kbps
   * @throws Exception
   */
  public void bandwidthSetting(int userId, String publicIp, long cir) throws Exception;

  /**
   * 带宽变更
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @param cir 带宽限速值 单位为kbps
   * @throws Exception
   */
  public void bandwidthUpdate(int userId, String publicIp, long cir) throws Exception;

  /**
   * 带宽删除
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void bandwidthDelete(int userId, String publicIp) throws Exception;

  /**
   * 删除私网ip在带宽应用acl中和公网ip的关联
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void deleteRuleFromAcl(int userId, String publicIp) throws Exception;

  /**
   * 设置私网ip在带宽应用acl中和公网ip的关联
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void addRuleToAcl(int userId, String publicIp) throws Exception;

  /**
   * 带宽设定（用户粒度多IP共享）
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @param cir 带宽限速值 单位为kbps
   * @throws Exception
   */
  public void bandwidthSettingForShare(int userId, long cir) throws Exception;

  /**
   * 带宽变更（用户粒度多IP共享）
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @param cir 带宽限速值 单位为kbps
   * @throws Exception
   */
  public void bandwidthUpdateForShare(int userId, long cir) throws Exception;

  /**
   * 带宽删除（用户粒度多IP共享）
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void bandwidthDeleteForShare(int userId) throws Exception;

  /**
   * 删除私网ip在带宽应用acl中和公网ip的关联（用户粒度多IP共享）
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void deleteRuleFromAclForShare(int userId, String publicIp) throws Exception;

  /**
   * 设置私网ip在带宽应用acl中和公网ip的关联（用户粒度多IP共享）
   * @param userId 用户id
   * @param publicIp 带宽限制ip
   * @throws Exception
   */
  public void addRuleToAclForShare(int userId, String publicIp) throws Exception;
}
