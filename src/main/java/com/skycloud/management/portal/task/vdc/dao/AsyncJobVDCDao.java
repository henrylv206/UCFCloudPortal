/**
 * 2012-3-13  下午02:14:54  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.dao;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;

/**
 * @author shixq
 * @version $Revision$ 下午02:14:54
 */
public interface AsyncJobVDCDao {
  /**
   * 插入任务表里待执行的记录
   * 
   * @param jobPO
   * @return
   */
  int insterAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改任务表里等待审批的记录
   * 
   * @param jobBO
   * @return
   */
  int updateAsyncJobAuditStateVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改任务表里任务执行的记录
   * 
   * @param jobBO
   * @return
   */
  int updateAsyncJobStateVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改任务表里任务执行成功并且带返回参数的记录
   * 
   * @param jobBO
   * @return
   */
  int updateAsyncJobPassVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改任务表里任务执行失败的记录
   * 
   * @param jobBO
   * @return
   */
  int updateAsyncJobNotPassVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 删除任务表里审批未通过的记录
   * 
   * @param jobBO
   * @return
   */
  int deleteAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 根据条件查询所有任务记录
   * 
   * @param jobPO
   * @return
   * @throws SCSException
   */
  List<AsyncJobVDCPO> queryAsyncJobList(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 对执行完的任务做数据归档，插入操作
   * 
   * @author shixq
   * @create-time 2012-3-15 下午08:08:00
   * @version $Id:$
   */
  int insertAysncJobHistory() throws SCSException;
  
  /**
   * 对执行完的任务做数据归档，删除操作
   * 
   * @author shixq
   * @create-time 2012-3-15 下午08:08:00
   * @version $Id:$
   */
  int deleteAysncJobHistory() throws SCSException;


}
