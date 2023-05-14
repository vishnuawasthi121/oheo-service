package com.ogive.oheo.services;

import com.ogive.oheo.dto.EmailDetails;

public interface EmailService {

	// Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);
 
    // Method
    // To send an email with attachment
    String sendMailWithAttachment(EmailDetails details);	
    
    public void sendEmail();
    
    public void sendEmailWithTemplate(EmailDetails details);
    
}
