package com.skycloud.management.portal.front.customer.service;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;





public interface IAdminService {
	/** 验证用户用户名和密码是否匹配 */
	public TUserBO checkUser(String username, String password);
	/** 更新公司信息 */
	public int insertUserAndCompany(TUserBO user,TCompanyInfo tcompanyinfo) throws Exception;
	public int saveOrUpadate(TCompanyInfo tcompanyinfo) throws Exception;
	public TCompanyInfo findCompanyById(int id)throws Exception;
	public TUserBO findAdminById(int id)throws Exception;
	public int savePwd(TUserBO user)throws Exception;

	public int saveDynPwd(TUserBO user) throws Exception;
	public int disabledAdmin(int userId);
	public TUserBO findAdminByAccount(String account);
	public TUserBO checkUser(String username , String password,int deptId);
	public int updateCompanyInfo (TCompanyInfo tcompanyinfo)throws Exception;
	public int updateUserAndCompany(TUserBO user,TCompanyInfo tcompanyinfo)throws Exception;
	public int insertUserAndCompanyInfo(TUserBO user, TCompanyInfo tcompanyinfo) throws Exception;
	public TCompanyInfo findCompInfoById(Integer companyId)throws Exception;
	public int updateCompany(TUserBO user, TCompanyInfo tcompanyinfo)throws Exception;
}
