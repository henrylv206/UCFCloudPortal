package com.skycloud.management.portal.webservice.databackup.service;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;

public interface IDBIntanceInfoService {
	/**
	 * 根据实例标识获取实例对象
	 * @param id
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午02:29:08
	 */
	public TInstanceInfoBO queryInstanceInfoBOByID (int id) throws ServiceException;
}
