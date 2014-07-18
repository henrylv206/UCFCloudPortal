package com.skycloud.management.portal.exception;

/**
 * 
 * @author shixq
 * @version $Revision$ 下午04:02:02
 */

public class SCSException extends SCSInfoException {
  /**
   * 
   */
  private static final long serialVersionUID = -6744905256356080114L;

  public SCSException(String _errCode) {
    super(_errCode);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _history
   *          异常历史
   */
  public SCSException(String _errCode, Throwable _history) {
    super(_errCode, _history);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errDesc
   *          错误描述
   */
  public SCSException(String _errCode, String _errDesc) {
    super(_errCode, _errDesc);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errPosition
   *          出错位置
   * @param _errDesc
   *          错误描述
   */
  public SCSException(String _errCode, String _errPosition, String _errDesc) {
    super(_errCode, _errPosition, _errDesc);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errDesc
   *          错误描述
   * @param _history
   *          异常历史
   */
  public SCSException(String _errCode, String _errDesc, Throwable _history) {
    super(_errCode, _errDesc, _history);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errDesc
   *          错误描述
   * @param _errPosition
   *          出错位置
   * @param _history
   *          异常历史
   */
  public SCSException(String _errCode, String _errDesc, String _errPosition, Throwable _history) {
    super(_errCode, _errDesc, _errPosition, _history);
  }
}
