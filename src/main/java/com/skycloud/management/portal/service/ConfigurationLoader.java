package com.skycloud.management.portal.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

/**
 * 
 * @author wangcf
 *
 */
public class ConfigurationLoader {
	
	
	private Properties properties  = null;
	
	private static ConfigurationLoader instance = null;
	
	public static synchronized ConfigurationLoader getInstance(){
		if(instance == null){
			instance = new ConfigurationLoader();
		}
		return instance;
	}
	
	/**
	 * 
	 *  Constructor 
	 *
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	private ConfigurationLoader(){
		try{	
			properties = new Properties();
			properties.load(getClass().getResourceAsStream("/config.properties"));
		}catch (Exception e) {
			properties = null;
		}
		
	}
	
	/**
	 * 
	 * getProperty 
	 *
	 * @param key
	 * @return  property value
	 */
	public String getProperty(String key){
		return properties == null ? "" : properties.getProperty(key);
	}
}
