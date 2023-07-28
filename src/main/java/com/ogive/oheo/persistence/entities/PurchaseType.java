package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@NamedQuery(name="PurchaseType.dropDown", query="SELECT id , name FROM PurchaseType  WHERE status = 'ACTIVE'")

@Table(name = "PURCHASE_TYPE")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_PURCHASE_TYPE", sequenceName = "SEQ_PURCHASE_TYPE")
@Entity
public class PurchaseType {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_PURCHASE_TYPE")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated(EnumType.STRING)
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

	public StatusCode getStatus() {
		return status;
	}

	public void setStatus(StatusCode status) {
		this.status = status;
	}

}
