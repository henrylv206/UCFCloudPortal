package com.skycloud.management.portal.front.resources.service;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.instance.entity.Iri;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.VolumeResumeVO;

public interface IVolumeOperateService {

	public void insertAttachVolumeOperate(Iri iri,TInstanceInfoBO infobo) throws Exception;
	
	public void insertDiskAttachVolumeOperate(TInstanceInfoBO infobo) throws Exception;
	
	public void insertCreatesnapshot(TInstanceInfoBO infobo,String id) throws Exception;
	
	public void insertDeletesnapshot(VolumeResumeVO volumevo,TInstanceInfoBO infobo) throws Exception;
	
	public void insertDetachVolumeOperate(Iri iri,TInstanceInfoBO infobo) throws Exception;
	
	public void insertResumeSnapshot(VolumeResumeVO volumevo,TInstanceInfoBO infobo) throws Exception;
	
	public void insertDestroyVolume(TInstanceInfoBO infobo,String reason,TUserBO user) throws Exception;
	public void insertDestroyIPSan(TInstanceInfoBO infobo, String reason,TUserBO user, int serviceId) throws Exception;
	public void insertDestroyIPSan4(TInstanceInfoBO infobo, String reason,TUserBO user, int serviceId) throws Exception;
	
	public void insertDiskDestroy(TInstanceInfoBO infobo,String reason,TUserBO user, int serviceId) throws Exception;
	public void insertDiskDestroy4(TInstanceInfoBO infobo,String reason,TUserBO user, int serviceId) throws Exception;

	void insertDetachVolumeOperateAuto(Iri iri, TInstanceInfoBO infobo)
			throws Exception;
	
}
