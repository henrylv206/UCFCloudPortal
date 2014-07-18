package com.skycloud.management.portal.admin.resmanage.dao.impl;

import java.sql.SQLException;
import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.admin.resmanage.dao.IChargeUnitDao;
import com.skycloud.management.portal.admin.resmanage.entity.ChargeUnit;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

/**
 * * haolong@20120910 01 计费模式变更
 * 何军辉 20130311 维护更新
 * 
 */
public class ChargeUnitDaoImpl extends SpringJDBCBaseDao implements IChargeUnitDao {
	
	private Logger logger = LogManager.getLogger(this.getClass());

	@Override
	public List<ChargeUnit> findAll() throws SQLException {
		String sql = "SELECT ID, UNIT, DESCRIPTION, UNIT_CODE FROM T_SCS_CHARGE_UNIT "
				+ "WHERE STATUS = 1";
		BeanPropertyRowMapper<ChargeUnit> menuRowMapper = new BeanPropertyRowMapper<ChargeUnit>(
				ChargeUnit.class);

		List<ChargeUnit> list = null;
		try {
			list = this.getJdbcTemplate().query(
					sql,
					new RowMapperResultSetExtractor<ChargeUnit>(
							menuRowMapper));
		} catch (Exception e) {
			throw new SQLException("query T_SCS_CHARGE_UNIT by product id"
					+ e.getMessage());
		}
		return list;
	}

	@Override
	public int findIdByUnit(String unit) {
		String i;
		try {
			i = this.findByUnit(unit).getId();
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(String.format("findIdByUnit(%s)", unit),e);
			return 0;
		}
		if(i == null || i.length() == 0){
			return 0;
		}else{
			return Integer.parseInt(i);
		}
	}

	@Override
	public ChargeUnit findByUnit(String unit) throws SQLException {
		String sql = "SELECT ID, UNIT, DESCRIPTION, UNIT_CODE FROM T_SCS_CHARGE_UNIT "
				+ "WHERE UNIT = '%s'";
		sql = String.format(sql, unit);
		BeanPropertyRowMapper<ChargeUnit> menuRowMapper = new BeanPropertyRowMapper<ChargeUnit>(
				ChargeUnit.class);

		List<ChargeUnit> list = null;
		try {
			list = this.getJdbcTemplate().query(
					sql,
					new RowMapperResultSetExtractor<ChargeUnit>(
							menuRowMapper));
		} catch (Exception e) {
			throw new SQLException("query T_SCS_CHARGE_UNIT by unit"
					+ e.getMessage());
		}
		return list.get(0);
	}
}