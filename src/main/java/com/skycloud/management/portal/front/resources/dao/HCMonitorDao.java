package com.skycloud.management.portal.front.resources.dao;

public interface HCMonitorDao {

  String getMonitorInfo(int instanceId);

  String getHCInfo(int instanceId);
}
