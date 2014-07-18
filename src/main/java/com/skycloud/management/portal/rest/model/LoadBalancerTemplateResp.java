package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.webservice.naas.entity.LoadBalancerTemplate;

@XmlRootElement
public class LoadBalancerTemplateResp extends BaseResp {

	private LoadBalancerTemplate template;
	private String message;

	public LoadBalancerTemplate getTemplate() {
		return template;
	}

	public String getMessage() {
		return message;
	}

	public void setTemplate(LoadBalancerTemplate template) {
		this.template = template;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
