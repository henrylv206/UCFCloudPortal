<%@page import="com.skycloud.management.portal.common.utils.ConstDef"%>
<%
//session.removeAttribute("username");
session.removeAttribute(ConstDef.SESSION_KEY_USER);
session.invalidate();
//response.setHeader("","");
response.sendRedirect("login.jsp");
%>