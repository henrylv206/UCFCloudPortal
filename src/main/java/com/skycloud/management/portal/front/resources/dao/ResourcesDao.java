/**
 * 2012-1-30  上午10:45:42  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateMCBO;

/**
 * @author shixq
 * @version $Revision$ 上午10:45:42
 */
public interface ResourcesDao {

  /**
   * 根据用户ID查询是否存在监控服务
   * 
   * @param vo
   * @return
   * @throws SCSException
   */

  List<TemplateMCBO> queryTemplateService(ResourcesQueryVO vo) throws SCSException;
}
