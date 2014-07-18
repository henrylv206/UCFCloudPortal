package com.skycloud.management.portal.front.subaccount;

import java.util.ResourceBundle;

import com.skycloud.management.portal.service.ConfigurationLoader;

/*****************************************************************************
 * @content: 二次开发子帐号静态属性及类方法                                                                                   *
 * @author : 那建林                                                                                                                      *
 * @crtDate: 2012-07-16 09:54                                               *
 ****************************************************************************/
public final class SubAccountConst {
	private SubAccountConst(){}
	
	
	
	
	//根据键值查找alarmEventExtConfig.properties相应的参数
	public final static String getAlarmEventExtConfigValue(String key) {
		ResourceBundle rb = ResourceBundle.getBundle("config");
		return rb.getString(key);
	}
	
	
	/**
	 * 当用户登陆超时后，获取跳转的URL
	 * @return
	 * 创建人：马志刚
	 * 创建时间 ： 2012-7-24 下午05:16:27
	 */
	public final static String getAdcLoginUrl(){
		
		return ConfigurationLoader.getInstance().getProperty("adc.login.url");
	}
}
