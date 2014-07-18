var product={
	init:function(id){
		product.getProductById(id);	
	},
    
	getProductById:function(id) {
        $.ajax({    	
          url : "../cloud_mall/getProductById.action?id="+id,
          type : 'POST',          
          datatype : "json", //设置获取的数据类型为json
          async : false,
          success : function(data) {
      	  	if(data != null) {
		   		if(typeof(data) == "string" && data.indexOf("error") == 0) {
		   			alert(data);
		   			return;
				}else{
					if(data == null) {
						return;
					}else{
						product.pro = data;
						var _unit = product.pro.unit;
                       	if(_unit == 'Y') {
                       		product.pro.unitC = '年';
                       	}
                       	else if(_unit == 'M') {
                       		product.pro.unitC = '月';
                        }else if (_unit == "W") {
                        	product.pro.unitC = "周";
                       	}
                      	else if(_unit == 'D') {
                      		product.pro.unitC = '日';
                        }
                       	else if (_unit == "H") {
                        	product.pro.unitC = "小时";
                        }
                        else if (_unit == "S") {
                        	product.pro.unitC = "GB";
                        }
					}
				}
      		}else{
      			return;
      		}
          }
        });
	}
	
};