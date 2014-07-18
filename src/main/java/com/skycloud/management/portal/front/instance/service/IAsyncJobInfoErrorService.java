package com.skycloud.management.portal.front.instance.service;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsynJobInfoError;

public interface IAsyncJobInfoErrorService {
	/**
     * 插入任务出错信息表
     * @param asyError
     * @throws RuntimeException
     * 创建人：  刘江宁   
     * 创建时间：2011-11-8  下午02:31:30
     */
	public void insertJobInfoError (AsynJobInfoError asyError) throws ServiceException;
}
