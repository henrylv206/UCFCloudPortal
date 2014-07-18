package com.skycloud.management.portal.webservice.databackup.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.task.vdc.enumtype.SnapshotState;
import com.skycloud.management.portal.webservice.databackup.dao.IDBUSerSnapshotDao;
import com.skycloud.management.portal.webservice.databackup.jdbc.BaseJdbcMysqlDao;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;

public class DBUserSnapshotDao extends BaseJdbcMysqlDao implements IDBUSerSnapshotDao {

	  @Override
	  public UserSnapshot querySnapshotByuser(int userId, int instanceId) throws DataAccessException {
	    // TODO Auto-generated method stub
	    String sql = "SELECT ID,INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,CREATE_DT,STATE,JOB_ID,TYPE, ASYN_ID,VM_BACKUP_ID, OS_TYPE_ID FROM T_SCS_USER_SNAPSHOT WHERE CREATE_USER_ID = "
	        + userId + " AND INSTANCE_INFO_ID =" + instanceId +" order by ID LIMIT 1";
	    return this.queryForObject(sql, UserSnapshot.class);
	  }
	
  @Override
  public List<UserSnapshot> queryTpSnapshotList(UserSnapshot snapshot) throws DataAccessException {
    StringBuffer sql = new StringBuffer();
    sql.append("SELECT TSUS.ID,TSUS.INSTANCE_INFO_ID,TSUS.E_SNAPSHOT_ID,TSUS.STORAGE_SIZE,TSUS.COMMENT,TSUS.CREATE_USER_ID,TSUS.CREATE_DT,TSUS.STATE,TSUS.JOB_ID,TSUS.TYPE,TSUS.ASYN_ID,TSTV.RESOURCE_POOLS_ID");
    sql.append(" FROM T_SCS_USER_SNAPSHOT TSUS ,T_SCS_INSTANCE_INFO TSII,T_SCS_TEMPLATE_VM TSTV WHERE TSUS.INSTANCE_INFO_ID = TSII.ID AND TSII.TEMPLATE_ID = TSTV.ID");
    if (snapshot.getCREATE_USER_ID() != 0) {
      sql.append(" AND TSUS.CREATE_USER_ID = ");
      sql.append(snapshot.getCREATE_USER_ID());
    }
    if (snapshot.getSTATE() != -1) {
      sql.append(" AND TSUS.STATE = ");
      sql.append(snapshot.getSTATE());
    }
    if (snapshot.getASYN_ID() != 0) {
      sql.append(" AND TSUS.ASYN_ID != 0 ");
      sql.append(" limit 100");
    }
    return this.queryForList(sql.toString(), UserSnapshot.class);
  }

  @Override
  public void insertIntoTpSnapshot(UserSnapshot snapshot) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "INSERT INTO T_SCS_USER_SNAPSHOT (INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,STATE,CREATE_DT,JOB_ID,TYPE,ASYN_ID) VALUES (?,?,?,?,?,?,now(),?,?,?)";
    Object[] args = new Object[] { snapshot.getINSTANCE_INFO_ID(), snapshot.getE_SNAPSHOT_ID(), snapshot.getSTORAGE_SIZE(),
        snapshot.getCOMMENT(), snapshot.getCREATE_USER_ID(), snapshot.getSTATE(), snapshot.getJOB_ID(), snapshot.getTYPE(),
        snapshot.getASYN_ID() };
    int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.VARCHAR, Types.INTEGER, Types.INTEGER, Types.INTEGER,
        Types.INTEGER, Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public int insertIntoSnapshot(final UserSnapshot snapshot) {

    KeyHolder keyHolder = new GeneratedKeyHolder();
    final String insertsql = "INSERT INTO T_SCS_USER_SNAPSHOT (INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,STATE,CREATE_DT,JOB_ID,TYPE,ASYN_ID,OS_TYPE_ID) VALUES (?,?,?,?,?,?,now(),?,?,?,?)";
    try {
      this.getJdbcTemplate().update(new PreparedStatementCreator() {
        int i = 1;

        @Override
        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
          PreparedStatement ps = con.prepareStatement(insertsql, Statement.RETURN_GENERATED_KEYS);
          ps.setInt(i++, snapshot.getINSTANCE_INFO_ID());
          ps.setInt(i++, snapshot.getE_SNAPSHOT_ID());
          ps.setInt(i++, snapshot.getSTORAGE_SIZE());
          ps.setString(i++, snapshot.getCOMMENT());
          ps.setInt(i++, snapshot.getCREATE_USER_ID());
          ps.setInt(i++, snapshot.getSTATE());
          ps.setInt(i++, snapshot.getJOB_ID());
          ps.setInt(i++, snapshot.getTYPE());
          ps.setInt(i++, snapshot.getASYN_ID());
          ps.setInt(i++, snapshot.getOS_TYPE_ID());
          return ps;
        }
      }, keyHolder);
    } catch (Exception e) {
      e.printStackTrace();

    }
    return keyHolder.getKey().intValue();

  }

  @Override
  public void updateTpSnapshot(UserSnapshot snapshot) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET E_SNAPSHOT_ID = ?,STATE = ? WHERE JOB_ID = ? AND CREATE_USER_ID = ?";
    Object[] args = new Object[] { snapshot.getE_SNAPSHOT_ID(), snapshot.getSTATE(), snapshot.getJOB_ID(), snapshot.getCREATE_USER_ID() };
    int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER, Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public void updateTpSnapshotJobId(UserSnapshot snapshot) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET JOB_ID = ? WHERE E_SNAPSHOT_ID = ? AND CREATE_USER_ID = ?";
    Object[] args = new Object[] { snapshot.getJOB_ID(), snapshot.getE_SNAPSHOT_ID(), snapshot.getCREATE_USER_ID() };
    int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public void updateTpSnapshotById(int id, int eSnapshotId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET E_SNAPSHOT_ID = ?,STATE = 0 WHERE ID = ? ";
    Object[] args = new Object[] { eSnapshotId, id };
    int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public void deleteTpSnapshotBySnapshotId(int snapshotId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET STATE = " + SnapshotState.DELETED.getValue() + " WHERE E_SNAPSHOT_ID = ?";
    Object[] args = new Object[] { snapshotId };
    int[] argTypes = new int[] { Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  public void deleteTpSnapshotByJobId(int snapshotId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET STATE = " + SnapshotState.DELETED.getValue() + " WHERE JOB_ID = ?";
    Object[] args = new Object[] { snapshotId };
    int[] argTypes = new int[] { Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  public void deleteTpSnapshotById(int id) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "UPDATE T_SCS_USER_SNAPSHOT SET STATE = " + SnapshotState.DELETED.getValue() + " WHERE ID = ?";
    Object[] args = new Object[] { id };
    int[] argTypes = new int[] { Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public void deteteTpSnashotByCreateUser(int createUserId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "DELETE FROM T_SCS_USER_SNAPSHOT WHERE CREATE_USER_ID = ?";
    Object[] args = new Object[] { createUserId };
    int[] argTypes = new int[] { Types.INTEGER };
    this.update(sql, args, argTypes);
  }

  @Override
  public UserSnapshot queryTpSnapshotBySnapshotId(int snapshotId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "SELECT ID,INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,CREATE_DT,STATE,JOB_ID,TYPE, ASYN_ID,VM_BACKUP_ID FROM T_SCS_USER_SNAPSHOT WHERE ID = "
        + snapshotId;
    return this.queryForObject(sql, UserSnapshot.class);
  }
  
  public UserSnapshot querySnapshotId(int snapshotId) throws DataAccessException {
	    // TODO Auto-generated method stub
	    String sql = "SELECT ID,INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,CREATE_DT,STATE,JOB_ID,TYPE, ASYN_ID,VM_BACKUP_ID,OS_TYPE_ID FROM T_SCS_USER_SNAPSHOT WHERE E_SNAPSHOT_ID = "
	        + snapshotId;
	    return this.queryForObject(sql, UserSnapshot.class);
	  }  

  @Override
  public int queryUserSnapshotSum(int createUserId) throws DataAccessException {
    // TODO Auto-generated method stub
    String sql = "SELECT SUM(STORAGE_SIZE) FROM T_SCS_USER_SNAPSHOT WHERE (STATE = " + SnapshotState.AVAILABLE.getValue() + " OR STATE = "
        + SnapshotState.NOAVAILABLE.getValue() + ") AND CREATE_USER_ID = " + createUserId;
    return this.queryForInt(sql);
  }

  @Override
  public List<UserSnapshot> queryTpSnapshotListByIntanceInfoId(int intanceInfoId) throws DataAccessException {
    // TODO Auto-generated method stub
    StringBuffer sql = new StringBuffer(
        "SELECT ID,INSTANCE_INFO_ID,E_SNAPSHOT_ID,STORAGE_SIZE,COMMENT,CREATE_USER_ID,CREATE_DT,STATE,JOB_ID,TYPE,ASYN_ID,VM_BACKUP_ID FROM T_SCS_USER_SNAPSHOT WHERE 1=1 AND STATE = 0 ");
    sql.append(" AND INSTANCE_INFO_ID = ");
    sql.append(intanceInfoId);
    return this.queryForList(sql.toString(), UserSnapshot.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.webservice.databackup.dao.IDBUSerSnapshotDao
   * #deleteTpSnapshotByIdVDC(int)
   */
  @Override
  public int deleteTpSnapshotByIdVDC(int id) throws DataAccessException {
    String sql = "DELETE FROM T_SCS_USER_SNAPSHOT WHERE ID = ?";
    if (id == 0) {
      return 0;
    }
    Object[] args = new Object[] { id };
    int[] argTypes = new int[] { Types.INTEGER };
    return this.update(sql, args, argTypes);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.webservice.databackup.dao.IDBUSerSnapshotDao
   * #
   * updateTpSnapshotByIdVDC(com.skycloud.management.portal.webservice.databackup
   * .po.UserSnapshot)
   */
  @Override
  public int updateTpSnapshotByIdVDC(UserSnapshot snapshot) throws DataAccessException {
    String backupID = snapshot.getVM_BACKUP_ID();
    StringBuffer sql = new StringBuffer();
    sql.append("UPDATE T_SCS_USER_SNAPSHOT SET STATE = ? ");
    List<Object> args = new ArrayList<Object>();
    args.add(snapshot.getSTATE());
    if (snapshot.getSTORAGE_SIZE() == 0 || snapshot.getID() == 0 || StringUtils.isBlank(backupID)) {
      return 0;
    }
    if (snapshot.getSTORAGE_SIZE() != 0) {
      sql.append(" ,STORAGE_SIZE =? ");
      args.add(snapshot.getSTORAGE_SIZE());
    }
    if (StringUtils.isNotBlank(backupID)) {
      sql.append(" ,VM_BACKUP_ID =? ");
      args.add(snapshot.getVM_BACKUP_ID());
    }
    sql.append(" WHERE ID = ? ");
    args.add(snapshot.getID());
    return getJdbcTemplate().update(sql.toString(), args.toArray());
  }

}
