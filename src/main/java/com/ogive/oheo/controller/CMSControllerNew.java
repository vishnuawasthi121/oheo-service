package com.ogive.oheo.controller;

import static com.ogive.oheo.dto.utils.CMSSpecifications.filterLiveProduct;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterProductByName;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterProductByStatus;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ogive.oheo.constants.ImageType;
import com.ogive.oheo.constants.StatusCode;
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.ImagesResponseDTO;
import com.ogive.oheo.dto.ProductRequestDTO;
import com.ogive.oheo.dto.ProductResponseDTO;
import com.ogive.oheo.dto.ProductSpecificationResponseDTO;
import com.ogive.oheo.dto.SliderRequestDTO;
import com.ogive.oheo.dto.SliderResponseDTO;
import com.ogive.oheo.dto.SpecificationDTO;
import com.ogive.oheo.dto.VehicleDetailResponseDTO;
import com.ogive.oheo.dto.VehicleFuelTypeResponseDTO;
import com.ogive.oheo.dto.VehicleModelResponseDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.exception.ValidationException;
import com.ogive.oheo.persistence.entities.Features;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.Product;
import com.ogive.oheo.persistence.entities.ProductSpecification;
import com.ogive.oheo.persistence.entities.Slider;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleFuelType;
import com.ogive.oheo.persistence.entities.VehicleModel;
import com.ogive.oheo.persistence.entities.VehicleTransmission;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.repo.FeaturesRepository;
import com.ogive.oheo.persistence.repo.ImagesRepository;
import com.ogive.oheo.persistence.repo.ProductRepository;
import com.ogive.oheo.persistence.repo.ProductSpecificationRepository;
import com.ogive.oheo.persistence.repo.SliderRepository;
import com.ogive.oheo.persistence.repo.VehicleBodyTypeRepository;
import com.ogive.oheo.persistence.repo.VehicleDetailRepository;
import com.ogive.oheo.persistence.repo.VehicleFuelTypeRepository;
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

	//Product API
	
	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/products", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addProduct(@ModelAttribute ProductRequestDTO productRequestDTO) {
		LOG.info("addProduct request received@@   {}", productRequestDTO);
		// Product Entity
		Product entity = new Product();
		BeanUtils.copyProperties(productRequestDTO, entity);
		// ProductSpecification
		SpecificationDTO SpecificationDTO = productRequestDTO.getSpecification();
		ProductSpecification specificationEntity = new ProductSpecification();
		BeanUtils.copyProperties(SpecificationDTO, specificationEntity);
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
		Optional<VehicleTransmission> vehicleTransmissionData = vehicleTransmissionRepository.findById(vehicleTransmissionId);

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
			imagesFile.forEach(image -> {
				Images fileEntity = new Images();
				multiPartToFileEntity(image, fileEntity, productRequestDTO.getImageType());
				fileEntity.setProduct(productEntity);
				imagesRepository.save(fileEntity);
			});
		}
		
		// Save brochure
		if (null != productRequestDTO.getBrochure()) {
			Images brochure = new Images();
			multiPartToFileEntity(productRequestDTO.getBrochure(), brochure, productRequestDTO.getImageType());
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
		return new ResponseEntity<Object>(productEntity.getId(), HttpStatus.OK);
	}
	
	@Transactional
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getProduct(@PathVariable Long id) {
		LOG.info("getProduct request received@@   {}", id);
		Optional<Product> entityData = productRepository.findById(id);
		if (entityData.isPresent()) {
			Product entity = entityData.get();
			ProductResponseDTO dto = new ProductResponseDTO();
			populateProductEntityToDTO(entity, dto);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@Transactional 
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllProducts(
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
		Page<Product> pages = productRepository
				.findAll(filterProductByName(criteria).and(filterProductByStatus(criteria)), paging);
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
				dto.add(linkTo(methodOn(CMSControllerNew.class).getProduct(product.getId())).withRel("Product"));
				allDTO.add(dto);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		});

		return new ResponseEntity<Object>(allDTO, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Pull slider", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/products-live/sliders/{id}",produces = { MediaType.APPLICATION_JSON_VALUE })
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
	
	@ApiOperation(value = "Deletes the entity with the given id", notes = "If the entity is not found in the persistence store it is silently ignored.", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/products/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteProduct(@PathVariable Long id) {
		LOG.info("deleteProduct request received @@   {}", id);
		productRepository.deleteById(id);
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

		//ProductSpecification
		ProductSpecification productSpecification = entity.getProductSpecification();
		if (Objects.nonNull(productSpecification)) {
			ProductSpecificationResponseDTO ProductSpecificationResponseDTO = new ProductSpecificationResponseDTO();
			BeanUtils.copyProperties(productSpecification, ProductSpecificationResponseDTO);
			dto.setProductSpecification(ProductSpecificationResponseDTO);
		}
		//Images
		Set<Images> images = entity.getImages();
		if (null != images) {
			Set<ImagesResponseDTO> imagesDTOList = new HashSet<>();
			images.forEach(img -> {
				ImagesResponseDTO imgDTO = new ImagesResponseDTO();
				BeanUtils.copyProperties(img, imgDTO);
				imgDTO.add(linkTo(methodOn(FileProcessingController.class).readFile(img.getId())).withSelfRel());
				imagesDTOList.add(imgDTO);
			});
			dto.setImages(imagesDTOList);
		}
		
		Set<Features> features = entity.getFeatures();
		if (null != features) {
			Set<String> featureSet = features.stream().map(feature -> feature.getName()).collect(Collectors.toSet());
			dto.setFeatures(featureSet);
		}
		dto.add(linkTo(methodOn(CMSControllerNew.class).getProduct(entity.getId())).withSelfRel());
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
	
	
	// Slider API 
	
	
	
	
}
