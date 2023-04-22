package com.ogive.oheo.persistence.entities;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.ogive.oheo.constants.StatusCode;

@Table(name = "FRAME")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_FRAME", sequenceName = "SEQ_FRAME")
@Entity
public class Frame {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_FRAME")
	private Long id;

	@Column(nullable = false)
	private String name;

	@Enumerated
	private StatusCode status;

	@Column(length = 65535)
	private String metaContent;

	@ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "Frame_Position", joinColumns = {@JoinColumn(referencedColumnName = "id") }, inverseJoinColumns = {@JoinColumn(referencedColumnName = "ID") })
	private Set<Position> positions;

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

	public String getMetaContent() {
		return metaContent;
	}

	public void setMetaContent(String metaContent) {
		this.metaContent = metaContent;
	}

	public Set<Position> getPositions() {
		return positions;
	}

	public void setPositions(Set<Position> positions) {
		this.positions = positions;
	}

}
