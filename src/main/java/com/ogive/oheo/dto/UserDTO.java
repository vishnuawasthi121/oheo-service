package com.ogive.oheo.dto;

import java.util.Set;

import com.ogive.oheo.constants.Roles;

public class UserDTO {

	private Long id;

	private String firstName;

	private String lastName;

	private String email;

	private String phone;

	private String zone;

	private String region;

	private String city;

	private String zipcode;

	private String status;

	private String username;

	private Set<Roles> roleName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
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

	public String getZone() {
		return zone;
	}

	public void setZone(String zone) {
		this.zone = zone;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Set<Roles> getRoleName() {
		return roleName;
	}

	public void setRoleName(Set<Roles> roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		return "UserDTO [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", phone=" + phone + ", zone=" + zone + ", region=" + region + ", city=" + city + ", zipcode="
				+ zipcode + ", status=" + status + ", username=" + username + ", roleName=" + roleName + "]";
	}

}
