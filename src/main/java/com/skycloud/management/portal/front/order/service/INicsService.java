package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;

public interface INicsService {

	int save(TNicsBO nics) throws SQLException;

	int delete(int nicsId) throws SQLException;

	int update(TNicsBO nics) throws SQLException;

	TNicsBO searchNicsById(int nicsId) throws SQLException;

	List<TNicsBO> searchAllNics() throws SQLException;

	int searchLastId() throws SQLException;

	List<TNicsBO> searchNicssByInstanceId(int instanceId) throws SQLException;

	List<TNicsBO> searchNicssByUserId(int userId) throws SQLException;

	int searchVlanIdByUserId(int userId) throws SQLException;

	int searchVlanDefaultFreeCount(int zoneId,Integer resourcePoolID) throws Exception;

	int searchVlanOtherFreeCount(int zoneId,Integer resourcePoolID) throws Exception;

	List<Long>  getAvailableNetworkByUserId(int userId,int vethAdaptorNum, int createNum, int resourceDomain,Integer resourcePoolID);
	List<TNicsBO> searchNicssByNetworkId(int networkId) throws SQLException;
    /**
     *根据网卡ID和ip查询本地虚机网卡记录
     * @param networkId
     * @param ipaddress
     * @return
     * @throws SQLException
     */
	List<TNicsBO> searchNicssByNetworkIdAndIp(int networkId,String ipaddress) throws SQLException;
	/**
	 * 根据订单id查询未设定vlan的网卡nics
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	List<TNicsBO> searchNicsDhcpByOrderId(int orderId) throws SQLException;

	/**
	 * 列出vlan剩余ip地址
	 *
	 * @param networkId
	 *            网卡id
	 * @return
	 * @throws Exception
	 */
	public List<listIpAddressesByNetWork> listIpAddressesByNetworkId(long networkId, Integer resourcePoolsId) throws Exception;

	/**
	 * 列出默认网卡信息
	 *
	 * @param zoneId
	 * @param resourcePoolID
	 * @return
	 * @throws Exception
	 */
	List<ENetwork> searchNetworkListDefault(int zoneId, Integer resourcePoolsId,int networkType) throws Exception;


	/**
	 * 列出非默认网卡信息
	 *
	 * @param zoneId
	 * @param resourcePoolID
	 * @return
	 * @throws Exception
	 */
	List<ENetwork> searchNetworkListOther(int zoneId, Integer resourcePoolsId,int networkType) throws Exception;

	/**
	 * 查询给定实例订单号中的网卡分配信息，TNicsBO.vlanCount表示在同一个实例中的重复分配次数
	 * @param orderId
	 * @return  返回值 TNicsBO.vlanCount>=2 则 vlan有重复分配
	 * @throws SQLException
	 */
	public List<TNicsBO> searchNicsRepeatVlanCountByorderId(final int orderId) throws SQLException;
	
	/**
	 * 查询给定实例订单号中的网卡分配信息，TNicsBO.ipCount表示在同一个实例中的重复分配次数
	 * @param orderId
	 * @return  返回值 TNicsBO.ipCount>=2 则 ip有重复分配
	 * @throws SQLException
	 */
	public List<TNicsBO> searchNicsRepeatIPCountByorderId(final int orderId) throws SQLException;
}
