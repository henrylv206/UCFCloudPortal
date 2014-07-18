/**
 * 2011-11-28 下午03:32:59 $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.sevice.IAuditSevice;
import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ParameterManager;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ActVM;
import com.skycloud.management.portal.front.command.res.AttachIso;
import com.skycloud.management.portal.front.command.res.CreateSnapshot;
import com.skycloud.management.portal.front.command.res.DeleteSnapshot;
import com.skycloud.management.portal.front.command.res.DeleteVM;
import com.skycloud.management.portal.front.command.res.DetachIso;
import com.skycloud.management.portal.front.command.res.ListAccounts;
import com.skycloud.management.portal.front.command.res.ListIsos;
import com.skycloud.management.portal.front.command.res.ListSnapshots;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.command.res.ListVolumes;
import com.skycloud.management.portal.front.command.res.PauseVirtualMachine;
import com.skycloud.management.portal.front.command.res.RebootVirtualMachine;
import com.skycloud.management.portal.front.command.res.RestoreVirtualMachine;
import com.skycloud.management.portal.front.command.res.StartVirtualMachine;
import com.skycloud.management.portal.front.command.res.StopVirtualMachine;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.instance.service.ICommandService;
import com.skycloud.management.portal.front.mall.dao.CloudServiceMallDao;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.INicsDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;
import com.skycloud.management.portal.front.order.service.IOrderSerivce;
import com.skycloud.management.portal.front.order.service.IProductInstanceRefService;
import com.skycloud.management.portal.front.order.service.IServiceInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.VolumeResumeVO;
import com.skycloud.management.portal.front.resources.service.VirtualMachineModifyService;
import com.skycloud.management.portal.front.resources.util.OrgOperateCommand;
import com.skycloud.management.portal.front.resources.util.ResourcesUtil;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.service.AsyncJobVDCService;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;
import com.skycloud.management.portal.webservice.databackup.service.IDBUserSnapshotService;

/**
 * @author shixq
 * @version $Revision$ $Date$
 */
public class VirtualMachineModifyServiceImpl extends OrgOperateCommand implements VirtualMachineModifyService {

  private static Log log = LogFactory.getLog(VirtualMachineModifyServiceImpl.class);

  private IProductDao productDao;

  private IOrderDao orderDao;

  private IAsyncJobInfoDAO asyncJobDao;

  private IInstanceInfoDao instanceInfoDao;

  private INicsDao nicsDao;

  private ICommandService commandService;

  private IServiceInstanceService serviceInstanceService;
  private IOrderSerivce orderService;
  private IProductInstanceRefService productInstanceRefService;

  private IAuditSevice auditService;

  private IDBUserSnapshotService dbUserSnapshotService;

  private AsyncJobVDCService asyncJobVDCService;

  private IVMTemplateDao VMTemplateDao;

  private String parameter;//

  private String operation;//

  private int id;// 虚拟机ID

  private List<TServiceInstanceBO> listVMS;

  private CloudServiceMallDao cloudServiceMallDao;

  private void approveOrder(TUserBO user, TInstanceInfoBO info, int saveOrderId) throws SCSException {
    if (saveOrderId > 0) {
      if (user.getRoleApproveLevel() == 4) {
        try {
          auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 2,
              info.getTemplateType());
        } catch (SQLException e) {
          log.error("approveOrder error:" + e.getMessage());
          throw new SCSException("approveOrder error:" + e.getMessage());
        }
      } else if (user.getRoleApproveLevel() < 4) {
        try {
          auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 2,
              info.getTemplateType());
        } catch (SQLException e) {
          log.error("approveOrder error:" + e.getMessage());
          throw new SCSException("approveOrder error:" + e.getMessage());
        }
      }
    }
  }

  private int insertAsyncJob(ResourcesModifyVO vmModifyVO) throws SCSException {
    AsyncJobInfo ajInfo = init(vmModifyVO);
    int index = 0;
    index = asyncJobDao.insertAsyncJob(ajInfo);
    return index;
  }

  private AsyncJobInfo init(ResourcesModifyVO vmModifyVO) {
    AsyncJobInfo ajInfo = new AsyncJobInfo();
    ajInfo.setAPPLY_ID(1);
    ajInfo.setINSTANCE_INFO_ID(vmModifyVO.getId());
    ajInfo.setOPERATION(operation);
    ajInfo.setPARAMETER(parameter);
    ajInfo.setJOBSTATE(1);
    return ajInfo;
  }

  @Override
  public TInstanceInfoBO updateInstanceInfoState(ResourcesModifyVO vmModifyVO) throws SCSException {
    int index = 0;
    int id = vmModifyVO.getId();
    TInstanceInfoBO info = null;
    if (id != 0) {
      info = instanceInfoDao.searchInstanceInfoByID(id);
      if (info.getState() == 6) {// to fix bug:3926
        return null;
      } else {
        info.setClusterId(info.getState());
        info.setState(6);
        index = instanceInfoDao.update(info);
        if (index == 0) {
          return null;
        }

      }
    }
    return info;
  }

  public void updateServiceInfoState4(ResourcesModifyVO vmModifyVO, int serviceID) throws SCSException {
    int instanceId = vmModifyVO.getId();
    TInstanceInfoBO instanceInfo = null;

    int serviceId = serviceID;
    // to fix bug [3864]
    if (instanceId != 0 && serviceId != 0) {
      instanceInfo = instanceInfoDao.searchInstanceInfoByID(instanceId);
      if (instanceInfo.getState() == 7) {
        instanceInfoDao.updateServiceState4(serviceId);
      } else {
        instanceInfoDao.updateServiceState(serviceID);
      }
    }
  }

  // to fix bug [3675]
  public TInstanceInfoBO updateInstanceInfoState3(ResourcesModifyVO vmModifyVO) throws SCSException {
    int index = 0;
    int id = vmModifyVO.getId();
    TInstanceInfoBO info = null;
    if (id != 0) {
      info = instanceInfoDao.searchInstanceInfoByID(id);
      if (info.getState() == 7) {// bug 0003961
        info.setClusterId(info.getState());
        info.setState(4);
        index = instanceInfoDao.update(info);
        return info;
      } else {
        info.setClusterId(info.getState());
        info.setState(3);
        index = instanceInfoDao.update(info);
        if (index == 0) {
          return null;
        }
      }
    }
    return info;
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

  public int updateInstanceTemplateId(int id, int templateId) throws SCSException {
    int index = 0;
    TInstanceInfoBO info = null;
    if (id != 0) {
      info = instanceInfoDao.searchInstanceInfoByID(id);
      info.setTemplateId(templateId);
      index = instanceInfoDao.update(info);
    }
    return index;

  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmStart()
   */
  @Override
  public String insertVMStart(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {

      if (ConstDef.curProjectId == 1) {
        StartVirtualMachine vmStart = new StartVirtualMachine();
        id = vmModifyVO.geteInstanceId();
        vmStart.setId(id);
        parameter = CommandCreateUtil.getJsonParameterStr(vmStart);// 得到json命令串
        operation = vmStart.getCOMMAND();// 获取操作命令
        // 对应ASYNCJOB表里OPERATION字段

        index = insertAsyncJob(vmModifyVO);
      }

      if (ConstDef.curProjectId == 2) {

        ActVM actVM = new ActVM();

        TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

        actVM.setVMID(String.valueOf(resCodeInfo.getResCode()));
        actVM.setActType(vmModifyVO.getActType());

        // String parameter1 =
        // CommandCreateUtil.getJsonParameterStr(actVM);

        Map<String, Object> mapJob = new HashMap<String, Object>();
        mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
        mapJob.put("actType", vmModifyVO.getActType());
        String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

        AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
        asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
        asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
        asynJobVDCPO.setOperation(OperationType.START_VM);
        asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
        asynJobVDCPO.setTemplate_id(info.getTemplateId());
        asynJobVDCPO.setParameter(parameter1);
        asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
        index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
      }

    }
    return ResourcesUtil.resultTOString(index);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmReboot()
   */
  @Override
  public String insertVMReboot(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    try {
      if (info != null) {

        if (ConstDef.curProjectId == 1) {

          RebootVirtualMachine vmReboot = new RebootVirtualMachine();
          id = vmModifyVO.geteInstanceId();
          // id = vmModifyVO.getId();
          vmReboot.setId(id);
          parameter = CommandCreateUtil.getJsonParameterStr(vmReboot);// 得到json命令串
          operation = vmReboot.getCOMMAND();// 获取操作命令

          index = insertAsyncJob(vmModifyVO);
        }

        if (ConstDef.curProjectId == 2) {

          ActVM actVM = new ActVM();

          TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

          actVM.setVMID(String.valueOf(resCodeInfo.getResCode()));
          actVM.setActType(vmModifyVO.getActType());
          // String parameter1 =
          // CommandCreateUtil.getJsonParameterStr(actVM);

          Map<String, Object> mapJob = new HashMap<String, Object>();
          mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
          mapJob.put("actType", vmModifyVO.getActType());
          String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

          AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
          asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
          asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
          asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
          asynJobVDCPO.setOperation(OperationType.RESTART_VM);
          asynJobVDCPO.setTemplate_id(info.getTemplateId());
          asynJobVDCPO.setParameter(parameter1);
          asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
          index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
        }

      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return ResourcesUtil.resultTOString(index);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmStop()
   */
  @Override
  public String insertVMStop(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {

      if (ConstDef.curProjectId == 1) {

        StopVirtualMachine vmStop = new StopVirtualMachine();
        id = vmModifyVO.geteInstanceId();
        // id = vmModifyVO.getId();
        vmStop.setId(id);
        parameter = CommandCreateUtil.getJsonParameterStr(vmStop);// 得到json命令串
        operation = vmStop.getCOMMAND();// 获取操作命令
        // 对应ASYNCJOB表里OPERATION字段

        index = insertAsyncJob(vmModifyVO);
      }

      if (ConstDef.curProjectId == 2) {

        ActVM actVM = new ActVM();

        TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

        actVM.setVMID(String.valueOf(resCodeInfo.getResCode()));
        actVM.setActType(vmModifyVO.getActType());

        // String parameter1 =
        // CommandCreateUtil.getJsonParameterStr(actVM);

        Map<String, Object> mapJob = new HashMap<String, Object>();
        mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
        mapJob.put("actType", vmModifyVO.getActType());
        String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

        AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
        asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
        asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
        asynJobVDCPO.setOperation(OperationType.STOP_VM);
        asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
        asynJobVDCPO.setTemplate_id(info.getTemplateId());
        asynJobVDCPO.setParameter(parameter1);
        asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
        index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
      }

    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertVMPause(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {
      PauseVirtualMachine vmPause = new PauseVirtualMachine();
      id = vmModifyVO.getId();
      vmPause.setId(id);
      parameter = CommandCreateUtil.getJsonParameterStr(vmPause);// 得到json命令串
      operation = vmPause.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段

      ActVM actVM = new ActVM();

      TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

      actVM.setVMID(resCodeInfo.getResCode());
      actVM.setActType(vmModifyVO.getActType());

      // String parameter1 = CommandCreateUtil.getJsonParameterStr(actVM);
      Map<String, Object> mapJob = new HashMap<String, Object>();
      mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
      mapJob.put("actType", vmModifyVO.getActType());
      String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

      AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
      asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
      asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
      asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
      asynJobVDCPO.setOperation(OperationType.PAUSE_VM);
      asynJobVDCPO.setTemplate_id(info.getTemplateId());
      asynJobVDCPO.setParameter(parameter1);
      asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
      index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);

    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertVMModify(ResourcesModifyVO vmModifyVO, TUserBO user) throws SCSException {
    // to fix bug [3545]
    TInstanceInfoBO info = updateInstanceInfoState3(vmModifyVO);
    int index = 0;
    if (info != null) {
      String templateID = vmModifyVO.getVmtemplateId();
      if (StringUtils.isBlank(templateID)) {
        return ResourcesUtil.resultTOString(index);
      }
      TTemplateVMBO vmBO = null;
      try {
        vmBO = VMTemplateDao.getTemplateById(Integer.valueOf(templateID));
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (vmBO == null) {
        return ResourcesUtil.resultTOString(index);
      }

      vmBO.setId(0);
      vmBO.setCpuNum(vmModifyVO.getCpu_num());
      vmBO.setMemorySize(vmModifyVO.getMem_size());
      vmBO.setStorageSize(vmModifyVO.getStorage_size());
      vmBO.setSpecial(1);
      int Tid = 0;
      try {
        Tid = VMTemplateDao.createTemplate_VDC(vmBO);
      } catch (Exception e1) {
        // TODO Auto-generated catch block
        e1.printStackTrace();
      }

      Product product = this.productDao.get(Integer.parseInt(vmModifyVO.getProductId()));
      product.setId(0);
      product.setTemplateId(Tid);
      int Pid = productDao.save(product);

      TOrderBO order = new TOrderBO();
      order.setType(2);// 变更申请
      order.setState(1);// 申请状态
      order.setCreatorUserId(user.getId());// 下单人ID
      order.setInstanceInfoId(vmModifyVO.getId());
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
      order.setResourceInfo(vmModifyVO.getResInfo());
      order.setReason(vmModifyVO.getApply_reason());
      order.setCpuNum(vmModifyVO.getCpu_num());
      order.setMemorySize(vmModifyVO.getMem_size());
      order.setStorageSize(vmModifyVO.getStorage_size());
      order.setTemplateId(Tid);
      order.setProductId(Pid);
      int saveOrderId = orderDao.save(order);

      approveOrder(user, info, saveOrderId);// by sxq

    //to fix bug [4893]
    //to fix bug [4921]
    //to fix bug [4937]
      //to fix bug [4789]
      //to fix bug [4948]
      //to fix bug [4940]
      //copy form hefk
      try{
		TServiceInstanceBO serviceInstanceBO = serviceInstanceService.searchServiceInstanceByInstanceInfoId(vmModifyVO.getId());
		int oldOrderId = serviceInstanceBO.getOrderId();
		int historyId = serviceInstanceBO.getId();
		//2.修改老的服务实例
		serviceInstanceBO.setHistoryState(1);//1,历史服务
		serviceInstanceService.update(serviceInstanceBO);
		TOrderBO orderBO = orderService.selectOrderByOrderId(saveOrderId);
		//3.创建新的服务实例
		serviceInstanceBO.setProductId(orderBO.getProductId());
		serviceInstanceBO.setOrderId(saveOrderId);
		serviceInstanceBO.setHistoryId(historyId);//变革前的服务
		serviceInstanceBO.setHistoryState(0);//0,新服务
		serviceInstanceBO.setProductId(Pid);
		int serviceId = serviceInstanceService.save(serviceInstanceBO);
		//4.创建新的服务资源实例关系内容
		int templateId4Edit = Integer.valueOf(vmModifyVO.getVmtemplateId());
		//to fix bug:6748
	    List<TProductInstanceInfoRefBO>  insRefList = productInstanceRefService.searchByServiceId(historyId);
		for (TProductInstanceInfoRefBO piRef:insRefList){
			piRef.setOrderId(saveOrderId);
			piRef.setServiceInstanceId(serviceId);
			piRef.setProductId(orderBO.getProductId());
			if(piRef.getInstanceInfoId()==vmModifyVO.getId()){
				piRef.setTemplateId(orderBO.getTemplateId());
			}
			productInstanceRefService.save(piRef);
		}
		//更新订单
		order = orderDao.searchOrderById(saveOrderId);
		order.setOrderId(saveOrderId);
		order.setServiceInstanceId(serviceId);
		order.setInstanceInfoId(vmModifyVO.getId());
		orderDao.update(order);
      }catch (Exception e) {
			e.printStackTrace();
      }

      //copy voer

      try {
        // if (ConstDef.curProjectId == 2) {
        // ModifyVirtualMachine vmModify = new ModifyVirtualMachine();
        //
        // int TemplateIdStart =
        // vmBO.getResourceTemplate().indexOf("<TemplateID>");
        // int TemplateIdend =
        // vmBO.getResourceTemplate().indexOf("</TemplateID>");
        // String TemplateId =
        // vmBO.getResourceTemplate().substring(TemplateIdStart + 12,
        // TemplateIdend);
        //
        // TInstanceInfoBO resCodeInfo =
        // instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());
        //
        // vmModify.setVMID(resCodeInfo.getResCode());
        // vmModify.setTemplateId(TemplateId);
        //
        // // parameter =
        // // CommandCreateUtil.getJsonParameterStr(vmModify);//
        // // 得到json命令串
        // // operation = vmModify.getCOMMAND();// 获取操作命令
        // // String parameter1 =
        // // CommandCreateUtil.getJsonParameterStr(vmModify);
        //
        // Map<String, Object> mapJob = new HashMap<String, Object>();
        // mapJob.put("ResourceTemplateID", String.valueOf(TemplateId));
        // mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
        // String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);
        //
        // AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
        // asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
        // // asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
        // asynJobVDCPO.setOrder_id(saveOrderId);
        // asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
        // asynJobVDCPO.setOperation(OperationType.UPDATE_VM);
        // asynJobVDCPO.setTemplate_res_id(TemplateId);
        // asynJobVDCPO.setTemplate_id(info.getTemplateId());
        // asynJobVDCPO.setParameter(parameter1);
        // asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
        // index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
        // } else {
        // index = saveOrderId;// bug 0003785
        // }

        index = saveOrderId;

      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertOMModify(ResourcesModifyVO vmModifyVO, TUserBO user) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {
      String templateID = vmModifyVO.getVmtemplateId();
      if (StringUtils.isBlank(templateID)) {
        return ResourcesUtil.resultTOString(index);
      }
      TTemplateVMBO vmBO = null;
      try {
        vmBO = VMTemplateDao.getTemplateById(Integer.valueOf(templateID));
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (vmBO == null) {
        return ResourcesUtil.resultTOString(index);
      }
      TOrderBO order = new TOrderBO();
      order.setType(2);
      order.setState(1);
      order.setCreatorUserId(user.getId());
      order.setInstanceInfoId(vmModifyVO.getId());
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000));
      order.setResourceInfo(vmModifyVO.getResInfo());
      order.setReason(vmModifyVO.getApply_reason());
      order.setStorageSize(vmBO.getStorageSize());
      int saveOrderId = orderDao.save(order);

      approveOrder(user, info, saveOrderId);

      // int newTemplateId =
      // updateInstanceTemplateId(vmModifyVO.getId(),Integer.valueOf(vmModifyVO.getVmtemplateId()));

    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertDISKModify(ResourcesModifyVO vmModifyVO, TUserBO user) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState3(vmModifyVO);
    int index = 0;
    if (info != null) {
      String templateID = String.valueOf(info.getTemplateId());
      if (StringUtils.isBlank(templateID)) {
        return ResourcesUtil.resultTOString(index);
      }
      TTemplateVMBO vmBO = null;
      try {
        vmBO = VMTemplateDao.getTemplateById(info.getTemplateId());
      } catch (Exception e) {
        e.printStackTrace();
      }
      if (vmBO == null) {
        return ResourcesUtil.resultTOString(index);
      }

      vmBO.setId(0);
      vmBO.setStorageSize(vmModifyVO.getStorage_size());
      vmBO.setSpecial(1);
      int Tid = 0;
      try {
        Tid = VMTemplateDao.createTemplate_VDC(vmBO);
      } catch (Exception e1) {
        e1.printStackTrace();
      }

      Product product = this.productDao.get(info.getProductId());
      product.setId(0);
      product.setTemplateId(Tid);
      int Pid = productDao.save(product);

      TOrderBO order = new TOrderBO();
      order.setType(2);
      order.setState(1);
      order.setCreatorUserId(user.getId());
      order.setInstanceInfoId(vmModifyVO.getId());
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000));
      order.setResourceInfo(vmModifyVO.getResInfo());
      order.setReason(vmModifyVO.getApply_reason());
      order.setStorageSize(vmBO.getStorageSize());
      order.setTemplateId(Tid);
      order.setProductId(Pid);
      int saveOrderId = orderDao.save(order);

      approveOrder(user, info, saveOrderId);

      //copy form hefk
      try{
    TServiceInstanceBO serviceInstanceBO = serviceInstanceService.searchServiceInstanceByInstanceInfoId(vmModifyVO.getId());
    int oldOrderId = serviceInstanceBO.getOrderId();
    int historyId = serviceInstanceBO.getId();
    //2.修改老的服务实例
    serviceInstanceBO.setHistoryState(1);//1,历史服务
    serviceInstanceService.update(serviceInstanceBO);
    TOrderBO orderBO = orderService.selectOrderByOrderId(saveOrderId);
    //3.创建新的服务实例
    serviceInstanceBO.setProductId(orderBO.getProductId());
    serviceInstanceBO.setOrderId(saveOrderId);
    serviceInstanceBO.setHistoryId(historyId);//变革前的服务
    serviceInstanceBO.setHistoryState(0);//0,新服务
    serviceInstanceBO.setProductId(Pid);
    int serviceId = serviceInstanceService.save(serviceInstanceBO);
    //4.创建新的服务资源实例关系内容
    int templateId4Edit = Integer.valueOf(templateID);
// to fix bug:6748   
    List<TProductInstanceInfoRefBO>  insRefList = productInstanceRefService.searchByServiceId(historyId);
    for (TProductInstanceInfoRefBO piRef:insRefList){
      piRef.setOrderId(saveOrderId);
      piRef.setServiceInstanceId(serviceId);
      piRef.setProductId(orderBO.getProductId());
      if(piRef.getInstanceInfoId()==vmModifyVO.getId()){
         piRef.setTemplateId(orderBO.getTemplateId());
      }
      productInstanceRefService.save(piRef);
    }
    //更新订单
    order = orderDao.searchOrderById(saveOrderId);
    order.setOrderId(saveOrderId);
    order.setServiceInstanceId(serviceId);
    order.setInstanceInfoId(vmModifyVO.getId());
    orderDao.update(order);
      }catch (Exception e) {
      e.printStackTrace();
      }
      // int newTemplateId =
      // updateInstanceTemplateId(vmModifyVO.getId(),Integer.valueOf(vmModifyVO.getVmtemplateId()));

    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertVMRestore(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {
      RestoreVirtualMachine vmRestore = new RestoreVirtualMachine();
      id = vmModifyVO.getId();
      vmRestore.setId(id);
      parameter = CommandCreateUtil.getJsonParameterStr(vmRestore);// 得到json命令串
      operation = vmRestore.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段

      ActVM actVM = new ActVM();

      TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

      actVM.setVMID(resCodeInfo.getResCode());
      actVM.setActType(vmModifyVO.getActType());

      // String parameter1 = CommandCreateUtil.getJsonParameterStr(actVM);

      Map<String, Object> mapJob = new HashMap<String, Object>();
      mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
      mapJob.put("actType", vmModifyVO.getActType());
      String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

      AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
      asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
      asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
      asynJobVDCPO.setOperation(OperationType.RESTORE_VM);
      asynJobVDCPO.setTemplate_id(info.getTemplateId());
      asynJobVDCPO.setParameter(parameter1);
      asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
      index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
    }
    return ResourcesUtil.resultTOString(index);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmMountIOS()
   */
  @Override
  public String insertVMMountIOS(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {
      AttachIso attachiso = new AttachIso();
      attachiso.setId(vmModifyVO.getIsoId());
      attachiso.setVirtualmachineid(vmModifyVO.geteInstanceId());
      parameter = CommandCreateUtil.getJsonParameterStr(attachiso);// 得到json命令串
      operation = attachiso.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段
      index = insertAsyncJob(vmModifyVO);
    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String listCommunityIso(int resourcePoolsId) throws SCSException {

    ListIsos listisos = new ListIsos();
    listisos.setIsofilter("community");
    try {
      Object response = commandService.executeAndJsonReturn(listisos, resourcePoolsId);
      JSONObject jsonRes = JSONObject.fromObject(response);
      JSONObject jsonObject = jsonRes.getJSONObject("listisosresponse");

      if (!jsonObject.isEmpty()) {
        return jsonObject.toString();
      }
    } catch (Exception e) {
      ResourcesUtil.resultTOString(0);
    }
    return ResourcesUtil.resultTOString(0);
  }

  @Override
  public String insertDetachIso(ResourcesModifyVO vmModifyVO) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
    int index = 0;
    if (info != null) {
      DetachIso detachiso = new DetachIso();
      detachiso.setVirtualmachineid(vmModifyVO.geteInstanceId());
      parameter = CommandCreateUtil.getJsonParameterStr(detachiso);// 得到json命令串
      operation = detachiso.getCOMMAND();// 获取操作命令 对应ASYNCJOB表里OPERATION字段
      index = insertAsyncJob(vmModifyVO);
    }
    return ResourcesUtil.resultTOString(index);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmVolumeSnapshot()
   */
  @Override
  public String insertVMVolumeSnapshot(ResourcesModifyVO vmModifyVO, int resourcePoolsId) {

    ListVolumes listvolumes = new ListVolumes();
    listvolumes.setVirtualmachineid(String.valueOf(vmModifyVO.geteInstanceId()));
    try {
      TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
      int index = 0;
      if (info != null) {
        if (ConstDef.curProjectId == 1) {
          Object response = commandService.executeAndJsonReturn(listvolumes, resourcePoolsId);
          JSONObject jsonRes = JSONObject.fromObject(response);
          JSONObject jsonObject = jsonRes.getJSONObject("listvolumesresponse");
          // 通过虚拟机id查找虚拟机对应root磁盘

          if (!jsonObject.isEmpty()) {
            for (int i = 0; i < jsonObject.getJSONArray("volume").size(); i++) {
              JSONObject jsonVolume = jsonObject.getJSONArray("volume").getJSONObject(i);
              if (jsonVolume.containsKey("id") && jsonVolume.getString("type").equals("ROOT")) {
                Integer volumeId = jsonVolume.getInt("id");
                index = this.insertCreateSnapshotCommand(volumeId, vmModifyVO, resourcePoolsId);
                return ResourcesUtil.resultTOString(index);
              }
            }
          }
        } else if (ConstDef.curProjectId == 2) {
          Integer volumeId = 0;
          index = this.insertCreateSnapshotCommand(volumeId, vmModifyVO, resourcePoolsId);
          return ResourcesUtil.resultTOString(index);
        }

      }

    } catch (Exception e) {
      return ResourcesUtil.resultTOString(0);
    }
    return ResourcesUtil.resultTOString(0);
  }

  @Override
  public List<VolumeResumeVO> queryVMRootSnapshotById(ResourcesModifyVO vmModifyVO, int resourcePoolsId) throws SCSException {

    List<UserSnapshot> UserSnapshotList = dbUserSnapshotService.queryTpSnapshotListByIntanceInfoId(vmModifyVO.getId());
    List<VolumeResumeVO> vrList = new ArrayList<VolumeResumeVO>();

    if (ConstDef.curProjectId == 1) {

      for (UserSnapshot userSnapshot : UserSnapshotList) {

        VolumeResumeVO volumeVo = new VolumeResumeVO();
        volumeVo.setInstanceId(userSnapshot.getINSTANCE_INFO_ID());
        volumeVo.setSnapshotId(userSnapshot.getE_SNAPSHOT_ID() + "");

        ListSnapshots listsnapshots = new ListSnapshots();
        listsnapshots.setId(userSnapshot.getE_SNAPSHOT_ID());
        Object response = commandService.executeAndJsonReturn(listsnapshots, resourcePoolsId);
        JSONObject jsonRes = JSONObject.fromObject(response);
        JSONObject jsonObject = jsonRes.getJSONObject("listsnapshotsresponse");
        if (jsonObject.isEmpty()) {
        } else {
          JSONObject jsonVolume = jsonObject.getJSONArray("snapshot").getJSONObject(0);
          volumeVo.setName(jsonVolume.getString("name"));
        }

        vrList.add(volumeVo);
      }
    } else if (ConstDef.curProjectId == 2) {
      for (UserSnapshot userSnapshot : UserSnapshotList) {

        VolumeResumeVO volumeVo = new VolumeResumeVO();
        volumeVo.setInstanceId(userSnapshot.getINSTANCE_INFO_ID());
        volumeVo.setSnapshotId(userSnapshot.getID() + "");

        ListSnapshots listsnapshots = new ListSnapshots();
        listsnapshots.setId(userSnapshot.getE_SNAPSHOT_ID());

        volumeVo.setName(String.valueOf(userSnapshot.getVM_BACKUP_ID()));

        vrList.add(volumeVo);
      }
    }

    return vrList;
    // if (vrList != null) {
    //
    // }else{
    // return null;
    // }
    //

    // ListVolumes listvolumes = new ListVolumes();
    // listvolumes.setVirtualmachineid(String.valueOf(vmModifyVO
    // .geteInstanceId()));
    // Object response = commandService.executeAndJsonReturn(listvolumes);
    // JSONObject jsonRes = JSONObject.fromObject(response);
    // JSONObject jsonObject = jsonRes.getJSONObject("listvolumesresponse");
    // // 通过虚拟机id查找虚拟机对应root磁盘
    // if (!jsonObject.isEmpty()) {
    // for (int i = 0; i < jsonObject.getJSONArray("volume").size(); i++) {
    // JSONObject jsonVolume = jsonObject.getJSONArray("volume")
    // .getJSONObject(i);
    // if (jsonVolume.containsKey("id")
    // && jsonVolume.getString("type").equals("ROOT")) {
    // Integer volumeId = jsonVolume.getInt("id");
    //
    // List<VolumeResumeVO> vrList = this.listSnapShot(volumeId);
    // if (vrList != null) {
    // return vrList;
    // }
    // }
    // }
    // }

  }

  /**
   * 判断创建快照是否需要执行删除快照 如果需要删除返回快照id
   *
   * @param volumeId
   * @return
   */
  // private List<VolumeResumeVO> listSnapShot(int volumeId) {
  // List<VolumeResumeVO> vrList = new ArrayList<VolumeResumeVO>();
  // ListSnapshots listsnapshots = new ListSnapshots();
  // listsnapshots.setVolumeid(String.valueOf(volumeId));
  // Object response = commandService.executeAndJsonReturn(listsnapshots);
  // JSONObject jsonRes = JSONObject.fromObject(response);
  // JSONObject jsonObject = jsonRes.getJSONObject("listsnapshotsresponse");
  // if (jsonObject.isEmpty()) {
  // return null;
  // } else {
  //
  // for (int i = 0; i < jsonObject.getJSONArray("snapshot").size(); i++) {
  // VolumeResumeVO vrvo = new VolumeResumeVO();
  // vrvo.setSnapshotId(jsonObject.getJSONArray("snapshot")
  // .getJSONObject(i).getString("id"));
  // vrvo.setName(jsonObject.getJSONArray("snapshot")
  // .getJSONObject(i).getString("name"));
  // vrList.add(vrvo);
  // }
  // return vrList;
  // }
  // }

  /*
   * 组织创建快照命令插入到async表中
   */
  private int insertCreateSnapshotCommand(int volumeId, ResourcesModifyVO vmModifyVO, int resourcePoolsId) throws Exception {

    // to fix bug 1726
    CreateSnapshot createsnapshot = new CreateSnapshot();
    createsnapshot.setVolumeid(volumeId);

    TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

    // to fix bug [2275]
    parameter = CommandCreateUtil.getJsonParameterStr(createsnapshot);// 得到json命令串
    operation = createsnapshot.getCOMMAND();// 获取操作命令

    // 对应ASYNCJOB表里OPERATION字段
    int index = 0;
    try {

      // to fix bug [2273]
      if (ConstDef.curProjectId == 1) {

        String osTypeId = "";

        UserSnapshot userSnapshot = dbUserSnapshotService.querySnapshotByuser(vmModifyVO.getUserID(), vmModifyVO.getId());
        if (userSnapshot != null) {
          osTypeId = String.valueOf(userSnapshot.getOS_TYPE_ID());
        }

        index = insertAsyncJob(vmModifyVO);

        UserSnapshot snapshot = new UserSnapshot();
        snapshot.setCREATE_USER_ID(vmModifyVO.getUserID());
        snapshot.setINSTANCE_INFO_ID(vmModifyVO.getId());
        snapshot.setSTORAGE_SIZE(vmModifyVO.getStorage_size());
        snapshot.setCOMMENT("1");
        snapshot.setTYPE(1);
        snapshot.setASYN_ID(index);
        snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());

        // to fix bug [2652]
        if (userSnapshot == null) {
          ListVirtualMachines listvm = new ListVirtualMachines();
          listvm.setId(vmModifyVO.geteInstanceId());
          Object response = commandService.executeAndJsonReturn(listvm, resourcePoolsId);
          JSONObject jsonRes = JSONObject.fromObject(response);
          JSONObject jsonObject = jsonRes.getJSONObject("listvirtualmachinesresponse");
          String templateId = jsonObject.getJSONArray("virtualmachine").getJSONObject(0).getString("templateid");
          ListTemplates listTemp = new ListTemplates();
          listTemp.setTemplateFilter("community");
          listTemp.setId(templateId);
          Object tempResponse = commandService.executeAndJsonReturn(listTemp, resourcePoolsId);
          JSONObject tempJsonRes = JSONObject.fromObject(tempResponse);
          JSONObject templJsonObject = tempJsonRes.getJSONObject("listtemplatesresponse");
          osTypeId = templJsonObject.getJSONArray("template").getJSONObject(0).getString("ostypeid");
          snapshot.setOS_TYPE_ID(Integer.valueOf(osTypeId));
        } else {
          snapshot.setOS_TYPE_ID(Integer.valueOf(osTypeId));
        }

         dbUserSnapshotService.insertIntoSnapshot(snapshot);

      }

      if (ConstDef.curProjectId == 2) {

        // CreateBackup createBackup = new CreateBackup();
        // createBackup.setVMID(resCodeInfo.getResCode());
        // String parameter1 =
        // CommandCreateUtil.getJsonParameterStr(createBackup);

        UserSnapshot snapshot = new UserSnapshot();
        snapshot.setCREATE_USER_ID(vmModifyVO.getUserID());
        snapshot.setINSTANCE_INFO_ID(vmModifyVO.getId());
        snapshot.setSTORAGE_SIZE(vmModifyVO.getStorage_size());
        snapshot.setCOMMENT("1");
        // 系统盘标识
        snapshot.setTYPE(1);
        snapshot.setASYN_ID(index);
        snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
        int snapshotId = dbUserSnapshotService.insertIntoSnapshot(snapshot);

        Map<String, Object> mapJob = new HashMap<String, Object>();
        mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
        String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

        AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
        asynJobVDCPO.setUser_id(vmModifyVO.getUserID());
        asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
        asynJobVDCPO.setOperation(OperationType.CREATE_VMBAK);
        asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
        asynJobVDCPO.setParameter(parameter1);
        asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);

        asynJobVDCPO.setInstance_info_iri_id(snapshotId);

        index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
      }

    } catch (SCSException e) {
      // log.error("insert asyncjobinfo error:" + e.getMessage());
      throw new Exception("insert asyncjobinfo error:" + e.getMessage());
    }
    return index;
  }

  /*
   * 组织删除快照命令插入到async表中
   */
  private int insertDeleteSnapshotCommand(String snapshotId, ResourcesModifyVO vmModifyVO, int userId, String res_code) throws Exception {

    Map<String, Map<String, Object>> map = new HashMap<String, Map<String, Object>>();
    Map<String, Object> subMap = new HashMap<String, Object>();
    subMap.put(DeleteSnapshot.ID, snapshotId);
    map.put(DeleteSnapshot.COMMAND, subMap);
    // to fix bug [1781]
    // to fix bug [2452]
    parameter = JsonUtil.getJsonString4JavaPOJO(map);
    operation = DeleteSnapshot.COMMAND;

    // 对应ASYNCJOB表里OPERATION字段
    int index = 0;
    try {

      if (ConstDef.curProjectId == 1) {

        index = insertAsyncJob(vmModifyVO);
      }

      if (ConstDef.curProjectId == 2) {

        TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vmModifyVO.getId());

        // DeleteBackup deleteBackup = new DeleteBackup();
        // deleteBackup.setVMBackupId(snapshotId);
        //
        // String parameter1 =
        // CommandCreateUtil.getJsonParameterStr(deleteBackup);
        UserSnapshot userSnapshot = dbUserSnapshotService.queryTpSnapshotBySnapshotId(Integer.parseInt(snapshotId));

        Map<String, Object> mapJob = new HashMap<String, Object>();
        mapJob.put("VMBackupId", String.valueOf(userSnapshot.getVM_BACKUP_ID()));
        String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

        AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
        asynJobVDCPO.setUser_id(userId);
        asynJobVDCPO.setInstance_info_id(vmModifyVO.getId());
        asynJobVDCPO.setOperation(OperationType.DELETE_VMBAK);
        asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
        asynJobVDCPO.setParameter(parameter1);
        asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
        index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
      }

      dbUserSnapshotService.deleteTpSnapshotBySnapshotId(Integer.parseInt(snapshotId));
    } catch (SCSException e) {
      // log.error("insert asyncjobinfo error:" + e.getMessage());
      throw new Exception("insert asyncjobinfo error:" + e.getMessage());
    }
    return index;
  }

  private int getDomainByAccount(String account, int resourcePoolsId) {
    // to fix bug [2590]
    ListAccounts domainAction = new ListAccounts();
    // to fix bug [2775]
    // domainAction.setAccount(account);
    domainAction.setName(account);
    Object response = commandService.executeAndJsonReturn(domainAction, resourcePoolsId);
    JSONObject jsonRes = JSONObject.fromObject(response);
    String result = jsonRes.getString("listaccountsresponse");
    JSONObject resultObject = JSONObject.fromObject(result);
    if (resultObject.containsKey("account")) {
      JSONObject domain = resultObject.getJSONArray("account").getJSONObject(0);
      return domain.getInt("domainid");
    }
    return -1;
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmSnapshotResume()
   */
  @Override
  public String insertVMSnapshotResume(VolumeResumeVO volumeResumeVO, TUserBO user, String res_code, int resourcePoolsId) {
    ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
    vmModifyVO.setId(volumeResumeVO.getInstanceId());

    List<AsyncJobInfo> asyncList = new ArrayList<AsyncJobInfo>();
    try {
      TInstanceInfoBO info = updateInstanceInfoState(vmModifyVO);
      if (info != null) {

        // asyncList.addAll(this.orgRebootCommand(nicsInfo, info));

        try {

          if (ConstDef.curProjectId == 1) {

            getTemplateOsTypeToVolumeVO(volumeResumeVO, info);
            asyncList.add(this.orgCreateTemplate(volumeResumeVO));
            // TInstanceInfoBO instanceInfoBO = instanceInfoDao
            // .searchInstanceInfoByID(volumeResumeVO.getInstanceId());

            List<TNicsBO> nicsInfo = nicsDao.searchNicssByInstanceId(info.getId());
            info.setNicsBOs(nicsInfo);
            asyncList.add(this.orgDestroyVirturlMachines(info));
            asyncList.add(this.orgupdateIriRecordState(info));
            int intDomainId = this.getDomainByAccount(user.getAccount(), resourcePoolsId);
            // to fix bug [1838]
            asyncList.addAll(this.orgDeployVirturlMachines(info, user.getAccount(), intDomainId));
            asyncList.add(this.orgDeleteTemplate(volumeResumeVO));
            asyncJobDao.updatebatchAsyncJobInfo(asyncList);
          }

          if (ConstDef.curProjectId == 2) {

            TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(volumeResumeVO.getInstanceId());

            // RestoreBackup restoreBackup = new RestoreBackup();
            // restoreBackup.setVMBackupId(volumeResumeVO.getSnapshotId());
            //
            // String parameter1 =
            // CommandCreateUtil.getJsonParameterStr(restoreBackup);
            UserSnapshot userSnapshot = dbUserSnapshotService.queryTpSnapshotBySnapshotId(Integer.parseInt(volumeResumeVO.getSnapshotId()));
            Map<String, Object> mapJob = new HashMap<String, Object>();
            mapJob.put("VMBackupId", String.valueOf(userSnapshot.getVM_BACKUP_ID()));
            String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

            AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
            asynJobVDCPO.setUser_id(user.getId());
            asynJobVDCPO.setOrder_id(resCodeInfo.getOrderId());
            asynJobVDCPO.setInstance_info_id(volumeResumeVO.getInstanceId());
            asynJobVDCPO.setOperation(OperationType.RESTORE_VMBAK);
            asynJobVDCPO.setParameter(parameter1);
            asynJobVDCPO.setAuditstate(AuditStateVDC.NO_AUDIT);
            asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
          }

          // asyncJobDao.updatebatchAsyncJobInfo(asyncList);
          return ResourcesUtil.resultTOString(asyncList.size());
        } catch (Exception e) {
          log.error("insert revome Snapshot Command error:" + e.getMessage());
          return ResourcesUtil.resultTOString(0);
        }
      }

    } catch (SCSException e) {
      log.error("qurey instanceInfo error:" + e.getMessage());
      e.printStackTrace();
    }

    return ResourcesUtil.resultTOString(0);
  }

  /**
   * 2012.3.22vm恢复添加通过vm查找相应ostype功能
   *
   * @param volumeResumeVO
   * @param info
   *          创建人： 冯永凯 创建时间：2012-3-21 下午05:35:16
   */
  private void getTemplateOsTypeToVolumeVO(VolumeResumeVO volumeResumeVO, TInstanceInfoBO info) {
    // ListVirtualMachines listvm = new ListVirtualMachines();
    // listvm.setId(info.geteInstanceId());
    // Object response = commandService.executeAndJsonReturn(listvm);
    // JSONObject jsonRes = JSONObject.fromObject(response);
    // JSONObject jsonObject =
    // jsonRes.getJSONObject("listvirtualmachinesresponse");
    // String templateId =
    // jsonObject.getJSONArray("virtualmachine").getJSONObject(0).getString("templateid");
    // ListTemplates listTemp = new ListTemplates();
    // listTemp.setTemplateFilter("community");
    // listTemp.setId(templateId);
    // Object tempResponse = commandService.executeAndJsonReturn(listTemp);
    // JSONObject tempJsonRes = JSONObject.fromObject(tempResponse);
    // JSONObject templJsonObject =
    // tempJsonRes.getJSONObject("listtemplatesresponse");
    // String osTypeId =
    // templJsonObject.getJSONArray("template").getJSONObject(0).getString("ostypeid");

    UserSnapshot userSnapshot = dbUserSnapshotService.querySnapshotId(Integer.parseInt(volumeResumeVO.getSnapshotId()));
    String osTypeId = String.valueOf(userSnapshot.getOS_TYPE_ID());

    volumeResumeVO.setOsTypeId(osTypeId);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmApplyUpdate()
   */
  @Override
  public String insertVMApplyUpdate(ResourcesModifyVO vo, TUserBO user) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vo);
    int index = 0;
    if (info != null) {
      TOrderBO order = new TOrderBO();
      order.setType(2);// 修改申请
      order.setState(1);// 申请状态
      order.setCreatorUserId(user.getId());// 下单人ID
      order.setCpuNum(vo.getCpu_num());
      order.setMemorySize(vo.getMem_size());
      order.setInstanceInfoId(vo.getId());
      order.setZoneId(info.getZoneId());
      order.setClusterId(info.getState()); // clusterId中保存原instance状态
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
      // 日期+6位随机数
      order.setReason(vo.getApply_reason());
      int saveOrderId = orderDao.save(order);

      approveOrder(user, info, saveOrderId);
    }
    return ResourcesUtil.resultTOString(index);
  }

  /*
   * (non-Javadoc)
   *
   * @see com.skycloud.management.portal.front.resources.service.
   * VirtualMachineModifyService#vmAapplyDestroy()
   */
  @Override
  public String insertVMAapplyDestroy(ResourcesModifyVO vo, TUserBO user) throws SCSException {
    TInstanceInfoBO info = updateInstanceInfoState(vo);

    int index = 0;
    if (info != null) {
      TOrderBO order = new TOrderBO();
      order.setType(3);// 作废申请
      order.setState(1);// 申请状态
      order.setClusterId(info.getState()); // clusterId中保存原instance状态
      order.setCreatorUserId(user.getId());// 下单人ID
      order.setInstanceInfoId(vo.getId());
      order.setZoneId(info.getZoneId());
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
      // 日期+6位随机数
      order.setReason(vo.getApply_reason());
      int saveOrderId = orderDao.save(order);

      // deleteVM

      if (saveOrderId > 0) {

        if (ConstDef.curProjectId == 2) {
          DeleteVM vmDelete = new DeleteVM();

          TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vo.getId());

          vmDelete.setVMID(resCodeInfo.getResCode());
          // parameter =
          // CommandCreateUtil.getJsonParameterStr(vmDelete);
          // operation = vmDelete.getCOMMAND();

          Map<String, Object> mapJob = new HashMap<String, Object>();
          mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
          String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

          AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
          asynJobVDCPO.setOrder_id(saveOrderId);
          asynJobVDCPO.setUser_id(vo.getUserID());
          asynJobVDCPO.setInstance_info_id(vo.getId());
          asynJobVDCPO.setOrder_id(saveOrderId);
          asynJobVDCPO.setOperation(OperationType.DELETE_VM);
          asynJobVDCPO.setTemplate_id(info.getTemplateId());
          asynJobVDCPO.setParameter(parameter1);
          asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
          index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
        }

        index = saveOrderId;
        if (user.getRoleApproveLevel() == 4) {
          try {
            auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 3,
                info.getTemplateType());
          } catch (SQLException e) {
            log.error("approveOrder error:" + e.getMessage());
            throw new SCSException("approveOrder error:" + e.getMessage());
          }
        } else if (user.getRoleApproveLevel() < 4) {
          try {
            auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 3,
                info.getTemplateType());
          } catch (SQLException e) {
            log.error("approveOrder error:" + e.getMessage());
            throw new SCSException("approveOrder error:" + e.getMessage());
          }
        }
      }
    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertVMDestroy(ResourcesModifyVO vo, TUserBO user, int serviceID) throws SCSException {
    // to fix bug [3703]
    // to fix bug [3724]
    updateServiceInfoState4(vo, serviceID);
    TInstanceInfoBO info = updateInstanceInfoState3(vo);

    int index = 0;
    if (info != null) {
      if (info.getState() != 4) {
        TOrderBO order = new TOrderBO();
        order.setType(3);// 作废申请
        order.setState(1);// 申请状态
        order.setClusterId(info.getState()); // clusterId中保存原instance状态
        order.setCreatorUserId(user.getId());// 下单人ID
        // order.setInstanceInfoId(vo.getId());
        order.setZoneId(info.getZoneId());
        order.setOrderApproveLevelState(user.getRoleApproveLevel());
        order.setState(user.getRoleApproveLevel());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
        order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
        order.setServiceInstanceId(serviceID);
        order.setReason(vo.getApply_reason());

        int saveOrderId = orderDao.save(order);

        if (saveOrderId > 0) {

          if (ConstDef.curProjectId == 2) {
            DeleteVM vmDelete = new DeleteVM();

            TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vo.getId());

            vmDelete.setVMID(resCodeInfo.getResCode());
            // parameter =
            // CommandCreateUtil.getJsonParameterStr(vmDelete);
            // operation = vmDelete.getCOMMAND();

            Map<String, Object> mapJob = new HashMap<String, Object>();
            mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
            String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

            AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
            asynJobVDCPO.setOrder_id(saveOrderId);
            asynJobVDCPO.setUser_id(vo.getUserID());
            asynJobVDCPO.setInstance_info_id(vo.getId());
            asynJobVDCPO.setOrder_id(saveOrderId);
            asynJobVDCPO.setOperation(OperationType.DELETE_VM);
            asynJobVDCPO.setTemplate_id(info.getTemplateId());
            asynJobVDCPO.setParameter(parameter1);
            asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
            index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
          }

          index = saveOrderId;
          if (user.getRoleApproveLevel() == 4) {
            try {
              auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 3,
                  info.getTemplateType());
            } catch (SQLException e) {
              log.error("approveOrder error:" + e.getMessage());
              throw new SCSException("approveOrder error:" + e.getMessage());
            }
          } else if (user.getRoleApproveLevel() < 4) {
            try {
              auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 3,
                  info.getTemplateType());
            } catch (SQLException e) {
              log.error("approveOrder error:" + e.getMessage());
              throw new SCSException("approveOrder error:" + e.getMessage());
            }
          }
        }
      } else {// bug 0003945 退订不可用vm时，直接作废的也属于操作成功!
        index = 1;
      }
    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public String insertVMSDestroy(ResourcesModifyVO vo, TUserBO user, int serviceID) throws SCSException {

    // to fix bug [3826]

    TInstanceInfoBO info = null;
    // to fix bug [3920]
    boolean createOrder = false;
    try {
      listVMS = cloudServiceMallDao.getQuitService(user, serviceID);

      for (int i = 0; i < listVMS.size(); i++) {
        vo.setId(Integer.valueOf(listVMS.get(i).getVmid()));
        vo.setClusterId(Integer.valueOf(listVMS.get(i).getClusterId()));
        vo.seteInstanceId(Integer.valueOf(listVMS.get(i).geteInstanceId()));
        vo.setRes_code(listVMS.get(i).geteInstanceId());
        info = updateInstanceInfoState3(vo);
        if (info != null && info.getState() != 4) {// bug 0003961
          createOrder = true;
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (createOrder) {
      instanceInfoDao.updateServiceState(serviceID);
    } else {
      instanceInfoDao.updateServiceState4(serviceID);
    }

    int index = 0;
    if (createOrder) {
      TOrderBO order = new TOrderBO();
      order.setType(3);// 作废申请
      order.setState(1);// 申请状态
      // order.setClusterId(info.getState()); // clusterId中保存原instance状态
      order.setCreatorUserId(user.getId());// 下单人ID
      // order.setInstanceInfoId(vo.getId());
      // order.setZoneId(info.getZoneId());
      order.setOrderApproveLevelState(user.getRoleApproveLevel());
      order.setState(user.getRoleApproveLevel());
      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");// 订单编码
      order.setOrderCode(sdf.format(new java.util.Date()) + (int) ((Math.random() * 9 + 1) * 100000)); // 订单编号
      order.setServiceInstanceId(serviceID);
      order.setReason(vo.getApply_reason());

      int saveOrderId = orderDao.save(order);

      if (saveOrderId > 0) {

        if (ConstDef.curProjectId == 2) {
          DeleteVM vmDelete = new DeleteVM();

          TInstanceInfoBO resCodeInfo = instanceInfoDao.searchInstanceInfoByID(vo.getId());

          vmDelete.setVMID(resCodeInfo.getResCode());
          // parameter =
          // CommandCreateUtil.getJsonParameterStr(vmDelete);
          // operation = vmDelete.getCOMMAND();

          Map<String, Object> mapJob = new HashMap<String, Object>();
          mapJob.put("VMID", String.valueOf(resCodeInfo.getResCode()));
          String parameter1 = JsonUtil.getJsonString4JavaPOJO(mapJob);

          AsyncJobVDCPO asynJobVDCPO = new AsyncJobVDCPO();
          asynJobVDCPO.setOrder_id(saveOrderId);
          asynJobVDCPO.setUser_id(vo.getUserID());
          asynJobVDCPO.setInstance_info_id(vo.getId());
          asynJobVDCPO.setOrder_id(saveOrderId);
          asynJobVDCPO.setOperation(OperationType.DELETE_VM);
          asynJobVDCPO.setTemplate_id(info.getTemplateId());
          asynJobVDCPO.setParameter(parameter1);
          asynJobVDCPO.setAuditstate(AuditStateVDC.WAIT_AUDIT);
          index = asyncJobVDCService.insterAsyncJobVDC(asynJobVDCPO);
        }

        index = saveOrderId;
        if (user.getRoleApproveLevel() == 4) {
          try {
            auditService.approveOrder(user.getId(), saveOrderId, user.getRoleApproveLevel(), 0, "自动审批", user.getEmail(), 3,
                info.getTemplateType());
          } catch (SQLException e) {
            log.error("approveOrder error:" + e.getMessage());
            throw new SCSException("approveOrder error:" + e.getMessage());
          }
        } else if (user.getRoleApproveLevel() < 4) {
          try {
            auditService.isAutoApproveUser(user.getId(), saveOrderId, user.getRoleApproveLevel() + 1, 0, "自动审批", user.getEmail(), 3, 1);
          } catch (SQLException e) {
            log.error("approveOrder error:" + e.getMessage());
            throw new SCSException("approveOrder error:" + e.getMessage());
          }
        }
      }
    }
    return ResourcesUtil.resultTOString(index);
  }

  @Override
  public TTemplateVMBO creatSpecalVMTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
    //
    template.setCode(null);// 模板编码
    template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);// 特殊模板
                                                             // 0：管理员定义的模板，1：用户定义的特殊模板
    template.setCpufrequency(Float.parseFloat(ParameterManager.getInstance().getValue(ConstDef.COMBOX_CPUHZ_GHZ)));
    template.setCpuNum(vminfo.getCpuNum());
    template.setMemorySize(vminfo.getMemorySize());
    vminfo.setDisknumber(0);
    template.setStorageSize(vminfo.getStorageSize());
    template.setVethAdaptorNum(vminfo.getVethAdaptorNum());
    if (user != null) {
      template.setCreatorUserId(user.getId());
    }
    template.setZoneId(vminfo.getZoneId());
    template.setResourcePoolsId(vminfo.getPoolId());
    template.setType(vminfo.getTemplateType());
    template.setOperType(1);
    Timestamp ts = new Timestamp(new Date().getTime());
    template.setCreateTime(ts.toString());
    template.setMeasureMode("Duration");
    template.setState(1);
    template.seteOsId(vminfo.getOsId());
    template.setVmos(vminfo.getVmos());
    template.setStoreType(vminfo.getStoreType());
    template.setExtendAttrJSON(vminfo.getExtendAttrJson());

    product.setName("VM");
    product.setCreateDate(ts);
    product.setType(template.getType());
    product.setState(ConstDef.STATE_THREE);
    product.setDescription("虚拟机自动创建服务");
    product.setSpecification(product.getDescription());
    product.setQuotaNum(1);
    product.setPrice(0f);
    product.setUnit(vminfo.getUnit());
    product.setOperateType(1);
    product.setIsDefault(0);
    return template;
  }

  @Override
  public TTemplateVMBO creatSpecalVdiskTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
    //
    template.setCode(null);// 模板编码
    template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);// 特殊模板
                                                             // 0：管理员定义的模板，1：用户定义的特殊模板
    template.setCpufrequency(0);
    template.setCpuNum(0);
    template.setMemorySize(0);
    vminfo.setDisknumber(0);
    template.setStorageSize(vminfo.getStorageSize());
    template.setVethAdaptorNum(0);
    if (user != null) {
      template.setCreatorUserId(user.getId());
    }
    template.setZoneId(vminfo.getZoneId());
    template.setResourcePoolsId(vminfo.getPoolId());
    template.setType(vminfo.getTemplateType());
    template.setOperType(1);
    Timestamp ts = new Timestamp(new Date().getTime());
    template.setCreateTime(ts.toString());
    template.setMeasureMode("Duration");
    template.setState(1);
    template.seteOsId(0);
    template.setVmos(vminfo.getVmos());
    template.setStoreType(vminfo.getStoreType());

    product.setName("Vdisk");
    product.setCreateDate(ts);
    product.setType(template.getType());
    product.setState(ConstDef.STATE_THREE);
    product.setDescription("虚拟硬盘自动创建服务");
    product.setSpecification(product.getDescription());
    product.setQuotaNum(1);
    product.setPrice(0f);
    product.setUnit(vminfo.getUnit());
    product.setOperateType(1);
    product.setIsDefault(0);
    return template;
  }

  @Override
  public TTemplateVMBO creatSpecalBKTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException {
    //
    template.setCode(null);// 模板编码
    template.setSpecial(ConstDef.RESOURCE_MODEL_SPECAL_FLAG);// 特殊模板
                                                             // 0：管理员定义的模板，1：用户定义的特殊模板
    template.setCpufrequency(0);
    template.setCpuNum(0);
    template.setMemorySize(0);
    vminfo.setDisknumber(0);
    template.setStorageSize(vminfo.getStorageSize());
    template.setVethAdaptorNum(0);
    if (user != null) {
      template.setCreatorUserId(user.getId());
    }
    template.setZoneId(vminfo.getZoneId());
    template.setResourcePoolsId(vminfo.getPoolId());
    template.setType(vminfo.getTemplateType());
    template.setOperType(1);
    Timestamp ts = new Timestamp(new Date().getTime());
    template.setCreateTime(ts.toString());
    template.setMeasureMode("Duration");
    //fix bug:4811
    template.setState(ConstDef.STATE_TWO);//可用状态modify by hfk 13-1-22
    template.seteOsId(0);
    template.setVmos(vminfo.getVmos());
    template.setStoreType(vminfo.getStoreType());

    product.setName("BK");
    product.setCreateDate(ts);
    product.setType(template.getType());
    product.setState(ConstDef.STATE_THREE);
    product.setDescription("备份服务自动创建服务");
    product.setSpecification(product.getDescription());
    product.setQuotaNum(1);
    product.setPrice(0f);
    product.setUnit(vminfo.getUnit());
    product.setOperateType(1);
    product.setIsDefault(0);
    return template;
  }

  @Override
  public String insertVMSnapshotDelete(VolumeResumeVO volumeResumeVO, int userId, String res_code) throws SCSException {

    ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
    vmModifyVO.setId(volumeResumeVO.getInstanceId());
    int index = 0;
    try {
      // if (info != null) {
      // asyncList.add(this.orgCreateTemplate(volumeResumeVO));
      // // TInstanceInfoBO instanceInfoBO = instanceInfoDao
      // // .searchInstanceInfoByID(volumeResumeVO.getInstanceId());
      //
      // List<TNicsBO> nicsInfo = nicsDao.searchNicssByInstanceId(info
      // .getId());
      // info.setNicsBOs(nicsInfo);
      // asyncList.add(this.orgDestroyVirturlMachines(info));
      // asyncList.add(this.orgupdateIriRecordState(info));
      // asyncList.addAll(this.orgDeployVirturlMachines(info));
      // // asyncList.addAll(this.orgRebootCommand(nicsInfo, info));
      // try {
      // asyncJobDao.updatebatchAsyncJobInfo(asyncList);
      // return ResourcesUtil.resultTOString(asyncList.size());
      // } catch (Exception e) {
      // log.error("insert revome Snapshot Command error:"+e.getMessage());
      // return ResourcesUtil.resultTOString(0);
      // }
      // }
      updateInstanceInfoState(vmModifyVO);
      index = this.insertDeleteSnapshotCommand(volumeResumeVO.getSnapshotId(), vmModifyVO, userId, res_code);
      return ResourcesUtil.resultTOString(index);
    } catch (Exception e) {
      log.error("qurey instanceInfo error:" + e.getMessage());
      e.printStackTrace();
    }
    return ResourcesUtil.resultTOString(0);

  }

  public IOrderDao getOrderDao() {
    return orderDao;
  }

  public void setOrderDao(IOrderDao orderDao) {
    this.orderDao = orderDao;
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

  public INicsDao getNicsDao() {
    return nicsDao;
  }

  public void setNicsDao(INicsDao nicsDao) {
    this.nicsDao = nicsDao;
  }

  public void setCommandService(ICommandService commandService) {
    this.commandService = commandService;
  }

  public void setAuditService(IAuditSevice auditService) {
    this.auditService = auditService;
  }

  public IDBUserSnapshotService getDbUserSnapshotService() {
    return dbUserSnapshotService;
  }

  public void setDbUserSnapshotService(IDBUserSnapshotService dbUserSnapshotService) {
    this.dbUserSnapshotService = dbUserSnapshotService;
  }

  public AsyncJobVDCService getAsyncJobVDCService() {
    return asyncJobVDCService;
  }

  public void setAsyncJobVDCService(AsyncJobVDCService asyncJobVDCService) {
    this.asyncJobVDCService = asyncJobVDCService;
  }

  public IVMTemplateDao getVMTemplateDao() {
    return VMTemplateDao;
  }

  public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
    VMTemplateDao = vMTemplateDao;
  }

  public CloudServiceMallDao getCloudServiceMallDao() {
    return cloudServiceMallDao;
  }

  public void setCloudServiceMallDao(CloudServiceMallDao cloudServiceMallDao) {
    this.cloudServiceMallDao = cloudServiceMallDao;
  }

  public IProductDao getProductDao() {
    return productDao;
  }

  public void setProductDao(IProductDao productDao) {
    this.productDao = productDao;
  }

public IServiceInstanceService getServiceInstanceService() {
	return serviceInstanceService;
}

public void setServiceInstanceService(
		IServiceInstanceService serviceInstanceService) {
	this.serviceInstanceService = serviceInstanceService;
}

public IOrderSerivce getOrderService() {
	return orderService;
}

public void setOrderService(IOrderSerivce orderService) {
	this.orderService = orderService;
}

public IProductInstanceRefService getProductInstanceRefService() {
	return productInstanceRefService;
}

public void setProductInstanceRefService(
		IProductInstanceRefService productInstanceRefService) {
	this.productInstanceRefService = productInstanceRefService;
}

}
