package com.skycloud.management.portal.front.vm.action;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.struts2.ServletActionContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.ConstDef;

/**
 * 多虚管理Action
 * 
 * @author CQ
 */
public class VMSManageAction extends ActionSupport {

	private static final long serialVersionUID = -6439121678588361075L;

	private static Logger logger = LoggerFactory.getLogger(VMSManageAction.class);

	//private IInstanceService instanceService;

	//private VirtualMachineListService vmListService;

	private IProductService productService;

	private int total;// 总记录数

	private int page = 1;// 当前页数

	private int pagesize = 10;// 每页显示多少条

	private Map<String, Object> listVMResult;

	private List<TTemplateVMBO> templates;

	private List<Product> mVMList;

	private int productId;

	private String message;

	private String name;

	 
 

	public String getMVMProductDefault() {
		TUserBO user = (TUserBO) ServletActionContext.getRequest().getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {
			mVMList = productService.findProductFrontDefault(user.getId(), ConstDef.RESOURCE_TYPE_MUTIL_VM);
		}
		catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			message = e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}
 

	public String checkProductRename() {
		try {
			// fix bug 2263
			// String name =
			// ServletActionContext.getRequest().getParameter("name");
			// fix bug 2263
			int productCnt = 0;
			if (StringUtils.isNotEmpty(name)) {
				productCnt = productService.checkVMSProductRename(name);
			}
			if (productCnt > 0) {
				message = "1";
			} else {
				message = "0";
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			logger.error(e.getMessage());
			return ERROR;
		}
		return SUCCESS;
	}

	public IProductService getProductService() {
		return productService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public Map<String, Object> getListVMResult() {
		return listVMResult;
	}

	public void setListVMResult(Map<String, Object> listVMResult) {
		this.listVMResult = listVMResult;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	/*public VirtualMachineListService getVmListService() {
		return vmListService;
	}

	public void setVmListService(VirtualMachineListService vmListService) {
		this.vmListService = vmListService;
	}*/

	public List<TTemplateVMBO> getTemplates() {
		return templates;
	}

	public void setTemplates(List<TTemplateVMBO> templates) {
		this.templates = templates;
	}

	public int getProductId() {
		return productId;
	}

	public void setProductId(int productId) {
		this.productId = productId;
	}

	public List<Product> getmVMList() {
		return mVMList;
	}

	public void setmVMList(List<Product> mVMList) {
		this.mVMList = mVMList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
