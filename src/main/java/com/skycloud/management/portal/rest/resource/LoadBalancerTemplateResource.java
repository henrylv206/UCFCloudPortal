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

import static com.skycloud.management.portal.common.Constants.CMCC_CRITERION_CODE;
import com.skycloud.management.portal.rest.model.LoadBalancerTemplateResp;
import com.skycloud.management.portal.webservice.naas.ObjectNotFoundException;
import com.skycloud.management.portal.webservice.naas.entity.LoadBalancerTemplate;
import com.skycloud.management.portal.webservice.naas.service.ILoadBalancerTemplateService;

/**
 * 负载均衡模板Rest Web Service API
 * 
 * @author liujijun
 * @since Feb 9, 2012
 * @version 1.0
 */
@Component
@Path("/loadBalancerTemplate")
public class LoadBalancerTemplateResource {
	@Autowired
	private ILoadBalancerTemplateService loadBalancerTemplateService;

	/**
	 * 显示所有负载均衡模板列表
	 * 
	 * @return
	 */
	@GET
	@Path("list")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public List<LoadBalancerTemplate> listLoadBalancerTemplate() {
		return loadBalancerTemplateService.listLoadBalancerTemplate();
	}

	/**
	 * 取得某个负载均衡模板信息
	 * 
	 * @param id
	 * @return
	 */
	@GET
	@Path("{id}")
	@Produces({ MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML })
	public Response getLoadBalancerTemplate(@PathParam("id") String id) {
		LoadBalancerTemplateResp resp = new LoadBalancerTemplateResp();
		try {
			LoadBalancerTemplate template = loadBalancerTemplateService
					.getLoadBalancerTemplateById(Integer.parseInt(id));
			resp.setTemplate(template);
			resp.setResCode(CMCC_CRITERION_CODE.RESPONSE_CODE_SUCCESS.getValue());
		} catch (ObjectNotFoundException e) {
			resp.setMessage(e.getMessage());
			resp.setResCode(CMCC_CRITERION_CODE.RESPONSE_CODE_T_ID_NOTEXIST.getValue());
		}
		return Response.ok(resp).build();

	}

}
