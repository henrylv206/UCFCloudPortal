package com.skycloud.management.portal.front.user.action;

import java.util.Date;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IUserManageService;
import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.DegistUtil;
import com.skycloud.management.portal.front.log.aop.LogInfo;

public class PortalUserAction extends BaseAction{

	private static final long serialVersionUID = -2024672109497384636L;
	private Logger logger=Logger.getLogger(PortalUserAction.class);
	private IUserManageService userService;
	private String pwd="";
	private String state;
	
	@LogInfo(desc="检验用户输入的旧密码是否正确",moduleName="个人信息-修改密码",functionName="修改密码-检查旧密码",operateType=4,parameters="pwd")
	public String oldPwdEquals() throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(
				ConstDef.SESSION_KEY_USER);
		try {
			if(DegistUtil.md5(pwd).equals(user.getPwd())){
				this.state="true";
			}else {
				this.state="false";
			}
			return SUCCESS;
		} catch (Exception e) {
			this.state="false";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ERROR;
	}
	
	@LogInfo(desc="用户规定的新密码替换旧密码",moduleName="个人信息-修改密码",functionName="修改密码",operateType=3,parameters="pwd")
	public String updateUserPwd() throws Exception {
		TUserBO user = (TUserBO) this.getSession().getAttribute(
				ConstDef.SESSION_KEY_USER);		
		try {
			user.setPwd(DegistUtil.md5(pwd));
			user.setLastupdateDt(new Date(System.currentTimeMillis()));
			userService.updateUserPwd(user);
			this.state="true";
			//密码修改成功,重设session
			this.getSession().setAttribute(ConstDef.SESSION_KEY_USER, user);
			TUserBO user1 = (TUserBO) this.getSession().getAttribute(
					ConstDef.SESSION_KEY_USER);
			return SUCCESS;
		} catch (Exception e) {
			this.state="false";
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return ERROR;
	}

	public void setPwd(String pwd) {
		this.pwd = pwd;
	}

	public String getPwd() {
		return pwd;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getState() {
		return state;
	}

	public void setUserService(IUserManageService userService) {
		this.userService = userService;
	}

	public IUserManageService getUserService() {
		return userService;
	}
	
}
