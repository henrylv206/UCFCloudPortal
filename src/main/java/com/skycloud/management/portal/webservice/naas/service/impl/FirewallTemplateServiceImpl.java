package com.skycloud.management.portal.webservice.naas.service.impl;

import java.util.List;

import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.dao.IFirewallTemplateDao;
import com.skycloud.management.portal.webservice.naas.entity.FirewallTemplate;
import com.skycloud.management.portal.webservice.naas.service.IFirewallTemplateService;

/**
 * @author liujijun
 * @since Feb 3, 2012
 * @version 1.0
 */
public class FirewallTemplateServiceImpl implements IFirewallTemplateService {
	private IFirewallTemplateDao firewallTemplateDao;

	@Override
	public FirewallTemplate getFirewallTemplateById(int id) throws ObjectNotFoundException{
		return firewallTemplateDao.getFirewallTemplateById(id);
	}

	@Override
	public List<FirewallTemplate> listFirewallTemplate() {
		return firewallTemplateDao.listFirewallTemplate();
	}

	public void setFirewallTemplateDao(IFirewallTemplateDao firewallTemplateDao) {
		this.firewallTemplateDao = firewallTemplateDao;
	}

	public IFirewallTemplateDao getFirewallTemplateDao() {
		return firewallTemplateDao;
	}

}
