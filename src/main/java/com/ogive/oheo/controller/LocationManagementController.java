package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import javax.validation.Valid;

import org.apache.commons.collections.CollectionUtils;
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

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.CityRequestDTO;
import com.ogive.oheo.dto.CityResponseDTO;
import com.ogive.oheo.dto.CountryDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.StateDTO;
import com.ogive.oheo.dto.StateResponseDTO;
import com.ogive.oheo.dto.ZipcodeRequestDTO;
import com.ogive.oheo.dto.ZipcodeResponseDTO;
import com.ogive.oheo.dto.ZoneDetailDTO;
import com.ogive.oheo.dto.ZoneDetailRequestDTO;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Country;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.CountryRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.ZipcodeRepository;
import com.ogive.oheo.persistence.repo.ZoneDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Geographic location")
@RestController
@RequestMapping("/locations")
public class LocationManagementController {

	private static final Logger LOG = LoggerFactory.getLogger(LocationManagementController.class);

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

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/countries", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addCountry(@Valid @RequestBody CountryDTO countryDTO) {
		LOG.info("addCountry request received@@   {}", countryDTO);
		Country entity = new Country();
		BeanUtils.copyProperties(countryDTO, entity);

		Country persistedCountry = countryRepository.save(entity);

		LOG.info("Saved @@   {}", persistedCountry);
		return new ResponseEntity<Object>(persistedCountry.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/countries/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateCountry(@PathVariable Long id, @RequestBody Country country) {
		LOG.info("updateCountry requested record id {} ", id);
		LOG.info("updateCountry request body@@   {}", country);

		Optional<Country> foundCountry = countryRepository.findById(id);
		if (foundCountry.isPresent()) {

			Country countryToUpdate = foundCountry.get();
			countryToUpdate.setCountryCode(country.getCountryCode());
			countryToUpdate.setCountryName(country.getCountryName());
			Country updatedCountry = countryRepository.save(countryToUpdate);
			return new ResponseEntity<Object>(updatedCountry, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Did not find a record to update with given id " + id, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getCountry(@PathVariable Long id) {
		LOG.info("getCountry request received@@   {}", id);
		Optional<Country> fetchedCountry = countryRepository.findById(id);
		if (fetchedCountry.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedCountry);
			return new ResponseEntity<Object>(fetchedCountry.get(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/countries/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCountry(@PathVariable Long id) {
		LOG.info("deleteCountry request received@@   {}", id);
		countryRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// State Api

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/countries/{countryCode}/states", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addState(@PathVariable String countryCode, @Valid @RequestBody StateDTO stateDTO) {
		LOG.info("addState request received@@   {}", stateDTO);
		State state = new State();
		BeanUtils.copyProperties(stateDTO, state);
		Country findByCountryCode = countryRepository.findByCountryCode(countryCode);
		if (null == findByCountryCode) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a country with countryCode=" + countryCode),
					HttpStatus.BAD_REQUEST);
		}
		state.setStatus(stateDTO.getSatus());
		state.setCountry(findByCountryCode);
		State saved = stateRepository.save(state);
		return new ResponseEntity<Object>(saved, HttpStatus.OK);
	}

	@PutMapping(path = "/countries/{countryCode}/states/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateState(@PathVariable String countryCode, @PathVariable Long id,
			@RequestBody StateDTO stateDTO) {
		LOG.info("updateState requested record id {} ", id);
		LOG.info("updateState request body@@   {}", stateDTO);

		Country country = countryRepository.findByCountryCode(countryCode);
		if (Objects.isNull(country)) {
			return new ResponseEntity<Object>(
					"Did not find a Country record to update with given countryCode " + countryCode, HttpStatus.OK);
		}
		State stateToUpdate = stateRepository.findByCountryAndId(country, id);

		if (Objects.nonNull(stateToUpdate)) {
			stateToUpdate.setStateName(stateDTO.getStateName());
			stateToUpdate.setStateCode(stateDTO.getStateCode());
			State updated = stateRepository.save(stateToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Did not find a record to update with given id " + id, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{countryCode}/states/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getState(@PathVariable String countryCode, @PathVariable Long id) {
		LOG.info("getState request received@@   {}", id);
		Country country = countryRepository.findByCountryCode(countryCode);

		if (Objects.isNull(country)) {
			LOG.info("Did not find country by @@countryCode   {}", countryCode);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		State state = stateRepository.findByCountryAndId(country, id);

		if (Objects.nonNull(state)) {
			StateResponseDTO responseDTO = new StateResponseDTO();
			BeanUtils.copyProperties(state, responseDTO);

			responseDTO
					.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode, id)).withSelfRel());
			// responseDTO.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode,
			// id)).withRel("Delete - Delete operation"));
			// responseDTO.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode,
			// id)).withRel("Put - update operation"));

			LOG.info("Found data against ID {} and returing response   {}", id, state);
			return new ResponseEntity<Object>(responseDTO, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type for a country", notes = "Returns all instances of the type for a country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{countryCode}/states/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllState(@PathVariable String countryCode) {
		LOG.info("getState request received@@   {}", countryCode);
		Country country = countryRepository.findByCountryCode(countryCode);

		if (Objects.isNull(country)) {
			LOG.info("Did not find country by @@countryCode   {}", countryCode);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}
		List<State> allStates = stateRepository.findByCountry(country);

		List<StateResponseDTO> stateDTOList = new ArrayList<>();

		if (CollectionUtils.isNotEmpty(allStates)) {
			allStates.forEach(state -> {
				StateResponseDTO responseDTO = new StateResponseDTO();
				BeanUtils.copyProperties(state, responseDTO);

				responseDTO.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode, state.getId()))
						.withSelfRel());

				stateDTOList.add(responseDTO);

			});
			return new ResponseEntity<Object>(stateDTOList, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/countries/{countryCode}/states/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteState(@PathVariable String countryCode, @PathVariable Long id) {
		LOG.info("deleteCountry request received@@   {}", id);
		Country country = countryRepository.findByCountryCode(countryCode);
		stateRepository.deleteByCountryAndId(country, id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Zone Api
	// India is divided into four zones based on the lifestyle of people. These are
	// Western India, Eastern India, Northern India and Southern India
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/countries/{countryCode}/zones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addZone(@Valid @RequestBody ZoneDetailRequestDTO zoneDetailDTO,
			@PathVariable String countryCode) {
		LOG.info("addZone request received@@   {}", zoneDetailDTO);

		ZoneDetail zone = new ZoneDetail();

		BeanUtils.copyProperties(zoneDetailDTO, zone);

		Country findByCountryCode = countryRepository.findByCountryCode(countryCode);

		if (null == findByCountryCode) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a country with countryCode=" + countryCode),
					HttpStatus.BAD_REQUEST);
		}

		zone.setStatus(StatusCode.valueOf(zoneDetailDTO.getStatus()));

		zone.setCountry(findByCountryCode);

		ZoneDetail saved = zoneDetailRepository.save(zone);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/countries/{countryCode}/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateZone(@PathVariable String countryCode, @PathVariable Long id,
			@RequestBody ZoneDetailDTO zoneDetailDTO) {
		LOG.info("updateZone requested record countryCode {} ", countryCode);
		LOG.info("updateZone requested record id {} ", id);
		LOG.info("updateZone request body@@   {}", zoneDetailDTO);

		ZoneDetail zoneDetailToUpdate = zoneDetailRepository.findZoneDetailByIdAndCountry(id,
				countryRepository.findByCountryCode(countryCode));
		if (Objects.nonNull(zoneDetailToUpdate)) {

			zoneDetailToUpdate.setName(zoneDetailDTO.getName());

			zoneDetailToUpdate.setStatus(StatusCode.valueOf(zoneDetailDTO.getStatus()));

			ZoneDetail updated = zoneDetailRepository.save(zoneDetailToUpdate);

			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Did not find a record to update with given id " + id, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{countryCode}/zones/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getZone(@PathVariable String countryCode, @PathVariable Long id) {
		LOG.info("getZone request received@@id   {}", id);

		LOG.info("getZone request received@@countryCode   {}", countryCode);
		Country country = countryRepository.findByCountryCode(countryCode);

		if (Objects.isNull(country)) {
			LOG.info("Did not find country by @@countryCode   {}", countryCode);
			return new ResponseEntity<Object>(HttpStatus.OK);
		}

		Optional<ZoneDetail> zoneDetailOption = zoneDetailRepository.findById(id);

		if (zoneDetailOption.isPresent()) {

			ZoneDetailDTO dto = new ZoneDetailDTO();
			ZoneDetail zoneDetail = zoneDetailOption.get();
			BeanUtils.copyProperties(zoneDetail, dto);
			dto.setStatus(zoneDetail.getStatus().name());
			// dto.add(linkTo(methodOn(LocationManagementController.class).deleteZone(id)).withRel("delete-zone"));
			dto.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode, id)).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", id, zoneDetail);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{countryCode}/zones", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllZone(@PathVariable String countryCode) {
		LOG.info("getAllZone request received");

		LOG.info("getAllZone request received@@countryCode   {}", countryCode);

		Country country = countryRepository.findByCountryCode(countryCode);

		Iterable<ZoneDetail> allZonesEntities = zoneDetailRepository.findByCountry(country);

		List<ZoneDetailDTO> allZonesDTO = new ArrayList<>();

		if (null != allZonesEntities) {
			allZonesEntities.forEach(entity -> {
				ZoneDetailDTO dto = new ZoneDetailDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus().name());
				dto.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode, entity.getId()))
						.withSelfRel());
				allZonesDTO.add(dto);
			});
		}
		LOG.info("Total zones count {}", allZonesDTO.size());
		return new ResponseEntity<Object>(allZonesDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/countries/{countryCode}/zones/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteZone(@PathVariable String countryCode, @PathVariable Long id) {
		LOG.info("deleteZone request received@@   {}", id);
		Country country = countryRepository.findByCountryCode(countryCode);
		LOG.info("deleteZone request received@@ country  {}", country);
		zoneDetailRepository.deleteByCountryAndId(country, id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// City API

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/cities", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addCity(@PathVariable Long stateId, @Valid @RequestBody CityRequestDTO cityDTO) {
		LOG.info("addCity request received@@   {}", cityDTO);

		City city = new City();

		BeanUtils.copyProperties(cityDTO, city);

		city.setStatus(cityDTO.getStatus());

		Optional<State> stateOptional = stateRepository.findById(stateId);

		if (!stateOptional.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a State with stateId=" + stateId),
					HttpStatus.BAD_REQUEST);
		}

		city.setState(stateOptional.get());

		City saved = cityRepository.save(city);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/cities/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateCity(@PathVariable Long id, @RequestBody CityRequestDTO cityDTO) {
		LOG.info("updateCity request body@@   {}", cityDTO);

		Optional<City> cityOptionalData = cityRepository.findById(id);

		Optional<State> stateOptionalData = stateRepository.findById(cityDTO.getStateId());

		if (cityOptionalData.isEmpty()) {
			LOG.info("Did not find City by id   {}", id);
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a City by id=" + id),
					HttpStatus.BAD_REQUEST);
		}

		if (stateOptionalData.isEmpty()) {
			LOG.info("Did not find State by id   {}", id);
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a State by id=" + id),
					HttpStatus.BAD_REQUEST);
		}

		City cityToUpdate = cityOptionalData.get();

		if (Objects.nonNull(cityToUpdate)) {

			cityToUpdate.setName(cityDTO.getName());

			cityToUpdate.setStatus(cityDTO.getStatus());

			cityToUpdate.setState(stateOptionalData.get());

			City updated = cityRepository.save(cityToUpdate);

			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>("Did not find a record to update with given id " + id, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/cities/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getCity(@PathVariable Long id) {
		LOG.info("getCity request received@@id   {}", id);

		Optional<City> cityOptional = cityRepository.findById(id);

		if (cityOptional.isPresent()) {
			CityResponseDTO dto = new CityResponseDTO();
			City city = cityOptional.get();
			BeanUtils.copyProperties(city, dto);

			dto.setStateName(city.getState().getStateName());
			dto.setStateId(city.getState().getId());

			dto.add(linkTo(methodOn(LocationManagementController.class).getCity(id)).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", id, city);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/cities/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllCities() {
		LOG.info("getAllCities request received");

		Iterable<City> allCities = cityRepository.findAll();
		List<CityResponseDTO> cityDTOList = new ArrayList<>();

		if (null != allCities) {
			allCities.forEach(city -> {
				CityResponseDTO dto = new CityResponseDTO();
				BeanUtils.copyProperties(city, dto);

				dto.setStateName(city.getState().getStateName());
				dto.setStateId(city.getState().getId());

				dto.add(linkTo(methodOn(LocationManagementController.class).getCity(city.getId())).withSelfRel());
				cityDTOList.add(dto);
			});
			return new ResponseEntity<Object>(cityDTOList, HttpStatus.OK);
		}
		LOG.info("Did not find any records  allCities {}", allCities);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/cities/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCity(@PathVariable Long id) {
		LOG.info("deleteCity request received@@   {}", id);
		cityRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Zipcode
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/zipcodes", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addZipcode(@Valid @RequestBody ZipcodeRequestDTO zipcodeDTO) {
		LOG.info("addZipcode request received@@   {}", zipcodeDTO);

		Optional<City> cityData = cityRepository.findById(zipcodeDTO.getCityId());

		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with cityId=" + zipcodeDTO.getCityId()),
					HttpStatus.BAD_REQUEST);

		}
		Zipcode zipcode = new Zipcode();
		BeanUtils.copyProperties(zipcodeDTO, zipcode);
		
		zipcode.setCode(zipcodeDTO.getZipcode());
		
		zipcode.setStatus(zipcodeDTO.getStatus());
		zipcode.setCity(cityData.get());
		Zipcode saved = zipcodeRepository.save(zipcode);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/zipcodes/{zipcode}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateZipcode(@PathVariable Long zipcode,
			@Valid @RequestBody ZipcodeRequestDTO zipcodeDTO) {
		LOG.info("updateZipcode request received@@   {}", zipcode);

		Optional<City> cityData = cityRepository.findById(zipcodeDTO.getCityId());

		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with cityId=" + zipcodeDTO.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcodeEntity = zipcodeRepository.findByCode(zipcode);

		
		if (Objects.isNull(zipcodeEntity)) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Zipcode with code=" + zipcode),
					HttpStatus.BAD_REQUEST);
		}
		BeanUtils.copyProperties(zipcodeDTO, zipcodeEntity);
		zipcodeEntity.setCode(zipcode);
		zipcodeEntity.setStatus(zipcodeDTO.getStatus());
		zipcodeEntity.setCity(cityData.get());
		Zipcode saved = zipcodeRepository.save(zipcodeEntity);
		return new ResponseEntity<Object>(saved, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zipcodes/{zipcode}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getZipcode(@PathVariable Long zipcode) {
		LOG.info("getZipcode request received@@id   {}", zipcode);

		Zipcode zipcodeEntity = zipcodeRepository.findByCode(zipcode);
		if (Objects.nonNull(zipcodeEntity)) {
			ZipcodeResponseDTO dto = new ZipcodeResponseDTO();
			BeanUtils.copyProperties(zipcodeEntity, dto);
			
			dto.setCityId(zipcodeEntity.getCity().getId());
			dto.setCityName(zipcodeEntity.getCity().getName());
			dto.setStatus(zipcodeEntity.getStatus());
			dto.setZipcode(zipcodeEntity.getCode());
			
			dto.add(linkTo(methodOn(LocationManagementController.class).getZipcode(zipcode)).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", zipcode, zipcodeEntity);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", zipcode);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zipcodes/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllZipcodes() {
		LOG.info("getAllZipcodes request received");

		Iterable<Zipcode> appZipcodes = zipcodeRepository.findAll();

		List<ZipcodeResponseDTO> zipcodesLists = new ArrayList<>();

		if (null != appZipcodes) {
			appZipcodes.forEach(zipcode -> {
				ZipcodeResponseDTO dto = new ZipcodeResponseDTO();
				BeanUtils.copyProperties(zipcode, dto);

				dto.setCityId(zipcode.getCity().getId());
				dto.setCityName(zipcode.getCity().getName());
				dto.setStatus(zipcode.getStatus());
				dto.setZipcode(zipcode.getCode());
				
				dto.add(linkTo(methodOn(LocationManagementController.class).getZipcode(zipcode.getCode()))
						.withSelfRel());
				zipcodesLists.add(dto);
			});
			return new ResponseEntity<Object>(zipcodesLists, HttpStatus.OK);
		}
		LOG.info("Did not find any records  getAllZipcodes {}", zipcodesLists);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/zipcodes/{zipcode}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteZipcode(@PathVariable Long zipcode) {
		LOG.info("deleteCity request received@@   {}", zipcode);
		Zipcode zipcodeEntity = zipcodeRepository.findByCode(zipcode);
		zipcodeRepository.delete(zipcodeEntity);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
