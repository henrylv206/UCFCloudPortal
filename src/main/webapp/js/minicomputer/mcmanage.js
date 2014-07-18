var mcmanage = {
		strCloud: "",
		root:"/UCFCloudPortal",
		minicomputer:"",
	init : function() {
		var hcEInstanceId;
		mcmanage.template_width= jQuery.makeArray();
		//mcmanage.validate();
		
		mcmanage.PublicPrivateSwitch();
		mcmanage.getWhichMonitor();   
		
		
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
	
	getType: function(type){
		var typeName = "";
		if(type == 1) {
	          typeName = "SUN";
	        }
	        else if(type == 2) {
	        	typeName = "IBM";
	        }
	        else if(type == 3){
	        	typeName = "HP";
	        }
		return typeName;
	},
	  
	mcConfig: function (info){
		var rs='';
		var cpu=info.cpuNum;
		var memSize=info.memorySize;
		var storageSize=info.storageSize!=undefined?','+info.storageSize:'';
		var typeName = mcmanage.getType(info.mcType);
		if(cpu!=''&&cpu!=undefined){
			rs+='cpu：'+cpu+'个';
		}
		if(memSize!=''&&memSize!=undefined){
			rs+='，内存：'+memSize+'Mb';
		}
		if(storageSize!=''&&storageSize!=undefined){
			rs+='，磁盘：'+storageSize+'G';
		}
		if(typeName!=''&&typeName!=undefined){
			rs+='，类型：'+typeName;
		}
		return rs;	
	},

	getMCListNew : function(hcID) {	        
		$.ajax({
			type : "POST",
			url : mcmanage.root+"/resourcesUsed/hcInfoByID.action?hcID="+hcID,
			datatype : "json", //设置获取的数据类型为json
			//data :  data,
			async : false,
			global : false,
			success : function(data) {
				var objs = eval("("	+ data + ")");
				if(null==objs||objs[0].length<=0 ){
					$("#mcmanage").html("没有找到记录");
					return;
				}
				mcmanage.minicomputer = objs[0];
			}
		});
	},
	

	
	showMCDiv : function(type, id, size,num,clusId) {
		initUpdatePopDiv(type, id, size,num,clusId,function(){
		    if(type == 1) {	     	      
//			      $("#cpu_num").show();
//			      $("#mem_size").show();
//			      $("#recpu_num").html("");
//			      $("#remem_size").html("");
		    }			
		});		
	    
	  },
	cancel : function() {
		   $("#recpu_num").html("");
	       $("#remem_size").html("");
		    $("#mcreason").val("");
		    $("#mc_confirm").hide();
	 },
	 confirm : function() {
		    if(commonUtils.len($("#mcreason").val()) == 0 || commonUtils.len($("#mcreason").val()) > 20 ) {
		      alert("申请理由限定1~20个字符（一个汉字算2个字符）");
		      $("#mcreason").focus();
		      return;
		    }
		    if($("#cpu_num").val()==null){
		    	alert("请选择要修改的cpu数目");
		    	return;
		    }
//		    if($("#mem_size").val()==null){
//		    	alert("请选择要修改的内存大小");
//		    	return;
//		    }		   
		    var data = {};
		    var url = "hcModify!hcApplyUpdate.action";
		    data.apply_reason =  $("#mcreason").val();
		    data.hcID = $("#mcinstanceId").val();
		    data.instanceId = $("#mcinstanceId").val();
		    data.clusterId = $("#mcclusterId").val();
		    if($("#mcoperType").val() == '1') {
		     //	fix bug 2156	
		      var cpu = $("#recpu_num option:selected").attr("cpu")	;
		      var mem = $("#recpu_num option:selected").attr("mem");
		      if(parseInt(mem) == parseInt($("#mccurSize").val()) && parseInt(cpu) == parseInt($("#mccurNum").val())) {
		        alert("不允许修改成现有的配置!");
		        //$("#mc_confirm").hide();
		        return;
		      }		      
		      data.cpu_num = cpu;
		      data.mem_size = mem;
		    }
		    $("#mc_confirm").hide();
		    mcmanage.applyDestroyOrChange(url,data);
	},
	applyDestroyOrChange : function(urls,datas) {
		$.ajax({
					url : mcmanage.root+"/"+urls+"",
					type : 'POST',
					data : datas,
					dataType : 'json',
					success : function(data) {
						if(data.search("error")!=-1){
							alert(data);
							return false;
						}else{
							javascript:window.location.reload() ;
						}
					}
		});
	},
	clickEventHandler : function () {
		 
		//获取下拉列表
		$.ajax({
			url : mcmanage.root+"/hcCPUMemList!queryHCTemplateCPUAndMemoryAvailableList.action",
			type : 'POST',
			data : null,
			async: false,
			dataType : 'json',
			success : function(data) {
				if(data.search("error")!=-1){
					alert(data);
					return false;
				}else{
					var objs = eval("("	+ data + ")");
//					fix bug 2156	
					var cpuObjs = objs[0];
					var memObjs = objs[1];
					if ( cpuObjs!= null &&  cpuObjs.length>0&&memObjs!= null &&  memObjs.length>0) {
						$("#recpu_num").html("");
						for ( var i = 0; i < cpuObjs.length; i++) {
							var cpuNum = cpuObjs[i].cpu_num;
							var memory_size = memObjs[i].memory_size;
							$("#recpu_num").append("<option cpu='" + cpuNum + "' mem='"+memory_size+"'>CPU个数：" +cpuNum+ "个|内存："+memory_size+"兆</option>"); 
						}
						$("#add_confirm").show();
					}
//					if ( memObjs!= null &&  memObjs.length>0) {
//						$("#remem_size").html("");
//						for ( var i = 0; i < memObjs.length; i++) {
//							var memory_size = memObjs[i].memory_size;
//							$("#remem_size").append("<option value='" + memory_size + "'>" +memory_size+ "MB</option>"); 
//						}
//					}
				}
			}
		});
	},
	//小型机监控信息显示
	get_hc_monitor_info : function(instance_id){
		$("#mcmanage .hostadmin_pro").hide(); 
		$("#mcMonitorInfo").show();
		$("#fanhui").show();
		$("#mcinstanceId").val(instance_id);
		$.ajax({
			url : mcmanage.root+"/mcMonitor.action",
			type : 'POST',
			data : {instanceId : instance_id},
			dataType : 'json',
			success : function(data) {
				var obj =eval("("	+ data.mcMonitorInfoResult + ")");
				if(obj != null ){
					if(obj.resCode!="00000000"&&obj.resCode!="90000004"){//90000004
						dialog_confirmation('#dialog_confirmation',"错误码为："+obj.resCode);
						return false;
					}else{
						var jsonObj = obj.monitorInfo;
						if(jsonObj!=null){
							var cpuUtili = ((jsonObj.cpuUtili==null)? "":jsonObj.cpuUtili+"%");
							var memUtili = ((jsonObj.memUtili==null)? "":jsonObj.memUtili+"%");
							$("#cpuUtili").html(cpuUtili);//CPU使用率
							$("#memUtili").html(memUtili);//MEM使用率
							var networkkbsread = jsonObj.byteIoRead;
							if(networkkbsread ==null || networkkbsread ==0){
								$("#networkkbsread").html("N/A");
							}else{
								$("#networkkbsread").html(convertBytes(networkkbsread* 1024));//网络读取
							}
							var networkkbswrite = jsonObj.byteIoWrite;
							if(networkkbswrite ==null || networkkbswrite ==0){
								$("#networkkbswrite").html("N/A");
							}else{
								$("#networkkbswrite").html(convertBytes(networkkbswrite* 1024));//网络写
							}
							
							var swap_used = ((jsonObj.swapUsed==null)? "":jsonObj.swapUsed+"Mbytes");
							var swap_util = ((jsonObj.swapUtil==null)? "":jsonObj.swapUtil+"%");
							var kernel_thread_runing_num = ((jsonObj.kernelThreadRuningNum==null)? "":jsonObj.kernelThreadRuningNum+"%");
							var kernel_thread_blocking_num = ((jsonObj.kernelThreadBlockingNum==null)? "":jsonObj.kernelThreadBlockingNum+"%");
							var mem_used = ((jsonObj.memUsed==null)? "":jsonObj.memUsed+"Mbytes");
							$("#swap_used").html(swap_used);//MEM使用率
							$("#swap_util").html(swap_util);//CPU使用率
							$("#kernel_thread_runing_num").html(kernel_thread_runing_num);//MEM使用率
							$("#kernel_thread_blocking_num").html(kernel_thread_blocking_num);//CPU使用率
							$("#mem_used").html(mem_used);//MEM使用率
						}
					}
				}

				var mcPhyInfoJson =eval("("	+ data.mcPhyInfoResult + ")");
				if(mcPhyInfoJson != null ){
					if(mcPhyInfoJson.resCode!="00000000"&&mcPhyInfoJson.resCode!="90000004"){//90000004
						dialog_confirmation('#dialog_confirmation',"错误码为："+mcPhyInfoJson.resCode);
						return false;
					}else{
						var mcPhyInfoJsonObj = mcPhyInfoJson.info;
						if(mcPhyInfoJsonObj!=null){ 
							var swap_size = ((mcPhyInfoJsonObj.swapSize==null)? "":mcPhyInfoJsonObj.swapSize+"Mbytes");
							$("#swap_size").html(swap_size);//CPU使用率
						}
					}
				}
			
			}
		});	
	},
	//切换到小型机信息
	returnInfo : function(){
		$("#mcmanage .hostadmin_pro").show(); 
		$("#mcMonitorInfo").hide();
		$("#fanhui").hide();
	},
	refresh : function (){
		var hcEInstanceId=$("#mcinstanceId").val();
		if(hcEInstanceId==null){
			return false;
		}
		mcmanage.get_hc_monitor_info(hcEInstanceId);
	},
	//-----------------------------------------小型机操作（启动、重启、停止）-------------------------
	
	hcStart : function(id,hcEInstanceId){
		var params={hcID:id,instanceId:hcEInstanceId};
		mcmanage.hcModify("hcModify!hcStart.action",params,id,"启动");
	},
	hcReboot : function(id,hcEInstanceId){
		var params={hcID:id,instanceId:hcEInstanceId};
		mcmanage.hcModify("hcModify!hcReboot.action",params,id,"重启");
	},
	hcStop : function(id,hcEInstanceId){
		var params={hcID:id,instanceId:hcEInstanceId};
		mcmanage.hcModify("hcModify!hcStop.action",params,id,"停止");
	},
	
	hcModify:function(actionName,params,id,str){
		if(confirm("您确定要执行‘"+str+"’小型机操作吗？")){
			$.ajax({
				url : mcmanage.root+"/"+actionName+"",
				type : 'POST',
				async: false,
				data : params,
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						alert(data);
						return false;
					}else{
						query();
					}
				}
			});
		}

	},
	 // ------------------------------------------产品续订-------------------------------------------
	showProductXDDiv : function(type,id){
		  var tdObj = $("#td_bw_" + type + "_" + id);
		    $("#pop_erneuern").css("top", tdObj[0].offsetTop+40).css("left", tdObj[0].offsetLeft - 100).css("display", "block");
		    mcmanage.getInstancePeriodInfo(id);
    },
    
    PublicPrivateSwitch : function() {			 
        $.getScript(mcmanage.root+"/js/privateSkySwitch.js", function(e){
        	mcmanage.privateSkySwitch = privateSkySwitch;
        	mcmanage.strCloud = mcmanage.privateSkySwitch.getCloudInfo();
        });
  	},

	getInstancePeriodInfo : function(id) {
		$.ajax({
			url : "/UCFCloudPortal/order/findInstancePeriodById.action?id="+id,
			type : 'POST',
			dataType : 'json',
			success : function(data) {
				if (data != null) {
					//alert(mcmanage.strCloud);
					$(".popwindow #old_period").html(data.buyPeriod);
					$(".popwindow #due_date").html(data.expireDateString);
					//to fix bug [1829]
		        	if(mcmanage.strCloud == "1"){    		        		
		        		$(".popwindow .cycle").html(data.price+"元/"+data.unitString);
		        	} 
		    		if(mcmanage.strCloud == "2"){    		    			
		    			$(".popwindow .cycle").html(data.unitString);
		    		} 
					
					$("#pop_erneuern #xd_unit").html(data.unit);
					$("#pop_erneuern #xd_id").html(id);
				}
			}
		});
	},
	updateInstancePeriod : function(){
		$.ajax({
			url : "/UCFCloudPortal/order/updateInstancePeriod.action?id="+Number($("#pop_erneuern #xd_id").html()),
			type : 'POST',
			data : {unit:$("#pop_erneuern #xd_unit").html(),num:Number($("#pop_erneuern .tilength").val())},
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

function mcState(info){
	$.mcmanage || ($.mcmanage = mcmanage);	
	mcmanage.root="..";
	mcmanage.init();
	mcmanage.getMCListNew(info.id);
	
	var stateName = "";		
	switch(info.state){	
		case 1:stateName="申请";break;
		case 2:stateName="运行中";break;
		case 3:stateName="申请处理中";break;
		case 4:stateName ="已删除";break;
		case 5:stateName="已关机";break;
		case 6:stateName="操作执行中";break;
		case 7:stateName = '创建失败';break;
		default:stateName = "就绪";	
	}
	return stateName;
};

function mcOperate(info){
    var serviceState=$('#serviceState').val();
	//var curMC = mcmanage.minicomputer;
	//console.log(curMC);
	var dom = ''; 
	//alert(info.state);
	if(info.state==2){
		//<a href="#" class="editicon3" title="解绑定" onclick="tipManage.goUnBindUser('+ipid+',\'' + j.id + '\', \'' + j.ip + '\'); return false;">
		dom='<a href="#" title="修改" id="td_bw_1_' + info.id + '"  onclick="javascript:mcmanage.showMCDiv(1, ' + info.id + ',\''+info.memory_size+'\',\''+info.cpu_num+'\',\''+info.cluster_id+'\');"><img src="../images/modify.png" />'
		+'<a title="重启" href="javascript:mcmanage.hcReboot(' + info.id + ','+info.eInstanceId+');"><img src="../images/restart.png" /></a>'
		+'<a title="停止" href="javascript:mcmanage.hcStop(' + info.id + ','+info.eInstanceId+');"><img src="../images/stop.png" />';
    	var str = main.whichMonitor;
    	if(privateSkySwitch.getCloudInfo()==2||str.indexOf('小型机') >=0){//bug 0004851 0004528 0005140
    		 dom += serviceState!=6?'<a href="javascript:showProductMonitor(\'MINPHY\','+info.eInstanceId +')" title="监控"><img src="../images/ulList1_liIcon_monitor.png"/> </a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/ulList1_liIcon_monitor.png"/>';
    	} else {
    		 dom += '';
    	} 
		dom=serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />'
		+'<img src="../images/restart.png" />'
		+'<img src="../images/stop.png" />'
		+'<img src="../images/ulList1_liIcon_monitor.png"/>';
	}else if(info.state==6||info.state==3){		
		dom = '<span><img src="../images/loading1.gif"></span>';
	}else if(info.state==5){
		dom+='<a href="#" title="修改" id="td_bw_1_' + info.id + '"  onclick="javascript:mcmanage.showMCDiv(1, ' + info.id + ',\''+info.memory_size+'\',\''+info.cpu_num+'\',\''+info.cluster_id+'\');"><img src="../images/modify.png" />'
			+'<a title="启动" href="javascript:mcmanage.hcStart(' + info.id + ','+info.eInstanceId+');"><img src="../images/start.png" />';
		dom=serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />'
		+'<img src="../images/start.png" />';
	}
	return dom;
}

function initUpdatePopDiv(type, id, size,num,clusId){
	$("#add_confirm").empty();
	$("#add_confirm").load(mcmanage.root + "/component/minicomputer/mcupdate.html","",function(){
		mcmanage.clickEventHandler();
		$("#add_confirm #mcinstanceId").val(id);
	    $("#add_confirm #mcoperType").val(type);
	    $("#add_confirm #mccurSize").val(size);
	    $("#mccurNum").val(num);
	    $("#mcclusterId").val(clusId);	    
	   // console.log($("#mccurNum").val()+"==="+$("#mccurSize").val());
		$("#add_confirm #close").click(function(){
			$("#add_confirm").empty();
			$(".popupDiv1,.shade").fadeOut("fast");
            query();
		});
	});
}