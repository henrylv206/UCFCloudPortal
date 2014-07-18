package com.skycloud.management.portal.rest.model;

import javax.xml.bind.annotation.XmlRootElement;

import com.skycloud.management.portal.rest.BaseResp;
import com.skycloud.management.portal.rest.entity.Volume;

/**
 * 获取块存储响应对象
 * @author jiaoyz
 */
@XmlRootElement
public class VolumeGetResp extends BaseResp {

  private Volume volume;

  public Volume getVolume() {
    return volume;
  }

  public void setVolume(Volume volume) {
    this.volume = volume;
  }
}
