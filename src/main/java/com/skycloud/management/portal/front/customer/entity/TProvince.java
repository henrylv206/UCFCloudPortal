package com.skycloud.management.portal.front.customer.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TProvince entity. @author MyEclipse Persistence Tools
 */

public class TProvince implements java.io.Serializable {

	// Fields

	private Integer provinceId;              //省直辖市编号
	private String provinceName;             //省直辖市名称
	private Set TCities = new HashSet(0);

	// Constructors

	/** default constructor */
	public TProvince() {
	}

	/** minimal constructor */
	public TProvince(String provinceName) {
		this.provinceName = provinceName;
	}

	/** full constructor */
	public TProvince(String provinceName, Set TCities) {
		this.provinceName = provinceName;
		this.TCities = TCities;
	}

	// Property accessors

	public Integer getProvinceId() {
		return this.provinceId;
	}

	public void setProvinceId(Integer provinceId) {
		this.provinceId = provinceId;
	}

	public String getProvinceName() {
		return this.provinceName;
	}

	public void setProvinceName(String provinceName) {
		this.provinceName = provinceName;
	}

	public Set getTCities() {
		return this.TCities;
	}

	public void setTCities(Set TCities) {
		this.TCities = TCities;
	}

}