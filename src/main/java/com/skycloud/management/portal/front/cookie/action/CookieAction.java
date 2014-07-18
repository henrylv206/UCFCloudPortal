package com.skycloud.management.portal.front.cookie.action;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.front.cookie.entity.TCookieVO;
import com.skycloud.management.portal.front.cookie.service.ICookieService;


public class CookieAction extends BaseAction{
	private TCookieVO cookie;
	private ICookieService cookieSerivce;
	private String key;
	private String state;

	public TCookieVO getCookie() {
		return cookie;
	}
	public void setCookie(TCookieVO cookie) {
		this.cookie = cookie;
	}
	public ICookieService getCookieSerivce() {
		return cookieSerivce;
	}
	public void setCookieSerivce(ICookieService cookieSerivce) {
		this.cookieSerivce = cookieSerivce;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	/**
	 * 增加或者修改Cookie;
	 * @return
	 */
	public String saveOrUpdateCookie(){
		try {
			if(cookieSerivce.saveOrUpdateCookie(cookie)){
				state = "true";
			}else{
				state = "false";
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 根据Key获得Cookie;
	 * @return
	 */
	public String getCookieByKey(){
		try {
			List<TCookieVO> list = cookieSerivce.getCookieByKey(key);
			if(list.size()>0){
				cookie = list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}
	/**
	 * 根据Key删除Cookie;
	 * @return
	 */
	public String deleteCookieByKey(){
		try {
			if(cookieSerivce.deleteCookieByKey(key)){
				state = "true";
			}else{
				state = "fasle";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SUCCESS;
	}

}
