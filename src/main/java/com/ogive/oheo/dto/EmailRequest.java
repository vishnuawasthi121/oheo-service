package com.ogive.oheo.dto;

import java.util.Set;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailRequest {

	private String subject;

	private String body;

	private Set<String> to;
}
