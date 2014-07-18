package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.AttachVolume;
import com.skycloud.management.portal.front.command.res.CreateSnapshot;
import com.skycloud.management.portal.front.command.res.CreateVolume;
import com.skycloud.management.portal.front.command.res.DeleteSnapshot;
import com.skycloud.management.portal.front.command.res.DeleteVolume;
import com.skycloud.management.portal.front.command.res.DetachVolume;
import com.skycloud.management.portal.front.command.res.ListAccounts;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.resources.action.vo.VolumeResumeVO;
import com.skycloud.management.portal.front.resources.dao.IVolumeHistoryDao;
import com.skycloud.management.portal.front.resources.dao.bo.VolumeHistoryBO;
import com.skycloud.management.portal.front.resources.service.IVolumeOperateService;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;
import com.skycloud.management.portal.webservice.databackup.service.IDBUserSnapshotService;

public class VolumeOperateServiceImpl implements IVolumeOperateService {

	private Logger log = Logger.getLogger(VolumeOperateServiceImpl.class);
	private IAsyncJobInfoDAO asyncJobDao;

	private IInstanceInfoDao instanceInfoDao;

	private IOrderDao orderDao;

	private IAuditSevice auditService;
	
	private IDBUserSnapshotService dbUserSnapshotService;
	
	private AsyncJobVDCService asyncJobVDCService;
	
	private IVolumeHistoryDao volumeHistoryDao;
	
	private ICommandService commandService;

	@Override
	public void insertAttachVolumeOperate(Iri iri, TInstanceInfoBO infobo)
			throws Exception {

		try {
			int id = asyncJobDao.updateiriInfoforVolume(iri);
			instanceInfoDao.update(infobo);
			//用户日志接口参数
			String memo = "";
			int project_switch = ConstDef.curProjectId;
            if (project_switch == 1){
            	AsyncJobInfo ajInfo = new AsyncJobInfo();
    			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    			Map<String, Object> subMap = new HashMap<String, Object>();
    			subMap.put(AttachVolume.ID, iri.getE_DISK_INSTANCE_INFO_ID());
    			subMap.put(AttachVolume.VIRTUAL_MACHINE_ID,
    					iri.getE_VM_INSTANCE_INFO_ID());
    			map.put(AttachVolume.COMMAND, subMap);
    			String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    			ajInfo.setAPPLY_ID(1);
    			ajInfo.setINSTANCE_INFO_ID(id);
    			ajInfo.setOPERATION(AttachVolume.COMMAND);
    			ajInfo.setPARAMETER(parameter);
    			ajInfo.setCREATE_DT(new Date(iri.getCREATE_DT().getTime()));
    			ajInfo.setJOBSTATE(1);
    			asyncJobDao.insertAsyncJob(ajInfo);
    			
    			//用户日志接口参数
				memo = parameter;
            }else if (project_switch == 2){
            	//JOB信息封装开始	
				AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
					String parameters = "";// 操作参数，调用api所需要的参数
						Map<String, Object> mapJob = new HashMap<String, Object>();
						TInstanceInfoBO infoVMbo = instanceInfoDao.searchInstanceInfoByID(iri.getVM_INSTANCE_INFO_ID());
//						String bsId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_DISK_INSTANCE_INFO_ID());
//						String vmId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_VM_INSTANCE_INFO_ID());
							mapJob.put("VMID", infoVMbo.getResCode());
							mapJob.put("BSID", infobo.getResCode());
						parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
					asynJobVDCPO.setParameter(parameters);
					//2012-05-03 hefk 修改: AuditStateVDC.WAIT_AUDIT 改为 AuditStateVDC.NO_AUDIT,原因:块绑定不需要审批
					asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
					asynJobVDCPO.setUser_id(iri.getCREATE_USER_ID());
					asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
					asynJobVDCPO.setOperation(OperationType.BIND_EBS);
					
					asynJobVDCPO.setInstance_info_id(iri.getDISK_INSTANCE_INFO_ID());
					asynJobVDCPO.setInstance_info_iri_id(id);
					
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
				//JOB信息封装结束
				
				//用户日志接口参数
				memo = parameters;
			
            }
            //用户日志记录
            ConstDef.saveLogInfo(1, "产品管理-块存储job", "挂接job", "VolumeOperateServiceImpl.java", "insertAttachVolumeOperate()", "", "", memo);
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}
	}
	
	public void insertDiskAttachVolumeOperate(TInstanceInfoBO infobo) throws Exception {

		try {
	 
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}
	}
	

	@Override
	public void insertCreatesnapshot(TInstanceInfoBO infobo, String id)
			throws Exception {
		try {
			instanceInfoDao.update(infobo);

			
			this.insertCreateSnapShotCommand(infobo,id);
			
			//SkyFormOpt1.1 修改备份逻辑
//			if (id == null) {
//				this.insertCreateSnapShotCommand(infobo);
//			} else {
//				this.insertCreateSnapShotCommand(infobo);
//				this.insertDeleteSnapShotCommand(infobo, id);
//			}

		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());

		}

	}

	/**
	 * 插入创建快照命令
	 * 
	 * @param infobo
	 * @throws Exception
	 */
	private void insertCreateSnapShotCommand(TInstanceInfoBO infobo,String userId)
			throws Exception {
		AsyncJobInfo ajInfo = new AsyncJobInfo();
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		Map<String, Object> subMap = new HashMap<String, Object>();
		subMap.put(CreateSnapshot.VOLUME_ID, infobo.geteInstanceId());

		map.put(CreateSnapshot.COMMAND, subMap);
		String parameter = JsonUtil.getJsonString4JavaPOJO(map);
		ajInfo.setAPPLY_ID(1);
		ajInfo.setINSTANCE_INFO_ID(infobo.getId());
		ajInfo.setOPERATION(CreateSnapshot.COMMAND);
		ajInfo.setPARAMETER(parameter);
		ajInfo.setCREATE_DT(new Date(System.currentTimeMillis()));
		ajInfo.setJOBSTATE(1);

		try {
			int index = asyncJobDao.insertAsyncJob(ajInfo);
			UserSnapshot snapshot = new UserSnapshot ();
			snapshot.setCREATE_USER_ID(Integer.parseInt(userId));
			snapshot.setINSTANCE_INFO_ID(infobo.getId());
			snapshot.setSTORAGE_SIZE(infobo.getStorageSize()*1024);
			snapshot.setCOMMENT("1");
			//系统盘标识
			snapshot.setTYPE(2);
			snapshot.setASYN_ID(index);
			snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
			dbUserSnapshotService.insertIntoTpSnapshot(snapshot);
		} catch (SCSException e) {
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}

	@Override
	public void insertDeletesnapshot(VolumeResumeVO volumevo,TInstanceInfoBO infobo)
			throws Exception {
	   try {
	      instanceInfoDao.update(infobo);
	      this.insertDeleteSnapShotCommand(infobo,volumevo.getSnapshotId());
	    } catch (Exception e) {
	      e.printStackTrace();
	      log.error("insert asyncjobinfo error:" + e.getMessage());
	      throw new Exception("insert asyncjobinfo error:" + e.getMessage());
	    }
	}

	/**
	 * 插入刪除快照命令
	 * 
	 * @param infobo
	 * @param id
	 * @throws Exception
	 */

	private void insertDeleteSnapShotCommand(TInstanceInfoBO infobo, String id)
			throws Exception {
		AsyncJobInfo ajInfo = new AsyncJobInfo();
		Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
		Map<String, Object> subMap = new HashMap<String, Object>();
		subMap.put(DeleteSnapshot.ID, id);

		map.put(DeleteSnapshot.COMMAND, subMap);
		String parameter = JsonUtil.getJsonString4JavaPOJO(map);
		ajInfo.setAPPLY_ID(1);
		ajInfo.setINSTANCE_INFO_ID(infobo.getId());
		ajInfo.setOPERATION(DeleteSnapshot.COMMAND);
		ajInfo.setPARAMETER(parameter);
		ajInfo.setCREATE_DT(new Date(System.currentTimeMillis()));
		ajInfo.setJOBSTATE(1);

		try {
			asyncJobDao.insertAsyncJob(ajInfo);
			dbUserSnapshotService.deleteTpSnapshotBySnapshotId(Integer.parseInt(id));
		} catch (SCSException e) {
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}
	}

	@Override
	public void insertDetachVolumeOperate(Iri iri, TInstanceInfoBO infobo)
			throws Exception {
		try {
			int id = asyncJobDao.updateiriInfoforVolume(iri);
			instanceInfoDao.update(infobo);
			//接口调用参数;
			String memo = "";
			int project_switch = ConstDef.curProjectId;
            if (project_switch == 1){
            	AsyncJobInfo ajInfo = new AsyncJobInfo();
    			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    			Map<String, Object> subMap = new HashMap<String, Object>();
    			subMap.put(DetachVolume.ID, iri.getE_DISK_INSTANCE_INFO_ID());
    			map.put(DetachVolume.COMMAND, subMap);
    			String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    			ajInfo.setAPPLY_ID(1);
    			ajInfo.setINSTANCE_INFO_ID(id);
    			ajInfo.setOPERATION(DetachVolume.COMMAND);
    			ajInfo.setPARAMETER(parameter);
    			ajInfo.setCREATE_DT(new Date(iri.getCREATE_DT().getTime()));
    			ajInfo.setJOBSTATE(1);
    			asyncJobDao.insertAsyncJob(ajInfo);
    			
    			//接口调用参数;
				memo = parameter;
            }else if (project_switch == 2){
            	//JOB信息封装开始	
				AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
					String parameters = "";// 操作参数，调用api所需要的参数
						Map<String, Object> mapJob = new HashMap<String, Object>();
//						String bsId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_DISK_INSTANCE_INFO_ID());
//						String vmId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_VM_INSTANCE_INFO_ID());
						TInstanceInfoBO infoVMbo = instanceInfoDao.searchInstanceInfoByID(iri.getVM_INSTANCE_INFO_ID());
							mapJob.put("VMID", infoVMbo.getResCode());
							mapJob.put("BSID", infobo.getResCode());
						parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
					asynJobVDCPO.setParameter(parameters);
					//解绑定无需审核
					asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
					asynJobVDCPO.setUser_id(iri.getCREATE_USER_ID());
					asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
					asynJobVDCPO.setOperation(OperationType.UNBIND_EBS);
					
					asynJobVDCPO.setInstance_info_id(iri.getDISK_INSTANCE_INFO_ID());
					asynJobVDCPO.setInstance_info_iri_id(id);
					
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
				//JOB信息封装结束
				
				//接口调用参数;
				memo = parameters;
            }
            
          //用户日志记录
		    ConstDef.saveLogInfo(1, "产品管理-块存储job", "解挂job", "VolumeOperateServiceImpl.java", "insertDetachVolumeOperate()", "", "", memo);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}
	}
	@Override
	public void insertDetachVolumeOperateAuto(Iri iri, TInstanceInfoBO infobo)
			throws Exception {
		try {
			int id = asyncJobDao.updateiriInfoforVolume(iri);
			instanceInfoDao.update(infobo);
			//接口调用参数;
			String memo = "";
			int project_switch = ConstDef.curProjectId;
            if (project_switch == 1){
            	AsyncJobInfo ajInfo = new AsyncJobInfo();
    			Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    			Map<String, Object> subMap = new HashMap<String, Object>();
    			subMap.put(DetachVolume.ID, iri.getE_DISK_INSTANCE_INFO_ID());
    			map.put(DetachVolume.COMMAND, subMap);
    			String parameter = JsonUtil.getJsonString4JavaPOJO(map);
    			ajInfo.setAPPLY_ID(1);
    			ajInfo.setINSTANCE_INFO_ID(id);
    			ajInfo.setOPERATION(DetachVolume.COMMAND);
    			ajInfo.setPARAMETER(parameter);
    			ajInfo.setCREATE_DT(new Date(iri.getCREATE_DT().getTime()));
    			ajInfo.setJOBSTATE(1);
    			asyncJobDao.insertAsyncJob(ajInfo);
    			
    			//接口调用参数;
				memo = parameter;
            }else if (project_switch == 2){
            	//JOB信息封装开始	
				AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
					String parameters = "";// 操作参数，调用api所需要的参数
						Map<String, Object> mapJob = new HashMap<String, Object>();
//						String bsId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_DISK_INSTANCE_INFO_ID());
//						String vmId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,iri.getE_VM_INSTANCE_INFO_ID());
						TInstanceInfoBO infoVMbo = instanceInfoDao.searchInstanceInfoByID(iri.getVM_INSTANCE_INFO_ID());
							mapJob.put("VMID", infoVMbo.getResCode());
							mapJob.put("BSID", infobo.getResCode());
						parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
					asynJobVDCPO.setParameter(parameters);
					//解绑定无需审核
					asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
					asynJobVDCPO.setUser_id(iri.getCREATE_USER_ID());
					asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
					asynJobVDCPO.setOperation(OperationType.UNBIND_EBS);
					
					asynJobVDCPO.setInstance_info_id(iri.getDISK_INSTANCE_INFO_ID());
					asynJobVDCPO.setInstance_info_iri_id(id);
					
				asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
				//JOB信息封装结束
				
				//接口调用参数;
				memo = parameters;
            }            
          log.info("---------insertDetachVolumeOperate is finished-------");
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}
	}

	@Override
	public void insertResumeSnapshot(VolumeResumeVO volumevo,
			TInstanceInfoBO infobo) throws Exception {

		try {
			instanceInfoDao.update(infobo);
			List<AsyncJobInfo> ajInfos = new ArrayList<AsyncJobInfo>();
			AsyncJobInfo ajInfo = new AsyncJobInfo();
			CreateVolume cvpo = new CreateVolume();
			cvpo.setSnapshotid(Integer.parseInt(volumevo.getSnapshotId()));
			cvpo.setAccount(JSONObject.fromObject(infobo.getResourceInfo()).getString("account"));
			
			ListAccounts account = new ListAccounts();
			account.setName(JSONObject.fromObject(infobo.getResourceInfo()).getString("account"));
			Object response = commandService.executeAndJsonReturn(account, Integer.parseInt(volumevo.getResourcePoolsId()));
      JSONObject jsonRes = JSONObject.fromObject(response);
      JSONObject jsonObject = jsonRes.getJSONObject("listaccountsresponse");
      Integer domainId = jsonObject.getJSONArray("account").getJSONObject(0).getInt("domainid");
			cvpo.setName(infobo.getInstanceName());
			cvpo.setDomainid(domainId);
			String parameter = CommandCreateUtil.getJsonParameterStr(cvpo);
			String operation = cvpo.getCOMMAND();

			ajInfo.setAPPLY_ID(1);
			ajInfo.setINSTANCE_INFO_ID(infobo.getId());
			ajInfo.setOPERATION(operation);
			ajInfo.setPARAMETER(parameter);
			ajInfo.setJOBSTATE(1);
			
			//恢复快照时同时删除之前volume
			AsyncJobInfo ajDeleteVolumeInfo = new AsyncJobInfo();
			DeleteVolume depo = new DeleteVolume();
			depo.setId(new Long(infobo.geteInstanceId()).intValue());
			ajDeleteVolumeInfo.setAPPLY_ID(1);
			ajDeleteVolumeInfo.setINSTANCE_INFO_ID(infobo.getId());
			ajDeleteVolumeInfo.setOPERATION(depo.getCOMMAND());
			ajDeleteVolumeInfo.setPARAMETER(CommandCreateUtil.getJsonParameterStr(depo));
			ajDeleteVolumeInfo.setJOBSTATE(1);
			ajInfos.add(ajInfo);
			ajInfos.add(ajDeleteVolumeInfo);
//			asyncJobDao.insertAsyncJob(ajInfo);
			asyncJobDao.updatebatchAsyncJobInfo(ajInfos);
			
			VolumeHistoryBO volumeHistory = new VolumeHistoryBO();
			volumeHistory.setUSER_ID(infobo.getUserId());
			volumeHistory.setUSER_NAME(JSONObject.fromObject(infobo.getResourceInfo()).getString("account"));
			volumeHistory.setE_DISK_ID((int) infobo.geteInstanceId());
			volumeHistory.setINSTANCE_ID(String.valueOf(infobo.getId()));
			volumeHistory.setINSTANCE_NAME(infobo.getInstanceName());
			volumeHistoryDao.insertVolumeHistory(volumeHistory);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}

	public void insertDestroyIPSan(TInstanceInfoBO infobo, String reason,
			TUserBO user, int serviceId) throws Exception {
		try {
			instanceInfoDao.update(infobo);
			
			TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceId);
			
			TOrderBO order = new TOrderBO();
			order.setType(3);// 删除申请
			order.setState(1);// 申请状态
			//order.setClusterId(infobo.getState()); //clusterId中保存原instance状态
			order.setCreatorUserId(user.getId());// 下单人ID
//			order.setInstanceInfoId(infobo.getId());
			order.setServiceInstanceId(serviceId);
			order.setZoneId(infobo.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号 日期+6位随机数
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				//vdc项目判断
				int project_switch = ConstDef.curProjectId;
				if (project_switch == 2){
	            	//JOB信息封装开始	

					AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
						String parameters = "";// 操作参数，调用api所需要的参数
							Map<String, Object> mapJob = new HashMap<String, Object>();
//							String bsId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,infobo.geteDiskId());
								mapJob.put("BSID", infobo.getResCode());
							parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
						asynJobVDCPO.setParameter(parameters);
						//销毁资源需要审核,增删改都要审核
						asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
						asynJobVDCPO.setUser_id(order.getCreatorUserId());
						asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
						asynJobVDCPO.setOperation(OperationType.DELETE_EBS);
						asynJobVDCPO.setOrder_id(saveOrderId);
					asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
					//JOB信息封装结束
					
					//用户日志记录
						String para = "templateid="+infobo.getTemplateId()+",instanceId="+infobo.geteDiskId();
						String memo = parameters;
					ConstDef.saveLogInfo(1, "产品管理-块存储job", "退定job", "VolumeOperateServiceImpl.java", "insertDestroyVolume()", para, "", memo);
					//用户日志结束
				}
				//vdc项目判断结束 
				
				//自动审核
				if (user.getRoleApproveLevel() == 4) {
					auditService.approveOrder(user.getId(), saveOrderId,
							user.getRoleApproveLevel(), 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				} else if (user.getRoleApproveLevel() < 4) {
					auditService.isAutoApproveUser(user.getId(), saveOrderId,
							user.getRoleApproveLevel()+1, 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				}
				//自动审核结束
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}	
	
	@Override
	public void insertDestroyVolume(TInstanceInfoBO infobo, String reason,
			TUserBO user) throws Exception {
		try {
			instanceInfoDao.update(infobo);
			TOrderBO order = new TOrderBO();
			order.setType(3);// 删除申请
			order.setState(1);// 申请状态
			//order.setClusterId(infobo.getState()); //clusterId中保存原instance状态
			order.setCreatorUserId(user.getId());// 下单人ID
			order.setInstanceInfoId(infobo.getId());
			order.setZoneId(infobo.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
			order.setOrderCode(sdf.format(new java.util.Date())
					+ (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号 日期+6位随机数
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				//vdc项目判断
				int project_switch = ConstDef.curProjectId;
				if (project_switch == 2){
	            	//JOB信息封装开始	

					AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
						String parameters = "";// 操作参数，调用api所需要的参数
							Map<String, Object> mapJob = new HashMap<String, Object>();
//							String bsId = String.format("CIDC-R-%1$02d-BS-%2$09d", 1,infobo.geteDiskId());
								mapJob.put("BSID", infobo.getResCode());
							parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
						asynJobVDCPO.setParameter(parameters);
						//销毁资源需要审核,增删改都要审核
						asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
						asynJobVDCPO.setUser_id(order.getCreatorUserId());
						asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
						asynJobVDCPO.setOperation(OperationType.DELETE_EBS);
						asynJobVDCPO.setOrder_id(saveOrderId);
					asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
					//JOB信息封装结束
					
					//用户日志记录
						String para = "templateid="+infobo.getTemplateId()+",instanceId="+infobo.geteDiskId();
						String memo = parameters;
					ConstDef.saveLogInfo(1, "产品管理-块存储job", "退定job", "VolumeOperateServiceImpl.java", "insertDestroyVolume()", para, "", memo);
					//用户日志结束
				}
				//vdc项目判断结束 
				
				//自动审核
				if (user.getRoleApproveLevel() == 4) {
					auditService.approveOrder(user.getId(), saveOrderId,
							user.getRoleApproveLevel(), 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				} else if (user.getRoleApproveLevel() < 4) {
					auditService.isAutoApproveUser(user.getId(), saveOrderId,
							user.getRoleApproveLevel()+1, 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				}
				//自动审核结束
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}
	
	public void insertDiskDestroy(TInstanceInfoBO infobo, String reason,TUserBO user, int serviceId) throws Exception {
		try {
			instanceInfoDao.update(infobo);
			TServiceInstanceBO serviceinfo = updateServiceInfoState(serviceId);
			TOrderBO order = new TOrderBO();
			order.setType(3); 
			order.setState(1); 
			 
			order.setCreatorUserId(user.getId()); 
			//order.setInstanceInfoId(infobo.getId());
			order.setServiceInstanceId(serviceId);
			order.setZoneId(infobo.getZoneId());
			order.setOrderApproveLevelState(user.getRoleApproveLevel());
			order.setState(user.getRoleApproveLevel());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss"); 
			order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000));  
			order.setReason(reason);
			int saveOrderId = orderDao.save(order);
			if (saveOrderId > 0) {
				 
				int project_switch = ConstDef.curProjectId;
				if (project_switch == 2){
 
					AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
						String parameters = ""; 
							Map<String, Object> mapJob = new HashMap<String, Object>();
 
							mapJob.put("BSID", infobo.getResCode());
							parameters = JsonUtil.getJsonString4JavaPOJO(mapJob);
						asynJobVDCPO.setParameter(parameters);
						 
						asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
						asynJobVDCPO.setUser_id(order.getCreatorUserId());
						asynJobVDCPO.setTemplate_id(infobo.getTemplateId());
						asynJobVDCPO.setOperation(OperationType.DELETE_EBS);
						asynJobVDCPO.setOrder_id(saveOrderId);
					asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
 
						String para = "templateid="+infobo.getTemplateId()+",instanceId="+infobo.geteDiskId();
						String memo = parameters;
					ConstDef.saveLogInfo(1, "产品管理-块存储job", "退定job", "VolumeOperateServiceImpl.java", "insertDestroyVolume()", para, "", memo);
 
				}
 
				if (user.getRoleApproveLevel() == 4) {
					auditService.approveOrder(user.getId(), saveOrderId,
							user.getRoleApproveLevel(), 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				} else if (user.getRoleApproveLevel() < 4) {
					auditService.isAutoApproveUser(user.getId(), saveOrderId,
							user.getRoleApproveLevel()+1, 0, "自动审批",
							user.getEmail(), 3,infobo.getTemplateType());
				}
 
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}
	
	public void insertDiskDestroy4(TInstanceInfoBO infobo, String reason,TUserBO user, int serviceId) throws Exception {
		try {
			instanceInfoDao.update(infobo);
			updateServiceInfoState4(serviceId);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}	
	
	public void insertDestroyIPSan4(TInstanceInfoBO infobo, String reason,TUserBO user, int serviceId) throws Exception {
		try {
			instanceInfoDao.update(infobo);
			updateServiceInfoState4(serviceId);
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error("insert asyncjobinfo error:" + e.getMessage());
			throw new Exception("insert asyncjobinfo error:" + e.getMessage());
		}

	}	
	
	public TServiceInstanceBO updateServiceInfoState(int serviceID) throws SCSException {
		int index = 0;
		int id = serviceID;
		TServiceInstanceBO info = null;
		if (id != 0) {
			try {
				info = instanceInfoDao.findServiceInstanceById(serviceID);
			} catch (SQLException e) { 			 
				e.printStackTrace();
			}
			if (info.getState() == 6) {
				return null;
			} else {				 				
				index = instanceInfoDao.updateServiceState(serviceID);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	} 
	
	public TServiceInstanceBO updateServiceInfoState4(int serviceID) throws SCSException {
		int index = 0;
		int id = serviceID;
		TServiceInstanceBO info = null;
		if (id != 0) {
			try {
				info = instanceInfoDao.findServiceInstanceById(serviceID);
			} catch (SQLException e) { 			 
				e.printStackTrace();
			}
			if (info.getState() == 6) {
				return null;
			} else {				 				
				index = instanceInfoDao.updateServiceState4(serviceID);
				if (index == 0) {
					return null;
				}

			}
		}
		return info;
	} 		

	public IAsyncJobInfoDAO getAsyncJobDao() {
		return asyncJobDao;
	}

	public void setAsyncJobDao(IAsyncJobInfoDAO asyncJobDao) {
		this.asyncJobDao = asyncJobDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public void setAuditService(IAuditSevice auditService) {
		this.auditService = auditService;
	}

	public IDBUserSnapshotService getDbUserSnapshotService() {
		return dbUserSnapshotService;
	}

	public void setDbUserSnapshotService(
			IDBUserSnapshotService dbUserSnapshotService) {
		this.dbUserSnapshotService = dbUserSnapshotService;
	}

	public AsyncJobVDCService getAsyncJobVDCService() {
		return asyncJobVDCService;
	}

	public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
		this.asyncJobVDCService = asyncJobVDCService;
	}

  public IVolumeHistoryDao getVolumeHistoryDao() {
    return volumeHistoryDao;
  }

  public void setVolumeHistoryDao(IVolumeHistoryDao volumeHistoryDao) {
    this.volumeHistoryDao = volumeHistoryDao;
  }

  public ICommandService getCommandService() {
    return commandService;
  }

  public void setCommandService(ICommandService commandService) {
    this.commandService = commandService;
  }
	
}
