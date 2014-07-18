/**
 * 物理机服务资源管理：
 * 物理机状态查询
 * 管理执行操作（开机、关机、重启、续订、退订等）
 * 物理机状态：Running-运行中,Starting-正在启动,Stopping-正在关机,Stopped-已关机,Released-已释放
 * ninghao 2012-08-30
 */
var pmmanage = {
	root:"/UCFCloudPortal",
	strCloud: "",
	pmcomputer:"",
	
	//初始化函数
    init : function(productId) {
    	pmmanage.strCloud = "1";
    	
		pmmanage.template_width = jQuery.makeArray();

		main.getWhichMonitor();//bug 0006741    
		pmmanage.PublicPrivateSwitch();
    	pmmanage.strCloud = ""+pmmanage.getCloudInfo();//fix bug7812私有云默认有监控
	
		//效验服务续订录入的周期
		pmmanage.validate();
		
		//产品续订相关功能
		$("#pop_erneuern #InputYes").click(function(){
			if(confirm("确定要续订该产品吗？")){
				pmmanage.updateInstancePeriod();
			}
		});
		$("#pop_erneuern #InputNo").click(function(){
			 $("#pop_erneuern").hide();
			 $("#pop_erneuern .tilength").val(1);
		});
    },

// -------------------------------产品续订-start--------------------------------------//
	showXDDiv : function(type,id){
		var tdObj = $("#td_extend_"+ id);
	    $("#pop_erneuern").css("top", tdObj[0].offsetTop+40).css("left", tdObj[0].offsetLeft - 80).css("display", "block");
	    pmmanage.getInstancePeriodInfo(id);
    },
    pmRenewal : function(type,id){
    	pmmanage.showProductXDDiv(type,id);
    },
    showProductXDDiv : function(type,id){
		var tdObj = $("#td_bw_" + type + "_" + id);
		$("#pop_erneuern").css("top", tdObj[0].offsetTop+40).css("left", tdObj[0].offsetLeft - 100).css("display", "block");
		pmmanage.getInstancePeriodInfo(id);
		$("#popwindow").show();
	},
	PublicPrivateSwitch : function() {			 
	    $.getScript(pmmanage.root+"/js/privateSkySwitch.js", function(e){
//	    	pmmanage.privateSkySwitch = privateSkySwitch;
	    	pmmanage.strCloud = privateSkySwitch.getCloudInfo();
	    });
	},
	//公有云私有云判断
	getCloudInfo: function(){
	    var cloudInfo = 0;
	    $.ajax({
	      url: pmmanage.root + "/sysParameters/getCloudInfo.action",
	      type: 'POST',
	      dataType: 'json',
	      async: false,
	      success: function(data) {
	        cloudInfo = data;
	      }
	    });
	    return cloudInfo;
	  },
	//获取服务实例周期
	getInstancePeriodInfo : function(id) {
		$.ajax({
			url : "/UCFCloudPortal/order/findInstancePeriodById.action?id="+id,
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				if (data != null) {
					
					$(".popwindow #old_period").html(data.buyPeriod);
					$(".popwindow #due_date").html(data.expireDateString);
					//to fix bug [1829]
		        	if(pmmanage.strCloud == "1"){    		        		
		        		$(".popwindow .cycle").html(data.price+"元/"+data.unitString);
		        	} 
		    		if(pmmanage.strCloud == "2"){    		    			
		    			$(".popwindow .cycle").html(data.unitString);
		    		} 										
					$("#pop_erneuern #xd_unit").val(data.unit);
					$("#pop_erneuern #xd_id").val(id);
				}
			}
		});
	},
	//续订：更新服务实例周期
	updateInstancePeriod : function(){
		$.ajax({
			url : "/UCFCloudPortal/order/updateInstancePeriod.action?id="+Number($("#pop_erneuern #xd_id").val()),
			type : 'POST',
			data : {unit:$("#pop_erneuern #xd_unit").val(),num:Number($("#pop_erneuern .tilength").val())},
			dataType : 'json',
			success : function(data) {
				if(data.indexOf("error") == 0) {
					alert("失败："+data);
				}else{
					alert(data);
					$("#pop_erneuern").hide();
					$("#pop_erneuern .tilength").val(1);
				}
			}
		});
	},
	//效验服务续订录入的周期
	validate: function(){
		var solf = this;
		$("#pop_erneuern .tilength").blur(function()
		{
			if (Number($(this).val()) + "" == "NaN") {
				$(this).val(1);
				return;
			}
			if (Number($(this).val()) < 1) {
				$(this).val(1);
				return;
			}
			if (Number($(this).val()) >9999) {
				$(this).val(9999);
				return;
			}
			if (Number($(this).val()) % 1 != 0) {
				$(this).val(1);
				return;
			}
			$(this).val(Number($(this).val()));
		});
	},
	// -------------------------------产品续订-end--------------------------------------//

	// -------------------------------产品退订-start------------------------------------//
	pmUnsub : function(id,instanceId) {
		pmmanage.showUnsubDiv(id);
	},
	showUnsubDiv : function(id) {
		$("#del_confirm .popwindow_t").html("退订服务");
		//var tdObj = $("#pm_del_td");
		var tdObj = $("#td_del_" + id);
		$("#del_confirm").css("top", tdObj[0].offsetTop).css("left", tdObj[0].offsetLeft - 20).css("display", "block");
	},
	confirmUnsub : function() {
		if(commonUtils.len($("#fw_reason").val()) == 0 || commonUtils.len($("#fw_reason").val()) > 20 ) {
		alert("申请理由限定1~20个字符（一个汉字算2个字符）");
		$("#fw_reason").focus();
		return;
	}
	var data = {};
	data.reason = $("#fw_reason").val();
	data.instanceId = $("#fwInstanceId").val();
	$("#del_confirm").hide();
	$.ajax({
	  type : "POST",
	  url : pmmanage.root+"/vdc_fwmanage/applyDestroy.action",
	  datatype : "json", //设置获取的数据类型为json
	  data : data,
	  async : false,
	  global : false,
	  success : function(data) {
	    alert(data);
	    //pmmanage.getList();
	  }
	});
	//pmmanage.cancel();
  },
	// -------------------------------产品续订-end--------------------------------------//

	// -------------------------------变更操作系统-start------------------------------------//
    pmChangeOS : function(pmId, pminstanceId, osDesc) {
		initChangeOSPopDiv(pmId, pminstanceId, osDesc,function(){
			pmmanage.clickChangeOSEventHandler();			
		});
	    
	},
	clickChangeOSEventHandler : function () {
	    $.ajax({
	        url : pmmanage.root + "/template/getConfigParams.action?type=10",
	        type : 'POST',
	        dataType : 'json',
	        async : false,
	        success : function(data) {
		        if(data != null) {
		            if(typeof(data) == "string" && data.indexOf("error") == 0) {
		            	alert(data);
		                return;
		            }
		            else 
		            {
		          	    $("#new_os_type").html("");
		                if(data.osname != undefined) {
		  	                var osArray = $.evalJSON(data.osname);
		  	                $(osArray).each(function(i) {
			  	                if(i == 0) {
			  	                    $("#new_os_type").append("<option value='" + osArray[i].value + "' selected>" + osArray[i].text + "</option>");
			  	                }
			  	                else {
			  	                    $("#new_os_type").append("<option value='" + osArray[i].value + "'>" + osArray[i].text + "</option>");
			  	                }
			  	            });
			  	            $("#add_confirm").show();
			            }
		            }
		        }else {
		            return;
		        }
	        }
	    });
	},
	confirmChangeOS : function() {
	    if(commonUtils.len($("#apply_reason").val()) == 0 || commonUtils.len($("#apply_reason").val()) > 20 ) {
	        alert("申请理由限定1~20个字符（一个汉字算2个字符）");
	        $("#apply_reason").focus();
	        return;
	    }
	    if($("#new_os_type").val()==null){
	    	alert("请选择要变更的操作系统！");
	    	return;
	    }   
	    var params = {};
	    params.apply_reason =  $("#apply_reason").val();
	    params.pmID = $("#pmId").val();
	    params.instanceId = $("#pminstanceId").val();
	    params.os_type = $("#new_os_type").val();
		pmmanage.pmModify("pmModify!pmApplyChangeOS.action",params,params.pmID,"变更操作系统");
	    $("#add_confirm").hide();
	},
	// -------------------------------变更操作系统-end--------------------------------------//

	//---------------------------物理机操作（启动、重启、停止）-start--------------------//
	
	pmStart : function(id,pmEInstanceId){
		var params={pmID:id,instanceId:pmEInstanceId};
		pmmanage.pmModify("pmModify!pmStart.action",params,id,"启动");
	},
	pmStop : function(id,pmEInstanceId){
		var params={pmID:id,instanceId:pmEInstanceId};
		pmmanage.pmModify("pmModify!pmStop.action",params,id,"停止");
	},
	pmReset : function(id,pmEInstanceId){
		var params={pmID:id,instanceId:pmEInstanceId};
		pmmanage.pmModify("pmModify!pmReboot.action",params,id,"重启");
	},
	pmPowerStatus : function(id,pmEInstanceId){
		var op_start   = '<a title=\"开机\" href="#" ';
		op_start  += '   onclick="pmmanage.pmStart(\''    + id + '\', \'' + pmEInstanceId + '\')" ';
		op_start  += '>';
		op_start  += '<img src="../images/start.png" />';
		op_start  += '</a>';
		
		var op_stop    = '<a title=\"关机\" href="#" ';
		op_stop   += '   onclick="pmmanage.pmStop(\'' + id + '\', \'' + pmEInstanceId + '\')" ';
		op_stop   += '>';
		op_stop   += '<img src="../images/stop.png"  />';
		op_stop   += '</a>';
		
		var op_reset   = '<a title=\"重启\" href="#" ';
		op_reset  += 'onclick="pmmanage.pmReset(\'' + id + '\', \'' + pmEInstanceId + '\')" ';
		op_reset  += '>';
		op_reset  += '<img src="../images/restart.png" />';
		op_reset  += '</a>';
		
		var op_monitor =  '';
		var str = main.whichMonitor;
		if( pmmanage.strCloud==2||str.indexOf('物理机') >=0){//bug 0004851 0004528 0005140
			op_monitor = serviceState!=6?'<a href="javascript:showProductMonitor(\'HOST\','+pmEInstanceId +')" title="监控"><img src="../images/ulList1_liIcon_monitor.png"/> </a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/ulList1_liIcon_monitor.png"/>';
		} else {
			op_monitor = '';
		} 
		var status = '';
		var params={pmID:id,instanceId:pmEInstanceId};
		var actionName = "pmModify!pmPowerStatus.action";
		$.ajax({
			url : pmmanage.root+"/"+actionName+"",
			type : 'POST',
			async: true,
			data : params,
			dataType : 'json',
			success : function(data) {
		        if(data != null) {
					//alert('data='+data);
					status = data;
					if('on' == status){
						//dom += op_stop + op_reset;
						$("#state_"+id).html("运行中");
						$("#caozuo_"+id).html(op_stop + op_reset+op_monitor);
					}else if('off' == status){
						//dom += op_start;
						$("#state_"+id).html("已关机");
						$("#caozuo_"+id).html(op_start);
					}else{
						//dom += op_start;
						$("#state_"+id).html("状态异常");
						$("#caozuo_"+id).html("状态异常");
					}
					return data;
		        }
			}
		});
		return status;
	},
	
	pmModify:function(actionName,params,id,str){
		if(confirm("您确定要执行‘"+str+"’物理机操作吗？"))
		{
			$.ajax({
				url : pmmanage.root+"/"+actionName+"",
				type : 'POST',
				async: false,
				data : params,
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						alert(data);
						return false;
					}else{
						alert(data);
						javascript:window.location.reload() ;
						return true;
					}
				}
			});
		}

	}

	//---------------------------物理机操作（启动、重启、停止）-end--------------------//
}
/**
 * 物理机配置详情显示
 * @param info
 * @returns {String}
 */
function pmConfig(info){
	var rs='';
	var cpu=info.cpuNum;
	var memSize=info.memorySize;
	var osDesc=info.osDesc;
	var storeType=info.storeType!=undefined?','+info.storeType:'';
	osDesc=osDesc!=undefined?osDesc:'';
	if(cpu!=''&&cpu!=undefined){
		rs+='cpu：'+cpu+'个';
	}
	if(memSize!=''&&memSize!=undefined){
		rs+='，内存：'+memSize+'G';
	}
	if(osDesc!=''&&osDesc!=undefined){
		rs+='，系统：'+osDesc;
	}
	if(storeType!=''&&storeType!=undefined){
		rs+='，存储：'+storeType;
	}
	//alert(info.resourceInfo);
	var resourceInfo=eval('('+info.resourceInfo+')');
	//var resourceInfo=info.resourceInfo;

	//alert(resourceInfo);
	if(resourceInfo != null && resourceInfo.nics != null){
		var nics = resourceInfo.nics;
		
		if(resourceInfo.username != null && resourceInfo.username != undefined && resourceInfo.username != ''){
			rs+='，用户：'+resourceInfo.username;
		}
		if(resourceInfo.password != null && resourceInfo.password != undefined && resourceInfo.password != ''){
			rs+= '，密码：' + resourceInfo.password;
		}

		$(nics).each( function(i) {
			if(nics[i].vlanid > 0 && nics[i].ip != '0'){
				rs+='，(vlan'+nics[i].vlanid + '-' + nics[i].ip+')';
			}
		});
	}
	return rs;	
}
/**
 * 物理机当前状态显示：
 * Running-运行中,Starting-正在启动,Stopping-正在关机,
 * Stopped-已关机,Released-已释放
 * @param info
 * @returns {String}
 */
function pmState(info){
	
	$.pmmanage || ($.pmmanage = pmmanage);	
	pmmanage.root="..";
	pmmanage.init();
	
	//pmmanage.getMCListNew(info.id);
	
	var stateName = "";		
	switch(info.state){	
		case 1:stateName="申请";break;
		case 2:stateName="运行中";break;
		case 3:stateName="申请处理中";break;
		case 4:stateName="已删除";break;
		case 5:stateName="已关机";break;
		case 6:stateName="操作执行中";break;
		case 7:stateName="创建失败";break;
		default:stateName="就绪";	
	}
	return stateName;
}

function pmOperate(info){
    var serviceState=$('#serviceState').val();
	var curPM = pmmanage.pmcomputer;
	var dom = ''; 
	//alert('state='info.state + '  ,  instantid='+info.eInstanceId);
	var viewTitle = info.instanceName;
	var op_changeOS='';//变更操作系统
	var op_start   ='';//启动、开机
	var op_stop    ='';//停止、关机
	var op_reset   ='';//重启
	var op_renewal ='';//续订
	//var op_modify  ='';//修改
	//var op_pause   ='';//暂停
	//var op_snap    ='';//快照
	var op_monitor ='';//监控
	//var unsubscribe='';//退订
	//物理机服务所有操作图标以及对应js操作函数
	op_start   = '<a title=\"开机\" href="#" ';
	op_start  += '   onclick="pmmanage.pmStart(\''    + info.id + '\', \'' + info.eInstanceId + '\')" ';
	op_start  += '>';
	op_start  += '<img src="../images/start.png" />';
	op_start  += '</a>';
	
	op_start = serviceState!=6?op_start:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/start.png" />';
	
	op_stop    = '<a title=\"关机\" href="#" ';
	op_stop   += '   onclick="pmmanage.pmStop(\'' + info.id + '\', \'' + info.eInstanceId + '\')" ';
	op_stop   += '>';
	op_stop   += '<img src="../images/stop.png"  />';
	op_stop   += '</a>';
	
	op_stop = serviceState!=6?op_stop:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/stop.png" />';
	
	op_reset   = '<a title=\"重启\" href="#" ';
	op_reset  += 'onclick="pmmanage.pmReset(\'' + info.id + '\', \'' + info.eInstanceId + '\')" ';
	op_reset  += '>';
	op_reset  += '<img src="../images/restart.png" />';
	op_reset  += '</a>';
	
	op_reset = serviceState!=6?op_reset:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/restart.png" />';
	
//资源实例不用执行续订、退订，这两个功能在服务层实现。	来源：wangzheng 2012-08-31
//	op_renewal = '<a title=\"续订\" href="#"  id=\"td_bw_1_' + info.id + '\" ';
//	op_renewal+= 'onclick="pmmanage.showProductXDDiv(1,' + info.id + ')" ';
//	op_renewal+= '>';
//	op_renewal+= '<img src="../images/tel_edit.png" />';
//	op_renewal+= '</a>';
//	
//	op_unsub   = '<a title=\"退订\" href="#"  id=\"td_del_' + info.id + '\" ';
//	op_unsub  += 'onclick="pmmanage.pmUnsub(\''   + info.id + '\', \'' + info.eInstanceId + '\')" ';
//	op_unsub  += '>';
//	op_unsub  += '<img src="../images/Destroy.png" />';
//	op_unsub  += '</a>';
	
//	op_changeOS ='<a title=\"变更操作系统\" href="#" ';
//	op_changeOS+='   onclick="pmmanage.pmChangeOS(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + pmConfig(info) + '\')" ';
//	op_changeOS+='>';
//	op_changeOS+='<img src="../images/modify.png" />';
//	op_changeOS+='</a>';
//	
//	op_changeOS = serviceState!=6?op_changeOS:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/restart.png" />';
	
	//op_snap    = '<a title=\"快照\" href="#" ';
	//op_snap   += '   onclick="pmmanage.CSnapshot(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ';
	//op_snap   += '>';
	//op_snap   += '<img src="../images/CSnapshot.png" />';
	//op_snap   += '</a>';
	
	//op_pause    = '<a title=\"暂停\" href="#" ';
	//op_pause   += '   onclick="pmmanage.pmPause(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ';
	//op_pause   += '>';
	//op_pause   += '<img src="../images/stop.png" />';
	//op_pause   += '</a>';
	
	var str = main.whichMonitor;
//	op_monitor   = '<a title=\"监控\" href="#" ';
//	op_monitor   += '  onclick="showProductMonitor(' + info.eInstanceId + ')" ';
//	op_monitor   += '>';
//	op_monitor   += '<img src="../images/ulList1_liIcon_monitor.png" />';
//	op_monitor   += '</a>';
	if( pmmanage.strCloud==2||str.indexOf('物理机') >=0){//bug 0004851 0004528 0005140
		op_monitor = serviceState!=6?'<a href="javascript:showProductMonitor(\'HOST\','+info.eInstanceId +')" title="监控"><img src="../images/ulList1_liIcon_monitor.png"/> </a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/ulList1_liIcon_monitor.png"/>';
	} else {
		op_monitor = '';
	} 
	
	if(info.state==2){
		pmmanage.pmPowerStatus(info.id,info.eInstanceId);
		//fix bug 5448 “我的服务”的资源列表中显示不出来物理机资源的具体信息，修改不显示资源变更造成
		dom += op_stop + op_reset + op_changeOS + op_monitor ;
	}else if(info.state==3){
		bwstate="申请处理中";
		var indoing =  '<span><img src="../images/loading1.gif"></span>';
		dom += indoing;
	}else if(info.state==4){
		bwstate="已删除";
		dom += bwstate;
	}else if(info.state==6){
		bwstate="操作执行中";
		var indoing =  '<span><img src="../images/loading1.gif"></span>';
		dom += indoing;
	}else if(info.state==5){
		bwstate="已关机";
		dom += op_start ;
	}
	return dom;
}
function initChangeOSPopDiv(pmId, pminstanceId, osDesc){
	$("#add_confirm").empty();
	$("#add_confirm").load(pmmanage.root + "/component/physical/pmchangeos.html","",function(){
		$("#add_confirm #pmId").val(pmId);
	    $("#add_confirm #pminstanceId").val(pminstanceId);
	    $("#add_confirm #old_os_type").html(osDesc);
        $("#close").click(function(){
            $(".popupDiv1,.shade").fadeOut("fast");
            query();
        });
	});
}