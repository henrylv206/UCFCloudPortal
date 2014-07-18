package com.skycloud.management.portal.front.task.queue;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.webservice.databackup.po.DBListVirtualMachineJsonPO;
import com.skycloud.management.portal.webservice.databackup.po.DbVirtumalMachinePo;
import com.skycloud.management.portal.webservice.databackup.utils.JacksonUtils;
/**
 * 服务端结果分析工具类
  *<dl>
  *<dt>类名：CommonUtil</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2011-12-13  下午02:16:53</dd>
  *<dd>创建人： 刘江宁</dd>
  *</dl>
 */
public class CommonUtil {
	
	public static Logger logger =  Logger.getLogger(CommonUtil.class);
	
	private static final String resultQueryName = "queryasyncjobresult";
	
	
	/**
	 * 将返回对象转换为json串返回
	 * @param response
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-22  下午05:59:02
	 */
	public static String fromResponseObjectToStr (Object response){
		JSONObject jsonRes = JSONObject.fromObject(response);
		return jsonRes.toString(); 	
	}
	 /**
	    * 从执行结果中获取JobId
	    * @param response
	    * @return
	    * 创建人：  刘江宁   
	    * 创建时间：2011-11-22  下午03:26:32
	    */
	  public static String getJobIdFromExecuteResponse (Object response,String name){
		  try{
			    String responsename = getRootResponseStr(response,name);
			    String jobId = CommonUtil.fromJsonToKeyValue(responsename, "jobid");
			    if (StringUtils.isBlank(jobId)) {
					logger.error("Job Error：JobId is Null");
					throw new RuntimeException("Job Error：JobId is Null");
				}
			    return jobId;
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getJobIdFromExecuteResponse Exception：",e);
			  throw new RuntimeException("[CommonUtil]Method:getJobIdFromExecuteResponse Exception："+e.getMessage());
		  }
	  }
	  
	  /**
	   * 获取执行结果状态值
	   * @param response
	   * @return
	   * 创建人：  刘江宁   
	   * 创建时间：2011-11-22  下午04:01:12
	   */
	  public static String getResponseResultStatus (Object response){
		  try{
			    String responsename = getRootResponseStr(response,resultQueryName);
			    return CommonUtil.fromJsonToKeyValue(responsename, "jobstatus");
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getResponseResultStatus Exception：",e);
				throw new RuntimeException("[CommonUtil]Method: getResponseResultStatus Exception："+e.getMessage());
		  }
	  }
	
	  /**
	   * 从查询返回结果中获取resId
	   * @param response
	   * @param operateTypeName
	   * @return
	   * 创建人：  刘江宁   
	   * 创建时间：2011-11-22  下午03:53:41
	   */
	  public static String getResIdFromExecuteResponse (Object response, String operateTypeName){
		  try{
			   if(operateTypeName.equals(TaskContext.OperateType.DELETSNAPHOT.getDesc())){
				   logger.info("快照删除返回Boolean变量，不用取ResId");
				   String responsenameStr = getRootResponseStr(response,resultQueryName);
				   String jobresult = fromJsonToKeyValue(responsenameStr, "jobresult");
				   String success = fromJsonToKeyValue(jobresult, "success");
				   if(isSuccess(success)){
					   return "0";
				   }
					 return "-1";
				}
			    String responsenameStr = getRootResponseStr(response,resultQueryName);
			    String jobresult = fromJsonToKeyValue(responsenameStr, "jobresult");
			    String virtualmachine = fromJsonToKeyValue(jobresult, operateTypeName);
			    return CommonUtil.fromJsonToKeyValue(virtualmachine, "id");
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getResIdFromExecuteResponse Exception：",e);
				throw new RuntimeException("[CommonUtil]Method: getResIdFromExecuteResponse Exception："+e.getMessage());
		  }
	  }
	  /**
	   * 获取ISO标识
	   * @param response
	   * @param operateTypeName
	   * @return
	   * 创建人：  刘江宁    
	   * 创建时间：2012-1-4  上午11:19:13
	   */
	  public static String getIsoResIdFromExecuteResponse (Object response, String operateTypeName){
		  try{
			    String responsenameStr = getRootResponseStr(response,resultQueryName);
			    String jobresult = fromJsonToKeyValue(responsenameStr, "jobresult");
			    String virtualmachine = fromJsonToKeyValue(jobresult, operateTypeName);
			    return CommonUtil.fromJsonToKeyValue(virtualmachine, "isoid");
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getResIdFromExecuteResponse Exception：",e);
				throw new RuntimeException("[CommonUtil]Method: getResIdFromExecuteResponse Exception："+e.getMessage());
		  }
	  }
	  private static boolean isSuccess (String str){
		  if(str.equals("true")){
			  return true;
		  }
		  return false;
	  }
	  /**
	   * 修改虚机获取ResId。该任务是同步任务所以处理逻辑不同于其他任务
	   * @param response
	   * @param operateTypeName
	   * @return
	   * 创建人：  刘江宁   
	   * 创建时间：2011-11-22  下午05:22:29
	   */
	  public static String getResIdFromExecuteChangeVmResponse (Object response, String operateTypeName){
		  try{
			    String responsenameStr = getRootResponseStr(response,operateTypeName);
			    String virtualmachine = fromJsonToKeyValue(responsenameStr, "virtualmachine");
			    return CommonUtil.fromJsonToKeyValue(virtualmachine, "id");
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getResIdFromExecuteChangeVmResponse Exception：",e);
				throw new RuntimeException("[CommonUtil]Method: getResIdFromExecuteChangeVmResponse Exception："+e.getMessage());
		  }
	  }
	  /**
	   * 获取json串指定属性的参数值,Json串中必须还有数组否则报错
	   * @param response
	   * @param commandName
	   * @param resultName
	   * @param attributeName
	   * @return
	   * 创建人：  刘江宁    
	   * 创建时间：2012-1-9  上午10:06:51
	   */
	  public static String getJsonAttributeFromResponse (Object response, String commandName,String resultName , String attributeName){
		  try{
			    String responsenameStr = getRootResponseStr(response,commandName);
			    String virtualmachine = fromJsonToKeyValue(responsenameStr, resultName).replace("[", "").replace("]", "");
			    return CommonUtil.fromJsonToKeyValue(virtualmachine, attributeName);
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getSnapshotListVolumeIdFromResponse Exception：",e);
				throw new RuntimeException("[CommonUtil] Method: getSnapshotListVolumeIdFromResponse Exception："+e.getMessage());
		  }
	  }
	  /**
	   * 根据对象名解析获取Json串中包含数组
	   * @param response
	   * @param commandName
	   * @param objectName
	   * @return
	   * 创建人：  刘江宁    
	   * 创建时间：2012-1-12  下午02:21:20
	   */
	  public static String getJsonArrayToObjectStr (Object response, String commandName,String objectName ){
		  try{
			    String responsenameStr = getRootResponseStr(response,commandName);
			    String objectStr = fromJsonToKeyValue(responsenameStr, objectName).replace("[", "").replace("]", "");
			    return objectStr;
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getJsonArrayToObjectStr Exception：",e);
				throw new RuntimeException("[CommonUtil] Method: getJsonArrayToObjectStr Exception："+e.getMessage());
		  }
	  }
	  public static DbVirtumalMachinePo getJsonArrayToObject (Object response, String commandName,String objectName ){
		  try{
			    String responsenameStr = getRootResponseStr(response,commandName);
			    String objectStr = fromJsonToKeyValue(responsenameStr, objectName);
			    objectStr = objectStr.substring(1,objectStr.length()-1);
			    DbVirtumalMachinePo virtualMachine =(DbVirtumalMachinePo)JacksonUtils.fromJson(objectStr, DbVirtumalMachinePo.class) ;
				return virtualMachine;
			    
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getJsonArrayToObjectStr Exception：",e);
				throw new RuntimeException("[CommonUtil] Method: getJsonArrayToObjectStr Exception："+e.getMessage());
		  }
	  }
	  /**
	   * 根据对象名解析获取Json串
	   * @param response
	   * @param commandName
	   * @param resultName
	   * @return
	   * 创建人：  刘江宁    
	   * 创建时间：2012-1-9  下午01:40:34
	   */
	  public static String getJsonToObjectStr (Object response, String commandName,String objectName ){
		  try{
			    String responsenameStr = getRootResponseStr(response,commandName);
			    String objectStr = fromJsonToKeyValue(responsenameStr, objectName);
			    return objectStr;
		  }catch(Exception e){
			  logger.error("[CommonUtil]Method: getJsonToObjectStr Exception：",e);
				throw new RuntimeException("[CommonUtil] Method: getJsonToObjectStr Exception："+e.getMessage());
		  }
	  }
	 
	  public static Map<String, BigInteger> getMapBigInteger(Map<String, String> map) {
			String key = "";
			Iterator it = map.entrySet().iterator();
			Map<String, BigInteger> keyIter = new HashMap<String, BigInteger>();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				key = (String) entry.getKey();
				keyIter.put(key, new BigInteger(entry.getValue().toString()));
			}
			return keyIter;
		}

		public static Map<String, Integer> getMapInteger(Map<String, String> map) {
			String key = "";
			Iterator it = map.entrySet().iterator();
			Map<String, Integer> keyIter = new HashMap<String, Integer>();
			while (it.hasNext()) {
				Map.Entry entry = (Map.Entry) it.next();
				key = (String) entry.getKey();
				keyIter.put(key, Integer.valueOf(entry.getValue().toString()));
			}
			return keyIter;
		}
	/**
	 * 根据操作类型获取返回值名称
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-22  下午03:34:01
	 */
	private static String getResponseNameFromOpeateType (String operateType){
		String responseName = "response" ;
		operateType = operateType.toLowerCase();
		responseName = operateType + responseName;
		return responseName;
	}
	/**
	 * 根据key值获取value
	 * @param jsonString
	 * @param key
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-4  下午03:43:13
	 */
	public static String fromJsonToKeyValue (Object response ,String key) {
		try{
			String tempJson = "";
			if(response == null){
				throw new RuntimeException("[CommonUtil]Method： fromJsonToKeyValue Exception：parameter is Null");
			}
			JSONObject jsonRes = JSONObject.fromObject(response);
			logger.info("Elaster response : "+jsonRes.toString());
			tempJson = JsonUtil.getMap4Json(jsonRes.toString()).get(key).toString();
			if(StringUtils.isBlank(tempJson)){
				logger.error("[CommonUtil]Method: fromJsonToKeyValue Exception： "+key+" value is null" );
				throw new RuntimeException("[CommonUtil]Method: fromJsonToKeyValue Exception： "+key+" value is null");
			}
			return tempJson;
		}catch(Exception e){
			logger.error("[CommonUtil]Method: fromJsonToKeyValue Exception：",e);
			throw new RuntimeException("[CommonUtil]Method: fromJsonToKeyValue Exception："+e);
		}
	}
	public static String getRootResponseStr (Object response , String name){
		  return CommonUtil.fromJsonToKeyValue(response, getResponseNameFromOpeateType(name));
	  }
}
