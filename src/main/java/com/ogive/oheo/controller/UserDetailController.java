package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.constants.RoleTypes;
import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.EmailDetails;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.UpdateUserRequestDTO;
/*import com.ogive.oheo.constants.UserRole;*/
import com.ogive.oheo.dto.UserDetailRequestDTO;
import com.ogive.oheo.dto.UserDetailResponseDTO;
import com.ogive.oheo.dto.UserRoleRequestDTO;
import com.ogive.oheo.dto.UserRoleResponseDTO;
import com.ogive.oheo.dto.utils.CommonsUtil;
import com.ogive.oheo.dto.utils.GeographicLocationSpecifications;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.UserRole;
import com.ogive.oheo.persistence.entities.ViewUserDetails;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.UserDetailRepository;
import com.ogive.oheo.persistence.repo.UserRoleRepository;
import com.ogive.oheo.persistence.repo.ViewUserDetailRepository;
import com.ogive.oheo.persistence.repo.ZipcodeRepository;
import com.ogive.oheo.persistence.repo.ZoneDetailRepository;
import com.ogive.oheo.services.EmailServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "User Setup")
@RestController
@RequestMapping("/user-setup")
public class UserDetailController {

	private static final Logger LOG = LoggerFactory.getLogger(UserDetailController.class);

	@Autowired
	private UserDetailRepository userDetailRepository;

	@Autowired
	private UserRoleRepository userRoleRepository;

	@Autowired
	private ZoneDetailRepository zoneDetailRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ZipcodeRepository zipcodeRepository;

	@Autowired
	private ViewUserDetailRepository viewUserDetailRepository;

	@Autowired
	private EmailServiceImpl emailServiceImpl;

	@Value("${app.email.enabled}")
	private boolean isEmailEnabled;

	@Value("${oheo.user.registration.email.subject}")
	private String registrationEmailsubject;

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addUser(@Valid @RequestBody UserDetailRequestDTO userDetailRequestDTO) {
		LOG.info("addUser request received@@   {}", userDetailRequestDTO);
		UserDetail entity = new UserDetail();
		BeanUtils.copyProperties(userDetailRequestDTO, entity);
		entity.setEmail(userDetailRequestDTO.getEmail().toUpperCase());

		//Email id validation if already exist in database.
		UserDetail entityByNewUserEmail= userDetailRepository.findByEmail(userDetailRequestDTO.getEmail().toUpperCase());
		
		if (Objects.nonNull(entityByNewUserEmail)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Email already exist=" + userDetailRequestDTO.getEmail()),
					HttpStatus.BAD_REQUEST);
		}
		
		entity.setCreated(new Date());
		entity.setUpdated(new Date());
		String createdByUser = userDetailRequestDTO.getCreatedByUser();
		// Created By
		UserDetail createdByUserEntity = userDetailRepository.findByEmail(createdByUser);
		if (Objects.isNull(createdByUserEntity)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserDetail with email=" + createdByUser),
					HttpStatus.BAD_REQUEST);
		}

		Optional<UserRole> roleData = userRoleRepository.findById(userDetailRequestDTO.getRoleId());

		if (!roleData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserRole with id=" + userDetailRequestDTO.getRoleId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(userDetailRequestDTO.getZoneId());
		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ZoneDetail with id=" + userDetailRequestDTO.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<State> stateData = stateRepository.findById(userDetailRequestDTO.getStateId());
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with id=" + userDetailRequestDTO.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(userDetailRequestDTO.getCityId());
		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with id=" + userDetailRequestDTO.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(userDetailRequestDTO.getZipcode());
		
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Zipcode by code =" + userDetailRequestDTO.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}
		// Set auto generated password to entity
		entity.setPassword(CommonsUtil.generatePassword());

		entity.setZone(zoneData.get());
		entity.setState(stateData.get());
		entity.setCity(cityData.get());
		entity.setZipcode(zipcode);

		entity.setRoot(createdByUserEntity);
		entity.setValidated(false);
		entity.setRole(roleData.get());
		UserDetail savedEntity = userDetailRepository.save(entity);

		Map<String, Object> map = new HashMap<>();
		map.put("email", savedEntity.getEmail());
		map.put("password", savedEntity.getPassword());
		map.put("id", savedEntity.getId());

		// Prepare and send email
		if (isEmailEnabled) {
			EmailDetails emailDetails = new EmailDetails();
			emailDetails.setSubject(registrationEmailsubject);
			emailDetails.setName(savedEntity.getName());
			emailDetails.setRecipient(savedEntity.getEmail());
			emailDetails.setUsername(savedEntity.getEmail());
			emailDetails.setUserPasswordToSend(savedEntity.getPassword());
			emailServiceImpl.sendEmailWithTemplate(emailDetails);
		}
		return new ResponseEntity<Object>(map, HttpStatus.OK);
	}

	@ApiOperation(value = "Update a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUser(@PathVariable Long id,@Valid @RequestBody UpdateUserRequestDTO userDetailRequestDTO) {
		LOG.info("updateUser request received@@   {}", userDetailRequestDTO);
		Optional<UserDetail> userData = userDetailRepository.findById(id);

		if (!userData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find UserDetail with id=" + id),HttpStatus.BAD_REQUEST);
		}
		
		/*
		 * // Email id validation if already exist in database. UserDetail
		 * entityByNewUserEmail =
		 * userDetailRepository.findByEmail(userDetailRequestDTO.getEmail().toUpperCase(
		 * ));
		 * 
		 * if (Objects.nonNull(entityByNewUserEmail)) { return new
		 * ResponseEntity<Object>( new ErrorResponseDTO("Email already exist=" +
		 * userDetailRequestDTO.getEmail()), HttpStatus.BAD_REQUEST); }
		 */
		
		UserDetail entity = userData.get();
		BeanUtils.copyProperties(userDetailRequestDTO, entity);
		entity.setUpdated(new Date());
		
		Optional<UserRole> roleData = userRoleRepository.findById(userDetailRequestDTO.getRoleId());

		if (!roleData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserRole with id=" + userDetailRequestDTO.getRoleId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(userDetailRequestDTO.getZoneId());
		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ZoneDetail with id=" + userDetailRequestDTO.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<State> stateData = stateRepository.findById(userDetailRequestDTO.getStateId());
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with id=" + userDetailRequestDTO.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(userDetailRequestDTO.getCityId());
		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with id=" + userDetailRequestDTO.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(userDetailRequestDTO.getZipcode());
		
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Zipcode by zipcode=" + userDetailRequestDTO.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}
		//entity.setEmail(userDetailRequestDTO.getEmail().toUpperCase());
		entity.setZone(zoneData.get());
		entity.setState(stateData.get());
		entity.setCity(cityData.get());
		entity.setZipcode(zipcode);

		UserDetail savedEntity = userDetailRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getUserDetail(@PathVariable Long id) {
		LOG.info("getUserDetail request received@@   {}", id);
		Optional<ViewUserDetails> fetchedData = viewUserDetailRepository.findById(id);
		if (fetchedData.isPresent()) {
			ViewUserDetails entity = fetchedData.get();
			UserDetailResponseDTO dto = new UserDetailResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(UserDetailController.class).getUserDetail(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves all entity along with search and filter", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllUsers(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status,
			@RequestParam(required = false) RoleTypes roleTypes) {
		LOG.info("getAllUsers request received@@");
		
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		filterCriteria.setRoleTypes(roleTypes);
		
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<UserDetailResponseDTO> dtoList = new ArrayList<>();
		Page<ViewUserDetails> pages = viewUserDetailRepository
				.findAll(GeographicLocationSpecifications.filterUserDetailByName(filterCriteria)
						.and(GeographicLocationSpecifications.filterUserDetailByStatus(filterCriteria)
								.and(GeographicLocationSpecifications.filterUserDetailByRoleTypes(filterCriteria))), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				UserDetailResponseDTO dto = new UserDetailResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(UserDetailController.class).getUserDetail(entity.getId())).withSelfRel());
				dtoList.add(dto);
			});
		}
		response.put("users", dtoList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrive all the child account of an root", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{rootId}/sub-users", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getSubUsers(@PathVariable Long rootId, @RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getSubUsers request received@@   {}", rootId);
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		filterCriteria.setId(rootId);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();

		Page<ViewUserDetails> pages = viewUserDetailRepository
				.findAll(GeographicLocationSpecifications.findAllUserDetailByRootId(filterCriteria), paging);

		List<UserDetailResponseDTO> dtoList = new ArrayList<>();
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				UserDetailResponseDTO dto = new UserDetailResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(UserDetailController.class).getUserDetail(entity.getId())).withSelfRel());
				dtoList.add(dto);
			});
		}
		response.put("users", dtoList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
		LOG.info("deleteUser request received @@   {}", id);
		userDetailRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Roles API
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/roles", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addUserRole(@Valid @RequestBody UserRoleRequestDTO roleRequest) {
		LOG.info("addUserRole request received@@   {}", roleRequest);
		UserRole entity = new UserRole();
		BeanUtils.copyProperties(roleRequest, entity);
		UserRole savedEntity = userRoleRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Update a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/roles/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUserRole(@PathVariable Long id,@Valid @RequestBody UserRoleRequestDTO roleRequest) {
		LOG.info("updateUserRole request received@@ ID, BODY  {}  {}", id, roleRequest);
		Optional<UserRole> roleData = userRoleRepository.findById(id);
		if (roleData.isPresent()) {
			UserRole entity = roleData.get();
			BeanUtils.copyProperties(roleRequest, entity);

			UserRole savedEntity = userRoleRepository.save(entity);
			LOG.info("Saved @@   {}", savedEntity);
			return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find UserRole with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/roles/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getRole(@PathVariable Long id) {
		LOG.info("getRole request received@@   {}", id);
		Optional<UserRole> fetchedData = userRoleRepository.findById(id);
		if (fetchedData.isPresent()) {
			UserRole entity = fetchedData.get();
			UserRoleResponseDTO dto = new UserRoleResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(UserDetailController.class).getRole(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves role list along with id &  name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/roles/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> userRoleDropdown() {
		LOG.info("userRoleDropdown request received@@");
		List<Object[]> roleData = userRoleRepository.dropDown();
		List<Map<Object, Object>> dropDowns = new ArrayList<>();
		roleData.forEach(data -> {
			Map<Object, Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns, HttpStatus.OK);
	}
}
