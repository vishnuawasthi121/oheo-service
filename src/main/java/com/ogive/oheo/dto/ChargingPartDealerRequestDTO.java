package com.ogive.oheo.dto;

import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class ChargingPartDealerRequestDTO {

	private String name;

	private String email;

	private String address;

	private String phone;

	private String contactPerson;

	private String gstNumber;

	private StatusCode status;

	
}
