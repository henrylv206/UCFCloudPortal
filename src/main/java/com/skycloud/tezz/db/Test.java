package com.skycloud.tezz.db;

import java.sql.Timestamp;
import java.util.Date;
import java.util.GregorianCalendar;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String date="2012-12-05";
//		String[] ds=date.split("-");
//		GregorianCalendar gc=new GregorianCalendar(Integer.valueOf(ds[0]), Integer.valueOf(ds[1]), Integer.valueOf(ds[2]));
//		gc.add(GregorianCalendar.MONTH, -1);
//		
//		String m="";
//		int month=gc.get(GregorianCalendar.MONTH);
//		if(month<=1){
//			gc.add(GregorianCalendar.YEAR, -1);
//		}
//		String y=gc.get(GregorianCalendar.YEAR)+"";
//		m=month+"";
//		if(month==0){
//			month=12;
//			m=month+"";
//		}
//		if(month<10){
//			m="0"+month;
//		}
//		System.out.println(y+"-"+m+"-01");
		Timestamp t1= new Timestamp(new Date().getTime());
		Timestamp t2= new Timestamp(2000, 1, 1, 0, 0, 0, 0);
		System.out.println(Integer.parseInt(t1.toString().substring(0,4))>2000);
	}

}
