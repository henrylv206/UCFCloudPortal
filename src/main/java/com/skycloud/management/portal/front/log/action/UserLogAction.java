package com.skycloud.management.portal.front.log.action;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.service.IUserLogService;

public class UserLogAction extends BaseAction {

	private static final long serialVersionUID = 7118345253981037290L;

	private final Logger logger = Logger.getLogger(UserLogAction.class);

	private IUserLogService userLogService;

	private String jsonStr;

	private List<TUserLogVO> returnList;

	private Map<String, Object> listResp;

	private TUserLogVO userLogVO = new TUserLogVO();

	private String returnJson;

	private String isAdvance;

	private int curPage = 1;// 当前页数

	private int pageSize = 10;// 每页显示多少条

	private int countTotal;

	private String message;

	private String moduleNames;
 
 
	/**
	 * 查询当前用户的日志
	 * 
	 * @return
	 * @throws SQLException 
	 */
	public String queryCurrentUserLog(){
		logger.info("query current log user ...");
		
		try {
//			if(jsonStr == null || "".equals(jsonStr.trim())){
//				returnJson="error : 日志配置为空！";
//				return ERROR;
//			}
//			JSONObject jsonObject1 = JSONObject.fromObject(jsonStr);
//			this.userLogVO = (TUserLogVO) JSONObject.toBean(jsonObject1, TUserLogVO.class);
			
			String account = getCurrentAccount();
			 userLogVO.setAccount(account);
			
			try {
				String key = userLogVO.getFunctionName();
				if(key != null && key.trim() != ""){
					key = new String(key.getBytes("ISO-8859-1"),"UTF-8");
				}
				userLogVO.setFunctionName(key);
				key = userLogVO.getModuleName();
				if(key != null && key.trim() != ""){
					key = new String(key.getBytes("ISO-8859-1"),"UTF-8");
				}
				userLogVO.setModuleName(key);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			countTotal = userLogService.searchAllUserLogAcount(userLogVO);
			
		} catch (SQLException e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}
		
		PageVO page = null;
		
		if (countTotal > 0) {
			page = new PageVO();
			page.setCurPage(curPage);
			page.setPageSize(pageSize);
		}

		try {
			returnList = userLogService.searchUserLogByCondition(userLogVO,page);
		} catch (SCSException e) {
			e.printStackTrace();
			returnJson = "error : " + e.getMessage();
			logger.error(e.getMessage());
		}

		listResp = new HashMap<String, Object>();
		listResp.put("page", getCurPage());
		listResp.put("size", getPageSize());
		listResp.put("list", returnList);
		listResp.put("total", countTotal);
		
		return SUCCESS;
	}

	/**
	 *
	 * @return
	 */
	private String getCurrentAccount(){
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		return user.getAccount();
	}
	
	public int saveLog(TUserLogVO logvo) throws SCSException {
		if (logvo == null) {
			return 0;
		}
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user != null) {
			String ip = getIpAddrByRequest(this.getRequest());
			logvo.setIp(ip);
			logvo.setUserId(user.getId());
			logvo.setRoleId(user.getRoleApproveLevel());
		}
		return userLogService.saveLog(logvo);
	}

	public String getIpAddrByRequest(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	/**
	 * 批量删除日志
	 * 
	 * @return
	 */
	public String batnthDeleteUserLog() {
		String parameters = "批量删除的日志ID:";
		try {
			if (this.getUserLogVO().getIds().size() > 0) {
				for (String id : this.getUserLogVO().getIds()) {
					if (!"".equals(id) && id != null) {
						parameters += " " + id;
						this.userLogService.deleteLog(Integer.valueOf(id));
					}
				}
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		this.message = "success:yes";
		// ConstDef.saveLogInfo(2, "系统管理", "批量删除日志", "UserLogAction",
		// "batnthDeleteUserLog", parameters, "参数描述", "备注");
		return SUCCESS;

	}

	/**
	 * 删除日志
	 * 
	 * @return
	 */
	public String deleteUserLog() {
		String parameters = "批量删除的日志ID:" + this.getUserLogVO().getId();
		try {
			this.userLogService.deleteLog(this.getUserLogVO().getId());
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		this.message = "success:yes";
		// ConstDef.saveLogInfo(2, "系统管理", "批量删除日志", "UserLogAction",
		// "batnthDeleteUserLog", parameters, "参数描述", "备注");
		return SUCCESS;
	}

	 

	/**
	 * 查询日志警报信息
	 * 
	 * @return
	 * @throws Exception
	 */
	public String queryLogWarn() throws Exception {

		return SUCCESS;
	}

 
	public IUserLogService getUserLogService() {
		return userLogService;
	}

	public void setUserLogService(IUserLogService userLogService) {
		this.userLogService = userLogService;
	}

	public String getJsonStr() {
		return jsonStr;
	}

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public List<TUserLogVO> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<TUserLogVO> returnList) {
		this.returnList = returnList;
	}

	public String getReturnJson() {
		return returnJson;
	}

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}

	public String getIsAdvance() {
		return isAdvance;
	}

	public void setIsAdvance(String isAdvance) {
		this.isAdvance = isAdvance;
	}

	public void setUserLogVO(TUserLogVO userLogVO) {
		this.userLogVO = userLogVO;
	}

	public TUserLogVO getUserLogVO() {
		return userLogVO;
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

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getModuleNames() {
		return moduleNames;
	}

	public void setModuleNames(String moduleNames) {
		this.moduleNames = moduleNames;
	}

}
