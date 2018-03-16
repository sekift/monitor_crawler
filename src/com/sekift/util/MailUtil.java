package com.sekift.util;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.mail.SimpleEmail;

/**
 * 
 * @author:sekift
 * @time:2015-2-6 上午11:30:11
 * @version:
 */
public class MailUtil {
	 private static final String hostName = "smtp.qq.com";
	 private static final String userName ="3300587869@qq.com";
	 private static final String password ="se...23";
	 private static final int port = 465;

//	private static final String hostName = "smtp.mxhichina.com";
//	private static final String userName = "master@bubbt.com";
//	private static final String password = "sekift123456-=";
	// private static final int port =25;

	private static final String from = "3300587869@qq.com";
//	private static final String from = "master@bubbt.com";
	private static final String to = "sekift@163.com";
	private static final String charset = "utf-8";

	// simple
	public static void sendSimple(String subject, String msg) {
		try {
			SimpleEmail email = new SimpleEmail();
			email.setHostName(hostName);
			email.setFrom(from);
			email.addTo(to);
			email.setAuthentication(userName, password);
			email.setCharset(charset);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	// 带邮箱的html
	@SuppressWarnings("deprecation")
	public static void sendHTML(String tomail, String subject, String msg) {
		try {
			/**
			 * 需要改成加密端口：465
			 */
			HtmlEmail email = new HtmlEmail();
			email.setHostName(hostName);
			email.setFrom(from);
//			email.setSmtpPort(port);
			email.addTo(tomail);
			email.setSSL(true);
			email.setSslSmtpPort(port+"");
			email.setAuthentication(userName, password);
			email.setCharset(charset);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	// html
	public static void sendHTML(String subject, String msg) {
		try {
			HtmlEmail email = new HtmlEmail();
			email.setHostName(hostName);
			// email.setSmtpPort(port);
			email.setFrom(from);
			email.addTo(to);
			email.setAuthentication(userName, password);
			email.setCharset(charset);
			email.setSubject(subject);
			email.setMsg(msg);
			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	// attach
	public static void sendAttach(String subject, String msg) {
		try {
			MultiPartEmail email = new MultiPartEmail();
			email.setHostName(hostName);
			email.setFrom(from);
			email.addTo(to);
			email.setAuthentication(userName, password);
			email.setCharset(charset);
			email.setSubject(subject);
			email.setMsg(msg);

			EmailAttachment attach = new EmailAttachment();
			attach.setPath("");
			attach.setDisposition(EmailAttachment.ATTACHMENT);
			attach.setName("a");
			email.attach(attach);

			email.send();
		} catch (EmailException e) {
			e.printStackTrace();
		}
	}

	public static void sendMail(String toEmail, String title, String centent)
			throws Exception {
		Properties prop = new Properties();
		prop.put("mail.host", hostName);
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", "true");
		Session session = Session.getInstance(prop);
		session.setDebug(true);
		Transport ts = session.getTransport();
		ts.connect(userName, password);
		Message message = new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		message.setRecipient(Message.RecipientType.TO, new InternetAddress(
				toEmail));
		message.setSubject(title);
		message.setContent(centent, "text/html;charset=utf-8");
		ts.sendMessage(message, message.getAllRecipients());
	}

	public static void main(String[] args) {
		String content[] = { "a", "a", "b", "c" };
		StringBuilder msg = new StringBuilder();
		msg.append("<tbody>");
		for (String list : content) {
			msg.append("<p>" + list + "</p>");
		}
		msg.append("</tbody>");
		// mail.sendHTML("测试", msg.toString());
		try {
			MailUtil.sendHTML(to, "测试", "具体内容。");
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("over");
	}

}