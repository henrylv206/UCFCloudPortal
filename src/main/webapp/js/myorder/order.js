//在我的订单中用到
var order = {
		//cloudStr:0,
	orderIns :{},
	reSubIns : new Array(),
//to fix bug:0003259，0003391
	orderState : {
		    "-1":"全部",
	        "1": "申请",
	        "2": "2级审核中",
	        "3": "3级审核中",
	        "4": "审核通过",
	        "5": "审核拒绝",
	        "6": "作废",
	        "7": "审核失败"
	},
    template : {
    	"1":"Windows XP SP3",
    	"2":"Redhat 5.5 (32-bit)"
    },
    //fix 1472
    productState:{
    	"1":"申请",
    	"2":"就绪",
    	"3":"申请处理中",
    	"4":"已删除",
    	"5":"已关机",
    	"6":"操作执行中",
    	"7":"创建失败"
    },
    orderImage:{
        "2" : "tel_review.png",
		"4" : "or_wtg.png",
		"5" : "or_dkt.png",
		"6" : "or_clz.png",
		"7" : "or_ktsb.png",
		"10" : "tel_success.png",
		"13" : "tel_error.png",
		"14" : "or_ktsb.png"
    },
    orderType: {
        "1": "申请开通",
        "2": "申请变更",
        "3": "申请退订",
        "4": "申请续订",
        "null": ""
    },
    orderOrigin: {
        "1": "门户",
        "2": "营业厅",
        "3": "CRM"
    },
    zoneId: {
        "1": "北京集群区域",
        "2": "西安集群区域"
    },
    payState: {
        "1": "支付",
        "2": "未支付"
    },
    payType: {
        "1": "电信充值卡",
        "2": "信用卡"
    },
 // 日期datepicker控件中文设置 to fix bug 3825
    setDatepicker: function(){
        $.datepicker.regional['zh-CN'] = {
            clearText: '清除',
            clearStatus: '清除已选日期',
            closeText: '关闭',
            closeStatus: '不改变当前选择',
            prevText: '&lt;',
            prevStatus: '显示上月',
            nextText: '&gt;',
            nextStatus: '显示下月',
            currentText: '恢复',
            currentStatus: '显示本月',
            monthNames: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            monthNamesShort: ['一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月', '十月', '十一月', '十二月'],
            monthStatus: '选择月份',
            yearStatus: '选择年份',
            weekHeader: '周',
            weekStatus: '年内周次',
            dayNames: ['星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六'],
            dayNamesShort: ['周日', '周一', '周二', '周三', '周四', '周五', '周六'],
            dayNamesMin: ['日', '一', '二', '三', '四', '五', '六'],
            dayStatus: '设置 DD 为一周起始',
            dateStatus: '选择 m月 d日, DD',
            dateFormat: 'yy-mm-dd',
            firstDay: 1,
            initStatus: '请选择日期',
            isRTL: false
        };
        $.datepicker.setDefaults($.datepicker.regional['zh-CN']);
    },
    getMonthBegin: function(){
    	var myDate = new Date((new Date().getTime()) - 30*24*3600*1000);
    	var year1 = myDate.getFullYear(); 
    	var month1 = myDate.getMonth()+1; 
    	//to fix bug 3868
    	if(month1<10){
    		month1 = "0"+month1;
    	}
    	var day1 = myDate.getDate(); //获取当前日(1-31)
    	if(day1<10){
    		day1 = "0"+day1;
    	}
    	var redate = year1+"-"+month1+"-"+day1;
    	return redate;
    },
    getWeekBegin: function(){
    	var myDate = new Date((new Date().getTime()) - 7*24*3600*1000);
    	var year1 = myDate.getFullYear(); 
    	var month1 = myDate.getMonth()+1; 
    	if(month1<10){
    		month1 = "0"+month1;
    	}

    	var day1 = myDate.getDate(); //获取当前日(1-31)
    	if(day1<10){
    		day1 = "0"+day1;
    	}

    	var redate = year1+"-"+month1+"-"+day1;
    	return redate;
    },
    getDateNow: function(){
    	var myDate = new Date();
    	var year1 = myDate.getFullYear(); 
    	var month1 = myDate.getMonth()+1; 
    	var day1 = myDate.getDate(); //获取当前日(1-31)
    	var redate = year1+"-"+month1+"-"+day1;
    	return redate;
    },
  //to fix bug:3328
  //小数位数控制，可以四舍五入 
    Fractional:function (n) { 
        //小数保留位数 
        var bit = 2; 
        //加上小数点后要扩充1位 
        bit++; 
        //数字转为字符串 
        n = n.toString(); 
        //获取小数点位置 
        var point = n.indexOf('.'); 
        //n的长度大于保留位数长度 
        //to fix bug:3355
        if (point>0 && n.length > point + bit) { 
            //保留小数后一位是否大于4，大于4进位 
            if (parseInt(n.substring(point + bit, point + bit + 1)) > 2) { 
                return n.substring(0, point) + "." + (parseInt(n.substring(point + 1, point + bit)) + 1); 
            } 
            else { 
                return n.substring(0, point) + n.substring(point, point + bit); 
            } 
        } 
        return n; 
    },
    //检查是否整数
    isInt: function (s)
    {
    	var reg=/^\d+$/g;
    	 if(!reg.test(s))
	     {
    		 return false;
	     }
    	 	return true;
    },
    init: function(){
        var solf = this;   
        /*solf.cloudStr = solf.getCloudInfo();*/
        for (var key in solf.orderState) {
            $(".prorder #orderState").append('<option value="' + key + '">' + solf.orderState[key] + '</option>');
        }
        $(".prorder #orderselect").click(function(){
        	var $the = $(this);
        	var orderId =  $(".prorder #orderId").val();
        	if (orderId!=""){
        		//to fix bug:3673
        		if (order.isInt(orderId)==false) {
					alert("非法订单编号!");
					return false;
				}
        	}
        	$the.css("cursor","wait");
            solf.selectOrder('1',function(){
            	$the.css("cursor","pointer");
            });
        });

       
     // 日期datepicker控件中文设置
        solf.setDatepicker();
        $(".prorder .i-date").datepicker({
            changeMonth: true,
            changeYear: true,
            showWeek: true,
            showButtonPanel: true,
            dateFormat: "yy-mm-dd",
            gotoCurrent : true,
            showClearButton: true
        });
        
        solf.selectOrder('1');
        $(".prorder .bab").hide();
        
        
        
        if(solf.true){
        	$("#orderMomey").hide();
        }                
        
    },
    
	/*getCloudInfo : function() {
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
	},*/
    
    queryOrder: function(curPage){

    	if(curPage>0){
    		$("#curPage").val(curPage); 
            order.selectOrder(curPage);
    	}else{
            order.selectOrder('1');
    	}
    	
        
    },
    stopBubble :function(e){				 //终止冒泡方法
    	if(e&&e.stopPropagation){
			e.stopPropagation();
		}else{
			window.event.cancelBubble = true;//否则使用ie的方法来取消事件冒泡
		}
    },
    selectOrder: function(page,callback){
        var solf = this;
        var curP = 0;
        if(page!= null && page != ''){
        	curP = page < 0 ? 0 : page;
        }
        var d = {
            	curPage: curP,
            	pageSize: 10,
            	totalPages: 0,
                sorder: {}
            };
            
//            to fix bug	0002278
            $(".prorder #orderState").val() != "-1" ? d.orderState = new Number($("#orderState").val()) * 1 : d;
            $(".prorder #startdate").val() != "" ? d.startdate = $(".prorder #startdate").val() : d;
            $(".prorder #enddate").val() != "" ? d.enddate = $(".prorder #enddate").val() : d;
            //to fix bug 2289
            $(".prorder #orderId").val() != "" ? d.id = $("#orderId").val() : 0;
            
            //to fix bug:3753
            if (d.startdate!="" &&  d.startdate> d.enddate){
            	alert("'开始日期'不能大于'结束日期'");
            	return;
            }
    		/*var cloudInfo = 0;
    		$.ajax({
    			url: "/UCFCloudPortal/sysParameters/getCloudInfo.action",
    			type: 'POST',
    			dataType: 'json',
    			async: false,
    			success: function(data) {
    				cloudInfo = data;
    			}
    		});    */         
        $.ajax({
        	url:"/UCFCloudPortal/order/myOrderServiceList.action",
        	type:'GET',
			contentType : "application/x-www-form-urlencoded; charset=utf-8",
			dataType:'json',
            data: d,
            async:false,
            //解决IE浏览器出现的缓存问题，mantisid：1792
            cache:false,
            //解决IE浏览器出现的缓存问题，mantisid：1792
            success: function(data){
                solf.orders = data;
                
                var dom='';
                if(data == null || data.list.length==0){
            		$("#total").val(0);//fix bug 3508
                    $("#pageInfo").text(0 + "/" + 0);
                    $("#totalPage").text(0);
					$("#page_specified").val(0);
					
                	$("#orderlisttab").accordion("destroy");
                	$("#orderlisttab").html('<tr><td colspan="7"><span style="padding-left:7px;">没有找到记录！</span></td></tr>').css("color","red").css("width","621px");
                }
                else{
    				if(solf.orders.total != undefined) {
  						$("#curPage").val(solf.orders.page);
  						//$("#pageSize").val(data.list.size);
  						var totalPage = Math.ceil(solf.orders.total / d.pageSize);
  						$("#total").val(totalPage);
  		                $("#pageInfo").text(totalPage!=0?solf.orders.page+"/"+totalPage:0 + "/" + totalPage);
  		                $("#totalPage").text(totalPage);
  						$("#page_specified").val(solf.orders.page);
  		                d.totalPages = totalPage;
  	                }
                $("#orderlisttab").accordion("destroy");
                var dom='';
				//奇偶行上色与焦点行事件处理   ninghao 2012-08-24 start
				//焦点行事件处理
				var focusEven = " onmouseover=$(this).addClass(\"over\") onmouseout=$(this).removeClass(\"over\") ";
				var checkbeforenum = 0;
				var checkednum = 0;
				var cancelnum = 0;
                for (var i = 0; i <data.list.length; i++) {
    				//奇偶行上色
    				var oddEven = (i%2) == 0 ? ' class=\"alt\" ' : ' ';
    				var trEvents = oddEven + focusEven;
    				
//                	if(cloudInfo == "1"){
                	var state= "";
                	//to fixed bug:3231
                	 if (data.list[i].orderApproveLevelState == data.list[i].state && data.list[i].state<4){ 
                		 state= "申请";
                	 }else  if (data.list[i].state<4){
                		 state= (data.list[i].state)+"级审核中";
                	 }else  if (data.list[i].state==4){
                		 //to fix bug:3248 3272
                		 state= "审核通过";
                	 }else{
                	     state= order.orderState[data.list[i].state];
                	 }
                	 var reason = data.list[i].reason;
                	 if (reason==null){
                		 reason = "";
                	 }
                	 //to fix bug:3202 3323
                	 
                	 /*if(solf.cloudStr=="1"){
                		dom += '<tr '+ trEvents +'>'+
                		 '<td class="number"><a class="quit" href="order_service.jsp?orderid=' +data.list[i].orderId +'&&ordertype='+data.list[i].type+' ">' +data.list[i].orderId +' </a></td>' +
                		 '<td>' +usercount +'</td>' +
                		 '<td>' +order.orderType[data.list[i].type] +'</td>' +
                		 '<td class="money">' + order.Fractional(data.list[i].price) +'元</td>' +
                		 '<td>' + data.list[i].createDt +' </td>' +
                		 '<td>' +state+'</td>' +
                		 '<td >' +reason+'</td>' +
                		 '<td class="shop last">' +
                		 '<a href="cloud_mall.jsp" title="继续申请"><img src="../images/icons/icon-edit.png"/></a>&nbsp;&nbsp;' +
                		 '<a href="#"  title="订单取消"><img src="../images/icons/icon-del.png"  name="'+data.list[i].orderId+'" class="bill_btn rejectOrder" /></a>' +
                		 '</td>' +
                		 '</tr>' ;                		 
                	 }
                	 
                	 if(solf.cloudStr=="2"){*/
                 		dom += '<tr '+ trEvents +'>'+
                 		 '<td class="number"><a class="quit" href="order_service.jsp?orderid=' +data.list[i].orderId +'&&ordertype='+data.list[i].type+' ">' +data.list[i].orderId +' </a></td>' +
                 		 '<td>' +usercount +'</td>' +
                 		 '<td>' +order.orderType[data.list[i].type] +'</td>' +                 		
                 		 '<td>' + data.list[i].createDt +' </td>' +
                 		 '<td>' +state+'</td>' +
                 		 '<td >' +reason+'</td>' +
                 		 '<td class="shop last">' +
                 		 '<a href="cloud_mall.jsp" title="继续申请"><img src="../images/icons/icon-edit.png"/></a>&nbsp;&nbsp;' +
                 		 '<a href="#"  title="订单取消"><img src="../images/icons/icon-del.png"  name="'+data.list[i].orderId+'" class="bill_btn rejectOrder" /></a>' +
                 		 '</td>' +
                 		 '</tr>' ;                		 
                 	 //}
//                	}
                	
                order.orderIns[data.list[i].orderId]=data.list[i];
                }//end for
                $("#orderlisttab").html(dom);
                //to fix bug:3081，3276
                $(".prorder #totalnum").html(solf.orders.total );
                $(".prorder #checkbeforenum").html(solf.orders.checkbeforeNum);
                $(".prorder #checkednum").html(solf.orders.checkedNum);
                $(".prorder #cancelnum").html(solf.orders.cancelNum);
                $(".prorder #waitpaynum").html(0);
               
              }
                $(".rejectOrder").hide();
                $(".rejectOrder").each(function(e){
                	var orderId = $(this).attr("name");
                	if(order.orderIns[orderId].state < 4 ){//|| (order.orderIns[orderId].state == 4 && isUsable) 
                		$(this).show();
                	}
                });
                if (typeof callback == "function") {
                	   setTimeout(callback, 500);
                }
            }
        });
       
        $(".orderlisttab ul li input[type=checkbox]").click(function(e){
        	order.stopBubble(e);
        })
        $(".tabCon ul li").unbind("click"); 
        /*
         * 将选中checkbox的实体Id 放入LIST中
         * */
        $("#orderlisttab :checkbox").click(function(){
        	var insId ;
        	insId = $(this).attr("name");
        	if($(this).attr("checked")){
        		order.reSubIns.push(insId);
        	}
        	else{
        		for(var i=0;i<order.reSubIns.length;i++){
        			if(insId == order.reSubIns[i]){
        				order.reSubIns.removeAt(i,1);
        			}
        		}
        	}
        });
        //order 整单退订
        $(".rejectOrder").click(function(e){
        	var id =  $(this).attr("name");
        	
        	if(confirm("您确定要取消该订单？")){
        		
        		var d ={"id":id};
        		$.ajax({
        			url:"/UCFCloudPortal/order/cancelOrder.action",
                	type:'GET',
        			contentType : "application/x-www-form-urlencoded; charset=utf-8",
        			dataType:'json',
                    data: d,
                    async:false,
                    success: function(data){
                    	alert("取消成功");
                    	order.selectOrder(1);//查询第一页
                    }
          	  	});
        	}
        	order.stopBubble(e);
        	
        });
        //MyOrder 重新提交 部分产品
        $(".reOrder").click(function(e){
        	 var reOrderId =  $(this).attr("name");
        	 var d = {
        			 id:reOrderId,
        			 insIds:[]
        	 };
        	 for(var i=0;i<order.reSubIns.length;i++){
        		 if(reOrderId== order.reSubIns[i].substring(0,reOrderId.length)){
        			 d.insIds.push(order.reSubIns[i].substring(reOrderId.length+1,order.reSubIns[i].length));
        		 }
        	 }
        	 if(order.reSubIns.length>0){ 
        		 main.server.getServer("reSubmit", {
	                  data: d,
	                  async:false,
	                  success: function(json){
	                	  alert(json);
	                	  order.selectOrder();
	                  }
        		 });
        	 }
        	 order.stopBubble(e);
        });
    }
}
