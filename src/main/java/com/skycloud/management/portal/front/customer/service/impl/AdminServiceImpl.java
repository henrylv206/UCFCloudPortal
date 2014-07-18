package com.skycloud.management.portal.front.customer.service.impl;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.front.customer.dao.IAdminDAO;
import com.skycloud.management.portal.front.customer.dao.ICompanyDAO;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;
import com.skycloud.management.portal.front.customer.service.IAdminService;



public class AdminServiceImpl implements IAdminService {

	private IAdminDAO adminDao;
	private ICompanyDAO companyDao;
	private IUserManageService userService;


	public IUserManageService getUserService() {
		return userService;
	}
	public void setUserService(IUserManageService userService) {
		this.userService = userService;
	}
	public ICompanyDAO getCompanyDao() {
		return companyDao;
	}
	public void setCompanyDao(ICompanyDAO companyDao) {
		this.companyDao = companyDao;
	}
	public IAdminDAO getAdminDao() {
		return adminDao;
	}
	public void setAdminDao(IAdminDAO adminDao) {
		this.adminDao = adminDao;
	}
	/**
	 * 	check Admin Account
	 * */
	@Override
	public TUserBO checkUser(String username , String password){
		TUserBO user = adminDao.checkUser(username, password);
		return user;
	}
	/**
	 * 	check Admin Account
	 * */
	@Override
	public TUserBO checkUser(String username , String password,int deptId){
		TUserBO user = adminDao.checkUser(username, password,deptId);
		return user;
	}

	/**保存一个公司信息*/
	@Override
	public int saveOrUpadate(TCompanyInfo tcompanyinfo)throws Exception {
		return this.companyDao.saveOrUpdateComp(tcompanyinfo);
	}
	/**保存个人及公司信息*/
	public int insertUserAndCompanyInfo(TCompanyInfo tcompanyinfo)throws Exception {
		return this.companyDao.saveComp(tcompanyinfo);
	}
	/**修改一个公司信息*/
	@Override
	public int updateCompanyInfo (TCompanyInfo tcompanyinfo)throws Exception {
		return this.companyDao.updateCompany(tcompanyinfo);
	}

	@Override
	public TCompanyInfo findCompanyById(int id) throws Exception{
		return this.companyDao.getTCompanyInfoById(id);
	}
	public TCompanyInfo findCompInfoById(int id) throws Exception{
		return this.companyDao.getTCompInfoById(id);
	}
	@Override
	public TUserBO findAdminById(int id)throws Exception{
		return adminDao.findAdminById(id);
	}
	@Override
	public int savePwd(TUserBO user) throws Exception{
		return adminDao.savePwd(user);
	}

	@Override
	public int saveDynPwd(TUserBO user) throws Exception {
		return adminDao.saveDynPwd(user);
	}
	@Override
	public int disabledAdmin(int userId){
		return adminDao.disabledAdmin(userId);
	}
	@Override
	public TUserBO findAdminByAccount(String account){
		return adminDao.findAdminByAccount(account);
	}
	@Override
	//to fix 1414
	public int updateUserAndCompany(TUserBO user, TCompanyInfo tcompanyinfo)
	throws Exception {
		if(null!=tcompanyinfo&&tcompanyinfo.getCompId()>0){
			this.updateCompanyInfo(tcompanyinfo);
			//	System.out.println("service:updateUserAndCompany");
		}
		return userService.updateUser(user);
	}
	//	更新selfcare个人信息
	@Override
	public int updateCompany(TUserBO user, TCompanyInfo tcompanyinfo)
	throws Exception {
		int result = -1;
		if(null!=tcompanyinfo&&tcompanyinfo.getCompId()>0){
			result = companyDao.updateCompInfo(tcompanyinfo);
		}
		return result;
	}

	@Override
	public int insertUserAndCompany(TUserBO user, TCompanyInfo tcompanyinfo)
	throws Exception {
		int compId = this.saveOrUpadate(tcompanyinfo);
		user.setCompId(compId);
		return userService.insertUser(user);
	}
	@Override
	public int insertUserAndCompanyInfo(TUserBO user, TCompanyInfo tcompanyinfo) throws Exception {
		// TODO Auto-generated method stub

		int ret = 0;
		int compId = companyDao.saveComp(tcompanyinfo);
		//	System.out.println("Service-compId:"+compId);
		if(compId>0){
			ret = companyDao.insertCompID(user.getId() ,compId);
			user.setCompId(compId);//对于新增企业信息，更新user中的值 fix bug 3301
			//		System.out.println("Service-user.getId():"+user.getId());
		}
		return ret;


	}
	@Override
	public TCompanyInfo findCompInfoById(Integer companyId) throws Exception {
		// TODO Auto-generated method stub
		TCompanyInfo cc = companyDao.getTCompInfoById(companyId);
		//	System.out.println("service:"+companyId);
		return cc;
	}

}

