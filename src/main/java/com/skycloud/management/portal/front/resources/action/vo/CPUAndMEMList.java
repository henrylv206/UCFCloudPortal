/**
 * 2011-12-1  下午08:06:22  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ 下午08:06:22
 */
public class CPUAndMEMList implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 4056243561395225654L;
  private String templateCPUAvailableList;
  private String templateMEMAvailableList;

  public String getTemplateCPUAvailableList() {
    return templateCPUAvailableList;
  }

  public void setTemplateCPUAvailableList(String templateCPUAvailableList) {
    this.templateCPUAvailableList = templateCPUAvailableList;
  }

  public String getTemplateMEMAvailableList() {
    return templateMEMAvailableList;
  }

  public void setTemplateMEMAvailableList(String templateMEMAvailableList) {
    this.templateMEMAvailableList = templateMEMAvailableList;
  }

}
