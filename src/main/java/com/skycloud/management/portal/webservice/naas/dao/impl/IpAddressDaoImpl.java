package com.skycloud.management.portal.webservice.naas.dao.impl;

import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.client.RestTemplate;

import com.skycloud.management.portal.webservice.naas.dao.IIpAddressConstants;
import com.skycloud.management.portal.webservice.naas.dao.IIpAddressDao;

/**
 * 
 * @author liujijun
 * @since Feb 4, 2012
 * @version 1.0
 */
public class IpAddressDaoImpl implements IIpAddressDao, IIpAddressConstants {
	private RestTemplate restTemplate;
	private JdbcTemplate jdbcTemplate;

	private String url;
	/* xml || json */
	private String dataType = "json";

	@Override
	public String listPublicIpAddress(String ipAddress) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("ipaddress", ipAddress);
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + LIST_IP_ADDRESS, String.class, urlVariables);
	}

	@Override
	public String listAvailableIpAddresses() {

		return restTemplate.getForObject(url + LIST_IP_ADDRESSES, String.class);
	}

	@Override
	public String associateIpAddress(int zoneId,int domainId, String account) {
		Map<String, Object> urlVariables = new HashMap<String, Object>();

		urlVariables.put("zoneid", zoneId);
		urlVariables.put("account",account);
		urlVariables.put("domainId", domainId);
		urlVariables.put("response", dataType);

		return restTemplate.getForObject(url + ASSOCIATE_IP_ADDRESS, String.class, urlVariables);
	}

	@Override
	public boolean isAvailableIpAddress(String ipAddress) {
		String sql = "select count(id) from T_SCS_IP_USAGE_STATISTIC where ip_address=?";

		return jdbcTemplate.queryForInt(sql, ipAddress) == 0;
	}

	public RestTemplate getRestTemplate() {
		return restTemplate;
	}

	public String getUrl() {
		return url;
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

	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

}
