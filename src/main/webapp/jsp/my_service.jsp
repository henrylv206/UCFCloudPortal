<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<script src="../js/jquery-1.7.2.min.js"></script>
	<script src="../js/jquery-extend.js"></script>
	<script src="../js/jquery.timers.js"></script>
	<script src="../js/jquery.cookie.js"></script>
	<script src="../js/jquery.jsoncookie.js"></script>
	<script src="../js/jqFancyTransitions.js"></script>
	
	<script src="../js/datepicker.js"></script>
    <script src="../js/beautifySelect.vdisk.js"></script>
    
	<script src="../js/common.js"></script>
	<script src="../js/commonUtils.js"></script>
	<script src="../js/mainS.js"></script>
	<script src="../js/myservice/service.js"></script>
	<script src="../js/mall/templateType.js"></script>
	<!-- <script src="../js/myservice/serviceState.js"></script>	 -->
			
    <!-- <script src="../js/swfobject.js"></script> -->
    
	<script type="text/javascript">
		$(document).ready(function() {
			$.service || ($.service = service);
			service.init();
			listTemplateType2();
            $.BeautifySelect(document.getElementById("state"),{
                //"langue":"EN"         （如果option内部显示文字是英文，添加 langue 属性）                
            });
            $.BeautifySelect(document.getElementById("typeId"),{
                //"langue":"EN"         （如果option内部显示文字是英文，添加 langue 属性）                
            });            
		});
	</script>	
	<title>UCF云平台自服务门户-我的服务</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	<link rel="stylesheet" type="text/css" href="../css/datepicker.css"/>
    <link rel="stylesheet" type="text/css" href="../css/myservice.css"/>
    <link rel="stylesheet" type="text/css" href="../css/beautify.select.css"/>
</head>

<body>		
	<jsp:include page="../common/header.jsp"/>
	
	<div class="page">
	  <div class="sSidebar">
	    <div class="sMenu">
	      <div class="menu">
	        <div class="cont">
	          <ul>
	            <li><a href="my_service.jsp" class="current">我的服务</a></li>
	            <li><a href="order_list.jsp">我的订单</a></li>
	            <li><a href="user_info.jsp">个人信息</a></li>
	            <li><a href="mylog.jsp">日志查询</a><span></span></li>
	          </ul>
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div> 
	  </div>
	 
	  <%@include file="../component/myservice/service_list.html" %>   
	 	  
	  <div class="clear"></div>
	  
		<div class="popupDiv1" id="popupDiv1" style="display: none">
		  <div class="topic">
		    <h4><img src="../images/icons/icon-popup-01.png" />服务续订</h4>
		  </div>
		  <div class="cont">
		    <table class="poopupTbl">
		      <tbody>
		        <tr>
		          <td class="name">已申请周期：</td>
		          <td id="old_period"></td>
		        </tr>
		        <tr>
		          <td class="name">到期时间：</td>
		          <td id="due_date"></td>
		        </tr>
		        <tr>
		          <td class="name">续订申请周期：</td>
		          <td><a class="Txtle">
		          	<input type="text" id="tilength" class="Txtri line6" maxlength="3" value="1"/></a><span id="cycle"></span><span class="red">*</span>
		          	<input type="hidden" id="xd_unit" />
					<input type="hidden" id="xd_id" />  
		          </td>
	
		        </tr>
		      </tbody>
		    </table>
		    <div class="shopArea2">
		      <input type="button" value="确认" class="popupBtn02" onclick="javascript:updateServicePeriod();"/>
		      <input type="button" value="取消" class="popupBtn02" id="close" onclick="javascript:closeContinueDiv();" />
		    </div>
		  </div>
		</div>   
		
		<div class="popupDiv1" id="popupDiv2" style="display: none">
			<div class="topic">
				<h4><img src="../images/icons/icon-popup-01.png" />服务退订</h4>
			</div>
			<div class="cont">
				<table class="poopupTbl">
					<tbody>
						<tr>
							<td class="name">退订原因：</td><!-- to fix bug [3960] -->
							<td><a class="Areale2"><textarea class="Areari2 line5" id="vm_reason"  style= "overflow:hidden; resize:none; "></textarea></a></td>	
						</tr>
					</tbody>
				</table>
				<div class="shopArea2">
					<input type="button" value="确认" class="popupBtn02" onclick="javascript:executeQuit();" /> 
					<input type="button" value="取消" class="popupBtn02" id="close" onclick="javascript:closeQuitDiv();"/>
					<input type="hidden" id="storageSize" />	
					<input type="hidden" id="serviceType" />	
					<input type="hidden" id="serviceID" />		
					<input type="hidden" id="vmID" />					
				    <input type="hidden" id="instanceId" />  
					<input type="hidden" id="ClusterId" />					
				    <input type="hidden" id="res_code" /> 	
				    <input type="hidden" id="order_Id" />
				    
				    
				</div>
			</div>
		</div>
			  	  
	</div>
  
	<script type="text/javascript" language="javascript">
		// table 隔行换色
		$(document).ready(function(){
			$(".listTbl tr").mouseover(function(){
			$(this).addClass("over");
		}).mouseout(function(){
			$(this).removeClass("over");})
			$(".listTbl tr:even").addClass("alt");
		});
	</script>
</body>
</html>
