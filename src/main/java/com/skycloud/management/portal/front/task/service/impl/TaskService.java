package com.skycloud.management.portal.front.task.service.impl;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsynJobInfoError;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.instance.service.IAsyncJobInfoErrorService;
import com.skycloud.management.portal.front.instance.service.IAsyncJobService;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.resources.dao.IVolumeHistoryDao;
import com.skycloud.management.portal.front.resources.dao.bo.VolumeHistoryBO;
import com.skycloud.management.portal.front.task.queue.CommonUtil;
import com.skycloud.management.portal.front.task.queue.MiniComputerExecuteUtil;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.front.task.service.ITaskService;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.rest.minicomputer.entity.MCAsyncJob;

public class TaskService extends BaseService implements ITaskService {

  private ICommandService commandService;

  private IAsyncJobService asyncJobService;

  private IAsyncJobInfoErrorService asyncJobInfoErrorService;

  private IInstanceService instanceService;

  private IVolumeHistoryDao volumeHistoryDao;

  @Override
  public AsyncJobInfo executeElasterJobInfoGetResult(AsyncJobInfo jobInfo) throws ServiceException {
    // flat getParameter()
    Object response = new Object();
    try {
      if (jobInfo.getJOBID() == isNewJob) {
        // 1.3功能，支持多资源池
        int id = jobInfo.getINSTANCE_INFO_ID();
        // 根据资源实例id(例如虚机id)查询其所属的资源池id
        int resourcePoolId = 0;//
        String operation = jobInfo.getOPERATION();

        if (StringUtils.isNotBlank(operation) && operation.equals(TaskContext.OperateType.ATTACHVOLUME.getDesc())
            || operation.equals(TaskContext.OperateType.DETACHVOLUME.getDesc())) {
          resourcePoolId = instanceService.queryResourcePoolIDByIRIID(id);
        } else {
          resourcePoolId = instanceService.getResourcePoolIdByInstanceId(id);
        }
       
        response = commandService.executeAndJsonReturn(CommandCreateUtil.buildOperatePo(jobInfo), resourcePoolId);
        String responseStr = CommonUtil.getRootResponseStr(response, jobInfo.getOPERATION());
        if(responseStr.lastIndexOf("errorcode") > 0){
          jobInfo.setJOBID(0);
          jobInfo.setRESID(-1);
          jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
          saveJobErrorInfo(responseStr, jobInfo.getID());
          logger.error("command error ：" + responseStr);
          asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
          return jobInfo;
        }
        //数据盘恢复是同时删除原有数据盘
        if(operation.equals(TaskContext.OperateType.DELETEVOLUME.getDesc())){
             if (JSONObject.fromObject(responseStr).getString("success").equals("true")){
               jobInfo.setJOBID(0);
               jobInfo.setRESID(0);
               jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
             }
           asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
           return jobInfo;
        }else{
          String jobId = CommonUtil.getJobIdFromExecuteResponse(response, jobInfo.getOPERATION());
          logger.debug("Get JobId：" + jobId);
          jobInfo.setJOBID(Integer.valueOf(jobId));
          asyncJobService.updateJobIdByJobInfo(jobInfo);
        }
      }
      formElasterJobIdToResId(jobInfo);// 查询任务完成状态，更新ResId
    } catch (Exception e) {
      logger.error("Execute[TaskService]Method：getExecuteElasterJobInfoResult Error：", e);
      throw new ServiceException("Execute[TaskService]Method：getExecuteElasterJobInfoResult Error：", e);
      // jobInfo.setRESID(-1);
      // jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
      // asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
    }
    return jobInfo;
  }

  @Override
  public AsyncJobInfo executeMiniComputerJobInfoGetResult(AsyncJobInfo jobInfo) throws ServiceException {
    // TODO Auto-generated method stub
    try {
      if (jobInfo.getJOBID() == isNewJob) {
        String resultJosn = MiniComputerExecuteUtil.callingMiniComputerPostInterfaceGetResult(jobInfo);
        logger.debug("小机接口返回JSON串：" + resultJosn.toString());
        String resultJobId = CommonUtil.fromJsonToKeyValue(resultJosn.toString(), "jobId");
        if (StringUtils.isBlank(resultJobId)) {
          logger.error("Get JobId Exception:JobId is Null");
          throw new ServiceException("Get JobId Exception:JobId is Null");
        }
        logger.debug("Get JobId is：" + resultJobId);
        jobInfo.setJOBID(Integer.valueOf(resultJobId));
        asyncJobService.updateJobIdByJobInfo(jobInfo);
      }
      getJobResult(jobInfo);

    } catch (ServiceException e) {
      logger.error("Execute[TaskService]Method getExecuteMiniComputerJobInfoResult Exception：", e);
      throw new ServiceException("Execute[TaskService]Method：getExecuteElasterJobInfoResult Error：", e);
      // jobInfo.setRESID(-1);
      // jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
      // asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
    }
    return jobInfo;
  }

  /**
   * 查询小机任务执行状态
   * 
   * @param jobInfo
   * @return 创建人： 刘江宁 创建时间：2011-11-11 下午04:10:42
   */
  private MCAsyncJob getJobResult(AsyncJobInfo jobInfo) {
    try {

      MCAsyncJob mcasynJob = MiniComputerExecuteUtil.callingMiniComputerGetIntanceGetResult(jobInfo);
      if (mcasynJob.getStatus() == TaskContext.STATUS_ASYNCJOB.SUCCESS.getCode()) {
        jobInfo.setRESID(Integer.valueOf(mcasynJob.getResult()));
        jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
        asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
        updateIntanceOrIriState(jobInfo);
      } else if (mcasynJob.getStatus() == TaskContext.STATUS_ASYNCJOB.FAILURE.getCode()) {
        logger.error("Query Job get fail of result. The Job is end.");
        jobInfo.setRESID(-1);
        jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
        asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
        saveJobErrorInfo(mcasynJob, jobInfo.getID());
        updateIntanceOrIriState(jobInfo);
      } else {
        logger.info("Job is runing....please wait moment." + jobInfo.getID());
      }
      return mcasynJob;
    } catch (Exception e) {
      logger.error("Execute[TaskService]Method getJobResult Exception", e);
      throw new ServiceException("Execute[TaskService]Method getJobResult Exception" + e.getMessage());
    }
  }

  /**
   * 查询任务完成状态，如若完成同时将RESID写入表中
   * 
   * @param jobId
   * @param commandService
   *          创建人： 刘江宁 创建时间：2011-11-7 上午09:36:36
   */
  private void formElasterJobIdToResId(AsyncJobInfo jobInfo) {
    Object response = new Object();
    try {
      String resId = "";
      String jobstatus = "";
      // 1.3功能，支持多资源池
      int id = jobInfo.getINSTANCE_INFO_ID();
      // 根据资源实例id(例如虚机id)查询其所属的资源池id
      int resourcePoolId = 0;

      String operation = jobInfo.getOPERATION();
      if (StringUtils.isNotBlank(operation) && operation.equals(TaskContext.OperateType.ATTACHVOLUME.getDesc())
          || operation.equals(TaskContext.OperateType.DETACHVOLUME.getDesc())) {
        resourcePoolId = instanceService.queryResourcePoolIDByIRIID(id);
      } else {
        resourcePoolId = instanceService.getResourcePoolIdByInstanceId(id);
      }

      response = commandService.executeAndJsonReturn(CommandCreateUtil.createQueryAsyncJobResultPo(String.valueOf(jobInfo.getJOBID())),
          resourcePoolId);
      jobstatus = CommonUtil.getResponseResultStatus(response);
      if (jobstatus.equals(TaskContext.ResultStatus.OPERATSUCCESS.getCode())) {// 成功
        if (jobInfo.getOPERATION().equals(TaskContext.OperateType.ATTACHISO.getDesc())) {
          resId = CommonUtil.getIsoResIdFromExecuteResponse(response.toString(), getJsonResultName(jobInfo.getOPERATION()));
        } else {
          resId = CommonUtil.getResIdFromExecuteResponse(response.toString(), getJsonResultName(jobInfo.getOPERATION()));
        }
        jobInfo.setRESID(Integer.valueOf(resId));
        jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());

        if (jobInfo.getOPERATION().equals(TaskContext.OperateType.CREATEVOLUME.getDesc())) {
          VolumeHistoryBO volumeHistory = new VolumeHistoryBO();
          volumeHistory.setSUCCESS("Y");
          volumeHistory.setINSTANCE_ID(String.valueOf(jobInfo.getINSTANCE_INFO_ID()));
          volumeHistoryDao.updateVolumeHistoryState(volumeHistory);
        }
        asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
        updateIntanceOrIriState(jobInfo);
      } else if (jobstatus.equals(TaskContext.ResultStatus.OPERATING.getCode())) {// 处理中
        logger.info("jobId : " + jobInfo.getJOBID() + "-Job is runing....please wait moment.," + jobInfo.getID());
      } else {// 失败
        VolumeHistoryBO volumeHistory = new VolumeHistoryBO();
        volumeHistory.setINSTANCE_ID(String.valueOf(jobInfo.getINSTANCE_INFO_ID()));
        volumeHistoryDao.deleteVolumeHistory(volumeHistory);

        logger.error("jobId : " + jobInfo.getID() + "-Query Job get fail of result. The Job is end.");
        jobInfo.setRESID(-1);
        jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
        asyncJobService.updateResIdAndStateByJobInfo(jobInfo);
        saveJobErrorInfo(response, jobInfo.getID());
        updateIntanceOrIriState(jobInfo);
      }
    } catch (Exception e) {
      saveJobErrorInfo(response, jobInfo.getID());
      logger.error("Execute [TaskService]Method formElasterJobIdToResId Exception：", e);
      throw new ServiceException("Execute [TaskService]Method formElasterJobIdToResId Exception：" + e.getMessage());
    }
  }

  /**
   * 将错误信息保存到错误日志表中
   * 
   * @param response
   * @param id
   *          创建人： 刘江宁 创建时间：2011-11-24 上午11:12:20
   */
  private void saveJobErrorInfo(Object response, int id) throws ServiceException {
    JSONObject jsonRes = JSONObject.fromObject(response);
    String json = jsonRes.toString();
    AsynJobInfoError asyError = new AsynJobInfoError();
    asyError.setASY_ID(id);
    asyError.setDESCRIPTION(json);
    asyncJobInfoErrorService.insertJobInfoError(asyError);
  }

  /**
   * 根据操作指令不同分别更新T_SCS_IRI表或者更新IntanceInfo表记录状态
   * 
   * @param jobInfo
   * @throws ServiceException
   *           创建人： 刘江宁 创建时间：2011-11-28 下午01:51:10
   */
  private void updateIntanceOrIriState(AsyncJobInfo jobInfo) throws ServiceException {

    int state = TaskContext.JobStatus.END.getCode();

    if (!judgeJobIsVolumeOperate(jobInfo.getOPERATION())) {
      if (isVmStopOperate(jobInfo.getOPERATION()) || isMcStopOperate(jobInfo.getOPERATION())) {
        state = TaskContext.JobStatus.TURNOFF.getCode();
      } else if (jobInfo.getOPERATION().equals(TaskContext.OperateType.STARTVIRTUALMACHINE.getDesc()) && !isSuccess(jobInfo.getRESID())) {
        state = TaskContext.JobStatus.TURNOFF.getCode();
      } else if (this.isDeleteVmOperate(jobInfo.getOPERATION())) {
        // this.isDeleteSnapshotOperate(jobInfo.getOPERATION())||
        state = TaskContext.JobStatus.CALLBACK.getCode();
      }
      instanceService.updateTScsIntanceInfoStateById(jobInfo.getINSTANCE_INFO_ID(), state);
    } else {
      if (isAttachVolume(jobInfo.getOPERATION()) && isSuccess(jobInfo.getRESID())) {
        logger.info("AttachVolume of operate success.update tablename: IRI");
        state = TaskContext.VolumeStatus.ATTACHSUCCESS.getCode();
        executeEndUpdateIriRecordState(jobInfo.getINSTANCE_INFO_ID(), state);
        return;
      } else if (isAttachVolume(jobInfo.getOPERATION()) && !isSuccess(jobInfo.getRESID())) {
        logger.info("AttachVolume of operate fail.update tablename: IRI");
        state = TaskContext.VolumeStatus.ATTACHFAIL.getCode();
        executeEndUpdateIriRecordState(jobInfo.getINSTANCE_INFO_ID(), state);
        return;
      } else if (isDetachVolume(jobInfo.getOPERATION()) && isSuccess(jobInfo.getRESID())) {
        logger.info("DetachVolume of operate success.update tablename: IRI");
        state = TaskContext.VolumeStatus.DETACHSUCCESS.getCode();
        executeEndUpdateIriRecordState(jobInfo.getINSTANCE_INFO_ID(), state);
        return;
      } else if (isDetachVolume(jobInfo.getOPERATION()) && !isSuccess(jobInfo.getRESID())) {
        logger.info("DetachVolume of operate fail.update tablename: Insance_Info");
        state = TaskContext.VolumeStatus.DETACHFAIL.getCode();
        executeEndUpdateIriRecordState(jobInfo.getINSTANCE_INFO_ID(), state);
        return;
      } else if (isCreateVolume(jobInfo.getOPERATION()) && isSuccess(jobInfo.getRESID())) {
        logger.info("CreateVolume of operate success.update tablename: Insance_Info");
        state = TaskContext.JobStatus.END.getCode();
        instanceService.updateTScsIntanceInfoStateResIdById(jobInfo.getINSTANCE_INFO_ID(), state, jobInfo.getRESID());
        return;
      } else if (isCreateVolume(jobInfo.getOPERATION()) && !isSuccess(jobInfo.getRESID())) {
        logger.info("CreateVolume of operate fail.update tablename: Insance_Info");
        state = TaskContext.JobStatus.END.getCode();
        instanceService.updateTScsIntanceInfoStateById(jobInfo.getINSTANCE_INFO_ID(), state);
        return;
      }
    }
  }

  /**
   * 判断操作类型是否是Volume操作，是返回true更新Iri表记录状态，否返回false更新IntanceInfo表记录状态
   * 
   * @param operate
   * @return 创建人： 刘江宁 创建时间：2011-12-2 下午03:31:51
   */
  private boolean judgeJobIsVolumeOperate(String operate) {
    String temp = operate.toLowerCase();
    if (temp.lastIndexOf("volume") > 0) {
      return true;
    }
    return false;
  }

  /**
   * 运行结束后更新IRI表记录状态
   * 
   * @param iriId
   * @param state
   *          创建人： 刘江宁 创建时间：2011-12-13 下午02:00:10
   */
  private void executeEndUpdateIriRecordState(int iriId, int state) {
    Iri iri = asyncJobService.queryIriPoById(iriId);
    if (iri != null) {
      asyncJobService.updateTScsIriStateById(state, iri.getID());
      instanceService.updateTScsIntanceInfoStateById(iri.getDISK_INSTANCE_INFO_ID(), TaskContext.JobStatus.END.getCode());
    } else {
      logger.error("executeEndUpdateIriRecordState Exception，Not find record");
      throw new ServiceException("executeEndUpdateIriRecordState Exception，Not find record");
    }
  }

  public ICommandService getCommandService() {
    return commandService;
  }

  public void setCommandService(ICommandService commandService) {
    this.commandService = commandService;
  }

  public IAsyncJobService getAsyncJobService() {
    return asyncJobService;
  }

  public void setAsyncJobService(IAsyncJobService asyncJobService) {
    this.asyncJobService = asyncJobService;
  }

  public IAsyncJobInfoErrorService getAsyncJobInfoErrorService() {
    return asyncJobInfoErrorService;
  }

  public void setAsyncJobInfoErrorService(IAsyncJobInfoErrorService asyncJobInfoErrorService) {
    this.asyncJobInfoErrorService = asyncJobInfoErrorService;
  }

  public IInstanceService getInstanceService() {
    return instanceService;
  }

  public void setInstanceService(IInstanceService instanceService) {
    this.instanceService = instanceService;
  }

  public IVolumeHistoryDao getVolumeHistoryDao() {
    return volumeHistoryDao;
  }

  public void setVolumeHistoryDao(IVolumeHistoryDao volumeHistoryDao) {
    this.volumeHistoryDao = volumeHistoryDao;
  }

}
