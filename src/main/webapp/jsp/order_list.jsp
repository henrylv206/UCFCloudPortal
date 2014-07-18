<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-我的订单</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	<link rel="stylesheet" type="text/css" href="../css/beautify.select.css"/>
	<link rel="stylesheet" type="text/css" href="../css/datepicker.css"/>
	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/beautifySelect.vdisk.js" type="text/javascript"></script>
	<script src="../js/jqFancyTransitions.js"></script>
	<script src="../js/datepicker.js"></script>
	<script src="../js/myorder/orderS.js"></script>
	<script src="../js/common.js"></script>
	<script src="../js/jquery-ui.min.js"></script>
	<script src="../js/jquery-extend.js"></script>
	<script src="../js/mall/templateType.js"></script>
	
	<script type="text/javascript">
		$(document).ready(function() {
			$.orderS || ($.orderS = orderS);
			$.orderS.init();
			
            //判断是否显示“网络拓扑”菜单
            //showMonitorDiv();
		});	

		// 翻页
		function turnOrderPage(direc, step) {
			if (direc == 1) {
				if (step == 0) {
					$("#curPage").val($("#total").val());
				} else {
					var nextPage = parseInt($("#curPage").val()) + parseInt(step);
		            if(nextPage>$("#total").val()){
		                return;
		            }		
		            $("#curPage").val(nextPage);	
				}
			} else {
				if (step == 0) {
					$("#curPage").val(1);
				} else {
					var prePage = parseInt($("#curPage").val()) - parseInt(step);
		            if(prePage<1){
		                return;
		            }
		            $("#curPage").val(prePage);
				}
			}
		    $("#page_specified").val($("#curPage").val());
			$.order || ($.order = order);
			$.order.queryOrder($("#curPage").val());
		}

		//到指定页
		function turnOrderSpecified() {
			var specPage=$("#page_specified").val();
			var scoreReg = /^[1-9]\d*$/;
			if (!scoreReg.exec($("#page_specified").val())) {
				dialog_confirmation('#dialog_confirmation', "只能输入正整数");
				$("#page_specified").focus();
				return;
			}
			if (parseInt($("#page_specified").val()) > parseInt($("#total").val())) {
				$("#curPage").val($("#total").val());
			} else {
				$("#curPage").val(specPage);
			}
			$("#page_specified").val($("#curPage").val());
			
			$.order || ($.order = order);
			$.order.queryOrder($("#curPage").val());
		}
	</script>
	<style type="text/css">
        /*global css*/
        img{border:0}
        ul,li{list-style:none; margin:0; padding:0}
    </style>
</head>

<body>
	<jsp:include page="../common/header.jsp"/>

	<div class="page">
	  <div class="sSidebar">
	    <div class="sMenu">
	      <div class="menu">
	        <div class="cont">
	          <ul>
	            <li><a href="my_service.jsp">我的服务</a></li>
	            <li><a href="order_list.jsp" class="current">我的订单</a></li>
	            <li><a href="user_info.jsp">个人信息</a></li>
	            <li><a href="mylog.jsp">日志查询</a><span></span></li>
	          </ul>
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div>
	  </div>
	  	  
	  <div class="sCont prorder">	    
	  </div>	  
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
		
		function dateChangeById(flag) {
			var flag = $(".prorder #recentDate").val();
			if (flag == "1"){
		        $(".prorder #startdate").val(order.getMonthBegin());
		    }else if (flag == "2"){
		        $(".prorder #startdate").val(order.getWeekBegin());
		    }else{
		        $(".prorder #startdate").val("");
		    }
		    $(".prorder #enddate").val(order.getDateNow());
		};
	</script>
</body>
</html>
