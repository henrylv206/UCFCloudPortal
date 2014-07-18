package com.skycloud.management.portal.admin.menu.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.RowMapper;

import com.skycloud.management.portal.admin.menu.dao.IMenuDAO;
import com.skycloud.management.portal.admin.menu.entity.MenuBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class MenuDAOImpl extends SpringJDBCBaseDao implements IMenuDAO{
	private static Log log = LogFactory.getLog(MenuDAOImpl.class);

	private static final class MenuMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			MenuBO menu = new MenuBO();
			menu.setMenuId(rs.getInt("MENU_ID"));
			menu.setMenuName(rs.getString("menu_name"));
			menu.setMenuCode(rs.getString("menu_code"));
			menu.setMenuDesc(rs.getString("menu_descr"));
			return menu;
		}
		
	}

	public List<MenuBO> listMenu() throws Exception{
		List<MenuBO> list = new ArrayList<MenuBO>();
		List<Object> param = new ArrayList<Object>();
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("SELECT MENU_ID,menu_name,SUBSTRING(menu_code, 10) as menu_code,menu_descr FROM T_SCS_MENU t ");
		sbuilder.append(" where SUBSTRING(menu_code, 1, 8)='TEMPLATE' and state='1' and PARENT_SCS_MENU_ID!=0");
		list  = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),new MenuMapper());
		return list;
	}

}
