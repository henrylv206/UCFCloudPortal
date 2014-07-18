package com.skycloud.management.portal.admin.customer.dao;


import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyAdmin;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;

public interface CTBRI_IAdminDAO {
	
	public CTBRI_TCompanyAdmin findAdminByComid(Integer comid);
	
	public TUserBO findAdminById(int userId);
}
