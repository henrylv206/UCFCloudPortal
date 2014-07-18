package com.skycloud.management.portal.webservice.databackup.dao.impl;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.webservice.databackup.dao.IDBTanceInfoDao;
import com.skycloud.management.portal.webservice.databackup.jdbc.BaseJdbcMysqlDao;

public class DBTIntanceInfodao extends BaseJdbcMysqlDao implements IDBTanceInfoDao {

	@Override
	public TInstanceInfoBO queryInstanceInfoBOByID(int id)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID,ORDER_ID,TEMPLATE_TYPE,TEMPLATE_ID,COMMENT,RESOURCE_INFO,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,ZONE_ID,CLUSTER_ID,CREATE_DT,STATE,LASTUPDATE_DT,E_INSTANCE_ID,E_SERVICE_ID,E_DISK_ID,E_NETWORK_ID,E_OS_ID,OS_DESC,INSTANCE_NAME FROM T_SCS_INSTANCE_INFO WHERE ID = "+id;
		return this.queryForObject(sql, TInstanceInfoBO.class);
	}

}
