var shopcart = {
	strCloud: "",
	root:"/UCFCloudPortal",

	//购物车初始化
	init : function() {
		main.carstate = true;
		main.totalcharge = new Array();

		var p = main.porder;
		var d = true;
		var total = '0'; 		 
		
		if (p.vminfos.length > 0) {
			var total = 0;

			//奇偶行上色与焦点行事件处理   ninghao 2012-08-24 start
			//焦点行事件处理
			var focusEven = " onmouseover=$(this).addClass(\"over\") onmouseout=$(this).removeClass(\"over\") ";
			var oddEven = '';
			var trEvents = '';
			
			for ( var i = 0; i < p.vminfos.length; i++) {
				//奇偶行上色
				oddEven = (i%2) == 0 ? ' class=\"alt\" ' : ' ';
				trEvents = oddEven + focusEven ;
				var dom_final = this.domFactory(p.vminfos[i],trEvents);	
 
				$("#shopcarHtml").append(dom_final);	
				//to fix bug [3321]
				total += Number(p.vminfos[i].charge * p.vminfos[i].period);
 
				d = false;
			}		
			total = total.toFixed(2);
			$("#productnum").html(p.vminfos.length);
			// to fix bug [3323]
			$("#productMoney").html(total+'元');						
			
		} else {
			$("#shopcarHtml").html("");	//to fix bug [5152]
			$("#productnum").html(0);
			$("#productMoney").html(0);
			//to fix bug [3172]
			//$("#jiesuan").attr('disabled',"true");
			
		}
		
		if (d) {
			dom = '<tr><td colspan="5">(未申请服务)</td></tr>';
			$(" .shopcart .trytab tbody").append(dom);
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
		
		//to fix bug [3623]
		$(".delcheckproduct").click(function(){
			var havedel = true;
			$("input[name='checkProduct']").each(function(){
				var delpara = '';				
				var delparas = {};
				if(this.checked){					 				
					havedel = false;
				}				 
			});
			
			if(havedel){
				alert('请选择要删除的服务！');
			} else {
				if (confirm('是否删除该服务？')) {					 
					$("input[name='checkProduct']").each(function(){
						var delpara = '';				
						var delparas = {};
						if(this.checked){
							delpara = $("#del_" + this.id).val();					 
							delparas = delpara.split(",");								
							shopcart.removeAllIns(delparas[0],delparas[1],delparas[2],delparas[3],delparas[4]);
						}
						$(".shopcarcheck").attr("checked",false);
					});
				}
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
		//shopcart.PublicPrivateSwitch();
		
	    $(".shopcart .trytab #shopcartTatolMoney").html('');
	    $(".shopcart .trytab #shopcartTatol").html('');	
	    $(".shopcart .trytab #shopcartTDPrice").html('');	
	    
		$("#serviceMenu").show();	
		$("#shopcartPrice1").hide();	
		$("#shopcartPrice2").hide();
		$("#shopcartPrice3").hide();
		$("#shopcartTotal").hide();
			
		//to fix bug [3200]
		$("#jiesuan").click(function(){
			if(main.porder.vminfos.length == 0){
				alert("你的购物车没有服务！");
			} else {
				javascript:window.location.href='submitorder.jsp';
			}
		});
		
	},
	
	//初始化时，创建购物车DOM页面
	domFactory : function(vminfo,trEvents) {
		//alert(vminfo.templateType);
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
		//to fix bug [3786]
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
		//to fix bug [3225]
		else if (vminfo.templateType == 2) {
			serviceType = '虚拟硬盘';
		}		
		else if (vminfo.templateType == 4) {			
			serviceType = '备份服务';
		}
		else if (vminfo.templateType == 5) {			
			serviceType = '监控服务';
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
			serviceType = '公网IP';
		}
		else if (vminfo.templateType == 10) {
			serviceType = '物理机';
		}
		else if (vminfo.templateType == 11) {
			serviceType = '对象存储';
		}
		else if (vminfo.templateType == 12) {
			serviceType = '块存储';
		}
		else if (vminfo.templateType == 50) {
			serviceType = '多实例';
		}
		else if (vminfo.templateType == 3) {
			serviceType = '小型机';
		}else if (vminfo.templateType == 13) {
			serviceType = 'NAS文件系统';
		}
		
		dom = '<tr id='+vminfo.virtual+' '+ trEvents +'>'    
         +'	 <td width="10%" class="number"><input type="checkbox" name="checkProduct" id='+vminfo.virtual+' /></td>'
         +'	 <td width="30%" ><a title="'+ vminfo.instanceName +'" class="quit" href="shoppingcartview.jsp?productId='+vminfo.virtual+'&pid='+vminfo.productId+'">'+tempname+'</a></td>'
         +'	 <td width="15%" >'+serviceType+'</td>'    				     				
         +'	 <td width="15%" >'+resourceNum+'</td>'    		
         +'	 <td width="25%" >'    
         +'	   <a title="减少时长" href="javascript:shopcart.subtractnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-reduce.png" /></a>'
         +'	   <input type="text" readonly class="textCss02" value="'+period+'" id="buynuti_'+vminfo.virtual+'" />' + unit    
         +'	   <a title="增加时长" href="javascript:shopcart.addnuti(\''+ vminfo.virtual+ '\',\''+ vminfo.charge+ '\',\''+ vminfo.num+ '\',\''+ vminfo.period+ '\');"><img src="../images/icons/icon-increase.png" /></a>'
         +'    <input type="hidden" id="del_'+vminfo.virtual+'" value="'+vminfo.instanceName+','+vminfo.productId+','+vminfo.virtual+','+vminfo.charge+','+vminfo.templateType+'" />'		         
         +'	 </td>'    	             
         +'	 <td  width="5%" class="shop last"><a href="#"><img title="删除" src="../images/icons/icon-del.png" onclick=shopcart.removeIns(\"'+ vminfo.instanceName+ '\",\"'+ vminfo.productId+ '\",\"'+ vminfo.virtual+ '\",\"'+ vminfo.charge + '\",\"'+vminfo.templateType+ '\"); /></a></td>'
         +'</tr>';
		
		return dom;
	},	
	
	addnuti:function(virtual, charge, num, period){
		var current = $("#buynuti_" + virtual).val();
		//var currentnum = $("#buynum_" + virtual).val();
		var total = 0;
		if (current < 99){
			current ++; 	
			$("#buynuti_" + virtual).val(current);
			$("#modifyCharge_" + virtual).html((current * charge).toFixed(2) + '元');
		}
		var p = main.porder.vminfos;
		for (var i = 0; i < p.length; i++) {
			if (p[i].virtual == virtual){
				p[i].period=current;
			}
		}		
		main.setShopCartCookie();
		
		for (var i = 0; i < p.length; i++) {
			//to fix bug [3321]
			total = Number(total) + Number(p[i].charge * p[i].period);						 	
		}	
 
		total = total.toFixed(2);
		$("#productMoney").html(total+'元');
		
	},
	
	subtractnuti:function(virtual, charge, num, period){
		var current = $("#buynuti_" + virtual).val();
		//var currentnum = $("#buynum_" + virtual).val();
		var total = 0;
		if (current != 1){
			current --;
			$("#buynuti_" + virtual).val(current);
			$("#modifyCharge_" + virtual).html((current * charge).toFixed(2) + '元');
		}		
		var p = main.porder.vminfos;
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
	
	PublicPrivateSwitch : function() {	
	    $(".shopcart .trytab #shopcartTatolMoney").html('');
	    $(".shopcart .trytab #shopcartTatol").html('');	
    },	
    
//	addnum:function(virtual, charge, num, period){
//	//var current = $("#buynum_" + virtual).val();
//	var currentnuti = $("#buynuti_" + virtual).val();		 
//	var total = 0;
//	current ++;
//	$("#buynum_" + virtual).val(current);
//	$("#modifyCharge_" + virtual).html((currentnuti * charge * 10) / 10);
//	 
//	var p = main.porder.vminfos;
//	for (var i = 0; i < p.length; i++) {
//		if (p[i].virtual == virtual){
//			p[i].num=current;
//		}
//	}		
//	main.setShopCartCookie();	 
//	
//	for (var i = 0; i < p.length; i++) {
//		total = Number(total) + Number(p[i].charge * p[i].period * p[i].num);		
//	}		
//	total = total.toFixed(2);
//	$("#productMoney").html(total);	
//},
//
//subtractnum:function(virtual, charge, num, period){
//	var current = $("#buynum_" + virtual).val();
//	var currentnuti = $("#buynuti_" + virtual).val();
//	var total = 0;
//	if (current != 1){
//		current --;
//		$("#buynum_" + virtual).val(current);
//		$("#modifyCharge_" + virtual).html((current * currentnuti * charge * 10) / 10);
//		
//		var p = main.porder.vminfos;
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
//		
//	}
//},    
 
	 
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
			  url : shopcart.roo+"/publicIp/showPublicIPAddressExists.action",
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
	
	//群删除 
	//to fix bug [3113]
	removeAllIns : function(resName, productId, virtual, charge, type) {
		 
		var k; // vms计数器
		var t; // vos 索引
		var y = new Array(); // ins 索引
		var j = 0; // 索引
		var h = 0;
		var vms = main.porder.vminfos;
		var vos = main.porder.voorders;
		var ins = main.porder.inslists;
		var nets = main.porder.subnets;
		var tempArr = new Array();
		var tempSubnet = new Array();
		var flag = true;
		 
			main.isEventBubbleCanceled = true;
			
		 
			if (type == 1) {
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].TProductInfo.productId == 40 && ins[i].configValue == virtual) {
						flag = false;
						alert('您删除的主机中含有申请的弹性IP');
						return;
					}
					if ((ins[i].TProductInfo.productId == 30 || ins[i].TProductInfo.productId == 31) && ins[i].configValue == virtual) {
						flag = false;
						alert('您删除的主机中含有申请的弹性存储');
						return;
					}
				}
				if (flag) {
					for ( var i = 0; i < vms.length; i++) {
						if (vms[i].virtual == virtual) {							 
							k = i;
						}
					} 
					main.porder.vminfos.removeAt(k, 1);
					for ( var i = 0; i < vos.length; i++) {
						if (vos[i].id == productId) {
							t = i;
						}
					}
					main.porder.voorders.removeAt(t, 1);
					$("#" + virtual).remove();					 
				}
				main.setShopCartCookie();
			}
			
			if (type == 11) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			
			
			if (type == 8) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			

			if (type == 4) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			
			
			if (type == 40) {
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].instId != virtual) {
						tempArr[j] = ins[i];
						j++;
					}
				}

				main.porder.inslists = tempArr;
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
 
			if (type == 2) {
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].instId != virtual) {
						tempArr[j] = ins[i];
						j++;
					}
				}

				main.porder.inslists = tempArr;
				$("#" + virtual).remove();
				main.setShopCartCookie();
			}
			
			if (productId == 60 || productId == 61) {

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].virtual == virtual) {
						t = i;
					}
				}
				main.porder.inslists.removeAt(t, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}

			if (type == 12) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 13) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 3) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}		
				
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 5) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 6) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 7) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 8) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}			
			if (type == 9) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 10) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}		
				
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			

			
			if (type == 15) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			
			if (type == 50) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}			
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (main.porder.voorders.length == 0) {
				$(".main1 .shopcart .trytab tbody")
						.html("")
						.html('<tr><th colspan="5" align="left">产品列表</th></tr><tr><td>(未申请服务)</td></tr>');
			}
			for ( var i = 0; i < main.totalcharge.length; i++) {
				if (shopcart.floatCompare(charge, main.totalcharge[i])) {
					main.totalcharge.removeAt(i, 1);
					break;
				}
			}
			//main.shownum_shopcart();
			
			$("#productnum").html(main.porder.vminfos.length);
 
			var delTotal = 0;
			for ( var i = 0; i < main.porder.vminfos.length; i++) {
				delTotal += Number(main.porder.vminfos[i].charge * main.porder.vminfos[i].period);
			}
			delTotal = delTotal.toFixed(2);
			$("#productMoney").html(delTotal);			
		
		  $(".shopcart .trytab #shopcartTatolMoney").html('');
		  $(".shopcart .trytab #shopcartTatol").html('');		  
	},	
	
	//删除购物车中的一条购买记录
	removeIns : function(resName, productId, virtual, charge, type) {
 
		var k; // vms计数器
		var t; // vos 索引
		var y = new Array(); // ins 索引
		var j = 0; // 索引
		var h = 0;
		var vms = main.porder.vminfos;
		var vos = main.porder.voorders;
		var ins = main.porder.inslists;
		var nets = main.porder.subnets;
		var tempArr = new Array();
		var tempSubnet = new Array();
		var flag = true;
		if (confirm('是否删除该服务？')) {
			main.isEventBubbleCanceled = true;
			
		 
			if (type == 1) {
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].TProductInfo.productId == 40 && ins[i].configValue == virtual) {
						flag = false;
						alert('您删除的主机中含有申请的弹性IP');
						return;
					}
					if ((ins[i].TProductInfo.productId == 30 || ins[i].TProductInfo.productId == 31) && ins[i].configValue == virtual) {
						flag = false;
						alert('您删除的主机中含有申请的弹性存储');
						return;
					}
				}
				if (flag) {
					for ( var i = 0; i < vms.length; i++) {
						if (vms[i].virtual == virtual) {							 
							k = i;
						}
					} 
					main.porder.vminfos.removeAt(k, 1);
					for ( var i = 0; i < vos.length; i++) {
						if (vos[i].id == productId) {
							t = i;
						}
					}
					main.porder.voorders.removeAt(t, 1);
					$("#" + virtual).remove();					 
				}
				main.setShopCartCookie();
			}
			
			if (type == 11) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			
			
			if (type == 8) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			

			if (type == 4) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				 
				main.setShopCartCookie();
			}			
			
			if (type == 40) {
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].instId != virtual) {
						tempArr[j] = ins[i];
						j++;
					}
				}

				main.porder.inslists = tempArr;
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
 
			if (type == 2) {
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].instId != virtual) {
						tempArr[j] = ins[i];
						j++;
					}
				}

				main.porder.inslists = tempArr;
				$("#" + virtual).remove();
				main.setShopCartCookie();
			}
			
			if (productId == 60 || productId == 61) {

				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < ins.length; i++) {
					if (ins[i].virtual == virtual) {
						t = i;
					}
				}
				main.porder.inslists.removeAt(t, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 12) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 13) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 3) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}		
				
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 5) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				$("#" + virtual).remove();
				main.setShopCartCookie();
			}
			if (type == 6) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 7) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 8) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}			
			if (type == 9) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (type == 10) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}		
				
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			

			
			if (type == 15) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}
				main.porder.vminfos.removeAt(k, 1);
                $("#" + virtual).remove();
				main.setShopCartCookie();
			}
			
			if (type == 50) {
				for ( var i = 0; i < vos.length; i++) {
					if (vos[i].id == productId) {
						t = i;
					}
				}
				main.porder.voorders.removeAt(t, 1);
				for ( var i = 0; i < vms.length; i++) {
					if (vms[i].virtual == virtual) {
						k = i;
					}
				}			
				main.porder.vminfos.removeAt(k, 1);
				main.porder.voorders.removeAt(t, 1);
				$("#" + virtual).remove();	
				main.setShopCartCookie();
			}
			if (main.porder.voorders.length == 0) {
				$(".main1 .shopcart .trytab tbody")
						.html("")
						.html('<tr><th colspan="5" align="left">产品列表</th></tr><tr><td>(未申请服务)</td></tr>');
			}
			for ( var i = 0; i < main.totalcharge.length; i++) {
				if (shopcart.floatCompare(charge, main.totalcharge[i])) {
					main.totalcharge.removeAt(i, 1);
					break;
				}
			}			 
			
			$("#productnum").html(main.porder.vminfos.length);
 
			var delTotal = 0;
			for ( var i = 0; i < main.porder.vminfos.length; i++) {
				//to fix bug [3592]
				delTotal += Number(main.porder.vminfos[i].charge * main.porder.vminfos[i].period);
			}
			delTotal = delTotal.toFixed(2);
			$("#productMoney").html(delTotal);			
		}
 
		  $(".shopcart .trytab #shopcartTatolMoney").html('');
		  $(".shopcart .trytab #shopcartTatol").html('');	
		  
	},
	//修改购物车

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
	}
};
