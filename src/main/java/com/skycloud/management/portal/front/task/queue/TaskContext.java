package com.skycloud.management.portal.front.task.queue;


public class TaskContext {
	/**
	 * 小机运行结果说明定义
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-24  下午05:04:35
	 */
	 public static enum STATUS_ASYNCJOB {
		    PENDING(0,"等待运行"), SUCCESS(1,"运行成功"), FAILURE(2,"运行失败"), RUNNING(3,"正在运行");
		    private int code;

			private String  desc;
			
		    private STATUS_ASYNCJOB(int code, String desc) {
		    	this.code = code;
				this.desc = desc;
		    }
		    public int getCode() {
				return code;
			}

			public String getDesc() {
				return desc;
			}
   }
	 /**
	  * 操作类型说明定义
	  * 创建人：  刘江宁   
	  * 创建时间：2011-11-24  下午05:04:20
	  */
	public static enum OperateType {
		STARTVIRTUALMACHINE("startVirtualMachine","开启虚机"),STOPVIRTUALMACHINE("stopVirtualMachine","停止虚机"),
		CHANGESERVICEFORVIRTUALMACHINE("changeServiceForVirtualMachine","修改虚机"),ATTACHVOLUME("attachVolume","挂接卷"),
		REBOOTVIRTUALMACHINE("rebootVirtualMachine","重启虚机"),DESTORYVIRTUALMACHINE("destroyVirtualMachine","销毁虚机"),
		DETACHVOLUME("detachVolume","卷撤销"),CREATESNAPSHOT("createSnapshot","创建快照"),MCREBOOTREQ("mcrebootReq","重启小机"),
		MCSTOPREQ("mcstopReq","停止小机"),MCSTARTREQ("mcstartReq","开启小机"),MCRESUMEREQ("mcresumeReq","恢复小机"),
		MCSUSPENDREQ("mcsuspendReq","暂停小机"),MCQUERYREQ("mcqueryReq","小机结果查询"),REBOOTROUTER("rebootRouter","重启路由"),
		CREATEVOLUME("createVolume","创建卷"),DELETEVOLUME("deleteVolume","删除卷"),DELETSNAPHOT("deleteSnapshot","删除快照"),ATTACHISO("attachIso","挂载光盘"),DETACHISO("detachIso","卸载光盘");

		private String  desc;
		
		private String code;
		
		OperateType(String desc, String code) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	
	/**
	 * 任务处理结果说明定义
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-24  下午05:04:52
	 */
	public static enum ResultStatus {
		OPERATING("0","处理中"),OPERATSUCCESS("1","处理成功"),OPERAFAIL("2","处理失败");
		private String code;

		private String  desc;
		
		ResultStatus(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	/**
	 * Elaster服务端返回结果说明定义
	 * 创建人：  刘江宁   
	 * 创建时间：2011-11-24  下午05:05:34
	 */
	public static enum JobStatus {
		START(1,"未完成"),END(2,"完成"),OPERATING(3,"正在处理中"),CALLBACK(4,"已回收"),TURNOFF(5,"关机"),RUNNING(6,"命令正在执行中");
		private int code;

		private String  desc;
		
		JobStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	public static enum VolumeStatus {
		ATTACHSUCCESS(2,"已经装载"),DETACHSUCCESS(4,"已经卸载"),ATTACHFAIL(6,"装载失败"),DETACHFAIL(7,"卸载失败");
		private int code;

		private String  desc;
		
		VolumeStatus(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	public static enum ResumeType {
		RESUMESYSTEMDISK(1,"系统盘恢复"),RESUMEOTHERDISK(2,"块存储恢复"),RESUMENEWVM(3,"创建新的虚拟机");
		private int code;

		private String  desc;
		
		ResumeType(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
	public static enum Status {
		USE(0,"可用"),UNUSE(1,"不可用"),DELETE(2,"已删除");
		private int code;

		private String  desc;
		
		Status(int code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public String getDesc() {
			return desc;
		}
	}
}
