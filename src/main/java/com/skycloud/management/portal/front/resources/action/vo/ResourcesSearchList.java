/**
 * 2011-12-6  下午03:46:24  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.action.vo;

import java.io.Serializable;

/**
 * @author shixq
 * @version $Revision$ 下午03:46:24
 */
public class ResourcesSearchList implements Serializable{
  /**
   * 
   */
  private static final long serialVersionUID = 6481897255828736945L;
  private String resultJsonList;// 返回查询结果信息
  private int countTotal;// 总记录数

  public String getResultJsonList() {
    return resultJsonList;
  }

  public void setResultJsonList(String resultJsonList) {
    this.resultJsonList = resultJsonList;
  }

  public int getCountTotal() {
    return countTotal;
  }

  public void setCountTotal(int countTotal) {
    this.countTotal = countTotal;
  }

}
