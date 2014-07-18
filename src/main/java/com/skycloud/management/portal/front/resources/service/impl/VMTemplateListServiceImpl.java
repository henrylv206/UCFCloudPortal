/**
 * 2011-11-28  下午04:55:22  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateVMBO;
import com.skycloud.management.portal.front.resources.service.VMTemplateListService;

/**
 * @author shixq
 * @version $Revision$ 下午04:55:22
 */
public class VMTemplateListServiceImpl implements VMTemplateListService {

  private static Log log = LogFactory.getLog(VMTemplateListServiceImpl.class);

  private IVMTemplateDao VMTemplateDao;

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.front.resources.service.VMTemplateListService
   * #queryVMTemplateCPUAndMemoryAvailableList()
   */
  @Override
  public List<TemplateVMBO> queryVMTemplateCPUAndMemoryAvailableList(String type) throws SCSException {
    List<TemplateVMBO> list = null;
    try {
      list = VMTemplateDao.queryVMTemplateCpuAndMemAvailableList(type);
    } catch (SCSException e) {
      log.error("vmTemplateDao is ==" + VMTemplateDao);
      throw e;
    }
    return list;
  }

  public IVMTemplateDao getVMTemplateDao() {
    return VMTemplateDao;
  }

  public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
    VMTemplateDao = vMTemplateDao;
  }

}
