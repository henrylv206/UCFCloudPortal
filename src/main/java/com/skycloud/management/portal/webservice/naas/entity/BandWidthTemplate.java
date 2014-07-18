package com.skycloud.management.portal.webservice.naas.entity;

/**
 * 带宽模板实体
 * 
 * @author jijun
 * 
 */
public class BandWidthTemplate {
	private int id;
	private String name;
	private String width;
	private String description;
	private String status;

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getWidth() {
		return width;
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

	public void setWidth(String width) {
		this.width = width;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

}
