package com.skycloud.management.portal.admin.sysmanage.dao;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.exception.SCSException;
/**
 * 公网IP接口
 * 
 * @author jijun
 * 
 */
public interface IPublicIPDao {
	
	
	/**
	 * 
	 * @param order
	 * @return
	 * @throws SCSException
	 * 创建人：   何福康    
	 * 创建时间：2012-2-2  下午04:59:20
	 */
	int save(TPublicIPBO publicIP) throws  SCSException;

	/**
	 * 
	 * @param curPage
	 *            :当前页，从1开始
	 * @param pageSize
	 *            ：每页大小
	 * @return
	 */
	public List<TPublicIPBO> listPublicIPs(int curPage, int pageSize,String searchKey);

	/**
	 * 所有IP数量
	 * 
	 * @return
	 */
	public int countPublicIPs(String searchKey);

	/**
	 * 通过运营商Id查找所有对应的公网IP
	 * 
	 * @param serviceProvider
	 *        =0表示联通
	 *        =1表示移动
	 *        =-1表示所有
	 * @return
	 */
	public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider);

	/**
	 * 修改公网IP
	 * @param publicIP
	 * @return
	 * @throws SCSException
	 * 创建人：   何福康    
	 * 创建时间：2012-2-3  下午05:43:05
	 */
	int update(TPublicIPBO publicIP) throws  SCSException;
	
	/**
	 * 删除公网IP
	 * @param publicIPid
	 * @return
	 * @throws SCSException
	 * 创建人：   何福康    
	 * 创建时间：2012-2-3  下午05:43:23
	 */
	int delete(int publicIPid) throws  SCSException;
	
	/**
	 * 查看公网IP地址是否存在
	 * @param publicIPAddress
	 * @return
	 * @throws SCSException
	 * 创建时间：2012-2-3  下午05:43:23
	 */
	int searchIPByIpAddress(String ipAddress) throws  SCSException;
	
	/**
	 * 根据ID查看公网IP
	 * @param publicIPId
	 * @return
	 * @throws SCSException
	 * 创建人：   何福康    
	 * 创建时间：2012-2-8  上午09:34:08
	 */
	TPublicIPBO queryPublicIPById(int publicIPId) throws SCSException;
	
	/**
   * 根据公网ip获取绑定的私网ip
   * @param publicIp 公网ip
   * @return
   */
  String getPrivateIpByPublicIp(String publicIp);

  /**
   * 获取公网ip所属运营商
   * @param publicIp 公网ip
   * @return
   */
  String getOperatorByPublicIp(String publicIp);
}
