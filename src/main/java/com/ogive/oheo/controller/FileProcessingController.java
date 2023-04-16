package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.FileResponseDTO;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.repo.ImagesRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "File processing")
@RestController
@RequestMapping("/files")
public class FileProcessingController {

	private static final Logger LOG = LoggerFactory.getLogger(FileProcessingController.class);

	@Autowired
	private ImagesRepository imagesRepository;
/**
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getFile(@PathVariable Long id) {
		LOG.info("getFile request received@@   {}", id);
		Optional<Images> fetchedData = imagesRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			Images entity = fetchedData.get();
			FileResponseDTO dto = new FileResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(FileProcessingController.class).getFile(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	**/
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> readFile(@PathVariable Long id) {
		LOG.info("getFile request received@@   {}", id);
		Optional<Images> fetchedData = imagesRepository.findById(id);

		if (!fetchedData.isPresent()) {
			return ResponseEntity.notFound().build();
		}

		Images images = fetchedData.get();
		return ResponseEntity.ok()
				//Use below to enable download 
				//.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + images.getName() + "\"")
				.header(HttpHeaders.CONTENT_DISPOSITION,"filename=\"" + images.getName() + "\"")
				.contentType(MediaType.valueOf(images.getContentType())).body(images.getData());
	}

}
