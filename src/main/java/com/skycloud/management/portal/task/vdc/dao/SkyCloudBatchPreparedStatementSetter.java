/**
 * $Id:$
 */
package com.skycloud.management.portal.task.vdc.dao;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.SqlTypeValue;
import org.springframework.jdbc.core.StatementCreatorUtils;
import org.springframework.util.Assert;

/**
 * @author Your Name
 * @version $Revision$ $Date$
 */
public class SkyCloudBatchPreparedStatementSetter implements BatchPreparedStatementSetter {
  private List<Object[]> argsList;

  public void setValues(PreparedStatement pstmt, int index) throws SQLException {
    if (this.argsList.size() > 0) {
      Object[] args = this.argsList.get(index);
      for (int i = 0; i < args.length; i++) {
        StatementCreatorUtils.setParameterValue(pstmt, i + 1, SqlTypeValue.TYPE_UNKNOWN, null, args[i]);
      }
    }
  }

  @SuppressWarnings("unused")
  private SkyCloudBatchPreparedStatementSetter() {
    super();
  }

  /**
   * 
   * @param sql
   * @param argsList
   */
  public SkyCloudBatchPreparedStatementSetter(List<Object[]> argsList) {
    this.argsList = argsList;
    Assert.notEmpty(argsList, "args for batch can not be empty!");
  }

  public int getBatchSize() {
    return this.argsList.size();
  }

}
