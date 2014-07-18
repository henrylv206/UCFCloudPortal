package com.skycloud.management.portal.front.cookie.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.cookie.entity.TCookieVO;


public interface ICookieDAO {
	public boolean saveOrUpdate(TCookieVO cookie)throws SQLException;

	public List<TCookieVO> getCookieByKey(String key);

	public void deleteCookieByKey(String key);

	public List<TCookieVO> getCookieByValue(String valueStr);
}
