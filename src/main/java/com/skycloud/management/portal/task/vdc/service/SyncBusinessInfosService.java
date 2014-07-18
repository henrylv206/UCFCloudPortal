package com.skycloud.management.portal.task.vdc.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.task.vdc.service.bo.RespInfosBO;

public interface SyncBusinessInfosService {

	/**
	 * 更新实例表信息和异步job表信息
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:17:56
	 */
	void updateInstanceAndAsyncInfo(RespInfosBO respBO) throws SCSException;
	
	/**
	 * 更新实例关系表信息，用于块存储挂载卸载
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:18:18
	 */
	void updateIriInfo(RespInfosBO respBO) throws SCSException;
	/**
	 * 更新用户快照表信息
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:18:53
	 */
	void updateUserSnapShotInfo(RespInfosBO respBO) throws SCSException;
	
	/**
	 * 更新异步vdcJob表信息
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:19:09
	 */
	void updateAsyncJobVDCInfo(RespInfosBO respBO) throws SCSException;
	
	/**
	 * 更新模板表信息
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:19:34
	 */
	void updateVmTemplateInfo(RespInfosBO respBO) throws SCSException;
	
	/**
	 * 更新实例和实例关系表信息
	 * @param respBO
	 * @throws SCSException
	 * 创建人：   冯永凯    
	 * 创建时间：2012-3-19  上午09:19:50
	 */
	void updateInstanceAndIriInfo(RespInfosBO respBO) throws SCSException;
	
}
