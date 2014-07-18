package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.sysmanage.dao.IDeptManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class DeptManageDaoImpl extends SpringJDBCBaseDao implements IDeptManageDao{

	@Override
	public List<TDeptBO> deptAll() throws SQLException {
		String sql="select DEPT_ID,DEPT_NAME,DEPT_DESC from T_SCS_DEPARTMENT order by DEPT_ID desc;";
		BeanPropertyRowMapper<TDeptBO> deptRowMapper = new BeanPropertyRowMapper<TDeptBO>(TDeptBO.class);
		List<TDeptBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new RowMapperResultSetExtractor<TDeptBO>(deptRowMapper));
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询部门失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

	@Override
	public TDeptBO findDeptById(final int deptId) throws SQLException {
		String sql="select DEPT_ID,DEPT_NAME,DEPT_DESC from T_SCS_DEPARTMENT where DEPT_ID=?";
		BeanPropertyRowMapper<TDeptBO> deptRowMapper = new BeanPropertyRowMapper<TDeptBO>(TDeptBO.class);
		List<TDeptBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, deptId);
				}
			}, deptRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询部门失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			return returnList.get(0);
		else
			return null;
	}
	
	@Override
	public int saveDept(final TDeptBO dept) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_DEPARTMENT(DEPT_ID,DEPT_NAME,DEPT_DESC) values(?,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, dept.getDeptId());
					ps.setString(i++, dept.getDeptName());
					ps.setString(i++, dept.getDeptDesc());
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("创建部门失败。部门描述：" +dept.getDeptDesc() + "失败原因：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}
	
	@Override
	public int updateDept(final TDeptBO dept) throws SQLException {
		String sql="update T_SCS_DEPARTMENT set DEPT_NAME=?,DEPT_DESC=? where DEPT_ID=?;";
		int result=0;
		try {
			result=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i=1;
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, dept.getDeptName());
					ps.setString(i++, dept.getDeptDesc());
					ps.setInt(i++, dept.getDeptId());
				}
			});
		} catch (Exception e) {
			throw new SQLException("更新部门失败。部门编号：" +dept.getDeptId() + "失败原因：" + e.getMessage());
		}
		return result;
	}

	@Override
	public int deleteDept(final int deptId) throws SQLException {
		String sql="delete from T_SCS_DEPARTMENT where DEPT_ID=? and DEPT_ID not in ( select DEPT_ID from T_SCS_USER);";
		int a=-1;
		try {
			a=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, deptId);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("删除部门失败。部门编号：" +deptId + "失败原因：" + e.getMessage());
		}
		return a;
	}

	@Override
	public TDeptBO findDeptByName(final String deptName,final String oldDeptName) throws SQLException {
		String sql = "select DEPT_ID,DEPT_NAME,DEPT_DESC from T_SCS_DEPARTMENT where DEPT_NAME=? ";
		if(oldDeptName!=null){
			sql+="AND DEPT_NAME!=? ;";
		}
		BeanPropertyRowMapper<TDeptBO> deptRowMapper = new BeanPropertyRowMapper<TDeptBO>(TDeptBO.class);
		// object
		List<TDeptBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, deptName);
							if(oldDeptName!=null){
								ps.setString(2, oldDeptName);
							}
						}
					}, deptRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询部门失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0)
			return returnList.get(0);
		else
			return null;
	}

	@Override
	public List<TUserBO> findUserByDept(final int deptId) throws SQLException {
		String sql="SELECT u.`NAME`,u.ACCOUNT,u.EMAIL,u.FAX,u.PHONE,u.MOBILE,d.DEPT_NAME,r.ROLE_NAME FROM T_SCS_USER u JOIN T_SCS_DEPARTMENT d ON u.DEPT_ID=d.DEPT_ID JOIN T_SCS_ROLE r ON r.ROLE_ID=u.ROLE_ID WHERE d.DEPT_ID=?;";
		BeanPropertyRowMapper<TUserBO> deptRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
		List<TUserBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, deptId);
				}
			}, deptRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询部门下得用户失败。失败原因：" + e.getMessage());
		}
	     return returnList;
	}

	@Override
	public List<TDeptBO> searchDept(final QueryCriteria criteria) throws SQLException {
		final StringBuffer sql = new StringBuffer();
		sql.append("select DEPT_ID,DEPT_NAME,DEPT_DESC from T_SCS_DEPARTMENT where 1 = 1 ");		
		if(criteria.getDeptName()!=null&&criteria.getDeptName()!="") 
			sql.append(" and DEPT_NAME LIKE ? ");		
		sql.append(" order by DEPT_ID desc; ");
		BeanPropertyRowMapper<TDeptBO> deptRowMapper = new BeanPropertyRowMapper<TDeptBO>(TDeptBO.class); 
		List<TDeptBO> returnList = null;
		
		try {
			returnList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {			
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					int i = 1;
					if(criteria.getDeptName()!=null&&criteria.getDeptName()!="")
						ps.setString(i++, "%"+criteria.getDeptName()+"%");
				}
			},deptRowMapper);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("查询部门失败。失败原因：" + e.getMessage());
		}
		return returnList;
	}

}
