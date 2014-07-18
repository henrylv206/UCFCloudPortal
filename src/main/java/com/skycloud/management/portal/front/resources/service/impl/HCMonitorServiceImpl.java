package com.skycloud.management.portal.front.resources.service.impl;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.MiniComputerMonitorVO;
import com.skycloud.management.portal.front.resources.dao.HCMonitorDao;
import com.skycloud.management.portal.front.resources.service.HCMonitorService;

public class HCMonitorServiceImpl implements HCMonitorService {
  private static final Logger logger = Logger.getLogger(VirtualMachineMonitorServiceImpl.class);

  private HCMonitorDao hcMonitorDao;

  /**
   * 
   * @param serverName
   * @return
   */
  public MiniComputerMonitorVO getMonitorInfo(int instanceId) {
    logger.info("get the monitor information for " + instanceId);

    MiniComputerMonitorVO vo = new MiniComputerMonitorVO();
    String resultStr = hcMonitorDao.getMonitorInfo(instanceId);
    JSONObject result = JSONObject.fromObject(resultStr);
    if (result.isEmpty()) {
      return vo;
    }
    if (result.containsKey("monitorInfo")) {
      JSONObject resultMoniror = result.getJSONObject("monitorInfo");
      if (resultMoniror.isEmpty()) {
        return vo;
      }
      vo.setCpuused(resultMoniror.getString("cpuUtili"));
      vo.setMemused(resultMoniror.getString("memUtili"));
      vo.setNetworkkbsread(resultMoniror.getString("netRecvSpeed"));
      vo.setNetworkkbswrite(resultMoniror.getString("netSendSpeed"));
      vo.setRootdiskkbsread(resultMoniror.getString("byteIoRead"));
      vo.setRootdiskkbswrite(resultMoniror.getString("byteIoWrite"));
    }
    return vo;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.front.resources.service.HCMonitorService
   * #queryInstanceInfo4Elaster(java.lang.String)
   */
  @Override
  public String queryInstanceInfo4Elaster(int instanceId) throws SCSException {
    return hcMonitorDao.getHCInfo(instanceId);
  }

  public HCMonitorDao getHcMonitorDao() {
    return hcMonitorDao;
  }

  public void setHcMonitorDao(HCMonitorDao hcMonitorDao) {
    this.hcMonitorDao = hcMonitorDao;
  }

}
