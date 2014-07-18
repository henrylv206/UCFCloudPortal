package com.skycloud.management.portal.exception;

/**
 * 
 * @author shixq
 * @version $Revision$ 下午03:59:14
 */

public class SCSInfoException extends Exception {

  /**
   * 
   */
  private static final long serialVersionUID = 8233407823736047645L;

  // 错误码
  protected String errCode = null;
  // 错误描述
  protected String errDesc = null;
  // 位置描述
  protected String errPosition = null;
  // 异常历史
  protected Throwable history = null;
  // 附加错误描述
  protected String errDescEx = null;

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   */
  public SCSInfoException() {
    super();
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   */
  public SCSInfoException(String _errCode) {
    errCode = _errCode;
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _history
   *          异常历史
   */
  public SCSInfoException(String _errCode, Throwable _history) {
    super(_errCode, _history);
    errCode = _errCode;
    history = _history;
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errDesc
   *          错误描述
   */
  public SCSInfoException(String _errCode, String _errDesc) {
    errCode = _errCode;
    errDesc = _errDesc;
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
  public SCSInfoException(String _errCode, String _errPosition, String _errDesc) {
    errCode = _errCode;
    errPosition = _errPosition;
    errDesc = _errDesc;
  }

  /**
   * 构造方法
   * 
   * @param throwable
   */
  public SCSInfoException(Throwable throwable) {
    super(throwable.getMessage(), throwable);
  }

  /**
   * 构造方法
   * 
   * @param _errCode
   *          错误码
   * @param _errDesc
   *          错误描述
   * @param history
   *          异常历史
   */
  public SCSInfoException(String _errCode, String _errDesc, Throwable _history) {
    super(_errCode, _history);
    errCode = _errCode;
    errDesc = _errDesc;
    history = _history;
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
   * @param history
   *          异常历史
   */
  public SCSInfoException(String _errCode, String _errDesc, String _errPosition, Throwable _history) {
    super(_errCode, _history);
    errCode = _errCode;
    errDesc = _errDesc;
    errPosition = _errPosition;
    history = _history;
  }

  /**
   * 获得错误码
   * 
   * @return
   */
  public String getErrCode() {
    return this.errCode;
  }

  /**
   * 错误描述
   * 
   * @return
   */
  public String getErrDesc() {
    return this.errDesc;
  }

  /**
   * 错误描述
   * 
   * @return
   */
  public String getErrPosition() {
    return this.errPosition;
  }

  /**
   * 获得异常历史
   * 
   * @return
   */
  public Throwable getHistory() {
    return this.history;
  }

  /**
   * 获得异常信息
   * 
   * @return
   */
  public String toString() {
    StringBuffer exString = new StringBuffer();
    // exString.append("错误码：" + this.errCode +"\t");
    exString.append(this.errCode + ": ");
    if (this.errDesc != null) {
      // exString.append("错误描述：" + this.errDesc + "\n");
      // exString.append(this.errDesc + "\n");
      exString.append(getErrDescEx() + "\n");
    }
    if (this.errPosition != null) {
      // exString.append("错误描述：" + this.errDesc + "\n");
      // exString.append(this.errDesc + "\n");
      exString.append(this.errPosition + "\n");
    }
    if (this.history != null) {
      // exString.append("异常历史:\n");
      exString.append(history.toString());
    }
    return exString.toString();
  }

  /**
   * 添加扩展错误描述信息
   * 
   * @param msg
   *          附加的错误描述信息
   */
  public void appendMsg(String msg) {
    this.errDescEx = msg;
  }

  /**
   * 获得扩展后的错误描述信息
   * 
   * @return 完整的错误描述信息，包括基本的和扩展的
   */
  public String getErrDescEx() {
    if (errDescEx == null)
      return errDesc;
    else
      return errDesc + "  " + errDescEx;
  }

  public String getMessage() {
    StringBuffer Msg = new StringBuffer();
    Msg.append("错误码：");
    Msg.append(errCode);
    if (this.errDesc != null) {
      Msg.append("  错误信息：");
      Msg.append(errDesc);
    }
    if (errDescEx != null) {
      Msg.append("(" + errDescEx + ")");
    }
    if (this.errPosition != null) {
      Msg.append(" 出错位置：");
      Msg.append(errPosition);
    }

    return Msg.toString();
  }

}
