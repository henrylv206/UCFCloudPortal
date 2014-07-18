package com.skycloud.management.portal.common.utils;

import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.front.task.queue.TaskContext;
import com.skycloud.management.portal.webservice.databackup.po.BaseDataBackUpResponse;
import com.skycloud.management.portal.webservice.databackup.po.DataBackUpJobIdResponse;
import com.skycloud.management.portal.webservice.databackup.po.DataBackUpSizeResponse;
import com.skycloud.management.portal.webservice.databackup.utils.UUIDGeneratorUtil;

public class BaseService {
	protected  final Logger  logger = Logger.getLogger(BaseService.class);
	
	/**
	 * 判断操作是否是小机停止操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-13  下午01:41:02
	 */
	public boolean isMcStopOperate (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.MCSTOPREQ.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否是虚机暂停操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-13  下午01:42:30
	 */
	public boolean isVmStopOperate (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.STOPVIRTUALMACHINE.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否是删除快照
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-24  上午10:53:28
	 */
	public boolean isDeleteSnapshotOperate (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.DELETSNAPHOT.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否是销毁虚机操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-24  上午10:53:57
	 */
	public boolean isDeleteVmOperate (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.DESTORYVIRTUALMACHINE.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否是挂接卷操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-13  下午01:44:35
	 */
	public boolean isAttachVolume (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.ATTACHVOLUME.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否卸载卷操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-13  下午01:45:44
	 */
	public boolean isDetachVolume (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.DETACHVOLUME.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 判断操作是否是创建卷操作
	 * @param operateValue
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2011-12-22  下午01:22:36
	 */
	public boolean isCreateVolume (String operateValue){
		if(operateValue.equals(TaskContext.OperateType.CREATEVOLUME.getDesc()))
			return true;
		else 
			return false;
	}
	/**
	 * 根据操作类型获取Json解析结果对象名
	 * @param operation
	 * @return
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-23  下午05:26:55
	 */
	public  String getJsonResultName (String operation){
		if(operation.equals(TaskContext.OperateType.ATTACHVOLUME.getDesc())||
		   operation.equals(TaskContext.OperateType.DETACHVOLUME.getDesc())||
		   operation.equals(TaskContext.OperateType.CREATEVOLUME.getDesc())){
			return "volume";
		}else if(operation.equals(TaskContext.OperateType.REBOOTROUTER.getDesc())){
			return "domainrouter";
		}else if (operation.equals(TaskContext.OperateType.CREATESNAPSHOT.getDesc())){
			return "snapshot";
		}else if (operation.equals(TaskContext.OperateType.DELETSNAPHOT.getDesc())){
			return "deleteSnapshot";
		}else{
			return "virtualmachine";
		}
	}
	
	public boolean isSuccess (int resId){
		if(resId>0){
			return true;
		}
		return false;
	}
	/**
	 * 是否是系统盘恢复
	 * @param type
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午11:17:38
	 */
	public boolean isResumeSystemDisk (int type){
		if(type == TaskContext.ResumeType.RESUMESYSTEMDISK.getCode()){
			return true;
		}
		return false;
	}
	/**
	 * 是否块存储恢复
	 * @param type
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午11:19:12
	 */
	public boolean isResumeOtherDisk (int type){
		if(type == TaskContext.ResumeType.RESUMEOTHERDISK.getCode()){
			return true;
		}
		return false;
	}
	/**
	 * 是否是根据快照创建新的虚拟机
	 * @param type
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  上午11:20:46
	 */
	public boolean isResumeNewVm (int type){
		if(type == TaskContext.ResumeType.RESUMENEWVM.getCode()){
			return true;
		}
		return false;
	}
	/**
	 * 产生任务查询标识
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-1-10  下午01:51:51
	 */
	public String getJobUUID (){
		return  UUIDGeneratorUtil.getUUID();
	}
	/**
	 * 参数缺失返回错误信息
	 * @return
	 * 创建人：  刘江宁    
	 * 创建时间：2012-2-1  上午10:56:58
	 */
	protected Response responseOutFailByParamer(){
		BaseDataBackUpResponse response = new BaseDataBackUpResponse ();
		response.setResCode("90000001");
		response.setMsg("参数缺失或者填写不正确");
		return Response.ok(response).build();
	}
	/**
	 * 输出自定义的失败信息
	 * @param msg
	 */
	protected Response responseOutFail(String msg){
		BaseDataBackUpResponse response = new BaseDataBackUpResponse ();
		response.setResCode("11111111");
		response.setMsg(msg);
		return Response.ok(response).build();
	}
	protected Response responseOutFail(){
		return responseOutFail("失败");
	}
	protected Response responseOutOperation(){
		return responseOutSuccess("正在执行");
	}
	protected Response responseOutOperating(String msg){
		BaseDataBackUpResponse response = new BaseDataBackUpResponse ();
		response.setResCode("22222222");
		response.setMsg(msg);
		return Response.ok(response).build();
	}
	/**
	 * 输出自定义的成功信息
	 * @param msg
	 */
	protected Response responseOutSuccess(String msg){
		BaseDataBackUpResponse response = new BaseDataBackUpResponse ();
		response.setResCode("00000000");
		response.setMsg(msg);
		return Response.ok(response).build();
	}
	protected Response responseOutSuccessJobId(String id){
		DataBackUpJobIdResponse response = new DataBackUpJobIdResponse ();
		response.setResCode("00000000");
		response.setMsg("成功");
		response.setId(id);
		return Response.ok(response).build();
		//return "{\"code\":1,\"id\":\""+id+"\",\"msg\":\"成功\"}";
	}
	protected Response responseOutSuccessJobIdSize(int id , int size){
		DataBackUpSizeResponse response = new DataBackUpSizeResponse ();
		response.setResCode("00000000");
		response.setMsg("成功");
		response.setId(id);
		response.setSize(size);
		return Response.ok(response).build();
	}
	/**
	 * 输出'操作成功'固定成功信息
	 */
	protected Response responseOutSuccess(){
		return responseOutSuccess("成功");
	}
}
