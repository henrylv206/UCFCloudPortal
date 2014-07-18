package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.h3c.loadbalance.po.vservice.VServiceRowPO;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.network.h3c.entity.HLJVpnInstance;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.FirewallVO;
import com.skycloud.management.portal.front.resources.action.vo.ObjectStorageVO;

/**
 * 防火墙资源使用相关业务接口
 * 
 * @author jiaoyz
 */
public interface IFirewallService {

	public List<ObjectStorageVO> getObjectStorage(int userId, int curPage, int pageSize) throws Exception;

	/**
	 * 获取防火墙实例列表
	 * 
	 * @param userId
	 *            用户ID
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @return 已经通过审核的防火墙实例列表
	 * @throws Exception
	 */
	public List<FirewallVO> getFirewallList(int userId, int curPage, int pageSize) throws Exception;

	/**
	 * 获取防火墙实例数
	 * 
	 * @param userId
	 *            用户ID
	 * @return 已经通过审核的防火墙实例数
	 * @throws Exception
	 */
	public int getFirewallListCount(int userId) throws Exception;

	/**
	 * 搜索防火墙实例列表
	 * 
	 * @param name
	 *            实例名称
	 * @param userId
	 *            用户ID
	 * @param curPage
	 *            当前页码
	 * @param pageSize
	 *            每页条数
	 * @return 防火墙实例列表
	 * @throws Exception
	 */
	public List<FirewallVO> searchFirewall(String name, int userId, int curPage, int pageSize) throws Exception;

	/**
	 * 搜索防火墙实例数
	 * 
	 * @param name
	 *            实例名称
	 * @param userId
	 *            用户ID
	 * @return 防火墙实例数
	 * @throws Exception
	 */
	public int searchFirewallCount(String name, int userId) throws Exception;

	public void destroyObjectStorage(int instanceId, TUserBO user, String reason) throws Exception;

	/**
	 * 退订防火墙
	 * 
	 * @param instanceId
	 *            实例ID
	 * @param user
	 *            退订人
	 * @param reason
	 *            退订理由
	 * @throws Exception
	 */
	public void destroyFirewall(int instanceId, TUserBO user, String reason) throws Exception;

	/**
	 * 特殊模板申请创建申请模板
	 * 
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	public TTemplateVMBO creatSpecalFirewallTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

	/**
	 * 查询防火墙或负载均衡
	 * 
	 * @param user
	 *            创建人
	 * @throws Exception
	 */
	public List<TInstanceInfoBO> queryFWOrLBInstance(int userId) throws Exception;

	public List<TInstanceInfoBO> queryBoughtOMInstance(int userId) throws Exception;

	public void insertDestroyFirewall(int instanceId, int lbId, TUserBO user, String reason) throws Exception;

	public void insertDestroyfw(int instanceId, TUserBO user, String reason, int serviceID) throws Exception;

	public void insertDestroyOM(int instanceId, TUserBO user, String reason) throws Exception;

	public void insertDestroyOS(int instanceId, TUserBO user, String reason, int serviceID) throws Exception;

	public void insertDestoryLoadBalance(int instanceId, TUserBO user, String reason) throws Exception;

	void insertDestroyLoadBalance(int instanceId, int lbId, TUserBO user, String reason) throws Exception;

	void insertDestroylb(int instanceId, TUserBO user, String reason, int serviceID) throws Exception;

	// fix bug 2417
	List queryBindedVSByIP(int userId);

	public long searchBindTipIdBylbId(TUserBO user, int lbId) throws Exception;

	public List<VServiceRowPO> queryVirtualServiceListByUser(TUserBO user)throws Exception;

	public HLJVpnInstance getInstanceByUserId(int userId) throws Exception;;
}
