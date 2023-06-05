package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.ChangePasswordRequestDTO;
import com.ogive.oheo.dto.CustomerResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.LoginRequestDTO;
import com.ogive.oheo.dto.UserDetailResponseDTO;
import com.ogive.oheo.persistence.entities.CustomerDetail;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.ViewUserDetails;
import com.ogive.oheo.persistence.repo.CustomerDetailRepository;
import com.ogive.oheo.persistence.repo.OTPRepository;
import com.ogive.oheo.persistence.repo.UserDetailRepository;
import com.ogive.oheo.persistence.repo.ViewUserDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Login")
@RestController
public class LoginController {
	
	private static final Logger LOG = LoggerFactory.getLogger(LoginController.class);
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private ViewUserDetailRepository viewUserDetailRepository;
	
	@Autowired
	private CustomerDetailRepository customerDetailRepository;
	
	@Autowired
	private OTPRepository otpRepository;

	@ApiOperation(value = "Login api - Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> doLogin(@Valid @RequestBody LoginRequestDTO loginRequest) {
		ViewUserDetails userDetailsEntity = viewUserDetailRepository.findByEmail(loginRequest.getEmail().toUpperCase());
		// Validate Password
		if (null != userDetailsEntity && userDetailsEntity.getPassword().equals(loginRequest.getPassword())) {
				UserDetailResponseDTO dto = new UserDetailResponseDTO();
				BeanUtils.copyProperties(userDetailsEntity, dto);
				dto.add(linkTo(methodOn(UserDetailController.class).getUserDetail(userDetailsEntity.getId())).withSelfRel());
				return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Invalid username or password"), HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Reset password of a user account - Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetPassword(@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		LOG.info("resetPassword  request  -  {}",changePasswordRequest );
		
		UserDetail userDetails = userDetailRepository.findByEmailAndPassword(
				changePasswordRequest.getEmail().toUpperCase(), changePasswordRequest.getPassword());

		if (Objects.nonNull(userDetails)) {
			userDetails.setPassword(changePasswordRequest.getNewPassword());
			userDetailRepository.save(userDetails);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Invalid username or password"), HttpStatus.BAD_REQUEST);
	}
	
	
	
	@ApiOperation(value = "Reset password of a user account - Customer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/customer-reset-password", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> resetCustomerPassword(
			@Valid @RequestBody ChangePasswordRequestDTO changePasswordRequest) {
		LOG.info("resetCustomerPassword  request  -  {}", changePasswordRequest);
		List<String> emailToFetch = new ArrayList<>();
		
		emailToFetch.add(changePasswordRequest.getEmail().toUpperCase());
		CustomerDetail customeDetail = customerDetailRepository.findByEmailIgnoreCaseIn(emailToFetch);

		if (null != customeDetail && customeDetail.getPassword().equals(changePasswordRequest.getPassword())) {

			customeDetail.setPassword(changePasswordRequest.getNewPassword());
			customerDetailRepository.save(customeDetail);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Invalid username or password"), HttpStatus.BAD_REQUEST);
	}
	
	
	@ApiOperation(value = "Login api - Customer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/customer-authenticate", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> doCustomerLogin(@Valid @RequestBody LoginRequestDTO loginRequest) {
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
}
