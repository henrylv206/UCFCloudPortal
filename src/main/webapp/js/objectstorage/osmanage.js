var osmanage = {
	strCloud: "",
	init : function() {		
		osmanage.PublicPrivateSwitch();	
  	},
	showOSDiv : function(id, size,type) {
	    $("#osinstanceId").val(id);
	    $("#osreason").val("");
	    $("#reOSSize").html("");
	    osmanage.osEdit(id,size);
	  },
  
  	showNewPage : function(username, password, ostoreURI) {
	    var demo = $('<form method="post" action = "' + ostoreURI + '"><table width="100%" style="font-size: 12px;"><tbody><tr><td><input type="hidden" value="' + username + '" name="username"></td></tr><tr><td><input type="hidden" value="' + password + '" name="password"></td></tr></tbody></table></form>');
//	    var demo = $('<form method="post" action = "http://172.16.210.239:8090/OStore/ostore/fileman?format=html"><table width="100%" style="font-size: 12px;"><tbody><tr><td><input type="hidden" value="network" name="username"></td></tr><tr><td><input type="hidden" value="100" name="allocated"><input type="hidden" value="e10adc3949ba59abbe56e057f20f883e" name="password"></td></tr></tbody></table></form>');
	     
	    $("#OStore").contents().find('body').html(demo); 
	    $("#OStore").contents().find('form').submit(); 
	    $("#OStore").show();
  	},  
	
    osEdit: function(instanceId, storageSize) {
	 
		$.ajax({
			url : "../sysParameters/getStorageSizeType.action",
			type : 'POST',
			async : false,
			data : {},
			dataType : 'json',
			success : function(json) {
				var flag =0;
				if(json!=null&&json.split("|").length> 0){
					var _array = json.split("|");
	                $("#reOSSize").append("<option id=\"\" value='0' selected>--请选择--</option>");
	                $(_array).each(function(i) {
	                	//只能变更到更高的规格，无法变更到低的规格 to fix bug 4038
	                	if(Number(_array[i]) > storageSize){
	                		$("#reOSSize").append("<option value='" + _array[i] + "'>" + _array[i] + "G</option>");
	                		 flag = 1;
	                	}
	                });
				}
				if(flag == 0){
					$(".popupDiv1").hide();
					alert("没有可用的变更属性");
					$(".popupDiv1 .shade").fadeOut("fast");
				}else{
				$("#add_confirm").show();
				}
				
				
			}
		});	
	},  
	confirm: function(){
		var instanceId = $("#osinstanceId").val();
		
			if ($("#reOSSize option:selected").attr("id") == "") {
				alert("请选择要修改的存储空间大小！");
				return;
			}
			if(commonUtils.len($("#osreason").val()) > 100 ) {
	          alert("申请理由限定100个字符（一个汉字算2个字符）");
	          $("#osreason").focus();
	          return;
        	}
		if(confirm("是否确认变更？")){
			var params = {			 
				instanceId : instanceId,
				storage_size : $("#reOSSize").find("option:selected").val(),
				apply_reason : $("#osreason").val()
			};
			
      		$("#add_confirm").hide();
			osmanage.omModify("vmModify!diskModify.action", params);
		}
    },
    
	omModify: function (actionName, params) {
		$.ajax({
			url : "${pageContext.request.contextPath}/vmModify!diskModify.action",
			type : 'POST',
			async : false,
			data : params,
			dataType : 'json',
			success : function(data) {
				if (data.search("error") != -1) {
					return false;
				} else {
					alert("修改已经提交");
					  query();
				}
			}
		});
	},   
	
    PublicPrivateSwitch : function() {			 
        $.getScript("/SkyFormOpt/portal/js/privateSkySwitch.js", function(e){
        	osmanage.privateSkySwitch = privateSkySwitch;
        	osmanage.strCloud = osmanage.privateSkySwitch.getCloudInfo();
        });
	}
	
};