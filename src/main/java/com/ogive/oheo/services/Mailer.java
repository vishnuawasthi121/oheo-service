package com.ogive.oheo.services;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mailer {

	static final String appPassword  = "ikpiehmcbsobpcen";
	public Properties emailConfigGmail() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		//props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.ssl.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		return props;
	}
	
	public Properties emailConfigZoho() {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.zoho.in");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		//mail.smtp.starttls.enable=false
		props.put("mail.smtp.starttls.enable", "false");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
		return props;
	}

	
	public static void main(String... strings) {
		Mailer mailer = new Mailer();
		// get Session
		Session session = Session.getDefaultInstance(mailer.emailConfigGmail(), new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				//return new PasswordAuthentication("marketing@jijivishaonline.com", "Marketing@1234");
				return new PasswordAuthentication("vishnu.awasthi.dev9@gmail.com", appPassword);
			}
		});

		// compose message
		try {
			MimeMessage message = new MimeMessage(session);
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("vishnuawasthi121@gmail.com"));
			message.setSubject("Test");
			message.setText("Hello world");
			// send message
			Transport.send(message);
			System.out.println("message sent successfully");
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}

	}

}
