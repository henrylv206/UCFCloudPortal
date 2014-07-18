package com.skycloud.management.portal.front.report.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.ipsan.schemas.adb.client.EBSOPStub.QueryBSReq;
import com.skycloud.ipsan.schemas.adb.client.EBSOPStub.QueryBSResp;
import com.skycloud.ipsan.schemas.adb.client.EBSOPStub.QueryVMInfoResp;
import com.skycloud.ipsan.service.impl.IpsanResourceServiceImpl;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.instance.service.impl.InstanceServiceImpl;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.task.vdc.util.TaskUtils;

public class ResourceUtils {

	private static Log log = LogFactory.getLog(ResourceUtils.class);

	public static String resConfig(TInstanceInfoBO info) {
		int type = info.getTemplateType();
		if (type == 1) {// 虚拟机
			return vmConfig(info);
		} else if (type == 2) {// 虚拟硬盘
			return vdiskConfig(info);
		} else if (type == 3) {// 小型机
			return pmConfig(info);
		} else if (type == 4) {// 虚拟机备份
			return backupConfig(info);
		} else if (type == 5) {// 云监控
			return monitorConfig(info);
		} else if (type == 6) {// 负载均衡
			int storageSize = info.getStorageSize();
			return storageSize + "个并发";
		} else if (type == 7) {// 防火墙
			return firewallConfig(info);
		} else if (type == 8) {// 公网带宽
			return bwConfig(info);
		} else if (type == 9) {// 弹性公网IP
			return tipConfig(info);
		} else if (type == 10) {// X86物理机
			return pmConfig(info);
		} else if (type == 11) {// 对象存储
			return osConfig(info);
		} else if (type == 12) {// 弹性块存储
			int storageSize = info.getStorageSize();
			return storageSize + "G"; // to fix bug:3807
		} else {
			return "";
		}
	}

	// 资源状态显示
	// 替换方法用xxxState(info)格式
	public static String resState(TInstanceInfoBO info) {
		int type = info.getTemplateType();
		String showState = "";

		switch (type) {
			case 1:// 虚拟机
				showState = vmState(info);
				break;
			case 2:// 虚拟硬盘
				showState = vdiskState(info);
				break;
			case 3:// 小型机
				showState = mcState(info);
				break;
			case 4:// 虚拟机备份
				showState = backupState(info);
				break;
			case 5:// 云监控
				showState = monitorState(info);
				break;
			case 6:// 负载均衡
				showState = loadBalancedState(info);
				break;
			case 7:// 防火墙
				showState = firewallState(info);
				break;
			case 8:// 公网带宽
				showState = bwState(info);
				break;
			case 9:// 弹性公网IP
				showState = tipState(info);
				break;
			case 10:// X86物理机
				showState = pmState(info);
				break;
			case 11:// 对象存储
				showState = osState(info);
				break;
			case 12:// 弹性块存储
				showState = ipsanState(info);
				break;
			default:
				showState = vmState(info);
		}// bug 0005410 0006996
		if (info.getServiceHistoryState() == 1) {
			if (info.getOrderType() != 3 && info.getState() != 4 && info.getOrderState() == 4) {
				showState = "历史";
			}
			if (info.getOrderType() == 0) {
				showState = "历史";
			}
		}
		return showState;
	}

	// 依赖资源显示
	public static String refRes(TInstanceInfoBO info) {
		int type = info.getTemplateType();
		String rs = "";
		switch (type) {
			case 3:
			case 1:
				rs = vmRefRes(info);
				break;
			case 2:
				rs = vdiskRefRes(info);
				break;
			case 6:
				rs = loadbalanceRefRes(info);
				break;
			case 8:
				rs = bwRefRes(info);
				break;
			case 9:
				rs = tipRefRes(info);
				break;
			case 12:
				rs = ipsanRefRes(info);
				break;
			default:
				rs = "";
		}

		return rs;
	}

	public static final String[] serviceProvider = { "中国电信", "中国联通", "中国移动", "中国铁通" };

	public static final String[] stateName = { "", "申请", "就绪", "申请处理中", "已删除", "已关机", "操作执行中", "创建失败" };

	// /////////////////////////////////////////虚拟机///////////////////////////////////////////
	public static String vmConfig(TInstanceInfoBO info) {
		String rs = "";
		int cpu = info.getCpuNum();
		int memSize = info.getMemorySize();
		String osDesc = info.getOsDesc();
		String storeType = !StringUtils.isEmpty(info.getStoreType()) ? "," + info.getStoreType() : "";
		osDesc = !StringUtils.isEmpty(osDesc) ? osDesc : "";
		if (cpu != 0 || memSize != 0) {
			if (cpu != 0) {
				rs += "cpu：" + cpu + "个";
			}
			if (memSize != 0) {
				rs += "，内存：" + memSize + "Mb";
			}
		}
		if (!StringUtils.isEmpty(osDesc) || !StringUtils.isEmpty(storeType)) {
			if (!StringUtils.isEmpty(osDesc)) {
				rs += "，系统：" + osDesc;
			}
			if (!StringUtils.isEmpty(storeType)) {
				rs += "，存储：" + storeType;
			}
		}
		return rs;
	}

	public static String vmState(TInstanceInfoBO info) {
		int state = info.getState();
		String showState = "";
		switch (state) {
			case 1:
				showState = "申请";
				break;
			case 2:
				showState = "就绪";
				break;
			case 3:
				showState = "申请处理中";
				break;// 正在开通，正在销毁，正在变更
			case 4:
				showState = "已删除";
				break;
			case 5:
				showState = "已关机";
				break;
			case 6:
				showState = "操作执行中";
				break;// bug 0003215 0003606 //开机，关机，重启，创建快照等
			case 7:
				showState = "创建失败";
				break;
			default:
				showState = "就绪";
				break;
		}
		return showState;
	}

	public static String vmRefRes(TInstanceInfoBO info) {
		String hostIp = info.getHostIpAddress();
		if (!StringUtils.isEmpty(hostIp)) {
			return "主机IP：" + hostIp;
		}
		return "";
	}

	// /////////////////////////////////////////虚拟磁盘///////////////////////////////////////////
	public static String vdiskConfig(TInstanceInfoBO info) {
		int storageSize = info.getStorageSize();
		return "大小：" + storageSize + "G";
	}

	public static String vdiskRefRes(TInstanceInfoBO info) {
		String Vvmdisplayname = "";
		if (info != null && !StringUtils.isEmpty(info.getVolumestate()) && info.getState() == 2) {// bug
			// 0004264
			if ("2,5,7".contains(info.getVolumestate())) {
				String Displayname = StringUtils.isEmpty(info.getVmName()) ? "" : "虚拟机: " + info.getVmName() + "(" + info.getHostIpAddress() + ")";
				Vvmdisplayname = Displayname;
			} else if ("1,4,6".contains(info.getVolumestate()) || StringUtils.isEmpty(info.getVolumestate())) {
				Vvmdisplayname = "";
			}
		}
		if (info != null && info.getState() == 6) {
			String Displayname = StringUtils.isEmpty(info.getVmName()) ? "" : "虚拟机: " + info.getVmName() + "(" + info.getHostIpAddress() + ")";
			Vvmdisplayname = Displayname;
		}
		return Vvmdisplayname;
	}

	public static String vdiskState(TInstanceInfoBO info) {
		String showState = stateName[info.getState()];
		if (info != null && !StringUtils.isEmpty(info.getVolumestate()) && info.getState() == 2) {// bug
			// 0004264
			if ("2".equals(info.getVolumestate()) && info.getState() == 2) {
				showState = "已挂载";// fix bug 3727 挂载成功显示“已挂载”
			} else if (("1,4,5,6,7".contains(info.getVolumestate()) || StringUtils.isEmpty(info.getVolumestate())) && info.getState() == 2) {
				showState = "就绪";
			}
		}
		if (info != null && info.getState() == 6) {
			showState = "操作执行中";
		}
		return showState;
	}

	// ////////////////////公网 IP/////////////////////
	public static String tipConfig(TInstanceInfoBO info) {
		String rs = "";
		String ipAddress = info.getHostIpAddress();// bug 0004275
		// bug 0004541
		String sp = serviceProvider[Integer.valueOf(info.getServiceProvider())];
		if (StringUtils.isNotEmpty(sp)) {
			rs += "运营商: " + sp;
		}
		if (StringUtils.isNotEmpty(ipAddress)) {// bug 0004736
			rs += "，";
			rs += "ip：" + ipAddress;
		}
		return rs;
	}

	public static String tipRefRes(TInstanceInfoBO info) {
		String name = "";
		long vmEid = 0;
		String vmName = info.getVmName(); // 此时代表可绑定的虚拟机名称(含ip)
		if (info.geteInstanceId() != 0) {
			vmEid = info.geteInstanceId(); // 此时代表绑定的虚拟机
			name = vmName;
		}
		if (vmEid > 0) {
			name = vmName;
		} else if (vmEid == -1) {
			name = "负载均衡: " + info.getVmName();
		}
		return name;
	}

	public static String tipState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}

	// /////////////////////物理机////////////////////
	/**
	 * 物理机配置详情显示
	 * 
	 * @param info
	 * @returns {String}
	 */
	public static String pmConfig(TInstanceInfoBO info) {
		String rs = "";
		int cpu = info.getCpuNum();
		int memSize = info.getMemorySize();
		String osDesc = info.getOsDesc();
		String storeType = info.getStoreType();
		if (cpu != 0) {
			rs += "cpu：" + cpu + "个";
		}
		if (memSize != 0) {
			rs += "，内存：" + memSize + "G";
		}
		if (!StringUtils.isEmpty(osDesc)) {
			rs += "，系统：" + osDesc;
		}
		if (!StringUtils.isEmpty(storeType)) {
			rs += "，存储：" + storeType;
		}
		return rs;
	}

	/**
	 * 物理机当前状态显示： Running-运行中,Starting-正在启动,Stopping-正在关机,
	 * Stopped-已关机,Released-已释放
	 * 
	 * @param info
	 * @returns {String}
	 */
	public static String pmState(TInstanceInfoBO info) {
		String stateName = "";
		switch (info.getState()) {
			case 1:
				stateName = "申请";
				break;
			case 2:
				stateName = "运行中";
				break;
			case 3:
				stateName = "申请处理中";
				break;
			case 4:
				stateName = "已删除";
				break;
			case 5:
				stateName = "已关机";
				break;
			case 6:
				stateName = "操作执行中";
				break;
			case 7:
				stateName = "创建失败";
				break;
			default:
				stateName = "就绪";
		}
		return stateName;
	}

	// ////////////////////对象存储/////////////////////
	public static String osConfig(TInstanceInfoBO info) {
		String storSize = "容量：" + info.getStorageSize();
		return storSize + "G";
	}

	public static String osState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}

	// /////////////////////云监控////////////////////
	public static String monitorConfig(TInstanceInfoBO info) {
		String monitorInfo = getMonitorInfo(info.getNetworkDesc());
		monitorInfo = !StringUtils.isEmpty(monitorInfo) ? monitorInfo : "";
		return monitorInfo;
	}

	public static String getMonitorInfo(String code) {
		String info = code;
		if (!StringUtils.isEmpty(code)) {
			info = info.replace("vm", "虚拟机");
			info = info.replace("mc", "小型机");
			info = info.replace("vl", "弹性块存储");
			info = info.replace("lb", "负载均衡");
			info = info.replace("fw", "防火墙");
			info = info.replace("pnip", "弹性公网IP");
			info = info.replace("bw", "公网带宽");
		}
		return info;
	}

	public static String monitorState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}

	// ///////////////////小型机//////////////////////
	public static String mcState(TInstanceInfoBO info) {
		String statename = stateName[info.getState()];
		if (info.getState() == 2) {
			statename = "运行中";
		}
		return statename;
	}

	// ///////////////////负载均衡//////////////////////
	public static String loadBalancedState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}

	public static String loadbalanceRefRes(TInstanceInfoBO info) {
		String rs = info.getHostIpAddress();
		return rs;
	}

	// ///////////////////弹性块存储//////////////////////
	public static String ipsanRefRes(TInstanceInfoBO info) {
		StringBuffer machineNames = new StringBuffer();
		String Vvmdisplayname = "";
		if (info.getState() == 2) {
			String bsstate = "0";
			String vmid = "0";
			String iqn = "";
			String tip = "";
			List<Map<String, Object>> ipsanInfoList = getIpsanInfoByRescode(info.getResCode());
			int stateAttach = 0;
			for (Map<String, Object> ipsanInfo : ipsanInfoList) {
				// Map<String, Object> json =
				// getIpsanInfoByRescode(info.getResCode());
				if (ipsanInfo != null) {
					bsstate = ipsanInfo.get("BSState").toString();
					vmid = ipsanInfo.get("VMID") != null ? ipsanInfo.get("VMID").toString() : "";
				}
				if (bsstate == "1") {
					String Displayname = "";// bug 0004709
					List<ResourcesVO> vmList = showUserMachine(info);
					for (int a = 0; a < vmList.size(); a++) {
						ResourcesVO v = vmList.get(a);
						if (vmid.equals(v.getId())) { // if
							// (vmid.equals(v.getE_instance_id()))
							// {
							String templateType = "";
							String typeId = v.getTemplate_type();
							if (typeId != null && "1".equals(typeId)) {
								templateType = "虚拟机";
							} else if (typeId != null && "3".equals(typeId)) {
								templateType = "物理机";
							} else if (typeId != null && "10".equals(typeId)) {
								templateType = "小型机";
							}
							Displayname = templateType + ": " + v.getInstance_name() + "(" + v.getNicsBOs().get(0).getIp() + ") ";
							break;
						}
					}
					Vvmdisplayname = Displayname;
					stateAttach = 1;
				} else if (bsstate == "2") {
					// Vvmdisplayname = "未绑定虚机";
				} else {
					Vvmdisplayname += "";
				}
				// 追加显示机器名称
				machineNames.append(Vvmdisplayname);
			}// end for ipsanInfoList

			if (stateAttach == 0) {
				Vvmdisplayname = "未绑定虚机";
			} else {
				Vvmdisplayname = machineNames.toString();
			}

		} else {
			Vvmdisplayname = "";
		}
		if (info.getState() == 6) {
			String Displayname = StringUtils.isEmpty(info.getVmName()) ? "" : info.getVmName();
			Vvmdisplayname = Displayname;
		}
		return Vvmdisplayname;
	}

	public static List<ResourcesVO> showUserMachine(TInstanceInfoBO info) {
		List<ResourcesVO> instanceList = new ArrayList<ResourcesVO>();
		TUserBO user = new TUserBO();
		user.setId(info.getUserId());
		ResourcesQueryVO vo = new ResourcesQueryVO();
		vo.setUser(user);
		vo.setOperateSqlType(1);
		IInstanceService instanceService = new InstanceServiceImpl();
		try {
			vo.setOperateSqlType(1);// 虚机
			List<ResourcesVO> instanceListVM = instanceService.queryInstanceInfoByUser(vo, 0, 0);
			vo.setOperateSqlType(3);// 小型机
			List<ResourcesVO> instanceListMc = instanceService.queryInstanceInfoByUser(vo, 0, 0);
			vo.setOperateSqlType(10);// 物理机
			List<ResourcesVO> instanceListPm = instanceService.queryInstanceInfoByUser(vo, 0, 0);
			instanceList.addAll(instanceListVM);
			instanceList.addAll(instanceListMc);
			instanceList.addAll(instanceListPm);
			return instanceList;
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String ipsanState(TInstanceInfoBO info) {
		String showState = "";
		String bsstate = "0";
		int stateAttach = 0;
		if (info.getState() == 1) {
			showState = "申请";
		} else if (info.getState() == 2) {
			List<Map<String, Object>> ipsaninfoList = getIpsanInfoByRescode(info.getResCode());
			for (Map<String, Object> ipsaninfo : ipsaninfoList) {
				if (ipsaninfo != null) {
					bsstate = ipsaninfo.get("BSState").toString();
				}
				if (bsstate == "1") {
					showState = "已挂载";
					stateAttach = 1;
					break;
				} else if (bsstate == "2" && stateAttach == 0) {
					showState = "就绪";
				} else if (stateAttach == 0) {
					showState = "就绪";
				}
			}
		} else if (3 == info.getState()) {
			showState = "申请处理中 ";
		} else if (4 == info.getState()) {
			showState = "已删除 ";
		} else if (6 == info.getState()) {
			showState = "操作执行中 ";
		} else if (7 == info.getState()) {
			showState = "创建失败 ";
		}
		return showState;
	}

	public static String message;

	public static List<Map<String, Object>> getIpsanInfoByRescode(String rescode) {
		List<Map<String, Object>> ipsaninfoList = new ArrayList<Map<String, Object>>();
		Map<String, Object> ipsaninfo = null;
		QueryBSResp queryBSResp = new QueryBSResp();
		try {
			QueryBSReq queryBSReq = new QueryBSReq();

			queryBSReq.setBSID(rescode);// resourcesVO.getRes_code()

			IpsanResourceServiceImpl ipsanResourceServiceImpl = new IpsanResourceServiceImpl();
			String ipsanURL = TaskUtils.getIpSanURL();
			queryBSResp = ipsanResourceServiceImpl.queryIpsan(ipsanURL, queryBSReq);

			// int bsstate = queryBSResp.getBSState();
			// // 状态 1.挂载 2.未挂载
			// ipsaninfo = new HashMap<String, Object>();
			// ipsaninfo.put("VMID", queryBSResp.getVMID());
			// ipsaninfo.put("BSState", String.valueOf(bsstate));
			// ipsaninfo.put("InitiatorName", queryBSResp.getInitiatorName());
			// ipsaninfo.put("TargetIP", queryBSResp.getTargetIP());

			QueryVMInfoResp vminfos[] = queryBSResp.getVMinfo();
			if (vminfos != null) {// bug 0006995
				int vmlen = vminfos.length;
				// 获取返回值数组中的iqn,目标IP
				for (int i = 0; i < vmlen; i++) {
					int bsstate = vminfos[i].getBSState();
					// 状态 1.挂载 2.未挂载
					ipsaninfo = new HashMap<String, Object>();
					ipsaninfo.put("VMID", vminfos[i].getVMID());
					ipsaninfo.put("BSState", String.valueOf(bsstate));
					ipsaninfo.put("InitiatorName", vminfos[i].getInitiatorName());
					ipsaninfo.put("TargetIP", vminfos[i].getTargetIP());
					ipsaninfoList.add(ipsaninfo);
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error:" + e.getMessage();
			log.error(e.getMessage());
		}
		return ipsaninfoList;
	}

	// ////////////////////防火墙/////////////////////
	public static String firewallConfig(TInstanceInfoBO info) {
		int storageSize = info.getStorageSize();
		String dom = storageSize + "条规则";
		return dom;
	}

	public static String firewallState(TInstanceInfoBO info) {
		String value = "";
		switch (info.getState()) {
			case 1:
				value = "申请开通";
				break;
			case 2:
				value = "就绪";// to fix bug:4014(将'正常'改为'就绪')
				break;
			case 3:
			case 6:
				value = "执行中";
				break;
			case 4:
				value = "退订";
				break;
			case 7:// bug 0004411
				value = "创建失败";
				break;
			default:
				value = "";
		}
		return value;
	}

	// ////////////////////公网带宽/////////////////////
	public static String bwConfig(TInstanceInfoBO info) {
		int storageSize = info.getStorageSize();
		return storageSize + "Mbps";
	}

	public static String bwState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}

	public static String bwRefRes(TInstanceInfoBO info) {// bug 0003800
		String ip = "公网IP: " + info.getHostIpAddress();// bug 0004275
		if (StringUtils.isEmpty(ip)) {
			ip = "";
		}
		return ip;
	}

	// ////////////////////虚拟机备份/////////////////////
	public static String backupConfig(TInstanceInfoBO info) {
		String storSize = "容量：" + info.getStorageSize() + "G";
		String used = "，已用：" + info.getBkStorageSize() + "G";// bug 0003223
		String rs = "";
		if (info.getStorageSize() > 0) {
			rs = storSize;
		}
		if (StringUtils.isEmpty(info.getBkStorageSize()) && "0".equals(info.getBkStorageSize())) {
			rs += used;
		}
		return rs;
	}

	public static String backupState(TInstanceInfoBO info) {
		return stateName[info.getState()];
	}
}
