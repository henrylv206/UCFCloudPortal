package com.skycloud.management.portal.admin.customer.utils;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.skycloud.management.portal.front.command.IResult;
import com.sun.jersey.api.MessageException;

public class Utils {
	
	private static final Logger log = Logger.getLogger("system");
	
	private static final int ipBits = 32;
	
	public static <E> E safeGet(List<E> list, int index){
		if(list == null || index < 0 || index >= list.size())
			return null;
		return list.get(index);
	}
	
	public static boolean isNull(Object obj){
		return obj == null;
	}
	
	public static boolean isNullOrEmpty(Collection<?> c){
		return c == null || c.isEmpty();
	}
	
	public static boolean isNullOrEmpty(String str){
		return str == null || str.trim().isEmpty();
	}
	
	public static boolean isIdValid(Integer id){
		return id != null && id > 0;
	}
	
	/**
	 * 将long类型的ip地址转换为int数组形式
	 * @param ip 2886738206
	 * @return [172, 16, 33, 30]
	 */
	public static int[] ipLong2Int(long ip){
		int len = 4;
		int rs[] = new int[len];
		for(int i = len - 1; i >= 0; i--){
			rs[i] = (int)(ip % 256);
			ip /= 256;
		}
		return rs;
	}
	
	public static String ipLong2String(long ip){
		return ipInt2String(ipLong2Int(ip));
	}
	
	/**
	 * 将int数组形式的ip地址转换为long型
	 * @param ip [172, 16, 33, 30]
	 * @return 2886738206
	 */
	public static long ipInt2Long(int[] ip){
		long rs = 0;
		for(int i = 0; i < ip.length; i++)
			rs += ip[i] * Math.pow(256, ip.length - 1 - i);
		return rs;
	}
	
	/**
	 * 将字符串形式的ip地址转换为int数组形式
	 * @param ip "172.16.33.30"
	 * @return [172, 16, 33, 30]
	 */
	public static int[] ipString2Int(String ip){
		if(isNullOrEmpty(ip)){
			return null;
		}
		ip = ip.trim();
		String[] arr = ip.split("\\.");
		if(arr.length != 4){
			log.error("IP格式错误: " + ip);
			return null;
		}
		int[] rs = new int[arr.length];
		for(int i = 0; i < arr.length; i++){
			rs[i] = Integer.parseInt(arr[i]);
		}
		return rs;
	}
	
	public static long ipString2Long(String ip){
		return ipInt2Long(ipString2Int(ip));
	}
	
	public static String ipInt2String(int[] ip){
		return join(ip, ".");
	}
	
	public static int[] ipByte2Int(byte[] b){
		int rs[] = new int[b.length];
		for(int i = 0; i < b.length; i++){
			rs[i] = ipByte2Int(b[i]);
		}
		return rs;
	}
	
	public static int ipByte2Int(byte b){
		return b < 0 ? 256 + b : b;
	}
	
	public static byte ipInt2Byte(int i){
		if(256 < i || i < 0)
			log.error("IP地址范围错误: " + i);
		return (byte)i;
	}
	
	@Deprecated
	public static String ipDefaultGateway(String ip){
		if(isNullOrEmpty(ip) || ip.lastIndexOf(".") < 0)
			return null;
		return ip.substring(0, ip.lastIndexOf(".")) + ".1";
	}
	
	/**
	 * 计算子网网关, 默认为子网中的第一个IP
	 * @param ip
	 * @param f
	 * @return
	 */
	public static long ipGateway(long ip, int f){
		return ipStart(ip, f, 0);
	}
	
	/**
	 * 计算子网掩码
	 * @param f
	 * @return
	 */
	public static long ipNetmask(int f){
		//long mask = mask(f, 0);
		return (long)((Math.pow(2, f) -1) * Math.pow(2, ipBits - f));
	}
	
	/**
	 * 计算子网起始IP
	 * @param ip IP地址数值表示
	 * @param f 例:192.168.0.0/24, f=24
	 * @param reserved 保留地址数量, 非网关reserved > 1
	 * @return
	 */
	public static long ipStart(long ip, int f, int reserved){
		long mask0 = ipNetmask(f);
		//long mask1 = mask(0, 1);
		long mask1 = reserved;
		ip = mask0 & ip;
		ip = mask1 | ip;
		return ip;
		//return ipLong2String(ip);
	}
	
	/**
	 * 计算子网结束IP
	 * @param ip IP地址数值表示
	 * @param f 例:192.168.0.0/24, f=24
	 * @param reserved 保留地址数量, 非广播地址reserved > 1
	 * @return
	 */
	public static long ipEnd(long ip, int f, int reserved){
		long mask0 = mask(f, 0);
		//long mask1 = mask(0, ipBits-f) - reserved;
		long mask1 = (long)Math.pow(2, ipBits-f) - 1 - reserved;
		ip = mask0 & ip;
		ip = mask1 | ip;
		return ip;
		//return ipLong2String(ip);
	}
	
	public static long mask(int front, int end){
		StringBuilder s = new StringBuilder("00000000000000000000000000000000");
		for(int i = 0; i < front; i++)
			s.replace(i, i+1, "1");
		for(int i = 0; i < end; i++)
			s.replace(ipBits-i-1, ipBits-i, "1");
		return Long.parseLong(s.toString(), 2);
	}
	
	public static String join(int[] arr, String sep){
		StringBuilder s = new StringBuilder(15);
		for(int i = 0; i < arr.length; i++){
			s.append(arr[i]);
			if(i != arr.length - 1)
				s.append(sep);
		}
		return s.toString();
	}
	
	public static String join(Object[] arr, String sep){
		StringBuilder s = new StringBuilder(15);
		for(int i = 0; i < arr.length; i++){
			s.append(arr[i]);
			if(i != arr.length - 1)
				s.append(sep);
		}
		return s.toString();
	}
	
	public static String replaceSpaceWithUnderscore(String src){
		return src.replaceAll("\\s", "_");
	}
	
	public static void checkResultUnique(IResult rs, String msg){
		if(!rs.isUnique()){
			if(rs.isError())
				throw new MessageException(msg+" "+rs.getResult());
			else if(rs.isStatusError())
				throw new MessageException(msg + "\n原因: " + rs.getErrorText());
			throw new MessageException(msg + rs);
		}
	}
	
	public static Integer parseInteger(String str){
		if(StringUtils.isEmpty(str) || !StringUtils.isNumeric(str) || str==null )
			return null;
		return Integer.parseInt(str);
	}
	
	public static boolean isValidIp(String ip) {
		
		if (StringUtils.isEmpty(ip)) {
			return false;
		}
		
		Pattern pattern = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
		Matcher matcher = pattern.matcher(ip);
		return matcher.matches();
	}
	


	public static void main(String[] args){
		testMask();
		testIp();
		System.out.println(isValidIp("192.168.254.4"));
		
		String beijing_ssvpn_ip = "219.143.125.220";
		String beijing_ravpn_ip = "219.143.125.221";
		String xian_ssvpn_ip = "219.143.125.222";
		String xian_ravpn_ip = "219.143.125.223";
		System.out.println("zone name | \t ssvpn IP        | \t ssvpn IP number");
		System.out.println("---------------------------------------------------");
		System.out.println("beijing   | \t " + beijing_ssvpn_ip + " | \t " + ipString2Long(beijing_ssvpn_ip));
		System.out.println("xian      | \t " + xian_ssvpn_ip + " | \t " + ipString2Long(xian_ssvpn_ip));
		System.out.println("===================================================");
		System.out.println("zone name | \t ravpn IP        | \t ravpn IP number");
		System.out.println("---------------------------------------------------");
		System.out.println("beijing   |  \t " + beijing_ravpn_ip + " | \t " + ipString2Long(beijing_ravpn_ip));
		System.out.println("xian      | \t " + xian_ravpn_ip + " | \t " + ipString2Long(xian_ravpn_ip));
		
	}
	private static void testMask(){
		//后8位1
		long n = (long)Math.pow(2, 8) -1;
		printMask(n);
		int f = 8;
		//32位1
		long mask = (long)Math.pow(2, ipBits) - 1;
		printMask(mask);
		//前f位1
		long mask2 = (long)((Math.pow(2, f) -1) * Math.pow(2, ipBits - f));
		printMask(mask2);
		mask = mask - mask2;
		printMask(mask);
	}
	private static void printMask(long mask){
		String s = Long.toBinaryString(mask);
		System.out.println(s+"="+s.length());
	}
	private static void testIp(){
		long ip = ipString2Long("10.241.41.112");
		for(int i = 8; i <= 28; i++)
			System.out.println(
					ipLong2String(ipNetmask(i))+"\t" + 
					ipLong2String(ipStart(ip, i, 1)) + "\t" + 
					ipLong2String(ipEnd(ip, i, 1)));
	}
	
	public static boolean equalsInteger(Integer num1,Integer num2){
		if(num1.intValue()==num2.intValue()){
			return true;
		}else{
			return false;
		}
	
	}
	
	public static boolean noEmpty(String str){
		boolean flag = false;
		if(!"".equals(str) && str!=null && !"null".equals(str)){
			flag=true;  
		}
		return flag;
	}
	public static boolean noEmpty(Integer num){
		boolean flag = false;
		if(num!=null){
			flag=true;  
		}
		return flag;
	}
	public static boolean equalsInteger(Long num1,Integer num2){
		if(num1.intValue()==num2.intValue()){
			return true;
		}else{
			return false;
		}
	
	}
	public static boolean saveVmByProductId(Integer productId,int[] productids){
		boolean b=false;
		if(productids.length>0){
			for (int i : productids) {
				if(productId.intValue()==i){
					b=true;
				}
			}
		}
		return b;
	}
	public static boolean replaceValue(String str,String[] strs){
		boolean b=false;
		if(strs.length>0){
			for (String string : strs) {
				if(string.equals(str)){
					b=true;
				}
			}
		}
		return b;
	}
	/*public static String getStrByKey(String json,String key){
		String str="";
		if(json!=null && !"".equals(json)){
			JSONObject j=JSONObject.parseObject(json);
			if(j.get(key)!=null){
				str=j.get(key).toString();
			}
		}
		return str;
	}
	public static String setStrByKey(String json,String key,String value){
		String str="";
		if(json!=null && !"".equals(json)){
			JSONObject j=JSONObject.parseObject(json);
			if(j.containsKey(key)){
				j.put(key, value);
				str=j.toJSONString();
			}
		}
		return str;
	}*/
	public static String getNowTime(){
		Date date=new Date();
		SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddhhmmss");
		return sdf.format(date);
	}
}