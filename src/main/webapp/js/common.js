
var menuController={
  x86:true,
  miniComputer:false,
  network:true,
  storage:true,
  ipsan:false
};

var stateName = {1:"申请",2:"就绪",3:"申请处理中",4:"已删除",5:"已关机",6:"操作执行中",7:"创建失败"};//bug 0003420

function getRequest() {
    var url = location.search; //获取url中"?"符后的字串
    var theRequest = new Object();
    if (url.indexOf("?") != -1) {
        var str = url.substr(1);
        strs = str.split("&");
        for(var i = 0; i < strs.length; i ++) {
            theRequest[strs[i].split("=")[0]]=unescape(strs[i].split("=")[1]);
            }
        }
    return theRequest;
}

function splitDateFormat(dt){
	var ret= new Array();
	ret[0]="";
	ret[1]="";
	var dot = dt.indexOf(".");
	if(dot!=-1){
		dt = dt.slice(0,dot);
	}
	if(dt.indexOf(" "!=-1)){
		ret = dt.split(" ");
	}
	return ret;
}
//显示灰色JS遮罩层
function showBg(ct, content) {
	var bH = $("body").height();
	var bW = $("body").width();
	var objWH = getObjWh(ct);
	$("#fullbg").css({"background-color":"#ffffff",width : bW,height : bH,display : "block","position":"absolute",top:0,"z-index": 2,"opacity":0.6});
	
	var tbT = objWH.split("|")[0] + "px";
	var tbL = objWH.split("|")[1] + "px";
	$("#" + ct).css({display : "block"});
	$("#" + content).html("<div style='text-align:center'>正在加载，请稍后...</div>");
	$(window).scroll(function() {
		resetBg();
	});
	$(window).resize(function() {
		resetBg();
	});
}
function getObjWh(obj) {
	var st = document.documentElement.scrollTop;// 滚动条距顶部的距离
	var sl = document.documentElement.scrollLeft;// 滚动条距左边的距离
	var ch = document.documentElement.clientHeight;// 屏幕的高度
	var cw = document.documentElement.clientWidth;// 屏幕的宽度
	var objH = $("#" + obj).height();// 浮动对象的高度
	var objW = $("#" + obj).width();// 浮动对象的宽度
	var objT = Number(st) + (Number(ch) - Number(objH)) / 2;
	var objL = Number(sl) + (Number(cw) - Number(objW)) / 2;
	return objT + "|" + objL;
}
function resetBg() {
	var fullbg = $("#fullbg").css("display");
	if (fullbg == "block") {
		var bH2 = $("body").height();
		var bW2 = $("body").width();
		$("#fullbg").css({	width : bW2,height : bH2});
		var objV = getObjWh("dialog");
		var tbT = objV.split("|")[0] + "px";
		var tbL = objV.split("|")[1] + "px";
		$("#dialog").css({top : tbT,left : tbL});
	}
}
// 关闭灰色JS遮罩层和操作窗口

function closeBg() {
	$("#fullbg").css("display", "none");	
}

// 关闭操作窗口
$(document).ready(function() {
	$('.close').click(function() {
		$('.popupDiv').hide("fast");
	});
});

// 圆角DIV
//addEvent(window, 'load', initCorners);
//function initCorners() {
//	var setting = {
//		tl : {	radius : 6},
//		tr : {	radius : 6},
//		bl : {	radius : 6},
//		br : {	radius : 6},
//		antiAlias : true
//	};
//	curvyCorners(setting, ".popupDiv");
//}

function dialog_confirmation(dialogID, text) {
  var Sys = {};
  var ua = navigator.userAgent.toLowerCase();
  var s;
  (s = ua.match(/msie ([\d.]+)/)) ? Sys.ie = s[1] :
  (s = ua.match(/firefox\/([\d.]+)/)) ? Sys.firefox = s[1] :
  (s = ua.match(/chrome\/([\d.]+)/)) ? Sys.chrome = s[1] :
  (s = ua.match(/opera.([\d.]+)/)) ? Sys.opera = s[1] :
  (s = ua.match(/version\/([\d.]+).*safari/)) ? Sys.safari = s[1] : 0;
  if(Sys.ie && ('IE: ' + Sys.ie).indexOf("IE: 7") != -1) {
    alert(text);
  }
  else {
    $(dialogID).text(text).dialog({
      autoOpen : true,
      width : 300,
      resizable : false,
      show : 'slide',
      hide : 'slide',
      modal : true,
      buttons : {
        "确定" : function() {
          $(this).dialog("close");
        }
      }
    }).dialog("open");
  }
}

/**
 * 拆分订单用户名和数字编号
 * @param orderStr:订单查询字符串
 * @param type：1返回用户名字符串，2返回订单编号数字串
 * @returns
 */
function getSplitOrder(orderStr,type){
    var orderUser = "";
    var orderCode = "";
    if (orderStr.indexOf("-")>=0){
         var oderstrs = orderStr.split("-"); 
         orderUser = oderstrs[0];
         orderCode = oderstrs[1];
         
    }else{
         if (isDigit(orderStr)==true){
           orderCode = orderStr;
         }else{
           orderUser = orderStr;
         }
    }
    if (type == 1){
        return orderUser;
    }else{
        return orderCode;
    }

}

//校验是否全由数字组成 
function isDigit(s) 
{ 
var patrn=/^\d+$/; 
if (!patrn.exec(s)) return false ;
return true ;
}


function serviceIcon(productId, type, size, typestr, resourcePoolsId, zoneId, special){
	var images='../images/products/';
	var url='../jsp/';
	switch(type){
		case 1://虚拟机
			images += 'vm'+size+'.png';
			url += productId>0?'buyvm.jsp':'buyVMCustom.jsp';
			break;
		case 2://虚拟硬盘
			images += 'vdisk'+size+'.png';
			url += productId>0?'buyvdisk.jsp':'buyVdiskCustom.jsp';
			break;
		case 3://小型机
			images += 'mc'+size+'.png';
			//url += 'buyminicomputer.jsp';
			url += productId>0?'buyminicomputer.jsp':'buyMcCustom.jsp';
			break;
		case 4://虚拟机备份
			images += 'backup'+size+'.png';
			url += productId>0?'buybackup.jsp':'buyBackupCustom.jsp';			
			break;
		case 5://云监控
			images += 'monitor'+size+'.png';
			url += 'buymonitor.jsp';
			break;
		case 6://负载均衡
			images += 'lb'+size+'.png';
			url += productId>0?'buyloadbalance.jsp':'buyloadbalanceCustom.jsp';
			break;
		case 7://防火墙
			images += 'firewall'+size+'.png';
			url += productId>0?'buyfirewall.jsp':'buyfirewallCustom.jsp';
			break;
		case 8://公网带宽
			images += 'bw'+size+'.png';
			url += productId>0?'buybw.jsp':'buyBwCustom.jsp';
			break;
		case 9://弹性公网IP
			images += 'tip'+size+'.png';
			url += 'buytip.jsp';
			break;
		case 10://X86物理机
			images += 'physical'+size+'.png';
			url += productId>0?'buyphysical.jsp':'buyPhysicalCustom.jsp';
			break;
		case 11://对象存储
			images += 'objstor'+size+'.png';
			url += productId>0?'buyos.jsp':'buyosCustom.jsp';
			break;
		case 12://弹性块存储
			images += 'vdisk'+size+'.png';
			url += productId>0?'buyipsan.jsp':'buyipsanCustom.jsp';
			break;
		case 13://Nas文件系统
			images += 'nas'+size+'.png';
			url += productId>0?'buynasstorage.jsp':'buynasstorageCustom.jsp';
			break;	
		case 50://多虚拟机
			images += 'mvm'+size+'.png';
			url += 'buymultivm.jsp';
			break;
		default://虚拟机
			images += 'vm'+size+'.png';
			url += 'buyvm.jsp';
			break;
	}
		url +='?id='+productId;
		url +='&resourcePoolsId='+resourcePoolsId;
		url +='&zoneId='+zoneId;
		url +='&special='+special;		
		var arr = new Array();
        url = url.replace('jsp?id=','jsp?type='+typestr+'&id=');
		arr[0] = images;
		arr[1] = url;
		return arr;
}

/*function addToFavorite() {
    var d=window.location.href;
    var c="SkyForm自服务门户-首页";
    if(document.all)  {
        window.external.AddFavorite(d,c);
    }else{
       if(window.sidebar) {
           window.sidebar.addPanel(c,d,"");
       }else{
            alert("对不起，您的浏览器不支持此操作!\n请您使用菜单栏或Ctrl+D收藏本站。");
       }
     }
 };*/
function cleanCookie(){
//	var data = {
//			"account":"",
//			"password":""
//	};
	var data = "";
	var login_cookie = "login_cookie";
	var expires =7;
	//fix bug 3305
	$.cookie(login_cookie, data, { expires: expires });
	//清除login_cookie fix bug 3341 3342
	//$.JSONCookie(login_cookie, data, { expires: expires }); 	
	//console.log($.JSONCookie(login_cookie));
}


//ICP备案管理, added by 那建林 begin
/*Dcp = new Object();
Dcp.namespace = function(name) { 
  var parent = window; 
  var arr = name.split('.'); 
  for(var i=0; i <arr.length; i++) { 
      if (!parent[arr[i]]) { 
          parent[arr[i]] = new Object();
      } 
      parent = parent[arr[i]]; 
  }
}
Dcp.namespace("Dcp.util");
Dcp.util.getContextPath = function() {
	var tempPath = document.location.pathname.substring(1);
	return '/' + tempPath.substring(0, tempPath.indexOf('/'));
}
Dcp.util.isBlank = function(str) { 
	// start to fix bug 4399 update by qinWenJiang 2012-12-10 -----------------------------------------
	//old if(str == 'undefined' || str == null || str.trim() == '' || str.trim() == 'null') return true;
	 str = $.trim(str);
	 if(str == undefined || str == null || str == '' || str == 'null'){
		 return true;
	 } 
	// end  to fix bug 4399 update by qinWenJiang 2012-12-10 -----------------------------------------
	return false;
}
Dcp.util.dalinReq = function(url, paramObj, sucsCb) {
	$.ajax({
		type     : "POST",
		url      : Dcp.util.getContextPath() + url,
		datatype : "json", //设置获取的数据类型为json
		data     : paramObj,
		async    : false,
		global   : false,
		success  : function(data) {
			sucsCb(data);
		},
		error    : function() {
			alert('对不起, 您的请求['+url+']失败, 请尽快联系管理员处理');
		}
	});
}

Dcp.util.validEmail = function(email) {
	var patten = new RegExp(/^[\w-]+(\.[\w-]+)*@([\w-]+\.)+(com|cn|net)$/);
	return patten.test(email);
}*/

/*function getUserBeianInfo(){
	var beianInfo;
	Dcp.util.dalinReq('/gdMobile/getUserBeianInfo.action', '', function(cbObj) {
		beianInfo = cbObj;
	});
	return beianInfo;
}*/
