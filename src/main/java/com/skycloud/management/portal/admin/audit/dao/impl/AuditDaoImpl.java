package com.skycloud.management.portal.admin.audit.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.admin.audit.dao.IAuditDao;
import com.skycloud.management.portal.admin.audit.entity.InstanceTypeBO;
import com.skycloud.management.portal.admin.audit.entity.TAuditBO;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.admin.audit.entity.TProductBO;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderLogBO;

/**
 * 审批对象持久化实现
 * <dl>
 * <dt>类名：AuditDaoImpl</dt>
 * <dd>描述:</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2012-2-13 上午09:53:36</dd>
 * <dd>创建人： 张爽</dd>
 * </dl>
 */
public class AuditDaoImpl extends SpringJDBCBaseDao implements IAuditDao {

	private static Log log = LogFactory.getLog(AuditDaoImpl.class);

	@Override
	public List<TAuditBO> pendingList(final TUserBO loginUser, final QueryCriteria criteria, PageVO vo) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID ");
		//to fix bug:3600
		sql.append(" where o.STATE >=1 ");
		sql.append(" and o.STATE=? ");
		// to fix bug:1970
		if (loginUser.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户只能审核本部门的普通用户提交的待处理订单
			sql.append(" and u.DEPT_ID=? ");
		}
		if (criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		if (criteria.getUserName() != null && criteria.getUserName() != "") {
			sql.append(" and u.ACCOUNT LIKE ? ");
		}
		sql.append(" order by  o.CREATE_DT ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append("  limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					ps.setInt(i++, loginUser.getRoleApproveLevel() - 1);
					// to fix bug:1970
					if (loginUser.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户只能审核本部门的普通用户提交的待处理订单
						ps.setInt(i++, loginUser.getDeptId());
					}
					if (criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(i++, "%" + criteria.getOrderCode().trim() + "%");
					}
					if (criteria.getUserName() != null && criteria.getUserName() != "") {
						ps.setString(i++, "%" + criteria.getUserName().trim() + "%");
					}
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(i++, (curPage - 1) * pageSize);
							ps.setInt(i++, pageSize);
						}
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TInstanceInfoBO> vmOrStorageOrSMDetailInfo(final int orderId, final int type, final int vOrs) throws SQLException {
		String si = "i.id, i.order_id, i.template_id, i.template_type, i.product_id, i.instance_name, i.resource_info, i.cpu_num, "
		        + "i.memory_size, i.storage_size, i.cluster_id, i.create_dt, i.state, i.lastupdate_dt, i.e_instance_id, i.e_service_id,"
			+ " i.e_disk_id, i.e_network_id, i.e_os_id, i.os_desc, i.comment, i.res_code, i.expire_date, i.e_host_id, t.zone_id";
		/**
		 * to fix bug: 0001754, to fix bug 1495
		 */
		String sql = "select " + si + ",t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.NETWORK_DESC,t.EXTEND_ATTR_JSON,";
		sql += " t.CPU_NUM  templateCpuNum, t.MEMORY_SIZE templateMemorySize,t.STORAGE_SIZE templateStorageSize,t.SPECIAL ,t.STORE_TYPE ,t.VETH_ADAPTOR_NUM,";
		sql += " o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID, ";
		sql +=" s.PERIOD,s.UNIT,s.PRICE ";
		sql +=" FROM T_SCS_INSTANCE_INFO i ";
		sql += " left join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
		sql += " left join T_SCS_SERVICE_INSTANCE s on  s.ID = r.SERVICE_INSTANCE_ID ";
		if (type == 1 || type == 2) {// 虚机和块存储
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where i.ORDER_ID = ? and i.TEMPLATE_TYPE=? and t.TYPE=?";
		} else if (type == 3) {// 小型机
			// T_SCS_TEMPLATE_MC 没有t.NETWORK_DESC 字段 update by CQ 20120604
			sql = "select " + si + ",t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,";
			// to fix bug 2134(小型机资源订单的详细信息部分显示不正确)
			sql += " t.CPU_NUM  templateCpuNum, t.MEMORY_SIZE templateMemorySize,t.STORAGE_SIZE templateStorageSize,";
			sql += " s.PERIOD,s.UNIT,s.PRICE, ";
			//小机的特殊模板标志需要查询出来，前台才允许修改 ninghao@chinaskycloud.com 2013-01-23
			sql += " o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID, t.SPECIAL";
			sql += " FROM T_SCS_INSTANCE_INFO i ";
			sql += "join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID  ";
			sql += "join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID ";
			sql += " left join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
			sql += " left join T_SCS_SERVICE_INSTANCE s on  s.ID = r.SERVICE_INSTANCE_ID ";
			sql += " where i.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		} else if (type >= 4) {// 备份，监控，网络等
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where i.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		} else {
			return null;
		}
		//fix bug:4846
		sql +=" group by i.id";
		BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
		List<TInstanceInfoBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, type);
					//					if (type == 2) {//to fix bug:3045
					//						ps.setInt(2, 3);
					//					}
					//					if (vOrs == 2){//to fix bug:3255
					//						ps.setInt(2, 2);
					//					}
					if (type == 1 || type == 2 ) {
						ps.setInt(3, vOrs);
					}
				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		return returnList;

	}

	@Override
	public List<TNicsBO> vmVlanInfo(final int orderId) throws SQLException {
		String sql = "SELECT r.ID,r.VM_INSTANCE_INFO_ID,r.E_VLAN_ID,r.IP,r.STATE  FROM T_SCS_NICS r LEFT JOIN T_SCS_INSTANCE_INFO info  ON r.VM_INSTANCE_INFO_ID = info.ID "
			+ " LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=info.ORDER_ID  WHERE o.ORDER_ID=? and r.ID is not NULL;";
		BeanPropertyRowMapper<TNicsBO> vlanRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, vlanRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询vm实例或是小型机实例所分配的IP地址以及Vlan信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	/**
	 * fix 1472
	 * */
	public List<TAuditBO> finishList(final TUserBO loginUser, final QueryCriteria criteria, PageVO vo) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select distinct o.ORDER_ID,o.TYPE ORDER_TYPE,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,o.STATE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID JOIN T_SCS_ORDER_LOG lo ON lo.ORDER_ID=o.ORDER_ID  ");
		sql.append(" where  lo.ROLE_APPROVE_LEVEL>=2");
		sql.append(" and lo.ROLE_APPROVE_LEVEL=? ");
		// to fix bug:1970
		if (loginUser.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户查看本部门已处理订单
			sql.append(" and u.DEPT_ID=? ");
		}
		if (criteria.getOrderId() > 0) {
			sql.append(" and o.ORDER_ID =  ? ");
		}
		if (criteria.getOrderStatus() > -1) {
			sql.append(" and o.STATE = ? ");
		}
		if (StringUtils.isNotEmpty(criteria.getStartDate())) {
			sql.append(" and o.CREATE_DT >=? ");
		}
		if (StringUtils.isNotEmpty(criteria.getEndDate())) {
			sql.append(" and o.CREATE_DT <=? ");
		}
		if (criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		if (criteria.getUserName() != null && criteria.getUserName() != "") {
			sql.append(" and u.ACCOUNT LIKE ? ");
		}
		sql.append(" order by  o.LASTUPDATE_DT DESC ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					ps.setInt(i++, loginUser.getRoleApproveLevel());
					// to fix bug:1970
					if (loginUser.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户查看本部门已处理订单
						ps.setInt(i++, loginUser.getDeptId());
					}
					if (criteria.getOrderId() > 0) {
						ps.setInt(i++, criteria.getOrderId());
					}
					if (criteria.getOrderStatus() > -1) {
						ps.setInt(i++, criteria.getOrderStatus());
					}
					if (StringUtils.isNotEmpty(criteria.getStartDate())) {
						ps.setString(i++, criteria.getStartDate() + " 00:00:00");
					}
					if (StringUtils.isNotEmpty(criteria.getEndDate())) {
						ps.setString(i++, criteria.getEndDate() + " 23:59:59");
					}
					if (criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(i++, "%" + criteria.getOrderCode().trim() + "%");
					}
					if (criteria.getUserName() != null && criteria.getUserName() != "") {
						ps.setString(i++, "%" + criteria.getUserName().trim() + "%");
					}
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(i++, (curPage - 1) * pageSize);
							ps.setInt(i++, pageSize);
						}
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询已审批订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int insertOrderLogs(final List<TOrderLogBO> log) throws SQLException {
		String sql = "INSERT INTO T_SCS_ORDER_LOG (ORDER_DETAIL_ID,USER_ID,ORDER_ID,CREATE_DT,STATE,COMMIT,ROLE_APPROVE_LEVEL) VALUES(?,?,?,?,?,?,?);";
		this.getJdbcTemplate().batchUpdate(sql, new BatchPreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps, int i) throws SQLException {
				ps.setInt(1, log.get(i).getOrderDetailId());
				ps.setInt(2, log.get(i).getUserId());
				ps.setInt(3, log.get(i).getOrderId());
				ps.setTimestamp(4, new Timestamp(log.get(i).getCreateDt().getTime()));
				ps.setInt(5, log.get(i).getState());
				ps.setString(6, log.get(i).getCommit());
				ps.setInt(7, log.get(i).getRoleApproveLevel());
			}

			@Override
			public int getBatchSize() {
				return log.size();
			}
		});
		return 0;
	}

	@Override
	public String roleIsAutoCommit(final int level) throws SQLException {
		// if(level==4){
		// return 0;
		// }
		String sql = "SELECT p.VALUE FROM T_SCS_PARAMETERS p WHERE p.TYPE=?;";
		Map<String, Object> map = this.getJdbcTemplate().queryForMap(sql, new Object[] { "ROLE_" + level + "_AUTO" });
		return String.valueOf(map.get("VALUE"));
	}

	@Override
	public List<TOrderLogBO> orderLogList(final int orderId) throws SQLException {
		//fix bug:7476 (u.NAME 改为 u.ACCOUNT)
		String sql = "SELECT l.ORDER_DETAIL_ID,l.USER_ID,l.ORDER_ID,l.CREATE_DT,l.STATE,l.COMMIT, u.ACCOUNT AUDIT_USER_NAME,l.ROLE_APPROVE_LEVEL FROM T_SCS_ORDER_LOG l JOIN T_SCS_USER u ON l.USER_ID = u.ID  WHERE l.ORDER_ID=? order by l.ORDER_DETAIL_ID;";
		BeanPropertyRowMapper<TOrderLogBO> orderLogRowMapper = new BeanPropertyRowMapper<TOrderLogBO>(TOrderLogBO.class);
		List<TOrderLogBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, orderLogRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询审核轨迹失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int updateOrder(final int level, final int type, final int orderId, final Date lastUpdateTime) throws SQLException {
		String sql = "update T_SCS_ORDER set STATE=?,LASTUPDATE_DT=? where ORDER_ID=? ";
		int result = 0;
		try {
			result = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					if (type == 0) {
						ps.setInt(1, level);
					} else if (type == 1) {
						ps.setInt(1, 5);
					}
					ps.setTimestamp(2, new Timestamp(lastUpdateTime.getTime()));
					ps.setInt(3, orderId);
					// ps.setInt(4, level-1);//这个先这样写，先改bug，之后再改
				}
			});
		}
		catch (Exception e) {
			throw new SQLException("审批订单失败。订单编号：" + orderId + "失败原因：" + e.getMessage());
		}
		return result;
	}

	@Override
	public int updateInstance(final int instanceInfoId, final int orderType, int auditType, final int orderId) throws SQLException {
		int ret_val = 0;
		String sql = "";
		if (auditType == 1) {// 审核拒绝
			if (orderType == 1) {// 新申请
				// to fix bug:0001973 新申请类型订单 审核拒绝， 实例需要改为作废状态:i.state = 4
				sql = "update T_SCS_INSTANCE_INFO i SET i.LASTUPDATE_DT = ?,i.STATE = 4  where i.ID=? ";
			}  else if (orderType == 2 || orderType == 3 )  {// 修改单,删除单
				// to fix bug:0001869,0001871 解决
				// 退订和修改在订单审核拒绝时，原来的实例状态反写不成功(i.CLUSTER_ID 改为 r.CLUSTER_ID)
				// to fix bug:0001929,0001930
				// 关于退订的功能所有的模块已经查过，都是在退订时将状态备份到instanceinfo.cluster_id中，
				// r.CLUSTER_ID 改为 i.CLUSTER_ID
				//				sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.STATE = i.CLUSTER_ID   where r.ORDER_ID=? and i.TEMPLATE_TYPE=?";
				//to fix bug:3642,3704,3700
				//状态回滚为就绪
				sql = "update T_SCS_INSTANCE_INFO i "
					+    "  join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID "
					+    "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
					+    "  or o.INSTANCE_INFO_ID = i.ID "
					+    " SET i.LASTUPDATE_DT = ? , i.STATE = "
					+    "  case when i.CLUSTER_ID >=1  then  i.CLUSTER_ID  "
					+    "   when i.CLUSTER_ID <=0  then  2  end "
					+    "  WHERE i.ID=?   and o.ORDER_ID = ? ";
			}else if (orderType == 4) {// 续订
				sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ?  where i.ID=? ";
			}
		} else {// 审核通过
			if (orderType == 1) {// 新申请
				// to fix bug:2308
				sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ?,i.STATE = 2  where i.ID=? ";
			} else if (orderType == 2) {// 修改单
				// to fix bug:2274 虚拟机备份空间无法修改
				// 处理：i.ORDER_ID=? 改为 r.ORDER_ID=?
				sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.STATE = 2 ,i.STORAGE_SIZE = r.STORAGE_SIZE where i.ID=?   and r.ORDER_ID = ? ";
			} else if (orderType == 3) {// 删除单
				// to fix bug:2431: 云监控服务退订后一直处于“命令正在执行中”状态
				// 处理：i.ORDER_ID=? 改为 r.ORDER_ID=?
				//				sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.STATE = 4  where r.ORDER_ID=? and i.TEMPLATE_TYPE=? ";
				sql = "update T_SCS_INSTANCE_INFO i "
					+    "  join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID "
					+    "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
					+    "  or o.INSTANCE_INFO_ID = i.ID "
					+    " SET i.LASTUPDATE_DT = ?,i.STATE = 4  "
					+    "  WHERE i.ID=?  and o.ORDER_ID = ? ";
			}else if (orderType == 4) {// 续订
				// to fix bug:4922
				sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ?  where i.ID=? ";
			}
		}

		try {
			ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					ps.setInt(2, instanceInfoId);
					if (orderType==2 || orderType==3){
						ps.setInt(3, orderId);
					}
				}
			});
		}
		catch (Exception e) {
			throw new SQLException("审批订单后修改实例状态失败。资源实例ID：" + instanceInfoId + "失败原因：" + e.getMessage());
		}
		return ret_val;
	}

	@Override
	public int updateInstance(final int orderId, final int templateType, final int orderType, final String vServerName) throws SQLException {
		String sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.ORDER_ID=i.ORDER_ID SET i.LASTUPDATE_DT = ? ,i.OS_DESC = ? ,i.STATE = i.CLUSTER_ID   where r.ORDER_ID=?";
		if (templateType == 4 || templateType == 5 || templateType == 6 || templateType == 7 || templateType == 8 || templateType == 9
				|| templateType == 15) {
			if (orderType == 1) {
				sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ? ,i.OS_DESC = ? ,i.STATE = 2  where i.ORDER_ID=?";
			} else if (orderType == 2) {// 负载均衡中虚服务的名称i.OS_DESC
				sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.OS_DESC = ? ,i.STATE = 2 ,i.STORAGE_SIZE = r.STORAGE_SIZE where r.ORDER_ID=?";
			} else if (orderType == 3) {
				sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.OS_DESC = ? ,i.STATE = 4  where r.ORDER_ID=?";
			}
		}
		try {
			this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					ps.setString(2, vServerName);// 负载均衡中虚服务的名称 vServerName
					// vdc专用
					ps.setInt(3, orderId);
				}
			});
		}
		catch (Exception e) {
			throw new SQLException("审批订单后修改实例状态失败。订单编号：" + orderId + "失败原因：" + e.getMessage());
		}
		return 0;
	}

	@Override
	public TUserBO firstUserByLevel(final int level, final int approveUserId) throws SQLException {
		String sql = "";
		// 下单人Id为0
		if (approveUserId == 0) {
			sql = "SELECT u.ID,u.NAME FROM T_SCS_ROLE r JOIN T_SCS_USER u ON r.ROLE_ID=u.ROLE_ID WHERE r.ROLE_APPROVE_LEVEL=?;";
		} else {
			// 解决1级2级自动审核为同一个审核人的问题
			sql = "SELECT u.ID,u.NAME FROM T_SCS_ROLE r JOIN T_SCS_USER u ON r.ROLE_ID=u.ROLE_ID WHERE r.ROLE_APPROVE_LEVEL=?;";
			// sql="SELECT u.ID,u.NAME FROM T_SCS_ROLE r JOIN T_SCS_USER u ON r.ROLE_ID=u.ROLE_ID  WHERE r.ROLE_APPROVE_LEVEL=? and u.DEPT_ID = (select ud.DEPT_ID from T_SCS_USER ud where ud.ID = ?);";
		}
		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, level);
					// if(level == 2){
					// ps.setInt(2, approveUserId);
					// }
				}
			}, userRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询审核轨迹失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<InstanceTypeBO> getIntanceType(final int orderId, final int orderType) throws SQLException {
		//to fix bug:3467(u.`ACCOUNT` as  NAME)
		String sql = "SELECT info.TEMPLATE_TYPE,info.STORAGE_SIZE,u.`ACCOUNT` as  NAME,d.DEPT_NAME,u.EMAIL, o.CREATOR_USER_ID USER_ID FROM T_SCS_ORDER o JOIN T_SCS_INSTANCE_INFO info ON info.ORDER_ID=o.ORDER_ID JOIN T_SCS_USER u ON u.ID=o.CREATOR_USER_ID JOIN T_SCS_DEPARTMENT d ON d.DEPT_ID=u.DEPT_ID WHERE o.ORDER_ID=?;";
		if (orderType == 2) {
			sql = "SELECT info.TEMPLATE_TYPE,info.STORAGE_SIZE,u.`ACCOUNT` as NAME,d.DEPT_NAME,u.EMAIL, o.CREATOR_USER_ID USER_ID  FROM T_SCS_ORDER o JOIN T_SCS_INSTANCE_INFO info ON o.INSTANCE_INFO_ID=info.ID JOIN T_SCS_USER u ON u.ID=o.CREATOR_USER_ID JOIN T_SCS_DEPARTMENT d ON d.DEPT_ID=u.DEPT_ID WHERE o.ORDER_ID=?;";
		}else if (orderType == 3 || orderType == 4) {
			sql = "SELECT info.TEMPLATE_TYPE,info.STORAGE_SIZE,u.`ACCOUNT` as NAME,d.DEPT_NAME,u.EMAIL, o.CREATOR_USER_ID USER_ID  FROM T_SCS_INSTANCE_INFO info "
				+  "	join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = info.ID   join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
				+  "		or o.INSTANCE_INFO_ID=info.ID JOIN T_SCS_USER u ON u.ID=o.CREATOR_USER_ID JOIN T_SCS_DEPARTMENT d ON d.DEPT_ID=u.DEPT_ID WHERE o.ORDER_ID=?;";
		}
		BeanPropertyRowMapper<InstanceTypeBO> RowMapper = new BeanPropertyRowMapper<InstanceTypeBO>(InstanceTypeBO.class);
		List<InstanceTypeBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, RowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询实例所属类型失败。失败原因：" + e.getMessage());
		}
		return returnList;

	}

	@Override
	public List<TInstanceInfoBO> detailInfo(final int orderId, final int type, final int vOrs) throws SQLException {
		/*
		 * to fix bug:1435 to fix bug: 0001754 to fix bug 1495
		 */
		String sql = "select  i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.NETWORK_DESC,t.EXTEND_ATTR_JSON,";
		sql += " o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,o.STORAGE_SIZE ORDER_STORAGE_SIZE, ";
		sql +=" s.PERIOD,s.UNIT,s.PRICE ";
		sql +=" FROM T_SCS_INSTANCE_INFO i ";
		sql += " left join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
		sql += " left join T_SCS_SERVICE_INSTANCE s on  s.ID = r.SERVICE_INSTANCE_ID ";
		if (type == 1 || type ==2) {// 虚机或块存储
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.INSTANCE_INFO_ID=i.ID where o.ORDER_ID = ? and i.TEMPLATE_TYPE=? and t.TYPE=?";
		} else if (type == 3) {// 小型机
			// T_SCS_TEMPLATE_MC表没有字段: NETWORK_DESC, EXTEND_ATTR_JSON
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,";
			sql += "o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,o.STORAGE_SIZE ORDER_STORAGE_SIZE FROM T_SCS_INSTANCE_INFO i ";
			sql += "join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.INSTANCE_INFO_ID=i.ID where o.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		} else if (type >= 4) {// 备份，监控和网络
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.INSTANCE_INFO_ID=i.ID where o.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		} else {
			return null;
		}
		//fix bug:4846 4998
		sql+=" and s.HISTORY_STATE=0 group by i.id ";
		//		System.out.println("detailInfo sql="+sql);;
		BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
		List<TInstanceInfoBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, type);
					if (type == 1 || type ==2) {
						ps.setInt(3, vOrs);
					}
				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}



	@Override
	public List<TInstanceInfoBO> detailServiceInfo(final int orderId, final int type, final int vOrs) throws SQLException {
		/*
		 * to fix bug:1435 to fix bug: 0001754 to fix bug 1495
		 */
		String sql = "select i.*,o.RESOURCE_INFO, s.EXPIRY_DATE as EXPIRE_DATE, t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,t.NETWORK_DESC,t.EXTEND_ATTR_JSON,";
		sql += " o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,o.STORAGE_SIZE ORDER_STORAGE_SIZE, ";
		sql +=" s.PERIOD,s.UNIT,s.PRICE ";
		sql +=" FROM T_SCS_INSTANCE_INFO i ";
		sql += " left join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
		sql += " left join T_SCS_SERVICE_INSTANCE s on  s.ID = r.SERVICE_INSTANCE_ID ";
		if (type == 3) {// 小型机
			// T_SCS_TEMPLATE_MC表没有字段: NETWORK_DESC, EXTEND_ATTR_JSON
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,";
			sql += "o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,o.STORAGE_SIZE ORDER_STORAGE_SIZE FROM T_SCS_INSTANCE_INFO i ";
			sql += "join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID "
				+    " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID "
				+    "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
				+    "  join T_SCS_SERVICE_INSTANCE s on s.ID = r.SERVICE_INSTANCE_ID "
				+    "  or o.INSTANCE_INFO_ID = i.ID "
				+"  where o.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		} else  {// 虚机或块存储 ,备份，监控和网络
			sql += " join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID "
				//to fix bug:5045 5053 4828 5073
//				+    " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID "
//				+    "  join T_SCS_SERVICE_INSTANCE s on s.ID = r.SERVICE_INSTANCE_ID "
				+    "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
				+    "  or o.INSTANCE_INFO_ID = i.ID "
				+     " where o.ORDER_ID = ? and i.TEMPLATE_TYPE=? ";
		}
		//		System.out.println("detailServiceInfo sql = "+sql);
		BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
		List<TInstanceInfoBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, type);

				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	// 一下是portal订单
	@Override
	public List<TAuditBO> getOrderNewList(final TUserBO loginUser, final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID  where u.ID = ? AND (o.STATE=1 or (o.STATE<4 and o.STATE=o.ORDER_APPROVE_LEVEL_STATE) ) ");
		if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		sql.append(" order by  o.CREATE_DT desc;");
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, loginUser.getId());
					if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(2, "%" + criteria.getOrderCode() + "%");
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TAuditBO> getOrderCheckingList(final TUserBO loginUser, final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID  where u.ID = ? AND (o.STATE<4 and o.STATE<>o.ORDER_APPROVE_LEVEL_STATE) ");
		if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		sql.append(" order by  o.CREATE_DT desc;");
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, loginUser.getId());
					if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(2, "%" + criteria.getOrderCode() + "%");
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TAuditBO> getOrderCheckedList(final TUserBO loginUser, final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID  where u.ID = ? AND o.STATE=4 ");
		if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		sql.append(" order by  o.CREATE_DT desc;");
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, loginUser.getId());
					if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(2, "%" + criteria.getOrderCode() + "%");
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public List<TAuditBO> getOrderRefuseList(final TUserBO loginUser, final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,u.ACCOUNT from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID  where u.ID = ? AND o.STATE=5 ");
		if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
		}
		sql.append(" order by  o.CREATE_DT desc;");
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, loginUser.getId());
					if (criteria != null && criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
						ps.setString(2, "%" + criteria.getOrderCode() + "%");
					}
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int getOrderState(final int orderId) throws SQLException {
		String sql = "SELECT p.STATE FROM T_SCS_ORDER p WHERE p.ORDER_ID=?;";
		/*
		 * Map<String, Object> map=this.getJdbcTemplate().queryForMap(sql, new
		 * Object[]{orderId}); if(map!=null){ return (Integer)map.get("STATE");
		 * }
		 */

		BeanPropertyRowMapper<TOrderLogBO> orderLogRowMapper = new BeanPropertyRowMapper<TOrderLogBO>(TOrderLogBO.class);
		List<TOrderLogBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, orderLogRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单状态。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0).getState();
		} else {
			return -1;
		}
	}

	@Override
	public TProductBO getProductByProId(final int proId) throws SQLException {
		String sql = "SELECT CODE,NAME,STATE,DESCRIPTION,CREATE_DATE,SPECIFICATION,QUOTA_NUM,PERIOD,PRICE,UNIT,PICTURE,DOC,TEMPLATE_ID,IS_DEFAULT   FROM T_SCS_PRODUCT WHERE ID=?;";
		BeanPropertyRowMapper<TProductBO> RowMapper = new BeanPropertyRowMapper<TProductBO>(TProductBO.class);
		List<TProductBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, proId);
				}
			}, RowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询实例关联的产品信息。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public int orderAcount(QueryCriteria criteria, int type, TUserBO user) throws SQLException {
		List<Object> param = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		if (type == -1) {
			sql.append("select count(0) from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID  where o.STATE=?");
			param.add(user.getRoleApproveLevel() - 1);
			// to fix bug:1970
			if (user.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户查看本部门未审核的订单
				sql.append(" and u.DEPT_ID=? ");
				param.add(user.getDeptId());
			}
		} else if (type == 1) {
			sql.append("select count(0) from T_SCS_ORDER o JOIN T_SCS_USER u ON o.CREATOR_USER_ID=u.ID JOIN T_SCS_ORDER_LOG lo ON lo.ORDER_ID=o.ORDER_ID  where  lo.ROLE_APPROVE_LEVEL=?");
			param.add(user.getRoleApproveLevel());
			// to fix bug:1970
			if (user.getRoleApproveLevel() == 2 && ConstDef.getCloudId() == 2) {// 私有云高级用户查看本部门已审核的订单
				sql.append(" and u.DEPT_ID=? ");
				param.add(user.getDeptId());
			}
		} else {
			return 0;
		}
		if (criteria.getOrderId() > 0) {
			sql.append(" and o.ORDER_ID =  ? ");
			param.add(criteria.getOrderId());
		}
		if (criteria.getOrderStatus() > -1) {
			sql.append(" and o.STATE = ? ");
			param.add(criteria.getOrderStatus());
		}
		if (criteria.getOrderCode() != null && criteria.getOrderCode() != "") {
			sql.append(" and o.ORDER_CODE LIKE ? ");
			param.add("%" + criteria.getOrderCode().trim() + "%");
		}
		if (criteria.getUserName() != null && criteria.getUserName() != "") {
			sql.append(" and u.ACCOUNT LIKE ? ");
			param.add("%" + criteria.getUserName().trim() + "%");
		}
		if (StringUtils.isNotEmpty(criteria.getStartDate())) {
			sql.append(" and o.CREATE_DT >=? ");
			param.add(criteria.getStartDate() + " 00:00:00");
		}
		if (StringUtils.isNotEmpty(criteria.getEndDate())) {
			sql.append(" and o.CREATE_DT <=? ");
			param.add(criteria.getEndDate() + " 23:59:59");
		}
		return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
	}

	@Override
	public int updateInstanceState_VDC(final int instanceInfoId,final int orderType,final int orderId) throws SQLException {
		String sql = "";
		if (orderType == 1) {//添加
			sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ?  where i.ID=?";//,i.STATE = 3
		} else if (orderType == 2) {//修改
			sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?,i.STORAGE_SIZE = r.STORAGE_SIZE,i.CPU_NUM = r.CPU_NUM,i.MEMORY_SIZE = r.MEMORY_SIZE where i.ID=?  and r.ORDER_ID = ?  ";//,i.STATE = 3
		} else if (orderType == 3) {//删除
			sql = "update T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER r ON r.INSTANCE_INFO_ID=i.ID SET i.LASTUPDATE_DT = ?  where i.ID=?   and   r.ORDER_ID = ? ";//,i.STATE = 3
		} else if (orderType == 4) {//续订
			sql = "update T_SCS_INSTANCE_INFO i  SET i.LASTUPDATE_DT = ?,i.STATE = 2  where i.ID=?";
		}
		try {
			this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
					ps.setInt(2, instanceInfoId);
					if(orderType==2 || orderType==3){
						ps.setInt(3, orderId);
					}
				}
			});
		}
		catch (Exception e) {
			throw new SQLException("VDC 审批订单后修改实例状态失败。资源实例ID：" + instanceInfoId + "失败原因：" + e.getMessage());
		}
		return 0;
	}

	@Override
	public List<InstanceTypeBO> findTemplateTypeList(final int orderId, int orderType) throws SQLException {
		String sql = "";
		if (orderType == 1) {//添加
			sql = "select i.TEMPLATE_TYPE,i.RESOURCE_INFO,i.ID,i.TEMPLATE_ID  from  T_SCS_INSTANCE_INFO i where i.ORDER_ID=?";
		} else if (orderType == 2) {//修改
			sql = "select i.TEMPLATE_TYPE,i.RESOURCE_INFO,i.ID,i.TEMPLATE_ID  from T_SCS_INSTANCE_INFO i JOIN T_SCS_ORDER o ON o.INSTANCE_INFO_ID=i.ID   where o.ORDER_ID=?";
		} else if (orderType == 3 || orderType == 4) {//退订，续订
			sql = "select i.TEMPLATE_TYPE,i.RESOURCE_INFO,i.ID,i.TEMPLATE_ID  from T_SCS_INSTANCE_INFO i "
				+    "  join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID "
				+    "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID "
				+    "  or o.INSTANCE_INFO_ID = i.ID "
				+     "  where o.ORDER_ID=?";
		}
		BeanPropertyRowMapper<InstanceTypeBO> RowMapper = new BeanPropertyRowMapper<InstanceTypeBO>(InstanceTypeBO.class);
		List<InstanceTypeBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, RowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单中实例的类型。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public int queryOrderLogCountByUser(int userId) throws SQLException {
		String sql = "SELECT COUNT(*) FROM T_SCS_ORDER_LOG WHERE USER_ID=?";
		return this.getJdbcTemplate().queryForInt(sql, new Object[] { userId });
	}

	@Override
	// fix bug 2204
	public int queryUnAuditOrderCountByUser(int userId) {
		StringBuilder sql = new StringBuilder("select count(*) from T_SCS_ORDER  o  join");
		sql.append(" (select distinct a.order_id from ");
		sql.append(" (select distinct order_id  from T_SCS_ORDER_LOG o  ) a left join");
		sql.append(" (select distinct order_id  from T_SCS_ORDER_LOG o where o.role_approve_level = 4) b on ");
		sql.append(" a.order_id = b.order_id where b.order_id is NULL) oo on o.order_id=oo.order_id");
		sql.append(" where o.STATE<>6 and o.CREATOR_USER_ID =?");
		log.debug("--------------:" + sql.toString());
		log.debug("--------------:" + userId);
		return this.getJdbcTemplate().queryForInt(sql.toString(), new Object[] { userId });
	}

	@Override
	public List<TAuditBO> queryWaitApproveOrderByInstanceInfo(final int orderId, final int templateType) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		//to fix bug:2477,3773
		sql.append("select o.TYPE ORDER_TYPE,o.ORDER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.ORDER_CODE,o.STATE,o.INSTANCE_INFO_ID,i.TEMPLATE_TYPE from T_SCS_ORDER o  ");
		sql.append(" left join T_SCS_ORDER o2 on o2.CREATOR_USER_ID=o.CREATOR_USER_ID  ");
		sql.append(" left join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = o.SERVICE_INSTANCE_ID  ");
		sql.append(" left join T_SCS_INSTANCE_INFO i on  i.ID =r.INSTANCE_INFO_ID   ");
		sql.append("	or o.INSTANCE_INFO_ID = i.ID  ");
		sql.append("  where o.STATE<4 and o2.ORDER_ID = ?  and i.TEMPLATE_TYPE=?  ");
		BeanPropertyRowMapper<TAuditBO> auditRowMapper = new BeanPropertyRowMapper<TAuditBO>(TAuditBO.class);
		List<TAuditBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, templateType);
				}
			}, auditRowMapper);
		}
		catch (Exception e) {
			throw new SQLException("查询订单失败。失败原因：" + e.getMessage());
		}
		return returnList;

	}

	@Override
	public int findSurplusByType(int type) {
		String sql = "SELECT SURPLUS FROM T_SCS_RESOURCE_STATE WHERE TYPE = ? ";
		return getJdbcTemplate().queryForInt(sql, type);
	}

}
