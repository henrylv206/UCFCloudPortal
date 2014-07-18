package com.skycloud.management.portal.common.utils;

import java.io.FileWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

/**
 * <p>
 * Title: Time
 * </p>
 * <p>
 * Description:
 * </p>
 * 此类主要用来取得本地系统的系统时间并用下面5种格式显示 1. YYMMDDHH 8位 2. YYMMDDHHmm 10位 3. YYMMDDHHmmss
 * 12位 4. YYYYMMDDHHmmss 14位 5. YYMMDDHHmmssxxx 15位 (最后的xxx 是毫秒)
 * 
 * @version 1.0
 */
public class CTime {

	public static final int YYMMDDhh = 8;

	public static final int YYMMDDhhmm = 10;

	public static final int YYMMDDhhmmss = 12;

	public static final int YYMMDDhhmmssxxx = 15;

	public static final int YYYYMMDDhhmmss = 14;

	/**
	 * 给定开始时间，得到n天后的日期
	 * 
	 * @param s
	 * @param n
	 * @param sdf
	 *            :yyyy-MM-dd或yyyy-MM-dd HH:mm:ss等
	 * @return
	 * @throws ParseException
	 */
	public static String addDay(SimpleDateFormat sdf, String s, int n) {
		String retime = "";
		try {
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(s));
			cd.add(Calendar.DATE, n);
			retime = sdf.format(cd.getTime());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retime;
	}
	
	public static String addHours(SimpleDateFormat sdf, String s, int n) {
		String retime = "";
		try {
			Calendar cd = Calendar.getInstance();
			cd.setTime(sdf.parse(s));
			cd.add(Calendar.HOUR, n);
			retime = sdf.format(cd.getTime());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return retime;
	}

	/**
	 * 将数据库格式时间转换为js日历空间中的时间格式 例如：20060922 -〉2006-09-22
	 * 
	 * @param time
	 * @return
	 */
	public static String DbToJs(String time) {
		if (time == null) {
			return "";
		}
		int len = time.trim().length();
		switch (len) {
			case 6:
				time = "20" + time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4, 6);
				break;
			case 15:
				time = "20" + time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4, 6);
				break;
			case 8:
				time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
				break;
			case 14:
				time = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8);
				break;
		}
		return time;
	}

	/**
	 * #本函数的主要功能是格式化时间，以便于页面显示 #time 时间 可为6位、8位、12位、15位 #return 返回格式化后的时间 #6位
	 * YY年MM月DD日 #8位 YYYY年MM月DD日 #12位 YY年MM月DD日 HH:II:SS #15位 YY年MM月DD日
	 * HH:II:SS:CCC
	 * 
	 * @param time
	 * @return
	 */
	public static String formattime(String time) {
		int length = 0;
		if (time == null || time.length() < 6) {
			return "";
		}
		length = time.length();
		String renstr = "";
		switch (length) {
			case 6:
				renstr = time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4) + "";
				break;
			case 8:
				renstr = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + "";
				break;
			case 12:
				renstr = time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4, 6) + " " + time.substring(6, 8) + ":"
				        + time.substring(8, 10) + ":" + time.substring(10, 12) + "";
				break;
			case 14:
				renstr = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":"
				        + time.substring(10, 12) + ":" + time.substring(12, 14) + "";
				break;
			case 15:
				renstr = time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4, 6) + " " + time.substring(6, 8) + ":"
				        + time.substring(8, 10) + ":" + time.substring(10, 12) + ":" + time.substring(12);
				break;
			default:
				renstr = time.substring(0, 2) + "-" + time.substring(2, 4) + "-" + time.substring(4) + "";
				break;
		}
		return renstr;
	}

	/**
	 * #本函数的主要功能是格式化时间，以便于页面显示 #time 时间 可为6位、8位、12位、15位 #return 返回格式化后的时间 #6位
	 * YY年MM月DD日 #8位 YYYY年MM月DD日 #12位 YY年MM月DD日 HH:II:SS #15位 YY年MM月DD日
	 * HH:II:SS:CCC
	 * 
	 * @param time
	 * @return
	 */
	public static String formattime2(String time) {
		int length = 0;
		if (time == null || time.length() < 6) {
			return "";
		}
		length = time.length();
		String renstr = "";
		switch (length) {
			case 6:
				renstr = time.substring(0, 4) + "年" + time.substring(4, 6) + "月";
				break;
			case 8:
				renstr = time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日";
				break;
			case 10:
				renstr = time.substring(0, 4) + "年" + time.substring(4, 6) + "月" + time.substring(6, 8) + "日" + time.substring(8, 10) + "时";
				break;
			case 12:
				renstr = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":"
				        + time.substring(10, 12);
				break;
			case 14:
				renstr = time.substring(0, 4) + "-" + time.substring(4, 6) + "-" + time.substring(6, 8) + " " + time.substring(8, 10) + ":"
				        + time.substring(10, 12) + ":" + time.substring(12, 14) + "";
				break;

		}
		return renstr;
	}

	/**
	 * 补0函数
	 * 
	 * @param iT
	 *            int
	 * @return String
	 * @throws Exception
	 */
	public static String get00String(int iT) {
		if (iT >= 10) {
			return String.valueOf(iT);
		} else {
			return "0" + String.valueOf(iT);
		}

	}

	/**
	 * 计算24小时内的得到倒计时<br>
	 * 修改人：hetao@abc.com<br>
	 * 日期：2010.7.24<br>
	 * 
	 * @return 倒计时
	 */
	public static String getCountdown() throws ParseException {
		String countdown = "";
		SimpleDateFormat dFormat = new SimpleDateFormat("HH小时mm分ss秒");
		Date curDay = new Date();
		Calendar dfs = Calendar.getInstance();
		dfs.setTime(curDay);
		dfs.add(Calendar.DATE, 1);
		Date secDay = dfs.getTime();
		String strTmp = secDay.toString();
		String subStr = strTmp.substring(11, 19);
		strTmp = strTmp.replace(subStr, "02:00:00");
		secDay = new Date(strTmp);

		long numCurDay = curDay.getTime();
		long numSecDay = secDay.getTime();
		long numTime = numSecDay - numCurDay;
		countdown = dFormat.format(new Date(numTime));
		return countdown;
	}

	/**
	 * #本函数主要作用是返回当前天数
	 * 
	 * @return
	 */
	public static String getDay() {
		Calendar time = Calendar.getInstance();
		int day = time.get(Calendar.DAY_OF_MONTH);
		String djday = "";
		if (day < 10) {
			djday = "0" + Integer.toString(day);
		} else {
			djday = Integer.toString(day);
		}
		return djday;
	}

	/**
	 * 时间格式
	 * 
	 * @param time
	 * @param format
	 * @return
	 */
	private static String getFormatTime(int time, int format) {
		StringBuffer numm = new StringBuffer(format);
		int length = String.valueOf(time).length();

		if (format < length) {
			return null;
		}

		for (int i = 0; i < format - length; i++) {
			numm.append("0");
		}
		numm.append(time);
		return numm.toString().trim();
	}

	/**
	 * 本函数作用是返回当前小时
	 * 
	 * @return
	 */
	public static String getHour() {
		Calendar time = Calendar.getInstance();
		int hour = time.get(Calendar.HOUR_OF_DAY);
		String djhour = "";
		if (hour < 10) {
			djhour = "0" + Integer.toString(hour);
		} else {
			djhour = Integer.toString(hour);
		}
		return djhour;
	}

	/**
	 * #本函数作用是返回当前分钟
	 * 
	 * @return
	 */
	public static String getMin() {
		Calendar time = Calendar.getInstance();
		int min = time.get(Calendar.MINUTE);
		String djmin = "";
		if (min < 10) {
			djmin = "0" + Integer.toString(min);
		} else {
			djmin = Integer.toString(min);
		}
		return djmin;
	}

	/**
	 * #本函数作用是返回当前月份（2位）
	 * 
	 * @return
	 */
	public static String getMonth() {
		Calendar time = Calendar.getInstance();
		int month = time.get(Calendar.MONTH) + 1;
		String djmonth = "";
		if (month < 10) {
			djmonth = "0" + Integer.toString(month);
		} else {
			djmonth = Integer.toString(month);
		}
		return djmonth;
	}

	/**
	 * 根据系统时间得到n天以后的日期
	 * 
	 * @param int n 天数
	 * @return String 得到的时间 YYYYMMDD
	 */
	public static String getNDayLater(int n) {
		String time = null;
		String dayStr = null;
		String monthStr = null;
		String yearStr = null;
		java.util.Calendar cal = java.util.Calendar.getInstance();
		cal.add(java.util.Calendar.DAY_OF_MONTH, n);
		int day = cal.get(java.util.Calendar.DATE);
		int month = cal.get(java.util.Calendar.MONTH) + 1;
		int year = cal.get(java.util.Calendar.YEAR);
		if (day < 10) {
			dayStr = "0" + Integer.toString(day);
		} else {
			dayStr = Integer.toString(day);
		}
		if (month < 10) {
			monthStr = "0" + Integer.toString(month);
		} else {
			monthStr = Integer.toString(month);
		}
		yearStr = Integer.toString(year);
		time = yearStr.substring(2) + monthStr + dayStr;
		dayStr = null;
		monthStr = null;
		yearStr = null;
		return time;
	}

	/**
	 * 取得本地系统的时间，时间格式由参数决定
	 * 
	 * @param format
	 *            时间格式由常量决定
	 * @return String 具有format格式的字符串
	 */

	public static String getTime(int format) {

		return getTime(format, 0);
	}

	/**
	 * getTime
	 * 
	 * @param format
	 * @param years
	 * @return
	 */
	public static String getTime(int format, int years) {
		StringBuffer cTime = new StringBuffer(15);
		Calendar time = Calendar.getInstance();
		int miltime = time.get(Calendar.MILLISECOND);
		int second = time.get(Calendar.SECOND);
		int minute = time.get(Calendar.MINUTE);
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int month = time.get(Calendar.MONTH) + 1;
		int year = -1;
		if (years != 0) {
			year = years;
		} else {
			year = time.get(Calendar.YEAR);
		}
		time = null;
		if (format != 14) {
			if (year >= 2000) {
				year = year - 2000;
			} else {
				year = year - 1900;
			}
		}
		if (format >= 2) {
			if (format == 14) {
				cTime.append(year);
			} else {
				cTime.append(getFormatTime(year, 2));
			}
		}
		if (format >= 4) {
			cTime.append(getFormatTime(month, 2));
		}
		if (format >= 6) {
			cTime.append(getFormatTime(day, 2));
		}
		if (format >= 8) {
			cTime.append(getFormatTime(hour, 2));
		}
		if (format >= 10) {
			cTime.append(getFormatTime(minute, 2));
		}
		if (format >= 12) {
			cTime.append(getFormatTime(second, 2));
		}
		if (format >= 15) {
			cTime.append(getFormatTime(miltime, 3));
		}
		return cTime.toString().trim();
	}

	/**
	 * getToday
	 * 
	 * @return
	 */
	public static String getToday() {
		Date dd = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		return df.format(dd);
	}

	/**
	 * 获得周
	 * 
	 * @return
	 */
	public static String getWeek() {
		final String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar time = Calendar.getInstance();
		String djyear = dayNames[time.get(Calendar.DAY_OF_WEEK) - 1];
		return djyear;
	}

	/**
	 * 返回给定时间内按周（周一至周日为一个单位）划分得到的时间数组
	 * 
	 * @param stime
	 *            String YYYYMMDD
	 * @param etime
	 *            String YYYYMMDD
	 * @return Vector
	 */
	public static Vector getWeekArray(String stime, String etime) {
		if (stime == null || stime.equals("") || etime == null || etime.equals("") || stime.length() < 8 || etime.length() < 8
		        || stime.compareTo(etime) > 0) {
			return null;
		}

		Vector v = new Vector();
		String[] s = null;
		if (stime.equals(etime)) {
			s = new String[2];
			s[0] = stime;
			s[1] = etime;
			v.add(s);
		} else {
			int year = 0;
			int month = 0;
			int date = 0;

			year = Integer.parseInt(stime.substring(0, 4));
			month = Integer.parseInt(stime.substring(4, 6));
			date = Integer.parseInt(stime.substring(6, 8));

			Calendar c = Calendar.getInstance();
			c.set(year, month - 1, date);

			int week = c.get(Calendar.DAY_OF_WEEK);
			int temp = 0;
			int i = 0;
			if (week != 2) {
				i++;
				s = new String[2];
				if (week == 1) {
					temp = 0;
				} else {
					temp = 8 - week;
				}
				s = new String[2];
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				date = c.get(Calendar.DATE);
				s[0] = year + get00String(month) + get00String(date);

				c.add(Calendar.DAY_OF_MONTH, temp);

				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				date = c.get(Calendar.DATE);
				s[1] = year + get00String(month) + get00String(date);

				v.add(s);
				c.add(Calendar.DAY_OF_MONTH, 1);
				stime = year + get00String(month) + get00String(date);
			}

			while (!stime.equals(etime)) {
				i++;
				s = new String[2];
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				date = c.get(Calendar.DATE);
				s[0] = year + get00String(month) + get00String(date);

				c.add(Calendar.DAY_OF_MONTH, 6);
				year = c.get(Calendar.YEAR);
				month = c.get(Calendar.MONTH) + 1;
				date = c.get(Calendar.DATE);
				s[1] = year + get00String(month) + get00String(date);

				stime = year + get00String(month) + get00String(date);
				if (stime.compareTo(etime) > 0) {
					s[1] = etime;
					v.add(s);
					break;
				}

				v.add(s);

				c.add(Calendar.DAY_OF_MONTH, 1);
				stime = year + get00String(month) + get00String(date);
			}
		}
		return v;
	}

	/**
	 * #本函数主要作用是返回当前年份 #len 返回位数，2位 4位
	 * 
	 * @param len
	 * @return
	 */
	public static String getYear(int len) {
		Calendar time = Calendar.getInstance();
		int year = time.get(Calendar.YEAR);
		String djyear = Integer.toString(year);
		if (len == 2) {
			djyear = djyear.substring(2);
		}
		return djyear;
	}

	/**
	 * 产生任意位的字符串
	 * 
	 * @param time
	 *            int 要转换格式的时间
	 * @param format
	 *            int 转换的格式
	 * @return String 转换的时间
	 */
	public synchronized static String getYearAdd(int format, int iyear) {
		StringBuffer cTime = new StringBuffer(10);
		Calendar time = Calendar.getInstance();
		time.add(Calendar.YEAR, iyear);
		int miltime = time.get(Calendar.MILLISECOND);
		int second = time.get(Calendar.SECOND);
		int minute = time.get(Calendar.MINUTE);
		int hour = time.get(Calendar.HOUR_OF_DAY);
		int day = time.get(Calendar.DAY_OF_MONTH);
		int month = time.get(Calendar.MONTH) + 1;
		int year = time.get(Calendar.YEAR);
		if (format != 14) {
			if (year >= 2000) {
				year = year - 2000;
			} else {
				year = year - 1900;
			}
		}
		if (format >= 2) {
			if (format == 14) {
				cTime.append(year);
			} else {
				cTime.append(getFormatTime(year, 2));
			}
		}
		if (format >= 4) {
			cTime.append(getFormatTime(month, 2));
		}
		if (format >= 6) {
			cTime.append(getFormatTime(day, 2));
		}
		if (format >= 8) {
			cTime.append(getFormatTime(hour, 2));
		}
		if (format >= 10) {
			cTime.append(getFormatTime(minute, 2));
		}
		if (format >= 12) {
			cTime.append(getFormatTime(second, 2));
		}
		if (format >= 15) {
			cTime.append(getFormatTime(miltime, 3));
		}
		return cTime.toString();
	}

	/**
	 * getYTime
	 * 
	 * @param years
	 * @return
	 */
	public static String getYTime(int years) {
		Date dd = new Date();
		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(dd);
	}

	/**
	 * 将js日历控件中的时间转换为数据库格式时间 例如：2006-09-22 -〉20060922
	 * 
	 * @param time
	 * @return
	 */
	public static String JsToDb(String time) {
		if (time == null) {
			return "";
		}
		int len = time.trim().length();
		switch (len) {
			case 10:
				time = time.substring(0, 4) + time.substring(5, 7) + time.substring(8);
				break;
			default:

		}
		return time;
	}

	/**
	 * split
	 * 
	 * @param aaa
	 * @param hhh
	 * @return
	 */
	public static String[] split(String aaa, String hhh) {
		String str = "1|1|1";
		int k = 0;
		String bbb = "";
		bbb = aaa;
		if (bbb == null || bbb == "") {
			bbb = str;
		}
		String fff = hhh;
		if (fff == null || fff == "") {
			fff = "|";
		}
		int start = 0;
		int m = 0;
		for (int n = m + 1; m < n; n++) {
			if (bbb.indexOf(fff, start) == -1) {
				break;
			}
			start = bbb.indexOf(fff, start) + 1;
			k++;
			m++;
		}

		String zzz[] = new String[k + 1];
		start = 0;
		int end = 0;
		for (int yyy = 0; yyy <= k; yyy++) {
			if (bbb.indexOf(fff, start) == -1) {
				zzz[yyy] = bbb.substring(end, bbb.length());
			} else {
				start = bbb.indexOf(fff, start);
				zzz[yyy] = bbb.substring(end, start);
				end = start + fff.length();
				start = end;
			}
		}
		return zzz;
	}

	/**
	 * 转换日期 YYYY-MM-DD 到 YYYYMMDD
	 * 
	 * @param date
	 * @return
	 */
	public static String tranDateToQuery(String date) {
		String[] d = split(date, "-");
		return d[0] + d[1] + d[2];
	}

	/**
	 * 转换日期 YYYYMMDD 到 YYYY-MM-DD
	 * 
	 * @param date
	 * @return
	 */
	public static String tranDateToShow(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-" + date.substring(6, 8);
	}

	/**
	 * 转换日期 YYYY-MM-DD 到 YYYYMMDDhhmmss
	 * 
	 * @param date
	 * @return
	 */
	public static String tranDateToSmil(String date) {
		return tranDateToQuery(date) + "000000";
	}

	/**
	 * 转换时间 HHMMSS 到 HH:MM:SS
	 * 
	 * @param date
	 * @return
	 */
	public static String tranHMSTimeToShow(String time) {
		if (time.length() < 6) {
			return time;
		} else {
			return time.substring(0, 2) + ":" + time.substring(2, 4) + ":" + time.substring(4, 6);
		}
	}

	/**
	 * 将14位日期格式转换成yyyy-MM-dd HH:mm:ss格式
	 * 
	 * @param srcDate
	 *            String
	 * @return String
	 */
	public static String transDate(String srcDate) {
		if (srcDate != null) {
			if (srcDate.trim().length() == 14) {
				return transDateTime(srcDate.trim(), "yyyyMMddHHmmss", "yyyy-MM-dd HH:mm:ss");
			}
			if (srcDate.trim().length() <= 8) {
				return transDateTime(srcDate.trim(), "yyyyMMdd", "yyyy-MM-dd");
			}
			return "";
		} else {
			return "";
		}
	}

	/**
	 * 将日期时间从一种格式转换为另一种格式
	 * 
	 * @param srcTime
	 *            源串
	 * @param srcPattern
	 *            源串格式
	 * @param destPattern
	 *            目标串格式
	 * @return String 目标串
	 */
	public static String transDateTime(String srcTime, String srcPattern, String destPattern) {
		if (srcTime == null) {
			return "";
		}
		try {
			SimpleDateFormat fmt = new SimpleDateFormat();
			fmt.applyPattern(srcPattern);
			Date date = fmt.parse(srcTime);
			fmt.applyPattern(destPattern);
			return fmt.format(date);
		}
		catch (Exception exp) {
		}
		return srcTime;
	}

	public void test() {

		try {
			PrintWriter bb = new PrintWriter(new FileWriter("test2.txt", true), true);
			bb.close();
		}
		catch (Exception ex) {
		}

	}
}
