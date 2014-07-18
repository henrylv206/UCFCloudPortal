/**
 * 2011-11-30  下午02:51:17  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午02:51:17
 */
public enum SqlType {
  INFO("ALL");
  private String value = null;

  SqlType(String value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  public static SqlType getByValue(String value) {
    for (SqlType type : SqlType.values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return null;
  }

}
