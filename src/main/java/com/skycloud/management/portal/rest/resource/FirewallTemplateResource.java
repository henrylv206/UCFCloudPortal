package com.skycloud.management.portal.rest.resource;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.common.Constants.CMCC_CRITERION_CODE;
import com.skycloud.management.portal.rest.model.FirewallTemplateResp;
import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.entity.FirewallTemplate;
import com.skycloud.management.portal.webservice.naas.service.IFirewallTemplateService;

/**
 * 防火墙模板Rest Web Service API
 * 
 * @author liujijun
 * @since Feb 9, 2012
 * @version 1.0
 */
@Component
@Path("/firewallTemplate")
public class FirewallTemplateResource {
	@Autowired
	private IFirewallTemplateService firewallTemplateService;

	/**
	 * 列出所有可用的防火墙模板
	 * 
	 * @return
	 */
	@GET
	@Path("list")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<FirewallTemplate> listFirewallTemplate() {

		return firewallTemplateService.listFirewallTemplate();
	}

	/**
	 * 列出指定编号的防火墙模板
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getFirewallTemplate(@PathParam("id") int id) {
		FirewallTemplateResp resp = new FirewallTemplateResp();
		try {
			FirewallTemplate template = firewallTemplateService.getFirewallTemplateById(id);
			resp.setTemplate(template);
			resp.setResCode(CMCC_CRITERION_CODE.RESPONSE_CODE_SUCCESS.getValue());
		} catch (ObjectNotFoundException e) {
			resp.setMessage(e.getMessage());
			resp.setResCode(CMCC_CRITERION_CODE.RESPONSE_CODE_T_ID_NOTEXIST.getValue());
		}
		return Response.ok(resp).build();
	}

}
