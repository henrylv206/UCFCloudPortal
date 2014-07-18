function bwConfig(info){
		var storageSize=info.storageSize;
		//var serviceProvider=info.serviceProvider;
		//var ipAddress=info.ipAddress;
		return storageSize+'Mbps';
}

function bwState(info){
	return stateName[info.state];
}
function bwRefRes(info){// bug 0003800
	var ip='IP：'+info.ipAddress;
	if(ip==undefined){
		ip='';
	}
	return ip;
}
function bwOperate(info){
	loadPopDiv_bw();
	var vXiugai='';
    if(info.state == "2"){
		vXiugai=''+
			//'	<a id="a1" href="#" >' +
			'		<input type="image" src="../images/tel_edit.png" title="公网带宽变更" onclick="javascript:bwmanage.showBandWidthDiv('+info.id+',\''+info.storageSize+'\', 1, \'' + info.templateId + '\', \'' + info.productId + '\');"/>' ;//+
			//'	</a>' ;
    }
	return vXiugai;
}

function loadPopDiv_bw(){	
	$("#add_confirm").empty();
	$("#add_confirm").load("../component/bandwidth/bwmanage.html","",function(){
		$("#close").click(function(){
			$("#add_confirm,.shade").fadeOut("fast");
			});
	});
}