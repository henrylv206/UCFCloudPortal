package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.webservice.naas.entity.FirewallTemplate;

@XmlRootElement
public class FirewallTemplateResp extends BaseResp {

	private FirewallTemplate template;
	private String message;
	
	public FirewallTemplate getTemplate() {
		return template;
	}
	public String getMessage() {
		return message;
	}
	public void setTemplate(FirewallTemplate template) {
		this.template = template;
	}
	public void setMessage(String message) {
		this.message = message;
	}


}
