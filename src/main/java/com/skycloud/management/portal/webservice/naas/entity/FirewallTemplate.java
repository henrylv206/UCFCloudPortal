package com.skycloud.management.portal.webservice.naas.entity;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * 防火墙模板实体
 * 
 * @author liujijun
 * @since Jan 30, 2012
 * @version 1.0
 */
@XmlRootElement
@XmlType(propOrder={"id","name","ruleNum","status"})
public class FirewallTemplate {
	private int id;
	private String name;
	private int ruleNum;
	private String status;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getRuleNum() {
		return ruleNum;
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

	public void setRuleNum(int ruleNum) {
		this.ruleNum = ruleNum;
	}


	public void setStatus(String status) {
		this.status = status;
	}

}
