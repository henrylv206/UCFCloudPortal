package com.skycloud.management.portal.front.task.service.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;

import org.apache.log4j.Logger;

import com.skycloud.management.portal.admin.audit.entity.TSMSInfoBO;
import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.service.IUserLogService;
import com.skycloud.management.portal.front.task.service.ISendSmsTaskService;

public class SendSmsTaskService implements ISendSmsTaskService {

	public static Logger logger = Logger.getLogger(SendSmsTaskService.class);

	private IUserLogService userLogService;

//	private JavaMailSenderImpl mailSender;

	
    public IUserLogService getUserLogService() {
		return userLogService;
	}

	public void setUserLogService(IUserLogService userLogService) {
		this.userLogService = userLogService;
	}
	
  /**
   * 执行发送短信任务
   * @return
   * 创建人：  ninghao@chinaskycloud.com
   * 创建时间：2013-01-11 上午09:21:01
   */
	public void sendSMSTask () throws ServiceException
	{
		//查询待发送短信信息 
		PageVO po = null;
		TUserLogVO vo = new TUserLogVO();
		vo.setTargetType(ConstDef.USERLOG_TARGET_TYPE_SMS);//发短信
		vo.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建

		List<TUserLogVO> log_list = null;
		
		try {
			//查询需要发短信，且未发送（已创建）的日志
			log_list = userLogService.searchUserLogByCondition(vo, po);
		} catch (SCSException e) {
			throw new ServiceException(e.getMessage());
		}

		//循环调用发送短信网关接口
		if(log_list != null && log_list.size() > 0){
			TUserLogVO log = null;
			for(int i=0;i<log_list.size();i++){
				log = log_list.get(i);
				
				try {
					this.SpringSendLogSMS(log);
				} catch (Exception e1) {
					//send mail have error Exception
					logger.error(e1.getMessage(), e1);
//					throw new ServiceException(e1.getMessage());
					//send next sms
					continue;
				}
				//send mail success
				try {
					//更新为已发送状态
					log.setStatus(ConstDef.USER_LOG_STATUS_SEND);
					log.setStatusName(ConstDef.USER_LOG_STATUS_MAP.get(log.getStatus()));//已发送
					userLogService.updateLog(log);
				} catch (SQLException e) {
					logger.error(e.getMessage(), e);
//					throw new ServiceException(e.getMessage());
					//send next sms
					continue;
				}
			}
		}
	}

	private void SpringSendLogSMS(TUserLogVO log) throws Exception {
		//log turn to sms object
		if(log != null){
			SendMailContent content = new SendMailContent();
			
			content.setFromMail(log.getFroms());
			content.setToMail(log.getTos());
			content.setSubject(log.getSubject());
			content.setSendText(log.getComment());
			content.setSendSmsText(log.getComment());
			//send SMS
			SpringSendSMS(content);
		}
	}


	private void SpringSendSMS(SendMailContent content) {
		try {
			int time = ConfigManager.getInstance().getInt("sms.Deadtime", 0);// 读配置文件,信息有效时间
			                                                                 // 以小时为单位
			Calendar calendar = Calendar.getInstance();
			int day = calendar.get(Calendar.HOUR_OF_DAY);
			calendar.set(Calendar.HOUR_OF_DAY, day + time);// 把当前时间+信息有效时间。
			TSMSInfoBO smsInfo = new TSMSInfoBO();
			smsInfo.setMobile(content.getToMobile());
			smsInfo.setContent(content.getSendSmsText());
			smsInfo.setDeadTime(calendar.getTime());
			smsInfo.setEid(ConfigManager.getInstance().getString("sms.Eid"));
			smsInfo.setPassword(ConfigManager.getInstance().getString("sms.Password"));
			smsInfo.setStatus(0);
			smsInfo.setUserId(ConfigManager.getInstance().getString("sms.Userid"));
			smsInfo.setUsrport(ConfigManager.getInstance().getInt("sms.Userport", 0));
			// 短信内容的长度限制在255内。如果超过255，就分成多条记录插到数据库dfsdl表中。
			int length = content.getSendSmsText().length();
			int len = length % 250 == 0 ? length / 250 : length / 250 + 1;
			for (int i = 0; i < len; i++) {
				if (i == len - 1) {
					smsInfo.setContent(content.getSendSmsText().substring(i * 250, length));
				} else {
					smsInfo.setContent(content.getSendSmsText().substring(i * 250, (i + 1) * 250));
				}
				//短信发送，调用短信网关接口发送短信信息 TODO 
			}
			System.out.println(" === send sms interface ==== ");

		}
//			catch (SQLException e) {
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
