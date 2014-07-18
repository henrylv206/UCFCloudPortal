/**
 * 2011-11-28  下午04:30:53  $Id:shixq
 */
package com.skycloud.management.portal.front.resources.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import com.skycloud.management.portal.SCSErrorCode;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.resources.dao.HCTemplateDao;
import com.skycloud.management.portal.front.resources.dao.bo.TemplateMCBO;

/**
 * @author shixq
 * @version $Revision$ 下午04:30:53
 */
public class HCTemplateDaoImpl implements HCTemplateDao {

  private static Log log = LogFactory.getLog(HCTemplateDaoImpl.class);
  private JdbcTemplate jt;

  /*
   * (non-Javadoc)
   * 
   * @see com.skycloud.management.portal.front.resources.dao.HCTemplateDao#
   * queryHCTemplateCPUAndMemoryAvailableList()
   */
  @Override
  public List<TemplateMCBO> queryHCTemplateCpuAndMemAvailableList(String type) throws SCSException {
    BeanPropertyRowMapper<TemplateMCBO> argTypes = new BeanPropertyRowMapper<TemplateMCBO>(TemplateMCBO.class);
    List<TemplateMCBO> list = new ArrayList<TemplateMCBO>();
    try {
      list = jt.query("select DISTINCT " + type + " from T_SCS_TEMPLATE_MC where state='2'", argTypes);
    } catch (Exception e) {
      log.error(e);
      throw new SCSException(SCSErrorCode.DB_SQL_QUERY_TEMPLATE_MC_COUNT_ERROR, SCSErrorCode.DB_SQL_QUERY_TEMPLATE_MC_COUNT_DESC);
    }
    return list;
  }
  @Override
  public List<Map<String,Object>> queryVMServer(){
	  String sql = "select * from VM_SERVER vs where vs.SYS_NAME like '%VM_%'";
	  return this.getJt().queryForList(sql);
  }
  
  public JdbcTemplate getJt() {
    return jt;
  }

  public void setJt(JdbcTemplate jt) {
    this.jt = jt;
  }

}
