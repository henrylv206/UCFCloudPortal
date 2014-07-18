var fwmanage = {
	strCloud: "",
	root:"UCFCloudPortal",
  init : function() {
    fwmanage.curProject = -1;
    fwmanage.template = jQuery.makeArray();
    fwmanage.publicIp = jQuery.makeArray();
    fwmanage.firewall = jQuery.makeArray();
    fwmanage.rule = jQuery.makeArray();
    fwmanage.PublicPrivateSwitch();
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/curProject.action",
      datatype : "json", //设置获取的数据类型为json
      data : {},
      async : false,
      global : false,
      success : function(data) {
        fwmanage.curProject = parseInt(data);
      }
    });
    fwmanage.templateList();
    fwmanage.getList();
    //产品续订相关功能
    fwmanage.validate();
    $("#pop_erneuern #InputYes").click(function() {
      if(confirm("确定要续订该产品吗？")) {
        fwmanage.updateInstancePeriod();
      }
    });
    $("#pop_erneuern #InputNo").click(function() {
      $("#pop_erneuern").hide();
      $("#pop_erneuern .tilength").val(1);
    });
  },
  templateList : function() {
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwTemplateList.action",
      datatype : "json", //设置获取的数据类型为json
      data : {},
      async : false,
      global : false,
      success : function(data) {
        if(data != null) {
          if(typeof(data) == "string" && data.indexOf("error") == 0) {
            alert(data);
            return;
          }
          else {
            $.merge(fwmanage.template, data);
          }
        }
        else {
          alert("没有找到防火墙服务模板记录！");
        }
      }
    });
  },
  getList : function() {
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwList.action?curPage=1&pageSize=10",
      datatype : "json", //设置获取的数据类型为json
      data : {},
      async : false,
      global : false,
      success : function(data) {
        if(data != null) {
          if(typeof(data) == "string" && data.indexOf("error") == 0) {
            $("#fw_tbody").html(data);
            return;
          }
          else {
            $("#fw_tbody").html("");
            var array = data.list;
            if(array == null || array.length == 0) {
              var dom = '<tr><td class="number" colspan="9"><span class="item">没有找到记录！</span></td></tr>'; 	
              $("#fw_tbody").html(dom);
              return;
            }
            else {            	
            	$.merge(fwmanage.firewall, array);
            	for (var i = 0; i < array.length; i++) {
            		var createDt = splitDateFormat(array[i].createDt);
        			var updateDt = splitDateFormat(array[i].updateDt);
                	var dom = '<tr><td class="number"><span class="item">1</span></td>'
                		+'<td><span class="item">'+array[i].instanceName+'</span></td>'
                		+'<td><span class="item"><span id="fw_rule_num">' + array[i].ruleNum + '</span>条规则</span></td> '
                		+'<td><span class="item">防火墙</span></td>'
                		+'<td><span class="item">'+array[i].comment+'</span></td>'
                		+'<td class="line6"><span class="time">'+createDt[0]+'</span><span class="time">'+createDt[1]+'</span></td>'
        	            +'<td class="line6"><span class="time">'+updateDt[0]+'</span><span class="time">'+updateDt[1]+'</span></td>'		    	       
        	            +'<td><span class="item">'+stateName[array[i].state]+'</span></td>'		    	            
        	            +'<td class="shop last"><span class="item">';
                	
                	if(3==array[i].state||6==array[i].state){
                		dom += '<span><img src="../images/loading1.gif"></span>';
                      }
                	else if(2==array[i].state){
                		dom +='<a href="#"><img src="../../images/icons/icon-right.png" title="增加规则" onclick="javascript:fwmanage.showPopDiv_addRule();"/></a>'
                  	  		+'<a href="#"><img src="../../images/icons/icon-del.png"title="删除规则" onclick="javascript:fwmanage.fwDelRule();"/></a>';
                	}
                	dom += '</span></td></tr>'; 
                	$("#fwInstanceId").val(array[i].instanceId);
                	$("#fw_tbody").html(dom);
                	fwmanage.ruleList();
            	}            	
            }
          }
        }
        else {
          $("#fw_tbody").html("没有找到记录！");
        }
      }
    });
  },
  ruleList : function() {
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwRuleList.action?curPage=" + $("#fwmanagePage_curPage").val() + "&pageSize=" + $("#fwmanagePage_pageSize").val() + "&id=" + $("#fwInstanceId").val(),
      datatype : "json", //设置获取的数据类型为json
      data : {},
      async : false,
      global : false,
      success : function(data) {
        $('#relative_thead').show();//bug 3872 0003894
        var thead='<tr><th>选择</th><th>虚拟机IP地址及子网掩码</th><th>来源端口</th><th>公网IP地址</th><th>目的端口</th><th>协议</th><th>许可</th><th>状态</th>';                 
        $('#relative_thead').html(thead);
        $("#relative_table").html("");
        if(data != null) {
          if(typeof(data) == "string" && data.indexOf("error") == 0) {
            $("#relative_table").append('<tr><td colspan="8">' + data + '</td></tr>');
            return;
          }
          else {
            var array = data.list;
            $.merge(fwmanage.rule, array);
            if($("#fw_rule_num").text().indexOf("/") > 0) {
              $("#fw_rule_num").text($("#fw_rule_num").text().replace(/\d+\//, data.total + "/"));
            }
            else {
              $("#fw_rule_num").text(data.total + "/" + $("#fw_rule_num").text());
            }
            if(array == null || array.length == 0) {
              $("#relative_table").append('<tr><td colspan="8">没有找到规则记录！</td></tr>');
              return;
            }
            else {
              $("#fwmanagePage_curPage").val(data.page);
              $("#fwmanagePage_pageSize").val(data.size);
              $(array).each(function(i) {
                
                if((i+1)%2==0){
                	$("#relative_table").append('<tr class=bj></tr>');
                }else{
                    $("#relative_table").append('<tr></tr>');	
                }
                var state = "";
                if(parseInt(array[i].status) == 0) {
                  state = "处理中";
                  $("#relative_table tr:last").append('<td><input name="rule_box" type="checkbox" value="' + array[i].id + '" disabled /></td>');
                }
                else if(parseInt(array[i].status) == 1) {
                  state = "生效";
                  $("#relative_table tr:last").append('<td><input name="rule_box" type="checkbox" value="' + array[i].id + '" /></td>');
                }
                else {
                  state = "失败";
                  $("#relative_table tr:last").append('<td><input name="rule_box" type="checkbox" value="' + array[i].id + '" /></td>');
                }
                $("#relative_table tr:last").append('<td align="center">' + array[i].sourceIp + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + (parseInt(array[i].sourcePort) == 0 ? "任意" : array[i].sourcePort) + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + array[i].destinationIp + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + (parseInt(array[i].destinationPort) == 0 ? "任意" : array[i].destinationPort) + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + array[i].protocol + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + (parseInt(array[i].access) == 1 ? "允许" : "拒绝") + '</td>');
                $("#relative_table tr:last").append('<td align="center">' + state + '</td>');
              });   
              fwmanage.initPage(data);
            }
          }
        }
        else {
          $("#relative_table").append('<tr><td colspan="8">没有找到规则记录！</td></tr>');
        }
      }
    });
  },
  
  initPage:function(data){
	  var totalPage = parseInt(data.total) % parseInt(data.size) == 0 ? parseInt(data.total) / parseInt(data.size) : parseInt(parseInt(data.total) / parseInt(data.size)) + 1;
	  $("#rule_page").empty();
	  var dom = ' <li><a href="#"><img src="../../images/icons/icon-p-first.gif" onclick="javascript:fwmanage.turnPage(-1, 0);"/></a>'
		  		+'<a href="#"><img src="../../images/icons/icon-p-prev.gif" onclick="javascript:fwmanage.turnPage(-1, 1);"/></a>'+data.page+'/'+totalPage
		  		+'<a href="#"><img src="../../images/icons/icon-p-next.gif" onclick="javascript:fwmanage.turnPage(1, 1);"/></a>'
		  		+'<a href="#"><img src="../../images/icons/icon-p-last.gif" onclick="javascript:fwmanage.turnPage(1, 0);"/></a></li>'
		  		+'<li>第<input type="text" class="text" />页<input type="button" value="GO" class="but" /></li>';
	  $("#rule_page").html(dom);
  },
  turnPage : function(direc, step) {
    if(direc == 1) {
      if(step == 0) {
        $("#fwmanagePage_curPage").val($("#fwmanagePage_totalPage").val());
      }
      else {
        $("#fwmanagePage_curPage").val(parseInt($("#fwmanagePage_curPage").val()) + parseInt(step));
      }
    }
    else {
      if(step == 0) {
        $("#fwmanagePage_curPage").val(1);
      }
      else {
        $("#fwmanagePage_curPage").val(parseInt($("#fwmanagePage_curPage").val()) - parseInt(step));
      }
    }
    fwmanage.ruleList();
  },
  fwCancel : function() {
	var bind = false;
	var vsCount = 0;
    if($("input[name='rule_box']").length == 0) {
    	$.ajax({
    	  type : "POST",
	      url : fwmanage.root+"/vdc_fwmanage/checkFWOrLB.action",
	      datatype : "json", //设置获取的数据类型为json
	      data : {},
	      async : false,
	      global : false,
	      success : function(data) {
	    	  if(data!=null){
	    		  var array = data.list;
	    		  if(array != null && array.length > 0) {	    			 
	    			  for(var i = 0;i<array.length;i++){
	    				  //购买过负载均衡  fix bug 2416
	    				  if(array[i].templateType==6){
	    					  bind = true;  
	    					  vsCount = array[i].cpuNum;
	    					  break;
	    				  }	    				  
	    			  }	    			  
	    		  }
	    		  if(bind){
		    		  if(confirm("您已经申请过负载均衡，是否一起退订？")){	 
		    			  if(vsCount>0){
		    				  alert("您申请的负载均衡已经绑定公网IP，请先解绑后，再退订！");
		    			  }
		    			  else {
		    				  $("#bindId").val(array[i].id);
							  fwmanage.showDiv();
		    			  }
					  }	
		    	  }
		    	  else fwmanage.showDiv();
	    	  }	    	  
	      }	
	 });
    }
    else {
      alert("请先删除全部规则。");
    }
  },
  cancel : function() {
    $("#fw_reason").val("");
    $("#del_confirm").hide();
  },
  showDiv : function() {
    $("#del_confirm .popwindow_t").html("退订服务");
    var tdObj = $("#fw_del_td");
    $("#del_confirm").css("top", tdObj[0].offsetTop).css("left", tdObj[0].offsetLeft - 20).css("display", "block");
  },
  confirm : function() {
    if(commonUtils.len($("#fw_reason").val()) == 0 || commonUtils.len($("#fw_reason").val()) > 20 ) {
      alert("申请理由限定1~20个字符（一个汉字算2个字符）");
      $("#fw_reason").focus();
      return;
    }
    var data = {};
    data.reason = $("#fw_reason").val();
    data.instanceId = $("#fwInstanceId").val();
    //loadblanceId
    data.bindId = $("#bindId").val();
    $("#del_confirm").hide();
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/applyDestroy.action",
      datatype : "json", //设置获取的数据类型为json
      data : data,
      async : false,
      global : false,
      success : function(data) {
        alert(data);
        fwmanage.getList();
        //bug 0003230
        query();    
      }
    });
    fwmanage.cancel();
  },
  
  showPopDiv_addRule : function(){//bug 0003664
    $("#add_confirm").empty();
    $("#add_confirm").load("../component/firewall/rulesadd.html","",function(){
        $("#close").click(function(){
            $("#add_confirm").empty();
            $("#add_confirm,.shade").fadeOut("fast");
            query();
        });
        fwmanage.fwAddRule();
    });  	
  },
  
  fwAddRule : function() {
    if(parseInt($("#fw_rule_num").text().split("/")[1]) - parseInt($("#fw_rule_num").text().split("/")[0]) <= 0) { //bug 2473 0003667
      alert("规则条数已达上限，无法继续添加");
      return;
    }
	if(fwmanage.firewall.ruleNum - fwmanage.rule.length<=0){
		alert("规则条数已达上限，无法继续添加");
	}  
    if(fwmanage.curProject == 2) { //VDC项目
      $("span[name='span_hide']").hide();
      $("#add_confirm table").css("width", "350px");
    }
    else { //1.1产品
      $("span[name='span_hide']").show();
    }
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwPublicIpList.action",
      datatype : "json", //设置获取的数据类型为json
      data : {},
      async : false,
      global : false,
      success : function(data) {
        if(data != null&&data.length!=0) {
          if(typeof(data) == "string" && data.indexOf("error") == 0) {
            alert(data);
            return;
          }
          else {
            fwmanage.publicIp = jQuery.makeArray();
            $.merge(fwmanage.publicIp, data);
            $("#fw_rule_ip_s").html('<option value=""></option>');
            $("#fw_rule_ip_d").html('<option value=""></option>');
            $(data).each(function(i) {
              $("#fw_rule_ip_s").append('<option value="' + data[i] + '">' + data[i] + '</option>');
              $("#fw_rule_ip_d").append('<option value="' + data[i] + '">' + data[i] + '</option>');
            });
            $("#add_confirm").css("display", "block");
          }
        }
        else {
          alert("请先确认您已经申请公网IP并绑定到虚拟机或虚服务上了。"); //to fix bug[2324]
          return;
        }
      }
    });
  },
  fwAddCancel : function() {
    $("#add_confirm").hide();
    //$("#add_confirm").reset();
    $("#fw_rule_ip_s_h").attr("disabled", true);
    $("#fw_rule_ip_s_t").attr("disabled", true);
    $("#fw_rule_ip_s").attr("disabled", true);
    $("#fw_rule_ip_s_mul").attr("disabled", true);
    $("#fw_rule_ip_d_h").attr("disabled", true);
    $("#fw_rule_ip_d_t").attr("disabled", true);
    $("#fw_rule_ip_d").attr("disabled", true);
    $("#fw_rule_ip_d_mul").attr("disabled", true);
    $("#mul_confirm").hide();
    $("#fw_button_add").removeAttr("disabled");
  },
  fwAddConfirm : function() { //取各种值，进行判断，符合条件开始提交，刷新页面
    var numReg = /^\d{1,5}$/;
    var obj = {};
    if(commonUtils.len(commonUtils.trim($("#fw_rule_port_s").val())) > 0) {
      var port = commonUtils.trim($("#fw_rule_port_s").val());
      if(! numReg.exec(port)) {
        alert("端口号只能包含不超过5位的数字");
        $("#fw_rule_port_s").focus();
        return;
      }
      else {
        if(parseInt(port) > 65535) {
          alert("端口号只能在0－65535之间");
          $("#fw_rule_port_s").focus();
          return;
        }
        else {
          obj.sPort = port;
        }
      }
    }
    if(commonUtils.len(commonUtils.trim($("#fw_rule_port_d").val())) > 0) {
      var port = commonUtils.trim($("#fw_rule_port_d").val());
      if(! numReg.exec(port)) {
        alert("端口号只能包含不超过5位的数字");
        $("#fw_rule_port_d").focus();
        return;
      }
      else {
        if(parseInt(port) > 65535) {
          alert("端口号只能在0－65535之间");
          $("#fw_rule_port_d").focus();
          return;
        }
        else {
          obj.dPort = port;
        }
      }
    }
    if(commonUtils.len(commonUtils.trim($("#fw_rule_ip_s_h").val())) == 0 && $("#fw_rule_ip_s").val() == "" && $("#fw_ip_s_mul_value").val() == "") {
      alert("请指定源IP");
      return;
    }
    else {
      if($('#fw_rule_ip_s_h').attr("disabled") != "disabled") {//bug 0003798
        obj.sIp = commonUtils.trim($("#fw_rule_ip_s_h").val());
        if(! commonUtils.checkIp(commonUtils.trim($("#fw_rule_ip_s_h").val()))) {
          alert("非法的IP地址!");
          $("#fw_rule_ip_s_h").focus();
          return;
        }
        if(fwmanage.curProject != 2) { //1.1产品
          if(commonUtils.len(commonUtils.trim($("#fw_rule_ip_s_t").val())) == 0) {
            alert("请指定源IP掩码");
            $("#fw_rule_ip_s_t").focus();
            return;
          }
          else {
            if(! commonUtils.checkMask(commonUtils.trim($("#fw_rule_ip_s_t").val()))) {
              alert("非法的IP地址反掩码!");
              $("#fw_rule_ip_s_t").focus();
              return;
            }
            obj.sIp = commonUtils.trim($("#fw_rule_ip_s_h").val()) + "/" + commonUtils.trim($("#fw_rule_ip_s_t").val());
          }
        }
      }
      else {
        var ip_s_mul=$("#fw_ip_s_mul_value").val();
        ip_s_mul=ip_s_mul!=undefined&&ip_s_mul!=''?ip_s_mul:'';
        var rule_ip=$("#fw_rule_ip_s").val();
        rule_ip=rule_ip!=undefined&&rule_ip!=''?rule_ip:'';
        obj.sIp = ip_s_mul + rule_ip;
      }
    }
    if(commonUtils.len(commonUtils.trim($("#fw_rule_ip_d_h").val())) == 0 && $("#fw_rule_ip_d").val() == "" && $("#fw_ip_d_mul_value").val() == "") {
      alert("请指定目的IP");
      return;
    }
    else {    	
      if($('#fw_rule_ip_d_h').attr("disabled") != "disabled") {//bug 0003798
        if(! commonUtils.checkIp(commonUtils.trim($("#fw_rule_ip_d_h").val()))) {
          alert("非法的IP地址!");
          $("#fw_rule_ip_d_h").focus();
          return;
        }
        if(fwmanage.curProject != 2) { //1.1产品
          if(commonUtils.len(commonUtils.trim($("#fw_rule_ip_d_t").val())) == 0) {
            alert("请指定目的IP掩码");
            $("#fw_rule_ip_d_t").focus();
            return;
          }
          else {
            if(! commonUtils.checkMask(commonUtils.trim($("#fw_rule_ip_d_t").val()))) {
              alert("非法的IP地址反掩码!");
              $("#fw_rule_ip_d_t").focus();
              return;
            }
            obj.dIp = commonUtils.trim($("#fw_rule_ip_d_h").val()) + "/" + commonUtils.trim($("#fw_rule_ip_d_t").val());
          }
        }
      }
      else {
        var ip_d_mul=$("#fw_ip_d_mul_value").val();
        ip_d_mul=ip_d_mul!=undefined&&ip_d_mul!=''?ip_d_mul:'';
        var rule_ip=$("#fw_rule_ip_d").val();
        rule_ip=rule_ip!=undefined&&rule_ip!=''?rule_ip:'';
        obj.dIp = ip_d_mul + rule_ip;
      }
    }
    if($("#fw_protocol").val() == "" && $("#fw_protocol_mul_value").val() == "") {
      alert("请指定协议类型");
      return;
    }
    else {
    	var protocol_mul=$("#fw_protocol_mul_value").val();
    	protocol_mul=protocol_mul!=undefined&&protocol_mul!=''?protocol_mul:'';
    	var protocol=$("#fw_protocol").val();
    	protocol=protocol!=undefined&&protocol!=''?protocol:'';
      obj.protocol = protocol + protocol_mul;
    }
    obj.access = $("#fw_access").val();
    obj.instanceId = $("#fwInstanceId").val();
    var curNum = parseInt($("#fw_rule_num").text().split("/")[0]);
    var limit = parseInt($("#fw_rule_num").text().split("/")[1]);
    var gonna = obj.sIp.split(",").length * obj.dIp.split(",").length * obj.protocol.split(",").length;
    if((limit - curNum) < gonna) {
      alert("此次操作您将创建" + gonna + "条规则，但您最多还能创建" + (limit - curNum) + "条规则");
      return;
    }
    $("#fw_button_add").attr("disabled", "disabled");
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwRuleAdd.action",
      datatype : "json", //设置获取的数据类型为json
      data : {queryJson:$.toJSON(obj)},
      success:function(data) {
        if(data.indexOf("error") == 0) {
          alert(data);
          $("#fw_button_add").removeAttr("disabled");
          return;
        }
        else {
          alert("规则创建成功！");
          fwmanage.ruleList();
          fwmanage.fwAddCancel();
        }
      }
    });
  },
  directionControl : function(direction) {//修正radio切换不正确问题 bug 0003818
    if(direction == "in") {
      if($("#fw_rule_ip_s_h").attr("disabled") == "disabled") {
        $("#fw_rule_radio_s_new").click();
      }
    }
    else if(direction == "out"){
      if($("#fw_rule_ip_d_h").attr("disabled") == "disabled") {
        $("#fw_rule_radio_s_old").click();
      }
    }
  },
  radioControl : function(target, which) {//修正radio切换不正确问题 bug 0003818
    if(target == "s") {
      if(which == "new") {
        $("#fw_rule_ip_s_h").attr("disabled", false);
        $("#fw_rule_ip_s_t").attr("disabled", false);
        $("#fw_rule_ip_s_mul").attr("disabled", true);
        $("#fw_ip_s_mul_value").val("");
        $("#selected_ip_s").val('');
        $("#fw_rule_ip_s option[value='']").attr("selected", "selected");
        $("#fw_rule_radio_d_old").click();
        $("#fw_rule_radio_dir_in").click();
      }
      else {
        $("#fw_rule_ip_s_h").attr("disabled", true);
        $("#fw_rule_ip_s_t").attr("disabled", true);
        $("#fw_rule_ip_s_mul").attr("disabled", false);
        if($("#fw_rule_ip_d_h").attr("disabled") == "disabled") {
          $("#fw_rule_radio_d_new").click();
        }
        $("#fw_rule_radio_dir_out").click();
      }
    }
    else {
      if(which == "new") {
        $("#fw_rule_ip_d_h").attr("disabled", false);
        $("#fw_rule_ip_d_t").attr("disabled", false);
        $("#fw_rule_ip_d_mul").attr("disabled", true);
        $("#fw_ip_d_mul_value").val("");
        $("#selected_ip_d").val('');
        $("#fw_rule_ip_d option[value='']").attr("selected", "selected");
        $("#fw_rule_radio_s_old").click();
        $("#fw_rule_radio_dir_out").click();
      }
      else {
        $("#fw_rule_ip_d_h").attr("disabled", true);
        $("#fw_rule_ip_d_t").attr("disabled", true);
        $("#fw_rule_ip_d_mul").attr("disabled", false);
        if($("#fw_rule_ip_s_h").attr("disabled") == "disabled") {
          $("#fw_rule_radio_s_new").click();
        }
        $("#fw_rule_radio_dir_in").click();
      }
    }
  },
  mulControl : function(type) {
    $("#fw_mul_choice").html("");
    $("#fw_mul_choose").html("");
    $("#fw_mul_target").val(type);
    if(type == "protocol") {
      if($("#fw_protocol_mul_value").val() == "") {
        $("#fw_protocol option").each(function(i) {
          if($(this).val() != "") {
            $("#fw_mul_choice").append('<option>' + $(this).val() + '</option>');
          }
        });
      }
      else {
        var tempArr = $("#fw_protocol_mul_value").val().split(",");
        $("#fw_protocol option").each(function(i) {
          if($(this).val() != "") {
            if(jQuery.inArray($(this).val(), tempArr) == -1) {
              $("#fw_mul_choice").append('<option>' + $(this).val() + '</option>');
            }
            else {
              $("#fw_mul_choose").append('<option>' + $(this).val() + '</option>');
            }
          }
        });
      }
    }
    else if(type == "s") {
      if($("#fw_ip_s_mul_value").val() == "") {
        $(fwmanage.publicIp).each(function(i) {
          $("#fw_mul_choice").append('<option>' + fwmanage.publicIp[i] + '</option>');
        });
      }
      else {
        var tempArr = $("#fw_ip_s_mul_value").val().split(",");
        $(fwmanage.publicIp).each(function(i) {
          if(jQuery.inArray(fwmanage.publicIp[i], tempArr) == -1) {
            $("#fw_mul_choice").append('<option>' + fwmanage.publicIp[i] + '</option>');
          }
          else {
            $("#fw_mul_choose").append('<option>' + fwmanage.publicIp[i] + '</option>');
          }
        });
      }
    }
    else {
      if($("#fw_ip_d_mul_value").val() == "") {
        $(fwmanage.publicIp).each(function(i) {
          $("#fw_mul_choice").append('<option>' + fwmanage.publicIp[i] + '</option>');
        });
      }
      else {
        var tempArr = $("#fw_ip_d_mul_value").val().split(",");
        $(fwmanage.publicIp).each(function(i) {
          if(jQuery.inArray(fwmanage.publicIp[i], tempArr) == -1) {
            $("#fw_mul_choice").append('<option>' + fwmanage.publicIp[i] + '</option>');
          }
          else {
            $("#fw_mul_choose").append('<option>' + fwmanage.publicIp[i] + '</option>');
          }
        });
      }
    }
    $("#mul_confirm").show();
  },
  mulSubmit : function() {
    if($("#fw_mul_target").val() == "protocol") {
      if($("#fw_mul_choose option").length > 0) {
        $("#fw_protocol option[value='']").attr("selected", "selected");
        var value = "";
        $("#fw_mul_choose option").each(function(i) {
          if(i == 0) {
            value = $(this).text();
          }
          else {
            value += "," + $(this).text();
          }
        });
        $("#fw_protocol_mul_value").val(value);
      }
      else {
        $("#fw_protocol_mul_value").val("");
      }
    }
    else if($("#fw_mul_target").val() == "s") {
      if($("#fw_mul_choose option").length > 0) {
        $("#fw_rule_ip_s option[value='']").attr("selected", "selected");
        var value = "";
        $("#fw_mul_choose option").each(function(i) {
          if(i == 0) {
            value = $(this).text();
          }
          else {
            value += "," + $(this).text();
          }
        });
        $("#fw_ip_s_mul_value").val(value);
        $("#selected_ip_s").val(value.replace(',', '\t\n'));//bug 0003798
      }
      else {
        $("#fw_ip_s_mul_value").val("");
        $("#selected_ip_s").val('');
      }
    }
    else {
      if($("#fw_mul_choose option").length > 0) {
        $("#fw_rule_ip_d option[value='']").attr("selected", "selected");
        var value = "";
        $("#fw_mul_choose option").each(function(i) {
          if(i == 0) {
            value = $(this).text();
          }
          else {
            value += "," + $(this).text();
          }
        });
        $("#fw_ip_d_mul_value").val(value);
        $("#selected_ip_d").val(value.replace(',', '\t\n'));//bug 0003798
      }
      else {
        $("#fw_ip_d_mul_value").val("");
        $("#selected_ip_d").val('');
      }
    }
    $("#mul_confirm").hide();
  },
  mulCancel : function() {
    $("#mul_confirm").hide();
  },
  mulMove : function(direction) {
    if(direction > 0) {
      if($("#fw_mul_choose").val() == null) {
        alert("请选择要取消的已选项。");
      }
      else {
        $("#fw_mul_choose option:selected").each(function() {
          $("#fw_mul_choice").prepend($(this));
        });
      }
    }
    else {
      if($("#fw_mul_choice").val() == null) {
        alert("请选择要添加的可选项。");
      }
      else {
        $("#fw_mul_choice option:selected").each(function() {
          $("#fw_mul_choose").prepend($(this));
        });
      }
    }
  },
  selChange : function(type) {
    if(type == "protocol") {
      if($("#fw_protocol").val() != "") {
        $("#fw_protocol_mul_value").val("");
      }
    }
    else if(type == "s") {
      if($("#fw_rule_ip_s").val() != "") {
        $("#fw_ip_s_mul_value").val("");
      }
    }
    else {
      if($("#fw_rule_ip_d").val() != "") {
        $("#fw_ip_d_mul_value").val("");
      }
    }
  },
  checkAll : function() {
    var bool = true;
    if($("#fw_checkall").attr("checked") == "checked") {
      bool = true;
    }
    else {
      bool = false;
    }
    $("input[name='rule_box']").each(function() {
      if($(this).attr("disabled") != "disabled") {
        $(this).attr("checked", bool);
      }
    });
  },
  fwDelRule : function() {
    var ids = "";
    $("input[name='rule_box']").each(function() {
      if($(this).attr("checked") == "checked") {
        if(ids == "") {
          ids = $(this).val();
        }
        else {
          ids += "," + $(this).val();
        }
      }
    });
    if(ids == "") {
      alert("请选择要删除的规则");
      return;
    }
    $.ajax({
      type : "POST",
      url : fwmanage.root+"/vdc_fwmanage/vdcfwRuleDel.action",
      datatype : "json", //设置获取的数据类型为json
      data : {queryJson:ids},
      success:function(data) {
        if(data.indexOf("error") == 0) {
          alert(data);
          return;
        }
        else {
          alert("规则删除成功！");
          fwmanage.ruleList();
          query();
        }
      }
    });
  },
  //------------------------------------------产品续订-------------------------------------------
  showProductXDDiv : function(type, id) {
    var tdObj = $("#td_bw_" + type + "_" + id);
      $("#pop_erneuern").css("top", tdObj[0].offsetTop + 40).css("left", tdObj[0].offsetLeft - 80).css("display", "block");
      fwmanage.getInstancePeriodInfo(id);
  },
  
  PublicPrivateSwitch : function() {			 
      $.getScript(fwmanage.root+"/js/privateSkySwitch.js", function(e){
    	  fwmanage.privateSkySwitch = privateSkySwitch;
    	  fwmanage.strCloud = fwmanage.privateSkySwitch.getCloudInfo();
      });
	},
  
  getInstancePeriodInfo : function(id) {
    $.ajax({
      url : "/UCFCloudPortal/order/findInstancePeriodById.action?id=" + id,
      type : 'POST',
      dataType : 'json',
      success : function(data) {
        if (data != null) {
        	//alert(fwmanage.strCloud);
          $(".popwindow #old_period").html(data.buyPeriod);
          $(".popwindow #due_date").html(data.expireDateString);
        //to fix bug [1829]
          if(fwmanage.strCloud == "1"){    		        		
        	  $(".popwindow .cycle").html(data.price + "元/" + data.unitString);
          } 
          if(fwmanage.strCloud == "2"){    		    			
        	  $(".popwindow .cycle").html(data.unitString);
          } 
          
          $("#pop_erneuern #xd_unit").html(data.unit);
          $("#pop_erneuern #xd_id").html(id);
        }
      }
    });
  },
  updateInstancePeriod : function() {
    $.ajax({
      url : "/UCFCloudPortal/order/updateInstancePeriod.action?id=" + Number($("#pop_erneuern #xd_id").html()),
      type : 'POST',
      data : {unit:$("#pop_erneuern #xd_unit").html(), num:Number($("#pop_erneuern .tilength").val())},
      dataType : 'json',
      success : function(data) {
        if(data.indexOf("error") == 0) {
          alert("失败：" + data);
        }
        else {
          alert(data);
          $("#pop_erneuern").hide();
          $("#pop_erneuern .tilength").val(1);
        }
      }
    });
  },
  validate: function() {
    var solf = this;
    $("#pop_erneuern .tilength").blur(function() {
      if(Number($(this).val()) + "" == "NaN") {
        $(this).val(1);
        return;
      }
      if(Number($(this).val()) < 1) {
        $(this).val(1);
        return;
      }
      if(Number($(this).val()) > 9999) {
        $(this).val(9999);
        return;
      }
      if(Number($(this).val()) % 1 != 0) {
        $(this).val(1);
        return;
      }
      $(this).val(Number($(this).val()));
    });
  },
  getStateName:function(stateId){
      var value = "";
      switch(stateId){ 
        case 1:
        	value = "申请开通";
			break;
		case 2:
		    value = "就绪";//to fix bug:4014(将'正常'改为'就绪')
			break;		
		case 3:
		case 6:
			value = "执行中";
			break;
		case 4:
		    value = "退订";
			break;
		case 7://bug 0004411 0004446 0004457
			value = "创建失败";
			break;
		default:
		    value = ""; 
      }
    return value; 
    }
};
function firewallConfig(info){
    var storageSize=info.storageSize;
	var dom=storageSize+'条规则';
	$('#fw_rule_num').text(storageSize);
	return dom;
}
function firewallState(info){
	$.fwmanage || ($.fwmanage = fwmanage);	
	fwmanage.root="..";
	fwmanage.init();
	$("#fwInstanceId").val(info.id);
	return fwmanage.getStateName(info.state);
};
function firewallOperate(info){
    var serviceState=$('#serviceState').val();
	loadPopDiv();
	var dom = '';
	if(3==info.state||6==info.state){		
		dom += '<span><img src="../images/loading1.gif"></span>';
      }
	else if(2==info.state){
		dom ='<a href="#"><img src="../images/modify.png" title="增加规则" onclick="javascript:fwmanage.showPopDiv_addRule('+info+');"/></a>'
  	  		+'<a href="#"><img src="../images/Destroy.png"title="删除规则" onclick="javascript:fwmanage.fwDelRule();"/></a>';
  	  	dom= serviceState!=6?dom:'<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/modify.png" title="增加规则"/>'
  	  	+'<img  style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/Destroy.png"title="删除规则"/>';
	}
	return dom;
}

function loadPopDiv(){	
	$("#add_confirm").empty();
	$("#add_confirm").load("../component/firewall/rulesadd.html","",function(){
		$("#close").click(function(){
			$("#add_confirm").empty();
			$("#add_confirm,.shade").fadeOut("fast");
			});
	});
}