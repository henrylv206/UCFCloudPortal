package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * 防火墙资源使用页面展现对象
 * @author jiaoyz
 */
public class FirewallVO implements Serializable {

  private static final long serialVersionUID = 919529592029927284L;

  private int instanceId; //实例ID

  private String instanceName; //实例名称

  private String comment; //实例描述

  private int templateId; //模板ID

  private String templateName;  //模板名称

  private int ruleNum; //规则条数

  private String resourcePool; //资源池名称
  //fix bug 2479
  private int state; //状态
  
  private String createDt;
  private String updateDt;

  public int getInstanceId() {
    return instanceId;
  }

  public void setInstanceId(int instanceId) {
    this.instanceId = instanceId;
  }

  public String getInstanceName() {
    return instanceName;
  }

  public void setInstanceName(String instanceName) {
    this.instanceName = instanceName;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public int getTemplateId() {
    return templateId;
  }

  public void setTemplateId(int templateId) {
    this.templateId = templateId;
  }

  public String getTemplateName() {
    return templateName;
  }

  public void setTemplateName(String templateName) {
    this.templateName = templateName;
  }

  public int getRuleNum() {
    return ruleNum;
  }

  public void setRuleNum(int ruleNum) {
    this.ruleNum = ruleNum;
  }

  public String getResourcePool() {
    return resourcePool;
  }

  public void setResourcePool(String resourcePool) {
    this.resourcePool = resourcePool;
  }

	public int getState() {
		return state;
	}
	
	public void setState(int state) {
		this.state = state;
	}

	public String getCreateDt() {
		return createDt;
	}

	public void setCreateDt(String createDt) {
		this.createDt = createDt;
	}

	public String getUpdateDt() {
		return updateDt;
	}

	public void setUpdateDt(String updateDt) {
		this.updateDt = updateDt;
	}
	
}
