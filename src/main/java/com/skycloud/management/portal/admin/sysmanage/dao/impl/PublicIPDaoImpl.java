package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.dao.IPublicIPDao;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;

public class PublicIPDaoImpl implements IPublicIPDao {
	private JdbcTemplate jdbcTemplate;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	
	private PreparedStatementCreator PreparedSaveSQLArgs(final TPublicIPBO publicIP,final String sql) {
		return    new PreparedStatementCreator() {
		        @Override
		        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		          PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		          ps.setInt(1,publicIP.getId());
		          ps.setString(2,publicIP.getIpAddress());
		          ps.setInt(3,publicIP.getStatus());
		          ps.setInt(4,publicIP.getIpType());
		          ps.setLong(5,publicIP.getServiceProvider());
		          ps.setLong(6,publicIP.getCreatorUserId());
		          ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
		          ps.setTimestamp(8, new Timestamp(System.currentTimeMillis()));
		          ps.setInt(9,publicIP.getInstanceInfoId());
		          ps.setInt(10, publicIP.getBandwidthId());
              ps.setString(11, publicIP.getPrivateIp());
		          return ps;
		        }        
		      };
	  }

	@Override
	public int save(TPublicIPBO publicIP) throws SCSException {
		int ret_val = -1;
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    String sql = "INSERT INTO T_SCS_PUBLIC_IP(ID,IP_ADDRESS,STATUS,IP_TYPE,SERVICE_PROVIDER,CREATOR_USER_ID,CREATED_DATE,LASTUPDATE_DATE,INSTANCE_INFO_ID)"
	        + " VALUES (?,?,?,?,?,?,?,?,?);";
	    try {
	      PreparedStatementCreator preCreator = PreparedSaveSQLArgs(publicIP,sql);
	      jdbcTemplate.update(preCreator,keyHolder);
	      ret_val = keyHolder.getKey().intValue();
	    } catch (Exception e) {
	      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE, publicIP.getCreatorUserId(),
	    		  publicIP.getCreatedDate()));
	    }
	    return ret_val;
	}



	@Override
	public List<TPublicIPBO> listPublicIPs(int curPage, int pageSize,String searchKey) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT `ID` id, `IP_ADDRESS` ipAddress, `STATUS` status, `IP_TYPE` ipType, `INSTANCE_INFO_ID` instanceInfoId, `SERVICE_PROVIDER` serviceProvider, `CREATOR_USER_ID` creatorUserId, `CREATED_DATE` createdDate, `LASTUPDATE_DATE` lastupdateDate, `BANDWIDTH_ID` bandwidthId, `PRIVATEIP` privateIp FROM `T_SCS_PUBLIC_IP`  where 1 = 1 ");
		if(searchKey!=null && searchKey!="" && searchKey.trim()!="") {
			sql.append(" and IP_ADDRESS LIKE ? ");	
		    param.add("%"+searchKey.trim()+"%");
		}
		sql.append(" limit ?,?");
		param.add((curPage - 1) * pageSize);
		param.add(pageSize);
		return jdbcTemplate.query(sql.toString(),param.toArray(), new TemplateRowMapper());
	}

	@Override
	public int countPublicIPs(String searchKey) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT count(*) FROM `T_SCS_PUBLIC_IP` where 1 = 1 " ) ;
		if(searchKey!=null && searchKey!="" && searchKey.trim()!="") {
			sql.append(" and IP_ADDRESS LIKE ? ");	
		    param.add("%"+searchKey.trim()+"%");
		}
		return jdbcTemplate.queryForInt(sql.toString(), param.toArray());
	}

	@Override
	public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider) {
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer();
		sql.append("SELECT `ID` id, `IP_ADDRESS` ipAddress, `STATUS` status, `IP_TYPE` ipType, `INSTANCE_INFO_ID` instanceInfoId, `SERVICE_PROVIDER` serviceProvider, `CREATOR_USER_ID` creatorUserId, `CREATED_DATE` createdDate, `LASTUPDATE_DATE` lastupdateDate,`BANDWIDTH_ID` bandwidthId ,`PRIVATEIP` privateIp FROM `T_SCS_PUBLIC_IP` where STATUS = 0 ");
		if (serviceProvider!=-1){
			sql.append(" AND `SERVICE_PROVIDER`= ? ");	
		    param.add(""+serviceProvider+"");
		}
		return jdbcTemplate.query(sql.toString(),param.toArray(), new TemplateRowMapper());
	}

	

	private class TemplateRowMapper implements RowMapper<TPublicIPBO> {
		@Override
		public TPublicIPBO mapRow(ResultSet rs, int rowNum) throws SQLException {
			TPublicIPBO template = new TPublicIPBO();
			template.setId(rs.getInt("id"));
			template.setIpAddress(rs.getString("ipAddress"));
			template.setServiceProvider(rs.getInt("serviceProvider"));
			template.setCreatorUserId(rs.getInt("creatorUserId"));
			template.setCreatedDate(rs.getTimestamp("createdDate"));
			template.setInstanceInfoId(rs.getInt("instanceInfoId"));
			template.setLastupdateDate(rs.getTimestamp("lastupdateDate"));
			template.setStatus(rs.getInt("status"));
			template.setIpType(rs.getInt("ipType"));
			template.setBandwidthId(rs.getInt("bandwidthId"));
			template.setPrivateIp(rs.getString("privateIp"));
			return template;
		}
	}

	@Override
	public int searchIPByIpAddress(String ipAddress) throws SCSException {
		StringBuffer sql = new StringBuffer();
		sql.append( "SELECT count(*) FROM `T_SCS_PUBLIC_IP` where IP_ADDRESS=? " ) ;
		return jdbcTemplate.queryForInt(sql.toString(), new Object[]{ipAddress});
	}

	@Override
	public int update(TPublicIPBO publicIP) throws SCSException {
		int ret_val=0;
		String sql = "UPDATE T_SCS_PUBLIC_IP SET IP_TYPE=?,SERVICE_PROVIDER=?,LASTUPDATE_DATE=?, BANDWIDTH_ID=?, PRIVATEIP=? WHERE ID=? ";
	    try {
	      ret_val = jdbcTemplate.update(sql,new Object[]{publicIP.getIpType(),publicIP.getServiceProvider(),publicIP.getLastupdateDate(),publicIP.getBandwidthId(),publicIP.getPrivateIp(),publicIP.getId()});
	    } catch (Exception e) {
	      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE,publicIP.getId(),
	    		  publicIP.getLastupdateDate()));
	    }
	    return ret_val;
	}


	@Override
	public int delete(int publicIPid) throws SCSException {
		int ret_val=0;
		String sql = "DELETE  FROM T_SCS_PUBLIC_IP  WHERE ID=?  AND `STATUS`=0";
	    try {
	      ret_val = jdbcTemplate.update(sql,new Object[]{publicIPid});
	    } catch (Exception e) {
	      throw new SCSException(String.format(ConstDef.ERROR_MESSAGE_PORTAL_ORDER_DAO_CREATE,publicIPid));
	    }
	    return ret_val;
	}
	

	@Override
	public TPublicIPBO queryPublicIPById(int publicIPId) throws SCSException {
		String sql="SELECT `ID` id, `IP_ADDRESS` ipAddress, `STATUS` status, `IP_TYPE` ipType, `INSTANCE_INFO_ID` instanceInfoId, `SERVICE_PROVIDER` serviceProvider, `CREATOR_USER_ID` creatorUserId, `CREATED_DATE` createdDate, `LASTUPDATE_DATE` lastupdateDate, `BANDWIDTH_ID` bandwidthId, `PRIVATEIP` privateIp FROM `T_SCS_PUBLIC_IP`  WHERE ID=? ";
		return jdbcTemplate.query(sql, new Object[]{publicIPId},new TemplateRowMapper()).get(0);
	}

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

  @Override
  public String getPrivateIpByPublicIp(String publicIp) {
    String sql = "SELECT privateIp FROM T_SCS_PUBLIC_IP WHERE IP_ADDRESS = ?";
    return jdbcTemplate.queryForObject(sql, String.class, publicIp);
  }

  @Override
  public String getOperatorByPublicIp(String publicIp) {
    String sql = "SELECT operators FROM T_SCS_PUBLIC_IP WHERE ip_address = ?";
    return jdbcTemplate.queryForObject(sql, String.class, publicIp);
  }
}
