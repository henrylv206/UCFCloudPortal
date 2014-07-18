package com.skycloud.management.portal.webservice.databackup.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;

public interface IDBUSerSnapshotDao {
  /**
   * 查询用户快照
   * 
   * @param snapshot
   * @param pageInfo
   * @return
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-31 下午03:30:48
   */
  public List<UserSnapshot> queryTpSnapshotList(UserSnapshot snapshot) throws DataAccessException;

  /**
   * 增加用户快照
   * 
   * @param snapshot
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-31 下午03:31:47
   */
  public void insertIntoTpSnapshot(UserSnapshot snapshot) throws DataAccessException;

  public int insertIntoSnapshot(UserSnapshot snapshot) throws DataAccessException;

  /**
   * 修改用户快照
   * 
   * @param snapshot
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-31 下午03:32:22
   */
  public void updateTpSnapshot(UserSnapshot snapshot) throws DataAccessException;

  /**
   * 通过快照标识删除用户快照信息，逻辑删除
   * 
   * @param snapshotId
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-31 下午03:33:22
   */
  public void deleteTpSnapshotBySnapshotId(int snapshotId) throws DataAccessException;

  /**
   * 根据创建人唯一标识删除用户快照列表,物理删除
   * 
   * @param createUserId
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-1-4 下午02:58:30
   */
  public void deteteTpSnashotByCreateUser(int createUserId) throws DataAccessException;

  /**
   * 根据快照标识获取唯一快照信息
   * 
   * @param snapshotId
   * @return
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2011-12-31 下午03:34:30
   */
  public UserSnapshot queryTpSnapshotBySnapshotId(int snapshotId) throws DataAccessException;
  public UserSnapshot querySnapshotId(int snapshotId) throws DataAccessException;
  public UserSnapshot querySnapshotByuser(int userId, int instanceId) throws DataAccessException;

  /**
   * 查询用户快照总大小
   * 
   * @param createUserId
   * @return
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-1-6 上午11:03:40
   */
  public int queryUserSnapshotSum(int createUserId) throws DataAccessException;

  /**
   * 根据JobId逻辑删除用户快照记录
   * 
   * @param snapshotId
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-1-10 下午05:03:26
   */
  public void deleteTpSnapshotByJobId(int snapshotId) throws DataAccessException;

  /**
   * 更新用户快照表任务标识
   * 
   * @param snapshot
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-1-12 下午05:38:19
   */
  public void updateTpSnapshotJobId(UserSnapshot snapshot) throws DataAccessException;

  /**
   * 根据IntanceInfoId来获取用户快照实例
   * 
   * @param intanceInfoId
   * @return
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-2-2 下午02:51:36
   */
  public List<UserSnapshot> queryTpSnapshotListByIntanceInfoId(int intanceInfoId) throws DataAccessException;

  /**
   * 根据用户快照唯一标识更新快照信息
   * 
   * @param id
   * @param eSnapshotId
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-2-3 上午10:37:17
   */
  public void updateTpSnapshotById(int id, int eSnapshotId) throws DataAccessException;

  /**
   * 根据快照主键删除快照信息
   * 
   * @param id
   * @throws DataAccessException
   *           创建人： 刘江宁 创建时间：2012-2-3 下午05:05:00
   */
  public void deleteTpSnapshotById(int id) throws DataAccessException;

  /**
   * 
   * @author shixq
   * @create-time 2012-7-1 下午09:04:07
   * @version $Id:$
   */
  int deleteTpSnapshotByIdVDC(int id) throws DataAccessException;

  /**
   * 
   * @author shixq
   * @create-time 2012-7-1 下午09:12:49
   * @version $Id:$
   */
  int updateTpSnapshotByIdVDC(UserSnapshot snapshot) throws DataAccessException;

}
