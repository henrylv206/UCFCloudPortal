/**
 * 2012-3-13  下午02:01:02  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.dao.po;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.skycloud.management.portal.task.vdc.enumtype.AuditStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.JobStateVDC;
import com.skycloud.management.portal.task.vdc.enumtype.OperationType;
import com.skycloud.management.portal.task.vdc.util.PKgen;

/**
 * @author shixq
 * @version $Revision$ 下午02:01:02
 */
public class AsyncJobVDCPO implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = -5306430422158311543L;
  private long unique_id = 0;// 唯一ID，不需要传
  private int user_id = 0;// 用户ID，必传
  private int order_id = 0;// 订单ID，没有就不传
  private int instance_info_id = 0;// 实例ID，没有就不传
  private int instance_info_iri_id = 0;// 实例块存储ID，没有就不传
  private int template_id = 0;// 模版ID，没有就不传
  private String template_res_id = "";// 资源模版ID，没有就不传
  private int firewall_rule_id = 0;// 防火墙规则ID，只有防火墙规则时传入，没有就不传
  private OperationType operation;// 操作名称 ，使用方式是：OperationType.操作名称，必传
  private String parameter = "";// 操作参数，调用api所需要的参数，必传
  private AuditStateVDC auditstate;// 审批状态，0为不需要审批，1为等待审批，2为审批通过，4为审批未通过，必传
  /**
   * 以下数据跟前台功能无关，无需给值
   */
  private JobStateVDC jobstate;// 任务状态，1为等待执行，2为已执行，3为已执行成功，待归档，4为执行失败，待归档
  private String resp_code;// 资源ID
  private String resp_parameter = "";// 返回参数，调用api所返回的参数
  private String error_code = "";// 错误CODE
  private String error_code_desc = "";// 错误描述

  private String create_dt;// 创建时间

  private String comment;// 备注

  public long getUnique_id() {
    if (unique_id == 0) {
      PKgen pk = PKgen.getInstance(0);
      return pk.nextPK();
    }
    return unique_id;
  }

  public void setUnique_id(long unique_id) {
    this.unique_id = unique_id;
  }

  public int getInstance_info_iri_id() {
    return instance_info_iri_id;
  }

  public void setInstance_info_iri_id(int instance_info_iri_id) {
    this.instance_info_iri_id = instance_info_iri_id;
  }

  public int getUser_id() {
    return user_id;
  }

  public void setUser_id(int user_id) {
    this.user_id = user_id;
  }

  public int getOrder_id() {
    return order_id;
  }

  public void setOrder_id(int order_id) {
    this.order_id = order_id;
  }

  public int getInstance_info_id() {
    return instance_info_id;
  }

  public String getTemplate_res_id() {
    return template_res_id;
  }

  public void setTemplate_res_id(String template_res_id) {
    this.template_res_id = template_res_id;
  }

  public void setInstance_info_id(int instance_info_id) {
    this.instance_info_id = instance_info_id;
  }

  public int getTemplate_id() {
    return template_id;
  }

  public void setTemplate_id(int template_id) {
    this.template_id = template_id;
  }

  public int getFirewall_rule_id() {
    return firewall_rule_id;
  }

  public void setFirewall_rule_id(int firewall_rule_id) {
    this.firewall_rule_id = firewall_rule_id;
  }

  public OperationType getOperation() {
    return operation;
  }

  public void setOperation(OperationType operation) {
    this.operation = operation;
  }

  public String getParameter() {
    return parameter;
  }

  public void setParameter(String parameter) {
    this.parameter = parameter;
  }

  public JobStateVDC getJobstate() {
    return jobstate;
  }

  public void setJobstate(JobStateVDC jobstate) {
    this.jobstate = jobstate;
  }

  public AuditStateVDC getAuditstate() {
    return auditstate;
  }

  public void setAuditstate(AuditStateVDC auditstate) {
    this.auditstate = auditstate;
  }

  public String getError_code() {
    return error_code;
  }

  public void setError_code(String error_code) {
    this.error_code = error_code;
  }

  public String getError_code_desc() {
    return error_code_desc;
  }

  public void setError_code_desc(String error_code_desc) {
    this.error_code_desc = error_code_desc;
  }

  public String getResp_parameter() {
    return resp_parameter;
  }

  public void setResp_parameter(String resp_parameter) {
    this.resp_parameter = resp_parameter;
  }

  public String getCreate_dt() {
    this.create_dt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    return create_dt;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public String getResp_code() {
    return resp_code;
  }

  public void setResp_code(String resp_code) {
    this.resp_code = resp_code;
  }
 
}
