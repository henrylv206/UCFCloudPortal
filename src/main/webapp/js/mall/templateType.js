$(function (event) {//bug 0003061
	//alert("templateType.js");
    $("#typBtn").blur(function (){    
        $("#tempType").hide('fast');
    });
    $("#typeBtn").click(function (event){    	    	    	
            //取消事件冒泡
           event.stopPropagation();
           //设置弹出层的位置
            var offset = $(event.target).offset();
            //$('#tempType').css({top: offset.top + $(event.target).height() + "px", left: offset.left });
             //按钮的toggle,如果div是可见的,点击按钮切换为隐藏的;如果是隐藏的,切换为可见的。
            
            $('#tempType').toggle('fast');
    }); 

     //点击空白处或者自身隐藏弹出层，下面分别为滑动和淡出效果。
     $(document).click(function (event) { $('#tempType').slideUp('fast'); });
     $('#tempType').click(function (event) { $(this).fadeOut(10); });
    
}); //bug 0003435
function listTemplateType() {
    var url = "../cloud_mall/listTemplateType.action";

    $.ajax({
        type : "post",
        url : url,
        data : {},
        //contentType : "application/json",
        dataType : 'json',
        async : false,
        success : function(data) {
            var arrays = data;

            $("#tempType_ul").html("");
            if (arrays != null && arrays.length > 0) {  
                $(arrays).each(
                        function(i) {
                            var info = arrays[i];
                            $("#tempType_ul").append(divhtml(info));
                        });
            }           
        }
    });
}
function listTemplateType2() {
    var url = "../cloud_mall/listTemplateType.action";

    $.ajax({
        type : "post",
        url : url,
        data : {},
        //contentType : "application/json",
        dataType : 'json',
        async : false,
        success : function(data) {
            var arrays = data;

            $("#typeId").html("");
            if (arrays != null && arrays.length > 0) {  
                $(arrays).each(
                        function(i) {
                            var info = arrays[i];
                            $("#typeId").append(divhtml2(info));
                        });
            }           
        }
    });
}

//function listResourcePool(username, psw) {
//    var url = "../resourcePools/getResourcePool.action";
//    
//    $.ajax({
//	    type : "post",
//        url : url,
//        data : {id:1},       
//        dataType : 'json',
//        async : false,
//        success : function(data) {
//            $("#newlink").html('<a href="http://' + data.ip + ':8080/SkyFormRes/templogin.jsp?username=' + username + '&encrypt='+ psw + '&domain=' + username + '" target="_blank">资源管理平台</a>' );
//        }
//    });
//}

function divhtml(info){
    var id=info.id;
    var name=info.templateName;
    var dom = ''
        +'<li onclick=\"change('+id+', \''+name+'\');\">'
        +'      <a >'+name+'</a>'
        +'</li>';
    return dom;
}

function divhtml2(info){
    var id=info.id;
    var name=info.templateName;
    var dom = '<option value=\"'+id+'\">'+name+'</option>';
    return dom;
}
function change(id, name){
    $("#typeId").val(id);
    $("#typeName").val(name);
    
    $("#tempType").hide("fast");
    query();
}
function closSelect(){
    $("#tempType").hide('fast');
}

function displayDiv(){
    $("#typeBtn").focus();
    $("#tempType").toggle("fast");  
}

function closeContinueDiv(){
    //$("#popupDiv1").hide();
    $("#popupDiv1").fadeOut(300);
}

function showContinueDiv(id){    
    getInstancePeriodInfo(id);      
    //$("#popupDiv1").show();
    $("#popupDiv1").fadeIn(300);
}

function closeQuitDiv(){
    $("#popupDiv2").fadeOut(300);
    (function(){query(1);});
}

function showQuitDiv(){
    $("#popupDiv2").fadeIn(300);     
}

function getInstancePeriodInfo (id) {
    
    var data = {instanceId: id};        
        $.ajax({
            url : "/UCFCloudPortal/order/findServiceInstancePeriodById.action?id="+id,
            type : 'POST',
            dataType : 'json',
            data : data,
            success : function(data) { 
                if (data != null) {                 
                    $("#old_period").html(data.buyPeriod);
                    $("#due_date").html(data.expiryDateString);                         
                    $("#cycle").html(data.price+"元/"+data.unitString);                                              
                    $("#xd_unit").val(data.unit);
                    $("#xd_id").val(id);
                }
            }
        });
    }


    function updateServicePeriod(){
        //to fix bug [3591]  
        var ex = /^\d+$/;
        if (ex.test($("#tilength").val())) {            
        } else {
            alert("请填写正确续订时长！");
            $("#tilength").focus();
            return;
        }
        $.ajax({
            url : "/UCFCloudPortal/order/updateServicePeriod.action?id="+Number($("#xd_id").val()),
            type : 'POST',
            data : {unit:$("#xd_unit").val(),num:Number($("#tilength").val())},
            dataType : 'json',
			async : false,
            success : function(data) {
                if(data.indexOf("error") == 0) {
                    alert("失败："+data);
                }else{
                    alert(data);
                    $("#popupDiv1").hide();
                    $("#tilength").val(1);
                }
		        $("#popupDiv1").hide();
		        query($("#curPage").val());   //刷页面
            }
        });
}

    function executeQuit() {        
        if(commonUtils.len($("#vm_reason").val()) == 0 || commonUtils.len($("#vm_reason").val()) > 20 ) {
            alert("申请理由限定1~20个字符（一个汉字算2个字符）");
            $("#vm_reason").focus();
            return;
        }       
        var storageSize = $("#storageSize").val();      
        var serviceType = $("#serviceType").val();      
        //to fix bug [3392]
        //多虚机
        if (serviceType==50){
            executeVMSQuit();           
        }
        
        //虚拟机
        if (serviceType==1){
            executeVMQuit();            
        }
        //虚拟硬盘
        if (serviceType==2){
            executeDiskQuit();  
        }       
        //小型机
        if (serviceType==3){
            executeMCQuit();    
        } 
        //备份
        if (serviceType==4){
            executeBKQuit();    
        } 
        //云监控
        if (serviceType==5){
            executeMOQuit();    
        }       
        //负载均衡
        if (serviceType==6){
            executeLBQuit();    
        }   
        //防火墙
        if (serviceType==7){
            executeFWQuit();    
        }   
        //公网带宽
        if (serviceType==8){
            executeBWQuit();    
        }           
        //弹性公网IP
        if (serviceType==9){
            executeIPQuit();    
        }
        //物理机
        if (serviceType==10){
            executePMQuit();    
        }       
        //对象存储
        if (serviceType==11){
            executeOSQuit();    
        }
        //IPSAN to fix bug [3659]
        if (serviceType==12){
            executeIPSANQuit(); 
        }        
        
        if (serviceType==13){
            executeNASQuit(); 
        } 
        
        //window.location.href='my_service.jsp';        
    }

    function executeBWQuit() {      
        $("#popupDiv2").hide();
        var url = "../resourcesUsed/bwDestroy.action";
        var data = {};
        data.apply_reason =  $("#vm_reason").val();
        data.bandWidthinstanceId = $("#vmID").val();        
        data.serviceID  = $("#serviceID").val();      
        
        $.ajax({
            url : url,
            type : 'POST',
            async : false,
            data : data,
            dataType : 'json',
            success : function(data) {
                if (data.search("失败") != -1) {
                    alert(data);
                    return false;
                } else {                    
                    alert("公网带宽服务退订申请已成功提交！");
                    $("#vm_reason").val("");
                    query($("#curPage").val());//bug 0003826
                }               
            }
        });
    }
    
    function executeMOQuit() {      
        $("#popupDiv2").hide();
        var url = "/UCFCloudPortal/portal_mo/moDestroy.action";
        var data = {};
        data.reason     =  $("#vm_reason").val();  
        data.instanceId = $("#vmID").val();         
        data.serviceID  = $("#serviceID").val();      
        $.ajax({
            url : url,
            type : 'POST',
            async : false,
            data : data,
            dataType : 'json',
            success : function(data) {
                if (data.search("失败") != -1) {
                    alert(data);
                    return false;
                } else {                    
                    alert("云监控服务退订申请已成功提交！");
                    $("#vm_reason").val("");
                    query($("#curPage").val());//bug 0003826

                    if(checkHasMonitor()){//判断是否申请了云监控
                        $.post("/UCFCloudPortal/jsp/set_session_value.jsp?mon=true");//更新SESSION
                    }else{//fix bug 3710 可用的云监控服务
                        $.post("/UCFCloudPortal/jsp/set_session_value.jsp?mon=false");//更新SESSION
                    }
                }               
            }
        });
    }
    
    function executeIPQuit() {
        $("#popupDiv2").hide();//bug 0003930
        
        var url = "/UCFCloudPortal/publicIp/IpDestroy.action";
        var data = {};
        data.apply_reason =  $("#vm_reason").val();  
        data.publicIpInstanceId = $("#vmID").val();  
        data.publicIp = $("#pipinstanceName").val();  
        data.serviceID  = $("#serviceID").val();      
                                
        $.ajax({
            url : url,
            type : 'POST',
            data : data,
            async : false,
            dataType : 'json',
            timeout:3000000,
            success : function(data) {
                if (data != null) {                 
                    if(data == "操作成功!") {
                        alert('弹性公网IP服务退订申请已成功提交！');
                        $("#vm_reason").val("");
                        query($("#curPage").val());//bug 0003826
                    } else {
                        alert(data);
                        $("#vm_reason").val("");
                    }                   
                }
            }
        });
    }   
    
    //to fix bug [3680]
    function executeLBQuit() {  
        var datac = {};
        $("#popupDiv2").hide();
        datac.instance_id = $("#vmID").val();
        $.ajax({
            type : "POST",
            url : "../portal_lb/checkBindTipByLb.action",
            datatype : "json",                  
            data : datac,
            async : false,
            global : false,
            success : function(json) {
                if (json != null) {                 
                    if(json != "ok") {                      
                        alert(json);
                        $("#vm_reason").val("");
                    }else{
                    	 var data = {};
                         data.reason = $("#vm_reason").val();
                         data.instance_id = $("#vmID").val();
                         data.serviceID   = $("#serviceID").val();                    
                         $.ajax({
                             type : "POST",
                             url : "../portal_lb/lbDestroy.action",
                             datatype : "json",  
                             timeout:5000000,
                             data : data,
                             async : false,
                             global : false,
                             success : function(data) {
                                 alert('负载均衡服务退订申请已成功提交！');
                                 $("#vm_reason").val("");
                                 query($("#curPage").val());//bug 0003826
                             }
                         });                        
                    }
                }                               
            }
        }); 
    }
    
    function executeFWQuit() {      
        $("#popupDiv2").hide();
        var data1 = {};
        data1.reason = $("#vm_reason").val();
        data1.instanceId = $("#vmID").val();
        data1.serviceID   = $("#serviceID").val();                   
        $.ajax({
            type : "POST",
            url : "../vdc_fwmanage/fwDestroy.action",
            datatype : "json",  
            data : data1,
            async : false,
            global : false,
            success : function(json) {
                 alert('防火墙服务退订申请已成功提交！');
                $("#vm_reason").val("");
                query($("#curPage").val());//bug 0003826
            }
        });
    }   
    
    function executeOSQuit() {
        $("#popupDiv2").hide();
        
        var data = {};
        data.reason = $("#vm_reason").val();
        data.instanceId = $("#vmID").val();
        data.serviceID    = $("#serviceID").val();      
 
        $.ajax({
            type : "POST",
            url : "../vdc_fwmanage/OSDestroy.action",
            datatype : "json",  
            data : data,
            async : false,
            global : false,
            success : function(data) {
                alert('对象存储服务退订申请已成功提交！');
                $("#vm_reason").val("");
                query($("#curPage").val());//bug 0003826
            }
        });
    }   
    
    function executeNASQuit() {
        $("#popupDiv2").hide();
        
        var data = {};
        data.reason = $("#vm_reason").val();
        data.instance_id = $("#vmID").val();
        data.serviceID = $("#serviceID").val();    
 
        $.ajax({
            type : "POST",
            url : "${pageContext.request.contextPath}/resourcesUsed/nasResourceOperate!NASDestroy.action",
            datatype : "json",  
            data : data,
            async : false,
            global : false,
            success : function(data) {
        	alert('文件系统服务退订申请已成功提交！');
                $("#vm_reason").val("");
                query($("#curPage").val());
            }
        });
    }    
    
    function executeIPSANQuit() {
        $("#popupDiv2").hide();
        var queryJsonObj={};
        queryJsonObj.volumeinstanceId = $("#vmID").val();
        queryJsonObj.reason = $("#vm_reason").val();
        queryJsonObj.serviceID = $("#serviceID").val();
 
        $.ajax({
            type : "POST",
            url : "${pageContext.request.contextPath}/resourcesUsed/destroyIPSan.action",
            datatype : "json",  
            data : {queryJson:$.toJSON(queryJsonObj)},
            async : false,
            global : false,
            success : function(data) {
                alert(data);
                $("#vm_reason").val("");
                query($("#curPage").val());//bug 0003826
            }
        });
    }
    
    function executeMCQuit() {
        $("#popupDiv2").hide();
        var url = "hcModify!hcDestroy.action";
        var data = {};
        data.apply_reason = $("#vm_reason").val();
        data.serviceID    = $("#serviceID").val();      
        data.hcID         = $("#vmID").val();
        data.instanceId   = $("#instanceId").val();
        data.clusterId    = $("#ClusterId").val();
        $.ajax({
            url : "${pageContext.request.contextPath}/"+url+"",
            type : 'POST',
            data : data,
            async : false,
            dataType : 'json',
            success : function(data) {
                if(data.search("error")!=-1){
                    alert(data);
                    return false;
                }else{
                    alert('小型机服务退订申请已成功提交！');
                    $("#vm_reason").val("");
                    query($("#curPage").val());//bug 0003826
                }
            }
        });
    }
    
    function executePMQuit() {      
        $("#popupDiv2").hide();
        var url = "pmModify!pmApplyDestroy.action";
        var data = {};
        data.apply_reason = $("#vm_reason").val();
        data.serviceID    = $("#serviceID").val();
        data.pmID         = $("#vmID").val();
        data.instanceId   = $("#instanceId").val();
        //data.clusterId    = $("#ClusterId").val();
        $.ajax({
            //url : "${pageContext.request.contextPath}/"+url+"",
            url : "../resourcesUsed/"+url+"",
            type : 'POST',
            data : data,
            async : false,
            dataType : 'json',
            success : function(data) {
                if(data.search("error")!=-1){
                    alert(data);
                    return false;
                }else{
                    alert('物理机服务退订申请已成功提交！');
                    $("#vm_reason").val("");
                    query($("#curPage").val());//bug 0003826
                }
            }
        });
    }
    

    //to fix bug [3274 3334]
    function executeBKQuit() {       
        $("#popupDiv2").hide();
        var data = {};
        data.reason = $("#vm_reason").val();    
        data.instanceId = $("#vmID").val(); 
        data.serviceID = $("#serviceID").val();
        $.ajax({
            type : "POST",
            url : "../vdc_bkmanage/backupDestroy.action",
            datatype : "json",  
            data : data,
            async : false,
            global : false,
            success : function(data) {
              alert(data);
              $("#vm_reason").val("");
              query($("#curPage").val());//bug 0003826
            }
        });     
    }
    
    function executeDiskQuit() {        
        $("#popupDiv2").hide();     
        var queryJsonObj={};
        queryJsonObj.volumeinstanceId=$("#vmID").val(); 
        queryJsonObj.reason = $("#vm_reason").val();        
        queryJsonObj.serviceID = $("#serviceID").val();     
        $.ajax({
            url : "${pageContext.request.contextPath}/resourcesUsed/diskDestroy.action",
            type : 'POST',
            data : {queryJson:$.toJSON(queryJsonObj)},
            dataType : 'json',
            async : false,
            success : function(data) {
                if(data.search("error")!=-1){
                    alert(data);
                }else{                   
                    alert('虚拟硬盘服务退订申请已成功提交！');      
                    $("#vm_reason").val('');
                    query($("#curPage").val());//bug 0003826 0003921
                }
            }
        });     
    }
    
    function executeVMSQuit() {      
        $("#popupDiv2").hide();    
        var dataQuit = {};
        dataQuit.serviceID    = $("#serviceID").val();
        dataQuit.apply_reason = $("#vm_reason").val();
        $.ajax({
            type : "POST",
            url : "../resourcesUsed/vmModify!vmsDestroy.action",
            datatype : "json",  
            data : dataQuit,
            async : false,
            global : false,
            success : function(data) {
                if(data.search("error")!=-1){
                    alert(data);
                }else{                   
                    alert('多实例服务退订申请已成功提交！');
                    $("#vm_reason").val('');
                    query($("#curPage").val());//bug 0003826
                }                                   
            }
        });
    }
            
    //to fix bug [3513 3430]
    function executeVMQuit() {
        $("#popupDiv2").hide();//bug 0003776
        
        var data = {};
        data.serviceID    = $("#serviceID").val();
        data.apply_reason = $("#vm_reason").val();
        data.vmID         = $("#vmID").val();
        data.instanceId   = $("#instanceId").val();
        data.ClusterId    = $("#ClusterId").val();
        data.res_code     = $("#res_code").val();    
            
        var ret = vmModify("vmModify!vmDestroy.action", data);
        if(ret){//fix bug 0003570 判断是否执行退订成功
            //to fix bug [3923]
            alert('虚拟机服务退订申请已成功提交！');     //bug 0003475
            $("#vm_reason").val('');
        }else{
            //alert('该虚拟机退订提交失败!');//fix bug 0003570
        }
    }   
    
    function vmModify(actionName, params) {
        var returnFlag = false;//fix bug 0003570 3723
        $.ajax({
            url : "${pageContext.request.contextPath}/" + actionName + "",
            type : 'POST',
            async : false,
            data : params,
            dataType : 'json',
            success : function(data) {
                if (data.indexOf("成功") == -1) {
                    alert('该虚拟机退订提交失败 ： '+data);//fix bug 0003570
                    returnFlag = false;
                }else{
                    returnFlag = true;
                    query($("#curPage").val());//bug 0003826 0003945
                }
            }
        });
        return returnFlag;
    }
    
    function checkQuit(serviceType, ID, resourcePoolsId) {    	    	
    	
        if(serviceType == 50){  
            
            var showQuitVms = -1;
            
            var vmid;
            var eInstanceId;
            var clusterId;
            var resCode;        
            var serviceName;
             
            $.ajax({
                type : "POST",
                url : "../cloud_mall/showQuitInstance.action",
                datatype : "json",  
                data : {serviceID:ID},
                async : false,
                global : false,
                success : function(data) {
                    for (var i = 0; i < data.listResp.list.length; i++) {
                        if(showQuitVms!=-1){
                            break;
                        }
                        //to fix bug [3732]
                        //to fix bug [3716]
                        serviceName = data.listResp.list[i].serviceName;
                        if(data.listResp.list[i].state == 6 || data.listResp.list[i].state == 3){
                            showQuitVms = 0;                        
                        }
                        //to fix bug [3723]
                        vmid = data.listResp.list[i].vmid;
                        eInstanceId = data.listResp.list[i].eInstanceId;
                        clusterId = data.listResp.list[i].clusterId;
                        resCode = data.listResp.list[i].resCode;
                        
                        var params = {
                            vmID : data.listResp.list[i].vmid,
                            instanceId : data.listResp.list[i].eInstanceId,
                            clusterId : data.listResp.list[i].clusterId,
                            res_code : data.listResp.list[i].resCode,
                            apply_reason : ''
                        };      
                        
                        $.ajax({
                            url : "../resourcesUsed/vmModify!getBindIPsan.action",
                            type : 'POST',
                            timeout:5000000,
                            async : false,
                            data : params,
                            dataType : 'json',
                            success : function(data7) {                           	 
                                if (data7>0) {
                                	showQuitVms = 7;
                                } else {
			                        $.ajax({
			                            url : "../resourcesUsed/vmModify!getSnapshotsByVolumeId.action",
			                            type : 'POST',
			                            timeout:5000000,
			                            async : false,
			                            data : params,
			                            dataType : 'json',
			                            success : function(data1) {//to fix bug [3942] 
			                                if (data1 != "[]" && data1!=undefined && data1!=null) {
			                                    showQuitVms = 1;
			                                } else {                     
			                                    $.ajax({
			                                        url : "../resourcesUsed/vmModify!checkVM2EBS.action",
			                                        type : 'POST',
			                                        timeout:5000000,
			                                        async : false,//bug 0003948
			                                        data : params,
			                                        dataType : 'json',
			                                        success : function(data2) {
			                                            if(data2>0){
			                                                showQuitVms = 2;
			                                            } else {
			                                                $.ajax({
			                                                    type : "POST",
			                                                    url : "../resourcesUsed/vmModify!checkVM2EBSing.action",
			                                                    data: params,
			                                                    timeout:5000000,
			                                                    async:false,
			                                                    success : function(data6) {
			                                                        if(data6>0){
			                                                            showQuitVms = 6;
			                                                        } else {                                                        	
			                                                            $.ajax({
			                                                                url : "../resourcesUsed/vmModify!checkVMBindIP.action",
			                                                                type : 'POST',
			                                                                timeout:5000000,
			                                                                async : false,
			                                                                data : params,
			                                                                dataType : 'json',
			                                                                success : function(data3) {
			                                                                    if(data3>0){
			                                                                        showQuitVms = 3;
			                                                                    } else {
			                                                                        $.ajax({
			                                                                            type : "GET",
			                                                                            url : "../portal_lb/getLoadBalanceInstance.action",
			                                                                            timeout:5000000,
			                                                                            async:false,
			                                                                            success : function(data4) {
			                                                                                if(data4 == null || data4.virtualServiceName == null){                                                      
			                                                                                } else {
			                                                                                    $.ajax({
			                                                                                        type : "POST",
			                                                                                        url : "../portal_lb/queryServiceList.action",//bug 0003809
			                                                                                        data:{
			                                                                                            "serviceGroupName":data4.serverGroupName,//bug 0003949
			                                                                                            "s_protocolType":data4.lbProtocol
			                                                                                        },
			                                                                                        timeout:5000000,
			                                                                                        async:false,
			                                                                                        success : function(data5) {
			                                                                                            if(null!=data5&&data5.length>0){
			                                                                                                for(var i=0;i<data5.length;i++){
			                                                                                                    if (data5[i].vmId == vmid){
			                                                                                                        showQuitVms = 5;
			                                                                                                    } 
			                                                                                                }
			                                                                                            }
			                                                                                        }
			                                                                                    });                 
			                                                                                }
			                                                                            }
			                                                                        }); 
			                                                                    }
			                                                                }
			                                                            }); 	                                                        	
			                                                        }
			                                                    }
			                                                }); 	  
			                                            }
			                                        }
			                                    });
			                                }
			                            }
			                        });                                                                           
                        		}
                    		}
                    	});                                                   
                    }                   
                }
            });                                     

            if(0==showQuitVms){
                alert("虚拟机实例" + serviceName + "正在执行用户请求命令，请等待该命令执行完成！");        
                return;                                         
            }else if(1==showQuitVms){
                alert('虚拟机实例' + serviceName + '有快照备份，请先删除该快照备份');          
                return;
            }else if(2==showQuitVms){
                alert('虚拟机实例' + serviceName + '已经挂载虚拟硬盘，请先解挂！');        
                return;             
            }else if(3==showQuitVms){
                alert('虚拟机实例' + serviceName + '已经绑定弹性公网IP，请先解绑定！');         
                return;             
            }else if(5==showQuitVms){      
                alert('虚拟机实例' + serviceName + '已经绑定弹性负载均衡，请先解绑定！');         
                return;
            }else if(6==showQuitVms){      
                alert('虚拟机实例' + serviceName + '正在进行挂载或解挂虚拟硬盘的操作，请等待该操作完成！');         
                return;
            }else if(7==showQuitVms){      
                alert('虚拟机实例' + serviceName + '已经挂载弹性块存储，请先解挂！');         
                return;
            }
            if (showQuitVms){
                $("#serviceType").val(serviceType);
                $("#serviceID").val(ID);
                $("#vmID").val(vmid);
                $("#instanceId").val(eInstanceId);
                $("#ClusterId").val(clusterId);
                $("#res_code").val(resCode);                                                            
                showQuitDiv();
            }   
            
        }
        
        var vmID;
        var instanceId;
        var ClusterId;
        var res_code;
        var state;
        var orderId;
        
        //to fix bug [3264 3378]
        $.ajax({
            type : "POST",
            url : "../cloud_mall/showQuitInstance.action",
            datatype : "json",  
            data : {serviceID:ID},
            async : false,
            global : false,
            success : function(data) {
                vmID = data.listResp.list[0].vmid;
                instanceId = data.listResp.list[0].eInstanceId;
                ClusterId = data.listResp.list[0].clusterId;
                res_code = data.listResp.list[0].resCode;    
                state = data.listResp.list[0].state;
                orderId = data.listResp.list[0].orderId;
            }
        }); 
        
        if(serviceType == 1){            
            checkVMQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code, state, resourcePoolsId);
        }
        if(serviceType == 2){
            checkvDiskQuit(storageSize, serviceType, ID, vmID, instanceId, ClusterId, res_code, state, resourcePoolsId);
        }       
        if(serviceType == 3){
            checkMCQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code);
        }       
        //备份
        if(serviceType == 4){
            checkBKQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, state);
        }
        if(serviceType == 5){
            checkMOQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code);
        }   
        //负载均衡
        if(serviceType == 6){
            checkLBQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, orderId);
        }
        //防火墙
        if(serviceType == 7){
            checkFWQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, orderId);
        }
        if(serviceType == 8){
            checkBWQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code);
        }       
        if(serviceType == 9){
            checkIPQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code);
        }
        if(serviceType == 10){
            checkPMQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code);
        }
        //bug 0006863 0006752
        if(serviceType == 11){
            checkOSQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, state);
        }
        if(serviceType == 12){
            checkIPSANQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, state);
        }    
        
        if(serviceType == 13){
            checkNASQuit(serviceType ,ID, vmID, instanceId, ClusterId, res_code, state);
        }    
        
    }
    
    function checkBWQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code) {
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        showQuitDiv();      
    }       
    
    function checkMOQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code) {
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        showQuitDiv();      
    }   
    
    function checkMCQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code) {
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        showQuitDiv();      
    }   
    
    function checkIPQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code) {
        var datac = {};
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        datac.publicIpInstanceId = $("#vmID").val();  
        $.ajax({
                type : "POST",
                url : "/UCFCloudPortal/publicIp/checkBindRsIdByIp.action",
                datatype : "json",                  
                data : datac,
                async : false,
                global : false,
                success : function(json) {
                    if(json=='ok'){
                        showQuitDiv();
                    }else{
                        confirm(json);
                        return;
                    }                   
                }
        }); 
    }   
    
    function checkLBQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code, orderId) {
        $("#order_Id").val(orderId);    
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);       
        var datac = {};
        datac.instance_id = $("#vmID").val();
        if(checkLoadBalaAboutVM()){
            confirm('此负载均衡还指派有虚拟机，还不能退订！');
            return ;
        }else{
        	//解除和FW的捆绑 update by CQ SkyForm1.3
//            $.ajax({
//                type : "POST",
//                url : "../portal_lb/checkBindTipByLb.action",
//                datatype : "json",                  
//                data : datac,
//                async : false,
//                global : false,
//                success : function(json) {
//                    if (json != null) {                 
//                        if(json != "ok") {                      
//                            alert(json);
//                            $("#vm_reason").val("");
//                            $("#popupDiv2").fadeOut(300);
//                        }else{
//                            $.ajax({
//                                type : "POST",
//                                url : "../cloud_mall/checkLB2FW.action",
//                                datatype : "json",  
//                                data : {orderId:orderId},
//                                async : false,
//                                global : false,
//                                success : function(data) {
//                                    if (data.length == 2) {
//                                        if(confirm('您所申请的防火墙需要一起退订，是否继续？')){
//                                            for(var i=0;i<2;i++){                                           
//                                                if(data[i].serviceType == 7){                                           
//                                                    $.ajax({
//                                                        type : "POST",//bug 0003890
//                                                        url : "../vdc_fwmanage/checkRuled4Fw.action",//bug 0003810
//                                                        datatype : "json",  
//                                                        data : {instanceId:data[i].vmid},
//                                                        async : false,
//                                                        global : false,
//                                                        success : function(data1) {     
//                                                            if(data1!='ok'){
//                                                                confirm(data1);
//                                                            }else{                                                      
//                                                                showQuitDiv();  
//                                                            }
//                                                        }
//                                                    }); 
//                                                }   
//                                            }
//                                        }
//                                    }
//                                    if (data.length == 1) {
//                                        showQuitDiv();  
//                                    }               
//                                }
//                            }); 
//                        }
//                    }
//                }
//            });
        	showQuitDiv(); 
        }
    }   
    
    function checkFWQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code, orderId) { 
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        $("#order_Id").val(orderId);    
       $.ajax({
            type : "POST",
            url : "../vdc_fwmanage/checkRuled4Fw.action",//bug 0003810
            datatype : "json",  
            data : {instanceId:$("#vmID").val()},
            async : false,
            global : false,
            success : function(data1) {     
                if(data1!='ok'){
                    alert(data1);
                }else{
                	//解除和LB的捆绑 update by CQ SkyForm1.3
//                    $.ajax({
//                        type : "POST",
//                        url : "../cloud_mall/checkLB2FW.action",
//                        datatype : "json",  
//                        data : {orderId:orderId},
//                        async : false,
//                        global : false,
//                        success : function(data) {
//                            if (data.length == 2) {
//                                if(confirm('您所申请的负载均衡需要一起退订，是否继续？')){
//                                	   var flag=0;
//                                        for (var i = 0; i < data.length; i++) {                      
//                                            if(data[i].serviceType == 6){
//                                            	 var datac={};
//                                                  datac.instance_id = vmID;
//                                            	 $.ajax({//bug 0003977
//                                                    type : "POST",
//                                                    url : "../portal_lb/checkBindTipByLb.action",
//                                                    datatype : "json",                  
//                                                    data : datac,
//                                                    async : false,
//                                                    global : false,
//                                                    success : function(json) {
//                                                        if (json != null) {                 
//                                                            if(json != "ok") {                                         	      
//                                                                alert(json);
//                                                                flag=1;
//                                                            }
//                                                        }
//                                                    }
//                                            	 });
//                                            }
//                                        }
//                                     if(checkLoadBalaAboutVM()){//bug 0003890
//                                        confirm('此负载均衡还指派有虚拟机，还不能退订！');
//                                        flag=1;
//                                     }
//                                     if(flag==0){
//                                        showQuitDiv();
//                                     }
//                                }
//                            }
//                            if (data.length == 1) {
//                                showQuitDiv();
//                            }               
//                        }
//                    });
                }	showQuitDiv();
            }
        });     
    }   
    
    function checkOSQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code, state) {
    	//bug 0006752
        if(state == 3||state == 6){
            alert("该对象存储正在执行变更命令，请等待该命令执行完成！");
            return;
        }
        
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        showQuitDiv();      
    }
    
    function checkIPSANQuit(serviceType, ID, vmID, instanceId ,ClusterId, res_code, state) {
    	
        if(state == 3){
            alert("该弹性块存储正在执行变更命令，请等待该命令执行完成！");
            return;
        }
        
        var params2 = {
        	vmID : vmID
        };  
        
        $.ajax({            
        	url : "../resourcesUsed/vmModify!getIPsanBindVM.action",
            type : 'POST',
            data : params2,
            dataType : 'json',
            success : function(data) {
        	    //to fix bug [7683]
        	    //to fix bug [7813]
                if(data=="1"){
                    alert("该弹性块存储已经挂载虚拟机，请先解挂。");
                    return;
                } else {                	
                    $("#serviceType").val(serviceType);
                    $("#serviceID").val(ID);
                    $("#vmID").val(vmID);
                    $("#instanceId").val(instanceId);
                    $("#ClusterId").val(ClusterId);
                    $("#res_code").val(res_code);   
                    showQuitDiv();                	                	
                }
            }
        });        
        
    	
   
    }   
    
    function checkNASQuit(serviceType, ID,vmID,instanceId,ClusterId,res_code, state) {
    	//bug 0006752
        if(state == 3){
            alert("该NAS存储正在执行变更命令，请等待该命令执行完成！");
            return;
        }
        
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code);   
        showQuitDiv();      
    }     
    
    //to fix bug [3711]
    function checkBKQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code, state) {
        //to fix bug [3730 3726]
        if(state == 3){
            alert("该虚拟机备份正在执行变更命令，请等待该命令执行完成！");
            return;
        }
        
        $.ajax({
            type : "POST",
            url : "../vdc_bkmanage/checkbackupDestroy.action",
            datatype : "json",  
            data : {},
            async : false,
            global : false,
            success : function(data) {
                if(data==0){
                    alert('该备份服务已生成快照,请删除所有快照再作废!');  
                }
                if(data==1){
                    $("#serviceType").val(serviceType);
                    $("#serviceID").val(ID);
                    $("#vmID").val(vmID);
                    $("#instanceId").val(instanceId);
                    $("#ClusterId").val(ClusterId);
                    $("#res_code").val(res_code);   
                    showQuitDiv();
                }             
            }
        });             
    }
    
    function checkPMQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code) {    	    	
        var params2 = {
            	vmID : vmID
        };  
            
        $.ajax({            
        	url : "../resourcesUsed/vmModify!checkVM2EBS.action",
            type : 'POST',
            data : params2,
            dataType : 'json',
            success : function(data) {        	        		        	
        	    //to fix bug [7807]
                if(data>0){//to fix bug [7807]
                    alert("该物理机已经挂载块存储，请先解挂。");
                    return;
                } else {                	
                    $("#serviceType").val(serviceType);
                    $("#serviceID").val(ID);
                    $("#vmID").val(vmID);
                    $("#instanceId").val(instanceId);
                    $("#ClusterId").val(ClusterId);
                    $("#res_code").val(res_code);   
                    showQuitDiv();                 	                	
                }
            }
        });      	    	    	    
    }
    
    function checkvDiskQuit(storageSize, serviceType, ID, vmID, instanceId, ClusterId, res_code, state, resourcePoolsId) {        
        //to fix bug [3690]
        if(state == 6){//to fix bug [7422]
            alert("该虚拟硬盘正在执行挂载或解挂或快照相关命令，请等待该命令执行完成！");
            return;
        }
        
        var params = {
        	vmID:ID,
            instanceID : vmID,
            resourcePoolsId:resourcePoolsId
        };  
        var params2 = {
        		volumeinstanceId : vmID,
                resourcePoolsId:resourcePoolsId
            };         
        //to fix bug [3503]
        $.ajax({            
            url : "${pageContext.request.contextPath}/vdc_volumemanage/checkDiskBind.action",
            type : 'POST',
            data : params,
            dataType : 'json',
            success : function(data) {
                if(data.totalcount>0){
                    alert("该虚拟硬盘已经挂载虚拟机，请先解挂。");
                    return;
                } else {
                	//to fix bug [5271]
                    $.ajax({                        
                    	url : "${pageContext.request.contextPath}/resourcesUsed/listSnapshotsByVolumeId.action",
                        type : "POST",
                        data : params2,
                        timeout:5000,
                        success : function(data) {                                         	
                            if(null!=data && data.length>2){                                                                          
                                alert('该虚拟硬盘已经生成快照，请先删除快照！');
                                return;                                                                                 
                            }else{                                                 
                                $("#storageSize").val(storageSize);
                                $("#serviceType").val(serviceType);
                                $("#serviceID").val(ID);
                                $("#vmID").val(vmID);
                                $("#instanceId").val(instanceId);
                                showQuitDiv();                                                                                 
                            }
                        }
                    });                  	
                	
                	
                }
            }
        });
    }   
    
    function checkLoadBalaAboutVM(){        
        var flag=false;
        var loadbalance = getLoadBalaProList();
        if(loadbalance==null){
        	return flag;
        }
        var serviceGName = loadbalance.serverGroupName;
        var s_protocolType = loadbalance.lbProtocol;
        var dom = '';
        $.ajax({
            type : "POST",
            url : "../portal_lb/queryServiceList.action",
            data:{"serviceGroupName":serviceGName, "s_protocolType":s_protocolType},
            async:false,
            timeout:500000,
            success : function(data) {
                if(null!=data&&data.length>0){
                    flag= true;
                }
            }
        });     
        return flag;
    }
    
    function getLoadBalaProList(){
        var loadbalance;
        $.ajax({
                type : "GET",
                url : "../portal_lb/getLoadBalanceInstance.action",
                timeout:5000,
                async:false,
                success : function(data) {
                	if(data!=null){
                        loadbalance=data;
                	}
                }
            });
        return loadbalance;
    }
    
    function checkVMQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code, state, resourcePoolsId) {
        //to fix bug [3708]     
        //to fix bug [3731]    
        //to fix bug [3741]   
        if(state == 6 || state == 3){
            alert("该虚拟机实例正在执行用户请求命令，请等待该命令执行完成！");          
            return;
        }
        
        
        var params = {
            vmID : vmID,
            instanceId : instanceId,
            clusterId : ClusterId,
            res_code : res_code,
            apply_reason : '',
            resourcePoolsId : resourcePoolsId
        };      
 
        $("#serviceType").val(serviceType);
        $("#serviceID").val(ID);
        $("#vmID").val(vmID);
        $("#instanceId").val(instanceId);
        $("#ClusterId").val(ClusterId);
        $("#res_code").val(res_code); 
        
        //to fix bug [4011]
        $.ajax({
            url : "../resourcesUsed/vmModify!getBindIPsan.action",
            type : 'POST',
            data : params,
            dataType : 'json',
            success : function(data) {         	 
                if (data==1) {
                    alert('该虚拟机已经挂载弹性块存储，请先解挂！');
                    return;
                } else {                
			        $.ajax({
			            url : "../resourcesUsed/vmModify!getSnapshotsByVolumeId.action",
			            type : 'POST',
			            data : params,
			            dataType : 'json',
			            success : function(data) {              
			                if (data != "[]") {
			                    alert('该虚拟机有快照备份，请先删除该快照备份！');
			                    return;
			                } else {
			                    $.ajax({
			                        url : "../resourcesUsed/vmModify!checkVM2EBS.action",
			                        type : 'POST',
			                        async : false,
			                        data : params,
			                        dataType : 'json',
			                        success : function(data) {
			                            if(data>0){
			                                alert('该虚拟机已经挂载虚拟硬盘，请先解挂！');
			                                return;
			                            } else {
			                                $.ajax({
			                                    type : "POST",
			                                    url : "../resourcesUsed/vmModify!checkVM2EBSing.action",
			                                    data: params,
			                                    timeout:5000000,
			                                    async:false,
			                                    success : function(data6) {
			                                        if(data6>0){
			                                            alert('该虚拟机正在进行挂载或解挂虚拟硬盘的操作，请等待该操作完成！');  //to fix bug [3946]
			                                            return;
			                                        } else {  
			                                            $.ajax({
			                                                url : "../resourcesUsed/vmModify!checkVMBindIP.action",
			                                                type : 'POST',
			                                                async : false,
			                                                data : {instanceId : instanceId},//bug 0003939
			                                                dataType : 'json',
			                                                success : function(data) {
			                                                    if(data>0){
			                                                        alert('该虚拟机已经绑定弹性公网IP，请先解绑定！');
			                                                        return;
			                                                    } else {
			             
			                                                        $.ajax({
			                                                            type : "GET",
			                                                            url : "../portal_lb/getLoadBalanceInstance.action",
			                                                            timeout:5000,
			                                                            async:false,
			                                                            success : function(data) {
			                                                                if(data == null || data.virtualServiceName == null){                                                      
			                                                                    showQuitDiv();
			                                                                    
			                                                                } else {
			                                                                    $.ajax({
			                                                                        type : "POST",
			                                                                        url : "../portal_lb/queryServiceList.action",//bug 0003809
			                                                                        data:{
			                                                                            "serviceGroupName":data.serverGroupName,//bug 0003940
			                                                                            "s_protocolType":data.lbProtocol
			                                                                        },
			                                                                        timeout:5000,
			                                                                        success : function(data) {
			                                                                            var flag=0;
			                                                                            if(null!=data&&data.length>0){
			                                                                                for(var i=0;i<data.length;i++){
			                                                                                    if (data[i].vmId == vmID){
			                                                                                    	flag=1; //bug 0003976           
			                                                                                    }
			                                                                                }
			                                                                            }
			                                                                            if(flag==1){                                                                            
			                                                                                alert('该虚拟机已经绑定弹性负载均衡，请先解绑定！');
			                                                                                return;                                                                                 
			                                                                            }else{                                                 
			                                                                                showQuitDiv();                                                                                  
			                                                                            }
			                                                                        }
			                                                                    });                 
			                                                                }
			                                                            }
			                                                        }); 
			                                                    }
			                                                }
			                                            });                                         	
			                                        }
			                                	}
			                                });
			                            }
			                        }
			                    });
			                }
			            }
			        });        
                }
	    	}
	    });        
    }

    function checkVMSQuit(serviceType, ID, vmID, instanceId, ClusterId, res_code, state) {  
        var showQuitVms = true;
        if(state == 6){
            alert("该虚拟机实例正在执行用户请求命令，请等待该命令执行完成！");          
            showQuitVms = false;
            return;
        }
        
        var params = {
            vmID : vmID,
            instanceId : instanceId,
            clusterId : ClusterId,
            res_code : res_code,
            apply_reason : ''
        };      
 
        $.ajax({
            url : "../resourcesUsed/vmModify!getSnapshotsByVolumeId.action",
            type : 'POST',
            data : params,
            dataType : 'json',
            success : function(data) {              
                if (data != "[]") {
                    alert('该虚拟机有快照备份，请先删除该快照备份');
                    showQuitVms = false;
                } else {                     
                    $.ajax({
                        url : "../resourcesUsed/vmModify!checkVM2EBS.action",
                        type : 'POST',
                        async : false,
                        data : params,
                        dataType : 'json',
                        success : function(data) {
                            if(data>0){
                                alert('该虚拟机已经挂载虚拟硬盘，请先解挂！');
                                showQuitVms = false;
                            } else {
                                $.ajax({
                                    url : "../resourcesUsed/vmModify!checkVMBindIP.action",
                                    type : 'POST',
                                    async : false,
                                    data : params,
                                    dataType : 'json',
                                    success : function(data) {
                                        if(data>0){
                                            alert('该虚拟机已经绑定弹性公网IP，请先解绑定！');
                                            showQuitVms = false;
                                        } else {
                                            $.ajax({
                                                type : "GET",
                                                url : "../portal_lb/getLoadBalanceInstance.action",
                                                timeout:5000,
                                                async:false,
                                                success : function(data) {
                                                    if(data == null || data.virtualServiceName == null){
                                                        $("#serviceType").val(serviceType);
                                                        $("#serviceID").val(ID);
                                                        $("#vmID").val(vmID);
                                                        $("#instanceId").val(instanceId);
                                                        $("#ClusterId").val(ClusterId);
                                                        $("#res_code").val(res_code);                                                       
                                                        //showQuitDiv();
                                                        
                                                    } else {
                                                        $.ajax({
                                                            type : "POST",
                                                            url : "../portal_lb/queryServiceList.action",//bug 0003809
                                                            data:{"serviceGroupName":""},
                                                            timeout:5000,
                                                            success : function(data) {
                                                                if(null!=data&&data.length>0){
                                                                    for(var i=0;i<data.length;i++){
                                                                        if (data[i].e_instance_id == instanceId){
                                                                            alert('该虚拟机已经绑定弹性负载均衡，请先解绑定！');
                                                                            showQuitVms = false;
                                                                        } else {
                                                                            $("#serviceType").val(serviceType);
                                                                            $("#serviceID").val(ID);
                                                                            $("#vmID").val(vmID);
                                                                            $("#instanceId").val(instanceId);
                                                                            $("#ClusterId").val(ClusterId);
                                                                            $("#res_code").val(res_code);                                                           
                                                                            //showQuitDiv();
                                                                        }
                                                                    }
                                                                } else {
                                                                    $("#serviceType").val(serviceType);
                                                                    $("#serviceID").val(ID);
                                                                    $("#vmID").val(vmID);
                                                                    $("#instanceId").val(instanceId);
                                                                    $("#ClusterId").val(ClusterId);
                                                                    $("#res_code").val(res_code);                                                       
                                                                    //showQuitDiv();
                                                                }
                                                            }
                                                        });                 
                                                    }
                                                }
                                            }); 
                                        }
                                    }
                                }); 
                            }
                        }
                    });
                }
            }
        });
        return showQuitVms;
    }

function chclass(obj){//bug 0003037
    $('.allsearch a#cell').attr('class','allserh4');
    $(obj).attr('class','current');
}


//网络拓扑网络结构图数据查询
function getTopoData(){
    var data = {
           resPlatformURL:""
          ,resourceIDsJson:""
          ,relationsJson:""
          ,topIDArray:""
    };
    $.ajax({
        url : "/UCFCloudPortal/cloud_mall/getMyTopoStructData.action",
        type : 'POST',
        async : false,
        dataType : 'json',
        data : data,
        success : function(data) {
            if (data != null) {
            }
        }
    });
}


function showTopoDiv(){
    //获取TOPO数据
    getTopoData();
    
    //延时，等待异步返回数据
    for(var fi=0;fi<300;fi++){
        //do nothink
    }

    var topoUrl  = "/UCFCloudPortal/jsp/my_topo.jsp";
    if($.browser.msie) {
         topoUrl  ="/UCFCloudPortal/jsp/my_topo_ie.jsp";
     }else if($.browser.mozilla) {
         topoUrl  ="/UCFCloudPortal/jsp/my_topo.jsp";
         //alert("这是一个火狐浏览器");
     }else if($.browser.opera) {
         topoUrl  ="/UCFCloudPortal/jsp/my_topo_other.jsp";
         //alert("这是一个opera浏览器");
     }else if($.browser.safari) {
         topoUrl  ="/UCFCloudPortal/jsp/my_topo_other.jsp";
         //alert("这是一safari浏览器");
     }
    //跳转到拓扑图jsp拓扑图URL
    window.location.href=topoUrl;
}

//网络拓扑网络结构图数据查询
function getMonitorCount(total){
var params = {
           key:""
          ,typeId:"5"//判断是否购买云监控
          ,state:"2"//fix bug 3710 可用的云监控服务
          ,start:""
          ,end:""
    };
    $.ajax({
        url : "/UCFCloudPortal/cloud_mall/getMyMonitorCount.action",
        type : 'POST',
        async : false,
        dataType : 'json',
        data : params,
        success : function(data) {
            if (data != null) {
                total = data;
            }
        }
    });
    return total;
}

//判断公有云和私有云
function getCloudInfo() {
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
}

function showMonitorDiv(){
    var total = "0";
    var cloudStr = getCloudInfo();
    if(cloudStr=='1'){//公有云
	    total = getMonitorCount(total);
	    if( total != "0" ){
	        //alert("show "+total);
	        $("#monitorMenuDiv").show();
	    }else{
	        //alert("hide "+total);
	        $("#monitorMenuDiv").hide();
	    }
    }else if(cloudStr=='2'){//私有云
        $("#monitorMenuDiv").show();    	
    }
    //$("#monitorMenuDiv").fadeIn(300);
}

function checkHasMonitor(){
    var total = "0";
    total = getMonitorCount(total);
    if( total != "0" ){
        return true;
    }else{
        return false;
    }
}