package com.skycloud.management.portal.front.log.aop;

import java.io.UnsupportedEncodingException;
import java.util.Collection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.service.IUserLogService;

@Aspect
// 该注解标示该类为切面类
public class LogAspect extends BaseAction {

	private static Log log = LogFactory.getLog(LogAspect.class);

	private IUserLogService userLogService;

	// 标注该方法体为后置通知，当目标方法执行成功后执行该方法体
	@AfterReturning("within(com.skycloud.management.portal.*.*.action..*) && @annotation(ulog)")
	public void addLog(JoinPoint jp, LogInfo ulog) throws UnsupportedEncodingException {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		// to fix bug 1820(日志高级搜索——模块列表中出现了其他自服务门户没有的模块操作)
		// 2012-07-25: 不再记录查询日志， operateType 1:insert,2:delete,3:update,4:select
//		if (user != null && ulog.operateType() != 4) {
		//不使用硬编码方式来控制日志记录，修改为通过日志配置方式来控制
		if (user != null && ulog != null) {
			String url = this.getRequest().getRequestURI();
			String paras = jp.getArgs().toString();
			String parameters = ulog.parameters();
			String paraArr[] = parameters.split(",");
			int len = paraArr.length;
			String paraValArr[] = new String[len];
			String paraStr = "";

			for (int i = 0; i < len; i++) {
				paraValArr[i] = this.getRequest().getParameter(paraArr[i]);
				if (paraValArr[i] != null && !"".equals(paraValArr[i])) {
					paraStr = paraStr + paraArr[i] + "=" + paraValArr[i];
					if (i != len - 1) {
						paraStr = paraStr + ",";
					}
				}
			}
			if (paraStr != null) {
				// 限制数据库字段长度
				if (200 < paraStr.length()) {
					paraStr = paraStr.substring(0, 200);
				}
				parameters = paraStr.toString();
			}

			// 获取模块功能名称
			String functionName = ulog.functionName();
			String desc = ulog.desc();
			String functionName_temp = getFunctionNameByParaAndDesc(desc, paraValArr[0]);
			if (functionName_temp != null && !"".equals(functionName_temp)) {
				functionName = functionName_temp;
			}

			String className = jp.getTarget().getClass().toString();// 获取目标类名
			className = className.substring(className.indexOf("com"));
			String signature = jp.getSignature().toString();// 获取目标方法签名
			String methodName = signature.substring(signature.lastIndexOf(".") + 1, signature.indexOf("("));
			String modelName = ulog.moduleName(); // 根据类名获取所属的模块

			String ip = getIpAddrByRequest(this.getRequest());

			TUserLogVO logvo = new TUserLogVO();
			logvo.setParameters(parameters);
			logvo.setUserId(user.getId());
			logvo.setRoleId(user.getRoleApproveLevel());
			logvo.setClassName(className);
			logvo.setMethodName(methodName);

			logvo.setComment(ulog.desc());
			logvo.setMemo(ulog.memo());
			logvo.setModuleName(modelName);
			logvo.setFunctionName(functionName);
			logvo.setIp(ip);
			logvo.setType(ulog.operateType());
			logvo.setUserName(user.getAccount());
			try {
				userLogService.saveLog(logvo,user);
			}
			catch (SCSException e) {
				e.printStackTrace();
			}
		} else {
			log.debug("用户没有登录，不能写入日志");
		}
	}

	/**
	 * 解析方法参数
	 * 
	 * @param parames
	 *            方法参数
	 * @return 解析后的方法参数
	 */
	private String parseParames(Object[] parames) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < parames.length; i++) {
			if (parames[i] instanceof Object[] || parames[i] instanceof Collection) {
				JSONArray json = JSONArray.fromObject(parames[i]);
				if (i == parames.length - 1) {
					sb.append(json.toString());
				} else {
					sb.append(json.toString() + ",");
				}
			} else {
				Object o = parames[i];
				// JSONObject json = JSONObject.fromObject(o);
				if (i == parames.length - 1) {
					sb.append(o.toString());
				} else {
					sb.append(o.toString() + ",");
				}
			}
		}
		String params = sb.toString();
		params = params.replaceAll("(\"\\w+\":\"\",)", "");
		params = params.replaceAll("(,\"\\w+\":\"\")", "");
		return params;
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

	public String getFunctionNameByParaAndDesc(String desc, String paraVal) {
		String functionName = null;
		if (desc != null) {
			String[] descArr = desc.split(",");
			for (int i = 0; i < descArr.length; i++) {
				String[] onedescArr = descArr[i].split("=>");
				if (onedescArr.length > 1) {
					String[] conditionArr = onedescArr[0].split("=");
					String conditionKey = null;
					String conditionVal = null;
					if (conditionArr.length > 1) {
						conditionKey = conditionArr[0];
						conditionVal = conditionArr[1];
					}
					String resultKey = null;
					String resultVel = null;
					if (onedescArr[1] != null) {
						String[] resultArr = onedescArr[1].split("=");
						resultKey = resultArr[0];
						resultVel = resultArr[1];
						if (conditionVal.equals(paraVal)) {
							functionName = resultVel;
							break;
						}
					}
				}

			}
		}
		return functionName;
	}

	public void setUserLogService(IUserLogService userLogService) {
		this.userLogService = userLogService;
	}

	public IUserLogService getUserLogService() {
		return userLogService;
	}

}
