package com.skycloud.management.portal.front.instance.action;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.instance.entity.TInstancePeriodInfo;
import com.skycloud.management.portal.front.instance.entity.TServicePeriodInfo;
import com.skycloud.management.portal.front.instance.service.IInstancePeriodManageService;

public class InstancePeriodManageAction extends BaseAction{

	/**
	 * 创建人：   张爽
	 * 创建时间：2012-3-27  下午02:50:17
	 */
	private static final long serialVersionUID = 3841608702976833579L;

	private final Logger logger = Logger.getLogger(InstancePeriodManageAction.class);

	private IInstancePeriodManageService instancePeriodManageService;
	private TInstancePeriodInfo instancePeriodInfo;
	private TServicePeriodInfo servicePeriodInfo;
	private int instanceId;
	private int num;
	private String unit;
	private String message;

	public String findInstancePeriodById() {
	   try {
	      int id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
	      instancePeriodInfo = instancePeriodManageService.findInstancePeriodById(id);
	    }
	    catch(Exception e) {
	      e.printStackTrace();
	      setMessage("error : " + e.getMessage());
	      logger.error(e.getMessage());
	      return ERROR;
	    }
	    return SUCCESS;
    }

	public String findServiceInstancePeriodById() {
		   try {
		      int id = Integer.parseInt(ServletActionContext.getRequest().getParameter("id"));
		      servicePeriodInfo = instancePeriodManageService.findServiceInstancePeriodById(id);
		    }
		    catch(Exception e) {
		      e.printStackTrace();
		      setMessage("error : " + e.getMessage());
		      logger.error(e.getMessage());
		      return ERROR;
		    }
		    return SUCCESS;
	    }

	public String updateInstancePeriod() {
		int result=0;
		   try {
			  String insId =  ServletActionContext.getRequest().getParameter("id");
			  if (insId == null){
				  this.message="error :续订失败";
			  }
		      int id = Integer.parseInt(insId);
		      instancePeriodInfo = instancePeriodManageService.findInstancePeriodById(id);
		      result = instancePeriodManageService.updateInstancePeriod(num, unit, instancePeriodInfo.getResourceInfo(), instancePeriodInfo.getExpireDate(), id);
		      if(result<=0){
		    	  this.message="error :续订失败";
		      }else{
		    	  this.message="续订成功";
		      }
		    }
		    catch(Exception e) {
		      e.printStackTrace();
		      setMessage("error : " + e.getMessage());
		      logger.error(e.getMessage());
		      return ERROR;
		    }
		    return SUCCESS;
	    }

	public String updateServicePeriod() {
		TUserBO user = (TUserBO) this.getSession().getAttribute(ConstDef.SESSION_KEY_USER);
		if (user == null) {
			this.message="error :续订失败";
			return SUCCESS;
		}
		int result=0;
		try {
			String insId =  ServletActionContext.getRequest().getParameter("id");
			if (insId == null){
				this.message="error :续订失败";
			}
			int id = Integer.parseInt(insId);
			servicePeriodInfo = instancePeriodManageService.findServiceInstancePeriodById(id);
			result = instancePeriodManageService.updateServicePeriod(num, unit, servicePeriodInfo.getPeriod(), servicePeriodInfo.getExpiryDate(), id,user);
			if(result<=0){
				this.message="error :续订失败";
			}else{
				this.message="续订成功";
			}
	    }
	    catch(Exception e) {
	    	e.printStackTrace();
	    	setMessage("error : " + e.getMessage());
	    	logger.error(e.getMessage());
	    	return ERROR;
	    }
	    return SUCCESS;
	}

	public IInstancePeriodManageService getInstancePeriodManageService() {
		return instancePeriodManageService;
	}
	public void setInstancePeriodManageService(
			IInstancePeriodManageService instancePeriodManageService) {
		this.instancePeriodManageService = instancePeriodManageService;
	}
	public TInstancePeriodInfo getInstancePeriodInfo() {
		return instancePeriodInfo;
	}
	public void setInstancePeriodInfo(TInstancePeriodInfo instancePeriodInfo) {
		this.instancePeriodInfo = instancePeriodInfo;
	}

	public TServicePeriodInfo getServicePeriodInfo() {
		return servicePeriodInfo;
	}

	public void setServicePeriodInfo(TServicePeriodInfo servicePeriodInfo) {
		this.servicePeriodInfo = servicePeriodInfo;
	}

	public int getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public String getMessage() {
		return message;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
