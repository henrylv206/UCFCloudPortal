package com.skycloud.management.portal.webservice.databackup.po;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="response")
public class BaseDataBackUpResponse {

	private String resCode;
	
	private String msg ;

	public String getResCode() {
		return resCode;
	}

	public void setResCode(String resCode) {
		this.resCode = resCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
	
}
