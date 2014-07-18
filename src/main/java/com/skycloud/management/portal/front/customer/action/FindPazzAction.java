package com.skycloud.management.portal.front.customer.action;

import com.skycloud.management.portal.common.action.BaseAction;
import com.skycloud.management.portal.common.utils.DegistUtil;
import com.skycloud.management.portal.front.customer.service.IFindPazzService;

public class FindPazzAction  extends BaseAction {

	IFindPazzService findpazzService;
	private String username;
	private String emailaddress ;
	private String state;
	
	public IFindPazzService getFindpazzService() {
		return findpazzService;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getState() {
		return this.state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public void setFindpazzService(IFindPazzService findpazzService) {
		this.findpazzService = findpazzService;
	}

	public String execute() {
		String flag = findpazzService.findUser(username, emailaddress);
		if(null!=flag&&!(flag).equals("")){			
		  try{
			  if("UNABLE".equals(flag)){
					this.state = "unable";
					return ERROR;
			  }
			  else {
				  String pazz= findpazzService.createPazz();
				  findpazzService.setPazz(username, emailaddress,DegistUtil.md5(pazz));
				  findpazzService.sendMail(username,emailaddress, pazz);
				  this.state="true";
			      return SUCCESS;
			  }
		  }catch(Exception e){
			  e.printStackTrace();
			  this.state="false";
			  return  ERROR;  
		  }
		}else{
			this.state="invalid";
			return  ERROR;  
		}
	}
}
