package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@Setter
@Getter
@NoArgsConstructor

@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_PRIVACY_POLICY", sequenceName = "SEQ_PRIVACY_POLICY")
@Entity
@Table(name = "PRIVACY_POLICY")
public class PrivacyPolicy {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PRIVACY_POLICY")
	private Long id;

	@Lob
	@Column(nullable = false)
	private String contents;

	private Date createdDate;
}
