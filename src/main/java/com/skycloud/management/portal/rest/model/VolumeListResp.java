package com.skycloud.management.portal.rest.model;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.rest.entity.Volume;

/**
 * 块存储列表响应对象
 * @author jiaoyz
 */
@XmlRootElement
public class VolumeListResp extends BaseResp {

  private VolumeListReq req;

  private int totalCount;

  List<Volume> result;

  public VolumeListReq getReq() {
    return req;
  }

  public void setReq(VolumeListReq req) {
    this.req = req;
  }

  public int getTotalCount() {
    return totalCount;
  }

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
  }

  @XmlElementWrapper(name="result")
  @XmlElements({@XmlElement(name="volume", type=Volume.class)})
  public List<Volume> getResult() {
    return result;
  }

  public void setResult(List<Volume> result) {
    this.result = result;
  }
}
