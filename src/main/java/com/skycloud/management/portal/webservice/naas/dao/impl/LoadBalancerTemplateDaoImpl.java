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
import com.skycloud.management.portal.webservice.naas.dao.ILoadBalancerTemplateDao;
import com.skycloud.management.portal.webservice.naas.entity.LoadBalancerTemplate;

/**
 * 
 * @author liujijun
 * @since Feb 3, 2012
 * @version 1.0
 */
public class LoadBalancerTemplateDaoImpl implements ILoadBalancerTemplateDao {

	private static final Logger logger = LoggerFactory.getLogger(LoadBalancerTemplateDaoImpl.class);

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	/**
	 * 根据主键取得负载均衡模板
	 * @throws ObjectNotFoundException 
	 */
	public LoadBalancerTemplate getLoadBalancerTemplateById(int id) throws ObjectNotFoundException {
		String sql = "SELECT ID id,TEMPLATE_DESC name,STORAGE_SIZE connNum,STATE status FROM `T_SCS_TEMPLATE_VM` WHERE `TYPE`=? AND `ID`=? AND STATE!=5";
		if (logger.isDebugEnabled()) {
			logger.debug("查询负载均衡模板id:{}", id);
		}
		LoadBalancerTemplate template = null;
		try {
			template =jdbcTemplate.queryForObject(sql,
					new Object[] { ConstDef.RESOURCE_TYPE_LOADBALANCED, id },
					new TemplateRowMapper());
		} catch (DataAccessException e) {
			throw new ObjectNotFoundException("查询的负载均衡模板不存在,对应编号为"+id);
		}
		
		return template;
	}

	/**
	 * 取得所有负载均衡模板
	 * 
	 */
	public List<LoadBalancerTemplate> listLoadBalancerTemplate() {
		String sql = "SELECT ID id,TEMPLATE_DESC name ,TYPE type,STORAGE_SIZE connNum,STATE status FROM `T_SCS_TEMPLATE_VM` WHERE `TYPE`=? AND STATE!=5";

		return jdbcTemplate.query(sql, new Object[] { ConstDef.RESOURCE_TYPE_LOADBALANCED },
				new TemplateRowMapper());
	}

	private class TemplateRowMapper implements RowMapper<LoadBalancerTemplate> {
		@Override
		public LoadBalancerTemplate mapRow(ResultSet rs, int rowNum) throws SQLException {
			LoadBalancerTemplate template = new LoadBalancerTemplate();
			template.setId(rs.getInt("id"));
			template.setName(rs.getString("name"));
			template.setConnNum(rs.getInt("connNum"));
			template.setStatus(rs.getString("status"));
			return template;
		}
	}

}
