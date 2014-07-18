package com.skycloud.tezz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skycloud.tezz.commons.Pagination;
import com.skycloud.tezz.db.DBUtils;
import com.skycloud.tezz.model.ToptenInfo;

public class ToptenDAO {
	public List<ToptenInfo> getToptenInfoList(String start, String end,
			String type,Pagination page) {
		// TODO Auto-generated method stub
		List<ToptenInfo> list=new ArrayList<ToptenInfo>();
		Connection conn = null;
		PreparedStatement pst = null;
		try {
			String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
			conn = DBUtils.getConnection(filePath,1);
			conn.setAutoCommit(false);
			String sql="";
			if("cpu".equals(type)){
				sql="SELECT H.HOST_NAME ,AVG(H.CPU_UT) FROM HOST_INFO_SNAP H WHERE H.TIME_STAMP>? AND H.TIME_STAMP<? GROUP BY HOST_ID ORDER BY AVG(H.CPU_UT)";
			}
			if("mem".equals(type)){
				sql="SELECT H.HOST_NAME ,AVG(H.MEM_USED) FROM HOST_INFO_SNAP H WHERE H.TIME_STAMP>? AND H.TIME_STAMP<? GROUP BY HOST_ID ORDER BY AVG(H.MEM_USED) DESC";
			}
			if("nwread".equals(type)){
				sql="SELECT H.HOST_NAME ,AVG(H.NETWORK_READ) FROM HOST_INFO_SNAP H WHERE H.TIME_STAMP>? AND H.TIME_STAMP<? GROUP BY HOST_ID ORDER BY AVG(H.NETWORK_READ) DESC";
			}
			if("nwwrite".equals(type)){
				sql="SELECT H.HOST_NAME ,AVG(H.NETWORK_WRITE) FROM HOST_INFO_SNAP H WHERE H.TIME_STAMP>? AND H.TIME_STAMP<? GROUP BY HOST_ID ORDER BY AVG(H.NETWORK_WRITE) DESC";
			}
			System.out.println(sql);
			pst=conn.prepareStatement(sql);
			pst.setString(1, start);
			pst.setString(2, end);
//			pst.setInt(3, (page.getPage()-1)*page.getPageSize());
//			pst.setInt(4, page.getPageSize()-1);
			ResultSet rs= pst.executeQuery();
			while(rs.next()){
				ToptenInfo info =new ToptenInfo();
				info.setHostName(rs.getString(1));
				info.setFw(rs.getDouble(2));
				list.add(info);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}	finally {
			DBUtils.closePreparedStatement(pst);
			DBUtils.closeConnection(conn);
		}
		
		return list;
	}
}
