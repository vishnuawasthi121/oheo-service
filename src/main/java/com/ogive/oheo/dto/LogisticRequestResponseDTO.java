package com.ogive.oheo.dto;

import java.sql.Date;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.ServiceProviderType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class LogisticRequestResponseDTO extends RepresentationModel<LogisticRequestResponseDTO> {

	private Long id;

	private String name;

	private String phoneNumber;

	private Long zipcode;

	private ServiceProviderType serviceType;

	private Long dealerId;

	private String dealerName;

	private Date date;

	private String time;

}
