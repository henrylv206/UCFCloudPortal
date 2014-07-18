package com.skycloud.management.portal.front.resources.dao;

public interface IObjectStorageDao {
	/**
	 * 查询对象存储用户状态
	 * @param username
	 * @return
	 * @throws Exception
	 */
	public int getOsUserStatus(String username) throws Exception ;

}
