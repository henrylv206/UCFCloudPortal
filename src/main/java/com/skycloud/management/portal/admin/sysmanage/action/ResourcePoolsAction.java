package com.skycloud.management.portal.admin.sysmanage.action;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.exception.SCSException;

public class ResourcePoolsAction extends BaseAction {
  /**
	 * 
	 */
  private static final long serialVersionUID = -1855184616569484014L;

  private IResourcePoolsService resourcePoolsService;
  private Logger logger = Logger.getLogger(ResourcePoolsAction.class);
  private String returnJson;
  private Map<String, Object> listResp;
  private TResourcePoolsBO resourcePoolsBO;
  private int curPage = 1;// 当前页数
  private int pageSize = 10;// 每页显示多少条
  private int countTotal;
  private String queryJson;
  private String message;

  public String listResourcePools() {
    try {
      listResp = new HashMap<String, Object>();
      List<TResourcePoolsBO> list = this.resourcePoolsService.searchAll();
      listResp.put("list", list);
//       listResp.put("total", list.size());
      // listResp.put("page", 1);
      // listResp.put("size", 10);
    } catch (SCSException e) {
      e.printStackTrace();
      message = "error : " + e.getMessage();
      logger.error(e.getMessage());
      return ERROR;
    }
    return SUCCESS;
  }
  

  public Map<String, Object> getListResp() {
    return listResp;
  }

  public void setListResp(Map<String, Object> listResp) {
    this.listResp = listResp;
  }

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

  public int getCountTotal() {
    return countTotal;
  }

  public void setCountTotal(int countTotal) {
    this.countTotal = countTotal;
  }

  public void setReturnJson(String returnJson) {
    this.returnJson = returnJson;
  }

  public String getReturnJson() {
    return returnJson;
  }

  public void setQueryJson(String queryJson) {
    this.queryJson = queryJson;
  }

  public String getQueryJson() {
    return queryJson;
  }

  public IResourcePoolsService getResourcePoolsService() {
    return resourcePoolsService;
  }

  public void setResourcePoolsService(IResourcePoolsService resourcePoolsService) {
    this.resourcePoolsService = resourcePoolsService;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public TResourcePoolsBO getResourcePoolsBO() {
    return resourcePoolsBO;
  }

  public void setResourcePoolsBO(TResourcePoolsBO resourcePoolsBO) {
    this.resourcePoolsBO = resourcePoolsBO;
  }

}
