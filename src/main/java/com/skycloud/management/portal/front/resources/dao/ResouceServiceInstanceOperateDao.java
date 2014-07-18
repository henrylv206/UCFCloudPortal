package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;


/**
 * @author fengyk
 * 创建时间 2011.01.10 15:15:15
 */

public interface ResouceServiceInstanceOperateDao {
	
	/**
	 * 查看用户服务总数
	 * @param 通过VO中operateSqlType区分查看服务信息
	 * @return 服务总数
	 * @throws Exception
	 */
	
	int queryResouceServiceInstanceInfoCount(ResourcesQueryVO rqvo) throws SCSException;
	int queryResouceServiceInstanceInfoCountBeforApprove(ResourcesQueryVO rqvo) throws SCSException;
	
	/**
	 * 查看用户服务列表信息
	 * @param 通过VO中operateSqlType区分查看服务信息
	 * @return 服务信息列表
	 * @throws Exception
	 */
	List<ResourcesVO> queryResouceServiceInstanceInfo(ResourcesQueryVO rqvo) throws SCSException;
	List<ResourcesVO> queryResouceServiceInstanceInfoBeforApprove(ResourcesQueryVO rqvo) throws SCSException;
	
	/**
	 * 查看备份及服务有效模板
	 * @param 通过VO中operateSqlType区分查看服务信息
	 * @return 
	 * @throws Exception 
	 */
	List<TemplateVMBO> queryResouceTemplateAvailableList(ResourcesQueryVO rqvo) throws Exception;
	
	/**
	 * 带宽和公网ip绑定后，把带宽主键id更新到公网ip表
	 * @param publicIP
	 * @return
	 * @throws SCSException
	 */
	public int update(TPublicIPBO publicIP) throws SCSException ;
	
	/**
	 * 多资源池、小机和数据盘模板类型区分开后 ，修改sql查询
	 * @param rqvo
	 * @return
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-12-13  下午12:34:18
	 */
	List<ResourcesVO> queryResouceInstanceInfoIncludeMc(ResourcesQueryVO rqvo) throws SCSException;
}	