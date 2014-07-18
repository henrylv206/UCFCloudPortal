<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@page import="java.net.URLEncoder"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="ctx" value="<%= pageContext.getServletContext().getContextPath()%>" />
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-系统消息</title>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/myhome.css"/>
	<link rel="stylesheet" type="text/css" href="${ctx}/css/datepicker.css"/>
	<script src="${ctx}/js/jquery-1.6.4.js"></script>
	<script src="${ctx}/js/jqFancyTransitions.js"></script>
	<script src="${ctx}/js/beautifySelect.vdisk.js" type="text/javascript"></script>
	<script src="${ctx}/js/datepicker.js"></script>
	<script src="${ctx}/js/common.js"></script>
	<script src="${ctx}/js/jquery-ui.min.js"></script>
	<script src="${ctx}/js/jquery-extend.js"></script>
	<script src="${ctx}/js/mall/templateType.js"></script>
	<script src="${ctx}/js/datepicker_zh_CN.js"></script>
	
	<script type="text/javascript">
		var originalHtml;
		$(document).ready(function() {				
			   $("#startDate").datepicker({
			       changeMonth: true,
			       changeYear: true,
			       showWeek: true,
			       showButtonPanel: true,
			       dateFormat: "yy-mm-dd",
			       gotoCurrent : true,
			       showClearButton: true
			   });
			   $("#endDate").datepicker({
			       changeMonth: true,
			       changeYear: true,
			       showWeek: true,
			       showButtonPanel: true,
			       dateFormat: "yy-mm-dd",
			       gotoCurrent : true,
			       showClearButton: true
			   });
			   originalHtml = $("#loglisttab").html();
			   $("#query_btn").click(function(){
			    	queryLogList(1); 	
			   });
					   				
				$("#serviceMenu").show();
				
			 $("input[type='text']").blur(function(){                	
		         var val = $.trim($(this).val());
		         $(this).val(val);
		         if (val.length >0&&!/^[\u4E00-\u9FA5a-zA-Z0-9_,.@\/-]+$/.test(val)) {
		         	alert("该输入不能包含非法字符！");
		             $(this).val("");
		             return;
		         }
		     });
		});	
		 
		function queryLogList(curPage){
			   $("#page_specified").val(curPage);
			   var startDate = $("#startDate").val();
			   var endDate   = $("#endDate").val();
			   var status = $("#status").val();
			   //URLEncoder.encode(key, "utf-8"))
			   var moduleName = $.trim($("#moduleName").val());
			   var functionName = $.trim($("#functionName").val());
			   var targetType = $("#targetType").val();
			   var  url = "${ctx}/log/queryCurrentUserLog.action?pageSize=10";
			   url += "&curPage=" + curPage;
			   
		       if (startDate != "" && endDate != "" && startDate> endDate){
		          	alert("操作时间的'开始时间'不能大于'结束时间'");
		           	return;
		       }
		       if (startDate != "" && endDate != null){
			   		url += "&userLogVO.startDate=" + startDate;
			   }
		       if (endDate != "" && endDate != null){
			   		url += "&userLogVO.endDate=" + endDate;
			   }
			   url += "&userLogVO.status=" + status;
		       if (moduleName != "" && moduleName != null){
			   		url += "&userLogVO.moduleName=" + encodeURIComponent(moduleName);
			   }
		       if (functionName != "" && functionName != null){//fix bug 7668 IE8不支持trim方法，中文查询有问题，需URI编码转换
			   		url += "&userLogVO.functionName=" + encodeURIComponent(functionName);
			   }
			   url += "&userLogVO.targetType=" + targetType;
			   /*
				var jsonStr =  {jsonStr :
					$.toJSON({
							startDate: startDate,
							endDate  : endDate,
							status   : status,
							moduleName   : moduleName,
							functionName : functionName,
							targetType   : targetType
							})
						};
			   //*/
		       $.ajax({
			       	url : url,
			       	type : "GET",
			       	async : false,
			       	dataType:'json',
					//data : jsonStr,
					//contentType : "application/json",
					contentType : "application/x-www-form-urlencoded; charset=utf-8",
					success : function(jsonObj){
						var object = jsonObj.listResp;
						if(object != null && object.total > 0){
							var totalPage = parseInt(object.total / 10);
							if(object.total % 10 != 0){
								totalPage = totalPage + 1;
							}
							$("#total").val(totalPage);
							$("#pageInfo").text(curPage + "/" + totalPage);
							$("#loglisttab").html(originalHtml);
							var template = $("#template_logrow");
			       			var list = object.list;
			       			for(var i = 0;i < list.length; i++){
			       				var t = template.clone();
			       				t.find("#moduleName").text(list[i].moduleName);
			       				t.find("#functionName").text(list[i].functionName);
			       				t.find("#createDtString").text(list[i].createDtString);
			       				t.find("#ip").text(list[i].ip);
			       				t.find("#comment").html(list[i].comment);
			       				$("#loglisttab").append(t.show());
			       			}
			       		}else{
							$("#loglisttab").html(originalHtml);
							$("#pageInfo").text("");
							$("#total").val(0);
			       		}	
			       	}
		       });       
		}
		
		function breakStr(strContent,intLen){
			var strTemp="";
			if(null!=strContent){
				while(strContent.length>intLen){
					strTemp+=strContent.substr(0,intLen)+"</br>"; 
					strContent=strContent.substr(intLen,strContent.length); 
				}
				strTemp+=strContent;
			}	
			return strTemp;
		}
		
		function turnLogPage(direc, step) {
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
		            if(prePage < 1){
		                return;
		            }
		            $("#curPage").val(prePage);
				}
			}
		    $("#page_specified").val($("#curPage").val());
			queryLogList($("#curPage").val());
		}
		
		//到指定页
		function turnLogSpecified() {
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
			queryLogList($("#curPage").val());
			
		}
	</script>
</head>
<body>
	<jsp:include page="/common/header.jsp"/>

	<div class="page">
	  <div class="sSidebar">
	    <div class="sMenu">
	      <div class="menu">
	        <div class="cont">
	          <ul>
	            <li><a href="my_service.jsp">我的服务</a></li>
	            <li><a href="order_list.jsp">我的订单</a></li>
	            <li><a href="user_info.jsp">个人信息</a></li>
	            <li><a href="mylog.jsp" class="current">日志查询</a><span></span></li>
	          </ul>
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div>
	    <!--end sMenu-->
	  </div>
	  <!--end sSidebar-->
	    
	  <div class="sCont">
	    	<div class="breadcrumbs"><span>我的日志</span></div>
		<div class="sShop">
	    <table class="shophead" border="0" cellspacing="0" cellpadding="0">
	      <tr>
	        <td class="line3">模块名称：</td>
	        <td><input class="inpText1" type="text" id="moduleName"/></td>
	        <td class="line3">功能名称：</td>
	        <td><input class="inpText1" type="text" id="functionName"/></td>
	        
	        <td class="line2">状态：</td>
	        <td>
		        <select id="status" name="status" width="100">
		                <option value="-1" >全部</option>
		                <option value="1" >已创建</option>
		                <option value="2" >已查看</option>
		                <option value="3" >已发送</option>
	                    <option value="4" >处理失败</option>
	                    <option value="5" >已处理</option>
	                    <option value="6" >已过期</option>
		        </select>
	        </td>
	      </tr>
	      <tr>
	        <td class="line3">操作时间：</td>
	        <td><input id="startDate" type="text" class="inpText1 query-pull i-date"/></td>
	        <td class="line3">至：</td>
	        <td><input id="endDate"   type="text" class="inpText1 query-pull i-date"/></td>
	        
	        <td class="line3"></td>
	        <td class="line3"><input id="query_btn" type="button" value="查询" class="btnCss01 fr"/></td>
	      </tr>
	    </table>
	    </div>
	    <div class="tblWrap">
	      <table class="listTbl">
	        <thead id="orderlisttop" class="listTbl">
	          <tr>
	            <th width="20%" nowrap="nowrap">模块名称</th>
	            <th width='20%' nowrap="nowrap">功能名称</th>
	            <th width='20%' nowrap="nowrap">操作时间</th>
	            <th width='20%' nowrap="nowrap">操作人IP地址</th>
	            <th width='20%' nowrap="nowrap">备注</th>
	          </tr>
	        </thead>
	      
	        <tbody  id="loglisttab"  class="listTbl">	
	        	<tr style="display: none;" id="template_logrow">
			        <td width='10%'  id="moduleName"></td>
			        <td width='20%'  id="functionName"></td>
			        <td width='20%' id="createDtString"></td>
			        <td width='12%' id="ip"></td>
			        <td width='28%' id="comment" style="text-align: left"></td>
			 	</tr>		
	        </tbody>
	        <tfoot>
	          <tr>
	            <td colspan="7">
	              <ul class="paging">
					<input type="hidden" id="curPage" value="1" />
	          		<input type="hidden" id="pageSize" value="10" />
	          		<input type="hidden" id="total" value="0" />
	          		<input type="hidden" id="targetType" value="log" />
	              <ul class="paging">
	                <li>
		                <a href="#"><img src="${ctx}/images/icons/icon-p-first.gif"  onclick="turnLogPage(-1, 0)" /></a>
		                <a href="#"><img src="${ctx}/images/icons/icon-p-prev.gif"   onclick="turnLogPage(-1, 1)" /></a>
		                <span id="pageInfo"></span>
		                <a href="#"><img src="${ctx}/images/icons/icon-p-next.gif"   onclick="turnLogPage(1, 1)" /></a>
		                <a href="#"><img src="${ctx}/images/icons/icon-p-last.gif"   onclick="turnLogPage(1, 0)" /></a>
	                </li>
	                <li>
	                	第<input type="text" id="page_specified" class="text"/>页
	                	<input type="button" value="GO" class="but"  onclick="turnLogSpecified()"/>
	                </li>
	              </ul>
	            </td>
	          </tr>
	        </tfoot>
	      </table>
	    </div>
	  </div>
	  
	  <div class="clear"></div>	 
	</div>
	
	<script type="text/javascript" language="javascript">
		// table 隔行换色
		$(document).ready(function(){
			$("#loglisttab").ajaxSuccess(function(evt, request, settings){
				$("#loglisttab tr").mouseover(function(){
					$(this).addClass("over");
				}).mouseout(function(){
					$(this).removeClass("over");})
					$("#loglisttab tr:even").addClass("alt");
			});	
		});
	</script>
</body>
</html>
