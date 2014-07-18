$(function() {	
	$.ajaxSetup({cache:false});//设置jQuery ajax缓存

	user_info.server = server;
	if("1" == cloudStr){
		user_info.initInfo();
	}	
	user_info.init();

});
//获取ID
var root = "/UCFCloudPortal";
var oldEmail = "";
var right = '<img src="'+root+'/images/icons/icon-right.png"/>';
//fix bug 7374
var cloudStr = "";
var privateUser = {};
$.ajax({
	url: root+"/sysParameters/getCloudInfo.action",
	type: 'POST',
	dataType: 'json',
	async: false,
	success: function(data) {
		cloudStr = data;
	}
});

var user_info={
		curUser:"",
		root: "/UCFCloudPortal",		
		init: function(){
			$.ajax({
				url: root+"/user/detailUser.action",
				type: 'POST',
				dataType: 'json',
				data:{userId:userNo},
				async: false,
				success: function(data) {
					privateUser = data;
				}
			});
			$("#youxiang").val(privateUser.email);
			//数据库中的邮箱 fix bug 5028
			oldEmail = $.trim($("#youxiang").val());
			// 特殊字符输入控制，允许输入中文,允许输入空格
			$("input[type='text']").blur(function() {
				var val = $(this).val();
				if (val.length < 1) {
					$(this).val("");
					return;
				}
				//邮箱可以输入@
				if($(this).is('#qymail')){
					if (!/^[ \u4E00-\u9FA5a-zA-Z0-9_.@\/-]+$/.test(val)) {
						$(this).val("");
						return;
					}
				}
				else if($(this).is('#youxiang')){
					if (!/^[ \u4E00-\u9FA5a-zA-Z0-9_.@\/-]+$/.test(val)) {
						$(this).val("");
						return;
					}
				}
				//银行帐户不能输入.
				else if($(this).is('#qykhyh')){
					if (!/^[ \u4E00-\u9FA5a-zA-Z0-9_\/-]+$/.test(val)) {
						$(this).val("");
						return;
					}
				}
				else if (!/^[ \u4E00-\u9FA5a-zA-Z0-9_.\/-]+$/.test(val)) {
					$(this).val("");
					return;
				}
			});
			//私有云用户信息保存
			$("#pSave").click(function(){
				$("#privateUser :text").trigger('blur');				
				var back = user_info.valite();
				if (!back) {
					return;
				} else {
					user_info.updatePrivateUser();
				}
			});

			// 保存
			$("#btnSave").click(function() {
				var $parent = $("#youxiang").parent();
			  	$parent.find("sub").removeClass().empty();
			  	var flag = valiter.checkEmailIsExist();
				if(flag){
					var errorMsg = "此邮箱已经被使用，请更换！";
					$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
					$("#youxiang").val("");	
					return;
				}
				else $parent.find("sub").html(right);
				
				$("#companyInfo :text").trigger('blur');				
				var back = user_info.valite();
				if (!back) {
					return;
				} else {
					user_info.insertInfo();
				}

			});

			
			// 日期datepicker控件中文设置
			$.datepicker.regional['zh-CN'] = {
				clearText : '清除',
				clearStatus : '清除已选日期',
				closeText : '关闭',
				closeStatus : '不改变当前选择',
				prevText : '&lt;上月',
				prevStatus : '显示上月',
				nextText : '下月&gt;',
				nextStatus : '显示下月',
				currentText : '今天',
				currentStatus : '显示本月',
				monthNames : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月', '九月',
						'十月', '十一月', '十二月' ],
				monthNamesShort : [ '一月', '二月', '三月', '四月', '五月', '六月', '七月', '八月',
						'九月', '十月', '十一月', '十二月' ],
				monthStatus : '选择月份',
				yearStatus : '选择年份',
				weekHeader : '周',
				weekStatus : '年内周次',
				dayNames : [ '星期日', '星期一', '星期二', '星期三', '星期四', '星期五', '星期六' ],
				dayNamesShort : [ '周日', '周一', '周二', '周三', '周四', '周五', '周六' ],
				dayNamesMin : [ '日', '一', '二', '三', '四', '五', '六' ],
				dayStatus : '设置 DD 为一周起始',
				dateStatus : '选择 m月 d日, DD',
				dateFormat : 'yy-mm-dd',
				firstDay : 1,
				initStatus : '请选择日期',
				isRTL : false

			};
			$.datepicker.setDefaults($.datepicker.regional['zh-CN']);
			// 时间控件
			$("#gsyyzzstime").datepicker({
				changeMonth : true,
				changeYear : true,
				showWeek : true,
				showButtonPanel : true,
				gotoCurrent : true,
				dateFormat : "yy-mm-dd",
				maxDate : +0
			});

			$("#gsyyzzetime").datepicker({
				changeMonth : true,
				changeYear : true,
				showWeek : true,
				showButtonPanel : true,
				gotoCurrent : true,
				dateFormat : "yy-mm-dd",
				minDate : +0
			});
			if("2"==cloudStr){	
				$("#pPhone").val(privateUser.phone);
				$("#pFax").val(privateUser.fax);
				$("#pMobile").val(privateUser.mobile);
				$.ajax({
					url : root+"/user/showDeptList.action",
					type : 'POST',
					contentType : "application/json",
					dataType : 'json',
					success : function(data) {
						$("pDept").find("option").remove();
						var array = data.returnList;						
						$(array).each(function(i) {
							//不显示公有云用户组   To Fix Bug Id:[1682]
							if(array[i].deptId!=1){
								var dom = "<option value='" + array[i].deptId+"'";
								if(privateUser.deptId==array[i].deptId){
									dom += " selected ";
								}		 
								dom += ">"+ array[i].deptName + "</option>";								
							}
							$("#pDept").append(dom);
						});											
					}
				});
				
				$.ajax({
					url : root+"/role/showAllRole.action",
					type : 'POST',
					contentType : "application/json",
					dataType : 'json',
					success : function(data) {
						$("#pRole").find("option").remove();
						var array = data.returnList;
						$(array).each(function(i) {
							var dom = "<option value='" + array[i].roleId+"'";
							if(privateUser.roleId==array[i].roleId){
								dom += " selected ";
							}		 
							dom += ">"+ array[i].roleName + "</option>";
							$("#pRole").append(dom);
						});							
					}
				});
			}	
			else {
				// 初始化所在城市
				$.ajax({				
					url : root+"/city/find_all.action",
					type : "POST",
					contentType : "application/json",
					data : {},
					dataType : "json",
					timeout : 5000,
					success : function(city) {
						if (city.length > 0) {
							for ( var o = 0; o < city.length; o++) {
								//fix bug 3383  3383
								var dom = "<option value='" + city[o].cityId+"'" ;
								if(null!=user_info.curUser&&user_info.curUser.cityId==city[o].cityId){
									dom += " selected ";
								}	
								dom	+= ">"+city[o].cityName + "</option>";
								$('#szcs').append(dom);
							}
						}
					}
				});			
				// 初始化行业分类
				$.ajax({
					url : root+"/category/find_all.action",
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : {},
					timeout : 5000,
					success : function(category) {
						if (category.length > 0) {
							for ( var o = 0; o < category.length; o++) {
								//fix bug 3383
								var dom = "<option value='" + category[o].categoryId+"'";
								if(null!=user_info.curUser&&user_info.curUser.compCategoryId==category[o].categoryId){
									dom += " selected ";
								}		 
								dom += ">"+ category[o].categoryName + "</option>";
								$('#hylb').append(dom);
							}
						}
					}
				});
				// 初始化企业分类
				$.ajax({
					url : root+"/property/find_all.action",
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : {},
					timeout : 5000,
					success : function(property) {
						if (property.length > 0) {
							for ( var o = 0; o < property.length; o++) {
								//fix bug 3383
								var dom = "<option value='" + property[o].propertyId+"'";
								if(null!=user_info.curUser&&user_info.curUser.compPropertyId==property[o].propertyId){
									dom += " selected ";
								}
								dom += ">"+ property[o].propertyName + "</option>";
								$('#qylb').append(dom);	
							}
						}
					}
				});

				// 初始化企业当前等级
				$.ajax({
					url : root+"/class/find_all.action",
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : {},
					timeout : 5000,
					success : function(tclass) {
						if (tclass.length > 0) {
							for ( var o = 0; o < tclass.length; o++) {
								//fix bug 3383
								var dom = "<option value='" + tclass[o].classId+"'";
								if(null!=user_info.curUser&&user_info.curUser.compClassId==tclass[o].classId){
									dom += " selected ";
								}
								dom += ">"+ tclass[o].className + "</option>";
								$('#qydqdj').append(dom);
							}
						}

					}
				});
			}
			
			
			
			// 文本框失去焦点后
		       $('body :text').blur(function(){
		    	   var $parent = $(this).parent();
		    	   $parent.find("sub").removeClass().empty();
		    	 //用户邮箱
		    	   if($(this).is('#youxiang')){			
						if (valiter.isNull($.trim(this.value))
								|| !valiter.checkEmail($.trim(this.value))) {
							var errorMsg = "*请输入正确的邮箱";
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}					
					}  
		    	// 企业中文名全称验证
					if ($(this).is('#compchinaname')) {
						if (valiter.isNull($.trim(this.value))||$.trim(this.value).length>30) {
							var errorMsg = "*30个非特殊字符以内";//fix bug 4137
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}else  {
							$parent.find("sub").html(right);
						}
					}					
					// 企业法人验证
					if ($(this).is('#qyfr')) {
						if (valiter.isNull($.trim(this.value))
								|| $.trim(this.value).length < 2||$.trim(this.value).length>50) {
							$parent.find("sub").removeClass("info").addClass("onError").html("*2-50个非特殊字符");
						} else {
							$parent.find("sub").html(right);
						}
					}
					// 企业法人身份证号码验证
					if ($(this).is('#qyfrid')) {
						if (this.value != "") {
							var msg = valiter.checkIdcard($.trim(this.value));
							if (msg.length != "1") {
								var errorMsg = msg;
								$parent.find("sub").removeClass("info").addClass("onError").html(msg);
							} else{
								$parent.find("sub").html(right);
							}
						} else {
							$parent.find("sub").removeClass("info").addClass("onError").html("*输入证件号码");
						}

					}

					// 企业地址验证
					if ($(this).is('#qydz')) {
						if (valiter.isNull($.trim(this.value))||$.trim(this.value).length>50) {
							$parent.find("sub").removeClass("info").addClass("onError").html("*50个非特殊字符以内");
						}else{
							$parent.find("sub").html(right);
						}
					}
					// 企业电话
					if ($(this).is('#qydh')) {
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.telephone($.trim(this.value))) {
								var errorMsg = "*按7/8位填写";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							} 
							else  {
								$parent.find("sub").html(right);
							}
						}						
					}
					if ($(this).is('#pPhone')) {
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.telephone($.trim(this.value))) {
								var errorMsg = "*按7/8位填写";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							} 
							else  {
								$parent.find("sub").html(right);
							}
						}						
					}
					if ($(this).is('#pFax')) {
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.telephone($.trim(this.value))) {
								var errorMsg = "*按7/8位填写";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							} 
							else  {
								$parent.find("sub").html(right);
							}
						}						
					}
					//常用联系人手机
					if($(this).is('#cysj')){
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.cellphone($.trim(this.value))) {
								var errorMsg = "*输入手机号码";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							}
							else {
								$parent.find("sub").html(right);
							}
						}
					}					
					if($(this).is('#pMobile')){
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.cellphone($.trim(this.value))) {
								var errorMsg = "*输入手机号码";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							}
							else {
								$parent.find("sub").html(right);
							}
						}
					}	
					// 所在城市验证
					if($(this).is('#szcs')){
						if(valiter.isNull($.trim(this.value))){
							var errorMsg = "所在城市选项值错误";
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}
					}
					// 企业类别验证
					if($(this).is('#qylb')){
						if(valiter.isNull($.trim(this.value))){
							var errorMsg = "企业类别选项值错误";
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}
					}					
					// 行业分类验证
					if($(this).is('#hylb')){
						if(valiter.isNull($.trim(this.value))){
							var errorMsg = "行业分类选项值错误";
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}
					}
					// 企业规模验证
					if($(this).is('#qydqdj')){
						if(valiter.isNull($.trim(this.value))){
							var errorMsg = "企业规模选项值错误";
							$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
						}
					}
					//企业传真
					if($(this).is('#qycz')){
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.telephone($.trim(this.value))) {
								var errorMsg = "*按7/8位填写";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							} else {
								$parent.find("sub").html(right);
							}
						}
					}
					//企业电子邮箱
					if($(this).is('#qymail')){
						if(!valiter.isNull($.trim(this.value))){
							if (!valiter.checkEmail($.trim(this.value))) {
								var errorMsg = "*输入邮箱";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							}
							else {
								$parent.find("sub").html(right);
							}
						}
					}		
					//邮政编码
					if($(this).is('#yzbm')){
						if(!valiter.isNull($.trim(this.value))){
							if (valiter.numberInt($.trim(this.value))) {
								var errorMsg = "*6位邮政编码";
								$parent.find("sub").removeClass("info").addClass("onError").html(errorMsg);
							}
							else {
								$parent.find("sub").html(right);
							}
						}
						
					}	
		    		// 企业组织机构代码验证
		            if( $(this).is('#compcode') ){
		                   if(valiter.isNull($.trim(this.value))||$.trim(this.value).length>10){
		                	   $parent.find("sub").removeClass("info").addClass("onError").html("*10个非特殊字符以内");
		                   }else{
		                	   $parent.find("sub").html(right);
		                   }
		            }
		         // 工商营业执照验证
					if ($(this).is('#gsyyzzid')) {
						if (valiter.isNull($.trim(this.value))
								|| valiter.numberInt($.trim(this.value))||$.trim(this.value).length>15) {
							$parent.find("sub").removeClass("info").addClass("onError").html("*不多于15位数字");
						} else {
							$parent.find("sub").html(right);
						}
					}
					// 营业执照有效开始时间验证
					if ($(this).is('#gsyyzzstime')) {
						if (valiter.isNull($.trim(this.value))) {
							$parent.find("sub").removeClass("info").addClass("onError").html("请选择营业执照有效开始时间");
						} else {
							$parent.find("sub").html(right);
						}
					}
					// 营业执照有效结束时间验证
					if ($(this).is('#gsyyzzetime')) {
						if (valiter.isNull($.trim(this.value))) {
							$parent.find("sub").removeClass("info").addClass("onError").html("请选择营业执照有效结束时间");
						} else {
							$parent.find("sub").html(right);
						}
					}
					// 企业开户银行验证在
					if ($(this).is('#qykhyh')) {
						if (valiter.bankName($.trim(this.value))
								|| $.trim(this.value).length < 4||$.trim(this.value).length>100) {
							$parent.find("sub").removeClass("info").addClass("onError").html("*4-100个非数字字符");
						} else {
							$parent.find("sub").html(right);
						}
					}
					// 企业开户帐号验证
					if ($(this).is('#khzh')) {
						if (valiter.isNull($.trim(this.value))
								|| valiter.numberInt2($.trim(this.value))) {
							$parent.find("sub").removeClass("info").addClass("onError").html("*10-19位的数字");
						} else {
							$parent.find("sub").html(right);
						}
					}
				
					//zhuce.c3 = "1";

		       }).keyup(function() {
					$(this).triggerHandler("blur");
				}).change(function() {
					$(this).triggerHandler("blur");
				});// end blur
		       
		       $('body :text').focus(function(){
		    	   var $parent = $(this).parent();
		    	   $parent.find("sub").removeClass().empty();
		    	// 企业中文名全称验证全称
					if ($(this).is('#compchinaname')) {//bug 4692 修改提示语言为“30个非特殊字符以内”
						$parent.find("sub").addClass("info").html("*30个非特殊字符以内");
					}	
					if ($(this).is('#compengname')) {
						$parent.find("sub").addClass("info").html("请输入100个非特殊字符以内的企业英文名称");
					}
					// 企业法人验证
					if ($(this).is('#qyfr')) {
						$parent.find("sub").addClass("info").html("*2-50个非特殊字符");
					}
					// 企业法人身份证号码验证
					if ($(this).is('#qyfrid')) {
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入证件号码");
					}

					// 企业地址验证
					if ($(this).is('#qydz')) {
						$parent.find("sub").removeClass("onError").addClass("info").html("*50个非特殊字符以内");
					}
					// 企业电话
					if ($(this).is('#qydh')) {
							$parent.find("sub").removeClass("onError").addClass("info").html("*按7/8位填写");
					}	
					if($(this).is('#pPhone')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*按7/8位填写");
					}
					//常用联系人手机
					if($(this).is('#cysj')){
							$parent.find("sub").removeClass("onError").addClass("info").html("*输入手机号码");							
					}
					if($(this).is('#pMobile')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入手机号码");							
					}
					//企业传真
					if($(this).is('#qycz')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*按7/8位填写");
					}
					if($(this).is('#pFax')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*按7/8位填写");
					}
					//用户电子邮箱
					if($(this).is('#youxiang')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入邮箱");
					}	
					//企业电子邮箱
					if($(this).is('#qymail')){
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入邮箱");
					}	
		            if( $(this).is('#compcode') ){
		            	$parent.find("sub").addClass("info").html("*10个非特殊字符以内");
		            }
		            // 工商营业执照验证  //fix bug 3322
					if ($(this).is('#gsyyzzid')) {
						$parent.find("sub").addClass("info").html("*不多于15位数字");
					}
					 //fix bug 3301
					if ($(this).is('#gsyyzzetime')) {
						//$parent.find("sub").addClass("info").html("*输入营业执照有效开始时间");
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入营业执照有效开始时间");
					}
					if ($(this).is('#gsyyzzstime')) {
						//$parent.find("sub").addClass("info").html("*输入营业执照有效结束时间");
						$parent.find("sub").removeClass("onError").addClass("info").html("*输入营业执照有效结束时间");
					}
					// 企业开户银行验证在 //fix bug 3322
					if ($(this).is('#qykhyh')) {
						$parent.find("sub").addClass("info").html("*4-100个非数字字符");
					}
					// 企业开户帐号验证
					if ($(this).is('#khzh')) {
						$parent.find("sub").addClass("info").html("*10-19位的数字");
					}
					if ($(this).is('#yzbm')) {
						$parent.find("sub").addClass("info").html("*6位邮政编码");
					}
					
		       });     
		       
		       
				
		},
		
		  initInfo:function (){
				if(companyId>0){
				    //	执行读取的方法
					$.ajax({
						url : root+"/customerAdmin/findCompInfoById.action",
						type : "POST",
						contentType : "application/json",
						dataType : "json",
						data : $.toJSON({companyId:companyId}),
						timeout : 50000,
						async : false,
						success : function(data) {
							user_info.curUser = data;
							 var compLegalPerson = "";
							 var compCnName = "";
							 var compLegalPersonId = "";
							 var cityId = "";
							 var propertyId = "";
							 var categoryId = "";
							 var classId = "";
							 var compAddress = "";
							 var postCode = "";
							 var compPhone = "";
							 var relaMobile = "";
							 var compFax = "";
							 var compEmail = "";
							 var compOrgCode = "";
							 var busLicenseNum = "";
							 var blnStartTime = "";
							 var blnEndTime = "";			 
							 var compBankName = "";
							 var compBankAccount = "";		 
						   if(data){						   
						   		if(data.compLegalPerson){
						   			compLegalPerson = data.compLegalPerson;
						   		}
						   		if(data.compCnName){
						   			compCnName = data.compCnName;
						   		}
						   		if(data.compLegalPersonId){
						   			compLegalPersonId = data.compLegalPersonId
						   		}
						   		if(data.cityId){
						   			cityId = data.cityId;
						   		}
						   		if(data.compPropertyId){
						   			propertyId = data.compPropertyId;
						   		}
						   		if(data.compCategoryId){
						   			categoryId = data.compCategoryId;
						   		}
						   		if(data.compClassId){
						   			classId = data.compClassId;
						   		}
						   		if(data.compAddress){
						   			compAddress = data.compAddress; 
						   		}
						   		if(data.postCode){
						   			postCode = data.postCode;
						   		}
						   		if(data.compPhone){
						   			compPhone = data.compPhone;
						   		}
						   		if(data.relaMobile){
						   			relaMobile = data.relaMobile;
						   		}
						   		if(data.compFax){
						   			compFax = data.compFax;
						   		}
						   		if(data.compEmail){
						   			compEmail = data.compEmail;
						   		}
						   		if(data.compOrgCode){
						   			compOrgCode = data.compOrgCode;
						   		}
						   		if(data.busLicenseNum){
						   			busLicenseNum = data.busLicenseNum;
						   		}
						   		if(data.blnStartTime){
						   			blnStartTime = data.blnStartTime;
						   		}
						   		if(data.blnEndTime){
						   			blnEndTime = data.blnEndTime;
						   		}
						   		if(data.compBankName){
						   			compBankName = data.compBankName;
						   		}
						   		if(data.compBankAccount){
						   			compBankAccount = data.compBankAccount;
						   		}
						   		$("#qyfr").val(compLegalPerson);
								$("#compchinaname").val(compCnName);
								$("#qyfrid").val(compLegalPersonId);
								$("#szcs option[value='"+cityId+"']").attr("selected", true);
								//$("#qylb").val(propertyId);
								$("#qylb option[value='"+propertyId+"']").attr("selected", true);
								//$("#hylb").val(categoryId);
								$("#qydqdj option[value='"+classId+"']").attr("selected", true);
								//$("#qydqdj").val(classId);
								$("#hylb option[value='"+categoryId+"']").attr("selected", true);
								$("#qydz").val(compAddress);
								$("#yzbm").val(postCode);
								$("#qydh").val(compPhone);
								$("#cysj").val(relaMobile);
								$("#qycz").val(compFax);
								$("#qymail").val(compEmail);
								$("#compcode").val(compOrgCode);
								$("#gsyyzzid").val(busLicenseNum);
								$("#gsyyzzstime").val(blnStartTime);
								$("#gsyyzzetime").val(blnEndTime);
								$("#qykhyh").val(compBankName);
								$("#khzh").val(compBankAccount); 
	    	 }else{
	    //	页面置空	
	    		
	    	 }
				 }
			  });
			}
		},
	    	
	    	 insertInfo:function(){
				//企业信息
				data['tcompanyinfo']['compId'] = companyId;
				data['tcompanyinfo']['compLegalPerson'] = $.trim($("#qyfr").val());
				data['tcompanyinfo']['compCnName'] = $.trim($("#compchinaname").val());					
				data['tcompanyinfo']['compLegalPersonId'] = $.trim($("#qyfrid").val());					
				data['tcompanyinfo']['TCity']['cityId'] = $.trim($("#szcs").val());
				data['tcompanyinfo']['TProperty']['propertyId'] = $.trim($("#qylb").val());
				data['tcompanyinfo']['TCategory']['categoryId'] = $.trim($("#hylb").val());
				data['tcompanyinfo']['TClass']['classId'] = $.trim($("#qydqdj").val());
				data['tcompanyinfo']['compAddress'] = $.trim($("#qydz").val());
				data['tcompanyinfo']['postCode'] = $.trim($("#yzbm").val());
				data['tcompanyinfo']['compPhone'] = $.trim($("#qydh").val());
				data['tcompanyinfo']['relaMobile'] = $.trim($("#cysj").val());
				data['tcompanyinfo']['compFax'] = $.trim($("#qycz").val());
				data['tcompanyinfo']['compEmail'] = $.trim($("#qymail").val());
				data['tcompanyinfo']['compOrgCode'] = $.trim($("#compcode").val());
				data['tcompanyinfo']['busLicenseNum'] = $.trim($("#gsyyzzid").val());
				data['tcompanyinfo']['compBankName'] = $.trim($("#qykhyh").val());
				data['tcompanyinfo']['compBankAccount'] = $.trim($("#khzh").val());
				//用户邮箱
				data['tcompanyinfo']['relaEmail'] = $.trim($("#youxiang").val());
			// 传递时间类型的参数
				data['blnStartTime'] = $.trim($("#gsyyzzstime").val());
				data['blnEndTime'] = $.trim($("#gsyyzzetime").val());		
				$.ajax({
					url : root+"/customerAdmin/insertUserInfo.action",
					type : "POST",
					contentType : "application/json",
					dataType : "json",
					data : $.toJSON(data),
					//timeout : 10000,
					success : function(state) {
						if("true"==state){
							alert("成功：信息保存成功！");							
						}
						else if("false"==state){
							alert("失败：信息保存失败！");
						}
					//	getCompInfo();
					}
				});
			},
		
		valite : function() {
			var numError = $('#con_tab_1 .onError').length;
			if (numError) {
				alert("必要信息没有填写正确，请检查！");
				return false;// test
			}
			if("1"==cloudStr){
				//fix bug 2876 允许手工修改日期
				if(!valiter.date($("#gsyyzzstime").val())){
					alert("营业执照有效开始日期["+$("#gsyyzzstime").val()+"]没有填写正确，请检查！");
					return false;
				}
				if(!valiter.date($("#gsyyzzetime").val())){
					alert("营业执照有效结束日期["+$("#gsyyzzetime").val()+"]没有填写正确，请检查！");
					return false;
				}
			}			
			return true;// test
		},
		updatePrivateUser : function() {
			$.ajax({
				url : root+"/user/updateUser.action",
				type : 'POST',
				contentType : "application/x-www-form-urlencoded; charset=utf-8",
				data : {
					jsonStr : $.toJSON({						
						id : privateUser.id,	
						name : privateUser.name,
						state : privateUser.state,
						roleId : privateUser.roleId,
						email : $("#youxiang").val(),
						fax : $("#pFax").val(),
						phone : $("#pPhone").val(),
						mobile : $("#pMobile").val(),
						deptId : $("#pDept option:selected").attr("value")
					}),id:privateUser.id,name:privateUser.name,state:privateUser.state,roleId:privateUser.roleId,
					email:$("#youxiang").val(),fax:$("#pFax").val(),
					phone:$("#pPhone").val(),mobile:$("#pMobile").val(),deptId:$("#pDept option:selected").attr("value")
				},
				//contentType : "application/json",
				dataType : 'json',
				timeout : 5000,
				//async:false,
				cache : false,
				success : function(data) {
					if (data == "true") {
						//fix bug 2250
						alert("成功：信息保存成功！");	
					}else if(data=="false"){
						alert("失败：信息保存失败！");
					}
				}
			});
		}
//		savePage : function(pageNo) {
//				data['tcompanyinfo']['compCnName'] = $.trim($("#compchinaname").val());
//				data['tcompanyinfo']['compLegalPerson'] = $.trim($("#qyfr").val());
//				data['tcompanyinfo']['compLegalPersonId'] = $.trim($("#qyfrid").val());
//				data['tcompanyinfo']['compAddress'] = $.trim($("#qydz").val());
//				data['tcompanyinfo']['compPhone'] = $.trim($("#qydh").val());
//				data['tcompanyinfo']['TCity']['cityId'] = $.trim($("#szcs").val());
//				data['tcompanyinfo']['TProperty']['propertyId'] = $.trim($("#qylb").val());
//				data['tcompanyinfo']['TCategory']['categoryId'] = $.trim($("#hylb").val());
//				data['tcompanyinfo']['TClass']['classId'] = $.trim($("#qydqdj").val());
//				data['user']['mobile'] = $.trim($("#cysj").val());
//				data['user']['phone'] =$.trim($("#qydh").val());			
//				data['tcompanyinfo']['compOrgCode'] = $.trim($("#compcode").val());
//				data['tcompanyinfo']['busLicenseNum'] = $.trim($("#gsyyzzid").val());
//				data['tcompanyinfo']['compBankName'] = $.trim($("#qykhyh").val());
//				data['tcompanyinfo']['compCreater'] = data['user']['name'];
//				data['tcompanyinfo']['domain'] = data['user']['account'];
//				// 传递时间类型的参数
//				 data['blnStartTime'] = $.trim($("#gsyyzzstime").val());
//				 data['blnEndTime'] = $.trim($("#gsyyzzetime").val());
//		},		
//		holdPage : function(pageNo) {
//				// 保存已填项
//				$("#compchinaname").val(data['tcompanyinfo']['compCnName']);
//				$("#compchinashname").val(data['tcompanyinfo']['compCnAbbreviation']);
//				$("#compengname").val(data['tcompanyinfo']['compEnName']);
//				$("#compengshname").val(data['tcompanyinfo']['compEnAbbreviation']);
//				$("#qyfr").val(data['tcompanyinfo']['compLegalPerson']);
//				$("#qyfrid").val(data['tcompanyinfo']['compLegalPersonId']);
//				$("#zone").val(data['tcompanyinfo']['dataCenter']);
//				$("#qydz").val(data['tcompanyinfo']['compAddress']);
//				$("#yzbm").val(data['tcompanyinfo']['postCode']);
//				$("#qydh").val(data['tcompanyinfo']['compPhone']);
//				$("#cysj").val(data['user']['mobile']);
//				$("#qycz").val(data['tcompanyinfo']['compFax']);
//				$("#qymail").val(data['tcompanyinfo']['compEmail']);
//				$("#szcs").val(data['tcompanyinfo']['TCity']['cityId']);
//				$("#qylb").val(data['tcompanyinfo']['TProperty']['propertyId']);
//				$("#hylb").val(data['tcompanyinfo']['TCategory']['categoryId']);
//				$("#qydqdj").val(data['tcompanyinfo']['TClass']['classId']);
//				// 保存已填项
//				changeImg();
//				$("#compcode").val(data['tcompanyinfo']['compOrgCode']);
//				$("#gsyyzzid").val(data['tcompanyinfo']['busLicenseNum']);
//				$("#qykhyh").val(data['tcompanyinfo']['compBankName']);
//				$("#khzh").val(data['tcompanyinfo']['compBankAccount']);
//				$("#cpjl").val(data['tcompanyinfo']['manager']);
//				$("#cylxr").val(data['tcompanyinfo']['relationName']);
//				$("#cyszbmmc").val(data['tcompanyinfo']['relaDepName']);
//				$("#cygddh").val(data['tcompanyinfo']['relaPhone']);
//				$("#cysj").val(data['tcompanyinfo']['relaMobile']);
//				$("#cycz").val(data['tcompanyinfo']['relaFax']);
//				$("#cymail").val(data['tcompanyinfo']['relaEmail']);
//				$("#gsyyzzstime").val(data['blnStartTime']);
//				$("#gsyyzzetime").val(data['blnEndTime']);
//		
//		}
};


var valiter = {
		// 检查邮箱是否被占用
		checkEmailIsExist : function() {
			var flag = false;		
			var val = $.trim($("#youxiang").val());
			if(oldEmail != val){
				email = {
					email : val
				};		
				$.ajax({
					url : "../customerAdmin/checkEmailIsExist.action",
					type : 'POST',
					//contentType : "application/json",
					dataType : "json",
					async : false,
					data : email,
					timeout : 50000,
					success : function(state) {
						if(state=="true"){
							flag = true;							
						}
					}
				});	
			}
			return flag;
		},

		checkEmail : function(email) {
			// var emailRegExp = new
			// RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
			if (!/^\w+((-\w+)|(\.\w+))*\@[A-Za-z0-9]+((\.|-)[A-Za-z0-9]+)*\.[A-Za-z0-9]+$/
					.test(email)) {
				return false;
			} else {
				return true;
			}
		},
		isNull : function(val) {
			if ($.trim(val) != "" && val != null) {
				return false;
			} else {
				return true;
			}
		},
		max : function(val, max) {
			try {
				if (parseFloat(val) <= parseFloat(max))
					return true;
				return false;
			} catch (e) {
				return false;
			}
		},

		min : function(val, min) {
			try {
				if (parseFloat(val) >= parseFloat(min))
					return true;
				return false;
			} catch (e) {
				return false;
			}
		},
		chinese : function(val) {
			try {
				if (/^[\u4e00-\u9fa5]+$/i.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		numberInt : function(val) {
			try {
				if (/^[-+]?[\d]+$/.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		// 只能输入*10-19位的数字
		numberInt2 : function(val) {
			try {
				if (/^[_0-9]{10,19}$/.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		// 开户银行验证，不能输入数字和特殊字符
		bankName : function(val) {
			try {
				if (/^[\u4E00-\u9FA5a-zA-Z_\/-]+$/.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		date : function(val) {//fix bug 2876 修改日期效验
			try {
				var regex = /^(\d{4})-(\d{2})-(\d{2})$/;
				if (!regex.test(val))
					return false;
				var d = new Date(val.replace(regex, '$1/$2/$3'));
				var back = (parseInt(RegExp.$2, 10) == (1 + d.getMonth()))
						&& (parseInt(RegExp.$3, 10) == d.getDate())
						&& (parseInt(RegExp.$1, 10) == d.getFullYear());
				return back;
			} catch (e) {
				return false;
			}
		},
		telephone : function(val) {
			try {
				if (/^((0\d{2,3})-)?(\d{7,8})(-(\d{3,}))?$/.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		cellphone : function(val) {
			try {
				if (val.length == 11 && /^1[358]\d{9}$/.test(val))
					return false;
				return true;
			} catch (e) {
				return false;
			}
		},
		// 身份证验证
		iscardid : function(val) {
			try {
				if (valiter.isNull(val)) {
					return false;
				}
				if (valiter.numberInt(val)) {
					return false;
				}
				if (!(val.length == 15 || val.length == 18)) {
					return false;
				}
				return true;
			} catch (e) {
				return false;
			}
		},
		// 身份证验证
		checkIdcard : function(idcard) {
			var Errors = new Array("1", // 验证通过返回值
			"证件位数不对!", "出生日期超出范围或含有非法字符!", "证件校验错误!", "证件地区非法!");
			var area = {
				11 : "北京",
				12 : "天津",
				13 : "河北",
				14 : "山西",
				15 : "内蒙古",
				21 : "辽宁",
				22 : "吉林",
				23 : "黑龙江",
				31 : "上海",
				32 : "江苏",
				33 : "浙江",
				34 : "安徽",
				35 : "福建",
				36 : "江西",
				37 : "山东",
				41 : "河南",
				42 : "湖北",
				43 : "湖南",
				44 : "广东",
				45 : "广西",
				46 : "海南",
				50 : "重庆",
				51 : "四川",
				52 : "贵州",
				53 : "云南",
				54 : "西藏",
				61 : "陕西",
				62 : "甘肃",
				63 : "青海",
				64 : "宁夏",
				65 : "新疆",
				71 : "台湾",
				81 : "香港",
				82 : "澳门",
				91 : "国外"
			};

			var idcard, Y, JYM;
			var S, M;
			var idcard_array = new Array();
			idcard_array = idcard.split("");
			// 地区检验
			if (area[parseInt(idcard.substr(0, 2))] == null)
				return Errors[4];
			// 身份号码位数及格式检验		
			switch (idcard.length) {
			case 15:
				if(parseInt(idcard.substr(6, 2))+1900<1940){
					return Errors[2];
				}
				if ((parseInt(idcard.substr(6, 2)) + 1900) % 4 == 0
						|| ((parseInt(idcard.substr(6, 2)) + 1900) % 100 == 0 && (parseInt(idcard
								.substr(6, 2)) + 1900) % 4 == 0)) {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}$/;// 测试出生日期的合法性
				} else {
					ereg = /^[1-9][0-9]{5}[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}$/;// 测试出生日期的合法性
				}
				if (ereg.test(idcard))
					return Errors[0];
				else
					return Errors[2];
				break;
			case 18:
				if(parseInt(idcard.substr(6, 4))<1940){
					return Errors[2];
				}
				// 18位身份号码检测
				// 出生日期的合法性检查
				// 闰年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))
				// 平年月日:((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))
				if (parseInt(idcard.substr(6, 4)) % 4 == 0
						|| (parseInt(idcard.substr(6, 4)) % 100 == 0 && parseInt(idcard
								.substr(6, 4)) % 4 == 0)) {
					ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|[1-2][0-9]))[0-9]{3}[0-9Xx]$/;// 闰年出生日期的合法性正则表达式
				} else {
					ereg = /^[1-9][0-9]{5}19[0-9]{2}((01|03|05|07|08|10|12)(0[1-9]|[1-2][0-9]|3[0-1])|(04|06|09|11)(0[1-9]|[1-2][0-9]|30)|02(0[1-9]|1[0-9]|2[0-8]))[0-9]{3}[0-9Xx]$/;// 平年出生日期的合法性正则表达式
				}
				if (ereg.test(idcard)) {// 测试出生日期的合法性
					// 计算校验位
					S = (parseInt(idcard_array[0]) + parseInt(idcard_array[10]))
							* 7
							+ (parseInt(idcard_array[1]) + parseInt(idcard_array[11]))
							* 9
							+ (parseInt(idcard_array[2]) + parseInt(idcard_array[12]))
							* 10
							+ (parseInt(idcard_array[3]) + parseInt(idcard_array[13]))
							* 5
							+ (parseInt(idcard_array[4]) + parseInt(idcard_array[14]))
							* 8
							+ (parseInt(idcard_array[5]) + parseInt(idcard_array[15]))
							* 4
							+ (parseInt(idcard_array[6]) + parseInt(idcard_array[16]))
							* 2 + parseInt(idcard_array[7]) * 1
							+ parseInt(idcard_array[8]) * 6
							+ parseInt(idcard_array[9]) * 3;
					Y = S % 11;
					M = "F";
					JYM = "10X98765432";
					M = JYM.substr(Y, 1);// 判断校验位
					if (M == idcard_array[17])
						return Errors[0]; // 检测ID的校验位
					else
						return Errors[3];
				} else
					return Errors[2];
				break;
			default:
				return Errors[1];
				break;
			}

		},

		isUserName : function(val) {
			if (/^[a-zA-Z]{1}([a-zA-Z0-9]){5,29}$/.test(val))
				return true;

			return false;
		},
		isChinese : function(val) {
			if (/[\u4E00-\u9FA5\uF900-\uFA2D]/.test(val))
				return true;
			return false;
		}
	};