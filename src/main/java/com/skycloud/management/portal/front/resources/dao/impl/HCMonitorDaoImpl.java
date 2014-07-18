package com.skycloud.management.portal.front.resources.dao.impl;

import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.front.resources.dao.HCMonitorDao;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

public class HCMonitorDaoImpl implements HCMonitorDao {

  private static final String REST_WEBSERVICE_VM_INFO = ConfigManager.getInstance().getString("REST_WEBSERVICE_VM_INFO");
  private static final String REST_WEBSERVICE_VM__MONITORURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_VM_MONITORURL");
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
  public String getMonitorInfo(int instanceId) {
    WebResource resource = client.resource(REST_WEBSERVICE_VM__MONITORURL + "/" + instanceId);
    ClientResponse resp = null;
    try {
      resp = resource.get(ClientResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return resp.getEntity(String.class);
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * com.skycloud.management.portal.front.resources.dao.HCMonitorDao#getHCInfo
   * (java.lang.String)
   */
  @Override
  public String getHCInfo(int instanceId) {
    WebResource resource = client.resource(REST_WEBSERVICE_VM_INFO + "/" + instanceId);
    ClientResponse resp = null;
    try {
      resp = resource.get(ClientResponse.class);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return resp.getEntity(String.class);
  }

}
