package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;
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

import com.ogive.oheo.dto.CompanyRequestDTO;
import com.ogive.oheo.dto.CompanyResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.PurchaseTypeRequestDTO;
import com.ogive.oheo.dto.PurchaseTypeResponseDTO;
import com.ogive.oheo.dto.VehicleBodyTypeRequestDTO;
import com.ogive.oheo.dto.VehicleBodyTypeResponseDTO;
import com.ogive.oheo.dto.VehicleFuelTypeRequestDTO;
import com.ogive.oheo.dto.VehicleFuelTypeResponseDTO;
import com.ogive.oheo.dto.VehicleModelRequestDTO;
import com.ogive.oheo.dto.VehicleModelResponseDTO;
import com.ogive.oheo.dto.VehicleTransmissionRequestDTO;
import com.ogive.oheo.dto.VehicleTransmissionResponseDTO;
import com.ogive.oheo.dto.VehicleTypeRequestDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.persistence.entities.Company;
import com.ogive.oheo.persistence.entities.PurchaseType;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleTransmission;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.repo.CompanyRepository;
import com.ogive.oheo.persistence.repo.PurchaseTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleBodyTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleFuelTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleModelRepository;
import com.ogive.oheo.persistence.repo.VehicleTransmissionRepository;
import com.ogive.oheo.persistence.repo.VehicleTypeRepository;

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
	public ResponseEntity<Object> getVehicleCompanies() {
		LOG.info("getVehicleCompanies request received");

		Iterable<Company> allCompanies = companyRepository.findAll();
		List<CompanyResponseDTO> companiesDTOList = new ArrayList<>();

		if (null != allCompanies) {
			allCompanies.forEach(entity -> {
				CompanyResponseDTO dto = new CompanyResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleCompany(entity.getId())).withSelfRel());
				companiesDTOList.add(dto);
			});
		}
		LOG.info("Total companiesDTOList count {}", companiesDTOList.size());
		return new ResponseEntity<Object>(companiesDTOList, HttpStatus.OK);
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
	public ResponseEntity<Object> getVehicleModels() {
		LOG.info("getVehicleModels request received");

		Iterable<VehicleModel> allVehicleModels = vehicleModelRepository.findAll();

		List<VehicleModelResponseDTO> allVehicleModelDTOList = new ArrayList<>();

		if (null != allVehicleModels) {
			allVehicleModels.forEach(entity -> {
				VehicleModelResponseDTO dto = new VehicleModelResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.setCompanyName(entity.getCompany().getCompanyName());
				dto.setCompanyId(entity.getCompany().getId());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleModel(entity.getId())).withSelfRel());
				allVehicleModelDTOList.add(dto);
			});
		}
		LOG.info("Total VehicleModels count {}", allVehicleModelDTOList.size());
		return new ResponseEntity<Object>(allVehicleModelDTOList, HttpStatus.OK);
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
	public ResponseEntity<Object> getVehicleTypees() {
		LOG.info("getVehicleTypees request received");

		Iterable<VehicleType> allVehicleTypes = vehicleTypeRepository.findAll();
		List<VehicleTypeResponseDTO> vehicleTypeDTOList = new ArrayList<>();

		if (null != allVehicleTypes) {
			allVehicleTypes.forEach(entity -> {
				VehicleTypeResponseDTO dto = new VehicleTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleType(entity.getId())).withSelfRel());
				vehicleTypeDTOList.add(dto);
			});
		}
		LOG.info("Total companiesDTOList count {}", vehicleTypeDTOList.size());
		return new ResponseEntity<Object>(vehicleTypeDTOList, HttpStatus.OK);
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
	public ResponseEntity<Object> getAllVehicleFuelTypes() {
		LOG.info("getAllVehicleFuelTypes request received");
		Iterable<VehicleFuelType> allFuelTypesData = vehicleFuelTypeRepository.findAll();
		List<CompanyResponseDTO> allFuelTypesDTOList = new ArrayList<>();

		if (null != allFuelTypesData) {
			allFuelTypesData.forEach(entity -> {
				CompanyResponseDTO dto = new CompanyResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleFuelType(entity.getId()))
						.withSelfRel());
				allFuelTypesDTOList.add(dto);
			});
		}
		LOG.info("Total allFuelTypesDTOList count {}", allFuelTypesDTOList.size());
		return new ResponseEntity<Object>(allFuelTypesDTOList, HttpStatus.OK);
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
	public ResponseEntity<Object> getAllVehicleBodyTypes() {
		LOG.info("getAllVehicleBodyTypes request received");
		Iterable<VehicleBodyType> allBodyTypesData = vehicleBodyTypeRepository.findAll();
		List<VehicleBodyTypeResponseDTO> allBodyTypesDTOList = new ArrayList<>();

		if (null != allBodyTypesData) {
			allBodyTypesData.forEach(entity -> {
				VehicleBodyTypeResponseDTO dto = new VehicleBodyTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleBodyType(entity.getId()))
						.withSelfRel());
				allBodyTypesDTOList.add(dto);
			});
		}
		LOG.info("Total allFuelTypesDTOList count {}", allBodyTypesDTOList.size());
		return new ResponseEntity<Object>(allBodyTypesDTOList, HttpStatus.OK);
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
	public ResponseEntity<Object> getAllPurchaseType() {
		LOG.info("getAllPurchaseType request received");

		Iterable<PurchaseType> allPurchaseData = purchaseTypeRepository.findAll();

		List<PurchaseTypeResponseDTO> allPurchaseTypeDTO = new ArrayList<>();

		if (null != allPurchaseData) {
			allPurchaseData.forEach(entity -> {
				PurchaseTypeResponseDTO dto = new PurchaseTypeResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getPurchaseType(entity.getId())).withSelfRel());
				allPurchaseTypeDTO.add(dto);
			});
		}
		LOG.info("Total allPurchaseTypeDTO count {}", allPurchaseTypeDTO.size());
		return new ResponseEntity<Object>(allPurchaseTypeDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/purchase-types/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deletePurchaseType(@PathVariable Long id) {
		LOG.info("deletePurchaseType request received @@   {}", id);
		purchaseTypeRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Vehicle Transmission
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
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a VehicleTransmission with id=" + id),HttpStatus.BAD_REQUEST);
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
	public ResponseEntity<Object> getAllVehicleTransmission() {
		LOG.info("getAllVehicleTransmission request received");

		Iterable<VehicleTransmission> allPurchaseData = vehicleTransmissionRepository.findAll();

		List<VehicleTransmissionResponseDTO> allTransmissionDTOList = new ArrayList<>();

		if (null != allPurchaseData) {
			allPurchaseData.forEach(entity -> {
				VehicleTransmissionResponseDTO dto = new VehicleTransmissionResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleTransmission(entity.getId()))
						.withSelfRel());
				allTransmissionDTOList.add(dto);
			});
		}
		LOG.info("Total allTransmissionDTOList count {}", allTransmissionDTOList.size());
		return new ResponseEntity<Object>(allTransmissionDTOList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-transmissions/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteVehicleTransmission(@PathVariable Long id) {
		LOG.info("deleteVehicleTransmission request received @@   {}", id);
		vehicleTransmissionRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
}
