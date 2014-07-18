/**
 * 2012-3-13  下午07:53:29  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:29
 */
public enum TempleVMStateVDC {
  /**
   * 1：申请；
   */
  APPLY(1),
  /**
   * 2：可用；
   */
  AVAILABLE(2),
  /**
   * 3：正在处理；
   */
  PROCESS(3),
  /**
   * 5：已删除；
   */
  DELETE(5),
  /**
   * 7：操作失败；
   */
  ERROR_OPERATE(7),
  /**
   * 8：资源池可用；
   */
  RESOURCE_POOL_USED(8),
  /**
   * 9；资源池不可用
   */
  RESOURCE_POOL_NOT_USED(9);

  private int value = 0;

  private TempleVMStateVDC(int value) {
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
  public static TempleVMStateVDC valueOf(int v) {
    for (TempleVMStateVDC m : TempleVMStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return TempleVMStateVDC.PROCESS;
  }

}
