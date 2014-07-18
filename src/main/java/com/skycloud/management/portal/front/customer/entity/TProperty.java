package com.skycloud.management.portal.front.customer.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TProperty entity. @author MyEclipse Persistence Tools
 */

public class TProperty implements java.io.Serializable {

	// Fields

	private Integer propertyId;                        //企业分类编号	
	private String propertyName;                       //企业分类名称
	private Set TCompanyInfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public TProperty() {
	}

	/** minimal constructor */
	public TProperty(String propertyName) {
		this.propertyName = propertyName;
	}

	/** full constructor */
	public TProperty(String propertyName, Set TCompanyInfos) {
		this.propertyName = propertyName;
		this.TCompanyInfos = TCompanyInfos;
	}

	// Property accessors

	public Integer getPropertyId() {
		return this.propertyId;
	}

	public void setPropertyId(Integer propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyName() {
		return this.propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public Set getTCompanyInfos() {
		return this.TCompanyInfos;
	}

	public void setTCompanyInfos(Set TCompanyInfos) {
		this.TCompanyInfos = TCompanyInfos;
	}

}