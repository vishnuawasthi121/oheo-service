package com.ogive.oheo.dto;

import java.sql.Date;

import javax.validation.constraints.NotEmpty;

import com.ogive.oheo.constants.ServiceProviderType;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LogisticRequestDTO {

	@NotEmpty
	private String name;

	@NotEmpty
	private String phoneNumber;

	@NotNull
	private Long zipcode;

	private ServiceProviderType serviceType;

	@NotNull
	private Long dealerId;
	

	@NotNull
	private Date date;

	private String time;
}
