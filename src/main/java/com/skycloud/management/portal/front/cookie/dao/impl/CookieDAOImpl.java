package com.skycloud.management.portal.front.cookie.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;

import com.skycloud.management.portal.front.cookie.dao.ICookieDAO;
import com.skycloud.management.portal.front.cookie.entity.TCookieVO;

public class CookieDAOImpl implements ICookieDAO {

	private JdbcTemplate jdbcT;// SCS JDBC


	public JdbcTemplate getJdbcT() {
		return jdbcT;
	}
	public void setJdbcT(JdbcTemplate jdbcT) {
		this.jdbcT = jdbcT;
	}


	@Override
	public boolean saveOrUpdate(final TCookieVO cookie) throws SQLException{

		List<TCookieVO> list = this.getCookieByKey(cookie.getCOOKIE_KEY());
		String sql ="";
		if(list.size() == 1){
			try {
				 sql = "UPDATE T_SCS_COOKIE SET  COOKIE_TYPE =?, COOKIE_VALUE=?, UPDATE_TIME=? WHERE COOKIE_KEY=?";
				jdbcT.update(sql,
						new Object[] {cookie.getCOOKIE_TYPE(),cookie.getCOOKIE_VALUE(),cookie.getUpdateTime(),cookie.getCOOKIE_KEY()
				});
				return true;
			} catch (DataAccessException e) {
				throw new SQLException("更新Cookie失败，失败原因：" + e.getMessage());
			}
		}else{
			try {
				 sql = "INSERT INTO T_SCS_COOKIE VALUES(?,?,?,?)";
				jdbcT.update(sql, new PreparedStatementSetter() {
					@Override
                    public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(1,cookie.getCOOKIE_KEY());
						ps.setInt(2,cookie.getCOOKIE_TYPE());
						ps.setString(3, cookie.getCOOKIE_VALUE());
						ps.setTimestamp(4, cookie.getUpdateTime());
						}
				});
				return true;
			} catch (DataAccessException e) {
				throw new SQLException("创建Cookie失败，失败原因：" + e.getMessage());
			}
		}
	}

	@Override
	public List<TCookieVO> getCookieByKey(final String key) {
		String sql = " SELECT * FROM T_SCS_COOKIE WHERE COOKIE_KEY = ? ";
		List<TCookieVO> returnList = null;

		BeanPropertyRowMapper<TCookieVO> cookieRowMapper = new BeanPropertyRowMapper<TCookieVO>(TCookieVO.class);

		returnList = jdbcT.query(sql , new PreparedStatementSetter() {
			@Override
            public void setValues(PreparedStatement ps)throws SQLException {
					ps.setString(1, key);
				}
			}, cookieRowMapper);

		return returnList;
	}

	@Override
	public void deleteCookieByKey(final String key) {
			String sql = "DELETE FROM T_SCS_COOKIE WHERE  COOKIE_KEY = ?";
			jdbcT.update(sql, new PreparedStatementSetter() {
				@Override
                public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			});
	}

	@Override
    public List<TCookieVO> getCookieByValue(final String valueStr) {
		String sql = " SELECT * FROM T_SCS_COOKIE t WHERE  instr(t.COOKIE_VALUE,?)>0 ";
		List<TCookieVO> returnList = null;

		BeanPropertyRowMapper<TCookieVO> cookieRowMapper = new BeanPropertyRowMapper<TCookieVO>(TCookieVO.class);

		returnList = jdbcT.query(sql , new PreparedStatementSetter() {
			@Override
            public void setValues(PreparedStatement ps)throws SQLException {
					ps.setString(1, valueStr);
				}
			}, cookieRowMapper);

		return returnList;
    }


}
