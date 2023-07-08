package com.ogive.oheo.persistence.entities;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.ServiceProviderType;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "LOGISTIC_REQUEST")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_LOGISTIC_REQUEST", sequenceName = "SEQ_LOGISTIC_REQUEST")
@Entity
public class LogisticRequest {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_LOGISTIC_REQUEST")
	private Long id;

	private String name;

	private String phoneNumber;

	private Long zipcode;

	private ServiceProviderType serviceType;

	@ManyToOne
	@JoinColumn(name = "user_id", nullable = true)
	private UserDetail userDetail;

	private Date date;

	private String time;

}
