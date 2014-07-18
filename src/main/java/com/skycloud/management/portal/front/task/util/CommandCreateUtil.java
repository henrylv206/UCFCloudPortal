package com.skycloud.management.portal.front.task.util;

import org.apache.log4j.Logger;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.command.res.QueryAsyncJobResult;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.rest.minicomputer.resource.model.MCOperateReq;

public class CommandCreateUtil {

	public static Logger logger = Logger.getLogger(CommandCreateUtil.class);
			
	private static BaseCommandPo commpo ;
	
	/**
	 * 构建Elaster请求任务对象
	 * @param jobInfo
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-24  下午05:03:48
	 */
	public static QueryCommand buildOperatePo (AsyncJobInfo jobInfo){
		commpo = commpo.newCommandObject(jobInfo.getOPERATION());
		return commpo.fromJsonToOperatePo(jobInfo.getPARAMETER());
	}
	/**
	 * 返回命令对象的Json串
	 * @param commandPo
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-28  下午06:11:13
	 */
	public static String getJsonParameterStr (BaseCommandPo commandPo){                           
		return commandPo.getParameter(commandPo);
	}
	
	/**
	 * 构建小机对象
	 * @param jobInfo
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午02:18:25
	 */
	public static MCOperateReq createMcoperateReq (AsyncJobInfo jobInfo){
		 try{
			   if(jobInfo == null){
					throw new RuntimeException("[CommandCreateUtil]Method createMcoperateReq Exception：parameter is null");
				}
			   String tempJson = "";
			   tempJson = JsonUtil.getMap4Json(jobInfo.getPARAMETER()).get(jobInfo.getOPERATION()).toString();
			   MCOperateReq mcrequest = (MCOperateReq)JsonUtil.getObject4JsonString(tempJson, MCOperateReq.class);
			   return mcrequest;
		   }catch(Exception e){
			   logger.error("[CommandCreateUtil]Method createMcoperateReq Exception：",e);
			   throw new RuntimeException("[CommandCreateUtil]Method createMcoperateReq Exception："+e);
		   }
	}
	
	/**
	 * 根据JobId构建结果查询对象 
	 * @param jobId
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-4  下午05:06:08
	 */
	public static QueryAsyncJobResult createQueryAsyncJobResultPo (String jobId){
		return new QueryAsyncJobResult(Long.valueOf(jobId));
	}
}
