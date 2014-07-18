package com.skycloud.management.portal.front.resources.service;

import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.MiniComputerMonitorVO;

public interface HCMonitorService {

  MiniComputerMonitorVO getMonitorInfo(int instanceId);

  String queryInstanceInfo4Elaster(int instanceId) throws SCSException;

}
