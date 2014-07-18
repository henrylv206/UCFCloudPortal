package com.skycloud.management.portal.front.instance.service.impl;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.dao.IAsyncJobInfoErrorDao;
import com.skycloud.management.portal.front.instance.entity.AsynJobInfoError;
import com.skycloud.management.portal.front.instance.service.IAsyncJobInfoErrorService;


public class AsyncJobInfoErrorService implements IAsyncJobInfoErrorService {
	
	public static Logger logger =  Logger.getLogger(AsyncJobInfoErrorService.class);
	
	private IAsyncJobInfoErrorDao asyncJobInfoErrorDao;
	
	@Override
	public void insertJobInfoError(AsynJobInfoError asyError)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			asyncJobInfoErrorDao.insertJobInfoError(asyError);
		}catch(Exception e){
			logger.error("insert T_SCS_ASYNCJOB_ERROR error :",e);
		}
	}

	public IAsyncJobInfoErrorDao getAsyncJobInfoErrorDao() {
		return asyncJobInfoErrorDao;
	}

	public void setAsyncJobInfoErrorDao(IAsyncJobInfoErrorDao asyncJobInfoErrorDao) {
		this.asyncJobInfoErrorDao = asyncJobInfoErrorDao;
	}

	

}
