package com.skycloud.management.portal.front.task.queue;

import com.skycloud.management.portal.common.utils.ConfigManager;

/**
 * 小机接口调用接口定义类
 * 创建人：  刘江宁   
 * 创建时间：2011-11-24  上午11:22:24
 */
public interface MiniComputerExecuteUrlDefine {
	    /**
	     * 查询任务完成情况
	     */
	    public static final String REST_WEBSERVICE_QUEYURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_QUEYURL");
		/**
		 * 小机开启
		 */
	    public static final String REST_WEBSERVICE_STARTURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_STARTURL");
	    /**
	     * 小机停止
	     */
	    public static final String REST_WEBSERVICE_STOPURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_STOPURL");
	    /**
	     * 小机重启
	     */
	    public static final String REST_WEBSERVICE_REBOOTURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_REBOOTURL");
	    /**
	     * 小机暂停
	     */
	    public static final String REST_WEBSERVICE_SUSPENDURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_SUSPENDURL");
	    /**
	     * 小机恢复
	     */
	    public static final String REST_WEBSERVICE_RESUMEURL = ConfigManager.getInstance().getString("REST_WEBSERVICE_RESUMEURL");
}
