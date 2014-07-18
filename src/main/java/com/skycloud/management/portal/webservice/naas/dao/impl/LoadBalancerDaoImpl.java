package com.skycloud.management.portal.webservice.naas.dao.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.client.RestTemplate;

import com.skycloud.management.portal.webservice.naas.dao.ILoadBalancerConstants;
import com.skycloud.management.portal.webservice.naas.dao.ILoadBalancerDao;

/**
 * 负载均衡实现,基于Spring Rest Client
 * 
 * @author liujijun
 * @since Jan 31, 2012
 * @version 1.0
 */
public class LoadBalancerDaoImpl implements ILoadBalancerDao, ILoadBalancerConstants {

	private RestTemplate restTemplate;
	private String url;

	/* xml || json,默认为json */
	private String dataType = "json";

	public String listLoadBalancerRules(int publicIpId) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("publicipid", publicIpId);
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + LIST_LBR, String.class, urlVariables);
	}

	public String createLoadBalancerRule(String name, String description, String publicIpId,
			int publicPort, int privatePort, String algorithm) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("name", name);
		urlVariables.put("description", description);
		urlVariables.put("publicipid", publicIpId);
		urlVariables.put("publicport", publicPort);
		urlVariables.put("privateport", privatePort);
		urlVariables.put("algorithm", algorithm);
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + CREATE_LBR, String.class, urlVariables);
	}

	public String deleteLoadBalancerRule(int id) {
		return restTemplate.getForObject(url + DELETE_LBR, String.class, id, dataType);
	}

	public String assignToLoadBalancerRule(int id, String vmIds) {
		return restTemplate.getForObject(url + ASSIGN_TO_LBR, String.class, id, vmIds, dataType);
	}

	public String updateLoadBalancerRule(int id, String name, String algorithm,
			String description) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();
		urlVariables.put("id", id);
		urlVariables.put("name", name);
		urlVariables.put("algorithm", algorithm);
		urlVariables.put("description", description);
		urlVariables.put("response", dataType);
		return restTemplate.getForObject(url + UPDATE_LBR, String.class, urlVariables);
	}

	public String removeFromLoadBalancerRule(int id, String vmIds) {
		return restTemplate.getForObject(url + REMOVE_FROM_LBR, String.class, id, vmIds, dataType);
	}

	public String listLoadBalancerRuleInstances(int id) {
		return restTemplate.getForObject(url + LIST_LBR_INSTANCES, String.class, id, dataType);
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
