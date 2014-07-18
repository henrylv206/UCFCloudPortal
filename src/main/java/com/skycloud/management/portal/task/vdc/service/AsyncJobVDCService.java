/**
 * 2012-3-13  下午02:27:01  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;

/**
 * @author shixq
 * @version $Revision$ 下午02:27:01
 */
public interface AsyncJobVDCService {

  /**
   * 插入任务表里待执行的记录
   * 
   * @param jobBO
   * @return
   */
  int insterAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改任务表里审批通过待执行的记录
   * 
   * @param jobBO
   * @return
   */
  int updateAsyncJobAuditStateVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 删除任务表里审批未通过的记录
   * 
   * @param jobBO
   * @return
   */
  @Deprecated
  int deleteAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException;

}
