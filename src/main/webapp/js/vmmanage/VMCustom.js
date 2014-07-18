var VMCustom = {
	root:"",
	strCloud: "",
	
    init : function(productId) {
	
	    var cloudStr = VMCustom.getCloudInfo(); 
	
		if(true){
			$("#BuyVMTop").hide();
			$(".mysearch").hide();
			$(".nav").hide();
			$(".footer").hide();
			$(".active01").hide();
			$(".active02").hide();
			$("#serviceMenu").show();
			$("#price").hide();
			$("#price2").hide();
		}
	
		VMCustom.ajax_getConfigParams();
	  
		VMCustom.template = {};
	
		VMCustom.num=''; 
	
		VMCustom.showProductByid(productId); 
		var templateId = $("#templateId").val();
		VMCustom.getVMTemplateById(templateId);
		var preStr = "VM";
		var serviceKey = preStr + VMCustom.buildKey();	   
		$("#instanceName").val(serviceKey);
		$("#pageNum").html("1/1");
		$("#vmbuyIPpage").val("1");
			    	     
    },
    
    vmbuynumChange:function (){  
    	
    	var nicsDhcpSwitch = VMCustom.getNicsDhcpSwitch();
    	
        if (Number($("#vmbuynum").val()) + "" == "NaN") {
        	$("#vmbuynum").val(1);
        }
        
        if (Number($("#vmbuynum").val()) == 0) {
        	$("#vmbuynum").val(1);
        }    
        //to fix bug [7395]
        //to fix bug [7664] 
        if (Number($("#vmbuynum").val()) > 1 && $("#poollist").val() != -1) {
        	if(nicsDhcpSwitch == 0){
            	$("#pageControl").show();
            	$("#vmbuyIPpage").val(1);
            	$("#vmNum").html(1);
        	}        		
        }  
        
        if (Number($("#vmbuynum").val()) == 1) {
        	$("#pageControl").hide();
        	$("#vmbuyIPpage").val(1);
        	$("#vmNum").html(1);
        }         
        
        $("#pageNum").html("1/" + $("#vmbuynum").val());       
    },    
    
  
    
    onChangeResPool: function(resourcePoolId){    	    	

		$("#storagelist").html("");			
		$("#oslist").html("");
		$("#zonelist").html("");    		 
	 
		VMCustom.initZoneParams(resourcePoolId); 
	    VMCustom.initOsParams(resourcePoolId); 
	    VMCustom.initStoreTypeParams(); 
	    VMCustom.initOtherParams();
	    
		var nicsDhcpSwitch = VMCustom.getNicsDhcpSwitch();
		$("#nicsDhcpSwitch").val(nicsDhcpSwitch);
	    if(nicsDhcpSwitch == 0){
			$("#vmbuyIPaddress0").show();			
			if (Number($("#vmbuynum").val()) > 1){
				$("#pageControl").show();
			}			
	    } 
	    
	    //to fix bug [7528]
	    //to fix bug [7464]
        //to fix bug [7534]
	    for (var i=1; i<9; i++) {
	    	var vlandome = '';
			vlandome+=''
		        +'  <table class="infoTbl01" id="ipInfo">'
		        +'    <tr id="vlan1">'
		        +'      <td class="name">网卡1-VLAN：</td>'
		        +'      <td>'
				+'		  <select class="buyvminput vlan1" id="buyvmvlan1">'
				+'			<option value="0">请选择</option>'
				+'		  </select>'
				+'	    </td>'
		        +'   </tr>'
		        +'   <tr id="ipaddress1">'
		        +'      <td class="name">IP地址1：</td>'
		        +'      <td>'
		        +'        <select class="buyvminput " id="buyvmIP1">'
		        +'        </select>'
		        +'      </td>'
		        +'   </tr>' 
		        +'   <tr id="vlan2" style="display:none">'
		        +'      <td class="name">网卡2-VLAN：</td>'
		        +'      <td>'
				+'	      <select class="buyvminput vlan2" id="buyvmvlan2">'
				+'		    <option value="0">请选择</option>'
				+'	      </select>'							
				+'	    </td>'
		        +'    </tr>'
		        +'    <tr id="ipaddress2" style="display:none">'
		        +'      <td class="name">IP地址2：</td>'
		        +'      <td>'
		        +'        <select class="buyvminput " id="buyvmIP2">'
		        +'        </select>'
		        +'      </td>'
		        +'    </tr>'	   
		        +'    <tr id="vlan3" style="display:none">'
		        +'      <td class="name">网卡3-VLAN：</td>'
		        +'      <td>'
				+'	      <select class="buyvminput vlan3" id="buyvmvlan3">'
				+'		    <option value="0">请选择</option>'
				+'	      </select>'							
				+'	    </td>'
		        +'    </tr>'
		        +'    <tr id="ipaddress3" style="display:none">'
		        +'      <td class="name">IP地址3：</td>'
		        +'      <td>'
		        +'        <select class="buyvminput " id="buyvmIP3">'
		        +'        </select>'
		        +'      </td>'
		        +'    </tr>'
		        +'    <tr id="vlan4" style="display:none">'
		        +'      <td class="name">网卡4-VLAN：</td>'
		        +'      <td>'
				+'	      <select class="buyvminput vlan4" id="buyvmvlan4">'
				+'		    <option value="0">请选择</option>'
				+'	      </select>'							
				+'	    </td>'
		        +'    </tr>'
		        +'    <tr id="ipaddress4" style="display:none">'
		        +'      <td class="name">IP地址4：</td>'
		        +'      <td>'
		        +'        <select class="buyvminput " id="buyvmIP4">'
		        +'        </select>'
		        +'      </td>'
		        +'    </tr>'			        			        		                        
		        +'  </table>';
			
			$("#vmbuyIPaddress" + i).html(vlandome);
	    }
	    
	    for(var i=1;i<5;i++){
	    	VMCustom.list_vlan($("#zonelist").val(),$("#poollist").val(),i);
	    }
    },
        
    list_vlan: function (zoneId,resourcePoolsId,n){
    	
    	var eVlanId = '';
    	//fix bug:7815(增加参数:resourcePoolsId）
    	var userVlanArray = new Array();
		$.ajax({
			url : "/UCFCloudPortal/user/findUserVlanByUserId.action",
			type:'POST',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			dataType:'json',
			data : {type:n,zoneId:zoneId,resourcePoolsId:resourcePoolsId},
			async : false,
			success : function(items) {
				if (items.returnList != null && items.returnList.length > 0) {
					for(var i=0; i<items.returnList.length; i++){
						userVlanArray.push(items.returnList[i].vlanId);									
					}
				}
			}					
		});    	
    	
    	if(n==1){
    		var url = "/UCFCloudPortal/order/PortalOrder_findNetworkList.action";
    	}else{
    		var url = "/UCFCloudPortal/order/PortalOrder_findNetworkListOther.action";
    	}
		$.ajax({
			url : url,
			type:'POST',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			dataType:'json',
			data : {flag:zoneId,resourcePoolsId:resourcePoolsId,networkType:n},
			async : false,
			success : function(items) {
				
			    $(".vlan"+n).html("");
				$(".vlan"+n).removeData("jsonvlan"+n); 
				if (items != null && items.length > 0) {
					$(".vlan"+n).data("jsonvlan"+n, items);
					$(items).each(function(i){																		
						var vlanip = items[i].startip.match(/\d+\.\d+\.\d+/)+"";
						items[i].vlanip = vlanip+".0/24";						
						if(userVlanArray.length>0){
							for(var j=0; j<userVlanArray.length; j++){																															
					 			if(userVlanArray[j] == items[i].id){
					 				if(eVlanId==''){
					 					eVlanId = userVlanArray[j];	//to fix bug [7618]			
					 				}					 						 									 						 				
					 				for(var k=0; k<9; k++){
					 					$("#vmbuyIPaddress"+k+" .vlan"+n).append("<option value="+items[i].id+" title="+items[i].name+":"+items[i].startip+"-"+items[i].endip+">"+items[i].name+"("+items[i].vlanip+")"+"</option>");		 					
					 				}
					 			}
							}	
						}														 
						
						if(userVlanArray.length==0){
							if(eVlanId==''){//to fix bug [7618]		
								eVlanId = items[i].id;
							}											
			 				for(var k=0; k<9; k++){
			 					$("#vmbuyIPaddress"+k+" .vlan"+n).append("<option value="+items[i].id+" title="+items[i].name+":"+items[i].startip+"-"+items[i].endip+">"+items[i].name+"("+items[i].vlanip+")"+"</option>");		 					
			 				}
						}											
					});
				}
			}
		});
		
    	for(var m=0; m<9; m++){
    		$("#vmbuyIPaddress" + m + " #buyvmIP"+n).html("");
    	}
		
        $.ajax({        	
            url : "../audit/listIpAddressesByNetWork.action",
            type:'POST',
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType:'json',
            data : {jsonStr:$.toJSON({eVlanId:eVlanId}), resourcePoolsId:resourcePoolsId},
            async : false,
            success : function(items) {

                if (items != null && items.count > 0 && items.returnList!=null) {
                    var ipList = items.returnList;
                    $(ipList).each(function(i){
                    	for(var k=0; k<9; k++){
                    		$("#vmbuyIPaddress" + k + " #buyvmIP"+n).append("<option value="+ipList[i].ipaddress+" >"+ipList[i].ipaddress+"</option>");
                    	}
                    });
                }
            }            
        });		
		
	},  
	
	buyvmIP1Add: function (eVlanId){    	
    	if(eVlanId==0){
    		eVlanId=$("#buyvmvlan1").val(); 
    	}    	
        $.ajax({        	
            url : "../audit/listIpAddressesByNetWork.action",
            type:'POST',
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType:'json',
            data : {jsonStr:$.toJSON({eVlanId:eVlanId}), resourcePoolsId:$("#poollist").val()},
            async : false,
            success : function(items) {
            	for(var m=0; m<9; m++){
            		$("#vmbuyIPaddress" + m + " #buyvmIP1").html("");
            	}
                if (items != null && items.count > 0 && items.returnList!=null) {
                    var ipList = items.returnList;
                    $(ipList).each(function(i){
                    	for(var k=0; k<9; k++){
                    		$("#vmbuyIPaddress" + k + " #buyvmIP1").append("<option value="+ipList[i].ipaddress+" >"+ipList[i].ipaddress+"</option>");
                    	}
                    });
                }
            }            
        });		    	
    },
    
	buyvmIP2Add: function (eVlanId){    	
    	if(eVlanId==0){
    		eVlanId=$("#buyvmvlan2").val(); 
    	}    	
        $.ajax({        	
            url : "../audit/listIpAddressesByNetWork.action",
            type:'POST',
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType:'json',
            data : {jsonStr:$.toJSON({eVlanId:eVlanId}), resourcePoolsId:$("#poollist").val()},
            async : false,
            success : function(items) {
            	for(var m=0; m<9; m++){
            		$("#vmbuyIPaddress" + m + " #buyvmIP2").html("");
            	}
                if (items != null && items.count > 0 && items.returnList!=null) {
                    var ipList = items.returnList;
                    $(ipList).each(function(i){
                    	for(var k=0; k<9; k++){
                    		$("#vmbuyIPaddress" + k + " #buyvmIP2").append("<option value="+ipList[i].ipaddress+" >"+ipList[i].ipaddress+"</option>");
                    	}
                    });
                }
            }            
        });		    	
    },    
    
	buyvmIP3Add: function (eVlanId){    	
    	if(eVlanId==0){
    		eVlanId=$("#buyvmvlan3").val(); 
    	}    	
        $.ajax({        	
            url : "../audit/listIpAddressesByNetWork.action",
            type:'POST',
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType:'json',
            data : {jsonStr:$.toJSON({eVlanId:eVlanId}), resourcePoolsId:$("#poollist").val()},
            async : false,
            success : function(items) {
            	for(var m=0; m<9; m++){
            		$("#vmbuyIPaddress" + m + " #buyvmIP3").html("");
            	}
                if (items != null && items.count > 0 && items.returnList!=null) {
                    var ipList = items.returnList;
                    $(ipList).each(function(i){
                    	for(var k=0; k<9; k++){
                    		$("#vmbuyIPaddress" + k + " #buyvmIP3").append("<option value="+ipList[i].ipaddress+" >"+ipList[i].ipaddress+"</option>");
                    	}
                    });
                }
            }            
        });		    	
    },     
    
	buyvmIP4Add: function (eVlanId){    	
    	if(eVlanId==0){
    		eVlanId=$("#buyvmvlan4").val(); 
    	}    	
        $.ajax({        	
            url : "../audit/listIpAddressesByNetWork.action",
            type:'POST',
            contentType : "application/x-www-form-urlencoded; charset=utf-8",
            dataType:'json',
            data : {jsonStr:$.toJSON({eVlanId:eVlanId}), resourcePoolsId:$("#poollist").val()},
            async : false,
            success : function(items) {
            	for(var m=0; m<9; m++){
            		$("#vmbuyIPaddress" + m + " #buyvmIP4").html("");
            	}
                if (items != null && items.count > 0 && items.returnList!=null) {
                    var ipList = items.returnList;
                    $(ipList).each(function(i){
                    	for(var k=0; k<9; k++){
                    		$("#vmbuyIPaddress" + k + " #buyvmIP4").append("<option value="+ipList[i].ipaddress+" >"+ipList[i].ipaddress+"</option>");
                    	}
                    });
                }
            }            
        });		    	
    }, 
    
    onChangeVlan: function(vlanId){
    	if(vlanId == '-1'){
    		return;
    	}
    	 
    	$("#vmip").html(vlanId+"个");
    	    	
    	for (var j=0; j<9; j++) {
        	for(var i=1;i<5;i++){	
        		$("#vmbuyIPaddress"+j+" #vlan"+i).hide();
        		$("#vmbuyIPaddress"+j+" #ipaddress"+i).hide();
        	}    	    	     
        	for(var i=0;i<vlanId;i++){	
        		$("#vmbuyIPaddress"+j+" #vlan"+Number(i+1)).show();
        		$("#vmbuyIPaddress"+j+" #ipaddress"+Number(i+1)).show();
        	}    		    		
    	}    	 
    },	
    	
    pagefirst:function (){
		var vmbuyIPpage = Number($("#vmbuyIPpage").val());
		var vmbuy_num = $("#vmbuynum").val(); 				 
	 	for(var i=0;i<10;i++){
	 		$("#vmbuyIPaddress" + i).hide();
	 	}		
	 	$("#vmbuyIPaddress" + Number(0)).show();
	 	$("#vmbuyIPpage").val(1);
	 	$("#pageNum").html(1 + "/" + vmbuy_num);	
	 	$("#vmNum").html(1);
	},

	pageend:function (){
		var vmbuyIPpage = Number($("#vmbuyIPpage").val());
		var vmbuy_num = $("#vmbuynum").val(); 		
		if (vmbuy_num > vmbuyIPpage){
	 		for(var i=0;i<10;i++){
	 			$("#vmbuyIPaddress" + i).hide();
	 		}		
	 		$("#vmbuyIPaddress" + Number(vmbuy_num-1)).show();
	 		$("#vmbuyIPpage").val(vmbuy_num);
	 		$("#pageNum").html(vmbuy_num + "/" + vmbuy_num);
	 		$("#vmNum").html(vmbuy_num);
		}		 
	},	
	
	pagedown:function (){
		var vmbuyIPpage = Number($("#vmbuyIPpage").val());
		var vmbuy_num = $("#vmbuynum").val();		  		
		if (vmbuy_num > vmbuyIPpage){
	 		for(var i=0;i<10;i++){
	 			$("#vmbuyIPaddress" + i).hide();
	 		}		
	 		$("#vmbuyIPaddress" + Number(vmbuyIPpage)).show();
	 		$("#vmbuyIPpage").val(vmbuyIPpage+1);
	 		$("#pageNum").html(vmbuyIPpage+1 + "/" + vmbuy_num);
	 		$("#vmNum").html(vmbuyIPpage+1);
		}		 
	},

	pageup:function (){
		var vmbuyIPpage = Number($("#vmbuyIPpage").val());
		var vmbuy_num = $("#vmbuynum").val();
		if(vmbuyIPpage>1){
	 		for(var i=0;i<10;i++){
	 			$("#vmbuyIPaddress" + i).hide();
	 		}
	 		$("#vmbuyIPaddress" + Number(vmbuyIPpage-2)).show();
	 		$("#vmbuyIPpage").val(vmbuyIPpage-1);
	 		$("#pageNum").html(vmbuyIPpage-1 + "/" + vmbuy_num);
	 		$("#vmNum").html(vmbuyIPpage-1);
		}
	},  	

    getNicsDhcpSwitch : function() {
		var nicsDhcpSwitch = -1;
		$.ajax({
			url: "/UCFCloudPortal/sysParameters/getNicsDhcpSwitch.action",
    		type: 'POST',
    		dataType: 'json',
    	 	async: false,
    		success: function(data) {
    			nicsDhcpSwitch = data;
    	    }
		});
		return nicsDhcpSwitch;
	},
  
	getCloudInfo : function() {
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
		return cloudInfo;
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
              //to fix bug [4785]
              if(data.cpuhz != undefined) {
                $("#vmcpu").html("0×" + data.cpuhz + "GHz");
              }
          }
          else {
            return;
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
	    	url : VMCustom.root+"/template/getVMTemplateById.action?type=1&id="+templateId,
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				VMCustom.vethAdaptorNum = json.vethAdaptorNum;
				VMCustom.template = json;
				$("#vmos").html(json.vmos);
				//模板暂时不支持录入硬盘个数与大小 --先注释
				//alert(json.storageSize);
				$("#vmstorage").append(json.storageSize);
				$("#vmstorage").append('GB');
				
				VMCustom.getAllResourcePools();
			}
		});
    },
    
    //查询资源池
    getAllResourcePools: function(){
    	$.ajax({
	    	url : VMCustom.root+"/resourcePools/listResourcePools.action",
			type : "GET",
			timeout : 5000,
			async:false,
			success : function(json) {
				if(json != null){
					 
					var objArray = json.list;
					//to fix bug [4770]
					if(objArray != null && objArray.length == 1){
						var poolId = -1;
						var poolName = '';
						$(objArray).each(function(i) {
							poolId = objArray[i].id;
							poolName = objArray[i].poolName;
							$("#poollist").append("<option value="+poolId+">"+poolName+"</option>");
						});
						$("#poollist option[value='-1']").remove(); 
						$("#singlePool").hide();
						VMCustom.onChangeResPool($("#poollist").val());
					}
					
					if(objArray != null && objArray.length > 1){
						var poolId = -1;
						var poolName = '';
						$(objArray).each(function(i) {
							poolId = objArray[i].id;
							poolName = objArray[i].poolName;
							$("#poollist").append("<option value="+poolId+">"+poolName+"</option>");
						});
					}
				}
			}
		});
    },
    
    initOtherParams : function(){		
    	var cpulist=document.getElementById("cpulist");
    	var memorylist=document.getElementById("memorylist");
    	var oslist=document.getElementById("oslist");
    	var bandWidthlist=document.getElementById("bandWidthlist");
    	var vlanlist=document.getElementById("vlanlist");
    	//to fix bug [7420]
    	cpulist.options[0].selected=true;
    	memorylist.options[0].selected=true;
    	oslist.options[0].selected=true;
    	bandWidthlist.options[0].selected=true;
    	vlanlist.options[0].selected=true;    
    	var cpu=$('#vmcpu').text();
    	var ocpu=cpu.split("×");
    	    	    	
    	$("#poollist option[value='-1']").remove();  
    	$("#memorylist option[value='-1']").remove();  
    	$("#oslist option[value='-1']").remove();  
    	$("#bandWidthlist option[value='-1']").remove();  
    	$("#vlanlist option[value='-1']").remove();  
    	$("#cpulist option[value='-1']").remove();    
    	
    	//to fix bug [7816]
    	$('#vmcpu').html(cpulist.options[0].value+"×"+ocpu[1]);       	      	
    	$('#vmmemory').html(memorylist.options[0].value/1024+"GB");    	    	
    	$("#vmos").html(oslist.options[0].text);    	
    	$("#bandWidth").html(bandWidthlist.options[0].text);    	
    	$("#vmip").html(vlanlist.options[0].text);    	
    	
    }, 
    
    initStoreTypeParams : function(){
    	var resourcePoolsId = $("#poollist").val();
//    	var oslist=document.getElementById("oslist");
//    	var osId = oslist.options[1].value;
    	var osId = $("#oslist").val();
    	$("#storagelist").html('');
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
	               if(data != undefined) {
	                   var storetypeArray = $.evalJSON(data);
	                   $(storetypeArray).each(function(i) {
		                   if(i == 0) {
		                     $("#storagelist").append("<option value='" + storetypeArray[i].value + "' selected>" + storetypeArray[i].text + "</option>");
		                   }
		                   else {
		                     $("#storagelist").append("<option value='" + storetypeArray[i].value + "'>" + storetypeArray[i].text + "</option>");
		                   }
	                   });
	               }             	
	             }
	           }
            });
        }    	
    },    
    
    initOsParams : function(resourcePoolId){
 	    $("#vmos").html("");
  	   $("#oslist").html("");
  	    $("#oslist").append("<option value='-1'>请选择</option>");
  	    
        if(resourcePoolId != undefined) {
        	
        var zoneId = $("#zonelist").get(0).options[0].value;

        $.ajax({
           url : "../template/getOsesByResourcePoolId.action?resourcePoolsId="+resourcePoolId+"&zoneId="+zoneId,
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
                     $("#vmos").html(osArray[i].text);                     
                   } else {
                     $("#oslist").append("<option value='" + osArray[i].value + "'>" + osArray[i].text + "</option>");
                   }
                 });
               }

             }
           }
         });
       }  	
    },    
    
    ChangeZone : function(zoneId){
  	    
	    var poolId = $(".VirtualMachine #poollist").val();
	    $("#oslist").html(""); 
	    $("#storagelist").html("");
        $.ajax({
           url : "../template/getOsesByResourcePoolId.action?resourcePoolsId="+poolId+"&zoneId="+zoneId,
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
                       $("#vmos").html(osArray[i].text);                     
                   } else {                	  
                       $("#oslist").append("<option value='" + osArray[i].value + "'>" + osArray[i].text + "</option>");
                   }
                 });
               }

             }
           }
         });
        VMCustom.initOsParams(poolId); 
	    VMCustom.initStoreTypeParams(); 
	    VMCustom.initOtherParams();
        	
    },
    
    initZoneParams : function(resourcePoolId){
 	   $("#zone").html("");
        if(resourcePoolId != undefined) {
         $.ajax({
           url : "../template/getZonesByResourcePoolId.action?resourcePoolsId="+resourcePoolId,
           type : "POST",
           async : false,
           dataType : "json",
           success : function(data) {
             if(data.indexOf("error") == 0) {
               dialog_confirmation('#dialog_confirmation', "失败：初始化资源域下拉列表失败，"+data);
               return;
             } else {
               if(data != undefined) {
                 var zoneArray = $.evalJSON(data);
                                  
                 //to fix bug [7420]
                 if(zoneArray.length==1){
                	 $("#singleZone").hide();
                 } else {
                	 $("#singleZone").show();
                 }
                 
                 $(zoneArray).each(function(i) {
                   if(i == 0) {
                     $("#zonelist").append("<option value='" + zoneArray[i].id + "' selected>" + zoneArray[i].name + "</option>");
                   }
                   else {
                     $("#zonelist").append("<option value='" + zoneArray[i].id + "'>" + zoneArray[i].name + "</option>");
                   }
                 });
               }
             }
           }
         });
       }  	
    },
    
    onChangeCpu: function(num){
    	if(num == '-1'){
    		return;
    	}
    	var cpu=$('#vmcpu').text();
    	var ocpu=cpu.split("×");
    	$('#vmcpu').html(num+"×"+ocpu[1]);    	
    },
    
    onChangeMem: function(num){
    	if(num == '-1'){
    		return;
    	}
    	$('#vmmemory').html("");    
    	$('#vmmemory').html(num/1024+"GB");    	
    },
    
    onChangeOS: function(os){
    	if(os == '-1'){
    		return;
    	}
    	//to fix bug [7627]
    	$('#vmos').html("");    
    	$('#vmos').html($("#oslist").find("option:selected").text());    
    	//fix bug:8052
    	var poolId = $(".VirtualMachine #poollist").val();
    	VMCustom.initStoreTypeParams();
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
    	VMCustom.getPhysicalHostVlanByZoneId(resourcePoolId,zoneId);
    },
    
    onChangebandWidth: function(bandWidth){
    	if(bandWidth == '-1'){
    		return;
    	}
    	$("#bandWidth").html();
    	$("#bandWidth").html(bandWidth+"Mbps");    	
    },
    
  
    
    //查询可用VLAN
    getPhysicalHostVlanByZoneId: function(resourcePoolId,zoneId){
		$("#vlanlist").html("");
		$("#vlanlist").html("<option value=\"-1\">请选择</option>");
		$("#iplist").html("");
		$("#iplist").html("<option value=\"\">请选择</option>");
    	$.ajax({
	    	url : VMCustom.root+"/resourcesUsed/pmList!listHostVlan.action?resourcePoolId="+resourcePoolId+"&zoneId="+zoneId,
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
 
//检查服务名称，不能与现有已经被使用服务名称重名
    //to fix bug [4986]
    checkProductRename : function(name){
		var count = 0;		
		var data = {
			name: name,
			type: 1
		};		
		$.ajax({
			url : VMCustom.root+"/product/checkProductRename.action",
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
	
	checkIp4VlanIpRange: function (vlan,ipAddress,jsonvlan){
        var return_val = false;
        $(jsonvlan).each(function(i){
		    if (vlan==jsonvlan[i].id){
			    if (jsonvlan[i].listVlanIpRange != null && jsonvlan[i].listVlanIpRange.length >0) {
					var len = jsonvlan[i].listVlanIpRange.length;
					var iprangeitems= jsonvlan[i].listVlanIpRange;
					for(var j=0;j<len;j++){
						if (return_val==false){
							var startip = iprangeitems[j].startip
							var endip = iprangeitems[j].endip;
							var a = startip.match(/\d+\.\d+\.\d+/)+"";
							var b = ipAddress.match(/\d+\.\d+\.\d+/)+"";
							if (a == b){
							    return_val = VMCustom.getIp4(startip,endip,ipAddress);
							}
						}
					}
				}
		    }
		});
		return return_val;
    },
     
	getIp4: function (ipstart,ipend,ip){ 
			//var reg = /^(\d{4})(-)(\d{2})\2(\d{2})$/; 
			var ip4 = /(\d+)\.(\d+)\.(\d+)\.(\d+)/;
			var r1 = ipstart.match(ip4); 
			var r2 = ipend.match(ip4);
			var r3 = ip.match(ip4);
			var startip = parseInt(r1[4]);
			var endip = parseInt(r2[4]);
			var myip = parseInt(r3[4]);				
			if (myip>=startip && myip<=endip){
			   return true;
			}
			return false; 
	},

    buyVMCustom : function(para){
	    //to fix bug [7037]
	    if(commonUtils.len($(".VirtualMachine #instanceName").val()) == 0) {
	      alert("请输入服务名称");
	      $(".VirtualMachine #instanceName").focus();
	      return;
	    }
	    if(commonUtils.len($(".VirtualMachine #instanceName").val()) > 20) {
	      alert("服务名称不能超过20个字符（一个汉字算2个字符）");
	      $("#instanceName").focus();
	      return;
	    }
	    if(! commonUtils.scoreReg.exec($(".VirtualMachine #instanceName").val())) {
	      alert("名称只能包含"+commonUtils.regMessage);
	      $(".VirtualMachine #instanceName").focus();
	      return;
	    }	    	   
	    
	    var resname = VMCustom.checkProductRename($(".VirtualMachine #instanceName").val());
	    if(resname == "1"){
			alert("该服务名称已经被使用");
			$(".VirtualMachine #instanceName").focus();
            return;
  	    }
	    
	    if(resname == "2"){
			alert("该服务名称已经被使用");
			$(".VirtualMachine #instanceName").focus();
            return;
  	    }	  
	    
	    if(resname == "3"){
			alert("该服务名称已经被使用");
			$(".VirtualMachine #instanceName").focus();
            return;
  	    }		    
	    
    	if(VMCustom.checkRename($(".VirtualMachine #instanceName").val())){
			alert("该服务名称与购物车中有重名");
			$(".VirtualMachine #instanceName").focus();
            return;
  	    }
    	//资源池poollist
    	if(Number($(".VirtualMachine #poollist").val()) <= 0){
		      alert("请选择资源池");
		      $(".VirtualMachine #poollist").focus();
		      return;
    	}

    	if(Number($(".VirtualMachine #oslist").val()) <= 0){
		      alert("请选择操作系统");
		      $(".VirtualMachine #oslist").focus();
		      return;
    	}
    	//资源域zonelist
    	if(Number($(".VirtualMachine #zonelist").val()) <= 0){
		      alert("请选择资源域");
		      $(".VirtualMachine #zonelist").focus();
		      return;
    	}

	    
	    if($(".VirtualMachine #instanceDesc").val().length > 100) {
	      alert("描述不能超过100个字符");
	      $(".VirtualMachine #instanceDesc").focus();
	      return;
	    }
	    var buyperiod = $(".VirtualMachine #buyperiod").val();
	    if(commonUtils.len(buyperiod) == 0) {
		      alert("请输入申请时长");
		      $(".VirtualMachine #buyperiod").focus();
		      return;
		}
	    if(!isDigit(buyperiod)){
		      alert("申请时长必须为大于0的正整数");
		      $(".VirtualMachine #buyperiod").focus();
		      return;
	    }else{
	    	if(Number(buyperiod) <= 0){
			      alert("申请时长必须为大于0的正整数");
			      $(".VirtualMachine #buyperiod").focus();
			      return;
	    	}
	    }
	    
	    var poolId = $(".VirtualMachine #poollist").val();
	    var zoneId = $(".VirtualMachine #zonelist").val();
	    var cpuId = $(".VirtualMachine #cpulist").val();
	    var memoryId = $(".VirtualMachine #memorylist").val();
	    var osId = $(".VirtualMachine #oslist").val();
	    var osText = $(".VirtualMachine #oslist").find("option:selected").text();
	    var storageId = $(".VirtualMachine #storagelist").val();
	    var vlanId = $(".VirtualMachine #vlanlist").val();
	    var vlanText = $(".VirtualMachine #vlanlist").find("option:selected").text();
	    var bandWidthId = $(".VirtualMachine #bandWidthlist").val();
	    
	    var vmbuynum = $(".VirtualMachine #vmbuynum").val();
	    var instanceName = $(".VirtualMachine #instanceName").val();
	    var instanceDesc = $(".VirtualMachine #instanceDesc").val();
	    
		var singleIpaddress = {};	 
		var singleVlan = {};	 
		var singleJsonvlan = {};	 
		var singleVlanTxt = {};	 
	    
		
		
		
		
	    
		var vlans_1 = {};
		var vlans_2 = {};
		var vlans_3 = {};
		var vlans_4 = {};
		var ipaddress_1 = {};
		var ipaddress_2 = {};
		var ipaddress_3 = {};
		var ipaddress_4 = {};
	    
		for(var i=0;i<vmbuynum;i++){
			//to fix bug [7639]	 
			vlans_1[i] = $("#vmbuyIPaddress" + i + " .vlan1").val();
			vlans_2[i] = $("#vmbuyIPaddress" + i + " .vlan2").val();
			vlans_3[i] = $("#vmbuyIPaddress" + i + " .vlan3").val();
			vlans_4[i] = $("#vmbuyIPaddress" + i + " .vlan4").val();
			 
			ipaddress_1[i] = $("#vmbuyIPaddress" + i + " #buyvmIP1").val();
			ipaddress_2[i] = $("#vmbuyIPaddress" + i + " #buyvmIP2").val();
			ipaddress_3[i] = $("#vmbuyIPaddress" + i + " #buyvmIP3").val();
			ipaddress_4[i] = $("#vmbuyIPaddress" + i + " #buyvmIP4").val();
			
			//ipaddress_1[i] = $("#vmbuyIPaddress" + i + " #subipaddress1_1").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress1_2").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress1_3").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress1_4").val();
			//ipaddress_2[i] = $("#vmbuyIPaddress" + i + " #subipaddress2_1").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress2_2").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress2_3").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress2_4").val();
			//ipaddress_3[i] = $("#vmbuyIPaddress" + i + " #subipaddress3_1").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress3_2").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress3_3").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress3_4").val();
			//ipaddress_4[i] = $("#vmbuyIPaddress" + i + " #subipaddress4_1").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress4_2").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress4_3").val()+"."+$("#vmbuyIPaddress" + i + " #subipaddress4_4").val();
		}	
	        		

	    var pid = product.pro.id;
	    
	    main.porder.voorders.push({
	        id : pid,
	        num : 1,
	        productName : "",
	        type : 1,
	        typeId : 10,
	        usePeriod : 0
	    });
	  
		if(para == 1){
			while (main.porderNow.vminfos.length>0){    	
	    		for(var i = 0;i<main.porderNow.vminfos.length;i++){					
	    			main.porderNow.vminfos.removeAt(i, 1);
	    		}	
			}	    		
		}	    
	   
	    if(vmbuynum=="1"){
		    var vminfos = {
			    productId: pid,
			    productName: product.pro.name,
			    shopcartdesc:product.pro.description,
		        virtual: main.buildKey(),
		        flag : "vmService",
		        
				vlan : Number(vlans_1[0]),
				vlan2 : Number(vlans_2[0]),
				vlan3 : Number(vlans_3[0]),
				vlan4 : Number(vlans_4[0]),				
				ipAddress:$("#vmbuyIPaddress0 #buyvmIP1").val(),
				ipAddress2:$("#vmbuyIPaddress0 #buyvmIP2").val(),
				ipAddress3:$("#vmbuyIPaddress0 #buyvmIP3").val(),
				ipAddress4:$("#vmbuyIPaddress0 #buyvmIP4").val(),      
		        
		        description : instanceDesc,
		        instanceName : instanceName,
		        poolId : poolId,   
		      
		        zoneId : zoneId,	       
		        templateId : $(".VirtualMachine #templateId").val(),
		        templateType : parseInt(product.pro.type),
		        charge:0,
		        unit:product.pro.unit,
		        unitC:product.pro.unitC,
		        num:$(".VirtualMachine #buyperiod").val(),
		        period:$(".VirtualMachine #buyperiod").val(),
		      
		        cpuNum:cpuId,
		        memorySize:memoryId,
		        vmos:osText,
		        osId:osId,
		        vethAdaptorNum:vlanId,
		        storeType:storageId,
		        extendAttrJson:bandWidthId
		    };	
			if(para == 0){
	    		main.porder.vminfos.push(vminfos);
			}
			if(para == 1){
	    		main.porderNow.vminfos.push(vminfos);
			}		  
	    }else{
	    	for(var i=0;i<vmbuynum;i++){
			    var vminfos = {
				    productId: pid,
				    productName: product.pro.name,
				    shopcartdesc:product.pro.description,
				    virtual:main.buildKey() + Number(i+1),//to fix bug [4886] to fix bug [4878] 
			        flag : "vmService", 
			        description : instanceDesc,
			        instanceName : instanceName + "_" + Number(i+1),
			        poolId : poolId,   			      
			        zoneId : zoneId,
			        
	    			vlan : Number(vlans_1[i]),
	    			vlan2 : Number(vlans_2[i]),
	    			vlan3 : Number(vlans_3[i]),
	    			vlan4 : Number(vlans_4[i]),		    			
	    			ipAddress:ipaddress_1[i],
	    			ipAddress2:ipaddress_2[i],
	    			ipAddress3:ipaddress_3[i],
	    			ipAddress4:ipaddress_4[i],	
			        
			        templateId : $(".VirtualMachine #templateId").val(),
			        templateType : parseInt(product.pro.type),
			        //to fix bug [4875]
			        //charge:pros[productId].price, 原来是这么写的
			        charge:0,
			        unit:product.pro.unit,
			        unitC:product.pro.unitC,
			        num:$(".VirtualMachine #buyperiod").val(),
			        period:$(".VirtualMachine #buyperiod").val(),
			      
			        cpuNum:cpuId,
			        memorySize:memoryId,
			        vmos:osText,
			        osId:osId,
			        vethAdaptorNum:vlanId,
			        storeType:storageId,
			        extendAttrJson:bandWidthId
			    };	
				if(para == 0){
		    		main.porder.vminfos.push(vminfos);
				}
				if(para == 1){
		    		main.porderNow.vminfos.push(vminfos);
				}	
	    		
	    	}
	    	
	    }

		if(para == 0){
			main.setShopCartCookie();
			javascript:window.location.href='shoppingcart.jsp';
		}
		if(para == 1){
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
  
};