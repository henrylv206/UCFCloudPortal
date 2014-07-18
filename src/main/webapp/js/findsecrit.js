var findsecrit = {
	root : "/UCFCloudPortal",
	//初始化事件
	init : function() {		
		//fix bug 4767
		$("#add_confirm #find_account").val("");
		$("#add_confirm #find_email").val("");
		$("#add_confirm #find_yzm").val("");
		findsecrit.changeNewImg();
		$("#find_account").focus();		
		
	},// end init
	
	/**提交事件*/ //fix bug 3096
	findPass:function(){
		//输入验证
	    var back = findsecrit.valide();
	    if(!back){
	    	return;
	    }
	    //验证验证码和提交
        var subData={
           username:$("#find_account").val(),
           emailaddress:$("#find_email").val()
        };
        $.ajax({
			url : findsecrit.root+"/customerAdmin/verifyCode.action",
			type : 'POST',
			data: {yzm:$("#find_yzm").val()},
			async : false,
			dataType : 'json',
			success: function(state){
        	 if(state=="false"){
				alert("验证码错误");
				findsecrit.changeNewImg()();
				$("#find_yzm").val("");
				$("#find_yzm").focus();
				return;
			 }
        	 if(state=="timeout"){
        		alert("操作超时，请重新输入验证码！");
        		findsecrit.changeNewImg()();
        		$("#find_yzm").val("");
        		$("#find_yzm").focus();
        	 }
			//验证码正确
			if(state=="true"){
				/**向后台提交数据*/
				findsecrit.postData("",subData);
			}
         }
     });
	    
	},//end findPass
	
	postData : function(url, data) {
		$.ajax({
			url : findsecrit.root+"/customerAdmin/findPazz.action",
			type : 'POST',
			data : data,
			async : false,
			dataType : 'json',
			success : function(state) {
				if (state == "true") {
					alert("发送成功,请查收邮件查看密码！");
					$(".popupDiv1,.shade").fadeOut("fast");
					changeImg();
					
				} else if (state == "invalid") {
					alert("用户名不存在或者注册邮箱不正确,请重新输入！");
					findsecrit.changeNewImg()();
	        		$("#find_yzm").val("");
					$("#find_account").focus();
				}else if(state == "unable"){
					alert("用户名还未生效，无法进行此类操作！");
					findsecrit.changeNewImg()();
	        		$("#find_yzm").val("");
					$("#find_account").focus();
				} 
				else {
					alert("发送失败");
				}
			}
			
		});
		closeBg();
	},//end postData

	postCode : function(url) {
		findsecrit.goflg = "0";
		var data = {
			yzm : $("#find_yzm").val()
		};
		findsecrit.server.getServer(url, {
			success : function(state) {
				if (state == "true") {
					findsecrit.goflg = "1";
				} else {
					findsecrit.goflg = "0";
				}
			},
			data : data
		});
	},//end postCode
	
	valide : function() {
		// 验证用户名
		if (valiter.isNull($("#find_account").val())) {
			alert("用户名不可为空");
			return false;
		}
		// 验证邮箱
		if (valiter.isNull($("#find_email").val())) {
			alert("邮箱不可为空");
			return false;
		}
		if (!valiter.checkEmail($("#find_email").val())) {
			alert("邮箱格式不正确");
			return false;
		}
		// 验证验证码
		 if($("#find_yzm").val()==""){
			 alert("验证码不能为空");
			 return false;
		 }
		return true;
	},//end valide
	showFind:function(){		
		//$("#add_confirm").show();		
		findsecrit.init();
		showBg("add_confirm", "dialog_content");		
	},
	changeNewImg:function (){
	    var imgSrc = $("#find_imgvercode");    
	    var src = imgSrc.attr("src");    
	    imgSrc.attr("src",chgUrl(src));    
	}
};


var valiter ={
	    checkEmail:function(email){
	    	var emailRegExp = new RegExp("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?");
	    	if (!emailRegExp.test(email)||email.indexOf('.')==-1){
	    		return false;
	    	}else{
	    		return true;
	    	}
	    },
		isNull:function(val){
	      if($.trim(val)!=""&&val!=null)
	      {
	    	  return false
	      }
	      else {
	    	  return true;
	      }
        },
       

        numberInt:function(val)
        {
            try
            {
                if(/^[-+]?[\d]+$/.test(val))
                    return false;
                return true;
            }
            catch(e)
            {
                return false;
            }
        },
        date:function(val)
        {
            try
            {
                var regex = /^(\d{4})-(\d{2})-(\d{2})$/;
                if(!regex.test(val)) return true;
                var d = new Date(val.replace(regex, '$1/$2/$3'));
                var back = (parseInt(RegExp.$2, 10) == (1+d.getMonth())) && (parseInt(RegExp.$3, 10) == d.getDate())&&(parseInt(RegExp.$1, 10) == d.getFullYear());
                return !back;
            }
            catch(e)
            {
                return false;
            }
        },
        telephone:function(val)
        {
            try
            {
                if(/^((\(0\d{2,3}\))|(0\d{2,3}-))?[1-9]\d{6,7}(-\d{1,4})?$/i.test(val))
                    return false;
                return true;
            }
            catch(e)
            {
                return false;
            }
        },
        cellphone:function(val){
        	try
            {
                if( val.length == 11 && /^1[358]\d{9}$/.test(val))
                	return false;
                return true;
            }
            catch(e)
            {
                return false;
            }
        }
};
