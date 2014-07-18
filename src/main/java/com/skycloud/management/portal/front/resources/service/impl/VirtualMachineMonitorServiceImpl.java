package com.skycloud.management.portal.front.resources.service.impl;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.front.resources.dao.VirtualMachineMonitorDao;
import com.skycloud.management.portal.front.resources.service.VirtualMachineMonitorService;

public class VirtualMachineMonitorServiceImpl implements VirtualMachineMonitorService{
	
	private static final Logger logger = Logger.getLogger(VirtualMachineMonitorServiceImpl.class);
	private VirtualMachineMonitorDao virtualMachineMonitorDao;

	/**
	 * 
	 * @param serverName
	 * @return
	 */
	public String getMonitorInfo(String instanceId) {
		logger.info("get the monitor information for "+instanceId);
		return virtualMachineMonitorDao.getMonitorInfo(instanceId);
	}

	public VirtualMachineMonitorDao getVirtualMachineMonitorDao() {
		return virtualMachineMonitorDao;
	}

	public void setVirtualMachineMonitorDao(VirtualMachineMonitorDao virtualMachineMonitorDao) {
		this.virtualMachineMonitorDao = virtualMachineMonitorDao;
	}

}
