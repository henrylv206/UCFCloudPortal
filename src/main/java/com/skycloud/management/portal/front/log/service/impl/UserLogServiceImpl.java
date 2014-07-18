package com.skycloud.management.portal.front.log.service.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeanUtils;

import com.skycloud.management.portal.admin.parameters.dao.ISysParametersDao;
import com.skycloud.management.portal.admin.parameters.entity.Parameters;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.dao.IUserLogConfigDao;
import com.skycloud.management.portal.front.log.dao.IUserLogDao;
import com.skycloud.management.portal.front.log.entity.TUserLogConfig;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.service.IUserLogService;
import com.skycloud.management.portal.front.log.util.IImportUserLog;
import com.skycloud.management.portal.front.log.util.impl.ImportUserLogLocal;

public class UserLogServiceImpl implements IUserLogService {
	
	private static Log log = LogFactory.getLog(UserLogServiceImpl.class);
	
	public static final Map<String, TUserLogConfig> configMap = new HashMap<String, TUserLogConfig>();
	
	public static long configReadTime = 0;

	private String fromMail = ConfigManager.getInstance().getString("mail.from");
	private String mailSubject = ConfigManager.getInstance().getString("mail.log.subject");
	private String smsContent = ConfigManager.getInstance().getString("sms.log.content");
	private String mailContent;

	private IUserLogDao userLogDao;

	private IUserLogConfigDao configDao;

	private ISysParametersDao parametersDao;

	public ISysParametersDao getParametersDao() {
		return parametersDao;
	}

	public void setParametersDao(ISysParametersDao parametersDao) {
		this.parametersDao = parametersDao;
	}

	@Override
	public int saveLog(TUserLogVO log) throws SCSException {
		if (log == null) {
			return 0;
		}
		return userLogDao.save(log);
	}

	@Override
	public int saveLog(TUserLogVO log, TUserBO user) throws SCSException {
		if (log == null) {
			return 0;
		}
//		System.out.println("saveLog");
		int saveResult = -1;
		
		//检查并获取日志配置
		this.getAllLogConfig();
		
		// 查询本操作的配置
		TUserLogConfig config = this.getOperateLogConfig(log);
		
		if(config != null){
			log.setCreateDt(new Date());
			//若需要记录消息
			if(config.getLogFlag() == ConstDef.YES_OR_NO_YES){
				//默认插入：数据库日志
				log.setTargetType(ConstDef.USERLOG_TARGET_TYPE_LOG);//消息
				log.setSubject(log.getComment());
				log.setMemo(ConstDef.USER_LOG_TARGET_TYPE_MAP.get(log.getTargetType()));
				log.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建
				log.setStatusName(ConstDef.USER_LOG_STATUS_MAP.get(log.getStatus()));//已创建
				saveResult = userLogDao.save(log);
				if(saveResult <= 0){
					return saveResult;
				}
			}
			if(config.getMsgFlag() == ConstDef.YES_OR_NO_YES){
				//消息通知
				log.setTargetType(ConstDef.USERLOG_TARGET_TYPE_MSG);//消息通知
				log.setSubject(log.getComment());
				log.setMemo(ConstDef.USER_LOG_TARGET_TYPE_MAP.get(log.getTargetType()));
				log.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建
				log.setStatusName(ConstDef.USER_LOG_STATUS_MAP.get(log.getStatus()));//已创建
				log.setCreateDt(new Date());
				saveResult = userLogDao.save(log);
				if(saveResult <= 0){
					return saveResult;
				}
			}
			//若需要发邮件
			if(config.getMailFlag() == ConstDef.YES_OR_NO_YES){
				TUserLogVO logMail = this.sendMailInfo(log,user);
				saveResult = userLogDao.save(logMail);
				if(saveResult <= 0){
					return saveResult;
				}
			}
			//若需要发短信
			if(config.getSmsFlag() == ConstDef.YES_OR_NO_YES){
				TUserLogVO logSms = this.sendSmsInfo(log,user);
				saveResult = userLogDao.save(logSms);
				if(saveResult <= 0){
					return saveResult;
				}
			}
		}
		
		return saveResult;
	}
	
	private TUserLogVO sendMailInfo(TUserLogVO logVO,TUserBO user){
		TUserLogVO logMail = new TUserLogVO();
		
		BeanUtils.copyProperties(logVO, logMail);
		logMail.setId(0);
		logMail.setUserId(user.getId());
		logMail.setFroms(this.fromMail);
		logMail.setTos(user.getEmail());
		logMail.setTargetType(ConstDef.USERLOG_TARGET_TYPE_MAIL);//发邮件
		logMail.setMemo(ConstDef.USER_LOG_TARGET_TYPE_MAP.get(logMail.getTargetType()));
		logMail.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建
		logMail.setStatusName(ConstDef.USER_LOG_STATUS_MAP.get(logMail.getStatus()));//已创建
		//处理邮件标题、内容
		StringBuilder subject = new StringBuilder(this.mailSubject);
		StringBuilder content = new StringBuilder(this.mailContent);
		int index = 0;
		String replaceStr = "";
		
		replaceStr = "#username#";
		index = content.indexOf(replaceStr);
		if(index >= 0){
			content = content.replace(index, index+replaceStr.length(), user.getName());
		}
		
		replaceStr = "#subject#";
		index = subject.indexOf(replaceStr);
		if(index >= 0){
			subject = subject.replace(index, index+replaceStr.length(), logVO.getModuleName()+"——"+logVO.getFunctionName());
		}
		replaceStr = "#operate#";
		index = content.indexOf(replaceStr);
		if(index >= 0){
			content = content.replace(index, index+replaceStr.length(), logVO.getFunctionName());
		}
		replaceStr = "#optime#"; 
		index = content.indexOf(replaceStr);
		if(index >= 0){ 
			content = content.replace(index, index+replaceStr.length(), logVO.getCreateDtString());
		}
		
		logMail.setSubject(subject.toString());
		logMail.setComment(content.toString());
		
		return logMail;
	}
	
	private TUserLogVO sendSmsInfo(TUserLogVO logVO,TUserBO user){
		TUserLogVO logSms = new TUserLogVO();
		
		BeanUtils.copyProperties(logVO, logSms);
		
		logSms.setId(0);
		logSms.setUserId(user.getId());
//		logSms.setFroms(this.fromMail);
		logSms.setTos(user.getMobile());
		logSms.setTargetType(ConstDef.USERLOG_TARGET_TYPE_SMS);//发短信
		logSms.setMemo(ConstDef.USER_LOG_TARGET_TYPE_MAP.get(logSms.getTargetType()));
		logSms.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建
		logSms.setStatusName(ConstDef.USER_LOG_STATUS_MAP.get(logSms.getStatus()));//已创建
		//处理邮件标题、内容
		StringBuilder subject = new StringBuilder(this.mailSubject);
		StringBuilder content = new StringBuilder(this.smsContent);
		int index = 0;
		String replaceStr = "#subject#";
		index = subject.indexOf(replaceStr);
		subject = subject.replace(index, index+replaceStr.length(), logVO.getModuleName()+"——"+logVO.getFunctionName());
		//用户名称
		replaceStr = "#username#";
		index = content.indexOf(replaceStr);
		if(index >= 0){
			content = content.replace(index, index+replaceStr.length(), user.getName());
		}
		
		//用户操作
		replaceStr = "#operate#";
		index = content.indexOf(replaceStr);
		if(index >= 0){
			content = content.replace(index, index+replaceStr.length(), logVO.getFunctionName());
		}
		replaceStr = "#optime#"; 
		index = content.indexOf(replaceStr);
		if(index >= 0){
			content = content.replace(index, index+replaceStr.length(), logVO.getCreateDtString());
		}
		
		logSms.setSubject(subject.toString());
		logSms.setComment(content.toString());
		
		return logSms;
	}
	
	private void getAllLogConfig()
	{
		//判断是否需要读取配置表
		//是否需要读取配置标志
		boolean readConfig = false;
		if(configReadTime <= 0 || configMap == null){
			readConfig = true;
		}else{
			long nowTime = (new Date()).getTime();
			long stepTime = 1000*60*5;//5分钟重新读取一次
			if(nowTime > configReadTime && nowTime - configReadTime > stepTime){
				readConfig = true;
			}
		}
		if(readConfig){
			// 读取全部配置
			TUserLogConfig vo = new TUserLogConfig();
			PageVO po = new PageVO();
			
			try {
				List<TUserLogConfig> configList = this.configDao.searchAllUserLogConfigByCondition(vo, po);
			
				if(configList != null && configList.size() > 0){
					String key = null;
					TUserLogConfig config = null;
					for(int i=0;i<configList.size();i++){
						config = configList.get(i);
						key = config.getKeyName();
						configMap.put(key, config);
					}
				}
			
			} catch (SQLException e) {
				e.printStackTrace();
				log.error("error", e);
			}
			
		}
	}
	
	private TUserLogConfig getOperateLogConfig(TUserLogVO log)
	{
		TUserLogConfig config = null;
		if(log == null){
			return null;
		}
		//从日志配置缓存中获取配置信息
		String key = log.getClassName() + "." + log.getMethodName();
		if(log != null){
			Object obj = configMap.get(key);
			if(obj != null){
				config = (TUserLogConfig)obj;
			}
		}
		if(config == null){
			config = new TUserLogConfig();
			config.setKeyName(key);
			config.setName(log.getModuleName()+"-"+log.getFunctionName());
			config.setStatus(ConstDef.STATE_ONE);
			config.setLogFlag(ConstDef.YES_OR_NO_YES);
//			config.setMailFlag(ConstDef.YES_OR_NO_NO);
//			config.setSmsFlag(ConstDef.YES_OR_NO_NO);
			String remark = log.getModuleName()+"-"+log.getFunctionName()+"-"+log.getComment();
			if(remark != null && remark.length() > 250){
				remark = remark.substring(0, 250);
			}
			config.setRemark(remark);
			
			try {
				this.configDao.save(config);
				configMap.put(key, config);
			} catch (SQLException e) {
				e.printStackTrace();
				this.log.error("error", e);
			}
			
		}
		
		return config;
	}

	@Override
	public int deleteLog(int id) throws SQLException {
		return userLogDao.delete(id);
	}

	@Override
	public int updateLog(TUserLogVO log) throws SQLException {
		if (log == null) {
			return 0;
		}
		return userLogDao.update(log);
	}

	@Override
	public List<TUserLogVO> searchAllUserLog(PageVO vo) throws SQLException {
		return userLogDao.searchAllUserLog(vo);
	}

	public IUserLogDao getUserLogDao() {
		return userLogDao;
	}

	public void setUserLogDao(IUserLogDao userLogDao) {
		this.userLogDao = userLogDao;
	}

	public IUserLogConfigDao getConfigDao() {
		return configDao;
	}

	public void setConfigDao(IUserLogConfigDao configDao) {
		this.configDao = configDao;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	@Override
	public TUserLogVO findUserLogById(int id) throws SQLException {
		return userLogDao.findUserLogById(id);
	}

	@Override
	public List<TUserLogVO> searchUserLogByCondition(PageVO vo, String account, Integer type, String moduleName, String startDate, String endDate,
	        String functionName) throws SQLException {
		return userLogDao.searchUserLogByCondition(vo, account, type, moduleName, startDate, endDate, functionName);
	}

	@Override
	public int searchAllUserLogAcount(TUserLogVO vo) throws SQLException {
		return userLogDao.searchAllUserLogAcount(vo);
	}

	/**
	 * 导出日志。成功返回true,失败返回false
	 */
	@Override
	public boolean importLog() throws SQLException {
		// TODO Auto-generated method stub
		int log_file_size = 10;// 默认10M
		int maxid = 0;
		List<TUserLogVO> list = userLogDao.getAllUserLog(null);
		List<Parameters> parmList = parametersDao.queryParameters(1, 10, "LOG_FILE_SIZE");
		if (!parmList.isEmpty()) {
			Parameters param = parmList.get(0);
			log_file_size = Integer.valueOf(param.getValue());
		}

		IImportUserLog log = new ImportUserLogLocal();
		try {
			if (list != null && !list.isEmpty()) {
				maxid = list.get(0).getId() + 1;
				boolean flag = log.importLog(list, log_file_size);
				if (flag) {
					userLogDao.deleteAll(maxid + 1);
				}
				return true;
			}
		}
		catch (Exception e) {
			e.printStackTrace();
			this.log.error("error", e);
		}
		return false;
	}

	@Override
	public String findModuleNames() {
		List<Map<String, Object>> list = this.userLogDao.findModuleNames();
		String moduleNames = "";
		if (null != list && list.size() > 0) {
			List<Map<String, String>> reList = new ArrayList<Map<String, String>>();
			for (int i = 0; i < list.size(); i++) {
				Map<String, Object> map = list.get(i);
				if (map.get("MODULE_NAME") != null) {
					Map<String, String> optionMap = new HashMap<String, String>();
					optionMap.put("text", String.valueOf(map.get("MODULE_NAME")));
					optionMap.put("value", String.valueOf(i));
					reList.add(optionMap);

				}
			}

			moduleNames = JSONArray.fromObject(reList).toString();

		}
		return moduleNames;
	}
	
	public List<TUserLogVO> searchUserLogByCondition(TUserLogVO vo , PageVO po) throws SCSException
	{
		List<TUserLogVO> log_list = null;

		try {
			log_list = this.userLogDao.searchUserLogByCondition(vo, po);
		} catch (SQLException e) {
			log.error("error", e);
			throw new SCSException("error",e.getMessage());
		}
		
		return log_list;
	}

}
