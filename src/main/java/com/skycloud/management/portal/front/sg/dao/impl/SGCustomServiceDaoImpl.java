package com.skycloud.management.portal.front.sg.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.sg.dao.ISGCustomServiceDao;
import com.skycloud.management.portal.front.sg.entity.SGCustomService;

/**
 * 自定义服务持久化实现
 * @author jiaoyz
 */
public class SGCustomServiceDaoImpl extends SpringJDBCBaseDao implements ISGCustomServiceDao {

  @SuppressWarnings("unchecked")
  private static final class SGCustomServiceMapper implements RowMapper {
    @Override
    public Object mapRow(ResultSet rs, int rn) throws SQLException {
      SGCustomService service = new SGCustomService();
      service.setId(rs.getInt("id"));
      service.setName(rs.getString("name"));
      service.setProtocol(rs.getString("protocol"));
      service.setSrcPort(rs.getInt("srcPort"));
      service.setDstPort(rs.getInt("dstPort"));
      service.setDeviceId(rs.getInt("deviceId"));
      service.setVdId(rs.getInt("vdId"));
      return service;
    }
  }

  @Override
  public int createCustomService(final SGCustomService service) throws Exception {
    KeyHolder keyHolder = new GeneratedKeyHolder();
    final String sql = "INSERT INTO T_SCS_SG_CUSTOMSERVICE(srcPort, dstPort, protocol, deviceId, vdId) VALUES (?, ?, ?, ?, ?)";
    getJdbcTemplate().update(new PreparedStatementCreator() {
      @Override
      public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
        PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, service.getSrcPort());
        ps.setInt(2, service.getDstPort());
        ps.setString(3, service.getProtocol());
        ps.setInt(4, service.getDeviceId());
        ps.setInt(5, service.getVdId());
        return ps;
      }
    }, keyHolder);
    return keyHolder.getKey().intValue();
  }

  @SuppressWarnings("unchecked")
  @Override
  public SGCustomService getSGCustomService(int vdId, int deviceId, String protocol, int srcPort, int dstPort) throws Exception {
    String sql = "SELECT * FROM T_SCS_SG_CUSTOMSERVICE WHERE protocol = ? AND srcPort = ? AND dstPort = ? AND deviceId = ? AND vdId = ?";
    SGCustomService service = null;
    try {
      service = (SGCustomService) getJdbcTemplate().queryForObject(sql, new SGCustomServiceMapper(), protocol, srcPort, dstPort, deviceId, vdId);
    }
    catch(EmptyResultDataAccessException e) {
      return null;
    }
    return service;
  }

  @Override
  public void updateCustomService(final SGCustomService service) throws Exception {
    String sql = "UPDATE T_SCS_SG_CUSTOMSERVICE SET name = ? WHERE id = ?";
    getJdbcTemplate().update(sql, new PreparedStatementSetter() {
      public void setValues(PreparedStatement ps) throws SQLException {
        ps.setString(1, service.getName());
        ps.setInt(2, service.getId());
      }
    });
  }

  @Override
  public void deleteCustomService(int id) throws Exception {
    String sql = "DELETE FROM T_SCS_SG_CUSTOMSERVICE WHERE id = ?";
    getJdbcTemplate().update(sql, id);
  }

}
