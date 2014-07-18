var bwmanage = {
	init : function() {
		//bwmanage.template_width= jQuery.makeArray();
		bwmanage.validate();
	},
	
	showBandWidthDiv : function(id, size, type, templateId, productId) {
		
    	$("#bw_params option").each(function(){
    		if($(this).val() <= Number(size)){
    		    $(this).remove();
    		}
    	});  
    	//to fix bug [6804]
    	if($("#bw_params").get(0).selectedIndex == -1){
    		alert('没有可用的变更属性');
    	} else {
    	    $("#bwinstanceId").val(id);
    	    $("#bwoperType").val(type);
    	    $("#bwcurSize").val(size);
    	    $("#bw_modify_reason").val("");
    	    $("#rebandWidthSize").html("");
    	    //bwmanage.ajax_getBandWidthSize(size);
    		$("#bw_modify").show();
    		
            $("#bw_modify_ok").attr("onclick","bwmanage.BWModifyOK(" + id + ", " + size + ", " + type + ", " + templateId + ", " + productId + ");");
            $('#bw_close').attr('onclick', 'bwmanage.cancel()');  	
    	}
	},
	
	BWModifyOK: function(id, size, type, templateId, productId){

        if(commonUtils.len($("#bw_modify_reason").val()) == 0 ) {
            alert("请输入申请理由！");
            $("#bw_modify_reason").focus();
            return;
        }		
      
        if(commonUtils.len($("#bw_modify_reason").val()) > 100 ) {
          alert("申请理由限定100个字符（一个汉字算2个字符）");
          $("#bw_modify_reason").focus();
          return;
        }
        $("#bw_modify").hide();         
        if(commonUtils.len($("#bw_modify_reason").val()) > 0 ) {        	
        	$("#bw_modify_reason").val(commonUtils.trim($("#ec_modify_reason").val()));
        }
		if(confirm("带宽是否确认变更？")){
			var params = {
					vmID : id,					
					apply_reason : $("#bw_modify_reason").val(),
					vmtemplateId : templateId,
					productId : productId,
					storage_Size : $("#bw_params").val()					 
				};
			
	        $("#caozuo_" + id).html('<span><img src="../images/loading1.gif"></span>');     
	
			vmmanage.vmModify("vmModify!vmModify.action", params);    
		}
    },	
	  
	cancel : function() {
		$("#bw_modify").hide();
	},
	
	confirm : function() {
		    if(commonUtils.len($("#bwreason").val()) == 0 || commonUtils.len($("#bwreason").val()) > 20 ) {
		      alert("申请理由限定1~20个字符（一个汉字算2个字符）");
		      $("#bwreason").focus();
		      return;
		    }
		    if($("#bwoperType").val() == '1') {
			    if($("#rebandWidthSize").val()==null){
			    	alert("请选择要修改的带宽大小");
			    	return;
			    }		    	
		    }
		    var url = "../resourcesUsed/bandWidthApplyDestroy.action";
		    var data = {};
		    data.apply_reason =  $("#bwreason").val();
		    data.bandWidthinstanceId = $("#bwinstanceId").val();
		    if($("#bwoperType").val() == '1') {
		      if(parseInt($("#rebandWidthSize").val()) == parseInt($("#bwcurSize").val())) {
		        alert("申请的带宽大小和目前带宽大小一致，无需申请修改");
		        $("#bw_confirm").hide();
		        return;
		      }
		      url = "../resourcesUsed/bandWidthApplyChange.action";
		      data.storage_size = $("#rebandWidthSize").val();
		    }
		    $("#bw_confirm").hide();
		    bwmanage.applyDestroyOrChange(url,data);
		    bwmanage.cancel();
		    alert("申请修改成功！");
	},
	applyDestroyOrChange : function(urls,datas) {
        $("#add_confirm").hide();
		$.ajax({
					url : urls,
					type : 'POST',
					data : datas,
					dataType : 'json',
					success : function(data) {
						if (data != null) {
							$("#bwreason").html("");
						}
                        //bug 0003230
                        query();
					}
		});
	},
	ajax_getBandWidthSize:function (callback) {
      $.ajax({
        url : "../template/getConfigParams.action",
        type : 'POST',
        async : false,
        dataType : 'json',
        success : function(data) {
          if(data != null) {
            if(typeof(data) == "string" && data.indexOf("error") == 0) {
              dialog_confirmation('#dialog_confirmation', "获取配置数据失败");
              return;
            }
            else {           	
              if(data.bandwidth != undefined) {
                var _array = $.evalJSON(data.bandwidth);
                $("#rebandWidthSize").append("<option value='0' selected>--请选择--</option>");
                $(_array).each(function(i) {
                  $("#rebandWidthSize").append("<option value='" + _array[i].value + "'>" + _array[i].text + "</option>");
                });
                $("#rebandWidthSize").val(callback);
              }
            }
          }
          else {
            return;
          }
        }
      });
	},
	
	 validate: function(){
		 var solf = this;
		 $("#pop_erneuern .tilength").blur(function(){
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
	 }
	
};