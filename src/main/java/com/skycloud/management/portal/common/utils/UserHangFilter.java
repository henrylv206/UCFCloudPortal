package com.skycloud.management.portal.common.utils;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.skycloud.management.portal.admin.sysmanage.entity.TMenuBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.dao.impl.AdminDAOImpl;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;

public class UserHangFilter implements Filter
{

  private String[] ignores; // 不做登录判断的URL请求
  private String[] portalUri; // portal请求目录
  private String portalRedirect;
  private ApplicationContext context;
  @Override
  public void destroy()
  {
  }

  @Override
  public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
      FilterChain filterChain) throws IOException, ServletException
  {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    TUserBO loginedUser = null;
    boolean portal = false;
    String uri = request.getRequestURI();
    if (uri.endsWith(".action") || uri.endsWith(".jsp"))
    {
      int lastIndex = uri.lastIndexOf("/");
      String fname = uri.substring(lastIndex + 1);
      HttpSession session = request.getSession();
      loginedUser = (TUserBO) session.getAttribute(ConstDef.SESSION_KEY_USER);
      if(loginedUser!=null){	
      	//fix bug 6734
		if (this.checkUserState(loginedUser) == CompanyCheckStateEnum.PAUSE) {
			session.removeAttribute(ConstDef.SESSION_KEY_USER);
		}
      }		
      if (!ArrayUtils.contains(ignores, uri.substring(lastIndex + 1)))
      {
        loginedUser = (TUserBO) session.getAttribute(ConstDef.SESSION_KEY_USER);
        if(loginedUser!=null){	
        	//fix bug 6734
			if (this.checkUserState(loginedUser) == CompanyCheckStateEnum.PAUSE) {						
				portal = this.isPortalUri(uri);
				// 管理平台无seesion
				if (!portal) {
					// 异步的http请求
					if (request.getHeader("x-requested-with") != null
							&& request.getHeader("x-requested-with")
									.equalsIgnoreCase("XMLHttpRequest")) {
						PrintWriter printWriter = response.getWriter();
						printWriter.print("{sessionState:1}");
						printWriter.flush();
						printWriter.close();
					} else {
						response.sendRedirect(request.getContextPath()
								+ "/logout1.jsp");
					}
					return;
				}
				// portal无session
				else {							
					if (StringUtils.isNotEmpty(uri) && portal) {
						response.sendRedirect(request.getContextPath()
								+ portalRedirect);
					} else {
						PrintWriter printWriter = response.getWriter();
						printWriter.print("{sessionState:1}");
						printWriter.flush();
						printWriter.close();
					}
					return;
				}
			}	
		}
      } else if (fname != null && fname.equals("logout.action"))
      {
        if (loginedUser != null)
        {
          session.removeAttribute(ConstDef.SESSION_KEY_USER);
        }
        session.invalidate();
        /**
         * 修改说明：跟踪代码发现，这段代码会使请求略过filterChain.doFilter(request,
         * response)，同时也略过了struts.xml中对logout.actiond的定义，所以注释
         */
        // response.sendRedirect(request.getContextPath() +
        // "/loginInit.action");
        // return;
      }
    }
    filterChain.doFilter(request, response);
    return;
  }
  
//获取用户最新的状态,如果挂起,不能继续操作——中通服
	private int checkUserState(TUserBO account){
	  int state = 0;
	  AdminDAOImpl adminDao = (AdminDAOImpl) context.getBean("adminDao");		
	  TUserBO user = adminDao.findAdminById(account.getId());
	  if(null!=user){
		  state = user.getState();
	  }
	  return state;
}
  
  private boolean validateURI(String uri, TUserBO loginedUser)
  {
    boolean isValidURL = false;
    for (TMenuBO m : loginedUser.getUnsortedMenuList())
    {
      if (m.getActionUrl() == null || m.getActionUrl().equals(""))
        continue;
      if (uri.contains(m.getActionUrl().replace("../", "")))
      {
        isValidURL = true;
        break;
      }
    }
    return isValidURL;
  }

  private boolean isPortalUri(String uri)
  {
    boolean flag = false;
    for (String ss : portalUri)
    {
      if ((uri.endsWith(".action")||uri.endsWith(".jsp"))&&uri.indexOf("/" + ss + "/") >= 0)
      {
        flag = true;
        break;
      }
    }
    return flag;
  }

  private void writeAlertMsg(HttpServletResponse response, String msg, String href)
  {
    try
    {
      response.setHeader("Cache-Control", "no-cache");
      response.setCharacterEncoding("gb2312");
      response.setContentType("text/html");

      ServletOutputStream os = response.getOutputStream();
      os.write("<script language=\"JavaScript\" type=\"text/javascript\">".getBytes());
      if (msg != null && !"".equals(msg.trim()))
      {
        os.write("alert(\"".getBytes());
        os.write(msg.getBytes("gb2312"));
        os.write("\");".getBytes());
      }
      if (href != null)
        os.write(("window.location.href=\"" + href + "\";").getBytes());
      os.write("</script>".getBytes());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  @Override
  public void init(FilterConfig config) throws ServletException
  {
	this.context = WebApplicationContextUtils.getWebApplicationContext(config.getServletContext());  
    ignores = config.getInitParameter("ignore").split("\\s*,\\s*");
    portalUri = config.getInitParameter("portal").split("\\s*,\\s*");
    portalRedirect = config.getInitParameter("redirect");
    if (portalRedirect == null || portalRedirect.trim().length() == 0)
    {
    	portalRedirect = "/jsp/login.jsp";
    }
  }

  public String[] getIgnores()
  {
    return ignores;
  }

  public void setIgnores(String[] ignores)
  {
    this.ignores = ignores;
  }

}
