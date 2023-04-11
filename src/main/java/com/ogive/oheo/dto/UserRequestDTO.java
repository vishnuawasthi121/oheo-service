package com.ogive.oheo.dto;

import javax.validation.constraints.NotNull;

import com.ogive.oheo.constants.StatusCode;

public class UserRequestDTO {

	private Long id;

	@NotNull(message = "name is mandatory")
	private String name;

	@NotNull(message = "groupId is mandatory")
	private Long groupId;

	@NotNull(message = "email is mandatory")
	private String email;

	private String phone;

	@NotNull(message = "zoneId is mandatory")
	private Long zoneId;

	@NotNull(message = "stateId is mandatory")
	private Long stateId;

	@NotNull(message = "cityId is mandatory")
	private Long cityId;

	@NotNull(message = "zipcode is mandatory")
	private Long zipcode;

	private StatusCode status;

	private String username;

	private String password;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getGroupId() {
		return groupId;
	}

	public void setGroupId(Long groupId) {
		this.groupId = groupId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Long getZoneId() {
		return zoneId;
	}

	public void setZoneId(Long zoneId) {
		this.zoneId = zoneId;
	}

	public Long getStateId() {
		return stateId;
	}

	public void setStateId(Long stateId) {
		this.stateId = stateId;
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

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
