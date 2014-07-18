package com.skycloud.management.portal.webservice.databackup.service.impl;

import java.util.List;

import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.webservice.databackup.dao.IDBAsycnJobDao;
import com.skycloud.management.portal.webservice.databackup.service.IDBAsycnJobService;

public class DBAsycnJobService extends BaseService implements
		IDBAsycnJobService { 

	private IDBAsycnJobDao dbAsycnJobDao ;
	@Override
	public List<AsyncJobInfo> queryAsyncJobInfoOperatingInfoByComment(String comment)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbAsycnJobDao.queryAsyncJobInfoOperatingInfoByComment(comment);
		}catch(ServiceException e){
			logger.error("Execute [DBAsycnJobService] method : queryAsyncJobInfoOperatingInfoByComment Exception : ",e);
			throw new ServiceException("查询异步任务是否有未完成的情况异常");
		}
	}
	@Override
	public List<AsyncJobInfo> queryAsyncJobInfoIsFailInfoByComment(String comment) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbAsycnJobDao.queryAsyncJobInfoIsFailInfoByComment(comment);
		}catch(ServiceException e){
			logger.error("Execute [DBAsycnJobService] method : queryAsyncJobInfoIsFailInfoByComment Exception : ",e);
			throw new ServiceException("查询异步任务完成是否有失败的情况异常");
		}
	}
	@Override
	public AsyncJobInfo queryAsyncJobInfoById(int id) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbAsycnJobDao.queryAsyncJobInfoById(id);
		}catch(ServiceException e){
			logger.error("Execute [DBAsycnJobService] method : queryAsyncJobInfoById Exception :",e);
			throw new ServiceException("根据异步任务唯一标识获取对象实例异常");
		}
	}
	
	public AsyncJobInfo queryAsyncJobInfoByPara(String para) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbAsycnJobDao.queryAsyncJobInfoByPara(para);
		}catch(ServiceException e){
			logger.error("Execute [DBAsycnJobService] method : queryAsyncJobInfoById Exception :",e);
			throw new ServiceException("根据异步任务唯一标识获取对象实例异常");
		}
	}	
	
	public IDBAsycnJobDao getDbAsycnJobDao() {
		return dbAsycnJobDao;
	}
	public void setDbAsycnJobDao(IDBAsycnJobDao dbAsycnJobDao) {
		this.dbAsycnJobDao = dbAsycnJobDao;
	}
	
}
