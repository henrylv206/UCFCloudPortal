	jQuery.fn.extend( {
		beautifyselect : function(options) {
			returnthis.each(function() {
				new jQuery.BeautifySelect(this, options);
			});
		}
	});
	
	
	if (!window.console) {
		var console = {
			log : function(msg) {
			}
		}
	};
	
	jQuery.BeautifySelect = function(selectobj, options) {
		var opt = options || {};
		opt.inputClass = opt.inputClass || "selectbox";
		opt.containerClass = opt.containerClass || "selectbox-wrapper";
		opt.hoverClass = opt.hoverClass || "hover";
		opt.currentClass = opt.selectedClass || "selected";
		opt.debug = opt.debug || false;
		opt.langue = opt.langue || "CN";
		opt.handler = opt.handler || null;
	
		var elm_id = selectobj.id;
		var active = 0;
		var hasfocus = 0;
 
		var $select = $(selectobj);
		var maxLi = 0;
		var minLi = $select.attr("width")
		var $options = $select.find("option");
		var charLen = 13;
		if (opt.langue == "EN") {
			charLen = 6;
		}
		var char_len =
	
		$($options).each(function(i, element) {
	
			if (element.text.length * charLen > minLi) {
				maxLi = element.text.length;
			}
		});
		// jquery container object
		var $container = setupContainer(opt);
		// jquery input object
		var $input = setupInput(opt);
		// hide select and append newly created elements
		$select.hide().before($input).before($container);
	
		init();
	
		$input.click(function(event) {
			$container.toggle();
		}).keydown(function(event) {
			switch (event.which) {
			case 38: // up
					event.preventDefault();
					moveSelect(-1);
					break;
				case40: // down
				event.preventDefault();
				moveSelect(1);
				break;
			// case 9: // tab
			case13: // enter
			event.preventDefault(); // seems not working in mac !
			$('li.' + opt.hoverClass).trigger('click');
			break;
		case27: // escape
		hideMe();
		break;
	}
	}	).blur(function() {
			if ($container.is(':visible') && hasfocus > 0) {
				if (opt.debug)
					console.log('container visible and has focus')
			} else {
				try {
					 
				if ($.browser.msie || $.browser.safari) {  									 
					if (document.activeElement.getAttribute('id').indexOf('_container') == -1) {
						hideMe();
					} else {
						$input.focus();
					}
				} else {
//					hideMe();
				}
			} catch (Error) {
				hideMe();
			}
		}
	}	);
		
		// 隐藏下拉菜单容器
		function hideMe() {
			hasfocus = 0;
			$container.hide();
		}
	
		// 初始化下拉菜单
		function init() {
			$container.append(getSelectOptions($input.attr('id'))).hide();
		
		}
		
		// 初始化下拉菜单容器并进行相关设置
		function setupContainer(options) {
			var container = document.createElement("div");
			$container = $(container);
			$container.attr('id', elm_id + '_container');
			$container.addClass(options.containerClass);
			var li_len;
			if (maxLi == 0) {
				li_len = minLi;
			} else {
				li_len = maxLi * 13;
			}
	
			$container.css("width", li_len + "px");
			//fixed bug 3385 
			$container.mouseleave(function(){
				setTimeout(function(){hideMe();},300);
			});
			return $container;
		}
		

	
		// 初始化下拉菜单并进行相关设置
		function setupInput(options) {
			var input = document.createElement("input");
			var $input = $(input);
			$input.attr("id", elm_id + "_input");
			$input.attr("type", "text");
			 
			$input.addClass(options.inputClass);
			$input.attr("autocomplete", "off");
			$input.attr("readonly", "readonly");
			$input.attr("onfocus", "this.blur()");//去掉光标
			$input.attr("tabIndex", $select.attr("tabindex")); 															
			$input.css("width", $select.attr("width"));
			return $input;
			
		}
		
 
		// 处理下拉菜单响应键盘上的上、下键
		function moveSelect(step) {
			var lis = $("li", $container);
			if (!lis || lis.length == 0)
				returnfalse;
			active += step;
			// loop through list
			if (active < 0) {
				active = lis.size() - 1;
			} else if (active > lis.size() - 1) {
				active = 0;
			}
			scroll(lis, active);
			lis.removeClass(opt.hoverClass);
	
			$(lis[active]).addClass(opt.hoverClass);
			$input.val($(lis[active]).html());
		}
	
		function scroll(list, active) {
			var el = $(list[active]).get(0);
			var list = $container.get(0);
			if (el.offsetTop + el.offsetHeight > list.scrollTop + list.clientHeight) {
				list.scrollTop = el.offsetTop + el.offsetHeight - list.clientHeight;
			} else if (el.offsetTop < list.scrollTop) {
				list.scrollTop = el.offsetTop;
			}
		}
	
		// 处理当前选择中的值
		function setCurrent(selectId) {			 
			var li = $("li." + opt.currentClass, $container).get(0);
			var ar = ('' + li.id).split('_');
			var el = ar[ar.length - 1];
			$select.val(el);
			$input.val($(li).html());
//			$("#zone_Id").val(selectId);
			return true;
		}
	
		// 获取当前选中项的索引
		function getCurrentSelected() {
			return $select.val();
		}
	
		// 获取当前选中项的值
		function getCurrentValue() {
			return $input.val();
		}
	
		// 获取下拉菜单的选择项
		function getSelectOptions(parentid) {
			var select_options = new Array();
			var ul = document.createElement('ul');
			$select.children('option').each(
					function() {
						var li = document.createElement('li');
						var selectId=$(this).val();	
						
						li.setAttribute('id', parentid + '_' + $(this).val());
						li.setAttribute('width', $select.attr("width"));	
					
						if (opt.handler!=null){
							li.setAttribute('onclick',opt.handler+'('+selectId+');');
						}
						
						li.innerHTML = $(this).html();
						if ($(this).is(':selected')) {
							$input.val($(this).html());
							$(li).addClass(opt.currentClass);
						}
						ul.appendChild(li);
						$(li).mouseover(
								function(event) {
									hasfocus = 1;
									if (opt.debug)
										console.log('over on : ' + this.id);
									jQuery(event.target, $container).addClass(
											opt.hoverClass);
								}).mouseout(
								function(event) {
									hasfocus = -1;
									if (opt.debug)
										console.log('out on : ' + this.id);
									jQuery(event.target, $container).removeClass(
											opt.hoverClass);
								}).click(
								function(event) {
									var fl = $('li.' + opt.hoverClass, $container)
											.get(0);
									if (opt.debug)
										console.log('click on :' + this.id);
									$('#' + elm_id + '_container' + ' li.' + opt.currentClass).removeClass(opt.currentClass);
									$(this).addClass(opt.currentClass);
									setCurrent(selectId);
									 
									$select.get(0).blur();
									hideMe();
								});
					});
			return ul;
		}
	};