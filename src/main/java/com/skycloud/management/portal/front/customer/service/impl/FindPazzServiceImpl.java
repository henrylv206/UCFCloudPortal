package com.skycloud.management.portal.front.customer.service.impl;

import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.front.customer.dao.IAdminDAO;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.customer.service.IFindPazzService;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.MD5andKL;
import com.skycloud.management.portal.admin.audit.sevice.IMailSender;
import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;


public class FindPazzServiceImpl implements IFindPazzService {
	
	IAdminDAO adminDao;
	IMailSender mailSend;
	private String fromMail;
	private String subject;
	private String text;
	
	
	
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public void setAdminDao(IAdminDAO adminDao) {
		this.adminDao = adminDao;
	}

	
	public void setMailSend(IMailSender mailSend) {
		this.mailSend = mailSend;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}
	
	@Override
	public String findUser(String username, String emailaddress) {
		TUserBO user;
		String tabName = null;
		//to fix 1821
		int cloud = ConstDef.getCloudId();
		try {
			user = adminDao.findPazzWithState(username, emailaddress,cloud);
			if(null!=user&&CompanyCheckStateEnum.SUCCESS==user.getState()){
				tabName = "T_SCS_USER";
			}
			else if(null!=user&&CompanyCheckStateEnum.SUCCESS!=user.getState()){
				tabName = "UNABLE";
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return tabName;
	}

	@Override
	public String createPazz() {
		return MD5andKL.genRandomNum(8);
	}

	@Override
	public void sendMail(String username,String emailaddress , String pazz) {
	 try {
		SendMailContent content  = new SendMailContent();
		  content.setToMail(emailaddress);
		  content.setFromMail(fromMail);
		  content.setSubject(this.getSubject());
		  StringBuilder text = new StringBuilder(this.getText());
		  int index = text.indexOf("#newpassword#");		  
		  content.setSendText(text.replace(index, index+13, pazz).toString());
		  int cloud = ConstDef.getCloudId();
		  TUserBO user = adminDao.findPazzWithState(username, emailaddress,cloud);
		  content.setToMobile(user.getMobile());
		  if(ConfigManager.getInstance().containsKey("sms.find.pazz.text")){
			  content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.find.pazz.text"),pazz));
		  }
		  mailSend.sendMail(content);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void setPazz(String account,String emailaddress ,String pazz)throws Exception {
		Object object = null;		
		
		if(this.findUser(account, emailaddress).equals("T_SCS_USER")){
			adminDao.updatePassword(account,emailaddress,pazz );
		}
		else if(this.findUser(account, emailaddress).equals("T_COMPANY_USER")){
			
		}}
		
	}
