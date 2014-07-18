package com.skycloud.management.portal.common.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.SimpleTimeZone;
import java.util.TimeZone;

import org.apache.log4j.Logger;

public class FormatUtil {
	private final static Logger log = Logger.getLogger(FormatUtil.class);
	public final static String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	public final static String DEFAULT_LONG_DATE_PATTERN = "yyyy-MM-dd HH:mm";
	public final static String DEFAULT_LONGER_DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
	public final static String DEFAULT_LONGEST_DATA_PATTERN = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	/**
	 * convert string to java.util.Date.
	 * 
	 * @param str
	 * @param pattern
	 *            default value is yyyy-MM-dd
	 * @return java.util.Date
	 */
	public static Date stringToDate(String str, String pattern) {
		if (str == null) {
			return null;
		}
		if (pattern == null) {
			pattern = DEFAULT_DATE_PATTERN;
		}

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			log.error(e);
		}

		return date;
	}

	/**
	 * 
	 * @param str
	 * @param pattern
	 * @param ifLog
	 * @return
	 */
	public static Date stringToDate(String str, String pattern, boolean ifLog) {
		if (str == null) {
			return null;
		}
		if (pattern == null) {
			pattern = DEFAULT_DATE_PATTERN;
		}

		SimpleDateFormat format = new SimpleDateFormat(pattern);
		Date date = null;
		try {
			date = format.parse(str);
		} catch (ParseException e) {
			if (ifLog)
				log.error(e);
		}

		return date;
	}

	/**
	 * convert java.util.Date to string.
	 * 
	 * @param date
	 * @param pattern
	 *            default value is yyyy-MM-dd
	 * @return java.util.Date
	 */
	public static String dateToString(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null) {
			pattern = DEFAULT_DATE_PATTERN;
		}
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		return format.format(date);
	}

	/**
	 * convert java.util.Date to string.
	 * 
	 * @param date
	 * @param pattern
	 *            default value is yyyy-MM-dd
	 * @return java.util.Date
	 */
	public static String dateToString2(Date date, String pattern) {
		if (date == null) {
			return null;
		}
		if (pattern == null) {
			pattern = DEFAULT_DATE_PATTERN;
		}
		TimeZone tz = new SimpleTimeZone(0 * 60 * 60 * 1000, "Asia/ShangHai");
		SimpleDateFormat format = new SimpleDateFormat(pattern);
		format.setTimeZone(tz);
		return format.format(date);
	}

	public static Date stringToShortDate(String dateStr) {
		return stringToDate(dateStr, DEFAULT_DATE_PATTERN);
	}

	public static Date stringToShortDate(String dateStr, boolean ifLog) {
		return stringToDate(dateStr, DEFAULT_DATE_PATTERN, ifLog);
	}

	public static Date stringToLongDate(String dateStr) {
		return stringToDate(dateStr, DEFAULT_LONG_DATE_PATTERN);
	}

	public static Date stringToLongDate(String dateStr, boolean ifLog) {
		return stringToDate(dateStr, DEFAULT_LONG_DATE_PATTERN, ifLog);
	}

	public static Date stringToLongerDate(String dateStr) {
		return stringToDate(dateStr, DEFAULT_LONGER_DATE_PATTERN);
	}

	public static Date stringToLongerDate(String dateStr, boolean ifLog) {
		return stringToDate(dateStr, DEFAULT_LONGER_DATE_PATTERN, ifLog);
	}

	public static Date stringToDate(String dateStr) {
		Date date = stringToLongDate(dateStr, false);
		if (date == null) {
			date = stringToShortDate(dateStr);
		}
		return date;
	}

	public static String dateToShortString(Date date) {
		return dateToString(date, DEFAULT_DATE_PATTERN);
	}

	public static String dateToLongString(Date date) {
		return dateToString(date, DEFAULT_LONG_DATE_PATTERN);
	}

	public static String dateToLongerString(Date date) {
		return dateToString(date, DEFAULT_LONGER_DATE_PATTERN);
	}

	public static String gbk2Utf8(String src) {
		String dest = "";
		if (src == null || src.trim().equals("")) {
			return dest;
		}
		try {
			dest = src;// new String(src.getBytes(),"GBK");
		} catch (Exception e) {
			dest = "";
		}
		return dest;
	}

	public static String getNoticeId(String noticeTo, String notificationID) {
		String ret = "";
		String nid = notificationID;
		if (noticeTo == null || notificationID == null) {
			log.debug("exist null value:noticeTo=" + String.valueOf(noticeTo)
					+ ",notificationID=" + String.valueOf(notificationID));
			return ret;
		}
		if (nid.startsWith("-")) {
			nid = nid.substring(1);
		}
		if ("1".equals(noticeTo) || "0".equals(noticeTo)) {
			ret = "        <mdNoticeID>" + nid + "</mdNoticeID>\n";
		}
		if ("2".equals(noticeTo)) {
			ret = "        <odNoticeID>" + nid + "</odNoticeID>\n";
		}

		return ret;
	}

	public static String getImei(String deviceId) {
		if (deviceId == null || deviceId.trim().equals("")) {
			return "";
		}
		return "        <imei>" + deviceId + "</imei>\n";
	}

	public static String getDayOfYear(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int doy = cal.get(Calendar.DAY_OF_YEAR);
		return "" + doy;
	}

	public static String getDayOfMonth(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int dom = cal.get(Calendar.DAY_OF_MONTH);
		return "" + dom;
	}

	public static String getDayOfWeek(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int dow = cal.get(Calendar.DAY_OF_WEEK);
		return "" + dow;
	}

	public static String getHHmm(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		int hour = cal.get(Calendar.HOUR_OF_DAY);
		int minute = cal.get(Calendar.MINUTE);
		int sm = minute / 10;
		return "" + hour + sm + "0";
	}

	public static Date getYYYYMMdd(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		;
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}

	public static Date getYYYYMMddHHmm10ss(Date d) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(d);
		;
		int minute = cal.get(Calendar.MINUTE);
		int sm = minute / 10;
		cal.set(Calendar.MINUTE, sm * 10);
		cal.set(Calendar.SECOND, 0);
		return cal.getTime();
	}
}
