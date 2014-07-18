package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.sysmanage.dao.IUserVlanDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;


public class UserVlanDaoImpl extends SpringJDBCBaseDao implements IUserVlanDao {
	public static final String ERROR_SAVE = "创建用户vlan失败。用户ID：%s，申请时间：%s，失败原因：%s";
	public static final String ERROR_MODIFY = "修改用户vlan失败。用户ID：%s，修改时间：%s，失败原因：%s";
	public static final String ERROR_QUERY = "查询用户vlan失败。失败原因：%s";
	public static final String ERROR_DELETE = "删除用户vlan失败。ID：%s，失败原因：%s";
	public static final String ERROR_DELETE_VLAN_ID = "删除用户vlan失败。VLAN_ID：%s，失败原因：%s";


	private PreparedStatementCreator PreparedSaveSQLArgs(final TUserVlanBO userVlan,final String sql) {
		return    new PreparedStatementCreator() {
		        @Override
		        public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
		          PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
		          ps.setInt(1,userVlan.getUserId());
		          ps.setLong(2,userVlan.getVlanId());
		          ps.setInt(3,userVlan.getState());
		          ps.setInt(4,userVlan.getType());
		          ps.setInt(5,userVlan.getZoneId());
		          ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
		          ps.setTimestamp(7, new Timestamp(System.currentTimeMillis()));
		          ps.setInt(8,userVlan.getResourcePoolsId());
		          return ps;
		        }
		      };
	  }

	@Override
	public int save(TUserVlanBO userVlan) throws SCSException {
		int ret_val = -1;
	    KeyHolder keyHolder = new GeneratedKeyHolder();
	    String sql = "INSERT INTO T_SCS_USER_VLAN(USER_ID,VLAN_ID,STATE,TYPE,ZONE_ID,CREATE_DT,MODIFY_DT,RESOURCE_POOLS_ID)"
	        + " VALUES (?,?,?,?,?,?,?,?);";
	    try {
	      PreparedStatementCreator preCreator = PreparedSaveSQLArgs(userVlan,sql);
	      jdbcTemplate.update(preCreator,keyHolder);
	      ret_val = keyHolder.getKey().intValue();
	    } catch (Exception e) {
	    	e.printStackTrace();
	      throw new SCSException(String.format(this.ERROR_SAVE, userVlan.getUserId(), userVlan.getCreateDt(),e.getMessage()));
	    }
	    return ret_val;
	}

	@Override
	public int update(TUserVlanBO userVlan) throws SCSException {
		int ret_val=0;
		String sql = "UPDATE T_SCS_USER_VLAN SET USER_ID=?,VLAN_ID=?,STATE=?, TYPE=?, ZONE_ID=?,MODIFY_DT=? WHERE ID=? ";
	    try {
	      ret_val = jdbcTemplate.update(sql,new Object[]{userVlan.getUserId(),userVlan.getVlanId(),userVlan.getState(),userVlan.getType(), userVlan.getZoneId(), new Timestamp(System.currentTimeMillis()),userVlan.getId()});
	    } catch (Exception e) {
	      throw new SCSException(String.format(this.ERROR_MODIFY,userVlan.getUserId(),  new Timestamp(System.currentTimeMillis()),e.getMessage()));
	    }
	    return ret_val;
	}

	@Override
	public int delete(int id) throws SCSException {
		int ret_val=0;
		String sql = "DELETE  FROM T_SCS_USER_VLAN  WHERE ID=?  ";
	    try {
	      ret_val = jdbcTemplate.update(sql,new Object[]{id});
	    } catch (Exception e) {
	      throw new SCSException(String.format(this.ERROR_DELETE,id,e.getMessage()));
	    }
	    return ret_val;
	}


	@Override
    public int deleteUserVlanByVlanId(long vlanId) throws SCSException {
		int ret_val=0;
		String sql = "DELETE  FROM T_SCS_USER_VLAN  WHERE VLAN_ID=?  ";
	    try {
	      ret_val = jdbcTemplate.update(sql,new Object[]{vlanId});
	    } catch (Exception e) {
	      throw new SCSException(String.format(this.ERROR_DELETE_VLAN_ID,vlanId,e.getMessage()));
	    }
	    return ret_val;
    }

	@Override
	public List<TUserVlanBO> findAll(final TUserVlanBO userV) throws SCSException {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT v.ID, v.USER_ID,v.VLAN_ID, v.TYPE, v.STATE, v.ZONE_ID, v.CREATE_DT, v.MODIFY_DT,u.ACCOUNT, v.RESOURCE_POOLS_ID FROM T_SCS_USER_VLAN v,T_SCS_USER u  ");
		sql.append(" where u.ID = v.USER_ID and v.STATE<>3 " ) ;

		if(userV!=null ){
			if(userV.getAccount()!=null && !"".equals(userV.getAccount())) {
				sql.append("  and u.ACCOUNT like ? ");
		    }
			if(userV.getVlanId()>0) {
				sql.append("  and v.VLAN_ID = ? ");
		    }
			if(userV.getResourcePoolsId()>0){
				sql.append("  and v.RESOURCE_POOLS_ID = ? ");
			}
	    }
		sql.append(" order by v.USER_ID,v.ID desc; ");
        BeanPropertyRowMapper<TUserVlanBO> rowMapper = new BeanPropertyRowMapper<TUserVlanBO>(TUserVlanBO.class);
        List<TUserVlanBO> returnList = null;
        try {
            returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
    			@Override
                public void setValues(PreparedStatement ps) throws SQLException {

    				if(userV!=null){
    					int i=1;
    					if( userV.getAccount()!=null &&  !"".equals(userV.getAccount())) {
    						ps.setString(i++, "%"+userV.getAccount()+"%");
    					}
    					if(userV.getVlanId()>0) {
    						ps.setLong(i++,userV.getVlanId());
    				    }
    					if(userV.getResourcePoolsId()>0){
    						ps.setInt(i++, userV.getResourcePoolsId());
    					}
    			    }

    			}
    		}, new RowMapperResultSetExtractor<TUserVlanBO>(rowMapper));
        } catch (Exception e) {
            throw new SCSException(String.format(this.ERROR_QUERY, e.getMessage()));
        }
//        if (returnList != null && returnList.size() > 0) {
//	        return returnList;
//        } else {
//	        return null;
//        }
        return returnList;
	}

	@Override
	public TUserVlanBO findById(final int id) throws SCSException {
		String sql = "SELECT v.ID, v.USER_ID,v.VLAN_ID, v.TYPE, v.STATE, v.ZONE_ID, v.CREATE_DT, v.MODIFY_DT,u.ACCOUNT,c.COMP_CN_NAME companyName, v.RESOURCE_POOLS_ID FROM T_SCS_USER_VLAN v  "+
		", T_SCS_USER u left join T_SCS_COMPANY_INFO c on u.COMP_ID=c.COMP_ID"+
    " where u.ID = v.USER_ID and v.ID = ?;";

		BeanPropertyRowMapper<TUserVlanBO> rowMapper = new BeanPropertyRowMapper<TUserVlanBO>(TUserVlanBO.class);
		List<TUserVlanBO> returnList = null;
		try {
		returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
			@Override
            public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		}, new RowMapperResultSetExtractor<TUserVlanBO>(rowMapper));
		} catch (Exception e) {
			throw new SCSException(String.format(this.ERROR_QUERY, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
    public List<TUserVlanBO> findUserVlan(final TUserVlanBO userVlan) throws SCSException {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT  ID, USER_ID,VLAN_ID, TYPE, ZONE_ID, CREATE_DT, MODIFY_DT, RESOURCE_POOLS_ID  from T_SCS_USER_VLAN  where STATE=1 ");
		if(userVlan.getId()!=0) {
	        sql.append("  and ID = ?");
        }
		if(userVlan.getUserId()!=0) {
	        sql.append("  and USER_ID = ?");
        }
		if(userVlan.getVlanId()!=0) {
	        sql.append("  and VLAN_ID = ?");
        }
		if(userVlan.getType()!=0) {
	        sql.append("  and TYPE = ?");
        }
		if(userVlan.getZoneId()!=0) {
	        sql.append("  and ZONE_ID = ?");
        }
		if(userVlan.getResourcePoolsId()!=0) {
	        sql.append("  and RESOURCE_POOLS_ID = ?");
        }
		sql.append(" order by ID desc; ");

		BeanPropertyRowMapper<TUserVlanBO> rowMapper = new BeanPropertyRowMapper<TUserVlanBO>(TUserVlanBO.class);
		List<TUserVlanBO> returnList = null;
		try {
		returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				if(userVlan.getId()!=0) {
					ps.setInt(i++, userVlan.getId());
		        }
				if(userVlan.getUserId()!=0) {
					ps.setInt(i++, userVlan.getUserId());
		        }
				if(userVlan.getVlanId()!=0) {
					ps.setLong(i++, userVlan.getVlanId());
		        }
				if(userVlan.getType()!=0) {
					ps.setInt(i++, userVlan.getType());
		        }
				if(userVlan.getZoneId()!=0) {
					ps.setInt(i++, userVlan.getZoneId());
		        }
				if(userVlan.getResourcePoolsId()!=0) {
					ps.setInt(i++, userVlan.getResourcePoolsId());
		        }
			}
		}, new RowMapperResultSetExtractor<TUserVlanBO>(rowMapper));
		} catch (Exception e) {
			throw new SCSException(String.format(this.ERROR_QUERY, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
    }

    @Override
    public List<TUserVlanBO> findUserZone(final TUserVlanBO userVlan) throws SCSException {
		final StringBuffer sql = new StringBuffer();
		sql.append("SELECT DISTINCT ZONE_ID from T_SCS_USER_VLAN  where STATE <>3 ");
		if(userVlan.getUserId()!=0) {
	        sql.append("  and USER_ID = ?");
        }
		sql.append(" order by ID desc; ");

		BeanPropertyRowMapper<TUserVlanBO> rowMapper = new BeanPropertyRowMapper<TUserVlanBO>(TUserVlanBO.class);
		List<TUserVlanBO> returnList = null;
		try {
		returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
            @Override
            public void setValues(PreparedStatement ps) throws SQLException {
				int i = 1;
				if(userVlan.getUserId()!=0) {
					ps.setInt(i++, userVlan.getUserId());
		        }
			}
		}, new RowMapperResultSetExtractor<TUserVlanBO>(rowMapper));
		} catch (Exception e) {
			throw new SCSException(String.format(this.ERROR_QUERY, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
    }


}
