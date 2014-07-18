package com.skycloud.management.portal.webservice.databackup.po;

import java.util.Date;

/**
 * 用户快照信息实体类 T_SCS_USER_SNAPSHOT
 * <dl>
 * <dt>类名：TPSnapshot</dt>
 * <dd>描述:</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2011-12-31 下午02:19:08</dd>
 * <dd>创建人： 刘江宁</dd>
 * </dl>
 */
public class UserSnapshot {

  /**
   * 主键
   */
  private int ID;
  /**
   * 存储实例唯一标识
   */
  private int INSTANCE_INFO_ID = 0;
  /**
   * Elaster快照id
   */
  private int E_SNAPSHOT_ID = 0;
  /**
   * 快照大小
   */
  private int STORAGE_SIZE;
  /**
   * 注释
   */
  private String COMMENT;
  /**
   * 用户唯一标识
   */
  private int CREATE_USER_ID = 0;
  /**
   * 创建日期
   */
  private Date CREATE_DT;
  /**
   * 状态
   */
  private int STATE = 0;
  /**
   * 异步任务标识
   */
  private int JOB_ID = 0;
  /**
   * 快照类型
   */
  private int TYPE = -1;
  /**
   * 异步表唯一标识
   */
  private int ASYN_ID = 0;

  private int RESOURCE_POOLS_ID = 0;
  /**
   * VDC备份ID
   * 
   * @author shixq
   * @create-time 2012-7-1 下午09:50:50
   * @version $Id:$
   */
  private String VM_BACKUP_ID = "";

  private int OS_TYPE_ID = 0;

  public int getRESOURCE_POOLS_ID() {
    return RESOURCE_POOLS_ID;
  }

  public void setRESOURCE_POOLS_ID(int rESOURCE_POOLS_ID) {
    RESOURCE_POOLS_ID = rESOURCE_POOLS_ID;
  }

  public int getASYN_ID() {
    return ASYN_ID;
  }

  public void setASYN_ID(int aSYN_ID) {
    ASYN_ID = aSYN_ID;
  }

  public int getTYPE() {
    return TYPE;
  }

  public void setTYPE(int tYPE) {
    TYPE = tYPE;
  }

  public int getJOB_ID() {
    return JOB_ID;
  }

  public void setJOB_ID(int jOB_ID) {
    JOB_ID = jOB_ID;
  }

  public int getSTATE() {
    return STATE;
  }

  public void setSTATE(int sTATE) {
    STATE = sTATE;
  }

  public int getID() {
    return ID;
  }

  public void setID(int iD) {
    ID = iD;
  }

  public int getINSTANCE_INFO_ID() {
    return INSTANCE_INFO_ID;
  }

  public void setINSTANCE_INFO_ID(int iNSTANCE_INFO_ID) {
    INSTANCE_INFO_ID = iNSTANCE_INFO_ID;
  }

  public int getE_SNAPSHOT_ID() {
    return E_SNAPSHOT_ID;
  }

  public void setE_SNAPSHOT_ID(int e_SNAPSHOT_ID) {
    E_SNAPSHOT_ID = e_SNAPSHOT_ID;
  }

  public int getSTORAGE_SIZE() {
    return STORAGE_SIZE;
  }

  public void setSTORAGE_SIZE(int sTORAGE_SIZE) {
    STORAGE_SIZE = sTORAGE_SIZE;
  }

  public String getCOMMENT() {
    return COMMENT;
  }

  public void setCOMMENT(String cOMMENT) {
    COMMENT = cOMMENT;
  }

  public int getCREATE_USER_ID() {
    return CREATE_USER_ID;
  }

  public void setCREATE_USER_ID(int cREATE_USER_ID) {
    CREATE_USER_ID = cREATE_USER_ID;
  }

  public Date getCREATE_DT() {
    return CREATE_DT;
  }

  public void setCREATE_DT(Date cREATE_DT) {
    CREATE_DT = cREATE_DT;
  }

  public String getVM_BACKUP_ID() {
    return VM_BACKUP_ID;
  }

  public void setVM_BACKUP_ID(String vM_BACKUP_ID) {
    VM_BACKUP_ID = vM_BACKUP_ID;
  }

  public int getOS_TYPE_ID() {
    return OS_TYPE_ID;
  }

  public void setOS_TYPE_ID(int oSTYPEID) {
    OS_TYPE_ID = oSTYPEID;
  }

}
