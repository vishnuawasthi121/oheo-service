
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

@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_TERM_CONDITIONS", sequenceName = "SEQ_TERM_CONDITIONS")
@Entity
@Table(name = "TERM_CONDITIONS")
public class TermAndConditions {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_TERM_CONDITIONS")
	private Long id;

	@Lob
	@Column(length = 65535, nullable = false)
	private String contents;
	
	private Date createdDate;

}
