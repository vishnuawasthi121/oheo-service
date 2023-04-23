package com.ogive.oheo.services;

import java.io.File;
import java.util.Date;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.NewsAddress;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.ogive.oheo.dto.EmailDetails;

@Service
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender javaMailSender;

	@Value("${spring.mail.username}")
	private String sender;

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
		javaMailSender.send(mailMessage);
		return "Mail Sent Successfully...";
	}

	public Properties getProps() {
		Properties props = System.getProperties();
		props.put("mail.smtp.user", "vishnu.p.awasthi@gmail.com");
		props.put("mail.smtp.password", "Secure*123");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", "587");
		props.put("mail.smtp.ssl.enable", "true");
		return props;
	}

	public void sendEmail() {

		javax.mail.Authenticator auth = null;
		auth = new javax.mail.Authenticator() {
			@Override
			public javax.mail.PasswordAuthentication getPasswordAuthentication() {
				return new javax.mail.PasswordAuthentication("vishnu.awasthi.dev9@gmail.com", "Secure*123");
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
			msg.setText("vishnu..awasthi.dev9@gmail.com");
			
			msg.setSubject("Greetings");
			msg.setSentDate(new Date());
			msg.setRecipient(RecipientType.TO, new NewsAddress("vishnuawasthi121@gmail.com"));
			
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

	@Override
	public String sendMailWithAttachment(EmailDetails details) {
		// Creating a mime message
		MimeMessage mimeMessage = javaMailSender.createMimeMessage();
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
			FileSystemResource file = new FileSystemResource(new File(details.getAttachment()));
			mimeMessageHelper.addAttachment(file.getFilename(), file);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Sending the mail
		javaMailSender.send(mimeMessage);
		return "Mail sent Successfully";
	}

}
