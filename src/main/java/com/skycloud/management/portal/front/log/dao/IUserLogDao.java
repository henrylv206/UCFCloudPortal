package com.skycloud.management.portal.front.log.dao;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;

public interface IUserLogDao {

	int save(TUserLogVO log) throws SCSException;

	int delete(int id) throws SQLException;

	int update(TUserLogVO log) throws SQLException;

	List<TUserLogVO> searchAllUserLog(PageVO vo) throws SQLException;

	int searchAllUserLogAcount(TUserLogVO vo) throws SQLException;

	TUserLogVO findUserLogById(int id) throws SQLException;

	List<TUserLogVO> searchUserLogByCondition(PageVO vo, String account, Integer type, String moduleName, String startDate, String endDate,
	        String functionName) throws SQLException;

	List<TUserLogVO> getAllUserLog(PageVO vo) throws SQLException;

	int deleteAll(int maxid) throws SQLException;

	List<Map<String, Object>> findModuleNames();
	
	public List<TUserLogVO> searchUserLogByCondition(TUserLogVO vo , PageVO po) throws SQLException;
}
