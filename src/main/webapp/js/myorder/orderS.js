var orderS = {
    root: "/UCFCloudPortal",    
    
    init: function(url){
	
		$.ajaxSetup({cache:true}); 
	
	    var solf = this;
	    $.getScript(solf.root + "/js/server.js", function(e){
	    	orderS.server = server;
	        orderS.server.callback = function(){
	        	orderS.server.load(".prorder", ["myorder", "order"]);

	        };
	        server.init();
	    });     
	    
	    $("#serviceMenu").show();		
	},
 	
    //&&& deleted? 发送请求去查询开关信息，1:SkyCloud1.1 ; 2:广东移动VDC; 3: 上海浦软；4：北研院
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
        $(".pros").animate({
	        left: "-" + (Number(id) * 625) + "px"
	    });

        index.clear_clazz();
       
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
    clear_clazz :function(){		 
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
	
    clear_class :function(){
        for(var i =1;i<12;i++){
             $(".w span:first").html("");
        }    
    },
    
    //返回首页方法
    toHomePage:function(){
        index.clear_clazz();
        $(".pros").animate({
            left: "-0px"
       });
    }
};
