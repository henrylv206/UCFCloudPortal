<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="com.skycloud.management.portal.admin.sysmanage.entity.TUserBO"%>
<%@page import="com.skycloud.management.portal.common.utils.ConstDef"%>
<%
	TUserBO user = (TUserBO)session.getAttribute(ConstDef.SESSION_KEY_USER);
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-个人信息</title>
	
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	<link rel="stylesheet" type="text/css" href="../css/datepicker.css"/>
	
    <script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/jquery-1.7.2.min.js"></script>
	<script src="../js/jquery.json-2.2.min.js"></script>
	<script type="text/javascript" src="../js/server.js"></script>
	<script src="../js/commonUtils.js"></script>
	<script src="../js/login/data.js"></script>		      
	<script src="../js/verifyCode.js"></script>
	<script src="../js/datepicker.js"></script>
	<script src="../js/jquery-extend.js"></script>
	<script src="../js/mall/templateType.js"></script>
	<script src="../js/myservice/changePassword.js"></script>		
	<script src="../js/myservice/user_info.js"></script>  
	
	<script type="text/javascript">
		var userNo = "<%=user.getId()%>";
	
		$(document).ready(function() { 		
			$("#serviceMenu").show();			   
		});	
	</script>
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
	            <li><a href="order_list.jsp">我的订单</a></li>
	            <li><a href="user_info.jsp" class="current">个人信息</a></li>
	            <li><a href="mylog.jsp">日志查询</a><span></span></li>
	          </ul>
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div>
	    <!--end sMenu--> 
	  </div>
	  <!--end sSidebar-->
	  
	  <div class="sCont" >
	    <div class="breadcrumbs"><span>个人信息</span></div>
	    <div class="tab01">
	      <ul class="tab">
	        <li id="tab1" onclick="tab('tab',1,2)" class="current">个人资料</li>
	        <li id="tab2" onclick="tab('tab',2,2)">修改密码</li>
	      </ul>
	      <div id="con_tab_1"  id="infor">
	        <div class="userInfo">
	          <div class="photoDiv">
	           <p><img src="../images/MyHome/img/userPhoto.gif" width="45" height="50"/></p>
	          </div>
	          <div class="infoDiv">
	            <table class="massage" border="1">
	              <tr>
	                <td class="name">用户名：</td>
	                <td class="line10"><%=user.getAccount() %></td>
	              </tr>
	              <tr>
	                <td class="name">邮&nbsp;&nbsp;&nbsp;箱：</td>
	                <td class="line10">
	                	<input id="youxiang" class="inpText1" type="text" maxlength="100" name="youxiang" >
	                	<span class="fontred"></span> <sub class="info"></sub>
	                </td>
	              </tr>
	            </table>
	          </div>
	          <div id="privateUser">
	          <div class="infoDiv">
	          <table class="massage" border="1">
	            <tr>
	              <td class="name ">移动电话：</td>
	              <td class="line10"><input type="text" class="inpText1"  name="pMobile"  id="pMobile" maxlength="16" /><span class="fontred"></span> <sub class="info"></sub></td>
	            </tr>
	            <tr>
	            	<td class="name ">固定电话：</td>
	              	<td ><input type="text" class="inpText1" name="pPhone" id="pPhone" maxlength="16" /><span class="fontred"></span> <sub class="info"></sub></td>
	            </tr>
	            <tr>
	              <td class="name">用户传真：</td>
	              <td class="line10"><input type="text" class="inpText1" name="pFax" id="pFax" maxlength="18" /><span class="fontred"></span> <sub class="info"></sub></td>
	            </tr>	            
	            <tr>
	                <td class="name"></td>
	                <td><input type="button" value="保存" id="pSave" class="btnCss01" /></td>
	            </tr>           
	            </table>
	            </div>
	          </div>	                   
	        </div>
	      </div>
	      <div id="con_tab_2" class="hide">
	        <div class="userInfo">
	          <div class="photoDiv"></div>
	          <div class="infoDiv">
	            <table class="massage">
	              <tr>
	                <td class="name line1">原密码：</td>
	                <td><input type="password" class="inpText1" id="oldpwd"  onblur="oldPwdEquals();"/><span class="onError oldpwd"></span></td>
	              </tr>
	              <tr>
	                <td class="name">新密码：</td>
	                <td><input type="password" class="inpText1" id="newpwd"  onblur="loseBlur();" /><span class="onError newpwd"></span></td>
	              </tr>
	              <tr>
	                <td class="name">确认密码：</td>
	                <td><input type="password" class="inpText1" id="renewpwd"  onblur="loseBlur();" /><span class="onError renewpwd"></span></td>
	              </tr>
	              <tr>
	                <td class="name"></td>
	                <td><input type="button" value="保存" id="savepwd" class="btnCss01" /></td>
	              </tr>
	            </table>
	          </div>
	        </div>
	      </div>
	    </div>
	    <!--end tab01--> 
	  </div>
	  <!--end sCont-->
	  
	  <div class="clear"></div>
	</div>
	
	<script type="text/javascript" language="javascript">			                        
		var companyId = <%=user.getCompId()%>
		// TAB
		function tab(name,cursel,n){
			for(i=1;i<=n;i++) {
				var menu=document.getElementById(name+i);
				var con=document.getElementById("con_"+name+"_"+i);
				menu.className=i==cursel?"current":"";
				con.style.display=i==cursel?"block":"none";
			}
		}
	</script>
</body>
</html>
