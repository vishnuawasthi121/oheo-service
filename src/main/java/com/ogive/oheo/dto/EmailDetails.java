package com.ogive.oheo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class EmailDetails {
	// Class data members
	private String name;
	private String username;
	private String recipient;
	private String msgBody;
	private String subject;
	private MultipartFile attachment;
}
