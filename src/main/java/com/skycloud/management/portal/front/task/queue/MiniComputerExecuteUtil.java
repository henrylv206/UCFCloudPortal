package com.skycloud.management.portal.front.task.queue;

import javax.ws.rs.core.MediaType;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.instance.entity.AsyncJobInfo;
import com.skycloud.management.portal.front.task.util.CommandCreateUtil;
import com.skycloud.management.rest.minicomputer.entity.MCAsyncJob;
import com.skycloud.management.rest.minicomputer.resource.model.MCOperateReq;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.api.json.JSONConfiguration;

/**
 * 小机任务执行工具类
 * 创建人：  刘江宁   
 * 创建时间：2011-11-24  上午11:20:35
 */
public class MiniComputerExecuteUtil  implements MiniComputerExecuteUrlDefine{
	
	    public static Logger logger =  Logger.getLogger(MiniComputerExecuteUtil.class);
	
	    private static ClientConfig config;
		
		private static Client client;
		
		private static WebResource service;
		
//		public static void main(String arg[]){
//			AsyncJobInfo jobInfo = new AsyncJobInfo ();
//			jobInfo.setOPERATION("mcrebootReq");
//			MiniComputerExecuteUtil.callingMiniComputerPostInterfaceGetResult(jobInfo);
//		}
		/**
		 * 通过Post方式调用小机接口
		 * @param jobInfo
		 * @return
		 * 创建人：  刘江宁   
		 * 创建时间：2011-11-24  下午01:45:15
		 */
		public static String callingMiniComputerPostInterfaceGetResult (AsyncJobInfo jobInfo){
			try{
				 init(jobInfo.getOPERATION());
				 MCOperateReq request = CommandCreateUtil.createMcoperateReq(jobInfo);
				 ClientResponse response = service.entity(request,MediaType.APPLICATION_JSON).post(ClientResponse.class);
				 JSONObject resultJosn = response.getEntity(JSONObject.class);
				 return resultJosn.toString();
			}catch(ServiceException e){
				logger.error("[MiniComputerExecuteUtil]Method: callingMiniComputerPostInterfaceGetResult Exception：",e);
				throw new ServiceException("[MiniComputerExecuteUtil]Method: callingMiniComputerPostInterfaceGetResult Exception："+e.getMessage());
			}
		}
		/**
		 * 通过GET方式调用小机接口，得到操作结果
		 * @param jobInfo
		 * @return
		 * 创建人：  刘江宁   
		 * 创建时间：2011-11-24  下午02:00:52
		 */
		public static MCAsyncJob callingMiniComputerGetIntanceGetResult (AsyncJobInfo jobInfo){
			try{
				MCAsyncJob mcasynJob = new MCAsyncJob ();
				init(REST_WEBSERVICE_QUEYURL+ "/" + jobInfo.getJOBID());
				ClientResponse response = service.type(MediaType.APPLICATION_JSON).get(ClientResponse.class);
			    JSONObject resultJosn = response.getEntity(JSONObject.class);
			    logger.debug("call miniApi get json："+resultJosn.toString());
			    mcasynJob = (MCAsyncJob)JsonUtil.getObject4JsonString(CommonUtil.fromJsonToKeyValue(resultJosn.toString(), "job"), MCAsyncJob.class);
			    return mcasynJob;
			}catch(ServiceException e){
				logger.error("[MiniComputerExecuteUtil]Method: callingMiniComputerGetIntanceGetResult Exception：",e);
				throw new ServiceException("[MiniComputerExecuteUtil]Method: callingMiniComputerGetIntanceGetResult Exception："+e.getMessage());
			}
		}
		/**
		 * 参数初始化
		 * @param operation
		 * 创建人：  刘江宁   
		 * 创建时间：2011-11-24  下午01:37:58
		 */
		private static void init(String operation) {
		    config = new DefaultClientConfig();
		    config.getFeatures().put(JSONConfiguration.FEATURE_POJO_MAPPING, true);
		    client = Client.create(config);
		    String url = getUrl(operation);
		    logger.debug("小机请求调接口地址："+url);
		    service = client.resource(url);
	    }
		/**
		 * 根据小机操作类型获取对应的url操作地址
		 * @param operation
		 * @return
		 * 创建人：  刘江宁   
		 * 创建时间：2011-11-24  上午11:10:59
		 */
		private static String getUrl (String operation){
			if(operation.equals(TaskContext.OperateType.MCREBOOTREQ.getDesc())){
				return REST_WEBSERVICE_REBOOTURL;
			}else if(operation.equals(TaskContext.OperateType.MCRESUMEREQ.getDesc())){
				return REST_WEBSERVICE_RESUMEURL;
			}else if(operation.equals(TaskContext.OperateType.MCSTARTREQ.getDesc())){
				return REST_WEBSERVICE_STARTURL;
			}else if(operation.equals(TaskContext.OperateType.MCSTOPREQ.getDesc())){
				return REST_WEBSERVICE_STOPURL;
			}else if(operation.equals(TaskContext.OperateType.MCSUSPENDREQ.getDesc())){
				return REST_WEBSERVICE_SUSPENDURL;
			}else{
				return operation;
			}
		}
		
}
