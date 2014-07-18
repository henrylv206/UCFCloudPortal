package com.skycloud.management.portal.webservice.databackup.dao.impl;

import java.util.List;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.webservice.databackup.dao.IDBTemplateDao;
import com.skycloud.management.portal.webservice.databackup.jdbc.BaseJdbcMysqlDao;
import com.skycloud.management.portal.webservice.databackup.po.DBTemplate;

public class DBTemplateDao extends BaseJdbcMysqlDao implements IDBTemplateDao {

	@Override
	public DBTemplate queryDataBackUpTemplateVmById(int templateId)
			throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID id,TEMPLATE_DESC description,STORAGE_SIZE size,CODE name FROM T_SCS_TEMPLATE_VM WHERE TYPE = 4 AND STATE = 2 AND ID = "+templateId;
		return this.queryForObject(sql, DBTemplate.class);
	}

	@Override
	public List<DBTemplate> queryDataBackUpTemplateVmList() throws DataAccessException {
		// TODO Auto-generated method stub
		String sql = "SELECT ID id,TEMPLATE_DESC description,STORAGE_SIZE size,CODE name FROM T_SCS_TEMPLATE_VM WHERE TYPE = 4 AND STATE = 2 ";
		return this.queryForList(sql, DBTemplate.class);
	}

}
