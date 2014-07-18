package com.skycloud.management.portal.front.order.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.exception.SCSException;

public interface INicsDao {

	int save(TNicsBO nics) throws SQLException;

	int delete(int nicsId) throws SQLException;

	int update(TNicsBO nics) throws SQLException;

	TNicsBO searchNicsById(int nicsId) throws SQLException;

	List<TNicsBO> searchAllNics() throws SQLException;

	int searchLastId() throws SQLException;

	List<TNicsBO> searchNicssByInstanceId(int instanceId) throws SQLException;

	int modifyNics(TNicsBO nics) throws SCSException;

	List<TNicsBO> searchNicssByUserId(int userId) throws SQLException;

	/**
	 * 查询虚机对默认vlan占用数，按资源域统计
	 *
	 * @param zoneId
	 * @return
	 * @throws SQLException
	 */
	// to fix bug:2622
	int searchVlanDefaultCount(int zoneId) throws SQLException;

	/**
	 * 查询虚机对非默认vlan占用数，按资源域统计
	 *
	 * @param zoneId
	 * @return
	 * @throws SQLException
	 */
	int searchVlanCount(int zoneId) throws SQLException;

	/**
	 * 查询虚机对非默认vlan占用数，按资源域统计
	 *
	 * @param zoneId
	 * @return
	 * @throws SQLException
	 */
	// to fix bug:2622
	int searchVlanOtherCount(int zoneId) throws SQLException;

	int searchNicssCountByvlanId(int vlanId) throws SQLException;
	int searchNicssCountByVlanId4UserId(int vlanId,int userId) throws SQLException;

	List<TNicsBO> searchNicssByNetworkId(int networkId) throws SQLException;

	List<TNicsBO> searchNicssByNetworkIdAndIp(int networkId,String ipaddress) throws SQLException;

	List<TNicsBO> searchNicsDhcpByOrderId(int orderId) throws SQLException;

	/**
	 * 删除实例下的网卡信息
	 * ninghao@chinaskycloud.com
	 * @param infoId
	 * @return
	 * @throws SQLException
	 */
	public int deleteNicsByInfoId(final int infoId) throws SQLException;
	/**
	 * 查询给定实例订单号中的网卡分配信息，TNicsBO.vlanCount表示在同一个实例中的重复分配次数
	 * @param orderId
	 * @return  返回值 TNicsBO.vlanCount>=2 则 vlan有重复分配
	 * @throws SQLException
	 */
	public List<TNicsBO> searchNicsRepeatVlanCountByorderId(final int orderId) throws SQLException;
	/**
	 * 查询可用实例中的ip重复网卡分配信息，TNicsBO.ipCount表示重复次数
	 * @param orderId
	 * @return  返回值 TNicsBO.ipCount>=2 则 ip有重复分配
	 * @throws SQLException
	 */
	public List<TNicsBO> searchNicsRepeatIP() throws SQLException;
	/**
	 * 根据订单号查询网卡信息
	 * @param orderId
	 * @return  返回值 TNicsBO
	 * @throws SQLException
	 */
	public List<TNicsBO> searchNicsByorderId(final int orderId) throws SQLException;
}
