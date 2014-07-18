package com.skycloud.management.portal.front.task.service;

import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public interface ISendMailTaskService {
	
    /**
     * 执行发送邮件任务
     * @return
     * 创建人：  ninghao@chinaskycloud.com
     * 创建时间：2013-01-11 上午09:21:01
     */
	public void sendMailTask () throws ServiceException;
}
