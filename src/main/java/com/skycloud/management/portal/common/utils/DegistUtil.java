package com.skycloud.management.portal.common.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("restriction")
public final class DegistUtil {
	public static String md5(String str){
		/*try {
			MessageDigest md=MessageDigest.getInstance("MD5");
			byte[] bys=md.digest(str.getBytes());
			BASE64Encoder encoder=new BASE64Encoder();
			return encoder.encode(bys);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}*/
		String password =str;
		MessageDigest md;
		String encryptPassword="";  	
		try {
			md = MessageDigest.getInstance("MD5");
			byte[] bs = md.digest(password.getBytes("utf-8"));
		    String hex = "";
		    for (byte b : bs)
		        hex += String.format("%02x", b);
		    encryptPassword = hex;  	
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	    return encryptPassword;
		
	}
}
