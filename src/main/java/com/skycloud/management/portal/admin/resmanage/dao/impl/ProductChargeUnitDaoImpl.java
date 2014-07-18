package com.skycloud.management.portal.admin.resmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.resmanage.dao.IProductChargeUnitDao;
import com.skycloud.management.portal.admin.resmanage.entity.ProuctChargeUnit;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

/**
 * * haolong@20120910 01 计费模式变更
 * 
 */
public class ProductChargeUnitDaoImpl extends SpringJDBCBaseDao implements
		IProductChargeUnitDao {

	@Override
	public List<Integer> save(List<ProuctChargeUnit> dataList) {
		List<Integer> list = new ArrayList<Integer>();

		for (ProuctChargeUnit date : dataList) {
			list.add(save(date));
		}
		// TODO Auto-generated method stub
		return list;
	}

	private Integer save(ProuctChargeUnit productCharUnit) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		String sql = "INSERT INTO T_SCS_PRODUCT_CHARGE_UNIT(PRODUCT_ID, UNIT_ID, PRICE, RESOURCE_ID) VALUES (?, ?, ?, ?)";
		getJdbcTemplate().update(createPreparedStatement(sql, productCharUnit),
				keyHolder);
		return keyHolder.getKey().intValue();
	}

	@Override
	public List<ProuctChargeUnit> findByProductId(final String productId)
			throws SQLException {
		// TODO Auto-generated method stub
		String sql = "SELECT C.UNIT, C.DESCRIPTION, C.UNIT_CODE, P.ID, P.PRODUCT_ID, P.UNIT_ID, P.PRICE, P.RESOURCE_ID "
				+ "FROM T_SCS_CHARGE_UNIT C, T_SCS_PRODUCT_CHARGE_UNIT P "
				+ "WHERE C.ID = P.UNIT_ID AND P.PRODUCT_ID = ?";
		BeanPropertyRowMapper<ProuctChargeUnit> menuRowMapper = new BeanPropertyRowMapper<ProuctChargeUnit>(
				ProuctChargeUnit.class);

		List<ProuctChargeUnit> list = null;
		try {
			list = this.getJdbcTemplate().query(sql, psSetter(productId),
					menuRowMapper);
		} catch (Exception e) {
			throw new SQLException(
					"query T_SCS_PRODUCT_CHARGE_UNIT by product id"
							+ e.getMessage());
		}
		return list;
	}

	private PreparedStatementSetter psSetter(final String productId) {
		return new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				// TODO Auto-generated method stub
				ps.setInt(1, Integer.parseInt(productId));
			}

		};
	}

	private PreparedStatementCreator createPreparedStatement(final String sql,
			final ProuctChargeUnit productCharUnit) {
		return new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				int i = 1;

				PreparedStatement ps = con.prepareStatement(sql,
						Statement.RETURN_GENERATED_KEYS);
				ps.setInt(i++, Integer.parseInt(productCharUnit.getProductId()));
				ps.setInt(i++, Integer.parseInt(productCharUnit.getUnitId()));
				ps.setFloat(i++, Float.parseFloat(productCharUnit.getPrice()));
				ps.setString(i++, productCharUnit.getResourceId());

				return ps;
			}
		};
	}

	@Override
	public String getNextStreamNumber() {
		// TODO Auto-generated method stub
		String sql = "SELECT CAST(RIGHT(RESOURCE_ID,6) as decimal) as id FROM T_SCS_PRODUCT_CHARGE_UNIT order by id desc limit 1";
		String result = null;
		Long l = null;
		try {
			l = getJdbcTemplate().queryForLong(sql);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (l == null) {
			result = "000001";
		} else {
			result = String.format("%06d", l + 1);
		}
		return result;
	}

	@Override
	public List<Integer>  modify(List<ProuctChargeUnit> dataListMod) throws NumberFormatException, SQLException {
		List<Integer> list = new ArrayList<Integer>();
		for(ProuctChargeUnit cu : dataListMod){
			list.add(update(cu));
		}
		return list;
	}
	
	@Override
	public int update(final ProuctChargeUnit entity) throws SQLException{

		List<Object> args = new ArrayList<Object>();
		String sql="update T_SCS_PRODUCT_CHARGE_UNIT set PRICE=? where ID=? ;";
		args.add(entity.getPrice());
		args.add(entity.getId());
		int a=-1;
		try {
			a=this.getJdbcTemplate().update(sql, args.toArray());
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("修改T_SCS_PRODUCT_CHARGE_UNIT失败。ID：" +entity.getId() + "失败原因：" + e.getMessage());
		}
		return a;
	}
	
	@Override
	public List<Integer> delete(List<ProuctChargeUnit> dataListdel) throws NumberFormatException, SQLException {
		List<Integer> list = new ArrayList<Integer>();
		for(ProuctChargeUnit cu : dataListdel){
			list.add(delete(Integer.parseInt(cu.getId())));
		}
		return list;
	}
	
	@Override
	public int delete(final int id) throws SQLException{

		String sql="delete from T_SCS_PRODUCT_CHARGE_UNIT where ID=? ;";
		int a=-1;
		try {
			a=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, id);
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new SQLException("删除T_SCS_PRODUCT_CHARGE_UNIT失败。ID：" +id + "失败原因：" + e.getMessage());
		}
		return a;
	}

	/**
	 * 新增 TScsProuctChargeUnit
	 * @param entity
	 * @return
	 * @throws SQLException
	 * 创建人：覃文讲
	 * 创建时间 ： 2012-11-27 上午11:25:00
	 */
	@Override
	public int add(ProuctChargeUnit entity) throws SQLException {
		// 调用原有方法进行插入
		return save(entity);
	}
}
