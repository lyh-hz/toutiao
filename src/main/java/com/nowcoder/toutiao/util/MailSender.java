package com.nowcoder.toutiao.util;

import java.util.Map;
import java.util.Properties;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.apache.velocity.app.VelocityEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailSender implements InitializingBean {
	private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
	private JavaMailSenderImpl mailSender;

	@Autowired
	private VelocityEngine velocityEngine;

	public boolean sendWithHTMLTemplantes(String to, String subject, String template, Map<String, Object> model) {
		try {
			String nick = MimeUtility.encodeText("牛客中级课");
			InternetAddress from = new InternetAddress(nick + "<casesc@163.com>");
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);
			String result = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, template, "UTF-8", model);
			mimeMessageHelper.setTo(to);
			mimeMessageHelper.setFrom(from);
			mimeMessageHelper.setSubject(subject);
			mimeMessageHelper.setText(result, true);
			mailSender.send(mimeMessage);
			return true;
		} catch (Exception e) {
			logger.error("发送邮件失败" + e.getMessage());
			return false;
		}
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		mailSender = new JavaMailSenderImpl();
		// 输入自己的邮箱和密码，用于发送邮件
		mailSender.setUsername("xxx@163.com");
		mailSender.setPassword("xxx");
		// 配置自己的邮箱和密码
		mailSender.setHost("smtp.163.com");
		mailSender.setPort(465);
		mailSender.setProtocol("smtps");
		mailSender.setDefaultEncoding("utf8");
		Properties javaMailProperties = new Properties();
		javaMailProperties.put("mail.smtp.ssl.enable", true);
		mailSender.setJavaMailProperties(javaMailProperties);

	}

}
