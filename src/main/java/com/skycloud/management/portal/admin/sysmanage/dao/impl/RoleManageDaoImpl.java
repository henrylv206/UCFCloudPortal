package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.admin.sysmanage.dao.IRoleManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TMenuBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TRoleBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class RoleManageDaoImpl extends SpringJDBCBaseDao implements IRoleManageDao {

	@Override
	public int deleteRoleMenu(final TRoleBO role) throws SQLException {
		String sql = "";
		try {
			sql = "DELETE FROM T_SCS_RRM WHERE ROLE_ID = ?;";
			this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, role.getRoleId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("删除角色权限失败。角色名称：" + role.getRoleName() + "，失败原因：" + e.getMessage());
		}
		 
		return 0;
	}
	@Override
	public int deleteRole(final int roleId) throws SQLException {
		String sql = "";
		int ret = 0;
		try {
			sql = "UPDATE  T_SCS_ROLE SET STATE = 0 WHERE ROLE_ID = ?;";
			ret = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, roleId);
				}
			});
		} catch (Exception e) {
			throw new SQLException("删除角色失败。角色名称：" + roleId + "，失败原因：" + e.getMessage());
		}
		 
		return ret;
	}
	@Override
	public int updateRole(final TRoleBO role) throws SQLException {
		String sql = "";
		int ret = 0;
		try {
			sql = "UPDATE  T_SCS_ROLE set ROLE_APPROVE_LEVEL=?,ROLE_DESCR=? WHERE ROLE_ID = ?;";
			ret = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, role.getRoleApproveLevel());
					ps.setString(2, role.getRoleDescr());
					ps.setInt(3, role.getRoleId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("修改角色失败。角色名称：" + role.getRoleName() + "，失败原因：" + e.getMessage());
		}
		 
		return ret;
	}
	
	@Override
	public int insertRole(final TRoleBO role) throws SQLException {
		String sql = "";
		int ret = 0;
		try {
			sql = "insert into T_SCS_ROLE (ROLE_NAME,ROLE_APPROVE_LEVEL,ROLE_DESCR,CREATOR_USER_ID,CREATE_TIME,LASTUPDATE_DT,STATE) values (?,?,?,?,?,?,1);";
			ret = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, role.getRoleName());
					ps.setInt(2, role.getRoleApproveLevel());
					ps.setString(3, role.getRoleDescr());
					ps.setInt(4, role.getCreatorUserId());
					ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
					ps.setTimestamp(6, new Timestamp(System.currentTimeMillis()));
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("增加角色失败。角色名称：" + role.getRoleName() + "，失败原因：" + e.getMessage());
		}
		 
		return ret;
	}
//	@Override
//	public int updateRole(final TRoleBO role) throws SQLException {
//		String sql = ""; 
//		final List<TMenuBO> menuList = role.getMenuList();
//		try{
//			sql = "INSERT INTO `T_SCS_RRM` (`ROLE_ID`, `MENU_ID`, `CREATE_TIME`, `LASTUPDATE_DT`) VALUES (?, ?, ?, ?);";
//			jdbcTemplate.batchUpdate(sql, new BatchPreparedStatementSetter() {
//				public void setValues(PreparedStatement ps, int i) throws SQLException {				 
//					ps.setInt(1, role.getRoleId());
//					ps.setInt(2, menuList.get(i).getMenuId());
//					ps.setDate(3, new java.sql.Date(System.currentTimeMillis()));
//					ps.setDate(4, new java.sql.Date(System.currentTimeMillis()));
//				}
//	
//				public int getBatchSize() {
//					return menuList.size();
//				}
//			});
//		}catch (Exception e) {
//			throw new SQLException("删除角色权限失败。角色名称：" + role.getRoleName() + "，失败原因：" + e.getMessage());
//		}
//		
//		return 0;
//	}
	@Override
	public List<TRoleBO> findAllRole() throws SQLException {
		String sql="select ROLE_ID,ROLE_NAME,ROLE_DESCR,CREATOR_USER_ID,ROLE_APPROVE_LEVEL from T_SCS_ROLE where STATE=1 order by ROLE_ID desc";
		BeanPropertyRowMapper<TRoleBO> roleRowMapper = new BeanPropertyRowMapper<TRoleBO>(TRoleBO.class);
		List<TRoleBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new RowMapperResultSetExtractor<TRoleBO>(roleRowMapper));
		} catch (Exception e) {
			throw new SQLException("查询角色失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}
	@Override
	public TRoleBO findRoleById(final int roleId) throws SQLException {
		String sql="select ROLE_ID,ROLE_NAME,ROLE_DESCR,CREATOR_USER_ID,CREATE_TIME,LASTUPDATE_DT,ROLE_APPROVE_LEVEL from T_SCS_ROLE where ROLE_ID=?";
		BeanPropertyRowMapper<TRoleBO> roleRowMapper = new BeanPropertyRowMapper<TRoleBO>(TRoleBO.class);
		List<TRoleBO> returnList = null;
		TRoleBO role=null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setInt(1, roleId);
						}
					}, roleRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询角色失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			role= returnList.get(0);
		else
			return null;
		
		role.setMenuList(findMenuByRoleId(roleId));
		return role;
		
	}
	
	@Override
	public TRoleBO findRoleByName(final String roleName) throws SQLException {
		String sql="select ROLE_ID,ROLE_NAME,ROLE_DESCR,CREATOR_USER_ID,CREATE_TIME,LASTUPDATE_DT,ROLE_APPROVE_LEVEL from T_SCS_ROLE where ROLE_NAME=? AND STATE=1";
		BeanPropertyRowMapper<TRoleBO> roleRowMapper = new BeanPropertyRowMapper<TRoleBO>(TRoleBO.class);
		List<TRoleBO> returnList = null;
		TRoleBO role=null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, roleName);
						}
					}, roleRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询角色失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			role= returnList.get(0);
		else
			return null;
		return role;
		
	}
	
	private List<TMenuBO> findMenuByRoleId(final int roleId) throws SQLException {
		String sql="select m.MENU_ID,m.MENU_NAME,m.MENU_CODE,m.MENU_DESCR,m.PARENT_SCS_MENU_ID," +
				    "m.ACTION_URL,m.IMG_URL,m.STATE,m.MENU_ORDER " +
				    "from T_SCS_RRM r JOIN T_SCS_MENU m on r.MENU_ID=m.MENU_ID where r.ROLE_ID=?;";
		BeanPropertyRowMapper<TMenuBO> menuRowMapper = new BeanPropertyRowMapper<TMenuBO>(TMenuBO.class);
		List<TMenuBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setInt(1, roleId);
						}
					}, menuRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询权限失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}
	@Override
	public List<TRoleBO> searchRole(final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select ROLE_ID,ROLE_NAME,ROLE_DESCR,ROLE_APPROVE_LEVEL from T_SCS_ROLE where STATE = 1 ");		
		if(criteria.getRoleName()!=null&&criteria.getRoleName()!="") 
			sql.append(" and ROLE_NAME LIKE ? ");		
		sql.append(" order by ROLE_ID desc; ");
		BeanPropertyRowMapper<TRoleBO> roleRowMapper = new BeanPropertyRowMapper<TRoleBO>(TRoleBO.class); 
		List<TRoleBO> returnList = null;
		
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {			
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					if(criteria.getRoleName()!=null&&criteria.getRoleName()!="")
						ps.setString(i++, "%"+criteria.getRoleName()+"%");
				}
			},roleRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询角色失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}
	@Override
	public int queryUsedRole(int roleID) throws SQLException {
		String sql="select count(*) from T_SCS_USER where ROLE_ID=?";
		int count = 0;
		try {
			count = this.getJdbcTemplate().queryForInt(sql,new Object[]{roleID});
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询角色失败。失败原因：" + e.getMessage());
		}
		return count;
	}

		
	
}
