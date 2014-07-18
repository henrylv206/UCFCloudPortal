var submitorderNow = {
	strCloud: "",
	root:"/UCFCloudPortal",

	//购物车初始化
	init : function() {
		
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

		main.carstate = true;
		main.totalcharge = new Array();

		var p = main.porderNow;
		var d = true;
		var total = '0';
		
		 
		if (p.vminfos.length > 0) {
			var total = 0;
 
			var focusEven = " onmouseover=$(this).addClass(\"over\") onmouseout=$(this).removeClass(\"over\") ";
			var oddEven = '';
			var trEvents = '';
			
			for ( var i = 0; i < p.vminfos.length; i++) {
			 
				oddEven = (i%2) == 0 ? ' class=\"alt\" ' : ' ';
				trEvents = oddEven + focusEven ;
				var dom_final = this.domFactory(p.vminfos[i],cloudInfo,trEvents,cloudInfo);	
 
				$("#submitorderNowHtml").append(dom_final);	
				// to fix bug [3090 ]
				total += Number(p.vminfos[i].charge * p.vminfos[i].period);
 
				d = false;
			}		
			total = total.toFixed(2);
			$("#productnum").html(p.vminfos.length);
			$("#productMoney").html(total+'元');

		} else {
			$("#submitorderNowHtml").html("无产品");	
			$("#productnum").html(0);
			$("#productMoney").html('￥ ' + 0);
		}
		
		if (d) {
			dom = '<tr><td colspan="5">(未申请服务)</td></tr>';
			$(" .submitorderNow .trytab tbody").append(dom);
		}
		
		$(".shopcarcheck").click(function(){
			if(this.checked){
				$("input[name='checkProduct']").each(function(){
					$(this).attr("checked",true);
				}); 
			} else {
				$("input[name='checkProduct']").each(function(){
					$(this).attr("checked",false);
				}); 
			}
		});
		
		$("#orderNowSubmit .submit").click(function() {
			//to fix bug [3319]
			//$(".w05 button").attr("disabled", true);
			if (main.porderNow.vminfos.length > 0) {
				//提交前检查产品的唯一性 fix bug 3751
				var flag = true;
				for(var i=0;i<main.porderNow.vminfos.length;i++){
					var info = main.porderNow.vminfos[i];
					//负载均衡
					if(6==info.templateType){
						flag = submitorderNow.checkExistPort(info);						
					}
					//防火墙
					if(7==info.templateType){
						flag = submitorderNow.checkFW();						
					}
					//云监控 fix bug 2798 3791
					else if(5==info.templateType){
						flag = submitorderNow.checkMonitor();	
					}
					//多虚机
					else if(50==info.templateType){
						//flag = submitorderNow.checkMultiVM(info);
					}
					else if(1==info.templateType){
						//flag = submitorderNow.checkMultiVM(info);
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
				submitorderNow.saveProOrder(message);
			} else {
				//$(".w05 .cancel").removeAttr("disabled");
				alert("购物车内没有产品，请选购！");
			}
        });		
			
		$("#subtractunti").click(function(){
			alert("subtractunti");
		});
		
		$("#addunti").click(function(){
			alert("addunti");
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
		//submitorderNow.PublicPrivateSwitch();

		//to fix bug [7432]
		//to fix bug [7415]
		if(cloudInfo == '1'){
			$("#submitOrderNowPrivate").hide();
			$("#submitOrderNowPublic").show();
		}
		
		if(cloudInfo == '2'){
		    $(".submitorderNow .trytab #shopcartTatolMoney").html('');
		    $(".submitorderNow .trytab #shopcartTatol").html('');	
		    $(".submitorderNow .trytab #shopcartTDPrice").html('');	
		    
			$("#SubmitTop").hide();
			$(".mysearch").hide();
			$(".nav").hide();
			$(".footer").hide();
			$(".active01").hide();
			$(".active02").hide();
			$("#serviceMenu").show();
			$("#submitOrderNowPrivate").show();
			$("#submitOrderNowPublic").hide();
			$("#returnCar").hide();
			$("#shoudPay").hide();			
	    }
	},
	
	//初始化时，创建购物车DOM页面
	domFactory : function(vminfo,cloudInfo,trEvents,cloudInfo) {
		
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
		//to fix bug [3748]
		var resourceNum = 0;		 
		if (vminfo.templateNum != undefined){
			for (var i = 0; i < vminfo.templateNum.length; i++) {
				resourceNum += vminfo.templateNum[i];
			}
		} else {
			resourceNum = 1;
		}		
 
		var serviceType = '';
		var unit = "";
		var period = "";
		var num = "";
 
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
			unit = '时';
        }		
		 		 		
		period = vminfo.period;
		num = vminfo.num;
		
		if (vminfo.templateType == 1) {
			serviceType = '虚拟机';
		}
		else if (vminfo.templateType == 2) {
			serviceType = '虚拟硬盘';
		}		
		else if (vminfo.templateType == 4) {			
			serviceType = '虚拟机备份';
		}
		else if (vminfo.templateType == 5) {			
			serviceType = '云监控';
		}
		else if (vminfo.templateType == 6) {
			serviceType = '负载均衡';
		}
		else if (vminfo.templateType == 7) {
			serviceType = '防火墙';
		}
		else if (vminfo.templateType == 8) {
			serviceType = '公网带宽';
		}
		else if (vminfo.templateType == 9) {
			serviceType = '弹性公网IP';
		}
		else if (vminfo.templateType == 10) {
			serviceType = '物理机';
		}
		else if (vminfo.templateType == 11) {
			serviceType = '对象存储';
		}
		else if (vminfo.templateType == 12) {
			serviceType = '弹性块存储';
		}
		else if (vminfo.templateType == 50) {
			serviceType = '多实例';
		}
		else if (vminfo.templateType == 3) {
			serviceType = '小型机';
		}else if (vminfo.templateType == 13) {
			serviceType = 'NAS文件存储';
		}
		
		//to fix bug [3462]
		//to fix bug [3737]
		
		if(cloudInfo == "1"){
			dom = '<tr id='+vminfo.virtual+' '+ trEvents +'>'    		         
	         +'	 <td width="20%" title="'+ vminfo.instanceName +'">'+tempname+'</td>'
	         +'	 <td width="13%" >'+serviceType+'</td>'    				     				
	         +'	 <td width="13%" >'+resourceNum+'</td>'    		
	         +'	 <td width="14%" >'+(vminfo.charge)+'元</td>'    				        		         
	         +'	 <td width="20%" >'    
	         +'	   <a href="javascript:submitorderNow.subtractnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-reduce.png" /></a>'
	         +'	   <input type="text" readonly class="textCss02" value="'+period+'" id="buynuti_'+vminfo.virtual+'" />' + unit    
	         +'	   <a href="javascript:submitorderNow.addnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-increase.png" /></a>'		         
	         +'	 </td>'    
	         +'	 <td width="20%" >￥<span id="modifyCharge_'+vminfo.virtual+'">'+(period * vminfo.charge).toFixed(2)+'元</span></td>'    		 
	         +'</tr>';
		}
		
		if(cloudInfo == "2"){
			dom = '<tr id='+vminfo.virtual+' '+ trEvents +'>'    		         
	         +'	 <td width="20%" title="'+ vminfo.instanceName +'">'+tempname+'</td>'
	         +'	 <td width="13%" >'+serviceType+'</td>'    				     				
	         +'	 <td width="13%" >'+resourceNum+'</td>'    			         			        		         
	         +'	 <td width="20%" >'    
	         +'	   <a href="javascript:submitorderNow.subtractnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-reduce.png" /></a>'
	         +'	   <input type="text" readonly class="textCss02" value="'+period+'" id="buynuti_'+vminfo.virtual+'" />' + unit    
	         +'	   <a href="javascript:submitorderNow.addnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-increase.png" /></a>'		         
	         +'	 </td>'    	           		
	         +'</tr>';
		}
 
		return dom;
	},	
	
	//to fix bug [3372]
	addnuti:function(virtual, charge, num, period){
		var current = $("#buynuti_" + virtual).val();		
		var total=0;
		//to fix bug [3736]
		if (current < 99){
			current ++;
			$("#buynuti_" + virtual).val(current);
			$("#modifyCharge_" + virtual).html((current * charge).toFixed(2) + '元');
		}
		var p = main.porderNow.vminfos;
		for (var i = 0; i < p.length; i++) {
			if (p[i].virtual == virtual){
				p[i].period=current;
			}
		}		
		main.setShopCartCookie();
		
		for (var i = 0; i < p.length; i++) {
			total = Number(total) + Number(p[i].charge * p[i].period);
		}		
		total = total.toFixed(2);
		$("#productMoney").html(total+'元');
		
	},
	
	subtractnuti:function(virtual, charge, num, period){
		var current = $("#buynuti_" + virtual).val();
		var total=0;
		if (current != 1){
			current --;
			$("#buynuti_" + virtual).val(current);
			$("#modifyCharge_" + virtual).html((current * charge).toFixed(2) + '元');
		}		
		var p = main.porderNow.vminfos;
		for (var i = 0; i < p.length; i++) {
			if (p[i].virtual == virtual){
				p[i].period=current;
			}
		}		
		main.setShopCartCookie();		

		for (var i = 0; i < p.length; i++) {
			total = Number(total) + Number(p[i].charge * p[i].period);
		}		
		total = total.toFixed(2);
		$("#productMoney").html(total+'元');		
		
	},	
	
//	addnum:function(virtual, charge, num, period){
//		var current = $("#buynum_" + virtual).val();
//		var currentnuti = $("#buynuti_" + virtual).val();
//		var total=0;
//		current ++;
//		$("#buynum_" + virtual).val(current);
//		$("#modifyCharge_" + virtual).html((current * currentnuti * charge * 10) / 10);
//		 
//		var p = main.porderNow.vminfos;
//		for (var i = 0; i < p.length; i++) {
//			if (p[i].virtual == virtual){
//				p[i].num=current;
//			}
//		}		
//		main.setShopCartCookie();	 
//		
//		for (var i = 0; i < p.length; i++) {
//			total = Number(total) + Number(p[i].charge * p[i].period * p[i].num);
//		}		
//		total = total.toFixed(2);
//		$("#productMoney").html(total);	
//	},
	
//	subtractnum:function(virtual, charge, num, period){
//		var current = $("#buynum_" + virtual).val();
//		var currentnuti = $("#buynuti_" + virtual).val();
//		var total=0;
//		if (current != 1){
//			current --;
//			$("#buynum_" + virtual).val(current);
//			$("#modifyCharge_" + virtual).html((current * currentnuti * charge * 10) / 10);
//			
//			var p = main.porderNow.vminfos;
//			for (var i = 0; i < p.length; i++) {
//				if (p[i].virtual == virtual){
//					p[i].num=current;
//				}
//			}		
//			main.setShopCartCookie();	 
//			
//			for (var i = 0; i < p.length; i++) {
//				total = Number(total) + Number(p[i].charge * p[i].period * p[i].num);
//			}		
//			total = total.toFixed(2);
//			$("#productMoney").html(total);				
//			
//		}
//	},
	

 
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
		    $(".submitorderNow .trytab #shopcartTatolMoney").html('');
		    $(".submitorderNow .trytab #shopcartTatol").html('');			  
	    }
    },	
	
    getProductBuyCountById:function (id){
		var count = 0;
		$.ajax({
			url : "/UCFCloudPortal/product/getProductBuyCountById.action?id="+id,
			data : null,
			async : false,
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
	        success : function(data) {
	        	if(null!=data){
	        		product = data[0];
	        	}	        	
	        }
	    });
		return product;
	},   
    
	//提交购物车
	saveProOrder : function(message) {
		
		var pid;		
		for ( var i = 0; i < main.porderNow.vminfos.length; i++) {
			pid = main.porderNow.vminfos[i].productId;	
			var product = submitorderNow.showProductByid(pid);
			if(null!=product){
				//检查服务是否已经下线——中通服
				if(product.state == 6){
					alert("服务 " + main.porderNow.vminfos[i].instanceName + " 已经下线！");
		    		return;
				}
				var quotaNum = product.quotaNum;
		    	var buyCount = submitorderNow.getProductBuyCountById(pid);
		    	if((quotaNum - buyCount) == 0){
		    		alert("服务 " + main.porderNow.vminfos[i].instanceName + " 申请已达到最大限额，请稍后再试！");
		    		return;
		    	}	
			}					
		}		
		//to fix bug [3880]
		if(submitorderNow.checkPublicIPAddressExists()){
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
					"templateInfo" : $.toJSON(main.porderNow.vminfos)
				},
				success : function(json) {
					$(".main1 .submitorderNow .trytab tbody").html('');
					if ("成功：订单信息成功写入数据库" == json) {
						main.porderNow = {
							voorders : [],
							vminfos : [],
							inslists : [],
							subnets : []
						};
						main.removeNowShopCartCookie();
						main.shownum_shopcart();
						main.carstate = true;
						server.load(".main1 .submitorderNow", [ "order", "succ" ],
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
	 
	checkPublicIPAddressExists:function(){//bug 0003871 0003858
		var ipAddress = "";
		for(var i = 0;i<main.porderNow.vminfos.length;i++){
			//9公网IP
			if(main.porderNow.vminfos[i].templateType == 9){				 
				ipAddress = main.porderNow.vminfos[i].ipAddress;
	            break;
			}
		}
		 var ret_val = false;
		  $.ajax({
			  url : submitorderNow.root+"/publicIp/showPublicIPAddressExists.action",
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

	//把每条记录的单价存入数组中，为求和用
	merge : function() {
		var total = 0;
		for ( var i = 0; i < main.totalcharge.length; i++) {
			total += Number(main.totalcharge[i]);
		}
		total = total.toFixed(2);
		return total;
	},
	//比较价格，在删除产品时要比较价格，精确比对到浮点数
	floatCompare : function(f1, f2) {
		var PEN = 0.000001;
		if (Math.abs(f1 - f2) < PEN) {
			return true;
		} else {
			return false;
		}
	},
	
	checkLB:function(){
		var flag = true;
		$.ajax({
			type : "POST",
		      url : submitorderNow.root+"/vdc_fwmanage/checkFWOrLB.action",
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
				url : submitorderNow.root+"/portal_lb/checkPortByIP.action",
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
			      url : submitorderNow.root+"/vdc_fwmanage/checkFWOrLB.action",
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
			url : submitorderNow.root + "/portal_mo/checkMonitor.action",
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
	
//	checkSingleVM:function(info){
//		multiVM.vethAdaptorNum = info.vethAdaptorNum;
//		//判断用户是否具备购买的vlan
//		var flag = multiVM.checkVlanForTemplate();
//		if(!flag){
//			alert("该用户分配的VLAN已作废，不能申请 "+info.instanceName);
//		}	
//		return flag;
//	}	
	
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
		    	    url : submitorderNow.root+"/vdc_vmmanage/getCountUserVlanByType.action",
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
			  	      url: submitorderNow.root+"/order/checkUserVlanId.action",
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
    	$.getScript(submitorderNow.root+"/js/privateSkySwitch.js", function(e){
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
