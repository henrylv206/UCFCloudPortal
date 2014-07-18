var server = {
    
    root: "/UCFCloudPortal",
    url: "/json/urlmap.js",
    urlmap: "",
    key: {
        type: 'POST',
        contentType: "application/json",
        dataType: "json",
        timeout: 30000
    },
    error: function(err){
 
    },
    init: function(){
        var solf = this;
        $.getScript(solf.root + "/js/jquery.json-2.2.min.js", function(e){
            $.getJSON(solf.root + "" + solf.url, null, function(json){
                solf.urlmap = json;
                $(function(){//bug 0003063
                	solf.callback();
                });                
            });
        });
    },
    callback: function(){
    },
    getJSON: $.getJSON,
    getServer: function(url, key){
        var solf = this;
        $.extend(key, this.key);
        
        key.url = solf.urlmap[url];
        if (key.data) {
        }
        else {
            key.data = {};
        }
        
        if(key.time_out == undefined){
        	var Newtimeout = key.timeout;
        }
        if(key.time_out != undefined){
        	var Newtimeout = key.time_out;
        }
        

        key.error = key.error || solf.error;
        $.ajax({
            url: solf.urlmap[url],
            type: key.type,
            contentType: key.contentType,
            dataType: key.dataType,
            data: $.toJSON(key.data),
            async : key.async,
            
            timeout: Newtimeout,
            success: function(json){
                key.success(json);
                $("input[type='text']").blur(function(){                	
                    var val = $(this).val();
                    if (val.length < 1) {
                        $(this).val("");
                        return;
                    }
                    //to fix bug 3887
                    //else if (!/^[\u4E00-\u9FA5a-zA-Z0-9_,.@\/-]+$/.test(val)) {
                    	//fix bug 3196
                    //	alert("该输入不能包含非法字符！");
                    //    $(this).val("");
                    //    return;
                    //}
                });
            },
            error: key.error
        });
    },
    load: function(dom, moName, success){
        var solf = this;
        try {
            $(dom).html("");
            $(dom).load(solf.root + "/component/" + moName[0] + "/" + moName[1] + ".html", null, function(){
                solf.loadJS(moName);
                if (typeof success == "function") {
                    success();
                }
            });
        } 
        catch (e) {
        }
    },
    loadMod: function(url, feature){
        this.load(".work", [url, feature]);
        if (main.leftaccordion) {
            for (var i = 0; i < main.leftaccordion.data.length; i++) {
                for (var a = 0; a < main.leftaccordion.data[i].href.length; a++) {
                    if (main.leftaccordion.data[i].href[a].feature == feature) {
                        $("#feature").html(main.leftaccordion.data[i].href[a].label);
                    }
                }
            }
        }
    },
    loadJS: function(moName){
        var solf = this;
        try {
            $.getScript(solf.root + "/js/" + moName[0] + "/" + moName[1] + ".js", function(e){
                window.main && (main[moName[1]] = window[moName[1]]);
                window[moName[1]].init && window[moName[1]].init();
                $("input[type='text']").blur(function(){
                    var val = $(this).val();
                    if (val.length < 1) {
                        $(this).val("");
                        return;
                    }
                    //to fix bug 3887
                    //else if (!/^[ \u4E00-\u9FA5a-zA-Z0-9_,.@\/-]+$/.test(val)) {
                    	//fix bug 3196
                    //	alert("该输入不能包含非法字符！");
                    //    $(this).val("");
                    //    return;
                    //}
                });
            });
        } 
        catch (e) {
//            alert(moName[1] + ".js"+" 文件加载错误");
        }
    },
    loadCSS: function(moName){
        //
    }
}
