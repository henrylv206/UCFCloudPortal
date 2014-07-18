package com.skycloud.management.portal.front.resources.dao;

public interface VirtualMachineMonitorDao {

	/**
	 * 
	 * @param serverName
	 * @return
	 */
	public String getMonitorInfo(String instanceId);
}
