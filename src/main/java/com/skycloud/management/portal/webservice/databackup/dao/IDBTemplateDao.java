package com.skycloud.management.portal.webservice.databackup.dao;

import java.util.List;

import org.springframework.dao.DataAccessException;
import com.skycloud.management.portal.webservice.databackup.po.DBTemplate;

public interface IDBTemplateDao {

	/**
	 * 根据模板标识获取唯一模板实体
	 * @param templateId
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-31  下午03:37:34
	 */
	public DBTemplate queryDataBackUpTemplateVmById (int templateId) throws DataAccessException;
	/**
	 * 获取存储空间模板列表
	 * @return
	 * @throws DataAccessException
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-4  下午02:19:40
	 */
	public List<DBTemplate> queryDataBackUpTemplateVmList () throws DataAccessException;
}
