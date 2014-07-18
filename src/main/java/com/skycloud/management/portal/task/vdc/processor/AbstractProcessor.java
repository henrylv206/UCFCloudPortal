/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.processor;

import java.rmi.RemoteException;
import java.sql.SQLException;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;

/**
 * @author shixq
 * @create-time 2012-3-15 下午09:49:18
 * @version $Id:$
 */
public abstract class AbstractProcessor {
  /**
   * 申请虚拟机
   * 
   * @author shixq
   * @create-time 2012-3-26 下午03:07:13
   * @version $Id:$
   */
  abstract AsyncJobVDCPO createVM(AsyncJobVDCPO jobPO) throws SCSException, SQLException, RemoteException;

  /**
   * 变更虚拟机
   */
  abstract AsyncJobVDCPO updateVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 退订虚拟机
   */
  abstract AsyncJobVDCPO deleteVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 启动虚拟机
   */
  abstract AsyncJobVDCPO startVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 停止虚拟机
   */
  abstract AsyncJobVDCPO stopVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 重启虚拟机
   */
  abstract AsyncJobVDCPO restartVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 暂停虚拟机
   */
  abstract AsyncJobVDCPO pauseVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 恢复虚拟机
   */
  abstract AsyncJobVDCPO restoreVM(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 申请块存储
   */
  abstract AsyncJobVDCPO createEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 退订块存储
   */
  abstract AsyncJobVDCPO deleteEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 挂截块存储
   */
  abstract AsyncJobVDCPO bindEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 解挂截块存储
   */
  abstract AsyncJobVDCPO unbindEBS(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 申请公网IP
   */
  abstract AsyncJobVDCPO createWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 退订公网IP
   */
  abstract AsyncJobVDCPO deleteWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 映射公网IP
   */
  abstract AsyncJobVDCPO bindWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 解映射公网IP
   */
  abstract AsyncJobVDCPO unbindWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 申请虚拟机备份
   */
  abstract AsyncJobVDCPO createVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 退订虚拟机备份
   */
  abstract AsyncJobVDCPO deleteVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 变更虚拟机备份
   */
  // abstract AsyncJobVDCPO updateVMBak(AsyncJobVDCPO jobPO) throws
  // SCSException;

  /**
   * 恢复虚拟机备份
   */
  abstract AsyncJobVDCPO restoreVMBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 申请数据云备份
   */
  abstract AsyncJobVDCPO createDataBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 退订数据云备份
   */
  abstract AsyncJobVDCPO deleteDataBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 变更数据云备份
   */
  abstract AsyncJobVDCPO updateDataBak(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 申请带宽
   */
  abstract AsyncJobVDCPO createBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 退订带宽
   */
  abstract AsyncJobVDCPO deleteBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 变更带宽
   */
  abstract AsyncJobVDCPO updateBandwidth(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 创建安全组规则
   */
  abstract AsyncJobVDCPO createSGrules(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 删除安全组规则
   */
  abstract AsyncJobVDCPO deleteSGrules(AsyncJobVDCPO jobPO) throws Exception, RemoteException;

  /**
   * 申请VM模板
   */
  abstract AsyncJobVDCPO createTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 修改VM模板
   */
  abstract AsyncJobVDCPO updateTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  /**
   * 删除VM模板
   */
  abstract AsyncJobVDCPO deleteTemplate(AsyncJobVDCPO jobPO) throws SCSException, RemoteException;

  abstract AsyncJobVDCPO defaultUpdate(AsyncJobVDCPO jobPO) throws SCSException;

  public AsyncJobVDCPO jobExcuteAPI(AsyncJobVDCPO jobPO) throws Exception {
    if (jobPO.getOperation().equals(OperationType.CREATE_VM)) {
      jobPO = createVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UPDATE_VM)) {
      jobPO = updateVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_VM)) {
      jobPO = deleteVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.START_VM)) {
      jobPO = startVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.STOP_VM)) {
      jobPO = stopVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.RESTART_VM)) {
      jobPO = restartVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.PAUSE_VM)) {
      jobPO = pauseVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.RESTORE_VM)) {
      jobPO = restoreVM(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_EBS)) {
      jobPO = createEBS(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_EBS)) {
      jobPO = deleteEBS(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.BIND_EBS)) {
      jobPO = bindEBS(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UNBIND_EBS)) {
      jobPO = unbindEBS(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_WANIP)) {
      jobPO = createWanip(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_WANIP)) {
      jobPO = deleteWanip(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.BIND_WANIP)) {
      jobPO = bindWanip(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UNBIND_WANIP)) {
      jobPO = unbindWanip(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_VMBAK)) {
      jobPO = createVMBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_VMBAK)) {
      jobPO = deleteVMBak(jobPO);
      // } else if (jobPO.getOperation().equals(OperationType.UPDATE_VMBAK)) {
      // jobPO = updateVMBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.RESTORE_VMBAK)) {
      jobPO = restoreVMBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_DATABAK)) {
      jobPO = createDataBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_DATABAK)) {
      jobPO = deleteDataBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UPDATE_DATABAK)) {
      jobPO = updateDataBak(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_BANDWIDTH)) {
      jobPO = createBandwidth(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_BANDWIDTH)) {
      jobPO = deleteBandwidth(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UPDATE_BANDWIDTH)) {
      jobPO = updateBandwidth(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_SGRULES)) {
      jobPO = createSGrules(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_SGRULES)) {
      jobPO = deleteSGrules(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.CREATE_TEMPLATE)) {
      jobPO = createTemplate(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.UPDATE_TEMPLATE)) {
      jobPO = updateTemplate(jobPO);
    } else if (jobPO.getOperation().equals(OperationType.DELETE_TEMPLATE)) {
      jobPO = deleteTemplate(jobPO);
    } else {
      jobPO = defaultUpdate(jobPO);
    }
    return jobPO;
  }
}
