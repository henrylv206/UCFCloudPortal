<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<c:set var="ctx" value="<%= pageContext.getServletContext().getContextPath()%>" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-登录</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/reset.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/layout.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/myhome.css" />
	<link rel="stylesheet" type="text/css" href="${ctx}/css/login.css" />
	<script src="${ctx}/js/jquery-1.6.4.js"></script>
	<script src="${ctx}/js/jquery.timers.js"></script>
	<script src="${ctx}/js/jquery.cookie.js"></script>
	<script src="${ctx}/js/jquery.jsoncookie.js"></script>
	<script src="${ctx}/js/jquery.json-2.2.min.js"></script>
	<script src="${ctx}/js/jquery-extend.js"></script>
	
	<script src="${ctx}/js/commonUtils.js"></script>
	<script src="${ctx}/js/login/Login.js"></script>
	<script src="${ctx}/js/verifyCode.js"></script>
	<script src="${ctx}/js/findsecrit.js"></script>
	<%-- <script src="${ctx}/js/swfobject.js"></script> --%>	
</head>
<body>
	<jsp:include page="../common/header.jsp" />
	<!--end header-->
	<div class="page">
		<div class="cartCont">
			<div class="cartBox">
				<div class="topic">
					<h3>用户登录</h3>
				</div>
				<div class="cont login">
					<div class="loginBox">
						<div class="tab02">
							<ul class="tab">
								<li id="tab1" class="current">普通登录</li>
							</ul>
							<div class="cont">
								<span class="radius-tr"></span> <span class="radius-bl"></span>
								<span class="radius-br"></span>
								<div id="con_tab_1">
									<table class="loginTbl">
										<tr>
											<td class="name">用户名：</td>
											<td><input type="text" class="textCss06" value=""
												id="zhanghao" /></td>
										</tr>
										<tr>
											<td class="name">密码：</td>
											<td><input type="password" value="" class="textCss06"
												name="" id="password" />&nbsp;<a href="#"
												onclick="findsecrit.showFind();" class="blue">忘记密码？</a></td>
										</tr>
										<tr>
											<td class="name">验证码：</td>
											<td colspan="2"><input id="yzm" type="text"
												class="textCss05" name="" /> <img
												src="<%=request.getContextPath()%>/gernerateCodeServlet"
												id="imgvercode"
												style="vertical-align: middle; cursor: pointer;"
												onclick="changeImg();" title="点击刷新验证码" /> <a
												href="javascript:void(0);" onclick="changeImg();"
												class="blue">&nbsp;看不清，换一张</a></td>
										</tr>
										<tr>
											<td colspan="2"><input type="checkbox" id="remUserName"
												title="要使用此功能，请确认您的浏览器支持cookie；当不再使用此功能时,请清除浏览器cookie" />&nbsp;记住用户名
												<input type="checkbox" id="remLogin"
												title="要使用此功能，请确认您的浏览器支持cookie, 当不再使用此功能时，请点击退出或注销按钮" />&nbsp;自动登录
												<input style="margin-top: 0px;" type="button" value="登 录"
												class="loginBtn01" id="login-submit1" /></td>
										</tr>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>

		<div class="clear"></div>
	</div>
	<!--end page-->
	
	<div id="fullbg"></div>
</body>
</html>
