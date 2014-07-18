package com.skycloud.management.portal.front.instance.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.res.AttachVolume;
import com.skycloud.management.portal.front.command.res.DetachVolume;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Ipsan;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.instance.service.IAsyncJobService;
import com.skycloud.management.portal.front.task.queue.TaskQueue;

public class AsyncJobServiceImpl implements IAsyncJobService {

  private Logger log = Logger.getLogger(AsyncJobServiceImpl.class);

  private IAsyncJobInfoDAO asyncJobDao;
  private ISysParametersService parametersService;

  protected static TaskQueue taskQueue = TaskQueue.getInstance();

  @Override
  public List<AsyncJobInfo> queryAsyncJobInfos() throws Exception {
    List<AsyncJobInfo> asyncJobInfos = null;
    try {
      asyncJobInfos = asyncJobDao.queryPendingAsyncJobs();
    } catch (Exception e) {
      throw new SQLException("query asyncJobInfo error:" + e.getMessage());
    }
    return asyncJobInfos;
  }

  @Override
  public void updateAsyncJobInfo(AsyncJobInfo asyncJobInfo) throws Exception {
    try {
      asyncJobDao.updateAsyncJobInfo(asyncJobInfo);
    } catch (Exception e) {
      throw new SQLException("update asyncJobInfo error:" + e.getMessage());
    }
  }

  @Override
  public void queryAsyncInfoToQueue() throws Exception {

    Integer projectId = parametersService.getCurProjectId();
    if (projectId != null && projectId > -1 && projectId == ConstDef.ProjectId.SkyFormOpt.getCode()) {
      log.info("---------this projectName is : " + ConstDef.ProjectId.SkyFormOpt.getDesc() + "--------");
      List<AsyncJobInfo> aJobInfos;
      try {
        aJobInfos = this.queryAsyncJobInfos();
        log.info("query ajobInfos size:" + aJobInfos.size());

        if (ConstDef.ASYNCJOBINFO_MAP.isEmpty()) {
          this.orgDataToMapAndQueue(aJobInfos);

        } else {
          for (AsyncJobInfo aJobInfo : aJobInfos) {
            if (!ConstDef.ASYNCJOBINFO_MAP.containsKey(aJobInfo.getID())) {
              this.orgDataToMapAndQueue(aJobInfos);
            }
          }
        }
        for (Map.Entry<Integer, AsyncJobInfo> e : ConstDef.ASYNCJOBINFO_MAP.entrySet()) {
          log.info("current aysncJob Id:" + e.getKey());
        }

      } catch (Exception e) {
        log.error("queryAsyncInfoToQueue error:" + e.getMessage());
        throw new Exception(e.getMessage());
      }
    }

  }

  @Override
  public List<AsyncJobInfo> queryAsyncJobInfoByIdAndState(int intanceId, int state) throws Exception {
    List<AsyncJobInfo> asyncJobInfos = null;
    try {
      asyncJobInfos = asyncJobDao.queryAsyncJobInfoByIdAndState(intanceId, state);
    } catch (Exception e) {
      throw new SQLException("query asyncJobInfo error:" + e.getMessage());
    }
    return asyncJobInfos;
  }

  @Override
  public AsyncJobInfo queryAsyncJobByIntanceInfoId(int intanceInfoId) throws Exception {
    AsyncJobInfo asyncJobInfo = null;
    try {
      asyncJobInfo = asyncJobDao.queryAsyncJobByIntanceInfoId(intanceInfoId);
    } catch (Exception e) {
      throw new SQLException("query asyncJobInfo error:" + e.getMessage());
    }
    return asyncJobInfo;
  }

  // 组织aysncjob表中数据到内存和队列中
  private void orgDataToMapAndQueue(List<AsyncJobInfo> aJobInfos) {
    List<AsyncJobInfo> aJobs = new ArrayList<AsyncJobInfo>();
    int instance_id = 0;
    // 区分attachvolume命令与其他命令同一个业务list提交
    String operation = "";
    int index = 0;
    for (AsyncJobInfo aJobInfo : aJobInfos) {
      if (instance_id == 0) {
        instance_id = aJobInfo.getINSTANCE_INFO_ID();
        operation = aJobInfo.getOPERATION();
        aJobs.add(aJobInfo);
        ConstDef.ASYNCJOBINFO_MAP.put(aJobInfo.getID(), aJobInfo);
      } else {
        if (instance_id == aJobInfo.getINSTANCE_INFO_ID() && checkTaskIsSame(operation, aJobInfo.getOPERATION())) {
          aJobs.add(aJobInfo);
          ConstDef.ASYNCJOBINFO_MAP.put(aJobInfo.getID(), aJobInfo);
        } else {
          // 提交到队列
          taskQueue.addItem(aJobs);
          aJobs = new ArrayList<AsyncJobInfo>();
          aJobs.add(aJobInfo);
          ConstDef.ASYNCJOBINFO_MAP.put(aJobInfo.getID(), aJobInfo);
          instance_id = aJobInfo.getINSTANCE_INFO_ID();
          operation = aJobInfo.getOPERATION();
        }
      }
      if (index == aJobInfos.size() - 1) {
        if (aJobs.size() > 0) {
          taskQueue.addItem(aJobs);
          // 提交到队列
        }
      }
      index++;
    }

  }

  /**
   * 区分部分命令(attach volume)与其他命令同一个业务list提交
   * 
   * @param firstOperation
   * @param secondOperation
   * @return true:是一个任务,false:不是一个任务
   */
  private boolean checkTaskIsSame(String firstOperation, String secondOperation) {
    String[] strs = { AttachVolume.COMMAND, DetachVolume.COMMAND };
    for (String str : strs) {
      if (str.equals(firstOperation) || str.equals(secondOperation)) {
        return false;
      }
    }
    return true;

  }

  public void updateJobIdByJobInfo(AsyncJobInfo jobInfo) throws ServiceException {
    try {
      asyncJobDao.updateJobIdByJobInfo(jobInfo);
    } catch (ServiceException e) {
      log.error("更新Job任务JobId失败:", e);
      throw new ServiceException("更新Job任务JobId失败:" + e.getMessage());
    }
  }

  public void updateResIdAndStateByJobInfo(AsyncJobInfo jobInfo) throws ServiceException {
    try {
      asyncJobDao.updateResIdAndStateByJobInfo(jobInfo);
    } catch (ServiceException e) {
      log.error("更新Job任务Resid和状态失败：", e);
      throw new ServiceException("更新Job任务Resid和状态失败：" + e.getMessage());
    }
  }

  public void updateTScsIriStateById(int state, int id) throws ServiceException {
    try {
      asyncJobDao.updateTScsIriStateById(state, id);
    } catch (ServiceException e) {
      log.error("更新IRI状态失败：", e);
      throw new ServiceException("更新IRI状态失败：" + e.getMessage());
    }
  }

  public Iri queryIriPoById(int id) throws ServiceException {
    try {
      return asyncJobDao.queryIriPoById(id);
    } catch (ServiceException e) {
      log.error("查询Iri状态失败：", e);
      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
    }
  }

  public int queryIriPoByVMId(int vmid) throws Exception {
    List<Iri> iriList = null;
    try {
      iriList = asyncJobDao.queryIriPoByVMId(vmid);

    } catch (ServiceException e) {
      log.error("查询Iri状态失败：", e);
      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
    }
    return iriList.size();
  }
  
  public List<Iri> queryIriListByVMId(int vmid) throws Exception {
	    List<Iri> iriList = null;
	    try {
	      iriList = asyncJobDao.queryIriPoByVMId(vmid);
	    } catch (ServiceException e) {
	      log.error("查询Iri状态失败：", e);
	      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
	    }
	    return iriList;
	  }  
  
  public List<Iri> queryIPsanBindVM(int vmid) throws Exception {
	    List<Iri> iriList = null;
	    try {
	      iriList = asyncJobDao.queryIPsanBindVM(vmid);
	    } catch (ServiceException e) {
	      log.error("查询Iri状态失败：", e);
	      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
	    }
	    return iriList;
	  }    

  // to fix bug [4011]
  public int getBindIPsan(int instanceId) throws Exception {
    List<Ipsan> ipsanList = null;
    try {
      ipsanList = asyncJobDao.queryIpsanByVMId(instanceId);

    } catch (ServiceException e) {
      log.error("查询Iri状态失败：", e);
      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
    }
    return ipsanList.size();
  }

  public int queryIriPoByVMIding(int vmid) throws Exception {
    List<Iri> iriList = null;
    try {
      iriList = asyncJobDao.queryIriPoByVMIding(vmid);

    } catch (ServiceException e) {
      log.error("查询Iri状态失败：", e);
      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
    }
    return iriList.size();
  }

  public int queryVMforTIP(int instanceId) throws Exception {
    List<TPublicIPBO> ipList = null;
    try {
      ipList = asyncJobDao.queryIPByinstanceId(instanceId);

    } catch (ServiceException e) {
      log.error("查询Iri状态失败：", e);
      throw new ServiceException("查询Iri状态失败：" + e.getMessage());
    }
    return ipList.size();
  }

  public IAsyncJobInfoDAO getAsyncJobDao() {
    return asyncJobDao;
  }

  public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
    this.asyncJobDao = asyncJobDao;
  }

  public void setParametersService(ISysParametersService parametersService) {
    this.parametersService = parametersService;
  }

}
