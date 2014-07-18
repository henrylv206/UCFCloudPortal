package com.skycloud.management.portal.front.instance.dao.impl;

import java.sql.Timestamp;
import java.sql.Types;
import org.springframework.dao.DataAccessException;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoErrorDao;
import com.skycloud.management.portal.front.instance.entity.AsynJobInfoError;

public class AsyncJobInfoErrorDao extends SpringJDBCBaseDao implements IAsyncJobInfoErrorDao {
	
	@Override
	public void insertJobInfoError(AsynJobInfoError asyError)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "INSERT INTO T_SCS_ASYNCJOB_ERROR (ASY_ID , DESCRIPTION, CREATE_DT) VALUES (?,?,?)";
		Object[] args = new Object[]{asyError.getASY_ID(),asyError.getDESCRIPTION(),new Timestamp(System.currentTimeMillis())};
		int[] argTypes = new int[]{Types.INTEGER,Types.VARCHAR,Types.TIMESTAMP};
		getJdbcTemplate().update(sql, args, argTypes);		
	}
}
