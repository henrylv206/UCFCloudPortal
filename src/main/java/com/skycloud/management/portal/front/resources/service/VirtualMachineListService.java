/**
 * 2011-12-13  下午03:11:13  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.action.vo.VirtualMachineMonitorVO;

/**
 * @author shixq
 * @version $Revision$ 下午03:11:13
 */
public interface VirtualMachineListService {
  /**
   * 查询Elaster Api返回状态
   * @param list
   * @return
   * @throws SCSException 
   */
  List<ResourcesVO> instanceInfoList4Elaster(List<ResourcesVO> list) throws SCSException;
  /**
   * 查询Elaster Api返回所有信息
   * @param list
   * @return
   * @throws SCSException
   */
  VirtualMachineMonitorVO instanceStatisticsInfoList4Elaster(String instanceId,int resourcePoolsId) throws SCSException;
  //获取默认的IP fix bug 2704
  List<ResourcesVO> instanceInfoList4ElasterDefault(List<ResourcesVO> list)
		throws SCSException;
}
