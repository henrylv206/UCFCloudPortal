/**
 * 2011-11-28  下午04:17:58  $Id:$shixq
 */
package com.skycloud.management.portal.front.resources.dao;

import java.util.List;
import java.util.Map;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateMCBO;

/**
 * 
 * @author shixq
 * @version $Revision$ 下午04:17:01
 */
public interface HCTemplateDao {

  /**
   * 查询可用小型机模板CPU、MEM列表
   * 
   * @param type
   * @return
   * @throws Exception
   */
  List<TemplateMCBO> queryHCTemplateCpuAndMemAvailableList(String type) throws SCSException;

  List<Map<String,Object>> queryVMServer();

}
