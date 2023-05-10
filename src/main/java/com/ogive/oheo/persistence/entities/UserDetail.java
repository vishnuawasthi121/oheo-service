package com.ogive.oheo.persistence.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/*import com.ogive.oheo.constants.UserRole;*/

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

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

	private String contact;

	@ManyToOne(cascade = { CascadeType.ALL })
	@JoinColumn(name = "distributor_id")
	private UserDetail distributor;

	@OneToMany(mappedBy = "distributor")
	private Set<UserDetail> dealers = new HashSet<UserDetail>();

	/**
	 * @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER) @JoinTable(
	 *                     name = "users_roles", joinColumns = @JoinColumn(name =
	 *                     "user_id"), inverseJoinColumns = @JoinColumn(name =
	 *                     "role_id")) private Set<UserRole> roles = new
	 *                     HashSet<>();
	 **/

	/*
	 * @ManyToOne(cascade = { CascadeType.ALL })
	 * 
	 * @JoinColumn(name = "role_id") private UserRole role;
	 */
	
	private Date created;
	
	private Date updated;
	
}
