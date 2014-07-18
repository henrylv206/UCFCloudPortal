package com.skycloud.management.portal.admin.resmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.ChargeUnit;


/**
 * * haolong@20120910 01 计费模式变更
 */
public interface IChargeUnitDao {
//	public int save(TScsChargeUnit scsChargeUnit);
//	public void delete(int id);
//	public void update(TScsChargeUnit scsChargeUnit);
	/**************************************************************************
	 * @content: 查询出所有有效(status：0无用，１可用)的计费方式
	 * @return : List<TScsChargeUnit>                            
	 * @author : 那建林                                                                                                                
	 * @throws SQLException 
	 * @crtDate: 2012-09-13 10:58                                            
	 */
	List<ChargeUnit> findAll() throws SQLException;

	ChargeUnit findByUnit(String unit) throws SQLException;
	
	int findIdByUnit(String unit);

}
