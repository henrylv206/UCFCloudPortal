package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class DataBackUpJobIdResponse extends BaseDataBackUpResponse {

	private String id;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	
}
