/**
 * 2012-3-13  下午07:53:58  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:58
 */
public enum AuditStateVDC {
  /**
   * 0为不需要审批
   */
  NO_AUDIT(0),
  /**
   * 1为等待审批
   */
  WAIT_AUDIT(1),
  /**
   * 2为审批通过
   */
  PASS_AUDIT(2),
  /**
   * 4为审批未通过
   */
  NOT_PASS_AUDIT(4);

  private int value = 0;

  private AuditStateVDC(int value) {
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
  public static AuditStateVDC valueOf(int v) {
    for (AuditStateVDC m : AuditStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return AuditStateVDC.NO_AUDIT;
  }
}
