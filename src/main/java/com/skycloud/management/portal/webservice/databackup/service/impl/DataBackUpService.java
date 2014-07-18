package com.skycloud.management.portal.webservice.databackup.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.ws.rs.core.Response;
import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.FormatUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.command.res.CreateSnapshot;
import com.skycloud.management.portal.front.command.res.CreateTemplate;
import com.skycloud.management.portal.front.command.res.CreateVolume;
import com.skycloud.management.portal.front.command.res.DeleteSnapshot;
import com.skycloud.management.portal.front.command.res.DestroyVirtualMachine;
import com.skycloud.management.portal.front.command.res.ListSnapshots;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.command.res.RebootRouter;
import com.skycloud.management.portal.front.command.res.UpdateRecordStateUninstall;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.service.IAsyncJobService;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.task.queue.CommonUtil;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.front.task.util.BaseCommandPo;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.portal.webservice.databackup.po.DBListVolumesCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.DBQueryDataBackUpInfoResponsePo;
import com.skycloud.management.portal.webservice.databackup.po.DBQuerySnapshotInfoResponsePo;
import com.skycloud.management.portal.webservice.databackup.po.DBQueryTemplateResponsePo;
import com.skycloud.management.portal.webservice.databackup.po.DBTemplate;
import com.skycloud.management.portal.webservice.databackup.po.DBUserSnapshotListResponsePo;
import com.skycloud.management.portal.webservice.databackup.po.DBVolume;
import com.skycloud.management.portal.webservice.databackup.po.DataBackUp;
import com.skycloud.management.portal.webservice.databackup.po.DbDeployVirtualMachine;
import com.skycloud.management.portal.webservice.databackup.po.DbNic;
import com.skycloud.management.portal.webservice.databackup.po.DbVirtumalMachinePo;
import com.skycloud.management.portal.webservice.databackup.po.JobResultQueryCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.RebootVirtualMachineCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.Snapshot;
import com.skycloud.management.portal.webservice.databackup.po.UserCancelDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserCreateSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserDeleteSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserModifyDaaBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQueryDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotListCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserResumeVirtualMachineCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;
import com.skycloud.management.portal.webservice.databackup.service.IDBAsycnJobService;
import com.skycloud.management.portal.webservice.databackup.service.IDBIntanceInfoService;
import com.skycloud.management.portal.webservice.databackup.service.IDBTemplateService;
import com.skycloud.management.portal.webservice.databackup.service.IDBUserSnapshotService;
import com.skycloud.management.portal.webservice.databackup.service.IDataBackUpService;
import com.skycloud.management.portal.webservice.databackup.utils.JacksonUtils;

public class DataBackUpService extends BaseService implements
		IDataBackUpService {

	private IDBTemplateService dbTemplateService;
	
	private IDBIntanceInfoService dbIntanceInfoService;
	
	private IDBUserSnapshotService dbUserSnapshotService;
	
	private ICommandService commandService;
	
	private IInstanceService instanceService;
	
	private IAsyncJobService asyncJobService;
	
	private IAsyncJobInfoDAO asyncJobDao;
	
	private IDBAsycnJobService dbAsycnJobService;
	@Override
	public Response QuerySnapshotTemplateService() throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<DBTemplate> templateList= dbTemplateService.queryDataBackUpTemplateVmList();
			DBQueryTemplateResponsePo po = new DBQueryTemplateResponsePo ();
			po.setTemplate(templateList);
			return Response.ok(po).build();
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QuerySnapshotTemplateService Exception: ",e);
			throw new ServiceException("获取用户快照模板列表异常");
		}
	}
	@Override
	public Response ModifyDataBackupService(UserModifyDaaBackUpCommandPo modifyPo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			int newTemplate = modifyPo.getTemplateId();
			int capacityUsed = getUsedCapacityCountNumberByCreateUser(modifyPo.getCreateUser());
			int capacityApply = getCaptacityCountNumberByTemplate(newTemplate);
		    if(capacityApply < capacityUsed){
		    	logger.error("ModifyDataBackupService Exception：capacity is not enough" );
		    	return responseOutFail();
		    }else{
		    	logger.info("ModifyDataBackupService Success ");
		    	return responseOutSuccess();
		    }
		}catch(Exception e){
			logger.error("Execute [QuerySnapshotTemplateService] method: ModifyDataBackupService Exception: ",e);
			throw new ServiceException("变更虚拟机备份参数校验异常");
		}
	}
	
	@Override
	public Response CreateSnapshotService(UserCreateSnapshotCommandPo createPo, int resourcePoolsId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			TInstanceInfoBO intanceInfoPo = getVirtualMachineResIdByIntanceInfoId (createPo.getIntanceInfoId());
			if(intanceInfoPo == null){
				return this.responseOutFail("操作失败，"+createPo.getIntanceInfoId()+" 没有记录");
			}
			if(!isUserDataBackUpCapacity(createPo , intanceInfoPo.getStorageSize())){
				logger.error("Execute [DataBackUpService] method: CreateSnapshotService Exception : Capacity is not enable!!");
				return this.responseOutFail("空间不够，无法成功生成快照");
			}
			if(!checkSnapshotType(createPo.getType() , intanceInfoPo.geteInstanceId(), resourcePoolsId)){
				logger.error("Execute [DataBackUpService] method: CreateSnapshotService Exception : check snapshot value fail!!");
				return this.responseOutFail("快照创建失败");
			}
			CreateSnapshot eSnaspshot = new CreateSnapshot ();
			if(createPo.getType() == 1){
				DBListVolumesCommandPo commanPo = new DBListVolumesCommandPo ();
				commanPo.setVirtualmachineid(String.valueOf(intanceInfoPo.geteInstanceId()));
				commanPo.setType("ROOT");	
				DBVolume volume = getElasterApiFromVolumeInfo (commanPo,resourcePoolsId);
				eSnaspshot.setVolumeid(Long.valueOf(volume.getId()));
			}else{
				eSnaspshot.setVolumeid(intanceInfoPo.geteInstanceId());
			}
			Object response = getExecuteElasterApi(eSnaspshot,resourcePoolsId);
			String jobId = CommonUtil.getJobIdFromExecuteResponse(response,TaskContext.OperateType.CREATESNAPSHOT.getDesc());
			UserSnapshot snapshot = new UserSnapshot ();
			snapshot.setCREATE_USER_ID(createPo.getCreateUser());
			snapshot.setINSTANCE_INFO_ID(createPo.getIntanceInfoId());
			snapshot.setSTORAGE_SIZE(intanceInfoPo.getStorageSize());
			snapshot.setCOMMENT(createPo.getDescription());
			snapshot.setJOB_ID(Integer.valueOf(jobId));
			snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
			snapshot.setTYPE(createPo.getType());
			dbUserSnapshotService.insertIntoTpSnapshot(snapshot);
			return responseOutSuccessJobIdSize(Integer.valueOf(jobId), intanceInfoPo.getStorageSize());
		}catch(Exception e){
			logger.error("Execute [DataBackUpService] method: CreateSnapshotService Exception : ",e);
			throw new ServiceException("用户创建虚拟机卷快照异常");
		}
	}
	
	@Override
	public Response ResumeVirtualMachineService(
			UserResumeVirtualMachineCommandPo resumePo,int resourcePoolsId) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			String asycnSelfJobId = getJobUUID();
			ListSnapshots querySnapshotInfo = new ListSnapshots ();
			querySnapshotInfo.setId(resumePo.getSnapshotId());
			Object response = getExecuteElasterApi(querySnapshotInfo,resourcePoolsId);
			String volumeId = CommonUtil.getJsonAttributeFromResponse (response,"listSnapshots" ,"snapshot" , "volumeid");
			String volumeName = CommonUtil.getJsonAttributeFromResponse (response,"listSnapshots" ,"snapshot" , "volumename");
			if(isResumeSystemDisk(resumePo.getType()) || isResumeNewVm(resumePo.getType())){//系统盘恢复、根据快照创建新的虚拟机
				if(!isCheckSnapshotTypeIsRoot(response)){
					logger.error("Exception : Resume type :1 or 2 But the snapshot type is datadisk. error !!!");
					this.responseOutFail();
				}
				getAnalyseOperatorTypeValue1Or3InsertIntoAsycnjobTable (resumePo.getSnapshotId(),resumePo.getVirtualMachineId(),resumePo.getType(),asycnSelfJobId,resourcePoolsId);
			}else if(resumePo.getType() == TaskContext.ResumeType.RESUMEOTHERDISK.getCode()){//快存储恢复
				if(!isVolumeStateDetach (volumeId,resourcePoolsId)){
					logger.error("Exception : Volume had detach !!!");
					return this.responseOutFail("该硬盘被虚机已经挂载，不能恢复");
				}
				if(isCheckSnapshotTypeIsRoot(response)){
					logger.error("Exception: Snapshot type is error");
					return this.responseOutFail("对数据盘进行恢复快照类型是系统盘快照，不能恢复");
				}
				getAnalyseOperatorTypeValue2InsertIntoAsycnjobTable(resumePo, volumeId, volumeName, asycnSelfJobId);
			}else{
				logger.error("incorrent resume type!!");
				throw new ServiceException ("恢复类型传入参数不正确");
			}
			return this.responseOutSuccessJobId(asycnSelfJobId);
		}catch(ServiceException e){
			logger.error("Execute[DataBackUpService] method : ResumeVirtualMachineService Exception :",e);
			throw new ServiceException ("快照恢复失败");
		}catch(Exception e){
			logger.error("Execute[DataBackUpService] method : ResumeVirtualMachineService Exception :",e);
			throw new ServiceException ("快照恢复失败");
		}
	}
	
	
	@Override
	public Response QueryDataBackupInfoService(UserQueryDataBackUpCommandPo dataBackupPo) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			DataBackUp dataBackUpInfo = new DataBackUp ();
			int capacityUsed = getUsedCapacityCountNumberByCreateUser(dataBackupPo.getCreateUser());
			int capacity = getCaptacityCountNumberByTemplate(dataBackupPo.getTemplateId());
			dataBackUpInfo.setTotal(capacity);
			dataBackUpInfo.setUsed(capacityUsed);
			DBQueryDataBackUpInfoResponsePo  po = new DBQueryDataBackUpInfoResponsePo();
			po.setDataBackup(dataBackUpInfo);
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("dataBackup", dataBackUpInfo);
			return Response.ok(po).build();
			//return JsonUtil.getJsonString4JavaPOJO(map);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QueryDataBackupInfoService Exception : ",e);
			throw new ServiceException("虚拟机备份详细信息查询异常");
		}
	}

	@Override
	public Response DeleteSnapshotService(UserDeleteSnapshotCommandPo deletePo,int resourcePoolsId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			DeleteSnapshot deletesnapshot = new DeleteSnapshot ();
			deletesnapshot.setId(String.valueOf(deletePo.getSnapshotId()));
			Object response = getExecuteElasterApi(deletesnapshot,resourcePoolsId);
			String jobId = CommonUtil.getJobIdFromExecuteResponse(response,TaskContext.OperateType.DELETSNAPHOT.getDesc());
			UserSnapshot snapshot = new UserSnapshot ();
			snapshot.setJOB_ID(Integer.valueOf(jobId));
			snapshot.setCREATE_USER_ID(deletePo.getCreateUser());
			snapshot.setE_SNAPSHOT_ID(deletePo.getSnapshotId());
			snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
			dbUserSnapshotService.updateTpSnapshotJobId(snapshot);
			return responseOutSuccessJobId(jobId);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : DeleteSnapshotService Exception : ",e);
			throw new ServiceException("删除虚拟机快照异常");
		}
	}

	@Override
	public Response CancelDataBackupService(UserCancelDataBackUpCommandPo cancelPo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			String asyncJobId = getJobUUID();
			UserSnapshot snapshot = new UserSnapshot ();
			snapshot.setCREATE_USER_ID(cancelPo.getCreateUser());
			snapshot.setSTATE(TaskContext.Status.USE.getCode());
			List<UserSnapshot> userSnapshotList =  dbUserSnapshotService.queryTpSnapshotList(snapshot);
			if(userSnapshotList != null){
				List<AsyncJobInfo> asycnList = new ArrayList<AsyncJobInfo>();
				for (UserSnapshot  deleSnapshotPo : userSnapshotList){
					DeleteSnapshot deletesnapshot = new DeleteSnapshot ();
					deletesnapshot.setId(String.valueOf(deleSnapshotPo.getE_SNAPSHOT_ID()));
					asycnList.add(bulidAsyncJobInfo(deletesnapshot,asyncJobId));
				}
				this.insertAsyncJobInfo(asycnList);
			}
			deleteSnapshotByCreateUser(cancelPo.getCreateUser());
			return this.responseOutSuccessJobId(asyncJobId);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method CancelDataBackupService Exception : ",e);
			throw new ServiceException ("撤销用户备份空间异常");
		}
	}
	
	@Override
	public Response QuerySnapshotInfoService(UserQuerySnapshotCommandPo queryPo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			UserSnapshot  userSnapshot = dbUserSnapshotService.queryTpSnapshotBySnapshotId(queryPo.getSnapshotId());
			if(userSnapshot != null){
				Snapshot snapshot = new Snapshot();
				snapshot.setId(String.valueOf(userSnapshot.getE_SNAPSHOT_ID()));
				snapshot.setSize(String.valueOf(userSnapshot.getSTORAGE_SIZE()));
				snapshot.setState(String.valueOf(userSnapshot.getSTATE()));
				snapshot.setCreateTime(FormatUtil.dateToString(userSnapshot.getCREATE_DT(),"yyyy-MM-dd HH:mm:ss"));
				snapshot.setDesc(userSnapshot.getCOMMENT());
				snapshot.setType(String.valueOf(userSnapshot.getTYPE()));
				DBQuerySnapshotInfoResponsePo po = new DBQuerySnapshotInfoResponsePo ();
				po.setSnapshot(snapshot);
//				Map<String, Object> map = new HashMap<String, Object>();
//				map.put("snapshot", snapshot);
//				return  JsonUtil.getJsonString4JavaPOJO(map);
				return Response.ok(po).build();
			}else{
				return responseOutFail();
			}
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method: QuerySnapshotInfoService Exception: ",e);
			throw new ServiceException ("快照详细信息查询异常");
		}
	}

	@Override
	public Response QuerySnapshotListService(UserQuerySnapshotListCommandPo queryPo) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			UserSnapshot snapshot = new UserSnapshot ();
			DBUserSnapshotListResponsePo resposne = new DBUserSnapshotListResponsePo ();
			snapshot.setCREATE_USER_ID(queryPo.getCreateUser());
			List<UserSnapshot>  result = dbUserSnapshotService.queryTpSnapshotList(snapshot);
			if(result == null){
				logger.warn("This user :"+queryPo.getCreateUser()+" snapshot list is empty.");
				return Response.ok(resposne).build();
				//return "{\"snapshots\":[]}";
			}
			List<Snapshot> queryList  = new ArrayList<Snapshot>();
			for(UserSnapshot snapshotDb : result ){
				Snapshot rSnapshot = new Snapshot(); 
				rSnapshot.setId(String.valueOf(snapshotDb.getE_SNAPSHOT_ID()));
				rSnapshot.setSize(String.valueOf(snapshotDb.getSTORAGE_SIZE()));
				rSnapshot.setState(String.valueOf(snapshotDb.getSTATE()));
				rSnapshot.setCreateTime(FormatUtil.dateToString(snapshotDb.getCREATE_DT(),"yyyy-MM-dd HH:mm:ss"));
				rSnapshot.setDesc(snapshotDb.getCOMMENT());
				rSnapshot.setType(String.valueOf(snapshotDb.getTYPE()));
				queryList.add(rSnapshot);
			}
			resposne.setSnapshot(queryList);
			return Response.ok(resposne).build();
//			Map<String, Object> map = new HashMap<String, Object>();
//			map.put("snapshots", queryList);
//			return  JsonUtil.getJsonString4JavaPOJO(map);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QuerySnapshotListService Exception ： ",e);
			throw new ServiceException("查询用户快照列表异常");
		}
	}

	@Override
	public Response QueryResumeVmResultService(JobResultQueryCommandPo jobPo)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<AsyncJobInfo> result = dbAsycnJobService.queryAsyncJobInfoOperatingInfoByComment(jobPo.getJobId());
			if(result.size() != 0){
				return this.responseOutFail("正在执行");
			}
			result = dbAsycnJobService.queryAsyncJobInfoIsFailInfoByComment(jobPo.getJobId());
			if(result .size() != 0){
				return this.responseOutFail();
			}
			return this.responseOutSuccess();
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QueryResumeVmResultService Exception :",e);
			throw new ServiceException();
		}
	}

	@Override
	public Response QueryCancelDataBackupResultService(
			JobResultQueryCommandPo jobPo) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			List<AsyncJobInfo> result = dbAsycnJobService.queryAsyncJobInfoOperatingInfoByComment(jobPo.getJobId());
			if(result != null){
				return this.responseOutFail("正在执行");
			}
			return this.responseOutSuccess();
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QueryCancelDataBackupResultService Exception : ",e);
			throw new ServiceException ("用户注销存储空间失败");
		}
	}
	@Override
	public Response QueryCreateSnapshotResultService (JobResultQueryCommandPo jobPo, int resourcePoolsId) throws ServiceException{
		// TODO Auto-generated method stub
		try{
			Object response = getExecuteElasterApi(CommandCreateUtil.createQueryAsyncJobResultPo(String.valueOf(jobPo.getJobId())),resourcePoolsId);
			String jobstatus =CommonUtil.getResponseResultStatus(response);
			if(jobstatus.equals(TaskContext.ResultStatus.OPERATSUCCESS.getCode())){//成功
				String resId = CommonUtil.getResIdFromExecuteResponse(response.toString(), getJsonResultName("createSnapshot"));
				this.updateUserSnapshotResult(Integer.valueOf(jobPo.getJobId()), jobPo.getCreateUser(), TaskContext.Status.USE.getCode(), resId);
				return this.responseOutSuccessJobId(resId);
			}else if(jobstatus.equals(TaskContext.ResultStatus.OPERATING.getCode())){
				return this.responseOutOperation();
			}else{
				logger.error("Elaster 接口返回失败");
				this.updateUserSnapshotResult(Integer.valueOf(jobPo.getJobId()), jobPo.getCreateUser(), TaskContext.Status.UNUSE.getCode(), "");
				return this.responseOutFail();
			}
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QueryCreateSnapshotResultService Exception : ",e);
			throw new ServiceException ("查询用户快照创建结果异常");
		}
	}
	
	@Override
	public Response QueryDeleteSnapshotResultService (JobResultQueryCommandPo jobPo, int resourcePoolsId) throws ServiceException{
		// TODO Auto-generated method stub
		try{
			Object response = getExecuteElasterApi(CommandCreateUtil.createQueryAsyncJobResultPo(String.valueOf(jobPo.getJobId())),resourcePoolsId);
			String jobstatus =CommonUtil.getResponseResultStatus(response);
			if(jobstatus.equals(TaskContext.ResultStatus.OPERATSUCCESS.getCode())){//成功
				dbUserSnapshotService.deleteTpSnapshotByJobId(Integer.valueOf(jobPo.getJobId()));
				return this.responseOutSuccess();
			}else if(jobstatus.equals(TaskContext.ResultStatus.OPERATING.getCode())){
				return this.responseOutOperation();
			}else{
				dbUserSnapshotService.deleteTpSnapshotByJobId(Integer.valueOf(jobPo.getJobId()));
				return this.responseOutFail();
			}
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : QueryDeleteSnapshotResultService Exception :",e);
			throw new  ServiceException ("查询快照删除结果异常");
		}
	}
	
	/**
	 * 得到用户已经使用的空间容量
	 * @param createUser
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-6  下午01:51:02
	 */
    public int getUsedCapacityCountNumberByCreateUser (int createUser) throws ServiceException{
    	try{
    		return dbUserSnapshotService.queryUserSnapshotSum(createUser);
    	}catch(ServiceException e){
    		throw new ServiceException("得到用户已经使用的空间容量异常");
    	}
    }
    
    /**
     * 根据模板标识获取该模板的大小
     * @param templateId
     * @return
     * @throws ServiceException
     * 创建人：  刘江宁    
     * 创建时间：2012-1-6  下午01:50:14
     */
    public int getCaptacityCountNumberByTemplate (int templateId) throws ServiceException{
    	try{
    		return  dbTemplateService.queryDataBackUpTemplateVmById(templateId).getSize();
    	}catch(ServiceException e){
    		throw new ServiceException("根据模板标识获取该模板的大小异常");
    	}
    }
    /**
     * 更新用户快照信息
     * @param jobid
     * @param createUser
     * @param state
     * @param resId
     * @throws ServiceException
     * 创建人：  刘江宁    
     * 创建时间：2012-1-13  下午04:28:55
     */
    private void updateUserSnapshotResult (int jobid , int createUser , int state,String resId) throws ServiceException{
    	try{
	    	UserSnapshot snapshot = new UserSnapshot ();
			snapshot.setJOB_ID(jobid);
			snapshot.setCREATE_USER_ID(createUser);
			snapshot.setSTATE(state);
			snapshot.setE_SNAPSHOT_ID(Integer.valueOf(resId));
			dbUserSnapshotService.updateTpSnapshot(snapshot);
    	}catch(ServiceException e){
    		logger.error("Execute [DataBackUpService] method : updateUserSnapshotResult :",e);
    		throw new ServiceException("更新用户快照信息异常");
    	}
	}
    /**
     * 执行Elaster接口返回对象
     * @param commandPo
     * @return
     * 创建人：  刘江宁    
     * 创建时间：2012-1-6  下午05:31:56
     */
    private Object getExecuteElasterApi (QueryCommand commandPo, int resourcePoolsId)throws ServiceException{
    	try{
    		Object response = new Object ();
    		return response = commandService.executeAndJsonReturn(commandPo,resourcePoolsId);
    	}catch(ServiceException e){
    		logger.error("Execute [DataBackUpService] method : getExecuteElasterApi :",e);
    		throw new ServiceException("调用Elaster接口异常");
    	}
    }
    /**
	 * 根据用户唯一标识物理删除用户快照列表
	 * @param createUser
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-13  下午04:26:38
	 */
    private void deleteSnapshotByCreateUser (int createUser) throws ServiceException{
    	try{
    		dbUserSnapshotService.deteteTpSnashotByCreateUser(createUser);
    	}catch(ServiceException e){
    		logger.error("Execute [DataBackUpService] method : deleteSnapshotByCreateUser :",e);
    		throw new ServiceException("根据用户唯一标识物理删除用户快照列表异常");
    	}
    }
    /**
     * 插入异步任务表
     * @param list
     * @throws ServiceException
     * 创建人：  刘江宁    
     * 创建时间：2012-1-9  下午02:39:41
     */
    private void insertAsyncJobInfo (List<AsyncJobInfo> list)throws ServiceException{
    	try{
    	    if(list == null){
    	    	logger.warn("insert AsyncJob List is empty!!!");
    	    	return;
    	    }
    		for(AsyncJobInfo asycnjob : list){
    			asyncJobDao.insertAsyncJob(asycnjob);
    		}
    	}catch(ServiceException e){
    		logger.error("Exceute [DataBackUpService] method: insertAsyncJobInfo Exception:",e);
    		throw new ServiceException ("插入任务队列失败");
    	}catch(SCSException e){
    		logger.error("Exceute [DataBackUpService] method: insertAsyncJobInfo Exception:",e);
    		throw new ServiceException ("插入任务队列失败");
    	}
    }
	
    /**
	 * 判断用户存储空间容量是否够大小
	 * @param createPo
	 * @param volumeSize
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  下午01:49:03
	 */
    private boolean isUserDataBackUpCapacity (UserCreateSnapshotCommandPo createPo,int volumeSize){
    	int capacityUsed = getUsedCapacityCountNumberByCreateUser(createPo.getCreateUser());
		int capacityApply = getCaptacityCountNumberByTemplate(createPo.getTemplateId());
		if((capacityUsed+volumeSize) > capacityApply){
			return false;
		}
		return true;
    }
    
    /**
	 * 分解操作类型是系统盘恢复或者创建新虚机的请求
	 * @param snapshotId
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午10:23:15
	 */
	private void getAnalyseOperatorTypeValue1Or3InsertIntoAsycnjobTable (int snapshotId , int intanceInfoVmId,int resumeType,String asycnSelfJobId,int resourcePoolsId){
		try{
			List<AsyncJobInfo> asycnList = new ArrayList<AsyncJobInfo>();
			DbVirtumalMachinePo virtualMachine = getElasterApiGetVirtualMachineInfo (intanceInfoVmId,resourcePoolsId);
			//创建模板
			CreateTemplate template = new CreateTemplate ();
			template.setSnapshotid(snapshotId);
			template.setDisplaytext(getBuildName());
			template.setName(getBuildName());
			asycnList.add(bulidAsyncJobInfo(template,asycnSelfJobId));
			if(resumeType == TaskContext.ResumeType.RESUMESYSTEMDISK.getCode()){
				//注销虚机
				DestroyVirtualMachine destoryVm = new DestroyVirtualMachine ();
				destoryVm.setId(Integer.valueOf(virtualMachine.getId()));
				asycnList.add(bulidAsyncJobInfo(destoryVm,asycnSelfJobId));
				//更改记录状态
				UpdateRecordStateUninstall updateState = new UpdateRecordStateUninstall ();
				asycnList.add(bulidAsyncJobInfo(updateState,asycnSelfJobId));
			}
			//发布虚机
			DbDeployVirtualMachine deployVm = new DbDeployVirtualMachine ();
			asycnList.add(bulidAsyncJobInfo(deployVm,virtualMachine,asycnSelfJobId));
			//重启路由
			List<DbNic> listNic = virtualMachine.getNic();
			if(listNic != null){
				for(DbNic nic : listNic){
					RebootRouter reRouter = new RebootRouter ();
					asycnList.add(bulidAsyncJobInfo(reRouter,asycnSelfJobId));
				}
			}
			//重启虚机
			RebootVirtualMachineCommandPo reVirtualMachine = new RebootVirtualMachineCommandPo();
			reVirtualMachine.setId("");
			asycnList.add(bulidAsyncJobInfo(reVirtualMachine,asycnSelfJobId));
			insertAsyncJobInfo(asycnList);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method　：　getAnalyseOperatorTypeValue1Or3InsertIntoAsycnjobTable　Exception :",e);
			throw new ServiceException ("分解操作类型是系统盘恢复或者创建新虚机的请求异常");
		}catch(Exception e){
			logger.error("Execute [DataBackUpService] method　：　getAnalyseOperatorTypeValue1Or3InsertIntoAsycnjobTable　Exception :",e);
			throw new ServiceException ("分解操作类型是系统盘恢复或者创建新虚机的请求异常");
		}
	}
	private String getBuildName (){
		String name = FormatUtil.dateToString(new Date(),"yyyyMMddHHmmss");
		return name+"c";
	}
	/**
	 * 根据IntanceInfo唯一标识调用Elaster接口获取虚拟机详细信息
	 * @param intanceInfoVmId
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午10:39:53
	 */
	private DbVirtumalMachinePo getElasterApiGetVirtualMachineInfo (int intanceInfoVmId, int resourcePoolsId) {
		try{
			TInstanceInfoBO project = getVirtualMachineResIdByIntanceInfoId (intanceInfoVmId);
			long elasterVmId = project.geteInstanceId();
			ListVirtualMachines queryPo = new ListVirtualMachines ();
			queryPo.setId(elasterVmId);
			Object response = getExecuteElasterApi(queryPo,resourcePoolsId);
			return CommonUtil.getJsonArrayToObject(response, "listvirtualmachines", "virtualmachine");
			//return (DbVirtumalMachinePo)JacksonUtils.fromJson(CommonUtil.getJsonArrayToObject(response, "listvirtualmachines", "virtualmachine"), DbVirtumalMachinePo.class) ;
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method : getElasterApiGetVirtualMachineInfo Exception:",e);
			throw new ServiceException ("调用Elaseter接口获取虚拟机详细信息异常");
		}
	}
	/**
	 * 根据IntanceInfo标识获取虚拟机Elaster唯一标识
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午10:11:29
	 */
	private TInstanceInfoBO getVirtualMachineResIdByIntanceInfoId (int intanceInfoId){
		try{
			TInstanceInfoBO project = instanceService.queryInstanceInfoById(intanceInfoId);
			if(project == null){
				logger.error("Execute [DataBackUpService] method :getVirtualMachineResIdByIntanceInfoId Exception: NullException" );
				throw new ServiceException("根据IntanceInfo标识获取虚拟机Elaster唯一标识异常");
			}
			return project;
		}catch(Exception e){
			logger.error("Execute [DataBackUpService] method :getVirtualMachineResIdByIntanceInfoId Exception: ",e);
			throw new ServiceException("根据IntanceInfo标识获取虚拟机Elaster唯一标识异常");
		}
	}
	/**
	 * 构建异步任务对象
	 * @param vmPo
	 * @param virtualMachineInfo
	 * @param common
	 * @return
	 * @throws Exception
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午09:51:54
	 */
	 private AsyncJobInfo bulidAsyncJobInfo (DbDeployVirtualMachine vmPo,DbVirtumalMachinePo virtualMachineInfo,String jobId)throws Exception{
			AsyncJobInfo asycnJobInfo = new AsyncJobInfo ();
			asycnJobInfo.setCREATE_DT(new Date());
	    	asycnJobInfo.setINSTANCE_INFO_ID(0);
	    	asycnJobInfo.setJOBSTATE(1);
	    	asycnJobInfo.setOPERATION(vmPo.getCommand());
	    	asycnJobInfo.setPARAMETER(vmPo.getParameter(virtualMachineInfo));
	    	asycnJobInfo.setCOMMON(jobId);
	    	return asycnJobInfo;
	 }
	 /**
	  * 构建异步任务对象
	  * @param command
	  * @param common
	  * @return
	  * 创建人：  刘江宁    
	  * 创建时间：2012-1-10  上午09:52:10
	  */
    private AsyncJobInfo bulidAsyncJobInfo (BaseCommandPo command,String jobId){
    	AsyncJobInfo asycnJobInfo = new AsyncJobInfo ();
    	asycnJobInfo.setCREATE_DT(new Date());
    	asycnJobInfo.setINSTANCE_INFO_ID(0);
    	asycnJobInfo.setJOBSTATE(1);
    	asycnJobInfo.setOPERATION(command.getCOMMAND());
    	asycnJobInfo.setPARAMETER(command.getParameter(command));
    	asycnJobInfo.setCOMMON(jobId);
    	return asycnJobInfo;
    }
    /**
	 * 分解操作类型是块存储恢复的请求
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  下午01:22:55
	 */
	private void getAnalyseOperatorTypeValue2InsertIntoAsycnjobTable (UserResumeVirtualMachineCommandPo resumePo ,String volumeId,String volumeName,String asycnSelfJobId){
		try{
			List<AsyncJobInfo> asycnList = new ArrayList<AsyncJobInfo>();
			CreateVolume createVolume = new CreateVolume ();
			createVolume.setSnapshotid(resumePo.getSnapshotId());
			createVolume.setName(volumeName);
			asycnList.add(bulidAsyncJobInfo(createVolume,asycnSelfJobId));
			insertAsyncJobInfo(asycnList);
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method　：　getAnalyseOperatorTypeValue2InsertIntoAsycnjobTable　Exception :",e);
			throw new ServiceException ("分解操作类型是块存储恢复的请求异常");
		}
	}
	/**
	 * 判断块存储是否被挂载在某个虚拟机上。
	 * @param volumeId
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  下午03:06:47
	 */
	private boolean isVolumeStateDetach(String volumeId, int resourcePoolsId){
		try{
			DBListVolumesCommandPo listVolumes = new DBListVolumesCommandPo ();
			listVolumes.setId(volumeId);
			DBVolume volume = getElasterApiFromVolumeInfo (listVolumes,resourcePoolsId);
			if(!volume.getState().equals("Ready")) return false;
			return true;
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method: isVolumeStateDetach Exception :" ,e);
			throw new ServiceException ("判断块存储是否被挂载在某个虚拟机上操作异常");
		}
	}
   
	/**
	 * 校验创建快照的类型和参数
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-12  下午03:11:28
	 */
	private boolean checkSnapshotType (int resumeType, long elasterResId, int resourcePoolsId){
		try{
			DBListVolumesCommandPo commanPo = new DBListVolumesCommandPo ();
			if (resumeType == 1){ //系统盘快照
				commanPo.setVirtualmachineid(String.valueOf(elasterResId));
				commanPo.setType("ROOT");	
			}else if(resumeType == 2){//普通块存储快照
				commanPo.setId(String.valueOf(elasterResId));
			}else {
				logger.error("incorrent snapshot type!!!!");
				throw new ServiceException ("校验创建快照类型和参数异常");
			}
			DBVolume volume = getElasterApiFromVolumeInfo (commanPo,resourcePoolsId);
			if(!volume.getState().equals("Ready")){
				logger.error("This Volume: "+elasterResId+" state :" +volume.getState());
				return false;
			}
			if(!volume.getVmstate().equals("Running")){
				logger.error("This Volume: "+elasterResId+" VirtualMachine Running State is not Running");
				return false;
			}
			return true;
		}catch(ServiceException e){
			logger.error("Execute [DataBackUpService] method: checkSnapshotType Exception :" ,e);
			throw new ServiceException ("校验创建快照类型和参数异常");
		}
	}
	/**
	 * 调用Elaster接口获取卷的详细信息
	 * @param commanPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-12  下午05:17:04
	 */
	private DBVolume getElasterApiFromVolumeInfo (DBListVolumesCommandPo commanPo,int resourcePoolsId){
		try{
			Object response =  getExecuteElasterApi(commanPo,resourcePoolsId);                                   
			return (DBVolume)JacksonUtils.fromJson(CommonUtil.getJsonArrayToObjectStr(response, "listvolumes", "volume"), DBVolume.class) ;
		}catch(Exception e){
			logger.error("Execute [DataBackUpService] method: checkSnapshotType Exception :" ,e);
			throw new ServiceException ("调用Elaster接口获取卷详细信息异常");
		}
	}
	/**
	 * 判断快照类型是否是系统盘的快照。
	 * @param response
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-12  上午10:14:28
	 */
	private boolean isCheckSnapshotTypeIsRoot (Object response){
		String type = CommonUtil.getJsonAttributeFromResponse (response,"listSnapshots" ,"snapshot" , "volumetype");
		if(type.equalsIgnoreCase("ROOT")){
			return true;
		}
		return false;
	}
	public IDBTemplateService getDbTemplateService() {
		return dbTemplateService;
	}

	public void setDbTemplateService(IDBTemplateService dbTemplateService) {
		this.dbTemplateService = dbTemplateService;
	}

	public IDBIntanceInfoService getDbIntanceInfoService() {
		return dbIntanceInfoService;
	}

	public void setDbIntanceInfoService(IDBIntanceInfoService dbIntanceInfoService) {
		this.dbIntanceInfoService = dbIntanceInfoService;
	}

	public IDBUserSnapshotService getDbUserSnapshotService() {
		return dbUserSnapshotService;
	}

	public void setDbUserSnapshotService(
			IDBUserSnapshotService dbUserSnapshotService) {
		this.dbUserSnapshotService = dbUserSnapshotService;
	}
	
	public ICommandService getCommandService() {
		return commandService;
	}
	
	public void setCommandService(ICommandService commandService) {
		this.commandService = commandService;
	}
	
	
	
	public IInstanceService getInstanceService() {
		return instanceService;
	}
	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}
	public IAsyncJobService getAsyncJobService() {
		return asyncJobService;
	}
	
	public void setAsyncJobService(IAsyncJobService asyncJobService) {
		this.asyncJobService = asyncJobService;
	}
	public IAsyncJobInfoDAO getAsyncJobDao() {
		return asyncJobDao;
	}
	public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
		this.asyncJobDao = asyncJobDao;
	}
	public IDBAsycnJobService getDbAsycnJobService() {
		return dbAsycnJobService;
	}
	public void setDbAsycnJobService(IDBAsycnJobService dbAsycnJobService) {
		this.dbAsycnJobService = dbAsycnJobService;
	}
	
}
