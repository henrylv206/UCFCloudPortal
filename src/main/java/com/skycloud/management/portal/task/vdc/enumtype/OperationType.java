/**
 * 2012-3-13  下午04:37:39  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午04:37:39
 */
public enum OperationType {
  /**
   * 申请虚拟机
   */
  CREATE_VM("CREATE_VM"), // OK 王海东
  /**
   * 变更虚拟机
   */
  UPDATE_VM("UPDATE_VM"), // OK 王征
  /**
   * 退订虚拟机
   */
  DELETE_VM("DELETE_VM"), // OK 王征
  /**
   * 启动虚拟机
   */
  START_VM("START_VM"), // OK 王征
  /**
   * 停止虚拟机
   */
  STOP_VM("STOP_VM"), // OK 王征
  /**
   * 重启虚拟机
   */
  RESTART_VM("RESTART_VM"), // OK 王征
  /**
   * 暂停虚拟机
   */
  PAUSE_VM("PAUSE_VM"), // OK 王征
  /**
   * 恢复虚拟机
   */
  RESTORE_VM("RESTORE_VM"), // OK 王征
  /**
   * 块存储申请
   */
  CREATE_EBS("CREATE_EBS"), // OK 何福康
  /**
   * 块存储退订
   */
  DELETE_EBS("DELETE_EBS"), // OK 何福康
  /**
   * 块存储挂起
   */
  BIND_EBS("BIND_EBS"), // OK 何福康
  /**
   * 块存储解挂起
   */
  UNBIND_EBS("UNBIND_EBS"), // OK 何福康
  /**
   * 公网IP申请
   */
  CREATE_WANIP("CREATE_WANIP"), // OK 甘坤彪
  /**
   * 公网IP退订
   */
  DELETE_WANIP("DELETE_WANIP"), // OK 甘坤彪
  /**
   * 公网IP映射
   */
  BIND_WANIP("BIND_WANIP"), // OK 甘坤彪
  /**
   * 公网IP解映射
   */
  UNBIND_WANIP("UNBIND_WANIP"), // OK 甘坤彪
  /**
   * 虚拟机备份申请
   */
  CREATE_VMBAK("CREATE_VMBAK"), // OK 王征
  /**
   * 虚拟机备份退订
   */
  DELETE_VMBAK("DELETE_VMBAK"), // OK 王征
  /**
   * 虚拟机备份变更
   */
  UPDATE_VMBAK("UPDATE_VMBAK"), // OK 王征
  /**
   * 虚拟机备份恢复
   */
  RESTORE_VMBAK("RESTORE_VMBAK"), // OK 王征
  /**
   * 数据云备份申请
   */
  CREATE_DATABAK("CREATE_DATABAK"), // 齐盟硕
  /**
   * 数据云备份退订
   */
  DELETE_DATABAK("DELETE_DATABAK"), // OK 齐盟硕
  /**
   * 数据云备份变更
   */
  UPDATE_DATABAK("UPDATE_DATABAK"), // OK 齐盟硕
  /**
   * 带宽申请
   */
  CREATE_BANDWIDTH("CREATE_BANDWIDTH"), // 张爽
  /**
   * 带宽退订
   */
  DELETE_BANDWIDTH("DELETE_BANDWIDTH"), // 张爽
  /**
   * 带宽变更
   */
  UPDATE_BANDWIDTH("UPDATE_BANDWIDTH"), // 张爽
  /**
   * 安全组创建规则
   */
  CREATE_SGRULES("CREATE_SGRULES"), // OK 焦永志
  /**
   * 安全组删除规则
   */
  DELETE_SGRULES("DELETE_SGRULES"), // OK 焦永志

  /**
   * 模板操作类型： 申请
   */
  CREATE_TEMPLATE("CREATE_TEMPLATE"), // OK 刘江宁

  /**
   * 模板操作类型： 修改
   */
  UPDATE_TEMPLATE("UPDATE_TEMPLATE"), // OK 刘江宁

  /**
   * 模板操作类型： 删除
   */
  DELETE_TEMPLATE("DELETE_TEMPLATE"), // OK 刘江宁

  DEFAULTNULL("");

  private String value = null;

  OperationType(String value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  public static OperationType getByValue(String value) {
    for (OperationType type : OperationType.values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return OperationType.DEFAULTNULL;
  }

}
