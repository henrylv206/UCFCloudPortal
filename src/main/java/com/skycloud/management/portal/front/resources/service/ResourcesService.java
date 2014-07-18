/**
 * 2012-1-16  下午04:16:17  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;

/**
 * @author shixq
 * @version $Revision$ 下午04:16:17
 */
public interface ResourcesService {

  /**
   * 修改用户备份服务信息
   * 
   * @param
   * @return
   * @throws Exception
   */
  String insertDirtyReadChangeInstance(ResourcesModifyVO vmModifyVO, TUserBO user) throws Exception;
  String insertMOInstance(ResourcesModifyVO vmModifyVO, TUserBO user, int serviceID) throws Exception;

  /**
   * 根据用户ID、类型查询是否存在监控服务
   * 
   * @param vo
   * @return
   * @throws SCSException
   */

  boolean queryTemplateService(ResourcesQueryVO vo) throws SCSException;
  TInstanceInfoBO searchInstanceInfoByID(int ID) throws SCSException;
  void updateInstanceInfo(TInstanceInfoBO bo)throws SCSException;
}
