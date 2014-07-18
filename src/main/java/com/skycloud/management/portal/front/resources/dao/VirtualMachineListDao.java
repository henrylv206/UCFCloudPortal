/**
 * 2011-11-23 下午01:24:29 $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.dao;

import java.util.List;

import net.sf.json.JSONObject;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

/**
 * @author shixq
 * @version $Revision$ 下午04:17:11
 */
public interface VirtualMachineListDao {

	/**
	 * 查询所有虚拟机集合
	 * 
	 * @return
	 * @throws Exception
	 */
	@Deprecated
	List<TemplateVMBO> queryVirtualMachineList() throws Exception;

	/**
	 * 查询Elaster Api返回所有信息
	 * 
	 * @param list
	 * @return
	 * @throws SCSException
	 */
	JSONObject instanceInfoList4Elaster(String instanceId, int resourcePoolsID) throws SCSException;

	List<ResourcesVO> queryVMList(int resourcePoolsID) throws SCSException;

	/**
	 * 查询Elaster Api返回所有信息
	 * 
	 * @param list
	 * @return
	 * @throws SCSException
	 */
	JSONObject instanceInfoList4Elaster(int resourcePoolsID) throws SCSException;

}
