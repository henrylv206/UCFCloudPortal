var mall = {
    root: "/UCFCloudPortal",  
     init: function(url){    
        $.ajaxSetup({cache:true}); 
    
        var solf = this;
        $.getScript(solf.root + "/js/server.js", function(e){
            mall.server = server;
            mall.server.callback = function(){                                
                mall.server.load("#commend_service", ["mall", "commendService"]);
                mall.server.load("#all_service_part1", ["mall", "allServicePart1"]);
                mall.server.load("#all_service_part2", ["mall", "allServicePart2"]);
            };
            server.init();
        });  
               		
		$("#AllServicePrice").hide();			
		$("#tempType").removeClass('select-o');
		$("#tempType").addClass('select-po');			
		$("#serviceMenu").show();
		$("#privateSwitch").html('<a id="myService" href="/UCFCloudPortal/jsp/my_service.jsp">我的服务</a>');        
    }    
};

function query(){
	search('0');
}

function order(order){
	if($("#"+order).val()==''){
		$("#"+order).val(1);
	}
	$("#"+order).val($("#"+order).val()*-1);
	if(order=="sales"){
		$("#price").val('');
	}else
	if(order=="price"){
		$("#sales").val('');
	}
	if(order=='date'){
		$("#price").val('');
		$("#sales").val('');		
	}
	search();
}

//所有服务
function getAllServiceList(part, typeId, key, sales, price) {
	var solf = this;
	param={
		part : part,
		typeId	:	typeId,
		key	:	key,
		sales	:	sales,
		price:	price
	};
	$.ajax({
		type : "POST",
		url : "../cloud_mall/showServiceMall.action?curPage="+$("#as"+part+"curPage").val()+"&pageSize="+$("#as"+part+"pageSize").val(),
		datatype : "json",
		data : param,
		async : false,
		global : false,
		success : function(data) {
			if(data != null) {
	            if(typeof(data) == "string" && "false"==data) {
					dialog_confirmation('#dialog_confirmation',"失败：获取数据失败");
					return;
	            }else {
				  if(data.listResp.total != undefined) {
	                $("#as"+part+"curPage").val(data.listResp.page);
	                $("#as"+part+"pageSize").val(data.listResp.size);		
                	var totalPage = Math.ceil(data.listResp.total/ data.listResp.size);
                	$("#as"+part+"total").val(totalPage);	 
                	spanDisplay("as"+part);
	              }
	            	var array = data.listResp.list;				
	            
					$("#all_service_span"+part+"").empty();
				
					if (array != null && array.length > 0) {	
						if(part == 2)$("#allpart2").show();//fix bug 3572 查询第二行存在记录时，全部服务第二行显示
						//fix bug 3103
						//part = 1
						if($("#as1curPage").val()==1){
							$("#as1p").css("display","none");
						}else{
							$("#as1p").css("display","block");
						}
						//part = 1 next
						//$("#as1n").css("display","none");
						//part = 2 pre
						//$("#as2p").css("display","none");
						//part = 2 next
						if($("#as2curPage").val()==$("#as2total").val()){
							$("#as2n").css("display","none");
						}else{
							$("#as2n").css("display","block");
						}
						
						$(array).each(function(i) {
							$("#all_service_span"+part+"").append(tables(array[i]));								
						});
					}else{
						$("#allpart2").hide();//part = 2 all hide
						//part = 1 pre
						if($("#as1curPage").val()==1){
							$("#as1p").css("display","none");
						}else{
							$("#as1p").css("display","block");
						}
						//part = 1 next
						$("#as1n").css("display","none");
					}
					if($("#as1total").val()==0){					
						$("#all_service_span1").html("&nbsp;&nbsp;&nbsp;&nbsp;没有找到记录！");
						$("#allpart2").hide();//fix bug 3572 当查询结果记录为空时，全部服务第二行隐藏
					}
	            }
	        }else{//fix bug 3498 当查询结果记录为空时，更新结果内容
				$("#all_service_span1").html("&nbsp;&nbsp;&nbsp;&nbsp;没有找到记录！");
				$("#allpart2").hide();//fix bug 3572 当查询结果记录为空时，全部服务第二行隐藏
	        }
		}
	});
}

//推荐服务
function getCommendServiceList() {
	var solf = this;
 
	$.ajax({
		type : "POST",
		url : "../cloud_mall/showCommendService.action?curPage="+$("#comcurPage").val()+"&pageSize="+$("#compageSize").val(),
		datatype : "json",
		data : {},
		async : false,
		global : false,
		success : function(data) {
			if(data != null) {
	            if(typeof(data) == "string" && "false"==data) {
					$("#commend_service_span").html("&nbsp;&nbsp;&nbsp;&nbsp;没有找到记录！");
					return;
	            }else {
				  if(data.listResp.total != undefined) {
	                $("#comcurPage").val(data.listResp.page);
	                $("#compageSize").val(data.listResp.size);			
                	var totalPage = Math.ceil(data.listResp.total / data.listResp.size);
                	$("#comtotal").val(totalPage);	       
                	spanDisplay("com");              
	              }
	            	var array = data.listResp.list;				
	            
					$("#commend_service_span").html("");
				
					if (array != null && array.length > 0) {	 
						$(array).each(function(i) {
							$("#commend_service_span").append(tables(array[i]));								
						});
					}
	            }
	        }
		}
	});
}

function tables(info){
	var name = info.name;
	
	name=name.length>9?name.substr(0,9)+'...':name;
 
	var desc = info.description;
	
	desc=desc.length>9?desc.substr(0,9)+'...':desc;
	 
	var price = info.price;
	
	var unit = "";			    			
	if(info.unit == 'Y') {                    	    			
		unit = '年';
    }
	if(info.unit == 'M') {                    	    			
		unit = '月';
    }
	if(info.unit == 'W') {                    	    			
		unit = '周';
    }
	if(info.unit == 'D') {                    	    			
		unit = '天';
    }
	if(info.unit == 'H') {                    	    			
		unit = '小时';
    }
    
    var image=serviceIcon(info.id, info.type, 90, 'mall', info.resourcePoolsId, info.zoneId, info.special)[0];
    
    var url=serviceIcon(info.id, info.type, 90, 'mall', info.resourcePoolsId, info.zoneId, info.special)[1];
    
    price=price+'元/'+unit;
    
    var price_part=price.length>5?price.substring(0,5)+'...':price;
    
    var dom = ''
		+ '<li>'
		+ '  <div class=\"hotIcon\"></div>'
		+ '  <p class=\"pro\"><a href='+url+'><img src=\"'+image+'\"/></a></p>'
		+ '  <p style="padding:0px 16px 0px 0px;font-weight: bold;" title="'+ info.name +'"><a href='+url+' >'+name+'</a></p>'
		+ '  <p style="padding:0px 16px 0px 0px;word-break:break-all; width:120px;" title="'+ info.description +'">'+desc+'</p>'			 
		+ '</li>';
    
	return dom;
}

function search(o){
	var typeId=$("#typeId").val();
	var key=$("#keyword").val();
	var sales=$("#sales").val();
	var price=$("#price").val();
	if(o!=''){
		$("#as1curPage").val('1');
		$("#as2curPage").val('1');
		$("#as1total").val('1');
		$("#as2total").val('1');
	}
	$(function(){
        getAllServiceList(1, typeId, key, sales, price);
        getAllServiceList(2, typeId, key, sales, price);
	});
	$("#tempType").hide('fast');
}

//所有服务的翻页
function turnPage(direc, step) {
	if (direc == 1) {
		if (step == 0) {
			$("#as1curPage").val($("#as1total").val());
			$("#as2curPage").val($("#as2total").val());
		} else {
			$("#as1curPage").val(parseInt($("#as1curPage").val())
					+ parseInt(step));
			$("#as2curPage").val(parseInt($("#as2curPage").val())
					+ parseInt(step));
		}
	} else {
		if (step == 0) {
			$("#as1curPage").val(1);
			$("#as2curPage").val(1);
		} else {
			$("#as1curPage").val(parseInt($("#as1curPage").val())
					- parseInt(step));
			$("#as2curPage").val(parseInt($("#as2curPage").val())
					- parseInt(step));
		}
	} 
	search('');
}

//推荐服务的翻页
function turnPage2(direc, step) {
	if (direc == 1) {
		if (step == 0) {
			$("#comcurPage").val($("#comtotal").val());
		} else {
			$("#comcurPage").val(parseInt($("#comcurPage").val())
					+ parseInt(step));
		}
	} else {
		if (step == 0) {
			$("#comcurPage").val(1);
		} else {
			$("#comcurPage").val(parseInt($("#comcurPage").val())
					- parseInt(step));
		}
	}
	getCommendServiceList();
}

function spanDisplay(p){
	if($("#"+p+"curPage").val()==1){
		$("#"+p+"p").css("display","none"); 	
	}else{
		$("#"+p+"p").css("display","block"); 		
	}
	if($("#"+p+"curPage").val()==$("#"+p+"total").val()){
		$("#"+p+"n").css("display","none"); 	
	}else{
		$("#"+p+"n").css("display","block"); 	
	}
}

function spanAllClick(p){
	var v=p;
	if($("#as"+p+"curPage").val()==1){
		$("#as"+p+"p").unbind(); 	
	}else{
		$("#as"+p+"p").bind('click',function(v){
			turnPage(-1, 1, v);
		}); 		
	}
	if($("#as"+p+"curPage").val()==$("#as"+p+"total").val()){
		$("#as"+p+"n").unbind();
	}else{
		$("#as"+p+"n").bind('click',function(v){
			turnPage(1, 1, v);
		});
	}
}

function spanComClick(){
	if($("#comcurPage").val()==1){
		$("#comp").unbind(); 	
	}else{
		$("#comp").bind('click',function(){turnPage2(-1, 1);}); 		
	}
	if($("#comcurPage").val()==$("#comtotal").val()){
		$("#comn").unbind();
	}else{
		$("#comn").bind('click',function(){turnPage2(1, 1);});
	}
}

