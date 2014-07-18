package com.skycloud.management.portal.webservice.databackup.po;

import com.skycloud.management.portal.front.command.impl.QueryCommand;
/**
 * 异步删除卷命令对象实体类
  *<dl>
  *<dt>类名：DBDeleteVolume</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-1-12  下午02:26:57</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class DBDeleteVolume extends QueryCommand {

  public static final String COMMAND = "deleteVolume";
  public static final String ID = "id";
  
  
  private int id;
  
  public DBDeleteVolume() {
    super(COMMAND);
  }
  public DBDeleteVolume(int id) {
    super(COMMAND);
    this.setParameter(ID, id);
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
    this.setParameter(ID, id);
  }
}
