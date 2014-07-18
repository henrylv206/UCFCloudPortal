package com.skycloud.management.portal.front.customer.entity;

import java.util.HashSet;
import java.util.Set;

/**
 * TClass entity. @author MyEclipse Persistence Tools
 */

public class TClass implements java.io.Serializable {

	// Fields

	private Integer classId;
	private String className;
	private Set TCompanyInfos = new HashSet(0);

	// Constructors

	/** default constructor */
	public TClass() {
	}

	/** minimal constructor */
	public TClass(String className) {
		this.className = className;
	}

	/** full constructor */
	public TClass(String className, Set TCompanyInfos) {
		this.className = className;
		this.TCompanyInfos = TCompanyInfos;
	}

	// Property accessors

	public Integer getClassId() {
		return this.classId;
	}

	public void setClassId(Integer classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public Set getTCompanyInfos() {
		return this.TCompanyInfos;
	}

	public void setTCompanyInfos(Set TCompanyInfos) {
		this.TCompanyInfos = TCompanyInfos;
	}

}