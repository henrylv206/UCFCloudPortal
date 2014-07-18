var privateSkySwitch = {
    
  getCloudInfo: function(){
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
  }

}
