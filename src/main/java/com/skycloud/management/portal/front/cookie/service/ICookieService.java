package com.skycloud.management.portal.front.cookie.service;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.front.cookie.entity.TCookieVO;


public interface ICookieService {
	/**
	 * 增加或修改一个Cookie
	 * @param key
	 * @param value
	 * @return
	 */
	public boolean saveOrUpdateCookie(TCookieVO cookie) throws SQLException;

	/**
	 * 根据Cookie Key获取Cookie
	 * @return TCookie
	 */
	public List<TCookieVO> getCookieByKey(String key);

	/**
	 * 根据Cookie 删除Cookie
	 * @return boolean
	 */
	public boolean deleteCookieByKey(String key);
	/**
	 * 根据Cookie value获取Cookie
	 * @param valueStr
	 * @return
	 */
	public List<TCookieVO> getCookieByValue(String valueStr);
}
