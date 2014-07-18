package com.skycloud.management.update.portal.admin.resmanage.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import com.skycloud.management.portal.common.utils.SpringJDBCBaseDao;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.update.portal.admin.resmanage.entity.ResourcePoolStatusPO;

public class ResourcePoolStatusDaoImpl extends SpringJDBCBaseDao
implements ResourcePoolStatusDao {
    private static final Logger logger = Logger.getLogger(ResourcePoolStatusDaoImpl.class);
    @Override
    public List<ResourcePoolStatusPO> queryResourcePoolStatus(int curPage, int pageSize) throws SCSException {
        ArrayList<ResourcePoolStatusPO> res = new ArrayList<ResourcePoolStatusPO>();
        
        try {
            String sql = "SELECT * FROM T_SCS_RESOURCE_STATE LIMIT ?,?;";
            
            List<Object> param = new ArrayList<Object>();
            param.add((curPage-1)*pageSize);
            param.add(pageSize);
            /*String sql = "select a1.* from T_SCS_RESOURCE_STATE a1"+
                         " inner join"+
                         " (select a.MODIFY_DATE,a.TYPE from T_SCS_RESOURCE_STATE a left join T_SCS_RESOURCE_STATE b"+
                         " on a.TYPE=b.TYPE and a.MODIFY_DATE<=b.MODIFY_DATE"+
                         " group by a.MODIFY_DATE,a.MODIFY_DATE"+
                         " having count(b.MODIFY_DATE)<=1"+
                         " )b1"+
                         " on a1.TYPE=b1.TYPE and a1.MODIFY_DATE=b1.MODIFY_DATE"+
                         " order by a1.TYPE,a1.MODIFY_DATE desc";
            */
            List<Map<String,Object>> list = this.jdbcTemplate.queryForList(sql, param.toArray());
            for(Map<String,Object> map : list) {
                ResourcePoolStatusPO po = new ResourcePoolStatusPO();
                po.setId((Integer)map.get("ID"));
                po.setDescription((String)map.get("DESCRIPTION"));
                po.setModifyDate((Date)map.get("MODIFY_DATE"));
                po.setSurplus((Integer)map.get("SURPLUS"));
                po.setTotal((Integer)map.get("TOTAL"));
                po.setType((Integer)map.get("TYPE"));
                po.setUsed((Float)map.get("USED"));
                
                res.add(po);
            }
        } catch (Exception ex) {
            logger.error("queryResourcePoolStatus Error", ex);
            throw new SCSException("queryResourcePoolStatus error：" + ex.getMessage(), ex);
        }
        return res;
    }
    @Override
    public int queryResourcePoolStatusTotal() throws SCSException {
        int total = 0;
        
        try {
            String sql = "SELECT COUNT(*) FROM T_SCS_RESOURCE_STATE;";
            total = jdbcTemplate.queryForInt(sql);
        } catch (Exception ex) {
            logger.error("queryResourcePoolStatus Error", ex);
            throw new SCSException("queryResourcePoolStatus error：" + ex.getMessage(), ex);
        }
        return total;
    }
    @Override
    public void newPoolState(final ResourcePoolStatusPO newState) throws SCSException {
        final String sql = "INSERT INTO T_SCS_RESOURCE_STATE(TYPE,TOTAL,USED,SURPLUS,MODIFY_DATE,DESCRIPTION)"+
        " VALUES (?,?,?,?,?,?);";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        
        this.getJdbcTemplate().update(new PreparedStatementCreator(){
            int i=1;
            @Override
            public PreparedStatement createPreparedStatement(Connection con)throws SQLException {
                PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
                ps.setInt(i++, newState.getType());
                ps.setInt(i++, newState.getTotal());
                ps.setFloat(i++, newState.getUsed());
                ps.setInt(i++, newState.getSurplus());
                ps.setTimestamp(i++, new Timestamp(newState.getModifyDate().getTime()));
                ps.setString(i++, newState.getDescription());
                return ps;
            }
        }, keyHolder); 
        newState.setId(keyHolder.getKey().intValue());
    }
    @Override
    public void updatePoolState(ResourcePoolStatusPO poolState)
            throws SCSException {
        String sql = "UPDATE T_SCS_RESOURCE_STATE SET TYPE=?,TOTAL=?,USED=?,SURPLUS=?,MODIFY_DATE=?,DESCRIPTION=? WHERE ID=?;";
        List<Object> param = new ArrayList<Object>();
        param.add(poolState.getType());
        param.add(poolState.getTotal());
        param.add(poolState.getUsed());
        param.add(poolState.getSurplus());
        param.add(poolState.getModifyDate());
        param.add(poolState.getDescription());
        param.add(poolState.getId());
        
        jdbcTemplate.update(sql, param.toArray());
    }
}
