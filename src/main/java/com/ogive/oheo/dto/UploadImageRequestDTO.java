package com.ogive.oheo.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@NoArgsConstructor

public class UploadImageRequestDTO {

	private String type;

	private MultipartFile file;

}
