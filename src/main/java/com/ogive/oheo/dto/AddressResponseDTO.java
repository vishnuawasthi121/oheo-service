package com.ogive.oheo.dto;

public class AddressResponseDTO {

	private Long id;

	private StateResponseDTO state;

	private ZoneDetailResponseDTO zone;

	private CityResponseDTO city;

	private ZipcodeResponseDTO zipcode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public StateResponseDTO getState() {
		return state;
	}

	public void setState(StateResponseDTO state) {
		this.state = state;
	}

	public CityResponseDTO getCity() {
		return city;
	}

	public void setCity(CityResponseDTO city) {
		this.city = city;
	}

	public ZipcodeResponseDTO getZipcode() {
		return zipcode;
	}

	public void setZipcode(ZipcodeResponseDTO zipcode) {
		this.zipcode = zipcode;
	}

	public ZoneDetailResponseDTO getZone() {
		return zone;
	}

	public void setZone(ZoneDetailResponseDTO zone) {
		this.zone = zone;
	}

}
