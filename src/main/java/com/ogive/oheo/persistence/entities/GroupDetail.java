package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "GROUP_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_GROUP_DETAIL", sequenceName = "SEQ_Role")
@Entity
public class GroupDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_GROUP_DETAIL")
	@Column(name = "GROUP_ID")
	private Long id;

	@Column(nullable = false, unique = true)
	private String groupName;

	private StatusCode status;

	private String description;

	// bi-directional many-to-many association to User
	/*
	 * @JsonIgnore
	 * 
	 * @ManyToMany(mappedBy = "groupDetails") private Set<UserInfo> users;
	 */

	@OneToMany(mappedBy="groupDetail")
	private Set<UserInfo> users;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

	public Set<UserInfo> getUsers() {
		return users;
	}

	public void setUsers(Set<UserInfo> users) {
		this.users = users;
	}

}
