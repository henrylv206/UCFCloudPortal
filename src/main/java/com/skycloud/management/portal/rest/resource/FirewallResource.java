package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.rest.model.FirewallReq;
import com.skycloud.management.portal.webservice.naas.service.IFirewallService;

/**
 * 防火墙Rest Web Service API
 * 
 * @author liujijun
 * @since Feb 9, 2012
 * @version 1.0
 */
@Component
@Path("/firewall")
public class FirewallResource {
	@Autowired
	private IFirewallService firewallService;

	/**
	 * 查询某个ip对应的防火墙规则
	 * 
	 * @param publicIpId
	 * @param accept
	 * @return
	 */
	@GET
	@Path("listFirewallRules/{publicIpId}")
	public String listFirewallRules(@PathParam("publicIpId") int publicIpId,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);

		return firewallService.listFirewallRules(publicIpId);
	}

	/**
	 * 创建防火墙规则
	 * 
	 * @param firewall
	 * @param accept
	 * @return
	 */
	@POST
	@Path("createFirewallRule")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String createFirewallRule(FirewallReq firewall, @HeaderParam("Accept") String accept) {

		setDataType(accept);
		return firewallService.createFirewallRule(firewall.getName(), firewall.getDescription(),
				firewall.getPublicIpId(), firewall.getPublicPort(), firewall.getPrivatePort());
	}

	/**
	 * 删除防火墙规则
	 * 
	 * @param id
	 * @param accept
	 * @return
	 */
	@GET
	@Path("deleteFirewallRule/{id}")
	public String deleteFirewallRule(@PathParam("id") int id, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return firewallService.deleteFirewallRule(id);
	}

	/**
	 * 更新防火墙规则
	 * 
	 * @param firewall
	 * @param accept
	 * @return
	 */
	@POST
	@Path("updateFirewallRule")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String updateFirewallRule(FirewallReq firewall, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return firewallService.updateFirewallRule(firewall.getId(), firewall.getName(),
				firewall.getDescription());
	}

	/**
	 * 分配防火墙规则到一台虚拟机，一条规则，只能对应一台虚拟机
	 * 
	 * @param id
	 * @param vmId
	 * @param accept
	 * @return
	 */
	@GET
	@Path("assignToFirewallRule/{id}/{vmId}")
	public String assignToFirewallRule(@PathParam("id") int id, @PathParam("vmId") int vmId,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);

		return firewallService.assignToFirewallRule(id, vmId);
	}

	/**
	 * 将虚拟机从防火墙规则中移除
	 * 
	 * @param id
	 * @param vmId
	 * @param accept
	 * @return
	 */
	@GET
	@Path("removeFromFirewallRule/{id}/{vmId}")
	public String removeFromFirewallRule(@PathParam("id") int id, @PathParam("vmId") int vmId,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);

		return firewallService.removeFromFirewallRule(id, vmId);
	}

	private void setDataType(String accept) {
		if (accept.toLowerCase().startsWith(MediaType.APPLICATION_XML)) {
			firewallService.setDataType("xml");
		} else {
			firewallService.setDataType("json");
		}
	}
}
