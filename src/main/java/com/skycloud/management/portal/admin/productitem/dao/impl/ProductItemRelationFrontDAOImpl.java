package com.skycloud.management.portal.admin.productitem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemRelationFrontDAO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationFrontBO;
import com.skycloud.management.portal.admin.resmanage.entity.Product;

public class ProductItemRelationFrontDAOImpl extends SpringJDBCBaseDao implements
		IProductItemRelationFrontDAO {
	private static Log log = LogFactory
			.getLog(ProductItemRelationFrontDAOImpl.class);
	
	private static final RowMapperResultSetExtractor<Product> productMapperExtractor = new RowMapperResultSetExtractor<Product>(
			new BeanPropertyRowMapper<Product>(Product.class));


	private static final class ProductItemRelationFrontMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductItemRelationFrontBO item = new ProductItemRelationFrontBO();
			item.setId(rs.getInt("id"));
			item.setProductId(rs.getInt("PRODUCT_ID"));
			item.setProductItemId(rs.getInt("PRODUCT_ITEM_ID"));
			if (null != (rs.getObject("state"))) {
				item.setState(rs.getInt("state"));
			}

			return item;
		}

	}

	@Override
	public int createProductItemRelationFront(final ProductItemRelationFrontBO relation)
			throws Exception {
		final String sql = "INSERT INTO T_SCS_PRODUCT_ITEM_RELATION_FRONT(ID,PRODUCT_ID,PRODUCT_ITEM_ID,STATE) VALUES (?,?,?,?)";
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setInt(1, relation.getId());
				ps.setInt(2, relation.getProductId());
				ps.setInt(3, relation.getProductItemId());
				ps.setInt(4, relation.getState());
				return ps;
			}
		});
		return relation.getId();
	}

	public void deleteProductItemRelationFront() throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_SCS_PRODUCT_ITEM_RELATION_FRONT");
		getJdbcTemplate().update(sql.toString());
	}

//	@Override
//	public ProductItemRelationFrontBO getProductItemRelationById(int id)
//			throws Exception {
//		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM_RELATION_FRONT WHERE id=?";
//		ProductItemRelationFrontBO item = null;
//		try {
//			item = (ProductItemRelationFrontBO) getJdbcTemplate().queryForObject(
//					sql, new ProductItemRelationFrontMapper(), id);
//		} catch (EmptyResultDataAccessException e) {
//			return null;
//		}
//		return item;
//	}

	/**
	 * frontOrNot 0-后台 1-前台 release 0-未发布 1-发布
	 */
//	@Override
//	public List<ProductItemRelationFrontBO> listProductItemRelationFront(int itemId)
//			throws Exception {
//		List<ProductItemRelationFrontBO> list = new ArrayList<ProductItemRelationFrontBO>();
//		List<Object> param = new ArrayList<Object>();
//		StringBuilder sbuilder = new StringBuilder();
//		sbuilder.append("SELECT * FROM T_SCS_PRODUCT_ITEM_RELATION_FRONT t where 1=1 ");
//		if (itemId >= 0) {
//			sbuilder.append(" and t.product_item_id=? ");
//			param.add(itemId);
//		}
//		list = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),
//				new ProductItemRelationFrontMapper());
//		return list;
//	}
	
	public List<Product> findFrontProduct(String key, int state, float price,
			int templateId, int type, Integer productItemId, int isDefault, int curPage, int pageSize,int frontOrNot) {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		if(frontOrNot>0){
			sql.append(" SELECT p.* from T_SCS_PRODUCT p,T_SCS_PRODUCT_ITEM_RELATION_FRONT f ");
		}else{
			sql.append(" SELECT p.* from T_SCS_PRODUCT p,T_SCS_PRODUCT_ITEM_RELATION f ");
		}		
		sql.append(" where p.ID=f.PRODUCT_ID ");
		//to fix bug:2358
//		String sql = "SELECT * FROM T_SCS_PRODUCT WHERE 1 = 1";
//		if (StringUtils.isNotBlank(key)) {
//			sql.append( " AND p.CODE like ?");
//			param.add("%" + key.trim() + "%");
//		}
		if (StringUtils.isNotBlank(key)) {
			sql.append( " AND p.NAME like ?");
			param.add("%" + key.trim() + "%");
		}
		if (state > 0) {
			sql.append( " AND p.STATE = ?");
			param.add(state);
		}
		if (price > 0) {
			sql.append( " AND p.PRICE = ?");
			param.add(price);
		}
		if (templateId > 0) {
			sql.append( " AND p.TEMPLATE_ID = ?");
			param.add(templateId);
		}
		if (type > 0) {
			sql.append( " AND p.TYPE = ?");
			param.add(type);
		}
		if (isDefault > 0) {
			sql.append( " AND p.IS_DEFAULT = ?");
			param.add(isDefault);
		}
		if (productItemId !=null) {
			sql.append( " AND f.PRODUCT_ITEM_ID = ?");
			param.add(productItemId);
		}
		sql.append( " ORDER BY p.ID DESC");
		if (curPage > 0 && pageSize > 0) {
			sql.append( " LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		
		return getJdbcTemplate().query(sql.toString(), param.toArray(),
				productMapperExtractor);
	}
	
	  public int listFrontProductCount(int productItemId, int state) throws Exception {
		    List<Object> param = new ArrayList<Object>();
		    StringBuilder sql = new StringBuilder();
		    sql.append(" SELECT count(0) from T_SCS_PRODUCT p,T_SCS_PRODUCT_ITEM_RELATION_FRONT f ");
//		    String sql = "SELECT count(0) FROM T_SCS_TEMPLATE_VM WHERE 1 = 1";
			sql.append(" where p.ID=f.PRODUCT_ID ");
//			String sql = "SELECT * FROM T_SCS_PRODUCT WHERE 1 = 1";
			if (state > 0) {
				sql.append( " AND p.STATE = ?");
				param.add(state);
			}
			if (productItemId >0) {
				sql.append( " AND f.PRODUCT_ITEM_ID = ?");
				param.add(productItemId);
			}

		    return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		  }
	  
		public List<Product> findItemsByProductId(int productId) {
			List<Object> param = new ArrayList<Object>();
			StringBuilder sql = new StringBuilder();
			sql.append(" SELECT p.* from T_SCS_PRODUCT_ITEM p,T_SCS_PRODUCT_ITEM_RELATION_FRONT f");
			sql.append(" WHERE p.ID=f.PRODUCT_ITEM_ID and f.PRODUCT_ID=? ");

			param.add(productId);
			sql.append( " ORDER BY p.ID DESC");
			
			return getJdbcTemplate().query(sql.toString(), param.toArray(),
					productMapperExtractor);
		}
	  
}
