package com.skycloud.management.portal.front.instance.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.ibm.wsdl.util.StringUtils;
import com.skycloud.management.portal.common.utils.CTime;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.instance.dao.IInstancePeriodManageDao;
import com.skycloud.management.portal.front.instance.entity.TInstancePeriodInfo;
import com.skycloud.management.portal.front.instance.entity.TServicePeriodInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;

public class InstancePeriodManageDaoImpl extends SpringJDBCBaseDao implements IInstancePeriodManageDao{
	private static Log log = LogFactory.getLog(InstancePeriodManageDaoImpl.class);
	private static Timestamp disabledStartDate = null;
	@Override
	public TInstancePeriodInfo findInstancePeriodById(int id) throws SQLException {
		BeanPropertyRowMapper<TInstancePeriodInfo> argTypes = new BeanPropertyRowMapper<TInstancePeriodInfo>(
				TInstancePeriodInfo.class);
		String sql = "SELECT info.ID,info.RESOURCE_INFO,info.EXPIRE_DATE,p.PERIOD,p.PRICE,p.UNIT from  T_SCS_INSTANCE_INFO info INNER JOIN T_SCS_PRODUCT p ON p.ID =info.PRODUCT_ID WHERE info.ID=?";
		Object[] args = new Object[] { id };
		List<TInstancePeriodInfo> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			throw new SQLException("query InstanceInfo error:" + e.getMessage());
		}
		if (result != null && result.size()>0) {
			return (TInstancePeriodInfo) result.get(0);
		}
		return null;
	}
	
	public TServicePeriodInfo findServiceInstancePeriodById(int id) throws SQLException {
		BeanPropertyRowMapper<TServicePeriodInfo> argTypes = new BeanPropertyRowMapper<TServicePeriodInfo>(TServicePeriodInfo.class);
		String sql = "SELECT info.ID, info.EXPIRY_DATE, info.PERIOD, info.UNIT, p.PRICE FROM T_SCS_SERVICE_INSTANCE info INNER JOIN T_SCS_PRODUCT p ON p.ID = info.PRODUCT_ID WHERE info.ID = ? ";
		Object[] args = new Object[] { id };
		List<TServicePeriodInfo> result = null;
		try {
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			throw new SQLException("query InstanceInfo error:" + e.getMessage());
		}
		if (result != null && result.size()>0) {
			return (TServicePeriodInfo) result.get(0);
		}
		return null;
	}	

	@Override
	public int updateInstancePeriod(final String resourceInfo,final Date expireDate,final int instanceId)
			throws SQLException {
		int index = 0;
		String sql = "UPDATE T_SCS_INSTANCE_INFO SET RESOURCE_INFO=? ,EXPIRE_DATE=? WHERE ID=? AND STATE NOT IN(4,7)";
		index=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {
			ps.setString(1, resourceInfo);
			ps.setTimestamp(2,new Timestamp(expireDate.getTime()));
			ps.setInt(3, instanceId);
		}
	});
		return index;
	}
	
	public int updateServicePeriod(final int periods, final Date expireDate, final int instanceId) throws SQLException {
		int index = 0;
		String sql = "UPDATE T_SCS_SERVICE_INSTANCE SET PERIOD=? ,EXPIRY_DATE=? WHERE ID=? AND STATE NOT IN(4,7)";
		index = this.getJdbcTemplate().update(sql,
				new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps)
							throws SQLException {
						ps.setInt(1, periods);
						ps.setTimestamp(2, new Timestamp(expireDate.getTime()));
						ps.setInt(3, instanceId);
					}
				});
		return index;
	}	

	@Override
	public List<TInstanceInfoBO> findExpireProduct() throws SQLException {
		
		BeanPropertyRowMapper<TInstanceInfoBO> argTypes = new BeanPropertyRowMapper<TInstanceInfoBO>(
				TInstanceInfoBO.class);
		String sql = "SELECT i.ID,i.ORDER_ID,i.TEMPLATE_ID,i.STATE,i.EXPIRE_DATE,i.TEMPLATE_TYPE,i.PRODUCT_ID,i.INSTANCE_NAME,i.RESOURCE_INFO, i.CPU_NUM," +
				"i.MEMORY_SIZE,i.STORAGE_SIZE,i.ZONE_ID,i.CLUSTER_ID,i.CREATE_DT,i.LASTUPDATE_DT,i.E_INSTANCE_ID,i.E_SERVICE_ID," +
				" i.E_DISK_ID,i.E_NETWORK_ID,i.E_OS_ID,i.OS_DESC,i.COMMENT,i.RES_CODE ,o.CREATOR_USER_ID USER_ID" +
				" FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_ORDER o ON i.ORDER_ID=o.ORDER_ID WHERE i.STATE NOT IN(1,4) AND EXPIRE_DATE <= ?";
		log.info("-----------------disabledStartDate:"+disabledStartDate);
		if(null!=disabledStartDate){
			sql += " AND EXPIRE_DATE > ?";
		}
	    Date currDate=new Date(System.currentTimeMillis());	    
		Object[] args = null;
		if(null!=disabledStartDate){
			args = new Object[] {new Timestamp(currDate.getTime()),disabledStartDate};			
		}
		else args = new Object[] {new Timestamp(currDate.getTime())};
		log.info("---------------currDate:"+new Timestamp(currDate.getTime()));
		log.info("---------------sql:"+sql);
		List<TInstanceInfoBO> result = null;
		try {
			
			result = this.getJdbcTemplate().query(sql, args, argTypes);
		} catch (Exception e) {
			throw new SQLException("query InstanceInfo error:" + e.getMessage());
		}
		disabledStartDate = new Timestamp(currDate.getTime());
		return result;
	}

	@Override
	public int updateMessageState(final int instanceId) throws SQLException {
		int index = 0;
		String sql = "update T_SCS_MESSAGE m inner join " +
				" (select i.INSTANCE_NAME,u.EMAIL from T_SCS_INSTANCE_INFO i inner join T_SCS_ORDER o on i.ORDER_ID=o.ORDER_ID " +
				" inner join T_SCS_USER u on u.ID =o.CREATOR_USER_ID " +
				" where i.ID=?)" +
				" t  on m.FROMS=t.EMAIL and m.SERVICE=t.INSTANCE_NAME SET m.`STATUS`=3";
		index=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {
			ps.setInt(1, instanceId);
		}
	   });
		return index;
	}

	@Override
	public int updateInstance4OrderPeriod(final String resourceInfo,final Date expireDate,
			final int orderId) throws SQLException {
		StringBuffer sql = new StringBuffer();
		int index=0;
		sql.append("UPDATE T_SCS_INSTANCE_INFO SET RESOURCE_INFO=? ,EXPIRE_DATE=? WHERE ORDER_ID=? AND STATE NOT IN(4,7)");
		index=this.getJdbcTemplate().update(sql.toString(), new PreparedStatementSetter() {
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, resourceInfo);
				ps.setTimestamp(2,new Timestamp(expireDate.getTime()));
				ps.setInt(3, orderId);
			}
		});
		return index;
	}
}
