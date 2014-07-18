package com.skycloud.management.portal.front.customer.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import org.apache.axis2.addressing.AddressingConstants.Final;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;



import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.customer.dao.ICompanyDAO;
import com.skycloud.management.portal.front.customer.entity.CompanyCheckStateEnum;
import com.skycloud.management.portal.front.customer.entity.TCompanyInfo;

public class CompanyDAOImpl extends SpringJDBCBaseDao implements ICompanyDAO {
	
	@Override
	public int saveOrUpdateComp(final TCompanyInfo tcompanyinfo)throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();
//		final String sql = "insert into T_SCS_COMPANY_INFO (COMP_ORG_CODE,COMP_CN_NAME,"
//			+" COMP_EN_NAME,COMP_CN_ABBREVIATION,COMP_EN_ABBREVIATION,BUS_LICENSE_NUM,"
//			+" BLN_START_TIME,BLN_END_TIME,COMP_LEGAL_PERSON,COMP_LEGAL_PERSON_ID,CITY_ID,"
//			+" COMP_CATEGORY_ID,COMP_PROPERTY_ID,COMP_ADDRESS,POST_CODE,COMP_PHONE,"
//			+" COMP_FAX,COMP_EMAIL,COMP_BANK_NAME,COMP_BANK_ACCOUNT,RELATION_NAME,"
//			+" RELA_DEP_NAME,RELA_PHONE,RELA_MOBILE,RELA_FAX,RELA_EMAIL,COMP_CLASS_ID,COMP_REG_TIME,"
//			+" MANAGER,COMP_CREATER,COMP_CREATE_TIME,"
//			+" CHECK_STATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";	
//
//		try {
//			this.getJdbcTemplate().update(new PreparedStatementCreator(){
//				int i=1;
//				@Override
//				public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
//					PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//					ps.setString(i++, tcompanyinfo.getCompOrgCode());
//					ps.setString(i++, tcompanyinfo.getCompCnName());
//					ps.setString(i++, tcompanyinfo.getCompEnName());
//					ps.setString(i++, tcompanyinfo.getCompCnAbbreviation());
//					ps.setString(i++, tcompanyinfo.getCompEnAbbreviation());
//					ps.setString(i++, tcompanyinfo.getBusLicenseNum());
//					ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnStartTime().getTime()));
//					ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnEndTime().getTime()));
//					ps.setString(i++, tcompanyinfo.getCompLegalPerson());
//					ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
//					ps.setInt(i++, tcompanyinfo.getTCity().getCityId());
//					ps.setInt(i++, tcompanyinfo.getTCategory().getCategoryId());
//					ps.setInt(i++, tcompanyinfo.getTProperty().getPropertyId());
//					ps.setString(i++, tcompanyinfo.getCompAddress());
//					ps.setString(i++, tcompanyinfo.getPostCode());
//					ps.setString(i++, tcompanyinfo.getCompPhone());
//					ps.setString(i++, tcompanyinfo.getCompFax());
//					ps.setString(i++, tcompanyinfo.getCompEmail());
//					ps.setString(i++, tcompanyinfo.getCompBankName());
//					ps.setString(i++, tcompanyinfo.getCompBankAccount());
//					ps.setString(i++, tcompanyinfo.getRelationName());
//					ps.setString(i++, tcompanyinfo.getRelaDepName());
//					ps.setString(i++, tcompanyinfo.getRelaPhone());
//					ps.setString(i++, tcompanyinfo.getRelaMobile());
//					ps.setString(i++, tcompanyinfo.getRelaFax());
//					ps.setString(i++, tcompanyinfo.getRelaEmail());
//					ps.setInt(i++, tcompanyinfo.getTClass().getClassId());
//					ps.setTimestamp(i++, tcompanyinfo.getCompRegTime());
//					ps.setString(i++, tcompanyinfo.getManager());
//					ps.setString(i++, tcompanyinfo.getCompCreater());
//					ps.setTimestamp(i++,new Timestamp(System.currentTimeMillis()));
//					ps.setInt(i++, CompanyCheckStateEnum.SUCCESS);
//					return ps;
//				}
//			}, keyHolder); 
			
			final String sql = "insert into T_SCS_COMPANY_INFO (COMP_ORG_CODE,COMP_CN_NAME,"
				+" BUS_LICENSE_NUM,"
				+" BLN_START_TIME,BLN_END_TIME,COMP_LEGAL_PERSON,COMP_LEGAL_PERSON_ID,CITY_ID,"
				+" COMP_CATEGORY_ID,COMP_PROPERTY_ID,COMP_ADDRESS,COMP_PHONE,"
				+" COMP_BANK_NAME,COMP_BANK_ACCOUNT,COMP_CLASS_ID,COMP_REG_TIME,"
				+" MANAGER,COMP_CREATER,COMP_CREATE_TIME, "
				+" CHECK_STATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";	

			try {
				this.getJdbcTemplate().update(new PreparedStatementCreator(){
					int i=1;
					@Override
					public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						ps.setString(i++, tcompanyinfo.getCompOrgCode());
						ps.setString(i++, tcompanyinfo.getCompCnName());
						ps.setString(i++, tcompanyinfo.getBusLicenseNum());
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnStartTime().getTime()));
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnEndTime().getTime()));
						ps.setString(i++, tcompanyinfo.getCompLegalPerson());
						ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
						ps.setInt(i++, tcompanyinfo.getTCity().getCityId());
						ps.setInt(i++, tcompanyinfo.getTCategory().getCategoryId());
						ps.setInt(i++, tcompanyinfo.getTProperty().getPropertyId());
						ps.setString(i++, tcompanyinfo.getCompAddress());
						ps.setString(i++, tcompanyinfo.getCompPhone());
						ps.setString(i++, tcompanyinfo.getCompBankName());
						ps.setString(i++, tcompanyinfo.getCompBankAccount());
						ps.setInt(i++, tcompanyinfo.getTClass().getClassId());
						ps.setTimestamp(i++, tcompanyinfo.getCompRegTime());
						ps.setString(i++, tcompanyinfo.getManager());
						ps.setString(i++, tcompanyinfo.getCompCreater());
						ps.setTimestamp(i++,new Timestamp(System.currentTimeMillis()));
						ps.setInt(i++, CompanyCheckStateEnum.SUCCESS);
						return ps;
					}
				}, keyHolder); 
		} catch (Exception e) {		
			e.printStackTrace();
			throw new SQLException("创建企业失败。");
		}
		return keyHolder.getKey().intValue();
	}

//保存selfcare用户和公司信息	
	@Override
	public int saveComp(final TCompanyInfo tcompanyinfo)throws Exception {
		KeyHolder keyHolder = new GeneratedKeyHolder();			
		final String sql = "insert into T_SCS_COMPANY_INFO (COMP_LEGAL_PERSON,COMP_CN_NAME,"
			+"COMP_LEGAL_PERSON_ID,"
			+" CITY_ID,COMP_PROPERTY_ID,"
			+" COMP_CATEGORY_ID,COMP_CLASS_ID,COMP_ADDRESS,POST_CODE,"
			+" COMP_PHONE,RELA_MOBILE,COMP_FAX,COMP_EMAIL,"
			+" COMP_ORG_CODE,BUS_LICENSE_NUM,BLN_START_TIME, "
			+" BLN_END_TIME,COMP_BANK_NAME,COMP_BANK_ACCOUNT) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";

			try {
				this.getJdbcTemplate().update(new PreparedStatementCreator(){
					int i=1;
					@Override
					public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
						PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
						ps.setString(i++, tcompanyinfo.getCompLegalPerson());	
						ps.setString(i++, tcompanyinfo.getCompCnName());						
						ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
						ps.setInt(i++, tcompanyinfo.getTCity().getCityId());	
						ps.setInt(i++, tcompanyinfo.getTProperty().getPropertyId());
						ps.setInt(i++, tcompanyinfo.getTCategory().getCategoryId());
						ps.setInt(i++, tcompanyinfo.getTClass().getClassId());
						ps.setString(i++, tcompanyinfo.getCompAddress());
						ps.setString(i++, tcompanyinfo.getPostCode());
						ps.setString(i++, tcompanyinfo.getCompPhone());
						ps.setString(i++, tcompanyinfo.getRelaMobile());
						ps.setString(i++, tcompanyinfo.getCompFax());
						ps.setString(i++, tcompanyinfo.getCompEmail());
						ps.setString(i++, tcompanyinfo.getCompOrgCode());
						ps.setString(i++, tcompanyinfo.getBusLicenseNum());
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnStartTime().getTime()));
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnEndTime().getTime()));
						ps.setString(i++, tcompanyinfo.getCompBankName());
						ps.setString(i++, tcompanyinfo.getCompBankAccount());
						return ps;
					}
				}, keyHolder); 
		} catch (Exception e) {		
			e.printStackTrace();
			throw new SQLException("创建企业失败。");
		}
		return keyHolder.getKey().intValue();
	}	
	//插入COMP_ID		
			public int insertCompID(final int userId,final int compId) throws Exception {
				final String sql = "update T_SCS_USER set COMP_ID = ? where ID=? ";
			      	int result = 0;
					try {
						result=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
							int i = 1;
							public void setValues(PreparedStatement ps) throws SQLException {
								ps.setInt(i++, compId);
								ps.setInt(i++, userId);
						//		System.out.println("userId"+userId);	
						//		System.out.println("compId"+compId);	
							}
						}); 
				} catch (Exception e) {		
					e.printStackTrace();
					throw new SQLException("修改个人信息失败。");
				}
					return result;
				}
	@Override
	public TCompanyInfo getTCompanyInfoById(final int id) {
		String sql = "select COMP_ID, COMP_ORG_CODE,COMP_CN_NAME,"
				+" BUS_LICENSE_NUM,"
				+" BLN_START_TIME,BLN_END_TIME,COMP_LEGAL_PERSON,COMP_LEGAL_PERSON_ID,CITY_ID,"
				+" COMP_CATEGORY_ID,COMP_PROPERTY_ID,COMP_ADDRESS,COMP_PHONE,"
				+" COMP_BANK_NAME,COMP_BANK_ACCOUNT,COMP_CLASS_ID,COMP_REG_TIME,"
				+" MANAGER,COMP_CREATER,COMP_CREATE_TIME, "
				+" CHECK_STATE from T_SCS_COMPANY_INFO where COMP_ID =  ?";
			
		List<TCompanyInfo> userList = null;
		TCompanyInfo user = null;
		BeanPropertyRowMapper<TCompanyInfo> userRowMapper = new BeanPropertyRowMapper<TCompanyInfo>(TCompanyInfo.class);
		try {				
			try {
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);						
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询企业失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			}				
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	//查询selfcare个人信息
	public TCompanyInfo getTCompInfoById(final int id) {
		String sql = "select COMP_ID, COMP_LEGAL_PERSON ,COMP_CN_NAME,"
				+" COMP_LEGAL_PERSON_ID,"
				+" CITY_ID,COMP_PROPERTY_ID,COMP_CATEGORY_ID,COMP_CLASS_ID,COMP_ADDRESS,"
				+" POST_CODE,COMP_PHONE,RELA_MOBILE,"
				+" COMP_FAX,COMP_EMAIL,COMP_ORG_CODE,BUS_LICENSE_NUM,"
				+" BLN_START_TIME,BLN_END_TIME, COMP_BANK_NAME,"
				+" COMP_BANK_ACCOUNT from T_SCS_COMPANY_INFO where COMP_ID =  ?";
			
		List<TCompanyInfo> userList = null;
		TCompanyInfo user = null;
		BeanPropertyRowMapper<TCompanyInfo> userRowMapper = new BeanPropertyRowMapper<TCompanyInfo>(TCompanyInfo.class);
		try {				
			try {
			//	System.out.println("dao:getTCompInfoById");
				userList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, id);						
					}
				}, userRowMapper);
			} catch (Exception e) {
				throw new SQLException("查询企业失败。失败原因：" + e.getMessage());
			}
			if (userList != null && userList.size() > 0){
				user = userList.get(0);
				return user;
			}				
			else
				return null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	@Override
	//to fix 1414
	public int updateCompany(final TCompanyInfo tcompanyinfo) throws Exception {
      String sql = "update T_SCS_COMPANY_INFO set COMP_ORG_CODE=?,COMP_CN_NAME=?,"
			+" BUS_LICENSE_NUM=?,COMP_ADDRESS=?,"
			+" BLN_START_TIME=?,BLN_END_TIME=?,COMP_LEGAL_PERSON=?,COMP_LEGAL_PERSON_ID=?,"
			+" COMP_BANK_NAME=?,COMP_BANK_ACCOUNT=?"
			+" WHERE COMP_ID=? ";	
      	int result = 0;
		try {
			result=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
				int i = 1;
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, tcompanyinfo.getCompOrgCode());
					ps.setString(i++, tcompanyinfo.getCompCnName());
					ps.setString(i++, tcompanyinfo.getBusLicenseNum());
					ps.setString(i++, tcompanyinfo.getCompAddress());
					ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnStartTime().getTime()));
					ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnEndTime().getTime()));
					ps.setString(i++, tcompanyinfo.getCompLegalPerson());
					ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
					ps.setString(i++, tcompanyinfo.getCompBankName());
					ps.setString(i++, tcompanyinfo.getCompBankAccount());
					ps.setInt(i++, tcompanyinfo.getCompId());
				}
			}); 
	} catch (Exception e) {		
		e.printStackTrace();
		throw new SQLException("修改企业信息失败。");
	}
		return result;
	}
	public int updateCompInfo(final TCompanyInfo tcompanyinfo) throws Exception {
	      String sql = "update T_SCS_COMPANY_INFO set COMP_LEGAL_PERSON=?,COMP_CN_NAME=?,"
				+" COMP_LEGAL_PERSON_ID=?,"
				+" CITY_ID=?,COMP_PROPERTY_ID=?,COMP_CATEGORY_ID=?,COMP_CLASS_ID=?,COMP_ADDRESS=?,"
				+" POST_CODE=?,COMP_PHONE=?,RELA_MOBILE=?,"
				+" COMP_FAX=?,COMP_EMAIL=?,COMP_ORG_CODE=?,BUS_LICENSE_NUM=?,"
				+" BLN_START_TIME=?,BLN_END_TIME=?, COMP_BANK_NAME=?,"
				+" COMP_BANK_ACCOUNT=?"
				+" WHERE COMP_ID=? ";	
	      	int result = 0;
			try {
			//	System.out.println("dao:updateCompInfo");
				result=this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {
					int i = 1;
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setString(i++, tcompanyinfo.getCompLegalPerson());	
						ps.setString(i++, tcompanyinfo.getCompCnName());						
						ps.setString(i++, tcompanyinfo.getCompLegalPersonId());
						ps.setInt(i++, tcompanyinfo.getTCity().getCityId());	
						ps.setInt(i++, tcompanyinfo.getTProperty().getPropertyId());
						ps.setInt(i++, tcompanyinfo.getTCategory().getCategoryId());
						ps.setInt(i++, tcompanyinfo.getTClass().getClassId());
						ps.setString(i++, tcompanyinfo.getCompAddress());
						ps.setString(i++, tcompanyinfo.getPostCode());
						ps.setString(i++, tcompanyinfo.getCompPhone());
						ps.setString(i++, tcompanyinfo.getRelaMobile());
						ps.setString(i++, tcompanyinfo.getCompFax());
						ps.setString(i++, tcompanyinfo.getCompEmail());
						ps.setString(i++, tcompanyinfo.getCompOrgCode());
						ps.setString(i++, tcompanyinfo.getBusLicenseNum());
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnStartTime().getTime()));
						ps.setTimestamp(i++, new Timestamp(tcompanyinfo.getBlnEndTime().getTime()));
						ps.setString(i++, tcompanyinfo.getCompBankName());
						ps.setString(i++, tcompanyinfo.getCompBankAccount());
						ps.setInt(i++, tcompanyinfo.getCompId());
					}
				}); 
		} catch (Exception e) {		
			e.printStackTrace();
			throw new SQLException("修改个人信息失败。");
		}
			return result;
		}
}
