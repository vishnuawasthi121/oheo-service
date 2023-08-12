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

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.BuyRequestDTO;
import com.ogive.oheo.dto.BuyRequestResponseDTO;
import com.ogive.oheo.dto.ChargingPartDealerRequestDTO;
import com.ogive.oheo.dto.ChargingPartDealerResponseDTO;
import com.ogive.oheo.dto.ChargingSlotBookingRequestDTO;
import com.ogive.oheo.dto.ChargingSlotBookingResponseDTO;
import com.ogive.oheo.dto.ChargingStationRequestDTO;
import com.ogive.oheo.dto.ChargingStationResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.utils.CMSSpecifications;
import com.ogive.oheo.dto.utils.ChargingMngtSpec;
import com.ogive.oheo.persistence.entities.ChargingPartDealer;
import com.ogive.oheo.persistence.entities.ChargingProduct;
import com.ogive.oheo.persistence.entities.ChargingSlotBookingRequest;
import com.ogive.oheo.persistence.entities.ChargingStation;
import com.ogive.oheo.persistence.entities.ChargingStationBuyRequest;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Company;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;
import com.ogive.oheo.persistence.repo.ChargingPartDealerRepository;
import com.ogive.oheo.persistence.repo.ChargingProductRepository;
import com.ogive.oheo.persistence.repo.ChargingSlotBookingRequestRepository;
import com.ogive.oheo.persistence.repo.ChargingStationBuyRequestRepository;
import com.ogive.oheo.persistence.repo.ChargingStationRepository;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.CompanyRepository;
import com.ogive.oheo.persistence.repo.ProductRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.UserDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleBodyTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleFuelTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleModelRepository;
import com.ogive.oheo.persistence.repo.VehicleTypeRepository;
import com.ogive.oheo.persistence.repo.ZipcodeRepository;
import com.ogive.oheo.persistence.repo.ZoneDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Api(tags = "Charging Management")
@RestController

@RequestMapping("charging-management")
public class ChargeManagementController {

	private static final Logger LOG = LoggerFactory.getLogger(ChargeManagementController.class);
	
	@Autowired
	private ChargingPartDealerRepository chargingPartDealerRepository;
	
	@Autowired
	private ChargingStationRepository chargingStationRepository;
	
	@Autowired
	private StateRepository stateRepository;
	
	@Autowired
	private ZoneDetailRepository zoneDetailRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private ZipcodeRepository zipcodeRepository;
	
	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;
	
	@Autowired
	private ChargingProductRepository chargingProductRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Autowired
	private VehicleBodyTypeRepository vehicleBodyTypeRepository;

	@Autowired
	private VehicleFuelTypeRepository vehicleFuelTypeRepository;

	@Autowired
	private VehicleDetailRepository vehicleDetailRepository;

	@Autowired
	private VehicleModelRepository vehicleModelRepository;
	
	@Autowired
	private ChargingStationBuyRequestRepository chargingStationBuyRequestRepository;
	
	@Autowired
	private ChargingSlotBookingRequestRepository chargingSlotBookingRequestRepository;
	
	@Autowired
	private ProductRepository productRepository;
	
	@Value("${website.electric.vehicle.fuel-type}")
	private String electricVehicleFuelTypeName;

	// ChargingPartDealer - API
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/part-dealers", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addChargingPartDealer(@Valid @RequestBody ChargingPartDealerRequestDTO requestBody) {
		LOG.info("addChargingPartDealer request received@@   {}", requestBody);
		ChargingPartDealer entity = new ChargingPartDealer();
		BeanUtils.copyProperties(requestBody, entity);

		ChargingPartDealer savedEntity = chargingPartDealerRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity.getId());
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Updates a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/part-dealers/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateChargingPartDealer(@PathVariable Long id,
			@Valid @RequestBody ChargingPartDealerRequestDTO requestBody) {
		LOG.info("updateChargingPartDealer request received@@   {}", requestBody);
		Optional<ChargingPartDealer> entityData = chargingPartDealerRepository.findById(id);
		if (entityData.isPresent()) {
			ChargingPartDealer entity = entityData.get();
			BeanUtils.copyProperties(requestBody, entity);
			ChargingPartDealer savedEntity = chargingPartDealerRepository.save(entity);
			LOG.info("Saved @@   {}", savedEntity.getId());
			return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
		}

		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a ChargingPartDealer with id=" + id),
				HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Fetch entity by id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/part-dealers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getChargingPartDealer(@PathVariable Long id) {
		LOG.info("getChargingPartDealer request received@@   {}", id);
		Optional<ChargingPartDealer> entityData = chargingPartDealerRepository.findById(id);
		if (entityData.isPresent()) {
			ChargingPartDealerResponseDTO dto = new ChargingPartDealerResponseDTO();
			ChargingPartDealer entity = entityData.get();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(ChargeManagementController.class).getChargingPartDealer(entity.getId()))
					.withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/part-dealers", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllChargingPartDealer(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllChargingPartDealer request received");

		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ChargingPartDealerResponseDTO> dtoList = new ArrayList<>();

		Page<ChargingPartDealer> pages = chargingPartDealerRepository
				.findAll(ChargingMngtSpec.filterChargingPartDealerByName(filterCriteria).and(ChargingMngtSpec.filterChargingPartDealerByStatus(filterCriteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
											ChargingPartDealerResponseDTO dto = new ChargingPartDealerResponseDTO();
											BeanUtils.copyProperties(entity, dto);
											dto.add(linkTo(methodOn(ChargeManagementController.class).getChargingPartDealer(entity.getId())).withSelfRel());
											dtoList.add(dto);
			});
		}
		response.put("dealers", dtoList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total cities count {}", dtoList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/part-dealers/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteChargingPartDealer(@PathVariable Long id) {
		LOG.info("deleteChargingPartDealer request received @@   {}", id);
		chargingPartDealerRepository.deleteById(id);
		LOG.info("Deleted ChargingPartDealer successfully by id {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	//Charging Station API
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/charging-stations", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addChargingStation(@Valid @RequestBody ChargingStationRequestDTO requestBody) {
		LOG.info("addChargingStation request received@@   {}", requestBody);
		ChargingStation entity = new ChargingStation();
		BeanUtils.copyProperties(requestBody, entity);
		// This all information could be fetched by just using zipcode.
		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(requestBody.getZoneId());

		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ZoneDetail with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}
		Optional<State> stateData = stateRepository.findById(requestBody.getStateId());
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with id=" + requestBody.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(requestBody.getCityId());

		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with id=" + requestBody.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(requestBody.getZipcode());
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Zipcode with zipcode=" + requestBody.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(requestBody.getVehicleTypeId());

		if (!vehicleTypeData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a VehicleType with id=" + requestBody.getVehicleTypeId()),
					HttpStatus.BAD_REQUEST);
		}
		
		Optional<UserDetail> userData = userDetailRepository.findById(requestBody.getDealerId());
		if (!userData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserDetail with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setUserDetail(userData.get());
		
		entity.setZone(zoneData.get());
		entity.setState(stateData.get());
		entity.setCity(cityData.get());
		entity.setZipcode(zipcode);
		entity.setVehicleType(vehicleTypeData.get());

		ChargingStation savedEntity = chargingStationRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity.getId());
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/charging-stations/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateChargingStation(@PathVariable Long id,@Valid @RequestBody ChargingStationRequestDTO requestBody) {
		LOG.info("addChargingStation request received@@   {}", requestBody);
		Optional<ChargingStation> entityData = chargingStationRepository.findById(id);
		
		if (!entityData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a ChargingStation with id=" + id),
					HttpStatus.BAD_REQUEST);
		}
		ChargingStation entity = entityData.get();

		BeanUtils.copyProperties(requestBody, entity);
		// This all information could be fetched by just using zipcode.
		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(requestBody.getZoneId());

		if (!zoneData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ZoneDetail with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}
		Optional<State> stateData = stateRepository.findById(requestBody.getStateId());
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with id=" + requestBody.getStateId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<City> cityData = cityRepository.findById(requestBody.getCityId());

		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with id=" + requestBody.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(requestBody.getZipcode());
		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Zipcode with zipcode=" + requestBody.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(requestBody.getVehicleTypeId());

		if (!vehicleTypeData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a VehicleType with id=" + requestBody.getVehicleTypeId()),
					HttpStatus.BAD_REQUEST);
		}
		Optional<UserDetail> userData = userDetailRepository.findById(requestBody.getDealerId());
		if (!userData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserDetail with id=" + requestBody.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setUserDetail(userData.get());
		
		entity.setZone(zoneData.get());
		entity.setState(stateData.get());
		entity.setCity(cityData.get());
		entity.setZipcode(zipcode);
		entity.setVehicleType(vehicleTypeData.get());
		ChargingStation savedEntity = chargingStationRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity.getId());
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Fetch entity by id", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-stations/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getChargingStation(@PathVariable Long id) {
		LOG.info("getChargingStation request received@@   {}", id);
		Optional<ChargingStation> entityData = chargingStationRepository.findById(id);
		if (entityData.isPresent()) {
			ChargingStationResponseDTO dto = new ChargingStationResponseDTO();
			ChargingStation entity = entityData.get();
			BeanUtils.copyProperties(entity, dto);
			String vehicleType =	entity.getVehicleType() == null ? "" :  entity.getVehicleType().getName();
			dto.setVehicleType(vehicleType);
			String cityName= entity.getCity() == null ?      "": entity.getCity().getName();
			dto.setCityName(cityName);
			
			String stateName  =  entity.getState() == null ? "" : entity.getState().getStateName(); 
			dto.setStateName(stateName);
			
			String zoneName  =  entity.getZone() == null ?   "" : entity.getZone().getName();
			dto.setZoneName(zoneName);
			
			Long zipcode  =  entity.getZipcode() == null ?   null : entity.getZipcode().getCode();
			dto.setZipcode(zipcode);
			
			String dealerName  =  entity.getUserDetail() == null?   "":  entity.getUserDetail().getName() ;
			dto.setDealerName(dealerName);
			
			dto.add(linkTo(methodOn(ChargeManagementController.class).getChargingStation(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-stations", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllChargingStation(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllChargingStation request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ChargingStationResponseDTO> dtoList = new ArrayList<>();
		Page<ChargingStation> pages = chargingStationRepository
				.findAll(ChargingMngtSpec.filterChargingStationByName(filterCriteria).and(ChargingMngtSpec.filterChargingStationByStatus(filterCriteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ChargingStationResponseDTO dto = new ChargingStationResponseDTO();
											BeanUtils.copyProperties(entity, dto);
											String vehicleType =	entity.getVehicleType() == null ? "" :  entity.getVehicleType().getName();
											dto.setVehicleType(vehicleType);
											String cityName= entity.getCity() == null ?      "": entity.getCity().getName();
											dto.setCityName(cityName);
											
											String stateName  =  entity.getState() == null ? "" : entity.getState().getStateName(); 
											dto.setStateName(stateName);
											
											String zoneName  =  entity.getZone() == null ?   "" : entity.getZone().getName();
											dto.setZoneName(zoneName);
											
											Long zipcode  =  entity.getZipcode() == null ?   null : entity.getZipcode().getCode();
											dto.setZipcode(zipcode);
											
											String dealerName  =  entity.getUserDetail() == null?   "":  entity.getUserDetail().getName() ;
											dto.setDealerName(dealerName);
											
											dto.add(linkTo(methodOn(ChargeManagementController.class).getChargingStation(entity.getId())).withSelfRel());
											dtoList.add(dto);
			});
		}
		response.put("stations", dtoList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total cities count {}", dtoList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/charging-stations/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteChargingStation(@PathVariable Long id) {
		LOG.info("deleteChargingStation request received @@   {}", id);
		chargingStationRepository.deleteById(id);
		LOG.info("Deleted ChargingStation successfully by id {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	
	
	// ###################################  CHARGING - BUY REQUEST ################################
	//Charging station buy request
	//Buy Request Api
	@Tag(name = "Buy Request - Charging")
	@Transactional
	@ApiOperation(value = "Create Buy request for a charging product.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/charging-stations/buy-requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addBuyRequest(@Valid @RequestBody BuyRequestDTO buyRequest) {
		LOG.info("addBuyRequest request received@@   {}", buyRequest);
		//ChargingStationBuyRequest Entity
		ChargingStationBuyRequest entity = new ChargingStationBuyRequest();
		BeanUtils.copyProperties(buyRequest, entity);

		Optional<ChargingProduct> productData = chargingProductRepository.findById(buyRequest.getProductId());
		if (!productData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Product with id=" + buyRequest.getProductId()),
					HttpStatus.BAD_REQUEST);
		}

		// userRepository.findById(buyRequest.getDealerId());
		// Dealer/Distributor
		Optional<ChargingPartDealer> chargingPartDealerData = chargingPartDealerRepository.findById(buyRequest.getDealerId());
		if (!chargingPartDealerData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ChargingPartDealer with id=" + buyRequest.getDealerId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setChargingPartDealer(chargingPartDealerData.get());
		
		// City
		Optional<City> cityData = cityRepository.findById(buyRequest.getCityId());
		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a City with id=" + buyRequest.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		// Company
		Optional<Company> companyData = companyRepository.findById(buyRequest.getCompanyId());
		if (!companyData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Company with id=" + buyRequest.getCompanyId()),
					HttpStatus.BAD_REQUEST);
		}

		// State
		Optional<State> stateData = stateRepository.findById(buyRequest.getStateId());
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a State with id=" + buyRequest.getStateId()),
					HttpStatus.BAD_REQUEST);
		}
		// Vehicle Model
		Optional<VehicleModel> modelData = vehicleModelRepository.findById(buyRequest.getModelId());
		if (!modelData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a VehicleModel with id=" + buyRequest.getModelId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setCity(cityData.get());
		entity.setState(stateData.get());
		entity.setCompany(companyData.get());
		entity.setModel(modelData.get());
		entity.setChargingProduct(productData.get());

		ChargingStationBuyRequest savedEntity = chargingStationBuyRequestRepository.save(entity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	
	
	@Tag(name = "Buy Request - Charging")
	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-stations/buy-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getBuyRequest(@PathVariable Long id) {
		LOG.info("getBuyRequest request received@@   {}", id);
		Optional<ChargingStationBuyRequest> entityData = chargingStationBuyRequestRepository.findById(id);
		if (entityData.isPresent()) {
			ChargingStationBuyRequest entity = entityData.get();
			BuyRequestResponseDTO dto = new BuyRequestResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			// Requested City
			dto.setRequestedCityId(entity.getCity().getId());
			dto.setRequestedCityName(entity.getCity().getName());
			// State
			dto.setRequestedStateId(entity.getState().getId());
			dto.setRequestedStateName(entity.getState().getStateName());
			// Model
			dto.setModelId(entity.getModel().getId());
			dto.setModelName(entity.getModel().getModelName());
			// Company
			dto.setCompanyId(entity.getCompany().getId());
			dto.setCompanyName(entity.getCompany().getCompanyName());
			// ChargingPartDealer Mapping
			if (Objects.nonNull(entity.getChargingPartDealer())) {
				dto.setDealerName(entity.getChargingPartDealer().getName());
				dto.setDealerId(entity.getChargingPartDealer().getId());
			}
			if (Objects.nonNull(entity.getChargingProduct())) {
				dto.setProductId(entity.getChargingProduct().getId());
				dto.setProductName(entity.getChargingProduct().getName());
			}
			dto.add(linkTo(methodOn(ChargeManagementController.class).getBuyRequest(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Tag(name = "Buy Request - Charging")
	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-stations/buy-requests", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllBuyRequest(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, @RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) String email) {
		LOG.info("getAllVehicleDetails request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, null);
		filterCriteria.setEmail(email);

		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<BuyRequestResponseDTO> buyRequestDTOList = new ArrayList<>();

		Page<ChargingStationBuyRequest> pages = chargingStationBuyRequestRepository
				.findAll(CMSSpecifications.filterChargingStationBuyRequestByEmail(filterCriteria)
						.and(CMSSpecifications.filterChargingStationBuyRequestByName(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				BuyRequestResponseDTO dto = new BuyRequestResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				// Requested City
				dto.setRequestedCityId(entity.getCity().getId());
				dto.setRequestedCityName(entity.getCity().getName());
				// State
				dto.setRequestedStateId(entity.getState().getId());
				dto.setRequestedStateName(entity.getState().getStateName());
				// Model
				dto.setModelId(entity.getModel().getId());
				dto.setModelName(entity.getModel().getModelName());
				// Company
				dto.setCompanyId(entity.getCompany().getId());
				dto.setCompanyName(entity.getCompany().getCompanyName());
				// ChargingPartDealer Mapping
				if (Objects.nonNull(entity.getChargingPartDealer())) {
					dto.setDealerName(entity.getChargingPartDealer().getName());
					dto.setDealerId(entity.getChargingPartDealer().getId());
				}
				if (Objects.nonNull(entity.getChargingProduct())) {
					dto.setProductId(entity.getChargingProduct().getId());
					dto.setProductName(entity.getChargingProduct().getName());
				}

				dto.add(linkTo(methodOn(ChargeManagementController.class).getBuyRequest(entity.getId())).withSelfRel());
				buyRequestDTOList.add(dto);

			});
		}
		response.put("buyRequests", buyRequestDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@Tag(name = "Buy Request - Charging")
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/charging-stations/buy-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteBuyRequest(@PathVariable Long id) {
		LOG.info("deleteBuyRequest request received @@   {}", id);
		chargingStationBuyRequestRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@Tag(name = "Buy Request - Charging",description = "These are ChargingPartDealer records")
	@ApiOperation(value = "Retrieves all parts dealer along with id,name in key value pair", notes = "Retrieves cities list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/part-dealers/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> partDealersDropdown() {
		LOG.info("citiesDropdown request received@@");
		List<Object[]> dealersData = chargingPartDealerRepository.dropDown();
		List<Map<Object, Object>> dropDowns = new ArrayList<>();
		dealersData.forEach(data -> {
			Map<Object, Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns, HttpStatus.OK);
	}
	
	//BOOK YOUR CHARGING SLOT
	//Web site -> Charging -> BOOK YOUR CHARGING SLOT
	
	@Tag(name = "Charging Management - Book your charging slot")
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/charging-requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addChargingRequest(@Valid @RequestBody ChargingSlotBookingRequestDTO requestBody) {
		LOG.info("addChargingRequest request received@@   {}", requestBody);
		ChargingSlotBookingRequest entity = new ChargingSlotBookingRequest();
		BeanUtils.copyProperties(requestBody, entity);
		Optional<ChargingStation> chargingStationData = chargingStationRepository.findById(requestBody.getChargingStationId());
				
		if (!chargingStationData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO(
							"Did not find a ChargingStation with id=" + requestBody.getChargingStationId()),HttpStatus.BAD_REQUEST);
		}
		entity.setChargingStation(chargingStationData.get());
		ChargingSlotBookingRequest savedEntity = chargingSlotBookingRequestRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity.getId());
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@Tag(name = "Charging Management - Book your charging slot")
	@ApiOperation(value = "EV live product dropdown by Vehicle Type like Two Wheelers, Three Wheelers etc", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live/ev/{vehicleTypeId}/dropdown", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getProductDropdownByvVehicleType(@PathVariable Long vehicleTypeId) {
		List<Object[]> productData = productRepository.fetchEVProductsDropDown(electricVehicleFuelTypeName,
				vehicleTypeId);
		List<Map<Object, Object>> dropDowns = new ArrayList<>();
		productData.forEach(data -> {
			Map<Object, Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns, HttpStatus.OK);
	}
	
	@Tag(name = "Charging Management - Manage booking request")
	@ApiOperation(value = "Accept charging request", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/charging-requests/{id}/accept",  produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> acceptChargingRequest(@PathVariable Long id) {
		LOG.info("acceptChargingRequest received@@   {}", id);
		Optional<ChargingSlotBookingRequest> data = chargingSlotBookingRequestRepository.findById(id);
		if (!data.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ChargingSlotBookingRequest with id=" + id),
					HttpStatus.BAD_REQUEST);
		}
		ChargingSlotBookingRequest entity = data.get();
		entity.setIsAccepted("Y");
		ChargingSlotBookingRequest updatedEntity = chargingSlotBookingRequestRepository.save(entity);
		LOG.info("Saved @@   {}", updatedEntity.getId());
		return new ResponseEntity<Object>(updatedEntity.getId(), HttpStatus.OK);
	}
	
	@Tag(name = "Charging Management - Manage booking request")
	@ApiOperation(value = "reject charging request", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/charging-requests/{id}/reject",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> rejectChargingRequest(@PathVariable Long id) {
		LOG.info("acceptChargingRequest received@@   {}", id);
		Optional<ChargingSlotBookingRequest> data = chargingSlotBookingRequestRepository.findById(id);
		if (!data.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a ChargingSlotBookingRequest with id=" + id),
					HttpStatus.BAD_REQUEST);
		}
		ChargingSlotBookingRequest entity = data.get();
		entity.setIsAccepted("N");
		ChargingSlotBookingRequest updatedEntity = chargingSlotBookingRequestRepository.save(entity);
		LOG.info("Saved @@   {}", updatedEntity.getId());
		return new ResponseEntity<Object>(updatedEntity.getId(), HttpStatus.OK);
	}
	
	
	
	@Tag(name = "Charging Management - Manage booking request")
	@ApiOperation(value = "Accept charging request", produces = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-requests", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getAllChargingRequest() {
		LOG.info("acceptChargingRequest received@@");
		Iterable<ChargingSlotBookingRequest> allData = chargingSlotBookingRequestRepository.findAll();
		List<ChargingSlotBookingResponseDTO> allDTO = new ArrayList<>();
		allData.forEach(entity -> {
			ChargingSlotBookingResponseDTO dto = new ChargingSlotBookingResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			if (entity.getChargingStation() != null) {
				dto.setChargingStation(entity.getChargingStation().getName());
			}
			dto.add(linkTo(methodOn(ChargeManagementController.class).acceptChargingRequest(entity.getId())).withRel("Accept"));
			dto.add(linkTo(methodOn(ChargeManagementController.class).rejectChargingRequest(entity.getId())).withRel("Reject"));
			dto.add(linkTo(methodOn(ChargeManagementController.class).deleteChargingRequest(entity.getId())).withRel("Delete"));
			allDTO.add(dto);
		});
		return new ResponseEntity<Object>(allDTO, HttpStatus.OK);
	}

	@Tag(name = "Charging Management - Manage booking request")
	@ApiOperation(value = "Delete the charging request", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/charging-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteChargingRequest(@PathVariable Long id) {
		LOG.info("deleteChargingRequest received @@   {}", id);
		chargingSlotBookingRequestRepository.deleteById(id);
		LOG.info("Deleted ChargingSlotBookingRequest successfully by id {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	
	
}
