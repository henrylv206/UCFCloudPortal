var service = {
	root : "/UCFCloudPortal",
	init : function(url) {

		$.ajaxSetup({
					cache : true
				});

		var solf = this;
		$.getScript(solf.root + "/js/server.js", function(e) {
					service.server = server;
					service.server.callback = function() {
						service.server.load("#service_tbody", ["myservice", "serviceTbody"]);
					};
					
					$("#serviceMenu").show();
					
					server.init();
					setDatepicker();       
						$(".sCont .i-date").datepicker({
				            changeMonth: true,
				            changeYear: true,
				            showWeek: true,
				            showButtonPanel: true,
				            dateFormat: "yy-mm-dd",
				            gotoCurrent : true,
				            showClearButton: true
				        });
					});
	},

};

var timeoutId = '';
var updateInterval = 10000;
var thread='';
var timerKey="my_server";
function update(){	//bug 0003745
	getAllServiceList();
    $("body").stopTime(timerKey);
    $("body").everyTime(10000,timerKey,function() {  
    	query($("#curPage").val());//bug 0003930
    });
}

function update1(){//bug 0003230
	if($("#popupDiv2").css('display')=='block'){
		return;
	}
    timeoutId = setTimeout(function(){
    	query($("#curPage").val());//fix bug 3638 修改自动刷新时仅刷新当前页
        setTimeout(arguments.callee, updateInterval);
    },updateInterval);
    Concurrent.Thread.sleep(10000);
    if(thread!=''){
       thread.kill();
       thread = Concurrent.Thread.create(update);//实现多线程
    }
}

function queryByOption(curPage) {//fix bug 7899 我的服务页面上，翻页功能不正常 
	$("#curPage").val(curPage);//fix bug 7854翻页后查询，当前页数置为1；
    queryService(curPage);//fix bug 3850
}
function query(curPage) {
    queryService(curPage);//fix bug 3850
}
function queryService(curPage) {
	//有弹出窗口时暂停页面刷新
    if($("#popupDiv1").css('display')!='none'){//bug 0003921
        $("body").stopTime(timerKey);
        return;
    }
    if($("#popupDiv2").css('display')!='none'){
        $("body").stopTime(timerKey);
        return;
    }
    update();//bug 0003745 
}

// 所有服务
function getAllServiceList() {
	if(curPage>0){
		$("#curPage").val(curPage); 
	}
	var key = $("#serviceName").val();
	var typeId = $('#typeId').val();
	var state = $('#state').val();
	var start = $('#start').val();
	var end = $('#end').val();
	start = start;
	end = end;
	//fix bug 3678 开始或者结束日期为空时，无需比较大小
	if(start != null && start != '' && end != null && end != ''){
	    if(start > end){
	    	alert('请选择正确的时间范围！');
	    	return ;
	    }
	}
	 
	var solf = this;
	param = {
		typeId : typeId,
		key : key,
		state : state,
		start : start,
		end : end
	};
	$.ajax({
		type : "POST",
		url : "../cloud_mall/showServiceList.action?curPage=" + $("#curPage").val() + "&pageSize=" + $("#pageSize").val(),
		datatype : "json",
		data : param,
		async : false,
		global : false,
		success : function(data) {
			if (data != null) {
				if (typeof(data) == "string" && "false" == data) {
					//dialog_confirmation('#dialog_confirmation',"失败：获取数据失败");
					return;
				} else {
					if (data.listResp.total != undefined) {
						$("#curPage").val(data.listResp.page);
						//$("#pageSize").val(data.listResp.size);
						var totalPage = Math.ceil(data.listResp.total / $("#pageSize").val());
						$("#total").val(totalPage);
		                $("#pageInfo").text(totalPage!=0?data.listResp.page+"/"+totalPage:0 + "/" + totalPage);
		                $("#totalPage").text(totalPage);
						$("#page_specified").val(data.listResp.page);//fix bug3638 更新我的服务当前页数为最新值
					}
					var array = data.listResp.list;

					$("#service_tbody").html("");

					if (array != null && array.length > 0) {

						$(array).each(function(i) {

							//奇偶行上色与焦点行事件处理   ninghao 2012-08-24 start
							//奇偶行上色
							var oddEven = (i%2) == 0 ? ' class=\"alt\" ' : ' ';
							//焦点行事件处理
							var focusEven = " onmouseover=$(this).addClass(\"over\") onmouseout=$(this).removeClass(\"over\") ";
							var trEvents = oddEven + focusEven;
							$("#service_tbody").append(tables(array[i],trEvents));
							//奇偶行上色与焦点行事件处理   ninghao 2012-08-24 end
						});
						
					}else{
						$("#pageInfo").text('0/0');
						$("#page_specified").val('0');//fix bug 3638 更新我的服务当前页数为最新值
						$("#service_tbody").html("<tr><td colspan=\"8\">&nbsp;&nbsp;&nbsp;&nbsp;没有找到记录！</td></tr>");
					}
				}
			}
		}
	});
}


function tables(info,trEvents) {
	var basePath = $("#basePath").val();
	var serviceId = info.id;
	var serviceName = info.sName;//bug 0003792
	var serviceNameAll=info.serviceName;
	var serviceDesc = info.sDesc;//bug 0003792
	var serviceTypeName = info.serviceTypeName;
	var start = (info.createDtStr + '').replace('.0', '');
	var end = (info.expiryDateStr + '').replace('.0', '');

	//是否已经过期，只做页面控制，数据库状态不变，续订后，继续可用 --- update by CQ
	var begintime_ms = Date.parse(info.expiryDateStr.replace(/-/g, "/"));
	var endtime_ms = Date.parse(new Date());
	var stepTime = endtime_ms-begintime_ms;
	if((begintime_ms-endtime_ms)<=0){
		//info.state = 8;//过期。//fix bug 7635 不存在过期的状态
	}
	 
	
	var state = info.state == 1 ?'申请开通':
						info.state == 2?'可用':
						info.state == 3?'正在开通'://bug 0003413 0003432 0003420 0003566
						info.state == 4?'作废':
						info.state == 5?'申请退订':
						info.state == 6?'正在退订'://bug 003506
						info.state == 7?'不可用':
						info.state == 8?'过期':'';
	serviceTypeName = serviceTypeName != undefined ? serviceTypeName.replace('模板','') : '';
	serviceDesc = serviceDesc != undefined ? serviceDesc.replace('模板','') : '';
	
	serviceDesc = serviceDesc != undefined && serviceDesc.length > 29 ? serviceDesc.substr(0,29) + ".." : serviceDesc;
	var serviceDescAll=info.serviceDesc;
	
	//alert(info.serviceType);
	//bug 0003079
	var caozuo11='       <a href=\"javascript:showContinueDiv('+ info.id +');\" title="续订"><img src=\"../images/icons/icon-right.png\" /></a>';
	//操作置灰 bug 0003373
	var caozuo12='       <img title="续订" class=\"caozuo12\" style=\"filter:alpha(opacity=30); -moz-opacity:.1;opacity:0.3;\" src=\"../images/icons/icon-right.png\" />';

    var caozuo21='     <a href=\"javascript:checkQuit(\'' + info.serviceType + '\',\'' + info.id + '\',\'' + info.resourcePoolsId + '\');\" title="退订"><img src=\"../images/icons/icon-del.png\" /></a>';
    //操作置灰
    var caozuo22='     <img title="退订" class=\"caozuo22\" style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/icons/icon-del.png\" />';

    var operStr=operate(info, caozuo11, caozuo12, caozuo21, caozuo22);
    //bug 0003346
    var param='';
    param+='serviceId='+serviceId;
    param+='&serviceState='+info.state;
    param+='&resourcePoolsId='+info.resourcePoolsId;
    param+='&zoneId='+info.zoneId;
    var href='<a method=\"get\" sendreferer=\"true\" class=\"quit\" href=\"'+service.root+'/jsp/my_resource.jsp?'+param+'\">'+ serviceName+ '</a>';
    href=info.state!=1&&info.state!=3&&info.state!=4&&info.state!=5&&info.state!=6?href:serviceName;    //bug 0003422
    
    //href = href != undefined && href.length > 10 ? href.substr(0, 10) + ".." : href;
    
	var dom = '' 
			+ '<tr '+trEvents+'>'
			+ '	<td width=\"6%\">'+ serviceId+ '</td>'
			+ '	<td width=\"17%\" style=\"line-height:18px;\" title=\"'+serviceNameAll+'\" >'+href+'</td>'//bug 0003170
			+ '	<td width=\"10%\" style=\"line-height:18px;\">'+ serviceTypeName+ '</td>'
			+ '	<td width=\"27%\" style=\"line-height:18px;\" title=\"'+serviceDescAll+'\">'+ serviceDesc+ '</td>'
			+ '	<td width=\"10%\" style=\"line-height:18px;\">'+ start+ '</td>'
			+ '	<td width=\"10%\" style=\"line-height:18px;\">'+ end+ '</td>'
			+ '	<td width=\"10%\" >'+ state+ '</td>'
			+ '	<td width=\"10%\" class=\"shop last \" id=\"caozuo_'+info.id+'\">'
			+operStr 
			+ '	</td>'
			+ '</tr>';
			dom=dom.replace('style=\"FILTER: gray\" ','style=\"filter:gray; -moz-opacity:.1;opacity:0.3;\"');
	return dom;
}

//c11:可续订、c12:不可续订、c21:可退订、c22:不可退订
function operate(info, c11, c12, c21, c22){
	var state=info.state;
	var rs='';
	switch(state){
		case 1://申请开通
		  rs=c12+c22;//此时也不容许操作了
		  break;
		case 2://可用
		  if(info.orderFlag!=0){
			 rs=c12+c21; 
		  }	
		  else rs=c11+c21;
		  break;
		case 3://正在开通
		  rs=c12+c22;
		case 4://作废 bug 0003348 0003371
		  rs=c12+c22;
		case 5://申请退订
		case 6://正在退订
		  rs=c12+c22;
		  break;
		case 7://不可用 to fix bug [5318]
		  rs=c12+c22;
		  break;
		case 8://过期，可以续订，退订
		  if(info.orderFlag!=0){
		    rs=c12+c21; 
		  }	
		  else rs=c11+c21;
		  break;
		default:
		  if(info.orderFlag!=0){
			rs=c12+c21;  
		  } 	 
		  else rs=c11+c21;
	}	
	return rs;
}
// 所有服务的翻页
function turnPage(direc, step) {
	if (direc == 1) {//bug 0003095
		if (step == 0) {
			$("#curPage").val($("#total").val());
		} else {
			var nextPage=parseInt($("#curPage").val()) + parseInt(step);
			if(nextPage>$("#total").val()){
				return;
			}
			$("#curPage").val(nextPage);
		}
	} else {
		if (step == 0) {
			$("#curPage").val(1);
		} else {
			var prePage=parseInt($("#curPage").val()) - parseInt(step);
			if(prePage<1){
				return;
			}
			$("#curPage").val(prePage);
		}
	}//bug 0003104
    $('#page_specified').val($("#curPage").val());
	query();
}

//到指定页
function turnSpecified() {
	var scoreReg = /^[1-9]\d*$/;
	var specPage=$("#page_specified").val();
	if (!scoreReg.exec(specPage)) {
		dialog_confirmation('#dialog_confirmation', "只能输入正整数");
		$("#page_specified").focus();
		return;
	}//bug 0003104
	//$('#page_specified').val(specPage);
	if (parseInt(specPage) > parseInt($("#total").val())) {
		$("#curPage").val($("#total").val());
	} else {
		$("#curPage").val(specPage);
	}
	$("#page_specified").val($("#curPage").val());
	query();
}

 // 日期datepicker控件中文设置 to fix bug 4607
    function setDatepicker(){
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
    }
    
    