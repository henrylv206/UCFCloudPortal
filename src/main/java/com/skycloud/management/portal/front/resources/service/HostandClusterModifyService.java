/**
 * 2011-11-28  下午02:57:24  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.service;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */
public interface HostandClusterModifyService {
  /**
   * 小型机开机
   * 
   * @return
   * @throws SCSException 
   */
  String insertHCStart(ResourcesModifyVO hcModifyVO) throws SCSException;

  /**
   * 小型机重启
   * 
   * @return
   * @throws SCSException 
   */
  String insertHCReboot(ResourcesModifyVO hcModifyVO) throws SCSException;

  /**
   * 小型机停止
   * 
   * @return
   * @throws SCSException 
   */
  String insertHCStop(ResourcesModifyVO hcModifyVO) throws SCSException;

  /**
   * 小型机申请修改
   * 
   * @return
   * @throws SCSException 
   */
  String insertHCApplyUpdate(ResourcesModifyVO hcModifyVO,TUserBO user) throws SCSException;

  /**
   * 小型机销毁
   * 
   * @return
   * @throws SCSException 
   */
  String insertHCAapplyDestroy(ResourcesModifyVO hcModifyVO,TUserBO user) throws SCSException;
  String insertHCDestroy(ResourcesModifyVO hcModifyVO,TUserBO user, int serviceID) throws SCSException;
}
