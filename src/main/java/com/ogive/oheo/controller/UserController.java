package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.CountryResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.GroupDetailResponseDTO;
import com.ogive.oheo.dto.UserRequestDTO;
import com.ogive.oheo.dto.UserResponseDTO;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Country;
import com.ogive.oheo.persistence.entities.GroupDetail;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.UserInfo;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.CountryRepository;
import com.ogive.oheo.persistence.repo.GroupDetailRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.UserRepository;
import com.ogive.oheo.persistence.repo.ZipcodeRepository;
import com.ogive.oheo.persistence.repo.ZoneDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Vishnu Awasthi Vishnuawasthi121@gmail.com
 */

@Api(tags = "User Management")
@RestController

@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private CountryRepository countryRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ZoneDetailRepository zoneDetailRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private ZipcodeRepository zipcodeRepository;

	@Autowired
	private GroupDetailRepository groupDetailRepository;

	@Autowired
	private UserRepository userRepository;

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addUser(@Valid @RequestBody UserRequestDTO requestBody) {
		LOG.info("addUser request received@@   {}", requestBody);
		UserInfo entity = new UserInfo();

		BeanUtils.copyProperties(requestBody, entity);
		Optional<State> stateData = stateRepository.findById(requestBody.getStateId());

		if (stateData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a state with id=" + requestBody.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(requestBody.getZoneId());
		if (zoneData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a zone with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(requestBody.getCityId());
		if (cityData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a city with id=" + requestBody.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(requestBody.getZipcode());
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a zipcode with id=" + requestBody.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<GroupDetail> groupData = groupDetailRepository.findById(requestBody.getGroupId());
		if (groupData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a group with id=" + requestBody.getGroupId()),
					HttpStatus.BAD_REQUEST);
		}

		entity.setCity(cityData.get());

		entity.setGroupDetail(groupData.get());

		entity.setState(stateData.get());

		entity.setZipcode(zipcode);

		entity.setZoneDetail(zoneData.get());
		UserInfo saved = userRepository.save(entity);
		LOG.info("Saved @@   {}", entity);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateUser(@PathVariable Long id, @Valid @RequestBody UserRequestDTO requestBody) {
		LOG.info("updateUser request received@@   {}", requestBody);

		Optional<UserInfo> entityData = userRepository.findById(id);

		if (entityData.isEmpty()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a UserInfo with id=" + id),
					HttpStatus.BAD_REQUEST);
		}
		UserInfo entity = entityData.get();

		BeanUtils.copyProperties(requestBody, entity);

		Optional<State> stateData = stateRepository.findById(requestBody.getStateId());

		if (stateData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a state with id=" + requestBody.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(requestBody.getZoneId());
		if (zoneData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a zone with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(requestBody.getCityId());
		if (cityData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a city with id=" + requestBody.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(requestBody.getZipcode());
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a zipcode with id=" + requestBody.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<GroupDetail> groupData = groupDetailRepository.findById(requestBody.getGroupId());
		if (groupData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a group with id=" + requestBody.getGroupId()),
					HttpStatus.BAD_REQUEST);
		}

		entity.setCity(cityData.get());

		entity.setGroupDetail(groupData.get());

		entity.setState(stateData.get());

		entity.setZipcode(zipcode);

		entity.setZoneDetail(zoneData.get());

		UserInfo saved = userRepository.save(entity);

		LOG.info("Saved @@   {}", entity);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getUser(@PathVariable Long id) {
		LOG.info("getUser request received@@   {}", id);
		Optional<UserInfo> userInfoData = userRepository.findById(id);
		if (userInfoData.isPresent()) {
			UserInfo entity = userInfoData.get();
			UserResponseDTO dto = new UserResponseDTO();
			populateUserResponseDTO(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllUsers() {
		LOG.info("getAllUsers request received");
		Iterable<UserInfo> allUsers = userRepository.findAll();
		List<UserResponseDTO> allUsersDTOList = new ArrayList<>();

		if (null != allUsers) {
			allUsers.forEach(entity -> {
				UserResponseDTO dto = new UserResponseDTO();
				populateUserResponseDTO(entity, dto);
				allUsersDTOList.add(dto);
			});
		}
		LOG.info("Total zones count {}", allUsersDTOList.size());
		return new ResponseEntity<Object>(allUsersDTOList, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id.", notes = "If the entity is not found in the persistence store it is silently ignored", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteUser(@PathVariable Long id) {
		LOG.info("deleteUser request received id {}", id);
		userRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	private void populateUserResponseDTO(UserInfo entity, UserResponseDTO dto) {
		BeanUtils.copyProperties(entity, dto);
		//City Details
		dto.setCityId(entity.getCity().getId());
		dto.setCityName(entity.getCity().getName());
		//Group Details
		GroupDetail groupDetail = entity.getGroupDetail();
		GroupDetailResponseDTO groupDTO = new GroupDetailResponseDTO();

		BeanUtils.copyProperties(groupDetail, groupDTO);
		groupDTO.setStatus(groupDetail.getStatus());
		dto.setGroupDetail(groupDTO);

		//Zone Detail
		dto.setZipcode(entity.getZipcode().getCode());
		dto.setZoneId(entity.getZoneDetail().getId());
		dto.setZoneName(entity.getZoneDetail().getName());
		//Region or state
		dto.setStateId(entity.getState().getId());
		dto.setStateName(entity.getState().getStateName());
		dto.setStatus(entity.getStatus());
		dto.add(linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel());
	}

}
