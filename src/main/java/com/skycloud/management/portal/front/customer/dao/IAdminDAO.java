package com.skycloud.management.portal.front.customer.dao;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;

public interface IAdminDAO {
	public TUserBO checkUser(String username, String password);
	public TUserBO findAdminById(int id);
	public int savePwd(TUserBO user);

	public int saveDynPwd(TUserBO user);
	public int disabledAdmin(int userId);
	public TUserBO findAdminByAccount(String account);
	public TUserBO findPazzWithState(String username, String email,int deptId)throws Exception;
	public int updatePassword(String account,String email,String pwd);
	/**
	 * 自服务用户登录
	 * @param deptId:用户组 ，区分共有云或私有云用户
	 * */
	public TUserBO checkUser(final String username,final String password,final int deptId);
}
