package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.LeaseDetailRequestDTO;
import com.ogive.oheo.dto.ProductLeaseDetailResponseDTO;
import com.ogive.oheo.dto.utils.CMSSpecifications;
import com.ogive.oheo.dto.utils.CommonsUtil;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.LeaseDetail;
import com.ogive.oheo.persistence.entities.Product;
import com.ogive.oheo.persistence.entities.ViewProductLeaseDetail;
import com.ogive.oheo.persistence.repo.ImagesRepository;
import com.ogive.oheo.persistence.repo.LeaseDetailRepository;
import com.ogive.oheo.persistence.repo.ProductRepository;
import com.ogive.oheo.persistence.repo.ViewProductLeaseDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Manage Lease")
@RestController

public class ManageLeaseController {
	
	private static final Logger LOG = LoggerFactory.getLogger(ManageLeaseController.class);

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private LeaseDetailRepository leaseDetailRepository;

	@Autowired
	private ImagesRepository imagesRepository;

	@Autowired
	private ViewProductLeaseDetailRepository viewProductLeaseDetailRepository;

	// Add Lease to product
	@Transactional
	@ApiOperation(value = "Add/Update Lease detail on a Vehicle product. ", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/products/{productId}/lease-details", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addLeaseDetail(@PathVariable Long productId,@ModelAttribute LeaseDetailRequestDTO requestBody) {
		LOG.info("addLeaseDetail request received@@   {}", requestBody);
		//Product Entity
		Optional<Product> productEntityData = productRepository.findById(productId);
		Product product = productEntityData.get();

		if (!productEntityData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Product with id=" + productId),
					HttpStatus.BAD_REQUEST);
		}
		// Create lease on a product
		LeaseDetail entity = null;
		if(null != requestBody.getLeaseId()) {
			Optional<LeaseDetail> entityData = leaseDetailRepository.findById(requestBody.getLeaseId());
			entity = entityData.get();
		}
		entity = entity == null ? new LeaseDetail() :  entity;
		BeanUtils.copyProperties(requestBody, entity);
		entity.setProduct(product);
		entity.setCreatedDate(new Date());
		entity = leaseDetailRepository.save(entity);

		if (null != requestBody.getImage() && !requestBody.getImage().isEmpty()) {
			List<ImageType> imageTypes = new ArrayList<>();
			imageTypes.add(ImageType.Lease);
			imagesRepository.deleteByLeaseDetailAndImageTypeIn(entity.getId(), imageTypes);
			Images leaseImage = new Images();
			CommonsUtil.multiPartToFileEntity(requestBody.getImage(), leaseImage, ImageType.Lease);
			leaseImage.setLeaseDetail(entity);
			imagesRepository.save(leaseImage);
		}
		productRepository.save(product);
		return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);

	}

	/**
	@Transactional
	@ApiOperation(value = "Update lease Details", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/products/{productId}/lease-details/{leaseId}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> updateLeaseDetail(@PathVariable Long productId, @PathVariable Long leaseId,@ModelAttribute LeaseDetailRequestDTO requestBody) {
		LOG.info("updateLeaseDetail request received@@  productId {}  and leaseId {leaseId}", productId, leaseId);
		Optional<LeaseDetail> entityData = leaseDetailRepository.findById(leaseId);
		
		if (entityData.isPresent()) {
			LeaseDetail entity = entityData.get();
			BeanUtils.copyProperties(requestBody, entity);
			entity.setUpdatedDate(new Date());
			entity = leaseDetailRepository.save(entity);
			if (null != requestBody.getImage() && !requestBody.getImage().isEmpty()) {
				List<ImageType> imageTypes = new ArrayList<>();
				imageTypes.add(ImageType.Lease);
				imagesRepository.deleteByLeaseDetailAndImageTypeIn(entity.getId(), imageTypes);
				Images leaseImage = new Images();
				CommonsUtil.multiPartToFileEntity(requestBody.getImage(), leaseImage, ImageType.Lease);
				leaseImage.setLeaseDetail(entity);
				imagesRepository.save(leaseImage);
			}
			return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a LeaseDetail with id=" + leaseId),
				HttpStatus.BAD_REQUEST);

	}  **/

	@Transactional
	@ApiOperation(value = "Delete lease details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/products/{productId}/lease-details/{leaseId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteLeaseDetail(@PathVariable Long productId, @PathVariable Long leaseId) {
		LOG.info("deleteLeaseDetail request received@@  productId {}  and leaseId {leaseId}", productId, leaseId);
		List<ImageType> imageTypes = new ArrayList<>();
		imageTypes.add(ImageType.Lease);
		imagesRepository.deleteByLeaseDetailAndImageTypeIn(leaseId, imageTypes);
		leaseDetailRepository.deleteById(leaseId);
		//productRepository.upadateSetAvailableForLease("N", productId);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products/lease-details/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getLeaseDetail(@PathVariable Long id) {
		LOG.info("getLeaseDetail request received@@   {}", id);
		Optional<ViewProductLeaseDetail> entityData = viewProductLeaseDetailRepository.findById(id);
		if (entityData.isPresent()) {
			ViewProductLeaseDetail entity = entityData.get();
			ProductLeaseDetailResponseDTO dto = new ProductLeaseDetailResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(ManageLeaseController.class).getLeaseDetail(entity.getLeaseId())).withSelfRel());
			dto.add(linkTo(methodOn(FileProcessingController.class).readFile(entity.getImageId()))
					.withRel("Lease-Doc"));
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products/lease-details", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllLeaseDetails(
			@RequestParam(defaultValue = "0") int page, 
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String filterByProductName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "leaseId") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllLeaseDetails request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByProductName, sortDirection, orderBy, status);

		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		
		Map<String, Object> response = new HashMap<>();
		List<ProductLeaseDetailResponseDTO> allDTO = new ArrayList<>();
		Page<ViewProductLeaseDetail> pages = viewProductLeaseDetailRepository.findAll(CMSSpecifications.filterLeaseDetailByProductName(criteria).and(CMSSpecifications.filterLeaseDetailByProductStatus(criteria)), paging);
		
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ProductLeaseDetailResponseDTO dto = new ProductLeaseDetailResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(ManageLeaseController.class).getLeaseDetail(entity.getLeaseId())).withSelfRel());
				dto.add(linkTo(methodOn(FileProcessingController.class).readFile(entity.getImageId())).withRel("Lease-Doc"));
				allDTO.add(dto);
			});
		}
		response.put("leaseDetails", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
}
