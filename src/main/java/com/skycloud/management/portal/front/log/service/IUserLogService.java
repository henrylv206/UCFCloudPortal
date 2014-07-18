package com.skycloud.management.portal.front.log.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;

public interface IUserLogService {

	int saveLog(TUserLogVO log) throws SCSException;

	int saveLog(TUserLogVO log, TUserBO user) throws SCSException;

	int deleteLog(int id) throws SQLException;

	int updateLog(TUserLogVO log) throws SQLException;

	List<TUserLogVO> searchAllUserLog(PageVO vo) throws SQLException;

	int searchAllUserLogAcount(TUserLogVO vo) throws SQLException;

	TUserLogVO findUserLogById(int id) throws SQLException;

	List<TUserLogVO> searchUserLogByCondition(PageVO vo, String account, Integer type, String moduleName, String startDate, String endDate,
	        String functionName) throws SQLException;

	boolean importLog() throws SQLException;

	String findModuleNames();
	
	public List<TUserLogVO> searchUserLogByCondition(TUserLogVO vo , PageVO po) throws SCSException;
}
