package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.sysmanage.dao.IResourcePoolsDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class ResourcePoolsDaoImpl extends SpringJDBCBaseDao implements IResourcePoolsDao {
    private final String ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_CREATE ="创建资源池失败。资源池名称：%s，IP：%s，创建时间：%s，失败原因：%s";
    private final String ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_UPDATE ="修改资源池失败。资源池ID: %s，资源池名称：%s，失败原因：%s";
    private final String ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_DELETE ="删除资源池失败。资源池ID：%s，失败原因：%s";
    private final String ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT ="查询资源池失败。资源池名称：%s，IP：%s，创建时间：%s，失败原因：%s";

    private static final class ResourcePoolsMapper implements RowMapper{
		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			TResourcePoolsBO pool = new TResourcePoolsBO();
			pool.setId(rs.getInt("id"));
			pool.setPoolName(rs.getString("POOL_NAME"));
			pool.setIp(rs.getString("IP"));
			pool.setUsername(rs.getString("USERNAME"));
			pool.setPassword(rs.getString("PASSWORD"));
			pool.setState(rs.getInt("STATE"));
			pool.setCreateDt(Constants.SDF.YYYYMMDDHHMMSS.getValue().format(rs.getTimestamp("CREATE_DT")));
			pool.setReviewState(rs.getInt("REVIEW_STATE"));
			pool.setType(rs.getInt("TYPE"));
			pool.setPort(rs.getString("PORT"));
			pool.setPhyRestPath(rs.getString("PHY_REST_PATH"));
			return pool;
		}

    }
    @Override
	public int save(final TResourcePoolsBO resourcePools) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO T_SCS_RESOURCE_POOLS("
			+ "POOL_NAME,IP,USERNAME,PASSWORD,STATE,CREATE_DT,PORT,PHY_REST_PATH) VALUES "
			+ "(?,?,?,?,?,?,?,?);";
		try {
			getJdbcTemplate().update(new PreparedStatementCreator() {
				@Override
				public PreparedStatement createPreparedStatement(Connection con)
						throws SQLException {
					int i = 1;
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setString(i++, resourcePools.getPoolName());
					ps.setString(i++, resourcePools.getIp());
					ps.setString(i++, resourcePools.getUsername());
					ps.setString(i++, resourcePools.getPassword());
					ps.setInt(i++, resourcePools.getState());
					ps.setString(i++, resourcePools.getCreateDt());
					ps.setString(i++, resourcePools.getPort());
					ps.setString(i++, resourcePools.getPhyRestPath());
					return ps;
				}

			},keyHolder);
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_CREATE, resourcePools.getPoolName(), resourcePools.getIp(),resourcePools.getCreateDt(),e.getMessage()));
		}
		return keyHolder.getKey().intValue();
	}

	@Override
	public int delete(final int resourcePoolsId) throws SQLException {
		int ret_val = -1;
		String sql = "delete from  T_SCS_RESOURCE_POOLS WHERE ID = ?";
		try {
			ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				@Override
                public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, resourcePoolsId);
				}
			});
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_DELETE, resourcePoolsId, e.getMessage()));
		}
		return ret_val;
	}

	@Override
	public int update(final TResourcePoolsBO resourcePools) throws SQLException {
		int ret_val = -1;
		String sql = "update T_SCS_RESOURCE_POOLS set POOL_NAME=?,IP=?,USERNAME=?,PASSWORD=?,STATE=?,REVIEW_STATE=?,PORT=?,PHY_REST_PATH=? WHERE ID = ?;";

		try {
			ret_val = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;

				@Override
                public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, resourcePools.getPoolName());
					ps.setString(i++, resourcePools.getIp());
					ps.setString(i++, resourcePools.getUsername());
					ps.setString(i++, resourcePools.getPassword());
					ps.setInt(i++, resourcePools.getState());
					ps.setInt(i++, resourcePools.getReviewState());
					ps.setString(i++, resourcePools.getPort());
					ps.setString(i++, resourcePools.getPhyRestPath());
					//条件
					ps.setInt(i++, resourcePools.getId());
				}
			});
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_UPDATE, resourcePools.getId(), resourcePools.getPoolName(),
					e.getMessage()));
		}
		return ret_val;
	}

	@Override
	public TResourcePoolsBO searchResourcePoolsById(final int resourcePoolsId)
			throws SQLException {

		StringBuffer sql = new StringBuffer();
		sql.append("SELECT ID,POOL_NAME,IP,USERNAME,PASSWORD,STATE,CREATE_DT,REVIEW_STATE,TYPE,PORT,PHY_REST_PATH from T_SCS_RESOURCE_POOLS ");
		sql.append("WHERE STATE = 1  and REVIEW_STATE = 1 and ID = ? ");
		TResourcePoolsBO pool = null;
		try {
			List<TResourcePoolsBO> pools = getJdbcTemplate().query(sql.toString(), new ResourcePoolsMapper(), resourcePoolsId);
			if (pools != null && pools.size()>0) {
				pool = pools.get(0);
			}
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT, e.getMessage()));
		}
		return pool;
	}

	@Override
	public List<TResourcePoolsBO> searchAllPools() throws SQLException {
		String sql = "SELECT ID,POOL_NAME,IP,USERNAME,PASSWORD,STATE,CREATE_DT from T_SCS_RESOURCE_POOLS";

		BeanPropertyRowMapper<TResourcePoolsBO> rowMapper = new BeanPropertyRowMapper<TResourcePoolsBO>(TResourcePoolsBO.class);
		List<TResourcePoolsBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TResourcePoolsBO>(rowMapper));
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
	        return returnList;
        } else {
	        return null;
        }
	}

	@Override
  public List<TResourcePoolsBO> searchResourcePoolsByName(String resourceName) throws SQLException {
	  String sql = "SELECT ID,POOL_NAME,IP,USERNAME,PASSWORD,STATE,CREATE_DT from T_SCS_RESOURCE_POOLS WHERE POOL_NAME like ? ";

    BeanPropertyRowMapper<TResourcePoolsBO> rowMapper = new BeanPropertyRowMapper<TResourcePoolsBO>(TResourcePoolsBO.class);
    List<TResourcePoolsBO> returnList = null;
    try {
      returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TResourcePoolsBO>(rowMapper),"%"+resourceName+"%");
    } catch (Exception e) {
      throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT, e.getMessage()));
    }
    if (returnList != null && returnList.size() > 0) {
          return returnList;
        } else {
          return null;
        }
  }

  @Override
	public int searchLastId() throws SQLException {
		int ret_Id = -1;
		try {
			String sql = "SELECT ID from T_SCS_RESOURCE_POOLS ORDER BY ID DESC LIMIT 0,1";
			ret_Id = this.getJdbcTemplate().queryForInt(sql);
		} catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT, e.getMessage()));
		}
		return ret_Id;
	}

    @Override
    public List<TResourcePoolsBO> searchForReviewPools() throws SQLException {
        String sql = "SELECT ID,POOL_NAME,IP,USERNAME,PASSWORD,STATE,CREATE_DT,REVIEW_STATE from T_SCS_RESOURCE_POOLS where REVIEW_STATE>0;";

        BeanPropertyRowMapper<TResourcePoolsBO> rowMapper = new BeanPropertyRowMapper<TResourcePoolsBO>(TResourcePoolsBO.class);
        List<TResourcePoolsBO> returnList = null;
        try {
            returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TResourcePoolsBO>(rowMapper));
        } catch (Exception e) {
            throw new SQLException(String.format(this.ERROR_MESSAGE_PORTAL_RESPOOlS_DAO_SELECT, e.getMessage()));
        }
        if (returnList != null && returnList.size() > 0) {
	        return returnList;
        } else {
	        return null;
        }
    }
}
