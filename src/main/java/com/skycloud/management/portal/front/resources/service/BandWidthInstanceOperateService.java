package com.skycloud.management.portal.front.resources.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface BandWidthInstanceOperateService {

	/**
	 * 查看带宽列表
	 */
	List<ResourcesVO> queryBandWidthInstanceList(ResourcesQueryVO rqvo) throws Exception;
	
	/**
	 * 查看用户带宽服务总数
	 * @param
	 * @return 带宽服务信息总数
	 * @throws Exception
	 */
	int queryBandWidthInstanceListCount(ResourcesQueryVO rqvo) throws Exception;
	
	
	/**
	 * 修改带宽服务信息
	 * @param vmModifyVO
	 * @param user
	 * @return
	 * @throws Exception
	 * 创建人：   冯永凯    
	 * 创建时间：2012-2-23  下午03:45:37
	 */
	String  insertDirtyReadChangeBandWidthInstance(ResourcesModifyVO vmModifyVO,TUserBO user) throws Exception;
	
	
	/**
	 * 删除带宽服务信息
	 * @param
	 * @return 
	 * @throws Exception
	 */
	String  insertDirtyReaddeleteBandWidthInstance(ResourcesModifyVO vmModifyVO,TUserBO user) throws Exception;
	String  insertBWInstance(ResourcesModifyVO vmModifyVO,TUserBO user, int serviceID) throws Exception;
	
	/**
	 * 查找可用的公网IP 供带宽使用
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-3-14  下午07:15:52
	 */
	List<TInstanceInfoBO> findUsableIpInstance(int userId) throws SQLException;
	
	/**
	 * 判断ipAddress 是否已经生成带宽
	 * @param ipAddress
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-3-14  下午07:15:46
	 */
	int findIPAddressExist(String ipAddress,int templateType,int userId) throws SQLException;
	
	/**
	 * 删除带宽服务信息
	 * @param
	 * @return 
	 * @throws Exception
	 */
	String  deleteBandWidthInstance(ResourcesModifyVO vmModifyVO) throws Exception;
	
	/**
	 * 购买带宽时查询可用的公网ip,项目不同查询逻辑不同
	 * 1.3功能，对于产品来说，支持多资源池
	 * @param userId
	 * @return
	 * @throws SQLException
	 */
	List<TInstanceInfoBO> findUsableIpInstance2(int userId,int resourcePoolsId,int zoneId) throws SQLException ;
	
	/**
	 * 更新公网ip表
	 * @param publicIP
	 * @return
	 * @throws SCSException
	 */
	int update(TPublicIPBO publicIP) throws SCSException ;
}
