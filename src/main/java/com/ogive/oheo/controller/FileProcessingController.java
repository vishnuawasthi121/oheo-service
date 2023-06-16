package com.ogive.oheo.controller;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterImagesByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterImagesByStatus;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.ImagesResponseDTO;
import com.ogive.oheo.exception.ValidationException;
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
	
	@RequestMapping(path = "/", method = RequestMethod.POST, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<Object> uploadFile(@RequestParam("file")  MultipartFile file) {
		LOG.info("uploadFile request received@@");
		Images fileEntity = new Images();
		fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
		fileEntity.setContentType(file.getContentType());
		try {
			fileEntity.setData(file.getBytes());
		} catch (IOException e) {
			LOG.error("@@@@@ Exception during reading file bytes data", e);
			throw new ValidationException(e.getMessage());
		}
		fileEntity.setSize(file.getSize());
		Images savedImage = imagesRepository.save(fileEntity);
		return new ResponseEntity<Object>(savedImage.getId(), HttpStatus.OK);
	}
	
	
	@PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateImage(@PathVariable Long id, @RequestPart("file") MultipartFile file) {
		LOG.info("updateImage requested record id {} ", id);

		Optional<Images> entityData = imagesRepository.findById(id);

		if (!entityData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Unable to find file against id = " + id),
					HttpStatus.BAD_REQUEST);
		}

		try {
			Images fileEntity = entityData.get();
			fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			fileEntity.setContentType(file.getContentType());
			fileEntity.setData(file.getBytes());
			fileEntity.setSize(file.getSize());
			imagesRepository.save(fileEntity);
			return new ResponseEntity<Object>(HttpStatus.OK);
		} catch (Exception e) {
			LOG.error("Exception while updading file id  = {}  ...  {}", id, e);
			return new ResponseEntity<Object>(new ErrorResponseDTO(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}
	

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllImages(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllImages request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ImagesResponseDTO> allDTO = new ArrayList<>();
		Page<Images> pages = imagesRepository.findAll(filterImagesByName(criteria).and(filterImagesByStatus(criteria)),
				paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ImagesResponseDTO dto = new ImagesResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(FileProcessingController.class).readFile(entity.getId())).withSelfRel());
				allDTO.add(dto);
			});
		}
		response.put("images", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	
	
	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteImage(@PathVariable Long id) {
		LOG.info("deleteFile request received@@   {}", id);
		imagesRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
