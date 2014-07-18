package com.skycloud.management.portal.admin.audit.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.audit.entity.InstanceTypeBO;
import com.skycloud.management.portal.admin.audit.entity.TAuditBO;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.entity.TProductBO;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderLogBO;

/**
 * 审批对象持久化接口
 * <dl>
 * <dt>类名：IAuditDao</dt>
 * <dd>描述:</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2012-2-13 上午09:53:51</dd>
 * <dd>创建人： 张爽</dd>
 * </dl>
 */
public interface IAuditDao {

	/**
	 * 待审批列表
	 *
	 * @param loginUser
	 *            当前登录用户
	 * @param criteria
	 *            查询条件
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-9 下午04:36:27
	 */
	List<TAuditBO> pendingList(TUserBO loginUser, final QueryCriteria criteria,
			PageVO vo) throws SQLException;

	/**
	 * 审批完成列表
	 *
	 * @param loginUser
	 *            当前登录用户
	 * @param criteria
	 *            查询条件
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-9 下午04:37:16
	 */
	List<TAuditBO> finishList(TUserBO loginUser, final QueryCriteria criteria,
			PageVO vo) throws SQLException;

	/**
	 * 获得订单的信息
	 *
	 * @param orderId
	 * @param type
	 * @param vOrs
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-9 下午04:37:55
	 */
	List<TInstanceInfoBO> vmOrStorageOrSMDetailInfo(int orderId, int type,
			int vOrs) throws SQLException;

	/**
	 * //获得订单  类型；2：修改申请；3：删除申请 的信息
	 *
	 * @param orderId
	 * @param type
	 * @param vOrs
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:39:45
	 */
	List<TInstanceInfoBO> detailInfo(int orderId, int type, int vOrs)	throws SQLException;

	/**
	 * 获取订单信息,退订时更加订单中的服务ID
	 * type = 3：退订订单
	 * @param orderId
	 * @param type
	 * @param vOrs
	 * @return
	 * @throws SQLException
	 */
	List<TInstanceInfoBO> detailServiceInfo(int orderId, int type,int vOrs) throws SQLException;

	/**
	 * 获得vm等服务所挂的Vlan信息
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:40:10
	 */
	List<TNicsBO> vmVlanInfo(int orderId) throws SQLException;

	/**
	 * 记录订单审批流程
	 *
	 * @param log
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:41:00
	 */
	int insertOrderLogs(List<TOrderLogBO> log) throws SQLException;

	/**
	 * 查看当前用户所属级别是否是自动审批
	 *
	 * @param level
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:41:26
	 */
	String roleIsAutoCommit(int level) throws SQLException;

	/**
	 * 查看审批轨迹
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:42:13
	 */
	List<TOrderLogBO> orderLogList(int orderId) throws SQLException;

	/**
	 * 获得指定角色中的一个用户信息
	 *
	 * @param level
	 * @param approveUserId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:42:24
	 */
	TUserBO firstUserByLevel(int level, int approveUserId) throws SQLException;

	/**
	 * 审批功能 type：0,同意。1,拒绝
	 *
	 * @param level
	 * @param type
	 * @param orderId
	 * @param lastUpdateTime
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:43:22
	 */
	int updateOrder(int level, int type, int orderId, Date lastUpdateTime)
			throws SQLException;

	/**
	 * 审核已修改或者已作废的订单，改实例表中state字段为2
	 *
	 * @param instanceInfoId
	 *            实例ID
	 * @param orderType
	 *            订单类型；1：新申请；2：修改申请；3：删除申请
	 * @param auditType
	 *            审核类型; 1:拒绝,0:通过
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:43:47
	 */
	int updateInstance(int instanceInfoId, int orderType, int auditType, int orderId)
			throws SQLException;

	/**
	 * vdc专用
	 *
	 * @param orderId
	 * @param templeType
	 * @param orderType
	 * @param virtualServerName
	 *            负载均衡中虚服务的名称
	 * @return
	 * @throws SQLException
	 *             创建人： 王海东 创建时间：2012-5-30 下午02:13:38
	 */
	int updateInstance(int orderId, int templeType, int orderType,
			String virtualServerName) throws SQLException;

	/**
	 * 判断实例类型，返回临时实例
	 *
	 * @param orderId
	 * @param orderType
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:01
	 */
	List<InstanceTypeBO> getIntanceType(int orderId, int orderType)
			throws SQLException;

	/**
	 * 查看订单审批状态
	 *
	 * @param orderId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:16
	 */
	int getOrderState(int orderId) throws SQLException;

	/**
	 * 新订单列表
	 *
	 * @param loginUser
	 * @param criteria
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:27
	 */
	List<TAuditBO> getOrderNewList(TUserBO loginUser,
			final QueryCriteria criteria) throws SQLException;

	/**
	 * 审批中列表
	 *
	 * @param loginUser
	 * @param criteria
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:34
	 */
	List<TAuditBO> getOrderCheckingList(TUserBO loginUser,
			final QueryCriteria criteria) throws SQLException;

	/**
	 * 审批完成列表
	 *
	 * @param loginUser
	 * @param criteria
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:43
	 */
	List<TAuditBO> getOrderCheckedList(TUserBO loginUser,
			final QueryCriteria criteria) throws SQLException;

	/**
	 * 审批完成列表
	 *
	 * @param loginUser
	 * @param criteria
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-13 上午09:44:52
	 */
	List<TAuditBO> getOrderRefuseList(TUserBO loginUser,
			final QueryCriteria criteria) throws SQLException;

	/**
	 * 获得产品信息
	 *
	 * @param proId
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-2-23 下午02:46:03
	 */
	TProductBO getProductByProId(int proId) throws SQLException;

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
	 * vdc订单审核通过该实例表中state状态为“正在执行中”
	 *
	 * @param instanceInfoId 实例id
	 * @orderType 订单类型；1：新申请；2：修改申请；3：删除申请
	 * @orderId 订单号
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-3-29 下午05:55:47
	 */
	int updateInstanceState_VDC(int instanceInfoId, int orderType, int orderId) throws SQLException;

	/**
	 * 云平台1.1用
	 *
	 * @param orderId
	 * @param orderType
	 * @return
	 * @throws SQLException
	 *             创建人： 张爽 创建时间：2012-3-29 下午07:25:50
	 */
	List<InstanceTypeBO> findTemplateTypeList(int orderId, int orderType)
			throws SQLException;

	/**
	 * 云平台1.1用
	 *
	 * @param userId
	 * @return
	 * @throws SQLException
	 *             创建人： CQ
	 */
	public int queryOrderLogCountByUser(final int userId) throws SQLException;

	public int queryUnAuditOrderCountByUser(int userId);

	/**
	 * 通过实例的模板类型查询待审核订单 (让负载均衡和安全组一起退订)
	 *
	 * @param templateType
	 * @return
	 * @throws SQLException
	 */
	// to fix bug:2477
	public List<TAuditBO> queryWaitApproveOrderByInstanceInfo(int orderId,
			int templateType) throws SQLException;

	/**
	 * 查询指定type的资源剩余量
	 *
	 * @param type
	 * @return
	 */
	public int findSurplusByType(int type);
}
