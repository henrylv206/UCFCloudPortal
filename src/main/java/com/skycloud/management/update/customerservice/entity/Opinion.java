package com.skycloud.management.update.customerservice.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 客服支持模块中用户意见实体
 * 
 * @author guoguangjun
 */
public class Opinion implements Serializable {
	/** serialVersionUID */
	private static final long serialVersionUID = -3300905590028912207L;
	/** 意见状态：全部 */
	public static final int OPINION_STATE_ALL = -1;
	/** 意见状态：未处理 */
	public static final int OPINION_STATE_UNTREATED = 0;
	/** 意见状态：已处理 */
	public static final int OPINION_STATE_TREATED = 1;
	/** id */
	private Integer id;
	/** 用户意见信息录入人 */
	private String createUser;
	/** 用户意见信息描述 */
	private String description;
	/** 联系电话 */
	private String contactTel;
	/** 电子邮件 */
	private String email;
	/** 留言时间 */
	private Date createDate;
	/** 意见处理时间 */
	private Date handleDate;
	/** 处理描述 */
	private String handleDesc;
	/** 意见状态 0：未处理， 1：已处理 */
	private Integer state;
	/** 意见所属分类 */
	private CommentCategory commentCategory;
	/** 处理人姓名 */
	private String handleUser;

	public Opinion() {
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getContactTel() {
		return contactTel == null ? "" : contactTel;
	}

	public void setContactTel(String contactTel) {
		this.contactTel = contactTel;
	}

	public String getEmail() {
		return email == null ? "" : email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHandleDesc() {
		return handleDesc == null ? "" : handleDesc;
	}

	public void setHandleDesc(String handleDesc) {
		this.handleDesc = handleDesc;
	}

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public CommentCategory getCommentCategory() {
		return commentCategory;
	}

	public void setCommentCategory(CommentCategory commentCategory) {
		this.commentCategory = commentCategory;
	}

	public String getHandleUser() {
		return handleUser == null ? "" : handleUser;
	}

	public void setHandleUser(String handleUser) {
		this.handleUser = handleUser;
	}

	@Override
	public String toString() {
		return getClass().getName() + "[createUser=" + createUser
				+ ", description=" + description + ", createDate=" + createDate
				+ ", state=" + state + "]";
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getHandleDate() {
		return handleDate;
	}

	public void setHandleDate(Date handleDate) {
		this.handleDate = handleDate;
	}
}
