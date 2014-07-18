package com.skycloud.management.portal.front.subaccount.dao;

import java.sql.SQLException;
import java.util.List;

import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.subaccount.entity.TPortalMenu;
import com.skycloud.management.portal.front.subaccount.entity.TPortalMenuRelation;

public interface ISubAccountDao {
	
//    int save(TUserLogVO log) throws  SCSException;
//	
//	int delete(int id) throws SQLException;
//	
//	int update(TUserLogVO log) throws SQLException;
//
//	List<TUserLogVO> searchAllUserLog(PageVO vo) throws SQLException;
	
	List<TPortalMenu> getAllPortalMenu() throws SQLException;
	
	public int upDateMenuPortalRelation(final Integer userId, final String[] menuIdAry) throws SQLException;

	List<TPortalMenuRelation> getAllPortalMenuRelationByUserId(Integer userId) throws SQLException;
	
	public List<TPortalMenu> searchUserIdByParam(String userName, String companyCode) throws SQLException;
}
