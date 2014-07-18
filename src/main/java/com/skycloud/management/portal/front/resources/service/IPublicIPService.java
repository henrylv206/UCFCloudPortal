package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.PublicIPBO;

public interface IPublicIPService {

	/**
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:55:13
	 */
	int insert(PublicIPBO publicIP) throws Exception;

	/**
	 * @param curPage
	 *            :当前页，从1开始
	 * @param pageSize
	 *            ：每页大小
	 * @param searchKey
	 * @return
	 */
	List<PublicIPBO> queryListPublicIPs(int curPage, int pageSize, String searchKey) throws Exception;

	/**
	 * 所有IP数量
	 * 
	 * @return
	 */
	int queryCountPublicIPs(String searchKey) throws Exception;

	/**
	 * 绑定公网IP
	 * 
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	int updateBindPublicIpToInstance(PublicIPBO publicIP) throws Exception;

	/**
	 * 解除绑定公网IP
	 * 
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	int updateUnBindPublicIpFromVm(PublicIPBO publicIP) throws Exception;

	/**
	 * 解除虚服务绑定的公网IP
	 * 
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	int updateUnBindPublicIpFromVs(PublicIPBO publicIP) throws Exception;

	/**
	 * 删除公网IP
	 * 
	 * @param publicIPid
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	int delete(int publicIPid) throws Exception;

	/**
	 * 查看公网IP地址
	 * 
	 * @param publicIPAddress
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */

	PublicIPBO getIPByIpAddress(String ipAddress) throws Exception;

	/**
	 * 根据ID查看公网IP
	 * 
	 * @param publicIPId
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	PublicIPBO queryPublicIPById(int publicIPId) throws Exception;

	/**
	 * 查询被绑定的公网ip信息
	 * 
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-4-8 下午02:02:06
	 */
	List<PublicIPBO> queryBeBindPublicIPs() throws Exception;

	/**
	 * 绑定到虚服务
	 * 
	 * @param bo
	 * @param vsIP
	 * @return
	 * @throws Exception
	 */
	int updateBindPublicIpToVs(PublicIPBO bo, String vsIP) throws Exception;

	String getVMIPAddress(int instanceInfoId, int resourcePoolsId) throws Exception;

	/**
	 * 根据虚机获取绑定的公网ip数量
	 * 
	 * @param instanceInfoId
	 * @return
	 * @throws Exception
	 */
	int getPublicIPCntByVMIP(int instanceInfoId, int resourcePoolsId) throws Exception;
}
