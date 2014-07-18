package com.skycloud.management.portal.front.network.h3c.dao;

import com.skycloud.management.portal.front.network.h3c.entity.H3CApiLog;

/**
 * 调用H3C API日志持久化接口
 * @author jiaoyz
 */
public interface IH3CApiLogDao {

  /**
   * 创建日志
   * @param log 日志信息
   * @return 日志id
   * @throws Exception
   */
  public int createApiLog(H3CApiLog log) throws Exception;
}
