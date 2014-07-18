package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

/**
 * @declare
 * @file_name PublicIPInstanceDao.java
 * @author gankb E-mail: gankb@chinaskycloud.com
 * @version 2012-3-9 下午03:39:14
 */
public interface PublicIPInstanceDao {

	/**
	 * 查看用户服务总数（可用状态）
	 * 
	 * @param 通过VO中operateSqlType区分查看服务信息
	 * @return 服务总数
	 * @throws Exception
	 */

	int queryResouceServiceInstanceInfoCount(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 查看用户服务列表信息（可用状态）
	 * 
	 * @param 通过VO中operateSqlType区分查看服务信息
	 * @return 服务信息列表
	 * @throws Exception
	 */
	List<ResourcesVO> queryResouceServiceInstanceInfo(ResourcesQueryVO rqvo) throws SCSException;

	/**
	 * 通过本实例的实例id 和 实例类型A查询一条A类型实例的信息
	 * 
	 * @param userId
	 *            当前用户id
	 * @param templateType
	 *            实例类型
	 * @param instanceId
	 *            本实例的实例id
	 * @return
	 * @throws SCSException
	 *             创建人： 甘坤彪 创建时间：2012-3-14 下午03:13:55
	 */
	TInstanceInfoBO searchInstanceInfoByID(final int userId, final int templateType, final int instanceId) throws SCSException;

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
	TInstanceInfoBO searchInstanceInfo2ByID(final int userId, final int templateType, final int instanceId) throws SCSException;

	long searchBindRsIdByipId(final int ipId) throws Exception;

	public ResourcesVO queryBindVMByIp(int ipid) throws SCSException;
}
