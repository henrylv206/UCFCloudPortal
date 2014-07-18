package com.skycloud.management.portal.front.task.service;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public interface ISendSmsTaskService {
	
    /**
     * 执行发送短信任务
     * @return
     * 创建人：  ninghao@chinaskycloud.com
     * 创建时间：2013-01-11 上午09:21:01
     */
	public void sendSMSTask () throws ServiceException;
}
