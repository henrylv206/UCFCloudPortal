package com.skycloud.management.portal.admin.customer.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;


public class CTBRI_DateUtils {

	public static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd";
	public static final String DATEHM_FORMAT_PATTERN = "yyyy-MM-dd HH:mm";
	public static final String DATETIME_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
	 //SimpleDateFormat不是线程安全的，如果多个线程访问会出现并发问题，不必考虑性能问题直接在方法里面使用 2012-09-18
	//private DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN);
	//private DateFormat dateHmFormat = new SimpleDateFormat(DATEHM_FORMAT_PATTERN);
	//private DateFormat datetimeFormat = new SimpleDateFormat(DATETIME_FORMAT_PATTERN);
	
	private static final Logger log = Logger.getLogger("system");
	
	/**
	 * 返回当前日期时间Timestamp
	 * @return
	 */
	public static Timestamp currentTimestamp(){
		return new Timestamp(System.currentTimeMillis());
	}
	
	/**
	 * 将"日期时间"的字符串转化为Date类型
	 * @param dateTime
	 * @return
	 */
	public static Date parseDateTime(String dateTime){
		if(Utils.isNullOrEmpty(dateTime))
			return null;
		try {
			return new SimpleDateFormat(DATETIME_FORMAT_PATTERN).parse(dateTime);
		} catch (Exception e) {
			log.debug("日期转换错误" + dateTime, e);
		}
		return null;
	}
	/**
	 * 确认工单时间处理
	 * 是否显示时分
	 * */
	public static Date parseDateForWOrder(String date){
		if(Utils.isNullOrEmpty(date))
			return null;
		try {			
			if(date.length()>10){
				return new SimpleDateFormat(DATEHM_FORMAT_PATTERN).parse(date);
			}
			else return new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(date);
		} catch (Exception e) {
			log.debug("日期转换错误", e);
		}
		return null;
	}
	
	/**
	 * 将"日期"字符串转化为Date类型
	 * @param date
	 * @return
	 */
	public static Date parseDate(String date){
		if(Utils.isNullOrEmpty(date))
			return null;
		try {
			return new SimpleDateFormat(DATE_FORMAT_PATTERN).parse(date);
		} catch (Exception e) {
			log.debug("日期转换错误", e);
		}
		return null;
	}
	
	public static String formatDate(Date date){
		return new SimpleDateFormat(DATE_FORMAT_PATTERN).format(date);
	}
	
	public static String formatDatetime(Date date){
		return new SimpleDateFormat(DATETIME_FORMAT_PATTERN).format(date);
	}
	
	public static Date lastSecondOfTheDay(Date d){
		if(d == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		cal.set(Calendar.HOUR_OF_DAY, 23);
		cal.set(Calendar.MINUTE, 59);
		cal.set(Calendar.SECOND, 59);
		cal.set(Calendar.MILLISECOND, 999);
		return cal.getTime();
	}
	
	public static Date timestamp2date(Timestamp timestamp){
		return new Date(timestamp.getTime());
	}
	
	public static Timestamp date2timestamp(Date date){
		return new Timestamp(date.getTime());
	}

}
