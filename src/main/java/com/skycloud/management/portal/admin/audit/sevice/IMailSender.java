package com.skycloud.management.portal.admin.audit.sevice;

import java.sql.SQLException;

import org.springframework.mail.MailException;

public interface IMailSender {
	
	public void sendMail(SendMailContent Content)throws MailException;
	public void FSendMail() throws MailException,SQLException;
	
}
