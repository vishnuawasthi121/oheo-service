package com.ogive.oheo.persistence.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

/*import com.ogive.oheo.constants.UserRole;*/

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@NamedQuery(name = "UserDetail.findAllSubUsersByRootId", query = "SELECT u FROM UserDetail u WHERE u.root.id = : rootId")

@Table(name = "USER_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_USER_DETAIL", sequenceName = "SEQ_USER_DETAIL")
@Entity
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_USER_DETAIL")
	private Long id;

	private String name;

	@Column(unique = true, nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated
	private StatusCode status;

	@Column
	private boolean isValidated;

	private String contact;

	@ManyToOne
	@JoinColumn(name = "root_id")
	private UserDetail root;

	@OneToMany(mappedBy = "root")
	private Set<UserDetail> childs = new HashSet<UserDetail>();

	private Date created;

	private Date updated;

	@ManyToOne
	@JoinColumn(name = "role_id")
	private UserRole role;

	// Region details
	@ManyToOne
	@JoinColumn(name = "zone_id")
	private ZoneDetail zone;

	@ManyToOne
	@JoinColumn(name = "state_id")
	private State state;

	@ManyToOne
	@JoinColumn(name = "city_id")
	private City city;

	@ManyToOne
	@JoinColumn(name = "zipcode_id")
	private Zipcode zipcode;

}
