package com.skycloud.management.portal.admin.customer.service;

import com.skycloud.management.portal.admin.customer.entity.CTBRI_TCompanyAdmin;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;




/*import com.skycloud.telecloud.customer.entity.TCompUgroup;
import com.skycloud.telecloud.customer.entity.TCompanyAdmin;
import com.skycloud.telecloud.customer.entity.TCompanyInfo;
import com.skycloud.telecloud.customer.vo.compGroupInfo;
import com.skycloud.telecloud.util.Page;*/

public interface CTBRI_IAdminService {
	
	public CTBRI_TCompanyAdmin findAdminByComid(Integer comid);
	
	public TUserBO findAdminById(int userId);


}
