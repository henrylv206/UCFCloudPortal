package com.skycloud.management.portal.front.sg.dao;

import java.util.List;

import com.skycloud.management.portal.front.sg.entity.DeviceInfo;
import com.skycloud.management.portal.front.sg.entity.DeviceType;

public interface IDeviceInfoDao {

  /**
   * 通过id查询设备详情
   * 
   * @param id
   * @return
   * @throws Exception
   */
  public DeviceInfo getDeviceInfoById(int id) throws Exception;

  public List<DeviceInfo> getDeviceInfoByType(DeviceType type);
}
