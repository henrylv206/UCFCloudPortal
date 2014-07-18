/**
 * 2011-12-1  下午04:48:23  $Id:shixq
 */
package com.skycloud.management.portal.common.entity;

/**
 * @author  shixq
 * @version $Revision$ 下午04:48:23
 */
public class PageVO {
  private int curPage;// 当前页数
  private int pageSize;// 每页显示多少条
  public int getCurPage() {
    return curPage;
  }
  public void setCurPage(int curPage) {
    this.curPage = curPage;
  }
  public int getPageSize() {
    return pageSize;
  }
  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
  }
  
}
