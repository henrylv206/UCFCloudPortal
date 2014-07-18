package com.skycloud.management.portal.admin.parameters.action;

import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.parameters.entity.Parameters;
import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConstDef;

public class SysParametersAction extends BaseAction {
 
	private static final long serialVersionUID = -1236825151434894097L;

	private final Log log = LogFactory.getLog(SysParametersAction.class);

	private int id;

	private List<Parameters> parameterList;

	private Parameters parameter;

	private String state;

	private int curPage = 1;// 当前页数

	private int pageSize = 10;// 每页显示多少条

	private int countTotal;

	private int qid;

	private String insName;

	private String start;

	private String end;

	private String queryJson;

	private Map<String, Object> listResp;

	private ISysParametersService parametersService;

	private String jsonStr;

	private String resultJsonList;

	private int asyId;

	public int getQid() {
		return qid;
	}

	public void setQid(int qid) {
		this.qid = qid;
	}

	public String getInsName() {
		return insName;
	}

	public void setInsName(String insName) {
		this.insName = insName;
	}

	public String getStart() {
		return start;
	}

	public void setStart(String start) {
		this.start = start;
	}

	public String getEnd() {
		return end;
	}

	public void setEnd(String end) {
		this.end = end;
	}

	public String getResultJsonList() {
		return resultJsonList;
	}

	public void setResultJsonList(String resultJsonList) {
		this.resultJsonList = resultJsonList;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public ISysParametersService getParametersService() {
		return parametersService;
	}

	public void setParametersService(ISysParametersService parametersService) {
		this.parametersService = parametersService;
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

	public String getQueryJson() {
		return queryJson;
	}

	public void setQueryJson(String queryJson) {
		this.queryJson = queryJson;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Parameters> getParameterList() {
		return parameterList;
	}

	public void setParameterList(List<Parameters> parameterList) {
		this.parameterList = parameterList;
	}

	public Parameters getParameter() {
		return parameter;
	}

	public void setParameter(Parameters parameter) {
		this.parameter = parameter;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public int getAsyId() {
		return asyId;
	}

	public void setAsyId(int asyId) {
		this.asyId = asyId;
	}
 

	/**
	 * @return 创建人： 何福康 创建时间：2012-5-17 下午03:08:27
	 */
	// to fix bug:0001678
	public String getNicsDhcpSwitch() {
		int dhcpSwitch = ConstDef.getNicsDhcpSwitch();
		resultJsonList = String.valueOf(dhcpSwitch);
		return SUCCESS;
	}
 
	public String getBWUsedType() {
		int cloudInfo = parametersService.getParameterByType("BANDWIDTH_USED_TYPE");
		resultJsonList = String.valueOf(cloudInfo);
		return SUCCESS;
	}

	 public String getStorageSizeType() {
	    String cloudInfo = parametersService.getParameterValueByType("COMBOX_STORAGESIZE");
	    resultJsonList = cloudInfo;
	    return SUCCESS;
	  }
	
	 
 
	public String insertParameter() {
		JSONObject jsonObject1 = JSONObject.fromObject(jsonStr);
		parameter = (Parameters) JSONObject.toBean(jsonObject1, Parameters.class);
		try {
			parametersService.insertParameter(parameter);
			state = "true";
		}
		catch (Exception e) {
			log.error(e.getMessage());
			state = "false";
			return ERROR;
		}
		return SUCCESS;
	}
  
	// to fix bug 3778
	public String queryInstallPackages() {
		try {
			parameterList = parametersService.queryParameters2(1, 20, "INSTALL_");
		}
		catch (Exception e) {
			log.error(e.getMessage());
			state = "false";
			return ERROR;
		}
		return SUCCESS;
	}
}
