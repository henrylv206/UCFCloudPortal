package com.skycloud.management.portal.front.task.util;

public interface OperationTypeDefine {

    public static final int startVm = 0; //开启虚机标识
	
	public static final int stopVm =  1; //关闭虚机
	
	public static final int changeVm = 2; //修改虚机
	
	public static final int attachVolume = 3; // 挂接卷
	
	public static final int rebootVm = 4;    //重启虚机
	
	public static final int destoryVm = 5; //销毁虚机
	
	public static final int detachVolume = 6; //卸载卷
	
	public static final int createSnapshot = 7;//创建快照
	
	public static final int rebootRebouter = 8;//重启路由
	
	public static final int createVolume = 9;//创建卷
	
	public static final int mcstartReq = 10; //开启小机
	
	public static final int mcstopReq = 11;//停止小机
	
	public static final int mcsuspendReq = 12;//小机暂停
	
	public static final int mcrebootReq = 13;//小机重启
	
	public static final int mcresumeReq = 14;//小机恢复
	
	public static final int deleteSnaphot = 15;//删除快照
	
	public static final int attachIso = 16;//挂载光盘
	
	public static final int detachIso = 17;//卸载光盘
	
	public static final int deleteVolume = 18;//删除卷
}
