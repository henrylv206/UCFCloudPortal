<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-服务申请成功</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	<link rel="stylesheet" type="text/css" href="../css/login.css"/>
	
	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/mainS.js"></script>
  	<script src="../js/jquery.cookie.js"></script>
	<script src="../js/jquery.jsoncookie.js"></script>		
	<script src="../js/jquery.json-2.2.min.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function(){			
			$("#serviceMenu").show();
		});
	</script>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>

	<div class="page">
	  <div class="cartCont">
	    <div class="cartBox">
	      <div class="topic"><h3>申请成功</h3></div>
	      <div class="cont login">
	      	<div class="orderOk">
	        	<h4>您已成功申请服务！</h4>
	            <p>如果您想查看申请记录，请点击 <a href="order_list.jsp" class="blue">我的订单</a> <br/>
	            感谢您申请我公司服务，您的订单已进入审核队列，审核通过后将开通服务，<br/>请耐心等待！<br/>欢迎您下次继续申请！</p>
	           <input type="button" value="继续申请&nbsp;&gt;&gt;" class="loginBtn02" onclick="javascript:window.location.href='cloud_mall.jsp'"/>
	        </div>
	      </div>
	    </div>
	    <!--end cartBox-->
	  </div>
	  <!--end cartCont-->
	  
	  <div class="clear"></div>	  
	</div>
	
	<script type="text/javascript" language="javascript">
		// table 隔行换色
		$(document).ready(function(){
			$(".listTbl tr").mouseover(function(){
			$(this).addClass("over");
		}).mouseout(function(){
			$(this).removeClass("over");})
			$(".listTbl tr:even").addClass("alt")
		});
	</script>
</body>
</html>
