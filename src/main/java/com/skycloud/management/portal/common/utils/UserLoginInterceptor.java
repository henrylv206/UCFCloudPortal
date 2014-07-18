package com.skycloud.management.portal.common.utils;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionInvocation;
import com.opensymphony.xwork2.interceptor.Interceptor;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.impl.UserManageServiceImpl;

public class UserLoginInterceptor  implements  Interceptor {
	
	private static Log log = LogFactory.getLog(UserLoginInterceptor.class);
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String intercept(ActionInvocation invocation) throws Exception {
//		System.out.println("<<<教师拦截器初始化...");
		log.debug("<<<教师拦截器初始化...");
        ActionContext ctx = ActionContext.getContext();
        //判断是不是注册用户，如果是注册用户（包含userRegisterPage）则继续执行，否则跳转到index视图
        HttpServletRequest request = (HttpServletRequest)ctx.get(ServletActionContext.HTTP_REQUEST); 
        HttpSession httpSession = request.getSession();
        TUserBO login = (TUserBO) httpSession.getAttribute(ConstDef.SESSION_KEY_USER);
        // sessionStudent

        if(null==login){
        	 return "portalIndex";
        }
        return invocation.invoke();    
	}

}
