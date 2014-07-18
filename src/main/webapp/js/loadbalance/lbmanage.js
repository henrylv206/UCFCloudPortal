var lbmanage = {		
		strCloud: "",
		root:"/UCFCloudPortal",
		algorithmText : "轮转",
		lbPolicyText : "轮转",
		productState:{
	    	"1":"申请",
	    	"2":"就绪",
	    	"3":"申请处理中",
	    	"4":"已删除",
	    	"5":"已关机",
	    	"6":"操作执行中",
	    	"7":"创建失败"
	    },
	    lbProtocolText:{
	    	'tcp':'tcp',
	    	'http':'http'
	    },
	    algorithmState:{
	    	"0":"轮转",
	    	"1":"加权轮转",
	    	"2":"最小连接",
	    	"3":"加权最小连接",
	    	"4":"随机",
	    	"5":"加权随机",
	    	"6":"源地址散列",
	    	"7":"源地址端口散列",
	    	"8":"目的地址散列"
	    },
		init : function(resourcePoolsId, zoneId){
			lbmanage.infoId = '';
			lbmanage.serverList = {};
			lbmanage.bindVMId = {},//保存已被指派虚机
			lbmanage.bindCount = 0;//已被指派虚机的计数器
			lbmanage.loadbalance = {};
			lbmanage.accessArray = new Array(),//保存已被指派主机
			lbmanage.PublicPrivateSwitch();
			
////			测试数据
//			lbmanage.vmList1 = [
//				{"id":674,"nicsBOs":{"ip":"10.1.130.54"},"instance_name":"vm2001"},
//				{"id":675,"nicsBOs":{"ip":"10.1.110.55"},"instance_name":"vm2002"}
//			];
//			lbmanage.vmList2 = [
//			    				{"id":676,"nicsBOs":{"ip":"10.1.130.54"},"instance_name":"vm2003"},
//			    				{"id":677,"nicsBOs":{"ip":"10.1.110.55"},"instance_name":"vm2004"}
//			];
			lbmanage.vmList = {};
			lbmanage.getLoadBalaProList(resourcePoolsId,zoneId);			
			$("#algo_btn").click(function(){
				lbmanage.saveLoadBala();
			});						
			$("#vm_btn").click(function(){
				lbmanage.addLoadBalaAboutVM();
			});
			$(".cancelLoadBala").click(function(){
				$(".algorithminput").hide();
				$(".algorithm").html(algorithmText).show();
				$(".lb_edit li:first").hide();
				$(".lb_edit .delete").hide();
				
			});
			lbmanage.validate();
			//产品续订相关功能
			$("#pop_erneuern #InputYes").click(function(){
				if(confirm("确定要续订该产品吗？")){
					lbmanage.updateInstancePeriod();
				}
				
			});
			$("#pop_erneuern #InputNo").click(function(){
				 $("#pop_erneuern").hide();
				 $("#pop_erneuern .tilength").val(1);
			});
		},		
		getLoadBalaProList : function(resourcePoolsId,zoneId){			
			$("#lbmanage").html("<img src='../images/loading2.gif'>");
			$.ajax({
				type : "GET",
				data : {
					resourcePoolsId : 0,
					zoneId : 0
				},
				url : lbmanage.root+"/portal_lb/getLoadBalanceInstance.action",
				timeout:5000,
				async:false,
				success : function(data) {
						var dom='';
						if(data == null || data.virtualServiceName == null){
							dom='<tr><td class="number" colspan="10"><span class="item">没有找到记录！</span></td></tr>';							
						}else{
							lbmanage.loadbalance[0]=data;
							
							lbmanage.queryLoadBalaAboutVM();
							//lbmanage.queryLoadBalaForElaster();							
							lbmanage.lbPolicyText = lbmanage.lbPolicyState[data.lbPolicy];//lbmanage.algorithmState[data.algorithm];							
							var createDt = splitDateFormat(data.createDt);
			    			var updateDt = splitDateFormat(data.updateDt);
							var port = data.publicPort;
							if(data.publicPort == 0){
								port = '0任意端口';
							}
							$("#instanceId").val(data.instanceId);
							dom +='';
							dom += '<tr><td class="number"><span class="item">'+1+'</span></td>'
								+ '<td><span class="item">'+data.loadbalanceName+'</span></td>'
								+ '<td><span class="item">'+port+'</span></td>'
								+ '<td><span class="item" id="alog_span">'+lbmanage.lbPolicyText+'</span></td>'
								+ '<td><span class="item">负载均衡</span></td>'
								+ '<td><span class="item">'+data.serverGroupName+'</span></td>'								
								+'<td class="line6"><span class="time">'+createDt[0]+'</span><span class="time">'+createDt[1]+'</span></td>'
			    	            +'<td class="line6"><span class="time">'+updateDt[0]+'</span><span class="time">'+updateDt[1]+'</span></td>'		    	       
			    	            +'<td><span class="item">'+stateName[data.state]+'</span></td>'		    	            
			    	            +'<td class="shop last"><span class="item">';
			    			if(3==data.state||6==data.state){			    				
			    				dom += '<span><img src="../images/loading1.gif"></span>';
			    			}
			    			else if(2==data.state){
			    				//'<a href="#"><img  title="修改算法" src="../../images/icons/icon-right.png" onclick="javascript:lbmanage.showModify();"/></a>'
			    				dom += '<a href="#"><img title="指派主机" src="../images/icons/icon-del.png" onclick="javascript:lbmanage.modifyLoadBala();"/></a>';
			    			}
			    			//执行中状态，屏蔽操作
			    			else if(data.state==6){
								$("#td_update_"+data.instanceId).hide();
								$("#td_del_"+data.instanceId).hide();
								$("#lbmanage #td_extend_"+data.instanceId).hide();
							}
			    			dom += '</span></td></tr>'; 
			    			$("#serviceGroupName").val(data.serverGroupName);
							$("#algorithm_hid").val(data.algorithm);
						}						
						 $(".lb_manage #lb_tbody").html(dom);						 
						
					}
			});
		},		
		//查询可供指派的虚机
		queryLoadBalaForElaster : function(info){
			$.ajax({
				type : "GET",
				data : {},
				url : lbmanage.root+"/portal_lb/queryVirtualMachine4Elaster.action",
				async:false,
				success : function(data) {
					if(data != null){
//						data = lbmanage.vmList1;//测试数据
						var dom='';
						var list = data;
    						for(var i=0;i<data.length;i++){
    							lbmanage.vmList[data[i].id]= data[i];//将数据保存到页面备用
    							//过滤已经指派的虚拟机 fix bug 2726 2729
    							if(lbmanage.bindCount>0&&undefined!=lbmanage.bindVMId[data[i].id]){									
    							}
    							else dom+='<option id='+data[i].id+'>'+data[i].instance_name+'</option>';
    						}
    						$(".vm .vmList").html("").append(dom);
    						if(dom==''){
						      confirm("当前没有可用主机！");
					      }else{					      	
                            $("#add_confirm").show();
					      }
					}
				}
			});
		},
		//查询已经指派的虚机
//		queryLoadBalaAboutVM  : function(){
//			var serviceGName = lbmanage.loadbalance.serverGroupName;
//			$.ajax({
//				type : "POST",
//				url : lbmanage.root+"/portal_lb/queryServiceList.action",
//				data:{"serviceGroupName":serviceGName},
//				async:false,
//				timeout:5000,
//				success : function(data) {
//					if(null!=data&&data.length>0){
////						data = lbmanage.vmList2;测试数据						
//						for(var i=0;i<data.length;i++){
//							lbmanage.vmList[data[i].id]= data[i];//将数据保存到页面备用
//							lbmanage.bindVMId[data[i].id] = data[i];//已经指定的VM
//							lbmanage.bindCount++;
//							var dom ='<tr name='+data[i].id+'><td><span class="item">'+data[i].service.rsname+'</span></td>'
//							+'<td><span class="item">'+data[i].service.weight+'</span></td>'
//							+'<td><span class="item">'+data[i].service.conlim+'</span></td>'
//							+'<td class="shop last"><span class="item">'
//							+'<a href="#"><img src="../../images/icons/icon-del.png" title="移除" onclick="lbmanage.reduceLoadBalaAboutVM('+data[i].id+');"></a>'
//							+'</span></td></tr>';
//							$(".lb_manage #vm_tbody").append(dom);
//							$(".vm .vmList option[value='"+data[i].id+"']").remove();
//						}
//						$("#designateVM").show();
//						$(".lb_edit li:first").hide();
//						$(".lb_edit .delete").hide();
//						
//					}
//				}
//			});
//		},
		
		queryLoadBalaAboutVM  : function(){
			var serviceGName = lbmanage.loadbalance[0].serverGroupName;
			var s_protocolType = lbmanage.loadbalance[0].lbProtocol;
			var dom = '';
			$.ajax({
				type : "POST",
				url : lbmanage.root+"/portal_lb/queryServiceList.action",
				data:{
					"serviceGroupName":serviceGName,
					"s_protocolType":s_protocolType
				},
				async:false,
				timeout:50000000,
				success : function(data) {
					$('#relative_thead').show();//bug 3872 0003894
					var thead='<tr> <th>主机名称</th><th>IP地址</th><th>端口</th><th>操作</th>';					
					$('#relative_thead').html(thead);
					
					if(null!=data&&data.length>0){
						dom += '';
                        //data = lbmanage.vmList2;测试数据						
						for(var i=0;i<data.length;i++){
							lbmanage.accessArray = [];
							lbmanage.vmList[data[i].vmId]= data[i];//将数据保存到页面备用
							lbmanage.bindVMId[data[i].vmId] = data[i];//已经指定的VM
							lbmanage.accessArray.push(data[i]);//已经指定的VM
							lbmanage.bindCount++;
							var tmp ='<tr id="vm_'+data[i].vmId+'"><td><span class="item" id="vnName_'+data[i].vmId+'">'+data[i].rsvo.rsName+'</span></td>'
                            +'<td><span class="item">'+data[i].rsvo.rsIp+'</span><span style="display:none" id="s_protocolType_'+data[i].vmId+'">'+data[i].rsvo.protocol+'</span></td>'
							+'<td><span class="item">'+data[i].rsvo.rsPort+'</span></td>'
							+'<td class=\"shop last\">'
							+'<span class="item"><input style="display:block; background:url(../images/icons/icon-del.png) no-repeat;height:20px; width:20px;border: none;cursor: pointer; margin-left:40px;margin-top:7px; "  alt="移除" onclick=\"lbmanage.reduceLoadBalaAboutVM('+data[i].vmId+');\"/></span>'  //修改BUG3872
							//+'<img style="display:block" src="../images/icons/icon-del.png" alt="移除" onclick="lbmanage.reduceLoadBalaAboutVM('+data[i].vmId+');"/>'
							+'</td></tr>';	
							if((i+1)%2==0){
								tmp=tmp.replace('<tr id=','<tr class=bj id=');
							}
							dom+=tmp;
							$(".vm .vmList option[value='"+data[i].vmId+"']").remove();
						}						
					}
					else {
						dom = '<td colspan=4>无指派主机</td>';
					}
					console.log(dom);
					$("#relative_table").empty();
					$("#relative_table").html(dom);
				}
			});
		},
		showModify : function(){
			var opt = $("#algorithm_hid").val();
			$(".lb_manage .algorithm").show();
			var dom = '<option value="0">轮转</option>'
				+'<option value="1">加权轮转</option>'
				+'<option value="2">最小连接</option>'
				+'<option value="3">加权最小连接</option>'
				+'<option value="4">随机</option>'
				+'<option value="5">加权随机</option>'
				+'<option value="6">源地址散列</option>'
				+'<option value="7">源地址端口散列</option>'
				+'<option value="8">目的地址散列</option>' ;
			$(".lb_manage #algo_select").html(dom);
			$(".lb_manage #algo_select option[value='"+opt+"']").attr("selected", true);
			//$("#algo_btn").attr("onclick","lbmanage.saveLoadBala();");
		},
		addLoadBalaAboutVM : function(){
            $('#caozuo_'+lbmanage.infoId).html('<span><img src="../images/loading1.gif"></span>');//bug 0003771
			var $selectedVm = $(".vmList option:selected");
			var $oldVM = $selectedVm.clone();					
			var lbPort = $.trim($("#lbPort").val());
			if(!isDigit(lbPort)){
				alert("请输入正确的数字！");
				$("#lbPort").focus();
				$("#lbPort").val("");
				return
			}
//			var dom = '<li name='+$selectedVm.attr("id")+'><span>'+$oldVM.text()+'</span><span class="delete" onclick=lbmanage.reduceLoadBalaAboutVM(\"'+$selectedVm.attr("id")+'\"); style="margin-left:5px;color: #ff6600;cursor: pointer;">移除</span></li>';
			var dom ='<tr id="vm_'+$selectedVm.attr("id")+'">'
					+'<td><span class="item">'+$oldVM.text()+'</span></td>'
					+'<td><span class="item">'+lbPort+'</span></td>'
					+'<td class=\"shop last\">'
					+'<img src="../images/icons/icon-del.png" alt="移除" onclick=lbmanage.reduceLoadBalaAboutVM(\"'+$selectedVm.attr("id")+'\");/>'
					+'</td></tr>';
			
			var vmId = $selectedVm.attr("id");
            $("#add_confirm").hide();
			$.ajax({
				type : "POST",
				url : lbmanage.root+"/portal_lb/addVirtualMachineOfLoadBalanceInstance.action",
				data:{ 
					   "s_servname":lbmanage.vmList[vmId].instance_name,
					   "s_ipaddr":$selectedVm.val(),
					   "s_port":$('#lbPort').val(),
					   "s_conn_limit":1000
					 },
				timeout:500000,
				success : function(data) {
					if(data=="xmlrpc command successful"){
						//fix bug 2403
						$(".vm .vmList option[id="+vmId+"]").remove();
						alert("保存成功");
						//$("#relative_table").append(dom);
						query();
					}
//					$selectedVm.remove();
//					if($selectedVm.length != 0){
//						
//					}
				}
			});
		},
		reduceLoadBalaAboutVM : function(id){	
			var $selectedVm = $("#vm_tbody,#vm_"+id+"");
			var name = $("#vm_tbody tr[name="+id+"] span:first").text();
			var dom = '<option id='+$selectedVm.attr('name')+'>'+name+'</option>';
			var vmId = id;
			var s_protocolType = $("#s_protocolType_"+id).text();
			
			
			if(confirm("您确定从负载均衡中移除该主机？")){
				$.ajax({
					type : "POST",
					url : lbmanage.root+"/portal_lb/deleteVirtualMachineOfLoadBalanceInstance.action",
					data:{ "s_servname":$('#vnName_'+vmId).text(),
						   "s_protocolType":s_protocolType},
					timeout:500000,
					success : function(data) {
						if(data=="xmlrpc command successful"){
							alert("移除成功");
							lbmanage.bindCount--;
							$(".vm .vmList").append(dom);
							$("#vm_"+id).remove();
							if(lbmanage.bindCount==0){
                                $("#relative_table").empty();//bug 3872
                                $("#relative_table").html('<td colspan=4>无指派主机</td>');//bug 0003904
							}
						}
					}
				});				
			}
		},
		modifyLoadBala : function(){
			$(".lb_manage #vm_weight").val("");	
			$(".lb_manage #vm_conn").val("");	
			$(".lb_manage .vm").show();	
		},
		lbCancel:function(id){
			
			if(lbmanage.bindCount>0){
				alert("请先解除指定的主机后再删除！");
				return;
			}
			var bind = false;
			var ruleCount = 0;
			var vsCount = 0;
			$.ajax({
		    	  type : "POST",
			      url : "../vdc_fwmanage/checkFWOrLB.action",
			      datatype : "json", //设置获取的数据类型为json
			      data : {},
			      async : false,
			      global : false,
			      success : function(data) {			    	  
			    	  if(data!=null){
			    		  var array = data.list;
			    		  if(array != null && array.length > 0) {			    			  
			    			  for(var i = 0;i<array.length;i++){
			    				  //购买过防火墙
			    				  if(array[i].templateType==7){
			    					  bind = true; 
			    					  ruleCount = array[i].cpuNum;
			    					  break;
			    				  }	
			    				  else if(array[i].templateType==6){
			    					  vsCount = array[i].cpuNum;
			    				  }
			    			  }			    			  
			    		  }	
			    		  //fix bug 2417
			    		  if(vsCount>0){
			    			  alert("您申请的负载均衡已经绑定公网IP，请先解绑后，再退订！");
			    		  }
			    		  else if(bind){
				    		  if(confirm("您已经申请过防火墙，是否一起退订？")){
				    			  if(ruleCount>0){
					    			  alert("您申请的防火墙已存在规则，请先删除全部规则后，再退订！");
					    		  }
				    			  else {
				    				  $("#bindId").val(array[i].id);
		    						  lbmanage.showDiv(id);  
				    			  }
	    					  }	
				    	  }
				    	  else lbmanage.showDiv(id);
			    	  } 		    	  
			      }	
			 });
		},
		
		 cancel : function() {
			    $("#fw_reason").val("");
			    $("#del_confirm").hide();
                query();
			  },
		  showDiv : function(id) {
		    $("#del_confirm .popwindow_t").html("终止服务");
		    var tdObj = $("#td_del_"+id);
		    $("#del_confirm").css("top", tdObj[0].offsetTop).css("left", tdObj[0].offsetLeft - 100).css("display", "block");
		  },
		  confirm : function() {
		    if(commonUtils.len($("#fw_reason").val()) == 0 || commonUtils.len($("#fw_reason").val()) > 20 ) {
		      alert("申请理由限定1~20个字符（一个汉字算2个字符）");
		      $("#fw_reason").focus();
		      return;
		    }
		    var data = {};
		    data.reason = $("#fw_reason").val();
		    data.instance_id = $("#instanceId").val();
		    //firewall
		    data.bindId = $("#bindId").val();
		    $("#del_confirm").hide();
		    $.ajax({
		      type : "POST",
		      url : "../../portal_lb/applyDestroy.action",
		      datatype : "json", //设置获取的数据类型为json
		      data : data,
		      async : false,
		      global : false,
		      success : function(data) {
		        alert(data);
		        lbmanage.getLoadBalaProList();
                //bug 0003230
                query();
		      }
		    });
		    lbmanage.cancel();
		  },	
//		不使用  update by CQ 20120516
//		destroyLoadBala : function(serviceGroupName,instance_id){			
//			$.ajax({
//				type : "POST",
//				url : "../../portal_lb/destroyLoadBalanceInstance.action",
//				data:{"serviceGroupName":serviceGroupName,"instance_id":instance_id},
//				timeout:5000,
//				success : function(data) {
//					alert(data);
//				}
//			});
//		},
		saveLoadBala : function(){
			var serviceGroupName =$("#serviceGroupName").val();
			var sg_scheduler = $("#algo_select option:selected").attr("value");
			$.ajax({
				type : "POST",
				url : lbmanage.root+"/portal_lb/updateLoadBalanceInstance.action",
				data:{"sg_groupname":serviceGroupName,"sg_scheduler":sg_scheduler},
				timeout:5000,
				success : function(data) {
					if("OK"==data){
						alert("保存成功");
						$("#ref_"+lbmanage.loadbalance[0].id).empty().html($("#algo_select option:selected").text());
						$("#fwInstanceId").val($("#algo_select option:selected").attr("value"));
					}
				}
			});
			
			$(".algorithm").fadeOut("fast");
			
		},
		// ------------------------------------------产品续订-------------------------------------------
		showProductXDDiv : function(type,id){
			  //fix bug 2398
			  var tdObj = $("#td_extend_"+ id);
			    $("#pop_erneuern").css("top", tdObj[0].offsetTop+40).css("left", tdObj[0].offsetLeft - 80).css("display", "block");
			    lbmanage.getInstancePeriodInfo(id);
	    },
	    
	    PublicPrivateSwitch : function() {			 
	        $.getScript(lbmanage.root+"/js/privateSkySwitch.js", function(e){
	        	lbmanage.privateSkySwitch = privateSkySwitch;
	        	lbmanage.strCloud = lbmanage.privateSkySwitch.getCloudInfo();
	        });
	  	},

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
			        	if(lbmanage.strCloud == "1"){    		        		
			        		$(".popwindow .cycle").html(data.price+"元/"+data.unitString);
			        	} 
			    		if(lbmanage.strCloud == "2"){    		    			
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
		 },
 selectBind: function(info,oper){	 	
	//$("#nasinstanceid").val(nasInstanceId);
	//$("#resourcepoolsid").val(0);
 		var ul = "/resourcesUsed/nasResourceOperate!showUserVm.action";
	    $(".popupDiv1").show();
	    if(oper==1){
	    $('#popTitle').html("挂载虚拟机");
	    }else if(oper==2){
	    	ul =  "/resourcesUsed/nasResourceOperate!showUserMc.action";
    	 $('#popTitle').html("挂载小型机");
	    }else if(oper==3){
	    	ul =  "/resourcesUsed/nasResourceOperate!showUserPM.action";
    	 $('#popTitle').html("挂载物理机");
	    }		
		$.ajax({
			url : lbmanage.root+ul,
			type : 'POST',
			data : {},
			async : false,
			dataType : 'json',
			success : function(data) {
				if(data.search("error")!=-1){
					alert(data);
					query();
				}else{
					var instancelist = $.evalJSON(data);
					if(instancelist.length > 0){
						$(".vm .vmList").html("");
						var str_snap = "<option id=\"\">=请选择=</option>";
						$(".vm .vmList").append(str_snap);
						for (var i = instancelist.length - 1; i >= 0; i--) {
							if(instancelist[i].nicsBOs!=null){
								for(var j = 0;j < instancelist[i].nicsBOs.length;j++){
									if($.inArray(instancelist[i].nicsBOs[j].ip, accessArray) == -1){
										str_snap = "<option id=\""
										+ instancelist[i].id + "\" value=\""
										+ instancelist[i].nicsBOs[j].ip + "\">"
										+ instancelist[i].instance_name +":"+ instancelist[i].nicsBOs[j].ip +"</option>";
										$(".vm .vmList").append(str_snap);										
										lbmanage.vmList[instancelist[i].id]= instancelist[i];//将数据保存到页面备用
										//console.log(lbmanage.vmList);
									}
								}
							}		
						}
						$("#add_confirm").show();
					} else {
						alert('没有可用的主机！');
						//$("#add_confirm").hide();	
					}
				}
			}
		});
     }
};
function loadBalancedState(info){
	$.lbmanage || ($.lbmanage = lbmanage);
	lbmanage.root="..";
	lbmanage.init(info.resourcePoolsId, info.zoneId);
	lbmanage.infoId=info.id;
	
	//存放算法
	if(null!=lbmanage.loadbalance[0]){
		$("#fwInstanceId").val(lbmanage.loadbalance[0].algorithm);
		setTimeout(function(){
			$("#ref_"+lbmanage.loadbalance[0].id).html(lbmanage.algorithmText);
			$("#serviceGroupName").val(lbmanage.loadbalance[0].serverGroupName);
			$("#lbProtocol").val(lbmanage.loadbalance[0].lbProtocol);
			$("#lbPort").val(lbmanage.loadbalance[0].lbPort);
			$("#lbPolicy").val(lbmanage.loadbalance[0].lbPolicy);
		},100);
	}
	else $("#fwInstanceId").val("");
	
	return stateName[info.state];
};

function loadBalancedOperate(data){
	var dom ='<input type="hidden" name="serviceGroupName">';
	if(3==data.state||6==data.state){
		dom += '<span><img src="../images/loading1.gif"></span>';
	}
	else if(2==data.state){
		//'<a href="#"><img  title="修改算法" src="../images/modify.png" onclick="javascript:loadPopDiv_algorithm();"/></a>'
		dom +=   '<a href="#"><img title="指派主机" src="../images/bind.png" onclick="javascript:loadPopDiv_lbupdate();"/></a>';
	}
	return dom;
}
function loadPopDiv_algorithm(){	
	$("#add_confirm").empty();
	$("#add_confirm").load("../component/loadbalance/algorithm.html","",function(){		
		var opt = $("#fwInstanceId").val();
		$(".lb_manage #algo_select option[value='"+opt+"']").attr("selected", true);
		$("#close").click(function(){
			$("#add_confirm").empty();
			$(".popupDiv1,.shade").fadeOut("fast");
            query();
			});
		$.BeautifySelect(document.getElementById("algo_select"),{
			//"langue":"EN" 		（如果option内部显示文字是英文，添加 langue 属性）  				
					});	
	});	
	
	$("#add_confirm").show();
	
}
function loadPopDiv_lbupdate(info){
	$("#add_confirm").empty();
	$("#add_confirm").load("../component/loadbalance/lbupdate.html","",function(){		
		$("#close").click(function(){
			$("#add_confirm").empty();
			$("#add_confirm").hide();
			query();//bug 0003964
			});
        $('#serviceGroupName').val();
        lbmanage.selectBind(info,1);    
       	$("#machineTypeS").change(function(){
       		var  selectType = $("#machineTypeS option:selected").attr("id");
 			if(selectType==1){
 				lbmanage.selectBind(info,selectType);  
 			}else if(selectType==2){
 				lbmanage.selectBind(info,selectType); 
 			}else if(selectType==3){
 				lbmanage.selectBind(info,selectType); 
 			}
 		}); 
//        if(1==type){
//        	lbmanage.queryLoadBalaForElaster(info);		
//        }
//        else if(3==type){
//        	lbmanage.queryLoadBalaForElaster(info);		
//        }
//        else if(10==type){
//        	lbmanage.queryLoadBalaForElaster(info);		
//        }
//		$.BeautifySelect(document.getElementById("uvmList"),{
//			//"langue":"EN" 		（如果option内部显示文字是英文，添加 langue 属性）  				
//					});	
	});	
}
//fix bug 6892	
function getLBPolicyList(){
	var lbPolicyList = new Array();
	$.ajax({
		type : "GET",
		data : {},
		url : lbmanage.root+"/portal_lb/getPolicyList.action",
		timeout:5000,
		async : false,
		success : function(data) {
			if(data!=null){
				lbPolicyList =  data;				
			}
		}
	})
	return lbPolicyList;
}	
//fix bug 5231 6892
function lbConfig(info){
	var lbPolicyList = getLBPolicyList();
	var config = info.storageSize+'个并发';
	config += "；协议："+info.lbProtocol;
	config += "；端口："+info.lbPort;
	var array = JSON.parse(lbPolicyList);
	$.each(array ,function(i){
		if(info.lbPolicy==array[i].value){
			info.lbPolicy = array[i].text;
			return false;
		}
	});
	config += "；策略 ："+info.lbPolicy;
	return config;
}
