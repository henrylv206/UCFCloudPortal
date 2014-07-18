package com.skycloud.management.portal.front.instance.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.front.instance.entity.TInstancePeriodInfo;
import com.skycloud.management.portal.front.instance.entity.TServicePeriodInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;


public interface IInstancePeriodManageDao {
	/**
	 * 查询实例的关于购买周期的一些信息
	 * @param id
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-3-24  下午02:58:03
	 */
	TInstancePeriodInfo findInstancePeriodById(int id) throws SQLException;
	
	TServicePeriodInfo findServiceInstancePeriodById(int id) throws SQLException;
	
	/**
	 * 更新实例的到期时间和购买日期
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-3-24  下午02:59:29
	 */
	int updateInstancePeriod(String resourceInfo,Date expireDate,int instanceId) throws SQLException;
	
	int updateServicePeriod(int periods,Date expireDate,int instanceId) throws SQLException;	
	
	/**
	 * 查找到期或者过期产品
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-3-27  下午05:16:31
	 */
	List<TInstanceInfoBO>  findExpireProduct() throws SQLException;
	
	/**
	 * 续订成功后修改Message表状态为3 已过期
	 * @param instanceId
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽    
	 * 创建时间：2012-4-10  下午01:55:42
	 */
	int updateMessageState(int instanceId) throws SQLException;
	
	/**
	 * 更新实例的到期时间和购买日期
	 * @return
	 * @throws SQLException
	 * 创建人：   王海东  
	 * 创建时间：2012-3-24  下午02:59:29
	 */
	int updateInstance4OrderPeriod(String resourceInfo,Date expireDate,int orderId) throws SQLException;

}
