function vdiskConfig(info) {
	var storSize = info.storageSize;
	if(info.storeType == null){
		return '大小：' + storSize + 'G    ' + '    存储类型：默认存储' ;//to fix bug [6873]
	} else {
		return '大小：' + storSize + 'G    ' + '    存储类型：' + info.storeType;
	}	
}
function vdiskRefRes(info) {
	var Vvmdisplayname = '';
	if (info.state == "2") {
		if (info.volumestate == "2" || info.volumestate == "7"
				|| info.volumestate == "5") {
			var Displayname = info.vmName == undefined ? "" : info.vmName;
			var titleDisplayname = '';

			// if(Displayname.length>12){
			// titleDisplayname = Displayname;
			// Displayname = Displayname.substring(0,12) + '...';
			// }
			Vvmdisplayname = Displayname;
		} else if (info.volumestate == "4" || info.volumestate == ""
				|| info.volumestate == "6" || !info.volumestate
				|| info.volumestate == "1") {
			Vvmdisplayname = '';
			/**
			 * Vvmdisplayname = '<select class=\"select1\" id=\"vdisk_sel_' +
			 * info.eInstanceId + '\">';
			 * main.server.getServer("findPortalMyVMByZone", { data: {volumeid:
			 * info.id, pagesize: 100,page: 1,async : false}, async:false,
			 * timeout:30000, success: function(vm){
			 * if(vm.instanceList.length==0){ Vvmdisplayname += ''; }else{ for
			 * (var a = 0; a < vm.instanceList.length; a++) { var v =
			 * vm.instanceList[a]; Vvmdisplayname += '<option
			 * selected=\"selected\" id=\"'+v.eInstanceId+'\" value=\"' + v.id +
			 * '\">' + v.instanceName + '</option>'; } } } }); Vvmdisplayname += '</select>';
			 */
		}
	}
	if (info.state == "6") {
		var Displayname = info.vmName == undefined ? "" : info.vmName;
		var titleDisplayname = '';
		// to fix bug [3619]
		// if(Displayname.length>12){
		// titleDisplayname = Displayname;
		// Displayname = Displayname.substring(0,12) + '...';
		// }
		Vvmdisplayname = Displayname;
		titleVvmdisplayname = titleDisplayname;
	}
	return Vvmdisplayname;
}
function vdiskState(info) {
	var showState = stateName[info.state];
	if (info.state == "2") {
		if (info.volumestate == "2" && info.state == "2") {
			showState = "已挂载";// fix bug 3727 挂载成功显示“已挂载”
		} else if ((info.volumestate == undefined || info.volumestate == ""
				|| info.volumestate == "1" || info.volumestate == "4"
				|| info.volumestate == "5" || info.volumestate == "6" || info.volumestate == "7")
				&& info.state == "2") {
			showState = "就绪";
		}
	}
	if (info.state == "6") {
		showState = '操作执行中';
	}
	return showState;
}
function vdiskOperate(info) {
	var CSnapshot = '';
	var USnapshot = '';
	var DSnapshot = '';
	var serviceState = $('#serviceState').val();
	var vGuaZai = '';
	if (info.state == "2") {
		if (info.volumestate == "2" || info.volumestate == "7"
				|| info.volumestate == "5") {
			$('#caozuo_' + info.id).attr('id', 'caozuo_' + info.eInstanceId);
			vGuaZai = ''
					+ '    <a href="#" onclick="vdiskmanage.detachVolume(\''
					+ info.eInstanceId + '\',\'' + info.id + '\', \''
					+ info.vmInstanceId + '\');" title=\"解挂\" >'
					+ '     <img src="../images/unPlugdisk.png" /></a>';
			vGuaZai = serviceState != 6
					? vGuaZai
					: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/unPlugdisk.png" />';
			DSnapshot = serviceState != 6
					? '<a href="javascript:vdiskmanage.loadPopDiv_vdisk_snapshot(2, \''
							+ info.id
							+ '\', \''
							+ info.eInstanceId
							+ '\',\''
							+ info.resourcePoolsId
							+ '\');" title=\"删除快照\" ><img src=\"../images/DSnapshot.png\" /></a>'
					: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/DSnapshot.png\" />';		
			vGuaZai = vGuaZai + DSnapshot;	 
		} else if (info.volumestate == "4" || info.volumestate == ""
				|| info.volumestate == "6" || !info.volumestate
				|| info.volumestate == "1" || info.volumestate == null) {
			resource.server.getServer("showStorageVmByZone", {
				data : {
					volumeid : info.id,
					pagesize : 100,
					page : 1,
					async : false,
					resourcePoolsId : info.resourcePoolsId
				},
				async : false,
				timeout : 30000,
				success : function(vm) {
					// to fix bug [3702]
					if (vm.instanceList.length == 0) {
						$('#caozuo_' + info.id).attr('id',
								'caozuo_' + info.eInstanceId);
						vGuaZai = '<a href="#" onclick="vdiskmanage.no_vdisk_bind()">';
						vGuaZai += '<img src="../images/plugdisk.png" title=\"挂载\"/></a>';
					} else {// bug 0003346 3519 3505
						vGuaZai = '<a href="#" onclick="vdiskmanage.loadPopDiv_vdisk_bind('
								+ info.id + ',' + info.eInstanceId + ','+ info.resourcePoolsId +','+ info.zoneId +')">';
						vGuaZai += '<img src="../images/plugdisk.png" title=\"挂载\" /></a>';// to
																							// fix
																							// bug
																							// [4002]
						vGuaZai = serviceState != 6
								? vGuaZai
								: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src="../images/loading1.gif" />';
					}
				}
			});
			CSnapshot = serviceState != 6
					? '<a href=\"#\" title=\"快照\" onclick=\"vdiskmanage.dataVolumeCreateSnapshot(\''
							+ info.id
							+ '\', \''
							+ info.eInstanceId
							+ '\', \''
							+ info.resourcePoolsId
							+ '\')" ><img src=\"../images/CSnapshot.png\" /></a>'
					: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/CSnapshot.png\" />';
			USnapshot = serviceState != 6
					? '<a href="javascript:vdiskmanage.loadPopDiv_vdisk_snapshot(1, \''
							+ info.id
							+ '\', \''
							+ info.eInstanceId
							+ '\',\''
							+ info.resourcePoolsId
							+ '\');" title=\"恢复\" ><img src=\"../images/USnapshot.png\" /></a>'
					: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/USnapshot.png\" />';
			DSnapshot = serviceState != 6
					? '<a href="javascript:vdiskmanage.loadPopDiv_vdisk_snapshot(2, \''
							+ info.id
							+ '\', \''
							+ info.eInstanceId
							+ '\',\''
							+ info.resourcePoolsId
							+ '\');" title=\"删除快照\" ><img src=\"../images/DSnapshot.png\" /></a>'
					: '<img style=\"filter:alpha(opacity=30);; -moz-opacity:.1;opacity:0.3;\" src=\"../images/DSnapshot.png\" />';
			vGuaZai = vGuaZai + CSnapshot + USnapshot + DSnapshot;
		}
	} else {
		if (info.state == 4) {
			vGuaZai = '';
		} else if (info.state == 2) {
			$('#caozuo_' + info.id).attr('id', 'caozuo_' + info.eInstanceId);
			vGuaZai = '      <p><img src="../images/plugdisk.png" /></p>';
		} else if (info.state == 6) {
			vGuaZai = '      <p><img src="../images/loading1.gif" /></p>';
		}
	}
	return vGuaZai;
}

// ///////////////////////////////////////////具体操作////////////////////////////////////////

var vdiskmanage = {// bug 0003346

	no_vdisk_bind : function() {
		//fix bug 7454 修改提示文字
		alert('现在没有开通的虚拟机或者虚拟机正在操作中，请申请虚拟机或等待虚拟机操作完成后再执行此挂载操作。');
	},

	loadPopDiv_vdisk_bind : function(volumeinstanceId, volumeid,resourcePoolsId,zoneId) {
		$("#add_confirm").empty();
		$("#add_confirm").load("../component/vdiskmanage/vdiskoperate.html",
				"", function() {
					$("#close").click(function() {
								$("#add_confirm,.shade").fadeOut("fast");
							});
					vdiskmanage.selectVM(volumeinstanceId, volumeid,resourcePoolsId,zoneId);
				});
	},

	selectVM : function(volumeinstanceId, volumeid,resourcePoolsId,zoneId) { // bug 0003346
		$('#popTitle').html('虚拟机挂载');
		$('#czz').html('选择虚拟机:');
		$("#add_confirm").show();
		$('#eInstanceId').val(volumeid);
		$('#volumeid').val(volumeinstanceId);
		var vName = '';
		resource.server.getServer("showStorageVmByZone", {
					data : {
						volumeid : volumeinstanceId,
						pagesize : 100,
						page : 1,
						async : false,
						resourcePoolsId :resourcePoolsId,
						zoneId : zoneId
					},
					async : false,
					timeout : 30000,
					success : function(vm) {
						if (vm.instanceList.length == 0) {
							vName += '';
						} else {
							vName += '';
							for (var a = 0; a < vm.instanceList.length; a++) {
								var v = vm.instanceList[a];
								if (v.e_instance_id != 0) {
									vName += '<option id="' + v.e_instance_id
											+ '" id1="' + v.id + '">'
											+ v.instance_name + '</option>';
								}
							}
						}
						$("#params").empty();
						$("#params").html(vName);
					}
				});

		$("#vmbind_btn").attr("onclick", "vdiskmanage.attachVolume();");

	},

	attachVolume : function() { // bug 0003346

		var volumeinstanceId = $('#volumeid').val();
		var volumeEid = $('#eInstanceId').val();
		var vmEid = $("#params option:selected").attr("id");
		var vmid = $("#params  option:selected").attr("id1");
		var vmName = $("#params option:selected").text();
		$.ajax({
					url : resource.actionRoot
							+ "/resourcesUsed/attachvolume.action",
					type : 'POST',
					data : {
						volumeid : volumeEid,
						vmid : vmEid,
						vminstanceId : vmid,
						volumeinstanceId : volumeinstanceId
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						if (data.search("error") != -1) {
							alert(data);
						} else {
							//$("#add_confirm").hide();
							//alert('挂载操作即将执行！');// to fix bug [2837]
							//$("#state_" + volumeinstanceId).html("操作执行中");
							//$("#ref_" + volumeinstanceId).html(vmName);
							//$("#caozuo_" + volumeinstanceId).empty();
							//$("#caozuo_" + volumeinstanceId).append("<img src=\"../images/loading1.gif\">");
							//$("#add_confirm").hide();
							
		                    $("#state_"+volumeinstanceId).html("操作执行中");
							$("#ref_" + volumeinstanceId).html(vmName);
							$("#ref_" + volumeinstanceId).attr('title',vmName);//bug4620修改title显示内容，挂载之前是空值
		                    $("#caozuo_" + volumeinstanceId).empty();
							$("#caozuo_" + volumeinstanceId).append("<img src=\"../images/loading1.gif\">");
		                    $("#add_confirm").hide();
							alert('发送挂载命令成功！');
							query();//bug 0004620刷新列表，重新查询加载
						}
					}
				});// bug 0003230
		//query();

	},

	detachVolume : function(volumeid, volumeinstanceId, vminstanceId) {// bug
																		// 0003346
		if (confirm('如果该虚拟硬盘已经在使用中，请先登录相应的虚拟机卸载该硬盘。\n\n您确认要解挂吗？')) {
			$.ajax({
				url : resource.actionRoot
						+ "/resourcesUsed/detachvolume.action",
				type : 'POST',
				data : {
					volumeid : volumeid,
					vminstanceId : vminstanceId,
					volumeinstanceId : volumeinstanceId
				},
				dataType : 'json',
				async : false,
				success : function(data) {
					if (data.search("error") != -1) {
						alert(data);
					} else {
						alert(data);
						$("#state_" + volumeinstanceId).html("操作执行中");
						$("#ref_" + volumeinstanceId).html('');
						$("#caozuo_" + volumeinstanceId).empty();
						$("#caozuo_" + volumeinstanceId).append("<img src=\"../images/loading1.gif\">");
						
						query();//bug 0004620刷新列表，重新查询加载
					}
				}
			});// bug 0003230
			//query();
		}
	},
	dataVolumeCreateSnapshot : function(volumeinstanceId, volumeid,resourcePoolsId) {
		
		var flag = 0;
			$.ajax({
					url : resource.actionRoot
							+ "/resourcesUsed/getElasterVolumeById.action",
					type : 'POST',
					data : {
						volumeid : volumeid,
						resourcePoolsId :resourcePoolsId
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						var elasterVolumeInfo  = $.evalJSON(data);
						if(elasterVolumeInfo.listvolumesresponse!=null){
						var state = elasterVolumeInfo.listvolumesresponse.volume[0].state;
						if(state=="Allocated"){
								flag = 1;
								alert("该存储还没有被挂载过vm不能创建快照！");
							}
						}
					}
				});
		if(flag == 1){
			return;
		}
		if (confirm('是否确认创建快照？')) {
			$.ajax({
					url : resource.actionRoot
							+ "/resourcesUsed/createsnapshot.action",
					type : 'POST',
					data : {
						volumeid : volumeid,
						volumeinstanceId : volumeinstanceId
					},
					async : false,
					dataType : 'json',
					success : function(data) {
						if (data.search("error") != -1) {
							alert(data);
						} else {
							alert(data);
							$("#state_" + volumeinstanceId).html("操作执行中");
							$("#ref_" + volumeinstanceId).html('');
							$("#caozuo_" + volumeinstanceId).empty();
							$("#caozuo_" + volumeinstanceId).append("<img src=\"../images/loading1.gif\">");

							query();//bug 0004620刷新列表，重新查询加载
						}
					}
				});// bug 0003230
		}
		
	},
	dataVolumeDeleteSnapshot : function(volumeinstanceId, snapshotId) {
		var queryJsonObj = {};
		queryJsonObj.volumeinstanceId = volumeinstanceId;
		queryJsonObj.snapshotId = snapshotId;
		$.ajax({
					url : resource.actionRoot
							+ "/resourcesUsed/deletesnapshot.action",
					type : 'POST',
					data : {
						queryJson : $.toJSON(queryJsonObj)
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						if (data.search("error") != -1) {
							alert(data);
						} else {
							alert(data);
							$("#state_" + volumeinstanceId).html("操作执行中");
							$("#ref_" + volumeinstanceId).html('');
							$("#caozuo_" + volumeinstanceId).empty();
							$("#caozuo_" + volumeinstanceId)
									.append("<img src=\"../images/loading1.gif\">");
							vdiskmanage.cancel();
						}
					}
				});// bug 0003230
		query();
	},
	dataVolumeResumeSnapshot : function(volumeinstanceId, snapshotId,resourcePoolsId) {
		
		var queryJsonObj = {};
		queryJsonObj.volumeinstanceId = volumeinstanceId;
		queryJsonObj.snapshotId = snapshotId;
		queryJsonObj.resourcePoolsId = resourcePoolsId;
		$.ajax({
					url : resource.actionRoot
							+ "/resourcesUsed/resumesnapshot.action",
					type : 'POST',
					data : {
						queryJson : $.toJSON(queryJsonObj)
					},
					dataType : 'json',
					async : false,
					success : function(data) {
						if (data.search("error") != -1) {
							alert(data);
						} else {
							alert(data);
							$("#state_" + volumeinstanceId).html("操作执行中");
							$("#ref_" + volumeinstanceId).html('');
							$("#caozuo_" + volumeinstanceId).empty();
							$("#caozuo_" + volumeinstanceId)
									.append("<img src=\"../images/loading1.gif\">");
							vdiskmanage.cancel();
						}
					}
				});// bug 0003230
		query();
	},
	
	loadPopDiv_vdisk_snapshot : function(type, volumeinstanceId, volumeid,
			resourcePoolsId) {
		$("#add_confirm").empty();
		$("#add_confirm").load("../component/vdiskmanage/vdiskoperate.html",
				"", function() {
					$("#close").click(function() {
								$("#add_confirm,.shade").fadeOut("fast");
							});
					vdiskmanage.showDataVolumeSnapshot(type, volumeinstanceId,
							volumeid, resourcePoolsId);
				});
	},
	showDataVolumeSnapshot : function(type, volumeinstanceId, volumeid,
			resourcePoolsId) {
		$("#volumeid").val(volumeinstanceId);
		$("#eInstanceId").val(volumeid);
		$("#resourcePoolsId").val(resourcePoolsId);
		$("#operType").val(type);
		if(type == 1){
		$('#popTitle').html('数据盘快照恢复');
			$('#czz').html('快照：');
		}else if (type == 2) {
			$('#popTitle').html('数据盘快照删除');
			$('#czz').html('快照：');
		}
			var params = {
				volumeinstanceId : volumeinstanceId,
				resourcePoolsId : resourcePoolsId
			};

			$.ajax({
				url : resource.actionRoot
						+ "/resourcesUsed/listSnapshotsByVolumeId.action",
				type : 'POST',
				data : params,
				dataType : 'json',
				success : function(data) {
					if (data != "[null]") {
						var snapshotList = $.evalJSON(data);
						if (snapshotList.length > 0) {
							$("#params").html("");
							var str_snap = "<option id=\"\">=请选择=</option>";
							$("#params").append(str_snap);
							for (var i = snapshotList.length - 1; i >= 0; i--) {
								str_snap = "<option id=\""
										+ snapshotList[i].snapshotId + "\" title=\""
										+ snapshotList[i].name + "\">"
										+ snapshotList[i].name + "</option>";
								$("#params").append(str_snap);
							}
							$("#tr_storage").show();
							$('#params').show();
							$('#add_confirm').show();

							var snapshotId = $("#params option:selected")
									.attr("id");
							if(type == 1){
								$('#vmbind_btn').attr('onclick',
									'vdiskmanage.resumeSnapshot()');
							}else if(type == 2){
								$('#vmbind_btn').attr('onclick',
									'vdiskmanage.delSnapshot()');
							}		
							$('#close').attr('onclick', 'vdiskmanage.cancel()');
						} else {
							alert('此数据盘没有快照！');
						}
					}
				}
			});
		
	},
	cancel : function() {
		$("#storageSize").empty();
		$("#operType").val("");
		$("#add_confirm").hide();
	},
	delSnapshot : function() {
		if ($("#params option:selected").attr("id") == "") {
			alert("请选择需要删除的快照！");
			return;
		}
		vdiskmanage.dataVolumeDeleteSnapshot($("#volumeid").val(),
				$("#params option:selected").attr("id"));
		$("#params").html("");
	},
	resumeSnapshot : function() {
		if ($("#params option:selected").attr("id") == "") {
			alert("请选择需要恢复的快照！");
			return;
		}
		vdiskmanage.dataVolumeResumeSnapshot($("#volumeid").val(),
				$("#params option:selected").attr("id"),$("#resourcePoolsId").val());
		$("#params").html("");
	}

};