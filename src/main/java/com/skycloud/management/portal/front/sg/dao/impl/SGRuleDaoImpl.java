package com.skycloud.management.portal.front.sg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.common.Constants;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.sg.dao.ISGRuleDao;
import com.skycloud.management.portal.front.sg.entity.SGRule;

/**
 * 安全组规则持久化实现
 * 
 * @author jiaoyz
 */
public class SGRuleDaoImpl extends SpringJDBCBaseDao implements ISGRuleDao {

	private static Log log = LogFactory.getLog(SGRuleDaoImpl.class);

	@SuppressWarnings("unchecked")
	private static final class SGRuleMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rn) throws SQLException {
			SGRule rule = new SGRule();
			rule.setId(rs.getInt("id"));
			rule.setSgId(rs.getString("sgId"));
			rule.setSourceIp(rs.getString("sourceIp"));
			rule.setSourcePort(rs.getInt("sourcePort"));
			rule.setDestinationIp(rs.getString("destinationIp"));
			rule.setDestinationPort(rs.getInt("destinationPort"));
			rule.setProtocol(rs.getString("protocol"));
			rule.setAccess(rs.getInt("access"));
			rule.setSgRulesId(rs.getInt("sgRulesId"));
			rule.setStatus(rs.getInt("status"));
			rule.setOperate(rs.getInt("operate"));
			return rule;
		}
	}

	@Override
	public int createRule(final SGRule rule) throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		final String sql = "INSERT INTO T_SCS_SG_RULE(sgId, sourceIp, sourcePort, destinationIp, destinationPort, protocol, access, sgRulesId, status, operate) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, rule.getSgId());
				ps.setString(2, rule.getSourceIp());
				ps.setInt(3, rule.getSourcePort());
				ps.setString(4, rule.getDestinationIp());
				ps.setInt(5, rule.getDestinationPort());
				ps.setString(6, rule.getProtocol());
				ps.setInt(7, rule.getAccess());
				ps.setInt(8, rule.getSgRulesId());
				ps.setInt(9, rule.getStatus());
				ps.setInt(10, rule.getOperate());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public void deleteRule(int id) throws Exception {
		String sql = "DELETE FROM T_SCS_SG_RULE WHERE id = ?";
		getJdbcTemplate().update(sql, id);
	}

	@SuppressWarnings("unchecked")
	@Override
	public SGRule getRuleById(int id) throws Exception {
		String sql = "SELECT * FROM T_SCS_SG_RULE WHERE id = ?";
		SGRule rule = null;
		try {
			rule = (SGRule) getJdbcTemplate().queryForObject(sql, new SGRuleMapper(), id);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return rule;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<SGRule> getRuleList(SGRule carrier, int curPage, int pageSize) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT * FROM T_SCS_SG_RULE WHERE 1 = 1";
		if (carrier != null) {
			if (StringUtils.isNotBlank(carrier.getSgId())) {
				sql += " AND sgId = ?";
				param.add(carrier.getSgId());
			}
			if (StringUtils.isNotBlank(carrier.getSourceIp())) {
				sql += " AND sourceIp like ?";
				param.add("%" + carrier.getSourceIp().replaceAll("_", "\\\\_") + "%");
			}
			if (carrier.getSourcePort() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND sourcePort = ?";
				param.add(carrier.getSourcePort());
			}
			if (StringUtils.isNotBlank(carrier.getDestinationIp())) {
				sql += " AND destinationIp like ?";
				param.add("%" + carrier.getDestinationIp().replaceAll("_", "\\\\_") + "%");
			}
			if (carrier.getDestinationPort() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND destinationPort = ?";
				param.add(carrier.getDestinationPort());
			}
			if (carrier.getAccess() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND access = ?";
				param.add(carrier.getAccess());
			}
			if (carrier.getStatus() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND status = ?";
				param.add(carrier.getStatus());
			}
			if (carrier.getOperate() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND operate = ?";
				param.add(carrier.getOperate());
			}
		}
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql, param.toArray(), new SGRuleMapper());
	}

	@Override
	public List<SGRule> getRuleListByBindIP(SGRule carrier) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT * FROM T_SCS_SG_RULE WHERE 1 = 1";
		sql += " AND (sourceIp = ? OR destinationIp = ?)";
		param.add(carrier.getSourceIp().replaceAll("_", "\\\\_"));
		param.add(carrier.getSourceIp().replaceAll("_", "\\\\_"));
		log.debug("getRuleListByBindIP:" + sql);
		log.debug("getRuleListByBindIP:" + param);
		return getJdbcTemplate().query(sql, param.toArray(), new SGRuleMapper());
	}

	@Override
	public int getRuleListCount(SGRule carrier) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT COUNT(0) FROM T_SCS_SG_RULE WHERE 1 = 1";
		if (carrier != null) {
			if (StringUtils.isNotBlank(carrier.getSgId())) {
				sql += " AND sgId = ?";
				param.add(carrier.getSgId());
			}
			if (StringUtils.isNotBlank(carrier.getSourceIp())) {
				sql += " AND sourceIp like ?";
				param.add("%" + carrier.getSourceIp().replaceAll("_", "\\\\_") + "%");
			}
			if (carrier.getSourcePort() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND sourcePort = ?";
				param.add(carrier.getSourcePort());
			}
			if (StringUtils.isNotBlank(carrier.getDestinationIp())) {
				sql += " AND destinationIp like ?";
				param.add("%" + carrier.getDestinationIp().replaceAll("_", "\\\\_") + "%");
			}
			if (carrier.getDestinationPort() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND destinationPort = ?";
				param.add(carrier.getDestinationPort());
			}
			if (carrier.getAccess() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND access = ?";
				param.add(carrier.getAccess());
			}
			if (carrier.getStatus() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND status = ?";
				param.add(carrier.getStatus());
			}
			if (carrier.getOperate() != Constants.STATUS_COMMONS.IGNORE.getValue()) {
				sql += " AND operate = ?";
				param.add(carrier.getOperate());
			}
		}
		return getJdbcTemplate().queryForObject(sql, param.toArray(), Integer.class);
	}

	@Override
	public void updateRule(final SGRule rule) throws Exception {
		String sql = "UPDATE T_SCS_SG_RULE SET sourceIp = ?, sourcePort = ?, destinationIp = ?, destinationPort = ?, protocol = ?, access = ?, sgRulesId = ?, status = ?, operate = ? WHERE id = ?";
		getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, rule.getSourceIp());
				ps.setInt(2, rule.getSourcePort());
				ps.setString(3, rule.getDestinationIp());
				ps.setInt(4, rule.getDestinationPort());
				ps.setString(5, rule.getProtocol());
				ps.setInt(6, rule.getAccess());
				ps.setInt(7, rule.getSgRulesId());
				ps.setInt(8, rule.getStatus());
				ps.setInt(9, rule.getOperate());
				ps.setInt(10, rule.getId());
			}
		});
	}

	@Override
	public int updateRuleJob(SGRule carrier) throws SCSException {
		StringBuffer sql = new StringBuffer("UPDATE T_SCS_SG_RULE SET STATUS = ? WHERE ID = ? ");
		int index = 0;
		List<Object> args = new ArrayList<Object>();
		if (carrier.getId() == 0) {
			throw new SCSException(SCSErrorCode.DB_SQL_PARAMETER_LOST_SG_RULE_ERROR, SCSErrorCode.DB_SQL_PARAMETER_LOST_SG_RULE_DESC);
		}
		args.add(carrier.getStatus());
		args.add(carrier.getId());
		try {
			index = getJdbcTemplate().update(sql.toString(), args.toArray());
		}
		catch (Exception e) {
			log.error(e);
			throw new SCSException(SCSErrorCode.DB_SQL_UPDATE_SG_RULE_ERROR, SCSErrorCode.DB_SQL_UPDATE_SG_RULE_DESC);
		}
		return index;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<SGRule> getRuleListByInstanceId(String id) throws Exception {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT * FROM T_SCS_SG_RULE WHERE sgId = ? and status=1";// bug
		                                                                       // 0003810
		param.add(id);
		return getJdbcTemplate().query(sql, param.toArray(), new SGRuleMapper());
	}
}
