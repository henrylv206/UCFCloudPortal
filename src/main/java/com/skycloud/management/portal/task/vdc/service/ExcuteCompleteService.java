/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;

/**
 * @author shixq
 * @create-time 2012-3-26 上午11:03:12
 * @version $Id:$
 */
public interface ExcuteCompleteService {
  /**
   * 申请虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:26
   * @version $Id:$
   */
  int updateCreateVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 变更虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:27
   * @version $Id:$
   */
  int updateUpdateVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 退订虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:29
   * @version $Id:$
   */
  int updateDeleteVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 启动虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:32
   * @version $Id:$
   */
  int updateStartVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 停止虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:34
   * @version $Id:$
   */
  int updateStopVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 重启虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:36
   * @version $Id:$
   */
  int updateRestartVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 暂停虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:38
   * @version $Id:$
   */
  int updatePauseVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 恢复虚拟机
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:38
   * @version $Id:$
   */
  int updateRestoreVM(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 申请块存储
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:40
   * @version $Id:$
   */
  int updateCreateEBS(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 退订块存储
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:42
   * @version $Id:$
   */
  int updateDeleteEBS(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 挂截块存储
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:45
   * @version $Id:$
   */
  int updateBindEBS(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 解挂截块存储
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:48
   * @version $Id:$
   */
  int updateUnbindEBS(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 申请公网IP
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:49
   * @version $Id:$
   */
  int updateCreateWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 退订公网IP
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:54
   * @version $Id:$
   */
  int updateDeleteWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 映射公网IP
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:56
   * @version $Id:$
   */
  int updateBindWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 解映射公网IP
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:38:59
   * @version $Id:$
   */
  int updateUnbindWanip(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 申请虚拟机备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:01
   * @version $Id:$
   */

  int updateCreateVMBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 退订虚拟机备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:03
   * @version $Id:$
   */
  int updateDeleteVMBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 变更虚拟机备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:06
   * @version $Id:$
   */
  int updateUpdateVMBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 恢复虚拟机备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:08
   * @version $Id:$
   */
  int updateRestoreVMBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 申请数据云备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:11
   * @version $Id:$
   */
  int updateCreateDataBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 退订数据云备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:12
   * @version $Id:$
   */
  int updateDeleteDataBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 变更数据云备份
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:15
   * @version $Id:$
   */
  int updateUpdateDataBak(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 申请带宽
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:17
   * @version $Id:$
   */
  int updateCreateBandwidth(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 退订带宽
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:19
   * @version $Id:$
   */
  int updateDeleteBandwidth(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 变更带宽
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:21
   * @version $Id:$
   */
  int updateUpdateBandwidth(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 创建安全组规则
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:23
   * @version $Id:$
   */
  int updateCreateSGrules(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 删除安全组规则
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:25
   * @version $Id:$
   */
  int updateDeleteSGrules(AsyncJobVDCPO jobPO) throws Exception;

  /**
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:26
   * @version $Id:$
   */
  int updateCreateBlmappings(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:28
   * @version $Id:$
   */
  int updateDeleteBlmappings(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 申请VM模板
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:31
   * @version $Id:$
   */
  int updateCreateTemplate(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 修改VM模板
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:33
   * @version $Id:$
   */
  int updateUpdateTemplate(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 删除VM模板
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:36
   * @version $Id:$
   */
  int updateDeleteTemplate(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:38
   * @version $Id:$
   */
  int updateDefaultUpdate(AsyncJobVDCPO jobPO) throws SCSException;

  /**
   * 执行失败
   * 
   * @author shixq
   * @create-time 2012-6-18 下午03:39:40
   * @version $Id:$
   */
  int executeError(AsyncJobVDCPO jobPO) throws SCSException;
}
