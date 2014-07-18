package com.skycloud.management.portal.webservice.naas.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.dao.IFirewallTemplateDao;
import com.skycloud.management.portal.webservice.naas.entity.FirewallTemplate;

/**
 * 防火墙模板数据访问层
 * 
 * @author liujijun
 * @since Jan 30, 2012
 * @version 1.0
 */
public class FirewallTemplateDaoImpl implements IFirewallTemplateDao {

	private static final Logger logger = LoggerFactory.getLogger(FirewallTemplateDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	public FirewallTemplate getFirewallTemplateById(int id) throws ObjectNotFoundException {
		String sql = "SELECT ID id,TEMPLATE_DESC name,STORAGE_SIZE ruleNum,STATE status FROM `T_SCS_TEMPLATE_VM` WHERE `TYPE`=? AND `ID`=? AND STATE!=5";
		logger.debug("查询防火墙模板id:" + id);
		try {
			return jdbcTemplate.queryForObject(sql,
					new Object[] { ConstDef.RESOURCE_TYPE_FIREWALL, id }, new TemplateRowMapper());
		} catch (DataAccessException e) {
			throw new ObjectNotFoundException("查询的防火墙模板不存在,对应编号为"+id);
		}
	}

	@Override
	public List<FirewallTemplate> listFirewallTemplate() {

		String sql = "SELECT ID id,TEMPLATE_DESC name,STORAGE_SIZE ruleNum,STATE status FROM `T_SCS_TEMPLATE_VM` WHERE `TYPE`=? AND STATE!=5";

		return jdbcTemplate.query(sql, new Object[] { ConstDef.RESOURCE_TYPE_FIREWALL },
				new TemplateRowMapper());
	}

	private class TemplateRowMapper implements RowMapper<FirewallTemplate> {
		@Override
		public FirewallTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
			FirewallTemplate template = new FirewallTemplate();
			template.setId(rs.getInt("id"));
			template.setName(rs.getString("name"));
			template.setRuleNum(rs.getInt("ruleNum"));
			template.setStatus(rs.getString("status"));
			return template;
		}
	}
}
