package com.skycloud.management.portal.admin.productitem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemDAO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemBO;

public class ProductItemDAOImpl extends SpringJDBCBaseDao implements IProductItemDAO {
	private static Log log = LogFactory.getLog(ProductItemDAOImpl.class);

	public static final class ProductItemMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			TProductItemBO item = new TProductItemBO();
			item.setId(rs.getInt("id"));
			item.setCode(rs.getString("code"));
			item.setName(rs.getString("name"));
			item.setParentId(rs.getInt("parent_id"));
			item.setLevel(rs.getInt("level"));
			item.setNodeType(rs.getInt("node_type"));
			item.setReleaseOrNot(rs.getInt("is_release"));
			if(null != (rs.getObject("state"))){
				item.setState(rs.getInt("state"));
			}
			
			return item;
		}
		
	}
	
	private static final class ProductItemXmlMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductItemXmlPO item = new ProductItemXmlPO();
			item.setId(rs.getInt("id"));
			item.setCode(rs.getString("code"));
			item.setName(rs.getString("name"));
			item.setParentId(rs.getInt("parent_id"));
			item.setLevel(rs.getInt("level"));
			return item;
		}
		
	}
	
	public int getId() throws Exception {
		return getJdbcTemplate().queryForInt("SELECT MAX(id) from T_SCS_PRODUCT_ITEM");
	}
	
	@Override
	public int createProductItem(final TProductItemBO item,final boolean fromXml) throws Exception {
		if(!fromXml){
			item.setId(this.getId()+1);
		}
		final String sql = "INSERT INTO T_SCS_PRODUCT_ITEM(ID,CODE,NAME,LEVEL,STATE,PARENT_ID,NODE_TYPE,IS_RELEASE,CHECK_RELEASE) VALUES (?,?,?,?,?,?,?,?,?)";
//		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator(){
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, item.getId());
				ps.setString(2, item.getCode());
				ps.setString(3, item.getName());
				ps.setInt(4, item.getLevel());
				ps.setInt(5, item.getState());
				ps.setInt(6, item.getParentId());
				ps.setInt(7, item.getNodeType());
				ps.setInt(8, item.getReleaseOrNot());
				ps.setInt(9, item.getCheckForRelease());
				return ps;
			}
		});
//		return keyHolder.getKey().intValue();
		return item.getId();
	}

	@Override
	public void deleteProductItem(int id) throws Exception {
		String sql = "";
		if(id>=0){
			sql = "DELETE FROM T_SCS_PRODUCT_ITEM WHERE id=?";
			getJdbcTemplate().update(sql, id);
		}else{
			//to fix bug:3260
			sql = "DELETE FROM T_SCS_PRODUCT_ITEM where id>18";
			getJdbcTemplate().update(sql);
		}
		
	}

	@Override
	public TProductItemBO getProductItemById(int id) throws Exception {
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM WHERE id=?";
		TProductItemBO item = null;
		try {
			item  = (TProductItemBO) getJdbcTemplate().queryForObject(sql, new ProductItemMapper(), id);
		} catch (EmptyResultDataAccessException e) {
		    return null;
		}
		return item;
	}

	/**
	 * frontOrNot 0-后台 1-前台
	 * release    0-未发布 1-发布
	 */
	@Override
	public List<TProductItemBO> listProductItem(int parentId,int release,int frontOrNot) throws Exception{
		List<TProductItemBO> list = new ArrayList<TProductItemBO>();
		List<Object> param = new ArrayList<Object>();
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("SELECT * FROM T_SCS_PRODUCT_ITEM t where 1=1 ");
		if(parentId>=0){
			sbuilder.append(" and t.PARENT_ID=? ");
			param.add(parentId);
		}
		if(release==1){
			if(frontOrNot==0){
				sbuilder.append(" and check_release=1 ");
			}else{
				sbuilder.append(" and IS_RELEASE=1 ");
			}
			
		}
		list  = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),new ProductItemMapper());
		return list;
	}
	
	public List<ProductItemXmlPO> listProductItemForExport() throws Exception{
		List<ProductItemXmlPO> list = new ArrayList<ProductItemXmlPO>();
		List<Object> param = new ArrayList<Object>();
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("SELECT * FROM T_SCS_PRODUCT_ITEM t where ");
//		if(parentId>=0){
			sbuilder.append(" t.id!=1 ");
//		}
//		if(release==1){
//			if(frontOrNot==0){
//				sbuilder.append(" and check_release=1 ");
//			}else{
//				sbuilder.append(" and IS_RELEASE=1 ");
//			}
//			
//		}
		list  = getJdbcTemplate().query(sbuilder.toString(),new ProductItemXmlMapper());
		return list;
	}

	@Override
	public void updateProductItem(int id,int check) throws Exception {
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("update T_SCS_PRODUCT_ITEM set IS_RELEASE=1 ");
//		String sql = "update T_SCS_PRODUCT_ITEM set IS_RELEASE=1 where id=?";
		if(check == 1){
			sbuilder.append(",check_release=1");
		}
		sbuilder.append(" where id=?");
		getJdbcTemplate().update(sbuilder.toString(), id);
	}
	
	/**
	 * 在重新发布之前把是否勾选状态,是否发布标志位全部恢复为0
	 */
	public void updateProductItem() throws Exception {
		String sql = "update T_SCS_PRODUCT_ITEM set IS_RELEASE=0,check_release=0 ";
		getJdbcTemplate().update(sql);
	}
	
	public void updateProductItem(int state) throws Exception {
		String sql = "update T_SCS_PRODUCT_ITEM set state=? where id=1";
		getJdbcTemplate().update(sql,state);
	}
	
	public int isItemReleased(int id) throws Exception{
		String sql = "SELECT count(1) from T_SCS_PRODUCT_ITEM t WHERE t.IS_RELEASE=1 and t.id=?";
		int count = getJdbcTemplate().queryForObject(sql, Integer.class, id);
		return count;
	}

	@Override
	public int updateProductItemInfo(TProductItemBO item) throws Exception {
		StringBuilder sbuilder = new StringBuilder();
		
		String name = item.getName();
		String code = item.getCode();
		Object[] args = null;
		int result = -1;
		if(null != code && !code.equals("")){
			sbuilder.append("update T_SCS_PRODUCT_ITEM ");
			sbuilder.append(" set CODE=? ");
			sbuilder.append(" where ID=? ");
			args = new Object[]{item.getCode(),item.getId()};
			result = getJdbcTemplate().update(sbuilder.toString(), args);
		}
		if(null != name && !name.equals("")){
			sbuilder.append("update T_SCS_PRODUCT_ITEM ");
			sbuilder.append(" set NAME=? ");
			sbuilder.append(" where ID=? ");
			args = new Object[]{item.getName(),item.getId()};
			result = getJdbcTemplate().update(sbuilder.toString(), args);
		}
		
				
		return result;
	}
	
	public TProductItemBO getProductItemByName(final String key) throws Exception{
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM WHERE NAME=? ";
		TProductItemBO item = null;
		try {
			List<TProductItemBO> list  = getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			}, new ProductItemMapper());
			if(null!=list&&!list.isEmpty()){
				item = list.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
		    return null;
		}
		return item;
	}
	public TProductItemBO getProductItemByCode(final String key) throws Exception{
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM WHERE CODE=? ";
		TProductItemBO item = null;
		try {
			List<TProductItemBO> list  = getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			}, new ProductItemMapper());
			if(null!=list&&!list.isEmpty()){
				item = list.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
		    return null;
		}
		return item;
	}
}
