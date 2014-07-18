<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<title>UCF云平台自服务门户-服务目录</title>
	<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
	<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
	<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/jquery-extend.js"></script>
	<script src="../js/jqFancyTransitions.js"></script>
	<script src="../js/mall/mall.js"></script>
	<script src="../js/mall/templateType.js"></script>
	<script src="../js/common.js"></script>
	<script src="../js/mainS.js"></script>
	<script src="../js/mall/mallitem.js"></script>
	<script type="text/javascript">
		$(document).ready(function() {
			$.mall || ($.mall = mall);
			mall.init();
			listTemplateType();
			$.mallitem || ($.mallitem = mallitem);
			mallitem.init();
			mallitem.loadFirProductItem();
			$(".cont2 li").mousemove(function(){
				$(this).find(".hidenav").show("fast");
			}).mouseleave(function(){
				$(this).find(".hidenav").hide("fast");
			})		
		});
		
		function getSubItems(obj,id){
			mallitem.loadSubProductItem(id);
			$("#item_li_hidenav_"+id).show();
		}
		
		function hideSubItems(id){
			$("#item_li_hidenav_"+id).hide();
		}
	</script>
</head>
<body>
	<jsp:include page="../common/header.jsp"/>

	<div class="page">
	  <div class="sSidebar mSidebar" id="cloud_mall_sidebar">
	    <div class="sMenu">
	      <div class="menu">
	        <div class="cont2">
	          <ul id="productitem_ul">
	            <li style="display:none;" id="productitem_li_{id}" >
	                                               
	            </li>          
	          </ul>          
	        </div>
	        <div class="bottom"></div>
	      </div>
	    </div>
	    <!--end sMenu--> 
	  </div>
	  <!--end sSidebar-->
	  
	  <div class="sCont" id="cloud_mall_rightbar">
	    <div class="mflash">
	      <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" 
	      	codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,19,0" 
	      	width="680" height="250">
	        <param name="movie" value="imageshow.swf" />
	        <param name="quality" value="high" />
	        <param name="wmode" value="transparent" />
	        <embed src="imageshow.swf" width="680" height="250" quality="high" 
	        	pluginspage="http://www.macromedia.com/go/getflashplayer" type="application/x-shockwave-flash"
	         	wmode="transparent"></embed>
	      </object>
	    </div>
	    <!--end mfalsh--> 
	  </div>
	  <!--end sCont-->
	
	   <!-- =================【 服务推荐 】================= -->
	  	<%@include file="../component/mall/commend_service.html"%>   
	
	  
	   <!-- =================【 全部服务 】================= -->
	  	<%@include file="../component/mall/all_service.html"%> 
	  
	  <div class="clear"></div>	  
	</div>
	
	<script type="text/javascript" language="javascript">
		// table 隔行换色
		$(document).ready(function(){
			$(".listTbl tr").mouseover(function(){
			$(this).addClass("over");
		}).mouseout(function(){
			$(this).removeClass("over");});
			$(".listTbl tr:even").addClass("alt");
		});
	</script>
</body>
</html>
