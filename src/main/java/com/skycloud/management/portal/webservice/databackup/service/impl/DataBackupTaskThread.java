package com.skycloud.management.portal.webservice.databackup.service.impl;

import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.skycloud.management.portal.common.utils.BeanFactoryUtil;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.webservice.databackup.po.JobResultQueryCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;
import com.skycloud.management.portal.webservice.databackup.service.IDBUserSnapshotService;
import com.skycloud.management.portal.webservice.databackup.service.IDataBackUpService;

public class DataBackupTaskThread extends Thread {

  @Autowired
  private IDataBackUpService dataBackUpService;
  @Autowired
  private IDBUserSnapshotService dbUserSnapshotService;

  public static Logger logger = Logger.getLogger(DataBackupTaskThread.class);

  public synchronized void run() {
    try {
      while (true) {
        try {
          logger.info("--SkyFormOpt DataBackupTaskThread TaskThread Wait 30 second----");
          Thread.sleep(30000);
        } catch (InterruptedException e) {
          logger.error("--SkyFormOpt DataBackupTaskThread TaskThread Exception:" + e.getMessage());
        }
        UserSnapshot querySnapshot = new UserSnapshot();
        querySnapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
        List<UserSnapshot> result = getSnapshotService().queryTpSnapshotList(querySnapshot);
        for (UserSnapshot snapshot : result) {
          if (checkAttributeValue(snapshot)) {
            // 经过与张慧征确认，此处方法没有action调用。
            queryCreateSnapshotResultService(snapshot, snapshot.getRESOURCE_POOLS_ID());
          }
        }
      }
    } catch (Exception e) {
      logger.error("--SkyFormOpt DataBackupTaskThread TaskThread Exception:" + e.getMessage());
    }
  }

  private void queryCreateSnapshotResultService(UserSnapshot snapshot, int resourcePoolsId) {
    try {
      JobResultQueryCommandPo jobPo = new JobResultQueryCommandPo();
      jobPo.setCreateUser(snapshot.getCREATE_USER_ID());
      jobPo.setJobId(String.valueOf(snapshot.getJOB_ID()));
      getDataBackUpService().QueryCreateSnapshotResultService(jobPo, resourcePoolsId);
    } catch (Exception e) {
      logger.error("--SkyFormOpt [DataBackupTaskThread] method : QueryCreateSnapshotResultService Exception:" + e.getMessage());
    }
  }

  private boolean checkAttributeValue(UserSnapshot snapshot) {
    if (snapshot.getCREATE_USER_ID() == 0 || snapshot.getJOB_ID() == 0)
      return false;
    return true;
  }

  private IDBUserSnapshotService getSnapshotService() {
    dbUserSnapshotService = (IDBUserSnapshotService) BeanFactoryUtil.getBean("dbUserSnapshotService");
    return dbUserSnapshotService;
  }

  private IDataBackUpService getDataBackUpService() {
    dataBackUpService = (IDataBackUpService) BeanFactoryUtil.getBean("dataBackUpService");
    return dataBackUpService;
  }
}
