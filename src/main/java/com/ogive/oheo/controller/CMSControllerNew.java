package com.ogive.oheo.controller;

import static com.ogive.oheo.dto.utils.CMSSpecifications.filterBuyRequestByEmail;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterBuyRequestByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterBuyRequestByUserId;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterChargingProductByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterChargingProductByStatus;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterLiveChargingProduct;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterLiveProduct;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterMaintenanceRecordByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterMaintenanceRecordByStatus;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterProductByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterProductByStatus;
import static com.ogive.oheo.dto.utils.CMSSpecifications.onlyFetchLoggedInUserChargingProduct;
import static com.ogive.oheo.dto.utils.CMSSpecifications.onlyFetchLoggedInUserProduct;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
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

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.BuyRequestDTO;
import com.ogive.oheo.dto.BuyRequestResponseDTO;
import com.ogive.oheo.dto.ChargingProductRequestDTO;
import com.ogive.oheo.dto.ChargingProductResponseDTO;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.ImagesResponseDTO;
import com.ogive.oheo.dto.ProductRequestDTO;
import com.ogive.oheo.dto.ProductResponseDTO;
import com.ogive.oheo.dto.ProductSpecificationResponseDTO;
import com.ogive.oheo.dto.SliderRequestDTO;
import com.ogive.oheo.dto.SliderResponseDTO;
import com.ogive.oheo.dto.SpecificationDTO;
import com.ogive.oheo.dto.TermAndConditionsRequestDTO;
import com.ogive.oheo.dto.TermAndConditionsResponseDTO;
import com.ogive.oheo.dto.VehicleDetailResponseDTO;
import com.ogive.oheo.dto.VehicleFuelTypeResponseDTO;
import com.ogive.oheo.dto.VehicleMaintenanceRecordRequestDTO;
import com.ogive.oheo.dto.VehicleMaintenanceRecordResponseDTO;
import com.ogive.oheo.dto.VehicleModelResponseDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.exception.ValidationException;
import com.ogive.oheo.persistence.entities.BuyRequest;
import com.ogive.oheo.persistence.entities.ChargingProduct;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Company;
import com.ogive.oheo.persistence.entities.Features;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.PrivacyPolicy;
import com.ogive.oheo.persistence.entities.Product;
import com.ogive.oheo.persistence.entities.ProductSpecification;
import com.ogive.oheo.persistence.entities.Slider;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.TermAndConditions;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleMaintenanceRecord;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleTransmission;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.repo.BuyRequestRepository;
import com.ogive.oheo.persistence.repo.ChargingProductRepository;
import com.ogive.oheo.persistence.repo.CityRepository;
import com.ogive.oheo.persistence.repo.CompanyRepository;
import com.ogive.oheo.persistence.repo.FeaturesRepository;
import com.ogive.oheo.persistence.repo.ImagesRepository;
import com.ogive.oheo.persistence.repo.PrivacyPolicyRepository;
import com.ogive.oheo.persistence.repo.ProductRepository;
import com.ogive.oheo.persistence.repo.ProductSpecificationRepository;
import com.ogive.oheo.persistence.repo.SliderRepository;
import com.ogive.oheo.persistence.repo.StateRepository;
import com.ogive.oheo.persistence.repo.TermAndConditionsRepository;
import com.ogive.oheo.persistence.repo.UserDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleBodyTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleFuelTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleMaintenanceRecordRepository;
import com.ogive.oheo.persistence.repo.VehicleModelRepository;
import com.ogive.oheo.persistence.repo.VehicleTransmissionRepository;
import com.ogive.oheo.persistence.repo.VehicleTypeRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "CMS-V2")
@RestController

@RequestMapping("/cms/v2")
public class CMSControllerNew {

	private static final Logger LOG = LoggerFactory.getLogger(CMSControllerNew.class);

	@Autowired
	private ProductSpecificationRepository productSpecificationRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FeaturesRepository featuresRepository;

	@Autowired
	private VehicleBodyTypeRepository vehicleBodyTypeRepository;

	@Autowired
	private VehicleFuelTypeRepository vehicleFuelTypeRepository;

	@Autowired
	private VehicleDetailRepository vehicleDetailRepository;

	@Autowired
	private VehicleModelRepository vehicleModelRepository;

	@Autowired
	private VehicleTransmissionRepository vehicleTransmissionRepository;

	@Autowired
	private ImagesRepository imagesRepository;

	@Autowired
	private VehicleTypeRepository vehicleTypeRepository;

	@Autowired
	private SliderRepository sliderRepository;

	@Autowired
	private VehicleMaintenanceRecordRepository vehicleMaintenanceRecordRepository;

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private CompanyRepository companyRepository;

	@Autowired
	private StateRepository stateRepository;

	@Autowired
	private BuyRequestRepository buyRequestRepository;

	@Autowired
	private TermAndConditionsRepository termAndConditionsRepository;

	@Autowired
	private PrivacyPolicyRepository privacyPolicyRepository;

	@Autowired
	private ChargingProductRepository chargingProductRepository;
	
	@Autowired
	private UserDetailRepository userDetailRepository;

	// Product API
	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/{userId}/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addProduct(@PathVariable Long userId,@ModelAttribute ProductRequestDTO productRequestDTO) {
		LOG.info("addProduct request received@@   {}", productRequestDTO);
		// Product Entity
		Product entity = new Product();
		BeanUtils.copyProperties(productRequestDTO, entity);
		// ProductSpecification
		SpecificationDTO SpecificationDTO = productRequestDTO.getSpecification();
		ProductSpecification specificationEntity = new ProductSpecification();
		BeanUtils.copyProperties(SpecificationDTO, specificationEntity);
		
		
		Optional<UserDetail> userDetailData = userDetailRepository.findById(userId);
		if (!userDetailData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a UserDetail with id=" + userId),HttpStatus.BAD_REQUEST);
		}
		entity.setUserDetail(userDetailData.get());
		// VehicleDetail
		Long vehicleDetailId = productRequestDTO.getVehicleDetailId();
		Optional<VehicleDetail> vehicleDetailData = vehicleDetailRepository.findById(vehicleDetailId);
		
		if (!vehicleDetailData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleDetailId),
					HttpStatus.BAD_REQUEST);
		}
		// VehicleFuelType
		Long vehicleFuelTypeId = productRequestDTO.getVehicleFuelTypeId();
		Optional<VehicleFuelType> vehicleFuelTypeData = vehicleFuelTypeRepository.findById(vehicleFuelTypeId);
		if (!vehicleFuelTypeData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId), HttpStatus.BAD_REQUEST);
		}
		// VehicleTransmission
		Long vehicleTransmissionId = productRequestDTO.getVehicleTransmissionId();
		Optional<VehicleTransmission> vehicleTransmissionData = vehicleTransmissionRepository
				.findById(vehicleTransmissionId);

		if (!vehicleTransmissionData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId), HttpStatus.BAD_REQUEST);
		}
		// VehicleModel
		Long vehicleModelId = productRequestDTO.getVehicleModelId();
		Optional<VehicleModel> vehicleModelData = vehicleModelRepository.findById(vehicleModelId);
		if (!vehicleModelData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId), HttpStatus.BAD_REQUEST);
		}
		// VehicleType
		Long vehicleTypeId = productRequestDTO.getVehicleTypeId();
		Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(vehicleTypeId);

		if (!vehicleTypeData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId),
					HttpStatus.BAD_REQUEST);
		}

		entity.setVehicleDetail(vehicleDetailData.get());
		entity.setVehicleFuelType(vehicleFuelTypeData.get());
		entity.setVehicleTransmission(vehicleTransmissionData.get());
		entity.setVehicleModel(vehicleModelData.get());
		entity.setVehicleType(vehicleTypeData.get());

		// Save Product
		Product productEntity = productRepository.save(entity);
		specificationEntity.setProduct(productEntity);
		// Save product specification
		productSpecificationRepository.save(specificationEntity);
		// Save Image
		List<MultipartFile> imagesFile = productRequestDTO.getImages();
		if (null != imagesFile) {
			imagesFile.stream().filter(image -> image != null && !image.isEmpty()).forEach(image -> {
				Images fileEntity = new Images();
				multiPartToFileEntity(image, fileEntity, productRequestDTO.getImageType());
				fileEntity.setProduct(productEntity);
				imagesRepository.save(fileEntity);
			});
		}
		// Save brochure
		if (null !=productRequestDTO.getBrochure() && !productRequestDTO.getBrochure().isEmpty()) {
			Images brochure = new Images();
			multiPartToFileEntity(productRequestDTO.getBrochure(), brochure, productRequestDTO.getImageType());
			brochure.setProduct(productEntity);
			imagesRepository.save(brochure);
		}
		// Save features
		if (!CollectionUtils.isEmpty(productRequestDTO.getFeatures())) {
			List<Features> featuresList = new ArrayList<>();
			productRequestDTO.getFeatures().forEach(featureStr -> {
				Features features = new Features();
				features.setName(featureStr);
				features.setProduct(productEntity);
				featuresList.add(features);
			});
			featuresRepository.saveAll(featuresList);
		}
		
		//  Save Video file 
		if (null != productRequestDTO.getVideo() && !productRequestDTO.getVideo().isEmpty()) {
			Images video = new Images();
			multiPartToFileEntity(productRequestDTO.getVideo(), video, ImageType.Video);
			video.setProduct(productEntity);
			imagesRepository.save(video);
		}
		return new ResponseEntity<Object>(productEntity.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{userId}/products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getProduct(
			@PathVariable Long userId,
			@PathVariable Long id) {
		LOG.info("getProduct request received@@   {}", id);
		Product entity = productRepository.findProductByUserIdAndId(userId, id);
		if (Objects.nonNull(entity)) {
			ProductResponseDTO dto = new ProductResponseDTO();
			populateProductEntityToDTO(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{userId}/products", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllProducts(
			@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllProducts request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		criteria.setUserId(userId);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ProductResponseDTO> allDTO = new ArrayList<>();
		Page<Product> pages = productRepository.findAll(onlyFetchLoggedInUserProduct(criteria).and(filterProductByName(criteria).and(filterProductByStatus(criteria))), paging);
		
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ProductResponseDTO dto = new ProductResponseDTO();
				populateProductEntityToDTO(entity, dto);
				allDTO.add(dto);
			});
		}
		response.put("products", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Returns all instances of the type if they are live. Live product are one will be visible to shop", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getLiveProduct(
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
		List<ProductResponseDTO> allDTO = new ArrayList<>();
		Page<Product> pages = productRepository.findAll(filterProductByName(criteria).and(filterProductByStatus(criteria).and(filterLiveProduct())), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ProductResponseDTO dto = new ProductResponseDTO();
				populateProductEntityToDTO(entity, dto);
				allDTO.add(dto);
			});
		}
		response.put("products", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Retrieves states along with city name in key value pair", notes = "Retrieves states list along with city name", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live/dropdown-live", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getLiveProductDropdown() {
		LOG.info("getLiveProductDropdown request received@@");
		List<Object[]> productData = productRepository.dropDownLive("Y");

		List<Map<Object, Object>> dropDowns = new ArrayList<>();
		productData.forEach(data -> {
			Map<Object, Object> map = new HashMap<>();
			map.put(data[0], data[1]);
			dropDowns.add(map);
		});
		return new ResponseEntity<Object>(dropDowns, HttpStatus.OK);
	}

	@ApiOperation(value = "This api is used to create sider data in backend. Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/products-live/slider", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addSlider(@Valid @ModelAttribute SliderRequestDTO sliderRequest) throws IOException {
		LOG.info("addSlider request received@@   {}", sliderRequest);
		Slider entity = new Slider();
		BeanUtils.copyProperties(sliderRequest, entity);
		Optional<Product> productData = productRepository.findById(sliderRequest.getProductId());

		if (!productData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + sliderRequest.getProductId()),
					HttpStatus.BAD_REQUEST);
		}
		entity.setData(sliderRequest.getSlider().getBytes());
		entity.setContentType(sliderRequest.getSlider().getContentType());
		entity.setName(StringUtils.cleanPath(sliderRequest.getSlider().getOriginalFilename()));

		entity.setProduct(productData.get());

		Slider savedData = sliderRepository.save(entity);
		LOG.info("Saved @@   {}", savedData);
		return new ResponseEntity<Object>(savedData.getId(), HttpStatus.OK);
	}

	@ApiOperation(value = "Get all sliders along with product link", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live/sliders")
	public ResponseEntity<Object> getSliders() throws IOException {
		LOG.info("getSliders request received@@ ");
		Iterable<Slider> allSliderEntity = sliderRepository.findAll();
		List<SliderResponseDTO> allDTO = new ArrayList<>();
		allSliderEntity.forEach(slider -> {
			SliderResponseDTO dto = new SliderResponseDTO();
			BeanUtils.copyProperties(slider, dto);
			Product product = slider.getProduct();
			dto.setProductId(product.getId());
			dto.setName(product.getName());

			// TODO - Add logic to pull slider image
			try {
				dto.add(linkTo(methodOn(CMSControllerNew.class).getSliderById(slider.getId())).withSelfRel());
				dto.add(linkTo(methodOn(CMSControllerNew.class).getProduct(product.getUserDetail().getId(),product.getId())).withRel("Product"));
				allDTO.add(dto);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

		return new ResponseEntity<Object>(allDTO, HttpStatus.OK);
	}

	@ApiOperation(value = "Pull slider", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live/sliders/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getSliderById(@PathVariable Long id) throws IOException {
		LOG.info("getSliderbyId request received@@ ");
		Optional<Slider> sliderData = sliderRepository.findById(id);
		if (!sliderData.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Slider sldier = sliderData.get();
		return ResponseEntity.ok()
				// Use below to enable download
				// .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" +
				// images.getName() + "\"")
				.header(HttpHeaders.CONTENT_DISPOSITION, "filename=\"" + sldier.getName() + "\"")
				.contentType(MediaType.valueOf(sldier.getContentType())).body(sldier.getData());

	}

	@Transactional
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
		LOG.info("deleteProduct request received @@   {}", id);
		Optional<Product> entityData = productRepository.findById(id);

		if (entityData.isPresent()) {
			//Delete Image
			imagesRepository.deleteByProductId(id);
			//Delete Slider 
			sliderRepository.deleteByProductId(id);
			//Delete product specification 
			productSpecificationRepository.deleteByProductId(id);
			//Delete product features 
			featuresRepository.deleteByProductId(id);
			//Now delete product			
			productRepository.deleteByProductId(id);
			
		} else {
			LOG.info("Did not find an Product entity to delete");
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	public void populateProductEntityToDTO(Product entity, ProductResponseDTO dto) {
		BeanUtils.copyProperties(entity, dto);
		// VehicleDetail
		VehicleDetail vehicleDetail = entity.getVehicleDetail();
		if (Objects.nonNull(vehicleDetail)) {
			VehicleDetailResponseDTO vehicleDetailDTO = new VehicleDetailResponseDTO();
			BeanUtils.copyProperties(vehicleDetail, vehicleDetailDTO);
			dto.setVehicleDetail(vehicleDetailDTO);
		}
		// VehicleFuelType
		VehicleFuelType vehicleFuelType = entity.getVehicleFuelType();
		if (Objects.nonNull(vehicleFuelType)) {
			VehicleFuelTypeResponseDTO vehicleFuelTypeResponseDTO = new VehicleFuelTypeResponseDTO();
			BeanUtils.copyProperties(vehicleFuelType, vehicleFuelTypeResponseDTO);
			dto.setVehicleFuelType(vehicleFuelTypeResponseDTO);
		}

		// VehicleModel
		VehicleModel vehicleModel = entity.getVehicleModel();
		if (Objects.nonNull(vehicleModel)) {
			VehicleModelResponseDTO vehicleModelResponseDTO = new VehicleModelResponseDTO();
			BeanUtils.copyProperties(vehicleModel, vehicleModelResponseDTO);
			dto.setVehicleModel(vehicleModelResponseDTO);
		}

		// VehicleType
		VehicleType vehicleType = entity.getVehicleType();
		if (Objects.nonNull(vehicleType)) {
			VehicleTypeResponseDTO vehicleTypeResponseDTO = new VehicleTypeResponseDTO();
			BeanUtils.copyProperties(vehicleType, vehicleTypeResponseDTO);
			dto.setVehicleType(vehicleTypeResponseDTO);
		}

		// ProductSpecification
		ProductSpecification productSpecification = entity.getProductSpecification();
		if (Objects.nonNull(productSpecification)) {
			ProductSpecificationResponseDTO ProductSpecificationResponseDTO = new ProductSpecificationResponseDTO();
			BeanUtils.copyProperties(productSpecification, ProductSpecificationResponseDTO);
			dto.setProductSpecification(ProductSpecificationResponseDTO);
		}
		// Images
		Set<Images> images = entity.getImages();
		if (null != images) {
			Set<ImagesResponseDTO> imagesDTOList = new HashSet<>();
			images.forEach(img -> {
				ImagesResponseDTO imgDTO = new ImagesResponseDTO();
				BeanUtils.copyProperties(img, imgDTO);
				ImageType imageType = img.getImageType();
				if( null == imageType) {
					imgDTO.add(linkTo(methodOn(FileProcessingController.class).readFile(img.getId())).withSelfRel());
				}else {
					imgDTO.add(linkTo(methodOn(FileProcessingController.class).readFile(img.getId())).withRel(imageType.name()));
				}
				imagesDTOList.add(imgDTO);
			});
			dto.setImages(imagesDTOList);
		}

		Set<Features> features = entity.getFeatures();
		if (null != features) {
			Set<String> featureSet = features.stream().map(feature -> feature.getName()).collect(Collectors.toSet());
			dto.setFeatures(featureSet);
		}
		dto.add(linkTo(methodOn(CMSControllerNew.class).getProduct(entity.getUserDetail().getId(),entity.getId())).withSelfRel());
	}

	public void multiPartToFileEntity(MultipartFile file, Images fileEntity, ImageType imageType) {
		fileEntity.setName(StringUtils.cleanPath(file.getOriginalFilename()));
		fileEntity.setContentType(file.getContentType());
		try {
			fileEntity.setData(file.getBytes());
		} catch (IOException e) {
			LOG.error("@@@@@ Exception during reading file bytes data", e);
			throw new ValidationException(e.getMessage());
		}
		fileEntity.setImageType(imageType);
		fileEntity.setSize(file.getSize());
	}

	// Manage Vehicle Maintenance record
	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - CMS Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/vehicle-maintenance-records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addVehicleMaintenanceRecord(
			@ModelAttribute VehicleMaintenanceRecordRequestDTO requestBody) {
		LOG.info("addVehicleMaintenanceRecord request received@@   {}", requestBody);
		VehicleMaintenanceRecord entity = new VehicleMaintenanceRecord();
		BeanUtils.copyProperties(requestBody, entity);
		// VehicleFuelType
		Long vehicleFuelTypeId = requestBody.getVehicleFuelTypeId();

		Optional<VehicleFuelType> vehicleFuelTypeData = vehicleFuelTypeRepository.findById(vehicleFuelTypeId);
		if (!vehicleFuelTypeData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId), HttpStatus.BAD_REQUEST);
		}

		// VehicleModel
		Long vehicleModelId = requestBody.getVehicleModelId();
		Optional<VehicleModel> vehicleModelData = vehicleModelRepository.findById(vehicleModelId);
		if (!vehicleModelData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId), HttpStatus.BAD_REQUEST);
		}
		// VehicleType
		Long vehicleTypeId = requestBody.getVehicleTypeId();
		Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(vehicleTypeId);

		if (!vehicleTypeData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId),
					HttpStatus.BAD_REQUEST);
		}
		// State
		Long stateId = requestBody.getStateId();
		Optional<State> stateData = stateRepository.findById(stateId);
		if (!stateData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId),
					HttpStatus.BAD_REQUEST);
		}

		// City
		Long cityId = requestBody.getCityId();
		Optional<City> cityData = cityRepository.findById(cityId);
		if (!cityData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId),
					HttpStatus.BAD_REQUEST);
		}
		// Company
		Long companyId = requestBody.getCompanyId();
		Optional<Company> companyData = companyRepository.findById(companyId);

		if (!companyData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId),
					HttpStatus.BAD_REQUEST);
		}
		entity.setVehicleFuelType(vehicleFuelTypeData.get());
		entity.setVehicleModel(vehicleModelData.get());
		entity.setVehicleType(vehicleTypeData.get());
		entity.setCity(cityData.get());
		entity.setState(stateData.get());
		entity.setCompany(companyData.get());

		// Save Image
		MultipartFile image = requestBody.getImage();
		if (null != image   && !image.isEmpty()) {
			Images fileEntity = new Images();
			multiPartToFileEntity(image, fileEntity, ImageType.Other);
			Images savedImage = imagesRepository.save(fileEntity);
			entity.setImage(savedImage);
		}

		VehicleMaintenanceRecord savedEntity = vehicleMaintenanceRecordRepository.save(entity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - CMS Admin", produces = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/vehicle-maintenance-records/{id}",produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateVehicleMaintenanceRecord(@PathVariable Long id,@Valid  @ModelAttribute VehicleMaintenanceRecordRequestDTO requestBody) {
		LOG.info("updateVehicleMaintenanceRecord  received@@   {}", requestBody);

		Optional<VehicleMaintenanceRecord> entityData = vehicleMaintenanceRecordRepository.findById(id);

		if (entityData.isPresent()) {
			VehicleMaintenanceRecord entity = entityData.get();
			BeanUtils.copyProperties(requestBody, entity);

			// VehicleFuelType
			Long vehicleFuelTypeId = requestBody.getVehicleFuelTypeId();

			Optional<VehicleFuelType> vehicleFuelTypeData = vehicleFuelTypeRepository.findById(vehicleFuelTypeId);
			if (!vehicleFuelTypeData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId),
						HttpStatus.BAD_REQUEST);
			}

			// VehicleModel
			Long vehicleModelId = requestBody.getVehicleModelId();
			Optional<VehicleModel> vehicleModelData = vehicleModelRepository.findById(vehicleModelId);
			if (!vehicleModelData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleFuelTypeId),
						HttpStatus.BAD_REQUEST);
			}

			// VehicleType
			Long vehicleTypeId = requestBody.getVehicleTypeId();
			Optional<VehicleType> vehicleTypeData = vehicleTypeRepository.findById(vehicleTypeId);

			if (!vehicleTypeData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId), HttpStatus.BAD_REQUEST);
			}
			// State
			Long stateId = requestBody.getStateId();
			Optional<State> stateData = stateRepository.findById(stateId);
			if (!stateData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId), HttpStatus.BAD_REQUEST);
			}

			// City
			Long cityId = requestBody.getCityId();
			Optional<City> cityData = cityRepository.findById(cityId);
			if (!cityData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId), HttpStatus.BAD_REQUEST);
			}
			// Company
			Long companyId = requestBody.getCompanyId();
			Optional<Company> companyData = companyRepository.findById(companyId);

			if (!companyData.isPresent()) {
				return new ResponseEntity<Object>(
						new ErrorResponseDTO("Did not find a Entity with id=" + vehicleTypeId), HttpStatus.BAD_REQUEST);
			}
			entity.setVehicleFuelType(vehicleFuelTypeData.get());
			entity.setVehicleModel(vehicleModelData.get());
			entity.setVehicleType(vehicleTypeData.get());
			entity.setCity(cityData.get());
			entity.setState(stateData.get());
			entity.setCompany(companyData.get());

			// Save Image
			MultipartFile image = requestBody.getImage();
			if (null != image && !image.isEmpty()) {
				Images fileEntity = entity.getImage() == null ? new Images() : entity.getImage();
				multiPartToFileEntity(image, fileEntity, ImageType.Other);
				imagesRepository.save(fileEntity);
			}
			VehicleMaintenanceRecord savedEntity = vehicleMaintenanceRecordRepository.save(entity);
			return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
		}

		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Entity with id=" + id),
				HttpStatus.BAD_REQUEST);
	}

	//Single Record
	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - CMS Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-maintenance-records/{id}")
	public ResponseEntity<Object> getMaintenaceRecordById(@PathVariable Long id) throws IOException {
		LOG.info("getMaintenaceRecordById request received@@ ");
		Optional<VehicleMaintenanceRecord> entityData = vehicleMaintenanceRecordRepository.findById(id);

		if (entityData.isPresent()) {
			VehicleMaintenanceRecord record = entityData.get();
			VehicleMaintenanceRecordResponseDTO dto = new VehicleMaintenanceRecordResponseDTO();
			BeanUtils.copyProperties(record, dto);
			City city = record.getCity();
			dto.setCityId(city.getId());
			dto.setCityName(city.getName());

			Company company = record.getCompany();
			dto.setCompanyId(company.getId());
			dto.setCompanyName(company.getCompanyName());

			State state = record.getState();
			dto.setStateId(state.getId());
			dto.setStateCode(state.getStateCode());
			dto.setStateName(state.getStateName());

			VehicleFuelType vehicleFuelType = record.getVehicleFuelType();
			dto.setVehicleFuelTypeId(vehicleFuelType.getId());
			dto.setVehicleFuelTypeName(vehicleFuelType.getName());

			VehicleType vehicleType = record.getVehicleType();
			dto.setVehicleTypeId(vehicleType.getId());
			dto.setVehicleTypeName(vehicleType.getName());

			VehicleModel vehicleModel = record.getVehicleModel();
			dto.setModelId(vehicleModel.getId());
			dto.setModelName(vehicleModel.getModelName());
			Images image = record.getImage();
			if(Objects.nonNull(image)) {
				dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId())).withRel("Image"));
			}
			
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - CMS Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-maintenance-records")
	public ResponseEntity<Object> getAllMaintenaceRecord(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) throws IOException {
		LOG.info("getAllMaintenaceRecord request received@@ ");
		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<VehicleMaintenanceRecordResponseDTO> allDTO = new ArrayList<>();
		Page<VehicleMaintenanceRecord> pages = vehicleMaintenanceRecordRepository.findAll(filterMaintenanceRecordByName(filterCriteria).and(filterMaintenanceRecordByStatus(filterCriteria)),paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(record -> {
				
				VehicleMaintenanceRecordResponseDTO dto = new VehicleMaintenanceRecordResponseDTO();
				BeanUtils.copyProperties(record, dto);
				City city = record.getCity();
				dto.setCityId(city.getId());
				dto.setCityName(city.getName());

				Company company = record.getCompany();
				dto.setCompanyId(company.getId());
				dto.setCompanyName(company.getCompanyName());

				State state = record.getState();
				dto.setStateId(state.getId());
				dto.setStateCode(state.getStateCode());
				dto.setStateName(state.getStateName());

				VehicleFuelType vehicleFuelType = record.getVehicleFuelType();
				dto.setVehicleFuelTypeId(vehicleFuelType.getId());
				dto.setVehicleFuelTypeName(vehicleFuelType.getName());

				VehicleType vehicleType = record.getVehicleType();
				dto.setVehicleTypeId(vehicleType.getId());
				dto.setVehicleTypeName(vehicleType.getName());

				VehicleModel vehicleModel = record.getVehicleModel();
				dto.setModelId(vehicleModel.getId());
				dto.setModelName(vehicleModel.getModelName());
				Images image = record.getImage();

				if(Objects.nonNull(image)) {
					dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId())).withRel("Image"));
				}
				allDTO.add(dto);
			});
		}
		response.put("records", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total records count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	
	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - CMS Admin", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/vehicle-maintenance-records/{id}")
	public ResponseEntity<Object> deleteMaintenaceRecordById(@PathVariable Long id) throws IOException {
		LOG.info("deleteMaintenaceRecordById request received@@ ID {} ", id);
		vehicleMaintenanceRecordRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Vehicle maintenace record - Customer", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/vehicle-maintenance-record/{email}")
	public ResponseEntity<Object> getMaintenaceRecord(@PathVariable String email) throws IOException {
		LOG.info("getMaintenaceRecord request received@@ ");
		Iterable<VehicleMaintenanceRecord> allEntity = vehicleMaintenanceRecordRepository.findByEmail(email);
		List<VehicleMaintenanceRecordResponseDTO> allDTO = new ArrayList<>();

		allEntity.forEach(record -> {
			VehicleMaintenanceRecordResponseDTO dto = new VehicleMaintenanceRecordResponseDTO();

			BeanUtils.copyProperties(record, dto);

			City city = record.getCity();
			dto.setCityId(city.getId());
			dto.setCityName(city.getName());

			Company company = record.getCompany();
			dto.setCompanyId(company.getId());
			dto.setCompanyName(company.getCompanyName());

			State state = record.getState();
			dto.setStateId(state.getId());
			dto.setStateCode(state.getStateCode());
			dto.setStateName(state.getStateName());

			VehicleFuelType vehicleFuelType = record.getVehicleFuelType();
			dto.setVehicleFuelTypeId(vehicleFuelType.getId());
			dto.setVehicleFuelTypeName(vehicleFuelType.getName());

			VehicleType vehicleType = record.getVehicleType();
			dto.setVehicleTypeId(vehicleType.getId());
			dto.setVehicleTypeName(vehicleType.getName());

			VehicleModel vehicleModel = record.getVehicleModel();
			dto.setModelId(vehicleModel.getId());
			dto.setModelName(vehicleModel.getModelName());
			Images image = record.getImage();
			if(Objects.nonNull(image)) {
				dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId())).withRel("Image"));
			}
			
			allDTO.add(dto);
		});

		return new ResponseEntity<Object>(allDTO, HttpStatus.OK);
	}

	// Buy Request Api
	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/buy-requests", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addBuyRequest(@Valid @RequestBody BuyRequestDTO buyRequest) {
		LOG.info("addBuyRequest request received@@   {}", buyRequest);
		// BuyRequest Entity
		BuyRequest entity = new BuyRequest();
		BeanUtils.copyProperties(buyRequest, entity);

		// userRepository.findById(buyRequest.getDealerId());
		// Dealer/Distributor 
		Optional<UserDetail> userDetailData = userDetailRepository.findById(buyRequest.getDealerId());
		if(!userDetailData.isPresent()) {
			return new ResponseEntity<Object>(
					new ErrorResponseDTO("Did not find a UserDetail with id=" + buyRequest.getDealerId()),HttpStatus.BAD_REQUEST);
		}
		
		entity.setUserDetail(userDetailData.get());

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

		BuyRequest savedEntity = buyRequestRepository.save(entity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/buy-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getBuyRequest(@PathVariable Long id) {
		LOG.info("getBuyRequest request received@@   {}", id);
		Optional<BuyRequest> entityData = buyRequestRepository.findById(id);
		if (entityData.isPresent()) {
			BuyRequest entity = entityData.get();
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
			// Dealer Mapping
			if (Objects.nonNull(entity.getUserDetail())) {
				dto.setDealerName(entity.getUserDetail().getName());
				dto.setDealerId(entity.getUserDetail().getId());
			}

			dto.add(linkTo(methodOn(CMSControllerNew.class).getBuyRequest(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{userId}/buy-requests", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllBuyRequest(
			@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) String email) {
		LOG.info("getAllVehicleDetails request received");

		FilterCriteria filterCriteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, null);
		filterCriteria.setEmail(email);
		filterCriteria.setUserId(userId);
		
		LOG.info("filterCriteria    {} ", filterCriteria);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<BuyRequestResponseDTO> buyRequestDTOList = new ArrayList<>();

		Page<BuyRequest> pages = buyRequestRepository
				.findAll(filterBuyRequestByName(filterCriteria).and(filterBuyRequestByEmail(filterCriteria)).and(filterBuyRequestByUserId(filterCriteria)), paging);

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
				//Dealer Mapping
				
				if(Objects.nonNull(entity.getUserDetail())) {
					dto.setDealerName(entity.getUserDetail().getName());
					dto.setDealerId(entity.getUserDetail().getId());
				}

				dto.add(linkTo(methodOn(CMSControllerNew.class).getBuyRequest(entity.getId())).withSelfRel());
				buyRequestDTOList.add(dto);

			});
		}
		response.put("buyRequests", buyRequestDTOList);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}

	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/buy-requests/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteBuyRequest(@PathVariable Long id) {
		LOG.info("deleteBuyRequest request received @@   {}", id);
		buyRequestRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Terms & Conditions
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/terms-and-conditions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addTermsAndConditions(@Valid @RequestBody TermAndConditionsRequestDTO requestBody) {
		LOG.info("addVehicleCompany request received@@   {}", requestBody);
		TermAndConditions entity = new TermAndConditions();
		BeanUtils.copyProperties(requestBody, entity);
		TermAndConditions saved = termAndConditionsRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/terms-and-conditions", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getTermsAndConditions() {
		LOG.info("getTermsAndConditions request received@@   {}");
		Optional<TermAndConditions> entityData = termAndConditionsRepository.findFirstByOrderByCreatedDateDesc();
		if (entityData.isPresent()) {
			TermAndConditions entity = entityData.get();
			TermAndConditionsResponseDTO dto = new TermAndConditionsResponseDTO();
			entity.setCreatedDate(new java.util.Date());
			BeanUtils.copyProperties(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Privacy Policy
	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/privacy-policies", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addPrivacyPolicy(@Valid @RequestBody TermAndConditionsRequestDTO requestBody) {
		LOG.info("addVehicleCompany request received@@   {}", requestBody);
		PrivacyPolicy entity = new PrivacyPolicy();
		BeanUtils.copyProperties(requestBody, entity);
		PrivacyPolicy saved = privacyPolicyRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/privacy-policies", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> GetPrivacyPolicy() {
		LOG.info("getTermsAndConditions request received@@   {}");
		Optional<PrivacyPolicy> entityData = privacyPolicyRepository.findFirstByOrderByCreatedDateDesc();
		if (entityData.isPresent()) {
			PrivacyPolicy entity = entityData.get();
			TermAndConditionsResponseDTO dto = new TermAndConditionsResponseDTO();
			entity.setCreatedDate(new java.util.Date());
			BeanUtils.copyProperties(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	// Charging product api
	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/{userId}/charging-products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addChargingProducts(
			@PathVariable Long userId,
			@ModelAttribute ChargingProductRequestDTO chargingProductRequestDTO) {
		LOG.info("addChargingProducts request received@@   {}", chargingProductRequestDTO);
		// ChargingProduct Entity
		ChargingProduct entity = new ChargingProduct();
		
		Optional<UserDetail> userDetailData = userDetailRepository.findById(userId);
		if (!userDetailData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a UserDetail with id=" + userId),HttpStatus.BAD_REQUEST);
		}
		entity.setUserDetail(userDetailData.get());
		
		BeanUtils.copyProperties(chargingProductRequestDTO, entity);
		// ProductSpecification
		// Save Image
		MultipartFile imagesFile = chargingProductRequestDTO.getImage();
		ChargingProduct savedEntity = chargingProductRepository.save(entity);

		if (null != imagesFile) {
			Images fileEntity = new Images();
			multiPartToFileEntity(imagesFile, fileEntity, chargingProductRequestDTO.getImageType());
			fileEntity.setChargingProduct(savedEntity);
			imagesRepository.save(fileEntity);

		}
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	
	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PutMapping(path = "/{userId}/charging-products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateChargingProducts(
			@PathVariable Long userId,
			@PathVariable Long id,
			@Valid @ModelAttribute ChargingProductRequestDTO chargingProductRequestDTO) {
		LOG.info("updateChargingProducts request received@@   {}", chargingProductRequestDTO);
		// ChargingProduct Entity
		
		Optional<UserDetail> userDetailData = userDetailRepository.findById(userId);
		if (!userDetailData.isPresent()) {
			return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a UserDetail with id=" + userId),HttpStatus.BAD_REQUEST);
		}
		
		Optional<ChargingProduct> chargingProductData = chargingProductRepository.findById(id);
		
		if (chargingProductData.isPresent()) {
			ChargingProduct entity = chargingProductData.get();
			BeanUtils.copyProperties(chargingProductRequestDTO, entity);
			MultipartFile imagesFile = chargingProductRequestDTO.getImage();
			entity.setUserDetail(userDetailData.get());
			
			ChargingProduct savedEntity = chargingProductRepository.save(entity);

			if (null != imagesFile) {

				Set<Images> fileEntities = entity.getImages();
				
				if (null != fileEntities) {
					fileEntities.forEach(fileEntity -> {
						multiPartToFileEntity(imagesFile, fileEntity, chargingProductRequestDTO.getImageType());
						fileEntity.setChargingProduct(savedEntity);
						imagesRepository.save(fileEntity);
					});
				}
			}
			return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a ChargingProduct with id=" + id),	HttpStatus.BAD_REQUEST);
			

	}

	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{userId}/charging-products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getChargingProducts(
			@PathVariable Long userId,
			@PathVariable Long id) {
		LOG.info("getChargingProducts request received@@   {}", id);
		ChargingProduct entity = chargingProductRepository.findProductByUserIdAndId(userId, id);
		if (Objects.nonNull(entity)) {
			ChargingProductResponseDTO dto = new ChargingProductResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(CMSControllerNew.class).getChargingProducts(entity.getUserDetail().getId(),entity.getId())).withSelfRel());
			
			Set<Images> images = entity.getImages();
			if (null != images) {
				images.forEach(image -> {
					dto.setImageType(image.getImageType());
					dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId())).withRel("Image"));
				});
			}
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{userId}/charging-products", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllChargingProducts(
			@PathVariable Long userId,
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllChargingProducts request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		criteria.setUserId(userId);
		
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ChargingProductResponseDTO> allDTO = new ArrayList<>();
		Page<ChargingProduct> pages = chargingProductRepository
				.findAll(filterChargingProductByName(criteria).and(filterChargingProductByStatus(criteria)).and(onlyFetchLoggedInUserChargingProduct(criteria)), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ChargingProductResponseDTO dto = new ChargingProductResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(CMSControllerNew.class).getChargingProducts(userId,entity.getId())).withSelfRel());
				Set<Images> images = entity.getImages();
				if (null != images) {
					images.forEach(image -> {
						dto.setImageType(image.getImageType());
						dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId()))
								.withRel("Image"));
					});
				}
				allDTO.add(dto);
			});
		}
		response.put("products", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total products count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/charging-products-live", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllChargingProductsLive(@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllChargingProductsLive request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<ChargingProductResponseDTO> allDTO = new ArrayList<>();
		Page<ChargingProduct> pages = chargingProductRepository
				.findAll(filterChargingProductByName(criteria).and(filterChargingProductByStatus(criteria).and(filterLiveChargingProduct())), paging);

		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				ChargingProductResponseDTO dto = new ChargingProductResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(CMSControllerNew.class).getChargingProducts(entity.getUserDetail().getId(),entity.getId())).withSelfRel());
				Set<Images> images = entity.getImages();
				if (null != images) {
					images.forEach(image -> {
						dto.setImageType(image.getImageType());
						dto.add(linkTo(methodOn(FileProcessingController.class).readFile(image.getId()))
								.withRel("Image"));
					});
				}
				allDTO.add(dto);
			});
		}
		response.put("products", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total products count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/charging-products/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> deleteChargingProducts(@PathVariable Long id) {
		LOG.info("deleteChargingProducts request received@@   {}", id);
		imagesRepository.deleteByChargingProductId(id);
		chargingProductRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
