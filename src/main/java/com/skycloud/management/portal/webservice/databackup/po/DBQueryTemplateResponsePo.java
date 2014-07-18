package com.skycloud.management.portal.webservice.databackup.po;

import java.util.List;


import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="templates")
public class DBQueryTemplateResponsePo {

	 private List<DBTemplate> template ;

	public List<DBTemplate> getTemplate() {
		return template;
	}

	public void setTemplate(List<DBTemplate> template) {
		this.template = template;
	}
	
}
