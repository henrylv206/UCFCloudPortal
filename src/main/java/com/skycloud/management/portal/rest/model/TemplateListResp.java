package com.skycloud.management.portal.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.rest.entity.VolumeTemplate;

/**
 * 获取模板列表响应对象
 * @author jiaoyz
 */
@XmlRootElement
public class TemplateListResp extends BaseResp {

  private int totalCount; //总记录数

  private List<VolumeTemplate> result; //记录集

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  @XmlElementWrapper(name="result")
  @XmlElements({@XmlElement(name="Template", type=VolumeTemplate.class)})
  public List<VolumeTemplate> getResult() {
    return result;
  }

  public void setResult(List<VolumeTemplate> result) {
    this.result = result;
  }
}
