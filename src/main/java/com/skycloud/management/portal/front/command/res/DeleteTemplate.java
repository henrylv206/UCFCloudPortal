package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

/**
 * 
 * <dl>
 * <dt>类名：DeleteTemplate</dt>
 * <dd>描述: 创建模板实体类</dd>
 * <dd>公司: 天云科技有限公司</dd>
 * <dd>创建时间：2011-12-6 下午04:58:27</dd>
 * <dd>创建人： 刘江宁</dd>
 * </dl>
 */
public class DeleteTemplate extends QueryCommand {

  public static final String COMMAND = "deleteTemplate";

  public static final String ID = "id";

  public static final String ZONEID = "zoneid";

  /**
   * the ID of the template
   */
  private String id;
  /**
   * the ID of zone of the template
   */
  private String zoneid;

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.setParameter(ID, id);
    this.id = id;
  }

  public String getZoneid() {
    return zoneid;
  }

  public void setZoneid(String zoneid) {
    this.setParameter(ZONEID, zoneid);
    this.zoneid = zoneid;
  }

  public DeleteTemplate() {
    super(COMMAND);
  }
}
