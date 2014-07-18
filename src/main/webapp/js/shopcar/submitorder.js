var submitorder = {
	strCloud: "",
	root:"/UCFCloudPortal",

	//购物车初始化
	init : function() {
	
		$.ajaxSetup({cache:false}); 
		
		var cloudInfo = 0;
		$.ajax({
			url: submitorder.root+"/sysParameters/getCloudInfo.action",
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				cloudInfo = data;
			}
		});

		main.carstate = true;
		main.totalcharge = new Array();

		var p = main.porder;
		var d = true;
		var total = '0';
		if (p.vminfos.length > 0) {
			var total = 0;
			
			for ( var i = 0; i < p.vminfos.length; i++) {				 
				var dom_final = this.domFactory(p.vminfos[i],cloudInfo);	
 
				$("#submitorderHtml").append(dom_final);	
				//to fix bug [3321]
				total += Number(p.vminfos[i].charge * p.vminfos[i].period);
 
				d = false;
			}		
			total = total.toFixed(2);
		 
			$("#submitorderMoney").html('￥ ' + total + '元');

		}
		
		if (d) {
			dom = '<tr><td colspan="5">(未选购商品)</td></tr>';
			$(" .shopcart .trytab tbody").append(dom);
		}

 
		$("#orderSubmit .submit").click(function() {
			$(".w05 button").attr("disabled", true);
			if (main.porder.vminfos.length > 0) {				
				//提交前检查产品的唯一性 fix bug 3751
				var flag = true;
				for(var i=0;i<main.porder.vminfos.length;i++){
					var info = main.porder.vminfos[i];
					
					 
					
					//负载均衡
					if(6==info.templateType){
						flag = submitorder.checkExistPort(info);						
					}
					//防火墙
					if(7==info.templateType){
						flag = submitorder.checkFW();						
					}
					//云监控  fix bug 2798 3791
					else if(5==info.templateType){
						flag = submitorder.checkMonitor();	
					}															
					//多虚机
					else if(50==info.templateType){
						//flag = submitorder.checkMultiVM(info);
					}
					//纯虚拟机
					//to fix bug [3934]
					else if(1==info.templateType){
						//flag = submitorder.checkMultiVM(info);
					}	
					//带宽
					else if(8==info.templateType){						
						 flag = bw.checkBW();
					}
					if(!flag){
						return;
					}
				}
				var isExitVm = false;
				var isExitVpc = false;
				var isBindVm = false;
				var isBindVpc = false;
				var vmZone = new Array();
				var message="ok";
				submitorder.saveProOrder(message);
			} else {
				$(".w05 .cancel").removeAttr("disabled");
				alert("购物车内没有产品，请选购！");
			}
        });

		$(".btnd").mouseover(function() {
			$(this).removeClass().addClass("btn_del2");
		}).mouseout(function() {
			$(this).removeClass().addClass("btn_del");
		});
		$(".btne").mouseover(function() {
			$(this).removeClass().addClass("btn_edit2");
		}).mouseout(function() {
			$(this).removeClass().addClass("btn_edit");
		});
		//shopcart.PublicPrivateSwitch();
		if(cloudInfo == '2'){
		    $(".shopcart .trytab #shopcartTatolMoney").html('');
		    $(".shopcart .trytab #shopcartTatol").html('');	
		    $(".shopcart .trytab #shopcartTDPrice").html('');	
		    
			$("#submitOrderTop").hide();
			$(".mysearch").hide();
			$(".nav").hide();
			$(".footer").hide();
			$(".active01").hide();
			$(".active02").hide();
			$("#serviceMenu").show();
			$("#submitorder1").hide();
			$("#submitorder2").hide();
			$("#submitPay").hide();
	    }
	},
	
	//初始化时，创建购物车DOM页面
	domFactory : function(vminfo,cloudInfo) {
		var id = vminfo.productId;
		var dom = '';
		 
		var str = vminfo.period;
		var zone;
		var template;
		var tempname;		
		if (vminfo.instanceName.length > 12) {
			tempname = vminfo.instanceName.substring(0, 12) + '...';
		} else {
			tempname = vminfo.instanceName;
		}
 
		//to fix bug [3959]
		var resourceNum = 0;
		if (vminfo.templateNum != undefined){
			for (var i = 0; i < vminfo.templateNum.length; i++) {
				resourceNum += vminfo.templateNum[i];
			}
		} else {
			resourceNum = 1;
		}		
		
		var serviceType = ''; 
		period = submitorder.getPeriod(vminfo.period);
		if (vminfo.templateType == 1) {
			serviceType = '虚拟机';
		}
		if (vminfo.templateType == 2) {
			serviceType = '虚拟硬盘';
		}
		if (vminfo.templateType == 4) {
			serviceType = '虚拟机备份';
		}
		else if (vminfo.templateType == 9) {
			serviceType = '弹性公网IP';
		}
		//to fix bug [3224]
		else if (vminfo.templateType == 5) {
			serviceType = '云监控';
		}
		else if (vminfo.templateType == 6) {
			serviceType = '负载均衡';
		}
		else if (vminfo.templateType == 7) {
			serviceType = '防火墙';
		}
		else if (vminfo.templateType == 50) {
			serviceType = '多实例';
		}
		else if (vminfo.templateType == 3) {
			serviceType = '小型机';
		}
		else if (vminfo.templateType == 10) {
			serviceType = '物理机';
		}
		else if (vminfo.templateType == 12) {
			serviceType = "弹性块存储";
		}
		else if (vminfo.templateType == 11) {
			serviceType = "对象存储";
		}
		else if (vminfo.templateType == 8) {
			serviceType = "公网带宽";
		}else if (vminfo.templateType == 13) {
			serviceType = "NAS文件系统";
		}
		if(cloudInfo == "1"){
			//to fix bug [7730]
			if(vminfo.unit == 'Y') {                    	    			
				unit = '年';
	        }
			if(vminfo.unit == 'M') {                    	    			
				unit = '月';
	        }
			if(vminfo.unit == 'W') {                    	    			
				unit = '周';
	        }
			if(vminfo.unit == 'D') {                    	    			
				unit = '天';
	        }
			if(vminfo.unit == 'H') {                    	    			
				unit = '小时';
			}				
			
			dom = '<tr>'    
		         +'	 <td title="'+ vminfo.instanceName +'">'+tempname+'</td>'
		         +'	 <td>'+serviceType+'</td>'
		         +'	 <td>'+resourceNum+'</td>'    				     				
		         +'	 <td>'+(vminfo.charge)+'元</td>'    		
		         +'	 <td>'+vminfo.period+unit+'</td>'    		    		
		         +'	 <td class="last">￥'+(vminfo.period * vminfo.charge).toFixed(2) + '元</td>'    		
		         +'</tr>';
		}
		
		var unit = "";
		if(cloudInfo == "2"){
			//to fix bug [7462]
			if(vminfo.unit == 'Y') {                    	    			
				unit = '年';
	        }
			if(vminfo.unit == 'M') {                    	    			
				unit = '月';
	        }
			if(vminfo.unit == 'W') {                    	    			
				unit = '周';
	        }
			if(vminfo.unit == 'D') {                    	    			
				unit = '天';
	        }
			if(vminfo.unit == 'H') {                    	    			
				unit = '小时';
			}			
			
			dom = '<tr>'    
		         +'	 <td title="'+ vminfo.instanceName +'">'+tempname+'</td>'
		         +'	 <td>'+serviceType+'</td>'
		         +'	 <td>'+resourceNum+'</td>'    				     				 		
		         +'	 <td>'+vminfo.period+unit+'</td>'    		    		
		         +'</tr>';
		}
		return dom;
	},
	
	getPeriod :function(period){
		var ret = period;
		if(ret.length>1){
			ret = ret.slice(0,1);
		}
		return ret;
	},	
	
	PublicPrivateSwitch : function() {			 
		var cloudInfo = 0;
		$.ajax({
			url: "/UCFCloudPortal/sysParameters/getCloudInfo.action",
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				cloudInfo = data;
			}
		});
		if(cloudInfo == '2'){
		    $(".shopcart .trytab #shopcartTatolMoney").html('');
		    $(".shopcart .trytab #shopcartTatol").html('');			  
	    }
    },	
    
    getProductBuyCountById:function (id){
		var count = 0;
		$.ajax({
			url : "/UCFCloudPortal/product/getProductBuyCountById.action?id="+id,
			data : null,
			async : false,
			cache : false,
			dataType : "json",
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
		return count;
	}, 
	
	showProductByid:function (pid){
		var product = '';
	    $.ajax({
	        type : "POST",
	        url : "../vdc_vmmanage/showProductByid.action",
	        datatype : "json", 
	        data : {id : pid},
	        async : false,
	        global : false,
	        cache:false,
	        success : function(data) {
	        	if(null!=data){
	        		product = data[0];
	        	}	        	
	        }
	    });
		return product;
	}, 
	
	checkProductRename : function (name, type){
		var count = 0;		
		var data = {
			name: name,
			type: type        
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
		return count;
	},	
	
	//提交购物车
	saveProOrder : function(message) {
		//to fix bug [3326]
		var pid;	
		var VMRename = 0;
		var shopcartProduct = 0;
		for ( var i = 0; i < main.porder.vminfos.length; i++) {
			pid = main.porder.vminfos[i].productId;	    	
			var product = submitorder.showProductByid(pid);
			var quotaNum = 0;
			if(null!=product){
				quotaNum = product.quotaNum;
				//检查服务是否已经下线——中通服
				if(product.state == 6){
					alert("服务 " + main.porder.vminfos[i].instanceName + " 已经下线！");
		    		return;
				}				
			}
	    	var buyCount = submitorder.getProductBuyCountById(pid);	    	
	    	//to fix bug [3326]
	    	
	    	shopcartProduct = 0;
			for ( var m = 0; m < main.porder.vminfos.length; m++) {
				if(main.porder.vminfos[m].productId == pid){
					shopcartProduct ++;
				}			
			}
	    	
	    	if(shopcartProduct > (quotaNum - buyCount)){
	    		alert("服务 " + main.porder.vminfos[i].instanceName + " 申请已达到最大限额，请稍后再试！");
	    		return;
	    	}	
	    	//to fix bug [3602]
	    	VMRename = submitorder.checkProductRename(main.porder.vminfos[i].instanceName, main.porder.vminfos[i].templateType);
			if(VMRename == "1"){
				alert("服务名称 " + main.porder.vminfos[i].instanceName + "已经被使用，请重新输入。");
				$("#vmName").focus();
				return;
			}
		}
		if(submitorder.checkPublicIPAddressExists()){
			$(".w05 button").attr("disabled", false);
			return;
		}
		if (main.carstate) {
			main.carstate = false;
			$.ajax({
				url:"/UCFCloudPortal/order/portal_saveNewOrder.action",
				type : "POST",
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				dataType : "json",
				timeout : 5000,
				async:false,
				data : {
					"templateInfo" : $.toJSON(main.porder.vminfos)
				},
				success : function(json) {
					$(".main1 .shopcart .trytab tbody").html('');
					if ("成功：订单信息成功写入数据库" == json) {
						main.porder = {
							voorders : [],
							vminfos : [],
							inslists : [],
							subnets : []
						};
						main.removeShopCartCookie();
						main.shownum_shopcart();
						main.carstate = true;
						server.load(".main1 .shopcart", [ "order", "succ" ],
								function() {
									$(".succ .message").html(message);
								});
						$(".w05 button").attr("disabled", false);
					} else {
						alert(json);
						$(".w05 button").attr("disabled", false);
					}
				}
			});
			javascript:window.location.href='applyok.jsp';
		}
	},
    
	checkPublicIPAddressExists:function(){
		var ipAddress = "";
		for(var i = 0;i<main.porder.vminfos.length;i++){
			//9公网IP
			if(main.porder.vminfos[i].templateType == 9){				 
				ipAddress = main.porder.vminfos[i].ipAddress;
	            break;
			}
		}
		 var ret_val = false;
		  $.ajax({
			  url : submitorder.root+"/publicIp/showPublicIPAddressExists.action",
				type : 'POST',
				dataType : 'json',
	         	data: {
	         		publicIp: ipAddress
	            },
	            async:false,
			  success : function(data) {
				  if("1"==data){	
					  alert("公网IP地址" + ipAddress + "已经被申请，请删除后选择其他公网IP地址重新申请！");
					  ret_val = true;
				  }				  
			  }
	      });
		  return ret_val;
	},
	
	checkLB:function(){
		var flag = true;
		$.ajax({
			type : "POST",
		      url : submitorder.root+"/vdc_fwmanage/checkFWOrLB.action",
		      datatype : "json", //设置获取的数据类型为json
		      data : {},
		      async : false,
		      global : false,
		      success : function(data) {
		    	  if(data!=null){
		    		  var array = data.list;			    		 
		    		  //fix bug 2329 0003640			    		  
		    		  if(array != null && array.length > 0) {
		    			  $(array).each(function(i) {
		    				  if(6==array[i].templateType){
		    					  alert("抱歉，您已申请负载均衡服务，不可以再申请同类服务");
		    					  flag = false;
		    					  return false;
		    				  }
		    				  else if(7==array[i].templateType){
		    					  alert("抱歉，您已申请防火墙服务，不可以再申请负载均衡服务");	
		    					  flag = false;
		    					  return false;
		    				  }
		    			  });			    			  
		    		  }		    		  
		    	  }
		      }
		});
		return flag;
	},
	checkExistPort:function(info){
		flag = true;
		if(''!=info.ipAddress&&''!=info.port){
			$.ajax({
				url : submitorder.root+"/portal_lb/checkPortByIP.action",
				data : {
						"s_ipaddr":info.ipAddress,
						"s_port":info.port
						},
				async : false,
				dataType : "json",
				success : function(data){
				       if("true"==data){
				    	   flag = false;
				    	   alert(info.instanceName+" 的端口号已经被使用，请重新输入。");
				       }
		        	}	
		        });			
		}
		return flag;
	},
	checkFW:function(){
		 var flag = true;
			$.ajax({
				type : "POST",
			      url : submitorder.root+"/vdc_fwmanage/checkFWOrLB.action",
			      datatype : "json", //设置获取的数据类型为json
			      data : {},
			      async : false,
			      global : false,
			      success : function(data) {
			    	  if(data!=null){
			    		  var array = data.list;
			    		  //fix bug 2329
			    		  if(array != null && array.length > 0) {
			    			  $(array).each(function(i) {  
				    			  if(7==array[i].templateType){
			    					  alert("抱歉，您已申请防火墙服务，不可以再申请同类服务");
			    					//fix bug 2498 0003640
			    					  flag = false;
			    					  return false;
			    				  }
			    			  });
			    		  }			    		 
			    	  }	    	 
			      }
			});
			return  flag;
	},
	
	checkMonitor:function(){
		var num = 0;
		var flag = true;
		$.ajax({
			type : "GET",
			async : false,
			url : submitorder.root + "/portal_mo/checkMonitor.action",
			success : function(data) {
				if (data) {
					num = data;
					if(num>0){
						flag = false;
						alert("抱歉，您已申请云监控，不可以再申请同类服务");
					}
				}
			}
		});
		return flag;
	},
	
	checkMultiVM:function(info){
		multiVM.vethAdaptorNum = info.vethAdaptorNum;
		multiVM.zoneId = info.zoneId;
		multiVM.total = info.clusterId;
		//判断用户是否具备购买的vlan
		var flag = multiVM.checkVlanForTemplate();
		if(!flag){
			alert("该用户分配的VLAN已作废，不能申请 "+info.instanceName);
			return flag;
		}
		else {
			//fix bug 3958
			flag = multiVM.checkUserVlanId(multiVM.vethAdaptorNum, multiVM.total, multiVM.zoneId);
			if(!flag){
				alert(multiVM.setVlanPoorMessage());
				return flag;
			} 	
		}
		return flag;
	}	
	
};
var multiVM ={
		vethAdaptorNum :1,
		zoneId:0,
		total:1,
		checkVlanForTemplate:function(){
			 var flag = true;			
			 //1：PrivateNetwork，2：StorageNetwork
			 if(2==multiVM.vethAdaptorNum){
				flag = multiVM.checkUserVlan2();
			 }
			 else flag = multiVM.checkUserVlan1();
			 return flag;
		 },   
		 checkUserVlan1:function(){
			 var flag1 = false;
			 //是否有PrivateNetwork
			 flag1 = multiVM.getCountUserVlanByType(1,multiVM.zoneId);
			 return flag1;
		 },
		 checkUserVlan2:function(){			 
			 var flag2 = false;
			 var flag1 = false;
			 //是否有StorageNetwork
			 flag2 = multiVM.getCountUserVlanByType(2,multiVM.zoneId);
			 //是否有PrivateNetwork
			 if(flag2){
				 flag1 = multiVM.checkUserVlan1();
			 }
			 return flag1&&flag2;
		 },		
		 getCountUserVlanByType:function(type,zoneId){	
			 var flag = false;
			 $.ajax({
		    	    url : submitorder.root+"/vdc_vmmanage/getCountUserVlanByType.action",
					type : "POST",
					data:{
						vlanType:type,
			 			zoneId:zoneId
					},
					timeout : 5000,
					async:false,
					success : function(json) {
						if(json>0) {
							flag = true;							
						}							
					}
				});
			 return flag;
		 },
		 checkUserVlanId:function(vethAdaptorNum,total,zoneId){
		    	var flag = true;
		    	var data = {
		    		vethAdaptorNum:vethAdaptorNum,
		    		total:total,
		    		zoneId:zoneId
		    	};
		    	$.ajax({
			  	      url: submitorder.root+"/order/checkUserVlanId.action",
			  		  type: 'POST',
			  		  dataType: 'json',
			  		  data:data,
			  		  async: false,
			  		  success: function(state) {
			  			  if(0==state){
			  				  flag = false;
			  			  }
			  	      }
			  		})
			  		return flag;
		    },
    setVlanPoorMessage:function(){
    	var strCloud  = "1";
    	$.getScript(submitorder.root+"/js/privateSkySwitch.js", function(e){
        	strCloud = privateSkySwitch.getCloudInfo();        	
        });
    	if(strCloud == "1"){
    		return "对不起，由于VLAN资源不足，你的虚拟机无法成功申请，请联系客服。";
    	} 
    	if(strCloud == "2"){
    		return "对不起，由于VLAN资源不足，你的虚拟机无法成功申请，请联系管理员。";
    	}
    }	    
};
var bw = {
	root:"/UCFCloudPortal",
	getBWUsedType : function(){
		var type = 0;
		$.ajax({
			url: bw.root+"/sysParameters/getBWUsedType.action",
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				type = data;
			}
		});
		return type;
	},	
	checkVPN:function() {
	  var flag = true;
	//暂时注释,审核时检查Vlan update by CQ fix bug 4975
//	  $.ajax({
//			type : "POST",
//		      url : bw.root+"/portal_lb/checkVPN.action",
//		      datatype : "json", //设置获取的数据类型为json
//		      data : {},
//		      async: false,
//		      success : function(data) {
//		    	  if(data=="true"){
//		    		  
//		    	  }	
//		    	  else if(data=="false"){
//		    		  flag = false;
//		    	  }
//		      }
//		  });
	  	return flag;
	  },
  checkBW:function(){	
	var flag = true; 
	if(bw.checkVPN()){		
		if(2==bw.getBWUsedType()){
			$.ajax({
				type : "POST",
			      url : bw.root+"/resourcesUsed/checkBWInstance.action",
			      datatype : "json", //设置获取的数据类型为json
			      data : {},
			      async: false,
			      success : function(data) {
			    	 if(data>0){
			    		alert("抱歉，您已申请带宽服务，不可以再申请同类服务");
			  			flag = false;
			    	 }
			      }
			  });
		}		
	}
	else {
		alert("带宽服务请先申请VLAN。");
		flag = false;
	}
	return flag;
	
  } ,
	checkPorder:function(type){
		var flag = false;
		//fix bug 3431
		if(null==main.porder){
			return;
		}
		for(var i = 0;i<main.porder.vminfos.length;i++){
			//8带宽
			if(main.porder.vminfos[i].templateType == type){				 
	             flag = true;
	             break;
			}
		}
		return flag;
	}	
}