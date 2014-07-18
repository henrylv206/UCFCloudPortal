package com.skycloud.management.portal.front.instance.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.instance.entity.Ipsan;
import com.skycloud.management.portal.front.instance.entity.Iri;


public interface IAsyncJobInfoDAO {
	
	/**
	 * 查找待处理状态job信息
	 * @return
	 * @throws Exception
	 */
	public List<AsyncJobInfo> queryPendingAsyncJobs() throws Exception;
	
	/**
	 * 通过状态查找job信息
	 * @param intanceId
	 * @param state
	 * @return
	 * @throws Exception
	 */
	public List<AsyncJobInfo> queryAsyncJobInfoByIdAndState (int intanceId , int state) throws Exception ; 

	/**
	 * 通过instanceId查找与之对应的信息
	 * @param intanceInfoId
	 * @return
	 * @throws Exception
	 */
	public AsyncJobInfo queryAsyncJobByIntanceInfoId (int intanceInfoId) throws Exception;
	
	/**
	 * 通过Id查找job的信息
	 * @param intanceInfoId
	 * @return
	 * @throws Exception
	 */
	public AsyncJobInfo queryAsyncJobById(AsyncJobInfo asyncJobInfo) throws Exception;
	
	/**
	 * 更新asyncJobInfo信息
	 * @param asyncJobInfo
	 * @throws Exception
	 */
	public void updateAsyncJobInfo(AsyncJobInfo asyncJobInfo) throws Exception;
	/**
	 * 批量更新asyncJobinfo
	 * @param asyJobInfos
	 * @throws Exception
	 */
	public void updatebatchAsyncJobInfo(List<AsyncJobInfo> asyJobInfos) throws Exception;
	
	/**
	 * 更新或新增iri表信息
	 * @param iri
	 * @throws Exception
	 */
	public int updateiriInfoforVolume(Iri iri)  throws Exception;
	
	public int insertAsyncJob(AsyncJobInfo asyncJobInfo) throws SCSException; 
	/**
	 * 更新任务记录信息
	 * @param jobInfo
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午03:04:17
	 */
	public void updateJobIdByJobInfo (AsyncJobInfo jobInfo) throws DataAccessException;
	
	/**
	 * 更新任务记录的ResId并且回写任务状态
	 * @param jobInfo
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午03:05:04
	 */
	public void updateResIdAndStateByJobInfo (AsyncJobInfo jobInfo) throws DataAccessException;
	/**
	 * 通过主键标识修改Iri状态
	 * @param state
	 * @param id
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-29  下午02:44:33
	 */
	public void updateTScsIriStateById (int state , int id) throws DataAccessException;
	/**
	 * 根据Id查询Iri表记录
	 * @param id
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁   
	 * 创建时间：2011-12-1  上午11:24:37
	 */
	public Iri queryIriPoById (int id) throws DataAccessException;
	
	public List<Iri> queryIriPoByVMId (int vmid) throws Exception; 
	public List<Iri> queryIPsanBindVM (int vmid) throws Exception; 
	public List<Iri> queryIriPoByVMIding (int vmid) throws Exception;
	public List<TPublicIPBO> queryIPByinstanceId (int instanceId) throws Exception; 
	//to fix bug [4011]
	public List<Ipsan> queryIpsanByVMId (int instanceId) throws Exception; 
}
