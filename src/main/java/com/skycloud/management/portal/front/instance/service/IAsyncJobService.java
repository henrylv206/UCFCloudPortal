package com.skycloud.management.portal.front.instance.service;

import java.util.List;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Iri;


public interface IAsyncJobService {

	public List<AsyncJobInfo> queryAsyncJobInfos() throws Exception;

	public void updateAsyncJobInfo(AsyncJobInfo asyJobInfo)
			throws Exception;

	public void queryAsyncInfoToQueue() throws Exception;
	
	public List<AsyncJobInfo> queryAsyncJobInfoByIdAndState (int intanceId , int state) throws Exception ; 

	public AsyncJobInfo queryAsyncJobByIntanceInfoId (int intanceInfoId) throws Exception;
	
	/**
	 * 更新任务记录的JobId
	 * @param jobInfo
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午03:04:17
	 */
	public void updateJobIdByJobInfo (AsyncJobInfo jobInfo) throws ServiceException;
	
	/**
	 * 更新任务记录的ResId并且回写任务状态
	 * @param jobInfo
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午03:05:04
	 */
	public void updateResIdAndStateByJobInfo (AsyncJobInfo jobInfo) throws ServiceException;
	/**
	 * 通过主键标识修改Iri状态
	 * @param state
	 * @param id
	 * @throws ServiceException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-29  下午02:46:48
	 */
	public void updateTScsIriStateById (int state , int id) throws ServiceException;
	
	/**
	 * 根据Id查询Iri表记录
	 * @param id
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-12-1  下午02:33:45
	 */
	public Iri queryIriPoById (int id) throws ServiceException;
	public int queryIriPoByVMId (int vmid) throws Exception;
	public List<Iri> queryIriListByVMId (int vmid) throws Exception;
	public List<Iri> queryIPsanBindVM (int vmid) throws Exception;
	public int queryIriPoByVMIding (int vmid) throws Exception;
	public int queryVMforTIP (int instanceId) throws Exception;
	//to fix bug [4011]
	public int getBindIPsan (int instanceId) throws Exception;
}
