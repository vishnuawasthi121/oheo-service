package com.ogive.oheo.controller;

import static com.ogive.oheo.dto.utils.CMSSpecifications.*;
import static com.ogive.oheo.dto.utils.CMSSpecifications.filterPositionByStatus;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.FilterCriteria;
import com.ogive.oheo.dto.FrameRequestDTO;
import com.ogive.oheo.dto.FrameResponseDTO;
import com.ogive.oheo.dto.PositionRequestDTO;
import com.ogive.oheo.dto.PositionResponseDTO;
import com.ogive.oheo.dto.WidgetRequestDTO;
import com.ogive.oheo.dto.WidgetResponseDTO;
import com.ogive.oheo.exception.ValidationException;
import com.ogive.oheo.persistence.entities.Frame;
import com.ogive.oheo.persistence.entities.Position;
import com.ogive.oheo.persistence.entities.Widget;
import com.ogive.oheo.persistence.repo.FrameRepository;
import com.ogive.oheo.persistence.repo.PositionRepository;
import com.ogive.oheo.persistence.repo.WidgetRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "CMS")
@RestController

@RequestMapping("/cms")
public class CMSController {

	private static final Logger LOG = LoggerFactory.getLogger(CMSController.class);

	@Autowired
	private PositionRepository positionRepository;
	
	@Autowired
	private FrameRepository frameRepository;
	
	@Autowired
	private WidgetRepository widgetRepository;

	// Position Api
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/positions", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addPosition(@Valid @RequestBody PositionRequestDTO positionRequestDTO) {
		LOG.info("addPosition request received@@   {}", positionRequestDTO);
		Position entity = new Position();
		BeanUtils.copyProperties(positionRequestDTO, entity);
		Position savedEntity = positionRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	@PutMapping(path = "/positions/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updatePosition(@PathVariable Long id,@Valid @RequestBody PositionRequestDTO positionRequestDTO) {
		LOG.info("updatePosition requested record id {} ", id);
		LOG.info("updatePosition request body@@   {}", positionRequestDTO);

		Optional<Position> entityData = positionRepository.findById(id);
		if (entityData.isPresent()) {
			Position entityToUpdate = entityData.get();
			entityToUpdate.setName(positionRequestDTO.getName());
			entityToUpdate.setShortCode(positionRequestDTO.getShortCode());
			entityToUpdate.setStatus(positionRequestDTO.getStatus());
			Position updatedCountry = positionRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(updatedCountry, HttpStatus.OK);
		}
		
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Position with id=" + id),HttpStatus.BAD_REQUEST);
				
	}
	
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/positions/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getPosition(@PathVariable Long id) {
		LOG.info("getPosition request received@@   {}", id);
		Optional<Position> entityData = positionRepository.findById(id);
		if (entityData.isPresent()) {
			Position entity = entityData.get();
			PositionResponseDTO dto = new PositionResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(CMSController.class).getPosition(entity.getId())).withSelfRel());
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/positions", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllPositions(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllPositions request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<PositionResponseDTO> allDTO = new ArrayList<>();
		Page<Position> pages = positionRepository.findAll(filterPositionByName(criteria).and(filterPositionByStatus(criteria)), paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				PositionResponseDTO dto = new PositionResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(CMSController.class).getPosition(entity.getId())).withSelfRel());
				allDTO.add(dto);
			});
		}
		response.put("positions", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total positions count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/positions/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deletePosition(@PathVariable Long id) {
		LOG.info("deletePosition request received@@   {}", id);
		positionRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	//Frame Api 
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/frames", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addFrame(@Valid @RequestBody FrameRequestDTO requestDTO) {
		LOG.info("addFrame request received@@   {}", requestDTO);
		Frame entity = new Frame();
		BeanUtils.copyProperties(requestDTO, entity);
		List<Long> positions = requestDTO.getPositions();

		if (!CollectionUtils.isEmpty(positions)) {
			Set<Position> positionEntities = new HashSet<>();
			positions.forEach(position -> {
				Optional<Position> positionData = positionRepository.findById(position);
				if (!positionData.isPresent()) {
					throw new ValidationException("Unable to find Position against id " + position);
				}
				positionEntities.add(positionData.get());
			});
			entity.setPositions(positionEntities);
		}
		Frame savedEntity = frameRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	
	@PutMapping(path = "/frames/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateFrame(@PathVariable Long id,
			@Valid @RequestBody FrameRequestDTO frameRequestDTO) {
		LOG.info("updateFrame requested record id {} ", id);
		LOG.info("updateFrame request body@@   {}", frameRequestDTO);
		Optional<Frame> entityData = frameRepository.findById(id);
		if (entityData.isPresent()) {
			Frame entityToUpdate = entityData.get();
			entityToUpdate.setName(frameRequestDTO.getName());
			entityToUpdate.setMetaContent(frameRequestDTO.getMetaContent());
			entityToUpdate.setStatus(frameRequestDTO.getStatus());
			// entityToUpdate.setPositions(null)
			List<Long> positions = frameRequestDTO.getPositions();
			if (!CollectionUtils.isEmpty(positions)) {
				Set<Position> positionEntities = new HashSet<>();
				positions.forEach(position -> {
					Optional<Position> positionData = positionRepository.findById(position);
					if (!positionData.isPresent()) {
						throw new ValidationException("Unable to find Position against id " + position);
					}
					positionEntities.add(positionData.get());
				});
				entityToUpdate.setPositions(positionEntities);
			}
			Frame upatedEntity = frameRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(upatedEntity, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Frame with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/frames/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getFrame(@PathVariable Long id) {
		LOG.info("getPosition request received@@   {}", id);
		Optional<Frame> entityData = frameRepository.findById(id);
		if (entityData.isPresent()) {
			Frame entity = entityData.get();
			FrameResponseDTO dto = new FrameResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(CMSController.class).getFrame(entity.getId())).withSelfRel());
			Set<Position> positions = entity.getPositions();
			if(!CollectionUtils.isEmpty(positions)) {
				    List<PositionResponseDTO> positionsDTO = positions.stream().map(position ->{
					PositionResponseDTO positionResponseDTO = new PositionResponseDTO();
					BeanUtils.copyProperties(position, positionResponseDTO);
					//positionResponseDTO.add(linkTo(methodOn(CMSController.class).getPosition(position.getId())).withSelfRel());
					return positionResponseDTO;
				}).collect(Collectors.toList());
				    dto.setPositions(positionsDTO);
			}
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/frames", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllFrames(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllFrames request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<FrameResponseDTO> allDTO = new ArrayList<>();
		Page<Frame> pages = frameRepository.findAll(filterFrameByName(criteria).and(filterFrameByStatus(criteria)),paging);
		
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				FrameResponseDTO dto = new FrameResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(CMSController.class).getFrame(entity.getId())).withSelfRel());
				allDTO.add(dto);
				Set<Position> positions = entity.getPositions();
				
				if (!CollectionUtils.isEmpty(positions)) {
					List<PositionResponseDTO> positionsDTO = positions.stream().map(position -> {
						PositionResponseDTO positionResponseDTO = new PositionResponseDTO();
						BeanUtils.copyProperties(position, positionResponseDTO);
						// positionResponseDTO.add(linkTo(methodOn(CMSController.class).getPosition(position.getId())).withSelfRel());
						return positionResponseDTO;
					}).collect(Collectors.toList());
					dto.setPositions(positionsDTO);
				}
			});
		}
		response.put("frames", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total frames count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/frames/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteFrame(@PathVariable Long id) {
		LOG.info("deleteFrame request received@@   {}", id);
		frameRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	//Widget
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/widgets", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addWidget(@Valid @RequestBody WidgetRequestDTO requestDTO) {
		LOG.info("addWidget request received@@   {}", requestDTO);
		Widget entity = new Widget();
		BeanUtils.copyProperties(requestDTO, entity);
		Optional<Position> positionData = positionRepository.findById(requestDTO.getPositionId());
		if (Objects.isNull(positionData.get())) {
			throw new ValidationException("Unable to find Position against id " + requestDTO.getPositionId());
		}
		entity.setPosition(positionData.get());
		Widget savedEntity = widgetRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntity);
		return new ResponseEntity<Object>(savedEntity.getId(), HttpStatus.OK);
	}
	
	@PutMapping(path = "/widgets/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateWidget(@PathVariable Long id, @Valid @RequestBody WidgetRequestDTO requestDTO) {
		LOG.info("updateWidget requested record id {} ", id);
		Optional<Widget> entityData = widgetRepository.findById(id);
		Optional<Position> positionData = positionRepository.findById(requestDTO.getPositionId());
		
		if (Objects.isNull(positionData.get())) {
			throw new ValidationException("Unable to find Position against id " + requestDTO.getPositionId());
		}
		if (entityData.isPresent()) {
			Widget entityToUpdate = entityData.get();
			
			entityToUpdate.setName(requestDTO.getName());
			entityToUpdate.setShortCode(requestDTO.getShortCode());
			entityToUpdate.setStatus(requestDTO.getStatus());
			entityToUpdate.setContent(requestDTO.getContent());
			entityToUpdate.setPosition(positionData.get());
			
			Widget savedEntity = widgetRepository.save(entityToUpdate);
			return new ResponseEntity<Object>(savedEntity, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Widget with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/widgets/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getWidget(@PathVariable Long id) {
		LOG.info("getWidget request received@@   {}", id);
		Optional<Widget> entityData = widgetRepository.findById(id);
		if (entityData.isPresent()) {
			
			Widget entity = entityData.get();
			WidgetResponseDTO dto = new WidgetResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			dto.add(linkTo(methodOn(CMSController.class).getWidget(entity.getId())).withSelfRel());
			Position position = entity.getPosition();
			if (Objects.nonNull(position)) {
				PositionResponseDTO posotionDTO = new PositionResponseDTO();
				BeanUtils.copyProperties(position, posotionDTO);
				dto.setPosition(posotionDTO);
			}
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/widgets", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllWidgets(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size, 
			@RequestParam(required = false) String filterByName,
			@RequestParam(required = false, defaultValue = "ASC") Direction sortDirection,
			@RequestParam(required = false, defaultValue = "id") String[] orderBy,
			@RequestParam(required = false) StatusCode status) {
		LOG.info("getAllWidgets request received");
		FilterCriteria criteria = new FilterCriteria(page, size, filterByName, sortDirection, orderBy, status);
		Direction sort = sortDirection == null ? Direction.ASC : sortDirection;
		Pageable paging = PageRequest.of(page, size, Sort.by(sort, orderBy));
		Map<String, Object> response = new HashMap<>();
		List<WidgetResponseDTO> allDTO = new ArrayList<>();
		Page<Widget> pages = widgetRepository.findAll(filterWidgetByName(criteria).and(filterWidgetByStatus(criteria)),
				paging);
		if (pages.hasContent()) {
			pages.getContent().forEach(entity -> {
				WidgetResponseDTO dto = new WidgetResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.add(linkTo(methodOn(CMSController.class).getWidget(entity.getId())).withSelfRel());
				Position position = entity.getPosition();
				if (Objects.nonNull(position)) {
					PositionResponseDTO posotionDTO = new PositionResponseDTO();
					BeanUtils.copyProperties(position, posotionDTO);
					dto.setPosition(posotionDTO);
				}
				allDTO.add(dto);
			});
		}
		response.put("widgets", allDTO);
		response.put("currentPage", pages.getNumber());
		response.put("totalElements", pages.getTotalElements());
		response.put("totalPages", pages.getTotalPages());
		LOG.info("Total widgets count {}", allDTO.size());
		return new ResponseEntity<Object>(response, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Delete an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/widgets/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteWidget(@PathVariable Long id) {
		LOG.info("deletePosition request received@@   {}", id);
		widgetRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	
	
}
