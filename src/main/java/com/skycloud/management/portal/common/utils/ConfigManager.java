package com.skycloud.management.portal.common.utils;

import org.apache.commons.configuration.CompositeConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.FileConfiguration;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.configuration.reloading.FileChangedReloadingStrategy;
import org.apache.log4j.Logger;


public class ConfigManager {
	private static Logger log = Logger.getLogger("system");
	private static ConfigManager configManager;
	private static CompositeConfiguration config;
	private static String configPath;

	/**
	 * @return
	 */
	public synchronized static ConfigManager getInstance() {
		if (configManager == null) {
			configManager = new ConfigManager();
		}
		return configManager;
	}

	private ConfigManager() {
		try {
			String configPath = this.getClass().getResource(ConstDef.SYSTEM_CONFIG).getFile();
			//String configPath2 = "/D:/Program%20Files/apache-tomcat-6.0.32/webapps/SkyFormOpt/WEB-INF/classes/config.properties";
		    configPath = java.net.URLDecoder.decode(configPath,"utf-8");  
			log.info("get sysconfig file : " + configPath);

			config = new CompositeConfiguration();
			FileConfiguration pconfig = new PropertiesConfiguration(configPath);
			config.addConfiguration(pconfig);
			// pconfig.setAutoSave(true);
			// reload strategy
			FileChangedReloadingStrategy reloadStrategy = new FileChangedReloadingStrategy();
			reloadStrategy.setRefreshDelay(3000);
			// for performance tunning , remove this line
			pconfig.setReloadingStrategy(reloadStrategy);

			log.info("initialize sysconfig config file OK");
		} catch (Exception e) {
			log.error("initialize sysconfig config file error...");
			e.printStackTrace();
		}
	}

	public String getConfigPath() {
		return configPath;
	}

	/**
	 * @return
	 */
	public String getString(String s) {
		return config.getString(s);
	}

	/**
	 * @param s
	 * @return
	 */
	public String[] getStringArray(String s) {
		return config.getStringArray(s);
	}

	/**
	 * return Configuration instance
	 * 
	 * @return
	 */
	public Configuration getConfig() {
		return config;
	}

	public int getInt(String key, int defaultValue) {
		try {
			String value = config.getString(key);
			if (value == null) {
				return defaultValue;
			}
			return Integer.parseInt(value.trim());
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}

	public boolean getBoolean(String key) {
		return this.getBoolean(key, true);
	}

	public boolean getBoolean(String key, boolean defaultValue) {
		try {
			String value = config.getString(key);
			if (value == null) {
				return defaultValue;
			}
			return Boolean.valueOf(value.trim());
		} catch (RuntimeException e) {
			return defaultValue;
		}
	}
	
	public boolean containsKey(String key){
		return config.containsKey(key);
	}
}
