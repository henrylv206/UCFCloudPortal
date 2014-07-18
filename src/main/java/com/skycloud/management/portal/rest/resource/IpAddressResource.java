package com.skycloud.management.portal.rest.resource;

import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.skycloud.management.portal.webservice.naas.service.IIpAddressService;

/**
 * 公网IP地址
 * 
 * @author liujijun
 * @since Feb 9, 2012
 * @version 1.0
 */
@Component
@Path("/ipAddress")
public class IpAddressResource {
	@Autowired
	private IIpAddressService ipAddressService;

	/**
	 * 
	 * @param ipAddress
	 * @param accept
	 * @return
	 */
	@GET
	@Path("listPublicIpAddress/{ipaddress}")
	public String listPublicIpAddress(@PathParam("ipaddress") String ipAddress,
			@HeaderParam("Accept") String accept) {

		setDataType(accept);

		return ipAddressService.listPublicIpAddress(ipAddress);
	}

	/**
	 * 
	 * @return
	 */
	@GET
	@Path("listAvailableIpAddresses")
	public String listAvailableIpAddresses() {
		return ipAddressService.listAvailableIpAddresses();
	}

	/**
	 * 获取IP地址
	 * @param zoneId
	 * @return
	 */
	@GET
	@Path("associateIpAddress/{zoneId}/{domainId}/{account}")
	public String associateIpAddress(@PathParam("zoneId") int zoneId,@PathParam("domainId") int domainId,
			@PathParam("account") String account,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);

		return ipAddressService.associateIpAddress(zoneId,domainId,account);
	}

	private void setDataType(String accept) {
		if (accept.toLowerCase().startsWith(MediaType.APPLICATION_XML)) {
			ipAddressService.setDataType("xml");
		} else {
			ipAddressService.setDataType("json");
		}
	}
}
