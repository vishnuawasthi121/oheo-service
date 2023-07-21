package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.ImageType;

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

	private Date createdDate;
	
	
	private String name;

	private String contentType;

	private Long size;

	@Lob
	private byte[] data;

	@Enumerated(EnumType.STRING)
	private ImageType imageType;
}
