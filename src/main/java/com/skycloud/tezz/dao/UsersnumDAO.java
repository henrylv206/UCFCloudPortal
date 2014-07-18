package com.skycloud.tezz.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.skycloud.tezz.db.DBUtils;
import com.skycloud.tezz.model.Users;

public class UsersnumDAO {
	public int copyUsers(){
		int resultnum=0;
		Connection conn = null;
		PreparedStatement pstatForInsert = null;
		PreparedStatement pstatForDelete = null;
		String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
		String deleteSql = "DELETE FROM T_SCS_USER_SNAP";
		String insertSql = "INSERT INTO T_SCS_USER_SNAP " +
				"(YEAR,MONTH,DAY,USERS_NUM) SELECT SUBSTRING(T1.CREATE_DT,1,4)YEAR,SUBSTRING(T1.CREATE_DT,1,7) MONTH,SUBSTRING(T1.CREATE_DT,1,10)DAY,COUNT(*) USERS " +
				"FROM T_SCS_USER T1 GROUP BY SUBSTRING(CREATE_DT,1,7)";
		try {
			conn=DBUtils.getConnection(filePath,3);
			pstatForDelete = conn.prepareStatement(deleteSql);
			pstatForDelete.executeUpdate();
			
			pstatForInsert = conn.prepareStatement(insertSql);
			resultnum=pstatForInsert.executeUpdate();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			try {
				conn.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		} finally {
			DBUtils.closePreparedStatement(pstatForInsert);
			DBUtils.closePreparedStatement(pstatForDelete);
			DBUtils.closeConnection(conn);
		}
		
		return resultnum;
	}
	
	public List<Users> selectUsersnum(){
		Connection conn = null;
		PreparedStatement pst = null;
		String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
		List<Users> ulist=new ArrayList<Users>();
		try {
			conn = DBUtils.getConnection(filePath,3);
			String sql="SELECT * FROM T_SCS_USER_SNAP";
			pst=conn.prepareStatement(sql);
			ResultSet rs=pst.executeQuery();
			while(rs.next()){
				Users users=new Users();
				users.setYear(rs.getString(1));
				users.setMonth(rs.getString(2));
				users.setDay(rs.getString(3));
				users.setUsersnum(rs.getInt(4));
				ulist.add(users);
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
		return ulist;
	}
	
	public void updateUsers(){
		List<Users> ulist=this.selectUsersnum();
		Connection conn = null;
		PreparedStatement pst = null;
		String filePath = getClass().getClassLoader().getResource("jdbc.properties").getPath();
		String sql="UPDATE T_SCS_USER_SNAP SET LAST_USERS_NUM=? WHERE DAY=?";
		try {
		if(ulist.size()>0){
			for(int i=0;i<ulist.size();i++){
				Users u=ulist.get(i);
				if(i==0){
					u.setLastusersnum(0);
				}else{
					u.setLastusersnum(ulist.get(i-1).getUsersnum());
				}
				conn = DBUtils.getConnection(filePath,3);
				pst=conn.prepareStatement(sql);
				pst.setInt(1, u.getLastusersnum());
				pst.setString(2, u.getDay());
				pst.executeUpdate();
			}
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
		
		
	}
	
}
