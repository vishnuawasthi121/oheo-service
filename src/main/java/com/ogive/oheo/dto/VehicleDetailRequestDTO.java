package com.ogive.oheo.dto;

import java.util.Set;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.persistence.entities.AddressRequestDTO;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class VehicleDetailRequestDTO {

	private String vehicleName;

	private String keyFeatures;

	private Double price;

	private StatusCode status;

	private Long vehicleTypeId;

	private Long vehicleBodyTypeId;

	private Long companyId;

	private AddressRequestDTO address;

	private Set<MultipartFile> files;

}
