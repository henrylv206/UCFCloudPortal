package com.skycloud.management.portal.webservice.databackup.service.impl;

import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.webservice.databackup.dao.IDBTanceInfoDao;
import com.skycloud.management.portal.webservice.databackup.service.IDBIntanceInfoService;

public class DBIntanceInfoService extends BaseService implements
		IDBIntanceInfoService {

	private IDBTanceInfoDao dbIntanceInfoDao;
	@Override
	public TInstanceInfoBO queryInstanceInfoBOByID(int id)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbIntanceInfoDao.queryInstanceInfoBOByID(id);
		}catch(ServiceException e){
			logger.error("Execute [DBIntanceInfoService] method: queryInstanceInfoBOByID Exception:",e);
			throw new ServiceException("根据唯一标识获取块存储实例对象异常");
		}
	}
	public IDBTanceInfoDao getDbIntanceInfoDao() {
		return dbIntanceInfoDao;
	}
	public void setDbIntanceInfoDao(IDBTanceInfoDao dbIntanceInfoDao) {
		this.dbIntanceInfoDao = dbIntanceInfoDao;
	}
}
