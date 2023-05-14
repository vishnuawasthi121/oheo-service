package com.ogive.oheo.services;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.ogive.oheo.dto.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	@Autowired
	private SpringTemplateEngine templateEngine;

	@Value("${app.email.sender.username}")
	private String sender;

	// This is app password not an email id password
	@Value("${app.mail.password}")
	private String appPassword;

	@Value("${mail.smtp.socketFactory.port}")
	private String socketFactoryPort;

	@Value("${mail.smtp.port}")
	private String port;

	@Value("${mail.smtp.host}")
	private String host;

	@Value("${mail.smtp.ssl.enable}")
	private boolean sslEnable;

	@Value("${mail.smtp.auth}")
	private boolean auth;

	@Override
	public String sendSimpleMail(EmailDetails details) {
		// Creating a simple mail message
		SimpleMailMessage mailMessage = new SimpleMailMessage();
		// Setting up necessary details
		mailMessage.setFrom(sender);
		mailMessage.setTo(details.getRecipient());
		mailMessage.setText(details.getMsgBody());
		mailMessage.setSubject(details.getSubject());
		// Sending the mail
		emailSender.send(mailMessage);
		return "Mail Sent Successfully...";
	}

	public Properties getProps() {
		Properties props = new Properties();
		props.put("mail.smtp.user", sender);
		props.put("mail.smtp.password", appPassword);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.host", port);
		props.put("mail.smtp.ssl.enable", sslEnable);
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", socketFactoryPort);
		return props;
	}

	@Override
	public String sendMailWithAttachment(EmailDetails details) {
		// Creating a mime message
		MimeMessage mimeMessage = emailSender.createMimeMessage();
		MimeMessageHelper mimeMessageHelper;
		// Setting multipart as true for attachments to
		// be send
		try {
			mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
			mimeMessageHelper.setFrom(sender);
			mimeMessageHelper.setTo(details.getRecipient());
			mimeMessageHelper.setText(details.getMsgBody());
			mimeMessageHelper.setSubject(details.getSubject());
			// Adding the attachment
			if (null != details.getAttachment()) {
				MultipartFile multipartFile = new MockMultipartFile(details.getAttachment().getOriginalFilename(),
						details.getAttachment().getBytes());
				File fileToSend = new File(details.getAttachment().getOriginalFilename());
				multipartFile.transferTo(fileToSend);
				FileSystemResource file = new FileSystemResource(fileToSend);
				mimeMessageHelper.addAttachment(file.getFilename(), file);
			}

		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Sending the mail
		emailSender.send(mimeMessage);
		return "Mail sent Successfully";
	}

	@Override
	public void sendEmailWithTemplate(EmailDetails details) {
		try {
			MimeMessage message = emailSender.createMimeMessage();
			MimeMessageHelper helper;
			helper = new MimeMessageHelper(message, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
					StandardCharsets.UTF_8.name());

			if (null != details.getAttachment()) {
				MultipartFile multipartFile = new MockMultipartFile(details.getAttachment().getOriginalFilename(),
						details.getAttachment().getBytes());
				File fileToSend = new File(details.getAttachment().getOriginalFilename());
				multipartFile.transferTo(fileToSend);
				FileSystemResource file = new FileSystemResource(fileToSend);
				helper.addAttachment(file.getFilename(), file);
			}

			Context context = new Context();
			// Add required field you need in template
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("name", details.getName());
			model.put("username", details.getUsername());
			model.put("password", details.getUserPasswordToSend());

			context.setVariables(model);
			String html = templateEngine.process("user-registration", context);
			helper.setTo(details.getRecipient());
			helper.setText(html, true);
			helper.setSubject(details.getSubject());
			helper.setFrom(sender);
			emailSender.send(message);
		} catch (MessagingException | IOException e) {
			e.printStackTrace();
		}
	}

	public Properties emailConfigGmail() {
		Properties props = new Properties();
		props.put("mail.smtp.host", host);
		props.put("mail.smtp.socketFactory.port", port);
		// props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.ssl.enable", sslEnable);
		props.put("mail.smtp.auth", auth);
		props.put("mail.smtp.port", port);
		return props;
	}

	public void sendEmail() {

		javax.mail.Authenticator auth = null;
		auth = new javax.mail.Authenticator() {
			@Override
			public javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication(sender, appPassword);
			}
		};
		Session session = Session.getInstance(getProps(), auth);
		// get transport object from session and connect to mail server
		Transport tr;
		try {
			tr = session.getTransport("smtp");
			tr.connect(session.getProperty("mail.smtp.host"), session.getProperty("mail.smtp.user"),
					session.getProperty("mail.smtp.password"));

			// create message and set from,recipients,content and other stuff here on the
			// message object.
			// ..................
			Message msg = new MimeMessage(session);
			// ...................

			// Save and send the message
			msg.setText("Hello Test");
			msg.setSubject("Greetings");
			msg.setSentDate(new Date());
			// msg.setRecipient(RecipientType.TO, new
			// NewsAddress("vishnuawasthi121@gmail.com"));

			msg.saveChanges();
			tr.sendMessage(msg, msg.getAllRecipients());
			tr.close();

		} catch (NoSuchProviderException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
