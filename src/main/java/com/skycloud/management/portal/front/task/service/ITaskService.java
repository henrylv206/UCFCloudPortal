package com.skycloud.management.portal.front.task.service;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public interface ITaskService {
	
    public static final int isNewJob = 0;
	
	/**
	 * 执行Elaster任务，返回执行结果
	 * @param jobInfo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午02:38:34
	 */
	public AsyncJobInfo executeElasterJobInfoGetResult(AsyncJobInfo jobInfo) throws ServiceException;
	/**
	 * 执行小机任务，返回执行结果
	 * @param jobInfo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午02:39:48
	 */
	public AsyncJobInfo executeMiniComputerJobInfoGetResult (AsyncJobInfo jobInfo) throws ServiceException;
}
