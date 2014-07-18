<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8"%>
<%@page import="com.skycloud.management.portal.admin.sysmanage.entity.TUserBO"%>
<%@page import="com.skycloud.management.portal.common.utils.ConstDef"%>
<%
	TUserBO user = (TUserBO) session.getAttribute(ConstDef.SESSION_KEY_USER);
%>
<div class="header">
	<div class="logo-nav">
		<img src="<%=request.getContextPath()%>/images/logo-nav.png" />
	</div>
	<!-- deleted -->
	<div class="mysearch" style="display: none">
		<input type="text" class="text" /> <input type="button" class="sbtn" />
	</div>
	<!-- deleted -->
	<div class="nav" style="display: none">
		<div class="nav-adorn"></div>
		<ul class="lnktools">
			<li class="serve"><a id="myService"
				href="<%=request.getContextPath()%>/jsp/my_service.jsp">我的服务</a></li>
			<li class="card"><a id="shoppingCart"
				href="<%=request.getContextPath()%>/jsp/shoppingcart.jsp">去购物车</a></li>
		</ul>
		<ul class="">
			<li><a id="index" href="<%=request.getContextPath()%>/index.jsp">首页</a></li>
			<li><a id="cloudMall"
				href="<%=request.getContextPath()%>/jsp/cloud_mall.jsp">云服务</a></li>
			<li><a id="contact"
				href="<%=request.getContextPath()%>/jsp/contact.jsp">联系我们</a></li>
		</ul>
	</div>

	<div class="nav-down" style="display: block"></div>

	<div id="serviceMenu" class="serviceMenu" style="display: none">
		<div class="nav-adorn"></div>
		<ul class="lnktools">
			<li><a id="PrivateExit" href="javascript:void(0)">退出</a></li>
			<li id="privateSwitch"><a id="PrivatecloudMall"
				href="<%=request.getContextPath()%>/jsp/cloud_mall.jsp">云资源</a></li>
			<li id="privateSwitch"><a id="PrivatecloudMall"
				href="<%=request.getContextPath()%>/jsp/shoppingcart.jsp">购物车</a></li>
			<%
				if (user != null) {
			%>
			&nbsp;&nbsp;&nbsp;&nbsp;登陆用户：<%=user.getAccount()%>
			<%
				}
			%>

		</ul>
	</div>

</div>
<!--end header-->

<script type="text/javascript">
	$(function(){
		$("#PrivateExit").click(function(){
			cleanCookie(); 
	        $.ajax({
	            url: "<%=request.getContextPath()%>/customerAdmin/frontLogout.action",
	            type: "POST",
	            contentType: "application/json",
	            dataType: "json",
	            data:$.toJSON({}),
	            success: function(d) {
	                		location.href = "<%=request.getContextPath()%>/jsp/login.jsp";
				},
				error : function() {
					//console.log(err);
				}
			});
		});
	});

	window.onload = function() {
		var url = window.location.href;
		if (url.indexOf("index.jsp") >= 0) {
			document.getElementById('index').setAttribute("class", "on");
			document.getElementById('cloudMall').setAttribute("class", "");
			document.getElementById('contact').setAttribute("class", "");
			document.getElementById('myService').setAttribute("class", "");
			document.getElementById('shoppingCart').setAttribute("class", "");
		} else if (url.indexOf("cloud_mall.jsp") >= 0
				|| url.indexOf("secondlist.jsp") >= 0
				|| url.indexOf("thirdlist.jsp") >= 0) {
			document.getElementById('index').setAttribute("class", "");
			document.getElementById('cloudMall').setAttribute("class", "on");
			document.getElementById('contact').setAttribute("class", "");
			document.getElementById('myService').setAttribute("class", "");
			document.getElementById('shoppingCart').setAttribute("class", "");
		} else if (url.indexOf("contact.jsp") >= 0) {
			document.getElementById('index').setAttribute("class", "");
			document.getElementById('cloudMall').setAttribute("class", "");
			document.getElementById('contact').setAttribute("class", "on");
			document.getElementById('myService').setAttribute("class", "");
			document.getElementById('shoppingCart').setAttribute("class", "");
		} else if (url.indexOf("my_service.jsp") >= 0
				|| url.indexOf("order_list.jsp") >= 0
				|| url.indexOf("user_info.jsp") >= 0
				|| url.indexOf("messages.jsp") >= 0
				|| url.indexOf("order_service.jsp") >= 0) {
			document.getElementById('index').setAttribute("class", "");
			document.getElementById('cloudMall').setAttribute("class", "");
			document.getElementById('contact').setAttribute("class", "");
			document.getElementById('myService').setAttribute("class", "on");
			document.getElementById('shoppingCart').setAttribute("class", "");
		} else if (url.indexOf("shoppingcart.jsp") >= 0) {
			document.getElementById('index').setAttribute("class", "");
			document.getElementById('cloudMall').setAttribute("class", "");
			document.getElementById('contact').setAttribute("class", "");
			document.getElementById('myService').setAttribute("class", "");
			document.getElementById('shoppingCart').setAttribute("class", "on");
		}
	};
</script>
