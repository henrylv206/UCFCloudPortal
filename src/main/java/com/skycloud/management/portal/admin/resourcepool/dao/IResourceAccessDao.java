package com.skycloud.management.portal.admin.resourcepool.dao;

import java.sql.SQLException;
import java.util.List;

public interface IResourceAccessDao {
	List<String> getOSListByPoolID(int poolID) throws SQLException;

	List<String> getVLanListByPoolID(int poolID) throws SQLException;
}
