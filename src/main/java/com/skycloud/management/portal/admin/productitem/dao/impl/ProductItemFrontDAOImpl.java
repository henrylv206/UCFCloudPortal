package com.skycloud.management.portal.admin.productitem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemFrontDAO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;
import com.skycloud.management.portal.admin.productitem.entity.TProductItemFrontBO;

public class ProductItemFrontDAOImpl extends SpringJDBCBaseDao implements IProductItemFrontDAO {
	private static Log log = LogFactory.getLog(ProductItemFrontDAOImpl.class);

	private static final class ProductItemFrontMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			TProductItemFrontBO item = new TProductItemFrontBO();
			item.setId(rs.getInt("id"));
			item.setCode(rs.getString("code"));
			item.setName(rs.getString("name"));
			item.setParentId(rs.getInt("parent_id"));
			item.setLevel(rs.getInt("level"));
			item.setNodeType(rs.getInt("node_type"));
			if(null != (rs.getObject("state"))){
				item.setState(rs.getInt("state"));
			}
			
			return item;
		}
		
	}
	
	@Override
	public int createProductItemFront(final TProductItemFrontBO item) throws Exception {
		final String sql = "INSERT INTO T_SCS_PRODUCT_ITEM_FRONT(id,CODE,NAME,LEVEL,STATE,PARENT_ID,NODE_TYPE) VALUES (?,?,?,?,?,?,?)";
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
				return ps;
			}
		});
//		return keyHolder.getKey().intValue();
		return item.getId();
	}

	@Override
	public void deleteProductItemFront(int id) throws Exception {
		String sql = "";
		if(id>=0){
			sql = "DELETE FROM T_SCS_PRODUCT_ITEM_FRONT WHERE id=?";
			getJdbcTemplate().update(sql, id);
		}else{
			sql = "DELETE FROM T_SCS_PRODUCT_ITEM_FRONT";
			getJdbcTemplate().update(sql);
		}
		
	}

	@Override
	public TProductItemFrontBO getProductItemFrontById(int id) throws Exception {
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM_FRONT WHERE id=?";
		TProductItemFrontBO item = null;
		try {
			item  = (TProductItemFrontBO) getJdbcTemplate().queryForObject(sql, new ProductItemFrontMapper(), id);
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
	public List<TProductItemFrontBO> listProductItemFront(int parentId) throws Exception{
		List<TProductItemFrontBO> list = new ArrayList<TProductItemFrontBO>();
		List<Object> param = new ArrayList<Object>();
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("SELECT * FROM T_SCS_PRODUCT_ITEM_FRONT t where 1=1 ");
		if(parentId>=0){
			sbuilder.append(" and t.PARENT_ID=? ");
			param.add(parentId);
		}
		list  = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),new ProductItemFrontMapper());
		return list;
	}

	
	public int isItemAduited() throws Exception{
		String sql = "SELECT count(1) from T_SCS_PRODUCT_ITEM_FRONT t";
		int count = getJdbcTemplate().queryForInt(sql);
		return count;
	}
	
	public TProductItemFrontBO getProductItemByName(final String key) throws Exception{
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM_FRONT WHERE NAME=? ";
		TProductItemFrontBO item = null;
		try {
			List<TProductItemFrontBO> list  = getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			}, new ProductItemFrontMapper());
			if(null!=list&&!list.isEmpty()){
				item = list.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
		    return null;
		}
		return item;
	}
	public TProductItemFrontBO getProductItemByCode(final String key) throws Exception{
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM_FRONT WHERE CODE=? ";
		TProductItemFrontBO item = null;
		try {
			List<TProductItemFrontBO> list  = getJdbcTemplate().query(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(1, key);
				}
			}, new ProductItemFrontMapper());
			if(null!=list&&!list.isEmpty()){
				item = list.get(0);
			}
		} catch (EmptyResultDataAccessException e) {
		    return null;
		}
		return item;
	}
}
