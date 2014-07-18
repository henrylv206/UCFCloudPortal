package com.skycloud.management.portal.admin.parameters.dao;

import java.util.List;

import com.skycloud.management.portal.admin.parameters.entity.ApiCallLog;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;

/**
 * 系统参数持久化接口
 * 
 * @author jiaoyz
 */
public interface ISysParametersDao {

	public String getParameter(String type) throws Exception;

	public String getParameterValueByType(String type) throws Exception;

	public List<Parameters> findAll();

	public int updateResetJob(int asyId);

	public int updateParameter(Parameters param);

	public void updateParameter(List<Parameters> params);

	public int insertParameter(Parameters param);

	public int deleteParameter(int id);

	public Parameters queryParameterById(int id);

	public List<Parameters> queryParameters(int curPage, int pageSize, String searchKey);

	public List<Parameters> queryParameters2(int curPage, int pageSize, String searchKey);

	public int countParameters(String searchKey);

	// fixed bug 2433 start
	public List<ApiCallLog> queryElasterApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName);

	public int countElasterApiCallLog(String start, String end, int qid, String insName);

	public List<ApiCallLog> queryH3cApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName);

	public int countH3cApiCallLog(String start, String end, int qid, String insName);

	public List<ApiCallLog> queryS3ApiCallLog(int curPage, int pageSize, String start, String end, int qid, String insName);

	public int countS3ApiCallLog(String start, String end, int qid, String insName);

	// fixed bug 2433 end

	/**
	 * 查询虚拟机资源、块存储资源、公网IP的阀值
	 */
	public int findThresholdsByFieldName(String fieldName);

}
