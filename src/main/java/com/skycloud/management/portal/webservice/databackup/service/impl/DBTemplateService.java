package com.skycloud.management.portal.webservice.databackup.service.impl;

import java.util.List;

import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.webservice.databackup.dao.IDBTemplateDao;
import com.skycloud.management.portal.webservice.databackup.po.DBTemplate;
import com.skycloud.management.portal.webservice.databackup.service.IDBTemplateService;

public class DBTemplateService extends BaseService implements
		IDBTemplateService {

	private IDBTemplateDao dbtemplateDao;
	@Override
	public DBTemplate queryDataBackUpTemplateVmById(int templateId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbtemplateDao.queryDataBackUpTemplateVmById(templateId);
		}catch(ServiceException e){
			logger.error("Execute [TTemplateVMBO] method: queryDataBackUpTemplateVmById Exception:",e);
			throw new ServiceException("根据模板标识获取唯一模板实体异常");
		}
	}

	@Override
	public List<DBTemplate> queryDataBackUpTemplateVmList()
			throws ServiceException {
		// TODO Auto-generated method stub
		try{
			return dbtemplateDao.queryDataBackUpTemplateVmList();
		}catch(ServiceException e){
			logger.error("Execute [TTemplateVMBO] method: queryDataBackUpTemplateVmList Exception:",e);
			throw new ServiceException("获取存储空间模板列表异常");
		}
	}

	public IDBTemplateDao getDbtemplateDao() {
		return dbtemplateDao;
	}

	public void setDbtemplateDao(IDBTemplateDao dbtemplateDao) {
		this.dbtemplateDao = dbtemplateDao;
	}

}
