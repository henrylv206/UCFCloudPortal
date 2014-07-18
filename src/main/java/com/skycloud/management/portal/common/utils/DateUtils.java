package com.skycloud.management.portal.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.MissingResourceException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Date工具类. <br>
 * Date工具类.
 */
public class DateUtils {

	private static String defaultDatePattern = null;

	private static Log log = LogFactory.getLog(DateUtils.class);

	private static String timePattern = "HH:mm:ss";

	private static final int HOUR = 24;

	private static final int SECOND = 60;

	private static final int MINITE = 60;

	private static final int MILLSECOND = 1000;

	/**
	 * 比较指定时间与系统时间相差的天数
	 * 
	 * @param date指定时间
	 *            ("yyyy-mm-dd" 形式)
	 */
	public static int compareTime(String date) {
		Date dates;
		int i = 0;
		try {
			dates = stringToDate(date);
			i = (int) ((dates.getTime() - System.currentTimeMillis()) / 24 / 3600 / 1000);
		}
		catch (ParseException e) {
			DateUtils.log.error("转换日期出错");
			e.printStackTrace();
		}
		return i;
	}

	/**
	 * String类型时间转换成Date类型采用默认格式yyyy-MM-dd
	 * 
	 * @param strDate
	 *            如：2010-12-20 08:57:20
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String strDate) throws ParseException {
		Date aDate = null;
		try {
			if (DateUtils.log.isDebugEnabled()) {
				DateUtils.log.debug("converting date with pattern: " + DateUtils.getDatePattern());
			}
			aDate = DateUtils.stringToDate(DateUtils.getDatePattern(), strDate);
		}
		catch (ParseException pe) {
			DateUtils.log.error("Could not convert '" + strDate + "' to a date, throwing exception");
			pe.printStackTrace();
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());

		}
		return aDate;
	}

	/**
	 * String类型时间转换成Date类型
	 * 
	 * @param aMask
	 *            格式 如:yyyy-MM-dd HH等
	 * @param strDate
	 *            字符串时间 如：2010-12-20 08:57:20
	 * @return
	 * @throws ParseException
	 */
	public static final Date stringToDate(String aMask, String strDate) throws ParseException {
		SimpleDateFormat df = null;
		Date date = null;
		df = new SimpleDateFormat(aMask);
		DateUtils.log.info("converting '" + strDate + "' to date with mask '" + aMask + "'");
		try {
			date = df.parse(strDate);
		}
		catch (ParseException pe) {
			throw new ParseException(pe.getMessage(), pe.getErrorOffset());
		}
		return (date);
	}

	/**
	 * 获取默认日期格式 yyyy-MM-dd
	 * 
	 * @return
	 */
	public static synchronized String getDatePattern() {
		try {
			DateUtils.defaultDatePattern = "yyyy-MM-dd";
		}
		catch (MissingResourceException mse) {
			DateUtils.defaultDatePattern = "yyyy-MM-dd";
		}

		return DateUtils.defaultDatePattern;
	}

	/**
	 * 获取当前时间 格式为HH:mm:ss
	 * 
	 * @return
	 */
	public static final String getDateTime() {
		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat df = null;
		String returnValue = "";
		if (date != null) {
			df = new SimpleDateFormat(DateUtils.getDateTimePattern());
			returnValue = df.format(date);
		}
		return (returnValue);
	}

	/**
	 * 获取当前时间
	 * 
	 * @param aMask
	 *            格式
	 * @return
	 */
	public static final Date getDateTime(String aMask) {
		SimpleDateFormat df = null;
		String returnValue = "";
		df = new SimpleDateFormat(aMask);
		returnValue = df.format(new Date());
		try {
			return df.parse(returnValue);
		}
		catch (ParseException e) {
			DateUtils.log.error(e);
			return new Date();
		}
	}

	/**
	 * Date类型根据格式转换成String类型
	 * 
	 * @param aMask
	 *            格式
	 * @param aDate
	 *            日期
	 * @return
	 */
	public static final String dateToString(String aMask, Date aDate) {
		SimpleDateFormat df = null;
		String returnValue = "";
		if (aDate == null) {
			DateUtils.log.error("aDate is null!");
		} else {
			df = new SimpleDateFormat(aMask);
			returnValue = df.format(aDate);
		}

		return (returnValue);
	}

	public static String getDateTimePattern() {
		return DateUtils.getDatePattern() + " HH:mm:ss";
	}

	/**
	 * 获取当前日期时间 格式为yyyy-MM-dd HH:mm:ss的Date类型对象
	 * 
	 * @return
	 */
	public static Date getTimeNow() {
		return DateUtils.getDateTime(DateUtils.getDateTimePattern());
	}

	/**
	 * 获取当前时间 HH:mm:ss
	 * 
	 * @param theTime
	 *            Date类型时间
	 * @return
	 */
	public static String getTimeNow(Date theTime) {
		return DateUtils.dateToString(DateUtils.timePattern, theTime);
	}

	/**
	 * 获取今天的Calendar对象
	 * 
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Date today = new Date();
		SimpleDateFormat df = new SimpleDateFormat(DateUtils.getDatePattern());
		String todayAsString = df.format(today);
		Calendar cal = new GregorianCalendar();
		cal.setTime(stringToDate(todayAsString));
		return cal;
	}

	/**
	 * 时间戳转换为Date类型
	 * 
	 * @param timestamp
	 *            时间戳
	 */
	public static Date long2Date(Long timestamp) {
		return new Date(timestamp);
	}

	/**
	 * 时间戳转换为日期字符串
	 * 
	 * @param timestamp
	 *            时间戳
	 */
	public static String long2DateString(Long timestamp) {
		return DateUtils.dateToString(DateUtils.getDateTimePattern(), new Date(timestamp));
	}

	/**
	 * 时间戳转换为日期字符串
	 * 
	 * @param timestamp
	 *            时间戳
	 * @param pattern
	 *            格式
	 */
	public static String longToDateString(Long timestamp, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(new Date(timestamp));
	}

	/**
	 * Date类型日期根据pattern转换成字符串
	 * 
	 * @param date
	 *            Date类型日期
	 * @param pattern
	 *            格式
	 * @return
	 */
	public static String dateToString(Date date, String pattern) {
		SimpleDateFormat df = new SimpleDateFormat(pattern);
		return df.format(date);
	}

	/**
	 * 时间格式的字符串,转换为long
	 * 
	 * @param dateString
	 *            字符串类型时间(如：2010-12-29 yyyy-MM-dd)
	 * @throws ParseException
	 */
	public static long stringToLong(String dateString) throws ParseException {
		if (dateString.length() == 10) {
			return stringToLong(dateString, DateUtils.getDatePattern());
		} else {
			return stringToLong(dateString, DateUtils.getDateTimePattern());
		}
	}

	/**
	 * 把指定的格式的日期时间的字符串,转换为long
	 * 
	 * @param dateString
	 *            时间字符串(如：20101229083627 yyyyMMddHHmmss,2010-12-29 yyyy-MM-dd)
	 * @param format
	 *            格式
	 * @return
	 * @throws ParseException
	 */
	public static long stringToLong(String dateString, String format) throws ParseException {
		if (dateString != null && !"".equals(dateString)) {
			Date date = DateUtils.stringToDate(format, dateString);
			return date.getTime();
		}
		return -1;
	}

	/**
	 * 获取今天在一年中的天数
	 * 
	 * @return
	 */
	public int getDayOfYear() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.add(Calendar.DATE, 0);
		return calendar.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * 获取今天的零点的long型时间
	 * 
	 * @return
	 */
	public static long getZeroLongTime() {
		GregorianCalendar calendar = new GregorianCalendar();
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		Long todayZeroTime = calendar.getTimeInMillis() - (23 * MINITE * SECOND * MILLSECOND + 59 * SECOND * MILLSECOND + 59 * MILLSECOND);// 今日零点时间
		return todayZeroTime;
	}

	/**
	 * 获取days天前的零点的long型时间
	 * 
	 * @param days
	 * @return
	 */
	public static long getZeroLongTimeServalDays(int days) {
		return getZeroLongTime() - days * HOUR * MINITE * SECOND * MILLSECOND;
	}

	/**
	 * 将传入时间与当前时间进行计算，返回时间差。格式为最大时间的时间数加上单位，如1天前、2小时前。 如果时间在一分钟内，则返回刚刚
	 * 
	 * @param start
	 *            开始日期（如：发布日期）
	 * @param end
	 *            结束日期（如：当前日期）
	 * @return
	 */
	public static String getLeftTime(long start, long end) {
		long left = end - start;
		long day = left / (MILLSECOND * SECOND * MINITE * HOUR);
		long hour = (left % (MILLSECOND * SECOND * MINITE * HOUR)) / (MILLSECOND * SECOND * MINITE);
		long min = ((left % (MILLSECOND * SECOND * MINITE * HOUR)) % (MILLSECOND * SECOND * MINITE)) / (MILLSECOND * SECOND);
		long second = (((left % (MILLSECOND * SECOND * MINITE * HOUR)) % (MILLSECOND * SECOND * MINITE)) % (MILLSECOND * SECOND)) / MILLSECOND;
		if (day != 0) {
			return day + "天前";
		} else if (hour != 0) {
			return hour + "小时前";
		} else if (min != 0) {
			return min + "分钟前";
		} else if (second != 0) {
			return second + "秒前";
		} else {
			return "刚刚";
		}
	}

	/**
	 * 获取days天 毫秒时间总和
	 * 
	 * @param days
	 *            天数
	 * @return
	 */
	public static long getDaysTime(int days) {
		return MILLSECOND * SECOND * MINITE * HOUR * days;
	}

	/**
	 * 字符串类型时间转换成字符串类型时间
	 * 
	 * @param date
	 *            时间 20101229083627
	 * @param oldFormat
	 *            时间格式 yyyyMMddHHmmss
	 * @param newFormat
	 *            时间新格式 yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String stringToString(String date, String oldFormat, String newFormat) {
		String result = "";
		try {
			Long d = stringToLong(date, oldFormat);
			result = longToDateString(d, newFormat);
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static void main(String[] args) throws ParseException {

		/*
		 * System.out.println(System.currentTimeMillis());
		 * System.out.println(System.currentTimeMillis() + 30000);
		 * System.out.println(getLeftTime(1290996553873l, 1290996583921l));
		 * System.out.println(toLong("20101201112745")); SimpleDateFormat sf =
		 * new SimpleDateFormat("yyyyMMddHHmmss"); String time = sf.format(new
		 * Date()); System.out.println(time); Date date = sf.parse(time);
		 * System.out.println(date.getTime());
		 */
		/*
		 * long zero = getZeroLongTime(); long d = getZeroLongTimeServalDays(3);
		 * String szero = longToDateString(zero, "yyyyMMddHHmmss"); String dzero
		 * = longToDateString(d, "yyyyMMddHHmmss"); String now =
		 * longToDateString(System.currentTimeMillis(), "yyyyMMddHHmmss");
		 * System.out.println(szero); System.out.println(dzero);
		 * System.out.println(now);
		 */

		// System.out.println(convertDateToString(new Date()));
		// System.out.println(DateUtils.stringToDate("2010-12-19 17:21"));
		System.out.println(DateUtils.dateToString("yyyy-MM-dd", new Date()));

		System.out.println(DateUtils.dateToString(new Date(), "yyyyMMddHHmmss"));
		System.out.println(DateUtils.longToDateString(System.currentTimeMillis(), "yyyyMMddHHmmss"));
		// System.out.println(toLong("20101229083627"));
		System.out.println(stringToLong("20101229083627", "yyyyMMddHHmmss"));
		System.out.println(stringToString("20101229083627", "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss"));
	}
}