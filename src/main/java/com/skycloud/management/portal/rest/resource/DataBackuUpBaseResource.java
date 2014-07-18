package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.webservice.databackup.po.JobResultQueryCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserCancelDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserCreateSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserDeleteSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserModifyDaaBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQueryDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotListCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserResumeVirtualMachineCommandPo;
import com.skycloud.management.portal.webservice.databackup.service.IDataBackUpService;

@Component
@Path("/dataBackUp")
public class DataBackuUpBaseResource extends BaseService {
	@Autowired
	private  IDataBackUpService dataBackUpService;
	/**
	 * 查询存储模板信息列表
	 */
	@GET
	@Path("QuerySnapshotTemplateService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QuerySnapshotTemplateService() {
		try{
			return dataBackUpService.QuerySnapshotTemplateService();
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QuerySnapshotTemplateService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 变更虚拟机备份参数校验
	 * @param modifyPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:06:18
	 */
	@POST
	@Path("ModifyDataBackupService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response ModifyDataBackupService (UserModifyDaaBackUpCommandPo modifyPo){
		try{
			if(!modifyPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.ModifyDataBackupService(modifyPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : ModifyDataBackupService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 创建虚拟机快照
	 * @param createPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:09:23
	 */
	@POST
	@Path("CreateSnapshotService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response CreateSnapshotService (UserCreateSnapshotCommandPo createPo){
		try{
			if(!createPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			//经过与张慧征确认，此处方法没有action调用。
			return dataBackUpService.CreateSnapshotService(createPo,1);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : CreateSnapshotService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 虚拟机快照恢复
	 * @param resumePo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:11:23
	 */
	@POST
	@Path("ResumeVirtualMachineService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response ResumeVirtualMachineService (UserResumeVirtualMachineCommandPo resumePo){
		try{
			if(!resumePo.attributeCheck()){
				return responseOutFailByParamer();
			}
			//经过与张慧征确认，此处方法没有action调用。
			return dataBackUpService.ResumeVirtualMachineService(resumePo,1);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : ResumeVirtualMachineService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 虚拟机备份详细信息查询
	 * @param dataBackupPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:22:56
	 */
	@POST
	@Path("QueryDataBackupInfoService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QueryDataBackupInfoService (UserQueryDataBackUpCommandPo dataBackupPo){
		try{
			if(!dataBackupPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.QueryDataBackupInfoService(dataBackupPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QueryDataBackupInfoService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 删除虚拟机快照备份
	 * @param deletePo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:25:36
	 */
	@POST
	@Path("DeleteSnapshotService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response DeleteSnapshotService(UserDeleteSnapshotCommandPo deletePo) {
		try{
			if(!deletePo.attributeCheck()){
				return responseOutFailByParamer();
			}
			//经过与张慧征确认，此处方法没有action调用。
			return dataBackUpService.DeleteSnapshotService(deletePo,1);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : DeleteSnapshotService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 取消虚拟机备份
	 * @param cancelPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:28:03
	 */
	@POST
	@Path("CancelDataBackupService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response CancelDataBackupService(UserCancelDataBackUpCommandPo cancelPo) {
		try{
			if(!cancelPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.CancelDataBackupService(cancelPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : CancelDataBackupService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 快照详细信息查询
	 * @param queryPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:30:08
	 */
	@POST
	@Path("QuerySnapshotInfoService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QuerySnapshotInfoService(UserQuerySnapshotCommandPo queryPo) {
		try{
			if(!queryPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.QuerySnapshotInfoService(queryPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QuerySnapshotInfoService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 查询快照列表
	 * @param queryPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:31:27
	 */
	@POST
	@Path("QuerySnapshotListService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QuerySnapshotListService(UserQuerySnapshotListCommandPo queryPo) {
		try{
			if(!queryPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.QuerySnapshotListService(queryPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QuerySnapshotListService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 虚拟机备份恢复异步任务查询
	 * @param jobPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:33:41
	 */
	@POST
	@Path("QueryResumeVmResultService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QueryResumeVmResultService(JobResultQueryCommandPo jobPo) {
		try{
			if(!jobPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.QueryResumeVmResultService(jobPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QueryResumeVmResultService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 取消虚拟机备份服务异步任务查询
	 * @param jobPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:35:43
	 */
	@POST
	@Path("QueryCancelDataBackupResultService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QueryCancelDataBackupResultService(JobResultQueryCommandPo jobPo) {
		try{
			if(!jobPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			return dataBackUpService.QueryCancelDataBackupResultService(jobPo);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QueryCancelDataBackupResultService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 创建虚拟机快照备份异步任务查询
	 * @param jobPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:37:04
	 */
	@POST
	@Path("QueryCreateSnapshotResultService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QueryCreateSnapshotResultService (JobResultQueryCommandPo jobPo) {
		try{
			if(!jobPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			//经过与张慧征确认，此处方法没有action调用。
			return dataBackUpService.QueryCreateSnapshotResultService(jobPo,1);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QueryCreateSnapshotResultService Exception ",e);
			return responseOutFail();
		}
	}
	/**
	 * 删除虚拟机快照异步任务查询
	 * @param jobPo
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-16  上午10:38:11
	 */
	@POST
	@Path("QueryDeleteSnapshotResultService")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response QueryDeleteSnapshotResultService (JobResultQueryCommandPo jobPo) {
		try{
			if(!jobPo.attributeCheck()){
				return responseOutFailByParamer();
			}
			//经过与张慧征确认，此处方法没有action调用。
			return dataBackUpService.QueryDeleteSnapshotResultService(jobPo,1);
		}catch(ServiceException e){
			logger.error("Execute [DataBackuUpBaseResource] method : QueryDeleteSnapshotResultService Exception ",e);
			return responseOutFail();
		}
	}
}
