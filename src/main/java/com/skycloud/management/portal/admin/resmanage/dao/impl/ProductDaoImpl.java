package com.skycloud.management.portal.admin.resmanage.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.admin.resmanage.dao.IProductDao;
import com.skycloud.management.portal.admin.resmanage.entity.Product;
import com.skycloud.management.portal.admin.template.entity.TTemplateVMBO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;

public class ProductDaoImpl extends SpringJDBCBaseDao implements IProductDao {

	private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);

	// 资源状态：1-待审核、2-待发布、3-已发布、-4-已删除、5-审核失败、6-产品下线。
	public static final int STATE_WAIT_ADUIT = 1;

	public static final int STATE_WAIT_RELEASE = 2;

	public static final int STATE_RELEASED = 3;

	public static final int STATE_DELETED = 4;

	public static final int STATE_ADUIT_FAILED = 5;

	public static final int STATE_OFFLINE = 6;

	// 操作类型
	public static final int OPERATE_TYPE_ADD = 1;

	public static final int OPERATE_TYPE_UPDATE = 2;

	public static final int OPERATE_TYPE_DEL = 3;

	private static final RowMapperResultSetExtractor<Product> productMapperExtractor = new RowMapperResultSetExtractor<Product>(
	        new BeanPropertyRowMapper<Product>(Product.class));

	@Override
	public int save(final Product product) {
		KeyHolder keyHolder = new GeneratedKeyHolder();
//		final String sql = "INSERT INTO T_SCS_PRODUCT(CODE, NAME, STATE, DESCRIPTION, CREATE_DATE, SPECIFICATION, QUOTA_NUM, PERIOD, "
//		        + "PRICE, PICTURE, DOC, TEMPLATE_ID, IS_DEFAULT,UNIT,TYPE,OPERATE_TYPE) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?)";
//		if (product.getProductItemId() != null) {
			final String sql = "INSERT INTO T_SCS_PRODUCT(CODE, NAME, STATE, DESCRIPTION, CREATE_DATE, SPECIFICATION, QUOTA_NUM, PERIOD, "
			        + "PRICE, PICTURE, DOC, TEMPLATE_ID, IS_DEFAULT,UNIT,TYPE,OPERATE_TYPE, PRODUCT_ITEM_ID) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?,?,?,?,?,?,?)";
//		}
		getJdbcTemplate().update(new PreparedStatementCreator() {

			@Override
			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
				ps.setString(1, product.getCode());
				ps.setString(2, product.getName());
				ps.setInt(3, product.getState());
				ps.setString(4, product.getDescription());
				ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
				ps.setString(6, product.getSpecification());
				ps.setInt(7, product.getQuotaNum());
				ps.setString(8, product.getPeriod());
				ps.setFloat(9, product.getPrice());
				ps.setString(10, product.getPicture());
				ps.setString(11, product.getDoc());
				ps.setInt(12, product.getTemplateId());
				ps.setInt(13, product.getIsDefault());
				ps.setString(14, product.getUnit());
				ps.setInt(15, product.getType());
				ps.setInt(16, OPERATE_TYPE_ADD);
				Integer itemId = product.getProductItemId();
				if (itemId == null) {
					ps.setInt(17, -10);
				}else{
					ps.setInt(17, product.getProductItemId());
				}

				return ps;
			}
		}, keyHolder);
		return keyHolder.getKey().intValue();
	}

//	private PreparedStatementCreator createPreparedStatement(final String sql, final Product product) {
//		return new PreparedStatementCreator() {
//
//			@Override
//			public PreparedStatement createPreparedStatement(Connection con) throws SQLException {
//				PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
//				ps.setString(1, product.getCode());
//				ps.setString(2, product.getName());
//				ps.setInt(3, product.getState());
//				ps.setString(4, product.getDescription());
//				ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
//				ps.setString(6, product.getSpecification());
//				ps.setInt(7, product.getQuotaNum());
//				ps.setString(8, product.getPeriod());
//				ps.setFloat(9, product.getPrice());
//				ps.setString(10, product.getPicture());
//				ps.setString(11, product.getDoc());
//				ps.setInt(12, product.getTemplateId());
//				ps.setInt(13, product.getIsDefault());
//				ps.setString(14, product.getUnit());
//				ps.setInt(15, product.getType());
//				ps.setInt(16, OPERATE_TYPE_ADD);
//				if (product.getProductItemId() != null) {
//					ps.setInt(17, product.getProductItemId());
//				}
//				return ps;
//			}
//		};
//	}

	@Override
	public void delete(final int id) {
		String sql = "DELETE FROM T_SCS_PRODUCT WHERE ID = ?;";
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		});
	}

	@Override
	public void update(final Product product) {
		String sql = "UPDATE T_SCS_PRODUCT set CODE=?,NAME=?,STATE=?,DESCRIPTION=?," + "SPECIFICATION=?,QUOTA_NUM=?,PERIOD=?,PRICE=?,DOC=?,"
		        + "PICTURE=?,TEMPLATE_ID=?,MODIFY_DATE=?, IS_DEFAULT=?,UNIT=?,TYPE=?,OPERATE_TYPE=? WHERE ID = ?";
		if (product.getProductItemId() != null) {
			sql = "UPDATE T_SCS_PRODUCT set CODE=?,NAME=?,STATE=?,DESCRIPTION=?," + "SPECIFICATION=?,QUOTA_NUM=?,PERIOD=?,PRICE=?,DOC=?,"
			        + "PICTURE=?,TEMPLATE_ID=?,MODIFY_DATE=?,IS_DEFAULT=?,UNIT=?,TYPE=?,OPERATE_TYPE=?, PRODUCT_ITEM_ID=? WHERE ID = ?";
		}
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setString(1, product.getCode());
				ps.setString(2, product.getName());
				ps.setInt(3, product.getState());
				ps.setString(4, product.getDescription());
				ps.setString(5, product.getSpecification());
				ps.setInt(6, product.getQuotaNum());
				ps.setString(7, product.getPeriod());
				ps.setFloat(8, product.getPrice());
				ps.setString(9, product.getDoc());
				ps.setString(10, product.getPicture());
				ps.setInt(11, product.getTemplateId());
				ps.setTimestamp(12, new Timestamp(System.currentTimeMillis()));
				ps.setInt(13, product.getIsDefault());
				ps.setString(14, product.getUnit());
				ps.setInt(15, product.getType());
				ps.setInt(16, product.getOperateType());
				if (product.getProductItemId() != null) {
					ps.setInt(17, product.getProductItemId());
					// 条件
					ps.setInt(18, product.getId());
				} else {
					// 条件
					ps.setInt(17, product.getId());
				}
			}
		});
	}

	@Override
	public List<Product> findAll() {
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p ORDER BY CREATE_DATE desc";
		BeanPropertyRowMapper<Product> deptRowMapper = new BeanPropertyRowMapper<Product>(Product.class);
		return this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<Product>(deptRowMapper));
	}

	@Override
	public Product get(final int id) {
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE ID=?";
		BeanPropertyRowMapper<Product> deptRowMapper = new BeanPropertyRowMapper<Product>(Product.class);
		List<Product> products = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, id);
			}
		}, deptRowMapper);
		if (products != null && !products.isEmpty()) {
			return products.get(0);
		}
		return null;
	}

	@Override
	public int getProductCountByName(final String name) throws Exception {
		if (name == null) {
			return -1;
		}
		int count = 0;
		try {
			String sql = "SELECT count(ID) FROM T_SCS_PRODUCT WHERE STATE<>4 AND NAME=?;";
			Object[] args = new Object[] { name };
			count = this.getJdbcTemplate().queryForInt(sql, args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_PRODUCT error：" + e.getMessage());
		}
		return count;
	}

	@Override
	public List<Product> findBuyByCatalog(int state, int type, int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE 1 = 1";
		if (state > 0) {
			sql += " AND STATE = ?";
			param.add(state);
		}
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		sql += " ORDER BY ID DESC";
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	/**
	 * 多实例排重
	 */
	private String returnSql4TemplateRelation(){
		StringBuffer sql = new StringBuffer();
		sql.append(" select  pr1.*,vm1.RESOURCE_POOLS_ID,vm1.ZONE_ID,vm1.SPECIAL  ");
		sql.append(",CAST(pr1.price AS DECIMAL(10,2)) price2 ");
		sql.append(" from T_SCS_PRODUCT pr1,T_SCS_TEMPLATE_VM vm1,T_SCS_PRODUCT_TEMPLATE_RELATION r1 ");
		sql.append(" where r1.TEMPLATE_TYPE<>3 and pr1.TYPE=50 and pr1.ID=r1.PRODUCT_ID and r1.TEMPLATE_ID=vm1.ID ");
		sql.append(" UNION ");
		sql.append(" select  pr2.*,vm2.RESOURCE_POOLS_ID,vm2.ZONE_ID,vm2.SPECIAL ");
		sql.append(",CAST(pr2.price AS DECIMAL(10,2)) price2 ");
		sql.append(" from T_SCS_PRODUCT pr2,T_SCS_TEMPLATE_MC vm2,T_SCS_PRODUCT_TEMPLATE_RELATION r2  ");
		sql.append(" where r2.TEMPLATE_TYPE=3 and pr2.TYPE=50 and pr2.ID=r2.PRODUCT_ID and r2.TEMPLATE_ID=vm2.ID  ");
		return sql.toString();
	}

	private String returnSql4respool() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p1.*, vm.RESOURCE_POOLS_ID, vm.ZONE_ID, vm.SPECIAL ");
		sql.append(",CAST(p1.price AS DECIMAL(10,2)) price2 ");
		
		sql.append(" from T_SCS_PRODUCT p1, T_SCS_TEMPLATE_VM vm ");
		sql.append(" where 	p1.TYPE not in(3,50) and p1.TEMPLATE_ID=vm.ID ");
		//to fix bug 4779
		//to fix bug [4924]
		//sql.append(" and vm.SPECIAL=0");
		sql.append(" union ");
		sql.append("select p2.*, mc.RESOURCE_POOLS_ID, mc.ZONE_ID, mc.SPECIAL ");
		sql.append(",CAST(p2.price AS DECIMAL(10,2)) price2 ");
		sql.append(" from T_SCS_PRODUCT p2, T_SCS_TEMPLATE_MC mc ");
		sql.append(" where 	p2.TYPE=3 and p2.TEMPLATE_ID=mc.ID ");
		//to fix bug 4779
		//sql.append(" and mc.SPECIAL=0");
		//added by zhanghuizheng 20121211
		//加上多虚机类型的服务
		sql.append(" union ");
		sql.append(" select pr.* from (" + returnSql4TemplateRelation() + ") pr  group by pr.ID  ");
		return sql.toString();
	}

	private String returnSql4respoolForOpt() {
		StringBuffer sql = new StringBuffer();
		sql.append("select p1.*, vm.RESOURCE_POOLS_ID, vm.ZONE_ID, vm.SPECIAL ");
		sql.append(",CAST(p1.price AS DECIMAL(10,2)) price2 ");
		sql.append(" from T_SCS_PRODUCT p1, T_SCS_TEMPLATE_VM vm ");
		sql.append(" where 	p1.TYPE not in(3,50) and p1.TEMPLATE_ID=vm.ID ");
		//to fix bug 4779
		sql.append(" and vm.SPECIAL=0");
		sql.append(" union ");
		sql.append("select p2.*, mc.RESOURCE_POOLS_ID, mc.ZONE_ID, mc.SPECIAL ");
		sql.append(",CAST(p2.price AS DECIMAL(10,2)) price2 ");
		sql.append(" from T_SCS_PRODUCT p2, T_SCS_TEMPLATE_MC mc ");
		sql.append(" where 	p2.TYPE=3 and p2.TEMPLATE_ID=mc.ID ");
		//to fix bug 4779
		sql.append(" and mc.SPECIAL=0");
		//added by zhanghuizheng 20121211
		//加上多虚机类型的服务
		sql.append(" union ");
		sql.append(" select pr.* from (" + returnSql4TemplateRelation() + ") pr  group by pr.ID  ");
		return sql.toString();
	}
	
	
	@Override
	public List<Product> find(int userId, String key, int state, float price, int templateId, int type, Integer productItemId, int isDefault,
	        int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE 1 = 1";
		// if (StringUtils.isNotBlank(key)) {
		// sql += " AND CODE like ?";
		// param.add("%" + key.trim() + "%");
		// }
		if (StringUtils.isNotBlank(key)) {
			sql += " AND instr(p.NAME, ?)>0";
			param.add(key.trim());
		}
		if (state > 0) {
			sql += " AND p.STATE = ?";
			param.add(state);
		}
		if (price > 0) {
			sql += " AND p.PRICE = ?";
			param.add(price);
		}
		if (templateId > 0) {
			sql += " AND p.TEMPLATE_ID = ?";
			param.add(templateId);
		}
		if (type > 0) {
			sql += " AND p.TYPE = ?";
			param.add(type);
		}
		if (isDefault > 0) {
			sql += " AND p.IS_DEFAULT = ?";
			param.add(isDefault);
		}
		if (productItemId != null && productItemId.intValue() > 0) {
			sql += " AND p.PRODUCT_ITEM_ID = ?";
			param.add(productItemId);
		}
		sql += " ORDER BY p.ID DESC";
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}
	
	public List<Product> findForItem(int userId, String key, int state, float price, int templateId, int type, Integer productItemId, int isDefault,
	        int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + this.returnSql4respoolForOpt() + ") p WHERE 1 = 1";
		// if (StringUtils.isNotBlank(key)) {
		// sql += " AND CODE like ?";
		// param.add("%" + key.trim() + "%");
		// }
		if (StringUtils.isNotBlank(key)) {
			sql += " AND instr(p.NAME, ?)>0";
			param.add(key.trim());
		}
		if (state > 0) {
			sql += " AND p.STATE = ?";
			param.add(state);
		}
		if (price > 0) {
			sql += " AND p.PRICE = ?";
			param.add(price);
		}
		if (templateId > 0) {
			sql += " AND p.TEMPLATE_ID = ?";
			param.add(templateId);
		}
		if (type > 0) {
			sql += " AND p.TYPE = ?";
			param.add(type);
		}
		if (isDefault > 0) {
			sql += " AND p.IS_DEFAULT = ?";
			param.add(isDefault);
		}
		if (productItemId != null && productItemId.intValue() > 0) {
			sql += " AND p.PRODUCT_ITEM_ID = ?";
			param.add(productItemId);
		}
		sql += " ORDER BY p.ID DESC";
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	
	public List<Product> findForOpt(TTemplateVMBO template, Product product,
	        int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
//		String sql = "SELECT p.* FROM (" + this.returnSql4respoolForOpt() + ") p WHERE 1 = 1";
		StringBuffer sql = new StringBuffer();		
		sql.append(" SELECT ");
		sql.append(" p.*, tm.RESOURCE_POOLS_ID, ");
		sql.append(" tm.ZONE_ID, ");
		sql.append(" tm.SPECIAL ");
		sql.append(" FROM ");
		sql.append(" T_SCS_PRODUCT p ");
		sql.append(" LEFT JOIN( ");
		sql.append(" SELECT ");
		sql.append(" 	vm.ID template_id, ");
		sql.append(" 	vm.RESOURCE_POOLS_ID, ");
		sql.append(" 	vm.ZONE_ID, ");
		sql.append(" 	vm.SPECIAL, ");
		sql.append(" 	vm.type template_type, ");
		sql.append(" 	vm.CPU_NUM, ");
		sql.append(" 	vm.MEMORY_SIZE, ");
		sql.append(" 	vm.E_OS_ID, ");
//		sql.append(" 	vm.STORE_TYPE, ");
		sql.append(" 	vm.STORAGE_SIZE, ");
		sql.append(" 	vm.CPUFREQUENCY ");
		sql.append(" FROM ");
		sql.append(" 	T_SCS_TEMPLATE_VM vm ");
		sql.append(" UNION ");
		sql.append(" 	SELECT ");
		sql.append(" 		mc.ID template_id, ");
		sql.append(" 		mc.RESOURCE_POOLS_ID, ");
		sql.append(" 		mc.ZONE_ID, ");
		sql.append(" 		mc.SPECIAL, ");
		sql.append(" 		3 AS template_type, ");
		sql.append(" 		mc.CPU_NUM, ");
		sql.append(" 		mc.MEMORY_SIZE, ");
		sql.append(" 		-1 AS E_OS_ID, ");
//		sql.append(" 		'' AS STORE_TYPE, ");
		sql.append(" 		mc.STORAGE_SIZE, ");
		sql.append(" 	    -1 AS CPUFREQUENCY ");
		sql.append(" 	FROM ");
		sql.append(" 		T_SCS_TEMPLATE_MC mc ");
		sql.append(" )tm ON p.TEMPLATE_ID = tm.template_id ");
		sql.append(" AND p.TYPE = tm.template_type ");
		sql.append(" where p.TYPE != 50 and p.type >0 ");
		sql.append(" and tm.SPECIAL=0 ");		
		
		String key = product.getName();
		if (StringUtils.isNotBlank(key)) {
			sql.append(" AND instr(p.NAME, ?)>0");
			param.add(key.trim());
		}
		// 状态
		int state = product.getState();
		if (state > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(state);
		}
		int templateId = product.getTemplateId();
		if (templateId > 0) {
			sql.append(" AND p.TEMPLATE_ID = ?");
			param.add(templateId);
		}
		int type = product.getType();
		if (type > 0) {
			sql.append(" AND p.TYPE = ?");
			param.add(type);
		}
		// 是否首页推荐
		int isDefault = product.getIsDefault();
		if (isDefault > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(isDefault);
		}
		Integer productItemId = product.getProductItemId();
		if (productItemId != null && productItemId.intValue() > 0) {
			sql.append(" AND p.PRODUCT_ITEM_ID = ?");
			param.add(productItemId);
		}
		
		//资源池 to fix bug 7448
		if(template.getResourcePoolsId()>0){
			sql.append(" and tm.RESOURCE_POOLS_ID = ? ");
			param.add(template.getResourcePoolsId());
		}
		
		// cpu个数
		if (template.getCpuNum() > 0) {
			sql.append(" and tm.CPU_NUM = ? ");
			param.add(template.getCpuNum());
		}		
		// 内存大小
		if (template.getMemorySize() > 0) {
			sql.append(" and tm.MEMORY_SIZE = ? ");
			param.add(template.getMemorySize());
		}
		// 操作系统
		if (template.geteOsId() > 0) {
			sql.append(" and tm.E_OS_ID = ? ");
			param.add(template.geteOsId());
		}	
		//物理机cpu频率
		if(type == 10){
			if (template.getCpufrequency() > 0) {
				sql.append(" and tm.CPUFREQUENCY = ? ");
				param.add(template.getCpufrequency());
			}					
		}
		// 存储类型
//		if (StringUtils.isNotBlank(template.getStoreType())) {
//			sql.append(" and tm.STORE_TYPE = ? ");
//			param.add(template.getStoreType().trim());
//		}

		// 网卡个数
//		if (template.getVethAdaptorNum() > 0) {
//			sql.append(" and tm.VETH_ADAPTOR_NUM = ? ");
//			param.add(template.getVethAdaptorNum());
//		}
		// 磁盘大小
		if (template.getStorageSize() > 0) {
			sql.append(" and tm.STORAGE_SIZE = ? ");
			param.add(template.getStorageSize());
		}
		// 价格
		float price = product.getPrice();
		if (price > 0) {
			sql.append(" AND p.PRICE = ?");
			param.add(price);
		}
		if (product.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(product.getPrice2());
		}
		
		sql.append(" ORDER BY p.ID DESC");
		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(), productMapperExtractor);
	}
	
	

	@Override
	public List<Product> findNew() {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE p.STATE = 3 and p.SPECIAL = 0 ORDER BY p.CREATE_DATE desc LIMIT 0,5 ";

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public List<Product> findRelativeProduct(int state, int type) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE p.STATE = ? AND p.TYPE = ? ORDER BY p.CREATE_DATE desc LIMIT 0,5 ";
		param.add(state);
		param.add(type);
		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public List<Product> findById(int id) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE p.id = ? ";
		param.add(id);
		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public List<Product> findCommend() {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + returnSql4respool() + ") p WHERE p.STATE = 3 and p.IS_DEFAULT = 1 ORDER BY p.CREATE_DATE desc LIMIT 0,4 ";

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public List<Product> findAduit(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + this.returnSql4respoolForOpt() + ") p WHERE 1 = 1 AND STATE IN (" + STATE_WAIT_ADUIT + "," + STATE_ADUIT_FAILED
		        + ") ";
//		if (StringUtils.isNotBlank(key)) {
//			sql += " AND CODE like ?";
//			param.add("%" + key.trim() + "%");
//		}
		if (StringUtils.isNotBlank(key)) {
			sql += " AND NAME like ?";
			param.add("%" + key.trim() + "%");
		}
		if (price > 0) {
			sql += " AND PRICE = ?";
			param.add(price);
		}
		if (templateId > 0) {
			sql += " AND TEMPLATE_ID = ?";
			param.add(templateId);
		}
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		if (productItemId != null) {
			sql += " AND PRODUCT_ITEM_ID = ?";
			param.add(productItemId);
		}
		sql += " ORDER BY STATE,ID DESC";
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public List<Product> findRelease(String key, float price, int templateId, int type, Integer productItemId, int curPage, int pageSize) {
		List<Object> param = new ArrayList<Object>();
		String sql = "SELECT p.* FROM (" + this.returnSql4respoolForOpt() + ") p WHERE 1 = 1 AND STATE IN (" + STATE_WAIT_RELEASE + "," + STATE_RELEASED + ","
		        + STATE_OFFLINE + ") ";
//		if (StringUtils.isNotBlank(key)) {
//			sql += " AND CODE like ?";
//			param.add("%" + key.trim() + "%");
//		}
		if (StringUtils.isNotBlank(key)) {
			sql += " AND NAME like ?";
			param.add("%" + key.trim() + "%");
		}
		if (price > 0) {
			sql += " AND PRICE = ?";
			param.add(price);
		}
		if (templateId > 0) {
			sql += " AND TEMPLATE_ID = ?";
			param.add(templateId);
		}
		if (type > 0) {
			sql += " AND TYPE = ?";
			param.add(type);
		}
		if (productItemId != null) {
			sql += " AND PRODUCT_ITEM_ID = ?";
			param.add(productItemId);
		}
		sql += " ORDER BY STATE,ID DESC";
		if (curPage > 0 && pageSize > 0) {
			sql += " LIMIT ?, ?";
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}

		return getJdbcTemplate().query(sql, param.toArray(), productMapperExtractor);
	}

	@Override
	public void update(final int productItemId, final int id) throws Exception {
		String sql = "UPDATE T_SCS_PRODUCT set PRODUCT_ITEM_ID=? WHERE ID = ?";
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, productItemId);
				ps.setInt(2, id);
			}
		});
	}

	@Override
	public void update(final int productItemId, String currentAllProductIds) throws Exception {
		StringBuilder sql = new StringBuilder();
		sql.append("update T_SCS_PRODUCT set PRODUCT_ITEM_ID=-1 where ID in (");
		sql.append(currentAllProductIds);
		sql.append(")");
		this.getJdbcTemplate().update(sql.toString());
	}

	@Override
	public void updateOldbyNew(final int oldItem, final int newItem) throws Exception {
		String sql = "UPDATE T_SCS_PRODUCT_ITEM_RELATION set PRODUCT_ITEM_ID=? WHERE PRODUCT_ITEM_ID = ?";
		this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

			@Override
			public void setValues(PreparedStatement ps) throws SQLException {
				ps.setInt(1, newItem);
				ps.setInt(2, oldItem);
			}
		});
	}

	@Override
	public int listProductCount(String name, int type, int state) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(0) FROM T_SCS_PRODUCT WHERE 1 = 1");
		if (StringUtils.isNotBlank(name)) {
			sql.append(" AND name like ?");
			param.add("%" + name.replaceAll("_", "\\\\_") + "%");
		}
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}
		return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);

	}

	@Override
	public int listItemProductCount(String name, int type, int state, int item) throws Exception {
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(0) FROM T_SCS_PRODUCT WHERE 1 = 1");
		if (StringUtils.isNotBlank(name)) {
			sql.append(" AND name like ?");
			param.add("%" + name.replaceAll("_", "\\\\_") + "%");
		}
		if (type > 0) {
			sql.append(" AND TYPE = ?");
			param.add(type);
		}
		if (state > 0) {
			sql.append(" AND STATE = ?");
			param.add(state);
		}
		if (item > 0) {
			sql.append(" AND PRODUCT_ITEM_ID = ?");
			param.add(item);
		}
		return getJdbcTemplate().queryForObject(sql.toString(), param.toArray(), Integer.class);

	}

	@Override
	public int getProductCountByCode(final String code) throws Exception {
		if (code == null) {
			return -1;
		}
		int count = 0;
		try {
			String sql = "SELECT count(ID) FROM T_SCS_PRODUCT WHERE STATE<>4 AND CODE=?;";
			Object[] args = new Object[] { code };
			count = this.getJdbcTemplate().queryForInt(sql, args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_PRODUCT error：" + e.getMessage());
		}
		return count;
	}

	@Override
	public int getInstanceCountByProductIdAndUserId(int productId, int userId) throws Exception {
		if (productId <= 0) {
			return -1;
		}
		int count = 0;
		Object[] args = null;
		if (userId > 0) {
			args = new Object[] { 0, 0 };
		} else {
			args = new Object[] { 0 };
		}
		StringBuilder sql = new StringBuilder();
		try {
			sql.append("SELECT count(1) FROM T_SCS_INSTANCE_INFO i  LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=i.ORDER_ID where i.STATE<>4 ");
			if (productId > 0) {
				sql.append(" AND i.PRODUCT_ID = ?");
				args[0] = productId;
			}
			if (userId > 0) {
				sql.append("  AND o.CREATOR_USER_ID = ?");
				args[1] = userId;
			}
			count = this.getJdbcTemplate().queryForInt(sql.toString(), args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_INSTANCE_INFO ,T_SCS_ORDER  error：" + e.getMessage());
		}
		return count;
	}

	@Override
	public int checkProductRename(String name) throws Exception {

		int count = 0;
		Object[] args = new Object[] { 0 };

		StringBuilder sql = new StringBuilder();
		try {
			// 4退订；7创建失败 fix bug 3789
			sql.append("SELECT count(1) FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=i.ORDER_ID where i.TEMPLATE_TYPE='1' AND (i.STATE<>4 and i.STATE<>7)");

			sql.append(" AND i.INSTANCE_NAME = ?");
			args[0] = name;
			count = this.getJdbcTemplate().queryForInt(sql.toString(), args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_INSTANCE_INFO ,T_SCS_ORDER  error：" + e.getMessage());
		}
		return count;
	}

	// fix bug 2166
	@Override
	public int checkProductRename(String name, int type) throws Exception {
		int count = 0;
		Object[] args = new Object[] { 0, 0 };
		StringBuilder sql = new StringBuilder();
		try {
			// 4退订；7创建失败 fix bug 3789
			sql.append("SELECT count(1) FROM T_SCS_INSTANCE_INFO i LEFT JOIN T_SCS_ORDER o ON o.ORDER_ID=i.ORDER_ID where i.TEMPLATE_TYPE=? AND (i.STATE<>4 and i.STATE<>7) ");

			sql.append(" AND i.INSTANCE_NAME = ?");
			args[0] = type;
			args[1] = name;
			count = this.getJdbcTemplate().queryForInt(sql.toString(), args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_INSTANCE_INFO ,T_SCS_ORDER  error：" + e.getMessage());
		}
		return count;
	}

	@Override
	// fix bug 2263
	public int checkVMSProductRename(String name) throws Exception {

		int count = 0;
		Object[] args = new Object[] { 0 };

		StringBuilder sql = new StringBuilder();
		try {
			// fix bug 2263 4退订；7创建失败 fix bug 3789
			sql.append("select count(1)  from ");
			sql.append(" (select ref.pi_name,i.state,i.order_id,i.template_type  from T_SCS_INSTANCE_INFO i  join T_SCS_PRODUCT_INSTANCE_REF ref on i.id=ref.instance_info_id ");
			sql.append("where  (i.state<>4 and i.state<>7)  and ref.pi_name= ?) a ");
			args[0] = name;
			logger.debug("----------------------------:" + sql.toString());
			logger.debug("----------------------------:" + args[0].toString());
			count = this.getJdbcTemplate().queryForInt(sql.toString(), args);
		}
		catch (Exception e) {
			throw new Exception("query T_SCS_INSTANCE_INFO ,T_SCS_PRODUCT_INSTANCE_REF  error：" + e.getMessage());
		}
		return count;
	}

	@Override
	public int updateState(int productId, int state) throws Exception {
		String sql = "UPDATE T_SCS_PRODUCT SET STATE = ? WHERE ID = ?";
		Object[] args = new Object[] { state, productId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER };
		int index = 0;
		index = getJdbcTemplate().update(sql, args, argTypes);
		return index;
	}

	@Override
	public int updateState(int productId, int state, int operateType) throws Exception {
		String sql = "UPDATE T_SCS_PRODUCT SET STATE = ?,OPERATE_TYPE=? WHERE ID = ?";
		Object[] args = new Object[] { state, operateType, productId };
		int[] argTypes = new int[] { Types.INTEGER, Types.INTEGER, Types.INTEGER };
		int index = 0;
		index = getJdbcTemplate().update(sql, args, argTypes);
		return index;
	}

	@Override
    public List<Product> find(TTemplateVMBO template, Product product, int curPage, int pageSize) {
		// to fix bug:3190
		List<Object> param = new ArrayList<Object>();
		StringBuilder sql = new StringBuilder();

		sql.append(" select * FROM  ");
		sql.append(" (  ");
		this.getSql4Product(sql, param, template, product);
		sql.append(" ) pro  ");

//		if (product.getSpecification() == null || product.getSpecification().equals("") || product.getSpecification().equals("data")) {
//			sql.append(" ORDER BY create_date DESC");
//		} else if (product.getSpecification().equals("price")) {
//			sql.append(" ORDER BY price DESC");
//		}

		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ?");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		Object[] arr = param.toArray();
		String _sql = sql.toString();
		List<Product> list = getJdbcTemplate().query(_sql, arr, productMapperExtractor);
		return list;
	}

	@Override
    public int getFindCount(TTemplateVMBO template, Product product) {
		final StringBuilder sql = new StringBuilder();
		List<Object> param = new ArrayList<Object>();
		sql.append(" select count(*) FROM  ");
		sql.append(" (  ");
		this.getSql4Product(sql, param, template, product);
		sql.append(" ) pro  ");

		String _sql = sql.toString();
		int intcount = getJdbcTemplate().queryForObject(_sql, param.toArray(), Integer.class);
		return intcount;
	}


	private void getSql4Product(StringBuilder sql,List<Object> param,TTemplateVMBO template, Product product){
		/************************** 非小型机和多虚机情况 ********************************/
		sql.append(" select p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p " );
		sql.append(" LEFT JOIN T_SCS_TEMPLATE_VM t ON p.TEMPLATE_ID = t.ID " );
		sql.append(" where p.TYPE !=3 AND p.TYPE != 50 ");
		// 要不要加判断条件过滤用户只能看到自己所属用户组对应的资源池的服务(产品)，公有云有用户组和所属的资源池的概念吗？？？？？？
		// sql.append(" and t.SPECIAL=0  ");

		// 内存大小
		if (template.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(template.getMemorySize());
		}

		// 存储类型
		if (StringUtils.isNotBlank(template.getStoreType())) {
			sql.append(" and t.STORE_TYPE = ? ");
			// param.add("'" + t.getStoreType().trim() + "'");
			// to fix bug:3324
			param.add(template.getStoreType().trim());
		}
		// cpu个数
		if (template.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(template.getCpuNum());
		}

		// 网卡个数
		if (template.getVethAdaptorNum() > 0) {
			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
			param.add(template.getVethAdaptorNum());
		}
		// 磁盘大小
		if (template.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(template.getStorageSize());
		}

		// 服务类型
		if (product.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(product.getType());
		}
		// 名称
		if (null != product.getName() && !product.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(product.getName());
		}

		// 状态
		if (product.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(product.getState());
		}

		// 价格
		if (product.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(product.getPrice());
		}

		if (product.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(product.getPrice2());
		}
		// 是否首页推荐
		if (product.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(product.getIsDefault());
		}

//		if (product.getProductItemId() != null) {
//			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
//			param.add(product.getProductItemId());
//		}
		/************************** 非小型机和多虚机情况 ********************************/

		/******************************** 小型机 ***************************************/
		sql.append(" UNION all  ");
		sql.append(" select p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p " );
		sql.append(" LEFT JOIN T_SCS_TEMPLATE_MC t ON p.TEMPLATE_ID = t.ID  ");
		sql.append(" where p.TYPE =3  ");
		// sql.append(" and t.SPECIAL=0  ");
		// 内存大小
		if (template.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(template.getMemorySize());
		}
		// cpu个数
		if (template.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(template.getCpuNum());
		}

//		// 网卡个数  小型机不按照网卡个数查询
//		if (template.getVethAdaptorNum() > 0) {
//			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
//			param.add(template.getVethAdaptorNum());
//		}
		// 磁盘大小
		if (template.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(template.getStorageSize());
		}

		// 名称
		if (null != product.getName() && !product.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(product.getName());
		}

		// 状态
		if (product.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(product.getState());
		}

		// 价格
		if (product.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(product.getPrice());
		}

		if (product.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(product.getPrice2());
		}
		// 是否首页推荐
		if (product.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(product.getIsDefault());
		}

//		if (product.getProductItemId() != null) {
//			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
//			param.add(product.getProductItemId());
//		}
		// to fix bug:3655
		// 服务类型
		if (product.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(product.getType());
		}
		/******************************** 小型机 ***************************************/

		/******************************** 多虚机 ***************************************/
		sql.append(" UNION ALL  ");
		sql.append(" select DISTINCT p.*,t.RESOURCE_POOLS_ID resourcePoolsId,t.ZONE_ID zoneId,t.SPECIAL special ");
		sql.append(" from T_SCS_PRODUCT p,T_SCS_PRODUCT_TEMPLATE_RELATION r,T_SCS_TEMPLATE_VM t  ");
		sql.append(" where p.ID=r.PRODUCT_ID  ");
		sql.append(" and p.TYPE =50  ");
		sql.append(" and r.TEMPLATE_ID=t.ID  ");
		// sql.append(" and t.SPECIAL=0  ");

		// 内存大小
		if (template.getMemorySize() > 0) {
			sql.append(" and t.MEMORY_SIZE = ? ");
			param.add(template.getMemorySize());
		}

		// 存储类型
		if (StringUtils.isNotBlank(template.getStoreType())) {
			sql.append(" and t.STORE_TYPE = ? ");
			// param.add("'" + t.getStoreType().trim() + "'");
			param.add(template.getStoreType().trim());
		}
		// cpu个数
		if (template.getCpuNum() > 0) {
			sql.append(" and t.CPU_NUM = ? ");
			param.add(template.getCpuNum());
		}

		// 网卡个数
		if (template.getVethAdaptorNum() > 0) {
			sql.append(" and t.VETH_ADAPTOR_NUM = ? ");
			param.add(template.getVethAdaptorNum());
		}
		// 磁盘大小
		if (template.getStorageSize() > 0) {
			sql.append(" and t.STORAGE_SIZE = ? ");
			param.add(template.getStorageSize());
		}

		// 服务类型
		if (product.getType() > 0) {
			sql.append(" and p.TYPE = ? ");
			param.add(product.getType());
		}
		// 名称
		if (null != product.getName() && !product.getName().equals("")) {
			sql.append(" and instr(concat(p.NAME, p.CODE), ?)>0 ");
			param.add(product.getName());
		}

		// 状态
		if (product.getState() > 0) {
			sql.append(" AND p.STATE = ?");
			param.add(product.getState());
		}

		// 价格
		if (product.getPrice() > 0) {
			sql.append(" AND p.PRICE >= ?");
			param.add(product.getPrice());
		}

		if (product.getPrice2() > 0) {
			sql.append(" AND p.PRICE <= ?");
			param.add(product.getPrice2());
		}
		// 是否首页推荐
		if (product.getIsDefault() > 0) {
			sql.append(" AND p.IS_DEFAULT = ?");
			param.add(product.getIsDefault());
		}

//		if (product.getProductItemId() != null) {
//			sql.append(" AND f.PRODUCT_ITEM_ID = ?");
//			param.add(product.getProductItemId());
//		}
		/******************************** 多虚机 ***************************************/

	}
	
	public List<Product> findByType(String searchKey,int type, int curPage, int pageSize){
		List<Object> param = new ArrayList<Object>();
		StringBuffer sql = new StringBuffer("SELECT p.* FROM T_SCS_PRODUCT p where 1=1 ");
		
		if (type > 0) {
			sql.append(" AND p.TYPE = ? ");
			param.add(type);
		}
		//to fix bug 7777 7598
		if(type == 50){
			sql.append(" AND p.TEMPLATE_ID = -1 ");
		}
		
		sql.append(" ORDER BY p.ID DESC ");
		if (curPage > 0 && pageSize > 0) {
			sql.append(" LIMIT ?, ? ");
			param.add((curPage - 1) * pageSize);
			param.add(pageSize);
		}
		return getJdbcTemplate().query(sql.toString(), param.toArray(), productMapperExtractor);
	}
	

}
