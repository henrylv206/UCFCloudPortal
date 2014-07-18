package com.skycloud.management.portal.front.mall.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.dao.IProductTemplateRelationDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.dao.IVMTemplateDao;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.command.res.ListVirtualMachines;
import com.skycloud.management.portal.front.mall.dao.CloudServiceMallDao;
import com.skycloud.management.portal.front.mall.entity.TServiceInstanceBO;
import com.skycloud.management.portal.front.mall.entity.TemplateTypeBO;
import com.skycloud.management.portal.front.mall.service.CloudServiceMallService;
import com.skycloud.management.portal.front.mall.vo.RelationVO;
import com.skycloud.management.portal.front.mall.vo.ResourceTopoVO;
import com.skycloud.management.portal.front.mall.vo.ResourceVO;
import com.skycloud.management.portal.front.order.dao.IInstanceInfoDao;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.resources.action.vo.PhysicalHostVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.entity.VirtualService;
import com.skycloud.management.portal.front.resources.rest.ListPhysicalHost;
import com.skycloud.management.portal.front.resources.service.IPublicIPInstanceService;
import com.skycloud.management.portal.front.resources.service.IVirtualServiceService;
import com.skycloud.management.portal.front.resources.service.PhysicalMachinesService;
import com.skycloud.management.portal.webservice.databackup.service.IDataBackUpService;

public class CloudServiceMallServiceImpl implements CloudServiceMallService {

	private CloudServiceMallDao dao;

	private IInstanceInfoDao instanceInfoDao;

	private IVirtualServiceService virtualServiceService;

	private IPublicIPInstanceService publicIpInstanceService;

	private IDataBackUpService dataBackUpService;

	private PhysicalMachinesService physicalMachineService;

	private IOrderDao orderDao;

	private final static String topoResourceTypeArray = ConfigManager.getInstance().getString("topo.topoResourceTypeArray");

	private ICloudAPISerivce cloudAPIService;

	public void setPhysicalMachineService(PhysicalMachinesService physicalMachineService) {
		this.physicalMachineService = physicalMachineService;
	}

	public PhysicalMachinesService getPhysicalMachineService() {
		return physicalMachineService;
	}

	public CloudServiceMallDao getDao() {
		return dao;
	}

	public void setDao(CloudServiceMallDao dao) {
		this.dao = dao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public ICloudAPISerivce getCloudAPIService() {
		return cloudAPIService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	@Override
	// bug 0006741
	public ResourcesVO getDeviceNameById(long id, String type) throws Exception {
		ResourcesVO vo = dao.getDeviceNameById(id, type);
		if ("VM".equalsIgnoreCase(type)) {// 虚拟机
			ListVirtualMachines vm = cloudAPIService.listVirtualMachines(Long.valueOf(id + "").intValue(), 0, 0, vo.getResourcePoolsId()).get(0);
			vo.setInstance_name(vm.getName());
		}
		if ("HOST".equalsIgnoreCase(type)) {// 物理机
			ListPhysicalHost listVO = new ListPhysicalHost();
			listVO.setId(id);
			List<PhysicalHostVO> phyVolist = physicalMachineService.findPhysicalHostByREST(listVO, vo.getResourcePoolsId());
			vo.setInstance_name(phyVolist.get(0).getName());
		}
		if ("MINPHY".equalsIgnoreCase(type)) {// 小型机
			// 还 需要跟西安确认device_name来源问题
		}
		return vo;
	}

	@Override
	public int getAllServiceCount(String key, String typeId, int userId) throws Exception {
		return dao.getAllServiceCount(key, typeId, userId);
	}

	@Override
	public List<Product> getAllService(PageVO vo, int part, String sales, String price, String key, String typeId, int userId) throws Exception {
		int cnt = getAllServiceCount(key, typeId, userId);
		int start = 0, end1 = 0, end2 = 0, end = 0;
		// fix bug 3103
		// 重构全部服务的排版顺序（重构PageVO） ninghao 2012-09-26

		start = 1;
		end = cnt;// 不对两排做半数拆分
		PageVO voNew = new PageVO();
		voNew.setPageSize(vo.getPageSize());
		if (part == 1) {// 第1排
			// 当前页=当前页码*2-1
			voNew.setCurPage(vo.getCurPage() * 2 - 1);
		} else if (part == 2) {// 第2排
			if (cnt <= 5) {// 小于5的列表就不拆分了
				return null;
			}
			// 当前页=当前页码*2
			voNew.setCurPage(vo.getCurPage() * 2);
		}
		List<Product> rs = dao.getAllService(voNew, start, end, sales, price, key, typeId, userId);
		return rs;
	}

	@Override
	public int getAllServiceCnt(int part, String key, String typeId, int userId) throws Exception {
		int cnt = getAllServiceCount(key, typeId, userId);
		if (part == 1 && cnt > 5) {
			cnt = cnt / 2;
		} else if (part == 2) {
			cnt = cnt - cnt / 2;
		}
		return cnt;
	}

	public IDataBackUpService getDataBackUpService() {
		return dataBackUpService;
	}

	public void setDataBackUpService(IDataBackUpService dataBackUpService) {
		this.dataBackUpService = dataBackUpService;
	}

	@Override
	public int getCommendServiceCount(int userId) {
		return dao.getCommendServiceCount(userId);
	}

	@Override
	public List<Product> getCommendService(PageVO vo, int userId) throws Exception {
		List<Product> rs = dao.getCommendService(vo, userId);
		return rs;
	}

	@Override
	public List<TemplateTypeBO> getTemplateTypeList() throws SQLException {
		return dao.getTemplateTypeList();
	}

	@Override
	public int getAllServiceCount(TUserBO user, String serviceName, String typeId, String state, String start, String end) throws Exception {
		return dao.getAllServiceCount(user, serviceName, typeId, state, start, end);
	}

	@Override
	public List<TServiceInstanceBO> getAllService(PageVO vo, TUserBO user, String serviceName, String typeId, String state, String start, String end)
	throws Exception {
		List<TServiceInstanceBO> list = dao.getAllService(vo, user, serviceName, typeId, state, start, end);
		list = this.getAllServiceOfOrder(list);
		for (TServiceInstanceBO bo : list) {// bug 0003792
			String sName = bo.getServiceName();
			if (sName.length() > 10) {
				sName = sName.substring(0, 10) + "..";
			}
			bo.setsName(sName);
			String sDesc = bo.getServiceDesc();
			if (sDesc != null) {
				sDesc = sDesc.replace("模板", "");
				bo.setServiceDesc(sDesc);
				bo.setsDesc(sDesc);
			}
		}
		return list;
	}

	private List<TServiceInstanceBO> getAllServiceOfOrder(List<TServiceInstanceBO> rs) throws Exception {
		List<TServiceInstanceBO> productList = new ArrayList<TServiceInstanceBO>();
		// 判断服务是否有未审核完毕的续订单
		if (null != rs && !rs.isEmpty()) {
			StringBuilder ids = new StringBuilder("(0");
			for (TServiceInstanceBO product : rs) {
				ids.append(",");
				ids.append(product.getId());
			}
			ids.append(")");
			List<TServiceInstanceBO> orderList = dao.getAllServiceOfOrder(ids.toString(), 4);

			for (TServiceInstanceBO product : rs) {
				product.setOrderFlag(0);
				if (null != orderList && !orderList.isEmpty()) {
					for (TServiceInstanceBO order : orderList) {
						if (order.getId() == product.getId() && order.getOrderFlag() != 4) {
							// 保存续订订单状态
							product.setOrderFlag(order.getOrderFlag());
						}
					}
				}
				productList.add(product);
			}
		}
		return productList;
	}

	@Override
	public List<TServiceInstanceBO> getQuitService(TUserBO user, int serviceID) throws Exception {
		return dao.getQuitService(user, serviceID);
	}

	@Override
	public List<TServiceInstanceBO> getQuitInstance(TUserBO user, int serviceID) throws Exception {
		return dao.getQuitInstance(user, serviceID);
	}

	@Override
	public List<TServiceInstanceBO> checkLB2FW(int orderId) throws Exception {
		return dao.checkLB2FW(orderId);
	}

	@Override
	public int getAllReourceCount(int serviceId) throws Exception {
		return dao.getAllReourceCount(serviceId);
	}

	@Override
	public List<TInstanceInfoBO> getAllReource(PageVO vo, int serviceId, TUserBO user) throws Exception {
		List<TInstanceInfoBO> rs = dao.getAllReource(vo, serviceId);
		for (TInstanceInfoBO info : rs) {
			if (8 == info.getTemplateType()) {// 公网带宽
				aboutBw(info); // bug 0003800
			} else if (9 == info.getTemplateType()) {// 弹性公网IP
				aboutTip(user, info);
			} else if (4 == info.getTemplateType()) {// 虚机备份
				aboutBackUp(user, info);// bug 0003223
			} else if (2 == info.getTemplateType()) {// 虚拟磁盘
				aboutVdisk(user, info); // bug 0003354
			} else if (1 == info.getTemplateType()) {// 虚拟机
				aboutPassword(info);
			}
		}
		return rs;
	}

	private void aboutPassword(TInstanceInfoBO info) throws Exception {
		String resourceInfo = info.getResourceInfo();
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		if (jsonObject.containsKey("password")) {
			info.setPassword(jsonObject.getString("password"));
		}
	}

	@Override
	public List<TInstanceInfoBO> getAllReource2(PageVO vo, int serviceId, TUserBO user, int orderid) throws Exception {
		List<TInstanceInfoBO> rs = dao.getAllReource(vo, serviceId);
		List<TInstanceInfoBO> rs2 = new ArrayList<TInstanceInfoBO>();
		QueryCriteria query = new QueryCriteria();
		query.setOrderId(orderid);

		List<TOrderBO> orderBOs = orderDao.searchOrders2(null, user, query);
		int type = -1;
		int instanceinfoid = -1;
		if (null != orderBOs && orderBOs.size() > 0) {
			type = orderBOs.get(0).getType();
			instanceinfoid = orderBOs.get(0).getInstanceInfoId();
		}

		int _index = -1;
		if (null != rs && rs.size() > 0) {
			// to fix bug 3720
			for (TInstanceInfoBO info : rs) {
				if (8 == info.getTemplateType()) {// 公网带宽
					aboutBw(info); // bug 0003800
				} else if (9 == info.getTemplateType()) {// 弹性公网IP
					aboutTip(user, info);
				} else if (4 == info.getTemplateType()) {// 虚机备份
					aboutBackUp(user, info);// bug 0003223
				} else if (2 == info.getTemplateType()) {// 虚拟磁盘
					aboutVdisk(user, info); // bug 0003354
				} else if (1 == info.getTemplateType()) { // to fix bug 3720
					if (2 == type) {
						if (instanceinfoid == info.getId()) {
							rs2.add(info);
						}
					}
				}
			}

			// if(rs.get(0).getTemplateType() == 1){
			// rs = rs2;
			// }
			// to fix bug:3943
			if (2 == type && rs2 != null && !rs2.isEmpty()) {
				rs = rs2;
			}
		}
		return rs;
	}

	private void aboutVdisk(TUserBO user, TInstanceInfoBO info) throws Exception {
		ResourcesVO vo = dao.getVolumeVO(user.getId(), info.getId());
		info.setVolumestate(vo.getVolumestate());
		info.setVmName(vo.getVmname());
		info.setVmInstanceId(vo.getVmInstanceId());
	}

	// bug 0003800
	private void aboutBw(TInstanceInfoBO info) throws Exception {
		String resourceInfo = info.getResourceInfo();
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		if (jsonObject.containsKey("ipAddress")) {
			info.setIpAddress(jsonObject.getString("ipAddress"));
		}
	}

	// bug 0003223
	private void aboutBackUp(TUserBO user, TInstanceInfoBO info) throws Exception {
		info.setBkStorageSize(Math.round(dataBackUpService.getUsedCapacityCountNumberByCreateUser(user.getId()) / 1024) + "");
	}

	// 特殊处理：弹性公网IP
	private void aboutTip(TUserBO user, TInstanceInfoBO info) throws Exception {
		String serviceProvider = "";// 运营商编码
		String ipAddressId = "";// 公网ip的id
		String ipAddress = "";// 公网ip地址
		String ipType = ""; // 公网ip类型，IPV4或者IPV6
		String vmId = ""; // 跟公网ip绑定的虚拟机id
		String vmName = ""; // 跟公网ip绑定的虚拟机
		String instanceId = "";
		String resourceInfo = info.getResourceInfo();
		JSONObject jsonObject = JSONObject.fromObject(resourceInfo);
		if (jsonObject.containsKey("instanceId")) {
			instanceId = jsonObject.getString("instanceId");
		}
		if (jsonObject.containsKey("serviceProvider")) {
			serviceProvider = jsonObject.getString("serviceProvider");
		}
		if (jsonObject.containsKey("ipType")) {
			ipType = jsonObject.getString("ipType");
		}
		if (jsonObject.containsKey("ipAddressId")) {
			ipAddressId = jsonObject.getString("ipAddressId");
		}
		if (jsonObject.containsKey("ipAddress")) {
			ipAddress = jsonObject.getString("ipAddress");
		}
		if (jsonObject.containsKey("instanceName")) {
			vmName = jsonObject.getString("instanceName");
		}
		if (ConstDef.curProjectId == 2) {
			if (instanceId.trim().length() > 0) {
				TInstanceInfoBO refVm = instanceInfoDao.searchInstanceInfoByID(Integer.valueOf(instanceId));
				info.setVmId(Integer.valueOf(instanceId)); // 绑定的虚机id
				info.setVmName(refVm.getInstanceName()); // 绑定的虚拟机
			} else {
				info.setVmId(Integer.valueOf(instanceId));// 绑定的虚机id
				info.setVmName(""); // 绑定的虚拟机
			}
		}
		if (ConstDef.curProjectId == 1) {
			int ipid = StringUtils.isNotEmpty(ipAddressId) ? Integer.parseInt(ipAddressId) : 0;
			List<VirtualService> vsList = virtualServiceService.getServiceListByUser(user.getId());
			ResourcesVO vm = publicIpInstanceService.queryBindVMByIp(ipid);
			if (null != vm) {
				int eid = Integer.parseInt(vm.getE_instance_id());
				if (eid > 0) {
					info.setVmId(vm.getId()); // 绑定的虚机id
					info.setVmName(vm.getInstance_name()); // 绑定的虚拟机
					info.seteInstanceId(Long.valueOf(vm.getE_instance_id())); // 绑定的虚拟机EID或则虚服务
				} else if (eid == -1) {
					if (null != vsList && !vsList.isEmpty()) {
						for (VirtualService vs : vsList) {
							// 绑定的虚拟机EID或则虚服务
							info.seteInstanceId(eid); // 虚服务
							info.setVmName(vs.getVsIp());
						}
					}
				}
			} else {
				if (null != vsList && !vsList.isEmpty()) {
					for (VirtualService vs : vsList) {
						// 绑定的虚拟机EID或则虚服务
						info.seteInstanceId(0); // 虚服务
						info.setVmName(vs.getVsIp());
					}
				}
			}
		}
		if (StringUtils.isNotEmpty(serviceProvider)) {
			info.setZoneId(Long.valueOf(serviceProvider));
		}
		info.setIpType(ipType);
		info.setIpAddressId(ipAddressId);
		info.setIpAddress(ipAddress);
		info.setServiceProvider(serviceProvider);
	}

	// ----------------------added by
	// zhanghuizheng------------------------------
	@Override
	public List<Product> find(TTemplateVMBO t, Product p, int curPage, int pageSize) {
		return dao.find(t, p, curPage, pageSize);
	}

	@Override
	public int getItemServiceCount(TTemplateVMBO t, Product p) {
		return dao.getItemServiceCount(t, p);
	}

	@Override
	public List<Product> findRand() {
		return dao.findRand();
	}

	private IProductDao productDao;

	private IProductTemplateRelationDao productTemplateRelationDao;

	private IVMTemplateDao VMTemplateDao;

	public IProductDao getProductDao() {
		return productDao;
	}

	public void setProductDao(IProductDao productDao) {
		this.productDao = productDao;
	}

	public IProductTemplateRelationDao getProductTemplateRelationDao() {
		return productTemplateRelationDao;
	}

	public void setProductTemplateRelationDao(IProductTemplateRelationDao productTemplateRelationDao) {
		this.productTemplateRelationDao = productTemplateRelationDao;
	}

	public IVMTemplateDao getVMTemplateDao() {
		return VMTemplateDao;
	}

	public void setVMTemplateDao(IVMTemplateDao vMTemplateDao) {
		VMTemplateDao = vMTemplateDao;
	}

	public IInstanceInfoDao getInstanceInfoDao() {
		return instanceInfoDao;
	}

	public void setInstanceInfoDao(IInstanceInfoDao instanceInfoDao) {
		this.instanceInfoDao = instanceInfoDao;
	}

	public IVirtualServiceService getVirtualServiceService() {
		return virtualServiceService;
	}

	public void setVirtualServiceService(IVirtualServiceService virtualServiceService) {
		this.virtualServiceService = virtualServiceService;
	}

	public IPublicIPInstanceService getPublicIpInstanceService() {
		return publicIpInstanceService;
	}

	public void setPublicIpInstanceService(IPublicIPInstanceService publicIpInstanceService) {
		this.publicIpInstanceService = publicIpInstanceService;
	}

	@Override
	public Product getProductById(int id) throws SCSException {
		Product p = productDao.get(id);
		// 如果是多虚机服务则获取产品关联的所有模板和数量
		if (p.getType() == ConstDef.RESOURCE_TYPE_MUTIL_VM) {
			List<TTemplateVMBO> templates = this.productTemplateRelationDao.getProductTemplates(id);
			if (null != templates && templates.size() > 0) {
				p.setTemplates(templates);
			}
		} else if (p.getType() == ConstDef.RESOURCE_TYPE_MINICOMPUTER) { // 小型机

		} else {
			int templateid = p.getTemplateId();
			if (templateid > 0) {
				TTemplateVMBO template = this.VMTemplateDao.getTemplateById(templateid);
				p.setTemplate(template);
			}
		}
		return p;
	}

	/**
	 * 查询我的拓扑图数据信息 ninghao 2012-09-04
	 * 
	 * @param user
	 * @return String json format
	 */
	@Override
	public ResourceTopoVO findMyTopoStructData(TUserBO user) throws Exception {
		String returnJson = "{}";

		// 模板类型含义明细：
		// 1：表示虚拟机；2：表示块存储;3：小型机;4：表示备份；5：表示监控；
		// 6：表示负载均衡；7：表示防火墙；8：表示带宽；9：表示公网IP；10：表示物理机；
		// 11：对象存储；12：快存储；15：云数据备份
		String[] type = null;
		// 查询VLAN

		// 查询VM，查询INFO表中TEMPLATE_TYPE=1的记录
		// type = new String[1];
		// type[0] = "1";
		// List<ResourceVO> vmlist = dao.getMyTopoReourceVO(user, type);

		// 查询LB，查询INFO表中TEMPLATE_TYPE=6的记录
		// type[0] = "6";
		// List<ResourceVO> lblist = dao.getMyTopoReourceVO(user, type);

		// 查询FW，查询INFO表中TEMPLATE_TYPE=7的记录
		// type[0] = "7";
		// List<ResourceVO> fwlist = dao.getMyTopoReourceVO(user, type);

		// 一次查询全部资源信息，1：表示虚拟机；6：表示负载均衡；7：表示防火墙；
		if (topoResourceTypeArray == null || "".equals(topoResourceTypeArray)) {
			type = new String[3];
			type[0] = "1";
			type[1] = "6";
			type[2] = "7";
		} else {
			type = topoResourceTypeArray.split(",");
		}

		List<ResourceVO> reslist = dao.getMyTopoReourceVO(user, type);

		// 查询关系
		List<RelationVO> relationlist = dao.getMyTopoRelationVO(user, type);

		// 重构VLAN的重复sid值
		this.convertVlanIdRepeat(relationlist, reslist);

		// 获取起始节点数组topIDArray
		// 取VLAN为起始节点
		String topIDArray = "";
		topIDArray = this.getTopIDArray(relationlist, reslist);

		// 对象转json字符串
		String resourceJson = net.sf.json.JSONArray.fromObject(reslist).toString();
		String relationJson = net.sf.json.JSONArray.fromObject(relationlist).toString();
		// 替换fromId为from，替换toId为to
		relationJson = relationJson.replaceAll("fromId", "from");
		relationJson = relationJson.replaceAll("toId", "to");

		// 资源管理平台URL配置
//		String portalURL = topoPortalURL;
//		String resPlatformURL = topoResPlatformURL;
		ResourceTopoVO topo = new ResourceTopoVO();
//		topo.setResPlatformURL(topoResPlatformURL);
		topo.setResourceIDsJson(resourceJson);
		topo.setRelationsJson(relationJson);
		topo.setTopIDArray(topIDArray);
		topo.setTopoPortalURL(ConstDef.getRegisterActiveURL());

		// returnJson = net.sf.json.JSONArray.fromObject(rsList).toString();

		return topo;
	}

	/**
	 * 判断VLAN的id是否与运营管理平台中资源id重复 若不重复，无需执行任何处理 若重复，需重新生成新的VLAN的id
	 * 
	 * @param relationlist
	 * @param reslist
	 */
	private void convertVlanIdRepeat(List<RelationVO> relationlist, List<ResourceVO> reslist) {
		if (relationlist == null || relationlist.size() == 0) {
			return;
		}
		if (reslist == null || reslist.size() == 0) {
			return;
		}
		// 先取Resource的最大sid
		int maxSid = 0;
		// 取VLAN资源
		List<ResourceVO> vlanlist = new ArrayList<ResourceVO>();
		// 取非VLAN资源
		List<ResourceVO> otherlist = new ArrayList<ResourceVO>();
		ResourceVO resvo = null;
		for (int i1 = 0; i1 < reslist.size(); i1++) {
			resvo = reslist.get(i1);
			if (resvo != null) {
				if (resvo.getSid() > maxSid) {
					maxSid = resvo.getSid();
				}
				if ("VLAN".equals(resvo.getType())) {
					try {// fix bug 3952 vlan名称显示资源管理设置的网络名称
						List<ENetwork> networkList = cloudAPIService.listNetworks(resvo.getId(), resvo.getPoolid());
						if (networkList != null && networkList.size() > 0) {
							String vlanName = networkList.get(0).getName();
							resvo.setName(vlanName);// 更新资源name
						}
					}
					catch (Exception e) {
						// e.printStackTrace();//出错，显示门户的"vlan"+id
						resvo.setName("VLAN" + resvo.getId());// 更新资源name
					}
					vlanlist.add(resvo);
				} else {
					otherlist.add(resvo);
				}
			}
		}
		// 判断VLAN的sid是否重复
		boolean isCHF = false;
		boolean isInNew = false;
		// 取VLAN资源new
		List<ResourceVO> vlannew = new ArrayList<ResourceVO>();
		// 取VLAN资源不重复的记录
		List<ResourceVO> vlanNotCF = new ArrayList<ResourceVO>();
		if (vlanlist != null && vlanlist.size() > 0 && otherlist != null && otherlist.size() > 0) {
			ResourceVO resVlan = null;
			for (int i2 = 0; i2 < vlanlist.size(); i2++) {
				resVlan = vlanlist.get(i2);
				isInNew = false;
				for (int i3 = 0; i3 < otherlist.size(); i3++) {
					resvo = otherlist.get(i3);
					if (resVlan.getSid() == resvo.getSid()) {// 重复
						maxSid = maxSid + 1;
						// resVlan.setSid(maxSid);//替换sid 暂时不替换，留待比较，后面替换
						resVlan.setId(maxSid);// 替换id
						// resVlan.setName(""+maxSid);//暂时不替换name
						isCHF = true;
						isInNew = true;
						vlannew.add(resVlan);
						break;
					}
				}
				if (!isInNew) {
					vlanNotCF.add(resVlan);
				}
			}
		}
		if (isCHF) {
			List<ResourceVO> vlannew2 = new ArrayList<ResourceVO>();
			// 重复，则替换关系中VLAN的fromId
			ResourceVO resVlan = null;
			RelationVO rvo = null;
			for (int i4 = 0; i4 < vlannew.size(); i4++) {
				resVlan = vlannew.get(i4);
				for (int i5 = 0; i5 < relationlist.size(); i5++) {
					rvo = relationlist.get(i5);
					if (rvo != null && resVlan != null) {
						if (rvo.getFromId() == resVlan.getSid()) {
							rvo.setFromId(resVlan.getId());// 更新关系fromId

							resVlan.setSid(resVlan.getId());// 更新资源sid
							// resVlan.setName("VLAN" + resVlan.getId());//
							// 更新资源name
							vlannew2.add(resVlan);
						}
					}
				}
			}
			// 重复，更新reslist
			reslist = otherlist;
			for (int i6 = 0; i6 < vlanNotCF.size(); i6++) {
				resVlan = vlanNotCF.get(i6);
				reslist.add(resVlan);
			}
			for (int i7 = 0; i7 < vlannew.size(); i7++) {
				resVlan = vlannew.get(i7);
				reslist.add(resVlan);
			}
		}
	}

	/**
	 * 获取顶层节点
	 * 
	 * @param relationlist
	 * @param reslist
	 */
	private String getTopIDArray(List<RelationVO> relationlist, List<ResourceVO> reslist) {
		String topIDArray = "[]";
		if (relationlist == null || relationlist.size() == 0) {
			// return topIDArray;//关系可以为0个，这种情况下所有资源都是孤岛，因此去除此判断
		}
		if (reslist == null || reslist.size() == 0) {
			return topIDArray;
		}
		int rlen = relationlist.size();
		RelationVO rvo = null;
		ResourceVO resvo = null;
		int[] fromIdArray = new int[rlen];
		int[] toIdArray = new int[rlen];
		for (int i = 0; i < relationlist.size(); i++) {
			rvo = relationlist.get(i);
			if (rvo != null) {
				fromIdArray[i] = rvo.getFromId();
				toIdArray[i] = rvo.getToId();
			}
		}
		if (rvo != null) {
			rvo = null;
		}
		int fromCount = 0;
		// 去除重复的顶点： 在fromIdArray中重复，则移除，保留一个顶点即可
		for (int i0 = 0; i0 < fromIdArray.length; i0++) {
			if (fromIdArray[i0] > 0) {
				for (int j = i0 + 1; j < fromIdArray.length; j++) {
					if (fromIdArray[i0] == fromIdArray[j]) {
						// 在toId中存在
						fromIdArray[j] = 0;
					}
				}
			}
		}
		// 在toId中存在，表示不是顶点，则移出顶点fromIdArray
		for (int i1 = 0; i1 < fromIdArray.length; i1++) {
			for (int j = 0; j < toIdArray.length; j++) {
				if (fromIdArray[i1] == toIdArray[j]) {
					// 在toId中存在
					fromIdArray[i1] = 0;
					break;
				}
			}
			if (fromIdArray[i1] > 0) {
				fromCount++;
			}
		}
		if (fromCount > 0) {// 有顶点，组装topIDArray
			topIDArray = "[";
			for (int i2 = 0; i2 < fromIdArray.length; i2++) {
				if (fromIdArray[i2] > 0) {
					if (fromCount == 1) {
						topIDArray += fromIdArray[i2] + "";
						break;
					} else {
						topIDArray += fromIdArray[i2] + ",";
					}
					fromCount--;
				}
			}
			// ResourceVO中的sid在from和to中不存在，表示单个节点的孤岛
			boolean isInFromTo = false;
			for (int i3 = 0; i3 < reslist.size(); i3++) {
				resvo = reslist.get(i3);
				isInFromTo = false;
				if (resvo != null) {
					for (int i4 = 0; i4 < fromIdArray.length; i4++) {
						if (resvo.getSid() == fromIdArray[i4]) {
							isInFromTo = true;// 存在，不是孤岛
							break;
						}
					}
					for (int i5 = 0; i5 < toIdArray.length; i5++) {
						if (resvo.getSid() == toIdArray[i5]) {
							isInFromTo = true;// 存在，不是孤岛
							break;
						}
					}
				}
				if (!isInFromTo) {// 单个节点的孤岛
					topIDArray += "," + resvo.getSid();
				}
			}
			topIDArray += "]";
		} else {// 全部资源节点都是顶点
			topIDArray = "[";
			for (int i6 = 0; i6 < reslist.size(); i6++) {
				resvo = reslist.get(i6);
				if (resvo != null) {
					if (i6 == 0) {
						topIDArray += resvo.getSid() + "";
					} else {
						topIDArray += "," + resvo.getSid();
					}
				}
			}
			topIDArray += "]";
		}

		return topIDArray;
	}
}
