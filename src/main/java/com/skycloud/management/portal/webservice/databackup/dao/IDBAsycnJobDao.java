package com.skycloud.management.portal.webservice.databackup.dao;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public interface IDBAsycnJobDao {
	/**
	 * 根据标识获取异步任务信息
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-11  下午03:50:14
	*/
	public AsyncJobInfo queryAsyncJobInfoById (int id) throws DataAccessException;
	
	public AsyncJobInfo queryAsyncJobInfoByPara (String para) throws DataAccessException;
	
	/**
	 * 根据Comment获取执行中的异步任务信息列表
	 * @param comment
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-11  下午03:50:44
	 */
	public List<AsyncJobInfo> queryAsyncJobInfoOperatingInfoByComment (String comment) throws DataAccessException;
	/**
	 * 根据Comment获取异步任务是否执行未成功的任务
	 * @param comment
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-11  下午04:11:40
	 */
	public List<AsyncJobInfo> queryAsyncJobInfoIsFailInfoByComment (String comment) throws DataAccessException;
}
