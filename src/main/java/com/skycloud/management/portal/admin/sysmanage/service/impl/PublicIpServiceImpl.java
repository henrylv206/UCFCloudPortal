package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.util.List;

import com.skycloud.management.portal.admin.sysmanage.entity.TPublicIPBO;
import com.skycloud.management.portal.admin.sysmanage.dao.IPublicIPDao;
import com.skycloud.management.portal.admin.sysmanage.service.IPublicIPService;
import com.skycloud.management.portal.exception.SCSException;

public class PublicIpServiceImpl implements IPublicIPService {
	IPublicIPDao publicIPDao;
	
	

	@Override
	public int insertPublicIP(TPublicIPBO publicIP) throws Exception {
		return publicIPDao.save(publicIP);
	}

	@Override
	public int updatePublicIP(TPublicIPBO publicIP) throws Exception {
		return publicIPDao.update(publicIP);
	}

	@Override
	public List<TPublicIPBO> queryPublicIP(int curPage, int pageSize,String searchKey)
			throws Exception {
		return publicIPDao.listPublicIPs(curPage, pageSize,searchKey);
	}

	@Override
	public TPublicIPBO queryPublicIPById(int publicIPId) throws SCSException {
		return publicIPDao.queryPublicIPById(publicIPId);
	}

	@Override
	public int deletePublicIP(int publicIPId) throws SCSException {
		return publicIPDao.delete(publicIPId);
	}

	public IPublicIPDao getPublicIPDao() {
		return publicIPDao;
	}

	public void setPublicIPDao(IPublicIPDao publicIPDao) {
		this.publicIPDao = publicIPDao;
	}

	@Override
	public int countPublicIPs(String searchKey) throws Exception {
		return publicIPDao.countPublicIPs(searchKey);
	}

	@Override
	public int searchIPByIpAddress(String ipAddress) throws SCSException {
		return publicIPDao.searchIPByIpAddress(ipAddress);
	}

	@Override
	public List<TPublicIPBO> listPublicIPByServiceProvider(int serviceProvider) {
		return publicIPDao.listPublicIPByServiceProvider(serviceProvider);
	}
	
	

}
