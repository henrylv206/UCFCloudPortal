package com.skycloud.tezz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.skycloud.tezz.db.DBUtils;
import com.skycloud.tezz.model.UserInfo;

public class UserInfoDAO {
	public List<UserInfo> getAllUserList(){
		List<UserInfo> list = new ArrayList<UserInfo>();
		String sql="SELECT * FROM T_SCS_USER WHERE STATE=4";
		String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
		Connection conn=null;
		PreparedStatement pst = null;
		try {
			conn=DBUtils.getConnection(filePath,3);
			pst=conn.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				UserInfo info=new UserInfo();
				info.setId(rs.getInt("ID"));
				info.setName(rs.getString("NAME"));
				list.add(info);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}finally {
			DBUtils.closePreparedStatement(pst);
			DBUtils.closeConnection(conn);
		}
		return list;
	}
}
