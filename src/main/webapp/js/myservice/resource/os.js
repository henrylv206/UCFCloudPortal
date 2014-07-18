function osConfig(){
		var storSize=info.storageSize;
		return storSize+'G';
}

function osState(info){
	return stateName[info.state];
}

function loadPopDiv_os(){	
	$("#add_confirm").empty();
	$("#add_confirm").load("../component/objectstorage/osmanage.html","",function(){
		$("#close").click(function(){
			$("#add_confirm,.shade").fadeOut("fast");
			});
	});
}

function osOperate(info){
    var serviceState=$('#serviceState').val();
    loadPopDiv_os();
	var vXiugai='';
    if(info.state == "2"){
    	
    	//var url="http://172.16.201.52:8080/SkyFormRes/obsmgr/obsurl";
    	//to fix bug 4037
		vXiugai=''+
			'	<a title="进入" id="a1" href="javascript:checkOsUserStatus('+info.resourcePoolsId+');">' +
			'		<img src="../images/tel_edit.png" />' +
			'	</a>'+
			'	<a title="变更" id="a2" href="javascript:osmanage.showOSDiv('+info.id+',\''+info.storageSize+'\', 1);">' +
			'		<img src="../images/modify.png" />' +
			'	</a>';
	    vXiugai = serviceState!=6?vXiugai:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/tel_edit.png" />';
    }
    return vXiugai;
}
//to fix bug 4042
function checkOsUserStatus(resourcePoolsId){
        $.ajax({
          url : "../resourcesUsed/checkOsUserStatus.action",
          type : "POST",
          dataType : "json",
          async : false,
          global : false,
          success : function(data) {
            if(data.indexOf("error") == 0) {
              //dialog_confirmation('#dialog_confirmation', data);
              return;
            }
            else {
              if($.evalJSON(data)) {
                //dialog_confirmation('#dialog_confirmation', "");
            	alert("您的账户处于暂停状态，请稍后再试");
                return;
              }
              else {
            	  showOsIframe(resourcePoolsId);
              }
            }
          }
        });

}

function showOsIframe(resourcePoolsId){
	$(".tblWrap").hide();
	$(".breadcrumbs .back").hide();
	$("#OStorediv").show();
	$("#OStore1").show();
	$("#hideOsBtn").show();
	$("#hideOsBtn").click(function() {
		hideOsIframe();
	});	
	if($("#OStore1").contents().find('body').html() == ""){
		getOsUrl(resourcePoolsId);
	}
	//$(".breadcrumbs:last").before('<input id="hideOsBtn" class="back" type="button" value="返回上一页" style="float:right" onclick="javascript:hideOsIframe();"/>');

}

function hideOsIframe(){
	$(".tblWrap").show();
	$(".breadcrumbs .back").show();
	$("#OStore1").html("");
	$("#OStore1").hide();
	$("#OStorediv").hide();
	$("#hideOsBtn").hide();
	$("#MonitorCancel").hide();
}

function getOsUrl(resourcePoolsId){		
		$.ajax({
		url : "../resourcesUsed/getOsUrl.action",
		data : {resourcePoolsId : resourcePoolsId},
		dataType : "json",
		async : false,
		success : function(data){
		        if(data != null) {
		          if(typeof(data) == "string" && data.indexOf("error") == 0) {
		            return "";
		          }else {
		        	var ostoreURI  = data.url;
		        	var username = data.account;
		        	var password = data.password;		        
			        var demo = $('<form id="osForm" method="post" action = "' + ostoreURI + '"><table width="100%" style="font-size: 12px;"><tbody><tr><td><input type="hidden" value="' + username + '" name="username"></td></tr><tr><td><input type="hidden" value="'+password+'" name="password"></td></tr></tbody></table></form>');
			        $("#OStore1").contents().find('body').html(demo); 
					$("#OStore1").contents().find('form').submit(); 
                    //bug 0003230
                    query();
			      }
		       } 
        	}
        });
    }