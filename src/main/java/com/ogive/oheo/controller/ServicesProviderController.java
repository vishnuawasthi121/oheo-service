package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.constants.ServiceProviderType;
import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.LogisticRequestDTO;
import com.ogive.oheo.dto.LogisticRequestResponseDTO;
import com.ogive.oheo.dto.ServiceProviderRegistrationRequestDTO;
import com.ogive.oheo.dto.ServiceProviderResponseDTO;
import com.ogive.oheo.dto.utils.CMSSpecifications;
import com.ogive.oheo.persistence.entities.LogisticRequest;
import com.ogive.oheo.persistence.entities.ServiceProviders;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.repo.LogisticRequestRepository;
import com.ogive.oheo.persistence.repo.ServiceProvidersRepository;
import com.ogive.oheo.persistence.repo.UserDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Services")
@RestController
public class ServicesProviderController {

	private static final Logger LOG = LoggerFactory.getLogger(ServicesProviderController.class);

	@Autowired
	private ServiceProvidersRepository serviceProvidersRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private LogisticRequestRepository logisticRequestRepository;
	

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/services-providers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addServiceProvider(@RequestBody @Valid ServiceProviderRegistrationRequestDTO request) {
		LOG.info("addServiceProvider request received@@   {}");
		ServiceProviders entity = new ServiceProviders();
		UserDetail userDetail = null;
		BeanUtils.copyProperties(request, entity);
		if (request.getCompanyOrGroupId() != null) {
			Optional<UserDetail> entityData = userDetailRepository.findById(request.getCompanyOrGroupId());
			if (entityData.isPresent()) {
				userDetail = entityData.get();
				entity.setUserDetail(userDetail);
				entity.setCompanyOrGroupName(userDetail.getName());
			}
		}
		if (StringUtils.isNotEmpty(request.getCompanyOrGroupName()) && Objects.isNull(userDetail)) {
			entity.setCompanyOrGroupName(request.getCompanyOrGroupName());
		}
		serviceProvidersRepository.save(entity);
		return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Updates a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/services-providers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateServiceProvider(@PathVariable Long id,@RequestBody @Valid ServiceProviderRegistrationRequestDTO request) {
		LOG.info("updateServiceProvider request received against id@@   {}", id);
		Optional<ServiceProviders> entityData = serviceProvidersRepository.findById(id);
		if (entityData.isPresent()) {
			ServiceProviders entity = entityData.get();
			BeanUtils.copyProperties(request, entity);

			UserDetail userDetail = null;
			if (request.getCompanyOrGroupId() != null) {
				Optional<UserDetail> userEntityData = userDetailRepository.findById(request.getCompanyOrGroupId());
				if (userEntityData.isPresent()) {
					userDetail = userEntityData.get();
					entity.setUserDetail(userDetail);
					entity.setCompanyOrGroupName(userDetail.getName());
				}
			}
			if (StringUtils.isNotEmpty(request.getCompanyOrGroupName()) && Objects.isNull(userDetail)) {
				entity.setCompanyOrGroupName(request.getCompanyOrGroupName());
			}
			serviceProvidersRepository.save(entity);
			return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
		}

		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a ServiceProviders with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/services-providers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getServiceProvider(@PathVariable Long id) {
		LOG.info("getServiceProvider request received@@   {}", id);
		Optional<ServiceProviders> entityData = serviceProvidersRepository.findById(id);
		if (entityData.isPresent()) {
			ServiceProviders entity = entityData.get();
			ServiceProviderResponseDTO dto = new ServiceProviderResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			
			if(Objects.nonNull(entity.getUserDetail())) {
				dto.setCompanyOrGroupId(entity.getUserDetail().getId());
			}
			
			dto.add(linkTo(methodOn(ServicesProviderController.class).getServiceProvider(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/services-providers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllServiceProvides(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status,
			@RequestParam(required = true) ServiceProviderType type) {
		LOG.info("getAllProducts request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		criteria.setType(type);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ServiceProviderResponseDTO> allDTO = new ArrayList<>();
		Page<ServiceProviders> pages = serviceProvidersRepository.findAll(CMSSpecifications.filterServiceProvidersByName(criteria).and(CMSSpecifications.filterServiceProvidersByStatus(criteria).and(CMSSpecifications.filterServiceProvidersByType(criteria))), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ServiceProviderResponseDTO dto = new ServiceProviderResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				if(Objects.nonNull(entity.getUserDetail())) {
					dto.setCompanyOrGroupId(entity.getUserDetail().getId());
				}
				dto.add(linkTo(methodOn(ServicesProviderController.class).getServiceProvider(entity.getId()))
						.withSelfRel());
				allDTO.add(dto);
			});
		}
		response.put("serviceProviders", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/services-providers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteServiaddLogisticRequestceProvider(@PathVariable Long id) {
		LOG.info("deleteServiceProvider request received @@   {}", id);
		serviceProvidersRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Logistic request - API 
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/logistic-requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addLogisticRequest(@RequestBody @Valid LogisticRequestDTO request) {
		LOG.info("addLogisticRequest request received@@   {}");
		LogisticRequest entity = new LogisticRequest();
		BeanUtils.copyProperties(request, entity);

		Optional<UserDetail> userEntityData = userDetailRepository.findById(request.getDealerId());
		
		if (!userEntityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Dealer (USER) with id=" + request.getDealerId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setUserDetail(userEntityData.get());
		logisticRequestRepository.save(entity);
		return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Update a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/logistic-requests/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateLogisticRequest(@PathVariable Long id,@RequestBody @Valid LogisticRequestDTO request) {
		LOG.info("addLogisticRequest request received@@   {}");
		Optional<LogisticRequest> logisticData = logisticRequestRepository.findById(id);
		if (logisticData.isPresent()) {
			LogisticRequest entity = logisticData.get();
			BeanUtils.copyProperties(request, entity);
			
			Optional<UserDetail> userEntityData = userDetailRepository.findById(request.getDealerId());

			if (!userEntityData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Dealer (USER) with id=" + request.getDealerId()),
						HttpStatus.BAD_REQUEST);
			}
			
			entity.setUserDetail(userEntityData.get());
			logisticRequestRepository.save(entity);
			return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(
				new ErrorResponseDTO("Did not find a LogisticRequest with id=" + request.getDealerId()),
				HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/logistic-requests/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getLogisticRequest(@PathVariable Long id) {
		LOG.info("addLogisticRequest request received@@   {}");
		Optional<LogisticRequest> logisticData = logisticRequestRepository.findById(id);
		if (logisticData.isPresent()) {
			LogisticRequestResponseDTO dto = new LogisticRequestResponseDTO();
			LogisticRequest entity = logisticData.get();
			BeanUtils.copyProperties(entity, dto);

			if (Objects.nonNull(entity.getUserDetail())) {
				dto.setDealerId(entity.getUserDetail().getId());
				dto.setDealerName(entity.getUserDetail().getName());
			}
			dto.add(linkTo(methodOn(ServicesProviderController.class).getLogisticRequest(entity.getId()))
					.withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/logistic-requests", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllLogisticRequests(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllLogisticRequests request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<LogisticRequestResponseDTO> allDTO = new ArrayList<>();
		Page<LogisticRequest> pages = logisticRequestRepository.findAll(CMSSpecifications.filterLogisticRequestByName(criteria), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				LogisticRequestResponseDTO dto = new LogisticRequestResponseDTO();
				BeanUtils.copyProperties(entity, dto);

				if (Objects.nonNull(entity.getUserDetail())) {
					dto.setDealerId(entity.getUserDetail().getId());
					dto.setDealerName(entity.getUserDetail().getName());
				}
				dto.add(linkTo(methodOn(ServicesProviderController.class).getLogisticRequest(entity.getId())).withSelfRel());
				allDTO.add(dto);
			});
		}
		response.put("logisticRequests", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Deletes the entity with the given id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/logistic-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteLogisticRequest(@PathVariable Long id) {
		LOG.info("deleteLogisticRequest request received @@   {}", id);
		logisticRequestRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
