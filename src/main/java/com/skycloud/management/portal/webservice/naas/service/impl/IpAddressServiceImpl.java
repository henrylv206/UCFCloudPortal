package com.skycloud.management.portal.webservice.naas.service.impl;

import com.skycloud.management.portal.webservice.naas.dao.IIpAddressDao;
import com.skycloud.management.portal.webservice.naas.service.IIpAddressService;

/**
 * 
 * @author liujijun
 * @since Feb 4, 2012
 * @version 1.0
 */
public class IpAddressServiceImpl implements IIpAddressService {
	private IIpAddressDao ipAddressDao;
	private String dataType = "json";

	@Override
	public String listPublicIpAddress(String ipAddress) {
		return ipAddressDao.listPublicIpAddress(ipAddress);
	}

	@Override
	public String listAvailableIpAddresses() {
		return ipAddressDao.listAvailableIpAddresses();
	}

	@Override
	public boolean isAvailableIpAddress(String ipAddress) {
		return ipAddressDao.isAvailableIpAddress(ipAddress);
	}

	public IIpAddressDao getIpAddressDao() {
		return ipAddressDao;
	}

	public void setIpAddressDao(IIpAddressDao ipAddressDao) {
		this.ipAddressDao = ipAddressDao;
	}

	public String getDataType() {
		return dataType;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}

	@Override
	public String associateIpAddress(int zoneId, int domainId, String account) {
		return ipAddressDao.associateIpAddress(zoneId,domainId,account);
	}



}
