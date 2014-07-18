package com.skycloud.management.portal.admin.audit.sevice.impl;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.List;
import java.util.Properties;

import javax.mail.internet.MimeMessage;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.skycloud.management.portal.admin.audit.dao.ISendInfoDao;
import com.skycloud.management.portal.admin.audit.entity.TSMSInfoBO;
import com.skycloud.management.portal.admin.audit.entity.TSendInfoBO;
import com.skycloud.management.portal.admin.audit.sevice.IMailSender;
import com.skycloud.management.portal.admin.audit.sevice.SendMailContent;
import com.skycloud.management.portal.admin.sysmanage.dao.IUserManageDao;
import com.skycloud.management.portal.admin.sysmanage.entity.TUserBO;
import com.skycloud.management.portal.common.utils.ConfigManager;
import com.skycloud.management.portal.front.order.dao.IOrderDao;
import com.skycloud.management.portal.front.order.entity.TInstanceInfoBO;
import com.skycloud.management.portal.front.order.entity.TOrderBO;

public class SendMailImpl implements IMailSender {

	private static Log log = LogFactory.getLog(SendMailImpl.class);

	private JavaMailSenderImpl mailSender;

	private ISendInfoDao sendInfoDao;

	private IOrderDao orderDao;

	private IUserManageDao userDao;

	private boolean IsSendSMS = true;

	public ISendInfoDao getSendInfoDao() {
		return sendInfoDao;
	}

	public void setSendInfoDao(ISendInfoDao sendInfoDao) {
		this.sendInfoDao = sendInfoDao;
	}

	public IOrderDao getOrderDao() {
		return orderDao;
	}

	public void setOrderDao(IOrderDao orderDao) {
		this.orderDao = orderDao;
	}

	public JavaMailSenderImpl getMailSender() {
		return mailSender;
	}

	public void setMailSender(JavaMailSenderImpl mailSender) {
		this.mailSender = mailSender;
	}

	@Override
	public void sendMail(SendMailContent content) {
		IsSendSMS = ConfigManager.getInstance().getBoolean("sms.isSend");// 读配置文件中，是否发送邮件
		if (ConfigManager.getInstance().containsKey("sms.isSend") && IsSendSMS) {
			SpringSendSMS(content);
		}
		SpringSendTextMail(content);
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
				sendInfoDao.insertSMSInfo(smsInfo);
			}

		}
		catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private void SpringSendTextMail(SendMailContent content) {
		this.organMailContent();
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
			messageHelper.setText("<html><head>" + setCss()
			        + "</head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=\"GBK\" /> <body>" + text + "</body></html>", true);
			mailSender.send(mailMessage);
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private String setCss() {
		StringBuffer sb = new StringBuffer();
		sb.append("<style type=\"text/css\"> ");
		sb.append("body {");
		sb.append("  font: normal 11px auto \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sb.append("  color: #4f6b72; ");
		sb.append("  background: #E6EAE9; ");
		sb.append("}");
		sb.append(" a {color: #c75f3e;} ");
		sb.append("#mytable { width: 500px; padding: 0; margin: 0; } ");
		sb.append("caption { padding: 0 0 5px 0; width: 500px; ");
		sb.append(" font: italic 11px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sb.append(" text-align: right; }");
		sb.append("th { font: bold 11px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; ");
		sb.append(" color: #4f6b72; border-right: 1px solid #C1DAD7; ");
		sb.append(" border-bottom: 1px solid #C1DAD7; ");
		sb.append(" border-top: 1px solid #C1DAD7; ");
		sb.append(" letter-spacing: 2px; text-transform: uppercase; ");
		sb.append(" text-align: left; padding: 6px 6px 6px 12px; background: #CAE8EA  no-repeat; }");
		sb.append(" th.nobg { border-top: 0; border-left: 0; border-right: 1px solid #C1DAD7; background: none; }");
		sb.append("td { border-right: 1px solid #C1DAD7; border-bottom: 1px solid #C1DAD7; background: #fff; ");
		sb.append(" font-size:11px; padding: 6px 6px 6px 12px; color: #4f6b72; }");
		sb.append("td.alt { background: #F5FAFA; color: #797268; }");
		sb.append("th.spec { border-left: 1px solid #C1DAD7; border-top: 0; background: #fff no-repeat; ");
		sb.append(" font: bold 10px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; }");
		sb.append("th.specalt { border-left: 1px solid #C1DAD7; border-top: 0; background: #f5fafa no-repeat; ");
		sb.append(" font: bold 10px \"Trebuchet MS\", Verdana, Arial, Helvetica, sans-serif; color: #797268; }");
		/*---------for IE 5.x bug*/
		sb.append("html>body td{ font-size:11px;} ");
		sb.append("body,td,th { font-family: 宋体, Arial; font-size: 12px; }");
		sb.append("</style> ");
		return sb.toString();
	}

	@Override
	public void FSendMail() throws MailException, SQLException {
		log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
		log.info("infoDao-----------=" + sendInfoDao);
		List<TSendInfoBO> lists = sendInfoDao.findAllPedding();
		if (lists.size() > 0) {
			SendMailContent content;
			for (TSendInfoBO tSendInfoBO : lists) {
				content = new SendMailContent();
				content.setToMail(tSendInfoBO.getReceiveAddress());
				content.setFromMail(ConfigManager.getInstance().getString("mail.from"));
				if (tSendInfoBO.getState() == 1) {
					content.setSubject("审核不通过");
					// 邮件内容 to fix bug 2495
					content.setSendText(setMailMessage(tSendInfoBO));
					// content.setSendText("您好，您的订单《" +
					// tSendInfoBO.getOrderCode() + "》没有通过审核，不通过理由《" +
					// tSendInfoBO.getApproveReason() + "》。");
					if (ConfigManager.getInstance().containsKey("sms.audit.order.no.text")) {
						content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.audit.order.no.text"),
						                                     tSendInfoBO.getOrderCode(), tSendInfoBO.getApproveReason()));
					}
				} else if (tSendInfoBO.getState() == 2) {
					// bugid = 0001707
					// 查询结果集中添加 o.TYPE 字段 :
					// 订单类型；1：新申请；2：修改申请；3：删除申请
					int type = tSendInfoBO.getType();
					String desc = "";
					if (type == 1) {
						desc = "已经审核通过，请等待开通使用，谢谢。";
						if (ConfigManager.getInstance().containsKey("sms.audit.order.pass.text1")) {
							content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.audit.order.pass.text1"),
							                                     tSendInfoBO.getOrderCode()));
						}
					} else if (type == 3) {
						// to fix bug 2160
						desc = "退订审核已通过，服务已关闭，谢谢。";
						if (ConfigManager.getInstance().containsKey("sms.audit.order.pass.text3")) {
							content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.audit.order.pass.text3"),
							                                     tSendInfoBO.getOrderCode()));
						}
					} else if (type == 2) {
						desc = "修改审核已通过，请等待开通使用，谢谢。";
						if (ConfigManager.getInstance().containsKey("sms.audit.order.pass.text2")) {
							content.setSendSmsText(String.format(ConfigManager.getInstance().getString("sms.audit.order.pass.text2"),
							                                     tSendInfoBO.getOrderCode()));
						}
					}
					content.setSubject("审核通过");
					// 邮件内容 to fix bug 2495
					content.setSendText(setMailMessage(tSendInfoBO));
					// content.setSendText("您好，您的订单《" +
					// tSendInfoBO.getOrderCode() + "》" + desc);
				} else {
					continue;
				}
				TUserBO user = userDao.findUserByOrderId(tSendInfoBO.getOrderId());
				if (user == null) {
					continue;
				}
				content.setToMobile(user.getMobile());
				if (StringUtils.isNotEmpty(content.getSendText())) {
					this.sendMail(content);
				} else {
					return;
				}
			}
			sendInfoDao.updateSendInfo(lists);
		}

	}

	// to fix bug 2495
	private String getTable(TSendInfoBO tSendInfoBO) {
		StringBuilder s1 = new StringBuilder(100);
		try {// bug 0003895 0003898 0003046
			List<TInstanceInfoBO> list = orderDao.findService2InstanceByOrderId(tSendInfoBO.getOrderId(), tSendInfoBO.getType());

			s1.append("<table  id=\"mytable\"  cellspacing=\"0\"><caption> </caption>");
			if (list.size() != 0) {// bug 0003884
				s1.append("<tr>");
				s1.append("<th scope=\"col\">服  务</th>");
				s1.append("<th scope=\"col\">资  源</th>");
				s1.append("<th scope=\"col\">类  型</th>");
				s1.append("</tr>");
			}// fix bug 0002495
			for (TInstanceInfoBO info : list) {
				s1.append("<tr>");
				s1.append("<td  class=\"row\">" + info.getServiceName() + "</td>");
				s1.append("<td  class=\"row\">" + info.getInstanceName() + "</td>");
				s1.append("<td  class=\"row\">" + info.getTemplateName() + "</td>");
				s1.append("</tr>");
			}
			s1.append("</table>");
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		return s1.toString();
	}

	// to fix bug 2495
	private String setMailMessage(TSendInfoBO tSendInfoBO) throws SQLException {
		StringBuilder sb = new StringBuilder(500);
		try {
			TUserBO auditor = orderDao.getAuditorByOrderId(tSendInfoBO.getOrderId());
			TOrderBO order = orderDao.searchOrderById(tSendInfoBO.getOrderId());
			// fix bug 2495
			sb.append("<br />" + order.getCreateUserName() + " 您好，您的订单" + getAuditStr(tSendInfoBO) + "，具体订单信息如下：");
			// fix bug 2495
			sb.append("<br />订单号为：" + tSendInfoBO.getOrderId() + "");
			sb.append("<br />包含以下服务内容：<br />" + getTable(tSendInfoBO));
			sb.append("<br />订单类型：" + getOrderType(tSendInfoBO) + "");
			sb.append("<br />当前审批人为：" + auditor.getName() + "");
			sb.append("<br />操作时间：" + auditor.getCreateDt().toString().replace(".0", "") + "");
			if (tSendInfoBO.getState() == 1) {// 审核拒绝
				sb.append("<br />审批不通过原因：" + tSendInfoBO.getApproveReason() + "");
				sb.append("<br />您可以重新选择其他服务进行申请，如有问题请登录自服务门户查询，谢谢您选择我们的云平台");
			}
			if (tSendInfoBO.getState() == 2) {// 审核通过
				// to fix bug:2853
				String desc = "";
				if (tSendInfoBO.getType() == 1) {// 新申请
					desc = "已经审核通过，敬请耐心等待服务开通使用，如有问题请登录自服务门户查询，谢谢您选择我们的云平台。";
				} else if (tSendInfoBO.getType() == 2) {// 修改
					desc = "修改审核已通过，请等待开通使用，谢谢。";
				} else if (tSendInfoBO.getType() == 3) {// 退订
					desc = "退订审核已通过，服务即将关闭，谢谢。";
				}
				sb.append("<br />" + desc);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	private String getAuditStr(TSendInfoBO tSendInfoBO) {
		int type = tSendInfoBO.getType();
		if (type == 4) {
			return "已经审核通过";
		} else if (type == 5) {
			return "未能成功审核通过";
		}
		return "";
	}

	private String getOrderType(TSendInfoBO tSendInfoBO) {
		int type = tSendInfoBO.getType();
		if (type == 1) {
			return "新申请";
		} else if (type == 2) {
			return "修改申请";
		} else if (type == 3) {
			return "删除申请";
		} else if (type == 4) {
			return "续订申请";
		}
		return "";
	}

	private void organMailContent() {
		mailSender.setHost(ConfigManager.getInstance().getString("mail.host"));
		mailSender.setPort(ConfigManager.getInstance().getInt("mail.port", 25));
		mailSender.setUsername(ConfigManager.getInstance().getString("mail.username"));
		mailSender.setPassword(ConfigManager.getInstance().getString("mail.password"));
		mailSender.setDefaultEncoding("utf-8");
		log.info("HOST>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>mailSender.getHost()=" + mailSender.getHost() + ",mailSender.getPort()="
		        + mailSender.getPort());
		Properties props = new Properties();
		props.setProperty("mail.smtp.auth", "true");
		props.setProperty("mail.smtp.timeout", "0");
		mailSender.setJavaMailProperties(props);
		this.setMailSender(mailSender);
	}

	public void setUserDao(IUserManageDao userDao) {
		this.userDao = userDao;
	}

	public IUserManageDao getUserDao() {
		return userDao;
	}

}
