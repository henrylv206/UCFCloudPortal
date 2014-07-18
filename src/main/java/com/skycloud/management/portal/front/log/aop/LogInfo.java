package com.skycloud.management.portal.front.log.aop;

import java.lang.annotation.Documented;   
import java.lang.annotation.ElementType;   
import java.lang.annotation.Retention;   
import java.lang.annotation.RetentionPolicy;   
import java.lang.annotation.Target;   
  
@Target({ElementType.METHOD})   
@Retention(RetentionPolicy.RUNTIME)   
@Documented  
public @interface LogInfo{  
	/**
	 * 作用：参数值与功能的关系描述
	 * 比如描述参数的值与模块功能的关系
	 * @return
	 */
    String desc() default "无描述信息"; 
    /**
     * 模块名称
     * @return
     */
    String moduleName() default "无模块信息"; 
    /**
     * 功能名称
     * @return
     */
    String functionName() default "无功能信息"; 
    
    /**
     * 作用：操作类型，用整数表示
     * 1:insert,2:delete,3:update,4:select
     * @return
     */
    int    operateType()  default 4;
    
    /**
     * 作用：参数名称。用  ‘,‘分隔
     * 如：parameters="para1,para2,para3"
     * @return
     */
    String parameters() default "无参数信息";
    
    /**
	 * 作用：备注信息
	 * 保存备注说明信息
	 * @return
	 */
    String memo() default "备注信息"; 
}  
