/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.job;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService;

/**
 * @author shixq
 * @create-time 2012-3-15 下午08:05:44
 * @version $Id:$
 */
public class AsyncJobVDCDataArchiving {
  private AsyncJobVDCTaskService taskService;

  private static Log log = LogFactory.getLog(AsyncJobVDCDataArchiving.class);

  /**
   * 数据清理任务
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:01:18
   * @version $Id:$
   */
  public void asyncJobVDCDataArchiving() {
    int indexInsert = 0;
    int indexDelete = 0;
    log.info("==========VDC JOB Data Archiving start");
    try {
      indexInsert = taskService.insertAysncJobHistory();
      indexDelete = taskService.deleteAysncJobHistory();
    } catch (SCSException e) {
      log.error(e);
    }
    log.info("==========VDC JOB Data Archiving end , insert" + indexInsert + "count, delete " + indexDelete + "count");
  }

  public AsyncJobVDCTaskService getTaskService() {
    return taskService;
  }

  public void setTaskService(AsyncJobVDCTaskService taskService) {
    this.taskService = taskService;
  }
}
