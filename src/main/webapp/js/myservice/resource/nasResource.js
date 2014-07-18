function nasRefRes(info){
    var Vvmdisplayname='';
    if(info.state == "2" || info.state == "6" ){
    	    var  json = nasmanage.getNasInfoByRescode(info.id,info.resourcePoolsId);
    	    var data =  $.evalJSON(json);
        if (data!=null&&data.records.length> 0 ) {
        	var Displayname = "";
        	for(var i = 0; i <data.records.length; i++){
        		if(i==data.records.length -1){
        		Vvmdisplayname += data.records[i].accessValue;
        		}else{
        		Vvmdisplayname += data.records[i].accessValue +",";
        		}
        	
        	}
        }else{
        	Vvmdisplayname = "未绑定";
        }
	}else{
		Vvmdisplayname ="";
	}
    return Vvmdisplayname;
}
function nasConfig(info){
//var dirinfo =nasmanage.getNasDirInfoByRescode(info.id,info.resourcePoolsId);
//$("#notice_Info").html("").html(dirinfo);
return info.storageSize+'G';
}
function nasState(info){
	 var showState ='';
	 if(info.state=="1"){
    	 showState = "申请";
     }else if(info.state=="2"){
		 showState = "就绪";
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

function nasOperate(info){
    var serviceState=$('#serviceState').val();
    var dom='';
    var operate='';
    var modify ='';
  	 if(info.state == "2"){
         	 var Einstanceid = "";
//         	 operate='<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/unPlugdisk.png" />';
        	 operate+='<a href="#" onclick="nasmanage.loadPopDiv_bind(\''+ info.id+'\',\''+ info.resourcePoolsId+'\');" title=\"挂载\" >'
             +'<img src="../images/plugdisk.png" />'
             +'</a>';
//              operate+='<a href="#" onclick="nasmanage.loadPopDiv_bind(\''+ info.id+'\',\''+ info.resourcePoolsId+'\',2);" title=\"挂载小型机\" >'
//             +'      <img src="../images/plugdisk.png" /> '
//             +'    </a>';
//              operate+='<a href="#" onclick="nasmanage.loadPopDiv_bind(\''+ info.id+'\',\''+ info.resourcePoolsId+'\',3);" title=\"挂载物理机\" >'
//             +'      <img src="../images/plugdisk.png" /> '
//             +'    </a>';
             operate+='<a href="#" onclick="nasmanage.loadPopDiv_noaccess(\''+ info.id+'\',\''+ info.resourcePoolsId+'\');" title=\"卸载\" >'
 	         +'<img src="../images/unPlugdisk.png" />'
 	         +'</a>';
 	        modify ='<a href="#" title=\"变更\" onclick="nasmanage.loadPopDiv_modify(\'' + info.id + '\', \'' + info.resourcePoolsId + '\',\'' + info.storageSize + '\')" ><img src="../images/modify.png" /></a>';
  		    modify = serviceState!=6?modify:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />';
  		    //添加详情
  		    modify+='<a href="#" onclick="nasmanage.getNasDirInfoByRescode(\''+ info.id+'\',\''+ info.resourcePoolsId+'\');" title=\"详情\" >'
 	        +'<img src="../images/detail.png" />'
 	        +'</a>';
 }else{
     operate=' ';                            	
 }
    dom += operate;
    dom += modify;
  	return dom;
}



var accessArray =new Array();
var nasmanage = {
		
		loadPopDiv_modify: function (id,resourcePoolsId,storage_size){	
//			var dataListLength = nasmanage.nasEditCheck(id,storage_size);
			$("#add_confirm").empty();
			$("#add_confirm").load("../component/nas/nasoperate.html","",function(){
				$("#close").click(function(){
					$("#add_confirm .shade").fadeOut("fast");
					query();
					});
			nasmanage.diskEdit(id,resourcePoolsId,storage_size);
			});
		},
		
		//oper 1:vm ,2:mc,3:pm
		loadPopDiv_bind: function (nasInstance,resourcePoolsId){
		    $("#add_confirm").empty();
		    $("#add_confirm").load("../component/nas/nasoperate.html","",function(){
		        $("#close").click(function(){
		            $("#add_confirm .shade").fadeOut("fast");
		            });
		        $("#params").attr("style","width:220px");    
		        nasmanage.selectBind(nasInstance,resourcePoolsId,1);    
		       	$("#machineTypeS").change(function(){
		       		var  selectType = $("#machineTypeS option:selected").attr("id");
		 			if(selectType==1){
		 			 nasmanage.selectBind(nasInstance,resourcePoolsId,selectType);  
		 			}else if(selectType==2){
		 			 nasmanage.selectBind(nasInstance,resourcePoolsId,selectType); 
		 			}else if(selectType==3){
		 			 nasmanage.selectBind(nasInstance,resourcePoolsId,selectType); 
		 			}
		 		});     
		    
		    });
		},
		
		loadPopDiv_noaccess: function (nasInstance,resourcePoolsId){
		    $("#add_confirm").empty();
		    $("#add_confirm").load("../component/nas/nasoperate.html","",function(){
		        $("#close").click(function(){
		            $("#add_confirm .shade").fadeOut("fast");
		            });
		     nasmanage.selectAccessForNas(nasInstance,resourcePoolsId);
		    });
		},
		  selectAccessForNas : function(nasInstance,resourcePoolsId){
		  	
		  	 $("#machineTypeForNas").hide();   
		 	 $("#nasinstanceid").val(nasInstance);
			 $("#resourcepoolsid").val(resourcePoolsId);
		     $("#add_confirm").show();
		     $('#popTitle').html("卸载通过IP地址");
		     $('#czz').html('请选择:');
			var json = nasmanage.getNasInfoByRescode(nasInstance,resourcePoolsId);
    	    var data =  $.evalJSON(json);
        	if (data!=null&&data.records.length> 0 ) {	
        		$("#params").html("");
				var str_snap = "<option id=\"\">=请选择=</option>";
				$("#params").append(str_snap);
					for (var i = data.records.length - 1; i >= 0; i--) {
						str_snap = "<option id=\""
						+ data.records[i].accessId + "\">"
						+ data.records[i].accessValue+"</option>";
						$("#params").append(str_snap);
					}
				$("#tr_storage").show();
				$('#params').show();
				$('#add_confirm').show();
				$("#allvm_btn").show();
        	}else{
				alert('没有可用的机器！');
				$("#add_confirm").hide();	
        	}
        	$('#vmbind_btn').attr('onclick',
					'nasmanage.noaccessNas(\''+ $("#nasinstanceid").val()+'\',\''+ $("#resourcepoolsid").val()+'\')');
			$('#allvm_btn').attr('onclick',
					'nasmanage.noAllaccessNas(\''+ $("#nasinstanceid").val()+'\',\''+ $("#resourcepoolsid").val()+'\')');		
	        $('#close').attr('onclick', 'nasmanage.cancel()');
        	
		  },
		 selectBind: function(nasInstanceId,resourcePoolsId,oper){
		 	
		 	$("#nasinstanceid").val(nasInstanceId);
			$("#resourcepoolsid").val(resourcePoolsId);
		 		var ul = "/resourcesUsed/nasResourceOperate!showUserVm.action";
			    $("#add_confirm").show();
			    if(oper==1){
			    $('#popTitle').html("挂载虚拟机");
			    }else if(oper==2){
			    	ul =  "/resourcesUsed/nasResourceOperate!showUserMc.action";
		    	 $('#popTitle').html("挂载小型机");
			    }else if(oper==3){
			    	ul =  "/resourcesUsed/nasResourceOperate!showUserPM.action";
		    	 $('#popTitle').html("挂载物理机");
			    }
				$('#czz').html('请选择:');
				
				$.ajax({
				url : resource.root+ul,
				type : 'POST',
				data : {},
				async : false,
				dataType : 'json',
				success : function(data) {
					if(data.search("error")!=-1){
						alert(data);
						$("#add_confirm").hide();	
						query();
					}else{
						var instancelist = $.evalJSON(data);
						if(instancelist.length > 0){
							$("#params").html("");
							var str_snap = "<option id=\"\">=请选择=</option>";
							$("#params").append(str_snap);
							//fix bug id 0007789
							for (var i = instancelist.length - 1; i >= 0; i--) {
								if(instancelist[i].nicsBOs!=null){
									for(var j = 0;j < instancelist[i].nicsBOs.length;j++){
										if($.inArray(instancelist[i].nicsBOs[j].ip, accessArray) == -1){
											str_snap = "<option id=\""
											+ instancelist[i].id + "\" value=\""
											+ instancelist[i].nicsBOs[j].ip + "\"  title=\""
											+ instancelist[i].instance_name +":"+ instancelist[i].nicsBOs[j].ip + "\">"
											+ instancelist[i].instance_name +":"+ instancelist[i].nicsBOs[j].ip +"</option>";
											$("#params").append(str_snap);
										}
									}
								}		
							}
							$("#tr_storage").show();
							$('#params').show();
							$('#add_confirm').show();
						} else {
							alert('没有可用的机器！');
							$("#add_confirm").hide();	
						}
					}
				}
			});	    
				$('#vmbind_btn').attr('onclick',
					'nasmanage.accessNas(\''+ $("#nasinstanceid").val()+'\',\''+ $("#resourcepoolsid").val()+'\')');
		        $('#close').attr('onclick', 'nasmanage.cancel()');
		     },
		     
		accessNas:  function(nasInstanceId, resourcePoolsId){
			if ($("#params option:selected").attr("id") == "") {
			alert("请选择需要挂载的资源！");
			return;
			}
			var instanceIp = $("#params option:selected").attr("value");
		    $.ajax({
				url : resource.root+"/resourcesUsed/nasResourceOperate!attachNasResource.action",
				type : 'POST',
				data : {
					nasInstanceId : nasInstanceId,
					resourcePoolsId: resourcePoolsId,
					instanceIp : instanceIp
				},
				dataType : 'json',
				success : function(data) {
						alert(data);
						nasmanage.getNasInfoByRescode(nasInstanceId,resourcePoolsId);
						$("#add_confirm").hide();	
						query();
				}
			});
		},

		noaccessNas: function(nasInstanceId,resourcePoolsId){    	
			  if ($("#params option:selected").attr("id") == "") {
				alert("请选择需要卸载的资源！");
				return;
				}
		    	var accessId = $("#params option:selected").attr("id");
		    $.ajax({
				url : resource.root+"/resourcesUsed/nasResourceOperate!detachNasResource.action",
				type : 'POST',
				data : {
					nasInstanceId : nasInstanceId,
					resourcePoolsId: resourcePoolsId,
					accessId : accessId
				},
				dataType : 'json',
				success : function(data) {
						alert(data);
						nasmanage.getNasInfoByRescode(nasInstanceId,resourcePoolsId);
						$("#add_confirm").hide();	
						query();
					
				}
			});
		},
		noAllaccessNas: function(nasInstanceId,resourcePoolsId){    	
		    $.ajax({
				url : resource.root+"/resourcesUsed/nasResourceOperate!detachAllNasResource.action",
				type : 'POST',
				data : {
					nasInstanceId : nasInstanceId,
					resourcePoolsId: resourcePoolsId
				},
				dataType : 'json',
				success : function(data) {
						alert(data);
						nasmanage.getNasInfoByRescode(nasInstanceId,resourcePoolsId);
						$("#add_confirm").hide();	
						query();
					
				}
			});
		},
//		help: function() {
//			alert('该块存储使用方法请参考《SkyForm_1.2_产品_弹性块存储_ISCSI挂载帮助手册》');
//		},
		cancel : function() {
		$("#storageSize").empty();
		$("#operType").val("");
		$("#add_confirm").hide();
		},
		diskEdit: function(id,resourcePoolsId,storage_size) {
			$("#add_confirm").show();
			$("#machineTypeForNas").hide();
			$('#popTitle').html("请选择修改大小");
		    $('#czz').html('请选择:');
		    $("#tr_nas_applereason").show();
			   $("#resourcepoolsid").val(resourcePoolsId);
//			    $("#ipsanreason").val("");
//			    $("#rebandWidthSize").html("");
		    
			    var vResName='';
				$.ajax({
					url :resource.root+"/sysParameters/getStorageSizeType.action",
					type : 'POST',
					async : false,
					data : {resourcePoolsId: resourcePoolsId},
					dataType : 'json',
					success : function(json) {
						var flag =0;
        				if (json!=null&&json.split("|").length> 0 ) {	
							$("#params").html("");
							var str_snap = "<option id=\"\">=请选择=</option>";
		          	    for (var i = 0; i < json.split("|").length; i++) {
		          		    var j = json.split("|")[i];
		          		    if(Number(j) > storage_size){
		          		    	 str_snap += '<option  value="' +j + '" >' + j + 'G &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</option>';
		          		    	 flag = 1;
		          		    	}
			          	    }
			          	    $("#params").append(str_snap);
						}
						$('#vmbind_btn').attr('onclick',
						'nasmanage.diskModifyOK('+id+')');
		        		$('#close').attr('onclick', 'nasmanage.cancel()');
						
						if(flag == 0){
							$("#add_confirm").hide();
							alert("没有可用的变更属性");
							$("#add_confirm .shade").fadeOut("fast");
						}
					}
				});
			},
		diskModifyOK: function(id){
			if ($("#params option:selected").attr("id") == "") {
			alert("请选择要修改的存储空间大小！");
			return;
			}
			if(commonUtils.len($("#nasreason").val()) > 100 ) {
	          alert("申请理由限定100个字符（一个汉字算2个字符）");
	          $("#nasreason").focus();
	          return;
        	}
			var instanceId = id;
			var storage_size = $("#params").find("option:selected").val();
			var applyreason = $("#nasreason").val();
			if(confirm("是否确认变更？")){
				var params = {			 
					instanceId : instanceId,
					storage_size : storage_size,
					apply_reason:applyreason
				};
		        $("#state_" + instanceId).html('申请处理中');  
		        $("#caozuo_" + instanceId).html("");  
		  	
				nasmanage.diskModify("vmModify!diskModify.action", params);    
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
					} else {
						alert("修改已经提交");
						nasmanage.getNasInfoByRescode(params.instanceId,$("#resourcepoolsid").val());
						$("#add_confirm").hide();	
                        query();
					}
					
				}
			});
		},
		getNasDirInfoByRescode: function(nasInstanceId,resourcePoolsId){    	
		    var solf = nasmanage;
		    
		    var nasdirinfo = null;
		        $.ajax({
					url : resource.root+"/resourcesUsed/nasResourceOperate!getNasDirInfo.action",
					type : 'POST',
					data : {
						"nasInstanceId": nasInstanceId,
						"resourcePoolsId" : resourcePoolsId
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						if(data==null || data.search("error")!=-1){
							alert("获取详细信息失败:"+data);
						}else{
							nasdirinfo = data;
						  var jsonObject =  $.evalJSON(nasdirinfo);
						  var path =jsonObject.directory.path;
						  var host = jsonObject.directory.device.hostname;
						  	
						nasdirinfo = "路径:"+path+ "</br>主机:"+host;
						$("#add_confirm").empty();
					    $("#add_confirm").load("../component/nas/nasoperate.html","",function(){
					    	$('#popTitle').html("详细信息");
					    	$('#machineTypeForNas').attr("style","display:none");
					    	$('#tr_storage').attr("style","display:none");
					   		$("#nas_notice_Info").html("").html(nasdirinfo);
					   		$('#vmbind_btn').attr('onclick', 'nasmanage.cancel()');
		        			$('#close').attr('onclick', 'nasmanage.cancel()');
					    });
						showBg("add_confirm", "dialog_content");
						}
					}
				});
		        return nasdirinfo;
		},
		getNasInfoByRescode: function(nasInstanceId,resourcePoolsId){    	
		    var solf = nasmanage;
		    
		    var nasinfo = null;
		        $.ajax({
					url : resource.root+"/resourcesUsed/nasResourceOperate!getNasAccess.action",
					type : 'POST',
					data : {
						"nasInstanceId": nasInstanceId,
						"resourcePoolsId" : resourcePoolsId
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						if(data==null || data.search("error")!=-1){
						}else{
							nasinfo = data;
						  	var jsonObject =  $.evalJSON(nasinfo);
						  	//已分配的access放入数组
						  	accessArray = [];
						  	if (jsonObject!=null&&jsonObject.records.length > 0) {
						  		for(var i = 0;i<jsonObject.records.length;i++){
						  			accessArray.push(jsonObject.records[i].accessValue);
						  		}
						  	}
						}
					}
				});
		        return nasinfo;
		}
};   
