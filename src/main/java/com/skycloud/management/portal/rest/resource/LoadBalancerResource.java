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

import com.skycloud.management.portal.rest.model.LoadBalancerReq;
import com.skycloud.management.portal.webservice.naas.service.ILoadBalancerService;

/**
 * 负载均衡Rest Web Service API
 * 
 * @author liujijun
 * @since Feb 9, 2012
 * @version 1.0
 */
@Component
@Path("/loadBalancer")
public class LoadBalancerResource {
	@Autowired
	private ILoadBalancerService loadBalancerService;

	/**
	 * 根据公网IP编号显示负载均衡规则
	 * 
	 * @param publicIpId
	 * @param accept
	 * @return
	 */
	@GET
	@Path("listLoadBalancerRules/{publicIpId}")
	public String listLoadBalancerRules(@PathParam("publicIpId") int publicIpId,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.listLoadBalancerRules(publicIpId);
	}

	/**
	 * 创建负载均衡规则
	 * 
	 * @param lb
	 * @param accept
	 * @return
	 */
	@POST
	@Path("createLoadBalancerRule")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String createLoadBalancerRule(LoadBalancerReq lb, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.createLoadBalancerRule(lb.getName(), lb.getDescription(),
				lb.getPublicIpId(), lb.getPublicPort(), lb.getPrivatePort(), lb.getAlgorithm());
	}

	/**
	 * 删除负载均衡规则
	 * 
	 * @param id
	 * @param accept
	 * @return
	 */
	@GET
	@Path("deleteLoadBalancerRule/{id}")
	public String deleteLoadBalancerRule(@PathParam("id")int id, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.deleteLoadBalancerRule(id);
	}

	/**
	 * 给指定的负载均衡规则分配虚拟机,多个虚拟机请用逗号分割，例如vmIds=1,2,3
	 * 
	 * @param id
	 * @param vmIds
	 * @param accept
	 * @return
	 */
	@GET
	@Path("assignToLoadBalancerRule/{id}/{vmIds}")
	public String assignToLoadBalancerRule(@PathParam("id") int id,
			@PathParam("vmIds") String vmIds, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.assignToLoadBalancerRule(id, vmIds);
	}

	/**
	 * 更新负载均衡规则
	 * 
	 * @param lb
	 * @param accept
	 * @return
	 */
	@POST
	@Path("updateLoadBalancerRule")
	@Consumes({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public String updateLoadBalancerRule(LoadBalancerReq lb, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.updateLoadBalancerRule(lb.getId(), lb.getName(),
				lb.getAlgorithm(), lb.getDescription());
	}

	/**
	 * 从指定的负载均衡中删除对应虚拟机,多个虚拟机请用逗号分割,例如vmIds=1,2,3 将删除编号为1、2和3的虚拟机实例
	 * 
	 * @param id
	 * @param vmIds
	 * @param accept
	 * @return
	 */
	@GET
	@Path("removeFromLoadBalancerRule/{id}/{vmIds}")
	public String removeFromLoadBalancerRule(@PathParam("id") int id,
			@PathParam("vmIds") String vmIds, @HeaderParam("Accept") String accept) {
		setDataType(accept);

		return loadBalancerService.removeFromLoadBalancerRule(id, vmIds);
	}

	/**
	 * 查询对应虚拟机规则下的虚拟机实例
	 * 
	 * @param id
	 * @param accept
	 * @return
	 */
	@GET
	@Path("listLoadBalancerRuleInstances/{id}")
	public String listLoadBalancerRuleInstances(@PathParam("id") int id,
			@HeaderParam("Accept") String accept) {
		setDataType(accept);
		return loadBalancerService.listLoadBalancerRuleInstances(id);
	}

	public void setDataType(String accept) {
		if (accept.toLowerCase().startsWith(MediaType.APPLICATION_XML)) {
			loadBalancerService.setDataType("xml");
		} else {
			loadBalancerService.setDataType("json");
		}
	}

}
