<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@ page import="com.skycloud.management.portal.service.VNCService" pageEncoding="UTF-8"%>

<%
response.addHeader("Cache-Control", "no-cache");   
response.addHeader("Expires", "Thu, 01 Jan 1970 00:00:01 GMT");
 %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
  <head>
    <title>Web Console</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="VNC,中国电信，云计算">
	<meta http-equiv="description" content="中国电信-园区和企业云宽带服务">
  </head>
  
  <body>   	
   <%
    String vmResId = request.getParameter("vm");
    ApplicationContext cxt = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
   	VNCService service = (VNCService)cxt.getBean("vncService");
   	String str = service.openVM(vmResId);
   	if(str.indexOf("http")!=-1){
   			response.sendRedirect(str);
   	}   	
    else out.println(str);//服务请求不成功
    %>
  </body>
</html>
