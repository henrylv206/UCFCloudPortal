package com.skycloud.management.portal.front.task.queue;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.skycloud.management.portal.common.utils.BaseService;


public class TaskServer extends BaseService implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try{
//			IAsyncJobService jobService = (IAsyncJobService)BeanFactoryUtil.getBean("asyncJobService");
//			List<AsyncJobInfo> result = jobService.queryAsyncJobInfos();
//			TaskQueue taskQueue = TaskQueue.getInstance();
//			taskQueue.addItem(result);
			TaskThread taskThread = new TaskThread();
			taskThread.start();
		}catch(Exception e){
			logger.error("任务启动失败：",e);
		}
	}

}
