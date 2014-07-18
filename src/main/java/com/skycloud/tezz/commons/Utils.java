package com.skycloud.tezz.commons;

import java.util.GregorianCalendar;
/**
 * 得到上个月的第一天
 * @author qms
 *
 */
public class Utils {
	public static String getLastDate(String date){
		String[] ds=date.split("-");
		GregorianCalendar gc=new GregorianCalendar(Integer.valueOf(ds[0]), Integer.valueOf(ds[1]), Integer.valueOf(ds[2]));
		gc.add(GregorianCalendar.MONTH, -1);
		
		String m="";
		int month=gc.get(GregorianCalendar.MONTH);
		if(month<=1){
			gc.add(GregorianCalendar.YEAR, -1);
		}
		String y=gc.get(GregorianCalendar.YEAR)+"";
		m=month+"";
		if(month==0){
			month=12;
			m=month+"";
		}
		if(month<10){
			m="0"+month;
		}
		return y+"-"+m+"-01";
	}
}
