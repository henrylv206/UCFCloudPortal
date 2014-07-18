package com.skycloud.management.portal.front.resources.dao.impl;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.resources.dao.IObjectStorageDao;

public class ObjectStorageDaoImpl extends SpringJDBCBaseDao implements IObjectStorageDao{

	@Override
	public int getOsUserStatus(String username) throws Exception {
		String sql = "SELECT count(0) FROM T_OBS_USER WHERE username=? and STATUS=1";
		int _status = jdbcTemplate.queryForInt(sql,username);
		return _status;
	}

}
