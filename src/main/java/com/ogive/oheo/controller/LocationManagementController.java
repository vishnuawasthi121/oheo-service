package com.ogive.oheo.controller;

import static com.ogive.oheo.dto.utils.GeographicLocationSpecifications.filterByName;
import static com.ogive.oheo.dto.utils.GeographicLocationSpecifications.filterByStatus;
import static com.ogive.oheo.dto.utils.GeographicLocationSpecifications.findZipcodeByCityName;
import static com.ogive.oheo.dto.utils.GeographicLocationSpecifications.findZipcodeByCode;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.CityRequestDTO;
import com.ogive.oheo.dto.CityResponseDTO;
import com.ogive.oheo.dto.CountryDTO;
import com.ogive.oheo.dto.CountryResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.StateRequestDTO;
import com.ogive.oheo.dto.StateResponseDTO;
import com.ogive.oheo.dto.ZipcodeRequestDTO;
import com.ogive.oheo.dto.ZipcodeResponseDTO;
import com.ogive.oheo.dto.ZoneDetailRequestDTO;
import com.ogive.oheo.dto.ZoneDetailResponseDTO;
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

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
	public ResponseEntity<Object> updateCountry(@PathVariable Long id, @Valid @RequestBody CountryDTO request) {
		LOG.info("updateCountry requested record id {} ", id);
		LOG.info("updateCountry request body@@   {}", request);

		Optional<Country> foundCountry = countryRepository.findById(id);
		if (foundCountry.isPresent()) {
			Country countryToUpdate = foundCountry.get();
			countryToUpdate.setCountryCode(request.getCountryCode());
			countryToUpdate.setCountryName(request.getCountryName());
			countryToUpdate.setStatus(request.getStatus());
			Country updatedCountry = countryRepository.save(countryToUpdate);
			return new ResponseEntity<Object>(updatedCountry, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a country with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves countries list along with city name", notes = "Retrieves countries list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> countriesDropdown() {
		LOG.info("countriesDropdown request received@@");
		List<Object[]> zipcodeData = countryRepository.dropDown();
		List<Map<Object,Object>> dropDowns = new ArrayList<>();
		zipcodeData.forEach(data -> {
			Map<Object,Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns,HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getCountry(@PathVariable Long id) {
		LOG.info("getCountry request received@@   {}", id);
		Optional<Country> fetchedCountry = countryRepository.findById(id);
		if (fetchedCountry.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedCountry);
			Country entity = fetchedCountry.get();
			CountryResponseDTO dto = new CountryResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(LocationManagementController.class).getCountry(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/countries", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllCountries() {
		LOG.info("getAllCountries request received");
		Iterable<Country> allCountries = countryRepository.findAll();
		List<CountryResponseDTO> allCountriesDTOList = new ArrayList<>();
		if (null != allCountries) {
			allCountries.forEach(entity -> {
				CountryResponseDTO dto = new CountryResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(LocationManagementController.class).getCountry(entity.getId())).withSelfRel());
				allCountriesDTOList.add(dto);
			});
		}
		LOG.info("Total Countries count {}", allCountriesDTOList.size());
		return new ResponseEntity<Object>(allCountriesDTOList, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/countries/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCountry(@PathVariable Long id) {
		LOG.info("deleteCountry request received@@   {}", id);
		countryRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// State Api

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/{countryCode}/states", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addState(@PathVariable String countryCode, @Valid @RequestBody StateRequestDTO stateDTO) {
		LOG.info("addState request received@@   {}", stateDTO);
		State state = new State();
		
		BeanUtils.copyProperties(stateDTO, state);
		Country country = countryRepository.findByCountryCode(countryCode);
		
		if (null == country) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a country with countryCode=" + countryCode),
					HttpStatus.BAD_REQUEST);
		}
		
		Long zoneId = stateDTO.getZoneId();
		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(zoneId);
		
		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find ZoneDetail with zoneId=" + zoneId),
					HttpStatus.BAD_REQUEST);
		}
		
		state.setZone(zoneData.get());
		state.setStatus(stateDTO.getSatus());
		state.setCountry(country);
		State saved = stateRepository.save(state);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/states/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateState(@PathVariable Long id, @Valid @RequestBody StateRequestDTO stateDTO) {
		LOG.info("updateState requested record id {} ", id);
		Optional<State> stateData = stateRepository.findById(id);
		Long zoneId = stateDTO.getZoneId();
		
		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(zoneId);
		
		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find ZoneDetail with zoneId=" + zoneId),
					HttpStatus.BAD_REQUEST);
		}

		if (stateData.isPresent()) {
			State stateToUpdate = stateData.get();
			stateToUpdate.setStateName(stateDTO.getStateName());
			stateToUpdate.setStateCode(stateDTO.getStateCode());
			stateToUpdate.setZone(zoneData.get());
			
			State updated = stateRepository.save(stateToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a state with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/states/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getState(@PathVariable Long id) {
		LOG.info("getState request received@@   {}", id);
		Optional<State> stateData = stateRepository.findById(id);

		if (stateData.isPresent()) {
			State state = stateData.get();
			StateResponseDTO responseDTO = new StateResponseDTO();
			BeanUtils.copyProperties(state, responseDTO);
			responseDTO.add(linkTo(methodOn(LocationManagementController.class).getState(id)).withSelfRel());
			// responseDTO.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode,
			// id)).withRel("Delete - Delete operation"));
			// responseDTO.add(linkTo(methodOn(LocationManagementController.class).getZone(countryCode,
			// id)).withRel("Put - update operation"));

			
			responseDTO.setCountryCode(state.getCountry().getCountryCode());
			responseDTO.setZoneId(state.getZone().getId());
			responseDTO.setZoneName(state.getZone().getName());
			LOG.info("Found data against ID {} and returing response   {}", id, state);
			return new ResponseEntity<Object>(responseDTO, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type for a country", notes = "Returns all instances of the type for a country", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/states/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllState(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(required = false, defaultValue = "stateName") String[] orderBy) {
		LOG.info("getAllState request received");
		LOG.info("page {}", page);
		LOG.info("size {}", size);
		LOG.info("filter {}", filterByName);
		LOG.info("orderBy {}", orderBy);

		Direction sort = (sortDirection == null || sortDirection.isEmpty()) ? Direction.ASC
				: Direction.valueOf(sortDirection.toUpperCase());
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));

		Page<State> pagedResult = null;
		if (ObjectUtils.isEmpty(filterByName)) {
			pagedResult = stateRepository.findAll(paging);
		} else {
			pagedResult = stateRepository.findByStateNameContaining(filterByName, paging);
		}

		Map<String, Object> response = new HashMap<>();
		List<StateResponseDTO> stateDTOList = new ArrayList<>();

		if (pagedResult.hasContent()) {
			pagedResult.getContent().forEach(state -> {
				StateResponseDTO responseDTO = new StateResponseDTO();
				BeanUtils.copyProperties(state, responseDTO);
				responseDTO.add(
						linkTo(methodOn(LocationManagementController.class).getState(state.getId())).withSelfRel());
				responseDTO.setCountryCode(state.getCountry().getCountryCode());
				responseDTO.setZoneId(state.getZone().getId());
				responseDTO.setZoneName(state.getZone().getName());
				stateDTOList.add(responseDTO);

			});
			response.put("states", stateDTOList);
			response.put("currentPage", pagedResult.getNumber());
			response.put("totalElements", pagedResult.getTotalElements());
			response.put("totalPages", pagedResult.getTotalPages());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves states along with city name in key value pair", notes = "Retrieves states list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/states/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> stateDropdown() {
		LOG.info("citiesDropdown request received@@");
		List<Object[]> citiesData = stateRepository.dropDown();
		List<Map<Object,Object>> dropDowns = new ArrayList<>();
		citiesData.forEach(data -> {
			Map<Object,Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns,HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/states/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteState(@PathVariable String countryCode, @PathVariable Long id) {
		LOG.info("deleteCountry request received@@   {}", id);
		Country country = countryRepository.findByCountryCode(countryCode);
		stateRepository.deleteByCountryAndId(country, id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Zone Api
	// India is divided into four zones based on the lifestyle of people. These are
	// Western India, Eastern India, Northern India and Southern India
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/{countryCode}/zones", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
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
		zone.setStatus(zoneDetailDTO.getStatus());
		zone.setCountry(findByCountryCode);
		ZoneDetail saved = zoneDetailRepository.save(zone);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/zones/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateZone(@PathVariable Long id,
			@Valid @RequestBody ZoneDetailRequestDTO zoneDetailDTO) {
		LOG.info("updateZone requested record id {} ", id);
		LOG.info("updateZone request body@@   {}", zoneDetailDTO);

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(id);

		if (zoneData.isPresent()) {
			ZoneDetail zoneDetailToUpdate = zoneData.get();
			zoneDetailToUpdate.setName(zoneDetailDTO.getName());
			zoneDetailToUpdate.setStatus(zoneDetailDTO.getStatus());
			ZoneDetail updated = zoneDetailRepository.save(zoneDetailToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a zone with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zones/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getZone(@PathVariable Long id) {
		LOG.info("getZone request received@@id   {}", id);
		Optional<ZoneDetail> zoneDetailOption = zoneDetailRepository.findById(id);

		if (zoneDetailOption.isPresent()) {
			ZoneDetailResponseDTO dto = new ZoneDetailResponseDTO();
			ZoneDetail zoneDetail = zoneDetailOption.get();
			BeanUtils.copyProperties(zoneDetail, dto);
			dto.setStatus(zoneDetail.getStatus());
			// dto.add(linkTo(methodOn(LocationManagementController.class).deleteZone(id)).withRel("delete-zone"));
			dto.add(linkTo(methodOn(LocationManagementController.class).getZone(id)).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", id, zoneDetail);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zones", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllZone(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllZone request received");

		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;

		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ZoneDetailResponseDTO> allZonesDTO = new ArrayList<>();
		Page<ZoneDetail> pages = zoneDetailRepository.findAll(filterByName(criteria).and(filterByStatus(criteria)),
				paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ZoneDetailResponseDTO dto = new ZoneDetailResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(LocationManagementController.class).getZone(entity.getId())).withSelfRel());
				allZonesDTO.add(dto);
			});
		}
		response.put("zones", allZonesDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total zones count {}", allZonesDTO.size());

		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/zones/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteZone(@PathVariable Long id) {
		LOG.info("deleteZone request received@@   {}", id);
		zoneDetailRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves zones along with  name and id in key value pair", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zones/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> zonesDropdown() {
		LOG.info("zonesDropdown request received@@");
		List<Object[]> zoneData = zoneDetailRepository.dropDown();
		List<Map<Object,Object>> dropDowns = new ArrayList<>();
		zoneData.forEach(data -> {
			Map<Object,Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns,HttpStatus.OK);
	}

	// City API
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/cities", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addCity(@Valid @RequestBody CityRequestDTO cityDTO) {
		LOG.info("addCity request received@@   {}", cityDTO);

		City city = new City();
		BeanUtils.copyProperties(cityDTO, city);
		city.setStatus(cityDTO.getStatus());
		Optional<State> stateOptional = stateRepository.findById(cityDTO.getStateId());

		if (!stateOptional.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with stateId=" + cityDTO.getStateId()),
					HttpStatus.BAD_REQUEST);
		}
		city.setState(stateOptional.get());
		City saved = cityRepository.save(city);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/cities/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateCity(@PathVariable Long id, @Valid @RequestBody CityRequestDTO cityDTO) {
		LOG.info("updateCity request body@@   {}", cityDTO);
		Optional<City> cityOptionalData = cityRepository.findById(id);
		Optional<State> stateOptionalData = stateRepository.findById(cityDTO.getStateId());

		if (!cityOptionalData.isPresent()) {
			LOG.info("Did not find City by id   {}", id);
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a City by id=" + id),HttpStatus.BAD_REQUEST);
		}

		if (!stateOptionalData.isPresent()) {
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
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a city with id=" + id),HttpStatus.BAD_REQUEST);
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

	@ApiOperation(value = "Returns all instances of the type. It accepts zero-based page index and the size of the page to be returned ", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/cities/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllCities(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") String sortDirection,
			@RequestParam(required = false, defaultValue = "name") String[] orderBy) {
		LOG.info("getAllCities request received");
		LOG.info("page {}", page);
		LOG.info("size {}", size);
		LOG.info("filter {}", filterByName);
		LOG.info("orderBy {}", orderBy);

		Direction sort = (sortDirection == null || sortDirection.isEmpty()) ? Direction.ASC
				: Direction.valueOf(sortDirection.toUpperCase());
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));

		Page<City> pagedResult = null;
		if (ObjectUtils.isEmpty(filterByName)) {
			pagedResult = cityRepository.findAll(paging);
		} else {
			pagedResult = cityRepository.findByNameContaining(filterByName, paging);
		}
		Map<String, Object> response = new HashMap<>();
		List<CityResponseDTO> cityDTOList = new ArrayList<>();

		if (pagedResult.hasContent()) {
			pagedResult.getContent().forEach(city -> {
				CityResponseDTO dto = new CityResponseDTO();
				BeanUtils.copyProperties(city, dto);
				dto.setStateName(city.getState().getStateName());
				dto.setStateId(city.getState().getId());
				dto.add(linkTo(methodOn(LocationManagementController.class).getCity(city.getId())).withSelfRel());
				cityDTOList.add(dto);
			});
			response.put("cities", cityDTOList);
			response.put("currentPage", pagedResult.getNumber());
			response.put("totalElements", pagedResult.getTotalElements());
			response.put("totalPages", pagedResult.getTotalPages());
			return new ResponseEntity<Object>(response, HttpStatus.OK);
		}
		LOG.info("Did not find any records response {}", response);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "Delete an entity by its id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/cities/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCity(@PathVariable Long id) {
		LOG.info("deleteCity request received@@   {}", id);
		cityRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves cities along with city name in key value pair", notes = "Retrieves cities list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/cities/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> citiesDropdown() {
		LOG.info("citiesDropdown request received@@");
		List<Object[]> citiesData = cityRepository.dropDown();
		List<Map<Object,Object>> dropDowns = new ArrayList<>();
		citiesData.forEach(data -> {
			Map<Object,Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns,HttpStatus.OK);
	}

	// Zipcode
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
		zipcode.setCity(cityData.get());
		Zipcode saved = zipcodeRepository.save(zipcode);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
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
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Zipcode with code=" + zipcode),HttpStatus.BAD_REQUEST);
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
			dto.setCode(zipcodeEntity.getCode());
			dto.add(linkTo(methodOn(LocationManagementController.class).getZipcode(zipcode)).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", zipcode, zipcodeEntity);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", zipcode);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zipcodes/", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllZipcodes(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByCode,
			@RequestParam(required = false) String filterByCityName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllZipcodes request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByCode, filterByCityName, sortDirection,
				status);

		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ZipcodeResponseDTO> zipcodesLists = new ArrayList<>();

		Page<Zipcode> pages = zipcodeRepository
				.findAll(findZipcodeByCode(filterCriteria).and(findZipcodeByCityName(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(zipcode -> {
				ZipcodeResponseDTO dto = new ZipcodeResponseDTO();
				BeanUtils.copyProperties(zipcode, dto);
				dto.setCityId(zipcode.getCity().getId());
				dto.setCityName(zipcode.getCity().getName());
				dto.setStatus(zipcode.getStatus());
				dto.setCode(zipcode.getCode());
				dto.add(linkTo(methodOn(LocationManagementController.class).getZipcode(zipcode.getCode())).withSelfRel());
				zipcodesLists.add(dto);
			});
		}
		response.put("zipcodes", zipcodesLists);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total zipcode count {}", zipcodesLists.size());
		LOG.info("Did not find any records  getAllZipcodes {}", zipcodesLists);
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves zipcodes list along with city name", notes = "Retrieves zipcodes list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/zipcodes/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> zipcodeDropdown() {
		LOG.info("zipcodeDropdown request received@@");
		List<Object[]> zipcodeData = zipcodeRepository.dropDown();
		List<Map<Object,Object>> dropDowns = new ArrayList<>();
		zipcodeData.forEach(data -> {
			Map<Object,Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns,HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "If the entity is not found in the persistence store it is silently ignored", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/zipcodes/{zipcode}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteZipcode(@PathVariable Long zipcode) {
		LOG.info("deleteCity request received@@   {}", zipcode);
		Zipcode zipcodeEntity = zipcodeRepository.findByCode(zipcode);
		zipcodeRepository.delete(zipcodeEntity);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
