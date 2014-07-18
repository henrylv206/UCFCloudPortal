package com.skycloud.management.portal.admin.sysmanage.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.admin.sysmanage.entity.TMenuBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TPermission;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.customer.dao.IAdminDAO;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;

public class PermissionDaoImpl extends SpringJDBCBaseDao  {


	/**
	 * 数据库校验用户名密码
	 */
	public List<TPermission> queryAllPermissions() {
		String sql = "SELECT  `T_SCS_MENU`.`MENU_ID`, `T_SCS_MENU`.`MENU_NAME`, `T_SCS_MENU`.`MENU_CODE`, "
			+" `T_SCS_MENU`.`MENU_DESCR`, `T_SCS_MENU`.`PARENT_SCS_MENU_ID`, `T_SCS_MENU`.`ACTION_URL`,"
			+" `T_SCS_MENU`.`IMG_URL`, `T_SCS_MENU`.`STATE`, `T_SCS_MENU`.`MENU_ORDER` "
			+" FROM T_SCS_MENU  WHERE STATE=1 order by MENU_ID, PARENT_SCS_MENU_ID";
		List<TPermission> perList = null;
		TUserBO user = null;
		BeanPropertyRowMapper<TPermission> userRowMapper = new BeanPropertyRowMapper<TPermission>(TPermission.class);
		try {				
			try {
				perList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询菜单失败。失败原因：" + e.getMessage());
			}
			if (perList != null && perList.size() > 0){
				return perList;
			}				
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<TMenuBO> queryAllMenus(final int id) throws SQLException{
		String sql = "SELECT  `T_SCS_MENU`.`MENU_ID`, `T_SCS_MENU`.`MENU_NAME`, `T_SCS_MENU`.`MENU_CODE`, "
			+" `T_SCS_MENU`.`MENU_DESCR`, `T_SCS_MENU`.`PARENT_SCS_MENU_ID`, `T_SCS_MENU`.`ACTION_URL`,"
			+" `T_SCS_MENU`.`IMG_URL`, `T_SCS_MENU`.`STATE`, `T_SCS_MENU`.`MENU_ORDER` "
			+" FROM T_SCS_MENU  WHERE STATE=1 and PARENT_SCS_MENU_ID = ? order by MENU_ID, PARENT_SCS_MENU_ID ";
		BeanPropertyRowMapper<TMenuBO> menuRowMapper = new BeanPropertyRowMapper<TMenuBO>(TMenuBO.class); 
		   
		List<TMenuBO> unsortedMenuList = null;
		try {			
			unsortedMenuList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);
				}}, menuRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用户菜单。失败原因：" + e.getMessage());
		}
		return unsortedMenuList;
	}
	
	public void saveMenusByDeptId(int deptId,String[] menus) throws SQLException{	
		try {
			
			String[] ins = new String[menus.length];
			int i = 0;
			for(String m :menus){
				if(StringUtils.isNotEmpty(m)){
					ins[i] ="insert T_SCS_DRM (DEPT_ID,MENU_ID) values("+deptId+","+m+")";
				}				
				i++;
			}				
			this.getJdbcTemplate().batchUpdate(ins);
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("insert用户菜单。失败原因：" + e.getMessage());
		}
		
	}
	
	public int delMenusByDeptId(int deptId){		
		String del = "delete from T_SCS_DRM where DEPT_ID = ?";		
		return this.getJdbcTemplate().update(del, new Object[]{deptId});		
	}
	
	public List<TMenuBO> queryMenusByDeptId(final int deptId) throws SQLException{		
		String sql = "SELECT  `T_SCS_MENU`.`MENU_ID`, `T_SCS_MENU`.`MENU_NAME`, "
			+" `T_SCS_MENU`.`MENU_CODE`, `T_SCS_MENU`.`MENU_DESCR`, `T_SCS_MENU`.`PARENT_SCS_MENU_ID`,"
			+" `T_SCS_MENU`.`ACTION_URL`, `T_SCS_MENU`.`IMG_URL`, `T_SCS_MENU`.`STATE`, "
			+" `T_SCS_MENU`.`MENU_ORDER` FROM `T_SCS_MENU` JOIN `T_SCS_DRM` ON  `T_SCS_MENU`.`MENU_ID` = `T_SCS_DRM`.`MENU_ID`"
			+" WHERE `T_SCS_DRM`.`DEPT_ID` = ? AND T_SCS_MENU.STATE=1;";
		
		BeanPropertyRowMapper<TMenuBO> menuRowMapper = new BeanPropertyRowMapper<TMenuBO>(TMenuBO.class); 
		   
		List<TMenuBO> unsortedMenuList = null;
		try {
			
			unsortedMenuList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, deptId);
				}
			}, menuRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用户菜单。失败原因：" + e.getMessage());
		}
		return unsortedMenuList;				
	}
	public List<TMenuBO> queryMenusByDeptIdForTree(final int deptId) throws SQLException{		
		String sql = "SELECT  `T_SCS_MENU`.`MENU_ID`, `T_SCS_MENU`.`MENU_NAME`, "
			+" `T_SCS_MENU`.`MENU_CODE`, `T_SCS_MENU`.`MENU_DESCR`, `T_SCS_MENU`.`PARENT_SCS_MENU_ID`,"
			+" `T_SCS_MENU`.`ACTION_URL`, `T_SCS_MENU`.`IMG_URL`, `T_SCS_MENU`.`STATE`, "
			+" `T_SCS_MENU`.`MENU_ORDER` FROM `T_SCS_MENU` JOIN `T_SCS_DRM` ON  `T_SCS_MENU`.`MENU_ID` = `T_SCS_DRM`.`MENU_ID`"
			+" WHERE `T_SCS_DRM`.`DEPT_ID` = ?  AND `T_SCS_MENU`.`PARENT_SCS_MENU_ID` <>0 AND T_SCS_MENU.STATE=1;";
		
		BeanPropertyRowMapper<TMenuBO> menuRowMapper = new BeanPropertyRowMapper<TMenuBO>(TMenuBO.class); 
		   
		List<TMenuBO> unsortedMenuList = null;
		try {
			
			unsortedMenuList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, deptId);
				}
			}, menuRowMapper);
		} catch (Exception e) {
			throw new SQLException("查询用户菜单。失败原因：" + e.getMessage());
		}
		return unsortedMenuList;				
	}
}
