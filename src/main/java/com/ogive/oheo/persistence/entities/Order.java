package com.ogive.oheo.persistence.entities;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

@Table(name = "ORDERS")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ORDERS", sequenceName = "SEQ_ORDERS")
@Entity
public class Order {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDERS")
	private Long id;

	private Date orderDate;

	private Date deliveryDate;

}
