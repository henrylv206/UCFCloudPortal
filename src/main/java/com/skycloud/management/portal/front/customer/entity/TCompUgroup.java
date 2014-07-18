package com.skycloud.management.portal.front.customer.entity;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;



/**
 * TCompUgroup entity. @author MyEclipse Persistence Tools
 */

public class TCompUgroup implements java.io.Serializable {

	// Fields

	private Integer compUgroupId;
	private TCompanyInfo TCompanyInfo;
	private String compUgroupName;
	private String compUgroupDescr;
	private String compUgroupCreater;
	private Timestamp compUgroupCtime;
	private String compUgroupModname;
	private Timestamp compUgroupModtime;
	private String compUgroupComment;
	private Set TCompanyUsers = new HashSet(0);

	// Constructors

	/** default constructor */
	public TCompUgroup() {
	}

	/** minimal constructor */
	public TCompUgroup(TCompanyInfo TCompanyInfo, String compUgroupName,
			String compUgroupDescr, String compUgroupCreater,
			Timestamp compUgroupCtime, Timestamp compUgroupModtime) {
		this.TCompanyInfo = TCompanyInfo;
		this.compUgroupName = compUgroupName;
		this.compUgroupDescr = compUgroupDescr;
		this.compUgroupCreater = compUgroupCreater;
		this.compUgroupCtime = compUgroupCtime;
		this.compUgroupModtime = compUgroupModtime;
	}

	/** full constructor */
	public TCompUgroup(TCompanyInfo TCompanyInfo, String compUgroupName,
			String compUgroupDescr, String compUgroupCreater,
			Timestamp compUgroupCtime, String compUgroupModname,
			Timestamp compUgroupModtime, String compUgroupComment,
			Set TCompanyUsers) {
		this.TCompanyInfo = TCompanyInfo;
		this.compUgroupName = compUgroupName;
		this.compUgroupDescr = compUgroupDescr;
		this.compUgroupCreater = compUgroupCreater;
		this.compUgroupCtime = compUgroupCtime;
		this.compUgroupModname = compUgroupModname;
		this.compUgroupModtime = compUgroupModtime;
		this.compUgroupComment = compUgroupComment;
		this.TCompanyUsers = TCompanyUsers;
	}

	// Property accessors

	public Integer getCompUgroupId() {
		return this.compUgroupId;
	}

	public void setCompUgroupId(Integer compUgroupId) {
		this.compUgroupId = compUgroupId;
	}

	public TCompanyInfo getTCompanyInfo() {
		return this.TCompanyInfo;
	}

	public void setTCompanyInfo(TCompanyInfo TCompanyInfo) {
		this.TCompanyInfo = TCompanyInfo;
	}

	public String getCompUgroupName() {
		return this.compUgroupName;
	}

	public void setCompUgroupName(String compUgroupName) {
		this.compUgroupName = compUgroupName;
	}

	public String getCompUgroupDescr() {
		return this.compUgroupDescr;
	}

	public void setCompUgroupDescr(String compUgroupDescr) {
		this.compUgroupDescr = compUgroupDescr;
	}

	public String getCompUgroupCreater() {
		return this.compUgroupCreater;
	}

	public void setCompUgroupCreater(String compUgroupCreater) {
		this.compUgroupCreater = compUgroupCreater;
	}

	public Timestamp getCompUgroupCtime() {
		return this.compUgroupCtime;
	}

	public void setCompUgroupCtime(Timestamp compUgroupCtime) {
		this.compUgroupCtime = compUgroupCtime;
	}

	public String getCompUgroupModname() {
		return this.compUgroupModname;
	}

	public void setCompUgroupModname(String compUgroupModname) {
		this.compUgroupModname = compUgroupModname;
	}

	public Timestamp getCompUgroupModtime() {
		return this.compUgroupModtime;
	}

	public void setCompUgroupModtime(Timestamp compUgroupModtime) {
		this.compUgroupModtime = compUgroupModtime;
	}

	public String getCompUgroupComment() {
		return this.compUgroupComment;
	}

	public void setCompUgroupComment(String compUgroupComment) {
		this.compUgroupComment = compUgroupComment;
	}

	public Set getTCompanyUsers() {
		return this.TCompanyUsers;
	}

	public void setTCompanyUsers(Set TCompanyUsers) {
		this.TCompanyUsers = TCompanyUsers;
	}

}