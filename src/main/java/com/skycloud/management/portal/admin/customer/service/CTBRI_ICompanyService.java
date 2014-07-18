package com.skycloud.management.portal.admin.customer.service;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyFile;
import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyInfo;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.Page;

public interface CTBRI_ICompanyService {
 
	public CTBRI_TCompanyInfo findById(int id);
	public CTBRI_TCompanyInfo findByAdminId(Integer id);
	public CTBRI_TCompanyInfo getCompanyInfoByDomain(String domain);
	public void deleteCompany(Integer compid,String taskId)throws Exception;
	
	/*获取所有公司信息*/
	public List<CTBRI_TCompanyInfo> findAllCompanyInfo();
	public List<CTBRI_TCompanyInfo> findApprovalCompanies();
	public void saveOrUpdate(CTBRI_TCompanyInfo company)throws Exception;
	
	public void update(CTBRI_TCompanyInfo company,boolean pass)throws Exception;
		/**
	 * 审核更新
	 * @param companyId 公司id
	 * @param pass 复审是否通过
	 * @param note 复审注释
	 */
	public int updateComInfoCheck(Integer userId, boolean pass, String note,String activeFlag);
	
	/**
	 * 查询公司
	 * @param company
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public List<CTBRI_TCompanyInfo> search(CTBRI_TCompanyInfo company, PageVO page);
	
	public List<CTBRI_TCompanyInfo> findCompany(PageVO page,CTBRI_TCompanyInfo company,Date startdate , Date enddate);
	
	@SuppressWarnings("rawtypes")
	public Page<CTBRI_TCompanyInfo> findByName(String name);
	
	public int getCompanyInfoCount(CTBRI_TCompanyInfo company);
	public void saveCompFile(Integer companyId, String filePath,
			String uploadFileName); 
	public List<CTBRI_TCompanyFile> getPicUrls(Integer companyId, String fileType);
	
	public int insertCompanyInfo(CTBRI_TCompanyInfo tcompanyinfo)throws SQLException;
	public int updateCompanyInfo(CTBRI_TCompanyInfo tcompanyinfo)throws SQLException;
	//fix bug 5008
	public void deleteCompany(Integer userId, Integer compid) throws Exception;
	public int updateCompanyUserState(int state, int id) throws SQLException;

}
