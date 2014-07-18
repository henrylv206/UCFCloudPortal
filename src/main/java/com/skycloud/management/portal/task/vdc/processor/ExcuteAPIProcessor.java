/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.processor;

import java.rmi.RemoteException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.axis2.AxisFault;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.idc.www.idc.schemas.adb.client.actvm.ActVMServiceStub;
import com.idc.www.idc.schemas.adb.client.applybandwidth.ApplyBandwidthServiceStub;
import com.idc.www.idc.schemas.adb.client.applypublicip.ApplyPublicIPServiceStub;
import com.idc.www.idc.schemas.adb.client.attachbs.AttachBSServiceStub;
import com.idc.www.idc.schemas.adb.client.bindingpublicip.BindingPublicIPServiceStub;
import com.idc.www.idc.schemas.adb.client.cancelbandwidth.CancelBandwidthServiceStub;
import com.idc.www.idc.schemas.adb.client.createbackup.CreateBackupServiceStub;
import com.idc.www.idc.schemas.adb.client.createbs.CreateBSServiceStub;
import com.idc.www.idc.schemas.adb.client.createvm.CreateVMServiceStub;
import com.idc.www.idc.schemas.adb.client.deletebackup.DeleteBackupServiceStub;
import com.idc.www.idc.schemas.adb.client.deletebs.DeleteBSServiceStub;
import com.idc.www.idc.schemas.adb.client.deletevm.DeleteVMServiceStub;
import com.idc.www.idc.schemas.adb.client.detachbs.DetachBSServiceStub;
import com.idc.www.idc.schemas.adb.client.modifybandwidth.ModifyBandwidthServiceStub;
import com.idc.www.idc.schemas.adb.client.modifyvm.ModifyVMServiceStub;
import com.idc.www.idc.schemas.adb.client.releasepublicip.ReleasePublicIPServiceStub;
import com.idc.www.idc.schemas.adb.client.resourcetemplate.ResourceTemplateStub;
import com.idc.www.idc.schemas.adb.client.restorebackup.RestoreBackupServiceStub;
import com.idc.www.idc.schemas.adb.client.unbindpublicip.UnbindPublicIPServiceStub;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.util.TaskErrorCode;
import com.skycloud.management.portal.task.vdc.util.TaskUtils;

/**
 * @author shixq
 * @create-time 2012-3-16 下午02:52:30
 * @version $Id:$
 */
public class ExcuteAPIProcessor extends AbstractProcessor {

  private static Log log = LogFactory.getLog(ExcuteAPIProcessor.class);

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#createVM
   * ()
   */
  @Override
  AsyncJobVDCPO createVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vLanID = "";
      String count = "";
      String vmName = "";
      String resourceTemplateID = "";

      if (paramterObject.containsKey("VLanID")) {
        vLanID = paramterObject.getString("VLanID");
      }
      if (paramterObject.containsKey("Count")) {
        count = paramterObject.getString("Count");
      }
      if (paramterObject.containsKey("VMNames")) {
        JSONArray vmNames = paramterObject.getJSONArray("VMNames");
        if (vmNames != null && vmNames.size() > 0) {
          vmName = vmNames.get(0).toString();
        }
        if (StringUtils.isBlank(vmName)) {
          jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
          jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
          return jobPO;
        }
      }
      if (paramterObject.containsKey("ResourceTemplateID")) {
        resourceTemplateID = paramterObject.getString("ResourceTemplateID");
        // wangchunfa test begin 暂时写死
//        resourceTemplateID = "CIDC-T-VM-00000001";
        // wangchunfa test end
      }

      CreateVMServiceStub.InputMapping1 input = new CreateVMServiceStub.InputMapping1();

      input.setResourceTemplateID(resourceTemplateID);
      input.setCount(Integer.valueOf(count));
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      if (vLanID != null || !"".equals(vLanID)) {
        input.setVLanID(vLanID);
      }
      input.setVMNames(vmName);

      CreateVMServiceStub.CreateVM createVM = new CreateVMServiceStub.CreateVM();
      createVM.setCreateVM(input);

      CreateVMServiceStub.AuthenticationInfoE authenticationInfo = new CreateVMServiceStub.AuthenticationInfoE();
      CreateVMServiceStub.AuthenticationInfo authInfo = new CreateVMServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      CreateVMServiceStub stub = new CreateVMServiceStub(TaskUtils.getIdcURI() + TaskUtils.getCreateVM());
      CreateVMServiceStub.CreateVMResponse response = stub.createVM(createVM, authenticationInfo);

      CreateVMServiceStub.OutputMapping1 output = response.getCreateVMResponse();

      jobPO.setResp_code(output.getVMID());
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#updateVM
   * ()
   */
  @Override
  AsyncJobVDCPO updateVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ModifyVMServiceStub.InputMapping1 input = new ModifyVMServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vmID = "";
      String resourceTemplateID = "";

      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (paramterObject.containsKey("ResourceTemplateID")) {
        resourceTemplateID = paramterObject.getString("ResourceTemplateID");
      }
      if (StringUtils.isBlank(vmID) || StringUtils.isBlank(resourceTemplateID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);
      input.setResourceTemplateID(resourceTemplateID);

      ModifyVMServiceStub.ModifyVM modifyVM = new ModifyVMServiceStub.ModifyVM();
      modifyVM.setModifyVM(input);

      ModifyVMServiceStub.AuthenticationInfoE authenticationInfo = new ModifyVMServiceStub.AuthenticationInfoE();
      ModifyVMServiceStub.AuthenticationInfo authInfo = new ModifyVMServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      ModifyVMServiceStub stub = new ModifyVMServiceStub(TaskUtils.getIdcURI() + TaskUtils.getModifyVM());
      ModifyVMServiceStub.ModifyVMResponse response = stub.modifyVM(modifyVM, authenticationInfo);

      ModifyVMServiceStub.OutputMapping1 output = response.getModifyVMResponse();

      jobPO.setResp_code(vmID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#deleteVM
   * ()
   */
  @Override
  AsyncJobVDCPO deleteVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      DeleteVMServiceStub.InputMapping1 input = new DeleteVMServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vmID = "";

      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);

      DeleteVMServiceStub.DeleteVM deleteVM = new DeleteVMServiceStub.DeleteVM();
      deleteVM.setDeleteVM(input);

      DeleteVMServiceStub.AuthenticationInfoE authenticationInfo = new DeleteVMServiceStub.AuthenticationInfoE();
      DeleteVMServiceStub.AuthenticationInfo authInfo = new DeleteVMServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      DeleteVMServiceStub stub = new DeleteVMServiceStub(TaskUtils.getIdcURI() + TaskUtils.getDeleteVM());
      DeleteVMServiceStub.DeleteVMResponse response = stub.deleteVM(deleteVM, authenticationInfo);

      DeleteVMServiceStub.OutputMapping1 output = response.getDeleteVMResponse();

      jobPO.setResp_code(vmID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  private AsyncJobVDCPO operateVM(AsyncJobVDCPO jobPO, ActVMServiceStub.ActTypeType actType) throws RemoteException {
    try {
      ActVMServiceStub.InputMapping1 input = new ActVMServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      if (StringUtils.isBlank(parameter)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);
      String vmID = "";

      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      input.setVMID(vmID);
      input.setActType(actType);
      ActVMServiceStub.ActVM actVM = new ActVMServiceStub.ActVM();
      actVM.setActVM(input);

      ActVMServiceStub.AuthenticationInfoE auth = new ActVMServiceStub.AuthenticationInfoE();
      ActVMServiceStub.AuthenticationInfo authInfo = new ActVMServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      auth.setAuthenticationInfo(authInfo);

      ActVMServiceStub stub = new ActVMServiceStub(TaskUtils.getIdcURI() + TaskUtils.getActVM());
      ActVMServiceStub.ActVMResponse resp = stub.actVM(actVM, auth);

      ActVMServiceStub.OutputMapping1 output = resp.getActVMResponse();

      jobPO.setResp_code(vmID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#startVM
   * ()
   */
  @Override
  AsyncJobVDCPO startVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    jobPO = operateVM(jobPO, ActVMServiceStub.ActTypeType.value1);
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#stopVM
   * ()
   */
  @Override
  AsyncJobVDCPO stopVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    jobPO = operateVM(jobPO, ActVMServiceStub.ActTypeType.value4);
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#restartVM
   * ()
   */
  @Override
  AsyncJobVDCPO restartVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    jobPO = operateVM(jobPO, ActVMServiceStub.ActTypeType.value5);
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#pauseVM
   * ()
   */
  @Override
  AsyncJobVDCPO pauseVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    jobPO = operateVM(jobPO, ActVMServiceStub.ActTypeType.value2);
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#restoreVM
   * (com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO)
   */
  @Override
  AsyncJobVDCPO restoreVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    jobPO = operateVM(jobPO, ActVMServiceStub.ActTypeType.value3);
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#createEBS
   * ()
   */
  @Override
  AsyncJobVDCPO createEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      CreateBSServiceStub.InputMapping1 input = new CreateBSServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String bsName = "";
      String bsTemplateId = "";

      if (paramterObject.containsKey("BSName")) {
        bsName = paramterObject.getString("BSName");
      }
      if (paramterObject.containsKey("BSTemplateId")) {
        bsTemplateId = paramterObject.getString("BSTemplateId");
      }
      if (StringUtils.isBlank(bsName) || StringUtils.isBlank(bsTemplateId)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBSName(bsName);
      input.setBSTemplateId(bsTemplateId);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      CreateBSServiceStub.CreateBS createBS = new CreateBSServiceStub.CreateBS();
      createBS.setCreateBS(input);

      CreateBSServiceStub.AuthenticationInfoE authenticationInfo = new CreateBSServiceStub.AuthenticationInfoE();
      CreateBSServiceStub.AuthenticationInfo authInfo = new CreateBSServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      CreateBSServiceStub stub = new CreateBSServiceStub(TaskUtils.getIdcURI() + TaskUtils.getCreateBS());
      CreateBSServiceStub.CreateBSResponse response = stub.createBS(createBS, authenticationInfo);

      CreateBSServiceStub.OutputMapping1 output = response.getCreateBSResponse();

      jobPO.setResp_code(output.getBSID());
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#deleteEBS
   * ()
   */
  @Override
  AsyncJobVDCPO deleteEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      DeleteBSServiceStub.InputMapping1 input = new DeleteBSServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String bsID = "";

      if (paramterObject.containsKey("BSID")) {
        bsID = paramterObject.getString("BSID");
      }
      if (StringUtils.isBlank(bsID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBSID(bsID);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      DeleteBSServiceStub.DeleteBS deleteBS = new DeleteBSServiceStub.DeleteBS();
      deleteBS.setDeleteBS(input);

      DeleteBSServiceStub.AuthenticationInfoE authenticationInfo = new DeleteBSServiceStub.AuthenticationInfoE();
      DeleteBSServiceStub.AuthenticationInfo authInfo = new DeleteBSServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      DeleteBSServiceStub stub = new DeleteBSServiceStub(TaskUtils.getIdcURI() + TaskUtils.getDeleteBS());
      DeleteBSServiceStub.DeleteBSResponse response = stub.deleteBS(deleteBS, authenticationInfo);

      DeleteBSServiceStub.OutputMapping1 output = response.getDeleteBSResponse();

      jobPO.setResp_code(bsID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#bindEBS
   * ()
   */
  @Override
  AsyncJobVDCPO bindEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      AttachBSServiceStub.InputMapping1 input = new AttachBSServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      //没有产生订单，orderId 为空，但目前埃森哲方必须需要，所以先随便给个编号
      int orderID = 6543;
      // int orderID = jobPO.getOrder_id();
      
    	  
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String bsID = "";
      String vmID = "";
      if (paramterObject.containsKey("BSID")) {
        bsID = paramterObject.getString("BSID");
      }
      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(bsID) || StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBSID(bsID);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);

      AttachBSServiceStub.AttachBS attachBS = new AttachBSServiceStub.AttachBS();
      attachBS.setAttachBS(input);

      AttachBSServiceStub.AuthenticationInfoE authenticationInfo = new AttachBSServiceStub.AuthenticationInfoE();
      AttachBSServiceStub.AuthenticationInfo authInfo = new AttachBSServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      AttachBSServiceStub stub = new AttachBSServiceStub(TaskUtils.getIdcURI() + TaskUtils.getAttachBS());
      AttachBSServiceStub.AttachBSResponse response = stub.attachBS(attachBS, authenticationInfo);

      AttachBSServiceStub.OutputMapping1 output = response.getAttachBSResponse();

      jobPO.setResp_code(bsID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#unbindEBS
   * ()
   */
  @Override
  AsyncJobVDCPO unbindEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      DetachBSServiceStub.InputMapping1 input = new DetachBSServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
    //没有产生订单，orderId 为空，但目前埃森哲方必须需要，所以先随便给个编号
      int orderID = 6543;
      // int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String bsID = "";
      String vmID = "";
      if (paramterObject.containsKey("BSID")) {
        bsID = paramterObject.getString("BSID");
      }
      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(bsID) || StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBSID(bsID);
       input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);

      DetachBSServiceStub.DetachBS detachBS = new DetachBSServiceStub.DetachBS();
      detachBS.setDetachBS(input);

      DetachBSServiceStub.AuthenticationInfoE authenticationInfo = new DetachBSServiceStub.AuthenticationInfoE();
      DetachBSServiceStub.AuthenticationInfo authInfo = new DetachBSServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      DetachBSServiceStub stub = new DetachBSServiceStub(TaskUtils.getIdcURI() + TaskUtils.getDetachBS());
      DetachBSServiceStub.DetachBSResponse response = stub.detachBS(detachBS, authenticationInfo);

      DetachBSServiceStub.OutputMapping1 output = response.getDetachBSResponse();

      jobPO.setResp_code(bsID);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#createWanip
   * ()
   */
  @Override
  AsyncJobVDCPO createWanip(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ApplyPublicIPServiceStub.InputMapping1 input = new ApplyPublicIPServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ipTemplateID = "";
      if (paramterObject.containsKey("IPTemplateID")) {
        ipTemplateID = paramterObject.getString("IPTemplateID");
      }
      if (StringUtils.isBlank(ipTemplateID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setIPTemplateID(ipTemplateID);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      ApplyPublicIPServiceStub.ApplyPublicIP applyPublicIP = new ApplyPublicIPServiceStub.ApplyPublicIP();
      applyPublicIP.setApplyPublicIP(input);

      ApplyPublicIPServiceStub.AuthenticationInfoE authenticationInfo = new ApplyPublicIPServiceStub.AuthenticationInfoE();
      ApplyPublicIPServiceStub.AuthenticationInfo authInfo = new ApplyPublicIPServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      ApplyPublicIPServiceStub stub = new ApplyPublicIPServiceStub(TaskUtils.getIdcURI() + TaskUtils.getApplyPublicIP());
      ApplyPublicIPServiceStub.ApplyPublicIPResponse resp = stub.applyPublicIP(applyPublicIP, authenticationInfo);
      ApplyPublicIPServiceStub.OutputMapping1 output = resp.getApplyPublicIPResponse();

      jobPO.setResp_code(output.getIP());
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#deleteWanip
   * ()
   */
  @Override
  AsyncJobVDCPO deleteWanip(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ReleasePublicIPServiceStub.InputMapping1 input = new ReleasePublicIPServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (StringUtils.isBlank(ip)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setIP(ip);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      ReleasePublicIPServiceStub.ReleasePublicIP releasePublicIP = new ReleasePublicIPServiceStub.ReleasePublicIP();
      releasePublicIP.setReleasePublicIP(input);

      ReleasePublicIPServiceStub.AuthenticationInfoE authenticationInfo = new ReleasePublicIPServiceStub.AuthenticationInfoE();
      ReleasePublicIPServiceStub.AuthenticationInfo authInfo = new ReleasePublicIPServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      ReleasePublicIPServiceStub stub = new ReleasePublicIPServiceStub(TaskUtils.getIdcURI() + TaskUtils.getReleasePublicIP());
      ReleasePublicIPServiceStub.ReleasePublicIPResponse response = stub.releasePublicIP(releasePublicIP, authenticationInfo);

      ReleasePublicIPServiceStub.OutputMapping1 output = response.getReleasePublicIPResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#bindWanip
   * ()
   */
  @Override
  AsyncJobVDCPO bindWanip(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      BindingPublicIPServiceStub.InputMapping1 input = new BindingPublicIPServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      // int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      String vmID = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(ip) || StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setIP(ip);
      // input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(String.valueOf(vmID));

      BindingPublicIPServiceStub.BindingPublicIP bindingPublicIP = new BindingPublicIPServiceStub.BindingPublicIP();
      bindingPublicIP.setBindingPublicIP(input);

      BindingPublicIPServiceStub.AuthenticationInfoE authenticationInfo = new BindingPublicIPServiceStub.AuthenticationInfoE();
      BindingPublicIPServiceStub.AuthenticationInfo authInfo = new BindingPublicIPServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      BindingPublicIPServiceStub stub = new BindingPublicIPServiceStub(TaskUtils.getIdcURI() + TaskUtils.getBindingPublicIP());
      BindingPublicIPServiceStub.BindingPublicIPResponse response = stub.bindingPublicIP(bindingPublicIP, authenticationInfo);

      BindingPublicIPServiceStub.OutputMapping1 output = response.getBindingPublicIPResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#unbindWanip
   * ()
   */
  @Override
  AsyncJobVDCPO unbindWanip(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      UnbindPublicIPServiceStub.InputMapping1 input = new UnbindPublicIPServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      // int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      String vmID = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(vmID)||StringUtils.isBlank(ip)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      input.setIP(ip);
      // input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);

      UnbindPublicIPServiceStub.UnbindPublicIP unbindPublicIP = new UnbindPublicIPServiceStub.UnbindPublicIP();
      unbindPublicIP.setUnbindPublicIP(input);

      UnbindPublicIPServiceStub.AuthenticationInfoE authenticationInfo = new UnbindPublicIPServiceStub.AuthenticationInfoE();
      UnbindPublicIPServiceStub.AuthenticationInfo authInfo = new UnbindPublicIPServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      UnbindPublicIPServiceStub stub = new UnbindPublicIPServiceStub(TaskUtils.getIdcURI() + TaskUtils.getUnbindPublicIP());
      UnbindPublicIPServiceStub.UnbindPublicIPResponse response = stub.unbindPublicIP(unbindPublicIP, authenticationInfo);

      UnbindPublicIPServiceStub.OutputMapping1 output = response.getUnbindPublicIPResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#createVMBak
   * ()
   */
  @Override
  AsyncJobVDCPO createVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      CreateBackupServiceStub.InputMapping1 input = new CreateBackupServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vmID = "";
      if (paramterObject.containsKey("VMID")) {
        vmID = paramterObject.getString("VMID");
      }
      if (StringUtils.isBlank(vmID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));
      input.setVMID(vmID);

      CreateBackupServiceStub.CreateBackup createBackup = new CreateBackupServiceStub.CreateBackup();
      createBackup.setCreateBackup(input);

      CreateBackupServiceStub.AuthenticationInfoE authenticationInfo = new CreateBackupServiceStub.AuthenticationInfoE();
      CreateBackupServiceStub.AuthenticationInfo authInfo = new CreateBackupServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      CreateBackupServiceStub stub = new CreateBackupServiceStub(TaskUtils.getIdcURI() + TaskUtils.getCreateBackup());
      CreateBackupServiceStub.CreateBackupResponse response = stub.createBackup(createBackup, authenticationInfo);

      CreateBackupServiceStub.OutputMapping1 output = response.getCreateBackupResponse();

      jobPO.setResp_code(output.getVMBackupId());
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#deleteVMBak
   * ()
   */
  @Override
  AsyncJobVDCPO deleteVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      DeleteBackupServiceStub.InputMapping1 input = new DeleteBackupServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
    //没有产生订单，orderId 为空，但目前埃森哲方必须需要，所以先随便给个编号
      int orderID = 6543;
//      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vmBackupId = "";
      if (paramterObject.containsKey("VMBackupId")) {
        vmBackupId = paramterObject.getString("VMBackupId");
      }
      if (StringUtils.isBlank(vmBackupId)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setVMBackupId(vmBackupId);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      DeleteBackupServiceStub.DeleteBackup deleteBackup = new DeleteBackupServiceStub.DeleteBackup();
      deleteBackup.setDeleteBackup(input);

      DeleteBackupServiceStub.AuthenticationInfoE authenticationInfo = new DeleteBackupServiceStub.AuthenticationInfoE();
      DeleteBackupServiceStub.AuthenticationInfo authInfo = new DeleteBackupServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      DeleteBackupServiceStub stub = new DeleteBackupServiceStub(TaskUtils.getIdcURI() + TaskUtils.getDeleteBackup());
      DeleteBackupServiceStub.DeleteBackupResponse response = stub.deleteBackup(deleteBackup, authenticationInfo);

      DeleteBackupServiceStub.OutputMapping1 output = response.getDeleteBackupResponse();

      jobPO.setResp_code(vmBackupId);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#updateVMBak
   * ()
   */
  // @Override
  // AsyncJobVDCPO updateVMBak(AsyncJobVDCPO jobPO) throws SCSException {
  // // TODO Auto-generated method stub
  // return jobPO;
  // }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * restoreVMBak()
   */
  @Override
  AsyncJobVDCPO restoreVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      RestoreBackupServiceStub.InputMapping1 input = new RestoreBackupServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      // int orderID = jobPO.getOrder_id();
    //没有产生订单，orderId 为空，但目前埃森哲方必须需要，所以先随便给个编号
      int orderID = 6543;
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String vmBackupId = "";
      if (paramterObject.containsKey("VMBackupId")) {
        vmBackupId = paramterObject.getString("VMBackupId");
      }
      if (StringUtils.isBlank(vmBackupId)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      input.setVMBackupId(vmBackupId);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      RestoreBackupServiceStub.RestoreBackup restoreBackup = new RestoreBackupServiceStub.RestoreBackup();
      restoreBackup.setRestoreBackup(input);

      RestoreBackupServiceStub.AuthenticationInfoE authenticationInfo = new RestoreBackupServiceStub.AuthenticationInfoE();
      RestoreBackupServiceStub.AuthenticationInfo authInfo = new RestoreBackupServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      RestoreBackupServiceStub stub = new RestoreBackupServiceStub(TaskUtils.getIdcURI() + TaskUtils.getRestoreBackup());
      RestoreBackupServiceStub.RestoreBackupResponse response = stub.restoreBackup(restoreBackup, authenticationInfo);

      RestoreBackupServiceStub.OutputMapping1 output = response.getRestoreBackupResponse();

      jobPO.setResp_code(vmBackupId);
      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * createDataBak()
   */
  @Override
  AsyncJobVDCPO createDataBak(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * deleteDataBak()
   */
  @Override
  AsyncJobVDCPO deleteDataBak(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * updateDataBak()
   */
  @Override
  AsyncJobVDCPO updateDataBak(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * createBandwidth()
   */
  @Override
  AsyncJobVDCPO createBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ApplyBandwidthServiceStub.InputMapping1 input = new ApplyBandwidthServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      String bwTemplateID = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (paramterObject.containsKey("BWTemplateID")) {
        bwTemplateID = paramterObject.getString("BWTemplateID");
      }
      if (StringUtils.isBlank(ip) || StringUtils.isBlank(bwTemplateID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBWTemplateID(bwTemplateID);
      input.setIP(ip);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      ApplyBandwidthServiceStub.ApplyBandwidth applyBandwidth = new ApplyBandwidthServiceStub.ApplyBandwidth();
      applyBandwidth.setApplyBandwidth(input);

      ApplyBandwidthServiceStub.AuthenticationInfoE authenticationInfo = new ApplyBandwidthServiceStub.AuthenticationInfoE();
      ApplyBandwidthServiceStub.AuthenticationInfo authInfo = new ApplyBandwidthServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      ApplyBandwidthServiceStub stub = new ApplyBandwidthServiceStub(TaskUtils.getIdcURI() + TaskUtils.getApplyBandwidth());
      ApplyBandwidthServiceStub.ApplyBandwidthResponse response = stub.applyBandwidth(applyBandwidth, authenticationInfo);

      ApplyBandwidthServiceStub.OutputMapping1 output = response.getApplyBandwidthResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * deleteBandwidth()
   */
  @Override
  AsyncJobVDCPO deleteBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      CancelBandwidthServiceStub.InputMapping1 input = new CancelBandwidthServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (StringUtils.isBlank(ip)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setIP(ip);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      CancelBandwidthServiceStub.CancelBandwidth cancelBandwidth = new CancelBandwidthServiceStub.CancelBandwidth();
      cancelBandwidth.setCancelBandwidth(input);

      CancelBandwidthServiceStub.AuthenticationInfoE authenticationInfo = new CancelBandwidthServiceStub.AuthenticationInfoE();
      CancelBandwidthServiceStub.AuthenticationInfo authInfo = new CancelBandwidthServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      CancelBandwidthServiceStub stub = new CancelBandwidthServiceStub(TaskUtils.getIdcURI() + TaskUtils.getCancelBandwidth());
      CancelBandwidthServiceStub.CancelBandwidthResponse response = stub.cancelBandwidth(cancelBandwidth, authenticationInfo);

      CancelBandwidthServiceStub.OutputMapping1 output = response.getCancelBandwidthResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * updateBandwidth()
   */
  @Override
  AsyncJobVDCPO updateBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ModifyBandwidthServiceStub.InputMapping1 input = new ModifyBandwidthServiceStub.InputMapping1();

      String parameter = jobPO.getParameter();
      int orderID = jobPO.getOrder_id();
      int userID = jobPO.getUser_id();

      if (StringUtils.isBlank(parameter) || orderID == 0 || userID == 0) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      JSONObject paramterObject = JSONObject.fromObject(parameter);

      String ip = "";
      String bwTemplateID = "";
      if (paramterObject.containsKey("IP")) {
        ip = paramterObject.getString("IP");
      }
      if (paramterObject.containsKey("BWTemplateID")) {
        bwTemplateID = paramterObject.getString("BWTemplateID");
      }
      if (StringUtils.isBlank(ip) || StringUtils.isBlank(bwTemplateID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }

      input.setBWTemplateID(bwTemplateID);
      input.setIP(ip);
      input.setOrderID(String.valueOf(orderID));
      input.setSerialNumber(String.valueOf(jobPO.getUnique_id()));
      input.setUserID(String.valueOf(userID));

      ModifyBandwidthServiceStub.ModifyBandwidth modifyBandwidth = new ModifyBandwidthServiceStub.ModifyBandwidth();
      modifyBandwidth.setModifyBandwidth(input);

      ModifyBandwidthServiceStub.AuthenticationInfoE authenticationInfo = new ModifyBandwidthServiceStub.AuthenticationInfoE();
      ModifyBandwidthServiceStub.AuthenticationInfo authInfo = new ModifyBandwidthServiceStub.AuthenticationInfo();
      authInfo.setUserName(TaskUtils.getIdcUserName());
      authInfo.setPassword(TaskUtils.getIdcPassword());
      authenticationInfo.setAuthenticationInfo(authInfo);

      ModifyBandwidthServiceStub stub = new ModifyBandwidthServiceStub(TaskUtils.getIdcURI() + TaskUtils.getModifyBandwidth());
      ModifyBandwidthServiceStub.ModifyBandwidthResponse response = stub.modifyBandwidth(modifyBandwidth, authenticationInfo);

      ModifyBandwidthServiceStub.OutputMapping1 output = response.getModifyBandwidthResponse();

      jobPO.setComment(output.getSerialNumber());
      jobPO.setError_code(output.getResultCode());
      jobPO.setError_code_desc(output.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * createSGrules()
   */
  @Override
  AsyncJobVDCPO createSGrules(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * deleteSGrules()
   */
  @Override
  AsyncJobVDCPO deleteSGrules(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * createTemplate()
   */
  @Override
  AsyncJobVDCPO createTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ResourceTemplateStub.CreateResourceTemplateReq createResourceTemplateReq = new ResourceTemplateStub.CreateResourceTemplateReq();
      String templateID = jobPO.getTemplate_res_id();
      String parameter = jobPO.getParameter();
      if (StringUtils.isBlank(templateID) || StringUtils.isBlank(parameter)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      createResourceTemplateReq.setResourceTemplateID(templateID);
      createResourceTemplateReq.setResourceTemplate(parameter);

      ResourceTemplateStub stub = new ResourceTemplateStub(TaskUtils.getIdcTemplateURI() + TaskUtils.getResourceTemplate());
      ResourceTemplateStub.CreateResourceTemplateResp response = stub.createResourceTemplate(createResourceTemplateReq);

      jobPO.setError_code(response.getResultCode());
      jobPO.setError_code_desc(response.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * updateTemplate()
   */
  @Override
  AsyncJobVDCPO updateTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ResourceTemplateStub.ModifyResourceTemplateReq modifyResourceTemplateReq = new ResourceTemplateStub.ModifyResourceTemplateReq();

      String templateID = jobPO.getTemplate_res_id();
      String parameter = jobPO.getParameter();
      if (StringUtils.isBlank(templateID) || StringUtils.isBlank(parameter)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      modifyResourceTemplateReq.setResourceTemplateID(templateID);
      modifyResourceTemplateReq.setResourceTemplate(parameter);
      ResourceTemplateStub stub = new ResourceTemplateStub(TaskUtils.getIdcTemplateURI() + TaskUtils.getResourceTemplate());
      ResourceTemplateStub.ModifyResourceTemplateResp response = stub.modifyResourceTemplate(modifyResourceTemplateReq);

      jobPO.setError_code(response.getResultCode());
      jobPO.setError_code_desc(response.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * deleteTemplate()
   */
  @Override
  AsyncJobVDCPO deleteTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException {
    try {
      ResourceTemplateStub.DeleteResourceTemplateReq deleteResourceTemplateReq = new ResourceTemplateStub.DeleteResourceTemplateReq();

      String templateID = jobPO.getTemplate_res_id();

      if (StringUtils.isBlank(templateID)) {
        jobPO.setError_code(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_ERROR);
        jobPO.setError_code_desc(TaskErrorCode.API_RESULT_CODE_77777777_COUNT_DESC);
        return jobPO;
      }
      deleteResourceTemplateReq.setResourceTemplateID(templateID);

      ResourceTemplateStub stub = new ResourceTemplateStub(TaskUtils.getIdcTemplateURI() + TaskUtils.getResourceTemplate());
      ResourceTemplateStub.DeleteResourceTemplateResp response = stub.deleteResourceTemplate(deleteResourceTemplateReq);

      jobPO.setError_code(response.getResultCode());
      jobPO.setError_code_desc(response.getFaultstring());

    } catch (AxisFault e) {
      log.error(e);
      throw e;
    } catch (RemoteException e) {
      log.error(e);
      throw e;
    }
    return jobPO;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.processor.AbstractProcessor#
   * defaultUpdate()
   */
  @Override
  AsyncJobVDCPO defaultUpdate(AsyncJobVDCPO jobPO) throws SCSException {
    // TODO Auto-generated method stub
    return jobPO;
  }

}
