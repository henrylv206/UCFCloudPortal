package com.skycloud.management.portal.webservice.databackup.service;

import javax.ws.rs.core.Response;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.webservice.databackup.po.JobResultQueryCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserCancelDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserCreateSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserDeleteSnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserModifyDaaBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQueryDataBackUpCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserQuerySnapshotListCommandPo;
import com.skycloud.management.portal.webservice.databackup.po.UserResumeVirtualMachineCommandPo;

public interface IDataBackUpService {
	/**
	 * 查询模板信息列表
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午04:53:46
	 */
	public Response QuerySnapshotTemplateService () throws ServiceException; 
	/**
	 * 变更虚拟机备份参数校验
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:19:18
	 */
	public Response ModifyDataBackupService (UserModifyDaaBackUpCommandPo modifyPo) throws ServiceException;
	/**
	 * 创建虚拟机快照
	 * @param createPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:21:04
	 */
	public Response CreateSnapshotService (UserCreateSnapshotCommandPo createPo,int resourcePoolsId) throws ServiceException ;
	/**
	 * 虚拟机快照恢复
	 * @param resumePo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:22:03
	 */
	public Response ResumeVirtualMachineService (UserResumeVirtualMachineCommandPo resumePo,int resourcePoolsId) throws ServiceException ;
	/**
	 * 虚拟机备份详细信息查询
	 * @param dataBackupPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:23:29
	 */
	public Response QueryDataBackupInfoService (UserQueryDataBackUpCommandPo dataBackupPo) throws ServiceException ;
	/**
	 * 删除虚拟机快照备份
	 * @param deletePo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:24:33
	 */
	public Response DeleteSnapshotService (UserDeleteSnapshotCommandPo deletePo,int resourcePoolsId)  throws ServiceException ;
	/**
	 * 取消虚拟机备份
	 * @param cancelPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:25:45
	 */
	public Response CancelDataBackupService (UserCancelDataBackUpCommandPo cancelPo) throws ServiceException ;
	/**
	 * 快照详细信息查询
	 * @param queryPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:26:58
	 */
	public Response QuerySnapshotInfoService (UserQuerySnapshotCommandPo queryPo) throws ServiceException ;
	/**
	 * 查询快照列表
	 * @param queryPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:28:07
	 */
	public Response QuerySnapshotListService (UserQuerySnapshotListCommandPo queryPo) throws ServiceException ;
	/**
	 * 虚拟机备份恢复异步任务查询
	 * @param jobPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:34:15
	 */
	public Response QueryResumeVmResultService (JobResultQueryCommandPo jobPo)throws ServiceException ;
	/**
	 * 取消虚拟机备份服务异步任务查询
	 * @param jobPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-5  下午05:38:31
	 */
	public Response QueryCancelDataBackupResultService (JobResultQueryCommandPo jobPo) throws ServiceException ;
	/**
	 * 创建虚拟机快照备份异步任务查询
	 * @param jobPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-6  上午10:48:57
	 */
	public Response QueryCreateSnapshotResultService (JobResultQueryCommandPo jobPo, int resourcePoolsId) throws ServiceException ;
	/**
	 * 删除虚拟机快照异步任务查询
	 * @param jobPo
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-6  上午10:50:15
	 */
	public Response QueryDeleteSnapshotResultService (JobResultQueryCommandPo jobPo, int resourcePoolsId) throws ServiceException ;
	
	 /**
     * 根据模板标识获取该模板的大小
     * @param templateId
     * @return
     * @throws ServiceException
     * 创建人：  刘江宁    
     * 创建时间：2012-1-6  下午01:50:14
     */
    public int getCaptacityCountNumberByTemplate (int templateId) throws ServiceException;
    /**
	 * 得到用户已经使用的空间容量
	 * @param createUser
	 * @return
	 * @throws ServiceException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-6  下午01:51:02
	 */
    public int getUsedCapacityCountNumberByCreateUser (int createUser) throws ServiceException;
	
}
