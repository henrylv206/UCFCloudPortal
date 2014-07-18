package com.skycloud.management.portal.front.order.service;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.front.command.res.ECluster;
import com.skycloud.management.portal.front.command.res.EListConfigurations;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.EOsCategory;
import com.skycloud.management.portal.front.command.res.EOsType;
import com.skycloud.management.portal.front.command.res.EStoragePool;
import com.skycloud.management.portal.front.command.res.EVlanIpRange;
import com.skycloud.management.portal.front.command.res.EZone;
import com.skycloud.management.portal.front.command.res.ListHosts;
import com.skycloud.management.portal.front.command.res.ListTemplates;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;


public interface ICloudAPISerivce {

	/*
	 * 列出vlan
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return List<EVlanIpRange>对象
	 */
	public List<EVlanIpRange> listVlan(long id,int zoneid,int networkid,Integer resourcePoolId) throws Exception;
	/*
	 * 列出OsType
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * List<EOsType>对象
	 */
	public List<EOsType> listOsType(long id,Integer resourcePoolId) throws Exception;
	/*
	 * 列出OsCategory
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return List<EOsCategory>对象
	 */
	public List<EOsCategory> listOsCategory(long id,Integer resourcePoolId) throws Exception;
	/*
	 * 列出StoragePool
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return List<EStoragePool>对象
	 */
	public List<EStoragePool> listStoragePool(long id,Integer resourcePoolId) throws Exception;
	/*
	 * 列出Cluster
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 *     hypervisorIsBareMetal: !=0表示所有，=1表示hypervisortype=>"BareMetal"及物理机
	 * return List<ECluster>对象
	 */
	public List<ECluster> listCluster(long id,int hypervisorIsBareMetal,Integer resourcePoolId) throws Exception;
	/*
	 * 列出Zone
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return List<EZone>对象
	 */
	public List<EZone> listZones(long id,Integer resourcePoolId) throws Exception;
	/*
	 * 列出Network
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return List<ENetwork>对象
	 */
	public List<ENetwork> listNetworks(long id,Integer resourcePoolId) throws Exception;
	/*
	 * 列出默认网卡Network
	 * zoneId:要查询的zoneId
	 * zoneId  =0 表示查询所有记录
	 * return List<ENetwork>
	 */
	public List<ENetwork> listDefaultNetworksByZoneId(int zoneId,Integer resourcePoolId) throws Exception;

	/*
	 * 列出其它网卡Network
	 * zoneId:要查询的zoneId
	 * zoneId  =0 表示查询所有记录
	 * return List<ENetwork>
	 */
	public List<ENetwork> listOtherNetworksByZoneId(int zoneId,Integer resourcePoolId) throws Exception;

	/**
	 * 更加网卡ID列出vlan的所有ip区间
	 * @return
	 */
	public Map<String, List<EVlanIpRange>> getVlanIpRangesMap(Integer resourcePoolId);

	/**
	 * 列出网卡vlan信息
	 * @param zoneId  资源域
	 * @param isdefault 1.true:默认网卡，2.false:非默认网卡
	 * @return
	 * @throws Exception
	 */
	public List<ENetwork> listNetworksByZoneId(int zoneId,boolean isdefault,Integer resourcePoolId) throws Exception ;
	/*
	 * 列出ResourcePools
	 * 参数 id:     查询条件
	 *     id = 0: 查询所有记录
	 * return String对象
	 */
	public String getResourcePoolNameById(int id) throws SQLException;
	public TResourcePoolsBO getResourcePoolById(int resourcePoolsId) throws SQLException ;
	/*
	 * 列出资源池所有记录ResourcePools
	 * 参数  无
	 * return List<TResourcePoolsBO>对象
	 * 生成时间：2011-12-22 14:58
	 */
	public List<TResourcePoolsBO> listAllResourcePools() throws SQLException;
	//to fix bug:0001910
	/**
	 * 列出全局变量的值
	 * @param name
	 * @return
	 * @throws SQLException
	 * 创建人：   何福康
	 * 创建时间：2012-5-31  上午11:28:09
	 */
	public List<EListConfigurations> listConfigurations(String name,Integer resourcePoolId) throws SQLException;


	public List<Map<String, String>> listVlanIpRanges(long networkId, int zoneId,Integer resourcePoolId) throws Exception;

	public List<ListVirtualMachines> listVirtualMachines(int id, long networkId, int zoneId, Integer resourcePoolId) throws Exception;

	/**
	 * 列出vlan剩余ip地址
	 * @param networkId  网卡id
	 * @return
	 * @throws Exception
	 */
	public List<listIpAddressesByNetWork> listIpAddressesByNetWork(long networkId,Integer resourcePoolId) throws Exception ;

	public int  ipTotal4VlanIpRange(List<EVlanIpRange> listVlan);

	/**
	 * 列出主机信息
	 * @param zoneId
	 * @param clusterId
	 * @param resourcePoolId
	 * @return
	 * @throws Exception
	 */
	public List<ListHosts> ListHosts(int id, int zoneId, int clusterId, Integer resourcePoolId) throws Exception;

	/**
	 * 列出操作系统信息
	 * @param id
	 * @param zoneId
	 * @param resourcePoolId
	 * @return
	 * @throws Exception
	 */
	public List<ListTemplates> ListTemplates(int eOsId,int zoneId,Integer resourcePoolId) throws Exception ;

}
