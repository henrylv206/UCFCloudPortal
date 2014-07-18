package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 
 * @author liujijun
 * @since Feb 2, 2012
 * @version 1.0
 */
@XmlRootElement
public class LoadBalancerReq {
	private int id;
	private String name;
	private String algorithm;
	private String publicIpId;
	private int publicPort;
	private int privatePort;
	private String description;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getAlgorithm() {
		return algorithm;
	}

	public String getPublicIpId() {
		return publicIpId;
	}

	public int getPublicPort() {
		return publicPort;
	}

	public int getPrivatePort() {
		return privatePort;
	}

	public String getDescription() {
		return description;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setAlgorithm(String algorithm) {
		this.algorithm = algorithm;
	}

	public void setPublicIpId(String publicIpId) {
		this.publicIpId = publicIpId;
	}

	public void setPublicPort(int publicPort) {
		this.publicPort = publicPort;
	}

	public void setPrivatePort(int privatePort) {
		this.privatePort = privatePort;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
