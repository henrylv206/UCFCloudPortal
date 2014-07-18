package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import org.springframework.http.ResponseEntity;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.entity.TVmInfo;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public interface NasResourceService {


	public  List<ResourcesVO> queryResouceInstanceInfoIncludeMc(ResourcesQueryVO rqvo) throws Exception ;


	public  ResponseEntity<String> accessToNasResource(TInstanceInfoBO infobo,String instanceIp) throws Exception;


	public String getNasResourceByDirId(TInstanceInfoBO infobo) throws Exception;

	public void deleteaccessFromNasResource(TInstanceInfoBO infobo,String dirId) throws Exception;

	void insertDestroyNAS(int instanceId, TUserBO user, String reason, int serviceID) throws Exception;

	String getNasResourceDirInfo(TInstanceInfoBO infobo) throws Exception;
	/**
	 * 特殊模板申请创建申请模板
	 * 
	 * @param vminfo
	 * @return TTemplateVMBO
	 */
	public TTemplateVMBO creatSpecalIpSanTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

	public TTemplateVMBO creatSpecalNasTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;

	public TTemplateVMBO creatSpecalObjectStorageTemplate(TVmInfo vminfo, TUserBO user, TTemplateVMBO template, Product product) throws SCSException;
}
