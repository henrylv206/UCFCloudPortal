package com.skycloud.management.portal.webservice.naas.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.skycloud.management.portal.webservice.naas.dao.IFirewallConstants;
import com.skycloud.management.portal.webservice.naas.dao.IFirewallDao;

/**
 * 防火墙实现
 * 
 * @author liujijun
 * @since Jan 31, 2012
 * @version 1.0
 */
public class FirewallDaoImpl implements IFirewallDao, IFirewallConstants {

	private RestTemplate restTemplate;
	private String url;
	/* xml || json */
	private String dataType = "json";

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.naas.dao.IFirewallDao#listFirewallRules
	 * (java.lang.String)
	 */
	public String listFirewallRules(int publicIpId) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("publicipid", publicIpId);
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + LIST_FWR, String.class, urlVariables);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.naas.dao.IFirewallDao#createFirewallRule
	 * (java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public String createFirewallRule(String name, String description, String publicIpId,
			int publicPort, int privatePort) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("name", name);
		urlVariables.put("description", description);
		urlVariables.put("publicipid", publicIpId);
		urlVariables.put("publicport", publicPort);
		urlVariables.put("privateport", privatePort);
		// 防火墙使用负载均衡的roundrobin算法
		urlVariables.put("algorithm", "roundrobin");
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + CREATE_FWR, String.class, urlVariables);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.skycloud.management.portal.naas.dao.IFirewallDao#assignToFirewallRule
	 * (java.lang.String, java.lang.String)
	 */
	public String assignToFirewallRule(int id, int vmId) {
		/**
		 * 一个防火墙规则只能对应一台虚拟机，所以这里要作判断
		 */
		if (isRuleAssigned(id)) {
			return "json".equals(dataType) ? ERROR_MSG_ASSIGN_CONFLICT_JSON
					: ERROR_MSG_ASSIGN_CONFLICT_XML;
		}

		return restTemplate.getForObject(url + ASSIGN_TO_FWR, String.class, id, vmId, dataType);
	}

	/**
	 * 
	 */
	public String deleteFirewallRule(int id) {
		return restTemplate.getForObject(url + DELETE_FWR, String.class, id, dataType);
	}

	/**
	 * 
	 */
	public String updateFirewallRule(int id, String name, String description) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("id", id);
		urlVariables.put("name", name);
		urlVariables.put("description", description);
		urlVariables.put("response", dataType);
		return restTemplate.getForObject(url + UPDATE_FWR, String.class, urlVariables);
	}
	
	@Override
	public String removeFromFirewallRule(int id, int vmId) {
		return restTemplate.getForObject(url + REMOVE_FROM_FWR, String.class, id, vmId, dataType);
	}

	/**
	 * 判断这个对应于id的规则是否已经分配，如果已经分配，那么返回的信息中将包含字符串loadbalancerruleinstance
	 * 
	 * @return
	 */
	private boolean isRuleAssigned(int id) {
		String response = restTemplate.getForObject(url + LIST_FWR_INSTANCES, String.class, id,
				dataType);
		return response.contains("\"loadbalancerruleinstance\"");
	}

	public void setRestTemplate(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
}
