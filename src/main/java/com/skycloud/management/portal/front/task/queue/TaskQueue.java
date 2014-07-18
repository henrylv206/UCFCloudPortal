package com.skycloud.management.portal.front.task.queue;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.skycloud.management.portal.common.utils.BaseService;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

/**
 * 等待任务阻塞队列
 * 创建人：  刘江宁   
 * 创建时间：2011-11-3  下午03:06:44
 */
public class TaskQueue extends BaseService {

	private static TaskQueue taskeQueue = new TaskQueue();

	private BlockingQueue<List<AsyncJobInfo>> taskBlockQueue = new LinkedBlockingQueue<List<AsyncJobInfo>>();

	private TaskQueue() {

	}
	public static TaskQueue getInstance() {
		if (taskeQueue == null) {
			return taskeQueue = new TaskQueue();
		}
		return taskeQueue;
	}

	public void addItem(List<AsyncJobInfo> list) {
		taskBlockQueue.add(list);
	}

	public List<AsyncJobInfo> popedItem() throws InterruptedException {
		return taskBlockQueue.take();
	}
}
