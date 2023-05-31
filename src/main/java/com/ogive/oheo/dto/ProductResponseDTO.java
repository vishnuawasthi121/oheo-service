package com.ogive.oheo.dto;

import java.util.Set;

import org.springframework.hateoas.RepresentationModel;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.persistence.entities.Features;
import com.ogive.oheo.persistence.repo.FeaturesRepository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class ProductResponseDTO extends RepresentationModel<ProductResponseDTO> {

	private Long id;

	private Double price;

	private String name;

	private String noOfSeats;

	private String engine;

	//private String isLive;

	private StatusCode status;

	private Set<ImagesResponseDTO> images;

	private ProductSpecificationResponseDTO productSpecification;

	private Set<String> features;

	private VehicleDetailResponseDTO vehicleDetail;

	private VehicleTypeResponseDTO vehicleType;

	private VehicleModelResponseDTO vehicleModel;

	private VehicleFuelTypeResponseDTO vehicleFuelType;

	private VehicleTransmissionResponseDTO vehicleTransmission;

	private String availableForLease;
}
