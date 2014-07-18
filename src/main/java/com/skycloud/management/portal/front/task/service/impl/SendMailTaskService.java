package com.skycloud.management.portal.front.task.service.impl;

import java.sql.SQLException;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;
import com.skycloud.management.portal.common.entity.PageVO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.common.utils.ConstDef;
import com.skycloud.management.portal.common.utils.ServiceException;
import com.skycloud.management.portal.exception.SCSException;
import com.skycloud.management.portal.front.log.entity.TUserLogVO;
import com.skycloud.management.portal.front.log.service.IUserLogService;
import com.skycloud.management.portal.front.task.service.ISendMailTaskService;

public class SendMailTaskService implements ISendMailTaskService {

	public static Logger logger = Logger.getLogger(SendMailTaskService.class);

	private IUserLogService userLogService;

	private JavaMailSenderImpl mailSender;

	public static boolean inRunning = false;//false=no,true=yes 

	
    public IUserLogService getUserLogService() {
		return userLogService;
	}

	public void setUserLogService(IUserLogService userLogService) {
		this.userLogService = userLogService;
	}

/**
   * 执行发送邮件任务
   * @return
   * 创建人：  ninghao@chinaskycloud.com
   * 创建时间：2013-01-11 上午09:21:01
   */
	public synchronized void sendMailTask() throws ServiceException
	{
		if(!inRunning){
			inRunning = true;
			//查询待发送邮件信息
			PageVO po = null;
			TUserLogVO vo = new TUserLogVO();
			vo.setTargetType(ConstDef.USERLOG_TARGET_TYPE_MAIL);//发邮件
			vo.setStatus(ConstDef.USER_LOG_STATUS_CREATE);//1-已创建
	
			List<TUserLogVO> log_list = null;
			
			try {
				//查询需要发邮件，且未发送（已创建）的日志
				log_list = userLogService.searchUserLogByCondition(vo, po);
			} catch (SCSException e) {
				throw new ServiceException(e.getMessage());
			}
			
			//循环调用邮件发送
			if(log_list != null && log_list.size() > 0){
				TUserLogVO log = null;
				for(int i=0;i<log_list.size();i++){
					log = log_list.get(i);
					
					try {
						this.SpringSendLogMail(log);
					} catch (Exception e1) {
						//send mail have error Exception
						logger.error(e1.getMessage(), e1);
	//					throw new ServiceException(e1.getMessage());
						//send next mail
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
						//send next mail
						continue;
					}
					
				}
			}
			inRunning = false;
		}
	}

	/**
	 * init java sprint mail properties
	 */
	private void sendMailInit() {
		if(mailSender == null){
			mailSender = new JavaMailSenderImpl();
			mailSender.setHost(ConfigManager.getInstance().getString("mail.host"));
			mailSender.setPort(ConfigManager.getInstance().getInt("mail.port", 25));
			mailSender.setUsername(ConfigManager.getInstance().getString("mail.username"));
			mailSender.setPassword(ConfigManager.getInstance().getString("mail.password"));
			mailSender.setDefaultEncoding("utf-8");
			logger.info("HOST>>>>>>>>>>>>>>>Host=" + mailSender.getHost() + ",Port="+ mailSender.getPort());
			Properties props = new Properties();
			props.setProperty("mail.smtp.auth", "true");
			props.setProperty("mail.smtp.timeout", "0");
			mailSender.setJavaMailProperties(props);
			this.setMailSender(mailSender);
		}
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	private void SpringSendLogMail(TUserLogVO log) throws Exception {
		//log turn to mail object
		if(log != null){
			SendMailContent content = new SendMailContent();
			
			content.setFromMail(log.getFroms());
			content.setToMail(log.getTos());
			content.setSubject(log.getSubject());
			content.setSendText(log.getComment());
			//send mail
			SpringSendTextMail(content);
		}
	}

	private void SpringSendTextMail(SendMailContent content) throws Exception {
		//init java sprint mail properties
		this.sendMailInit();
		
		//mail handle
		final String toMail = content.getToMail();
		final String fromMail = content.getFromMail();
		final String subject = content.getSubject();
		final String text = content.getSendText();
		
		try {
			JavaMailSenderImpl senderImpl = new JavaMailSenderImpl();
			final MimeMessage mailMessage = senderImpl.createMimeMessage();

			MimeMessageHelper messageHelper = new MimeMessageHelper(mailMessage, true, "GBK");
			messageHelper.setTo(toMail);
			messageHelper.setFrom(fromMail);
			messageHelper.setSubject(subject);
			
			StringBuffer sbText = new StringBuffer();
			sbText.append("<html><head>");
			sbText.append(setCss());
			sbText.append("</head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"GBK\" /> <body>");
			sbText.append(text);
			sbText.append("</body></html>");
			messageHelper.setText(sbText.toString(), true);
			//spring mail send
			mailSender.send(mailMessage);
		}
		catch (Exception ex) {
			ex.printStackTrace();
			throw ex;
		}
	}

	private String setCss() {
		StringBuffer sbCss = new StringBuffer();
		sbCss.append("<style type=\"text/css\"> ");
		sbCss.append("body {");
		sbCss.append("  font: normal 11px auto \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sbCss.append("  color: #4f6b72; ");
		sbCss.append("  background: #E6EAE9; ");
		sbCss.append("}");
		sbCss.append(" a {color: #c75f3e;} ");
		sbCss.append("#mytable { width: 500px; padding: 0; margin: 0; } ");
		sbCss.append("caption { padding: 0 0 5px 0; width: 500px; ");
		sbCss.append(" font: italic 11px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sbCss.append(" text-align: right; }");
		sbCss.append("th { font: bold 11px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sbCss.append(" color: #4f6b72; border-right: 1px solid #C1DAD7; ");
		sbCss.append(" border-bottom: 1px solid #C1DAD7; ");
		sbCss.append(" border-top: 1px solid #C1DAD7; ");
		sbCss.append(" letter-spacing: 2px; text-transform: uppercase; ");
		sbCss.append(" text-align: left; padding: 6px 6px 6px 12px; background: #CAE8EA  no-repeat; }");
		sbCss.append(" th.nobg { border-top: 0; border-left: 0; border-right: 1px solid #C1DAD7; background: none; }");
		sbCss.append("td { border-right: 1px solid #C1DAD7; border-bottom: 1px solid #C1DAD7; background: #fff; ");
		sbCss.append(" font-size:11px; padding: 6px 6px 6px 12px; color: #4f6b72; }");
		sbCss.append("td.alt { background: #F5FAFA; color: #797268; }");
		sbCss.append("th.spec { border-left: 1px solid #C1DAD7; border-top: 0; background: #fff no-repeat; ");
		sbCss.append(" font: bold 10px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; }");
		sbCss.append("th.specalt { border-left: 1px solid #C1DAD7; border-top: 0; background: #f5fafa no-repeat; ");
		sbCss.append(" font: bold 10px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; color: #797268; }");
		/*---------for IE 5.x bug*/
		sbCss.append("html>body td{ font-size:11px;} ");
		sbCss.append("body,td,th { font-family: 宋体, Arial; font-size: 12px; }");
		sbCss.append("</style> ");
		return sbCss.toString();
	}
}
