package com.skycloud.management.portal.common.utils;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.skycloud.management.portal.admin.sysmanage.entity.TMenuBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.dao.impl.AdminDAOImpl;

public class UserLoginFilter implements Filter
{

  private String[] ignores; // 不做登录判断的URL请求
  private String[] portalUri; // portal请求目录
  private String portalRedirect;
  private final String LOGIN_COOKIE = "login_cookie"; 
  //公有云普通用户组
	private final  int DEPT_PUBLIC = 1;
	//私有云普通用户组
	private final  int DEPT_PRIVATE = 2;
	
	private Set<String> urls = new HashSet<String>();

  @Override
  public void destroy()
  {
	  for (String url : urls) {
		  System.out.println(url);
	  }
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
    urls.add(uri);
    if (uri.endsWith(".action") || uri.endsWith(".jsp"))
    {
      int lastIndex = uri.lastIndexOf("/");
      String fname = uri.substring(lastIndex + 1);
      HttpSession session = request.getSession();      
      if (!ArrayUtils.contains(ignores, uri.substring(lastIndex + 1)))
      {      
        loginedUser = (TUserBO) session.getAttribute(ConstDef.SESSION_KEY_USER);
        //无session时，取cookie
        if(loginedUser==null){
        	this.checkeCookie(request); 
        	loginedUser = (TUserBO) session.getAttribute(ConstDef.SESSION_KEY_USER);
        }
        portal = this.isPortalUri(uri);
        if (loginedUser == null)
        {
          // 管理平台无seesion
          if (!portal)
          {
            // 异步的http请求
            if (request.getHeader("x-requested-with") != null
                && request.getHeader("x-requested-with").equalsIgnoreCase("XMLHttpRequest"))
            {
              PrintWriter printWriter = response.getWriter();
              printWriter.print("{\"sessionState\":0}");
              printWriter.flush();
              printWriter.close();
            } else
            {
              response.sendRedirect(request.getContextPath() + "/logout1.jsp");
            }
            return;
          }
          // portal无session
          else
          {
        	if(uri!=null&&uri.indexOf("UCFCloudPortal")>0) {
        		response.sendRedirect(request.getContextPath() + portalRedirect);
        	} else {
        		 PrintWriter printWriter = response.getWriter();
                 printWriter.print("{\"sessionState\":0}");
                 printWriter.flush();
                 printWriter.close();
        	}
            return;
          }
        }
        // 管理平台用户url有效性判断,前台用户无需
        else if (!portal&&null!=loginedUser.getAllActionURL()&&!loginedUser.getAllActionURL().isEmpty())
        {
          if (loginedUser.getAllActionURL().contains(uri.replace("/SkyFormOpt/", "../")))
          {
            if (!validateURI(uri, loginedUser))
              return;
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
    ignores = config.getInitParameter("ignore").split("\\s*,\\s*");
    portalUri = config.getInitParameter("portal").split("\\s*,\\s*");
    portalRedirect = config.getInitParameter("redirect");
    if (portalRedirect == null || portalRedirect.trim().length() == 0)
    {
      portalRedirect = "/selfcare/login.jsp";
    }
  }
  //fix bug 3342 3341
//  public void checkeCookie(HttpServletRequest request){
//	  Cookie[] cookies=request.getCookies();
//	  if(cookies != null && cookies.length > 0){//fix bug 3510 session过期、清除缓存，后报错问题
//	  for(Cookie cookie: cookies){
//	      if(cookie.getName().equals(this.LOGIN_COOKIE)){
//	    	  try {
//				JSONObject jObj = JSONObject.fromObject(URLDecoder.decode(cookie.getValue(),"UTF-8"));
//				if(null!=jObj&&jObj.containsKey("account")&&jObj.containsKey("password")){
//					String account = jObj.getString("account");
//					String password = jObj.getString("password");
//					TUserBO user = null;						
//					if(StringUtils.isNotEmpty(account)&&StringUtils.isNotEmpty(password)){
//						password=DegistUtil.md5(password);
//						ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
//						AdminDAOImpl adminDao = (AdminDAOImpl) context.getBean("adminDao");
//						if(1==ConstDef.getCloudId()){
//							user = adminDao.checkUser(account, password,DEPT_PUBLIC);
//						}
//						else if(2==ConstDef.getCloudId()){			
//							user = adminDao.checkUser(account,password);	
//						}
//						if(null!=user){
//							request.getSession().removeAttribute(ConstDef.SESSION_KEY_USER);
//							request.getSession().setAttribute(ConstDef.SESSION_KEY_USER, user);
//						}					
//					}
//				}
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//	     }
//	  }
//	  }
//  }  
  //fix bug 3305
  public void checkeCookie(HttpServletRequest request){
	  Cookie[] cookies=request.getCookies();
	  if(cookies != null && cookies.length > 0){//fix bug 3510 session过期、清除缓存，后报错问题
	  for(Cookie cookie: cookies){
	      if(cookie.getName().equals(this.LOGIN_COOKIE)){
	    	  try {
				String cookieString = URLDecoder.decode(cookie.getValue(),"UTF-8");
				if(StringUtils.isNotEmpty(cookieString)&&cookieString.indexOf("&")!=-1){
					String[] userlogin = cookieString.split("&");
					if(null!=userlogin&&userlogin.length==2){
						String account = userlogin[0];
						String password = userlogin[1];
						TUserBO user = null;						
						if(StringUtils.isNotEmpty(account)&&StringUtils.isNotEmpty(password)){
							password=DegistUtil.md5(password);
							ApplicationContext context = new ClassPathXmlApplicationContext("spring/applicationContext.xml");
							AdminDAOImpl adminDao = (AdminDAOImpl) context.getBean("adminDao");
							if(1==ConstDef.getCloudId()){
								user = adminDao.checkUser(account, password,DEPT_PUBLIC);
							}
							else if(2==ConstDef.getCloudId()){			
								user = adminDao.checkUser(account,password);	
							}
							if(null!=user){
								request.getSession().removeAttribute(ConstDef.SESSION_KEY_USER);
								request.getSession().setAttribute(ConstDef.SESSION_KEY_USER, user);
							}					
						}
					}					
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	     }
	  }
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
