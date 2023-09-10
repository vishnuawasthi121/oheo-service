package com.ogive.oheo.persistence.entities;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@Table(name = "ORDER_DETAIL")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_ORDER_DETAIL", sequenceName = "SEQ_ORDER_DETAIL")
@Entity

public class OrderDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_DETAIL")
	private Long id;

	private BigDecimal price;

	private String order_id;
}
