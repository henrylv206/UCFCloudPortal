/**
 * 2011-11-28  下午04:54:01  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service;

import java.util.List;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;

/**
 * @author shixq
 * @version $Revision$ 下午04:54:01
 */
public interface VMTemplateListService {
  /**
   * 查询所有虚拟机可用模板的CPU、内存列表
   * 
   * @param type
   * @return
   * @throws SCSException 
   */
  List<TemplateVMBO> queryVMTemplateCPUAndMemoryAvailableList(String type) throws SCSException;

}
