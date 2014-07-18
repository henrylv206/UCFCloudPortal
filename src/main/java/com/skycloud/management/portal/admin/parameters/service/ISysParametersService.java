package com.skycloud.management.portal.admin.parameters.service;

import java.util.List;

import com.skycloud.management.portal.admin.parameters.entity.ApiCallLog;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;

public interface ISysParametersService {

	public List<Parameters> findAllParameters();

	public int resetJob(int asyId);

	public void updateParameters(List<Parameters> parameters);

	public int updateParameters(Parameters parameters);

	public int insertParameter(Parameters parameters);

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
	public Integer getParameterByType(String content);

	public String getParameterValueByType(String type);

	public Integer getCloudId();

	/**
	 * 获取当前工程id
	 * 
	 * @param type
	 * @return 创建人： 冯永凯 创建时间：2012-3-14 下午04:24:21
	 */
	Integer getCurProjectId();

	/**
	 * 查询线程每次读取条数
	 * 
	 * @author shixq
	 * @create-time 2012-3-15 下午03:59:28
	 * @version $Id:$
	 */
	Integer getQuerycount();

	/**
	 * 查询每个线程执行次数
	 * 
	 * @author shixq
	 * @create-time 2012-3-15 下午03:59:12
	 * @version $Id:$
	 */
	Integer getThreadExcuteCount();
}
