var monitor = {
	root : "/UCFCloudPortal",
	init : function(pid) {
	
	var cloudStr = monitor.getCloudInfo(); 
	
	if(true){
		$("#BuyMonitorTop").hide();
		$(".mysearch").hide();
		$(".nav").hide();
		$(".footer").hide();
		$(".active01").hide();
		$(".active02").hide();
		$("#serviceMenu").show();
		$("#price").hide();
		$("#price2").hide();
	}
	
		monitor.product = {};
		monitor.getProduct(pid);
        monitor.PublicPrivateSwitch();
		monitor.num = '';
		$.ajax({
					type : "GET",
					url : monitor.root + "/portal_mo/checkMonitor.action",
					success : function(data) {
						if (data) {
							monitor.num = data;
						}
					}
				});
		monitor.validateBuyCycle();
		//monitor.findRelativeProduct();
		monitor.loaddata();
	},
	
	
	loaddata : function(){
        var id = $(".monitor #id").val();
        var pro = monitor.product[id];
        $(".monitor #productId").val(pro.id);
        //to fix bug:3580
        $(".monitor #productName").val(pro.name);
        
        $(".monitor #con_tab_1").html(pro.description);
        var price = pro.price + "元/" + pro.unitC;       
        if (1==Number($("#PublicPrivate").val())) {
            $(".monitor #serviceprice").html(price);
        }else
        if (2==Number($("#PublicPrivate").val())) {
            $(".monitor #serviceprice").html(pro.unitC);
        }		
	},
	
	// 购买周期输入框验证validateBuyCycle
	validateBuyCycle : function() {
		$("#monitor_buyCycle").blur(function() {
				if (Number($(this).val()) + "" == "NaN") {
					$(this).val(1);
					return;
				}
				if (Number($(this).val()) < 1) {
					$(this).val(1);
					return;
				}
				if (Number($(this).val()) > 9999) {
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

	getVMTemplateById : function(templateId) {
		var template = {};
		$.ajax({
			url : monitor.root + "/template/getVMTemplateById.action?type=1&id=" + templateId,
			type : "GET",
			timeout : 5000,
			async : false,
			success : function(json) {
				vethAdaptorNum = json.vethAdaptorNum;
				template[templateId] = json;
				$(".monitor #templateType").val(json.type);
				$(".monitor #templateId").val(json.id);
				$(".monitor #templateName").val(json.templateDesc);
				$(".monitor #monitorRes").html(json.networkDesc);
			}
		});
	},

	getProduct : function(pid) {
		$.ajax({
			type : "POST",
			url : monitor.root + "/vdc_vmmanage/showProductByid.action",
			datatype : "json",
			data : {
				id : pid
			},
			async : false,
			global : false,
			success : function(data) {
				if (data != null) {
					if (typeof(data) == "string" && data.indexOf("error") == 0) {
						alert(data);
						return;
					} else {
						var info = data[0];
						if (info.unit == 'Y') {
							info.unitC = '年';
						} else if (info.unit == 'M') {
							info.unitC = '月';
						} else if (info.unit == 'D') {
							info.unitC = '天';
						} else if (info.unit == 'H') {
							info.unitC = '小时';
						} else if (info.unit == 'W') {
							info.unitC = '周';
						}
						//fix bug 3991
						$(".monitor #ti_unit").html(info.unitC);
						monitor.product[info.id] = info;
						monitor.getVMTemplateById(info.templateId);
					}
				}
			}
		});
	},

	submit : function(para) {

		if ($(".monitor #instanceName").val().length == 0) {
			alert("请输入服务名称");
			$(".monitor #instanceName").focus();
			return;
		}
		if ($(".monitor #instanceName").val().length > 20) {
			alert("服务名称不能超过20个字符（一个汉字算2个字符）");
			$(".monitor #instanceName").focus();
			return;
		}
		if (!commonUtils.scoreReg.exec($(".monitor #instanceName").val())) {
			alert("服务名称只能包含"+commonUtils.regMessage);
			$(".monitor #instanceName").focus();
			return;
		}
		//if ($(".monitor #instanceDesc").val().length == 0) {
		//	alert("请输入描述");
		//	$("#.monitor #instanceDesc").focus();
		//	return;
		//}
		//to fix bug 3887
		if ($(".monitor #instanceDesc").val().length > 100) {
			alert("描述不能超过100个字符");
			$(".monitor #instanceDesc").focus();
			return;
		}
		//if (!scoreReg.exec($(".monitor #instanceDesc").val())) {
		//	alert("描述只能包含中文、字母、数字、下划线及连接符");
		//	$(".monitor #instanceDesc").focus();
		//	return;
		//}
		if (monitor.num > 0) {
			alert("您已经申请云监控服务，或该服务正在审核中");
			return;
		}
		if(monitor.checkMonitorInShop()){//fix bug3791
			alert("您已经申请云监控服务，在您的购物车中");
			return;
		}

		var pid = $(".monitor #id").val();
		var pro = monitor.product[pid];
		
		//main.porder.voorders.push({
		//			id : pid,
		//			num : 1,
		//			productName : "",
		//			type : 5,
		//			typeId : 10,
		//			usePeriod : 0
		//		});
        //to fix bug [3270]
        if(para == 1){
            for(var i = 0;i<main.porderNow.vminfos.length;i++){                 
                main.porderNow.vminfos.removeAt(i, 1);
            }   
            //for(var i = 0;i<main.porderNow.vminfos.length;i++){                 
            //    main.porderNow.vminfos.removeAt(i, 1);
            //}               
        }				
        var vminfos ={//bug 0003309
                    productId : pid,
                    virtual : main.buildKey(),
                    flag : "monitorService",
                    templateId : pro.templateId,
                    templateType : Number(pro.type),
                    productName : $(".monitor #productName").val(),
                    shopcartdesc : pro.description,
                    description : $("#instanceDesc").val(),
                    instanceName : $("#instanceName").val(),
                    charge : pro.price,
                    unit : pro.unit,
                    unitC : pro.unitC,
                    num : Number($(".monitor #monitor_buyCycle").val()),
                    period : Number($(".monitor #monitor_buyCycle").val())
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
	},//fix bug 3791检查购物车中是否已购买云服务
    checkMonitorInShop : function(){
		var flag = false;		
		for(var i = 0;i<main.porder.vminfos.length;i++){
			if(main.porder.vminfos[i].templateType == 5){
				flag = true;
				break;
			}
		}
		return flag;
	},

	// 发送请求去查询开关信息，1:SkyCloud1.1 ; 2:广东移动VDC; 3: 上海浦软；4：北研院
	getSwitchInfo : function() {
		var switchInfo = 1;
		$.ajax({
					url : monitor.root + "/publicIp/getSwitchInfo.action",
					type : 'POST',
					dataType : 'json',
					async : false,
					success : function(data) {
						switchInfo = data;
					}
				});
		return switchInfo;
	},

  getCloudInfo: function(){
    var cloudInfo = 0;
    $.ajax({
      url: monitor.root + "/sysParameters/getCloudInfo.action",
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
        $('#PublicPrivate').val(monitor.getCloudInfo()+'');
    },
    
	findRelativeProduct : function() {
		var dom = '';
		$.ajax({
			url : monitor.root + "/product/findRelativeProduct.action?type=5",
			data : null,
			dataType : "json",
			success : function(data) {
				if (data != null) {
					if (typeof(data) == "string" && data.indexOf("error") == 0) {
						return -1;
					} else {
						var array = data.listResp.list;
						if (array == null || array.length == 0) {
							return;
						} else {
							$(array).each(function(i) {
								dom += '<li><div class="picArea"><a href="buymonitor.jsp?id=' + array[i].id + '" title="' + array[i].name + '"><img src="../images/products/monitor124.png" /></a></div></li>';
							});
							$(".monitor .ulList03").html(dom);
						}
					}
				}
			}

		});
	}
};