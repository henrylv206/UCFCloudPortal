package com.skycloud.management.portal.admin.sysmanage.action;

import java.util.List;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserVlanBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserVlanService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.command.res.ENetwork;
import com.skycloud.management.portal.front.log.aop.LogInfo;

public class UserVlanAction extends BaseAction{


	private static final long serialVersionUID = 1L;
	private final Logger logger=Logger.getLogger(UserVlanAction.class);
	private IUserVlanService userVlanService;
	private String returnJson; // 符合Json格式的语法的数组字符串
	private List<TUserVlanBO> returnList;
	private TUserVlanBO userVlan;
	private String jsonStr;
	private String state;
	private int userId;
	private int vlanId;
	private int type;
	private int zoneId;
	private int id;
	private String account;
	private List<ENetwork> networkList;

	private QueryCriteria criteria;
	private int resourcePoolsId;
	List<TResourcePoolsBO>  resourcePoolslist;


	@LogInfo(desc="显示指定用户VLAN详细信息",moduleName="系统管理-VLAN管理",functionName="用户VLAN详情",operateType=4,parameters="id")
	public String findUserVlanByUserId() throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		try {
			TUserVlanBO userVlan1 = new TUserVlanBO();

			userVlan1.setUserId(user.getId());
			userVlan1.setZoneId(zoneId);
			//fix bug:7815
			userVlan1.setResourcePoolsId(resourcePoolsId);
			if(type>0){
				userVlan1.setType(type);
				returnList = userVlanService.findUserVlan(userVlan1);
			} else {
				returnList = userVlanService.findUserZone(userVlan1);
			}



			logger.info("query the user by userId ,id="+id);
			return SUCCESS;
		} catch (Exception e) {
			returnJson="error : "+e.getMessage();
			logger.error(e.getMessage());
		}
		return "ERROR";
	}


    public IUserVlanService getUserVlanService() {
    	return userVlanService;
    }

    public void setUserVlanService(IUserVlanService userVlanService) {
    	this.userVlanService = userVlanService;
    }

	public void setReturnJson(String returnJson) {
		this.returnJson = returnJson;
	}
	public String getReturnJson() {
		return returnJson;
	}

	public List<TUserVlanBO> getReturnList() {
		return returnList;
	}

	public void setReturnList(List<TUserVlanBO> returnList) {
		this.returnList = returnList;
	}


    public TUserVlanBO getUserVlan() {
    	return userVlan;
    }

    public void setUserVlan(TUserVlanBO userVlan) {
    	this.userVlan = userVlan;
    }

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public int getVlanId() {
    	return vlanId;
    }

    public void setVlanId(int vlanId) {
    	this.vlanId = vlanId;
    }

    public int getType() {
    	return type;
    }

    public void setType(int type) {
    	this.type = type;
    }




    public int getZoneId() {
    	return zoneId;
    }


    public void setZoneId(int zoneId) {
    	this.zoneId = zoneId;
    }

	public void setJsonStr(String jsonStr) {
		this.jsonStr = jsonStr;
	}

	public String getJsonStr() {
		return jsonStr;
	}



    public int getId() {
    	return id;
    }

    public void setId(int id) {
    	this.id = id;
    }


    public List<ENetwork> getNetworkList() {
    	return networkList;
    }


    public void setNetworkList(List<ENetwork> networkList) {
    	this.networkList = networkList;
    }


    public String getAccount() {
    	return account;
    }


    public void setAccount(String account) {
    	this.account = account;
    }


    public int getResourcePoolsId() {
    	return resourcePoolsId;
    }


    public void setResourcePoolsId(int resourcePoolsId) {
    	this.resourcePoolsId = resourcePoolsId;
    }


    public List<TResourcePoolsBO> getResourcePoolslist() {
    	return resourcePoolslist;
    }


    public void setResourcePoolslist(List<TResourcePoolsBO> resourcePoolslist) {
    	this.resourcePoolslist = resourcePoolslist;
    }







}
