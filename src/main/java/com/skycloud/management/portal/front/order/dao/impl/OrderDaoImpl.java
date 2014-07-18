package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.service.IOrderHistoryService;
import com.skycloud.management.portal.webservice.usage.model.orderVO;

public class OrderDaoImpl extends SpringJDBCBaseDao implements IOrderDao {

	private static Log log = LogFactory.getLog(OrderDaoImpl.class);

	private IOrderHistoryService orderHistoryService;

	private List<Object> initInsertSQLArgs(TOrderBO order) {
		List<Object> args = new ArrayList<Object>();
		args.add(order.getType());
		args.add(order.getOrderApproveLevelState());
		args.add(order.getState());
		args.add(order.getOrderCode());
		args.add(order.getZoneId());
		args.add(order.getClusterId());
		args.add(order.getCreatorUserId());
		args.add(order.getInstanceInfoId());
		args.add(order.getCpuNum());
		args.add(order.getMemorySize());
		args.add(order.getStorageSize());
		args.add(order.getReason());
		return args;
	}

	private PreparedStatementCreator PreparedSaveSQLArgs(final TOrderBO order, final String sql) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, order.getType());
				ps.setInt(2, order.getOrderApproveLevelState());
				ps.setInt(3, order.getState());
				ps.setString(4, order.getOrderCode());
				ps.setLong(5, order.getZoneId());
				ps.setLong(6, order.getClusterId());
				ps.setInt(7, order.getCreatorUserId());
				ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
				ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
				ps.setInt(10, order.getInstanceInfoId());
				ps.setInt(11, order.getCpuNum());
				ps.setInt(12, order.getMemorySize());
				ps.setInt(13, order.getStorageSize());
				ps.setString(14, order.getReason());
				ps.setString(15, order.getResourceInfo());
				ps.setInt(16, order.getServiceInstanceId());
				ps.setInt(17, order.getTemplateId());
				ps.setInt(18, order.getProductId());
				return ps;
			}
		};
	}

	@Override
	public int save(final TOrderBO order) throws SCSException {
		log.debug("--------------save order begin!-------------------");
		int ret_val = -1;
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO T_SCS_ORDER(TYPE,ORDER_APPROVE_LEVEL_STATE,STATE,ORDER_CODE,ZONE_ID,CLUSTER_ID,CREATOR_USER_ID,CREATE_DT,LASTUPDATE_DT,"
		        + "INSTANCE_INFO_ID,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,REASON,RESOURCE_INFO,SERVICE_INSTANCE_ID,TEMPLATE_ID,PRODUCT_ID) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

		try {
			log.debug("sql=" + sql.toString());
			PreparedStatementCreator preCreator = PreparedSaveSQLArgs(order, sql);
			this.getJdbcTemplate().update(preCreator, keyHolder);
			ret_val = keyHolder.getKey().intValue();
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE, order.getReason(), order.getCreatorUserId(),
			                                     order.getCreateDt()));
		}
		log.debug("--------------save order end!-------------------");
		return ret_val;
	}

	@Override
	public int delete(final int orderId) throws SQLException {
		log.debug("--------------delete order begin!-------------------");
		log.debug("orderId=" + orderId);
		int ret_val = -1;
		String sql = "DELETE FROM T_SCS_ORDER WHERE ORDER_ID = ?;";
		try {
			log.debug("sql=" + sql.toString());
			ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			});
		}
		catch (Exception e) {
			log.debug("delete order error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_DELETE, orderId, e.getMessage()));
		}
		log.debug("--------------delete order end!-------------------");
		return ret_val;
	}

	@Override
	public int update(final TOrderBO order) throws SQLException {
		log.debug("--------------update order begin!-------------------");
		int ret_val = -1;
		String sql = "UPDATE T_SCS_ORDER set TYPE=?,ORDER_APPROVE_LEVEL_STATE=?,STATE=?,ORDER_CODE=?"+
		         " ,ZONE_ID=?,CLUSTER_ID=?,CREATOR_USER_ID=?,CREATE_DT=?,LASTUPDATE_DT=?"+
		         " ,INSTANCE_INFO_ID=?,CPU_NUM=?,MEMORY_SIZE=?,STORAGE_SIZE=?,REASON=? " +
		         " ,SERVICE_INSTANCE_ID=?" +
		         " WHERE ORDER_ID = ?;";

		try {
			log.debug("sql=" + sql.toString());
			ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, order.getType());
					ps.setInt(i++, order.getOrderApproveLevelState());
					ps.setInt(i++, order.getState());
					ps.setString(i++, order.getOrderCode());

					ps.setLong(i++, order.getZoneId());
					ps.setLong(i++, order.getClusterId());
					ps.setLong(i++, order.getCreatorUserId());
					ps.setTimestamp(i++, new Timestamp(order.getCreateDt().getTime()));
					ps.setTimestamp(i++, new Timestamp(order.getLastupdateDt().getTime()));

					ps.setLong(i++, order.getInstanceInfoId());
					ps.setInt(i++, order.getCpuNum());
					ps.setLong(i++, order.getMemorySize());
					ps.setLong(i++, order.getStorageSize());
					ps.setString(i++, order.getReason());
					ps.setInt(i++, order.getServiceInstanceId());
					// 条件
					ps.setInt(i++, order.getOrderId());
				}
			});
		}
		catch (Exception e) {
			log.debug("update order error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_UPDATE, order.getReason(), order.getCreatorUserId(),
			                                     order.getLastupdateDt(), e.getMessage()));
		}
		log.debug("--------------update order end!-------------------");
		return ret_val;
	}

	@Override
	public TOrderBO searchOrderById(final int orderId) throws SQLException {
		// 为解决服务实例ID字段(T_SCS_ORDER.SERVICE_INSTANCE_ID)值为空的问题
		this.getJdbcTemplate().execute("update T_SCS_ORDER set SERVICE_INSTANCE_ID=0 where SERVICE_INSTANCE_ID is null");
		String sql = "SELECT o.ORDER_ID,o.TYPE,o.ORDER_APPROVE_LEVEL_STATE,o.STATE,ORDER_CODE, "
		        + " o.ZONE_ID,o.CLUSTER_ID,o.CREATOR_USER_ID,o.CREATE_DT,o.LASTUPDATE_DT,o.RESOURCE_INFO,u.NAME as CREATE_USER_NAME, "
		        + " o.INSTANCE_INFO_ID,o.CPU_NUM,o.MEMORY_SIZE,o.STORAGE_SIZE,o.REASON "
		        + ",o.SERVICE_INSTANCE_ID ,o.INSTANCE_INFO_ID,o.PRODUCT_ID,o.TEMPLATE_ID "
		        + " FROM T_SCS_ORDER o,  T_SCS_USER u "
		        + " WHERE o.ORDER_ID = ? "
		        + " and o.CREATOR_USER_ID=u.ID ";
		BeanPropertyRowMapper<TOrderBO> orderRowMapper = new BeanPropertyRowMapper<TOrderBO>(TOrderBO.class);
		List<TOrderBO> returnList = null;
		try {
			log.debug("--------------searchOrderById  begin!-------------------");
			log.debug("sql=" + sql.toString());
			log.debug("orderId=" + orderId);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, orderId);
				}
			}, new RowMapperResultSetExtractor<TOrderBO>(orderRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("--------------searchOrderById  end!-------------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<TOrderBO> searchAllOrder() throws SQLException {
		String sql = "SELECT ORDER_ID,TYPE,ORDER_APPROVE_LEVEL_STATE,STATE,ORDER_CODE,"
		        + "ZONE_ID,CLUSTER_ID,CREATOR_USER_ID,CREATE_DT,LASTUPDATE_DT,"
		        + "INSTANCE_INFO_ID,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,REASON " +
		         ",o.SERVICE_INSTANCE_ID ,o.INSTANCE_INFO_ID,o.PRODUCT_ID,o.TEMPLATE_ID "+
		        " FROM T_SCS_ORDER";
		BeanPropertyRowMapper<TOrderBO> orderRowMapper = new BeanPropertyRowMapper<TOrderBO>(TOrderBO.class);

		List<TOrderBO> returnList = null;
		try {
			log.debug("--------------searchAllOrder  begin!-------------------");
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TOrderBO>(orderRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("--------------searchAllOrder end!-------------------");
		return returnList;
	}

	@Override
	public int searchLastId() throws SQLException {
		int returnId = -1;
		String sql = "SELECT ORDER_ID FROM T_SCS_ORDER ORDER BY ORDER_ID DESC LIMIT 0,1;";
		try {
			log.debug("--------------searchLastId  begin!-------------------");
			log.debug("sql=" + sql.toString());
			returnId = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			log.debug("searchLastId=" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("--------------searchLastId  end!-------------------");
		return returnId;
	}

	@Override
	public orderVO searchVMOrderByElInstanceId(String ElInstaceId) throws SQLException {
		int id = Integer.parseInt(ElInstaceId);
		orderVO tempOrder = new orderVO();
		List tempOrder1 = new ArrayList<Map<String, Object>>();
		String sql = " SELECT o.TYPE,o.ORDER_ID , o.ORDER_CODE , o.CREATOR_USER_ID,o.INSTANCE_INFO_ID,i.E_INSTANCE_ID "
		        + " FROM  T_SCS_ORDER AS o  RIGHT JOIN T_SCS_INSTANCE_INFO AS i "
		        + " ON o.ORDER_ID = i.ORDER_ID " + " WHERE o.TYPE=1 "
		        + " AND i.E_INSTANCE_ID=" + id;
		try {
			log.debug("--------------searchVMOrderByElInstanceId(String ElInstaceId)  begin!-------------------");
			log.debug("sql=" + sql);
			tempOrder1 = this.getJdbcTemplate().queryForList(sql);

		}
		catch (Exception e) {
			log.debug("searchVMOrderByElInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("--------------searchVMOrderByElInstanceId(String ElInstaceId)  end!-------------------");
		if (tempOrder1.size() == 0) {
			return null;
		} else {
			Map<String, Object> map = (Map<String, Object>) tempOrder1.get(0);
			tempOrder.setCreatorUserId(map.get("CREATOR_USER_ID").toString());
			tempOrder.setOrderCode((String) map.get("ORDER_CODE"));
			tempOrder.setOrderId(map.get("ORDER_ID").toString());
			tempOrder.setType((Integer) map.get("TYPE"));
			return tempOrder;
		}
	}

	@Override
	public orderVO searchLBOrderByElInstanceId(String ElInstaceId) throws SQLException {
		log.debug("------------------searchLBOrderByElInstanceId(String ElInstaceId)    begin!---------------------");
		orderVO tempOrder = new orderVO();
		List<Map<String, Object>> tempOrder1 = null;
		try {
			String sql = " SELECT o.TYPE,o.ORDER_ID , o.ORDER_CODE , o.CREATOR_USER_ID,o.INSTANCE_INFO_ID,i.E_INSTANCE_ID "
			        + " FROM  T_SCS_ORDER AS o  RIGHT JOIN T_SCS_INSTANCE_INFO AS i " + " ON o.ORDER_ID = i.ORDER_ID " + " WHERE i.TEMPLATE_TYPE =6 "
			        + " AND i.E_INSTANCE_ID =" + ElInstaceId;
			log.debug("sql=" + sql);
			tempOrder1 = this.getJdbcTemplate().queryForList(sql);
		}
		catch (Exception e) {
			log.error("searchLBOrderByElInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("------------------searchLBOrderByElInstanceId(String ElInstaceId)    end!---------------------");
		if (tempOrder1.size() == 0) {
			return null;
		} else {
			Map<String, Object> map = tempOrder1.get(0);
			tempOrder.setCreatorUserId(map.get("CREATOR_USER_ID").toString());
			tempOrder.setOrderCode((String) map.get("ORDER_CODE"));
			tempOrder.setOrderId(map.get("ORDER_ID").toString());
			tempOrder.setType((Integer) map.get("TYPE"));
			return tempOrder;
		}
	}

	@Override
	public List<TOrderBO> searchOrders(final PageVO vo, final TUserBO user, final QueryCriteria query) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		log.debug("----------------searchOrders(PageVO vo,TUserBO user,QueryCriteria query)  begin!----------");
		sql.append("SELECT ORDER_ID,TYPE,ORDER_APPROVE_LEVEL_STATE,STATE,ORDER_CODE," + "ZONE_ID,CLUSTER_ID,CREATE_DT,LASTUPDATE_DT,"
		        + "INSTANCE_INFO_ID,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,REASON FROM T_SCS_ORDER where CREATOR_USER_ID=? ");
		if (null != query) {
			if (query.getOrderStatus() > -1) {
				sql.append(" and STATE = ? ");
			}
			if (query.getPayStatus() > -1) {
				sql.append(" and TYPE = ? ");
			}
			if (query.getStartDate() != null) {
				sql.append(" and CREATE_DT >=? ");
			}
			if (query.getEndDate() != null) {
				sql.append(" and CREATE_DT <=? ");
			}
			// to fix bug 2289
			if (query.getOrderId() != 0) {
				sql.append(" and ORDER_ID =? ");
			}
		}
		sql.append(" ORDER BY CREATE_DT DESC  ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TOrderBO> myOrderRowMapper = new BeanPropertyRowMapper<TOrderBO>(TOrderBO.class);
		List<TOrderBO> returnList = null;

		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					ps.setInt(i++, user.getId());
					if (null != query) {
						if (query.getOrderStatus() > -1) {
							ps.setInt(i++, query.getOrderStatus());
						}
						if (query.getPayStatus() > -1) {
							ps.setInt(i++, query.getPayStatus());
						}
						if (query.getStartDate() != null) {
							ps.setString(i++, query.getStartDate() + " 00:00:00");
						}
						if (query.getEndDate() != null) {
							ps.setString(i++, query.getEndDate() + " 23:59:59");
						}
						if (query.getOrderId() != 0) {
							ps.setInt(i++, query.getOrderId());
						}

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
			}, myOrderRowMapper);
		}
		catch (Exception e) {
			log.error("searchOrders error:" + e.getMessage());
			throw new SQLException("查询我的订单失败原因：" + e.getMessage());
		}
		log.debug("----------------searchOrders(PageVO vo,TUserBO user,QueryCriteria query)  end!----------");
		return returnList;
	}

	@Override
	public List<TOrderBO> searchOrders2(final PageVO vo, final TUserBO user, final QueryCriteria query) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		log.debug("----------------searchOrders(PageVO vo,TUserBO user,QueryCriteria query)  begin!----------");
		sql.append("SELECT ORDER_ID,TYPE,ORDER_APPROVE_LEVEL_STATE,STATE,ORDER_CODE," + "ZONE_ID,CLUSTER_ID,CREATE_DT,LASTUPDATE_DT,"
		        + "INSTANCE_INFO_ID,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,REASON FROM T_SCS_ORDER where CREATOR_USER_ID=? ");
		if (null != query) {
			if (query.getOrderStatus() > -1) {
				sql.append(" and STATE = ? ");
			}
			if (query.getPayStatus() > 0) {
				sql.append(" and TYPE = ? ");
			}
			if (query.getStartDate() != null) {
				sql.append(" and CREATE_DT >=? ");
			}
			if (query.getEndDate() != null) {
				sql.append(" and CREATE_DT <=? ");
			}
			if (query.getOrderId() != 0) {
				sql.append(" and ORDER_ID =? ");
			}
		}
		sql.append(" ORDER BY CREATE_DT DESC  ");
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql.append(" limit ?, ?");
			}
		}
		BeanPropertyRowMapper<TOrderBO> myOrderRowMapper = new BeanPropertyRowMapper<TOrderBO>(TOrderBO.class);
		List<TOrderBO> returnList = null;

		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					ps.setInt(i++, user.getId());
					if (null != query) {
						if (query.getOrderStatus() > -1) {
							ps.setInt(i++, query.getOrderStatus());
						}
						if (query.getPayStatus() > 0) {
							ps.setInt(i++, query.getPayStatus());
						}
						if (query.getStartDate() != null) {
							ps.setString(i++, query.getStartDate() + " 00:00:00");
						}
						if (query.getEndDate() != null) {
							ps.setString(i++, query.getEndDate() + " 23:59:59");
						}
						if (query.getOrderId() != 0) {
							ps.setInt(i++, query.getOrderId());
						}

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
			}, myOrderRowMapper);
		}
		catch (Exception e) {
			log.error("searchOrders error:" + e.getMessage());
			throw new SQLException("查询我的订单失败原因：" + e.getMessage());
		}
		log.debug("----------------searchOrders(PageVO vo,TUserBO user,QueryCriteria query)  end!----------");
		return returnList;
	}

	@Override
	public int searchOrdersAmount(TUserBO user, QueryCriteria query) throws SQLException {
		log.debug("------------------searchOrdersAmount(TUserBO user, QueryCriteria query)  begin!--------------------");
		List<Object> param = new ArrayList<Object>();
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT count(0) FROM T_SCS_ORDER  where CREATOR_USER_ID=? ");
		param.add(user.getId());
		if (null != query) {
			if (query.getOrderStatus() > -1) {
				sql.append(" and STATE = ? ");
				param.add(query.getOrderStatus());
			}
			if (query.getOrderStatusStr() != null) {
				sql.append(" and STATE in (" + query.getOrderStatusStr() + ") ");
				// param.add(query.getOrderStatusStr());
			}
			if (query.getPayStatus() > -1) {
				sql.append(" and TYPE = ? ");
				param.add(query.getPayStatus());
			}
			if (query.getStartDate() != null) {
				sql.append(" and CREATE_DT >=? ");
				param.add(query.getStartDate() + " 00:00:00");
			}
			if (query.getEndDate() != null) {
				sql.append(" and CREATE_DT <=? ");
				param.add(query.getEndDate() + " 23:59:59");
			}
		}
		log.debug("sql=" + sql.toString());
		int amount = getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		log.debug("------------------searchOrdersAmount(TUserBO user, QueryCriteria query)  end!--------------------");
		return amount;
	}

	@Override
	public TUserBO getAuditorByOrderId(final int orderId) throws SQLException {
		log.debug("---------------getAuditorByOrderId(final int orderId) begin-----------");
		String sql = "select u.ID, u.NAME, L.CREATE_DT from T_SCS_USER u, T_SCS_ORDER_LOG L ";
		sql += " where u.ID=L.USER_ID and L.ORDER_ID=? ";
		sql += " order by L.CREATE_DT desc";// fix bug 0002495 0002745
		BeanPropertyRowMapper<TUserBO> infoRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			log.error("getAuditorByOrderId error:" + e.getMessage());
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		log.debug("---------------getAuditorByOrderId(final int orderId) end!-----------");
		return returnList.get(0);
	}

	// bug 0003046
	private String sql4ServiceInstanceByOrderId(int orderType) {
		String sql = "";
		sql = "select  o.REASON, tm.TEMPLATE_NAME, si.id, si.service_name as instance_name "
		        + "FROM T_SCS_SERVICE_INSTANCE si,  T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE tm "
		        + " where si.SERVICE_TYPE = tm.ID and o.ORDER_ID = si.ORDER_ID and  o.ORDER_ID = ? ";
		if (orderType == 2 || orderType == 3 || orderType == 4) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单
			// to fix bug:2634,2635(将i.ORDER_ID = ? 改为 o.ORDER_ID = ?)
			sql = "select  o.REASON, tm.TEMPLATE_NAME, si.id, si.service_name as instance_name "
			        + "FROM T_SCS_SERVICE_INSTANCE si,  T_SCS_ORDER o, T_SCS_TEMPLATE_TYPE tm "
			        + " where si.SERVICE_TYPE = tm.ID and o.SERVICE_INSTANCE_ID = si.ID and  o.ORDER_ID = ? ";
		}
		return sql;
	}

	@Override
	public List<TInstanceInfoBO> findInstanceByOrderId(final int orderId, int orderType) throws SQLException {
		log.debug("---------------findInstanceByOrderId(final int orderId,int orderType) begin-----------");
		String sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME FROM T_SCS_INSTANCE_INFO i ";
		sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID , T_SCS_TEMPLATE_TYPE tm where i.ORDER_ID = ? and t.type=tm.id";
		sql += " union ";
		sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME  FROM T_SCS_INSTANCE_INFO i ";
		sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.ORDER_ID=i.ORDER_ID where o.ORDER_ID = ? ";

		if (orderType == 2) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单

			// to fix bug:2634,2635(将i.ORDER_ID = ? 改为 o.ORDER_ID = ?)
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME  FROM T_SCS_INSTANCE_INFO i ";
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.INSTANCE_INFO_ID=i.ID, T_SCS_TEMPLATE_TYPE tm where o.ORDER_ID = ? and t.type=tm.id";
			sql += " union ";
			sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID join T_SCS_ORDER o on o.INSTANCE_INFO_ID=i.ID where o.ORDER_ID = ?";
		} else if (orderType == 3 || orderType == 4) {// 3表示删除订单、4表示续订订单
			// to fix bug:3568，3587 退订时在订单中先关联服务实例ID，再关联资源实例ID
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME FROM T_SCS_INSTANCE_INFO i ";
			sql += "join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ";
			sql += "join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
			sql += "join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID ";
			sql += " or o.INSTANCE_INFO_ID=i.ID, T_SCS_TEMPLATE_TYPE tm where o.ORDER_ID = ? and t.type=tm.id";
			sql += " union ";
			sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ";
			sql += "join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
			sql += "join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID ";
			sql += " or o.INSTANCE_INFO_ID=i.ID where o.ORDER_ID = ?";
		}
		// to fix bug:3886 (以下sql语句导致错误)
		// if (ConstDef.curProjectId == 1) {// bug 0003046
		// sql = sql4ServiceInstanceByOrderId(orderType);
		// }

		BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
		List<TInstanceInfoBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, orderId);
				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			log.error("findInstanceByOrderId error:" + e.getMessage());
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		log.debug("---------------findInstanceByOrderId(final int orderId,int orderType) end!-----------");
		return returnList;
	}

	@Override
	// bug 0003919
	public List<TInstanceInfoBO> findService2InstanceByOrderId(final int orderId, int orderType) throws SQLException {
		log.debug("---------------findInstanceByOrderId(final int orderId,int orderType) begin-----------");
		String sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME,  s.id service_id, s.service_name FROM T_SCS_INSTANCE_INFO i ";
		sql += " join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ";
		sql += " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID ";
		sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID";
		sql += " join T_SCS_ORDER o on o.ORDER_ID = s.ORDER_ID";
		sql += ",T_SCS_TEMPLATE_TYPE tm where o.ORDER_ID = ? and t.type=tm.id";
		sql += " union ";
		sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME, s.id service_id, s.service_name FROM T_SCS_INSTANCE_INFO i ";
		sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID  ";
		sql += " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID ";
		sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID ";
		sql += " join T_SCS_ORDER o on o.ORDER_ID=s.ORDER_ID ";
		sql += " where o.ORDER_ID = ?";
		if (orderType == 2) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单

			// to fix bug:2634,2635(将i.ORDER_ID = ? 改为 o.ORDER_ID = ?)
			// 变更时没有往服务里写订单号，由订单关联到实例上 0003898
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME, s.id service_id, s.service_name FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID";
			sql += " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID ";
			// bug 0003898
			sql += " join T_SCS_ORDER o on  o.INSTANCE_INFO_ID = i.ID ";
			sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID ";
			sql += ", T_SCS_TEMPLATE_TYPE tm where o.ORDER_ID = ? and t.type=tm.id";
			sql += " union ";
			sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME, s.id service_id, s.service_name  FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ";
			sql += " join T_SCS_PRODUCT_INSTANCE_REF r on r.INSTANCE_INFO_ID = i.ID ";
			sql += " join T_SCS_ORDER o on  o.INSTANCE_INFO_ID = i.ID ";
			sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID where o.ORDER_ID = ? ";
		} else if (orderType == 3 || orderType == 4) {// 3表示删除订单、4表示续订订单
			// to fix bug:3568，3587 退订时在订单中先关联服务实例ID，再关联资源实例ID
			sql = "select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tm.TEMPLATE_NAME, s.id service_id, s.service_name FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_VM t on  i.TEMPLATE_ID = t.ID ";
			sql += " join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
			sql += " join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID  or o.INSTANCE_INFO_ID=i.ID ";
			sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID";
			sql += ", T_SCS_TEMPLATE_TYPE tm where o.ORDER_ID = ? and t.type=tm.id";
			sql += " union ";
			sql += " select i.*,t.`CODE` TEMPLATE_CODE, t.TEMPLATE_DESC,o.MEMORY_SIZE ORDER_MEMORY_SIZE ,o.CPU_NUM ORDER_CPU_NUM,o.REASON,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,'小型机模板' as TEMPLATE_NAME, s.id service_id, s.service_name FROM T_SCS_INSTANCE_INFO i ";
			sql += " join T_SCS_TEMPLATE_MC t on  i.TEMPLATE_ID = t.ID ";
			sql += " join T_SCS_PRODUCT_INSTANCE_REF r on  r.INSTANCE_INFO_ID = i.ID ";
			sql += " join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = r.SERVICE_INSTANCE_ID or o.INSTANCE_INFO_ID=i.ID  ";
			sql += " join T_SCS_SERVICE_INSTANCE s on s.id=r.SERVICE_INSTANCE_ID where o.ORDER_ID = ?  ";
		}
		// to fix bug:3886 (以下sql语句导致错误)
		// if (ConstDef.curProjectId == 1) {// bug 0003046
		// sql = sql4ServiceInstanceByOrderId(orderType);
		// }

		BeanPropertyRowMapper<TInstanceInfoBO> infoRowMapper = new BeanPropertyRowMapper<TInstanceInfoBO>(TInstanceInfoBO.class);
		List<TInstanceInfoBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					ps.setInt(2, orderId);
				}
			}, infoRowMapper);
		}
		catch (Exception e) {
			log.error("findInstanceByOrderId error:" + e.getMessage());
			throw new SQLException("查询实例信息失败。失败原因：" + e.getMessage());
		}
		log.debug("---------------findInstanceByOrderId(final int orderId,int orderType) end!-----------");
		return returnList;
	}

	@Override
	public List<TOrderBO> searchOrdersByInstanceId(final int instanceId) throws SQLException {
		String sql = "SELECT ORDER_ID,TYPE,ORDER_APPROVE_LEVEL_STATE,STATE,ORDER_CODE,"
		        + "ZONE_ID,CLUSTER_ID,CREATOR_USER_ID,CREATE_DT,LASTUPDATE_DT,"
		        + "INSTANCE_INFO_ID,CPU_NUM,MEMORY_SIZE,STORAGE_SIZE,REASON FROM T_SCS_ORDER "
		        + " WHERE  STATE = 4 and INSTANCE_INFO_ID = ?   order by ORDER_ID DESC ;";

		BeanPropertyRowMapper<TOrderBO> orderRowMapper = new BeanPropertyRowMapper<TOrderBO>(TOrderBO.class);
		List<TOrderBO> returnList = null;
		try {
			log.debug("--------------searchOrdersByInstanceId  begin!-------------------");
			log.debug("sql=" + sql.toString());
			log.debug("instanceId=" + instanceId);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, instanceId);
				}
			}, new RowMapperResultSetExtractor<TOrderBO>(orderRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_QUERY, e.getMessage()));
		}
		log.debug("--------------searchOrdersByInstanceId  end!-------------------");
		return returnList;
	}

	public IOrderHistoryService getOrderHistoryService() {
		return orderHistoryService;
	}

	public void setOrderHistoryService(IOrderHistoryService orderHistoryService) {
		this.orderHistoryService = orderHistoryService;
	}

}
