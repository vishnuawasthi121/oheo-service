package com.ogive.oheo.dto;

import javax.validation.constraints.NotEmpty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor
public class TermAndConditionsRequestDTO {

	@NotEmpty
	private String contents;

}
