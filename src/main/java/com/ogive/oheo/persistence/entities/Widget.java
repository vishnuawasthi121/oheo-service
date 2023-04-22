package com.ogive.oheo.persistence.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "WIDGET")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_WIDGET", sequenceName = "SEQ_WIDGET")
@Entity
public class Widget {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_WIDGET")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated
	private StatusCode status;

	@Column(length = 65535)
	private String content;

	@Column(nullable = false)
	private String shortCode;

	@ManyToOne
	@JoinColumn(name = "position_id", nullable = false)
	private Position position;

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

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Position getPosition() {
		return position;
	}

	public void setPosition(Position position) {
		this.position = position;
	}

	public String getShortCode() {
		return shortCode;
	}

	public void setShortCode(String shortCode) {
		this.shortCode = shortCode;
	}

}
