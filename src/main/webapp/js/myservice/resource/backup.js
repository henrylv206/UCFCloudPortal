function backupConfig(info){
    var storSize='空间大小：'+info.storageSize+'G';
    var used='，已使用：'+info.bkStorageSize+'G';//bug 0003223
    var rs='';
    if(info.storageSize>0&&info.storageSize!=undefined){
    	rs=storSize;
    }
    if(info.bkStorageSize!=''&&info.bkStorageSize!=undefined){
    	rs+=used;
    }
    return rs;    	
}

function backupState(info){
    return stateName[info.state];
}

function backupOperate(info){
	var state=info.state;	
	//to fix bug [3534] fix bug 3747
	var caozuo = '';
	if(state== "2"){
	    var serviceState=$('#serviceState').val();
		loadPopDiv_bk();
	    caozuo = '<a href=\"javascript:bkmanage.bkEdit(' + info.id + ', \'' + info.storageSize + '\', \'' + info.templateId + '\', \'' + info.productId + '\');\" title=\"变更\" ><img src="../images/modify.png" /></a>';
	    caozuo = serviceState!=6?caozuo:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" />';
	}
	else if(state== "3"){
		caozuo = '<span><img src="../images/loading1.gif"></span>';
	}
	else if(state== "6"){
		caozuo = '<span><img src="../images/loading1.gif"></span>';
	}
	else if(state== "4"||state=="7"){
		caozuo = '';
	}
	return caozuo;
}

function loadPopDiv_bk(){  
    $("#add_confirm").empty();
    $("#add_confirm").load("../component/backupnamage/bkoperate.html","",function(){
        $("#close").click(function(){
            $(".popupDiv1,.shade").fadeOut("fast");
            });
    });
}

var bkmanage = {
 
    strCloud: "",
    init : function() {
            bkmanage.template = jQuery.makeArray();
            bkmanage.templateList();            
            bkmanage.PublicPrivateSwitch();
    },

      templateList : function() {
        $.ajax({
          type : "POST",
          url : resource.actionRoot+"/vdc_bkmanage/vdcbkTemplateList.action",
          datatype : "json", //设置获取的数据类型为json
          data : {
        	  resourcePoolsId: resource.resourcePoolsId,
        	  zoneId: resource.zoneId
          },
          async : false,
          global : false,
          success : function(data) {
            if(data != null) {
              if(typeof(data) == "string" && data.indexOf("error") == 0) {
                alert(data);
                return;
              }
              else {
                $.merge(bkmanage.template, data);
              }
            }
            else {
              alert("没有找到备份服务模板记录！");
            }
          }
        });
      },
      
    bkEdit: function(ID, storageSize, templateId, productId) {   
 
    	$("#bk_params option").each(function(){
    		if($(this).val() <= Number(storageSize)){
    		    $(this).remove();
    		}
    	});  
    	//to fix bug [6804]
    	if($("#bk_params").get(0).selectedIndex == -1){
    		alert('没有可用的变更属性');
    	} else {
      	    $("#vmID").val(vmID);
            $('#bkpopTitle').html('备份变更');           			 				                 
            $("#bk_modify_reason").val("");
            $("#bk_modify").show();                    
            $("#bk_modify_ok").attr("onclick", "bkmanage.bkModifyOK(" + ID + ", " + storageSize + ", " + templateId + ", " + productId + ");");        
            $('#bk_close').attr('onclick', 'bkmanage.cancel()');  
    	}         
  	},      
      
  	bkModifyOK: function(ID, storageSize, templateId, productId){

        if(commonUtils.len($("#bk_modify_reason").val()) == 0 ) {
            alert("请输入变更理由！");
            $("#bk_modify_reason").focus();
            return;
        }	

        if(commonUtils.len($("#bk_modify_reason").val()) == 0 || commonUtils.len($("#bk_modify_reason").val()) > 20 ) {
          alert("变更理由限定1~20个字符（一个汉字算2个字符）");
          $("#bk_modify_reason").focus();
          return;
        }
        
        if(! commonUtils.scoreReg.exec($("#bk_modify_reason").val())) {
            alert("变更理由只能包含"+commonUtils.regMessage);
            $("#bk_modify_reason").focus();
            return;
        }        
         
		if(confirm("是否确认备份变更？")){
			var params = {
				vmID : ID,
				apply_reason : $("#bk_modify_reason").val(),
				storage_Size : $("#bk_params").val(),
				vmtemplateId : templateId,
				productId : productId
			};
			
	        $("#caozuo_" + vmID).html('<span><img src="../images/loading1.gif"></span>');     
	        $("#bk_modify").hide();
			$.ajax({
				url : resource.actionRoot+"/resourcesUsed/vmModify!vmModify.action",
				type : 'POST',
				async : false,
				data : params,
				dataType : 'json',
				success : function(data) {
					if (data.search("error") != -1) {						
	                    alert(data);        
						return false;
					} else {
	                     alert(data);      
					}
	                query();  
				}
			});	        
		}        
        
  	},
      
    showDiv : function(type, id, size) {     
        $("#instanceId").val(id);
        $("#operType").val(type);
        $("#curSize").val(size);
        if(type == 1) {
          $("#poptitle").html("虚拟机备份变更");//fix 0003677
          $("#tr_storage").show();
          $("#storageSize").html("");
          var options = jQuery.makeArray();
          $(bkmanage.template).each(function(i) {
            if(jQuery.inArray(bkmanage.template[i].storageSize, options) < 0) {
            	//to fix bug 3569
            	//to fix bug 3676
            	if(bkmanage.template[i].storageSize != size){
            		$.merge(options, [bkmanage.template[i].storageSize]);
            	}           
            }
          });
          //fix bug 3750
          if(options.length==0){
        	  alert("当前没有可申请变更的空间大小");
        	  bkmanage.cancel();
        	  return;
          }
          $(options).each(function(i) {
            $("#storageSize").append("<option value='" + options[i] + "'>" + options[i] + "G</option>");
          });          
        }
        else {
          $("#poptitle").html("虚拟机备份退订");
          $("#tr_storage").hide();
        }
        var tdObj = $("#td_" + type + "_" + id);
        $("#bk_btn").attr("onclick","bkmanage.confirm();");     
        $("#close").attr("onclick","bkmanage.cancel();");        
        //to fix bug [1960]
        $("#add_confirm").show();//bug 0003213
        //.css("top", tdObj[0].offsetTop).css("left", tdObj[0].offsetLeft - 120).css("display", "block");
      },


      cancel : function() {
        $("#storageSize").empty();
        $("#reason").val("");
        $("#add_confirm").hide();
        $("#bk_modify").hide();
        query();
      },
  
      confirm : function() {
            if(commonUtils.len($("#reason").val()) == 0 || commonUtils.len($("#reason").val()) > 20 ) {
              alert("申请理由限定1~20个字符（一个汉字算2个字符）");
              $("#reason").focus();
              return;
            }
            //to fix bug [1848]
            if(! commonUtils.scoreReg.exec($("#reason").val())) {
                alert("申请理由只能包含"+commonUtils.regMessage);
                $("#reason").focus();
                return;
            }
            //to fix bug [3610]
            var url = resource.actionRoot+"/vdc_bkmanage/applyChange.action";
            var data = {};
            if($("#operType").val() == '1') {
//              if(parseInt($("#storageSize").val()) == parseInt($("#curSize").val().split("/")[1])/1024) {
            if(parseInt($("#storageSize").val()) == parseInt($("#curSize").val())) {    
            	alert("申请的空间大小和目前空间大小一致，无需申请修改");
                $("#add_confirm").hide();
                return;
              }
              //to fix bug [2319]
//              if(parseInt($("#storageSize").val()) < parseInt($("#curSize").val().split("/")[0])/1024) {
            if(parseInt($("#storageSize").val()) < parseInt($("#curSize").val())) {
                alert("申请的空间大小小于已经使用的空间大小，无法修改");
                $("#add_confirm").hide();
                return;
              }
              data.storageSize = $("#storageSize").val();
              
              data.reason = $("#reason").val();
              data.instanceId = $("#instanceId").val();
            }
            
            //to fix bug [2570]
            data.reason = $("#reason").val();
            data.instanceId = $("#instanceId").val();
            
            $("#add_confirm").hide();
            $.ajax({
              type : "POST",
              url : url,
              datatype : "json",  
              data : data,
              async : false,
              global : false,
              success : function(data) {
                alert(data);
                //bug 0003230
                query();
              }
            });
            bkmanage.cancel();
      },
  
      PublicPrivateSwitch : function() {             
          $.getScript(resource.root+"/js/privateSkySwitch.js", function(e){
              bkmanage.privateSkySwitch = privateSkySwitch;
              bkmanage.strCloud = bkmanage.privateSkySwitch.getCloudInfo();
          });
        }      
};