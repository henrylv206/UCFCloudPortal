var ipsan = {
	strCloud: "",
	root:"",
    init: function(productId){
        var solf = this;        

        solf.cloudStr = solf.getCloudInfo(); 
        
		if(solf.true){
			$("#BuyIpsanTop").hide();
			$(".mysearch").hide();
			$(".nav").hide();
			$(".footer").hide();
			$(".active01").hide();
			$(".active02").hide();
			$("#serviceMenu").show();			
			//fix bug:7475
			$(".price").hide();
		}
        
        ipsan.findRelativeProduct(productId);//相关产品初始化
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
 
    setTab: function (name,cursel,n){
		for(i=1;i<=n;i++){
		  var menu=document.getElementById(name+i);
		  var con=document.getElementById("con_"+name+"_"+i);
		  menu.className=i==cursel?"current":"";
		  con.style.display=i==cursel?"block":"none";
		 }
	},

    buildKey: function (){
	    var myDate = new Date();
	    var str = "" + (20+myDate.getHours()) + myDate.getMinutes() +
	    myDate.getSeconds() +
	    myDate.getMilliseconds();
	    return Number(str);
	},

	ipsanbuy: function (para){
		if(para == 1){
			ipsan.clearShopCar();	
		}		
		
		  var productId = $(".ipsan #ipsanProductId").val();			  
			var templateId = $(".ipsan #ipsanTemplateId").val();
		    main.porder.voorders.push({
		        id : parseInt(templateId),
		        num : 1,
		        productName : "",
		        type : 12,
		        typeId : 12,
		        usePeriod : 0
		      });
			var buyCycle = $(".ipsan #ipsan_buyCycle").val();	
			var description=$("#description").val();
			var ipsan_instanceName=$("#ipsan_instanceName").val();
			//to fix bug:3779
		    if(commonUtils.len(ipsan_instanceName) == 0) {
		      alert("请输入服务名称");
		      $("#ipsan_instanceName").focus();
		      return;
		    }
		    if(commonUtils.len(ipsan_instanceName) > 20) {
		      alert("服务名称不能超过20个字符（一个汉字算2个字符）");
		      $("#ipsan_instanceName").focus();
		      return;
		    }
		    if(! commonUtils.scoreReg.exec(ipsan_instanceName)) {
		      alert("服务名称只能包含"+commonUtils.regMessage);
		      $("#ipsan_instanceName").focus();
		      return;
		    }
		    //fix bug 3865
		    var resname = ipsan.checkProductRename(ipsan_instanceName);
		    if(resname == "1"){
				alert("该服务名称已经被使用");
				$("#ipsan_instanceName").focus();
	            return;
	  	    }
		    
	    	if(ipsan.checkRename(ipsan_instanceName)){
				alert("该服务名称与购物车中有重名");
				$("#ipsan_instanceName").focus();
	            return;
	  	    }
		    //if(commonUtils.len(description) == 0) {
		    //  alert("请输入描述");
		    //  $("#description").focus();
		    //  return;
		    //}
		    //to fix bug 3892
		    if(description.length > 100) {
		      alert("描述不能超过100个字符");
		      $("#description").focus();
		      return;
		    }
		    //if(! scoreReg.exec(description)) {
		    //  alert("描述只能包含中文、字母、数字、下划线及连接符");
		    //  $("description").focus();
		    //  return;
		    //}
			//to fix bug:3781
			var ex = /^\d+$/;
	    	if (ex.test(buyCycle)) {    		
	    	} else {
	    		alert("请填写正确申请时长！");
				$(".ipsan #ipsan_buyCycle").focus();
				return;
	    	}
	 
		  var ipsaninfos = {
	      productId: productId ,
	      virtual: main.buildKey(),
	      flag : "ipsanService",
	      description : description,
	      instanceName : ipsan_instanceName,
	      templateId : parseInt(templateId),
	      templateType : 12,
	      charge:pros[pid].price,
	      zoneName:"",
	      diskSize:$("#ipsanStorageSize").html(),
	      productName:$("#ipsanTemplateName").val(),
	      num:1,
	      unit:pros[pid].unit,
	      period:buyCycle
		  };
		  if(para == 0){
			  	main.porder.vminfos.push(ipsaninfos);
			  	main.setShopCartCookie();
	    		javascript:window.location.href='shoppingcart.jsp';
	    	}      
	    	if(para == 1){
	    		main.porderNow.vminfos.push(ipsaninfos);	
	    		main.setNowShopCartCookie();	
	    		javascript:window.location.href='submitorderNow.jsp';
	    	} 
	},

	
	getVMTemplateById: function(templateId){
		$.ajax({
	    	url : "/UCFCloudPortal/template/getVMTemplateById.action?type=1&id="+templateId,
				type : "GET",
				timeout : 5000,
				async:false,
				success : function(json) {
					vethAdaptorNum = json.vethAdaptorNum;
					template[templateId] = json;
					$("#ipsanStorageSize").html(json.storageSize + "GB");
					$(".ipsan #ipsanTemplateType").val(json.type);
					$(".ipsan #ipsanTemplateId").val(json.id);
					$(".ipsan #ipsanTemplateName").val(json.templateDesc);
					var raidValue = "";
					if(json!=null && json.extendAttrJSON!=null){
						//to fix bug:7001
						var raid =json.extendAttrJSON;
						if (raid!=null && raid!=""){
							//fix bug:7475
							raidValue = ipsan.queryRaidType(raid,json.resourcePoolsId);
						}
					}
					$(".ipsan #raid").html(raidValue);

				}
			});
	},
	
	queryRaidType : function(raid,resourcePoolsId) {
		var raidValue = "";
		$.ajax({
			url: "/UCFCloudPortal/vdc_volumemanage/queryRaidType.action?resourcePoolsId="+resourcePoolsId,
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				if (data!=null && data.length>0){
					var ipsaninfo = data;
					for(var j=0;j<ipsaninfo.length;j++){
						if (raid == ipsaninfo[j].raid){
							raidValue = ipsaninfo[j].raidValue;
						}
					}
				}
			}
		});
		return raidValue;
	},
	
	//fix bug 3777 显示相关产品
	findRelativeProduct:function (){//获取相关产品列表信息
    	var dom = '';
		$.ajax({
		url : ipsan.root+"/product/findRelativeProduct.action?type=12",//查询块存储相关产品
		data : null,
		dataType : "json",
		success : function(data){
		        if(data != null) {
		          if(typeof(data) == "string" && data.indexOf("error") == 0) {
		            return -1;
		          }else {
		        	  var array = data.listResp.list;
		        	  if(array == null || array.length == 0) {						            	
		                return;
		              }
		              else {
		                $(array).each(function(i) {  
		                	if(array[i].id>0){
		                		dom +='<li><div class="picArea"><a href="buyipsan.jsp?id='+array[i].id+'" title="'+array[i].name+'"><img src="../images/products/vdisk124.png" /></a></div></li>';
		                	}else if(array[i].id<0){
		                		dom +='<li><div class="picArea"><a href="buyipsanCustom.jsp?id='+array[i].id+'" title="'+array[i].name+'"><img src="../images/products/vdisk124.png" /></a></div></li>';
		                	} 
		                } );
		                $(".ulList03").html(dom);
		              }
			      }
		       } 
        	}

        });
    },
//检查服务名称，不能与现有已经被使用服务名称重名
    checkProductRename : function(name){
		var count = 0;
		//type=10代表物理机，name为实例名称
		var data = {
			name: name,
			type: 12
		};		
		$.ajax({
			url : ipsan.root+"/product/checkProductRename.action",
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
	
	clearShopCar: function(){
		while (main.porderNow.vminfos.length>0){
    		for(var i = 0;i<main.porderNow.vminfos.length;i++){
    			main.porderNow.vminfos.removeAt(i, 1);
    		}
		}
	}
	
};
