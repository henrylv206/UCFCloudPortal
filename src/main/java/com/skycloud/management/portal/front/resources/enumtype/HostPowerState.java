package com.skycloud.management.portal.front.resources.enumtype;


public enum HostPowerState{
	NONE("None",1),ON("On",2),OFF("Off",3);
	private String name;  
    private int index;  
    // 构造方法  
    private HostPowerState(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (HostPowerState optype : HostPowerState.values()) {  
            if (optype.getIndex() == index) {  
                return optype.name;  
            }  
        }  
        return null;  
    } 
    public static int getIndex(String name){
    	for (HostPowerState optype : HostPowerState.values()) {  
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
