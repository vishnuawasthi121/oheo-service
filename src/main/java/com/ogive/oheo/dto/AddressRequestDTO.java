package com.ogive.oheo.dto;

public class AddressRequestDTO {

	private Long stateId;

	private Long zoneId;

	private Long cityId;

	private Long zipcode;

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getCityId() {
		return cityId;
	}

	public void setCityId(Long cityId) {
		this.cityId = cityId;
	}

	public Long getZipcode() {
		return zipcode;
	}

	public void setZipcode(Long zipcode) {
		this.zipcode = zipcode;
	}

	@Override
	public String toString() {
		return "AddressRequestDTO [stateId=" + stateId + ", zoneId=" + zoneId + ", cityId=" + cityId + ", zipcode="
				+ zipcode + "]";
	}
	
	

}
