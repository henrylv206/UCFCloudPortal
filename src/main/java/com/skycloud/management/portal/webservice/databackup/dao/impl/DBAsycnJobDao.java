package com.skycloud.management.portal.webservice.databackup.dao.impl;

import java.sql.Types;
import java.util.List;

import org.springframework.dao.DataAccessException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.webservice.databackup.dao.IDBAsycnJobDao;
import com.skycloud.management.portal.webservice.databackup.jdbc.BaseJdbcMysqlDao;

public class DBAsycnJobDao extends BaseJdbcMysqlDao implements IDBAsycnJobDao {

	public AsyncJobInfo queryAsyncJobInfoById(int id)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER,RESID,COMMENT FROM T_SCS_ASYNCJOB WHERE ID = ?";
		Object[] args = new Object[]{id};
		int [] argTypes = new int [] {Types.INTEGER};
		return this.queryForObject(sql, args, argTypes, AsyncJobInfo.class);
	}
	
	public AsyncJobInfo queryAsyncJobInfoByPara(String para) throws DataAccessException {
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER,RESID,COMMENT FROM T_SCS_ASYNCJOB where OPERATION in('attachVolume', 'detachVolume') and PARAMETER like ? order by id desc LIMIT 0,1";
		Object[] args = new Object[] { "%\"id\":" + para + "%" };
		int[] argTypes = new int[] { Types.VARCHAR };
		return this.queryForObject(sql, args, argTypes, AsyncJobInfo.class);
	}

	@Override
	public List<AsyncJobInfo> queryAsyncJobInfoOperatingInfoByComment(String comment)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER,RESID,COMMENT FROM T_SCS_ASYNCJOB WHERE COMMENT = ? AND JOBSTATE = 1 ";
		Object[] args = new Object[]{comment};
		int [] argTypes = new int [] {Types.VARCHAR};
		return this.queryForList(sql, args, argTypes, AsyncJobInfo.class);
	}
	@Override
	public List<AsyncJobInfo> queryAsyncJobInfoIsFailInfoByComment(String comment)
			throws DataAccessException {
		String sql = "SELECT ID,INSTANCE_INFO_ID,CREATE_DT,JOBSTATE,OPERATION,PARAMETER,RESID,COMMENT FROM T_SCS_ASYNCJOB WHERE COMMENT = ? AND JOBSTATE = 2 AND RESID = -1";
		Object[] args = new Object[]{comment};
		int [] argTypes = new int [] {Types.VARCHAR};
		return this.queryForList(sql, args, argTypes, AsyncJobInfo.class);
	}
}
