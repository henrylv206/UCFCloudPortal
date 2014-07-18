package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.order.dao.IOrderHistoryDao;
import com.skycloud.management.portal.front.order.entity.TOrderHistoryBO;

public class OrderHistoryDaoImpl extends SpringJDBCBaseDao implements IOrderHistoryDao {
	private static Log log = LogFactory.getLog(OrderHistoryDaoImpl.class);
	public static final String ERROR_MESSAGE_DAO_CREATE =  "创建历史订单失败。订单ID：%s，失败原因：%s";
	public static final String ERROR_MESSAGE_DAO_DEL=  "删除历史订单失败。历史订单ID：%s，失败原因：%s";
	public static final String ERROR_MESSAGE_DAO_UPDATE =  "修改历史订单失败。历史订单ID：%s，失败原因：%s";
	public static final String ERROR_MESSAGE_DAO_QUERY=  "查询历史订单失败。查询ID：%s，失败原因：%s";

	@Override
	public int save(final TOrderHistoryBO orderHistory) throws SQLException {
		int returnValue = -1;
		String sql = "INSERT INTO T_SCS_ORDER_HISTORY("+
			                 "INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,	ORDER_ID, CREATE_DT) VALUES "+
			                 "(?,?,?,?,?);";
		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;
	
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, orderHistory.getInstanceInfoId());
					ps.setInt(i++, orderHistory.getProductId());
					ps.setInt(i++, orderHistory.getTemplateId());
					ps.setInt(i++, orderHistory.getOrderId());
					ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				}
			});
		} catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_CREATE, orderHistory.getOrderId(),
					e.getMessage()));
			
		}
		return returnValue;
	}

	@Override
	public int delete(final int id) throws SQLException {
		int returnValue = -1;
		String sql = "DELETE FROM T_SCS_ORDER_HISTORY WHERE ID = ?;";
		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);
				}
			});
		} catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_DEL, id, e.getMessage()));
		}
		return returnValue;
	}

	@Override
	public int update(final TOrderHistoryBO orderHistory) throws SQLException {
		int returnValue = -1;
		String sql = "update T_SCS_ORDER_HISTORY set  INSTANCE_INFO_ID=?,PRODUCT_ID=?,TEMPLATE_ID=?,ORDER_ID=?, CREATE_DT=? WHERE PI_ID = ?;";

		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;
	
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, orderHistory.getInstanceInfoId());
					ps.setInt(i++, orderHistory.getProductId());
					ps.setInt(i++, orderHistory.getTemplateId());
					ps.setInt(i++, orderHistory.getOrderId());
					ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					//条件
					ps.setInt(i++, orderHistory.getId());
				}
			});
		} catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_UPDATE, orderHistory.getId(),
					e.getMessage()));
		}
		return returnValue;
	}

	@Override
	public TOrderHistoryBO searchById(final int id) throws SQLException {
		String sql = "select ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
        "from T_SCS_ORDER_HISTORY  where ID = ? ;";

		BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
		List<TOrderHistoryBO> returnList = null;
		try {
				returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);
					}
				}, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
		} catch (Exception e) {
				throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, id, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0)
				return returnList.get(0);
		else
				return null;
	}

	@Override
	public List<TOrderHistoryBO> searchAll() throws SQLException {
		
			String sql = "select PI_ID,PI_NAME,PI_CODE,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
	        "from T_SCS_ORDER_HISTORY ;";
	
			BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
			List<TOrderHistoryBO> returnList = null;
			try {
				returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
			} catch (Exception e) {
				throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, e.getMessage()));
			}
			if (returnList != null && returnList.size() > 0)
				return returnList;
			else
				return null;
	}

	@Override
	public List<TOrderHistoryBO> searchByOrderId(final int orderId)
			throws SQLException {
			String sql = "select ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
	        "from T_SCS_ORDER_HISTORY  where ORDER_ID = ? ;";
	
			BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
			List<TOrderHistoryBO> returnList = null;
			try {
				returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
					}
				}, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
			} catch (Exception e) {
				throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, orderId, e.getMessage()));
			}
			if (returnList != null && returnList.size() > 0)
				return returnList;
			else
				return null;
	}

	@Override
	public List<TOrderHistoryBO> searchByProductId(final int productId)
			throws SQLException {
			String sql = "select ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
	        "from T_SCS_ORDER_HISTORY  where PRODUCT_ID = ? ;";
	
			BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
			List<TOrderHistoryBO> returnList = null;
			try {
				returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, productId);
					}
				}, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
			} catch (Exception e) {
				throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, productId, e.getMessage()));
			}
			if (returnList != null && returnList.size() > 0)
				return returnList;
			else
				return null;
	}

	@Override
	public List<TOrderHistoryBO> searchByInstanceId(final int instanceId)
			throws SQLException {
		String sql = "select ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
        "from T_SCS_ORDER_HISTORY  where INSTANCE_INFO_ID = ? ;";

		BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
		List<TOrderHistoryBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, instanceId);
				}
			}, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, instanceId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0)
			return returnList;
		else
			return null;
	}

	@Override
	public List<TOrderHistoryBO> searchByTemplateId(final int templateId)
			throws SQLException {
		String sql = "select ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "+
        "from T_SCS_ORDER_HISTORY  where TEMPLATE_ID = ? ;";

		BeanPropertyRowMapper<TOrderHistoryBO> rowMapper = new BeanPropertyRowMapper<TOrderHistoryBO>(TOrderHistoryBO.class);
		List<TOrderHistoryBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, templateId);
				}
			}, new RowMapperResultSetExtractor<TOrderHistoryBO>(rowMapper));
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, templateId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0)
			return returnList;
		else
			return null;
	}

}
