package com.skycloud.management.portal.front.instance.service;

import java.sql.SQLException;
import java.util.Date;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.instance.entity.TInstancePeriodInfo;
import com.skycloud.management.portal.front.instance.entity.TServicePeriodInfo;

public interface IInstancePeriodManageService {

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
	int updateInstancePeriod(int num,String unit,String resourceInfo,Date expireDate,int instanceId) throws SQLException;
	int updateServicePeriod(int num,String unit,String periods,Date expireDate,int instanceId, TUserBO user) throws SQLException;

	/**
	 * 产品自动退订功能
	 * @return
	 * @throws SQLException
	 * 创建人：   张爽
	 * 创建时间：2012-3-28  下午01:14:16
	 */
	int destroyExpireProduct() throws Exception;
	////fix bug 4561
	int disabledExpireProduct() throws Exception;

}
