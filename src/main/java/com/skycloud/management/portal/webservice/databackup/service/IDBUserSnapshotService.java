package com.skycloud.management.portal.webservice.databackup.service;

import java.util.List;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;

public interface IDBUserSnapshotService {
	/**
	 * 查询用户快照
	 * @param snapshot
	 * @param pageInfo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:30:48
	 */
	public List<UserSnapshot> queryTpSnapshotList (UserSnapshot snapshot) throws ServiceException;
	/**
	 * 增加用户快照
	 * @param snapshot
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:31:47
	 */
	public void insertIntoTpSnapshot (UserSnapshot snapshot) throws ServiceException;
	public int insertIntoSnapshot (UserSnapshot snapshot) throws ServiceException;
	/**
	 * 修改用户快照
	 * @param snapshot
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:32:22
	 */
	public void updateTpSnapshot (UserSnapshot snapshot) throws ServiceException;
	/**
	 * 通过快照标识删除用户快照信息，逻辑删除
	 * @param snapshotId
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:33:22
	 */
	public void deleteTpSnapshotBySnapshotId (int snapshotId) throws ServiceException;
	/**
	 * 根据创建人唯一标识删除用户快照列表,物理删除
	 * @param createUserId
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-4  下午02:58:30
	 */
	public void deteteTpSnashotByCreateUser (int createUserId)  throws ServiceException;
	/**
	 * 根据快照标识获取唯一快照信息
	 * @param snapshotId
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:34:30
	 */
	public UserSnapshot queryTpSnapshotBySnapshotId (int snapshotId) throws ServiceException;
	public UserSnapshot querySnapshotId (int snapshotId) throws ServiceException;
	public UserSnapshot querySnapshotByuser (int userId, int instanceId) throws ServiceException;
	/**
	 * 获取用户全部快照大小
	 * @param createUserId
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-6  上午11:00:39
	 */
	public int queryUserSnapshotSum (int createUserId)throws ServiceException;
	/**
	 * 根据Jobid逻辑删除用户快照记录
	 * @param snapshotId
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  下午05:04:04
	 */
	public void deleteTpSnapshotByJobId(int snapshotId)throws ServiceException;
	/**
	 * 更新用户快照记录任务标识
	 * @param snapshot
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-12  下午05:40:06
	 */
	public void updateTpSnapshotJobId (UserSnapshot snapshot)throws ServiceException;
	/**
	 * 根据IntanceInfoId来获取用户快照实例
	 * @param intanceInfoId
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-2-2  下午02:54:09
	 */
	public List<UserSnapshot> queryTpSnapshotListByIntanceInfoId(int intanceInfoId) throws ServiceException;
	/**
	 * 定时任务根据异步任务唯一标识同步快照信息
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-2-3  上午10:39:54
	 */
	public void updateUserSnapshotByAsycnJobId () throws ServiceException ;
}
