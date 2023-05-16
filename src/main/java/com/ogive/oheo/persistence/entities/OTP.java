package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@Table(name = "OTP")
@Entity
public class OTP {

	@Id
	private String email;

	@Column(nullable = false)
	private String otpCode;

	private Date createdDate;

}
