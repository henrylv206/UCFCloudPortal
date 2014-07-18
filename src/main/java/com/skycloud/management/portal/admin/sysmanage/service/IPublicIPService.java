/**
 * 2012-1-16  下午04:16:17  $Id:shixq
 */
package com.skycloud.management.portal.admin.sysmanage.service;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.exception.SCSException;

/**
 * 
  *<dl>
  *<dt>类名：IPublicIP</dt>
  *<dd>描述: 公网IP</dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-2  下午05:48:24</dd>
  *<dd>创建人： 何福康</dd>
  *</dl>
 */
public interface IPublicIPService {

  /**
   * 增加公网IP信息
   * @param publicIP
   * @return id
   * @throws Exception
   * 创建人：   何福康    
   * 创建时间：2012-2-2  下午05:51:38
   */
  int insertPublicIP(TPublicIPBO publicIP) throws Exception;
  
  /**
   * 修改公网IP信息
   * @param publicIP
   * @return id
   * @throws Exception
   * 创建人：   何福康    
   * 创建时间：2012-2-3  下午04:52:52
   */
  int updatePublicIP(TPublicIPBO publicIP) throws Exception;
  
  /**
	 * 所有IP数量
	 * 
	 * @return
	 */
  int countPublicIPs(String searchKey) throws Exception;
  
  List<TPublicIPBO> queryPublicIP(int curPage, int pageSize,String searchKey)  throws Exception;
  
   /**
	 * 查看公网IP地址是否存在
	 * @param publicIPAddress
	 * @return
	 * @throws SCSException
	 * 创建时间：2012-2-3  下午05:43:23
	 */
  int searchIPByIpAddress(String ipAddress) throws  SCSException;

  /**
   * 
   * @param publicIPId
   * @return
   * @throws SCSException
   * 创建人：   何福康    
   * 创建时间：2012-2-2  下午05:51:30
   */
  TPublicIPBO queryPublicIPById(int publicIPId) throws SCSException;
  
  /**
   * 删除公网IP
   * @param publicIPId
   * @return
   * @throws SCSException
   * 创建人：   何福康    
   * 创建时间：2012-2-3  下午04:54:35
   */
  int deletePublicIP(int publicIPId) throws SCSException;
  
  /**
   * 根据服务提供商ID“serviceProvider”列出公网IP
   * @param serviceProvider
   * @return
   * 创建人：   何福康    
   * 创建时间：2012-2-8  下午05:49:57
   */
    public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider);
}
