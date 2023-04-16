package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static com.ogive.oheo.dto.utils.GeographicLocationSpecifications.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

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
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.CompanyRequestDTO;
import com.ogive.oheo.dto.CompanyResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.PurchaseTypeRequestDTO;
import com.ogive.oheo.dto.PurchaseTypeResponseDTO;
import com.ogive.oheo.dto.VehicleBodyTypeRequestDTO;
import com.ogive.oheo.dto.VehicleBodyTypeResponseDTO;
import com.ogive.oheo.dto.VehicleDetailRequestDTO;
import com.ogive.oheo.dto.VehicleDetailResponseDTO;
import com.ogive.oheo.dto.VehicleFuelTypeRequestDTO;
import com.ogive.oheo.dto.VehicleFuelTypeResponseDTO;
import com.ogive.oheo.dto.VehicleModelRequestDTO;
import com.ogive.oheo.dto.VehicleModelResponseDTO;
import com.ogive.oheo.dto.VehicleTransmissionRequestDTO;
import com.ogive.oheo.dto.VehicleTransmissionResponseDTO;
import com.ogive.oheo.dto.VehicleTypeRequestDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.dto.ZipcodeResponseDTO;
import com.ogive.oheo.dto.utils.CommonsUtil;
import com.ogive.oheo.persistence.entities.Address;
import com.ogive.oheo.persistence.entities.AddressRequestDTO;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Company;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.PurchaseType;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleTransmission;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;
import com.ogive.oheo.persistence.repo.AddressRepository;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.CompanyRepository;
import com.ogive.oheo.persistence.repo.ImagesRepository;
import com.ogive.oheo.persistence.repo.PurchaseTypeRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.VehicleBodyTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleFuelTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleModelRepository;
import com.ogive.oheo.persistence.repo.VehicleTransmissionRepository;
import com.ogive.oheo.persistence.repo.VehicleTypeRepository;
import com.ogive.oheo.persistence.repo.ZipcodeRepository;
import com.ogive.oheo.persistence.repo.ZoneDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Vehicle Setup")
@RestController
@RequestMapping("/vehicle-setup")
public class VehicleSetupController {

	private static final Logger LOG = LoggerFactory.getLogger(VehicleSetupController.class);

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	@Autowired
	private VehicleFuelTypeRepository vehicleFuelTypeRepository;

	@Autowired
	private VehicleBodyTypeRepository vehicleBodyTypeRepository;

	@Autowired
	private PurchaseTypeRepository purchaseTypeRepository;

	@Autowired
	private VehicleTransmissionRepository vehicleTransmissionRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private ImagesRepository imagesRepository;

	@Autowired
	private VehicleDetailRepository vehicleDetailRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private ZoneDetailRepository zoneDetailRepository;

	@Autowired
	private ZipcodeRepository zipcodeRepository;

	// Vehicle company api
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-companies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleCompany(@Valid @RequestBody CompanyRequestDTO requestBody) {
		LOG.info("addVehicleCompany request received@@   {}", requestBody);
		Company entity = new Company();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		Company saved = companyRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-companies/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleCompany(@PathVariable Long id,
			@Valid @RequestBody CompanyRequestDTO requestBody) {
		LOG.info("updateVehicleCompany requested record id {} ", id);
		LOG.info("updateVehicleCompany request body@@   {}", requestBody);

		Optional<Company> data = companyRepository.findById(id);
		if (data.isPresent()) {
			Company entityToUpdate = data.get();
			entityToUpdate.setCompanyName(requestBody.getCompanyName());
			entityToUpdate.setStatus(requestBody.getStatus());
			Company updated = companyRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Company with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-companies/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleCompany(@PathVariable Long id) {
		LOG.info("getVehicleCompany request received@@   {}", id);
		Optional<Company> fetchedData = companyRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			Company entity = fetchedData.get();
			CompanyResponseDTO dto = new CompanyResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());
			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleCompany(entity.getId())).withSelfRel());

			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-companies", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleCompanies(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getVehicleCompanies request received");
		
		FilterCriteria filterCriteria = new  FilterCriteria(page,size,filterByName,sortDirection,orderBy,status);
		LOG.info("filterCriteria    {} ",filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<CompanyResponseDTO> companiesDTOList = new ArrayList<>();
		
		Page<Company> pages = companyRepository.findAll(filterCompanyByCompanyName(filterCriteria).and(filterCompanyByStatus(filterCriteria)),paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				CompanyResponseDTO dto = new CompanyResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleCompany(entity.getId())).withSelfRel());
				companiesDTOList.add(dto);
			});
		}
		response.put("cities", companiesDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total cities count {}", companiesDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-companies/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteCompany(@PathVariable Long id) {
		LOG.info("deleteCompany request received @@   {}", id);
		companyRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle Model API
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-models", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleModel(@Valid @RequestBody VehicleModelRequestDTO requestBody) {
		LOG.info("addVehicleModel request received@@   {}", requestBody);
		VehicleModel entity = new VehicleModel();
		
		Optional<Company> companyData = companyRepository.findById(requestBody.getCompanyId());

		if (companyData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Company with id=" + requestBody.getCompanyId()),
					HttpStatus.BAD_REQUEST);
		}

		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		entity.setCompany(companyData.get());

		VehicleModel saved = vehicleModelRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-models/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleModel(@PathVariable Long id,
			@Valid @RequestBody VehicleModelRequestDTO requestBody) {
		LOG.info("updateVehicleModel requested record id {} ", id);
		LOG.info("updateVehicleModel request body@@   {}", requestBody);
		// TODO - Need to pull the company details

		Optional<Company> companyData = companyRepository.findById(requestBody.getCompanyId());

		if (companyData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Company with id=" + requestBody.getCompanyId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<VehicleModel> vehicleModelData = vehicleModelRepository.findById(id);

		if (vehicleModelData.isPresent()) {
			VehicleModel entityToUpdate = vehicleModelData.get();
			entityToUpdate.setModelName(requestBody.getModelName());
			entityToUpdate.setStatus(requestBody.getStatus());

			entityToUpdate.setCompany(companyData.get());
			VehicleModel updated = vehicleModelRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleModel with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-models/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleModel(@PathVariable Long id) {
		LOG.info("getVehicleModel request received@@   {}", id);
		Optional<VehicleModel> fetchedData = vehicleModelRepository.findById(id);

		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleModel entity = fetchedData.get();
			VehicleModelResponseDTO dto = new VehicleModelResponseDTO();
			BeanUtils.copyProperties(entity, dto);

			dto.setStatus(entity.getStatus());
			dto.setCompanyName(entity.getCompany().getCompanyName());
			dto.setCompanyId(entity.getCompany().getId());

			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleModel(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-models", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleModels(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getVehicleModels request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleModelResponseDTO> allVehicleModelDTOList = new ArrayList<>();
		Page<VehicleModel> pages = vehicleModelRepository.findAll(filterVehicleModelByModelName(filterCriteria).and(filterVehicleModelByStatus(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleModelResponseDTO dto = new VehicleModelResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.setCompanyName(entity.getCompany().getCompanyName());
				dto.setCompanyId(entity.getCompany().getId());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleModel(entity.getId())).withSelfRel());
				allVehicleModelDTOList.add(dto);
			});
		}
		response.put("models", allVehicleModelDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total zipcode count {}", allVehicleModelDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-models/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleModel(@PathVariable Long id) {
		LOG.info("deleteVehicleModel request received @@   {}", id);
		vehicleModelRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle Type : VehicleTypeRequestDTO
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-types", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleTypes(@Valid @RequestBody VehicleTypeRequestDTO requestBody) {
		LOG.info("addVehicleTypes request received@@   {}", requestBody);
		VehicleType entity = new VehicleType();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		VehicleType saved = vehicleTypeRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-types/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleTypes(@PathVariable Long id,
			@Valid @RequestBody VehicleTypeRequestDTO requestBody) {
		LOG.info("updateVehicleTypes requested record id {} ", id);
		LOG.info("updateVehicleTypes request body@@   {}", requestBody);

		Optional<VehicleType> data = vehicleTypeRepository.findById(id);
		if (data.isPresent()) {

			VehicleType entityToUpdate = data.get();
			entityToUpdate.setName(requestBody.getName());
			entityToUpdate.setStatus(requestBody.getStatus());
			VehicleType updated = vehicleTypeRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleType with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleType(@PathVariable Long id) {
		LOG.info("getVehicleType request received@@   {}", id);
		Optional<VehicleType> fetchedData = vehicleTypeRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleType entity = fetchedData.get();
			VehicleTypeResponseDTO dto = new VehicleTypeResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());

			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleType(entity.getId())).withSelfRel());

			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-types", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleTypees(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getVehicleTypees request received");

		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleTypeResponseDTO> vehicleTypeDTOList = new ArrayList<>();
		Page<VehicleType> pages = vehicleTypeRepository.findAll(
				filterVehicleTypeByName(filterCriteria).and(filterVehicleTypeByStatus(filterCriteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleTypeResponseDTO dto = new VehicleTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleType(entity.getId())).withSelfRel());
				vehicleTypeDTOList.add(dto);
			});
		}
		response.put("types", vehicleTypeDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total vehicleTypeDTOList count {}", vehicleTypeDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleType(@PathVariable Long id) {
		LOG.info("deleteVehicleType request received @@   {}", id);
		vehicleTypeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle Fuel Type
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-fuel-types", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleFuelType(@Valid @RequestBody VehicleFuelTypeRequestDTO requestBody) {
		LOG.info("addVehicleFuelType request received@@   {}", requestBody);
		VehicleFuelType entity = new VehicleFuelType();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		VehicleFuelType saved = vehicleFuelTypeRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-fuel-types/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleFuelType(@PathVariable Long id,
			@Valid @RequestBody VehicleFuelTypeRequestDTO requestBody) {
		LOG.info("updateVehicleFuelType requested record id {} ", id);
		LOG.info("updateVehicleFuelType request body@@   {}", requestBody);

		Optional<VehicleFuelType> data = vehicleFuelTypeRepository.findById(id);

		if (data.isPresent()) {

			VehicleFuelType entityToUpdate = data.get();
			entityToUpdate.setName(requestBody.getName());
			entityToUpdate.setStatus(requestBody.getStatus());
			VehicleFuelType updated = vehicleFuelTypeRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleFuelType with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-fuel-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleFuelType(@PathVariable Long id) {
		LOG.info("getVehicleFuelType request received@@   {}", id);
		Optional<VehicleFuelType> fetchedData = vehicleFuelTypeRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleFuelType entity = fetchedData.get();
			VehicleFuelTypeResponseDTO dto = new VehicleFuelTypeResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());
			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleFuelType(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-fuel-types", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllVehicleFuelTypes(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllVehicleFuelTypes request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);

		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleFuelTypeResponseDTO> allFuelTypesDTOList = new ArrayList<>();
		Page<VehicleFuelType> pages = vehicleFuelTypeRepository.findAll(filterVehicleFuelTypeByName(filterCriteria).and(filterVehicleFuelTypeByStatus(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleFuelTypeResponseDTO dto = new VehicleFuelTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleFuelType(entity.getId())).withSelfRel());
				allFuelTypesDTOList.add(dto);
			});
		}
		response.put("fuelTypes", allFuelTypesDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total allFuelTypesDTOList count {}", allFuelTypesDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-fuel-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleFuelTypel(@PathVariable Long id) {
		LOG.info("deleteVehicleModel request received @@   {}", id);
		vehicleFuelTypeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle body type
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-body-types", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleBodyType(@Valid @RequestBody VehicleBodyTypeRequestDTO requestBody) {
		LOG.info("addVehicleBodyType request received@@   {}", requestBody);
		VehicleBodyType entity = new VehicleBodyType();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		VehicleBodyType saved = vehicleBodyTypeRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-body-types/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleBodyType(@PathVariable Long id,
			@Valid @RequestBody VehicleBodyTypeRequestDTO requestBody) {
		LOG.info("updateVehicleBodyType requested record id {} ", id);
		LOG.info("updateVehicleBodyType request body@@   {}", requestBody);

		Optional<VehicleBodyType> data = vehicleBodyTypeRepository.findById(id);

		if (data.isPresent()) {

			VehicleBodyType entityToUpdate = data.get();
			entityToUpdate.setName(requestBody.getName());
			entityToUpdate.setStatus(requestBody.getStatus());
			VehicleBodyType updated = vehicleBodyTypeRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleBodyType with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-body-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleBodyType(@PathVariable Long id) {
		LOG.info("getVehicleBodyType request received@@   {}", id);
		Optional<VehicleBodyType> fetchedData = vehicleBodyTypeRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleBodyType entity = fetchedData.get();
			VehicleBodyTypeResponseDTO dto = new VehicleBodyTypeResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());
			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleBodyType(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-body-types", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllVehicleBodyTypes(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllVehicleBodyTypes request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleBodyTypeResponseDTO> allBodyTypesDTOList = new ArrayList<>();
		
		Page<VehicleBodyType> pages = vehicleBodyTypeRepository.findAll(
				filterVehicleBodyTypeByName(filterCriteria).and(filterVehicleBodyTypeByStatus(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleBodyTypeResponseDTO dto = new VehicleBodyTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleBodyType(entity.getId()))
						.withSelfRel());
				allBodyTypesDTOList.add(dto);
			});
		}
		response.put("bodyTypes", allBodyTypesDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total allFuelTypesDTOList count {}", allBodyTypesDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-body-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleBodyTypel(@PathVariable Long id) {
		LOG.info("deleteVehicleModel request received @@   {}", id);
		vehicleBodyTypeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Purchase Type
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/purchase-types", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addPurchaseType(@Valid @RequestBody PurchaseTypeRequestDTO requestBody) {
		LOG.info("addPurchaseType request received@@   {}", requestBody);
		PurchaseType entity = new PurchaseType();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		PurchaseType saved = purchaseTypeRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/purchase-types/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePurchaseType(@PathVariable Long id,
			@Valid @RequestBody PurchaseTypeRequestDTO requestBody) {
		LOG.info("updatePurchaseType requested record id {} ", id);
		LOG.info("updatePurchaseType request body@@   {}", requestBody);

		Optional<PurchaseType> data = purchaseTypeRepository.findById(id);

		if (data.isPresent()) {

			PurchaseType entityToUpdate = data.get();
			entityToUpdate.setName(requestBody.getName());
			entityToUpdate.setStatus(requestBody.getStatus());
			PurchaseType updated = purchaseTypeRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a PurchaseType with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/purchase-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getPurchaseType(@PathVariable Long id) {
		LOG.info("getPurchaseType request received@@   {}", id);
		Optional<PurchaseType> fetchedData = purchaseTypeRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			PurchaseType entity = fetchedData.get();
			PurchaseTypeResponseDTO dto = new PurchaseTypeResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());
			dto.add(linkTo(methodOn(VehicleSetupController.class).getPurchaseType(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/purchase-types", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllPurchaseType(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllPurchaseType request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);

		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<PurchaseTypeResponseDTO> allPurchaseTypeDTO = new ArrayList<>();
		Page<PurchaseType> pages = purchaseTypeRepository.findAll(filterPurchaseTypeByName(filterCriteria).and(filterPurchaseTypeByStatus(filterCriteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				PurchaseTypeResponseDTO dto = new PurchaseTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getPurchaseType(entity.getId())).withSelfRel());
				allPurchaseTypeDTO.add(dto);
			});
		}
		response.put("purchaseTypes", allPurchaseTypeDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());

		LOG.info("Total allPurchaseTypeDTO count {}", allPurchaseTypeDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/purchase-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deletePurchaseType(@PathVariable Long id) {
		LOG.info("deletePurchaseType request received @@   {}", id);
		purchaseTypeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	//Vehicle Transmission
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-transmissions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleTransmission(
			@Valid @RequestBody VehicleTransmissionRequestDTO requestBody) {
		LOG.info("addVehicleTransmission request received@@   {}", requestBody);
		VehicleTransmission entity = new VehicleTransmission();
		BeanUtils.copyProperties(requestBody, entity);
		entity.setStatus(requestBody.getStatus());
		VehicleTransmission saved = vehicleTransmissionRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@PutMapping(path = "/vehicle-transmissions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleTransmission(@PathVariable Long id,
			@Valid @RequestBody VehicleTransmissionRequestDTO requestBody) {
		LOG.info("updateVehicleTransmission requested record id {} ", id);
		LOG.info("updateVehicleTransmission request body@@   {}", requestBody);

		Optional<VehicleTransmission> data = vehicleTransmissionRepository.findById(id);

		if (data.isPresent()) {

			VehicleTransmission entityToUpdate = data.get();
			entityToUpdate.setName(requestBody.getName());
			entityToUpdate.setStatus(requestBody.getStatus());
			VehicleTransmission updated = vehicleTransmissionRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updated, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleTransmission with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-transmissions/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleTransmission(@PathVariable Long id) {
		LOG.info("getVehicleTransmission request received@@   {}", id);
		Optional<VehicleTransmission> fetchedData = vehicleTransmissionRepository.findById(id);
		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleTransmission entity = fetchedData.get();
			VehicleTransmissionResponseDTO dto = new VehicleTransmissionResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.setStatus(entity.getStatus());
			dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleTransmission(entity.getId()))
					.withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-transmissions", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllVehicleTransmission(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllVehicleTransmission request received");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleTransmissionResponseDTO> allTransmissionDTOList = new ArrayList<>();
		
		Page<VehicleTransmission> pages = vehicleTransmissionRepository.findAll(
				filterVehicleTransmissionByName(filterCriteria).and(filterVehicleTransmissionByStatus(filterCriteria)),
				paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleTransmissionResponseDTO dto = new VehicleTransmissionResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleTransmission(entity.getId())).withSelfRel());
				allTransmissionDTOList.add(dto);
			});
		}
		response.put("transmissions", allTransmissionDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total allTransmissionDTOList count {}", allTransmissionDTOList.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-transmissions/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleTransmission(@PathVariable Long id) {
		LOG.info("deleteVehicleTransmission request received @@   {}", id);
		vehicleTransmissionRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle API
	@Transactional
	// All Vehicle -> Add Vehicle : Vehicle API -
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-details", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleDetails(@ModelAttribute VehicleDetailRequestDTO requestBody) {
		LOG.info("addVehicleDetails request received@@   {}", requestBody);
		// TODO - Logic to save Vehicle details and file into database
		AddressRequestDTO addressDTO = requestBody.getAddress();

		Set<MultipartFile> files = requestBody.getFiles();

		if (ObjectUtils.isEmpty(requestBody.getVehicleName())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("vehicleName is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(requestBody.getPrice())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("price is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(requestBody.getVehicleTypeId())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("vehicleTypeId is mandatory"),
					HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(requestBody.getVehicleBodyTypeId())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("vehicleBodyTypeId is mandatory"),
					HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(addressDTO)) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("address is mandatory"), HttpStatus.BAD_REQUEST);
		}

		// Address Validation
		if (Objects.isNull(addressDTO.getCityId())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("cityId is mandatory"), HttpStatus.BAD_REQUEST);
		}

		if (Objects.isNull(addressDTO.getStateId())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("stateId is mandatory"), HttpStatus.BAD_REQUEST);
		}

		if (Objects.isNull(addressDTO.getZipcode())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("zipcode is mandatory"), HttpStatus.BAD_REQUEST);
		}
		if (Objects.isNull(addressDTO.getZoneId())) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("zoneId is mandatory"), HttpStatus.BAD_REQUEST);
		}

		if (CollectionUtils.isEmpty(files)) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("files is mandatory"), HttpStatus.BAD_REQUEST);
		}

		VehicleDetail vehicleDetail = new VehicleDetail();
		vehicleDetail.setKeyFeatures(requestBody.getKeyFeatures());
		vehicleDetail.setPrice(requestBody.getPrice());
		vehicleDetail.setStatus(requestBody.getStatus());
		vehicleDetail.setVehicleName(requestBody.getVehicleName());

		Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(requestBody.getVehicleTypeId());

		if (vehicleTypeData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find VehicleType by id=" + requestBody.getVehicleTypeId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<VehicleBodyType> vehicleBodyTypeData = vehicleBodyTypeRepository
				.findById(requestBody.getVehicleBodyTypeId());
		if (vehicleBodyTypeData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find VehicleBodyType by id=" + requestBody.getVehicleBodyTypeId()),
					HttpStatus.BAD_REQUEST);
		}

		// Address
		Address address = new Address();

		Optional<City> cityData = cityRepository.findById(addressDTO.getCityId());
		if (cityData.isEmpty()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find City by id=" + addressDTO.getCityId()),
					HttpStatus.BAD_REQUEST);
		}

		Zipcode zipcode = zipcodeRepository.findByCode(addressDTO.getZipcode());

		if (Objects.isNull(zipcode)) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find Zipcode by id=" + addressDTO.getZipcode()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<ZoneDetail> zoneData = zoneDetailRepository.findById(addressDTO.getZoneId());

		if (zoneData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find ZoneDetail by id=" + addressDTO.getZoneId()),
					HttpStatus.BAD_REQUEST);
		}

		Optional<State> stateData = stateRepository.findById(addressDTO.getStateId());

		if (stateData.isEmpty()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find State by id=" + addressDTO.getStateId()),
					HttpStatus.BAD_REQUEST);
		}
		address.setCity(cityData.get());
		address.setState(stateData.get());
		address.setZipcode(zipcode);
		address.setZone(zoneData.get());
		LOG.info("Saving  @@@@  address {}", address);
		Address savedAddress = addressRepository.save(address);
		LOG.info("Saved   address id {}", savedAddress.getId());

		// IMAGES
		List<Images> images = new ArrayList<>();

		files.forEach(file -> {
			Images fileEntity = new Images();
			fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
			fileEntity.setContentType(file.getContentType());
			try {
				fileEntity.setData(file.getBytes());
			} catch (IOException e) {
				LOG.error("@@@@@ Exception during reading file bytes data", e);
			}
			fileEntity.setSize(file.getSize());
			images.add(fileEntity);
		});

		vehicleDetail.setVehicleType(vehicleTypeData.get());
		vehicleDetail.setVehicleBodyType(vehicleBodyTypeData.get());
		vehicleDetail.setAddress(savedAddress);

		// Save Vehicle details
		VehicleDetail savedVehicleDetail = vehicleDetailRepository.save(vehicleDetail);
		images.stream().forEach(image -> image.setVehicleDetail(savedVehicleDetail));
		imagesRepository.saveAll(images);
		return new ResponseEntity<Object>(savedVehicleDetail.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-details/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getVehicleDetails(@PathVariable Long id) {
		LOG.info("getVehicleDetails request received@@   {}", id);
		Optional<VehicleDetail> fetchedData = vehicleDetailRepository.findById(id);

		if (fetchedData.isPresent()) {
			LOG.info("Found data against ID {} and returing response   {}", id, fetchedData.get());
			VehicleDetail entity = fetchedData.get();
			VehicleDetailResponseDTO dto = new VehicleDetailResponseDTO();
			CommonsUtil.entityToDTO(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-details", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllVehicleDetails(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllVehicleDetails request received");

		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleDetailResponseDTO> vehicleDetailDTOList = new ArrayList<>();

		Page<VehicleDetail> pages = vehicleDetailRepository.findAll(
				filterVehicleDetailByName(filterCriteria).and(filterVehicleDetailByStatus(filterCriteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				VehicleDetailResponseDTO dto = new VehicleDetailResponseDTO();
				CommonsUtil.entityToDTO(entity, dto);
				vehicleDetailDTOList.add(dto);
			});
		}
		response.put("vehicleDetails", vehicleDetailDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-details/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleDetails(@PathVariable Long id) {
		LOG.info("deleteVehicleTransmission request received @@   {}", id);
		vehicleDetailRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// TODO - Update Vehicle Detail
}
