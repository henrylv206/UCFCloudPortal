package com.skycloud.management.portal.front.resources.dao.impl;

import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.front.resources.dao.VirtualMachineMonitorDao;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class VirtualMachineMonitorDaoImpl implements VirtualMachineMonitorDao {
	private static final String REST_WEBSERVICE_VM_MONITORURL = ConfigManager.getInstance().getString(
			"REST_WEBSERVICE_VM_MONITORURL");
	private static Client client;
	static {
		ClientConfig config = new DefaultClientConfig();
		config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		client = Client.create(config);
	}

	/**
	 * 
	 * @param serverName
	 * @return
	 */
	public String getMonitorInfo(String instanceId) {
		WebResource resource = client.resource(REST_WEBSERVICE_VM_MONITORURL + "/" + instanceId);
		ClientResponse resp = resource.get(ClientResponse.class);
		return resp.getEntity(String.class);
	}
}
