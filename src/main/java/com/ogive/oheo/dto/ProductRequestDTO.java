package com.ogive.oheo.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class ProductRequestDTO {

	// private List<UploadImageRequestDTO> images;
	// private List<MultipartFile> images;
	private List<MultipartFile> images;

	private ImageType imageType;

	private String isLive = "N";

	private Double price;

	@NotEmpty
	private String name;

	private String noOfSeats;

	private String engine;

	private StatusCode status;

	@Valid
	private SpecificationDTO specification;

	@NotNull
	private Long vehicleDetailId;

	@NotNull
	private Long vehicleTypeId;

	@NotNull
	private Long vehicleModelId;

	@NotNull
	private Long vehicleFuelTypeId;

	@NotNull
	private Long vehicleTransmissionId;

	private List<String> features;

	private MultipartFile brochure;

}
