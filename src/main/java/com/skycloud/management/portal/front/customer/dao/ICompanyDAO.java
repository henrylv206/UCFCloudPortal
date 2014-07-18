package com.skycloud.management.portal.front.customer.dao;

import java.util.Date;
import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;



public interface ICompanyDAO {

	public int saveOrUpdateComp(TCompanyInfo tcompanyinfo)throws Exception;	

	public TCompanyInfo getTCompanyInfoById(int id);
	
	public int updateCompany(TCompanyInfo tcompanyinfo)throws Exception;

	public int saveComp(TCompanyInfo tcompanyinfo) throws Exception;

	public int insertCompID(int user, int compId) throws Exception;

	public TCompanyInfo getTCompInfoById(int id) throws Exception;

	public int updateCompInfo(TCompanyInfo tcompanyinfo) throws Exception;	
	
}
