/**
 * 2012-3-13  下午07:53:29  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:29
 */
public enum SgRuleStateVDC {
  /**
   * 0-处理中
   */
  PROCESS(1),
  /**
   * 1-生效 
   */
  PASS(2),
  /**
   * 2-失败
   */
  ERROR(3);

  private int value = 0;

  private SgRuleStateVDC(int value) {
    this.value = value;
  }

  /**
   * @return the value
   */
  public int getValue() {
    return value;
  }

  /**
   * @param v
   * @return
   */
  public static SgRuleStateVDC valueOf(int v) {
    for (SgRuleStateVDC m : SgRuleStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return SgRuleStateVDC.PROCESS;
  }

}
