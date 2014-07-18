/**
 * 2012-1-13  上午09:56:29  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.service.impl;

import java.util.List;

import com.skycloud.management.portal.admin.template.util.TemplateUtils;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.dao.ResouceServiceInstanceOperateDao;
import com.skycloud.management.portal.front.resources.service.MonitorInstanceService;

/**
 * @author shixq
 * @version $Revision$ 上午09:56:29
 */
public class MonitorInstanceServiceImpl implements MonitorInstanceService {
  private ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao;

  
  private List<ResourcesVO> retrunNetWorkDescList(List<ResourcesVO> list){
    for (ResourcesVO vo : list) {
      String networkDesc = vo.getNetwork_desc();
      if (networkDesc != null && !"".equals(networkDesc)) {
        vo.setNetwork_desc(TemplateUtils.getResourceTypesByCodes(networkDesc));
      }
    }
    return list;
  }
  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.front.resources.service.MonitorInstanceService
   * #queryResouceServiceInstanceInfoCount(com.skycloud.management.portal.front.
   * resources.action.vo.ResourcesQueryVO)
   */
  @Override
  public int queryResouceServiceInstanceInfoCount(ResourcesQueryVO rqvo) throws SCSException {
    return resouceServiceInstanceOperateDao.queryResouceServiceInstanceInfoCount(rqvo);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.front.resources.service.MonitorInstanceService
   * #
   * queryResouceServiceInstanceInfo(com.skycloud.management.portal.front.resources
   * .action.vo.ResourcesQueryVO)
   */
  @Override
  public List<ResourcesVO> queryResouceServiceInstanceInfo(ResourcesQueryVO rqvo) throws SCSException {
    List<ResourcesVO> list = resouceServiceInstanceOperateDao.queryResouceServiceInstanceInfo(rqvo);
    return retrunNetWorkDescList(list);
  }
  
  @Override
  public List<ResourcesVO> queryResouceServiceInstanceInfoBeforApprove(ResourcesQueryVO rqvo) throws SCSException {
    List<ResourcesVO> list = resouceServiceInstanceOperateDao.queryResouceServiceInstanceInfoBeforApprove(rqvo);
    return retrunNetWorkDescList(list);
  }

  public ResouceServiceInstanceOperateDao getResouceServiceInstanceOperateDao() {
    return resouceServiceInstanceOperateDao;
  }

  public void setResouceServiceInstanceOperateDao(ResouceServiceInstanceOperateDao resouceServiceInstanceOperateDao) {
    this.resouceServiceInstanceOperateDao = resouceServiceInstanceOperateDao;
  }

}
