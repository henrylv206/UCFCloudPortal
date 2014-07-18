package com.skycloud.management.portal.webservice.databackup.service;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public interface IDBAsycnJobService {
	/**
	 * 根据Comment获取异步任务信息列表
	 * @param comment
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-11  下午03:50:44
	 */
	public List<AsyncJobInfo> queryAsyncJobInfoOperatingInfoByComment (String comment) throws ServiceException ;
	/**
	 * 根据Comment获取异步任务是否执行未成功的任务
	 * @param comment
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-11  下午04:16:22
	 */
	public List<AsyncJobInfo> queryAsyncJobInfoIsFailInfoByComment(String comment) throws ServiceException ;
	/**
	 * 根据异步任务唯一标识获取对象实例
	 * @param id
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-2-3  上午10:20:08
	 */
	public AsyncJobInfo queryAsyncJobInfoById(int id) throws ServiceException ;
	
	public AsyncJobInfo queryAsyncJobInfoByPara(String para) throws ServiceException ;
	
}
