package com.skycloud.management.portal.front.vm.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.admin.sysmanage.service.impl.UserVlanServiceImpl;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.instance.service.IInstanceService;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.service.VMTemplateListService;
import com.skycloud.management.portal.front.resources.service.VirtualMachineListService;

/**
 * 虚机管理Action
 * 
 * @author wangzheng
 */
public class VMManageAction extends BaseAction {

	private static final long serialVersionUID = -6439121678588361075L;

	private static Log log = LogFactory.getLog(VMManageAction.class);

	private IInstanceService instanceService;

	private VMTemplateListService vmTemplateListService;

	private VirtualMachineListService vmListService;

	private IProductService productService;

	private String resultHCTemplateCPUAndMEMAvailableList;// 返回查询可用虚拟机模板CPU、MEM内存类表

	private String resultVMJsonList;// 返回查询虚拟机结果信息

	private int total;// 总记录数

	private List<Product> proList;

	private Product product;

	private String vmID;// 虚拟机ID

	private int id;

	private int page = 1;// 当前页数

	private int pagesize = 10;// 每页显示多少条

	private Map<String, Object> listVMResult;

	private String searchName;// 查询名字

	private String message;

	private UserVlanServiceImpl userVlanService;

	private int vlanType;

	private int zoneId;

	private int resourcePoolsId;

	List<Product> vpnproductList = new ArrayList<Product>();

	 
	public String showProductByid() {
		try {
			proList = productService.findProductById(id);
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}
   

	/**
	 * 获取VPN列表
	 * 
	 * @param productService
	 */
	public String getVpnProductList() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		List<Product> ssvpnList = productService.findProductFrontDefault(user.getId(), 17);
		System.out.println(ssvpnList.size());
		List<Product> reaccList = productService.findProductFrontDefault(user.getId(), 18);
		System.out.println(reaccList.size());
		for (Product product : ssvpnList) {
			vpnproductList.add(product);
		}
		for (Product product : reaccList) {
			vpnproductList.add(product);
		}
		return SUCCESS;
	}

	// 检查用户是否已经分配了正确的vlan
	public String getCountUserVlanByType() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (null == user) {
			return ERROR;
		}
		TUserVlanBO userVlan = new TUserVlanBO();
		userVlan.setUserId(user.getId());
		userVlan.setType(vlanType);
		userVlan.setZoneId(zoneId);
		try {
			List<TUserVlanBO> list = userVlanService.findUserVlan(userVlan);
			if (null != list && !list.isEmpty()) {
				total = list.size();
			}
		}
		catch (SCSException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return SUCCESS;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public List<Product> getProList() {
		return proList;
	}

	public void setProList(List<Product> proList) {
		this.proList = proList;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public String getResultVMJsonList() {
		return resultVMJsonList;
	}

	public void setResultVMJsonList(String resultVMJsonList) {
		this.resultVMJsonList = resultVMJsonList;
	}

	public String getResultHCTemplateCPUAndMEMAvailableList() {
		return resultHCTemplateCPUAndMEMAvailableList;
	}

	public void setResultHCTemplateCPUAndMEMAvailableList(String resultHCTemplateCPUAndMEMAvailableList) {
		this.resultHCTemplateCPUAndMEMAvailableList = resultHCTemplateCPUAndMEMAvailableList;
	}

	public IInstanceService getInstanceService() {
		return instanceService;
	}

	public void setInstanceService(IInstanceService instanceService) {
		this.instanceService = instanceService;
	}

	public VMTemplateListService getVmTemplateListService() {
		return vmTemplateListService;
	}

	public void setVmTemplateListService(VMTemplateListService vmTemplateListService) {
		this.vmTemplateListService = vmTemplateListService;
	}

	public VirtualMachineListService getVmListService() {
		return vmListService;
	}

	public void setVmListService(VirtualMachineListService vmListService) {
		this.vmListService = vmListService;
	}

	public String getVmID() {
		return vmID;
	}

	public void setVmID(String vmID) {
		this.vmID = vmID;
	}

	public int getCountTotal() {
		return total;
	}

	public void setCountTotal(int total) {
		this.total = total;
	}

	@Override
	public int getPage() {
		return page;
	}

	@Override
	public void setPage(int page) {
		this.page = page;
	}

	@Override
	public int getPagesize() {
		return pagesize;
	}

	@Override
	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getSearchName() {
		return searchName;
	}

	public void setSearchName(String searchName) {
		this.searchName = searchName;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Map<String, Object> getListVMResult() {
		return listVMResult;
	}

	public void setListVMResult(Map<String, Object> listVMResult) {
		this.listVMResult = listVMResult;
	}

	public List<Product> getVpnproductList() {
		return vpnproductList;
	}

	public void setVpnproductList(List<Product> vpnproductList) {
		this.vpnproductList = vpnproductList;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public UserVlanServiceImpl getUserVlanService() {
		return userVlanService;
	}

	public void setUserVlanService(UserVlanServiceImpl userVlanService) {
		this.userVlanService = userVlanService;
	}

	public int getVlanType() {
		return vlanType;
	}

	public void setVlanType(int vlanType) {
		this.vlanType = vlanType;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

}
