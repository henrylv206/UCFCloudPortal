package com.skycloud.management.portal.front.task.queue;

import org.apache.log4j.Logger;
import com.skycloud.management.portal.common.utils.BeanFactoryUtil;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.service.IAsyncJobService;
import com.skycloud.management.portal.front.task.service.ITaskService;

public abstract class BaseThread extends Thread {

  public static Logger logger = Logger.getLogger(BaseThread.class);

  protected static TaskQueue taskQueue = TaskQueue.getInstance();

  /**
   * 执行小机任务
   * 
   * @param jobInfo
   * @return 创建人： 刘江宁 创建时间：2011-11-24 上午09:21:01
   */
  protected int[] executeMiniComputerJobInfo(AsyncJobInfo jobInfo) {
    try {
      jobInfo = getTaskService().executeMiniComputerJobInfoGetResult(jobInfo);
      return getResultAssignIntanceInfoArray(jobInfo);
    } catch (Exception e) {
      logger.error("任务执行[BaseThread]方法executeMiniComputerJobInfo异常：", e);
      throw new ServiceException("失败[BaseThread]方法executeMiniComputerJobInfo异常：" + e.getMessage());
    } finally {
      ConstDef.ASYNCJOBINFO_MAP.remove(jobInfo.getID());// 从内存中删除该消息的任务
    }
  }

  /**
   * 执行Elaster任务
   * 
   * @param jobInfo
   * @return 创建人： 刘江宁 创建时间：2011-11-22 下午04:54:52
   */
  protected int[] executeElasterJobInfo(AsyncJobInfo jobInfo) {
    try {
      jobInfo = getTaskService().executeElasterJobInfoGetResult(jobInfo);
      return getResultAssignIntanceInfoArray(jobInfo);
    } catch (Exception e) {
      logger.error("任务执行[BaseThread]方法executeJobInfo异常：", e);
      throw new ServiceException("失败[BaseThread]方法executeJobInfo异常：" + e.getMessage());
    } finally {
      ConstDef.ASYNCJOBINFO_MAP.remove(jobInfo.getID());// 从内存中删除该消息的任务
    }
  }

  /**
   * 将运行结果赋值与IntanceInfo数组
   * 
   * @param jobInfo
   * @return 创建人： 刘江宁 创建时间：2011-11-17 上午10:33:27
   */
  private int[] getResultAssignIntanceInfoArray(AsyncJobInfo jobInfo) {
    int[] intanceInfoId = new int[2];
    intanceInfoId[0] = jobInfo.getINSTANCE_INFO_ID();
    intanceInfoId[1] = jobInfo.getRESID();
    return intanceInfoId;
  }

  private ITaskService getTaskService() {
    ITaskService taskService = (ITaskService) BeanFactoryUtil.getBean("taskService");
    return taskService;
  }

  protected IAsyncJobService getAsyncJobService() {
    IAsyncJobService asyncJobService = (IAsyncJobService) BeanFactoryUtil.getBean("asyncJobService");
    return asyncJobService;
  }
}
