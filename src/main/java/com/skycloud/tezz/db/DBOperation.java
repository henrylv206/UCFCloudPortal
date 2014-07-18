package com.skycloud.tezz.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DBOperation {
	public int GeneratingDeviceData(String deviceType, String start, String end,Integer uid) {
		System.out.println("start:"+start);
		System.out.println("end:"+end);
		int resultnum=0;
		//Connection conn = null;
		//Connection dlconn = null;
		Connection vdcconn = null;
		PreparedStatement pstatForInsert = null;
		PreparedStatement pstatForDelete = null;
		try {
			// insert into tbl_name1(col1,col2) select col3,col4 from tbl_name2;
			String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
//			conn = DBUtils.getConnection(filePath);
//			dlconn=DBUtils.getDLConnection(filePath);
			//conn = DBUtils.getConnection(filePath,1);
			//dlconn=DBUtils.getConnection(filePath,2);
			vdcconn=DBUtils.getConnection(filePath,3);
//			conn.setAutoCommit(false);
//			dlconn.setAutoCommit(false);
			String deleteSql = "DELETE FROM HOST_INFO_SNAP_TEMP";
			String insertSql = "INSERT INTO HOST_INFO_SNAP_TEMP " +
					"(HOST_NAME,HOST_ID,YEAR,MONTH,DAY,CPU_UT,CLUSTER_ID,CLUSTER_NAME," +
					"HYPERVISOR,CPU_NUM,CPU_HZ,CPU_USED,MEM_ALLOCATED,MEM_TOTAL,MEM_USED," +
					"DISCONNECTED,CAPABILITY,IP_ADDRESS,ISLOCALDISK,DISK_TOTAL,DISK_ALLOCAGTED," +
					"NETWORK_READ,NETWORK_WRITE,OS_NAME,HOST_TYPE,ZONE_ID,ZONE_NAME,POD_ID,POD_NAME,STATE) " +
					"SELECT HOST_NAME,HOST_ID,substring(TIME_STAMP,1,4),substring(TIME_STAMP,1,7),substring(TIME_STAMP,1,10)," +
					"CPU_UT,CLUSTER_ID,CLUSTER_NAME,HYPERVISOR,CPU_NUM,CPU_HZ,CPU_USED,MEM_ALLOCATED,MEM_TOTAL,MEM_USED," +
					"DISCONNECTED,CAPABILITY,IP_ADDRESS,ISLOCALDISK,DISK_TOTAL,DISK_ALLOCAGTED," +
					"NETWORK_READ,NETWORK_WRITE,OS_NAME,HOST_TYPE,ZONE_ID,ZONE_NAME,POD_ID,POD_NAME,STATE FROM HOST_INFO_SNAP " +
					"WHERE TIME_STAMP >= ? AND TIME_STAMP <=?";
			System.out.println(insertSql);
			
			if(deviceType.equals("VM") ||deviceType.equals("VMnetwork")){
				deleteSql = "DELETE FROM VM_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO VM_INFO_SNAP_TEMP " +
						"(VM_NAME,VM_ID,YEAR,MONTH,DAY,DOMAIN_ID,DOMAIN_NAME,GROUP_ID," +
						"GROUP_NAME,HYPERVISOR,HOST_NAME,CPU_NUM,CPU_HZ,CPU_UT,CREATE_TIME,MEM_ALLOCATED,MEM_TOTAL,MEM_USED," +
						"TEMPLATE_NAME,CAPABILITY,IP_ADDRESS,HARDDISK_READ,HARDDISK_WRITE,NETWORK_READ,NETWORK_WRITE,OS_NAME,VM_TYPE," +
						"ZONE_ID,ZONE_NAME,STATE) SELECT VM_NAME,VM_ID,substring(TIME_STAMP,1,4),substring(TIME_STAMP,1,7),substring(TIME_STAMP,1,10)," +
						"DOMAIN_ID,DOMAIN_NAME,GROUP_ID,GROUP_NAME,HYPERVISOR,HOST_NAME,CPU_NUM,CPU_HZ,CPU_UT,CREATE_TIME,MEM_ALLOCATED," +
						"MEM_TOTAL,MEM_USED,TEMPLATE_NAME,CAPABILITY,IP_ADDRESS,HARDDISK_READ,HARDDISK_WRITE,NETWORK_READ,NETWORK_WRITE," +
						"OS_NAME,VM_TYPE,ZONE_ID,ZONE_NAME,STATE FROM VM_INFO_SNAP "+
						"WHERE TIME_STAMP >= ? AND TIME_STAMP <=?";
				System.out.println(insertSql);
			}
			if(deviceType.equals("onestorage")){
				deleteSql = "DELETE FROM STORAGE_ONE_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO STORAGE_ONE_INFO_SNAP_TEMP " +
						"SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM STORAGE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <=? AND TYPE=2";
				System.out.println(insertSql);
			}
			if(deviceType.equals("onestorageUsability")){
				deleteSql = "DELETE FROM STORAGE_ONE_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO STORAGE_ONE_INFO_SNAP_TEMP " +
						"SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM STORAGE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <? AND TYPE=2";
				System.out.println(insertSql);
			}
			if(deviceType.equals("twostorage")){
				deleteSql = "DELETE FROM STORAGE_TWO_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO STORAGE_TWO_INFO_SNAP_TEMP " +
						"(TIME_STAMP,YEAR,MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME) SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM STORAGE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <=? AND TYPE=6";
				System.out.println(insertSql);
			}
			if(deviceType.equals("twostorageUsability")){
				deleteSql = "DELETE FROM STORAGE_TWO_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO STORAGE_TWO_INFO_SNAP_TEMP " +
						"(TIME_STAMP,YEAR,MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME) SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM STORAGE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <? AND TYPE=6";
				System.out.println(insertSql);
			}
			if(deviceType.equals("service")){
				deleteSql = "DELETE FROM SERVICE_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO SERVICE_INFO_SNAP_TEMP " +
						"(TIME_STAMP,YEAR,QUA,MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME) SELECT TIME_STAMP,YEAR,QUARTER(TIME_STAMP),MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM SERVICE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <=?";
				System.out.println(insertSql);
			}
			if(deviceType.equals("servicecpu")){
				deleteSql = "DELETE FROM SERVICE_CPU_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO SERVICE_CPU_INFO_SNAP_TEMP " +
						"(TIME_STAMP,YEAR,QUA,MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME) SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),QUARTER(TIME_STAMP),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM SERVICE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <? AND TYPE=1";
				System.out.println(insertSql);
			}
			if(deviceType.equals("servicemem")){
				deleteSql = "DELETE FROM SERVICE_MEM_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO SERVICE_MEM_INFO_SNAP_TEMP " +
						"(TIME_STAMP,YEAR,QUA,MONTH,DAY,TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME) SELECT TIME_STAMP,SUBSTRING(TIME_STAMP,1,4),QUARTER(TIME_STAMP),SUBSTRING(TIME_STAMP,1,7),SUBSTRING(TIME_STAMP,1,10),TYPE,ZONE_ID,ZONE_NAME," +
						"POD_ID,POD_NAME,CAPACITY_USED,CAPACITY_TOTAL,PERCENT_USED,NAME FROM SERVICE_INFO_SNAP WHERE TIME_STAMP >= ? AND TIME_STAMP <? AND TYPE=0";
				System.out.println(insertSql);
			}
//			if(deviceType.equals("tousu")){
//				deleteSql = "DELETE FROM ATZKHTOUSU";
//				insertSql = "INSERT INTO ATZKHTOUSU " +
//						"SELECT substring(TS.TOUSUSJ,1,4),concat(QUARTER(TS.TOUSUSJ),'季度'),substring(TS.TOUSUSJ,1,7),COUNT(*) " +
//						"FROM ATZTOUSUGD TS " +
//						"WHERE TS.TOUSUSJ >= ? AND TS.TOUSUSJ <= ? " +
//						"GROUP BY substring(TS.TOUSUSJ,1,7)";
//				System.out.println(insertSql);
//
//			}
			if(deviceType.equals("tousu")){
			deleteSql = "DELETE FROM ATZKHTOUSU";
			insertSql = "INSERT INTO ATZKHTOUSU " +
					"SELECT to_char(TS.TOUSUSJ,'yyyy'),to_char(TS.TOUSUSJ,'q')||'季度',to_char(TS.TOUSUSJ,'yyyy-mm'),COUNT(*) " +
					"FROM ATZTOUSUGD TS " +
					"WHERE TS.TOUSUSJ >=to_date(?,'yyyy-mm-dd') AND TS.TOUSUSJ< to_date(?,'yyyy-mm-dd') " +
					"GROUP BY to_char(TS.TOUSUSJ,'yyyy'),to_char(TS.TOUSUSJ,'q')||'季度',to_char(TS.TOUSUSJ,'yyyy-mm')";
			System.out.println(insertSql);

	    	}
			if(deviceType.equals("cpu")){
				deleteSql = "DELETE FROM CPUTOPTEN";
				insertSql = "INSERT INTO CPUTOPTEN" +
						"(HOST_NAME,FW) " +
						"SELECT H.HOST_NAME ,AVG(H.CPU_UT) " +
						"FROM HOST_INFO_SNAP H " +
						"WHERE H.TIME_STAMP>=? AND H.TIME_STAMP<=? " +
						"GROUP BY HOST_ID ORDER BY AVG(H.CPU_UT) DESC";
				System.out.println(insertSql);

			}
			if(deviceType.equals("mem")){
				deleteSql = "DELETE FROM CPUTOPTEN";
				insertSql = "INSERT INTO CPUTOPTEN" +
						"(HOST_NAME,FW) " +
						"SELECT H.HOST_NAME ,AVG(H.MEM_USED) " +
						"FROM HOST_INFO_SNAP H " +
						"WHERE H.TIME_STAMP>=? AND H.TIME_STAMP<=? " +
						"GROUP BY HOST_ID ORDER BY AVG(H.MEM_USED) DESC";
				System.out.println(insertSql);

			}
			if(deviceType.equals("nwread")){
				deleteSql = "DELETE FROM CPUTOPTEN";
				insertSql = "INSERT INTO CPUTOPTEN" +
						"(HOST_NAME,FW) " +
						"SELECT H.HOST_NAME ,AVG(H.NETWORK_READ) " +
						"FROM HOST_INFO_SNAP H " +
						"WHERE H.TIME_STAMP>=? AND H.TIME_STAMP<=? " +
						"GROUP BY HOST_ID ORDER BY AVG(H.NETWORK_READ) DESC";
				System.out.println(insertSql);

			}
			if(deviceType.equals("nwwrite")){
				deleteSql = "DELETE FROM CPUTOPTEN";
				insertSql = "INSERT INTO CPUTOPTEN" +
						"(HOST_NAME,FW) " +
						"SELECT H.HOST_NAME ,AVG(H.NETWORK_WRITE) " +
						"FROM HOST_INFO_SNAP H " +
						"WHERE H.TIME_STAMP>=? AND H.TIME_STAMP<=? " +
						"GROUP BY HOST_ID ORDER BY AVG(H.NETWORK_WRITE) DESC";
				System.out.println(insertSql);

			}
			if(deviceType.equals("users")){
				deleteSql = "DELETE FROM T_SCS_USER_SNAP_TEMP";
				insertSql = "INSERT INTO T_SCS_USER_SNAP_TEMP " +
						"SELECT YEAR,MONTH,DAY,USERS_NUM,LAST_USERS_NUM " +
						"FROM T_SCS_USER_SNAP WHERE DAY>=? AND DAY<?";
				System.out.println(insertSql);

			}
			if(deviceType.equals("order")){
				deleteSql = "DELETE FROM T_SCS_ORDER_INFO_SNAP_TEMP";
				insertSql = "INSERT INTO T_SCS_ORDER_INFO_SNAP_TEMP " +
						"SELECT SUBSTRING(O.CREATE_DT,1,4) YEAR,SUBSTRING(O.CREATE_DT,1,7) MONTH,SUBSTRING(O.CREATE_DT,1,10) DAY," +
						"INS.TEMPLATE_TYPE,CASE INS.TEMPLATE_TYPE WHEN 1 THEN '虚拟机和块存储' WHEN 2 THEN '小型机' WHEN 3 THEN 'X86物理机' WHEN 4 THEN '备份服务' WHEN 5 THEN '监控服务' WHEN 6 THEN '负载均衡服务' WHEN 7 THEN '防火墙资源服务' WHEN 8 THEN '带宽资源服务' WHEN 9 THEN '公网IP资源服务' WHEN 10 THEN '物理机' WHEN 15 THEN '数据云备份服务' END AS TYPE,COUNT(*) NUM " +
						"FROM T_SCS_INSTANCE_INFO AS INS INNER JOIN T_SCS_ORDER AS O ON INS.ORDER_ID =O.ORDER_ID " +
						"WHERE O.CREATE_DT>=? AND O.CREATE_DT<? AND O.CREATOR_USER_ID=? AND O.TYPE=1 GROUP BY SUBSTRING(O.CREATE_DT,1,4),SUBSTRING(O.CREATE_DT,1,7),SUBSTRING(O.CREATE_DT,1,10),INS.TEMPLATE_TYPE";
				System.out.println(insertSql);
			}
			if(deviceType.equals("unsubscribeorder")){
				deleteSql = "DELETE FROM T_SCS_UNSUBSCRIBE_ORDER_SNAP_TEMP";
				insertSql = "INSERT INTO T_SCS_UNSUBSCRIBE_ORDER_SNAP_TEMP " +
						"SELECT SUBSTRING(O.CREATE_DT,1,4) YEAR,SUBSTRING(O.CREATE_DT,1,7) MONTH,SUBSTRING(O.CREATE_DT,1,10) DAY," +
						"INS.TEMPLATE_TYPE,CASE INS.TEMPLATE_TYPE WHEN 1 THEN '虚拟机和块存储' WHEN 2 THEN '小型机' WHEN 3 THEN 'X86物理机' WHEN 4 THEN '备份服务' WHEN 5 THEN '监控服务' WHEN 6 THEN '负载均衡服务' WHEN 7 THEN '防火墙资源服务' WHEN 8 THEN '带宽资源服务' WHEN 9 THEN '公网IP资源服务' WHEN 10 THEN '物理机' WHEN 15 THEN '数据云备份服务' END AS TYPE,COUNT(*) NUM " +
						"FROM T_SCS_INSTANCE_INFO AS INS INNER JOIN T_SCS_ORDER AS O ON INS.ID =O.INSTANCE_INFO_ID " +
						"WHERE O.CREATE_DT>=? AND O.CREATE_DT<? AND O.CREATOR_USER_ID=? AND O.TYPE=3 GROUP BY SUBSTRING(O.CREATE_DT,1,4),SUBSTRING(O.CREATE_DT,1,7),SUBSTRING(O.CREATE_DT,1,10),INS.TEMPLATE_TYPE";
				System.out.println(insertSql);
			}
//			if(deviceType.equals("Satisfaction")){
//			    deleteSql = "DELETE FROM ATZKHMYD ";
//			    insertSql = "INSERT INTO ATZKHMYD " +
//			    		"SELECT OK.YEAR,QUARTER(concat(OK.MONTH,'-01')),OK.MONTH, ACOU,BCOU FROM(" +
//			    		"SELECT YEAR,MONTH,COUNT(*) ACOU FROM(" +
//			    		"SELECT substring(FW.BAOXIUSJ,1,4) YEAR,substring(FW.BAOXIUSJ,1,7) MONTH,FW.KHPJYJ VALUE FROM ATZFUWUGD FW WHERE FW.BAOXIUSJ>=? AND FW.BAOXIUSJ<=? " +
//			    		"UNION ALL " +
//			    		"SELECT substring(WX.BAOXIUSJ,1,4) YEAR,substring(WX.BAOXIUSJ,1,7) MONTH,WX.KHPJYJ VALUE FROM ATZWEIXIUGD WX WHERE WX.BAOXIUSJ>=? AND WX.BAOXIUSJ<=?) A " +
//			    		"where A.VALUE IN ('很满意','基本满意') GROUP BY YEAR,MONTH)OK," +
//			    		"(SELECT YEAR,MONTH,COUNT(*) BCOU FROM(" +
//			    		"SELECT substring(FW.BAOXIUSJ,1,4) YEAR,substring(FW.BAOXIUSJ,1,7) MONTH,FW.KHPJYJ VALUE FROM ATZFUWUGD FW WHERE FW.BAOXIUSJ>=? AND FW.BAOXIUSJ<=? " +
//			    		"UNION ALL " +
//			    		"SELECT substring(WX.BAOXIUSJ,1,4) YEAR,substring(WX.BAOXIUSJ,1,7) MONTH,WX.KHPJYJ VALUE FROM ATZWEIXIUGD WX WHERE WX.BAOXIUSJ>=? AND WX.BAOXIUSJ<=?) A " +
//			    		"GROUP BY YEAR,MONTH)ZS " +
//			    		"WHERE OK.YEAR= ZS.YEAR " +
//			    		"AND OK.MONTH=ZS.MONTH ";
//			    		System.out.println(insertSql);
//			}
			if(deviceType.equals("Satisfaction")){
			    deleteSql = "DELETE FROM ATZKHMYD ";
			    insertSql = "INSERT INTO ATZKHMYD " +
			    		"SELECT OK.YEAR,to_char(to_date(OK.MONTH,'yyyy-mm'),'q'),OK.MONTH, ACOU,BCOU " +
			    		"FROM(SELECT YEAR,MONTH,COUNT(*) ACOU FROM(SELECT to_char(FW.BAOXIUSJ,'yyyy') YEAR,to_char(FW.BAOXIUSJ,'yyyy-mm') MONTH,FW.KHPJYJ VALUE " +
			    		"FROM ATZFUWUGD FW WHERE FW.BAOXIUSJ >=to_date(?,'yyyy-mm-dd') AND FW.BAOXIUSJ< to_date(? ,'yyyy-mm-dd') " +
			    		"UNION ALL " +
			    		"SELECT to_char(WX.BAOXIUSJ,'yyyy') YEAR,to_char(WX.BAOXIUSJ,'yyyy-mm') MONTH,WX.KHPJYJ VALUE " +
			    		"FROM ATZWEIXIUGD WX WHERE WX.BAOXIUSJ >=to_date(?,'yyyy-mm-dd') AND WX.BAOXIUSJ< to_date(?,'yyyy-mm-dd')) A " +
			    		"WHERE A.VALUE IN ('很满意','基本满意') GROUP BY YEAR,MONTH)OK,(SELECT YEAR,MONTH,COUNT(*) BCOU " +
			    		"FROM(SELECT to_char(FW.BAOXIUSJ,'yyyy') YEAR,to_char(FW.BAOXIUSJ,'yyyy-mm') MONTH,FW.KHPJYJ VALUE " +
			    		"FROM ATZFUWUGD FW WHERE FW.BAOXIUSJ >=to_date(?,'yyyy-mm-dd') AND FW.BAOXIUSJ< to_date(?,'yyyy-mm-dd') " +
			    		"UNION ALL " +
			    		"SELECT to_char(WX.BAOXIUSJ,'yyyy') YEAR,to_char(WX.BAOXIUSJ,'yyyy-mm') MONTH,WX.KHPJYJ VALUE " +
			    		"FROM ATZWEIXIUGD WX WHERE WX.BAOXIUSJ >=to_date(?,'yyyy-mm-dd') AND WX.BAOXIUSJ< to_date(?,'yyyy-mm-dd')) A GROUP BY YEAR,MONTH)ZS " +
			    		"WHERE OK.YEAR= ZS.YEAR AND OK.MONTH=ZS.MONTH";
			    		
			    		System.out.println(insertSql);
			}
			/*
			if(deviceType.equals("Satisfaction")||deviceType.equals("tousu")){
				//pstatForDelete = dlconn.prepareStatement(deleteSql);
				//pstatForDelete.execute();
				//pstatForInsert = dlconn.prepareStatement(insertSql);
				//pstatForInsert.setString(1, start);
				//pstatForInsert.setString(2, end);
				//if(deviceType.equals("Satisfaction")){
				//	pstatForInsert.setString(3, start);
				//	pstatForInsert.setString(4, end);
				//	pstatForInsert.setString(5, start);
				//	pstatForInsert.setString(6, end);
				//	pstatForInsert.setString(7, start);
				//	pstatForInsert.setString(8, end);
				}
				resultnum=pstatForInsert.executeUpdate();
			}else */if(deviceType.equals("users")||deviceType.equals("order")||deviceType.equals("unsubscribeorder")){
				pstatForDelete = vdcconn.prepareStatement(deleteSql);
				pstatForDelete.executeUpdate();
				
				pstatForInsert = vdcconn.prepareStatement(insertSql);
				pstatForInsert.setString(1, start);
				pstatForInsert.setString(2, end);
				if(deviceType.equals("order")||deviceType.equals("unsubscribeorder")){
					if(uid!=null){
						pstatForInsert.setInt(3, uid);
					}else{
						pstatForInsert.setInt(3, 0);
					}
				}
				resultnum=pstatForInsert.executeUpdate();
			}//else{
				//pstatForDelete = conn.prepareStatement(deleteSql);
				//pstatForDelete.executeUpdate();
				
				//pstatForInsert = conn.prepareStatement(insertSql);
				//pstatForInsert.setString(1, start);
				//pstatForInsert.setString(2, end);
				//resultnum=pstatForInsert.executeUpdate();
			//}
			
			
			
		} catch (Exception e) {
			try {
				//conn.rollback();
				//dlconn.rollback();
				vdcconn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		} finally {
			DBUtils.closePreparedStatement(pstatForDelete);
			DBUtils.closePreparedStatement(pstatForInsert);
			//DBUtils.closeConnection(conn);
			//DBUtils.closeConnection(dlconn);
			DBUtils.closeConnection(vdcconn);
		}
		return resultnum;
	}

}
