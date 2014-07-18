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
	<script src="../js/vdiskmanage/vDiskCustom.js"></script>

	<script type="text/javascript">		
		$(document).ready(function(){
			var productId = <%=pid%>;
			main.init();
			$.product || ($.product = product);
			product.init(productId);
			$.vDiskCustom || ($.vDiskCustom = vDiskCustom);
			vDiskCustom.root="${pageContext.request.contextPath}";
			vDiskCustom.init(productId);
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

<body class="VirtualDisk"> 
	<jsp:include page="../common/header.jsp"/>
 
	<div class="page">
	  <div class="aCont">
	 
	    <div class="cartBox" style="margin-top:20px;">
	      <div class="topic">
	        <h4>填写参数</h4>
	      </div>
	      <div class="cont" style="padding:0;" >
	      
	        <div class="txcsLeft">        
	        	<div class="picArea"><img src="../images/products/vdisk124.png" /></div>
		        <table class="fontbod" height="120px">
		         <tr>
	           	 	<td class="name">磁盘空间：</td>
	            	<td width="100px" id="vDiskStorageSize"></td>
	<%--            	<td id="price" class="name">价格：</td>--%>
	<%--            	<td id="price2" width="120px">￥<span class="red" id="vDiskprice"></span></td>--%>
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
		        	<div id="con_tab_2" style="display:none"> 通过虚拟化技术将磁盘LUN格式化为Hypervisor能够识别的文件系统，通过Hypervisor集中管理和分配。请填写服务名称，资源域，申请时长及描述提交申请，等开通成功后，进入【我的服务】查看申请的资源，可将虚拟硬盘挂载到某个虚拟机上进行使用。</div>
		      	</div>
		    	</div>
	        </div>
	        
	        <div class="txcsRight" style="overflow:hidden; padding-bottom:10px; margin-bottom:15px;" >
	           <table class="infoTbl01">
	            <tr>
	              <td class="name">服务名称：</td>
	              <td><a class="Txtle"><input type="text" id="instanceName" class="Txtri line7" value=""/></a></td>
	            </tr>
	            <tr id="tr_pool" style="display:none">
	              <td class="name">资源池位置：</td>
	              <td>
						<select class="buyvm poollist" id="poollist" style="width:250px;" onchange="vDiskCustom.onChangeResPool(this.value)">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>
	            <tr id="tr_zone" style="display:none">
	              <td class="name">资源域：</td>
	              <td>
						<select class="buyvm zonelist" id="zonelist" style="width:250px;" onchange="vDiskCustom.onChangeHost(this.value)">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>
	            
	            <tr style="display:none">
	              <td class="name">系统盘OS选择：</td>
	              <td>
						<select class="buyvm oslist" id="oslist" style="width:250px;">
							<option value="">请选择</option>
						</select>
	              </td> 	
	            </tr>              
	                       
	            <tr id="tr_storageType" style="display:none">
	              <td class="name">存储类型：</td>
	              <td>
						<select class="buyvm storagelist" id="storagelist" style="width:250px;" onchange="vDiskCustom.onChangeVlan(this.value)">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>            
	            <tr>
	              <td class="name">磁盘大小：</td>
	              <td>
						<select class="buyvm disklist" id="disklist" style="width:250px;" >
							<option value="-1">请选择</option>
							<skyform:parameter type="COMBOX_STORAGESIZE" unit="G"/>
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
	 
	    <div class="shopArea1">
	      <input type="button" class="jxBtn" value="加入购物车" onclick="vDiskCustom.buyVMCustom(0);"/>
	      <input type="button" class="jxBtn on" value="立即申请" onclick="vDiskCustom.buyVMCustom(1);"/>
	    </div>
	    <div class="clear"></div>
	  </div>	  
	</div> 
</body>
</html>