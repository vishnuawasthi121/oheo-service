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

import com.ogive.oheo.dto.ErrorResponseDTO;
import com.ogive.oheo.dto.GroupDetailRequestDTO;
import com.ogive.oheo.dto.GroupDetailResponseDTO;
import com.ogive.oheo.persistence.entities.GroupDetail;
import com.ogive.oheo.persistence.repo.GroupDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Group Management")
@RestController
@RequestMapping("/groups")
public class GroupDetailController {

	private static final Logger LOG = LoggerFactory.getLogger(GroupDetailController.class);

	@Autowired
	private GroupDetailRepository groupDetailRepository;

	@ApiOperation(value = "Saves a given entity. Use the returned instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addGroup(@Valid @RequestBody GroupDetailRequestDTO groupDetailRequestDTO) {
		LOG.info("addGroup request received@@   {}", groupDetailRequestDTO);
		GroupDetail entity = new GroupDetail();
		
		BeanUtils.copyProperties(groupDetailRequestDTO, entity);
		entity.setStatus(groupDetailRequestDTO.getStatus());
		
		GroupDetail saved = groupDetailRepository.save(entity);
		LOG.info("Saved @@   {}", saved);
		return new ResponseEntity<Object>(saved.getId(), HttpStatus.OK);
	}
	
	@PutMapping(path = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateGroup(@PathVariable Long id, @RequestBody GroupDetailRequestDTO requestBody) {
		LOG.info("updateGroup requested record id {} ", id);
		LOG.info("updateGroup request body@@   {}", requestBody);

		Optional<GroupDetail> groupData = groupDetailRepository.findById(id);

		if (groupData.isPresent()) {

			GroupDetail entity = groupData.get();
			entity.setDescription(requestBody.getDescription());
			entity.setGroupName(requestBody.getGroupName());
			entity.setStatus(requestBody.getStatus());

			GroupDetail updatedEntity = groupDetailRepository.save(entity);
			return new ResponseEntity<Object>(updatedEntity, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(new ErrorResponseDTO("Did not find a Group with id=" + id),HttpStatus.BAD_REQUEST);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getGroup(@PathVariable Long id) {
		LOG.info("getGroup request received@@id   {}", id);
		Optional<GroupDetail> entityData = groupDetailRepository.findById(id);
		
		if (entityData.isPresent()) {
			GroupDetail groupDetail = entityData.get();
			GroupDetailResponseDTO dto = new GroupDetailResponseDTO();
			BeanUtils.copyProperties(groupDetail, dto);
			dto.setStatus(groupDetail.getStatus());
			
			dto.add(linkTo(methodOn(GroupDetailController.class).getGroup(groupDetail.getId())).withSelfRel());
			LOG.info("Found data against ID {} and returing response   {}", id, groupDetail);
			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		LOG.info("Did not find ZoneDetail by @@id   {}", id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	@ApiOperation(value = "Returns all instances of the type", notes = "Returns all instances of the type", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getAllGroupss() {
		LOG.info("getAllGroupss request received");

		Iterable<GroupDetail> groups = groupDetailRepository.findAll();
		List<GroupDetailResponseDTO> groupsDTOList = new ArrayList<>();

		if (null != groups) {
			groups.forEach(entity -> {
				GroupDetailResponseDTO dto = new GroupDetailResponseDTO();
				BeanUtils.copyProperties(entity, dto);
				dto.setStatus(entity.getStatus());
				dto.add(linkTo(methodOn(GroupDetailController.class).getGroup(entity.getId())).withSelfRel());
				groupsDTOList.add(dto);
			});
			return new ResponseEntity<Object>(groupsDTOList, HttpStatus.OK);
		}
		LOG.info("Did not find any records  getAllGroupss {}", groups);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}

	@ApiOperation(value = "Delete an entity by its id", notes = "Deletes a given entity.If the entity is not found in the persistence store it is silently ignored", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@DeleteMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> deleteGroup(@PathVariable Long id) {
		LOG.info("deleteGroup request received@@   {}", id);
		groupDetailRepository.deleteById(id);
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
}
