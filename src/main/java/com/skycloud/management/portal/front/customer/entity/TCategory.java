package com.skycloud.management.portal.front.customer.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TCategory entity. @author MyEclipse Persistence Tools
 */

public class TCategory implements java.io.Serializable {

	// Fields

	private Integer categoryId;                    //行业分类编号
	private String categoryName;                   //行业分类名称
	private Set TCompanyInfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public TCategory() {
	}

	/** minimal constructor */
	public TCategory(String categoryName) {
		this.categoryName = categoryName;
	}

	/** full constructor */
	public TCategory(String categoryName, Set TCompanyInfos) {
		this.categoryName = categoryName;
		this.TCompanyInfos = TCompanyInfos;
	}

	// Property accessors

	public Integer getCategoryId() {
		return this.categoryId;
	}

	public void setCategoryId(Integer categoryId) {
		this.categoryId = categoryId;
	}

	public String getCategoryName() {
		return this.categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}

	public Set getTCompanyInfos() {
		return this.TCompanyInfos;
	}

	public void setTCompanyInfos(Set TCompanyInfos) {
		this.TCompanyInfos = TCompanyInfos;
	}

}