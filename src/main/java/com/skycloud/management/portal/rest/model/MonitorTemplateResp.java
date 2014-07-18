/**
 * 2012-1-18  下午03:08:14  $Id:shixq
 */
package com.skycloud.management.portal.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.entity.MonitorTemplate;

/**
 * @author shixq
 * @version $Revision$ 下午03:08:14
 */
@XmlRootElement
public class MonitorTemplateResp {

  private int totalCount;

  List<MonitorTemplate> result;

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  public List<MonitorTemplate> getResult() {
    return result;
  }

  public void setResult(List<MonitorTemplate> result) {
    this.result = result;
  }

}
