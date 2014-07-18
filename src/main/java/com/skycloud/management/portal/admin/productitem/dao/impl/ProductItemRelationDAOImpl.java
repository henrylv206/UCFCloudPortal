package com.skycloud.management.portal.admin.productitem.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.admin.productitem.dao.IProductItemRelationDAO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemRelationBO;
import com.skycloud.management.portal.admin.productitem.entity.ProductItemXmlPO;

public class ProductItemRelationDAOImpl extends SpringJDBCBaseDao implements
		IProductItemRelationDAO {
	private static Log log = LogFactory
			.getLog(ProductItemRelationDAOImpl.class);

	private static final class ProductItemRelationMapper implements RowMapper {

		@Override
		public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProductItemRelationBO item = new ProductItemRelationBO();
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
	public int createProductItemRelation(final ProductItemRelationBO relation)
			throws Exception {
		final String sql = "INSERT INTO T_SCS_PRODUCT_ITEM_RELATION(PRODUCT_ID,PRODUCT_ITEM_ID,STATE) VALUES (?,?,?)";
		KeyHolder keyHolder = new GeneratedKeyHolder();
		getJdbcTemplate().update(new PreparedStatementCreator() {
			@Override
			public PreparedStatement createPreparedStatement(Connection con)
					throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setInt(1, relation.getProductId());
				ps.setInt(2, relation.getProductItemId());
				ps.setInt(3, relation.getState());
				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

//	@Override
//	public void deleteProductItemRelation(int id) throws Exception {
//		String sql = "";
//		if (id >= 0) {
//			sql = "DELETE FROM T_SCS_PRODUCT_ITEM_RELATION WHERE id=?";
//			getJdbcTemplate().update(sql, id);
//		} else {
//			sql = "DELETE FROM T_SCS_PRODUCT_ITEM_RELATION";
//			getJdbcTemplate().update(sql);
//		}
//
//	}

	public void deleteProductItemRelation(int productItemId,
			String currentAllProductIds,int notAll) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("DELETE FROM T_SCS_PRODUCT_ITEM_RELATION WHERE product_item_id=? ");
		if(notAll>0){
			if(null !=  currentAllProductIds && !currentAllProductIds.equals("")){
				sql.append(" and product_id in (");
				sql.append(currentAllProductIds);
				sql.append(")");				
			}
		}
		getJdbcTemplate().update(sql.toString(), productItemId);
	}

	public void update(final int productItemId, String currentAllProductIds)
			throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update T_SCS_PRODUCT set PRODUCT_ITEM_ID=-1 where ID in (");
		sql.append(currentAllProductIds);
		sql.append(")");
		this.getJdbcTemplate().update(sql.toString());
	}

	@Override
	public ProductItemRelationBO getProductItemRelationById(int id)
			throws Exception {
		String sql = "SELECT * FROM T_SCS_PRODUCT_ITEM_RELATION WHERE id=?";
		ProductItemRelationBO item = null;
		try {
			item = (ProductItemRelationBO) getJdbcTemplate().queryForObject(
					sql, new ProductItemRelationMapper(), id);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
		return item;
	}

	/**
	 * frontOrNot 0-后台 1-前台 release 0-未发布 1-发布
	 */
	@Override
	public List<ProductItemRelationBO> listProductItemRelation(int itemId)
			throws Exception {
		List<ProductItemRelationBO> list = new ArrayList<ProductItemRelationBO>();
		List<Object> param = new ArrayList<Object>();
		StringBuilder sbuilder = new StringBuilder();
		sbuilder.append("SELECT * FROM T_SCS_PRODUCT_ITEM_RELATION t where 1=1 ");
		if (itemId >= 0) {
			sbuilder.append(" and t.product_item_id=? ");
			param.add(itemId);
		}
		list = getJdbcTemplate().query(sbuilder.toString(), param.toArray(),
				new ProductItemRelationMapper());
		return list;
	}
	
	  public int listItemProductCount(String name,int type, int state,int item) throws Exception {
		    List<Object> param = new ArrayList<Object>();
		    StringBuilder sql = new StringBuilder();
		    sql.append(" SELECT count(0) from T_SCS_PRODUCT p,T_SCS_PRODUCT_ITEM_RELATION f ");
		    sql.append(" where p.ID=f.PRODUCT_ID ");
//		    sql.append("SELECT count(0) FROM T_SCS_PRODUCT WHERE 1 = 1");
		    if (StringUtils.isNotBlank(name)) {
		    	sql.append(" AND p.name like ?");
		    	param.add("%" + name.replaceAll("_", "\\\\_") + "%");
		      }
		    if (type > 0) {
		    	sql.append(" AND p.TYPE = ?");
		      param.add(type);
		    }
		    if (state > 0) {
		    	sql.append(" AND p.STATE = ?");
		      param.add(state);
		    }
		    if (item > 0) {
		    	sql.append(" AND f.PRODUCT_ITEM_ID = ?");
		      param.add(item);
		    }
		    return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);
		    
	  }


}
