package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.management.portal.front.resources.entity.PublicIPBO;

/**
 * 公网IP接口
 * 
 * @author fengyk
 */
public interface IPublicIPDao {

	/**
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:55:13
	 */
	int save(PublicIPBO publicIP) throws Exception;

	/**
	 * @param curPage
	 *            :当前页，从1开始
	 * @param pageSize
	 *            ：每页大小
	 * @param searchKey
	 * @return
	 */
	List<PublicIPBO> listPublicIPs(int curPage, int pageSize, String searchKey) throws Exception;

	/**
	 * 所有IP数量
	 * 
	 * @return
	 */
	int countPublicIPs(String searchKey) throws Exception;

	/**
	 * 通过运营商Id查找所有对应的公网IP
	 * 
	 * @param serviceProvider
	 *            =0表示联通 =1表示移动 =-1表示所有
	 * @return
	 */
	List<PublicIPBO> listPublicIPByServiceProvider(int serviceProvider) throws Exception;

	/**
	 * 更新公网IP
	 * 
	 * @param publicIP
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-3-31 上午10:57:44
	 */
	int update(PublicIPBO publicIP) throws Exception;

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
	PublicIPBO searchIPByIpAddress(String ipAddress) throws Exception;

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
	 * @return
	 * @throws Exception
	 *             创建人： 冯永凯 创建时间：2012-5-4 下午02:17:42
	 */
	List<PublicIPBO> queryUnBindPublicIPsForUser(String userId) throws Exception;

	/**
	 * 根据公网ip获取绑定的私网ip
	 * 
	 * @param publicIp
	 *            公网ip
	 * @return
	 */
	String getPrivateIpByPublicIp(String publicIp);

	/**
	 * 根据私网ip获取绑定的公网ip
	 * 
	 * @param publicIp
	 *            公网ip
	 * @return
	 */
	int getPublicIpCntByPrivateIp(String privateIp);
}
