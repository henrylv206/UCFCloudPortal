<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@taglib uri="../WEB-INF/parameter/parameter-tags.tld" prefix="skyform"%>
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-我的资源</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
    <link rel="stylesheet" type="text/css" href="../css/myservice.css"/>
    <link rel="stylesheet" type="text/css" href="../css/beautify.select.css"/>
    
	<script src="../js/jquery-1.7.2.min.js"></script>
	<script src="../js/jquery-extend.js"></script>
    <script src="../js/jqFancyTransitions.js"></script>
    <script src="../js/beautifySelect.vdisk.js"></script>
    <script src="../js/jquery.timers.js"></script>
<%--    <script src="../js/Concurrent.Thread-full-20090713.js"></script>--%>
    <script language="javascript" type="text/javascript" src="../js/excanvas.js"></script>
    <script src="../js/jquery.flot.js"></script>
	<script src="../js/mainS.js"></script>
    <script src="../js/Map.js"></script>
	<script src="../js/common.js"></script>
	<script src="../js/commonUtils.js"></script>
    <script src="../js/myservice/resource.js"></script>
	<script src="../js/myservice/resource/vm.js"></script>
	<script src="../js/myservice/resource/vdisk.js"></script>
	<script src="../js/myservice/resource/backup.js"></script>
	<script src="../js/myservice/resource/monitor.js"></script>
	<script src="../js/myservice/resource/bw.js"></script>
	<script src="../js/myservice/resource/tip.js"></script>
	<script src="../js/physical/pmmanage.js"></script>
	<script src="../js/myservice/resource/os.js"></script>
	<script src="../js/myservice/resource/ipsan.js"></script>
	<script src="../js/minicomputer/mcmanage.js"></script>
	<script src="../js/firewall/fwmanage.js"></script>
	<script src="../js/loadbalance/lbmanage.js"></script>
	<script src="../js/bandwidth/bwmanage.js"></script>
	<script src="../js/mall/templateType.js"></script>
    <script src="../js/swfobject.js"></script>
	<script src="../js/objectstorage/osmanage.js"></script>
	<script src="../js/myservice/resource/nasResource.js"></script>

	<script type="text/javascript">
	    //获取参数orderid
	    var Request = new Object();
		Request = getRequest();
		var serviceId = Request['serviceId'];
		var serviceState = Request['serviceState'];
		var resourcePoolsId = Request['resourcePoolsId'];
		var zoneId = Request['zoneId'];
		$("#serviceId").val(serviceId);
		$(document).ready(function() {
			$.resource || ($.resource = resource);  
			resource.init();
			$("#hideOsBtn").hide();
			
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
	            <li><a href="../jsp/my_service.jsp" class="current">我的服务</a></li>
	            <li><a href="../jsp/order_list.jsp">我的订单</a></li>
	            <li><a href="../jsp/user_info.jsp">个人信息</a></li>
	            <li><a href="../jsp/mylog.jsp">日志查询</a><span></span></li>
	          </ul>
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div>
	  </div>
	  <div class="popupDiv1"  id="add_confirm" style="display:none">
	  </div>
	
	  <!-- 虚拟机变更专用DIV -->
	  <div class="popupDiv2" id="vm_modify" style="display:none">
		  <div class="topic">
		    <h4><img src="../images/icons/icon-popup-01.png" /><span id="popTitle"></span></h4>
		  </div>
		  <div class="cont" id="confirm">
		    <table class="poopupTbl">
		      <tbody> 
		        <tr id="tr_cpu">
		          <td class="name">CPU个数：</td>
		          <td>
		            <select style="width:60px" id="cpu_params">
		              <skyform:parameter type="COMBOX_CPUNUM" unit="个"/>
		            </select> 
		          </td>
		        </tr>
		        <tr id="tr_memory">
		          <td class="name">内存大小：</td>
		          <td>
		            <select style="width:60px" id="memory_params">
		              <skyform:parameter type="COMBOX_MEMORYSIZE" unit="M"/>
		            </select> 
		          </td>
		        </tr>	        
			    <tr>
			      <td align="right"><span>申请理由：</span></td>
			      <td>
			        <textarea id="vm_modify_reason" style=" resize:none; width:200px;height:60px; font-size:12px;margin-right:10px;margin-top:10px;"></textarea>
			      </td>
			    </tr>
		      </tbody>
		    </table>
		    <div class="shopArea2">
		      <input type="button" value="确认" class="popupBtn02" id="vm_btn"/>
		      <input type="button" value="取消" class="popupBtn02" id="close" />
	          <input type="hidden" id="vmID" /> 
	          <input type="hidden" id="operType" />
	          <input type="hidden" id="curSize" />
	          <input type="hidden" id="res_code" />      
		    </div>
		  </div>  
	  </div>
	  
	  <!-- 虚拟机名称变更专用DIV -->
	  <div class="popupDiv2" id="vmName_modify" style="display:none">
	      <div class="topic">
	        <h4><img src="../images/icons/icon-popup-01.png" /><span id="popTitle"></span></h4>
	      </div>
	      <div class="cont" id="confirm">
	        <table class="poopupTbl">
	          <tbody> 
	            <tr >
	              <td class="name">虚拟机名称：</td>
	              <td>
	                <input id="vmName" >
	              </td>
	            </tr>
	            <tr>
	              <td align="right"><span>虚拟机描述：</span></td>
	              <td>
	                <textarea id="vmDesc" style=" resize:none; width:200px;height:60px; font-size:12px;margin-right:10px;margin-top:10px;"></textarea>
	              </td>
	            </tr>
	          </tbody>
	        </table>
	        <div class="shopArea2">
	          <input type="button" value="确认" class="popupBtn02" id="vmname_btn"/>
	          <input type="button" value="取消" class="popupBtn02" id="vmname_close" />
	          <input type="hidden" id="vmID" /> 
	          <input type="hidden" id="instanceId" />
	          <input type="hidden" id="resourcePoolsId" />
	        </div>
	      </div>  
	  </div>
	  
	  <!-- 备份变更专用DIV -->
	  <div class="popupDiv2" id="bk_modify" style="display:none">
		  <div class="topic">
		    <h4><img src="../images/icons/icon-popup-01.png" /><span id="bkpopTitle"></span></h4>
		  </div>
		  <div class="cont" id="confirm">
		    <table class="poopupTbl">
		      <tbody> 
		        <tr id="tr_cpu">
		          <td class="name">空间大小：</td>
		          <td>
		            <select style="width:60px" id="bk_params">
		              <skyform:parameter type="COMBOX_BACKUPSIZE" unit="G"/>
		            </select> 
		          </td>
		        </tr>	        
			    <tr>
			      <td align="right"><span>申请理由：</span></td>
			      <td>
			        <textarea id="bk_modify_reason" style=" resize:none; width:200px;height:60px; font-size:12px;margin-right:10px;margin-top:10px;"></textarea>
			      </td>
			    </tr>
		      </tbody>
		    </table>
		    <div class="shopArea2">
		      <input type="button" value="确认" class="popupBtn02" id="bk_modify_ok"/>
		      <input type="button" value="取消" class="popupBtn02" id="bk_close" />
	          <input type="hidden" id="vmID" /> 
	          <input type="hidden" id="operType" />
	          <input type="hidden" id="curSize" />
	          <input type="hidden" id="res_code" />      
		    </div>
		  </div>  
	  </div>  
	  
	  <!-- 带宽变更专用DIV -->
	  <div class="popupDiv2" id="bw_modify" style="display:none">
		  <div class="topic">
		    <h4><img src="../images/icons/icon-popup-01.png" /><span id="ecpopTitle"></span></h4>
		  </div>
		  <div class="cont" id="confirm">
		    <table class="poopupTbl">
		      <tbody> 
		        <tr id="tr_cpu">
		          <td class="name">带宽大小：</td>
		          <td>
		            <select style="width:80px" id="bw_params">
		              <skyform:parameter type="COMBOX_BANDWIDTH" unit="Mbps"/>
		            </select> 
		          </td>
		        </tr>	        
			    <tr>
			      <td align="right"><span>申请理由：</span></td>
			      <td>
			        <textarea id="bw_modify_reason" style=" resize:none; width:200px;height:60px; font-size:12px;margin-right:10px;margin-top:10px;"></textarea>
			      </td>
			    </tr>
		      </tbody>
		    </table>
		    <div class="shopArea2">
		      <input type="button" value="确认" class="popupBtn02" id="bw_modify_ok"/>
		      <input type="button" value="取消" class="popupBtn02" id="bw_close" />
	          <input type="hidden" id="bwinstanceId" />
	          <input type="hidden" id="bwoperType" />
	          <input type="hidden" id="bwcurSize" />
		    </div>
		  </div>  
	  </div>    
	  
	  <div class="sCont">
	  	<%@include file="../component/myservice/resource_list.html" %>
	  <table class="listTbl">
	  	<tbody id="relative_table">
	  	<thead id="relative_thead" style="display:none">
	  	</thead>
	  	</tbody>
	  </table>	
	  </div>  	     
	  <div class="tblWrap">   
	  <input type="hidden" id="fwmanagePage_curPage" value="1" />
	  <input type="hidden" id="fwmanagePage_pageSize" value="10" />
	  <input type="hidden" id="fwmanagePage_totalPage" value="0" />
	  <input type="hidden" id="fwInstanceId"/>
	  <input type="hidden" id="fw_protocol_mul_value" />
	  <input type="hidden" id="fw_ip_s_mul_value" />
	  <input type="hidden" id="fw_ip_d_mul_value" />
	  <span id="fw_rule_num" style="display:none">0</span>
	  </div>
	  <div id="OStorediv" style="float: left;padding-left:7px;" >
	  <iframe id="OStore1" name="OStore1" width="700" height="550" frameborder="0" style="display:none;"></iframe>
		</div>
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
		// close 
		$(document).ready(function(){
			$("#close").click(function(){
			$(".popupDiv1,.shade").fadeOut("fast");
			});
		});
	</script>
</body>
</html> 
