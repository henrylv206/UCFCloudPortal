package com.skycloud.management.portal.webservice.naas.service.impl;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.webservice.naas.dao.IFirewallDao;
import com.skycloud.management.portal.webservice.naas.service.IFirewallService;

/**
 * 
 * @author liujijun
 * @since Feb 3, 2012
 * @version 1.0
 */
public class FirewallServiceImpl implements IFirewallService {
	private IFirewallDao firewallDao;

	@Override
	public String listFirewallRules(int publicIpId) {
		return filter(firewallDao.listFirewallRules(publicIpId));
	}

	@Override
	public String createFirewallRule(String name, String description, String publicIpId,
			int publicPort, int privatePort) {
		return filter(firewallDao.createFirewallRule(name, description, publicIpId, publicPort,
				privatePort));
	}

	@Override
	public String deleteFirewallRule(int id) {
		return filter(firewallDao.deleteFirewallRule(id));
	}

	@Override
	public String updateFirewallRule(int id, String name, String description) {
		return filter(firewallDao.updateFirewallRule(id, name, description));
	}

	@Override
	public String assignToFirewallRule(int id, int vmId) {
		return filter(firewallDao.assignToFirewallRule(id, vmId));
	}

	@Override
	public String removeFromFirewallRule(int id, int vmId) {
		return filter(firewallDao.removeFromFirewallRule(id, vmId));
	}

	@Override
	public void setDataType(String dataType) {
		firewallDao.setDataType(dataType);
	}

	public IFirewallDao getFirewallDao() {
		return firewallDao;
	}

	public void setFirewallDao(IFirewallDao firewallDao) {
		this.firewallDao = firewallDao;
	}

	/**
	 * 这里简单的将返回的内容中替换, 替换规则:
	 * <ul>
	 * <li>loadbalancer=>firewall</li>
	 * <li>algorithm:roundrobin => ""</li>
	 * <li>&lt;algorithm&gt;roundrobin&lt;/algorithm&gt;=>""</li>
	 * </ul>
	 * 
	 * @param response
	 * @return
	 */
	private String filter(String response) {
		return StringUtils.replaceEach(response, new String[] { "loadbalancer",
				"\"algorithm\":\"roundrobin\",", "<algorithm>roundrobin</algorithm>" },
				new String[] { "firewall", "", "" });
	}

}
