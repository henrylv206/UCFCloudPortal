package com.skycloud.management.portal.front.instance.dao;

import org.springframework.dao.DataAccessException;

import com.skycloud.management.portal.front.instance.entity.AsynJobInfoError;

public interface IAsyncJobInfoErrorDao {
    /**
     * 插入任务出错信息表
     * @param asyError
     * @throws DataAccessException
     * 创建人：  刘江宁   
     * 创建时间：2011-11-8  下午02:31:30
     */
	public void insertJobInfoError (AsynJobInfoError asyError) throws DataAccessException;
}
