package com.skycloud.management.portal.admin.audit.sevice;

public class SendMailContent {

	private String toMail;
	private String fromMail;
	private String sendText;
	private String subject;
	
	private String sendSmsText;
	private String toMobile;
	private int sendType;
	
	public String getToMail() {
		return toMail;
	}

	public void setToMail(String toMail) {
		this.toMail = toMail;
	}

	public String getFromMail() {
		return fromMail;
	}

	public void setFromMail(String fromMail) {
		this.fromMail = fromMail;
	}

	public String getSendText() {
		return sendText;
	}

	public void setSendText(String sendText) {
		this.sendText = sendText;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
		}
	public SendMailContent clone(){
		SendMailContent mail = new SendMailContent();
		mail.setFromMail(fromMail);
		mail.setToMail(toMail);
		mail.setSubject(subject);
		mail.setSendText(sendText);
		return mail;
	}

	public void setSendSmsText(String sendSmsText) {
		this.sendSmsText = sendSmsText;
	}

	public String getSendSmsText() {
		return sendSmsText;
	}

	public void setSendType(int sendType) {
		this.sendType = sendType;
	}

	public int getSendType() {
		return sendType;
	}

	public void setToMobile(String toMobile) {
		this.toMobile = toMobile;
	}

	public String getToMobile() {
		return toMobile;
	}
}
