function tipConfig(info){
	var rs='';
	var ipAddress=info.ipAddress;
    var sp=tipmanage.serviceProvider[info.serviceProvider];
    if(sp!=undefined){
    	rs+='sp：'+sp;
    }
    if(ipAddress!=undefined){
    	rs+='，ip：'+ipAddress;
    }
	return rs;
}

function tipRefRes(info){
	var dom='';
	var name='';
    var serviceState=$('#serviceState').val();
	var vmEid = 0;
    var vmName = info.vmName; //此时代表可绑定的虚拟机名称
    if (info.eInstanceId!=null&&info.eInstanceId!='') {
    	vmId = info.vmId; //此时代表绑定的虚拟机id                        	
    	vmEid = Number(info.eInstanceId);                        	
    }
    if(vmEid>0){	    	            	
    	var subVMName = vmName;
    	if(vmName.length>10){
    		subVMName = vmName.slice(0,10);
    	}
    	dom = '<span id=\"VMnameforbinding_' + info.id + '\" >' + subVMName + '</span><span id=\"VMIdforbinding_' + info.id + '\" style=\"display: none;\">' + vmEid + '</span>';
    	name=vmName;
    }
    else if(vmEid==-1){
    	dom = '<span id=\"VMnameforbinding_' + info.id + '\" >负载均衡</span><span id=\"VMIdforbinding_' + info.id + '\" style=\"display: none;\">' + info.vmName + '</span>';
    	name='负载均衡';
    } 	
    return [dom, name];
}

function tipState(info){
	return stateName[info.state];
}
function tipOperate(info){	
    var serviceState=$('#serviceState').val();
//	loadPopDiv_tip();
	var dom='';
	//公网ip的id
    var ipid = info.ipAddressId;
	var vmEid = 0;
	var vmName=info.vmName;
	//公网ip的id
    var ipid = info.ipAddressId;
    if (info.eInstanceId!=null&&info.eInstanceId!='') {                     	
    	vmEid = Number(info.eInstanceId);      	
    }            
	if(3==info.state||6==info.state){
		dom = '<span><img src="../images/loading1.gif"></span>';
	}
	if(2==info.state){
		  //已绑定
		if(vmEid!=0){
			if (vmEid>0) {
		    	dom = '<a href="#" title="解绑定" onclick="tipmanage.goUnBindUser('+ipid+',\'' + info.id + '\', \'' + info.ipAddress + '\');"><img src="../images/unbind.png" /></a>';
		    	dom = serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/unbind.png" />';
			} 
		    else if(vmEid==-1){			                    	
		    	dom = '<a href="#" title="解绑定" onclick="tipmanage.goVSUnBindUser(\''+ info.ipAddress + '\','+ipid+','+info.id+');"><img src="../images/unbind.png" /></a>';
		    	dom = serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/unbind.png" />';
		    }
		}		                    
		else if(vmEid==0){
		    if(vmName!==''||vmName=='undefined'){
		    	dom = '<a href="#" title="虚服务绑定" onclick="tipmanage.selectUserVs(\'' + info.ipAddress + '\',\'' + info.id + '\');"><img src="../images/bind.png" /></a>';
		    	dom = serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/bind.png" />';
			}			                    
		    var dom1= '<a href="#" title="机器绑定" onclick="tipmanage.loadPopDiv_tip('+ipid+',\'' + info.id + '\', \'' + info.ipAddress + '\');"><img src="../images/bind.png" /></a>';
		    dom+=serviceState!=6?dom1:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/bind.png" />';
//		    dom+=serviceState!=6?'<a href="#" title="小型机绑定" onclick="tipmanage.selectBind('+ipid+',\'' + info.id + '\', \'' + info.ipAddress + '\',2);"><img src="../images/bind.png" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/bind.png" />';
//		    dom+=serviceState!=6?'<a href="#" title="物理机绑定" onclick="tipmanage.selectBind('+ipid+',\'' + info.id + '\', \'' + info.ipAddress + '\',3);"><img src="../images/bind.png" /></a>':'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/bind.png" />';
		}		
	}
	return dom;
}


var tipmanage = {
    root:"",
    strCloud: "",   
    serviceProvider:{0:"中国电信",1:"中国联通",2:"中国移动",3:"中国铁通"},
    init: function(){       
        
        tipmanage.PublicPrivateSwitch();
        
        $("#pop_erneuern #InputNo").click(function(){
             $("#pop_erneuern").hide();
             $("#pop_erneuern .tilength").val(1);
        });     
    },
    
    
 loadPopDiv_tip:function(ipid,infoid,ipaddress){  
    $("#add_confirm").empty();
    $("#add_confirm").load("../component/tip/tipoperate.html","",function(){
        $("#close").click(function(){
            $(".popupDiv1,.shade").fadeOut("fast");
            });
       tipmanage.selectBind(ipid,infoid,ipaddress,1);    
       	$("#machineTypeS").change(function(){
       		var  selectType = $("#machineTypeS option:selected").attr("id");
 			if(selectType==1){
 			 tipmanage.selectBind(ipid,infoid,ipaddress,selectType);  
 			}else if(selectType==2){
 			 tipmanage.selectBind(ipid,infoid,ipaddress,selectType); 
 			}else if(selectType==3){
 			 tipmanage.selectBind(ipid,infoid,ipaddress,selectType); 
 			}
 		});      
    });
},
    
    selectUserVs : function(ip,id){
       $("#add_confirm").empty();
       $("#add_confirm").load("../component/tip/tipoperate.html","",function(){
        $("#close").click(function(){
            $(".popupDiv1,.shade").fadeOut("fast");
            });
        
        $("#add_confirm").show();
    	$("#machineTypeForIp").hide();
    	$('#popTitle').html("绑定虚服务");
    	 $.ajax({
            url :  resource.root+"/publicIp/getVirtualServiceList.action",
    		type : 'POST',
		    data: {},
		    async:false,
	        success: function(data){	
	        	if(data.search("error")!=-1){
					alert(data);
					query();
				}else{
					var vslist = $.evalJSON(data);
					if(vslist.length > 0){
					  $("#vm_select").html("");
			          var str_snap ='<option  value="-1">=请选择=</option>';
			          $("#vm_select").append(str_snap);
			            for (var i = 0; i < vslist.length; i++) {
			            	str_snap = "<option id=\""
										+ vslist[i].vsId + "\" value=\""
										+ vslist[i].vsIp + "\">"
										+ vslist[i].vsName+":"+vslist[i].vsIp+"</option>";
										$("#vm_select").append(str_snap);
			            	
			            }
					}else{
						alert('没有可用的虚服务！');
						$("#add_confirm").hide();	
					}
					}
				}
    	 });
		 $("#vmbind_btn").attr("onclick","tipmanage.goVSBindUser(\'" + ip +" \', \'" + id + "\');");
		 }); 
    },
/////////////////////////////////////////////具体操作////////////////////////////////////////
     selectBind: function(ipid,id,ip,oper){    
    	$("#add_confirm").show();
    	var ul = "/resourcesUsed/nasResourceOperate!showUserVm.action";
		    if(oper==1){
		    $('#popTitle').html("绑定虚拟机");
		    }else if(oper==2){
		    	ul =  "/resourcesUsed/nasResourceOperate!showUserMc.action";
	    	 $('#popTitle').html("绑定小型机");
		    }else if(oper==3){
		    	ul =  "/resourcesUsed/nasResourceOperate!showUserPM.action";
	    	 $('#popTitle').html("绑定物理机");
		    }
    	
        $.ajax({
            url : resource.root+ul,
    		type : 'POST',
		    data: {},
		    async:false,
	        success: function(data){	
	        	if(data.search("error")!=-1){
					alert(data);
					query();
				}else{
					var instancelist = $.evalJSON(data);
					if(instancelist.length > 0){
					  $("#vm_select").html("");
			          var str_snap ='<option  value="-1">=请选择=</option>';
			          $("#vm_select").append(str_snap);
			            for (var i = 0; i < instancelist.length; i++) {
		            		if(instancelist[i].nicsBOs!=null){
								for(var j = 0;j < instancelist[i].nicsBOs.length;j++){
										str_snap = "<option id=\""
										+ instancelist[i].e_instance_id + "\" value=\""
										+ instancelist[i].nicsBOs[j].ip + "\">"
										+ instancelist[i].instance_name +":"+ instancelist[i].nicsBOs[j].ip +"</option>";
										$("#vm_select").append(str_snap);
								}
							}
			            }
					}else{
						alert('没有可用的机器！');
						$(".popupDiv1").hide();	
					}
				}
	        			    		                
	       }
		});		
        $("#vmbind_btn").attr("onclick","tipmanage.goBindUser('"+ipid+"',\'" + id +" \', \'" + ip + "\');");
     },
     
    //公网ip绑定---参数：此公网ip的 实例id、ip地址、状态
    goBindUser: function(ipid,id, ip) {
       	var optionObj = $("#vm_select option:selected"); //被选中的弹性主机
    	var optionValue = optionObj.attr("id"); //被选中的虚拟机的option的value---虚拟机id
    	var optionHtml = optionObj.html(); //被选中的虚拟机的option的htm---虚拟机名称
    	var privateIp =  optionObj.attr("value");
    	//判断时候选择了弹性主机
    	if (privateIp == "-1"){
    		alert("请选择需要绑定公网IP的机器");
    		return;
    	}else{//bug 0003903
            $("body").stopTime(timerKey);//bug 0003917
            $("#add_confirm").hide();//bug 0003913
            $('#caozuo_'+id).html('<span><img src="../images/loading1.gif"></span>');//bug 0003771
        	var url = resource.root+"/publicIp/bindPublicIp.action";
    		var data = {};
    		data.publicIpInstanceId = ipid; //需要绑定的公网ip实例id
    		data.publicIp = ip; //需要绑定的公网ip地址
    		data.bindVmId =  optionValue; //需要绑定的虚拟机id
    		data.privateIp = privateIp;  //选择的机器的ip地址
    		data.bindVmName =  optionHtml; //需要绑定的虚拟机名称
//    		var page = $("#curPage").val(); //当前页页码
    		$.ajax({
    			url : url,
    			type : 'POST',
    			data : data,
    			dataType : 'json',
    			timeout:300000000,
    			success : function(data) {
    				if (data != null) {
    					if("true"==data){
    						alert("绑定虚拟机成功");
    						//$("#solf .select3 option[value="+optionValue+"]").remove();
    					}
    					else if("false"==data){
    						alert("绑定虚拟机失败");
    					}//bug 0003230
    					query();
    //					$("#pipreason").html("");
    				}
    			},
                error : function(data){//bug 0003808                 
                    if (data != null) {
                         if("false"==data){
                            alert("绑定虚拟机失败");
                        }
                    }
                }
    		});
    	}
    },
    
    checkLbState : function(){
	   var flag=0;
        $.ajax({
            url : "../portal_lb/getLoadBalanceInstance.action",
            type : 'GET',
            async:false,
            dataType : 'json',
            timeout:300000000,
            success : function(data) {
            	if(data==null||data.state!=2){
            		flag=1;
            	}
            }
        });
    	return flag;
    },
    
    //公网ip解绑定---参数：此公网ip的 实例id、ip地址
    goUnBindUser: function(ipid,id, ip) {
    	var VMnameforbinding = $("#VMnameforbinding_" + id); //绑定在公网IP名上的弹性主机名称
    	var vmname = VMnameforbinding.html();
    	var VMIdforbinding = $("#VMIdforbinding_" + id); //绑定在公网IP名上的弹性主机id
    	var vmid = VMIdforbinding.html();    	
    	if(confirm("确定要将此公网IP从虚拟机" + vmname + "上解绑定吗？")){
    		if(tipmanage.searchRuleOfFirewall(vmid,1,ip)){
    			return;
    		}else{
                $("body").stopTime(timerKey);//bug 0003917
                $('#caozuo_'+id).html('<span><img src="../images/loading1.gif"></span>');   //bug 0003771
        		var url = resource.root+"/publicIp/unBindPublicIp.action";
        		var data = {};
        		data.publicIpInstanceId = ipid; //需要解绑定的公网ip实例id
        		data.publicIp = ip; //需要解绑定的公网ip地址
        		data.bindVmId =  vmid; //需要解绑定的虚拟机id
        		data.bindVmName =  vmname; //需要解绑定的虚拟机名称
        		var page = $("#curPage").val(); //当前页页码
        		$.ajax({
        			url : url,
        			type : 'POST',
        			data : data,
        			dataType : 'json',
        			//async : false,//bug 0003916
        			timeout:300000000,
        			success : function(data) {
        				if (data != null) {
        					if("true"==data){
        						alert("解绑定成功");
        						//$("#solf .select3").append("<option value="+vmid+">"+vmname+"</option>");
        					}
        					else if("false"==data){
        						alert("解绑定失败");
        					}//bug 0003230
    						query();
    //    					$("#pipreason").html("");
        				}
        			},
        			error : function(data){//bug 0003808    				
                        if (data != null) {
                        	 if("false"==data){
                                alert("解绑定失败");
                            }
                        }
        			}
        		});
    		}
    	}
    },
    
  //公网ip绑定虚服务---参数：虚服务IP,此公网ip的 ip地址、
    goVSBindUser: function(ip,id) {
	 	var optionObj = $("#vm_select option:selected"); //被选中的vs
    	var optionValue = optionObj.attr("id"); //被选中的虚拟机的option的value---虚拟机id
    	var optionHtml = optionObj.html(); //被选中的虚拟机的option的htm---虚拟机名称
    	var vsIp =  optionObj.attr("value");
    	//判断时候选择了vs
    	if (vsIp == "-1"){
    		alert("请选择需要绑定公网IP的虚服务");
    		return;
        }else{
        	$("#add_confirm").hide();
            $('#caozuo_'+id).html('<span><img src="../images/loading1.gif"></span>');  //bug 0003771
            var url = resource.root+"/publicIp/bindVSPublicIp.action";
            var data = {};
            data.publicIp = ip; 
            data.vsIP = vsIp;  //需要绑定的虚服务ip
            var page = $("#curPage").val(); //当前页页码
            $("body").stopTime(timerKey);//bug 0003917
    		$.ajax({
    			url : url,
    			type : 'POST',
    			data : data,
    			dataType : 'json',
    			timeout:300000000,
    			success : function(data) {
    				if (data != null) {
    					if("true"==data){
    						alert("绑定负载均衡成功！");
    					}
    					else if("false"==data){
    						alert("绑定负载均衡失败。");
    					}
    					else if("not"==data){
    						alert("绑定未成功！您还没有负载均衡业务，须先申请此业务。");
    					}
    					//bug 0003230
    					window.query();
    //					$("#pipreason").html("");
    				}
    			},
                error : function(data){//bug 0003808  
                    if (data != null) {
                         if("false"==data){
                            alert("绑定负载均衡失败");
                        }
                    }
                }
    		});
        }
    },
    
  //公网ip虚服务解绑定---参数：此公网ip的 实例id、ip地址、状态
    goVSUnBindUser: function(ip,ipid,id) {    	
    	if (confirm("确定要将此公网IP从负载均衡上解绑定吗？")) {  
    		if(tipmanage.searchRuleOfFirewall(0,-1,ip)){
    			return ;
    		}else{
                $("body").stopTime(timerKey);//bug 0003917
                $('#caozuo_'+id).html('<span><img src="../images/loading1.gif"></span>');//bug 0003771  
        		var url = resource.root+"/publicIp/unBindVSPublicIp.action";
        		var data = {};    		
        		data.publicIp = ip; //需要解绑定的公网ip地址
        		data.publicIpInstanceId = ipid; //需要解绑定的公网ip实例id
        		//var page = new Number($("#solfPage #PageNo").html()); //当前页页码
        		$.ajax({
        			url : url,
        			type : 'POST',
        			data : data,
        			//async : false, //bug 0003916
        			dataType : 'json',
        			timeout:300000000,
        			success : function(data) {
        				if (data != null) {
        					if("true"==data){
        						alert("解绑定成功");    						
        					}
        					else if("false"==data){
        						alert("解绑定失败");
        					}//bug 0003230
    						query();
    //    					$("#pipreason").html("");
        				}
        			},
                    error : function(data){//bug 0003808                 
                        if (data != null) {
                             if("false"==data){
                                alert("解绑定失败");
                            }
                        }
                    }
        		});
    		}
    	}
    },

    //推定前检查绑定的服务IP是否已经有安全策略
    searchRuleOfFirewall: function(vmEid,type,publicIp){
    	var isRule = false;
    	var id = 0;
    	//查询虚拟机
    	if(type==1){
    		id = vmEid;
    	}
    	//查询虚服务
    	else if(type==-1){
    		id = -1;
    	}
    	$.ajax({
			url : resource.root+"/publicIp/searchRuleOfFirewall.action",
			type : 'POST',
			data : {
			 bindVmId:id,
			 publicIp:publicIp},
			dataType : 'json',
			async : false,
			timeout:100000,
			success : function(data) {
				if (data != null) {					
					if(data.length>0){
						alert("请先删除该服务的防火墙规则！");
						isRule = true;
					}
				}
			}
		});
    	return isRule;
    },

  getCloudInfo: function(){
    var cloudInfo = 0;
    $.ajax({
      url: resource.root + "/sysParameters/getCloudInfo.action",
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
        tipmanage.strCloud = tipmanage.getCloudInfo();
    }       
};    