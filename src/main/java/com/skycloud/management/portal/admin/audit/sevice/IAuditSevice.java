package com.skycloud.management.portal.admin.audit.sevice;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.InstanceTypeBO;
import com.skycloud.management.portal.admin.audit.entity.TAuditBO;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.ListHosts;
import com.skycloud.management.portal.front.command.res.listIpAddressesByNetWork;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderLogBO;
import com.skycloud.management.portal.front.resources.rest.CheckPhysicalHost;

/**
 * 订单管理业务接口
 * <dl>
 * <dt>类名：IAuditSevice</dt>
 * <dd>描述:</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2012-2-8 下午01:26:16</dd>
 * <dd>创建人： 张爽</dd>
 * </dl>
 */
public interface IAuditSevice {

	/**
	 * 查询待审核订单
	 *
	 * @param loginUser
	 *            当前登录用户信息
	 * @param criteria
	 *            查询条件
	 * @return 返回订单列表
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 上午10:39:42
	 */
	List<TAuditBO> pendingList(TUserBO loginUser, QueryCriteria criteria,
			PageVO vo) throws SQLException;

	/**
	 * 查询当前用户审核过得订单
	 *
	 * @param loginUser
	 *            当前登录用户信息
	 * @param criteria
	 *            查询条件
	 * @return 返回订单列表
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 上午10:44:58
	 */
	List<TAuditBO> finishList(TUserBO loginUser, QueryCriteria criteria,
			PageVO vo) throws SQLException;

	/**
	 * 订单详情
	 *
	 * @param orderId
	 *            订单id
	 * @param type
	 *            订单模板类型
	 * @param vOrs
	 *            辅助区分虚拟机和磁盘
	 * @param orderType
	 *            订单类别，新申请、作废、修改
	 * @return 订单实例
	 * @throws Exception
	 *             创建人： 张爽 创建时间：2012-2-8 上午10:48:26
	 */
	List<TInstanceInfoBO> detailOrder(int orderId, int type, int vOrs,
			int orderType) throws Exception;

	/**
	 * 查看当前用户级别是否是自动审批
	 *
	 * @param approveUserId
	 * @param orderId
	 * @param level
	 * @param type
	 * @param commit
	 * @param email
	 * @param orderType
	 * @param templateType
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 下午01:19:04
	 */
	void isAutoApproveUser(int approveUserId, int orderId, int level, int type,
			String commit, String email, int orderType, int templateType)
			throws SQLException;

	/**
	 * 查看订单审批流程
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 下午01:20:53
	 */
	List<TOrderLogBO> orderLogList(int orderId) throws SQLException;

	/**
	 * 订单审批动作
	 *
	 * @param approveUserId
	 *            审批人id
	 * @param orderId
	 *            订单id
	 * @param level
	 *            审批级别
	 * @param type
	 *            0,同意。1,拒绝
	 * @param commit
	 * @param email
	 * @param orderType
	 * @param templateType
	 *            订单模板类别
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 下午01:21:49
	 */
	int approveOrder(int approveUserId, int orderId, int level, int type,
			String commit, String email, int orderType, int templateType)
			throws SQLException;

	/**
	 * 判断实例类型，返回临时实例
	 *
	 * @param orderId
	 * @param orderType
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 下午01:23:27
	 */
	List<InstanceTypeBO> getIntanceType(int orderId, int orderType)
			throws SQLException;

	/**
	 * 获得订单状态
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-8 下午01:23:47
	 */
	int getOrderState(int orderId) throws SQLException;

	List<TAuditBO> orderNewList(TUserBO loginUser, QueryCriteria criteria)
			throws SQLException;

	List<TAuditBO> orderCheckingList(TUserBO loginUser, QueryCriteria criteria)
			throws SQLException;

	List<TAuditBO> orderCheckedList(TUserBO loginUser, QueryCriteria criteria)
			throws SQLException;

	List<TAuditBO> orderRefuseList(TUserBO loginUser, QueryCriteria criteria)
			throws SQLException;

	/**
	 * 查询待审核订单总条数
	 *
	 * @param criteria
	 * @param type
	 *            -1:待审核订单类表，1：为审核后订单列表
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-3-19 下午01:25:42
	 */
	int orderAcount(QueryCriteria criteria, int type, TUserBO user)
			throws SQLException;

	/**
	 * 查询用户的审核订单数量
	 *
	 * @param userId
	 * @return
	 * @throws SQLException
	 *             创建人： 陈强
	 */
	public int queryOrderLogCountByUser(int userId) throws SQLException;

	/**
	 * 获取剩余Vlan状态: 1:vlanDefaultFreeCount<1, 2:vlanOtherFreeCount<0,
	 * 3:vlanTwoFreeCount<0
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	int getVlanFreeState(int orderId) throws SQLException;

	/**
	 * 查询对应的资源是否充足
	 *
	 * @param templateType
	 * @return ok:充足；0：虚拟机资源不足；1：块存储资源不足；2：公网IP资源不足
	 *
	 * 创建人：guoguangjun
	 */
	String getResourceState(int templateType);

	 /**
     * 根据id查询网卡nics记录
     * @param nicsId
     * @return
     * @throws SQLException
     */
	TNicsBO searchNicsById(int nicsId) throws SQLException;
    /**
     * 设置vlan和ip信息
     * @param nics
     * @return
     * @throws SQLException
     */
    int updateNics(TNicsBO nics) throws SQLException;

    /**
	 * 列出vlan剩余ip地址
	 * @param networkId  网卡id
	 * @return
	 * @throws Exception
	 */
	public List<listIpAddressesByNetWork> listIpAddressesByNetWork(long networkId, Integer resourcePoolsId) throws Exception;

	/**
	 * 根据订单id查询未设定vlan的网卡nics
	 * @param orderId
	 * @return
	 * @throws SQLException
	 */
	List<TNicsBO> searchNicsDhcpByOrderId(int orderId) throws SQLException;

	/**
	 * 修改实例信息中的主机
	 *
	 * @param eHostId
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public int updateInstanceInfo(int eHostId, int id) throws SQLException;
	/**
	 * 列出主机
	 * @param zoneId
	 * @param resourcePoolId
	 * @return
	 * @throws Exception
	 */
	public List<ListHosts> ListHosts(int templateId, int zoneId, Integer resourcePoolsId) throws Exception;

	/**
	 * 列出默认网卡信息
	 *
	 * @param zoneId
	 *            资源域
	 * @param resourcePoolID
	 *            资源池
	 * @param userId
	 *            用户信息
	 * @return
	 * @throws Exception
	 */
	List<ENetwork> searchNetworkListDefault(int zoneId, Integer resourcePoolsId, int orderId) throws Exception;

	/**
	 * 列出非默认网卡信息
	 *
	 * @param zoneId
	 *            资源域
	 * @param resourcePoolID
	 *            资源池
	 * @param userId
	 *            用户信息
	 * @param networkType
	 *            第几块网卡
	 * @return
	 * @throws Exception
	 */
	List<ENetwork> searchNetworkListOther(int zoneId, Integer resourcePoolsId, int orderId, int networkType) throws Exception;

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

	/**
	 * 检查物理机是否可用
	 * @param checkPhysicalHost
	 * @param resourcePoolId
	 * @return 返回值 >0 可用物理机数，=0 资源不足  <0 调用接口失败
	 * @throws Exception
	 */
	public int CheckPhysicalHost(final int orderId) throws Exception ;
	
}
