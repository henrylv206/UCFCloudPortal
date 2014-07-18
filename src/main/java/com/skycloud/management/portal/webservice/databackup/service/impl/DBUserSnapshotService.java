package com.skycloud.management.portal.webservice.databackup.service.impl;

import java.util.List;

import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.webservice.databackup.dao.IDBUSerSnapshotDao;
import com.skycloud.management.portal.webservice.databackup.po.UserSnapshot;
import com.skycloud.management.portal.webservice.databackup.service.IDBAsycnJobService;
import com.skycloud.management.portal.webservice.databackup.service.IDBUserSnapshotService;

public class DBUserSnapshotService extends BaseService implements
		IDBUserSnapshotService {

	private IDBUSerSnapshotDao dbuserSnapshotDao;

	private IDBAsycnJobService dbAsycnJobService;

	public UserSnapshot querySnapshotByuser(int userId, int instanceId) throws ServiceException {
		 
		try {
			return dbuserSnapshotDao.querySnapshotByuser(userId,instanceId);
		} catch (ServiceException e) {
			logger.error("Exceute [DBUserSnapshotService] method: queryTpSnapshotBySnapshotId Exception :", e);
			throw new ServiceException("根据快照标识获取唯一快照信息异常");
		}
	}
	

	@Override
	public List<UserSnapshot> queryTpSnapshotList(UserSnapshot snapshot)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return dbuserSnapshotDao.queryTpSnapshotList(snapshot);
		} catch (ServiceException e) {
			logger.error(
					"Execute [DBUserSnapshotService] method: queryTpSnapshotList Exception :",
					e);
			throw new ServiceException("查询用户快照异常");
		}
	}

	@Override
	public void insertIntoTpSnapshot(UserSnapshot snapshot)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			dbuserSnapshotDao.insertIntoTpSnapshot(snapshot);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: insertIntoTpSnapshot Exception :",
					e);
			throw new ServiceException("创建用户快照异常");
		}
	}
	
	@Override
	public int insertIntoSnapshot(UserSnapshot snapshot) throws ServiceException {
		int id = 0;
		try {
			id = dbuserSnapshotDao.insertIntoSnapshot(snapshot);
		} catch (ServiceException e) {
			throw new ServiceException("创建用户快照异常");
		}
		return id;
	}

	@Override
	public void updateTpSnapshot(UserSnapshot snapshot) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			dbuserSnapshotDao.updateTpSnapshot(snapshot);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: updateTpSnapshot Exception :",
					e);
			throw new ServiceException("修改用户快照信息异常");
		}
	}

	@Override
	public void deleteTpSnapshotBySnapshotId(int snapshotId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			dbuserSnapshotDao.deleteTpSnapshotBySnapshotId(snapshotId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: deleteTpSnapshotBySnapshotId Exception :",
					e);
			throw new ServiceException("逻辑删除,用户快照信息异常");
		}
	}

	@Override
	public void deteteTpSnashotByCreateUser(int createUserId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			dbuserSnapshotDao.deteteTpSnashotByCreateUser(createUserId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: deteteTpSnashotByCreateUser Exception :",
					e);
			throw new ServiceException("物理删除,用户快照信息异常");
		}
	}

	@Override
	public UserSnapshot queryTpSnapshotBySnapshotId(int snapshotId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return dbuserSnapshotDao.queryTpSnapshotBySnapshotId(snapshotId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: queryTpSnapshotBySnapshotId Exception :",
					e);
			throw new ServiceException("根据快照标识获取唯一快照信息异常");
		}
	}
	
	public UserSnapshot querySnapshotId(int snapshotId) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			return dbuserSnapshotDao.querySnapshotId(snapshotId);
		} catch (ServiceException e) {
			logger.error("Exceute [DBUserSnapshotService] method: queryTpSnapshotBySnapshotId Exception :", e);
			throw new ServiceException("根据快照标识获取唯一快照信息异常");
		}
	}	

	@Override
	public List<UserSnapshot> queryTpSnapshotListByIntanceInfoId(
			int intanceInfoId) throws ServiceException {
		try {
			return dbuserSnapshotDao
					.queryTpSnapshotListByIntanceInfoId(intanceInfoId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: queryTpSnapshotListByIntanceInfoId Exception :",
					e);
			throw new ServiceException("根据实例标识获取唯一快照信息异常");
		}
	}

	@Override
	public int queryUserSnapshotSum(int createUserId) throws ServiceException {
		try {
			return dbuserSnapshotDao.queryUserSnapshotSum(createUserId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: queryUserSnapshotSum Exception :",
					e);
			throw new ServiceException("查询用户快照总大小异常");
		}
	}

	@Override
	public void deleteTpSnapshotByJobId(int snapshotId) throws ServiceException {
		try {
			dbuserSnapshotDao.deleteTpSnapshotByJobId(snapshotId);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: queryUserSnapshotSum Exception :",
					e);
			throw new ServiceException("查询用户快照总大小异常");
		}
	}

	@Override
	public void updateTpSnapshotJobId(UserSnapshot snapshot)
			throws ServiceException {
		try {
			dbuserSnapshotDao.updateTpSnapshotJobId(snapshot);
		} catch (ServiceException e) {
			logger.error(
					"Exceute [DBUserSnapshotService] method: updateTpSnapshotJobId Exception :",
					e);
			throw new ServiceException("更新用户快照任务标识异常");
		}
	}

	@Override
	public void updateUserSnapshotByAsycnJobId() throws ServiceException {

			try {
				UserSnapshot snapshot = new UserSnapshot();
				snapshot.setSTATE(TaskContext.Status.UNUSE.getCode());
				snapshot.setJOB_ID(1);
				List<UserSnapshot> result = dbuserSnapshotDao
						.queryTpSnapshotList(snapshot);
				if (result == null) {
					logger.info("没有要同步的用户快照状态!!!");
				}
				for (UserSnapshot usersnapshot : result) {
					AsyncJobInfo asyncInfo = dbAsycnJobService
							.queryAsyncJobInfoById(usersnapshot.getASYN_ID());
					if (asyncInfo != null && asyncInfo.getRESID() != 0) {
						if (asyncInfo.getRESID() == -1) {
							dbuserSnapshotDao.deleteTpSnapshotById(usersnapshot
									.getID());
						} else {
							dbuserSnapshotDao.updateTpSnapshotById(
									usersnapshot.getID(), asyncInfo.getRESID());
						}
					}
				}
			} catch (ServiceException e) {
				logger.error(
						"Exceute [DBUserSnapshotService] method: updateUserSnapshotByAsycnJobId Exception :",
						e);
				throw new ServiceException("定时任务根据异步任务唯一标识同步快照信息异常");
			}
	}

	private boolean queryAsyncJobInfoResId(int resId) {
		if (resId != 0) {
			return true;
		}
		return false;
	}

	public IDBAsycnJobService getDbAsycnJobService() {
		return dbAsycnJobService;
	}

	public void setDbAsycnJobService(IDBAsycnJobService dbAsycnJobService) {
		this.dbAsycnJobService = dbAsycnJobService;
	}

	public IDBUSerSnapshotDao getDbuserSnapshotDao() {
		return dbuserSnapshotDao;
	}

	public void setDbuserSnapshotDao(IDBUSerSnapshotDao dbuserSnapshotDao) {
		this.dbuserSnapshotDao = dbuserSnapshotDao;
	}


}
