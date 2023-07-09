package com.ogive.oheo.dto;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;

import groovyjarjarantlr4.v4.runtime.misc.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter

public class UpdateProductRequestDTO {

	private List<MultipartFile> images;

	private ImageType imageType;

	@NotEmpty
	private String isLive;

	@NotNull
	private Double price;

	@NotEmpty
	private String name;

	@NotEmpty
	private String noOfSeats;

	@NotEmpty
	private String engine;

	private StatusCode status;

	@Valid
	private SpecificationDTO specification;

	private List<String> features;

	private MultipartFile brochure;

	private MultipartFile video;

}
