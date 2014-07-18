package com.skycloud.management.portal.front.resources.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.front.resources.dao.IVolumeHistoryDao;
import com.skycloud.management.portal.front.resources.dao.bo.VolumeHistoryBO;

public class VolumeHistoryDaoImpl implements IVolumeHistoryDao {

  private JdbcTemplate jdbcTemplate;

  @Override
  public void insertVolumeHistory(final VolumeHistoryBO volumeHistory) throws Exception {
    String sql = "INSERT INTO T_SCS_VOLUME_RESTORE_HISTORY(USER_ID, USER_NAME, E_DISK_ID, REMOVE_DT, INSTANCE_ID,INSTANCE_NAME) VALUES (?, ?, ?, ?, ?, ? )";
    jdbcTemplate.update(sql, new PreparedStatementSetter() {
      @Override
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setInt(1, volumeHistory.getUSER_ID());
        ps.setString(2, volumeHistory.getUSER_NAME());
        ps.setInt(3, volumeHistory.getE_DISK_ID());
        ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
        ps.setString(5, volumeHistory.getINSTANCE_ID());
        ps.setString(6, volumeHistory.getINSTANCE_NAME());
      }
    });
  }

  @Override
  public void updateVolumeHistoryState(final VolumeHistoryBO volumeHistory) throws Exception {
    String sql = "UPDATE T_SCS_VOLUME_RESTORE_HISTORY SET SUCCESS = ? where INSTANCE_ID = ? ";
    jdbcTemplate.update(sql, new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, volumeHistory.getSUCCESS());
        ps.setString(2, volumeHistory.getINSTANCE_ID());
      }
    });
  }
  
  @Override
  public void deleteVolumeHistory(VolumeHistoryBO volumeHistory) throws Exception {
    String sql = "DELETE  FROM T_SCS_VOLUME_RESTORE_HISTORY  WHERE INSTANCE_ID=?  AND SUCCESS ='N'";
    try {
       jdbcTemplate.update(sql, new Object[] { volumeHistory.getINSTANCE_ID() });
    }
    catch (Exception e) {
      throw new Exception("delete T_SCS_VOLUME_RESTORE_HISTORY where INSTANCE_ID=" + volumeHistory.getINSTANCE_ID() + " error:" + e.getMessage());
    }
  }

  public JdbcTemplate getJdbcTemplate() {
    return jdbcTemplate;
  }

  public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

}
