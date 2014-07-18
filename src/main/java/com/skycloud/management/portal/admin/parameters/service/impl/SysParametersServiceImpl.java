package com.skycloud.management.portal.admin.parameters.service.impl;

import java.util.List;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.parameters.entity.ApiCallLog;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;
import com.skycloud.management.portal.admin.parameters.service.ISysParametersService;
import com.skycloud.management.portal.common.utils.ConstDef;

public class SysParametersServiceImpl implements ISysParametersService {

	private ISysParametersDao parametersDao;

	public ISysParametersDao getParametersDao() {
		return parametersDao;
	}

	public void setParametersDao(ISysParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	@Override
	public List<Parameters> findAllParameters() {
		return parametersDao.findAll();
	}

	@Override
	public void updateParameters(List<Parameters> parameters) {
		parametersDao.updateParameter(parameters);
	}

	@Override
	public int updateParameters(Parameters parameters) {
		return parametersDao.updateParameter(parameters);
	}

	@Override
	public int insertParameter(Parameters parameters) {
		return parametersDao.insertParameter(parameters);
	}

	@Override
	public int deleteParameter(int id) {
		return parametersDao.deleteParameter(id);
	}

	@Override
	public Parameters queryParameterById(int id) {
		return parametersDao.queryParameterById(id);
	}

	@Override
	public List<Parameters> queryParameters(int curPage, int pageSize, String searchKey) {
		return parametersDao.queryParameters(curPage, pageSize, searchKey);
	}

	@Override
	public List<Parameters> queryParameters2(int curPage, int pageSize, String searchKey) {
		return parametersDao.queryParameters2(curPage, pageSize, searchKey);
	}

	@Override
	public int countParameters(String searchKey) {
		return parametersDao.countParameters(searchKey);
	}

	@Override
	public int resetJob(int instanceId) {
		return parametersDao.updateResetJob(instanceId);
	}

	@Override
	// fixed bug 2433 start
	public List<ApiCallLog> queryElasterApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		return parametersDao.queryElasterApiCallLog(curPage, pageSize, start, end, qid, insName);
	}

	@Override
	public int countElasterApiCallLog(String start, String end, int qid, String insName) {
		return parametersDao.countElasterApiCallLog(start, end, qid, insName);
	}

	@Override
	public List<ApiCallLog> queryH3cApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		return parametersDao.queryH3cApiCallLog(curPage, pageSize, start, end, qid, insName);
	}

	@Override
	public int countH3cApiCallLog(String start, String end, int qid, String insName) {
		return parametersDao.countH3cApiCallLog(start, end, qid, insName);
	}

	@Override
	public List<ApiCallLog> queryS3ApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName) {
		return parametersDao.queryS3ApiCallLog(curPage, pageSize, start, end, qid, insName);
	}

	@Override
	public int countS3ApiCallLog(String start, String end, int qid, String insName) {
		return parametersDao.countS3ApiCallLog(start, end, qid, insName);
	} // fixed bug 2433 end

	@Override
	public Integer getCurProjectId() {
		Integer projectId = null;
		try {
			if (ConstDef.curProjectId > 0) {
				projectId = ConstDef.curProjectId;
			} else {
				//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
				String strProjectId = parametersDao.getParameter("PROJECT_SWITCH");
				if(null != strProjectId && !strProjectId.equals("")){
					projectId = Integer.parseInt(strProjectId);
					ConstDef.curProjectId = projectId;
				}				
			}
			return projectId;
		}
		catch (Exception e) {
			return projectId;
		}
	}

	@Override
	public Integer getCloudId() {
		Integer cloudId = null;
		try {
			if (ConstDef.curCloudId > 0) {
				cloudId = ConstDef.curCloudId;
			} else {
				//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
				String strCloudId = parametersDao.getParameter("PUBLIC_PRIVATE_CLOUD");
				if(null != strCloudId && !strCloudId.equals("")){
					cloudId = Integer.parseInt(strCloudId);
					ConstDef.curCloudId = cloudId;
				}				
			}
			return cloudId;
		}
		catch (Exception e) {
			return cloudId;
		}
	}

	@Override
	public Integer getParameterByType(String content) {
		Integer mailParameter = null;
		try {
			//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
			String strMailParameter = parametersDao.getParameter(content);
			if(null != strMailParameter && !strMailParameter.equals("")){
				mailParameter = Integer.parseInt(strMailParameter);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return mailParameter;
	}

	@Override
	public String getParameterValueByType(String type) {
		String ret_value = null;
		try {
			ret_value = parametersDao.getParameterValueByType(type);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret_value;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.admin.parameters.service.ISysParametersService
	 * #getQuerycount()
	 */
	@Override
	public Integer getQuerycount() {
		Integer queryCount = null;
		try {
			//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
			String strQueryCount = parametersDao.getParameter("JOB_QUERY_COUNT");
			if(null != strQueryCount && !strQueryCount.equals("")){
				queryCount = Integer.parseInt(strQueryCount);
			}			
		}
		catch (Exception e) {
			return queryCount;
		}
		return queryCount;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.skycloud.management.portal.admin.parameters.service.ISysParametersService
	 * #getThreadExcuteCount()
	 */
	@Override
	public Integer getThreadExcuteCount() {
		Integer threadExcuteCount = null;
		try {
			//modified by zhanghuizheng 20130121 参数表的value改为string类型后，查询数据库的返回值改为string类型的
			String strThreadExcuteCount = parametersDao.getParameter("JOB_THREAD_EXCUTE_COUNT");
			threadExcuteCount = Integer.parseInt(strThreadExcuteCount);
		}
		catch (Exception e) {
			return threadExcuteCount;
		}
		return threadExcuteCount;
	}
	
	public String getParametersByType(String type) {
		String ret_value = null;
		try {
			String params = parametersDao.getParameterValueByType(type);
			if(null != params && !params.isEmpty()){
				String[] arr = params.split("|");
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return ret_value;
	}
	

}
