<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="../WEB-INF/parameter/parameter-tags.tld" prefix="skyform"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%
    String pid = request.getParameter("id");
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-虚拟机申请</title>
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
	<script src="../js/vmmanage/VMCustom.js"></script>

	<script type="text/javascript">		
		$(document).ready(function(){
			var productId = <%=pid%>;
			main.init();
			$.product || ($.product = product);
			product.init(productId);
			$.VMCustom || ($.VMCustom = VMCustom);
			VMCustom.root="${pageContext.request.contextPath}";
			VMCustom.init(productId);
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
<body class="VirtualMachine">
	<jsp:include page="../common/header.jsp"/>
 
	<div class="page">
	  <div class="aCont">
	
	    <div class="cartBox" style="margin-top:20px;">
	      <div class="topic">
	        <h4>填写参数</h4>
	      </div>
	      <div class="cont" style="padding:0;" >	
	        <div class="txcsLeft">        
	        	<div class="picArea"><img src="../images/products/vm124.png" /></div>
		        <table class="fontbod">
		          <tr>
		            <td width="25%" align="right">网卡个数：</td>
		            <td id="vmip" align="left">0个</td>
		            <td width="25%" align="right">内存：</td>
		            <td id="vmmemory" align="left">0GB</td>
		          </tr>
		          <tr>
		            <td align="right">带宽大小：</td>
		            <td align="left" id="bandWidth">0Mbps</td>
		            <td align="right">CPU个数：</td>
		            <td align="left" id="vmcpu">0个</td>
		          </tr>	
		          <tr>
		            <td align="right">操作系统：</td>
		            <td align="left" id="vmos" colspan="3"></td><!-- to fix bug [7626] -->
	<%--	            <td align="right">价格：</td>--%>
	<%--	            <td align="left" id="price2"><span id="vmprice" class="red"></span></td>--%>
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
			        	<div id="con_tab_2" style="display:none"> 基于ESXi或XenServer实现的虚拟机管理。请填写服务名称，资源域，申请时长，申请数量，网卡-VLAN及IP地址等信息提交申请，等开通成功后，进入【我的服务】查看申请的资源，通过Web Console、远程桌面或SSH连接到虚拟机桌面进行操作。</div>
			      	</div>
		    	</div>
	        </div>
	
	        <div class="txcsRight" >
	          <table class="infoTbl01">
	            <tr>
	              <td class="name">服务名称：</td>
	              <td><a class="Txtle"><input type="text" id="instanceName" class="Txtri line7" value=""/></a></td>
	            </tr>
	            <tr id="singlePool">
	              <td class="name">资源池位置：</td>
	              <td>
						<select class="buyvm poollist" id="poollist" style="width:250px;" onchange="VMCustom.onChangeResPool(this.value)">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>
	            <tr id="singleZone">
	              <td class="name">资源域：</td>
	              <td>
						<select class="buyvm zonelist" id="zonelist" style="width:250px;" onchange="VMCustom.ChangeZone(this.value)">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>
	            <tr>
	              <td class="name">CPU个数：</td>
	              <td>
						<select class="buyvm cpulist" id="cpulist" style="width:250px;" onchange="VMCustom.onChangeCpu(this.value)">											
							<option value="-1">请选择</option>	
							<skyform:parameter type="COMBOX_CPUNUM" unit="个"/>
						</select>
	              </td> 	
	            </tr>
	            <tr>
	              <td class="name">内存大小：</td>
	              <td>
						<select class="buyvm memorylist" id="memorylist" style="width:250px;" onchange="VMCustom.onChangeMem(this.value)">
							<option value="-1">请选择</option>						
							<skyform:parameter type="COMBOX_MEMORYSIZE" unit="M"/>
						</select>
	              </td> 	
	            </tr>
	            <tr>
	              <td class="name">系统盘OS选择：</td>
	              <td>
						<select class="buyvm oslist" id="oslist" style="width:250px;" onchange="VMCustom.onChangeOS(this.value)">
							<option value="">请选择</option>
						</select>
	              </td> 	
	            </tr>            
	            <tr>
	              <td class="name">存储类型：</td>
	              <td>
						<select class="buyvm storagelist" id="storagelist" style="width:250px;">
							<option value="-1">请选择</option>
						</select>
	              </td> 	
	            </tr>
				<tr>
				  <td class="name">带宽大小：</td>
				  <td>
				  	  <select class="buyvm bandWidthlist" id="bandWidthlist" style="width:250px;" onchange="VMCustom.onChangebandWidth(this.value)">
				  	  	<option value="-1">请选择</option>
				  	  	<skyform:parameter type="COMBOX_VMBANDWIDTH" unit="Mbps" comment="0"/>
					  </select>
				  </td>
				</tr>  
	            <tr>
	              <td class="name">网卡个数：</td>
	              <td>
						<select class="buyvm vlanlist" id="vlanlist" style="width:250px;" onchange="VMCustom.onChangeVlan(this.value)">
							<option value="-1">请选择</option>												
							<skyform:parameter type="COMBOX_NETWORKNUM" unit="个"/>                      
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
	              <td><a class="Txtle"><input type="text" id="buyperiod" maxlength="2" class="Txtri line3" value="1"/></a>&nbsp;<span id="td_unit"></span></td>
	            </tr>
	            <tr>
	              <td class="name">申请数量：</td>
	              <td><a class="Txtle"><input type="text" class="Txtri line3" id="vmbuynum" value="1" maxlength="1" onblur="VMCustom.vmbuynumChange();" /></a>个</td>              
	            </tr>            
	          </table>
	          
	          <!-- 手工输入IP -->
	          <span id="vmbuyIPaddress0" style="display:none">	         
		          <table class="infoTbl01" id="ipInfo">
		            <tr id="vlan1">
		              <td class="name">网卡1-VLAN：</td>
		              <td>
						<select onChange="VMCustom.buyvmIP1Add(0);" class="buyvminput vlan1" id="buyvmvlan1">
							<option value="0">请选择</option>
						</select>
					  </td>
		            </tr>
		            <tr id="ipaddress1">
		              <td class="name">IP地址1：</td>
		              <td>
						<select class="buyvminput " id="buyvmIP1">
							<option value="0">请选择</option>
						</select>
		              </td>
		            </tr>	            
		            <tr id="vlan2" style="display:none">
		              <td class="name">网卡2-VLAN：</td>
		              <td>
						<select onChange="VMCustom.buyvmIP2Add(0);" class="buyvminput vlan2" id="buyvmvlan2">
							<option value="0">请选择</option>
						</select>							
					  </td>
		            </tr>
		            <tr id="ipaddress2" style="display:none">
		              <td class="name">IP地址2：</td>
		              <td>
						<select class="buyvminput " id="buyvmIP2">
							<option value="0">请选择</option>
						</select>
		              </td>
		            </tr>            	            
		            <tr id="vlan3" style="display:none">
		              <td class="name">网卡3-VLAN：</td>
		              <td>
						<select onChange="VMCustom.buyvmIP3Add(0);" class="buyvminput vlan3" id="buyvmvlan3">
							<option value="0">请选择</option>
						</select>							
					  </td>
		            </tr>
		            <tr id="ipaddress3" style="display:none">
		              <td class="name">IP地址3：</td>
		              <td>
						<select class="buyvminput " id="buyvmIP3">
							<option value="0">请选择</option>
						</select>
		              </td>
		            </tr>	            	            
		            <tr id="vlan4" style="display:none">
		              <td class="name">网卡4-VLAN：</td>
		              <td>
						<select onChange="VMCustom.buyvmIP4Add(0);" class="buyvminput vlan4" id="buyvmvlan4">
							<option value="0">请选择</option>
						</select>							
					  </td>
		            </tr>
		            <tr id="ipaddress4" style="display:none">
		              <td class="name">IP地址4：</td>
		              <td>
						<select class="buyvminput " id="buyvmIP4">
							<option value="0">请选择</option>
						</select>
		              </td>
		            </tr>	            	            	            
		          </table>	         
			    </span>
			    	          
				<span id="vmbuyIPaddress1" style="display:none"></span>
				<span id="vmbuyIPaddress2" style="display:none"></span>
				<span id="vmbuyIPaddress3" style="display:none"></span>
				<span id="vmbuyIPaddress4" style="display:none"></span>
				<span id="vmbuyIPaddress5" style="display:none"></span>
				<span id="vmbuyIPaddress6" style="display:none"></span>
				<span id="vmbuyIPaddress7" style="display:none"></span>
		        <span id="vmbuyIPaddress8" style="display:none"></span>	 
	        </div>
	        
	        <ul class="paging3" id="pageControl" style="display:none; margin-left: 552px;width: 290px;">
	          <li>
	          	<a href="javascript:VMCustom.pagefirst();"><img src="../images/icons/icon-p-first.gif" /></a>
	          	<a href="javascript:VMCustom.pageup();"><img src="../images/icons/icon-p-prev.gif" /></a>
	          	<span id="pageNum"></span>
	          	<a href="javascript:VMCustom.pagedown();"><img src="../images/icons/icon-p-next.gif" /></a>
	          	<a href="javascript:VMCustom.pageend();"><img src="../images/icons/icon-p-last.gif" /></a>
	          </li>	          
	          <li>第<input type="text" class="text" readonly id="vmbuyIPpage" />台虚拟机<input style="display:none" type="button" value="GO" class="but" /></li>	          
	        </ul>	        
	        
	 		<div class="clear"></div> 
	      </div>
	    </div>
	    <input type="hidden" id="nicsDhcpSwitch" /> 
	    <input type="hidden" id="templateId" /> 
	    <input type="hidden" id="nicNumer" /> 
	    <div class="shopArea1">
	      <input type="button" class="jxBtn" value="加入购物车" onclick="VMCustom.buyVMCustom(0);"/>
	      <input type="button" class="jxBtn on" value="立即申请" onclick="VMCustom.buyVMCustom(1);"/>
	    </div>
	    <div class="clear"></div>
	  </div>	  
	</div> 
</body>
</html>