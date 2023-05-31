package com.ogive.oheo.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.EmailDetails;
import com.ogive.oheo.persistence.entities.OTP;
import com.ogive.oheo.persistence.repo.OTPRepository;
import com.ogive.oheo.services.EmailServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Auxiliary services like Email OTP, etc.")
@RestController

public class AuxiliaryServicesController {

	private static final Logger LOG = LoggerFactory.getLogger(AuxiliaryServicesController.class);

	@Autowired
	private OTPRepository otpRepository;

	@Autowired
	private EmailServiceImpl serviceImpl;

	@Value("${app.email.enabled}")
	private boolean isEmailEnabled;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@ApiOperation(value = "Email OTP service", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/opt/{email}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> generateOTP(@PathVariable String email) {
		// otpRepository.deleteById(email);
		OTP otp = new OTP();
		otp.setEmail(email);
		Random random = new java.util.Random();
		String id = String.format("%04d", random.nextInt(10000));
		otp.setOtpCode(id);
		otp.setCreatedDate(new Date());
		// otp-validation.html
		Map<String, Object> model = new HashMap<>();
		/**
		 * String recipient= (String) model.get("recipient"); String subject= (String)
		 * model.get("subject"); String templateKey= (String) model.get("templateKey");
		 **/
		if (isEmailEnabled) {
			model.put("recipient", email);
			model.put("subject", "OHEO - Email Validation");
			// model.put("attachment", null);
			model.put("templateKey", "otp-validation");
			serviceImpl.sendEmail(model);
		}
		return new ResponseEntity<Object>(otp, HttpStatus.OK);
	}

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/email-sender", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> emailTest(@ModelAttribute EmailDetails details) {
		LOG.info("emailTest request received@@");
		// emailServiceImpl.sendEmail();
		serviceImpl.sendMailWithAttachment(details);
		// emailServiceImpl.sendEmailWithTemplate(details);
		return new ResponseEntity<Object>("Email sent successfully", HttpStatus.OK);
	}

}
