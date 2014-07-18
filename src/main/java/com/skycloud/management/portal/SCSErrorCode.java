/**
 * 2011-12-5  下午03:49:33  $Id:shixq
 */
package com.skycloud.management.portal;

/**
 * @author shixq
 * @version $Revision$ 下午03:49:33
 */
public class SCSErrorCode {

  public static final String DB_SQL_QUERY_TEMPLATE_MC_COUNT_ERROR = "10000001";
  public static final String DB_SQL_QUERY_TEMPLATE_MC_COUNT_DESC = "执行查询TEMPLATE_MC表失败";
  public static final String DB_SQL_QUERY_INSTANCE_COUNT_ERROR = "10000002";
  public static final String DB_SQL_QUERY_INSTANCE_COUNT_DESC = "执行查询INSTANCE_INFO表失败";
  public static final String DB_SQL_QUERY_INSTANCE_INFO_COUNT_ERROR = "10000003";
  public static final String DB_SQL_QUERY_INSTANCE_INFO_COUNT_DESC = "执行查询INSTANCE_INFO单条记录失败";
  public static final String DB_SQL_QUERY_TEMPLATE_VM_COUNT_ERROR = "10000004";
  public static final String DB_SQL_QUERY_TEMPLATE_VM_COUNT_DESC = "执行查询TEMPLATE_VM表失败";
  public static final String DB_SQL_INSERT_ASYNCJOB_COUNT_ERROR = "10000005";
  public static final String DB_SQL_INSERT_ASYNCJOB_COUNT_DESC = "插入ASYNCJOB表失败";

  // /////////////VDC////////////////////////////////////

  public static final String DB_SQL_QUERY_ASYNCJOB_ERROR = "20000001";
  public static final String DB_SQL_QUERY_ASYNCJOB_DESC = "查询ASYNCJOB表记录失败";
  public static final String DB_SQL_UPDATE_ASYNCJOB_ERROR = "20000002";
  public static final String DB_SQL_UPDATE_ASYNCJOB_DESC = "修改ASYNCJOB表记录失败";
  public static final String DB_SQL_INSERT_ASYNCJOB_ERROR = "20000003";
  public static final String DB_SQL_INSERT_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败";
  public static final String DB_SQL_DELETE_ASYNCJOB_ERROR = "20000004";
  public static final String DB_SQL_DELETE_ASYNCJOB_DESC = "删除ASYNCJOB表记录失败";
  public static final String DB_SQL_PARAMETER_LOST_ASYNCJOB_ERROR = "20000005";
  public static final String DB_SQL_PARAMETER_LOST_ASYNCJOB_DESC = "修改ASYNCJOB表记录失败，缺少orderID修改参数";

  public static final String DB_SQL_PARAMETER_UNIQUE_ID_ASYNCJOB_ERROR = "20000006";
  public static final String DB_SQL_PARAMETER_UNIQUE_ID_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，生成流水号错误";
  public static final String DB_SQL_PARAMETER_USERID_ASYNCJOB_ERROR = "20000007";
  public static final String DB_SQL_PARAMETER_USERID_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，USERID为空";
  public static final String DB_SQL_PARAMETER_AUDIT_STATE_ASYNCJOB_ERROR = "20000008";
  public static final String DB_SQL_PARAMETER_AUDIT_STATE_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，审批状态操作名称为空";
  public static final String DB_SQL_PARAMETER_AUDIT_STATE_ERROR_ASYNCJOB_ERROR = "20000009";
  public static final String DB_SQL_PARAMETER_AUDIT_STATE_ERROR_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，审批状态只能为0为不需要审批，1为等待审批";
  public static final String DB_SQL_PARAMETER_OPERATION_ASYNCJOB_ERROR = "20000010";
  public static final String DB_SQL_PARAMETER_OPERATION_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，调用API操作名称为空";
  public static final String DB_SQL_PARAMETER_API_ASYNCJOB_ERROR = "20000011";
  public static final String DB_SQL_PARAMETER_API_ASYNCJOB_DESC = "插入ASYNCJOB表记录失败，调用API操作参数为空";

  public static final String DB_SQL_INSERT_ASYNCJOB_VDC_HISTORY_ASYNCJOB_ERROR = "20000012";
  public static final String DB_SQL_INSERT_ASYNCJOB_VDC_HISTORY_ASYNCJOB_DESC = "数据归档失败，插入JOBHISTORY表错误";
  public static final String DB_SQL_DELETE_ASYNCJOB_VDC_HISTORY_ASYNCJOB_ERROR = "20000013";
  public static final String DB_SQL_DELETE_ASYNCJOB_VDC_HISTORY_ASYNCJOB_DESC = "数据归档失败，刪除JOBHISTORY表错误";

  public static final String DB_SQL_UPDATE_IRI_ERROR = "20000014";
  public static final String DB_SQL_UPDATE_IRI_DESC = "修改IRI表记录失败";

  public static final String DB_SQL_UPDATE_INSTANCEINFO_ERROR = "20000015";
  public static final String DB_SQL_UPDATE_INSTANCEINFO_DESC = "修改InstanceInfo表记录失败";
  public static final String DB_SQL_UPDATE_VMTEMPLATE_ERROR = "20000016";
  public static final String DB_SQL_UPDATE_VMTEMPLATE_DESC = "修改VMTEMPLATE表记录失败";
  public static final String DB_SQL_UPDATE_USERSNAPSHOT_ERROR = "20000017";
  public static final String DB_SQL_UPDATE_USERSNAPSHOT_DESC = "修改USERSNAPSHOT表记录失败";

  public static final String DB_SQL_PARAMETER_LOST_SG_RULE_ERROR = "20000018";
  public static final String DB_SQL_PARAMETER_LOST_SG_RULE_DESC = "修改SG_RULE表记录失败，缺少ID修改参数";
  
  public static final String DB_SQL_UPDATE_SG_RULE_ERROR = "20000019";
  public static final String DB_SQL_UPDATE_SG_RULE_DESC = "修改SG_RULE表记录失败";
  
  public static final String DB_SQL_UPDATE_NICS_ERROR = "20000020";
  public static final String DB_SQL_UPDATE_NICS_DESC = "修改SG_RULE表记录失败";

}
