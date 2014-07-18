package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import com.skycloud.h3c.loadbalance.po.vservice.VServiceRowPO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.FirewallVO;
import com.skycloud.management.portal.front.resources.action.vo.ObjectStorageVO;

/**
 * 防火墙资源使用相关持久化接口
 * @author jiaoyz
 */
public interface IFirewallDao {
	
	public List<ObjectStorageVO> getObjectStorage(int userId, int curPage, int pageSize) throws Exception;
	

  /**
   * 获取防火墙实例列表
   * @param userId 用户ID
   * @param curPage 当前页码
   * @param pageSize 每页条数
   * @return 已经通过审核的防火墙实例列表
   * @throws Exception
   */
  public List<FirewallVO> getFirewallList(int userId, int curPage, int pageSize) throws Exception;

  /**
   * 获取防火墙实例数
   * @param userId 用户ID
   * @return 已经通过审核的防火墙实例数
   * @throws Exception
   */
  public int getFirewallListCount(int userId) throws Exception;

  /**
   * 搜索防火墙实例列表
   * @param name 实例名称
   * @param userId 用户ID
   * @param curPage 当前页码
   * @param pageSize 每页条数
   * @return 防火墙实例列表
   * @throws Exception
   */
  public List<FirewallVO> searchFirewall(String name, int userId, int curPage, int pageSize) throws Exception;

  /**
   * 搜索防火墙实例数
   * @param name 实例名称
   * @param userId 用户ID
   * @return 防火墙实例数
   * @throws Exception
   */
  public int searchFirewallCount(String name, int userId) throws Exception;

  
  /**
   * 搜索防火墙或负载均衡实例数
   * @param userId 用户ID
   * @return 防火墙实例数
   * */ 
  public List<TInstanceInfoBO> getFWOrLBList(int userId) throws Exception;
  
  public List<TInstanceInfoBO> getBoughtOMList(int userId) throws Exception;
  //fix bug 2417
  public List queryBindedVSByIP(int userId);


  public List<VServiceRowPO> queryVirtialServiceListByUser(TUserBO user)
		throws Exception;  
  
}
