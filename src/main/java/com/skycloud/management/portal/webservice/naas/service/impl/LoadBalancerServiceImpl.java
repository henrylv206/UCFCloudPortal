package com.skycloud.management.portal.webservice.naas.service.impl;

import com.skycloud.management.portal.webservice.naas.dao.ILoadBalancerDao;
import com.skycloud.management.portal.webservice.naas.service.ILoadBalancerService;

public class LoadBalancerServiceImpl implements ILoadBalancerService {
	private ILoadBalancerDao loadBalancerDao;

	@Override
	public String listLoadBalancerRules(int publicIpId) {
		return loadBalancerDao.listLoadBalancerRules(publicIpId);
	}

	@Override
	public String createLoadBalancerRule(String name, String description, String publicIpId,
			int publicPort, int privatePort, String algorithm) {
		return loadBalancerDao.createLoadBalancerRule(name, description, publicIpId, publicPort,
				privatePort, algorithm);
	}

	@Override
	public String deleteLoadBalancerRule(int id) {
		return loadBalancerDao.deleteLoadBalancerRule(id);
	}

	@Override
	public String assignToLoadBalancerRule(int id, String vmIds) {
		return loadBalancerDao.assignToLoadBalancerRule(id, vmIds);
	}

	@Override
	public String updateLoadBalancerRule(int id, String name, String algorithm,
			String description) {
		return loadBalancerDao.updateLoadBalancerRule(id, name, algorithm, description);
	}

	@Override
	public String removeFromLoadBalancerRule(int id, String vmIds) {
		return loadBalancerDao.removeFromLoadBalancerRule(id, vmIds);
	}

	@Override
	public String listLoadBalancerRuleInstances(int id) {
		return loadBalancerDao.listLoadBalancerRuleInstances(id);
	}

	@Override
	public void setDataType(String dataType) {
		loadBalancerDao.setDataType(dataType);
	}

	public ILoadBalancerDao getLoadBalancerDao() {
		return loadBalancerDao;
	}

	public void setLoadBalancerDao(ILoadBalancerDao loadBalancerDao) {
		this.loadBalancerDao = loadBalancerDao;
	}
	
}
