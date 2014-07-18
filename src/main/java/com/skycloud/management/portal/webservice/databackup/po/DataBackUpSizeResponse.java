package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class DataBackUpSizeResponse extends BaseDataBackUpResponse {

	private int id;
	
	private int size;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}
	
	
}
