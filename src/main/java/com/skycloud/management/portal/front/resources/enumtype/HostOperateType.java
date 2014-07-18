package com.skycloud.management.portal.front.resources.enumtype;


public enum HostOperateType{
	ADD("1",1),EDIT("2",2),DEPLOY("3",3),DELETE("4",4),START("5",5),STOP("6",6),RESET("7",7),POWER_STATE("8",8);
	private String name;  
    private int index;  
    // 构造方法  
    private HostOperateType(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (HostOperateType optype : HostOperateType.values()) {  
            if (optype.getIndex() == index) {  
                return optype.name;  
            }  
        }  
        return null;  
    } 
    public static int getIndex(String name){
    	for (HostOperateType optype : HostOperateType.values()) {  
            if (name.equals(optype.getName())) {  
                return optype.index;  
            }  
        }  
        return -1;  
    }
    // get set 方法  
    public String getName() {  
        return name;  
    }  
    public void setName(String name) {  
        this.name = name;  
    }  
    public int getIndex() {  
        return index;  
    }  
    public void setIndex(int index) {  
        this.index = index;  
    }      
}
