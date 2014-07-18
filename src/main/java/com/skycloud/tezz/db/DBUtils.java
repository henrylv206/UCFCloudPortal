package com.skycloud.tezz.db;

import java.io.FileInputStream;
import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DBUtils {
	public static Connection getConnection(String path,int type) throws Exception {
		Properties properties = new Properties();
		FileInputStream fis = new FileInputStream(URLDecoder.decode(path, "UTF-8"));
		properties.load(fis);
		if(type==1){
			Class.forName(properties.getProperty("drivername"));
			return DriverManager.getConnection(properties.getProperty("url"));
		}else if(type==2){
			Class.forName(properties.getProperty("dldrivername"));
			return DriverManager.getConnection(properties.getProperty("dlurl"),properties.getProperty("dluser"),properties.getProperty("dlpassword"));
		}else if(type==3){
			Class.forName(properties.getProperty("vdcdrivername"));
			return DriverManager.getConnection(properties.getProperty("vdcurl"));
		}else{
			return null;
		}
		
	}
//	public static Connection getDLConnection(String path) throws Exception {
//		Properties properties = new Properties();
//		FileInputStream fis = new FileInputStream(URLDecoder.decode(path, "UTF-8"));
//		properties.load(fis);
//		Class.forName(properties.getProperty("dldrivername"));
//		return DriverManager.getConnection(properties.getProperty("dlurl"),properties.getProperty("dluser"),properties.getProperty("dlpassword"));
//	}
	public static void closeConnection(Connection conn) {
		if (conn != null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeStatement(Statement stat) {
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closePreparedStatement(PreparedStatement stat) {
		if (stat != null) {
			try {
				stat.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}
