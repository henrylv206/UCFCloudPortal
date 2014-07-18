/**
 * 2012-1-30  上午10:45:52  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.action.vo.ResourcesQueryVO;
import com.skycloud.management.portal.front.resources.dao.ResourcesDao;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateMCBO;

/**
 * @author shixq
 * @version $Revision$ 上午10:45:52
 */
public class ResourcesDaoImpl implements ResourcesDao {
  private static Log log = LogFactory.getLog(ResourcesDaoImpl.class);
  private JdbcTemplate jt;

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.front.resources.dao.ResourcesDao#
   * queryHCTemplateMonitorList(java.lang.String)
   */
  @Override
  public List<TemplateMCBO> queryTemplateService(ResourcesQueryVO vo) throws SCSException {
    BeanPropertyRowMapper<TemplateMCBO> argTypes = new BeanPropertyRowMapper<TemplateMCBO>(TemplateMCBO.class);
    int userID = vo.getUser().getId();
    String monitorType = vo.getMonitorType();
    List<TemplateMCBO> list = new ArrayList<TemplateMCBO>();
    Object[] args = new Object[2];

    String sql = "SELECT t.NETWORK_DESC FROM T_SCS_ORDER o,T_SCS_INSTANCE_INFO i ,T_SCS_TEMPLATE_VM t "
        + "where o.ORDER_ID = i.ORDER_ID and i.TEMPLATE_ID = t.ID and i.TEMPLATE_TYPE = '5' and t.STATE='2' and  i.STATE in (2,6 ) and o.CREATOR_USER_ID=? and t.NETWORK_DESC like ? ";
    args[0] = userID;
    args[1] = "%" + monitorType + "%";
    try {
      list = jt.query(sql, args, argTypes);
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_MC_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_MC_COUNT_DESC);
    }
    return list;
  }

  public JdbcTemplate getJt() {
    return jt;
  }

  public void setJt(JdbcTemplate jt) {
    this.jt = jt;
  }

}
