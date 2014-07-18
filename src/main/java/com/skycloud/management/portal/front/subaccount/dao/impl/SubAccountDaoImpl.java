package com.skycloud.management.portal.front.subaccount.dao.impl;

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

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.subaccount.dao.ISubAccountDao;
import com.skycloud.management.portal.front.subaccount.entity.TPortalMenu;
import com.skycloud.management.portal.front.subaccount.entity.TPortalMenuRelation;

public class SubAccountDaoImpl extends SpringJDBCBaseDao implements ISubAccountDao{

	@Override
	public List<TPortalMenu> getAllPortalMenu() throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT  `ID`, `MENU_ID`, `MENU_NAME`, `PARAM4`, `PARAM5` " +
				"FROM `T_SCS_PORTAL_MENU`;";
		BeanPropertyRowMapper<TPortalMenu> subActMapper = new BeanPropertyRowMapper<TPortalMenu>(TPortalMenu.class);
	   
		List<TPortalMenu> tPortalList = null;
		try {
			tPortalList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TPortalMenu>(subActMapper));
		} catch (Exception e) {
			throw new SQLException("query error,error msg:" + e.getMessage());
		}
		return tPortalList;
	}
	
	@Override
	public List<TPortalMenuRelation> getAllPortalMenuRelationByUserId(Integer userId) throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT  `ID`, `USER_ID`, `MENU_ID` " +
				"FROM `T_SCS_USER_PORTAL_MENU_RELATION` WHERE USER_ID = "+userId+";";
		BeanPropertyRowMapper<TPortalMenuRelation> subActMapper = new BeanPropertyRowMapper<TPortalMenuRelation>(TPortalMenuRelation.class);
	   
		List<TPortalMenuRelation> tPortalList = null;
		try {
			tPortalList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TPortalMenuRelation>(subActMapper));
		} catch (Exception e) {
			throw new SQLException("query error,error msg:" + e.getMessage());
		}
		
//		List<TUserBO> userList = null;
//		TUserBO user = null;
//		BeanPropertyRowMapper<TUserBO> userRowMapper = new BeanPropertyRowMapper<TUserBO>(TUserBO.class);
//				
//		userList = this.getJdbcTemplate().query(sql.toString(), new PreparedStatementSetter() {
//			public void setValues(PreparedStatement ps) throws SQLException {
//				ps.setInt(1, CompanyCheckStateEnum.SUCCESS);
//			}
//		}, userRowMapper);			

		return tPortalList;
	}
	
	@Override
	public int upDateMenuPortalRelation(final Integer userId, final String[] menuIdAry) throws SQLException{
		int ret = 0;
		
		delMenuPortalRelationByUserId(userId);
		
		for(String strTmp : menuIdAry){
			if (strTmp != null && !strTmp.equals("")) {
				saveMenuPortalRelation(userId, Integer.parseInt(strTmp));
			}
		}
		return ret;
	}
	
	public int saveMenuPortalRelation(final Integer userId, final Integer menuId) throws SQLException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql="insert into T_SCS_USER_PORTAL_MENU_RELATION(" +
				       "ID,USER_ID,MENU_ID) values(null,?,?);";
		try {
			this.getJdbcTemplate().update(new PreparedStatementCreator(){
				int i=1;
				@Override
				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
					ps.setInt(i++, userId);
					ps.setInt(i++, menuId);
					return ps;
				}
			}, keyHolder); 
		} catch (Exception e) {
			throw new SQLException("add user_protal_menu_relation err：" + e.getMessage());
		}
		return keyHolder.getKey().intValue();
	}
	
	public int delMenuPortalRelationByUserId(final Integer userId) throws SQLException {
		String sql="delete from T_SCS_USER_PORTAL_MENU_RELATION where USER_ID=?;";
		try {
			this.getJdbcTemplate().update(sql,new PreparedStatementSetter(){
				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, userId);
				}
			});
		} catch (Exception e) {
			throw new SQLException("delete MenuPortal Relation by User ID："+userId+",  ERRORMSG："+e.getMessage());
		}
		return 0;
	}

	@Override
	public List<TPortalMenu> searchUserIdByParam(final String userName, final String companyCode) throws SQLException {
		String sql = "select  TM.* from T_SCS_PORTAL_MENU TM, T_SCS_USER_PORTAL_MENU_RELATION TR " +
				"where TM.ID = TR.MENU_ID and TR.USER_ID = " +
				"(select TU.ID from T_SCS_USER TU, T_SCS_COMPANY_INFO TC  " +
				"where TU.COMP_ID = TC.COMP_ID and TU.ACCOUNT=? and TC.COMP_ORG_CODE=?)";
		BeanPropertyRowMapper<TPortalMenu> userRowMapper = new BeanPropertyRowMapper<TPortalMenu>(
				TPortalMenu.class);
		List<TPortalMenu> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, userName);
							ps.setString(2, companyCode);
						}
					}, userRowMapper);
		} catch (Exception e) {
			throw new SQLException("query error msg ：" + e.getMessage());
		}
		
		return returnList;
	}
	
}
