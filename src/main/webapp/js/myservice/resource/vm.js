function vmConfig(info){
	var rs='';
	var cpu=info.cpuNum;
	var memSize=info.memorySize;
	var osDesc=info.osDesc;
	var storeType=info.storeType!=undefined?''+info.storeType:'';//fix bug7600不需要“,”号
	osDesc=osDesc!=undefined?osDesc:'';
	if(cpu!=''&&cpu!=undefined){
		rs+='cpu：'+cpu+'个';
	}
	if(memSize!=''&&memSize!=undefined){
		rs+='，内存：'+memSize+'Mb';
	}
	if(osDesc!=''&&osDesc!=undefined){
		rs+='，系统：'+osDesc;
	}
	if(storeType!=''&&storeType!=undefined){
		rs+='，存储：'+storeType;
	}
	return rs;	
}
function vmState(info){
	var state=info.state;
	var showState = '';
	switch(state){
		case 1:showState = '申请';break;
		case 2:showState = '运行中';break;
		case 3:showState = '申请处理中';break;//正在开通，正在销毁，正在变更
		case 4:showState = '已删除';break;
		case 5:showState = '已关机';break;
		case 6:showState = '操作执行中';break;//bug 0003215 0003606 //开机，关机，重启，创建快照等
		case 7:showState = '创建失败';break;
		default : showState = '就绪';break;
	}
	return showState;
}

function vmOperate(info){
	var vcaozuo='';
	var vcaozuo1='';
	var vcaozuo2='';
	var CSnapshot='';
	var USnapshot='';
	var DSnapshot='';
	var modify='';
	var modifyInsName='';
	var pause='';
	var jiankong='';
	var state=info.state;
    var serviceState=$('#serviceState').val();
	if(state== "2"){
		var viewvm = info.instanceName;                	
        if(viewvm.length>10){                            
        	viewvm = viewvm.substring(0,10) + '...';
        }  
		var viewvm='<a href=\"javascript:void(0)\" onclick=\'vmmanage.viewvm("' + info.eInstanceId + '");return false;\' class=\"ckxq\">' + viewvm + '</a>';                        	
		 vcaozuo =  serviceState!=6?'<a href=\"#\" title=\"重启\" onclick=\"vmmanage.vmReboot(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ><img src=\"../images/restart.png\" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/restart.png\" />';
    	 vcaozuo1 = serviceState!=6?'<a href=\"#\" title=\"停止\" onclick=\"vmmanage.vmStop(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ><img src=\"../images/stop.png\" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/stop.png\" />';                                       	
    	 CSnapshot = serviceState!=6?'<a href=\"#\" title=\"快照\" onclick=\"vmmanage.CSnapshot(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\', \'' + info.resourcePoolsId + '\')" ><img src=\"../images/CSnapshot.png\" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/CSnapshot.png\" />';
    	 USnapshot = serviceState!=6?'<a href="javascript:vmmanage.showDiv(1, \'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\', \'' + info.resourcePoolsId + '\');" title=\"恢复\" ><img src=\"../images/USnapshot.png\" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/USnapshot.png\" />';
    	 DSnapshot = serviceState!=6?'<a href="javascript:vmmanage.showDelDiv(2, \'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\', \'' + info.resourcePoolsId + '\');" title=\"删除快照\" ><img src=\"../images/DSnapshot.png\" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/DSnapshot.png\" />';
    	if(curProjectId==2){
    		 pause = serviceState!=6?'<a href=\"#\" title=\"暂停\" onclick="vmmanage.vmPause(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ><img src=\"../images/stop.png\" /> </a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/stop.png\" />';                		                		                		
    	}
    	if(curProjectId==1){
    		 pause = '';
    	}
    	var resCode=info.resCode!=undefined&&info.resCode!=null?info.resCode:'';//bug 0003331
    	 modify = serviceState!=6?'<a href=\"#\" title=\"变更\" onclick=\"vmmanage.vmEdit(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + resCode + '\', \'' + info.templateId + '\', \'' + info.productId + '\', \'' + info.cpuNum + '\', \'' + info.memorySize + '\')" ><img src="../images/modify.png" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />';
    	 modifyInsName = serviceState!=6?'<a href=\"#\" title=\"修改虚拟机实例名称\" onclick=\"vmmanage.vmNameEdit(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resourcePoolsId + '\', \'' + info.instanceName + '\', \'' + info.comment + '\')" ><img src="../images/modifyName.png" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modifyName.png" />';
    	 
    	var str = main.whichMonitor;
    	if(vmmanage.getCloudInfo()==2||str.indexOf('虚拟机') >=0){
    		 //to fix bug [4814]
    		//to fix bug [5025] 0004851 0004528 0005140
    		jiankong = serviceState!=6?'<a href="javascript:showProductMonitor(\'VM\','+info.eInstanceId +')" title="监控"><img src="../images/ulList1_liIcon_monitor.png"/> </a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/ulList1_liIcon_monitor.png"/>';
    	} else {
    		 jiankong = '';
    	}                	                	
	} else if(state== "5"){
		var viewvm = info.instanceName;
		if(viewvm.length>10){                            
			viewvm = viewvm.substring(0,10) + '...';
		}       				
		 vcaozuo =  serviceState!=6?'<a href="#" title=\"启动\" onclick="vmmanage.vmStart(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ><img src="../images/start.png" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/start.png" />';
	} else if(state== "7"){
		var viewvm = info.instanceName;
		if(viewvm.length>10){                            
			viewvm = viewvm.substring(0,10) + '...';
		}       				
		 //vcaozuo =  serviceState!=6?'<p><a href="#" title=\"恢复\" onclick="vmmanage.vmRestore(\'' + info.id + '\', \'' + info.eInstanceId + '\', \'' + info.resCode + '\')" ><img src="../images/start.png" /> </a></p>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/start.png" />';
		vcaozuo =  '';//fix bug 3509
	} else if(state== "6"){
		var viewvm = info.instanceName;
		if(viewvm.length>10){                            
			viewvm = viewvm.substring(0,10) + '...';
		}       				
		 vcaozuo =  '<span><img src="../images/loading1.gif"></span>';
	} else if(state== "3"){
		var viewvm = info.instanceName;
		if(viewvm.length>10){                            
			viewvm = viewvm.substring(0,10) + '...';
		}       				
		 vcaozuo =  '<span><img src="../images/loading1.gif"></span>';    				
	} else {    				
		var viewvm = info.instanceName;
		if(viewvm.length>10){                            
			viewvm = viewvm.substring(0,10) + '...';
		}       				
	}
	return vcaozuo+vcaozuo1+CSnapshot+USnapshot+DSnapshot+modify+modifyInsName+pause+jiankong;
}

function loadPopDiv_vm(){  

//    $("#add_confirm").empty();
//    $("#add_confirm").load("../component/vmmanage/vmoperate.html","",function(){
//        $(".popupDiv1,.shade").fadeIn("fast");
//        $("#close").click(function(){
//            $(".popupDiv1,.shade").fadeOut("fast");
//            query();
//        });
//    });

}

var vmmanage = {
  strCloud: "",
  init : function() {    
    vmmanage.getWhichMonitor();    
    vmmanage.PublicPrivateSwitch();    
  },

  getWhichMonitor: function(){
    $.ajax({
          type : "GET",
          url : resource.actionRoot+"/portal_mo/getMonitorInsByUser.action",
          success : function(data) {
              main.whichMonitor = data;
            }
    });
  },
  
   viewvm: function (id){
  	//防止盗链：判断id(resID)是否属于当前用户    	
	  	var width = 860;
	  	var height = 668;
	  	var wt = (screen.width - width) / 2;
	  	var wl = (screen.height - height) / 2;	  	
	  	var viewer = window.open(resource.root+'/jsp/vnc.jsp?vm=' + id,"VNCWindows","width=820,height=640,left="+ wt+ ",resizable=yes,menubar=no,status=no,scrollbars=yes,toolbar=no,location=no");
	  	viewer.focus();
	  	return false;
    },  
    
    CSnapshot: function(vmID, instanceId, resCode, resourcePoolsId) {
		var params = {
			vmID : vmID,
			instanceId : instanceId,
			res_code : resCode,
			resourcePoolsId : resourcePoolsId
		};
		
		if (confirm('是否确认创建快照？')) {
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');	        
         
			$.ajax({//bug 0003223
				url :  resource.actionRoot+"/resourcesUsed/vmModify!vmVolumeSnapshot.action",
				type : 'POST',
				async : false,
				data : params,
				dataType : 'json',
				success : function(data) {
					//to fix bug [3670]
					alert(data);					
				}
			});	   
			query();
		}		
	},    
  
    vmStart: function(vmID,instanceId, resCode) { 
		if(confirm("确定要启动该虚拟机吗？")){
			var params = {
				vmID : vmID,
				instanceId : instanceId,
				res_code : resCode,
				ActType : 1
			};
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');
	        
			vmmanage.vmModify("vmModify!vmStart.action", params);
		}
	},
  
    vmStop: function(vmID, instanceId, resCode) {
		 
		if(confirm("确定要停止该虚拟机吗？")){
			var params = {
				vmID : vmID,
				instanceId : instanceId,
				res_code : resCode,
				ActType : 4
			};
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');      
	        
			vmmanage.vmModify("vmModify!vmStop.action", params);
		}
	},

		
     vmPause: function(vmID, instanceId, resCode) {		
		var params = {
			vmID : vmID,
			instanceId : instanceId,
			res_code : resCode,
			ActType : 2
		};		
        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');
 
        vmmanage.vmModify("vmModify!vmPause.action", params);
	},
	
	 vmRestore: function(vmID, instanceId, resCode) {		
		var params = {
			vmID : vmID,
			instanceId : instanceId,
			res_code : resCode,
			ActType : 3
		};		
        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');        
        vmmanage.vmModify("vmModify!vmRestore.action", params);
	},
	
	 vmReboot: function(vmID, instanceId, resCode) {
		 
		if(confirm("确定要重启该虚拟机吗？")){
			var params = {
				vmID : vmID,
				instanceId : instanceId,
				res_code : resCode,
				ActType : 5
			};
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');
	
			vmmanage.vmModify("vmModify!vmReboot.action", params);
		}
	},		
	
	vmModify: function(actionName, params) {
        $("body").stopTime(timerKey);//bug 0003917
        $("#add_confirm").hide();//bug 0003987
		$.ajax({
			url : resource.actionRoot+"/resourcesUsed/" + actionName,
			type : 'POST',
			async : false,
			data : params,
			dataType : 'json',
			success : function(data) {
				if (data.search("error") != -1) {
					//dialog_confirmation('#dialog_confirmation', data);
                    alert(data);        
					return false;
				} else {
                     alert(data);      
				}
                query();  
			}
		});
	},
	
	vmNameModify: function(actionName, params) {
        $("body").stopTime(timerKey);
        $("#add_confirm").hide();
		$.ajax({
			url : resource.actionRoot+"/resourcesUsed/" + actionName,
			type : 'POST',
			async : false,
			data : params,
			dataType : 'json',
			success : function(data) {
				if (data.search("error") != -1) {
                    alert(data);        
					return false;
				} else {
                     alert(data);      
				}
                query();  
			}
		});
	},

	 
	showDiv: function(type, vmID, instanceId, resCode, resourcePoolsId) {
		
	    $("#add_confirm").empty();
	    $("#add_confirm").load("../component/vmmanage/vmoperate.html","",function(){
	        $("#close").click(function(){
	            $(".popupDiv1,.shade").fadeOut("fast");
	            query();
	        });
	        
	        //弹出窗后	        
		    $("#vmID").val(vmID);
		    $("#operType").val(type);
		    $("#res_code").val(resCode);
		    if(type == 1) {
		    	$('#popTitle').html('虚拟机快照恢复');
		    	$('#czz').html('快照：');
		    	$("#storageSize").html("");
		      
		    	var params = {
		    		vmID : vmID,
		    		instanceId : instanceId,
		    		resourcePoolsId:resourcePoolsId
		    	};
		     
		    	$.ajax({
					url : resource.actionRoot+"/resourcesUsed/vmModify!getSnapshotsByVolumeId.action",
					type : 'POST',
					data : params,
					dataType : 'json',
					success : function(data) {
						if (data != "[null]") {
							var snapshotList = $.evalJSON(data);
							 
							if(snapshotList.length > 0){//bug 0003235
	                            $("#params").html("");
	                            var str_snap = "";
	                            $("#params").append(str_snap);
								for ( var i = snapshotList.length - 1; i >= 0; i--) {
									//str_snap = "<option id=\"" + snapshotList[i].snapshotId + "\">" + snapshotList[i].name + "</option>";
									//to fix bug [7769]
									str_snap = "<option id=\"" + snapshotList[i].snapshotId + "\" title=\""+ snapshotList[i].name + "\">" + snapshotList[i].name + "</option>";
								    $("#params").append(str_snap);
								}	//bug 0003130
	                            $("#add_confirm").show();
	                            $('#params').show();    
	                            $("#tr_storage").show();
	                            $("#vm_liyou").hide();
	                            $("#vm_btn").attr("onclick","vmmanage.vmSnapshotResume(resourcePoolsId);");
	                            $('#close').attr('onclick', 'vmmanage.cancel()');   			   
							} else {
					            $(".popupDiv1,.shade").fadeOut("fast");
							    $("#add_confirm").empty();
								alert('此虚拟主机没有卷快照！');
							}
						}
					}
		    	});  	
		    }
	        
	        
	    });
	},   
	
	showDelDiv: function(type, vmID, instanceId, resCode, resourcePoolsId) {

	    $("#add_confirm").empty();
	    $("#add_confirm").load("../component/vmmanage/vmoperate.html","",function(){
	        $("#close").click(function(){
	            $(".popupDiv1,.shade").fadeOut("fast");
	            query();
	        });
	        
	        //弹出窗后
		    $("#vmID").val(vmID);
		    $("#operType").val(type);
		    $("#resCode").val(resCode);
		    if(type == 2) {
	          $('#popTitle').html('虚拟机快照删除');
	          $('#czz').html('快照：');
		      $("#storageSize").html("");
		      
			  var params = {
				vmID : vmID,
				instanceId : instanceId,
	    		resourcePoolsId:resourcePoolsId
			  };
		     
	        $.ajax({
				url :  resource.actionRoot+"/resourcesUsed/" + "vmModify!getSnapshotsByVolumeId.action",
				type : 'POST',
				data : params,
				dataType : 'json',
				success : function(data) {
					if (data != "[null]") {
						var snapshotList = $.evalJSON(data);
						if(snapshotList.length > 0){
							$("#params").html("");
							var str_snap = "";
							$("#params").append(str_snap);
							for ( var i = snapshotList.length - 1; i >= 0; i--) {//to fix bug [6965 6964]
								str_snap = "<option id=\"" + snapshotList[i].snapshotId + "\" title=\""+ snapshotList[i].name + "\">" + snapshotList[i].name + "</option>";
							    $("#params").append(str_snap);                    
							}
	                    $("#tr_storage").show();
	                    $('#params').show();						
	                    $('#add_confirm').show();
	                    $("#popTitle").show();
	                    $("#vm_liyou").hide();
	                    $('#vm_btn').attr('onclick', 'vmmanage.delSnapshot()');
	                    $('#close').attr('onclick', 'vmmanage.cancel()');
						} else {
							alert('此虚拟主机没有卷快照！');
						}
					} 
				}
			  });	
		    }
	        
	        
	    });
	},	

      cancel : function() {
        $("#storageSize").empty();
        $("#vmID").val("");
        $("#operType").val("");
        $("#add_confirm").hide();
        $("#vm_modify").hide();
        $("#vmName_modify").hide();
      },
      
    delSnapshot : function (){
        var params = {
            vmID : $("#vmID").val(),
            instanceId : $("#params option:selected").attr("id")
        };
        vmmanage.vmModify("vmModify!deleteVmVolumeSnapshot.action", params);
        $("#params").html("");
    },  
   
    vmEdit: function(vmID, instanceId, resCode, templateId, productId, cpuNum, memorySize) {
    	    	
    	$("#cpu_params option").each(function(){
    		if($(this).val() <= Number(cpuNum)){
    		    $(this).remove();
    		}
    	});       	
    	if($("#cpu_params").get(0).selectedIndex == -1){
    		$("#tr_cpu").hide();
    	}
    	
    	$("#memory_params option").each(function(){
    		if($(this).val() <= Number(memorySize)){
    		    $(this).remove();
    		}
    	});       	
    	if($("#memory_params").get(0).selectedIndex == -1){
    		$("#tr_memory").hide();
    	}    	
    	//to fix bug [6804]
    	if($("#cpu_params").get(0).selectedIndex == -1 && $("#memory_params").get(0).selectedIndex == -1){
    		alert('没有可用的变更属性');
    	} else {
    	    $("#vmID").val(vmID);
            $('#popTitle').html('虚拟机变更');             			 				                 
            $("#vm_modify_reason").val("");
            $("#vm_modify").show();                         
            $("#vm_btn").attr("onclick","vmmanage.VMModifyOK(" + vmID + ", " + instanceId + ", " + templateId + ", " + productId + ", " + cpuNum + ", " + memorySize + ");");
            $('#close').attr('onclick', 'vmmanage.cancel()');              
    	}    
	},
	//to fix bug [4865] 
	//to fix bug [4802] 
	//to fix bug [4775]
	VMModifyOK: function(vmID, instanceId, templateId, productId, cpuNum, memorySize){

        if(commonUtils.len($("#vm_modify_reason").val()) == 0 ) {
            alert("请输入申请理由！");
            $("#vm_modify_reason").focus();
            return;
        }		
      
        if(commonUtils.len($("#vm_modify_reason").val()) > 100 ) {
          alert("申请理由限定100个字符（一个汉字算2个字符）");
          $("#vm_modify_reason").focus();
          return;
        }
        $("#vm_modify").hide();         
        if(commonUtils.len($("#vm_modify_reason").val()) > 0 ) {        	
        	$("#vm_modify_reason").val(commonUtils.trim($("#vm_modify_reason").val()));
        }
        
        if($("#cpu_params").val()==null){
        	var cpuNum = cpuNum;
        } else {
        	var cpuNum = $("#cpu_params").val();
        }        
        if($("#memory_params").val()==null){
        	var memorySize = memorySize;
        } else {
        	var memorySize = $("#memory_params").val();
        }        
        
		if(confirm("虚拟机是否确认变更？")){
			var params = {
					vmID : vmID,
					instanceId : instanceId,
					apply_reason : $("#vm_modify_reason").val(),
					vmtemplateId : templateId,
					productId : productId,
					cpu_num : cpuNum,
					mem_size : memorySize
				};
			
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');     
	
			vmmanage.vmModify("vmModify!vmModify.action", params);    
		}
    },
    
    vmNameEdit: function(vmID, instanceId, resourcePoolsId, vmName, comment) {
    	
    	if(vmID <= 0 || instanceId <= 0){
    		alert('虚机id无效，不可更改实例名称!');
    	} else {
    	    $("#vmID").val(vmID);
            $('#popTitle').html('虚拟机名称变更');             			 				                 
            $("#vmName").val(vmName);
            $("#vmDesc").val(comment);
            $("#vmName_modify").show();  
            comment="comment";
            $("#vmname_btn").attr("onclick","vmmanage.VMNameModifyOK(" + vmID + ", " + instanceId + ", " + resourcePoolsId + ", '" + vmName + "', '" + comment + "');");
            $('#vmname_close').attr('onclick', 'vmmanage.cancel()');              
    	}    
	},
	
	VMNameModifyOK: function(vmID, instanceId, resourcePoolsId, vmName, comment){

        if(commonUtils.len($("#vmName").val()) == 0 ) {
            alert("请输入虚拟机名称！");
            $("#vmName").focus();
            return;
        }
        if(commonUtils.len($("#vmName").val()) > 64 ) {
            alert("虚拟机名称限定64个字符！");
            $("#vmName").focus();
            return;
        }
        
        var _vmName = $("#vmName").val();
        if(_vmName==vmName){
        	alert("虚拟机名称未修改，不能提交！");
            $("#vmName").focus();
            return;
        }
        var VMRename = vmmanage.checkProductRename(_vmName);
		if(VMRename == "1"){
			alert("该虚拟机名称已经被使用，请重新输入。");
			$("#vmName").focus();
			return;
		}
        
        if(commonUtils.len($("#vmDesc").val()) == 0 ) {
            alert("请输入虚拟机描述信息！");
            $("#vmDesc").focus();
            return;
        }
        if(commonUtils.len($("#vmDesc").val()) > 100 ) {
          alert("虚拟机描述信息限定100个字符（一个汉字算2个字符）");
          $("#vmDesc").focus();
          return;
        }
        $("#vmName_modify").hide(); 
        
        if(commonUtils.len($("#vmDesc").val()) > 0 ) {        	
        	$("#vmDesc").val(commonUtils.trim($("#vmDesc").val()));
        }
              
        var _vmDesc = $("#vmDesc").val()       
        
		if(confirm("虚拟机名称是否确认修改？")){
			var params = {
					vmID : vmID,
					instanceId : instanceId,
					resourcePoolsId : resourcePoolsId,
					vmName : _vmName,
					vmDesc : _vmDesc
				};
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');     
	
			vmmanage.vmNameModify("vmModify!vmNameModify.action", params);    
		}
    },
    
    checkProductRename: function(name){
		var count = 0;		
		var data = {
			name: name,
			type: 1        
		};		
		$.ajax({
			url : "/UCFCloudPortal/product/checkProductRename.action",
			type : "POST",
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			dataType : "json",
			data : data,
			async : false,			
			success : function(data){
				if(data != null) {
					if(typeof(data) == "string" && data.indexOf("error") == 0) {
						return -1;
					}else {
						count = data;
					}
				} 
			}
		});
		if(count <= 0){			
			var param = {
				name: name,
				type: 50        
			};
			$.ajax({
				url : "/UCFCloudPortal/vmsManageAction/checkProductRename.action",
				type : "POST",
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				dataType : "json",
				data : param,
				async : false,			
				success : function(data){
					if(data != null) {
						if(typeof(data) == "string" && data.indexOf("error") == 0) {
							return -1;
						}else {
							count = data;
							//alert(count);
						}
					} 
				}
			});
		}
		return count;
	},	
    
    vmSnapshotResume : function (resourcePoolsId) {
    	
    	if($("#params option:selected").attr("id") == 0){
    		alert('请选择快照！');
    		return;
    	}
    	
        var params = {
            vmID : $("#vmID").val(),
            instanceId : $("#params option:selected").attr("id"),
            res_code : $("#res_code").val(),
            resourcePoolsId:resourcePoolsId
        };

        var operType = $("#operType").val();

        if(operType=='1'){
        	//to fix bug [6968]
        	//alert("为安全起见，密码会被重置为随机密码，请使用新密码登录并及时更改。\n\n恢复系统后需重新挂载弹性块存储，并在系统中对存储进行激活。");
            vmmanage.vmModify("vmModify!vmSnapshotResume.action", params);
            //alert('成功恢复所选快照！');
         
            $("#caozuo_" + $("#vmID").val()).html('<span><img src="../images/loading1.gif"></span>');
        }else
        if(operType=='2'){
            vmmanage.vmModify("vmModify!deleteVmVolumeSnapshot.action", params);
            alert('成功删除所选快照！');
 
            $("#caozuo_" + $("#vmID").val()).html('<span><img src="../images/loading1.gif"></span>');
        }
        $("#params").empty();
    },

  getCloudInfo: function(){
    var cloudInfo = 0;
    $.ajax({
      url: resource.actionRoot + "/sysParameters/getCloudInfo.action",
      type: 'POST',
      dataType: 'json',
      async: false,
      success: function(data) {
        cloudInfo = data;
      }
    });
    return cloudInfo;
  },
  
    PublicPrivateSwitch : function() {               
        $('#PublicPrivate').val(vmmanage.getCloudInfo()+'');
    },
    
    validate: function(){
        var solf = this;
        $("#pop_erneuern .tilength").blur(function(){
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
    }
};    