/**
 * 2012-3-13  下午07:53:29  $Id:shixq
 */
package com.skycloud.management.portal.task.vdc.enumtype;

/**
 * @author shixq
 * @version $Revision$ 下午07:53:29
 */
public enum InstanceInfoStateVDC {
  /**
   * 1：申请状态
   */
  APPLY(1),
  /**
   * 2：可用状态
   */
  AVAILABLE(2),
  /**
   * 3：正在处理状态
   */
  PROCESS(3),
  /**
   * 4：已作废；
   */
  DELETE(4),
  /**
   * 5：关机
   */
  SHUTDOWN(5),
  /**
   * 6：命令正在执行中
   */
  RUNNING(6),
  /**
   * 7：暂停
   */
  PAUSE(7),
  /**
   * 9：VM创建失败，VMBak创建、恢复、删除失败
   */
  ERROR(9);

  private int value = 0;

  private InstanceInfoStateVDC(int value) {
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
  public static InstanceInfoStateVDC valueOf(int v) {
    for (InstanceInfoStateVDC m : InstanceInfoStateVDC.values()) {
      if (m.getValue() == v) {
        return m;
      }
    }
    return InstanceInfoStateVDC.RUNNING;
  }

}
