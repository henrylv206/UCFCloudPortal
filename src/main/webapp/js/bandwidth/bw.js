var bw={
	root:"/UCFCloudPortal",
	bwUsedType:0,
	init:function(resourcePoolsId,zoneId){
        var solf = this;
        
        var cloudStr = solf.getCloudInfo(); 
        bw.bwUsedType = bw.getBWUsedType();         
		if(true){
			$("#BuyBwTop").hide();
			$(".mysearch").hide();
			$(".nav").hide();
			$(".footer").hide();
			$(".active01").hide();
			$(".active02").hide();
			$("#serviceMenu").show();
			$("#toshopcart").hide();
			$("#price").hide();
			$("#price2").hide();
		}	
		if(2==bw.bwUsedType){
			$("#ipaddress_tr").hide();
		}
        
                var d = true;
                	if(product.pro != undefined) {
                		$("#bwname").html(product.pro.name);
                		$("#con_tab_1").html(product.pro.specification);
                		$("#bwvalue").html(product.pro.template.extendAttrJSON);
                		$("#bwprice").html(product.pro.price+"元/"+product.pro.unitC);
                		//$(".cycle").html(product.pro.price+"元/"+product.pro.unitC);
                		$(".cycle").html("&nbsp;"+product.pro.unitC);
                    }
                	
                    if (d) {
                    	//fix bug 6774
                        $("#instanceName").val("BW" + main.buildKey());                       
                        if(2!=bw.bwUsedType){
                        	//需要根据resourcePoolsId和zoneId查询属于同一资源池和资源域的依赖资源-公网ip ！后台已去掉
                            bw.list_ce_publicIp(resourcePoolsId,zoneId);
                            //此段代码为没有可用公网ip时做回退用
                        	bw.chckPublicIp();
                        }                        
                        //查询相关的服务
//                        /bw.findRelativeProduct();
                        $("#toshopcart").click(function() {
                             bw.apply(0);
                        });                       
                        solf.validate();                        
                        d=false;                       
                    }		
	},
	
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
	//此方法没有用到，是否需要查询资源域？？？？？？？？？？？？？？？？？？？？？？？？？
    list_zones:function(templateId){
		    	$.ajax({
					url : "/UCFCloudPortal/resourcesUsed/getBandWidthProductList.action?id="+templateId,
					data : null,
					async : false,
					dataType : "json",
					success : function(json) {
						var dom = "";
						for ( var i = 0; i < json.length; i++) {
							dom += '<option value="' + json[i].id + '">'
									+ json[i].name + '</option>';
						}
						$(".zoneId").html(dom);
					}
				});
	},
	//检查是否有可用tip
    chckPublicIp : function(){
        var flag=0;
       $.ajax({
             url : "/UCFCloudPortal/resourcesUsed/findUsableIpInstance2.action",
             data : null,
             async : false,
             dataType : "json",
             success : function(json) {
                  var dom = "";
                  var size = 0;
                  for (var i = 0; i < json.ipInstanceLists.length; i++) {
                       //alert(json.ipInstanceLists[i].ipAddress);
                       //to fix bug:3812
                    if(bw.checkResIPAddress(json.ipInstanceLists[i].ipAddress)==0){
                    	size++;//bug 0003905
                    }
                  }
                  if(size==0){
                    alert("目前没有可用的公网IP");
                    if(type=='mall'){
                        window.location.href='/UCFCloudPortal/jsp/cloud_mall.jsp';           
                    }else if(type=='item'){
                    	//此处surparentId=4&parentId=15写死有风险，因为带宽所在的服务目录不是绝对不变的
                        window.location.href='/UCFCloudPortal/jsp/mallthirdlist.jsp?surparentId=4&parentId=15&secitemcode=bw&fromSec=1';
                    }else{
                        window.location.href='/UCFCloudPortal/index.jsp';
                    }
                  }
           }
       });
    },
    //支持多资源池，查询与带宽在同一资源池和资源域的可用的公网ip ！后台已去掉resourcePoolsId,zoneId
   list_ce_publicIp:function(resourcePoolsId,zoneId){
   	    var flag=0;
	   $.ajax({
             url : "/UCFCloudPortal/resourcesUsed/findUsableIpInstance2.action",
			 data : {
			    	  "resourcePoolsId" : resourcePoolsId,
			    	  "zoneId" : zoneId
			 		},
             async : false,
             dataType : "json",
             success : function(json) {
                  var dom = "";
                   for (var i = 0; i < json.ipInstanceLists.length; i++) {
                	   //to fix bug:3812
                   	if(bw.checkResIPAddress(json.ipInstanceLists[i].ipAddress)==0){
                        dom += '<option value="'
                                              + json.ipInstanceLists[i].id + '">'
                                              + json.ipInstanceLists[i].ipAddress
                                              + '</option>';
    	 			}
                   }
                       $("#ipaddress").html(dom);
                       //$("#ipaddress").val(callback);
                   }
       });
  },
  
	//获取产品库存
	getProductBuyCountById: function(id){
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
	checkVPN:function(para,callback1,callback2) {
	  $.ajax({
			type : "POST",
		      url : bw.root+"/portal_lb/checkVPN.action",
		      datatype : "json", //设置获取的数据类型为json
		      data : {},
		      success : function(data) {
		    	  if(data=="true"&&callback1){
		    		  callback1();
		    	  }	
		    	  else if(data=="false"&&callback2){
		    		 callback2();
		    	  }
		      }
		  });
	  },
	apply:function(para){
		//暂时注释,审核时检查Vlan update by CQ fix bug 4975
//		bw.checkVPN(para, function(){
//			if(2==bw.bwUsedType){
//				bw.checkBW(para, function(){
//					bw.submit(para);
//				},function(){
//					alert("抱歉，您已申请带宽服务，不可以再申请同类服务");
//					return;
//				});
//			}
//			else bw.submit(para);			    		
//    	}, function(){
//    		alert("请先申请VLAN。");
//    		return;
//    	});  
		if(2==bw.bwUsedType){
			bw.checkBW(para, function(){
				bw.submit(para);
			},function(){
				alert("抱歉，您已申请带宽服务，不可以再申请同类服务");
				return;
			});
		}
		else bw.submit(para);	
	} ,
	checkBW:function(para,callback1,callback2){		
		$.ajax({
			type : "POST",
		      url : bw.root+"/resourcesUsed/checkBWInstance.action",
		      datatype : "json", //设置获取的数据类型为json
		      data : {},
		      success : function(data) {
		    	  if(data==0){
		    		  var flag = bw.checkPorder(8);	
		    		  if(flag){
		    			  alert("每个用户只能申请一个带宽服务，该服务已经放入购物车");
		    			  return;
		    		  }
		    		  else callback1();
		    	  }	
		    	  else if(data>0&&callback2){
		    		 callback2();
		    	  }
		      }
		  });
	},
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
	},
	//购买带宽的条件：1.申请了虚机或者虚服务，2.购买了公网ip,3.购买的公网ip没有与其它带宽绑定
    submit:function(para){
    	if(2!=bw.bwUsedType&&$("#ipaddress").val() == null  ){
			//to fix bug 2528
			alert("您尚未申请公网IP，不能申请公网带宽资源。");
			//alert("没有可用的IP地址");
			return;
		}
	  //to fix bug 3668
        var _period = commonUtils.trim($("#buyperiod").val());
        if(commonUtils.len(_period) == 0) {
            alert("请输入申请周期");
          $("#buyperiod").focus();
            return;
        }

        if(commonUtils.len($("#instanceName").val()) == 0) {
          alert("请输入服务名称");
          $("#instanceName").focus();
          return;
        }
        if(commonUtils.len($("#instanceName").val()) > 20) {
          alert("服务名称不能超过20个字符（一个汉字算2个字符）");
          $("#instanceName").focus();
          return;
        }
        if(! commonUtils.scoreReg.exec($("#instanceName").val())) {
          alert("服务名称只能包含"+commonUtils.regMessage);
          $("#instanceName").focus();
          return;
        }
        
		var pid = product.pro.id;
    	//库存余额判断
    	var buyCount = bw.getProductBuyCountById(pid);
    	var quotaNum = product.pro.quotaNum;
    	if(quotaNum <= buyCount){
    		alert("服务申请已达到上限，请稍后再试");
    		return;
    	};

		//to fix bug:2641
    	if(2!=bw.bwUsedType){
    		for(var i=0;i<main.porder.vminfos.length;i++){
	            if($("#ipaddress").val()==main.porder.vminfos[i].publicipId && product.pro.type==main.porder.vminfos[i].templateType){
                      alert("购物车中已经存在该公网IP下申请的公网带宽，一个公网IP只能买一个公网带宽!");
                      return;
	            }                    
          }
    	}		
		if(bw.checkResname(Number($("#ipaddress").val()), pid, $("#instanceName").val())==1){
            alert("公网带宽名称 " + $("#instanceName").val() + " 已经存在可用的重名同类服务，请重新填写");
            $("#instanceName").focus();
            return;
		}	
        if(bw.checkResname(Number($("#ipaddress").val()), pid, $("#instanceName").val())==2){
            alert("公网带宽名称 " + $("#instanceName").val() + " 已经在购物车中有重名同类服务，请重新填写");
            $("#instanceName").focus();
            return;
        }
		$("#instanceName").parent().next().children().html("");
		if (!bw.validate.validateValue.val()) {
				return;
		}  	 	
        if($("#instanceDesc").val().length > 100) {
          alert("描述不能超过100个字符");
          $("#instanceDesc").focus();
          return;
        }	
		
		//if(bw.checkResIPAddress()!=0){
		//	alert("您已在该IP地址下开通公网带宽业务");
		//	return;
		//}
		
        main.porder.voorders.push({
                                id : pid,
                                num : 1,
                                productName : "",
                                type : 8,
                                typeId : 8,
                                usePeriod : 0
                            });
		if(para == 1){
			while (main.porderNow.vminfos.length>0){    	
	    		for(var i = 0;i<main.porderNow.vminfos.length;i++){					
	    			main.porderNow.vminfos.removeAt(i, 1);
	    		}	
			}	    		
		}		
		var ipAddress = $("#ipaddress option:selected").html();
		var publicipId = $("#ipaddress").val();
		if(2==bw.bwUsedType){
			ipAddress = "";
			publicipId = 0;
		}
                            
        var vminfos = {
        		productName:product.pro.name,
                charge:product.pro.price,
                productId: pid,
                instanceName: $("#instanceName").val(),
                virtual: main.buildKey(),
                period: $("#buyperiod").val(),
                publicipId:publicipId,
                ipAddress:ipAddress,
                description:$("#instanceDesc").val(),
                productName : product.pro.name,
                templateId:product.pro.templateId,
                templateType:product.pro.type,
                ipInstanceId:publicipId,
                periodInfo:$("#buyperiod").val()+""+product.pro.unit,
                unitString: $("#bwprice").html(),
                unit:product.pro.unit
            };       	       

                         
		if(para == 0){
			 main.porder.vminfos.push(vminfos);
			 main.setShopCartCookie();
			 javascript:window.location.href='shoppingcart.jsp';
		}
		if(para == 1){
			 main.porderNow.vminfos.push(vminfos);
			 main.setNowShopCartCookie();
			 javascript:window.location.href='submitorderNow.jsp';
		}

    },
    checkResname:function(zid,pid,resname){//bug 0003854
    	var flag;
		  $.ajax({
				url : "../order/PortalOrder_showInstanceInfoNameExists.action",
				type:'POST',
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				dataType:'json',
				data : {"searchStr":resname},
				async : false,
				success : function(data) {
	            	if(data=='1'){
	            		flag=1;
	            	}
	            	for (var i = 0; i < main.porder.vminfos.length; i++) {
	            		if (main.porder.vminfos[i].templateType== 8
	                            && main.porder.vminfos[i].instanceName==resname){
	                                 flag=2;                                        
	                    		}	
	            	}
				}
			});
    	return flag;
    },
    checkResIPAddress:function(ipaddress){
    	var flag;
    		  $.ajax({
    			  url : "/UCFCloudPortal/resourcesUsed/findIPAddressExist.action",
    				type:'POST',
    				contentType : "application/x-www-form-urlencoded; charset=utf-8",
    				dataType:'json',
    				data : {"ipAddress":ipaddress},
    				async : false,
    				success : function(data) {
                    	if(data=='-1'){
                    		flag=-1;
                    	}else if(data==0){
                    		flag=0;
                    	}else{
                    		flag=1;
                    	}
    				}
    			});
    	return flag;
    },
    validate: function(){
        var solf = this;
        valdata = {
            "0": "不能为空",
            "1": "内容过长",
            "2": "格式错误",
            "3": "名称重复"
        };
        solf.validate.validateValue = {
            resName: function(that){
                that = that|| "#instanceName";
                var v = $(that).val();
                if (commonUtils.len(v) < 1) {
                    $(that).parent().next().children().html(valdata["0"]).css("color","red");
                    return false;
                }
                if (commonUtils.len(v) > 32) {
                    $(that).parent().next().children().html(valdata["1"]).css("color","red");
                    return false;
                }
                if (!/^[\u4E00-\u9FA5a-zA-Z0-9_-]+$/.test(v)) {
                    $(that).parent().next().children().html(valdata["2"]).css("color","red");
                    return false;
                }
                $(that).parent().next().children().html("");
                return true;
            },resDesc: function(that){
                that = that|| "#instanceDesc";
                var v = $(that).val();
                //if (commonUtils.len(v) < 1) {
                //    $(that).parent().next().children().html(valdata["0"]).css("color","red");
                //    return false;
                //}
                //to fix bug 3889
                if (v.length > 100) {
                    $(that).parent().next().children().html(valdata["1"]).css("color","red");
                    return false;
                }
                //if (!/^[\u4E00-\u9FA5a-zA-Z0-9_-]+$/.test(v)) {
                //    $(that).parent().next().children().html(valdata["2"]).css("color","red");
                //    return false;
                //}
                return true;
            },
           
            val: function(){
                var d = true;
                for (var k in this) {
                    if (k != "val") {
                        var a;
                        if (typeof this[k] == "function") {
                            a = this[k]();
                        }
                        else {
                            a = this[k];
                        }
                        if (!a) {
                            d = false;
                        }
                    }
                }
                return d;
            }
        };
        var vvl = solf.validate.validateValue;
        var valData = {
            "0": "不能为空",
            "1": "名称过长",
            "2": "格式错误",
            "3": "名称重复"
        };
        $("#instanceName").blur(function(){
            if (!vvl.resName(this)) {
                return;
            }
        });
        $("#buyperiod").blur(function(){
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
	findRelativeProduct:function (){
    	var dom = '';
		$.ajax({
		url : "../product/findRelativeProduct.action?type=8",
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
		                	dom +='<li><div class="picArea"><a href="buybw.jsp?id='+array[i].id+'" title="'+array[i].name+'"><img src="../images/products/bw124.png" /></a></div></li>'; 
		                } );
		                $(".bw .ulList03").html(dom);
		              }
			      }
		       } 
        	}

        });
    },
	checkPorder:function(type){
		var flag = false;		
		if(null==main.porder){
			return;
		}

		for(var i = 0;i<main.porder.vminfos.length;i++){
			
			if(main.porder.vminfos[i].templateType == type){				 
	             flag = true;
	             break;
			}
		}
		return flag;
	}

};
