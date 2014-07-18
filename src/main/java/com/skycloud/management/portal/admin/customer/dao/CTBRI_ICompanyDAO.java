package com.skycloud.management.portal.admin.customer.dao;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyFile;
import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyInfo;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;

/*import com.skycloud.telecloud.customer.entity.TCompanyInfo;
import com.skycloud.telecloud.util.Page;*/

public  interface CTBRI_ICompanyDAO{


	public void saveOrUpdateComp(CTBRI_TCompanyInfo tcompanyinfo)throws Exception;
	
	public void update(CTBRI_TCompanyInfo tcompanyinfo,boolean pass);

	public void delCTBRI_TCompanyInfo(int id);
	
	public CTBRI_TCompanyInfo getCTBRI_TCompanyInfoById(int  userId);

	public List getAllCompanyInfo();
	
	public CTBRI_TCompanyInfo findByDomain(String domain);
	
	public CTBRI_TCompanyInfo findByAdminId(Integer id);
	
	@SuppressWarnings("rawtypes")
	public List<CTBRI_TCompanyInfo> search(CTBRI_TCompanyInfo company, PageVO page);
	
	public List<CTBRI_TCompanyInfo> findCompany(PageVO page, CTBRI_TCompanyInfo company,Date startdate, Date enddate);
	
	public CTBRI_TCompanyInfo findCTBRI_TCompanyInfoById(Integer id);
	
	public List<CTBRI_TCompanyInfo> findApprovalCompanies();

	public int getCompanyInfoCount(CTBRI_TCompanyInfo compInfo);

	public boolean saveCompFile(Integer companyId, String filePath, String uploadFileName);

	public List<CTBRI_TCompanyFile> getPicUrls(Integer companyId, String fileType);
	
	public int saveCompany(CTBRI_TCompanyInfo tcompanyinfo)throws SQLException;
	public int updateCompany(CTBRI_TCompanyInfo tcompanyinfo)throws SQLException;
	public int insertCompID(int userId ,int compId,String newPwd)throws SQLException;
	public int updatePwd(int userId ,String newPwd)throws SQLException;

	public int updateUser(TUserBO user) throws SQLException;

	public int updateEmail(int userId, String email) throws SQLException;

	public TUserBO findUserInfoByCompId(Integer compId) throws SQLException;

	public void delCTBRI_TCompanyInfo(int userId, int id);
	//fix bug 5008
	public int updateCompanyUserState(int state, int userId) throws SQLException;

}
