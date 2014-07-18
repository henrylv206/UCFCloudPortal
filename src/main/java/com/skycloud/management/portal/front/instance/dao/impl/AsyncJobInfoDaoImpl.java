package com.skycloud.management.portal.front.instance.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoDAO;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Ipsan;
import com.skycloud.management.portal.front.instance.entity.Iri;

/**
 * @author fengyk
 * @date 2011.11.21
 */
public class AsyncJobInfoDaoImpl extends SpringJDBCBaseDao implements
		IAsyncJobInfoDAO {
	private static Log log = LogFactory.getLog(AsyncJobInfoDaoImpl.class);

	/**
	 * 查找命令级job记录、通过applyid与创建级命令区分
	 */
	public List<AsyncJobInfo> queryPendingAsyncJobs() throws Exception {
		BeanPropertyRowMapper<AsyncJobInfo> argTypes = new BeanPropertyRowMapper<AsyncJobInfo>(
				AsyncJobInfo.class);
		//version 1
//		String sql = "select ttt.ORDER_ID,ttt.ID,tt.INSTANCE_INFO_ID,ttt.CREATE_DT,ttt.JOBSTATE,ttt.OPERATION,ttt.PARAMETER,ttt.JOBID,ttt.`COMMENT` from (select DISTINCT t.INSTANCE_INFO_ID from T_SCS_ASYNCJOB t  where t.JOBSTATE=1 and t.APPLY_ID=1 limit 5 ) tt "
//				+ "left join T_SCS_ASYNCJOB ttt on tt.INSTANCE_INFO_ID=ttt.INSTANCE_INFO_ID where ttt.JOBSTATE= 1  order by ttt.ID ASC";
		StringBuffer sql = new StringBuffer();
		sql.append("select ttt.ORDER_ID,ttt.ID,ttt.INSTANCE_INFO_ID,ttt.CREATE_DT,ttt.JOBSTATE,ttt.OPERATION,ttt.PARAMETER,ttt.JOBID,ttt.`COMMENT` from");
		sql.append(" T_SCS_ASYNCJOB ttt where ttt.JOBSTATE= 1 and ttt.APPLY_ID=1 order by ttt.ID ASC limit 100 ");
		List<AsyncJobInfo> result = null;
		try {
			result = this.getJdbcTemplate().query(sql.toString(), argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}

		return result;
	}

	public List<AsyncJobInfo> queryAsyncJobInfoByIdAndState(int intanceId,
			int state) throws Exception {
		BeanPropertyRowMapper<AsyncJobInfo> argTypes = new BeanPropertyRowMapper<AsyncJobInfo>(
				AsyncJobInfo.class);
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER FROM T_SCS_ASYNCJOB  WHERE INSTANCE_INFO_ID = ? AND JOBSTATE = ? AND OPERATION <> 'attachVolume'";
		Object[] args = new Object[] { intanceId, state };
		List<AsyncJobInfo> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}

	public AsyncJobInfo queryAsyncJobByIntanceInfoId(int intanceInfoId)
			throws Exception {
		BeanPropertyRowMapper<AsyncJobInfo> argTypes = new BeanPropertyRowMapper<AsyncJobInfo>(
				AsyncJobInfo.class);
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER,RESID FROM T_SCS_ASYNCJOB  where INSTANCE_INFO_ID = ? and (OPERATION = 'deployVirtualMachine' or OPERATION = 'createVolume' or OPERATION = 'changeServiceForVirtualMachine' or OPERATION='destroyVirtualMachine')";
		Object[] args = new Object[] { intanceInfoId };
		List<AsyncJobInfo> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		if (result != null) {
			return (AsyncJobInfo) result.get(0);
		}
		return null;
	}

	@Override
	public AsyncJobInfo queryAsyncJobById(AsyncJobInfo asyncJobInfo)
			throws Exception {
		BeanPropertyRowMapper<AsyncJobInfo> argTypes = new BeanPropertyRowMapper<AsyncJobInfo>(
				AsyncJobInfo.class);
		String sql = "SELECT * FROM T_SCS_ASYNCJOB  where ID = ? ";
		Object[] args = new Object[] { asyncJobInfo.getID() };
		AsyncJobInfo result = null;
		try {
			result = this.getJdbcTemplate().queryForObject(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}

	public void updateAsyncJobInfo(final AsyncJobInfo asyncJobInfo)
			throws Exception {
		String sql = "update T_SCS_ASYNCJOB set JOBSTATE = ?,JOBID = ?";
		try {
			this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;

				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, asyncJobInfo.getJOBSTATE());
					ps.setInt(i++, asyncJobInfo.getJOBID());

				}
			});
		} catch (Exception e) {
			log.error("update asyncJobInfo error:" + e.getMessage());
			throw new SQLException("update asyncJobInfo error："
					+ e.getMessage());
		}

	}

	public int insertAsyncJob(final AsyncJobInfo asyncJobInfo)
			throws SCSException {
		int index = 0;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "insert into T_SCS_ASYNCJOB (ID,INSTANCE_INFO_ID,OPERATION,PARAMETER,CREATE_DT,COMMENT,JOBSTATE,ORDER_ID,APPLY_ID) values (?,?,?,?,?,?,?,?,?)";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator() {

				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql,
							Statement.RETURN_GENERATED_KEYS);
					ps.setInt(1, asyncJobInfo.getID());
					ps.setInt(2, asyncJobInfo.getINSTANCE_INFO_ID());
					ps.setString(3, asyncJobInfo.getOPERATION());
					ps.setString(4, asyncJobInfo.getPARAMETER());
					ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
					ps.setString(6, asyncJobInfo.getCOMMON());
					ps.setInt(7, asyncJobInfo.getJOBSTATE());
					ps.setInt(8, asyncJobInfo.getORDER_ID());
					ps.setInt(9, asyncJobInfo.getAPPLY_ID());
					
					return ps;
				}
			}, keyHolder);
			index = keyHolder.getKey().intValue();
			// index = this.getJdbcTemplate().update(sql, args.toArray());

		} catch (Exception e) {
			e.printStackTrace();
			log.error(e);
			throw new SCSException(
					SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_COUNT_ERROR,
					SCSErrorCode.DB_SQL_INSERT_ASYNCJOB_COUNT_DESC);
		}
		return index;
	}

	@Override
	public void updatebatchAsyncJobInfo(List<AsyncJobInfo> asyJobInfos)
			throws Exception {
		final List<AsyncJobInfo> infos = asyJobInfos;

		String sql = "insert into T_SCS_ASYNCJOB (INSTANCE_INFO_ID,OPERATION,PARAMETER,CREATE_DT,COMMENT,JOBSTATE,ORDER_ID,APPLY_ID) values (?,?,?,?,?,?,?,?)";
		BatchPreparedStatementSetter pss = new BatchPreparedStatementSetter() {

			public int getBatchSize() {
				return infos.size();
			}

			public void setValues(PreparedStatement ps, int i)
					throws SQLException {
				AsyncJobInfo asyncinfo = infos.get(i);
				ps.setInt(1, asyncinfo.getINSTANCE_INFO_ID());
				ps.setString(2, asyncinfo.getOPERATION());
				ps.setString(3, asyncinfo.getPARAMETER());
				ps.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
				ps.setString(5, asyncinfo.getCOMMON());
				ps.setInt(6, asyncinfo.getJOBSTATE());
				ps.setInt(7, asyncinfo.getORDER_ID());
				ps.setInt(8, asyncinfo.getAPPLY_ID());

			}
		};
		try {
			this.getJdbcTemplate().batchUpdate(sql, pss);
		} catch (Exception e) {
			log.error("batchUpdateAsyncJobInfo error:" + e.getMessage());
			throw new Exception("batchUpdateAsyncJobInfo error:"
					+ e.getMessage());
		}

	}

	@Override
	public int updateiriInfoforVolume(final Iri iri) throws Exception {
		String sql = "select id from T_SCS_IRI where DISK_INSTANCE_INFO_ID=? and VM_INSTANCE_INFO_ID=?";
		Object[] args1 = new Object[] { iri.getDISK_INSTANCE_INFO_ID(),
				iri.getVM_INSTANCE_INFO_ID() };
		List<Map<String, Object>> list = this.getJdbcTemplate().queryForList(
				sql, args1);
		if (list.size() <= 0) {
			KeyHolder keyHolder = new GeneratedKeyHolder();
			final String insertsql = "insert into T_SCS_IRI (VM_INSTANCE_INFO_ID,DISK_INSTANCE_INFO_ID,CREATE_DT,CREATE_USER_ID,STATE) values(?,?,?,?,?)";
			try {
				this.getJdbcTemplate().update(new PreparedStatementCreator() {
					int i = 1;

					@Override
					public PreparedStatement createPreparedStatement(
							Connection con) throws SQLException {
						PreparedStatement ps = con.prepareStatement(insertsql,
								Statement.RETURN_GENERATED_KEYS);
						ps.setInt(i++, iri.getVM_INSTANCE_INFO_ID());
						ps.setInt(i++, iri.getDISK_INSTANCE_INFO_ID());
						ps.setTimestamp(i++, new Timestamp(iri.getCREATE_DT()
								.getTime()));
						ps.setInt(i++, iri.getCREATE_USER_ID());
						ps.setInt(i++, iri.getSTATE());
						return ps;
					}
				}, keyHolder);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("insert iri error:" + e.getMessage());
				throw new SQLException("insert iri error: " + e.getMessage());
			}
			return keyHolder.getKey().intValue();
		} else {

			String updatesql = "update T_SCS_IRI set state=?,CREATE_DT=? where DISK_INSTANCE_INFO_ID =? and VM_INSTANCE_INFO_ID=?";
			Object[] args = new Object[] { iri.getSTATE(), iri.getCREATE_DT(),
					iri.getDISK_INSTANCE_INFO_ID(),
					iri.getVM_INSTANCE_INFO_ID() };
			try {
				this.getJdbcTemplate().update(updatesql, args);
			} catch (Exception e) {
				e.printStackTrace();
				log.error("update iri error:" + e.getMessage());
				throw new SQLException("update iri error: " + e.getMessage());
			}
			return Integer.parseInt(list.get(0).get("id").toString());

		}

	}

	public void updateJobIdByJobInfo(AsyncJobInfo jobInfo)
			throws DataAccessException {
		String sql = "UPDATE T_SCS_ASYNCJOB SET JOBID = ? ,PARAMETER = ? WHERE ID = ?";
		Object[] args = new Object[] { jobInfo.getJOBID(),
				jobInfo.getPARAMETER(), jobInfo.getID() };
		int[] argTypes = new int[] { Types.INTEGER, Types.VARCHAR,
				Types.INTEGER };
		getJdbcTemplate().update(sql, args, argTypes);
	}

	public void updateResIdAndStateByJobInfo(AsyncJobInfo jobInfo)
			throws DataAccessException {
		String sql = "UPDATE T_SCS_ASYNCJOB SET RESID = ? ,JOBSTATE = ? WHERE ID = ?";
		Object[] args = new Object[] { jobInfo.getRESID(),
				jobInfo.getJOBSTATE(), jobInfo.getID() };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER,
				Types.INTEGER };
		getJdbcTemplate().update(sql, args, argTypes);
	}

	public void updateTScsIriStateById(int state, int id)
			throws DataAccessException {
		String sql = "UPDATE T_SCS_IRI SET STATE = ? WHERE ID = ?";
		Object[] args = new Object[] { state, id };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		getJdbcTemplate().update(sql, args, argTypes);
	}

	public Iri queryIriPoById(int id) throws DataAccessException {
		String sql = "SELECT ID ID, VM_INSTANCE_INFO_ID VM_INSTANCE_INFO_ID,DISK_INSTANCE_INFO_ID DISK_INSTANCE_INFO_ID,CREATE_DT CREATE_DT,STATE STATE FROM T_SCS_IRI WHERE ID = ?";
		Object[] args = new Object[] { id };
		int[] argTypes = new int[] { Types.INTEGER };
		BeanPropertyRowMapper<Iri> classTypes = new BeanPropertyRowMapper<Iri>(
				Iri.class);
		return getJdbcTemplate()
				.queryForObject(sql, args, argTypes, classTypes);
	}
	
	public List<Iri> queryIriPoByVMId(int vmid) throws Exception {
		BeanPropertyRowMapper<Iri> argTypes = new BeanPropertyRowMapper<Iri>(Iri.class);
		String sql = "SELECT ID ID, VM_INSTANCE_INFO_ID VM_INSTANCE_INFO_ID,DISK_INSTANCE_INFO_ID DISK_INSTANCE_INFO_ID,CREATE_DT CREATE_DT,STATE STATE FROM T_SCS_IRI WHERE VM_INSTANCE_INFO_ID = ? and STATE = 2 ";
		Object[] args = new Object[] { vmid };
		List<Iri> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}	
	
	public List<Iri> queryIPsanBindVM(int vmid) throws Exception {
		BeanPropertyRowMapper<Iri> argTypes = new BeanPropertyRowMapper<Iri>(Iri.class);
		String sql = "SELECT ID ID, VM_INSTANCE_INFO_ID VM_INSTANCE_INFO_ID,DISK_INSTANCE_INFO_ID DISK_INSTANCE_INFO_ID,CREATE_DT CREATE_DT,STATE STATE FROM T_SCS_IRI WHERE DISK_INSTANCE_INFO_ID = ? and STATE = 2 ";
		Object[] args = new Object[] { vmid };
		List<Iri> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}		
	
	//to fix bug [4011]
	public List<Ipsan> queryIpsanByVMId(int instanceId) throws Exception {
		BeanPropertyRowMapper<Ipsan> argTypes = new BeanPropertyRowMapper<Ipsan>(Ipsan.class);
		String sql = "SELECT BSID FROM T_EBS_RESOURCE WHERE VM_ID = ? ";
		Object[] args = new Object[] { instanceId };
		List<Ipsan> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}		
	
	public List<Iri> queryIriPoByVMIding(int vmid) throws Exception {
		BeanPropertyRowMapper<Iri> argTypes = new BeanPropertyRowMapper<Iri>(Iri.class);
		String sql = "SELECT ID ID, VM_INSTANCE_INFO_ID VM_INSTANCE_INFO_ID,DISK_INSTANCE_INFO_ID DISK_INSTANCE_INFO_ID,CREATE_DT CREATE_DT,STATE STATE FROM T_SCS_IRI WHERE VM_INSTANCE_INFO_ID = ? and STATE = 5 ";
		Object[] args = new Object[] { vmid };
		List<Iri> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}	
	
	public List<TPublicIPBO> queryIPByinstanceId(int instanceId) throws Exception {
		BeanPropertyRowMapper<TPublicIPBO> argTypes = new BeanPropertyRowMapper<TPublicIPBO>(TPublicIPBO.class);
		String sql = "SELECT ID FROM T_SCS_PUBLIC_IP WHERE INSTANCE_INFO_ID = ? and STATUS = 1 ";
		Object[] args = new Object[] { instanceId };
		List<TPublicIPBO> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			log.error("query asyncJobInfo error:" + e.getMessage());
			throw new SQLException("query asyncJobInfo error:" + e.getMessage());
		}
		return result;
	}	
	
	public void updateJobInfoByJobPo(AsyncJobInfo jobInfo)
			throws DataAccessException {

	}
}
