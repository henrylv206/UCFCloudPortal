package com.skycloud.management.portal.service;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;

/**
 * 调用elaster的接口集中写在此类
 * @author zhanghuizheng
 *
 */
public interface IElasterSerivce {
	//1.3功能，支持多资源池
	//根据资源池id和zoneid查询elaster上该资源池下所有社区模板，即该资源池下所有操作系统
	List<Map<String, String>> listTemplates(Integer resourcePoolsId,int zoneId) throws Exception;
	//查询所有资源池下的操作系统
	List<Map<String, String>> listTemplatesForAllPools(List<TResourcePoolsBO> poollist);
	//根据操作系统id查询elaster上的操作系统
	Map getTemplateById(int osId,Integer resourcePoolsId);
	//查询某个资源池下的cluster
	Map<String, String> listClusters(String hypervisortype,Integer resourcePoolsId);
	//查询elaster的listConfigurations接口
	Map<String, String> listConfigurations(Integer resourcePoolsId);
	//在某个资源池下根据操作系统id查询所有的存储类型
	String getStoreTypesByOsId(int osId, Integer resourcePoolsId);
	//在某个资源池下根据clusterid查询存储类型
	List<Map<String, String>> getStoreTypeByClusterId(long clusterid,Integer resourcePoolsId);
	//自服务门户查询存储类型
	List<Map> getAllStoreType(Integer resourcePoolsId);
	//运营管理平台-资源模板管理-虚拟硬盘资源模板管理
	List<Map> getAllStoreTypeByZoneId(Integer resourcePoolsId,int zoneId) ;

}
