//fix bug 3310
$(function() {	
	$.ajaxSetup({cache:false});//设置jQuery ajax缓存

	$("#savepwd").click(function(){
		updateUserPwd();
	});

	
	$("#oldpwd").focus(function() {
	    $(".oldpwd").html("");
	});
	$("#newpwd").focus(function() {
	    $(".newpwd").html("");
	});
	$("#renewpwd").focus(function() {
	    $(".renewpwd").html("");
	});

});
var root = "/UCFCloudPortal";
function oldPwdEquals(){
	   $.ajax({
			url : root+"/user/oldPwdEquals.action",
			//type : "POST",
			contentType : "application/json",
			data : {
				pwd:$("#oldpwd").val()
			},
			dataType : 'json',
			success : function(result) {
				if (result== "true") {
					$(".oldpwd").html("");
				}else{							
				   $("#oldpwd").val("");
				   $(".oldpwd").html("原密码输入有误，请重新输入！");
				}
			}
		});
	};
function updateUserPwd(){
	   if($("#oldpwd").val()=="" || $("#newpwd").val()=="" ||$("#renewpwd").val()==""){
     //to fix 1958
		   alert("原密码、新密码、确认密码不能为空！");
		   return;
      }
      else if($(".oldpwd").html()!="" || $(".renewpwd").html()!=""){
    	  alert("输入信息有误,请重新输入！");
    	  return ;
      }
	   //fix bug 7804
	 var numError = $('#con_tab_2 .onError').is(":visible");
	 if (numError) {
		alert("必要信息没有填写正确，请检查！");
		return false;// test
	 }
   
   $.ajax({
		url : root+"/user/updateUserPwd.action",
		//type : "POST",
		contentType : "application/json",
		data : {
			pwd:$("#newpwd").val()
		},
		dataType : 'json',
		success : function(result) {
			if (result== "true") {
			   alert("恭喜，密码修改成功！");
			   reset();
			}
			else if(result== "false"){
				alert("失败，密码修改失败！");
			   reset();
			}
		}
	});
};
function loseBlur(){
	if($("#newpwd").val().length<8||$("#newpwd").val().length>32){
		$(".newpwd").html("请输入8-32位密码！");
	}	
	else if($("#renewpwd").val()!=$("#newpwd").val()){
   		$(".renewpwd").html("新密码两次输入不一致，请重新输入！");
   }else{
      $(".renewpwd").html("");
   }
};
function reset() {
	$(":password").each(function() {
		$(this).val('');
	});	
}
