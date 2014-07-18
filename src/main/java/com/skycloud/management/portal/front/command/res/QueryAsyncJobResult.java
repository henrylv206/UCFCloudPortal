package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * 查询Job状态命令对象
 * @author fengyk
 */
public class QueryAsyncJobResult extends QueryCommand {

  public static final String COMMAND = "queryAsyncJobResult";
  public static final String JOB_ID = "jobid";

  private long jobid;

  public QueryAsyncJobResult(long jobid) {
    super(COMMAND);
    this.setJobid(jobid);
  }

  public long getJobid() {
    return jobid;
  }

  public void setJobid(long jobid) {
    this.jobid = jobid;
    this.setParameter(JOB_ID, jobid);
  }

}
