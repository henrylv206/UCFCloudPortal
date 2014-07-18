/**
 * 2012-3-13  下午07:53:29  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:29
 */
public enum JobStateVDC {
  /**
   * 1为等待执行
   */
  WAIT_RUN(1),
  /**
   * 2为已执行
   */
  OK_RUN(2),
  /**
   * 3为已执行成功，待归档
   */
  PASS_RUN(3),
  /**
   * 4为执行失败，待归档
   */
  NOT_PASS_RUN(4);

  private int value = 0;

  private JobStateVDC(int value) {
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
  public static JobStateVDC valueOf(int v) {
    for (JobStateVDC m : JobStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return JobStateVDC.WAIT_RUN;
  }

}
