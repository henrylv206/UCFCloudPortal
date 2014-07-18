package com.skycloud.management.portal.task.vdc.service;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.service.IAsyncJobService;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.service.bo.RespInfosBO;

public class SyncBusinessInfosServiceImpl implements SyncBusinessInfosService {

	private AsyncJobVDCTaskService taskService;

	private IInstanceService instanceService;

	private IVMTemplateService VMTemplateService;
	
	private IAsyncJobService asyncJobService;

	private static Log log = LogFactory.getLog(SyncBusinessInfosServiceImpl.class);
	
	@Override
	public void updateInstanceAndIriInfo(RespInfosBO respBO)
			throws SCSException {
		this.updateInstanceAndAsyncInfo(respBO);
		this.updateIriInfo(respBO);
		
	}

	@Override
	public void updateInstanceAndAsyncInfo(RespInfosBO respBO) throws SCSException {

		int insid = respBO.getAsyncJobPO().getInstance_info_id();
		int state = respBO.getState();
		String rescode = respBO.getRes_code();
		try{
			instanceService.updateIntanceInfoJob(insid, state,
					rescode);
		}catch(Exception e){
			 log.error(e);
			  throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_INSTANCEINFO_ERROR, SCSErrorCode.DB_SQL_UPDATE_INSTANCEINFO_DESC);
		}
		this.updateAsyncJobVDCInfo(respBO);
	}

	@Override
	public void updateIriInfo(RespInfosBO respBO) throws SCSException {
		int iristate  = respBO.getIriState();
		int iriId = respBO.getAsyncJobPO().getInstance_info_iri_id();
		try{
			asyncJobService.updateTScsIriStateById(iristate, iriId);
		}catch(Exception e){
			 log.error(e);
		     throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_IRI_ERROR, SCSErrorCode.DB_SQL_UPDATE_IRI_DESC);
		}
		
	}

	@Override
	public void updateUserSnapShotInfo(RespInfosBO respBO) throws SCSException {
		// TODO Auto-generated method stub
	}

	@Override
	public void updateAsyncJobVDCInfo(RespInfosBO respBO) throws SCSException {
		
		AsyncJobVDCPO jobPO = respBO.getAsyncJobPO();
		jobPO.setResp_code(respBO.getRes_code());
		
		try{
			 taskService.updateAsyncJobStateVDC(jobPO);
		}catch(Exception e){
			 log.error(e);
		     throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_DESC);
		}
	}

	@Override
	public void updateVmTemplateInfo(RespInfosBO respBO) throws SCSException {
		
		int state = respBO.getState();
		int templateId = respBO.getAsyncJobPO().getTemplate_id();
		
		try{
			VMTemplateService.upateTemplateState(templateId, state);
		}catch(Exception e){
			 log.error(e);
		     throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_VMTEMPLATE_ERROR, SCSErrorCode.DB_SQL_UPDATE_VMTEMPLATE_DESC);
		}
		this.updateAsyncJobVDCInfo(respBO);
	}

	public void setTaskService(AsyncJobVDCTaskService taskService) {
		this.taskService = taskService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	public void setVMTemplateService(IVMTemplateService vMTemplateService) {
		VMTemplateService = vMTemplateService;
	}

	public void setAsyncJobService(IAsyncJobService asyncJobService) {
		this.asyncJobService = asyncJobService;
	}

	
	
}
