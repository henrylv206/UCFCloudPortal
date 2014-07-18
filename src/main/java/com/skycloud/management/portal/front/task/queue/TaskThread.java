package com.skycloud.management.portal.front.task.queue;

import java.util.ArrayList;
import java.util.List;

import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;

public class TaskThread extends BaseThread {
    //缺少service的配置文件
	public synchronized void run()  {
		try{
			logger.info("----SkyFormOpt TaskThread Wait 30 second----");
			Thread.sleep(30000);
		}catch(InterruptedException e){
			
		}
		while (true) {
			List<AsyncJobInfo> result = new ArrayList<AsyncJobInfo>();
			try{
				result =  taskQueue.popedItem();
				boolean taskIsOver = false;
				for(AsyncJobInfo jobInfo : result){//循环任务数组,区分是调用elaster接口任务还是调用webService接口
					logger.info("parameter : " + jobInfo.getPARAMETER()+" operation : "+jobInfo.getOPERATION()+"result.size"+result.size());
					if((jobInfo.getOPERATION().equals(TaskContext.OperateType.MCREBOOTREQ.getDesc())||
							jobInfo.getOPERATION().equals(TaskContext.OperateType.MCRESUMEREQ.getDesc())||
							jobInfo.getOPERATION().equals(TaskContext.OperateType.MCSTARTREQ.getDesc())||
							jobInfo.getOPERATION().equals(TaskContext.OperateType.MCSTOPREQ.getDesc())||
							jobInfo.getOPERATION().equals(TaskContext.OperateType.MCSUSPENDREQ.getDesc()))
							&& jobInfo.getRESID()==0){
						this.executeMiniComputerJobInfo(jobInfo);
					}else {
					  //fix bug 005244
					  if(taskIsOver){
              jobInfo.setJOBID(0);
              jobInfo.setRESID(-1);
              jobInfo.setJOBSTATE(TaskContext.JobStatus.END.getCode());
              getAsyncJobService().updateResIdAndStateByJobInfo(jobInfo);
              continue;
            }
						int[] resultId = this.executeElasterJobInfo(jobInfo);
						 if (resultId[1] == 0) {// 任务没有执行完成，后续任务退出，等待下次轮训
	              break;
						 }else if(resultId[1] == -1){
						   taskIsOver = true;
						   continue;
						 }
					}
				}
			}catch(Exception e){
				e.printStackTrace();
				logger.error("TaskThread Exception：",e);
			}finally{
				for(AsyncJobInfo jobInfo : result){
					ConstDef.ASYNCJOBINFO_MAP.remove(jobInfo.getID());//从内存中删除该消息的任务
				}
			}
		}
	}
}
