package com.skycloud.management.portal.webservice.databackup.dao;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;

public interface IDBTanceInfoDao {
	/**
	 * 根据实例标识获取实例对象
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-4  下午04:51:13
	 */
	public TInstanceInfoBO queryInstanceInfoBOByID (int id) throws DataAccessException; 
}
