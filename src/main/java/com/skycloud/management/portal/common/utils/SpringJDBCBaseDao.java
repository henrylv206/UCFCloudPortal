package com.skycloud.management.portal.common.utils;

import org.springframework.jdbc.core.JdbcTemplate;

public class SpringJDBCBaseDao {
	public JdbcTemplate jdbcTemplate;

	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	} 
	 
}
