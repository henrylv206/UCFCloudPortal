package com.skycloud.management.portal.front.order.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.front.order.dao.IProductInstanceRefDao;
import com.skycloud.management.portal.front.order.entity.TProductInstanceInfoRefBO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesVO;

public class ProductInstanceRefDaoImpl extends SpringJDBCBaseDao implements IProductInstanceRefDao {

	private static Log log = LogFactory.getLog(ProductInstanceRefDaoImpl.class);

	public static final String ERROR_MESSAGE_DAO_CREATE = "创建服务实例失败。服务实例名：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_DAO_DEL = "删除服务实例失败。服务实例ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_DAO_UPDATE = "修改服务实例失败。服务实例ID：%s，失败原因：%s";

	public static final String ERROR_MESSAGE_DAO_QUERY = "查询服务实例失败。查询ID：%s，失败原因：%s";

	@Override
	public int save(final TProductInstanceInfoRefBO piRef) throws SQLException {
		int returnValue = -1;
		String sql = "INSERT INTO T_SCS_PRODUCT_INSTANCE_REF("
		        + "PI_NAME,	PI_CODE,INSTANCE_INFO_ID,SERVICE_INSTANCE_ID,PRODUCT_ID,	TEMPLATE_ID,	ORDER_ID, CREATE_DT) VALUES "
		        + "(?,?,?,?,?,?,?,?);";
		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, piRef.getPiName());
					ps.setString(i++, piRef.getPiCode());
					ps.setInt(i++, piRef.getInstanceInfoId());
					ps.setInt(i++, piRef.getServiceInstanceId());
					ps.setInt(i++, piRef.getProductId());
					ps.setInt(i++, piRef.getTemplateId());
					ps.setInt(i++, piRef.getOrderId());
					ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
				}
			});
		}
		catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_CREATE, piRef.getPiName(), e.getMessage()));

		}
		return returnValue;
	}

	@Override
	public int delete(final int piId) throws SQLException {
		int returnValue = -1;
		String sql = "DELETE FROM T_SCS_PRODUCT_INSTANCE_REF WHERE PI_ID = ?;";
		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, piId);
				}
			});
		}
		catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_DEL, piId, e.getMessage()));
		}
		return returnValue;
	}

	@Override
	public int update(final TProductInstanceInfoRefBO piRef) throws SQLException {
		int returnValue = -1;
		String sql = "update T_SCS_PRODUCT_INSTANCE_REF set PI_NAME=?,	PI_CODE=?,INSTANCE_INFO_ID=?,PRODUCT_ID=?,	TEMPLATE_ID=?,	ORDER_ID=?, CREATE_DT=? WHERE PI_ID = ?;";

		try {
			returnValue = this.getJdbcTemplate().update(sql, new PreparedStatementSetter() {

				int i = 1;

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setString(i++, piRef.getPiName());
					ps.setString(i++, piRef.getPiCode());
					ps.setInt(i++, piRef.getInstanceInfoId());
					ps.setInt(i++, piRef.getProductId());
					ps.setInt(i++, piRef.getTemplateId());
					ps.setInt(i++, piRef.getOrderId());
					ps.setTimestamp(i++, new Timestamp(System.currentTimeMillis()));
					// 条件
					ps.setInt(i++, piRef.getPiId());
				}
			});
		}
		catch (Exception e) {
			log.error(e);
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_UPDATE, piRef.getPiId(), e.getMessage()));
		}
		return returnValue;
	}

	@Override
	public TProductInstanceInfoRefBO searchById(final int piId) throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF  where PI_ID = ? ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, piId);
				}
			}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, piId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList.get(0);
		} else {
			return null;
		}
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchAll() throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByOrderId(final int orderId) throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF  where ORDER_ID = ? ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, orderId);
				}
			}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, orderId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByProductId(final int productId) throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF  where PRODUCT_ID = ? ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, productId);
				}
			}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, productId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByInstanceId(final int instanceId) throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF  where INSTANCE_INFO_ID = ? ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, instanceId);
				}
			}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, instanceId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<TProductInstanceInfoRefBO> searchByTemplateId(final int templateId) throws SQLException {
		String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
		        + "from T_SCS_PRODUCT_INSTANCE_REF  where TEMPLATE_ID = ? ;";

		BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
		        TProductInstanceInfoRefBO.class);
		List<TProductInstanceInfoRefBO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

				@Override
				public void setValues(PreparedStatement ps) throws SQLException {
					ps.setInt(1, templateId);
				}
			}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, templateId, e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public List<ResourcesVO> queryPICodeVMSByUser(ResourcesQueryVO vo) throws SQLException {
		String sql = "select i.*,ref.PI_NAME ,ref.PI_CODE,t.TEMPLATE_DESC,t.CPUFREQUENCY,t.RESOURCE_POOLS_ID,tsrp.pool_name  from T_SCS_INSTANCE_INFO i "
		        + " left join T_SCS_TEMPLATE_VM t on i.TEMPLATE_ID = t.ID "
		        + " left join T_SCS_RESOURCE_POOLS tsrp on t.RESOURCE_POOLS_ID =tsrp.ID  "
		        + " join (select f.* from T_SCS_PRODUCT_INSTANCE_REF f,T_SCS_ORDER o where f.order_id = o.order_id and o.CREATOR_USER_ID= ? ) ref on i.ID = ref.INSTANCE_ID "
		        + " and i.STATE in (2,3,5,6,7) order by ref.PI_CODE,i.id";
		List<Object> args = new ArrayList<Object>();
		args.add(vo.getUser().getId());
		PageVO page = vo.getPage();
		if (page != null) {
			int curPage = page.getCurPage();
			int pageSize = page.getPageSize();
			if (curPage > 0 && pageSize > 0) {
				sql += " limit ?, ?";
				args.add((curPage - 1) * pageSize);
				args.add(pageSize);
			}
		}
		BeanPropertyRowMapper<ResourcesVO> piRefRowMapper = new BeanPropertyRowMapper<ResourcesVO>(ResourcesVO.class);

		List<ResourcesVO> returnList = null;
		try {
			returnList = this.getJdbcTemplate().query(sql, args.toArray(), new RowMapperResultSetExtractor<ResourcesVO>(piRefRowMapper));
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, vo.getUser().getId(), e.getMessage()));
		}
		if (returnList != null && returnList.size() > 0) {
			return returnList;
		} else {
			return null;
		}
	}

	@Override
	public int queryCountPICodeVMSByUser(final int userId) throws SQLException {
		String sql = "select  COUNT(0) from T_SCS_INSTANCE_INFO i "
		        + " left join T_SCS_TEMPLATE_VM t on i.TEMPLATE_ID = t.ID "
		        + " join (select f.* from T_SCS_PRODUCT_INSTANCE_REF f,T_SCS_ORDER o where f.order_id = o.order_id and o.CREATOR_USER_ID= ? ) ref on i.ID = ref.INSTANCE_ID "
		        + " and i.STATE in (2,3,5,6,7) ;";
		int ret = 0;
		try {
			ret = this.getJdbcTemplate().queryForInt(sql, userId);
		}
		catch (Exception e) {
			throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, userId, e.getMessage()));
		}
		return ret;
	}

	@Override
    public List<TProductInstanceInfoRefBO> searchByServiceId(final int serviceId) throws SQLException {
			String sql = "select PI_ID,PI_NAME,PI_CODE,SERVICE_INSTANCE_ID,INSTANCE_INFO_ID,PRODUCT_ID,TEMPLATE_ID,ORDER_ID, CREATE_DT  "
			        + "from T_SCS_PRODUCT_INSTANCE_REF  where SERVICE_INSTANCE_ID = ? ;";

			BeanPropertyRowMapper<TProductInstanceInfoRefBO> piRefRowMapper = new BeanPropertyRowMapper<TProductInstanceInfoRefBO>(
			        TProductInstanceInfoRefBO.class);
			List<TProductInstanceInfoRefBO> returnList = null;
			try {
				returnList = this.getJdbcTemplate().query(sql, new PreparedStatementSetter() {

					@Override
					public void setValues(PreparedStatement ps) throws SQLException {
						ps.setInt(1, serviceId);
					}
				}, new RowMapperResultSetExtractor<TProductInstanceInfoRefBO>(piRefRowMapper));
			}
			catch (Exception e) {
				throw new SQLException(String.format(this.ERROR_MESSAGE_DAO_QUERY, serviceId, e.getMessage()));
			}
			if (returnList != null && returnList.size() > 0) {
				return returnList;
			} else {
				return null;
			}
	}
	
	
}
