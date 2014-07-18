var mallitem={
	root: "/UCFCloudPortal",
	init:function(){
		$.ajaxSetup({cache:true}); 
		var solf = this;
					
		$("#serviceMenu").show();
		$("#pricerange_dl").hide();
		$("#itemServicePrice").hide();
    	$("#cloud_mall_sidebar,#cloud_mall_rightbar,#cloud_mall_hot_service").hide();
		
	    $.getScript(solf.root + "/js/server.js", function(e){
	        mallitem.server = server;
	        mallitem.server.callback = function(){	            	            
	        	mallitem.server.load("#hotsale_item", ["mall", "hotsale"]);
	        };
	        server.init();
	    });    
	    
		mallitem.ulObj = $("#productitem_ul");
		mallitem.liObj = $("#productitem_li_\\{id\\}").clone();
		mallitem.allProductitems = [];
		mallitem.loadAllProductitems();		
		/*mallitem.allInstallPackages = [];
		mallitem.queryInstallPackages();
    	var packs = mallitem.allInstallPackages;
    	mallitem.x86 = '';
    	mallitem.minicomputer = '';
    	mallitem.network = '';
    	mallitem.storage = '';
    	mallitem.ipsan = '';
    	mallitem.pm = '';
    	$(packs).each(function(i){
    		if(packs[i].type == 'INSTALL_X86'){
    			mallitem.x86 = packs[i].value;
    		}else if(packs[i].type == 'INSTALL_MINICOMPUTER'){
    			mallitem.minicomputer = packs[i].value;
    		}else if(packs[i].type == 'INSTALL_NETWORK'){
    			mallitem.network = packs[i].value;
    		}else if(packs[i].type == 'INSTALL_STORAGE'){
    			mallitem.storage = packs[i].value;
    		}else if(packs[i].type == 'INSTALL_IPSAN'){
    			mallitem.ipsan = packs[i].value;
    		}else if(packs[i].type == 'INSTALL_PHYSICALMACHINE'){
    			mallitem.pm = packs[i].value;
    		}
    	});*/

    },
    
	getCloudInfo : function() {
		var cloudInfo = 0;
		$.ajax({
			url: "/UCFCloudPortal/sysParameters/getCloudInfo.action",
			type: 'POST',
			dataType: 'json',
			async: false,
			success: function(data) {
				cloudInfo = data;
			}
		});
		return cloudInfo;
	},
    
    /*queryInstallPackages : function(){
		$.ajax({
      		url : "../sysParameters/queryInstallPackages.action",
      		type : "POST",
      		dataType : "json",
      		async : false,
      		global : false,
      		success : function(data) {
				mallitem.allInstallPackages = data;		
      		}
    	});
    },*/
    loadAllProductitems : function(){
		$.ajax({
      		url : "../frontProductItem/loadFrontAllItems.action",
      		type : "POST",
      		dataType : "json",
      		async : false,
      		global : false,
      		success : function(data) {
      			if(data != null) {
		   			if(typeof(data) == "string" && data.indexOf("error") == 0) {
		   				alert(data);
		   				return;
					}else{
						if(data == null || data.length == 0) {
							return;
						}else{
							$(data).each(function(i){
	              				if(mallitem.allProductitems[i] == undefined) {	              					
                     		    	mallitem.allProductitems.push(data[i]);
                     		    }            	
							});
						}
					}
      			}else{
      				return;
      			}
      		}
    	});

    },
	loadFirProductItem: function(){
    	mallitem.ulObj.html("");
    	var data = mallitem.allProductitems;    
    	$(data).each(function(i){
    		if(data[i].parentId == 1){
    			//to fix bug:3778
    			$.dataToContainer(mallitem.ulObj, mallitem.liObj, data[i]);

    			var firitemid = data[i].id; //一级目录id
    			//to fix bug 3033
    			var firitemname = data[i].name; //一级目录名称
    			//弹出导航	
				var _subItem = ''+    
				'<h1 id="secItemName_'+firitemid+'"><span class="more"></span><a href="mallsecondlist.jsp?surparentId='+1+'&&parentId='+firitemid+'&&fromFri=1">'+firitemname+'</a></h1>'+
            	'<div class="hidenav">'+
                '	<div class="tophover">'+
                '    	<span class="round1"></span>'+
                '    	<span class="round2"></span>'+
                '        <p></p>'+
                '    </div>'+
                ' 	<div class="midhover">'+

                '    	<div class="lefblock">'+
                '       	<h2>选分类</h2>';    		
    			
    		if(data[i].children != null){	
    			if(data[i].children.length > 0){
    				$(data[i].children).each(function(k){
    				var secItem = data[i].children[k];
    					
    			_subItem +=
                '            <div class="block">'+                
    			'                    <dt><a class="title" href="mallthirdlist.jsp?surparentId='+secItem.parentId+'&&parentId='+secItem.id+'&&secitemcode='+secItem.code+'&&fromSec=1">'+secItem.name+'</a></dt>';
                
                
				if(secItem.children != null){
					if(secItem.children.length > 0){
						$(secItem.children).each(function(j){
				_subItem +=			
                '                    <dd>'+
                '                    	<a class="tex" href="mallthirdlist.jsp?surparentId='+secItem.parentId+'&&parentId='+secItem.id+'&&itemid='+secItem.children[j].id+'&&secitemcode='+secItem.code+'">'+secItem.children[j].name+'</a><span style="color:#ccc;font-weight: normal">|</span>'+
                '					</dd>';
						});
					}
				}
                _subItem +=
                '            </div>';
    			 
                });  
                }
     		}
    			_subItem +=
                '         </div>'+
                '    </div>'+
                '	<div class="bothover">'+
                '    	<span class="round3"></span>'+
                '    	<span class="round4"></span>'+
                '        <p></p>'+
                '    </div>'+
                '</div>';
                //弹出导航结束
				$("#productitem_li_"+firitemid).append(_subItem);
    		}
    	});
	},
	loadSecProductItem: function(parentNodeId,_parentId,_itemid){
		mallitem.secUlObj = $("#secProductitem_ul");
		mallitem.secLiObj = $("#secProductitem_li_\\{id\\}").clone();
		mallitem.secUlObj.html("");
    	var data = mallitem.allProductitems;
    	var breadcrumb_1_name = "";
    	var breadcrumb_2_name = "";
    	
    	$(data).each(function(i){
    		if(data[i].id == _parentId){
    			breadcrumb_1_name = data[i].name;
    		}
    		if(data[i].id == _itemid){
    			breadcrumb_2_name = data[i].name;
    		}

    		//to fix bug 4600
            var _menu_li = 
            	'<li class="CenterDiv active" id="secProductitem_li_'+data[i].id+'" onclick="showItem('+data[i].id+',\''+data[i].name+'\')">'
                +'<b><a href="javascript:showItemPro('+data[i].id+');">'+data[i].name+'</a></b>'
                +'<span></span>'
                +'<div class="cat-item_list menu_con" style="display: none;" id="secProductitem_div_'+data[i].id+'">'
                +'</div>'
                +'</li>';    
    		
    		if(data[i].parentId == parentNodeId){//parentNodeId=1
    			//to fix bug 3909
//    			$.dataToContainer(mallitem.secUlObj, mallitem.secLiObj, data[i]);	
    			//to fix bug 4600 
    			mallitem.secUlObj.append(_menu_li);		
    			
				var _subItem = '<ul class="zk">'; 
				if(data[i].children != null){
					if(data[i].children.length > 0){//data[0]=弹性计算 data[0].children[0]=虚拟机
						$(data[i].children).each(function(j){
							_subItem += '<li><a href="mallthirdlist.jsp?surparentId='+data[i].id+'&&parentId='+data[i].children[j].id+'&&secitemcode='+data[i].children[j].code+'&&fromSec=1">'+data[i].children[j].name+'</a></li>';
						});									
					}									
				}
				_subItem += '</ul>';	
				$("#secProductitem_div_"+data[i].id).append(_subItem);				
    		}  		
    	});
    	$("#breadcrumb_1").append(breadcrumb_1_name);
    	if(breadcrumb_2_name != ""){
    		$("#breadcrumb_2").append('>&nbsp;'+breadcrumb_2_name);
    	}
    	//$("#breadcrumb_2").append('>&nbsp;<a>'+breadcrumb_2_name+'</a>');
    	//$("#breadcrumb_2").append('>&nbsp;<a href="mallsecondlist.jsp?surparentId='+1+'&&parentId='+_parentId+'&&itemid='+_itemid+'">'+breadcrumb_2_name+'</a>');
	},//_surparentId,_parentId,_itemid
	loadThiProductItem: function(surparentId,parentId,itemid){
		mallitem.thiUlObj = $("#thiProductitem_ul");
		mallitem.thiLiObj = $("#thiProductitem_li_\\{id\\}").clone();
		var breadcrumb_1_name = "";
    	var breadcrumb_2_name = "";
    	var breadcrumb_3_name = "";

		//mallitem.thiUlObj.html('<li class="CenterDiv"><b>弹性计算</b></li>');
    	var data = mallitem.allProductitems;
    	var parentName;
    	$(data).each(function(i){
    		if(data[i].id == surparentId){
    			breadcrumb_1_name = data[i].name;
    			parentName = data[i].name;
    			$("#thiParentitem_li").append('<b>'+parentName+'</b>');
    			//to fix bug:3733
    			$("#thiParentitem_li").click(function() {
    				//to fix bug:3794
    				$("#breadcrumb_2").html("");
    				$("#breadcrumb_3").html("");
    				mallproduct.getProductsByItem(surparentId,0);
    				
				});
    		}
    		if(data[i].id == parentId){
    			breadcrumb_2_name = data[i].name;
    		}
    		if(itemid != undefined){
	    		if(data[i].id == itemid){
	    			breadcrumb_3_name = data[i].name;
	    		}
    		}
    		 
    		if(data[i].parentId == surparentId){
    			//to fix bug 4600
    	   		var _menu_li_third = 
    	   	        '<li class="CenterDiv1 active" id="thiProductitem_li_'+data[i].id+'" onclick="showItem('+data[i].id+',\''+data[i].name+'\',\''+data[i].code+'\')">'
    	   	        +' <b><a href="javascript:showItemPro('+data[i].id+');">'+data[i].name+'</a></b>'
    	   	        +' <span></span>'
    	   	        +' <div class="cat-item_list menu_con"  id="thiProductitem_div_'+data[i].id+'" style="display: none;">'
    	   	        +'</div>'
    	   	        +'</li>';

//    			$.dataToContainer(mallitem.thiUlObj, mallitem.thiLiObj, data[i]);
    	   	    //to fix bug 4600
    	   		$("#thiProductitem_ul").append(_menu_li_third);
    	   		
				var _subItem = '<ul class="zk">'; 
				if(data[i].children != null){
					if(data[i].children.length > 0){
						$(data[i].children).each(function(j){
							//onclick="mallproduct.setBreadcrumb(\''+data[i].name+'\',\''+data[i].children[j].name+'\')"
							//_subItem += '<li><a href="mallthirdlist.jsp?surparentId='+data[i].parentId+'&&parentId='+data[i].id+'&&itemid='+data[i].children[j].id+'&&secitemcode='+data[i].code+'" >'+data[i].children[j].name+'</a></li>';
							_subItem += '<li><a href="javascript:mallproduct.getProductsByItem('+data[i].children[j].id+',0,0,0,\'bread_3\',\''+data[i].children[j].name+'\');mallproduct.showQueryCondition(\''+data[i].code+'\');" >'+data[i].children[j].name+'</a></li>';							
						});									
					}									
				}
				_subItem += '</ul>';	
				$("#thiProductitem_div_"+data[i].id).append(_subItem);						
    		}  		
    	});
    	//to fix bug:3531
    	$("#breadcrumb_1").append('<a href=\"mallsecondlist.jsp?surparentId='+1+'&&parentId='+surparentId+'&&itemid='+parentId+'&&fromFri=1\">'+breadcrumb_1_name+'</a>');
    	//to fix bug:3618
    	$("#breadcrumb_2").append(">&nbsp;"+breadcrumb_2_name);
    	//$("#breadcrumb_2").append(">&nbsp;<a href=\"#\">"+breadcrumb_2_name+"</a>");
    	if(breadcrumb_3_name != ""){
    		//$("#breadcrumb_3").append(">&nbsp;<a style=\"TEXT-DECORATION:none\">"+breadcrumb_3_name+"</a>");
    		//to fix bug:3618
    		$("#breadcrumb_3").append(">&nbsp;"+breadcrumb_3_name);
    	}
    	
    	//$("#breadcrumb_3").append('><a href="mallsecondlist.jsp?surparentId='+1+'&&parentId='+_parentId+'&&itemid='+_itemid+'">'+breadcrumb_3_name+'</a>');

	}
    
    
};