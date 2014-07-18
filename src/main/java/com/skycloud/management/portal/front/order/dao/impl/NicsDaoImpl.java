package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.admin.audit.entity.TNicsBO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.order.dao.INicsDao;

public class NicsDaoImpl extends SpringJDBCBaseDao implements INicsDao {

	private static Log log = LogFactory.getLog(NicsDaoImpl.class);

	@Override
	public int save(final TNicsBO nics) throws SQLException {
		log.debug("-----------------save  Nics  begin!--------------");
		int returnValue = -1;
		String sql = "INSERT INTO T_SCS_NICS(" + "VM_INSTANCE_INFO_ID,E_VLAN_ID,IP,STATE) VALUES " + "(?,?,?,?);";
		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, nics.getVmInstanceInfoId());
					ps.setLong(i++, nics.geteVlanId());
					ps.setString(i++, nics.getIp());
					ps.setInt(i++, nics.getState());
				}
			});
		}
		catch (Exception e) {
			log.error("save  Nics  error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_CREATE, nics.getVmInstanceInfoId(), nics.getIp(),
			                                     e.getMessage()));

		}
		log.debug("-----------------save  Nics  end!--------------");
		return returnValue;
	}

	@Override
	public int delete(final int nicsId) throws SQLException {
		log.debug("-----------------delete  Nics  begin!--------------");
		int returnValue = -1;
		String sql = "DELETE FROM T_SCS_NICS WHERE ID = ?;";
		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, nicsId);
				}
			});
		}
		catch (Exception e) {
			log.error("delete  Nics  error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_DELETE, nicsId, e.getMessage()));
		}
		log.debug("-----------------delete  Nics  end!--------------");
		return returnValue;
	}

	@Override
	public int deleteNicsByInfoId(final int infoId) throws SQLException {
		log.debug("-----------------delete  Nics by infoId  begin!--------------");
		int returnValue = -1;
		String sql = "DELETE FROM T_SCS_NICS WHERE VM_INSTANCE_INFO_ID = ?;";
		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, infoId);
				}
			});
		}
		catch (Exception e) {
			log.error("delete Nics by infoId  error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_DELETE, infoId, e.getMessage()));
		}
		log.debug("-----------------delete  Nics by infoId end!--------------");
		return returnValue;
	}

	@Override
	public int update(final TNicsBO nics) throws SQLException {
		log.debug("-----------------update  Nics  begin!--------------");
		int returnValue = -1;
		String sql = "update T_SCS_NICS set VM_INSTANCE_INFO_ID=?,E_VLAN_ID=?,IP=?,STATE=? WHERE ID = ?;";

		try {
			log.debug("sql=" + sql.toString());
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(i++, nics.getVmInstanceInfoId());
					ps.setLong(i++, nics.geteVlanId());
					ps.setString(i++, nics.getIp());
					ps.setInt(i++, nics.getState());
					// 条件
					ps.setInt(i++, nics.getId());
				}
			});
		}
		catch (Exception e) {
			log.error("update  Nics error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_UPDATE, nics.getVmInstanceInfoId(), nics.getIp(),
			                                     e.getMessage()));
		}
		log.debug("-----------------update  Nics  end!--------------");
		return returnValue;
	}

	@Override
	public TNicsBO searchNicsById(final int nicsId) throws SQLException {
		log.debug("-----------------searchNicsById(int nicsId)   begin!--------------");
		String sql = "select ID,VM_INSTANCE_INFO_ID,E_VLAN_ID,IP,STATE from T_SCS_NICS " + "where ID = ? ;";

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			log.debug("nicsId=" + nicsId);
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, nicsId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.debug("searchNicsById error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchNicsById(int nicsId)   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<TNicsBO> searchAllNics() throws SQLException {
		log.debug("-----------------searchAllNics()   begin!--------------");
		String sql = "select ID,VM_INSTANCE_INFO_ID,E_VLAN_ID,IP,STATE from T_SCS_NICS;";
		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchAllNics error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchAllNics()   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public int searchLastId() throws SQLException {
		log.debug("-----------------searchLastId()   begin!--------------");
		int returnId = -1;
		String sql = "SELECT ID FROM T_SCS_ORDER ORDER BY ORDER_ID DESC LIMIT 0,1";
		try {
			log.debug("sql=" + sql);
			returnId = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			log.error("searchLastId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchLastId()   end!--------------");
		return returnId;
	}

	@Override
	public List<TNicsBO> searchNicssByInstanceId(final int instanceId) throws SQLException {
		log.debug("-----------------searchNicssByInstanceId( int instanceId)   begin!--------------");

		String sql = "select ID,VM_INSTANCE_INFO_ID,E_VLAN_ID,IP,STATE from T_SCS_NICS " + "where VM_INSTANCE_INFO_ID = ?";

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, instanceId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicssByInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchNicssByInstanceId( int instanceId)   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.front.order.dao.INicsDao#modifyNics(com.
	 * skycloud.management.portal.admin.audit.entity.TNicsBO)
	 */
	@Override
	public int modifyNics(TNicsBO nics) throws SCSException {
		String sql = "UPDATE T_SCS_NICS SET E_VLAN_ID=?,IP=?,STATE=? WHERE VM_INSTANCE_INFO_ID = ?;";
		int index = 0;
		List<Object> args = new ArrayList<Object>();
		// args.add(nics.getVlan());//error:nics.getVlan() value is null
		args.add(nics.geteVlanId());
		args.add(nics.getIp());
		args.add(nics.getState());
		args.add(nics.getVmInstanceInfoId());
		log.info("@@@@@@@@ sql=" + sql + "," + args);
		try {
			index = this.getJdbcTemplate().update(sql.toString(), args.toArray());
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_NICS_ERROR, SCSErrorCode.DB_SQL_UPDATE_NICS_DESC);
		}
		return index;
	}

	@Override
	public List<TNicsBO> searchNicssByUserId(final int userId) throws SQLException {
		log.debug("-----------------searchNicssByUserId( int userId)   begin!--------------");

		String sql = "select n.ID, n.VM_INSTANCE_INFO_ID, n.E_VLAN_ID, n.IP, n.STATE " + " from T_SCS_NICS AS n "
		        + "  left JOIN T_SCS_INSTANCE_INFO AS i  " + "  ON n.VM_INSTANCE_INFO_ID = i.ID  " + " left JOIN T_SCS_ORDER AS o  "
		        + " ON o.ORDER_ID = i.ORDER_ID  " + " where n.E_VLAN_ID>0 AND o.CREATOR_USER_ID = ?  " + " group by n.E_VLAN_ID "
		        + " order by n.ID desc ";
		// System.out.println("sql="+sql);
		// System.out.println("userId="+userId);

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, userId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicssByInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		log.debug("-----------------searchNicssByUserId( int  userId)   end!--------------");
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public int searchVlanDefaultCount(final int zoneId) throws SQLException {
		int count = 0;
		// to fix bug:2622
		//to fix bug:3698 3705
		String sql = "select count(distinct n.e_vlan_id) B from  T_SCS_NICS n"
		        + "	 left join T_SCS_INSTANCE_INFO i   on  n.VM_INSTANCE_INFO_ID = i.ID  "
		        + "	 where i.STATE not in (4,7)  and  n.e_vlan_id<>0  and i.ZONE_ID=" + zoneId
		        + " and n.ID in (SELECT min(n2.ID)  FROM  T_SCS_NICS n2 group by n2.vm_instance_info_id)";

		try {
			log.debug("sql=" + sql.toString());
			count = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			System.out.println("------------------------------------");
			e.printStackTrace();
			log.error("searchNicssByInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		return count;
	}

	@Override
	public int searchVlanCount(final int zoneId) throws SQLException {
		int count = 0;
		// to fix bug:2622
		String sql = "select count(distinct n.e_vlan_id) D from T_SCS_NICS n"
		        + "	 left join T_SCS_INSTANCE_INFO i   on  n.VM_INSTANCE_INFO_ID = i.ID  "
		        + "	 where i.STATE not in(4,7) and n.e_vlan_id<>0 and i.ZONE_ID=" + zoneId;

		try {
			log.debug("sql=" + sql.toString());
			count = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			log.error("searchNicssByInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		return count;
	}

	@Override
	public int searchVlanOtherCount(final int zoneId) throws SQLException {
		int count = 0;
		// to fix bug:2622
		String sql = "select count(distinct n.e_vlan_id) B from  T_SCS_NICS n"
		        + "	 left join T_SCS_INSTANCE_INFO i   on  n.VM_INSTANCE_INFO_ID = i.ID  "
		        + "	 where i.STATE not in (4,7)  and  n.e_vlan_id<>0  and i.ZONE_ID=" + zoneId
		        + " and n.ID not in (SELECT min(n2.ID)  FROM  T_SCS_NICS n2 group by n2.vm_instance_info_id)";

		try {
			log.debug("sql=" + sql.toString());
			count = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			System.out.println("------------------------------------");
			e.printStackTrace();
			log.error("searchNicssByInstanceId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		return count;
	}

	@Override
    public int searchNicssCountByvlanId(int vlanId) throws SQLException {
		int count = 0;
		//fix bug:7637(解除vlan与用户绑定关系时,判断该vlan是否被改用户使用过)
		String sql = "select count(n.e_vlan_id) D from T_SCS_NICS n " +
		       	" left join T_SCS_INSTANCE_INFO i   on  n.VM_INSTANCE_INFO_ID = i.ID " + 
		        " left join T_SCS_USER_VLAN v on v.VLAN_ID = n.E_VLAN_ID " +
				" left join T_SCS_ORDER o on o.ORDER_ID = i.ORDER_ID "+
		        " where o.CREATOR_USER_ID=v.USER_ID and " +
		        " i.STATE not in(4,7) and n.e_vlan_id=" + vlanId;
		try {
			log.debug("sql=" + sql.toString());
			count = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			log.error("searchNicssCountByvlanId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		return count;
    }

	@Override
    public int searchNicssCountByVlanId4UserId(int vlanId, int userId) throws SQLException {
		int count = 0;
		String sql = "select count(n.e_vlan_id) D from T_SCS_NICS n"
		        + "	 left join T_SCS_INSTANCE_INFO i   on  n.VM_INSTANCE_INFO_ID = i.ID  "
		        + "	 left join T_SCS_ORDER o   on  o.ORDER_ID = i.ORDER_ID  "
		        + "	 where i.STATE not in(4,7) and n.e_vlan_id=" + vlanId
				+ "	 and o.CREATOR_USER_ID=" + userId;
		try {
			log.debug("sql=" + sql.toString());
			count = this.getJdbcTemplate().queryForInt(sql);
		}
		catch (Exception e) {
			log.error("searchNicssCountByvlanId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		return count;
    }

	@Override
    public List<TNicsBO> searchNicssByNetworkId(final int networkId) throws SQLException {
		log.debug("-----------------searchNicssByNetworkId( int networkId)   begin!--------------");

		String sql = "select n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE from T_SCS_NICS n "
				+ " left join T_SCS_INSTANCE_INFO i on i.ID = n.VM_INSTANCE_INFO_ID "
				+ " where  i.STATE not in (4,7)  "
				+ " and n.E_VLAN_ID = ? ";

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, networkId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicssByNetworkId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		    log.debug("-----------------searchNicssByNetworkId( int networkId)   end!--------------");
			return returnList;
    }

	@Override
    public List<TNicsBO> searchNicssByNetworkIdAndIp(final int networkId,final String ipaddress) throws SQLException {
		log.debug("-----------------searchNicssByNetworkIdAndIp( int networkId,String ipaddress)   begin!--------------");

		String sql = "select n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE from T_SCS_NICS n "
			+ " left join T_SCS_INSTANCE_INFO i on i.ID = n.VM_INSTANCE_INFO_ID "
			+ " where  i.STATE not in (4,7)  "
    		+ " and n.E_VLAN_ID = ?  and  n.IP = ?";

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, networkId);
					ps.setString(2, ipaddress);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicssByNetworkId error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		    log.debug("-----------------searchNicssByNetworkIdAndIp(  int networkId,String ipaddress)   end!--------------");
			return returnList;
    }


	@Override
    public List<TNicsBO> searchNicsDhcpByOrderId(final int orderId) throws SQLException {
		log.debug("-----------------searchNicsDhcpByOrderId( int orderId)   begin!--------------");
		//to fix bug:7477(增加条件:or n.IP is null or n.IP = '0')
		String sql = "select n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE from T_SCS_NICS n "
			+ " left join T_SCS_INSTANCE_INFO i on i.ID = n.VM_INSTANCE_INFO_ID "
			+ " where (n.E_VLAN_ID = 0 or n.IP is null or n.IP = '0') "
			+ " and i.STATE not in (4,7) and i.TEMPLATE_TYPE <>3 and i.ORDER_ID = ?";

		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicsDhcpByOrderId( int orderId) error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
		    log.debug("-----------------searchNicsDhcpByOrderId( int orderId)   end!--------------");
			return returnList;
    }

	@Override
    public List<TNicsBO> searchNicsRepeatVlanCountByorderId(final int orderId) throws SQLException {
		String sql = "select  n3.* " +
				"from ( " +
				"  select n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE " +
				" 		,(select count(n2.E_VLAN_ID) from T_SCS_NICS n2 where n2.E_VLAN_ID=n.E_VLAN_ID and n2.vm_instance_info_id = n.vm_instance_info_id) vlanCount " +
				"  from T_SCS_NICS n " +
				"  join T_SCS_INSTANCE_INFO i on n.VM_INSTANCE_INFO_ID = i.ID " +
				"  where n.E_VLAN_ID>0 and i.ORDER_ID = ? " +
				")n3 " +
				"order by n3.vlanCount desc; ";
		BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
		List<TNicsBO> returnList = null;
		try {
			log.debug("sql=" + sql.toString());
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
		}
		catch (Exception e) {
			log.error("searchNicsDhcpByOrderId( int orderId) error:" + e.getMessage());
			throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
		}
			return returnList;
    }

	@Override
    public List<TNicsBO> searchNicsRepeatIP() throws SQLException {
		String sql ="select  ni.ipCount,ni.ID,ni.VM_INSTANCE_INFO_ID,ni.E_VLAN_ID,ni.IP,ni.STATE from (" +
				"    select count(n.ip) ipCount,n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE " +
				"              ,i.order_id " +
				"    from T_SCS_NICS n,T_SCS_INSTANCE_INFO i " +
				"    where n.VM_INSTANCE_INFO_ID=i.ID and i.STATE not in(4,7) " +
				"    group by n.ip order by ipCount desc " +
				")  ni " +
				" where ni.ipCount>=2 " +
				" order by ni.ipCount desc ";
				BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
				List<TNicsBO> returnList = new ArrayList<TNicsBO>();
				try {
					log.debug("sql=" + sql.toString());
					returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
				}
				catch (Exception e) {
					log.error("searchNicsRepeatIPCount() error:" + e.getMessage());
					throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
				}
	       return returnList;
    }

	@Override
    public List<TNicsBO> searchNicsByorderId(final int orderId) throws SQLException {
		String sql = " select n.ID,n.VM_INSTANCE_INFO_ID,n.E_VLAN_ID,n.IP,n.STATE " +
				"    from T_SCS_NICS n,T_SCS_INSTANCE_INFO i " +
				"    where n.VM_INSTANCE_INFO_ID=i.ID " +
				"    and  i.order_id = ? " +
				"    order by n.ID ";
				BeanPropertyRowMapper<TNicsBO> orderRowMapper = new BeanPropertyRowMapper<TNicsBO>(TNicsBO.class);
				List<TNicsBO> returnList = new ArrayList<TNicsBO>();
				try {
					log.debug("sql=" + sql.toString());
					returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
						@Override
						public void setValues(PreparedStatement ps) throws SQLException {
							ps.setInt(1, orderId);
						}
					}, new RowMapperResultSetExtractor<TNicsBO>(orderRowMapper));
				}
				catch (Exception e) {
					log.error("searchNicsByorderId( int orderId) error:" + e.getMessage());
					throw new SQLException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_NICS_DAO_QUERY, e.getMessage()));
				}
	       return returnList;
    }
	
	

}
