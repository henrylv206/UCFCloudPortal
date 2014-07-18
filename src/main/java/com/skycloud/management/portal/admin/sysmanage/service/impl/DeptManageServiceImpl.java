package com.skycloud.management.portal.admin.sysmanage.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.sysmanage.dao.IDeptManageDao;
import com.skycloud.management.portal.admin.sysmanage.dao.IDeptPoolDao;
import com.skycloud.management.portal.admin.sysmanage.entity.QueryCriteria;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TDeptPoolBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TResourcePoolsBO;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.admin.sysmanage.service.IDeptManageService;
import com.skycloud.management.portal.admin.sysmanage.service.IResourcePoolsService;
import com.skycloud.management.portal.admin.sysmanage.vo.ResPoolDeptRelationVO;
import com.skycloud.management.portal.exception.SCSException;

/**
 * 部门管理业务实现
  *<dl>
  *<dt>类名：DeptManageServiceImpl</dt>
  *<dd>描述: </dd>
  *<dd>公司: 天云科技有限公司</dd>
  *<dd>创建时间：2012-2-13  上午10:07:11</dd>
  *<dd>创建人： 张爽</dd>
  *</dl>
 */
public class DeptManageServiceImpl implements IDeptManageService{
	
	private Logger logger = Logger.getLogger(DeptManageServiceImpl.class);
	
	private IDeptManageDao deptDao;
	private IDeptPoolDao deptPoolDao;//TODO 配置文件需要增加
	private IResourcePoolsService resourcePoolsService;//TODO 配置文件需要增加
	
	@Override
	public int createDept(TDeptBO dept) throws SQLException {
		int flag = deptDao.saveDept(dept);
		TDeptBO dbo = findDeptByName(dept.getDeptName(),null);
		if(dbo != null && dbo.getDeptId() > 0){
			int deptId = dbo.getDeptId();
			//保存部门与资源池关系信息
			if(dept.getOldDeptName() != null && !"".equals(dept.getOldDeptName()))
			{
				//将字符串转为对象BO
				net.sf.json.JSONArray jsonArray = JSONArray.fromObject(dept.getOldDeptName());
				TDeptPoolBO[] dpbo_arr = (TDeptPoolBO[])JSONArray.toArray(jsonArray, TDeptPoolBO.class);
				//将数组转为List，并赋部门id值
				List<TDeptPoolBO> dpbo_list = new ArrayList<TDeptPoolBO>();
				if(dpbo_arr != null && dpbo_arr.length > 0){
					for(int i=0;i<dpbo_arr.length;i++){
						dpbo_arr[i].setDeptId(deptId);
						dpbo_arr[i].setState("1");
						
						dpbo_list.add(dpbo_arr[i]);
					}
					//插入部门与资源池关系信息
					this.deptPoolDao.saveDeptPoolRelation(dpbo_list);
				}
			}
		}
		return flag;
	}

	@Override
	public List<TDeptBO> getAllDept() throws SQLException {
		return deptDao.deptAll();
	}

	@Override
	public TDeptBO getDeptById(int deptId) throws SQLException {
		return deptDao.findDeptById(deptId);
	}

	@Override
	public int deleteDept(int deptId) throws SQLException {
		int nDeleteFlag = deptDao.deleteDept(deptId);
		if(deptId > 0 && nDeleteFlag > 0){
			//删除部门与资源池关系
			int delFlag = 2 ;//2：物理删除
			this.deptPoolDao.deleteDeptPoolRelation(deptId,delFlag);
		}
		return 	nDeleteFlag;
	}

	@Override
	public int updateDept(TDeptBO dept) throws SQLException {
		int nUpdateFlag = -1;
		if(dept != null && dept.getDeptId() > 0){
			nUpdateFlag = deptDao.updateDept(dept);
			int deptId = dept.getDeptId();
			if(nUpdateFlag > 0){
				//删除部门与资源池关系
				int delFlag = 1 ;//1：逻辑删除
				this.deptPoolDao.deleteDeptPoolRelation(dept.getDeptId(),delFlag);
				//部门资源池
				if(dept.getOldDeptName() != null && !"".equals(dept.getOldDeptName()))
				{
					//将字符串转为对象BO
//					JSONObject jsonObject = JSONObject.fromObject(dept.getOldDeptName());
					
					net.sf.json.JSONArray jsonArray = JSONArray.fromObject(dept.getOldDeptName());
//					List<TDeptPoolBO> dpbo_list = JSONArray.toList(jsonArray, TDeptPoolBO.class);
					TDeptPoolBO[] dpbo_arr = (TDeptPoolBO[])JSONArray.toArray(jsonArray, TDeptPoolBO.class);
					//将数组转为List，并赋部门id值
					List<TDeptPoolBO> dpbo_list = new ArrayList<TDeptPoolBO>();
					if(dpbo_arr != null && dpbo_arr.length > 0){
//						TDeptPoolBO dpbo = null;
						for(int i=0;i<dpbo_arr.length;i++){
							dpbo_arr[i].setDeptId(deptId);
							dpbo_arr[i].setState("1");
							
							dpbo_list.add(dpbo_arr[i]);
						}
						//插入部门与资源池关系信息
						this.deptPoolDao.saveDeptPoolRelation(dpbo_list);
					}
				}
			}
		}
		return 	nUpdateFlag;
	}
	
	@Override
	public TDeptBO findDeptByName(String deptName,String oldDeptName) throws SQLException {
		return deptDao.findDeptByName(deptName,oldDeptName);
	}

	public void setDeptDao(IDeptManageDao deptDao) {
		this.deptDao = deptDao;
	}

	public IDeptManageDao getDeptDao() {
		return deptDao;
	}

	@Override
	public List<TUserBO> findUserByDept(int deptId) throws SQLException {
		return deptDao.findUserByDept(deptId);
	}

	@Override
	public List<TDeptBO> searchDept(QueryCriteria criteria) throws SQLException {
		return deptDao.searchDept(criteria);
	}

	public IDeptPoolDao getDeptPoolDao() {
		return deptPoolDao;
	}

	public void setDeptPoolDao(IDeptPoolDao deptPoolDao) {
		this.deptPoolDao = deptPoolDao;
	}


	public IResourcePoolsService getResourcePoolsService() {
		return resourcePoolsService;
	}

	public void setResourcePoolsService(IResourcePoolsService resourcePoolsService) {
		this.resourcePoolsService = resourcePoolsService;
	}

	/**
	 * 查询部门已分配资源池
	 * @param deptId
	 * @return
	 * @throws SCSException
	 * 创建人：  ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  下午05:20:00
	 */
	@Override
	public List<ResPoolDeptRelationVO> findResPoolByDeptId(int deptId) throws SCSException {
		List<ResPoolDeptRelationVO> drr_list = new ArrayList<ResPoolDeptRelationVO>();
		
		if(deptId > 0){
			List<ResPoolDeptRelationVO> pool_list = this.findResPoolAndDeptPool(deptId);
			//保留已分配给本部门的资源池
			if(pool_list != null && pool_list.size() > 0){
				ResPoolDeptRelationVO dprVO = null;
				for(int i=0;i<pool_list.size();i++){
					dprVO = pool_list.get(i);
					if(dprVO.getDeptId() == deptId){
						drr_list.add(dprVO);
					}
				}
			}
		}
		
		return drr_list;
	}


	/**
	 * 查询全部资源池，并对已分配本部门的资源池做标志
	 * @param deptId
	 * @return
	 * @throws SCSException
	 * 创建人：  ninghao@chinaskycloud.com
	 * 创建时间：2012-12-06  下午05:30:00
	 */
	@Override
	public List<ResPoolDeptRelationVO> findResPoolAndDeptPool(int deptId) throws SCSException {
		List<ResPoolDeptRelationVO> drr_list = new ArrayList<ResPoolDeptRelationVO>();
		//查询全部资源池
		List<TResourcePoolsBO> all_pool_list = null;
		all_pool_list = this.resourcePoolsService.searchAll();
		
		//已分配本部门的资源池
		List<TDeptPoolBO> dept_pool_list = null;

		try {
			dept_pool_list = deptPoolDao.findDeptPoolByDeptId(deptId);
		} catch (SQLException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
			throw new SCSException(e.getMessage());
		}
		
		//并对已分配本部门的资源池做标志
		if(all_pool_list != null && all_pool_list.size() > 0){
			ResPoolDeptRelationVO drrVo = null;
			TResourcePoolsBO poolBO = null;
			TDeptPoolBO dpBO = null;
			String check = null;
			for(int i=0;i<all_pool_list.size();i++){
				poolBO = all_pool_list.get(i);
				//转为部门与资源池关系VO
				drrVo = new ResPoolDeptRelationVO();
				drrVo.setPoolId(poolBO.getId());
				drrVo.setPoolName(poolBO.getPoolName());
				
				//检查是否已分配给本部门
				check = " ";
				if(dept_pool_list != null && dept_pool_list.size() > 0){
					for(int j=0;j<dept_pool_list.size();j++){
						dpBO = dept_pool_list.get(j);
						if(dpBO.getResourcePoolId() == drrVo.getPoolId()){
							check = "checked";
							drrVo.setDeptId(deptId);
							break;
						}
					}
				}
				drrVo.setCheck(check);
				
				drr_list.add(drrVo);
			}
			drrVo = null;
			poolBO = null;
			dpBO = null;
			check = null;
		}
		all_pool_list = null;
		dept_pool_list = null;
		
		return drr_list;
	}
	
	/**
	 * 查找订单全部相关资源对应的全部资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findOrderResPoolByOrderId(int orderId) throws SQLException
	{
		return this.deptPoolDao.findOrderResPoolByOrderId(orderId);
	}
	
	/**
	 * 查找已分配给用户的资源池
	 * @param  orderId
	 * @return
	 * @throws SQLException
	 * 创建人： ninghao@chinaskycloud.com
	 * 创建时间：2012-12-07  下午6:00:00
	 */
	public List<ResPoolDeptRelationVO> findUserResPoolByUserId(int userId) throws SQLException
	{
		return this.deptPoolDao.findUserResPoolByUserId(userId);
	}

}
