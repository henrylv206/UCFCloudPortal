package com.skycloud.management.portal.front.task.util;


import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import com.skycloud.management.portal.common.utils.JsonUtil;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.front.command.impl.QueryCommand;
import com.skycloud.management.portal.front.command.res.AttachIso;
import com.skycloud.management.portal.front.command.res.AttachVolume;
import com.skycloud.management.portal.front.command.res.ChangeServiceForVirtualMachine;
import com.skycloud.management.portal.front.command.res.CreateSnapshot;
import com.skycloud.management.portal.front.command.res.CreateVolume;
import com.skycloud.management.portal.front.command.res.DeleteSnapshot;
import com.skycloud.management.portal.front.command.res.DeleteVolume;
import com.skycloud.management.portal.front.command.res.DestroyVirtualMachine;
import com.skycloud.management.portal.front.command.res.DetachIso;
import com.skycloud.management.portal.front.command.res.DetachVolume;
import com.skycloud.management.portal.front.command.res.McResumeRequetCommandPo;
import com.skycloud.management.portal.front.command.res.McStartRequestCommandPo;
import com.skycloud.management.portal.front.command.res.McStopRequestCommandPo;
import com.skycloud.management.portal.front.command.res.McSuspendRequestCommandPo;
import com.skycloud.management.portal.front.command.res.McrebootRequestCommandPo;
import com.skycloud.management.portal.front.command.res.RebootVirtualMachine;
import com.skycloud.management.portal.front.command.res.StartVirtualMachine;
import com.skycloud.management.portal.front.command.res.StopVirtualMachine;
import com.skycloud.management.portal.front.task.queue.TaskContext;
import net.sf.json.JSON;
import net.sf.json.JSONSerializer;
import net.sf.json.JsonConfig;

public abstract class BaseCommandPo  extends QueryCommand implements OperationTypeDefine{
	
	public BaseCommandPo (String OperationValue){
		super(OperationValue);
	}
	
	public static Logger logger =  Logger.getLogger(BaseCommandPo.class);
	/**
	 * 获取AsycnJobInfo表的operate字段值
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-28  下午04:17:54
	 */
	public abstract String getCOMMAND ();
	/**
	 * 获取AsycnJobInfo表的parameter字段值
	 * @param po
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-28  下午04:18:09
	 */
	public abstract String getParameter (BaseCommandPo po);
	
	/**
	 * 将Json串转换为操作对象
	 * @param jsonStr
	 * @param OperationValue
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-22  上午10:44:10
	 */
	protected abstract QueryCommand fromJsonToOperatePo (String jsonStr);
	
	static BaseCommandPo newCommandObject (String operationValue){
		
		switch (getOperateValeFromOperationStr (operationValue)) {
		case startVm:
			return new StartVirtualMachine();
		case stopVm:
			return new StopVirtualMachine ();
		case changeVm:
			return new ChangeServiceForVirtualMachine ();
		case attachVolume:
			return new AttachVolume ();
		case rebootVm:
			return new RebootVirtualMachine ();
		case destoryVm:
			return new DestroyVirtualMachine ();
		case detachVolume :
			return new DetachVolume ();
		case createSnapshot:
			return new CreateSnapshot ();
		case createVolume:
			return new CreateVolume();
		case deleteVolume:
      return new DeleteVolume();
		case mcrebootReq:
			return new McrebootRequestCommandPo();
		case mcresumeReq:
			return new McResumeRequetCommandPo();
		case mcsuspendReq:
			return new McSuspendRequestCommandPo();
		case mcstartReq:
			return new McStartRequestCommandPo();
		case mcstopReq:
			return new McStopRequestCommandPo();
		case deleteSnaphot:
			return new DeleteSnapshot();
		case attachIso:
			return new AttachIso();
		case detachIso:
			return new DetachIso();
		default:
			throw new IllegalArgumentException("Incorrect operation Code");
		}
	}
	
	private static int getOperateValeFromOperationStr (String operatType){
		 try{
			   if(operatType.equals(TaskContext.OperateType.STARTVIRTUALMACHINE.getDesc())){//开启虚机
				   return startVm;
			   }else if(operatType.equals(TaskContext.OperateType.STOPVIRTUALMACHINE.getDesc())){//停止虚机
				   return stopVm;
			   }else if(operatType.equals(TaskContext.OperateType.CHANGESERVICEFORVIRTUALMACHINE.getDesc())){//修改虚机
				   return changeVm;
			   }else if(operatType.equals(TaskContext.OperateType.CREATESNAPSHOT.getDesc())){//创建快照
				   return createSnapshot;
			   }else if(operatType.equals(TaskContext.OperateType.DESTORYVIRTUALMACHINE.getDesc())){//注销虚机
				   return destoryVm;
			   }else if(operatType.equals(TaskContext.OperateType.DETACHVOLUME.getDesc())){//卸载硬盘
				   return detachVolume;
			   }else if(operatType.equals(TaskContext.OperateType.REBOOTVIRTUALMACHINE.getDesc())){//重启虚机
				   return rebootVm;
			   }else if(operatType.equals(TaskContext.OperateType.ATTACHVOLUME.getDesc())){//挂接卷 卷分配
				   return attachVolume;
			   }else if(operatType.equals(TaskContext.OperateType.CREATEVOLUME.getDesc())){//创建卷  卷恢复
				   return createVolume;
			   }else if(operatType.equals(TaskContext.OperateType.DELETEVOLUME.getDesc())){//删除卷  卷恢复
           return deleteVolume;
         }else if(operatType.equals(TaskContext.OperateType.MCREBOOTREQ.getDesc())){//小机重启
				   return mcrebootReq;
			   }else if(operatType.equals(TaskContext.OperateType.MCRESUMEREQ.getDesc())){//小机恢复
				   return mcresumeReq;
			   }else if(operatType.equals(TaskContext.OperateType.MCSTARTREQ.getDesc())){//小机开启
				   return mcstartReq;
			   }else if(operatType.equals(TaskContext.OperateType.MCSTOPREQ.getDesc())){//小机停止
				   return mcstopReq;
			   }else if(operatType.equals(TaskContext.OperateType.MCSUSPENDREQ.getDesc())){//小机暂停
				   return mcsuspendReq;
			   } else if(operatType.equals(TaskContext.OperateType.DELETSNAPHOT.getDesc())){//删除快照
				   return deleteSnaphot;
			   } else if(operatType.equals(TaskContext.OperateType.ATTACHISO.getDesc())){//挂载光盘
				   return attachIso;
			   }else if(operatType.equals(TaskContext.OperateType.DETACHISO.getDesc())){//卸载光盘
				   return detachIso;
			   } else{
				   throw new IllegalArgumentException("Incorrect parameter value：" +operatType);
			   }
		   }catch(Exception e){
			   throw new RuntimeException("get operater value is exception："+e.getMessage());
		   }
	}
	/**
	 * 将对象转换成Json串，属性为空时不显示该key值
	 * @param po
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午09:47:57
	 */
	protected String fromPoToJsonStr (BaseCommandPo po){
		try{
			JsonConfig jsonConfig =  new JsonConfig();
			jsonConfig.setExcludes(new String[]{"COMMAND","commandName","deviceid","keyValues","page","pagesize","parameters","uriParams"});
			Map<String, Object> tempMap = new HashMap<String, Object>();
			Map<String, String> objecttMap = BeanUtils.describe(po);
			Set<String> keySet = objecttMap.keySet();
	        for (Iterator it = keySet.iterator(); it.hasNext();) {
	            String key = (String) it.next();
	            String value = objecttMap.get(key);
	            if(StringUtils.isNotBlank(value)){
	            	if(StringUtils.isNumeric(value)&&Integer.valueOf(value)==0){
	            		continue;
	            	}
	            	tempMap.put(key, value);
	            }
	        }
	        Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(po.getCOMMAND(), tempMap);
			JSON json =  JSONSerializer.toJSON(resultMap,jsonConfig);
		return json.toString();
		}catch(Exception e){
			throw new ServiceException ("[BaseCommandPo]Method:fromPoToJsonStr Exception：",e);
		}
	}
	/**
	 * 将对象转换成Json串，属性为空时显示该key值
	 * @param po
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午09:47:57
	 */
	protected String fromPoToJsonStrSupportValueIsNull (BaseCommandPo po){
		try{
			JsonConfig jsonConfig =  new JsonConfig();
			jsonConfig.setExcludes(new String[]{"COMMAND","commandName","deviceid","keyValues","page","pagesize","parameters","uriParams"});
	        Map<String, Object> resultMap = new HashMap<String, Object>();
			resultMap.put(po.getCOMMAND(), BeanUtils.describe(po));
			JSON json =  JSONSerializer.toJSON(resultMap,jsonConfig);
		return json.toString();
		}catch(Exception e){
			throw new ServiceException ("[BaseCommandPo]Method:fromPoToJsonStr Exception：",e);
		}
	}
	protected Map<String,Object> fromJsonToMap (String jsonString ){
		Map<String,Object> packetOBMap = JsonUtil.getMap4Json(jsonString);
		return packetOBMap;
	}
}
