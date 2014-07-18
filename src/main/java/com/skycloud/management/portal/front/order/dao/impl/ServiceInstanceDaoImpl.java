package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.order.dao.IServiceInstanceDao;

public class ServiceInstanceDaoImpl extends SpringJDBCBaseDao implements IServiceInstanceDao {

	private static Log log = LogFactory.getLog(ServiceInstanceDaoImpl.class);

	public static final String ERROR_MESSAGE_SERVICEINSTANCE_DAO_CREATE = "创建服务实例失败。serviceInstanceId：%s，orderId：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_SERVICEINSTANCE_DAO_DELETE = "删除服务实例失败. id: %s, 失败原因：%s";

	public static final String ERROR_MESSAGE_SERVICEINSTANCE_DAO_UPDATE = "创建服务实例失败。serviceInstanceId：%s，orderId：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY = "查询服务实例失败。 失败原因：%s";

	private PreparedStatementCreator PreparedSaveSQLArgs(final TServiceInstanceBO serviceInstance, final String sql) {
		return new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				int i = 1;
				ps.setInt(i++, serviceInstance.getOrderId());
				ps.setLong(i++, serviceInstance.getProductId());
				ps.setString(i++, serviceInstance.getServiceName());
				ps.setInt(i++, serviceInstance.getServiceType());
				ps.setString(i++, serviceInstance.getServiceDesc());
				if (serviceInstance.getCreateDt() != null) {// bug 0005410
					ps.setTimestamp(i++, new Timestamp(serviceInstance.getCreateDt().getTime()));
				}else{
					ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				}
				ps.setTimestamp(i++, new Timestamp(serviceInstance.getExpiryDate().getTime()));
				ps.setInt(i++, serviceInstance.getNum());
				ps.setString(i++, serviceInstance.getFlag());
				ps.setInt(i++, serviceInstance.getState());
				// 新增购买周期和价格
				ps.setInt(i++, serviceInstance.getPeriod());
				ps.setString(i++, serviceInstance.getUnit());
				ps.setDouble(i++, serviceInstance.getPrice());
				//当前的服务实例ID设置为0
				ps.setInt(i++, serviceInstance.getHistoryId());
				ps.setInt(i++, serviceInstance.getHistoryState());
				return ps;
			}
		};
	}

	@Override
	public int save(final TServiceInstanceBO serviceInstance) throws SQLException {
		log.debug("-----------------save()  ServiceInstance  begin!--------------");
		int returnValue = -1;

		String sql = "INSERT INTO T_SCS_SERVICE_INSTANCE("
			+ " ORDER_ID,PRODUCT_ID,SERVICE_NAME,SERVICE_TYPE,SERVICE_DESC,"
			+ " CREATE_DT,EXPIRY_DATE,NUM,FLAG,STATE,PERIOD,  UNIT, PRICE, HISTORY_ID,HISTORY_STATE) VALUES "
			+ " (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
		try {
			log.debug("sql=" + sql.toString());
			KeyHolder keyHolder = new GeneratedKeyHolder();

			PreparedStatementCreator preCreator = PreparedSaveSQLArgs(serviceInstance, sql);
			this.getJdbcTemplate().update(preCreator, keyHolder);
			returnValue = keyHolder.getKey().intValue();
		}
		catch (Exception e) {
			e.printStackTrace();
			log.error("save  ServiceInstance  error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_CREATE, serviceInstance.getId(),
			                                     serviceInstance.getOrderId(), e.getMessage()));
		}
		log.debug("-----------------save()  ServiceInstance  end!--------------");
		return returnValue;
	}

	@Override
	public int delete(final int id) throws SQLException {
		log.debug("-----------------delete  ServiceInstance  begin!--------------");
		int returnValue = -1;
		String sql = "DELETE FROM T_SCS_SERVICE_INSTANCE WHERE ID = ?;";
		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);
				}
			});
		}
		catch (Exception e) {
			log.error("delete  ServiceInstance  error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_DELETE, id, e.getMessage()));
		}
		log.debug("-----------------delete  ServiceInstance  end!--------------");
		return returnValue;
	}

	@Override
	public int update(final TServiceInstanceBO serviceInstance) throws SQLException {
		log.debug("-----------------update  ServiceInstance  begin!--------------");
		int returnValue = -1;
		String sql = "update T_SCS_SERVICE_INSTANCE set "
			+ " ORDER_ID=?,PRODUCT_ID=?,SERVICE_NAME=?,SERVICE_TYPE=?,SERVICE_DESC=?,"
			+ " EXPIRY_DATE=?,STATE=?,NUM=?,FLAG=?, PERIOD=?,  UNIT=?, PRICE=?,HISTORY_ID=?,HISTORY_STATE=?  WHERE ID = ?;";

		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, serviceInstance.getOrderId());
					ps.setLong(i++, serviceInstance.getProductId());
					ps.setString(i++, serviceInstance.getServiceName());
					ps.setInt(i++, serviceInstance.getServiceType());
					ps.setString(i++, serviceInstance.getServiceDesc());

					ps.setTimestamp(i++, new Timestamp(serviceInstance.getExpiryDate().getTime()));
					ps.setInt(i++, serviceInstance.getState());
					ps.setInt(i++, serviceInstance.getNum());
					ps.setString(i++, serviceInstance.getFlag());
					// 新增购买周期和价格
					ps.setInt(i++, serviceInstance.getPeriod());
					ps.setString(i++, serviceInstance.getUnit());
					ps.setDouble(i++, serviceInstance.getPrice());
					// 设置为变更前的服务ID
					ps.setInt(i++, serviceInstance.getHistoryId());
					ps.setInt(i++, serviceInstance.getHistoryState());

					// 条件
					ps.setInt(i++, serviceInstance.getId());
				}
			});
		}
		catch (Exception e) {
			log.error("update  Nics error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_UPDATE, serviceInstance.getId(),
			                                     serviceInstance.getOrderId(), e.getMessage()));

		}
		log.debug("-----------------update  ServiceInstance  end!--------------");
		return returnValue;
	}

	@Override
	public TServiceInstanceBO searchById(final int id) throws SQLException {
		log.debug("-----------------searchById(int InstanceInfoId)   begin!--------------");
		String sql = "select ID,ORDER_ID,PRODUCT_ID,SERVICE_NAME,SERVICE_TYPE,SERVICE_DESC," +
		" CREATE_DT,EXPIRY_DATE,STATE,NUM,FLAG,PERIOD,  UNIT, PRICE , HISTORY_ID,HISTORY_STATE " +
		" from T_SCS_SERVICE_INSTANCE " +
		" where ID = ? ;";

		BeanPropertyRowMapper<TServiceInstanceBO> orderRowMapper = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
		List<TServiceInstanceBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("id=" + id);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);
				}
			}, new RowMapperResultSetExtractor<TServiceInstanceBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoById error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchById(int InstanceInfoId)   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<TServiceInstanceBO> searchAll() throws SQLException {

		log.debug("-----------------searchById(int InstanceInfoId)   begin!--------------");
		String sql = "select ID,ORDER_ID,PRODUCT_ID,SERVICE_NAME,SERVICE_TYPE,SERVICE_DESC," +
		" CREATE_DT,EXPIRY_DATE,STATE,NUM,FLAG,PERIOD,  UNIT, PRICE , HISTORY_ID,HISTORY_STATE " +
		" from T_SCS_SERVICE_INSTANCE  ;";

		BeanPropertyRowMapper<TServiceInstanceBO> rowMapper = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
		List<TServiceInstanceBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TServiceInstanceBO>(rowMapper));
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoAll error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchAll(int InstanceInfoId)   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<TServiceInstanceBO> searchByOrderId(final int orderId, final int orderType) throws SQLException {
		log.debug("-----------------searchByOrderId(int orderId)   begin!--------------");
		String sql = "select  count(r.PI_ID) SUM, s.ID, s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
			+ "  s.CREATE_DT, s.EXPIRY_DATE, s.STATE, s.NUM,FLAG, "
			+ "  s.PERIOD,  s.UNIT, s.PRICE"
			+ "  from T_SCS_SERVICE_INSTANCE s "
			+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
			+ "  group by r.SERVICE_INSTANCE_ID "
			+ "  having  s.ORDER_ID = ? ;";
		//to fix bug:3386,3393,3231
		//to fix bug:3468,3540,1330,3343,3543
		//to fix bug:3694,3695
		if (orderType == 2 || orderType == 3 || orderType == 4) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单
			sql = "select  distinct(s.ID), s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
				+ "  s.CREATE_DT, s.EXPIRY_DATE,s.STATE, s.NUM,FLAG, "
				+ "  s.PERIOD,  s.UNIT, s.PRICE, o.ORDER_ID newOrderId,"
				//fix bug 3853
				//+" i.STATE instanceState, "
				+ "  (select count(r2.PI_ID) from T_SCS_PRODUCT_INSTANCE_REF r2 where r2.SERVICE_INSTANCE_ID = s.ID ) SUM  "
				+ "  from T_SCS_SERVICE_INSTANCE s "
				+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
				+ "   join T_SCS_INSTANCE_INFO i  on i.ID=r.INSTANCE_INFO_ID "
				+ "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = s.ID "
				+ "  or o.INSTANCE_INFO_ID=i.ID "
				+ "  where o.ORDER_ID = ? ; ";
			// + "  group by r.SERVICE_INSTANCE_ID having  o.ORDER_ID = ? ;";
		}
		BeanPropertyRowMapper<TServiceInstanceBO> orderRowMapper = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
		List<TServiceInstanceBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("id=" + orderId);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, new RowMapperResultSetExtractor<TServiceInstanceBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoById error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchByOrderId(int orderId)   end!--------------");

		return returnList;

	}

	@Override
	public List<TServiceInstanceBO> searchByOrderId(final PageVO vo,final int orderId, final int orderType) throws SQLException {
		log.debug("-----------------searchByOrderId(int orderId)   begin!--------------");
		String sql = "select  count(r.PI_ID) SUM, s.ID, s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
			+ "  s.CREATE_DT, s.EXPIRY_DATE, s.STATE, s.NUM,FLAG, "
			+ "  s.PERIOD,  s.UNIT, s.PRICE"
			+ "  from T_SCS_SERVICE_INSTANCE s "
			+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
			+ "  group by r.SERVICE_INSTANCE_ID "
			+ "  having  s.ORDER_ID = ? ";
		//to fix bug:3386,3393,3231
		//to fix bug:3468,3540,1330,3343,3543
		//to fix bug:3694,3695
		if ( orderType == 3 || orderType == 4) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单
			sql = "select  distinct(s.ID), s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
				+ "  s.CREATE_DT, s.EXPIRY_DATE,s.STATE, s.NUM,FLAG, "
				+ "  s.PERIOD,  s.UNIT, s.PRICE, o.ORDER_ID newOrderId,"
				//fix bug 3853
				//+" i.STATE instanceState, "
				+ "  (select count(r2.PI_ID) from T_SCS_PRODUCT_INSTANCE_REF r2 where r2.SERVICE_INSTANCE_ID = s.ID ) SUM  "
				+ "  from T_SCS_SERVICE_INSTANCE s "
				+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
				+ "   join T_SCS_INSTANCE_INFO i  on i.ID=r.INSTANCE_INFO_ID "
				+ "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = s.ID "
				+ "  or o.INSTANCE_INFO_ID=i.ID "
				//to fix bug:5242，5262,5282,5281
				+ "  where o.ORDER_ID = ? ";
			//针对修改排重
			//bug id:5194
			//			        + "  group by s.ID having  o.ORDER_ID = ? ";
		}else if(orderType == 2){
			sql = "select  distinct(s.ID), s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
				+ "  s.CREATE_DT, s.EXPIRY_DATE,s.STATE, s.NUM,FLAG, "
				+ "  s.PERIOD,  s.UNIT, s.PRICE, o.ORDER_ID newOrderId,"
				//fix bug 3853
				//+" i.STATE instanceState, "
				+ "  (select count(r2.PI_ID) from T_SCS_PRODUCT_INSTANCE_REF r2 where r2.SERVICE_INSTANCE_ID = s.ID ) SUM  "
				+ "  from T_SCS_SERVICE_INSTANCE s "
				+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
				+ "   join T_SCS_INSTANCE_INFO i  on i.ID=r.INSTANCE_INFO_ID "
				+ "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = s.ID "
				+ "  or o.INSTANCE_INFO_ID=i.ID "
				//bug id:5149 针对修改排重
				//bug id:5245  修改时，以服务实例中的订单号为准
				+ "  group by s.ID having  s.ORDER_ID = ? ";

		}
		final PageVO page = vo;
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql += " limit ?, ?";
			}
		}


		BeanPropertyRowMapper<TServiceInstanceBO> orderRowMapper = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
		List<TServiceInstanceBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("id=" + orderId);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					if (page != null) {
						int curPage = page.getCurPage();
						int pageSize = page.getPageSize();
						if (curPage > 0 && pageSize > 0) {
							ps.setInt(2, (curPage - 1) * pageSize);
							ps.setInt(3, pageSize);
						}
					}

				}
			}, new RowMapperResultSetExtractor<TServiceInstanceBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoById error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchByOrderId(int orderId)   end!--------------");

		return returnList;

	}



	@Override
	public int countDetailByOrderId(final int orderId, final int orderType) throws SQLException {
		log.debug("-----------------searchByOrderId(int orderId)   begin!--------------");
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT count(0) "
			+" FROM ("
			+" select  s.* "
			+ "  from T_SCS_SERVICE_INSTANCE s "
			+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
			+ "  group by r.SERVICE_INSTANCE_ID "
			+ "  having  s.ORDER_ID = ? ) as detail";
		if (orderType == 2 || orderType == 3 || orderType == 4) {// orderType：1表示新申请订单、2表示修改订单、3表示删除订单、4表示续订订单
			sql = "select  count(0) "
				+ "  from T_SCS_SERVICE_INSTANCE s "
				+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
				+ "   join T_SCS_INSTANCE_INFO i  on i.ID=r.INSTANCE_INFO_ID "
				+ "  join T_SCS_ORDER o on o.SERVICE_INSTANCE_ID = s.ID "
				+ "  or o.INSTANCE_INFO_ID=i.ID "
				+ "  where o.ORDER_ID = ? ;";

		}
		param.add(orderId);
		int amount = -1;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("id=" + orderId);
			amount = getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoById error:" + e.getMessage());
			throw new SQLException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchByOrderId(int orderId)   end!--------------");

		return amount;

	}


	@Override
	public int updateServiceStateByInstanceInfoId(final int state,final int instanceInfoId) throws SCSException {
		int ret_val = -1;
		String sql = "update T_SCS_SERVICE_INSTANCE s set s.STATE=? where s.ID = "
			+" (SELECT r.SERVICE_INSTANCE_ID from  T_SCS_PRODUCT_INSTANCE_REF r "
			+" where r.SERVICE_INSTANCE_ID = s.ID " +
			" and s.HISTORY_STATE = 0 " +
			" and r.INSTANCE_INFO_ID = ?);";
		List<Object> args = new ArrayList<Object>();
		args.add(state);
		args.add(instanceInfoId);
		try {
			ret_val = this.getJdbcTemplate().update(sql, args.toArray());
		}
		catch (Exception e) {
		}
		return ret_val;
	}

	@Override
	public int updateServiceStateByOrderId(int state, int orderId) throws SCSException {
		int ret_val = -1;
		String sql = "update T_SCS_SERVICE_INSTANCE s set s.STATE=? where s.ORDER_ID =? ";
		List<Object> args = new ArrayList<Object>();
		args.add(state);
		args.add(orderId);
		try {
			ret_val = this.getJdbcTemplate().update(sql, args.toArray());
		}
		catch (Exception e) {
		}
		return ret_val;
	}

	@Override
	public int updateServiceStateByServiceId(int state, int serviceId) throws SCSException {
		int ret_val = -1;
		String sql = "update T_SCS_SERVICE_INSTANCE s set s.STATE=? where s.ID = ?;";
		List<Object> args = new ArrayList<Object>();
		args.add(state);
		args.add(serviceId);
		try {
			ret_val = this.getJdbcTemplate().update(sql, args.toArray());
		}
		catch (Exception e) {
		}
		return ret_val;
	}

	@Override
	public int rollbackServiceStateByServiceId(int serviceId) throws SCSException {
		int ret_val = -1;
		String sql = "update T_SCS_SERVICE_INSTANCE s set s.STATE= "
			+    "  case when  s.NUM >=1  then   s.NUM "
			+    "  when  s.NUM <=0  then  2  end "
			+    "  where s.ID = ?;";
		List<Object> args = new ArrayList<Object>();
		args.add(serviceId);
		try {
			ret_val = this.getJdbcTemplate().update(sql, args.toArray());
		}
		catch (Exception e) {
		}
		return ret_val;
	}

	@Override
	public TServiceInstanceBO searchServiceInstanceByInstanceInfoId(final int instanceInfoid) throws SCSException {
		log.debug("-----------------searchServiceInstanceByInstanceInfoId(int instanceInfoid)   begin!--------------");
		String sql ="select  distinct(s.ID), s.ORDER_ID, s.PRODUCT_ID, s.SERVICE_NAME, s.SERVICE_TYPE, s.SERVICE_DESC,"
			+ "  s.CREATE_DT, s.EXPIRY_DATE,s.STATE, s.NUM,FLAG, "
			+ "  s.PERIOD,  s.UNIT, s.PRICE, s.HISTORY_ID,s.HISTORY_STATE, "
			+ "  (select count(r2.PI_ID) from T_SCS_PRODUCT_INSTANCE_REF r2 where r2.SERVICE_INSTANCE_ID = s.ID ) SUM  "
			+ "  from T_SCS_SERVICE_INSTANCE s "
			+ "  join T_SCS_PRODUCT_INSTANCE_REF r on r.SERVICE_INSTANCE_ID = s.ID "
			+ "  where r.INSTANCE_INFO_ID = ?  and s.HISTORY_STATE = 0; ";

		BeanPropertyRowMapper<TServiceInstanceBO> orderRowMapper = new BeanPropertyRowMapper<TServiceInstanceBO>(TServiceInstanceBO.class);
		List<TServiceInstanceBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("instanceInfoid=" + instanceInfoid);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, instanceInfoid);
				}
			}, new RowMapperResultSetExtractor<TServiceInstanceBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.debug("searchInstanceInfoById error:" + e.getMessage());
			throw new SCSException(String.format(this.ERROR_MESSAGE_SERVICEINSTANCE_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchByOrderId(int orderId)   end!--------------");

		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}







}
