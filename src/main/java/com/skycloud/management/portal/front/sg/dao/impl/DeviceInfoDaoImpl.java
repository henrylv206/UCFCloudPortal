package com.skycloud.management.portal.front.sg.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.sg.dao.IDeviceInfoDao;
import com.skycloud.management.portal.front.sg.entity.DeviceInfo;
import com.skycloud.management.portal.front.sg.entity.DeviceType;

public class DeviceInfoDaoImpl extends SpringJDBCBaseDao implements IDeviceInfoDao {

	@Override
	public DeviceInfo getDeviceInfoById(int id) throws Exception {
		String sql = "SELECT * FROM `T_SCS_H3C_DEVICE` WHERE `id`=?";
		DeviceInfo info = null;
		try {
			info = getJdbcTemplate().queryForObject(sql, new DeviceInfoRowMapper(), id);
		}
		catch (EmptyResultDataAccessException e) {
			return null;
		}
		return info;
	}

	class DeviceInfoRowMapper implements RowMapper<DeviceInfo> {

		@Override
		public DeviceInfo mapRow(ResultSet rs, int rowNum) throws SQLException {
			DeviceInfo device = new DeviceInfo();
			device.setId(rs.getInt("id"));
			device.setModel(rs.getString("model"));
			device.setName(rs.getString("name"));
			device.setType(DeviceType.valueOf(rs.getString("type")));
			device.setUrl(rs.getString("url"));
			device.setUsername(rs.getString("username"));
			device.setPassword(rs.getString("password"));
			device.setVldcode(rs.getString("vldcode"));
			device.setVendor(rs.getString("vendor"));
			device.setVirtualIp(rs.getString("virtualIp"));
			device.setMaster(rs.getInt("master"));
			return device;
		}
	}

	@Override
	public List<DeviceInfo> getDeviceInfoByType(DeviceType type) {
		String sql = "SELECT * FROM `T_SCS_H3C_DEVICE` WHERE `type`=?";
		return getJdbcTemplate().query(sql, new String[] { type.toString() }, new DeviceInfoRowMapper());
	}
}
