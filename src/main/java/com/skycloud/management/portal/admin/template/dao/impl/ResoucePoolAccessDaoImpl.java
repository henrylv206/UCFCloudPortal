package com.skycloud.management.portal.admin.template.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.admin.template.dao.IResourcePoolAccessDao;
import com.skycloud.management.portal.admin.template.entity.TResourcePoolBO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class ResoucePoolAccessDaoImpl extends SpringJDBCBaseDao implements
		IResourcePoolAccessDao {

	private JdbcTemplate resoucePoolJdbcTemplate;

	public boolean configJdbcTemplate(final String resourcePoolId)
			throws SQLException {

		String sql = "SELECT `T_SCS_RESOURCE_POOLS`.`ID`, `T_SCS_RESOURCE_POOLS`.`POOL_NAME`, `T_SCS_RESOURCE_POOLS`.`IP`, `T_SCS_RESOURCE_POOLS`.`USERNAME`,"
				+ "`T_SCS_RESOURCE_POOLS`.`PASSWORD`, `T_SCS_RESOURCE_POOLS`.`STATE`, `T_SCS_RESOURCE_POOLS`.`CREATE_DT` FROM `T_SCS_RESOURCE_POOLS` "
				+ "WHERE STATE = 1 AND ID = ?";
		BeanPropertyRowMapper<TResourcePoolBO> resourcePoolRowMapper = new BeanPropertyRowMapper<TResourcePoolBO>(
				TResourcePoolBO.class); // reusable
		List<TResourcePoolBO> returnList = null;// object

		try {
			returnList = this.getJdbcTemplate().query(
					sql,
					new PreparedStatementSetter() {
						public void setValues(PreparedStatement ps)
								throws SQLException {
							ps.setString(1, resourcePoolId);
						}
					},
					new RowMapperResultSetExtractor<TResourcePoolBO>(
							resourcePoolRowMapper));
		} catch (Exception e) {
			throw new SQLException("查询模板失败。失败原因：" + e.getMessage());
		}
		if (returnList != null && returnList.size() > 0) {
			TResourcePoolBO resourcePool = returnList.get(0);
			BasicDataSource ds = new BasicDataSource();
			ds.setUrl(resourcePool.getIp());
			ds.setUsername(resourcePool.getUsername());
			ds.setPassword(resourcePool.getPassword());
			ds.setDriverClassName(ConfigManager.getInstance().getString(
					"jdbc.driverClassName"));

			resoucePoolJdbcTemplate.setDataSource(ds);
			return true;
		} else
			return false;

	}

	@Override
	public void getServiceOfferingFromResourcePool(String resourcePoolId) {
		

	}

	@Override
	public void getDiskOfferingFromResourcePool(String resourcePoolId) {
		// TODO Auto-generated method stub

	}

	@Override
	public void getOSTemplateFromResourcePool(String resourcePoolId) {
		// TODO Auto-generated method stub

	}

}
