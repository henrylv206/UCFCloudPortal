package com.skycloud.management.portal.admin.resmanage.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.admin.resmanage.entity.ProuctChargeUnit;

public interface IProductChargeUnitDao {
	/**************************************************************************
	 * @content: 保存产品的计价信息，具体为TScsProuctChargeUnit表，主键自动生成
	 * @param  : dataList　                             
	 * @return : List<Integer> 主键的集合                            
	 * @author : 那建林                                                                                                                
	 * @crtDate: 2012-09-13 10:２8                                            
	 */
	List<Integer> save(List<ProuctChargeUnit> dataList);
	
	
	/**************************************************************************
	 * @content: 根据PRODUCT_ID查询出此产品的计价信息，要带出主表TScsChargeUnit信息
	 * @param  : productId　                             
	 * @return : List<TScsProuctChargeUnit>                            
	 * @author : 那建林                                                                                                                
	 * @throws SQLException 
	 * @crtDate: 2012-09-13 10:58                                            
	 */
	List<ProuctChargeUnit> findByProductId(String productId) throws SQLException;
	
	/**************************************************************************
	 * @content: 得到下一个６位流水号,全表扫描后６位最大值加１
	 * @return : String                       
	 * @author : 那建林                                                                                                                
	 * @throws SQLException 
	 * @crtDate: 2012-09-13 15:50                                            
	 */
	String getNextStreamNumber();
//	public void delete(int id);
//	public void update(TScsProuctChargeUnit scsProuctChargeUnit);
//	public List<TScsProuctChargeUnit> findAll();


	/**
	 * 修改资源计费标准中间表TScsProuctChargeUnit
	 * @param dataListMod
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 * 创建人：马志刚
	 * 创建时间 ： 2012-10-25 下午02:12:27
	 */
	List<Integer> modify(List<ProuctChargeUnit> dataListMod) throws NumberFormatException, SQLException;


	/** 
	 * 删除资源计费标准中间表TScsProuctChargeUnit
	 * @param dataListdel
	 * @return
	 * @throws NumberFormatException
	 * @throws SQLException
	 * 创建人：马志刚
	 * 创建时间 ： 2012-10-25 下午02:12:30
	 */
	List<Integer> delete(List<ProuctChargeUnit> dataListdel) throws NumberFormatException, SQLException;

	/**
	 * 删除资源计费标准中间表TScsProuctChargeUnit
	 * @param id
	 * @return
	 * @throws SQLException
	 * 创建人：马志刚
	 * 创建时间 ： 2012-10-25 下午02:11:54
	 */
	int delete(int id) throws SQLException;

	/**
	 * 修改资源计费标准中间表TScsProuctChargeUnit
	 * @param entity
	 * @return
	 * @throws SQLException
	 * 创建人：马志刚
	 * 创建时间 ： 2012-10-25 下午02:11:49
	 */
	int update(ProuctChargeUnit entity) throws SQLException;
	
	/**
	 * 新增 TScsProuctChargeUnit
	 * @param entity
	 * @return
	 * @throws SQLException
	 * 创建人：覃文讲
	 * 创建时间 ： 2012-11-27 上午11:25:00
	 */
	int add(ProuctChargeUnit entity) throws SQLException;
}
