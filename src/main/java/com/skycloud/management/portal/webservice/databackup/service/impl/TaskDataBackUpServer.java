package com.skycloud.management.portal.webservice.databackup.service.impl;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.webservice.databackup.service.impl.DataBackupTaskThread;

public class TaskDataBackUpServer extends BaseService implements ServletContextListener {

	public void contextDestroyed(ServletContextEvent arg0) {
		// TODO Auto-generated method stub

	}

	public void contextInitialized(ServletContextEvent arg0) {
		// TODO Auto-generated method stub
		try{
			DataBackupTaskThread backupThread = new DataBackupTaskThread ();
			backupThread.start();
		}catch(Exception e){
			logger.error("任务启动失败：",e);
		}
	}

}
