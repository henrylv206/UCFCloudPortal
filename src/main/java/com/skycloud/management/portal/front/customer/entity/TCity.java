package com.skycloud.management.portal.front.customer.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TCity entity. @author MyEclipse Persistence Tools
 */

public class TCity implements java.io.Serializable {

	// Fields

	private Integer cityId;                       //城市编号
	private TProvince TProvince;                  //省直辖市编号
	private String cityName;                      //城市名称
	private Set TCompanyInfos = new HashSet(0);   

	// Constructors

	/** default constructor */
	public TCity() {
	}

	/** minimal constructor */
	public TCity(TProvince TProvince, String cityName) {
		this.TProvince = TProvince;
		this.cityName = cityName;
	}

	/** full constructor */
	public TCity(TProvince TProvince, String cityName, Set TCompanyInfos) {
		this.TProvince = TProvince;
		this.cityName = cityName;
		this.TCompanyInfos = TCompanyInfos;
	}

	// Property accessors

	public Integer getCityId() {
		return this.cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public TProvince getTProvince() {
		return this.TProvince;
	}

	public void setTProvince(TProvince TProvince) {
		this.TProvince = TProvince;
	}

	public String getCityName() {
		return this.cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public Set getTCompanyInfos() {
		return this.TCompanyInfos;
	}

	public void setTCompanyInfos(Set TCompanyInfos) {
		this.TCompanyInfos = TCompanyInfos;
	}

}