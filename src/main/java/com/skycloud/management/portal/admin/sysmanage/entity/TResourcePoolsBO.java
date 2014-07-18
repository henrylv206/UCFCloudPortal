package com.skycloud.management.portal.admin.sysmanage.entity;

/**
 * TOrder entity.
 * 
 * @author hefk
 */

@SuppressWarnings("serial")
public class TResourcePoolsBO implements java.io.Serializable {

  /**
   * 订单日志信息实体对象
   */

  private int id = -1; // 主键
  private String poolName; // 资源池名称
  private String ip; // IP
  private String username; // 用户名
  private String password; // 密码
  private String createDt; // 创建时间
  private int state; // 状态；1：可用；2：不可用
  private int reviewState; // 审核状态 1审核可用2审核不可用3审核暂停
  private String port; // 端口
  private String phyRestPath;//物理机REST 服务路径
  private int type; // 类型
  

  public int getReviewState() {
    return reviewState;
  }

  public void setReviewState(int reviewState) {
    this.reviewState = reviewState;
  }

  /** default constructor */
  public TResourcePoolsBO() {
    super();
  }

  public TResourcePoolsBO(int id, String poolName, String ip, String username, String password, String createDt, int state) {
    super();
    this.id = id;
    this.poolName = poolName;
    this.ip = ip;
    this.username = username;
    this.password = password;
    this.createDt = createDt;
    this.state = state;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getPoolName() {
    return poolName;
  }

  public void setPoolName(String poolName) {
    this.poolName = poolName;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getCreateDt() {
    return createDt;
  }

  public void setCreateDt(String createDt) {
    this.createDt = createDt;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }

  public String getPort() {
    return port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

public String getPhyRestPath() {
	return phyRestPath;
}

public void setPhyRestPath(String phyRestPath) {
	this.phyRestPath = phyRestPath;
}

}