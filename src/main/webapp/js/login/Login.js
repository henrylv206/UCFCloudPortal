$(function() {
	$.Login || ($.Login = Login);
	Login.init();			
});

window.onload = function(){
	javascript:window.history.forward(1);  
};  

var Login = {
	root : "/UCFCloudPortal",
	ddd : "",
	account_cookie:"account_cookie",
	login_cookie:"login_cookie",
	expires:7,
	init : function() {			
		$("#remUserName").removeAttr("checked");
		$("#remLogin").removeAttr("checked");
		
		Login.getAccountCookie();	
					
		// 设置jQuery AJAX缓存
		$.ajaxSetup({
			cache : true
		});
	
		$("#zhanghao").focus();
			
		var solf = this;
		$.getScript(solf.root + "/js/server.js", function(e) {
			Login.server = server;
			server.init();
		});
			
		$("#login-submit1").click(function() {		
			Login.loginAt1();
		});
				
		// Enter提交 
		$("#con_tab_1").keydown(function(event) {
			if (event.keyCode == 13) {
				Login.loginAt1();
			}
		});
						
		$("#remUserName").click(function(){
			var account = $.trim($("#zhanghao").val());
			var checkBoxs = $("#remUserName:checked"); 
			if(checkBoxs.length>0){				
				//console.log(account);
				if(commonUtils.len(account)==0){
					alert("请先输入用户名！");
					$("#remUserName").attr("checked",false);
					return;
				}
				$.cookie(Login.account_cookie, account, { expires: Login.expires }); 	
			}
			else $.cookie(Login.account_cookie, "", { expires: Login.expires }); 		
		});	
		
		$("#remLogin").click(function() {
//			var data = {
//					"account":"",
//					"password":""
//			};
            //fix bug 3305
			var data = "";
			var account = $.trim($("#zhanghao").val());			
			var password = $("#password").val();			
			var checkBoxs = $("#remLogin:checked"); 
			
			if(checkBoxs.length>0){			
				if(commonUtils.len(account)==0||commonUtils.len(password)==0){
					alert("请输入用户名和密码！");
					$("#remLogin").attr("checked",false);
					return;
				}			
//				var data = {
//						"account":account,
//						"password":password
//					};
				data = account+"&"+password;
				//console.log($.toJSON(data));
				$.cookie(Login.login_cookie, data, { expires: Login.expires }); 		
			}
			//fix buf 3312
			else {				
				//console.log($.toJSON(data));
				$.cookie(Login.login_cookie, data, { expires: Login.expires }); 		
			}	
		});	
	},

	loginAt1 : function() {
		var b, c, loginData;
		b = $("#zhanghao").val();		
		c = $("#password").val();
		code = $("#yzm").val();
		
		// 验证输入有效性
		if (b == "") {
			alert("请输入帐号！");
			$("#zhanghao").focus();
			return false;
		}

		if (c == "") {
			alert("请输入密码！");
			$("#password").focus();
			return false;
		}
		if (code == "") {
			alert("请输入验证码！");
			$("#yzm").focus();
			return false;
		}
		loginData = {
			username : b,
			password : c,
			loginType : 1
		};
//		Login.postData("Login", loginData);			
			
		// 验证验证码
		$.ajax({
			url : Login.root+"/customerAdmin/verifyCode.action",
			type : 'POST',
			data : {
				yzm : code
			},
			async : false,
			dataType : 'json',
			success : function(state) {
				if (state == "false") {
					alert("验证码错误！");
					changeImg();
					$("#yzm").val("");
					$("#yzm").focus();
					return false;
				}
				// 验证码session超时
				if (state == "timeout") {
					alert("操作超时，请重新输入验证码！");
					changeImg();
					$("#yzm").val("");
					$("#yzm").val("");
					$("#yzm").focus();
					return false;
				}
				// 验证码正确
				$.ajax({
					url : Login.root+"/customerAdmin/login.action",
					type : 'POST',
					async : false,
					data :loginData,
					dataType : 'json',
					success : function(state) {
						if (state == "true") {
						    //登录后进入工作区
							window.location.href = "/UCFCloudPortal/jsp/my_service.jsp";							
						} 
						else if(state=="inactive"){
							alert("帐号不存在或等待审核！");
							changeImg();
							$("#yzm").val("");
							$("#zhanghao").focus();
						}
						else if(state=="false"){
							alert("帐号或密码错误！");
							changeImg();
							$("#yzm").val("");
							$("#password").val("");
							$("#password").focus();
						}
						else if(state=="notactive"){//fix buf 3040 增加用户激活功能
							alert("帐号未激活，请先激活帐号！");
							changeImg();
							$("#yzm").val("");
							$("#password").val("");
							$("#password").focus();
						}else if(state=="pause"){//fix bug 6734
							alert("帐号被挂起，请等待管理员解挂！");
							changeImg();
							$("#yzm").val("");
							$("#password").val("");
							$("#password").focus();
						} else if("authorize"==state){
							alert("用户无权限登录该系统！");
							changeImg();
							$("#yzm").val("");
							$("#password").val("");
							$("#password").focus();
						}			
					}
				});	
			}

		});

	},
	
	cookieLogin:function(loginData) {
		$.ajax({
			url : Login.root+"/customerAdmin/login.action",
			type : 'POST',
			async : false,
			data :loginData,
			dataType : 'json',
			success : function(state) {
				if (state == "true") {
				    //登录后进入工作区
					if(checkHasMonitor()){//判断是否申请了云监控
						$.post("/UCFCloudPortal/jsp/set_session_value.jsp?mon=true");//更新SESSION
						showTopoDiv();//进入网络拓扑页面
					}else{
						$.post("/UCFCloudPortal/jsp/set_session_value.jsp?mon=false");//更新SESSION
						window.location.href = "/UCFCloudPortal/jsp/my_service.jsp";
					}				
				} 
				else if(state=="inactive"){	
					$("#zhanghao").focus();
				}
				else if(state=="false"){					
					$("#zhanghao").focus();
				}
				return status;
			}
		});	
	},
	
	getAccountCookie:function() {	
		var account = $.cookie(Login.account_cookie);
		//console.log(account);
		if(null!=account&&account!=''){
			$("#zhanghao").val(account);
		}
	}	
};
