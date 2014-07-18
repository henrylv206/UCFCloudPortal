package com.skycloud.management.portal.front.command.res;

import com.skycloud.management.portal.front.command.impl.QueryCommand;

public class ListTemplates extends QueryCommand {

  public static final String COMMAND = "listTemplates";

  public static final String TEMPLATEFILTER = "templatefilter";

  public static final String ID = "id";

  //查询操作系统时增加资源域作为查询条件
  public static final String ZONEID = "zoneid";

  private String id;

  private String templateFilter;

  private int zoneId;

  private String hypervisor;

  private String name;

  public ListTemplates() {
    super(COMMAND);
  }

  public String getTemplateFilter() {
    return templateFilter;
  }

  public void setTemplateFilter(String templateFilter) {
    this.templateFilter = templateFilter;
    this.setParameter(TEMPLATEFILTER, templateFilter);
  }

public String getId() {
	return id;
}

public void setId(String id) {
	this.id = id;
	 this.setParameter(ID, id);
}

public int getZoneId() {
	return zoneId;
}

public void setZoneId(int zoneId) {
	this.zoneId = zoneId;
	this.setParameter(ZONEID,zoneId);
}


public String getHypervisor() {
	return hypervisor;
}


public void setHypervisor(String hypervisor) {
	this.hypervisor = hypervisor;
}


public String getName() {
	return name;
}


public void setName(String name) {
	this.name = name;
}


}
