var main = {
    
    isEventBubbleCanceled:false,
    curtUserId:"",
    curtUserName:"",//当前用户名
    root: "/UCFCloudPortal",
    offmain: this,
    whichMonitor:'',
    url:'',
    
    userId:0,
    companyId:0,
    
	vdiskmanageinit: function(){
	
		$.ajaxSetup({cache:false}); 
	
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){
	        	main.findLoginInfo(main.loginCallBack);         	
	        	main.server.load("#vdiskmanagebody", ["vdiskmanage", "vdiskmanagenew"]);        	
	        };
	        server.init();
	    });   
	}, 

	  getWhichMonitor: function(){//bug 0006741
	    $.ajax({
	          type : "GET",
	          url : main.root+"/portal_mo/getMonitorInsByUser.action",
	          success : function(data) {
	              main.whichMonitor = data;
	            }
	    });
	  },

	backupmanageinit: function(){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){
	        	main.findLoginInfo(main.loginCallBack);         	
	        	main.server.load("#backupmanagebody", ["backupnamage", "backupnamagenew"]);        	
	        };
	        server.init();
	    });   
	}, 
    
    vmmanageinit: function(){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){
	        	main.findLoginInfo(main.loginCallBack);         	
	        	main.server.load("#vmmanagebody", ["vmmanage", "vmmanagenew"]);        	
	        };
	        server.init();
	    });   
	},    
    
    submitorderinit: function(url){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){
	
	        	main.findLoginInfo(main.loginCallBack); 
	        	
	        	main.server.load("#submitorder", ["shopcar", "submitorder"]);
	        	
	        };
	        server.init();
	    });   
	},
    
    shopcarinit: function(url){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){

	        	main.findLoginInfo(main.loginCallBack); 
	        	
	        	main.server.load("#shoppingcar", ["shopcar", "shopcart"]);
	        	
	        };
	        server.init();
	    });   
        Array.prototype.removeAt = function(index, length){
            this.splice(index, length || 1);
        };
	},
	
    shopcarviewinit: function(url){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){

	        	main.findLoginInfo(main.loginCallBack); 
	        	
	        	main.server.load("#shoppingcarview", ["shopcar", "shopcartview"]);
	        	
	        };
	        server.init();
	    });   
        Array.prototype.removeAt = function(index, length){
            this.splice(index, length || 1);
        };
	},	
	
	submitorderNowinit: function(url){
		
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){

	        	main.findLoginInfo(main.loginCallBack); 
	        	
	        	main.server.load("#submitorderNow", ["shopcar", "submitorderNow"]);
	        	
	        };
	        server.init();
	    });   
        Array.prototype.removeAt = function(index, length){
            this.splice(index, length || 1);
        };
	},	
   
    //初始化方法
    init: function(url){
 
		$.ajaxSetup({cache:false}); 
		
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	        main.server = server;
	        main.server.callback = function(){
	            	            
	        	main.server.load("#newHost", ["index", "newHost"]);
	        	main.server.load("#commendHost", ["index", "commendHost"]);
	        	main.findLoginInfo(main.loginCallBack);//设置登录信息
	        };
	        server.init();
	    });   

//        window.onbeforeunload = function(){return "您尚未保存的数据可能丢失！";};
          //禁用backspace，对浏览器的后退
          $(document).keydown(function(event){
             var obj = event.target || event.srcElement;//获取事件源
             var t = obj.type || obj.getAttribute('type');//获取事件源类型
	         if(event.keyCode == 8 && t != "password" && t != "text" && t != "textarea"){ 
                        return false;
                }
                if(event.keyCode == 9 && t=="submit"){ 
                        return false;
                } 
	      });
          //禁用Tab
        
        //$.ajaxSetup({cache:true});
      	
      	$.ajaxSetup({
              contentType:"application/x-www-form-urlencoded;charset=utf-8",
              timeout:10000,
              cache:true,//设置jQuery ajax缓存
              complete:function(XHR,TS){
  	           var resText=XHR.responseText;
  	           if(resText=='{"sessionState":0}'){  
  	        	   //jump to index.html
  	        	   main.getRootWin().open(main.root+'/html/index.html','_top');				   
  	           }
  	           if(resText=='{"sessionState":1}'){    	   
  	        	   alert('你已被管理员锁定，不允许操作！');		   
  	           }  	           
  	        }
  	     }); 
    	
        var solf = this;    
        
		solf.setDatepicker;
		/************************************************/
//        /*我的购物车*/
        $(".a_goShopcart").click(main.go_shopcart);
        /*产品概览*/
        $(".teleindex_adv01").css("cursor","pointer").click(function(){
            window.open("chinatel_productoverview.html");}
        );
        /**/
        $(".main1 .tele_img202").css("cursor","pointer").click(function(){
            main.switchDomain();
        });
        $(".main1 .pro_cri02 img").css("cursor","pointer").click(function(){
            main.switchDomain();
        });
        $(".main2 .tele_proimg202").css("cursor","pointer").click(function(){
            main.switchDomain();
        });
        $(".main2 .pro_cri03 img").css("cursor","pointer").click(function(){
            main.switchDomain();
        });
        
     // 从指定位置删除一个或者length个元素
        Array.prototype.removeAt = function(index, length){
            this.splice(index, length || 1);
        };
             
    	$("#main_leftAdvertisement").hide();
    	$("#main_leftNotAdvertisement").show();  
    	$("#yonghuzhuxiao").hide();
    	//to fix bug [1804]
    	$("#gerenxinxi").hide();
        
        //var switchStr = main.getSwitchInfo();
        
        $(".teleindex_adv06").click(function(){
        	
        });
        /*网站地图*/
        $("#webMap").click(function(){        	
        	showBg("directMap","");
        	//$("#directMap").css("display","block");
        }); 
        $("#mapClose").click(function(){
        	$("#directMap").css("display","none");
        	closeBg();
        });
    },
    
    //发送请求去查询开关信息，1:SkyCloud1.1 ; 2:广东移动VDC; 3: 上海浦软；4：北研院
	getSwitchInfo : function() {
		var switchInfo = 1;
		$.ajax({
			url: "/UCFCloudPortal/publicIp/getSwitchInfo.action",
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				switchInfo = data;
			}
		});
		return switchInfo;
	},
	
	//导航栏按下效果
	menuAnimate: function(id, i) { 
		$(".main1 .teleindex_product").show();
		$(".main1 .shopcart").hide();
		 
        $(".pros").animate({
            left: "-" + (Number(id) * 625) + "px"
        });
        
        main.clear_color();
        
        //导航栏按钮着色
        var addColor = {
        		 "vm":"ws1",			                 
                 "ebs":"ws2",
                 "backup": "ws3",
                 "monitor": "ws4",
                 "mulvm": "ws5",
                 "mc": "ws6",
                 "tip": "ws7", 
                 "ce": "ws8",
                 "firewall": "ws9",
                 "loadBalance": "ws10",
                 "objectStorage": "ws11",
                 "disk": "ws12"         
        };
        $(".w" + i +" span:first").addClass("ws"+i);
    },	
    
     //导航栏按钮去色
    clear_color :function(){		 
        for(var i =1;i<13;i++){         
             $(".w" + i +" span:first").removeClass("ws"+i);
        } 
    },   
    
    getRootWin:function(){
    	var win = window;
    	while (win != win.parent){
    		win = win.parent;
    	}
    	return win;
    },
    loginCallBack:function(){    
    	
    	//alert(main.curtUserId);

        /*********购物车变量初始化，改为cookie存放方式。****/
        var porderObj = $.JSONCookie(main.curtUserId+"_shopCart_porder"); 
    	main.porder = main.getShopCartCookie();
    	main.porderNow = main.getNowShopCartCookie();

        main.ofResult = { 
			vpcs : new Array(),
			subnets : new Array(),
			vpns : new Array(),
			vms : new Array()
		};
		main.existsSubnet = []; 	
	  
        //$(".a_goShopcart #shopcart_num").html(main.porder.vminfos.length+" ");
    },
    go_shopcart: function(){
             main.clear_clazz();
	         if($(".main1").css("left")=='960px'){
	            main.switchDomain();
	           }
	         main.clear_color();
             $(".main1 .teleindex_product").hide();
                 main.server.load(".main1 .shopcart", ["order", "shopcart"],function(){
                    $(".main1 .shopcart").show();
                 });
             $(".a_goShopcart #shopcart_num").html(main.porder.vminfos.length+" ");
             
             $("html,body").animate({scrollTop: $(".main1").offset().top}, 1000);//跳转到指定锚点
    },
    shownum_shopcart:function(){
             $(".a_goShopcart #shopcart_num").html(main.porder.vminfos.length+" ");
    },
    unshow_shopcart:function(){
             $(".main1 .shopcart").hide();
             $(".main1 .teleindex_product").show();
    },
    go_work_shopcart : function(){
            $(".main2 .buyPros").hide();
            main.server.load(".main2 .shopcart", ["order", "shopcart"],function(){
                     $(".main2 .shopcart").show();
                 });
    },
    
    
    //我的订单
    myorder: function(){
    	main.clear_clazz();
    	$(".prorder").html("");
        if($(".main1").css("left")=='960px'){
            main.switchDomain();
        }
        main.clear_color();
        var showValue = $(".main1 .teleindex_product").css('display');
        if(showValue =="none"){main.unshow_shopcart();}
        main.server.load(".prorder", ["order", "order"]);
        $(".pros").animate({
            left: "-" + (11 * 625) + "px"
        });
    
    },
    //产品区页面切换方法
    switchMenu:function(targeMenu){
    	var prodetail_vms=($(".main1 .vms .vms")).css('display');
    	if(prodetail_vms=="block"){
    		$(".main1 .vms .pro_detail:first").css("display","block");
    		$(".main1 .vms .vms").css("display","none");
    	}
    	
    	
    	var prodetail_ebs=($(".main1 .ebs .ebs")).css('display');
    	if(prodetail_vms=="block"){
    		$(".main1 .ebs .pro_detail:first").css("display","block");
    		$(".main1 .ebs .ebs").css("display","none");
    	}  	

    	var prodetail_tip=($(".main1 .tips .tips")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .tips .pro_detail:first").css("display","block");
    		$(".main1 .tips .tips").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .backups .backups")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .backups .pro_detail:first").css("display","block");
    		$(".main1 .backups .backups").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .databackups .databackups")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .databackups .pro_detail:first").css("display","block");
    		$(".main1 .databackups .databackups").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .firewalls .firewalls")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .firewalls .pro_detail:first").css("display","block");
    		$(".main1 .firewalls .firewalls").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .loadBalances .loadBalances")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .loadBalances .pro_detail:first").css("display","block");
    		$(".main1 .loadBalances .loadBalances").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .vpcs .vpcs")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .vpcs .pro_detail:first").css("display","block");
    		$(".main1 .vpcs .vpcs").css("display","none");
    	}
    	var prodetail_tip=($(".main1 .vpns .vpns")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .vpns .pro_detail:first").css("display","block");
    		$(".main1 .vpns .vpns").css("display","none");
    	}
    	/*var prodetail_tip=($(".main1 .bandwidtys .bandwidty")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .bandwidtys .pro_detail:first").css("display","block");
    		$(".main1 .bandwidtys .bandwidty").css("display","none");
    	}*/
    	var prodetail_tip=($(".main1 .monitors .monitors")).css('display');
    	if(prodetail_tip=="block"){
    		$(".main1 .monitors .pro_detail:first").css("display","block");
    		$(".main1 .monitors .monitors").css("display","none");
    	}
 
    	var prodetail_vpc=($(".main1 .setvpc .vpc")).css('display');
    	if(prodetail_vpc=="block"){
    		$(".main1 .setvpc .pro_detail:first").css("display","block");
    		$(".main1 .setvpc .vpc").css("display","none");
    	}

        var showValue = $(".main1 .teleindex_product").css('display');
    	if(showValue =="none"){main.unshow_shopcart();}
        
        var solf = targeMenu;
 
    },//end switchMenu   

    clear_clazz: function(){
        for(var i =1;i<12;i++){
           $(".w").removeClass("w"+i+"s");
      }
    },
    
    //返回首页方法
    toHomePage:function(){
    	main.clear_color();
        main.clear_clazz();
	    if($(".main1").css("left")=='960px'){
	            main.switchDomain();
	    }
        var showValue_s = $(".main1 .teleindex_product").css('display');
        if(showValue_s =="none"){main.unshow_shopcart();}
    	$(".pros").animate({
            left: "-0px"
       });
       if($(".main1").css("display")=="none"){
    	   main.switchDomain();
       }
    },
    //工作区 产品区切换方法
    switchDomain: function(){  	
    	var solf = this;
    	solf.cutover();    	
        if($(".main1").css("left")=="0px"){
                $(".main1").animate({
                left: "-960px"},
                '16000',
                function(){$(".main1").css("left","960px");});
	            $(".main2").animate({left: "0px"},'16000');
	            if($("#tagContent1").css("display")=="block"){
	            	main.server.loadJS([ "businessusers", "accoutgroupinfo" ]);
	    			main.server.load("#groupListma", [ "businessusers", "grouplist" ]);
	    			main.server.load("#useraccountInfo", [ "businessusers",
	    					"accoutinfo" ]);
	        		$("#users").empty();
	        		$("#users").html("<span class=\"fontorg\">用户管理</span><button class=\"userbutton3\" style=\"float: right;\" title=\"添加用户\"></button><span class=\"fontorg1\">添加用户</span>");	
	        	}
        }
        if($(".main2").css("left")=="0px"){        	
                $(".main2").animate({left: "-960px"},function(){$(".main2").css("left","960px");;});
	            $(".main1").animate({left: "0px"});	            
        }    	
    },
	
	getNicsDhcpSwitch : function(){
			var nicsDhcpSwitch = -1;
			$.ajax({
	      url: "../sysParameters/getNicsDhcpSwitch.action",
		  	type: 'POST',
		  	dataType: 'json',
		  	async: false,
		  	success: function(data) {
			  	nicsDhcpSwitch = data;
	      }
			});
		  return nicsDhcpSwitch;
	},
    
    buildKey: function(){
        var myDate = new Date();
        var str = "" + (20+myDate.getHours()) + myDate.getMinutes() +
        myDate.getSeconds() +
        myDate.getMilliseconds();
        return Number(str);
    }, 
    
    cutover: function(){
    	
    	
		$(".main2 .teleindex_product").css("display","block");
		$(".main2 .buyPros").css("display","none");
        $(".main2 .shopcart").css("display","none");
    	
        var pid=$("#wbuytype").attr("value");
       
        main.server.load("#WorkSpaceArea", ["servicemanage", "vmmanage"]);
        
        var tmenu = '<span class="bluetxt boldtxt">虚拟机管理</span>';
        $("#menu20").html('虚拟机管理');
        $("#menu30").html('vdisk管理');
        $("#menu40").html('弹性公网IP管理');
        $("#menu70").html('虚拟机备份管理');
        $("#menu71").html('公网带宽管理');
        $("#menu72").html('云监控管理');
        $("#menu73").html('数据云备份管理');
        $("#menu74").html('防火墙管理');
        $("#menu76").html('负载均衡管理');
        $("#menu77").html('对象存储管理');
        $("#menu78").html('块存储管理');
        $("#menu20").html(tmenu);
        $(".teleindex_product .order_title").html('虚拟机管理');
        
        $("#buycar span:first").click(function(){
            $(".main2 .teleindex_product").hide();
            $(".main2 .buyPros").show();
        });
    },
    getRandomNum:function(lbound, ubound) {
        return (Math.floor(Math.random() * (ubound - lbound)) + lbound); 
    },
    getNumAndChartString:function(num){
        var str="abcdefghijklmnopqrstuvwxyz1234567890";
        var s=str.split(""); 
        var t=""; 
        for (var i=0;i<num;i++){ 
            t+=s[main.getRandomNum(1,36)]; 
        }
        return t;
    },
	/**设置Cookie**/
 
	/**删除Cookie**/
    
	/**设置DB Cookie**/
	setShopCartCookie:function(){
		var data = {
				cookie:{
					COOKIE_KEY:main.curtUserId+"_shopCart_porder",
					COOKIE_VALUE:JSON.stringify(main.porder),
					COOKIE_TYPE:1
				}
		};
		 $.ajax({
			 	url : "/UCFCloudPortal/cookie/put.action",
				type:'POST',
				dataType:'json',
				contentType : "application/json",
				data : $.toJSON(data),
				async : false,
				success : function(z) {}
		 });
	},
	
	/**获取DB Cookie**/
	getShopCartCookie:function(){
		var result;
		main.server.getServer("getDBCookie", {
			 data:{key:main.curtUserId+"_shopCart_porder"},
			 success : function(z) {
				if(z && z.COOKIE_VALUE){
					result = $.secureEvalJSON(z.COOKIE_VALUE);
				}
			 },
			 async : false//同步请求
		 });
		if(!result||result == null || result == "" || result == "undefined"|| result.length == 0||typeof(result.voorders)=='undefined'){
			result = {
                    voorders: [],
                    vminfos: [],
                    inslists: [],
                    subnets: []
            };
			
		}
		return result;
	},
	
	/**立即购买**/
	
	// to fix bug [3059]
	
	setNowShopCartCookie:function(){	
		
		var data = {
			cookie:{
				COOKIE_KEY:main.curtUserId+"_shopCart_porder_Now",
				COOKIE_VALUE:JSON.stringify(main.porderNow),
				COOKIE_TYPE:1
			}
		};
		$.ajax({
		 	url : "/UCFCloudPortal/cookie/put.action",
			type:'POST',
			dataType:'json',
			contentType : "application/json",
			data : $.toJSON(data),
			async : false,
			success : function(z) {}
		});
	},	
	
	getNowShopCartCookie:function(){
		var result;
		main.server.getServer("getDBCookie", {
			 data:{key:main.curtUserId+"_shopCart_porder_Now"},
			 success : function(z) {
				if(z && z.COOKIE_VALUE){
					result = $.secureEvalJSON(z.COOKIE_VALUE);
				}
			 },
			 async : false//同步请求
		 });
		if(!result||result == null || result == "" || result == "undefined"|| result.length == 0||typeof(result.voorders)=='undefined'){
			result = {
                    voorders: [],
                    vminfos: [],
                    inslists: [],
                    subnets: []
            };
			
		}
		return result;
	},	
	
	/**删除DB Cookie**/
    removeShopCartCookie:function(){
		 main.server.getServer("deleteDBCookie", {
			 data:{key:main.curtUserId+"_shopCart_porder"},
			 success : function(z) {},
			 async : false//同步请求
		 });
	},
	
    removeNowShopCartCookie:function(){
		 main.server.getServer("deleteDBCookie", {
			 data:{key:main.curtUserId+"_shopCart_porder_Now"},
			 success : function(z) {},
			 async : false//同步请求
		 });
	},
	
	/**设置单独产品Cookie
	 * 在第二次申请时读取该产品值
	 * **/
	setSigleProCookie : function(object){
//		$.JSONCookie(main.curtUserId+"_prevPro",object, {expires: 1,path: '/'});
	},
	/**取出SigleProCookie方法 
	 * 返回存入的Object**/
	getSigleProCookie : function(){
//		return $.JSONCookie(main.curtUserId+"_prevPro");//取cookie
	},
	/**
	 * 清空SigleProCookie方法 
	 * **/
	removeSigleProCookie : function(){
//		$.JSONCookie(main.curtUserId+"_prevPro", {}, {expires: 1,path: '/'});
	},
	goBindUser1: function(vmResId){
        var solf = this;
        solf.id = vmResId;
        $("#popBindUser").append("");
        var W = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth);
        var H = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
        if (main.manager) {
            main.manager.clear();
        }
        main.server.getServer("getbindTree", {
            data: {
                vmResId: vmResId
            },
            success: function(user){
                if (main.manager) {
                
                    main.manager.append(null, user);
                }
                else {
                    $("#popBindUser").ligerTree({
                        data: user
                    });
                    manager = $("#popBindUser").ligerGetTreeManager();
                    main.manager = manager;
                }
            }
        });
        $(".BindUser").css("top", "190px");
        $(".BindUser").css("left", "740px");
        $(".BindUser").css("display", "block");
        $(".BindUser").css("height", "460px");
        mask = document.createElement("div");
        mask.id = "mask";
        mask.style.cssText = "position:absolute;z-index:5;width:" +
        W +
        "px;height:" +
        H +
        "px;background:#000;filter:alpha(opacity=30);opacity:0.3;top:0;left:0;";
        document.body.appendChild(mask);
    },
	
	goBindUser: function(vmResId){
        var solf = this;
  
        main.server.getServer("getbindTree", {
            data: {
                vmResId: vmResId
            },
            success: function(user){
                if(user.length == 0){
                	alert('当前没有组和用户，请先创建组和用户！');
                } else {
                	main.goBindUser1(vmResId);
                }
            }
        });

    },
    
    hiddenBindUser: function(){
        var W = Math.max(document.documentElement.scrollWidth, document.body.scrollWidth);
        var H = Math.max(document.documentElement.scrollHeight, document.body.scrollHeight);
        $("#popBindUser").append("");
        $(".BindUser").css("display", "none");
        mask.id = "mask";
        mask.style.cssText = "position:absolute;z-index:5;width:" +
        W +
        "px;height:" +
        H +
        "px;background:#000;filter:alpha(opacity=30);opacity:0.3;top:0;left:0;";
        document.body.removeChild(mask);
    },
    
    getChecked: function(){
        var solf = this;
        var notes = manager.getChecked();

        var text = "";
        for (var i = 0; i < notes.length; i++) {
            if ((notes.length - i) > 1) {
                text += notes[i].data.id + ",";
            }
            else 
                if ((notes.length - i) == 1) {
                    text += notes[i].data.id;
                }
        }
        if (notes.length > 0) {
            var arr = new Array(notes.length);
            arr = text.split(",");          
        }
        
        main.server.getServer("bindUserToPro", {
            data: {
                vmResId: main.id,
                userIdList: arr
            },
            success: function(json){
                if (json) {
                    alert('成功！');
                    solf.hiddenBindUser();
                }
            }
        });
    },
    
    collapseAll: function(){
        manager.collapseAll();
    },
    
    expandAll: function(){
        manager.expandAll();
    },
	
    /*****退出*********/
    logout: function(){  
    	  cleanCookie();	
		  main.server.getServer("logout", {
          success: function(){
          	//退出登录时清空保存上一次操作的cookie
          	main.removeSigleProCookie();
          	location.href = "index.jsp";
          }
      });
    },
    
    
    
	/*********设置登录信息**********/
    findLoginInfo:function(callback){
					    	$.ajax({
					    		url : "/UCFCloudPortal/customerAdmin/getSessioninfo.action",
					    		type : "POST",
					    		contentType : "application/json",
					    		dataType : "json",
					    		data :  $.toJSON({}),
					    		async : false,
					    		timeout : 10000,
					    		success : function(data) {
					    		
					    		//console.log("data = " + data);
					    		
					    		  if(data != null) {
					    			  //alert(data.id);
                                    main.userId = data.id;
                                    main.companyId = data.compId;
                                    main.curtUserId = data.id;
                                    main.curtUserName = data.account;
					    		  }
					    		}					    	
					    });
					    	
                    	//$("#welcome-user-name").html(mian.curtUserName);
                    	
                    	if (typeof callback == "function"){
                    		callback();
                        }
    }
    
};

