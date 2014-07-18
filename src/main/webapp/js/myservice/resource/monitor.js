var map='';    	
$.getScript(resource.root + "/js/hashMap.js", function(e){
	map = new Map();
});

var mo = map;
var dataF = [];
var vmID = '';
var totalPoints = 300;
var updateInterval = 10000;
var timeoutId = '';
var tickSize = 240;
var monitormg = {   
    root: "/UCFCloudPortal",  
    rs : {},
     cpu_plot : '',
     mem_plot : '',
     net_plot : '',
     disk_plot : '',
    init : function(id) {
      /**  var end = new Date();
        var start = monitormg.nextNDate(end,-1);
        var vmidList = {
            vmid : id,
            type : 'VM',
            startTime : start,
            endTime : end.getTime()
        };

        $.ajax({
            type : "POST",
            url : resource.root + "/cloud_mall/getMonitorData.action",
            data : vmidList,
            dataType : 'json',
            async : false,
            global : false,
            success : function(data) {
                if(data.length>0&&data.length<240){
                	tickSize=5;
                }
            }
        });
        */
    },

    getMonitorInfo : function(code) {
        var info = code;
        info = info.replace("vm", "虚拟机");
        info = info.replace("mc", "小型机");
		info = info.replace("pm", "物理机");//fix bug 7766 云监控服务，物理机未替换为中文
        info = info.replace("vl", "弹性块存储");
        info = info.replace("lb", "负载均衡");
        info = info.replace("fw", "防火墙");
        info = info.replace("pnip", "弹性公网IP");
        info = info.replace("bw", "公网带宽");
        return info;
    },

    nextNDate : function(begin, n){
        //var beginDateStr = "2007/01/29";
        //创建开始时间对象
     beginDate = begin;
        //设置增加的天数
        beginDate.setDate(beginDate.getDate() + n);

        //获取增加天数后的时间字串
        //-3- js中getMonth()的返回值从0开始到11，因此要加1，才是正确的值
        var endDateStr =beginDate.getFullYear() + "-" + (beginDate.getMonth()+1) + "-" + beginDate.getDate() + " "+ beginDate.getHours() + ":"+ beginDate.getMinutes() + ":" +beginDate.getSeconds();
        endDateStr = endDateStr.replace("-","/").replace("-","/");
        var rs=new Date(Date.parse(endDateStr.replace(/-/g, "/")));
        return rs.getTime();
        //输出：2007/2/5
        //alert(beginDate.toLocaleString());
    },


    initBackground : function(){
        var jg = new jsGraphics("moDiv");
        jg.setColor("#F0F0F0");
        jg.fillRect(0,0,700,650);
        jg.paint();     
    },

       getCPUData : function(){
            return {
                            color : "purple",
                            label : " CPU (%)",
                            data : monitormg.rs.get("cpu")
                        };
        },
        getMEMData : function(){
            return {
                            color : "blue",
                            label : " MEM (%)",
                            data : monitormg.rs.get("mem")
                        };
        },

                        
    initPlot : function() {
        monitormg.cpu_plot = $.plot($("#CpuArea"), [monitormg.getCPUData()], optionsCpu);
        monitormg.mem_plot = $.plot($("#MemArea"), [monitormg.getMEMData()], optionsMem);
        monitormg.net_plot = $.plot($("#NetArea"), [{
                            color : "green",
                            label : " NetRead (Mbps)",
                            data : monitormg.rs.get("net_r"),
                            lines : {
                                show : true
                            }
                        }, {
                            color : "red",
                            label : " NetWrite (Mbps)",
                            data : monitormg.rs.get("net_w"),
                            lines : {
                                show : true
                            }
                        }], optionsNet);
        monitormg.disk_plot = $.plot($("#DiskArea"), [ {
                            color : "green",
                            label : " DiskRead (Mbps)",
                            data : monitormg.rs.get("disk_r"),
                            lines : {
                                show : true
                            }
                        }, {
                            color : "red",
                            label : " DiskWrite (Mbps)",
                            data : monitormg.rs.get("disk_w"),
                            lines : {
                                show : true
                            }
                        }], optionsDisk);       
        $('#MonitorCancel').show();
    },
    
     drawPlot : function(){
        monitormg.cpu_plot.setData([monitormg.getCPUData()]); 
        monitormg.cpu_plot.draw();
        
        monitormg.mem_plot.setData([monitormg.getMEMData()]); 
        monitormg.mem_plot.draw();
        
        monitormg.net_plot.setData([{
                            color : "green",
                            label : " NetRead (Mbps)",
                            data : monitormg.rs.get("net_r"),
                            lines : {
                                show : true
                            }
                        }, {
                            color : "red",
                            label : " NetWrite (Mbps)",
                            data : monitormg.rs.get("net_w"),
                            lines : {
                                show : true
                            }
                        }]);  
        monitormg.net_plot.draw();
        
        monitormg.disk_plot.setData([ {
                            color : "green",
                            label : " DiskRead (Mbps)",
                            data : monitormg.rs.get("disk_r"),
                            lines : {
                                show : true
                            }
                        }, {
                            color : "red",
                            label : " DiskWrite (Mbps)",
                            data : monitormg.rs.get("disk_w"),
                            lines : {
                                show : true
                            }
                        }]);  
        monitormg.disk_plot.draw();         
    },    
    
    getMonitorData : function(type,id){//bug 0004851 0004528 0005140
        var cpu = [], mem = [], net_r = [], net_w = [], disk_r = [], disk_w = [];
        //bug 0005533
        $.getScript(resource.root + "/js/hashMap.js", function(e){
        	map = new Map();
        });
        mo = map;
        var end = new Date();
        var start = monitormg.nextNDate(end,-1);
        var vmidList = {
            vmid : id,
            type : type,
            startTime : start,
            endTime : end.getTime()
        };

        $.ajax({
            type : "POST",
            url : resource.root + "/cloud_mall/getMonitorData.action",
            data : vmidList,
            dataType : 'json',
            async : false,
            global : false,
            success : function(data) {
                dataF = data;
            }
        });
        if(dataF.length > 0){
            for(i=0;i<dataF.length;i++){
                var info = dataF[i];
                var ti = new Date(Date.parse(info.timeStamp.replace(/-/g, "/")));
                //bug 0004000 0003999 0003997 0003996
                //cpu从接口拿到的数值是以10000表示100%的，所以需要除以100得到可显示的数值
                cpu.push([ti, info.cpuUt/100]);
                mem.push([ti, info.memUt/100]);
                net_r.push([ti, Math.round(info.networkRead*10/1024)/10]);
                disk_r.push([ti, Math.round(info.diskRead*10/1024)/10]);//bug 0005533
                net_w.push([ti, Math.round(info.networkWrite*10/1024)/10]);
                disk_w.push([ti, Math.round(info.diskWrite*10/1024)/10]);              
            }
                    
            mo.put("cpu", cpu);
            mo.put("mem", mem);
            mo.put("net_r", net_r);
            mo.put("net_w", net_w);
            mo.put("disk_r", disk_r);
            mo.put("disk_w", disk_w);
        }
        return mo;      
    }   
};

     var optionsCpu = {
        series : {
            shadowSize : 0
        }, // drawing is faster without shadows
        yaxis : {
            min : 0,
            max :100
        },
        xaxis : {
            mode : "time",
            timeformat : "%h %p",
            minTickSize : [1, "minute"],
            //tickSize : [tickSize, "minute"],
            twelveHourClock : false,
            tickFormatter: function (val, axis) {   
                var d = new Date(val);   //bug 0004001 0004019
                return commonUtils.trim(d.toLocaleTimeString().substr(0,5).replaceAll(':',' ')).replaceAll(' ',':');//转为当地时间格式
            }   

        },
        legend : {
            position : 'nw'
        }
    };
    var optionsMem = {
        series : {
            shadowSize : 0
        }, // drawing is faster without shadows
        yaxis : {
            min : 0,
            max :100
        },
        xaxis : {
            mode : "time",
            timeformat : "%h %p",
            minTickSize : [1, "minute"],
            //tickSize : [tickSize, "minute"],
            twelveHourClock : false,
            tickFormatter: function (val, axis) {   
                var d = new Date(val);   //bug 0004001
                return commonUtils.trim(d.toLocaleTimeString().substr(0,5).replaceAll(':',' ')).replaceAll(' ',':');//转为当地时间格式
            }   
        },
        legend : {
            position : 'nw'
        }
    };
    var optionsNet = {
        series : {
            shadowSize : 0
        }, // drawing is faster without shadows
        yaxis : {
            min : 0,
            autoscaleMargin : 1.5
        },
        xaxis : {
            mode : "time",
            timeformat : "%h %p",
            minTickSize : [1, "minute"],
            //tickSize : [tickSize, "minute"],
            twelveHourClock : false,
            tickFormatter: function (val, axis) {   
                var d = new Date(val);   //bug 0004001
                return commonUtils.trim(d.toLocaleTimeString().substr(0,5).replaceAll(':',' ')).replaceAll(' ',':');//转为当地时间格式
            }   
        },
        legend : {
            position : 'nw'
        }
    };
    var optionsDisk = {
        series : {
            shadowSize : 0
        }, // drawing is faster without shadows
        yaxis : {
            min : 0,
            autoscaleMargin : 2
        },
        xaxis : {
            mode : "time",
            timeformat : "%h %p",
            minTickSize : [1, "minute"],
            //tickSize : [tickSize, "minute"],
            twelveHourClock : false,
            tickFormatter: function (val, axis) {   
                var d = new Date(val);   //bug 0004001
                return commonUtils.trim(d.toLocaleTimeString().substr(0,5).replaceAll(':',' ')).replaceAll(' ',':');//转为当地时间格式
            }   
        },
        legend : {
            position : 'nw'
        }
    };
function monitorConfig(info) {
	var monitorInfo = monitormg.getMonitorInfo(info.networkDesc);
	monitorInfo = monitorInfo != undefined ? monitorInfo : '';
	return monitorInfo;
}

function monitorState(info) {
	return stateName[info.state];
}

function monitorOperate(info) {
	return '';
}

    function hideProductMonitor() {
        $("#resourceArea").show();
        $("#prev").show();
        $("#moDiv").hide();
        $("#MonitorCancel").hide();
        $("#monitorArea").hide();
        clearTimeout(timeoutId);
    }

    function showMonitor() {
        $("#resourceArea").hide();
        $("#prev").hide();
        $("#monitorArea").show();
        $("#moDiv").show();
        $("#MonitorCancel").show();
    }

var thread='';
var timerKey="my_monitor";
function update(type,vmid){  
    $("body").stopTime(timerKey);
    $("body").everyTime(updateInterval,timerKey,function() {  //10秒钟刷新一次
        if(vmid==undefined){
            return;
        }
        monitormg.rs = monitormg.getMonitorData(type,vmid);
        monitormg.initPlot();
        monitormg.drawPlot();
    });
}

    function update1(vmid) {//bug 0003230
        timeoutId = setTimeout(function(){
        	if(vmid==undefined){
        		return;
        	}
            monitormg.rs = monitormg.getMonitorData(vmid);
            monitormg.drawPlot();
            setTimeout(arguments.callee, updateInterval);
        },updateInterval);
        Concurrent.Thread.sleep(updateInterval);
        if(thread!=''){
           thread.kill();
           thread = Concurrent.Thread.create(update);//实现多线程
        }
    }

    function showProductMonitor(type,vmid) {
    	query();//bug 0003993
        $("#monitorArea").html("");
        $("#monitorArea").load(resource.root+"/component/monitor/monitor.html","",function(){   
            showMonitor();
            monitormg.rs = monitormg.getMonitorData(type,vmid);//bug 0005116
            monitormg.initPlot();
            update(type,vmid);//bug 0005116
        });   
    }