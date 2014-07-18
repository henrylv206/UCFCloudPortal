package com.skycloud.management.portal.front.cookie.service.impl;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import com.skycloud.management.portal.front.cookie.dao.impl.CookieDAOImpl;
import com.skycloud.management.portal.front.cookie.entity.TCookieVO;
import com.skycloud.management.portal.front.cookie.service.ICookieService;

public class CookieServiceImpl implements ICookieService {
	private CookieDAOImpl cookieDAO;



	public CookieDAOImpl getCookieDAO() {
		return cookieDAO;
	}

	public void setCookieDAO(CookieDAOImpl cookieDAO) {
		this.cookieDAO = cookieDAO;
	}

	@Override
	public boolean saveOrUpdateCookie(TCookieVO cookie) throws SQLException {
			cookie.setUpdateTime(new Timestamp(System.currentTimeMillis()));
			cookieDAO.saveOrUpdate(cookie);
			return true;
	}

	@Override
	public List<TCookieVO> getCookieByKey(String key) {
			return cookieDAO.getCookieByKey(key);
	}

	@Override
	public boolean deleteCookieByKey(String key) {
		 cookieDAO.deleteCookieByKey(key);
		 return true;
	}

	@Override
    public List<TCookieVO> getCookieByValue(String valueStr) {
	    return cookieDAO.getCookieByValue(valueStr);
    }


}
