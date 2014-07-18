package com.skycloud.management.portal.admin.audit.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TSMSInfoBO;
import com.skycloud.management.portal.admin.audit.entity.TSendInfoBO;

public interface ISendInfoDao {
	int insertSendInfo(TSendInfoBO info) throws SQLException;
	List<TSendInfoBO> findAllPedding() throws SQLException;
	int updateSendInfo(List<TSendInfoBO> infos) throws SQLException;
	
	/**
	 * 插入发送短信的信息
	 * @param info
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-6-25  下午02:23:30
	 */
	int insertSMSInfo(TSMSInfoBO info) throws SQLException;

}
