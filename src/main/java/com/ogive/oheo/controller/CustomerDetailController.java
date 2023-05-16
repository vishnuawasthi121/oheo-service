package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
import com.ogive.oheo.dto.CustomerRequestDTO;
import com.ogive.oheo.dto.CustomerResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.LoginRequestDTO;
import com.ogive.oheo.dto.UpdateCustomerRequestDTO;
import com.ogive.oheo.dto.utils.CustomerSpecification;
import com.ogive.oheo.persistence.entities.CustomerDetail;
import com.ogive.oheo.persistence.repo.CustomerDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Customer API")
@RestController

public class CustomerDetailController {

	private static final Logger LOG = LoggerFactory.getLogger(CustomerDetailController.class);

	@Autowired
	private CustomerDetailRepository customerDetailRepository;

	@ApiOperation(value = "Login api - Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/customer-authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> doLogin(@Valid @RequestBody LoginRequestDTO loginRequest) {
		List<String> emailToFetch  = new ArrayList<>();
		emailToFetch.add(loginRequest.getEmail().toUpperCase());
		LOG.info("Email  {}",emailToFetch);
		
		CustomerDetail customeDetail = customerDetailRepository.findByEmailIgnoreCaseIn(emailToFetch);
		// Validate Password
		if (null != customeDetail && customeDetail.getPassword().equals(loginRequest.getPassword())) {
			CustomerResponseDTO dto = new CustomerResponseDTO();
			BeanUtils.copyProperties(customeDetail, dto);
			dto.add(linkTo(methodOn(CustomerDetailController.class).getCustomer(customeDetail.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto,HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Invalid username or password"), HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Customer registraiton API", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/customers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addCustomer(@Valid @RequestBody CustomerRequestDTO request) {
		CustomerDetail customeDetail = new CustomerDetail();
		BeanUtils.copyProperties(request, customeDetail);
		customeDetail.setEmail(request.getEmail().toUpperCase());
		//TODO - Logic to handle EMAIL OTP
		
		
		try {
			customerDetailRepository.save(customeDetail);
		} catch (Exception e) {
			return new ResponseEntity<Object>(new ErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<Object>(customeDetail.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Update customer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/customers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateCustomer(@PathVariable Long id,@Valid @RequestBody UpdateCustomerRequestDTO request) {

		Optional<CustomerDetail> customeData = customerDetailRepository.findById(id);
		if (customeData.isPresent()) {
			try {
				CustomerDetail entity = customeData.get();
				entity.setFirstName(request.getFirstName());
				entity.setLastName(request.getLastName());
				customerDetailRepository.save(entity);
				return new ResponseEntity<Object>(entity.getId(), HttpStatus.OK);
			} catch (Exception e) {
				return new ResponseEntity<Object>(new ErrorResponseDTO(e.getMessage()), HttpStatus.BAD_REQUEST);
			}
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find customer record agains id=" + id),
				HttpStatus.BAD_REQUEST);

	}

	@ApiOperation(value = "Get customer details by id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/customers/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCustomer(@PathVariable Long id) {
		Optional<CustomerDetail> customeData = customerDetailRepository.findById(id);
		if (customeData.isPresent()) {
			CustomerDetail entity = customeData.get();
			CustomerResponseDTO dto = new CustomerResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(CustomerDetailController.class).getCustomer(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Get all customer details", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCustomer(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String firstName,
			@RequestParam(required = false) String lastName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		// For customer we have name = firstName
		// Last as a seperate field
		FilterCriteria criteria = new FilterCriteria(page, size, firstName, sortDirection, orderBy, status);
		criteria.setLastName(lastName);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));

		Map<String, Object> response = new HashMap<>();
		List<CustomerResponseDTO> allDTO = new ArrayList<>();
		Page<CustomerDetail> pages = customerDetailRepository.findAll(CustomerSpecification
				.filterCustomerByFirstName(criteria).and(CustomerSpecification.filterCustomerByLastName(criteria)),
				paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				CustomerResponseDTO dto = new CustomerResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				// dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(CustomerDetailController.class).getCustomer(entity.getId())).withSelfRel());
				allDTO.add(dto);
			});
		}
		response.put("customers", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total customer count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/customers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCustomer(@PathVariable Long id) {
		LOG.info("deleteCustomer request received@@   {}", id);
		customerDetailRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
