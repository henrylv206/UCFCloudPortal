var resource = {
    root: "/UCFCloudPortal",  
    actionRoot: "/UCFCloudPortal",
	 init: function(url){
	
		$.ajaxSetup({cache:true}); 
	
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        resource.server = server;
	        resource.server.callback = function(){
	        	//to fix bug [4820]
	        	//resource.server.load("#resource_tbody", ["myservice", "resourceTbody"]);
	        	$(function(){
                    vmmanage.init();
                    tipmanage.init();
                    bkmanage.init();
                    pmmanage.init();
	        	});
	        };
	        server.init();
	        
	        getAllResourceList();
	        
	    });     
	}
};
var curProjectId=0;

var timeoutId = '';
var updateInterval = 30000;
var thread='';
var timerKey="my_resource";
function resUpdate(){  //bug 0003745
    $("body").stopTime(timerKey);
    $("body").everyTime(30000,timerKey,function() {  
        query();
    });
}

function resUpdate1(){//bug 0003230
    if($("#add_confirm").css('display')=='block'){
        return;
    }
    if($("#OStore1").css('display')=='block'){
        return;
    }
    //query();        
    timeoutId = setTimeout(function(){
        query();
        setTimeout(arguments.callee, updateInterval);
    },updateInterval);
    Concurrent.Thread.sleep(30000);
    if(thread!=''){
       thread.kill();
       thread = Concurrent.Thread.create(resUpdate);//实现多线程
    }
}

function query() {
    resUpdate();//bug 0003745
    if($("#add_confirm").css('display')!='none'){
        return;
    }
    if($("#OStore1").css('display')!='none'){
        return;
    }
	getAllResourceList();
}

//所有服务
function getAllResourceList() {
    var Request = new Object();
    Request = getRequest();
    var serviceId = Request['serviceId'];    
    var orderid = Request['orderid'];
	param={serviceId:serviceId};
	var pageSize = Number($("#pageSize").val());
	if(pageSize<1){
		pageSize = 10;
	}
	//to fix bug 3720
	$.ajax({
		type : "POST",
		url : "../cloud_mall/showResourceList2.action?curPage="+$("#curPage").val()+"&pageSize="+pageSize+"&orderId="+orderid,
		datatype : "json",
		data : param,
		async : false,
		global : false,
		success : function(data) {
			if(data != null) {
	            if(typeof(data) == "string" && "false"==data) {
					return;
	            }else {
				  //$("#serviceName").html(data.listResp.serviceName);
				  if(data.listResp.total != undefined) {
						$("#curPage").val(data.listResp.page);
						//fix bug 3620 显示所在页数
						$("#page_specified").val(data.listResp.page);
						//$("#pageSize").val(data.listResp.size);
						var totalPage = Math.ceil(data.listResp.total / pageSize);
						$("#total").val(totalPage);
		                $("#pageInfo").text(totalPage!=0?data.listResp.page+"/"+totalPage:0 + "/" + totalPage);
		                $("#totalPage").text(totalPage);
	              }
	            	var array = data.listResp.list;				
	            	curProjectId = data.listResp.curProjectId;
					$("#resource_tbody").html("");
					if (array != null && array.length > 0) { 
						$(array).each(function(i) {
							//bug 0003170
                            $('#sn').html(array[0].serviceName);
							//奇偶行上色与焦点行事件处理   ninghao 2012-08-24 start
							//奇偶行上色
							var oddEven = (i%2) == 0 ? ' class=\"alt\" ' : ' ';
							//焦点行事件处理
							var focusEven = " onmouseover=$(this).addClass(\"over\") onmouseout=$(this).removeClass(\"over\") ";
							var trEvents = oddEven + focusEven;
							$("#resource_tbody").append(tables(i, array[i], trEvents));								
						});
					}else{
						$("#resource_tbody").html('<tr><td class=\"number\" colspan=\"9\"><span class=\"item\">没有找到记录！</span></td></tr>');
					}
	            }
	        }
		}
	});
}



function tables(i, info, trEvents) {
	var resId = info.id;
	var eId = info.eInstanceId;
	var templateType = info.templateType;//1 is for vm type
	var resName = info.instanceName;
	var resNameAll = resName;
	var resourceInfo = resConfig(info);// 资源配置明细
	resourceInfo=resourceInfo!=undefined?resourceInfo:'';
	var templateTypeName = info.templateTypeName;
	var comment = info.comment;
	comment = comment!=undefined?comment:'';
	var ref = refRes(info);
	ref=ref!=undefined?ref:'';
	var start = (info.createDt + '').replace('T',' ').replace('.0', '');
	var end = (info.lastupdateDt + '').replace('T',' ').replace('.0', '');
	var state =resState(info);
	end = end != undefined ? end : '';
	var resall=resourceInfo;//资源配置明细(title)
	var comall=comment;
	var refResAll='';
	if(isArray(ref)&&ref.length==2&&ref[1]!=undefined){
		refResAll=ref[1];
	}else
	if(isString(ref)){
		refResAll=ref;
	}
	//var refResAll=ref;
	resName = resName.length >17 ? resName.substr(0,17) + '..' : resName;
	resourceInfo = resourceInfo !=undefined && resourceInfo.length > 27 ? resourceInfo.substring(0,27) + '..' : resourceInfo;
	templateTypeName = templateTypeName != undefined ? templateTypeName.replace('模板','') : '';
	comment = comment != undefined && comment.length > 22 ? comment.substr(0, 22) + ".." : comment;
	var vref='';
	if(ref!=undefined){
        if(isArray(ref)&&ref.length==2&&ref[0]!=undefined){
            vref=ref[0];
        	if(ref[1].length>8){
                vref=ref[0].replace(ref[1],ref[1].substr(0,8)+'...');  
        	}
        }else 
        if(isString(ref)){
            vref=ref;
        	if(ref.length>8){
                vref=ref.substr(0,8)+'...';
        	}
        }
	}
	var resName = eId>0&&templateType==1?'<a href="javascript:viewvm('+eId+')" class="quit">'+resName+'</a>':resName;
	var dom = ''
          	+ '<tr '+trEvents+'>'
          	+ '	<td width=\'5%\' >'+resId+'</td>'//bug 0003388
			+ '	<td width=\'10%\' style=\"line-height:18px;\" title=\"'+resNameAll+'\">'+resName+'</td>'
			+ '	<td width=\'25%\' style=\"line-height:18px;\" title=\"'+resall+'\" style=\"text-align:center\">'+resourceInfo+'</td>'// 资源配置明细
			+ '	<td width=\'10%\' style=\"line-height:18px;\" >'+templateTypeName+'</td>'
			+ '	<td width=\'20%\' style=\"line-height:18px;\" title=\"'+comall+'\" style=\"text-align:center\">'+comment+'</td>'
			+ '	<td width=\'10%\' style=\"line-height:18px;\" id=\"ref_'+info.id+'\" title=\"'+refResAll+'\ "style=\"text-align:center\">'+vref+'</td>'
			+ '	<td width=\'10%\' style=\"line-height:18px;\" >'+start+'</td>'
			+ '	<td width=\'10%\' style=\"line-height:18px;\" id=\"state_'+info.id+'\">'+state+'</td>'// 资源状态显示
			+ '</tr>';	
	return dom;
}

function viewvm(id){
  	//防止盗链：判断id(resID)是否属于当前用户    	
	var width = 400;
  	var height = 220;
  	var wt = (screen.width - width) / 2;
  	var wl = (screen.height - height) / 2;	  	
  	var viewer = window.open('UCFCloudPortal/../../jsp/vnc.jsp?vm=' + id,"VNCWindows","width=820,height=640,left="+ wt+ ",resizable=yes,menubar=no,status=no,scrollbars=yes,toolbar=no,location=no");
  	viewer.focus();
}

// 资源配置明细（内容没有用方法替换的是提供了需要的内容参考，最终是要替换成各自的方法的）
//替换方法用xxxConfig(info)格式
function resConfig(info){
	var type=info.templateType;
	if(type==1){//虚拟机
		return vmConfig(info); 
	}else
	if(type==2){//虚拟硬盘
		return vdiskConfig(info);
	}else
	if(type==3){//小型机
		return mcConfig(info);
	}else
	if(type==4){//虚拟机备份
		return backupConfig(info);		
	}else
	if(type==5){//云监控
		return monitorConfig(info);
	}else
	if(type==6){//负载均衡
		var storageSize=info.storageSize;
		return storageSize+'个并发';		
	}else
	if(type==7){//防火墙
		return firewallConfig(info);
	}else
	if(type==8){//公网带宽
		return bwConfig(info);
	}else
	if(type==9){//弹性公网IP
		return tipConfig(info);
	}else
	if(type==10){//X86物理机
		return pmConfig(info);
	}else
	if(type==11){//对象存储
		var storageSize=info.storageSize;
		return storageSize+'G';
	}else
	if(type==12){//弹性块存储
		var storSize=info.storageSize;
		return storSize+'G';		//to fix bug:3807
	}
}

// 资源状态显示
//替换方法用xxxState(info)格式
function resState(info){
	var type=info.templateType;
	var showState='';
	
	switch(type){
		case 1://虚拟机
			showState= vmState(info);break;
		case 2://虚拟硬盘
			showState= vdiskState(info);break;
		case 3://小型机
			showState= mcState(info);break;
		case 4://虚拟机备份
			showState= backupState(info);break;
		case 5://云监控
			showState= monitorState(info);break;
		case 6://负载均衡
			showState= loadBalancedState(info);break;
		case 7://防火墙
			showState= firewallState(info);break;
		case 8://公网带宽
			showState= bwState(info);break;			
		case 9://弹性公网IP
			showState= tipState(info);break;
		case 10://X86物理机
			showState= pmState(info);break;
		case 11://对象存储
			showState= osState(info);break;
		case 12://弹性块存储
			showState= ipsanState(info);break;
		default :
			showState= vmState(info);
	}
	return showState;	
}

// 依赖资源显示
function refRes(info){
	var type=info.templateType;

	rs='';
	switch(type){
		case 2:
			rs=vdiskRefRes(info);
			break;
        case 8:// bug 0003800
            rs=bwRefRes(info);
            break;
		case 9:
			rs=tipRefRes(info);
			break;
		case 12:
			rs=ipsanRefRes(info);
			break;
		default:
			rs='';		
	}	
	
	return rs;
}
//资源操作显示
//替换方法用xxxOperate(info)格式
function operate(info){
	var type=info.templateType;
	var rs='';
	switch(type){
		case 1://虚拟机
			rs = vmOperate(info);break;
		case 2://虚拟硬盘
			rs = vdiskOperate(info);break;
		case 3://小型机
			rs = mcOperate(info);break;
		case 4://虚拟机备份
			rs = backupOperate(info);break;
		case 5://云监控
			rs = monitorOperate(info);break;
		case 6://负载均衡
			rs = loadBalancedOperate(info);break;
		case 7://防火墙
			rs = firewallOperate(info);break;
		case 8://公网带宽
			rs = bwOperate(info);break;			
		case 9://弹性公网IP
			rs = tipOperate(info);break;
		case 10://X86物理机
			rs = pmOperate(info);break;
		case 11://对象存储
			rs = osOperate(info);break;
		case 12://弹性块存储
			rs = ipsanOperate(info);break;	
		default :
			rs = vmOperate(info);
	}
	return rs;
}
    function getMonitorInfo(code){
	    var info = code;
	    info = info.replace("vm", "虚拟机");
		info = info.replace("mc", "小型机");
		info = info.replace("vl", "块存储");
		info = info.replace("lb", "负载均衡");
		info = info.replace("fw", "防火墙");
		info = info.replace("pnip", "公网IP");
		info = info.replace("bw", "公网带宽");
		return info;
	}
// 所有服务的翻页
function turnPage(direc, step) {
	if (direc == 1) {
		if (step == 0) {
			$("#curPage").val($("#total").val());
		} else {
			var nextPage=parseInt($("#curPage").val()) + parseInt(step);
			//$("#curPage").val(parseInt($("#curPage").val()) + parseInt(step));
            if(nextPage>$("#total").val()){//bug 0003227
                return;
            }		
            $("#curPage").val(nextPage);	
		}
	} else {
		if (step == 0) {
			$("#curPage").val(1);
		} else {
			var prePage=parseInt($("#curPage").val()) - parseInt(step);
			//$("#curPage").val(parseInt($("#curPage").val()) - parseInt(step));
            if(prePage<1){//bug 0003227
                return;
            }
            $("#curPage").val(prePage);
		}
	}
    $('#page_specified').val($("#curPage").val());
	query();
}

//到指定页
function turnSpecified() {
	var specPage=$("#page_specified").val();
	var scoreReg = /^[1-9]\d*$/;
	if (!scoreReg.exec($("#page_specified").val())) {
		dialog_confirmation('#dialog_confirmation', "只能输入正整数");
		$("#page_specified").focus();
		return;
	}
    //$('#page_specified').val(specPage);
	if (parseInt($("#page_specified").val()) > parseInt($("#total").val())) {
		$("#curPage").val($("#total").val());
	} else {
		$("#curPage").val(specPage);
	}
	$("#page_specified").val($("#curPage").val());
	query();
}