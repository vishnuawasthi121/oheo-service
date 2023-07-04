package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

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

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.ServiceProviderRegistrationRequestDTO;
import com.ogive.oheo.dto.ServiceProviderResponseDTO;
import com.ogive.oheo.dto.utils.CMSSpecifications;
import com.ogive.oheo.persistence.entities.ServiceProviders;
import com.ogive.oheo.persistence.repo.ServiceProvidersRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Services")
@RestController
public class ServicesProviderController {

	private static final Logger LOG = LoggerFactory.getLogger(ServicesProviderController.class);

	@Autowired
	private ServiceProvidersRepository serviceProvidersRepository;

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/services-providers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addServiceProvider(@RequestBody @Valid ServiceProviderRegistrationRequestDTO request) {
		LOG.info("addServiceProvider request received@@   {}");
		ServiceProviders entity = new ServiceProviders();
		BeanUtils.copyProperties(request, entity);
		serviceProvidersRepository.save(entity);
		return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Updates a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/services-providers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateServiceProvider(@PathVariable Long id,
			@RequestBody @Valid ServiceProviderRegistrationRequestDTO request) {
		LOG.info("updateServiceProvider request received against id@@   {}", id);
		Optional<ServiceProviders> entityData = serviceProvidersRepository.findById(id);
		if (entityData.isPresent()) {
			ServiceProviders entity = entityData.get();
			BeanUtils.copyProperties(request, entity);
			serviceProvidersRepository.save(entity);
			return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a ServiceProviders with id=" + id),
				HttpStatus.BAD_REQUEST);
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
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllProducts request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ServiceProviderResponseDTO> allDTO = new ArrayList<>();
		Page<ServiceProviders> pages = serviceProvidersRepository.findAll(CMSSpecifications.filterServiceProvidersByName(criteria).and(CMSSpecifications.filterServiceProvidersByStatus(criteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ServiceProviderResponseDTO dto = new ServiceProviderResponseDTO();
				BeanUtils.copyProperties(entity, dto);
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
	public ResponseEntity<Object> deleteServiceProvider(@PathVariable Long id) {
		LOG.info("deleteServiceProvider request received @@   {}", id);
		serviceProvidersRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
