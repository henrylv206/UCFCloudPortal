package com.skycloud.management.portal.rest;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * 基本响应对象
 * @author jiaoyz
 */
@XmlRootElement
public class BaseResp {

  private String resCode; //接口返回码

  public String getResCode() {
    return resCode;
  }

  public void setResCode(String resCode) {
    this.resCode = resCode;
  }
}
