<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-购物车</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	
	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/mainS.js"></script>
    <script src="../js/jquery.cookie.js"></script>
	<script src="../js/jquery.jsoncookie.js"></script>		
	<script src="../js/jquery.json-2.2.min.js"></script>	
	<script src="../js/jquery-extend.js"></script>	
			
	<script type="text/javascript"> 
		main.shopcarinit();
	</script>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>

	<div class="page">
	  <div class="cartCont">
	    <div class="breadcrumbs">
	      <img src="../images/MyHome/img-shopingCard-nav1.png" />
	    </div>
	    <div class="cartBox">
	      <div class="topic"><h2>我的购物车</h2></div>
	      <div class="cont">
	        <div class="tblWrap">
	          <table class="listTbl" id="shoppingcar">
	
	          </table>
	        </div>
	        <!--end tblWrap-->
	        <div class="cardTotal">
	          <span id="shopcartTotal">共 <em class="red" id="productnum"></em> 件产品　　　　总计：<em class="red">￥</em><em class="red" id="productMoney"></em></span>
	          <input class="back delcheckproduct" type="button" value="删除选中的服务" />
	        </div>
	        <div class="shopArea1" >
	          <input type="button" class="jxBtn" value="&lt;&lt;继续申请" onclick="javascript:window.location.href='cloud_mall.jsp'"/>
	          <input type="button" class="jxBtn on" value="去结算&gt;&gt;" id="jiesuan"/>
	        </div>
	      </div>
	    </div>   
	  </div>
	 
	  <div class="clear"></div>
	</div> 
	
	<script type="text/javascript" language="javascript"> 
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
