package com.skycloud.management.portal.front.log.entity;

import java.io.Serializable;

public class TUserLogBase implements Serializable {

	/**
	 * 用户操作日志实体
	 */
	private static final long serialVersionUID = 4181642385997791749L;

	private int type;

	private String className;

	private String methodName;

	private String parameters;

	private String moduleName;

	private String functionName;

	private String comment;

	private String memo;

	private String targetType;//处理类型;msg: 消息，mail: 邮件，sms: 短信，log:文件，alert:提示，warn:警告，error:错误

	private String subject;

	private String froms;

	private String tos;

	private String userName;

	private int status;//用户日志状态[1:已创建，2:已查看，3:已发送，4:处理失败，5:已处理，6:已过期]

	private String statusName;

	public TUserLogBase() {
		super();
	}

	public TUserLogBase(String className, String methodName, String parameters, String moduleName, String functionName, Integer type, String comment,
	                    String memo) {
		super();
		this.type = type;
		this.className = className;
		this.methodName = methodName;
		this.parameters = parameters;
		this.moduleName = moduleName;
		this.functionName = functionName;
		this.comment = comment;
		this.memo = memo;

	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getModuleName() {
		return moduleName;
	}

	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}

	public String getFunctionName() {
		return functionName;
	}

	public void setFunctionName(String functionName) {
		this.functionName = functionName;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public String getTargetType() {
		return targetType;
	}

	public void setTargetType(String targetType) {
		this.targetType = targetType;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getFroms() {
		return froms;
	}

	public void setFroms(String froms) {
		this.froms = froms;
	}

	public String getTos() {
		return tos;
	}

	public void setTos(String tos) {
		this.tos = tos;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getStatusName() {
		return statusName;
	}

	public void setStatusName(String statusName) {
		this.statusName = statusName;
	}

}
