/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.JobStateVDC;

/**
 * @author SXQ
 * @version 2012-3-15 下午03:31:19
 */
public interface AsyncJobVDCTaskService {

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
   * 根据条件查询所有任务记录
   * 
   * @param jobPO
   * @return
   * @throws SCSException
   */
  List<AsyncJobVDCPO> queryAsyncJobList(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 根据开关和任务状态查询所要执行条数
   * 
   * @author shixq
   * @create-time 2012-3-15 下午04:21:54
   * @version $Id:$
   */
  List<AsyncJobVDCPO> queryAsyncJobList4Task(JobStateVDC jobstate) throws SCSException;

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
