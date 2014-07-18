<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>UCF云平台Wen</title>
<link rel="stylesheet" type="text/css" href="../css/reset.css"/>
<link rel="stylesheet" type="text/css" href="../css/layout.css"/>
<link rel="stylesheet" type="text/css" href="../css/myhome.css"/>
</head>

<body>
<div class="topShop">
  <div class="cont">
    <ul class="sever">
      <li class="add">收藏本站</li>
      <li class="tel">客服电话：010-28372837</li>
    </ul>
    <p class="user">您好：欢迎使用UCF云平台自服务门户！<a href="Login.html">请登录</a> 新用户？<a href="Register.html">[免费注册]</a>　 <a href="Indent.html">我的订单</a> <a href="#">在线咨询</a> <a href="help.html" target="_blank">帮助</a></p>
  </div>
</div>
<!--end topshop-->

<div class="header">
  <div class="logo-nav"><img src="../images/logo-nav.png" /></div>
  <div class="mysearch">
    <input type="text" class="text" />
    <input type="button" class="sbtn" />
  </div>
  <div class="nav">
    <div class="nav-adorn"></div>
    <ul class="lnktools">
      <li class="serve"><a class="on" href="Serve.html">我的服务</a></li>
      <li class="card"><a href="ShoppingCart.html">去购物车</a></li>
    </ul>
    <ul class="">
      <li><a href="../index.html">首页</a></li>
      <li><a href="CloudMall.html">云商城</a></li>
      <li><a href="#">联系我们</a></li>
    </ul>
  </div>
</div>
<!--end header-->
<div class="page">
  <div class="sSidebar">
    <div class="sMenu">
      <div class="menu">
        <div class="cont">
          <ul>
            <li><a href="Serve.html" class="current">我的服务</a></li>
            <li><a href="UserInfo.html">个人信息</a></li>
            <li><a href="Indent.html">我的订单</a></li>
            <li><a href="Messages.html">系统消息</a><span>（10）</span></li>
          </ul>
        </div>
        <div class="bottom"></div>
      </div>
    </div>
    <!--end sMenu-->
  </div>
  <!--end sSidebar-->
  
  
  <div class="sCont">
    <div class="breadcrumbs"><input class="back" type="button" value="返回上一步" style="float:right" onclick="javascript:window.location.href='IndentSee.html'"/>我的订单&nbsp;>&nbsp;212454213546&nbsp;>&nbsp;<span>网管应用</span></div>
    <div class="tblWrap">
      <table class="listTbl">
        <thead>
          <tr>
            <th class="number">序号</th>
            <th>资源名称</th>
            <th>配置明细</th>
            <th>资源类型</th>
            <th>资源描述</th>
            <th>创建时间</th>
            <th>修改时间</th>
            <th>资源状态</th>
            <th class="shop last">操作</th>
          </tr>
        </thead>
        <tfoot>
          <tr>
            <td colspan="9">
              <ul class="paging">
                <li><a href="#"><img src="../images/icons/icon-p-first.gif" /></a>
                <a href="#"><img src="../images/icons/icon-p-prev.gif" /></a>1/10
                <a href="#"><img src="../images/icons/icon-p-next.gif" /></a>
                <a href="#"><img src="../images/icons/icon-p-last.gif" /></a></li>
                <li>第<input type="text" class="text" />页<input type="button" value="GO" class="but" /></li>
              </ul>
            </td>
          </tr>
        </tfoot>
        <tbody>
          <tr>
            <td class="number"><span class="item">1</span></td>
            <td><span class="item">VM24244</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></span></td>
            <td><span class="item">正在创建</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">2</span></td>
            <td><span class="item">VM3543</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></td>
            <td><span class="item">已停止</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2 editicon2on"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">3</span></td>
            <td><span class="item">VM2556</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td><span class="item">运行中</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">4</span></td>
            <td><span class="item">VM7464</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></td>
            <td><span class="item">正在启动</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">5</span></td>
            <td><span class="item">VM2124</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td><span class="item">正在删除</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">6</span></td>
            <td><span class="item">VM4634</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></td>
            <td><span class="item">运行中</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">7</span></td>
            <td><span class="item">VM35555</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td><span class="item">已关机</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">8</span></td>
            <td><span class="item">VM7464</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td><span class="item">运行中</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">9</span></td>
            <td><span class="item">VM2342</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></td>
            <td><span class="item">已删除</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
          <tr>
            <td class="number"><span class="item">10</span></td>
            <td><span class="item">VM0888</span></td>
            <td><span class="item">1cpu,2/150g,Win2k3,NAS</span></td>
            <td><span class="item">多虚拟机</span></td>
            <td><span class="item">新申请</span></td>
            <td class="line6"><span class="time">2012-07-13</span><span class="time">19:12:20</span></td>
            <td class="line6"><span class="time"></td>
            <td><span class="item">运行中</span></td>
            <td class="shop last"><span class="item">
              <a href="#" class="editicon1"></a>
              <a href="#" class="editicon2"></a>
              <a href="ServeContinue.html" class="editicon3"></a>
              <a href="ServeLogout.html" class="editicon4"></a></span>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
    <!--end tblWrap-->
  </div>
  <!--end sCont-->
  
  <div class="clear"></div>
  
  <div class="active01">
    <h3>
      <a href="#">网站地图</a>
      <a href="#">联系我们</a>
      <a href="#">技术支持</a>
      <a href="#">后台管理</a>
    </h3>
  </div>
  <!--end active01-->
  
  <div class="active02">
    <ul>
      <li class="fast">物美价廉<span>正品行货又便宜</span></li>
      <li>211限时达<span>上午下单当日送到</span></li>
      <li class="last">售后100分<span>收检100分钟内给出处理意见</span></li>
    </ul>
  </div>
  <!--end active02-->  
</div>
<!--end page-->

<div class="footer">
  <div class="cont">
	<p>Copyright. @ 2008-2015 北京天云融创软件技术有限公司</p>
  </div>
</div>
<!--end footer-->
</body>
<script type="text/javascript" language="javascript" src="../js/jquery-1.7.2.min.js"></script>
<script type="text/javascript" language="javascript">
// table 隔行换色
$(document).ready(function(){
	$(".listTbl tr").mouseover(function(){
	$(this).addClass("over");
}).mouseout(function(){
	$(this).removeClass("over");})
	$(".listTbl tr:even").addClass("alt")
});
// close 
$(document).ready(function(){
	$("#close").click(function(){
	$(".popupDiv1,.shade").fadeOut("fast");
	})
});
</script>
</html>
