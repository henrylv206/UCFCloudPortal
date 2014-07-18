package com.skycloud.management.portal.webservice.naas.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 负载均衡模板实体
 * 
 * @author jijun
 * 
 */
@XmlRootElement
@XmlType(propOrder={"id","name","connNum","status"})
public class LoadBalancerTemplate {

	private int id;
	private String name;
	private int connNum;
	private String status;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getConnNum() {
		return connNum;
	}

	public String getStatus() {
		return status;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConnNum(int connNum) {
		this.connNum = connNum;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
