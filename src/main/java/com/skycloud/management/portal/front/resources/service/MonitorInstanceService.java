/**
 * 2012-1-13  上午09:54:35  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

/**
 * @author shixq
 * @version $Revision$ 上午09:54:35
 */
public interface MonitorInstanceService {

  /**
   * 查看用户服务总数
   * 
   * @param 通过VO中operateSqlType区分查看服务信息
   * @return 服务总数
   * @throws SCSException 
   * @throws Exception
   */

  int queryResouceServiceInstanceInfoCount(ResourcesQueryVO rqvo) throws SCSException;

  /**
   * 查看用户服务列表信息
   * 
   * @param 通过VO中operateSqlType区分查看服务信息
   * @return 服务信息列表
   * @throws SCSException 
   * @throws Exception
   */
  List<ResourcesVO> queryResouceServiceInstanceInfo(ResourcesQueryVO rqvo) throws SCSException;
  List<ResourcesVO> queryResouceServiceInstanceInfoBeforApprove(ResourcesQueryVO rqvo) throws SCSException;
}
