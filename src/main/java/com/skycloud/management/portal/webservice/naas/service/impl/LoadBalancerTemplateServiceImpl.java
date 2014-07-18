package com.skycloud.management.portal.webservice.naas.service.impl;

import java.util.List;

import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.dao.ILoadBalancerTemplateDao;
import com.skycloud.management.portal.webservice.naas.entity.LoadBalancerTemplate;
import com.skycloud.management.portal.webservice.naas.service.ILoadBalancerTemplateService;

/**
 * 
 * @author liujijun
 * @since Feb 3, 2012
 * @version 1.0
 */
public class LoadBalancerTemplateServiceImpl implements ILoadBalancerTemplateService {
	private ILoadBalancerTemplateDao loadBalancerTemplateDao;

	@Override
	public LoadBalancerTemplate getLoadBalancerTemplateById(int id) throws ObjectNotFoundException {
		return loadBalancerTemplateDao.getLoadBalancerTemplateById(id);
	}

	@Override
	public List<LoadBalancerTemplate> listLoadBalancerTemplate() {
		return loadBalancerTemplateDao.listLoadBalancerTemplate();
	}

	public ILoadBalancerTemplateDao getLoadBalancerTemplateDao() {
		return loadBalancerTemplateDao;
	}

	public void setLoadBalancerTemplateDao(ILoadBalancerTemplateDao loadBalancerTemplateDao) {
		this.loadBalancerTemplateDao = loadBalancerTemplateDao;
	}

}
