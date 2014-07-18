/**
 * 2011-11-28  下午05:56:47  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午05:56:47
 */
public enum VMStateType {
  Destroyed("4"), Running("2"),Stopped("5"), Error("-1");
  private String value = "-1";

  VMStateType(String value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  public static VMStateType getByValue(String value) {
    for (VMStateType type : VMStateType.values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return null;
  }
  
  

}
