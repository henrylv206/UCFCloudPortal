/**
 * 2012-3-13  下午02:15:12  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.dao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.dao.po.AsyncJobVDCPO;
import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.JobStateVDC;
import com.skycloud.management.portal.task.vdc.util.TaskUtils;

/**
 * @author shixq
 * @version $Revision$ 下午02:15:12
 */
public class AsyncJobVDCDaoImpl implements AsyncJobVDCDao {

  private static Log log = LogFactory.getLog(AsyncJobVDCDaoImpl.class);
  private JdbcTemplate jt;

  /**
   * 准备插入数据
   * 
   * @param jobPO
   * @return
   */
  private List<Object> initInsertData(AsyncJobVDCPO jobPO) {
    List<Object> args = new ArrayList<Object>();
    args.add(jobPO.getUnique_id());
    args.add(jobPO.getUser_id());
    args.add(jobPO.getOrder_id());
    args.add(jobPO.getInstance_info_id());
    args.add(jobPO.getInstance_info_iri_id());
    args.add(jobPO.getTemplate_id());
    args.add(jobPO.getTemplate_res_id());
    args.add(jobPO.getFirewall_rule_id());
    args.add(jobPO.getOperation().getValue());
    args.add(jobPO.getParameter());
    args.add(jobPO.getAuditstate().getValue());
    args.add(jobPO.getCreate_dt());
    args.add(jobPO.getComment());
    return args;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#insterAsynJobVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int insterAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException {
    String sql = "INSERT INTO T_SCS_ASYNCJOB_VDC (UNIQUE_ID,USER_ID,ORDER_ID,INSTANCE_INFO_ID,INSTANCE_INFO_IRI_ID,TEMPLATE_ID,TEMPLATE_RES_ID,FIREWALL_RULE_ID,OPERATION,PARAMETER,JOBSTATE,AUDITSTATE,CREATE_DT,COMMENT)VALUES(?,?,?,?,?,?,?,?,?,?,"
        + JobStateVDC.WAIT_RUN.getValue() + ",?,?,?);";
    int index = 0;
    List<Object> args = initInsertData(jobPO);
    log.info("@@@@@@@@ SQL=" + sql + "," + args);
    try {
      index = jt.update(sql, args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#
   * updateAsynJobAuditStateVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int updateAsyncJobAuditStateVDC(AsyncJobVDCPO jobPO) throws SCSException {
    StringBuffer sql = new StringBuffer("UPDATE T_SCS_ASYNCJOB_VDC SET AUDITSTATE=? WHERE ORDER_ID=? ");
    int index = 0;
    List<Object> args = new ArrayList<Object>();
    if (jobPO.getOrder_id() == 0) {
      throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_LOST_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_PARAMETER_LOST_ASYNCJOB_DESC);
    }
    args.add(jobPO.getAuditstate().getValue());
    args.add(jobPO.getOrder_id());
    // if (jobPO.getOrder_id() != 0) {
    // sql.append("AND ORDER_ID =?");
    // args.add(jobPO.getOrder_id());
    // }
    // if (jobPO.getInstance_info_id() != 0) {
    // sql.append("AND INSTANCE_INFO_ID =?");
    // args.add(jobPO.getInstance_info_id());
    // }
    // if (jobPO.getTemplate_id() != 0) {
    // sql.append("AND TEMPLATE_ID =?");
    // args.add(jobPO.getTemplate_id());
    // }
    // if (jobPO.getFirewall_rule_id() != 0) {
    // sql.append("AND FIREWALL_RULE_ID =?");
    // args.add(jobPO.getFirewall_rule_id());
    // }
    log.info("@@@@@@@@ SQL=" + sql.toString() + "," + args);
    try {
      index = jt.update(sql.toString(), args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#
   * updateAsynJobStateVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int updateAsyncJobStateVDC(AsyncJobVDCPO jobPO) throws SCSException {
    StringBuffer sql = new StringBuffer("UPDATE T_SCS_ASYNCJOB_VDC SET JOBSTATE=?,RESP_CODE=?,RESP_PARAMETER=?,ERROR_CODE=?,ERROR_CODE_DESC=?,COMMENT=? WHERE UNIQUE_ID = ? ");
    int index = 0;
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(jobPO.getJobstate().getValue());
      args.add(jobPO.getResp_code());
      args.add(jobPO.getResp_parameter());
      args.add(jobPO.getError_code());
      args.add(jobPO.getError_code_desc());
      args.add(jobPO.getComment());
      args.add(jobPO.getUnique_id());
      log.info("@@@@@@@@ SQL=" + sql.toString() + "," + args);
      index = jt.update(sql.toString(), args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#updateAsynJobPassVDC
   * (java.util.List)
   */
  @Override
  public int updateAsyncJobPassVDC(AsyncJobVDCPO jobPO) throws SCSException {
    StringBuffer sql = new StringBuffer("UPDATE T_SCS_ASYNCJOB_VDC SET JOBSTATE='" + JobStateVDC.PASS_RUN.getValue()
        + "' WHERE UNIQUE_ID =? ");
    int index = 0;
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(jobPO.getUnique_id());
      log.info("@@@@@@@@ SQL=" + sql.toString() + "," + args);
      index = jt.update(sql.toString(), args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#
   * updateAsynJobNotPassVDC(java.util.List)
   */
  @Override
  public int updateAsyncJobNotPassVDC(AsyncJobVDCPO jobPO) throws SCSException {
    StringBuffer sql = new StringBuffer("UPDATE T_SCS_ASYNCJOB_VDC SET JOBSTATE ='" + JobStateVDC.NOT_PASS_RUN.getValue()
        + "', ERROR_CODE=?,ERROR_CODE_DESC=? WHERE UNIQUE_ID =? ");
    int index = 0;
    List<Object> args = new ArrayList<Object>();
    try {
      args.add(jobPO.getError_code());
      args.add(jobPO.getError_code_desc());
      args.add(jobPO.getUnique_id());
      log.info("@@@@@@@@ SQL=" + sql.toString() + "," + args);
      index = jt.update(sql.toString(), args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_UPDATE_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#deleteAsynJobVDC
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public int deleteAsyncJobVDC(AsyncJobVDCPO jobPO) throws SCSException {
    StringBuffer sql = new StringBuffer("DELETE T_SCS_ASYNCJOB_VDC WHERE USER_ID=? ");
    int index = 0;
    List<Object> args = initInsertData(jobPO);
    args.add(jobPO.getUser_id());
    if (jobPO.getOrder_id() != 0) {
      sql.append("AND ORDER_ID =?");
      args.add(jobPO.getOrder_id());
    }
    if (jobPO.getInstance_info_id() != 0) {
      sql.append("AND INSTANCE_INFO_ID =?");
      args.add(jobPO.getInstance_info_id());
    }
    if (jobPO.getTemplate_id() != 0) {
      sql.append("AND TEMPLATE_ID =?");
      args.add(jobPO.getTemplate_id());
    }
    if (jobPO.getFirewall_rule_id() != 0) {
      sql.append("AND FIREWALL_RULE_ID =?");
      args.add(jobPO.getFirewall_rule_id());
    }
    log.info("@@@@@@@@ SQL=" + sql.toString() + "," + args);
    try {
      index = jt.update(sql.toString(), args.toArray());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_DELETE_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_DELETE_ASYNCJOB_DESC);
    }
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.task.vdc.dao.AsynJobVDCDao#queryAsynJobList
   * (com.skycloud.management.portal.task.vdc.dao.po.AsynJobVDCPO)
   */
  @Override
  public List<AsyncJobVDCPO> queryAsyncJobList(AsyncJobVDCPO jobPO) throws SCSException {
    BeanPropertyRowMapper<AsyncJobVDCPO> argTypes = new BeanPropertyRowMapper<AsyncJobVDCPO>(AsyncJobVDCPO.class);
    List<AsyncJobVDCPO> list = new ArrayList<AsyncJobVDCPO>();
    Object[] args = new Object[1];
    args[0] = jobPO.getJobstate().getValue();
    StringBuffer sql = new StringBuffer(" ");
    sql.append("SELECT UNIQUE_ID,USER_ID,ORDER_ID,INSTANCE_INFO_ID,INSTANCE_INFO_IRI_ID,TEMPLATE_ID,");
    sql.append("TEMPLATE_RES_ID,FIREWALL_RULE_ID,OPERATION,PARAMETER,RESP_CODE,RESP_PARAMETER,ERROR_CODE,");
    sql.append("COMMENT FROM T_SCS_ASYNCJOB_VDC WHERE JOBSTATE=? AND AUDITSTATE IN ('");
    sql.append(AuditStateVDC.NO_AUDIT.getValue());
    sql.append("','");
    sql.append(AuditStateVDC.PASS_AUDIT.getValue());
    sql.append("') LIMIT 0,");
    sql.append(TaskUtils.getQueryCount());

    log.info("==========VDC JOB Query wait execute sql=" + sql.toString() + ", JOBSTATE =" + jobPO.getJobstate().getValue());
    try {
      list = jt.query(sql.toString(), args, argTypes);
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_ASYNCJOB_ERROR, SCSErrorCode.DB_SQL_QUERY_ASYNCJOB_DESC);
    }
    return list;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.dao.AsyncJobVDCDao#
   * insertAysncJobHistory()
   */
  @Override
  public int insertAysncJobHistory() throws SCSException {
    StringBuffer sql = new StringBuffer(" ");
    sql.append(" INSERT INTO T_SCS_ASYNCJOB_VDC_HISTORY ");
    sql.append("SELECT UNIQUE_ID,USER_ID,ORDER_ID,INSTANCE_INFO_ID,INSTANCE_INFO_IRI_ID,TEMPLATE_ID,TEMPLATE_RES_ID,");
    sql.append("FIREWALL_RULE_ID,OPERATION,PARAMETER,JOBSTATE,AUDITSTATE,RESP_CODE,RESP_PARAMETER,ERROR_CODE,ERROR_CODE_DESC,");
    sql.append("CREATE_DT,`COMMENT` FROM T_SCS_ASYNCJOB_VDC WHERE JOBSTATE IN ('");
    sql.append(JobStateVDC.PASS_RUN.getValue());
    sql.append("','");
    sql.append(JobStateVDC.NOT_PASS_RUN.getValue());
    sql.append("')");
    int index = 0;
    try {
      index = jt.update(sql.toString());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_VDC_HISTORY_ASYNCJOB_ERROR,
          SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_VDC_HISTORY_ASYNCJOB_DESC);
    }
    log.info("==========VDC JOB Data Archiving sql = " + sql.toString() + ",index=" + index);
    return index;
  }

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.task.vdc.dao.AsyncJobVDCDao#
   * deleteAysncJobHistory()
   */
  @Override
  public int deleteAysncJobHistory() throws SCSException {
    StringBuffer sql = new StringBuffer(" ");
    sql.append("DELETE T_SCS_ASYNCJOB_VDC FROM T_SCS_ASYNCJOB_VDC WHERE UNIQUE_ID IN (SELECT UNIQUE_ID FROM T_SCS_ASYNCJOB_VDC_HISTORY)");
    int index = 0;
    try {
      index = jt.update(sql.toString());
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_DELETE_ASYNCJOB_VDC_HISTORY_ASYNCJOB_ERROR,
          SCSErrorCode.DB_SQL_DELETE_ASYNCJOB_VDC_HISTORY_ASYNCJOB_DESC);
    }
    log.info("==========VDC JOB Data Archiving sql =" + sql.toString() + ",index=" + index);
    return index;
  }

  public JdbcTemplate getJt() {
    return jt;
  }

  public void setJt(JdbcTemplate jt) {
    this.jt = jt;
  }

}
