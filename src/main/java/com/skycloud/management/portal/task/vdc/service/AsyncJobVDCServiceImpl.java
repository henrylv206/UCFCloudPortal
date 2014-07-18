/**
 * 2012-3-13  下午02:27:08  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.service;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.AsyncJobVDCDao;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.JobStateVDC;

/**
 * @author shixq
 * @version $Revision$ 下午02:27:08
 */
public class AsyncJobVDCServiceImpl implements AsyncJobVDCService, AsyncJobVDCTaskService {

  private static Log log = LogFactory.getLog(AsyncJobVDCServiceImpl.class);
  private AsyncJobVDCDao asyncJobVDCDao;

  /**
   * 插入前检查必传数据
   * 
   * @param jobPO
   * @throws SCSException
   */
  private void check(AsyncJobVDCPO jobPO) throws SCSException {
    log.info("@@@@@ check ,getUnique_id=" + jobPO.getUnique_id() + ",getUser_id=" + jobPO.getUser_id() + ",getOperation="
        + jobPO.getOperation() + ",getAuditstate=" + jobPO.getAuditstate());
    if (jobPO.getUnique_id() == 0)
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_UNIQUE_ID_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_PARAMETER_UNIQUE_ID_ASYNCJOB_DESC);
    if (jobPO.getUser_id() == 0)
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_USERID_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_PARAMETER_USERID_ASYNCJOB_DESC);
    if (jobPO.getOperation() == null)
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_OPERATION_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_PARAMETER_OPERATION_ASYNCJOB_DESC);
    if (jobPO.getAuditstate() == null)
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_AUDIT_STATE_ASYNCJOB_ERROR,
          SCSErrorCode.DB_SQL_PARAMETER_AUDIT_STATE_ASYNCJOB_DESC);
    else if (jobPO.getAuditstate() == AuditStateVDC.PASS_AUDIT || jobPO.getAuditstate() == AuditStateVDC.NOT_PASS_AUDIT)
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_AUDIT_STATE_ERROR_ASYNCJOB_ERROR,
          SCSErrorCode.DB_SQL_PARAMETER_AUDIT_STATE_ERROR_ASYNCJOB_DESC);

    if (StringUtils.isBlank(jobPO.getParameter())) {
      if (jobPO.getOperation().getValue().startsWith("DELETE"))
        jobPO.setParameter("");
      else
        throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_API_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_PARAMETER_API_ASYNCJOB_DESC);
    }
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.service.AsynJobVDCService#
   * insterAsynJobVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int insterAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException {
    try {
      check(jobPO);
    } catch (Exception e) {
      log.debug(e);
    }
    return asyncJobVDCDao.insterAsyncJobVDC(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.service.AsynJobVDCService#
   * updateAsynJobAuditStateVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int updateAsyncJobAuditStateVDC(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.updateAsyncJobAuditStateVDC(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * updateAsyncJobStateVDC(java.lang.String)
   */
  @Override
  public int updateAsyncJobStateVDC(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.updateAsyncJobStateVDC(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * updateAsyncJobPassVDC(java.util.List)
   */
  @Override
  public int updateAsyncJobPassVDC(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.updateAsyncJobPassVDC(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * updateAsyncJobNotPassVDC(java.util.List)
   */
  @Override
  public int updateAsyncJobNotPassVDC(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.updateAsyncJobNotPassVDC(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * queryAsyncJobList
   * (com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO)
   */
  @Override
  public List<AsyncJobVDCPO> queryAsyncJobList(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.queryAsyncJobList(jobPO);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * queryAPI(com.skycloud.management.portal.task.vdc.enumtype.JobStateVDC)
   */
  @Override
  public List<AsyncJobVDCPO> queryAsyncJobList4Task(JobStateVDC jobstate) throws SCSException {
    List<AsyncJobVDCPO> list = null;
    AsyncJobVDCPO jobPO = new AsyncJobVDCPO();
    jobPO.setJobstate(jobstate);
    list = asyncJobVDCDao.queryAsyncJobList(jobPO);
    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * insertAysncJobHistory()
   */
  @Override
  public int insertAysncJobHistory() throws SCSException {
    return asyncJobVDCDao.insertAysncJobHistory();
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.service.AsyncJobVDCTaskService#
   * deleteAysncJobHistory()
   */
  @Override
  public int deleteAysncJobHistory() throws SCSException {
    return asyncJobVDCDao.deleteAysncJobHistory();
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.service.AsynJobVDCService#
   * deleteAsynJobVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int deleteAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException {
    return asyncJobVDCDao.deleteAsyncJobVDC(jobPO);
  }

  public AsyncJobVDCDao getAsyncJobVDCDao() {
    return asyncJobVDCDao;
  }

  public void setAsyncJobVDCDao(AsyncJobVDCDao asyncJobVDCDao) {
    this.asyncJobVDCDao = asyncJobVDCDao;
  }

}
