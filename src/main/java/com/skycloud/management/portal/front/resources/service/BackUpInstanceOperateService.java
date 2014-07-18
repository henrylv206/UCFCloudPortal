package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

/**
 * @author fengyk
 * 创建时间  2012.1.10 14:11:11
 */
public interface BackUpInstanceOperateService {

	
	/**
	 * 查看用户备份服务总数
	 * @param
	 * @return 备份服务信息总数
	 * @throws Exception
	 */
	int queryBackUpInstanceListCount(ResourcesQueryVO rqvo) throws Exception;
	int queryBackUpInstanceListCountBeforApprove(ResourcesQueryVO rqvo) throws Exception;
	int queryInstanceListCountBeforeApprove(ResourcesQueryVO rqvo) throws Exception ;
	
	/**
	 * 查看用户备份服务列表信息
	 * @param
	 * @return 备份服务信息列表
	 * @throws Exception
	 */
	List<ResourcesVO> queryBackUpInstanceList(ResourcesQueryVO rqvo) throws Exception;
	
	/**
	 * 修改用户备份服务信息
	 * @param
	 * @return 
	 * @throws Exception
	 */
	String  insertDirtyReadChangeBackUpInstance(ResourcesModifyVO vmModifyVO,TUserBO user) throws Exception;
	
	/**
	 * 删除用户备份服务信息
	 * @param
	 * @return 
	 * @throws Exception
	 */
	String  insertDirtyReaddeleteBackUpInstance(ResourcesModifyVO vmModifyVO,TUserBO user) throws Exception;
	
	String  insertBackUpDestroy(ResourcesModifyVO vmModifyVO,TUserBO user, int serviceID) throws Exception;
	
	/**
	 * 查看备份及服务有效模板
	 * @param
	 * @return 
	 * @throws Exception 
	 */
	List<TemplateVMBO> queryBackTemplateAvailableList(ResourcesQueryVO rqvo) throws Exception;
}
