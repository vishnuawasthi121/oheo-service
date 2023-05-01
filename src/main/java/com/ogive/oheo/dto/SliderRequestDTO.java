package com.ogive.oheo.dto;

import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@NoArgsConstructor
@ToString

public class SliderRequestDTO {
	private String name;

	private MultipartFile slider;

	private ImageType imageType;

	private String description;

	private StatusCode status;

	private Long productId;
}
