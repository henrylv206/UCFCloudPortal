<%@ page contentType="text/html;charset=UTF-8" language="java" %>
 
<%
response.setHeader("Pragma","No-cache");   
response.setHeader("Cache-Control","no-cache");   
response.setDateHeader("Expires", 0);  
%>
<%
String resPlatformURL = "";
String resourceIDsJson = "";
String relationsJson = "";
String topIDArray = "";
    //TOPO test data
    resPlatformURL = "http://172.16.201.52:8080/SkyFormRes/";
    
    //resourceIDsJson = "[{\"sid\":220,\"id\":220,\"name\":\"VLAN220\",\"type\":\"VLAN\",\"host\":null},{\"sid\":1729,\"id\":46,\"name\":\"FIREWALL22\",\"type\":\"FIREWALL\",\"host\":null},{\"sid\":1727,\"id\":47,\"name\":\"LB333\",\"type\":\"LOADBALANCE\",\"host\":null},{\"sid\":1728,\"id\":43,\"name\":\"VM01\",\"type\":\"VM\",\"host\":null},{\"sid\":1730,\"id\":45,\"name\":\"VM02\",\"type\":\"VM\",\"host\":null},{\"sid\":1732,\"id\":48,\"name\":\"VM03\",\"type\":\"VM\",\"host\":null}]";
    //relationsJson = "[{\"sid\":1,\"from\":220,\"fromPort\":0,\"to\":1727,\"toPort\":0},{\"sid\":2,\"from\":220,\"fromPort\":0,\"to\":1728,\"toPort\":0},{\"sid\":3,\"from\":220,\"fromPort\":0,\"to\":1729,\"toPort\":0},{\"sid\":4,\"from\":220,\"fromPort\":0,\"to\":1730,\"toPort\":0},{\"sid\":5,\"from\":220,\"fromPort\":0,\"to\":1732,\"toPort\":0}]";
    //relationsJson = "[{\"sid\":1,\"from\":220,\"fromPort\":0,\"to\":1727,\"toPort\":0},{\"sid\":2,\"from\":220,\"fromPort\":0,\"to\":1728,\"toPort\":0},{\"sid\":3,\"from\":220,\"fromPort\":0,\"to\":1729,\"toPort\":0},{\"sid\":4,\"from\":220,\"fromPort\":0,\"to\":1730,\"toPort\":0},{\"sid\":5,\"from\":220,\"fromPort\":0,\"to\":1732,\"toPort\":0}]";
    
    //resourceIDsJson = "[{sid:220,id:220,name:VLAN220,type:VLAN,host:null},{sid:1729,id:46,name:FIREWALL22,type:FIREWALL,host:null},{sid:1727,id:47,name:LB333,type:LOADBALANCE,host:null},{sid:1728,id:43,name:VM01,type:VM,host:null},{sid:1730,id:45,name:VM02,type:VM,host:null},{sid:1732,id:48,name:VM03,type:VM,host:null}]";
    //relationsJson = "[{sid:1,from:220,fromPort:0,to:1727,toPort:0},{sid:2,from:220,fromPort:0,to:1728,toPort:0},{sid:3,from:220,fromPort:0,to:1729,toPort:0},{sid:4,from:220,fromPort:0,to:1730,toPort:0},{sid:5,from:220,fromPort:0,to:1732,toPort:0}]";
    
    resourceIDsJson = "[{$sid$:220,$id$:220,$name$:$VLAN22022$,$type$:$VLAN$,$host$:null},{$sid$:1729,$id$:46,$name$:$FIREWALL22$,$type$:$FIREWALL$,$host$:null},{$sid$:1727,$id$:47,$name$:$LB333$,$type$:$LOADBALANCE$,$host$:null},{$sid$:1728,$id$:43,$name$:$VM01$,$type$:$VM$,$host$:null},{$sid$:1730,$id$:45,$name$:$VM02$,$type$:$VM$,$host$:null},{$sid$:1732,$id$:48,$name$:$VM03$,$type$:$VM$,$host$:null}]";
    relationsJson = "[{$sid$:1,$from$:220,$fromPort$:0,$to$:1727,$toPort$:0},{$sid$:2,$from$:220,$fromPort$:0,$to$:1728,$toPort$:0},{$sid$:3,$from$:220,$fromPort$:0,$to$:1729,$toPort$:0},{$sid$:4,$from$:220,$fromPort$:0,$to$:1730,$toPort$:0},{$sid$:5,$from$:220,$fromPort$:0,$to$:1732,$toPort$:0}]";
    
    topIDArray = "[220]";
%>
<html>
<head>
    <title>我的资源网络拓扑图</title>

    <style type="text/css" media="screen">
        html, body { height: 100%; }
        body { margin: 0; padding: 0; overflow: auto; text-align: center; background-color: #ffffff; }
        object:focus { outline: none; }
        #flashContent { display: none; }
    </style>

    <!-- Enable Browser History by replacing useBrowserHistory tokens with two hyphens -->
    <!-- BEGIN Browser History required section -- >
    <link rel="stylesheet" type="text/css" href="history/history.css"/>
    <script type="text/javascript" src="history/history.js"></script>
    <!-- END Browser History required section -->

	<script src="../js/jquery-1.6.4.js"></script>
	<script src="../js/jquery-extend.js"></script>
    <script type="text/javascript" src="../js/swfobject.js"></script>
    <script type="text/javascript">
        // For version detection, set to min. required Flash Player version, or 0 (or 0.0.0), 
        // for no version detection.
        var swfVersionStr = "10.2.0";
        // To use express install, set to playerProductInstall.swf, 
        // otherwise the empty string.
        var xiSwfUrlStr = "playerProductInstall.swf";
        var flashvars = {
            resPlatformURL:'<%=resPlatformURL%>',
            resourceIDsJson:'<%=resourceIDsJson%>',
            relationsJson:'<%=relationsJson%>',
            topIDArray:'<%=topIDArray%>'
        };
		//alert("flashvars.resPlatformURL:"+flashvars.resPlatformURL);
		//alert("resourceIDsJson:"+flashvars.resourceIDsJson);
		//alert("relationsJson:"+flashvars.relationsJson);
		//alert("topIDArray:"+flashvars.topIDArray);
		//alert(jQuery.toJSONString(flashvars.resourceIDsJson));
        var params = {};
        params.quality = "high";
        params.bgcolor = "#ffffff";
        params.allowscriptaccess = "sameDomain";
        params.allowfullscreen = "true";
        var attributes = {};
        attributes.id = "portalTopo";
        attributes.name = "portalTopo";
        attributes.align = "middle";
        swfobject.embedSWF(
                "portalTopo.swf", //portalTopo.swf imageshow.swf
                "flashContent",
                "100%", "100%",
                swfVersionStr, xiSwfUrlStr,
                flashvars, params, attributes);
        // JavaScript enabled so display the flashContent div in case it is not replaced with a swf object.
        swfobject.createCSS("#flashContent", "display:block;text-align:left;");
    </script>
</head>
<body>
<!-- SWFObject's dynamic embed method replaces this alternative HTML content with Flash content when enough
     JavaScript and Flash plug-in support is available. The div is initially hidden so that it doesn't show
     when JavaScript is disabled.
-->
<div id="flashContent">
    <p>
        To view this page ensure that Adobe Flash Player version
        10.2.0 or greater is installed.
    </p>
    <script type="text/javascript">
        var pageHost = ((document.location.protocol == "https:") ? "https://" : "http://");
        document.write("<a href='http://www.adobe.com/go/getflashplayer'>"
        			  +"<img src='" + pageHost + "www.adobe.com/images/shared/download_buttons/get_flash_player.gif' "
        			  +"alt='Get Adobe Flash player' />"
        			  +"</a>");
    </script>
</div>

<noscript>
    <object classid="clsid:D27CDB6E-AE6D-11cf-96B8-444553540000" width="100%" height="100%" id="portalTopo">
        <param name="movie" value="portalTopo.swf"/><!-- imageshow.swf  portalTopo.swf  -->
        <param name="quality" value="high"/>
        <param name="bgcolor" value="#ffffff"/>
        <param name="allowScriptAccess" value="sameDomain"/>
        <param name="allowFullScreen" value="true"/>
        <!--[if !IE]>-->
        <object type="application/x-shockwave-flash" data="portalTopo.swf" width="100%" height="100%">
            <param name="quality" value="high"/>
            <param name="bgcolor" value="#ffffff"/>
            <param name="allowScriptAccess" value="sameDomain"/>
            <param name="allowFullScreen" value="true"/>
            <!--<![endif]-->
            <!--[if gte IE 6]>-->
            <p>
                Either scripts and active content are not permitted to run or Adobe Flash Player version
                10.2.0 or greater is not installed.
            </p>
            <!--<![endif]-->
            <a href="http://www.adobe.com/go/getflashplayer">
                <img src="http://www.adobe.com/images/shared/download_buttons/get_flash_player.gif"  alt="Get Adobe Flash Player"/>
            </a>
            <!--[if !IE]>-->
        </object>
        <!--<![endif]-->
    </object>
</noscript>
</body>
</html>
