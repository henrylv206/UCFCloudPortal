<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="../WEB-INF/parameter/parameter-tags.tld" prefix="skyform"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    String pid = request.getParameter("id");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-虚拟硬盘申请</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>

	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/mainS.js"></script>
	<script src="../js/jquery.cookie.js"></script>
	<script src="../js/jquery.jsoncookie.js"></script>		
	<script src="../js/jquery.json-2.2.min.js"></script>
	<script src="../js/commonUtils.js"></script>
	<script src="../js/common.js"></script>
	<script src="../js/mall/product.js"></script>
	<script src="../js/backupnamage/backUpCustom.js"></script>

	<script type="text/javascript">		
		$(document).ready(function(){
			var productId = <%=pid%>;
			main.init();
			$.product || ($.product = product);
			product.init(productId);	
			$.backUpCustom || ($.backUpCustom = backUpCustom);
			backUpCustom.root="${pageContext.request.contextPath}";	
			backUpCustom.init(productId);
		});
		
		function setTab(name,cursel,n){
			for(i=1;i<=n;i++){
				var menu=document.getElementById(name+i);
				var con=document.getElementById("con_"+name+"_"+i);
				menu.className=i==cursel?"current":"";
				con.style.display=i==cursel?"block":"none";
			}
		}	
	</script>
</head>
<body class="backUp">
	<jsp:include page="../common/header.jsp"/>
 
	<div class="page">
	  <div class="aCont">	 
	    <div class="cartBox" style="margin-top:20px;">
	      <div class="topic">
	        <h4>填写参数</h4>
	      </div>
	      <div class="cont" style="padding:0;" >      
			<div class="txcsLeft">        
				<div class="picArea"><img src="../images/products/backup124.png" /></div>
				<table class="fontbod" height="120px">
		          <tr>
		            <td class="name">备份空间：</td>
		            <td width="100px" id="bkStorageSize"></td>
	<%--	            <td id="price" class="name">价格：</td>--%>
	<%--	            <td id="price2" width="100px">￥<span class="red" id="backupprice"></span></td>--%>
		          </tr>
				</table>
			    <div class="tab02">
			      <ul class="tab">
			        <li id="tab1" onclick="setTab('tab',1,2)" class="current">详细信息</li>
			        <li id="tab2" onclick="setTab('tab',2,2)">帮助信息</li>
			      </ul>
			      <div class="cont">
			        <span class="radius-tr"></span>
			        <span class="radius-bl"></span>
			        <span class="radius-br"></span>
			        <div style="word-break: break-all;" id="con_tab_1"></div>
			        <div id="con_tab_2" style="display:none"> 
			        	创建虚拟机快照需要先申请虚拟机备份服务，请填写服务名称，申请时长，服务描述提交申请，等开通成功后，进入【我的服务】查看和管理申请的资源。如果你的虚拟机备份服务剩余空间不足以再次创建虚拟机快照，请提交虚拟机备份服务变更申请，等待审核通过。
			        </div>
			      </div>
	    		</div>    
			</div>
	    
	        <div class="txcsRight" style="overflow:hidden; padding-bottom:10px; margin-bottom:15px;" >
	           <table class="infoTbl01">
	            <tr>
	              <td class="name">服务名称：</td>
	              <td><a class="Txtle"><input type="text" id="instanceName" class="Txtri line7" value=""/></a></td>
	            </tr>	
	            <tr>
	              <td class="name">备份大小：</td>
	              <td>
						<select class="buyvm disklist" id="disklist" style="width:250px;" onchange="backUpCustom.onChangeStorage()">						
							<skyform:parameter type="COMBOX_BACKUPSIZE" unit="G"/>
						</select>
	              </td> 	
	            </tr>         
	            <tr>
	              <td class="name">描述：</td>
	              <td><a class="Areale"><textarea id="instanceDesc" class="Areari line7"></textarea></a>
	              </td>
	            </tr>
	            <tr>
	              <td class="name">申请时长：</td>            
	              <td><a class="Txtle"><input type="text" id="buyperiod" maxlength="3" class="Txtri line3" value="1"/></a>&nbsp;<span id="td_unit"></span></td>
	            </tr>
	          </table>
	        </div>
	 		<div class="clear"></div>
	 		 
	      </div>
	    </div>
	    
	    <input type="hidden" id="templateId" /> 
	    <input type="hidden" id="nicNumer" /> 
	    <div class="shopArea1">
	      <input type="button" class="jxBtn" value="加入购物车" onclick="backUpCustom.buyVMCustom(0);"/>
	      <input type="button" class="jxBtn on" value="立即申请" onclick="backUpCustom.buyVMCustom(1);"/>
	    </div>
	    <div class="clear"></div>
	  </div>	  
	</div>	 
</body>
</html>