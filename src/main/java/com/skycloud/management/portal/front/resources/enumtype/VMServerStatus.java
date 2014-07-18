package com.skycloud.management.portal.front.resources.enumtype;


public enum VMServerStatus{
	STOP("0",5),RUNNING("1",2),NEW("2",3),UPDATE("3",3),DELETE("4",4);
	private String name;  
    private int index;  
    // 构造方法  
    private VMServerStatus(String name, int index) {  
        this.name = name;  
        this.index = index;  
    }  
    // 普通方法  
    public static String getName(int index) {  
        for (VMServerStatus c : VMServerStatus.values()) {  
            if (c.getIndex() == index) {  
                return c.name;  
            }  
        }  
        return null;  
    } 
    public static int getIndex(String name){
    	for (VMServerStatus c : VMServerStatus.values()) {  
            if (name.equals(c.getName())) {  
                return c.index;  
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
