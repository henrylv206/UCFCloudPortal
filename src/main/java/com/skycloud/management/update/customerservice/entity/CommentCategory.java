package com.skycloud.management.update.customerservice.entity;

import java.io.Serializable;

/**
 * 客服分类实体
 * 
 * @author guoguangjun
 * 
 */
public class CommentCategory implements Serializable {

	private static final long serialVersionUID = -3566317824442301493L;
	/** 类别ID */
	private Integer id;
	/** 类别名称 */
	private String name;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name == null ? "" : name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
