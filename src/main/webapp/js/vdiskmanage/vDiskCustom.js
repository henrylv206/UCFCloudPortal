var vDiskCustom = {
	root:"",
	strCloud: "",

    init : function(productId) {
		$("#serviceMenu").show();
		$("#price").hide();
		$("#price2").hide();
	
		vDiskCustom.ajax_getConfigParams();
	  
		vDiskCustom.template = {};
		vDiskCustom.pros = {};
		vDiskCustom.num=''; 
		//vDiskCustom.findRelativeProduct(productId); 
		vDiskCustom.showProductByid(productId); 
		var templateId = $("#templateId").val();
		vDiskCustom.getVMTemplateById(templateId);
		var preStr = "vDisk";//to fix bug [4795]
		var serviceKey = preStr + vDiskCustom.buildKey();	   
		$("#instanceName").val(serviceKey);
		  	
    },
    
   ajax_getConfigParams : function() {
      $.ajax({
        url : "../template/getConfigParams.action?type=1",
        type : 'POST',
        dataType : 'json',
        async : false,
        success : function(data) {
          if(data != null) {
            	$("#curprojectid").val(data.curprojectid);
              if(data.curprojectid == 3){
		      	$("#tr_maxCpuNum").show();
		      	$("#tr_maxMemorySize").show();
		      	$("#tr_maxStorageSize").show();
		      }else{
		      	$("#tr_maxCpuNum").hide();
		      	$("#tr_maxMemorySize").hide();
		      	$("#tr_maxStorageSize").hide();		      
		      }
		      if(data.curprojectid == 1){
		      	$("#tr_storeType").show();	
		      }else{
		      	$("#tr_storeType").hide();	
		      }
		      
		      //改成默认显示带宽
		      if(data.curprojectid == 2){
		      	$("#tr_storageSize").show();	
		      }else{
		      	$("#tr_storageSize").hide();
		      }		         
              
              if(data.curprojectid == 2){
	              if(data._vmos != undefined) {
	                var _osArray = $.evalJSON(data._vmos);
	                $("#vmos_search").append("<option value='' selected>--请选择--</option>");
	                $(_osArray).each(function(i) {
	                  if(i == 0) {
	                    $("#osName").append("<option value='" + _osArray[i].value + "' selected>" + _osArray[i].text + "</option>");
	                  }
	                  else {
	                    $("#osName").append("<option value='" + _osArray[i].value + "'>" + _osArray[i].text + "</option>");
	                  }
	                  $("#vmos_search").append("<option value='" + _osArray[i].value + "'>" + _osArray[i].text + "</option>");
	                });
	              }
	              if(data.storagesize != undefined) {
	                var storageArray = $.evalJSON(data.storagesize);
	                $(storageArray).each(function(i) {
	                  $("#storageSize").append("<option value='" + storageArray[i].value + "'>" + storageArray[i].text + "</option>");
	                });
	              }                           
              }

              if(data.poolname != undefined) {
                var poolArray = $.evalJSON(data.poolname);
                $(poolArray).each(function(i) {
                  if(i == 0) {
                    $("#resourcePool").append("<option value='" + poolArray[i].value + "' selected>" + poolArray[i].text + "</option>");
                  }
                  else {
                    $("#resourcePool").append("<option value='" + poolArray[i].value + "'>" + poolArray[i].text + "</option>");
                  }
                });
              }
              if(data.cpuhz != undefined) {
                $("#cpuHz").val(data.cpuhz);
              }
          }
          else {
            return;
          }
        }
      });
    },

	findRelativeProduct:function (){    	
		$.ajax({
			type : "POST",
			url : "/UCFCloudPortal/vdc_volumemanage/getStorageProductList.action",
			datatype : "json",
			data : {},
			async : false,
			global : false,
			success : function(data) {
				if (data.length > 0) {
					for ( var i = 0; i < data.length; i++) {
						var relatedVM = '<li><div class="picArea"><a href="buyvdisk.jsp?id='+ data[i].id + '" title="'+data[i].description + '\n' + data[i].specification+'"><img src="../images/products/vdisk124.png" /></a></div></li>';
						$(".ulList03").append(relatedVM);
						if (i > 4) {
							return;
						}
					}
				}
			}
		});
    },
//获取产品库存
	getProductBuyCountById: function(productId){
		var count = 0;
		$.ajax({
				url : "/UCFCloudPortal/product/getProductBuyCountById.action?id="+productId,
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
//根据id获取产品信息
	showProductByid: function(productId){
		$.ajax({
	        type : "POST",
	        url : "../vdc_vmmanage/showProductByid.action",
	        datatype : "json", 
	        data : {id : productId},
	        async : false,
	        global : false,
	        success : function(data) 
	        {
    			var unit = "";
    			var productType = "";
    			//购买单位中文描述 ：年、月、周、天、小时 
    			if(data[0].unit == 'Y') {                    	    			
    				unit = '年';
                }
    			if(data[0].unit == 'M') {                    	    			
    				unit = '月';
                }
    			if(data[0].unit == 'W') {                    	    			
    				unit = '周';
                }
    			if(data[0].unit == 'D') {                    	    			
    				unit = '天';
                }
    			if(data[0].unit == 'H') {                    	    			
    				unit = '小时';
    			}
    			$("#vmprice").html(data[0].price + "元/" + unit);
    			$("#productId").val(data[0].id);//产品id
    			$("#templateId").val(data[0].templateId);//模板id
    			$("#con_tab_1").html(data[0].description);//详细信息
    			//申请时长，显示单位
    			$("#td_unit").html(unit);
	        }
		});
	},
	 //生成服务名称
	 buildKey : function(){
	    var myDate = new Date();
	    var str = "" + (20+myDate.getHours()) + myDate.getMinutes() +
	    myDate.getSeconds() +
	    myDate.getMilliseconds();
	    return Number(str);
	},
//根据模板id得到模板信息
    getVMTemplateById: function(templateId){
    	$.ajax({
	    	url : vDiskCustom.root+"/template/getVMTemplateById.action?type=1&id="+templateId,
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				vDiskCustom.vethAdaptorNum = json.vethAdaptorNum;
				vDiskCustom.template = json;
				$("#vmos").html(json.vmos);
				
				$("#vmcpu").append(json.cpuNum);
				$("#vmcpu").append('×');
				$("#vmcpu").append(json.cpufrequency);
				$("#vmcpu").append('G');

				$("#vmmemory").append(json.memorySize);
				$("#vmmemory").append('G');
				//模板暂时不支持录入硬盘个数与大小 --先注释
				//alert(json.storageSize);
				$("#vmstorage").append(json.storageSize);
				$("#vmstorage").append('G');
				
				vDiskCustom.getAllResourcePools();
			}
		});
    },
    //查询资源池
    getAllResourcePools: function(){
    	$.ajax({
	    	url : vDiskCustom.root+"/resourcePools/listResourcePools.action",
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				if(json != null){
					//alert('getAllResourcePools data='+json);
					var objArray = json.list;
	                var num = 0;
					//alert('objArray data='+objArray);
					if(objArray != null && objArray.length > 0){
						var poolId = -1;
						var poolName = '';
						$(objArray).each(
								function(i) {
									num = i+1;
									poolId = objArray[i].id;
									poolName = objArray[i].poolName;
					                   if(i == 0) {
					                     $("#poollist").append("<option value="+poolId + " selected>"+poolName+"</option>");
					                   }
					                   else {
					                	   $("#poollist").append("<option value="+poolId+">"+poolName+"</option>");
					                   }
								});
					}
					if(num != 1){
						var tr_pool=document.getElementById("tr_pool");
						tr_pool.style.display = '';
					}
					vDiskCustom.onChangeResPool($("#poollist").val());
				}
			}
		});
    },
  
    onChangeResPool: function(resourcePoolId){    	    	

			$("#storagelist").html("");			
			$("#oslist").html("");
			$("#zonelist").html("");    		 
    	 
			vDiskCustom.initZoneParams(resourcePoolId); 
    	    vDiskCustom.initOsParams(resourcePoolId); 
    	    vDiskCustom.initStoreTypeParams(resourcePoolId); 

    	    $("#disklist option[value='-1']").remove();    
    	
    },
    
    initStoreTypeParams : function(resourcePoolsId){
    	var osId = $("#oslist").val();

        if(osId != undefined) {
            $.ajax({
	           url : "../template/getStoreTypesByOsId.action?osId=" + osId+"&resourcePoolsId="+resourcePoolsId,
	           type : "POST",
	           async : false,
	           dataType : "json",
	           success : function(data) {
	             if(data.indexOf("error") == 0) {
	                 dialog_confirmation('#dialog_confirmation', "失败：查询存储类型失败，"+data);
	                 return;
	             }
	             else {
	            	 var num = 0;
	               if(data != undefined) {
	                   var storetypeArray = $.evalJSON(data);
	                   $(storetypeArray).each(function(i) {
	                	   num = i;
		                   if(i == 0) {
		                     $("#storagelist").append("<option value='" + storetypeArray[i].value + "' selected>" + storetypeArray[i].text + "</option>");
		                   }
		                   else {
		                     $("#storagelist").append("<option value='" + storetypeArray[i].value + "'>" + storetypeArray[i].text + "</option>");
		                   }
	                   });
	               }
	               //存储类型仅为1个时隐藏，大于1时显示
					if(num != 1){
						var tr_type=document.getElementById("tr_storageType");
						tr_type.style.display = '';
					}

	             }
	           }
            });
        }    	
    },    
    
    initOsParams : function(resourcePoolId){
 	    $("#osName").html("");
        if(resourcePoolId != undefined) {
        	
        var zoneId = $("#zonelist").val();
        
        $.ajax({
           url : "/UCFCloudPortal/template/getOsesByResourcePoolId.action?resourcePoolsId="+resourcePoolId+"&zoneId="+zoneId,
           type : "POST",
           async : false,
           dataType : "json",
           success : function(data) {
             if(data.indexOf("error") == 0) {
               dialog_confirmation('#dialog_confirmation', "失败：初始化操作系统下拉列表失败，"+data);
               return;
             }
             else {
               if(data != undefined) {
                 var osArray = $.evalJSON(data);                
                 $(osArray).each(function(i) {
                   if(i == 0) {
                     $("#oslist").append("<option value='" + osArray[i].value + "' selected>" + osArray[i].text + "</option>");
                   }
                   else {
                     $("#oslist").append("<option value='" + osArray[i].value + "'>" + osArray[i].text + "</option>");
                   }
                 });
               }

             }
           }
         });
       }  	
    },    
    
    initZoneParams : function(resourcePoolId){
 	   $("#zone").html("");
        if(resourcePoolId != undefined) {
        	//alert('resourcePoolId='+resourcePoolId);
         $.ajax({
           url : "../template/getZonesByResourcePoolId.action?resourcePoolsId="+resourcePoolId,
           type : "POST",
           async : false,
           dataType : "json",
           success : function(data) {
             if(data.indexOf("error") == 0) {
               dialog_confirmation('#dialog_confirmation', "失败：初始化资源域下拉列表失败，"+data);
               return;
             }
             else {
	                var num = 0;
               if(data != undefined) {
                 var zoneArray = $.evalJSON(data);
                 $(zoneArray).each(function(i) {
		                num = i+1;
                   if(i == 0) {
                     $("#zonelist").append("<option value='" + zoneArray[i].id + "' selected>" + zoneArray[i].name + "</option>");
                   }
                   else {
                     $("#zonelist").append("<option value='" + zoneArray[i].id + "'>" + zoneArray[i].name + "</option>");
                   }
                 });
               }
               
				if(num != 1){
					var tr_zone=document.getElementById("tr_zone");
					tr_zone.style.display = '';
				}
				vDiskCustom.onChangeZone($("#zonelist").val());

             }
           }
         });
       }  	
    },
    //查询可用VLAN
    onChangeZone: function(zoneId){
    	if(zoneId <= 0){
    		//alert('请选择资源域！');
    		$("#vlanlist").html("");
    		$("#vlanlist").html("<option value=\"-1\">请选择</option>");
    		$("#iplist").html("");
    		$("#iplist").html("<option value=\"\">请选择</option>");
    		return;
    	}
		var resourcePoolId = $("#poollist").val();
    	if(resourcePoolId <= 0){
    		//alert('请选择资源池！');
    		$("#vlanlist").html("");
    		$("#vlanlist").html("<option value=\"-1\">请选择</option>");
    		$("#iplist").html("");
    		$("#iplist").html("<option value=\"\">请选择</option>");
    		return;
    	}
    	vDiskCustom.getPhysicalHostVlanByZoneId(resourcePoolId,zoneId);
    },
    //查询可用VLAN的可用IP
    onChangeVlan: function(vlanId){
    	if(vlanId <= 0){
    		//alert('请选择资源域的VLAN！');
			$("#iplist").html("");
			$("#iplist").html("<option value=\"\">请选择</option>");
    		return;
    	}
		var resourcePoolId = $("#poollist").val();
    	if(resourcePoolId <= 0){
    		//alert('请选择资源池！');
			$("#iplist").html("");
			$("#iplist").html("<option value=\"\">请选择</option>");
    		return;
    	}
    	//alert('请选择VLAN='+vlanId);
    	vDiskCustom.getPhysicalHostVlanIPByVlanId(resourcePoolId,vlanId);
    },
    //查询可用VLAN
    getPhysicalHostVlanByZoneId: function(resourcePoolId,zoneId){
		$("#vlanlist").html("");
		$("#vlanlist").html("<option value=\"-1\">请选择</option>");
		$("#iplist").html("");
		$("#iplist").html("<option value=\"\">请选择</option>");
    	$.ajax({
	    	url : vDiskCustom.root+"/resourcesUsed/pmList!listHostVlan.action?resourcePoolId="+resourcePoolId+"&zoneId="+zoneId,
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				if(json != null){
					var objArray = json.list;
					if(objArray != null && objArray.length > 0){
						var vlanId = -1;
						var vlanName = '';
						$(objArray).each(
								function(i) {
									vlanId = objArray[i].vlanid;
									vlanName = vlanId + ':' +objArray[i].businesstype+ '('+objArray[i].desc+')';
									$("#vlanlist").append("<option value="+vlanId+">"+vlanName+"</option>");
								});
					}
				}
			}
		});
    },
    //查询可用VLAN下可用的IP
    getPhysicalHostVlanIPByVlanId: function(resourcePoolId,vlanId){
		$("#iplist").html("");
		$("#iplist").html("<option value=\"\">请选择</option>");
    	$.ajax({
	    	url : vDiskCustom.root+"/resourcesUsed/pmList!listVlanIP.action?resourcePoolId="+resourcePoolId+"&vlanId="+vlanId,
	    	//url : vDiskCustom.root+"/resourcesUsed/pmList!listHostVlanIp.action?resourcePoolId="+resourcePoolId+"&vlanId="+vlanId,
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				if(json != null){
					var objArray = json.list;
					if(objArray != null && objArray.length > 0){
						var ip = '';
						$(objArray).each(
								function(i) {
									ip = objArray[i].ip;
									$("#iplist").append("<option value="+ip+">"+ip+"</option>");
								});
					}
				}
			}
		});
    },
//检查服务名称，不能与现有已经被使用服务名称重名
    checkProductRename : function(name){
		var count = 0;		
		var data = {
			name: name,
			type: 2
		};		
		$.ajax({
			url : vDiskCustom.root+"/product/checkProductRename.action",
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
//检查服务名称，不能与现有购物车中订单服务名称重名
    checkRename : function(name){
		var flag = false;		
		for(var i = 0;i<main.porder.vminfos.length;i++){					
			if(main.porder.vminfos[i].instanceName == name){				 
				flag = true;
				break;
			}
		}
		return flag;
	},
//提交物理机订单信息：0：加入购物车；1:立即购买
  buyVMCustom : function(para){

	    if(commonUtils.len($(".VirtualDisk #instanceName").val()) == 0) {
	      alert("请输入服务名称");
	      $(".VirtualDisk #instanceName").focus();
	      return;
	    }
	    if(commonUtils.len($(".VirtualDisk #instanceName").val()) > 20) {
	      alert("服务名称不能超过20个字符（一个汉字算2个字符）");
	      $("#instanceName").focus();
	      return;
	    }
	    if(! commonUtils.scoreReg.exec($(".VirtualDisk #instanceName").val())) {
	      alert("服务名称只能包含"+commonUtils.regMessage);
	      $(".VirtualDisk #instanceName").focus();
	      return;
	    }
	    var resname = vDiskCustom.checkProductRename($(".VirtualDisk #instanceName").val());
	    if(resname == "1"){
			alert("该服务名称已经被使用");
			$(".VirtualDisk #instanceName").focus();
            return;
  	    }
	    
    	if(vDiskCustom.checkRename($(".VirtualDisk #instanceName").val())){
			alert("该服务名称与购物车中有重名");
			$(".VirtualDisk #instanceName").focus();
            return;
  	    }
    	//资源池poollist
    	if(Number($(".VirtualDisk #poollist").val()) <= 0){
		      alert("请选择资源池");
		      $(".VirtualDisk #poollist").focus();
		      return;
    	}

    	if(Number($(".VirtualDisk #oslist").val()) <= 0){
		      alert("请选择操作系统");
		      $(".VirtualDisk #oslist").focus();
		      return;
    	}
    	//资源域zonelist
    	if(Number($(".VirtualDisk #zonelist").val()) <= 0){
		      alert("请选择资源域");
		      $(".VirtualDisk #zonelist").focus();
		      return;
    	}

	    
	    if($(".VirtualDisk #instanceDesc").val().length > 100) {
	      alert("描述不能超过100个字符");
	      $(".VirtualDisk #instanceDesc").focus();
	      return;
	    }
	    var buyperiod = $(".VirtualDisk #buyperiod").val();
	    if(commonUtils.len(buyperiod) == 0) {
		      alert("请输入申请时长");
		      $(".VirtualDisk #buyperiod").focus();
		      return;
		}
	    if(!isDigit(buyperiod)){
		      alert("申请时长必须为大于0的正整数");
		      $(".VirtualDisk #buyperiod").focus();
		      return;
	    }else{
	    	if(Number(buyperiod) <= 0){
			      alert("申请时长必须为大于0的正整数");
			      $(".VirtualDisk #buyperiod").focus();
			      return;
	    	}
	    }
	    var poolId = $(".VirtualDisk #poollist").val();
	    var zoneId = $(".VirtualDisk #zonelist").val(); 
	    var storageId = $(".VirtualDisk #storagelist").val();	
	    var storageSizeId = $(".VirtualDisk #disklist").val();	

	  var pid = product.pro.id;
	  main.porder.voorders.push({
	        id : pid,
	        num : 1,
	        productName : "",
	        type : 1,
	        typeId : 10,
	        usePeriod : 0
	    });
	  //物理机订单信息放到购物车信息中
	  var vminfos = {
		  productId: pid,
		  productName: product.pro.name,
		  shopcartdesc:product.pro.description,
	      virtual: main.buildKey(),
	      flag : "ebsService",
	      description : $(".VirtualDisk #instanceDesc").val(),
	      instanceName : $(".VirtualDisk #instanceName").val(),
	      poolId : poolId,   	      
	      zoneId : zoneId,
	      templateId : $(".VirtualDisk #templateId").val(),//模板
	      templateType : parseInt(product.pro.type),
	      charge:0,
	      unit:product.pro.unit,
	      unitC:product.pro.unitC,
	      num:$(".VirtualDisk #buyperiod").val(),
	      period:$(".VirtualDisk #buyperiod").val(),
	      storeType:storageId,
	      storageSize:storageSizeId
	  };

		//main.setShopCartCookie(); //信息存放在cookie
		//判断是0：加入购物车；1:立即购买，跳转到不同处理页面
		if(para == 0){//0：加入购物车
    		 main.porder.vminfos.push(vminfos);
    		 main.setShopCartCookie(); //信息存放在cookie
			 javascript:window.location.href='shoppingcart.jsp';
		}
		if(para == 1){//1:立即购买
    		for(var i = 0;i<main.porderNow.vminfos.length;i++){					
    			main.porderNow.vminfos.removeAt(i, 1);
    		}	
    		for(var i = 0;i<main.porderNow.vminfos.length;i++){					
    			main.porderNow.vminfos.removeAt(i, 1);
    		}
    		 main.porderNow.vminfos.push(vminfos);
    		 main.setNowShopCartCookie();
			 javascript:window.location.href='submitorderNow.jsp';
		}

  },
  cancel : function(){
	  	$("#poolName").val("");
	    $("#storageSize").html("");
	    $("#instanceName").val("");
	    $("#instanceDesc").val("");
  }
  
}