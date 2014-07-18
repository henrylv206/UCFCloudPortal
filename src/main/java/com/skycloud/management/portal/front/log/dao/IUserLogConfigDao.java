package com.skycloud.management.portal.front.log.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogConfig;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;

public interface IUserLogConfigDao {

	public int save(TUserLogConfig config) throws SQLException;

	public int delete(int id) throws SQLException;

	public int update(TUserLogConfig config) throws SQLException;

	public int deleteByCondition(TUserLogConfig vo) throws SQLException;

	public TUserLogConfig findUserLogConfigById(int id) throws SQLException;

	public TUserLogConfig findUserLogConfigByKeyName(String keyName) throws SQLException;
	
	public int countUserLogConfigByCondition(TUserLogConfig queryVO) throws SQLException;

	public List<TUserLogConfig> searchAllUserLogConfigByCondition(TUserLogConfig queryVO , PageVO po) throws SQLException;
}
