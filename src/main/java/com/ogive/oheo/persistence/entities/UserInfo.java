package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "USER_INFO")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_USER", sequenceName = "SEQ_USER")
@Entity
public class UserInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER")
	@Column(name = "ID")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String phone;

	private StatusCode status;

	@Column
	private String username;

	@Column(nullable = false)
	private String password;

	// bi-directional many-to-many association to Role
	// @ManyToMany
	// @JoinTable(name = "ROLES", joinColumns = { @JoinColumn(name = "ID") },
	// inverseJoinColumns = {
	// @JoinColumn(name = "ROLE_ID") })

	/**
	 * @ManyToMany(cascade = { CascadeType.ALL })
	 * @JoinTable(name = "USER_GROUP_MAPPING", joinColumns = { @JoinColumn(name =
	 *                 "USER_ID") }, inverseJoinColumns = {
	 * @JoinColumn(name = "GROUP_ID") }) private Set<GroupDetail> groupDetails;
	 **/

	@ManyToOne
	@JoinColumn(name = "GROUP_ID", nullable = false)
	private GroupDetail groupDetail;

	@ManyToOne
	@JoinColumn(name = "ZONE_ID", nullable = false)
	private ZoneDetail zoneDetail;

	@ManyToOne
	@JoinColumn(name = "STATE_ID", nullable = false)
	private State state;

	@ManyToOne
	@JoinColumn(name = "CITY_ID", nullable = false)
	private City city;

	@ManyToOne
	@JoinColumn(name = "ZIPCODE", nullable = false)
	private Zipcode zipcode;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public void setId(Long id) {
		this.id = id;
	}

	public GroupDetail getGroupDetail() {
		return groupDetail;
	}

	public void setGroupDetail(GroupDetail groupDetail) {
		this.groupDetail = groupDetail;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public ZoneDetail getZoneDetail() {
		return zoneDetail;
	}

	public void setZoneDetail(ZoneDetail zoneDetail) {
		this.zoneDetail = zoneDetail;
	}

	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public Zipcode getZipcode() {
		return zipcode;
	}

	public void setZipcode(Zipcode zipcode) {
		this.zipcode = zipcode;
	}

}
