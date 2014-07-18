<%
String req = request.getParameter("mon");
if("true".equals(req)) {
	session.setAttribute("mon", true);
} else {
	session.setAttribute("mon", false);
}
%>