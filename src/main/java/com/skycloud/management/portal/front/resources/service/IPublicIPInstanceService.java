package com.skycloud.management.portal.front.resources.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

/**
 * @declare
 * @file_name IPublicIPService.java
 * @author gankb E-mail: gankb@chinaskycloud.com
 * @version 2012-3-7 下午02:23:52
 */
public interface IPublicIPInstanceService {

	/**
	 * 根据服务提供商ID“serviceProvider”列出公网IP
	 * 
	 * @param serviceProvider
	 * @return 创建人： 甘坤彪 创建时间：2012-3-7 下午03:42:06
	 */
	public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider);

	/**
	 * 根据条件查询弹性实例信息
	 * 
	 * @param rqvo
	 *            查询条件 return 弹性实例数目
	 * @throws SQLException
	 */
	public int queryPublicIPInstanceListCount(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 根据条件查询弹性实例信息
	 * 
	 * @param rqvo
	 *            return ResourcesVO 弹性实例信息
	 * @throws SQLException
	 */
	public List<ResourcesVO> queryPublicIPInstanceList(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 退订公网IP服务信息
	 * 
	 * @param vmModifyVO
	 *            退订信息，包括退订理由
	 * @param user
	 *            用户对象
	 * @param IP
	 *            需要退订的公网IP地址
	 * @return
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-16 下午04:15:04
	 */
	public String insertDirtyReaddeletePublicIpInstance(ResourcesModifyVO vmModifyVO, TUserBO user, String IP) throws Exception;

	public String insertIpInstance(ResourcesModifyVO vmModifyVO, TUserBO user, String IP, int serviceID) throws Exception;

	/**
	 * 根据实例Id查询实例表记录
	 * 
	 * @param ID
	 *            实例Id
	 * @return
	 * @throws SCSException
	 *             创建人： 甘坤彪 创建时间：2012-3-12 下午12:44:08
	 */
	public TInstanceInfoBO searchInstanceInfoByID(int ID) throws SCSException;

	/**
	 * 修改实例表IntanceInfo记录
	 * 
	 * @param instanceInfo
	 * @return
	 * @throws SCSException
	 *             创建人： 甘坤彪 创建时间：2012-3-12 下午12:45:29
	 */
	public int update(TInstanceInfoBO instanceInfo) throws SCSException;

	/**
	 * 通过本实例的实例id 和 实例类型A查询一条A类型实例的信息 状态是2,6的
	 * 
	 * @param userId
	 *            当前用户id
	 * @param templateType
	 *            实例类型
	 * @param instanceId
	 *            本实例的实例id
	 * @return
	 * @throws SCSException
	 *             创建人： 甘坤彪 创建时间：2012-3-14 下午03:21:42
	 */
	public TInstanceInfoBO searchInstanceInfoByID(final int userId, final int templateType, final int instanceId) throws SCSException;

	/**
	 * 通过本实例的实例id 和 实例类型A查询一条A类型实例的信息 状态是1,2,3,6的
	 * 
	 * @param userId
	 *            当前用户id
	 * @param templateType
	 *            实例类型
	 * @param instanceId
	 *            本实例的实例id
	 * @return
	 * @throws SCSException
	 *             创建人： 何涛 创建时间：2012-10-20 下午01:13:55
	 */
	public TInstanceInfoBO searchInstanceInfo2ByID(final int userId, final int templateType, final int instanceId) throws SCSException;

	/**
	 * 判断用户是否已经购买了同样的公网IP地址。每个用户只能同时购买同一个公网IP地址一次
	 * 
	 * @param userId
	 *            当前用户id
	 * @param ipAddress
	 *            公网ip地址
	 * @return
	 * @throws SCSException
	 *             创建人： 甘坤彪 创建时间：2012-3-15 上午10:28:28
	 */
	public int checkPullicIpAddressExists(final int userId, final String ipAddress) throws SQLException;

	/**
	 * 通过模板id获取模板信息
	 * 
	 * @param id
	 *            模板id
	 * @return
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-15 下午03:34:52
	 */
	public TTemplateVMBO getTemplateById(final int templateId) throws Exception;

	/**
	 * 通过用户id取出所有公网IP地址
	 * 
	 * @param userId
	 * @return
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-15 下午05:19:23
	 */
	public List<String> getAllPublicAddress(final int userId) throws Exception;

	/**
	 * 绑定公网ip/解绑定公网ip，向T_SCS_ASYNCJOB_VDC表写入绑定公网IP、解绑定公网IP信息
	 * 
	 * @param userId
	 *            用户id
	 * @param instanceId
	 *            实例id
	 * @param IP
	 *            公网IP地址，调VDC接口的请求参数
	 * @param VMID
	 *            虚拟机id，调VDC接口的请求参数
	 * @param operationType
	 *            操作命令类型---绑定/解绑定
	 * @return
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-16 下午05:26:41
	 */
	public int insertAsyncjobVdcForBind(int userId, int instanceId, String IP, String VMID, int operationType) throws Exception;

	/**
	 * 调VDC接口申请公网IP，成功后，回写本地数据表T_SCS_INSTANCE_INFO的方法
	 * 
	 * @param instanceId
	 *            实例id，代表要回写的T_SCS_INSTANCE_INFO表记录
	 * @param state
	 *            实例状态，成功后需要改为可用状态---2
	 * @param ipAddress
	 *            VDC接口成功返回的公网IP地址
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-16 下午03:19:27
	 */
	public void writeBackForApplyPublicIP(int instanceId, int state, String ipAddress) throws Exception;

	/**
	 * 调VDC接口退订公网IP，收到命令返回信息后，回写本地数据表T_SCS_INSTANCE_INFO的方法
	 * 
	 * @param instanceId
	 *            实例id，代表要回写的T_SCS_INSTANCE_INFO表记录
	 * @param state
	 *            实例状态：成功后需要改为已回收状态---4；失败后需传入-1，代表调用接口失败
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-16 下午05:05:53
	 */
	public void writeBackForReleasePublicIP(int instanceId, int state) throws Exception;

	/**
	 * 调VDC接口绑定公网IP，收到命令返回信息后，回写本地数据表T_SCS_INSTANCE_INFO的方法
	 * 
	 * @param instanceId
	 *            实例id，代表要回写的T_SCS_INSTANCE_INFO表记录
	 * @param state
	 *            实例状态：成功后需要改为可用状态---2；失败后需传入-1，代表调用接口失败
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-19 下午02:03:40
	 */
	// public void writeBackForBindPublicIP(int instanceId, int vmId, int state)
	// throws Exception;
	public void writeBackForBindPublicIP(int instanceId, int state) throws Exception;

	/**
	 * 调VDC接口解绑定公网IP，收到命令返回信息后，回写本地数据表T_SCS_INSTANCE_INFO的方法
	 * 
	 * @param instanceId
	 *            实例id，代表要回写的T_SCS_INSTANCE_INFO表记录
	 * @param state
	 *            实例状态：成功后需要改为可用状态---2；失败后需传入-1，代表调用接口失败
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-19 下午03:17:28
	 */
	// public void writeBackForUnBindPublicIP(int instanceId, int vmId, int
	// state) throws Exception;
	public void writeBackForUnBindPublicIP(int instanceId, int state) throws Exception;

	public int checkPullicIpAddressExists(String ipAddress) throws SQLException;

	public ResourcesVO queryBindVMByIp(int ipid) throws SCSException;

	/**
	 * 如果操作（绑定、解绑定、退订）操作失败，则需要将实例状态由“命令正在执行中”改回“可用状态”，以便继续操作
	 * 
	 * @param instanceId
	 *            实例id，代表要回写的T_SCS_INSTANCE_INFO表记录
	 * @param state
	 *            实例状态，如果请求失败，则改回原来状态---可用状态2
	 * @throws Exception
	 *             创建人： 甘坤彪 创建时间：2012-3-21 上午09:56:01
	 */
	long searchBindRsIdByIpInstanceId(TUserBO user, int ipInstanceId) throws Exception;
	// public void writeBackForRequestFailure(int instanceId, int state) throws
	// Exception;
}
