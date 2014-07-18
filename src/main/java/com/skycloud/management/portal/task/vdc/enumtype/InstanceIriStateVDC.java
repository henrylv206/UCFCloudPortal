/**
 * 2012-3-13  下午07:53:29  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:29
 */
public enum InstanceIriStateVDC {
  /**
   * 1、申请装载；
   */
  APPLY(1),
  /**
   * 2：已经装载；
   */
  ATTACH(2),
  /**
   * 3：申请卸载；
   */
  APPLY_DETACH(3),
  /**
   * 4：已经卸载；
   */
  DETACH(4),
  /**
   * 5：正在处理中；
   */
  PROCESS(5),
  /**
   * 6：装载失败；
   */
  ERROR_ATTACH(6),
  /**
   * 7：卸载失败
   */
  ERROR_DETACH(7);

  private int value = 0;

  private InstanceIriStateVDC(int value) {
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
  public static InstanceIriStateVDC valueOf(int v) {
    for (InstanceIriStateVDC m : InstanceIriStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return InstanceIriStateVDC.PROCESS;
  }

}
