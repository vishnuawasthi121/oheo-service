package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "POSITION")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_POSITION", sequenceName = "SEQ_POSITION")
@Entity
public class Position {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_POSITION")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String shortCode;

	@Enumerated
	private StatusCode status;

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

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}
	
	
}
