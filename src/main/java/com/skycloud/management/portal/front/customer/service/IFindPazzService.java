package com.skycloud.management.portal.front.customer.service;


public interface IFindPazzService {
	
	public String findUser(String username , String emailaddress);
	
	public String createPazz();
	
	public void sendMail(String username,String emailaddress , String text) throws Exception;
	
	public void setPazz(String username, String emailaddress, String pazz) throws Exception;
	
}
