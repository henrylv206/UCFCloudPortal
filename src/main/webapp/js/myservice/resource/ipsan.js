function ipsanConfig(){
	
}
function ipsanRefRes(info){
    var Vvmdisplayname='';
    if(info.state == "2"){
    	 var bsstate = "0";
    	 var vmid = "0";
    	 var iqn = "";
    	 var tip = "";
    	 var stateAttach = 0;
//    	    var  json = ipsanmanage.getIpsanInfoByRescode(info.resCode);
//    	    bsstate = json.BSState;
//	    	vmid = json.VMID;resourcePoolsId
    	var  json = ipsanmanage.getIpsanInfoByRescode(info.resCode,info.resourcePoolsId);
	    if (json!=null && json.length>0){
	    	for(var i = 0; i< json.length; i++ ){
	    		bsstate = json[i].BSState;
    	    	vmid = json[i].VMID;
    	    	json[i].InstanceName;
    	    	  if (bsstate == "1") {
    	          	var Displayname = "";
//    	              resource.server.getServer("findPortalMyVM", {
//    	  			    	data: {volumeid: info.id, pagesize: 100,page: 1,async : false},
//    	  			    	async:false,
//    	  			    	timeout:3000000,
//    	  		            success: function(vm){         
//    	  	                    for (var a = 0; a < vm.instanceList.length; a++) {
//    	  	                        var v = vm.instanceList[a];	  
////    	  	                        if(v.e_instance_id!=0 && v.e_instance_id == vmid){
//    	  	                         if(v.id!=0 && v.id == vmid){
//    	  	                        	Displayname = v.instance_name;
//    	  	                        	stateAttach = 1;
//    	  	                        	break;
//    	  	                        }                    			                  
//    	  	                    }  	
//    	  		            }
//    	  			    });	
    	          	stateAttach = 1;
    	          	Displayname = json[i].InstanceName;
    	          	//to fix bug:5311
    	          	Vvmdisplayname = Vvmdisplayname + Displayname+"  ";
    	          }
//    	    	  else if(bsstate == "2"){
//    	          	Vvmdisplayname = "未绑定虚机";
//    	          }
    	    	  else{
    	          	Vvmdisplayname += ''; 
    	          }
    	    	  
    	    	  
	    	}
	    }
	    if(stateAttach == 0){
	    	//Vvmdisplayname = "未绑定虚机";//fix bug7689未绑定虚机时与虚拟磁盘处理相同：显示空白
	    }
	    
	}else{
		Vvmdisplayname ="";
	}
    if(info.state == "6"){
    	var Displayname = info.vmname==undefined?"":j.vmname;
        if(Displayname.length>12){
        	Displayname = Displayname.substring(0,12) + '...';
        }
    	Vvmdisplayname = Displayname;
    } 
//    return [Vvmdisplayname];
    //to fix bug:3717
    return Vvmdisplayname;
}

function ipsanState(info){
	 var showState ='';
	 var bsstate = "0";
	 if(info.state=="1"){
    	 showState = "申请";
     }else if(info.state=="2"){
		var  ipsaninfo = ipsanmanage.getIpsanInfoByRescode(info.resCode,info.resourcePoolsId);
		if (ipsaninfo!=null){
			for(var i = 0; i< ipsaninfo.length; i++ ){
	    		bsstate = ipsaninfo[i].BSState;
	    		 if (bsstate == "1"){
	    			 break;
	    		 }
			}
		}
		 if (bsstate == "1"){
			 showState = "已挂载";
		 }else if(bsstate == "2"){
			 showState = "就绪";
		 } else {
			 showState = "就绪";
		 }
     }else if("3"==info.state){
    	 showState = "申请处理中 ";
     }else if("4"==info.state){
    	 showState = "已删除 ";
     } else if("6" == info.state) {
    	 showState = "操作执行中 ";
     }else if("7" == info.state) {
    	 showState = "创建失败 ";
     }
	 return showState;
}

function ipsanOperate(info){
    var serviceState=$('#serviceState').val();
    var dom='';
    var vGuaZai='';
    var vJieGua='';
  	var vmEid = 0;
  	var bsstate = "0";
  	var vmid = "0";
  	var InitiatorName = '';
  	var TargetIP = '';
  	var  json = ipsanmanage.getIpsanInfoByRescode(info.resCode,info.resourcePoolsId);
  	//to fix bug:5040  5095
  	if (json!=null &&  json.length>0){
  		bsstate = json[0].BSState;
  		vmid = json[0].VMID;
  		InitiatorName = json[0].InitiatorName;
  		TargetIP = json[0].TargetIP;
  	}
  	var vmName=info.vmName;
  	//公网ip的id
    var ipid = info.ipAddressId;
      if (info.eInstanceId!=null&&info.eInstanceId!='') { 
      	vmEid = Number(info.eInstanceId);
      } 
     var modify ='';
  	 if(info.state == "2"){
  		 modify ='<a href="#" title=\"变更\" onclick="ipsanmanage.loadPopDiv_modify(\'' + info.id + '\', \'' + info.storageSize + '\')" ><img src="../images/modify.png" /></a>';
  		 modify = serviceState!=6?modify:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />';
  		 if (bsstate=="1") {//to fix bug:5071,7613,5239
         	vJieGua='<a href="#" onclick="ipsanmanage.loadPopDiv_detach(\'' +info.id + '\', \'' +info.vmInstanceId + '\', \'' +info.resourcePoolsId + '\');" title=\"解除挂载\" >'
         	       +'     <img src="../images/unPlugdisk.png" />'
         	       +'    </a>';
         	vJieGua = serviceState!=6?vJieGua:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/unPlugdisk.png" />';
         }
//  		 else if(bsstate=="2"){
//        	 vGuaZai='<a href="#" onclick="ipsanmanage.loadPopDiv_bind(\'' + info.id + '\',\'' + info.e_instance_id + '\',\'' + InitiatorName+ ' \',\'' + TargetIP + '\');" title=\"挂载\" >'
        	 vGuaZai='<a href="#" onclick="ipsanmanage.loadPopDiv_bind(\'' + info.id + '\',\'' + info.resourcePoolsId + '\',\'' + InitiatorName+ ' \',\'' + TargetIP + '\');" title=\"挂载\" >'
             +'      <img src="../images/plugdisk.png" /> '
             +'    </a>';
//         }	                                
 }else{
     vGuaZai=' ';                            	
 }
  	dom += modify;
    dom += vGuaZai;
    dom +=vJieGua;
  	return dom;
}


var accessArray =new Array();
var ipsanmanage = {
		
		loadPopDiv_modify: function (id,storage_size){	
			//to fix bug:4413
//			var dataListLength = ipsanmanage.diskEditCheck(id,storage_size);
//			if(dataListLength ==0){
//				$("#add_confirm").hide();
//				alert("没有可用的模板");
//				return;
//			}
			$("#add_confirm").empty();
			$("#add_confirm").load("../component/ipsan/ipsanmanage.html","",function(){
				$("#close").click(function(){
					$("#add_confirm,.shade").fadeOut("fast");
					query();
					});
			ipsanmanage.diskEdit(id,storage_size);
			});
		},

		loadPopDiv_bind: function (instanceId, resourcePoolsId,InitiatorName,TargetIP){  
		    $("#add_confirm").empty();
		    $("#add_confirm").load("../component/ipsan/ipsanoperateMulti.html","",function(){
		        $("#close").click(function(){
		            $("#add_confirm,.shade").fadeOut("fast");
		            });
//		     ipsanmanage.selectBind(id, e_instance_id,InitiatorName,TargetIP);
		     ipsanmanage.selectBindMulti(instanceId, resourcePoolsId,1);
		 	$("#machineTypeS").change(function(){
	       		var  selectType = $("#machineTypeS option:selected").attr("id");
	 			if(selectType==1){
	 				ipsanmanage.selectBindMulti(instanceId,resourcePoolsId,selectType);  
	 			}else if(selectType==2){
	 				ipsanmanage.selectBindMulti(instanceId,resourcePoolsId,selectType); 
	 			}else if(selectType==3){
	 				ipsanmanage.selectBindMulti(instanceId,resourcePoolsId,selectType); 
	 			}
	 		});     
		 	
		    });
		},
		
		loadPopDiv_detach: function (instanceId, vmInstanceId, resourcePoolsId){  
		    $("#add_confirm").empty();
		    $("#add_confirm").load("../component/ipsan/ipsandetachMulti.html","",function(){
		        $("#close").click(function(){
		            $("#add_confirm,.shade").fadeOut("fast");
		         });
//		     ipsanmanage.selectBind(id, e_instance_id,InitiatorName,TargetIP);
		     ipsanmanage.selectDetachMulti(instanceId, resourcePoolsId,1);
		    });
		},
		
		 selectBindMulti: function(instanceId,resourcePoolsId,oper){
			 	
			 	$("#instanceId").val(instanceId);
				$("#resourcepoolsid").val(resourcePoolsId);
			 		var ul = "/resourcesUsed/showUserVmMcPm.action";
			 		var typeId = 1;
				    $("#add_confirm").show();
				    if(oper==1){
				    	typeId = 1;
				    	$('#popTitle').html("挂载虚拟机");
				    }else if(oper==2){
				    	typeId = 3;
			    	 $('#popTitle').html("挂载小型机");
				    }else if(oper==3){
				    	typeId = 10;
			    	 $('#popTitle').html("挂载物理机");
				    }
					$('#czz').html('请选择：');
					
					$.ajax({
					url : resource.root+ul,
					type : 'POST',
					data : {
						typeId:typeId,
						volumeinstanceId:instanceId,
						resourcePoolsId:0,
						zoneId:0
						},
					async : false,
					dataType : 'json',
					success : function(data) {
						var vResName = '';
						var instancelist = data; 
						if(instancelist.length==0){
			    			vResName += '';
			    			$("#params").html("");
			    		}else{
		                    for (var a = 0; a < instancelist.length; a++) {
		                        var v = instancelist[a];	  
		                        	vResName += '<option selected="selected" id="'+v.id+'" value="' +  v.targetIP + '"   title="' +  v.targetIP+ '">' + v.instance_name + '</option>';
		                    }  	
		                    $("#tr_storage").show();
							$('#params').show();
							$('#add_confirm').show();
							$("#params").append(vResName);
			    		}
					}
				});	    
			        $('#close').attr('onclick', 'ipsanmanage.cancel()');
			        $("#vmbind_btn").attr("onclick","ipsanmanage.attachVolumeMulti('"+ instanceId + "');");
				     
			     },
			     selectDetachMulti: function(instanceId,resourcePoolsId,oper){
					 	$("#instanceId").val(instanceId);
						$("#resourcepoolsid").val(resourcePoolsId);
					 		var ul = "/resourcesUsed/showDetachMachine.action";
					 		var typeId = 1;
						    $('#popTitle').html("解挂载虚拟机、物理机、小型机");
							$('#czz').html('请选择：');
							
							$.ajax({
							url : resource.root+ul,
							type : 'POST',
							data : {
								typeId:typeId,
								volumeinstanceId:instanceId,
								resourcePoolsId:resourcePoolsId,
								zoneId:0
								},
							async : false,
							dataType : 'json',
							success : function(data) {
								var vResName = '';
								var instancelist = data; 
								if(instancelist.length==0){
					    			vResName += '';
					    			$("#params").html("");
					    		}else{
				                    for (var a = 0; a < instancelist.length; a++) {
				                        var v = instancelist[a];	  
				                        var templateType=v.template_type;
				                        var templateName = "";
				                        if (templateType==1){
				                        	templateName ="虚拟机";
				                        }else if (templateType==3){
				                        	templateName ="小型机";
				                        }else if (templateType==10){
				                        	templateName ="物理机";
				                        }
				                        //to fix bug:7839
				                        	var titleshow =v.initiatorName+ '\n目标ip：'+v.targetIP;
				                        	vResName += '<option selected="selected" id="'+v.id+'" value="' +  v.targetIP + '"   title="' + titleshow +'">' + v.instance_name + '-'+templateName+'</option>';
				                    }  	
									$('#params').show();
									$('#add_confirm').show();
									$("#params").append(vResName);
					    		}
							}
						});	    
				        $('#close').attr('onclick', 'ipsanmanage.cancel()');
				        $("#vmbind_btn").attr("onclick",'ipsanmanage.detachVolumeSelected(\'' +instanceId + '\', \'' +resourcePoolsId + '\');');
		 },

		 selectBind: function(id, e_instance_id,InitiatorName,TargetIP){    
			     $("#add_confirm").show();
		    	var vResName = '';
	        	 resource.server.getServer("findPortalMyVM", {
				    	data: {volumeid: id, pagesize: 100,page: 1,async : false},
				    	async:false,
				    	timeout:30000,
			            success: function(vm){
				    		if(vm.instanceList.length==0){
				    			vResName += '';
				    		}else{
			                    for (var a = 0; a < vm.instanceList.length; a++) {
			                        var v = vm.instanceList[a];	  
			                        if (v.e_instance_id!=0){
			                        	vResName += '<option selected="selected" id="'+v.e_instance_id+'" value="' +  v.nicsBOs[0].ip + '"   title="' +  v.nicsBOs[0].ip + '">' + v.instance_name + '</option>';
			                        }
			                    }  	
				    		}
				    		 $("#vm_select").html(vResName);
			            }
				    });
	        	
		        $("#vmbind_btn").attr("onclick","ipsanmanage.attachVolume('"+ id + "',\'" + e_instance_id +"\',\'" + InitiatorName +" \', \'" + TargetIP + "\');");
		},
		     
		attachVolumeMulti:  function(volumeinstanceId){
		    var solf = resource.diskmanage;                        
		    var vmIP = $("#params").attr("value");
		    var vminstanceId = $("#params option:selected").attr("id");
//		    vminstanceId = 1159;
		    var resourcePoolsId = $("#resourcepoolsid").val();
		    if (vminstanceId==null || vminstanceId=="undifined"){
		    	alert('没有选择要绑定的机器');
		    	return;
		    }
		    //fixed bug:5199,5215 先隐藏弹窗口，query()刷新才会生效
//		    $("#add_confirm").hide();
		    $.ajax({
				url : resource.root+"/resourcesUsed/diskAttachVolume.action",
				type : 'POST',
				async : false,
				data : {
					vmIP : vmIP,
					vminstanceId:vminstanceId,
					volumeinstanceId : volumeinstanceId,
					resourcePoolsId:resourcePoolsId
				},
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						ipsanmanage.loadPopDiv_message(data);
//						query();//to fix bug:3992
					}else{
					    //fix bug:7869
						ipsanmanage.loadPopDiv_message(data);
					}
				}
			});
		},
		
		loadPopDiv_message: function (message){  
		    $("#add_confirm").empty();
		    $("#add_confirm").load("../component/ipsan/ipsanMessage.html","",function(){
		        $("#close").click(function(){
//		            fix bug:7977 7974
		            $("#add_confirm").hide();	
		            query();
		         });
		        $('#popTitle').html("提示信息");
		        $('#message').html(message);
		    });
		},
		
		attachVolume:  function(volumeinstanceId, volumeid, InitiatorName, TargetIP){
		    var solf = resource.diskmanage;                        
		    $("#td_bw_1_" + volumeinstanceId).html("");
//		    alert("InitiatorName = " + InitiatorName + "   TargetIP = " + TargetIP)
		    $.ajax({
				url : resource.root+"/resourcesUsed/diskAttachVolume.action",
				type : 'POST',
				data : {
					volumeid : volumeid,
					vmid: $("#vm_select option:selected").attr("id"),
					vmIP : $("#vm_select").attr("value"),
					volumeinstanceId : volumeinstanceId
				},
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						alert(data);
					}else{
						alert(data);
                        query();
					}
					$("#add_confirm").hide();	
				}
			});
		},

		detachVolume: function(volumeinstanceId,vminstanceId,resourcePoolsId){    	
		    var solf = resource.diskmanage;
		    //to fix bug:5198
//		    alert("dddd");
		    if (confirm('如果该磁盘已经在使用，请先登录操作系统卸载。\n\n您确认要解挂吗？')) {
		        $.ajax({
					url : resource.root+"/resourcesUsed/Axisdetachvolume.action",
					type : 'POST',
					data : {
//						volumeid : volumeid,
//						vminstanceId : vminstanceId,
						volumeinstanceId : volumeinstanceId,
						resourcePoolsId:resourcePoolsId
					},
					dataType : 'json',
					success : function(data) {
						if(data.search("error")!=-1){
							alert(data);
						}else{
							alert(data);
                            query();
						}
						$("#add_confirm").hide();	
					}
				});
		    }
		},
		
		detachVolumeSelected: function(volumeinstanceId,resourcePoolsId){    	
	    var solf = resource.diskmanage;
	    var vminstanceId = $("#params option:selected").attr("id");
	    if (vminstanceId==null || vminstanceId=="undifined"){
	    	alert('没有选择要绑定的机器');
	    	return;
	    }
	    //fix bug:7876
	    $("#add_confirm").hide();
	    if (confirm('如果该磁盘已经在使用，请先登录操作系统卸载。\n\n您确认要解挂吗？')) {
	        $.ajax({
				url : resource.root+"/resourcesUsed/Axisdetachvolume.action",
				type : 'POST',
				data : {
//					volumeid : volumeid,
					vminstanceId : vminstanceId,
					volumeinstanceId : volumeinstanceId,
					resourcePoolsId:resourcePoolsId
				},
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						alert(data);
						query();
					}else{
						alert(data);
                        query();
					}
					$("#add_confirm").hide();	
				}
			});
	    }
	},

		help: function() {
			alert('该块存储使用方法请参考《SkyForm_1.2_产品_弹性块存储_ISCSI挂载帮助手册》');
		},
		cancel : function() {
			$("#storageSize").empty();
			$("#operType").val("");
			$("#add_confirm").hide();
			},
		diskEdit: function(id,storage_size) {
			$("#add_confirm").show();
			   $("#ipsaninstanceId").val(id);
			    $("#ipsanreason").val("");
			    $("#rebandWidthSize").html("");
			    var vResName='';
				$.ajax({
					url : resource.root+"/sysParameters/getStorageSizeType.action",
					type : 'POST',
					async : false,
					data : {},
					dataType : 'json',
					success : function(json) {
						var flag =0;
        				if (json!=null&&json.split("|").length> 0 ) {	
							vResName +='<option id=\"\" value=0 >--请选择--</option>';
		          	    for (var i = 0; i < json.split("|").length; i++) {
			          		    var j = json.split("|")[i];
//			          		vResName += '<option  value="' +j.id  + '" >模板名称：'+ j.templateDesc +'&nbsp;大小：' + j.storageSize + 'G</option>';
			          		      if(Number(j) > storage_size){
			          		      	vResName += '<option  value="' +j  + '" >' + j + 'G &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
			          		      	flag = 1;
			          		      }
			          		 
			          	    }
			          	   // $("#rebandWidthSize").append('<a href="#" onclick="ipsanmanage.diskModifyOK(\'' + id + '\');" title=\"确定\"><p><img src="../images/done-square.png" /> 确定</p></a>');
		          	  $("#rebandWidthSize").html(vResName); 
						}
						if(flag == 0){
							$("#add_confirm").hide();
							alert("没有可用的变更属性");
							$("#add_confirm,.shade").fadeOut("fast");
						}
					}
				});
			},
			
			diskEditCheck: function(id,storage_size) {
				   var dataListLength = 0;
					$.ajax({
						url : resource.root+"/template/listDISKTemplateByType.action?storage_size=" + storage_size,
						type : 'POST',
						async : false,
						data : {},
						dataType : 'json',
						success : function(data) {
							 dataListLength = data.listResp.list.length;
						}
					});
				   return  dataListLength;
				},
			
		diskModifyOK: function(){
			if ($("#rebandWidthSize option:selected").attr("id") == "") {
			alert("请选择要修改的存储空间大小！");
			return;
			}
			if(commonUtils.len($("#ipsanreason").val()) > 100 ) {
	          alert("申请理由限定100个字符（一个汉字算2个字符）");
	          $("#ipsanreason").focus();
	          return;
        	}
			var instanceId = $("#ipsaninstanceId").val();
			var storage_size = $("#rebandWidthSize").find("option:selected").val();
			var applyreason = $("#ipsanreason").val();
			if(confirm("是否确认变更？")){
				var params = {			 
					instanceId : instanceId,
					storage_size : storage_size,
					apply_reason:applyreason
				};
				
				 $("#rebandWidthSize").html("");
		        $("#state_" + instanceId).html('申请处理中');  
		  	
				ipsanmanage.diskModify("vmModify!diskModify.action", params);    
			}
		},

		diskModify: function (actionName, params) {
			$.ajax({
				url : resource.root+"/"+ actionName + "",
				type : 'POST',
				async : false,
				data : params,
				dataType : 'json',
				success : function(data) {
					if (data.search("error") != -1) {
						return false;
					} else {//to fix bug: 3941
						alert("修改已经提交");
						$("#add_confirm").hide();	
                        query();
					}
//					$("#add_confirm").hide();
					
				}
			});
		},

		getIpsanInfoByRescode: function(rescode,resourcePoolsId){    	
		    var solf = ipsanmanage;
		    var ipsaninfo = null;
		        $.ajax({
					url : resource.root+"/vdc_volumemanage/getIpsanInfoByRescode.action",
					type : 'POST',
					data : {
						"param": rescode,
						resourcePoolsId:resourcePoolsId
					},
					dataType : 'json',
					async : false,
					success : function(data) {
//						if(data==null || data.search("error")!=-1){
						if(data==null){
							//to fix bug:3233
//							alert("调用ipsan接口失败:"+data);
						}else{
							ipsaninfo = data;
							
						}
					}
				});
		        return ipsaninfo;
		}
};   
