package com.ogive.oheo.persistence.entities;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ogive.oheo.constants.ImageType;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

@NamedQuery(name = "Images.deleteB1yChargingProductId", query = "delete from Images where chargingProduct.id  =:productId")
@NamedQuery(name = "Images.deleteByProductId", query = "delete from Images where product.id  =:productId")
@NamedQuery(name = "Images.deleteByProductIdAndImageTypeIn", query = "delete from Images where product.id  =:productId  AND imageType IN (:imageTypes)")
@NamedQuery(name = "Images.deleteByLeaseDetailAndImageTypeIn", query = "delete from Images where leaseDetail.id  =:leaseId  AND imageType IN (:imageTypes)")

@Table(name = "IMAGES")
@SequenceGenerator(allocationSize = 1, initialValue = 100, name = "SEQ_IMAGES", sequenceName = "SEQ_IMAGES")
@Entity
public class Images {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_IMAGES")
	private Long id;

	private String name;

	private String contentType;

	private Long size;

	@Lob
	private byte[] data;

	@Enumerated(EnumType.STRING)
	private ImageType imageType;

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "VEHICLE_DETAIL_ID", nullable = true)
	private VehicleDetail vehicleDetail;

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	@JsonIgnore
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "PRODUCT_ID", nullable = true)
	private Product product;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CHARGING_PRODUCT_ID", nullable = true)
	private ChargingProduct chargingProduct;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "LEASE_ID", nullable = true)
	private LeaseDetail leaseDetail;

}
