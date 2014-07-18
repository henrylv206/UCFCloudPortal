package com.skycloud.management.portal.front.log.entity;

import java.sql.Timestamp;

import com.skycloud.management.portal.common.entity.PageVO;

/**
 * TScsUserLogwarn entity. @author MyEclipse Persistence Tools
 */

public class TScsUserLogwarn implements java.io.Serializable {

	// Fields

	private Integer id;
	private String ip;
	private String logpath;
	private Timestamp createDt;
	private Integer logsize;
	private String comment;

	private PageVO page;//分页信息
	// Constructors

	public PageVO getPage() {
		return page;
	}

	public void setPage(PageVO page) {
		this.page = page;
	}

	/** default constructor */
	public TScsUserLogwarn() {
	}

	/** full constructor */
	public TScsUserLogwarn(String ip, String logpath, Timestamp createDt,
			Integer logsize, String comment) {
		this.ip = ip;
		this.logpath = logpath;
		this.createDt = createDt;
		this.logsize = logsize;
		this.comment = comment;
	}

	// Property accessors

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLogpath() {
		return this.logpath;
	}

	public void setLogpath(String logpath) {
		this.logpath = logpath;
	}

	public Timestamp getCreateDt() {
		return this.createDt;
	}

	public void setCreateDt(Timestamp createDt) {
		this.createDt = createDt;
	}

	public Integer getLogsize() {
		return this.logsize;
	}

	public void setLogsize(Integer logsize) {
		this.logsize = logsize;
	}

	public String getComment() {
		return this.comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

}