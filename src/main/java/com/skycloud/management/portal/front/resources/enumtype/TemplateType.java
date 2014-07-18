/**
 * 2011-11-28  下午05:56:47  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午05:56:47
 */
public enum TemplateType {
  CPU_NUM("CPU_NUM"), MEMORY_SIZE("MEMORY_SIZE");
  private String value = null;

  TemplateType(String value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public String getValue() {
    return value;
  }

  public static TemplateType getByValue(String value) {
    for (TemplateType type : TemplateType.values()) {
      if (type.getValue().equals(value)) {
        return type;
      }
    }
    return null;
  }

}
