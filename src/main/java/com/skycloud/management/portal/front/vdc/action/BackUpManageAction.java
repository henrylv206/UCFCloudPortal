package com.skycloud.management.portal.front.vdc.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.PropertyUtils;
import org.apache.struts2.ServletActionContext;

import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.resmanage.service.IProductService;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.admin.template.service.IVMTemplateService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.order.service.ICloudAPISerivce;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesModifyVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;
import com.skycloud.management.portal.front.resources.service.BackUpInstanceOperateService;
import com.skycloud.management.portal.webservice.databackup.service.IDataBackUpService;

/**
 * 备份服务Action
 * 
 * @author jiaoyz
 */
public class BackUpManageAction extends BaseAction {

	private static final long serialVersionUID = 4757883618543650798L;

	private BackUpInstanceOperateService backUpInstanceOperateService;

	private IDataBackUpService dataBackUpService;

	private IVMTemplateService VMTemplateService;

	private ICloudAPISerivce cloudAPIService;

	private IProductService productService;

	private int curPage = 1;

	private int pageSize = 10;

	private Map<String, Object> listResp;

	private String message;

	private List<Map<String, Object>> templateList;

	private int instanceId;

	private int storageSize;

	private String reason;

	private int serviceID;

	private List<Product> proList;

	private int resourcePoolsId;

	private int zoneId;

	private int special;
	
	private int creatorUserId;
	private IUserManageService userService;

	 
 
	public String getBKList() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user == null) {
			message = "error : no login info";
			return ERROR;
		} else {
			listResp = new HashMap<String, Object>();
			listResp.put("page", getCurPage());
			listResp.put("size", getPageSize());
			try {
				ResourcesQueryVO vo = new ResourcesQueryVO();
				vo.setUser(user);
				int total = backUpInstanceOperateService.queryBackUpInstanceListCount(vo);
				listResp.put("total", total);
				if (total > 0) {
					PageVO page = new PageVO();
					page.setCurPage(getCurPage());
					page.setPageSize(getPageSize());
					vo.setPage(page);
					List<ResourcesVO> list = backUpInstanceOperateService.queryBackUpInstanceList(vo);
					for (ResourcesVO rvo : list) {
						rvo.setStorage_size(dataBackUpService.getUsedCapacityCountNumberByCreateUser(user.getId()) + "/" + rvo.getStorage_size());
					}
					listResp.put("list", list);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
				message = "error : " + e.getMessage();
				return ERROR;
			}
		}
		return SUCCESS;
	}

	@SuppressWarnings("unchecked")
	public String templateList() {
		try {
			templateList = new ArrayList<Map<String, Object>>();
			List<TResourcePoolsBO> poolList = cloudAPIService.listAllResourcePools();
			if (poolList != null && poolList.size() > 0) {
				Map<String, String> poolMap = new HashMap<String, String>();
				for (TResourcePoolsBO pool : poolList) {
					poolMap.put(String.valueOf(pool.getId()), pool.getPoolName());
				}
				List<TTemplateVMBO> list = VMTemplateService.listTemplate(ConstDef.RESOURCE_TYPE_BACKUP, ConstDef.STATE_TWO, -1, -1, resourcePoolsId,
				                                                          zoneId);
				if (list != null && list.size() > 0) {
					for (TTemplateVMBO bo : list) {
						Map<String, Object> map = PropertyUtils.describe(bo);
						map.put("poolName", poolMap.get(String.valueOf(bo.getResourcePoolsId())));
						templateList.add(map);
					}
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public String productList() {
		try {
			// to fix bug [2833]
			proList = productService.findProductFrontDefaultAllTJ(ConstDef.RESOURCE_TYPE_BACKUP);
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error : " + e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}
 

	public String applyChange() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(instanceId);
		vmModifyVO.setApply_reason(reason);
		vmModifyVO.setStorage_size(storageSize);
		int usedSize = dataBackUpService.getUsedCapacityCountNumberByCreateUser(user.getId());
		// to fix bug [2274]
		if (storageSize < usedSize / 1024) {
			message = "已使用备份空间大于要修改为的备份空间!";
			return ERROR;
		}
		try {
			backUpInstanceOperateService.insertDirtyReadChangeBackUpInstance(vmModifyVO, user);
			message = "修改备份空间大小的申请已经成功提交！";
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error ：" + e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}
 

	public String backupDestroy() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);

		ResourcesModifyVO vmModifyVO = new ResourcesModifyVO();
		vmModifyVO.setId(instanceId);
		vmModifyVO.setApply_reason(reason);
		try {
		String _fromOpt = ServletActionContext.getRequest().getParameter("fromOpt");
		
		if(null != _fromOpt && _fromOpt.equals("1")){
			int _userid = this.creatorUserId;
			user = this.userService.getUserById(_userid);
		}else{
			user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		}
		
		int usedSize = dataBackUpService.getUsedCapacityCountNumberByCreateUser(user.getId());
		if (usedSize > 0) {
			message = "已生成快照,请删除所有快照再作废!";
			return ERROR;
		}
		
			backUpInstanceOperateService.insertBackUpDestroy(vmModifyVO, user, serviceID);
			message = "备份服务退订申请已成功提交！";
		}
		catch (Exception e) {
			e.printStackTrace();
			message = "error ：" + e.getMessage();
			return ERROR;
		}
		return SUCCESS;
	}

	public String checkbackupDestroy() {
		TUserBO user = (TUserBO) getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		int usedSize = dataBackUpService.getUsedCapacityCountNumberByCreateUser(user.getId());
		if (usedSize > 0) {
			message = "0";
		} else {
			message = "1";
		}
		return SUCCESS;
	}

	public void setBackUpInstanceOperateService(BackUpInstanceOperateService backUpInstanceOperateService) {
		this.backUpInstanceOperateService = backUpInstanceOperateService;
	}

	public void setDataBackUpService(IDataBackUpService dataBackUpService) {
		this.dataBackUpService = dataBackUpService;
	}

	public void setVMTemplateService(IVMTemplateService templateService) {
		VMTemplateService = templateService;
	}

	public void setCloudAPIService(ICloudAPISerivce cloudAPIService) {
		this.cloudAPIService = cloudAPIService;
	}

	public void setProductService(IProductService productService) {
		this.productService = productService;
	}

	public int getCurPage() {
		return curPage;
	}

	public void setCurPage(int curPage) {
		this.curPage = curPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public Map<String, Object> getListResp() {
		return listResp;
	}

	public void setListResp(Map<String, Object> listResp) {
		this.listResp = listResp;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public List<Map<String, Object>> getTemplateList() {
		return templateList;
	}

	public void setTemplateList(List<Map<String, Object>> templateList) {
		this.templateList = templateList;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}

	public int getStorageSize() {
		return storageSize;
	}

	public void setStorageSize(int storageSize) {
		this.storageSize = storageSize;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public List<Product> getProList() {
		return proList;
	}

	public void setProList(List<Product> proList) {
		this.proList = proList;
	}

	public int getServiceID() {
		return serviceID;
	}

	public void setServiceID(int serviceID) {
		this.serviceID = serviceID;
	}

	public int getResourcePoolsId() {
		return resourcePoolsId;
	}

	public void setResourcePoolsId(int resourcePoolsId) {
		this.resourcePoolsId = resourcePoolsId;
	}

	public int getZoneId() {
		return zoneId;
	}

	public void setZoneId(int zoneId) {
		this.zoneId = zoneId;
	}

	public int getSpecial() {
		return special;
	}

	public void setSpecial(int special) {
		this.special = special;
	}

	public int getCreatorUserId() {
		return creatorUserId;
	}

	public void setCreatorUserId(int creatorUserId) {
		this.creatorUserId = creatorUserId;
	}

	public IUserManageService getUserService() {
		return userService;
	}

	public void setUserService(IUserManageService userService) {
		this.userService = userService;
	}
	
}
