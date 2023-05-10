package com.ogive.oheo.controller;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Date;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.constants.RoleTypes;
/*import com.ogive.oheo.constants.UserRole;*/
import com.ogive.oheo.dto.UserDetailRequestDTO;
import com.ogive.oheo.dto.UserDetailResponseDTO;
import com.ogive.oheo.persistence.entities.UserDetail;
import com.ogive.oheo.persistence.repo.UserDetailRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "User Setup")
@RestController
@RequestMapping("/user-setup")
public class UserDetailController {

	private static final Logger LOG = LoggerFactory.getLogger(UserDetailController.class);

	@Autowired
	private UserDetailRepository userDetailRepository;

	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addUser(@Valid @RequestBody UserDetailRequestDTO userDetailRequestDTO) {
		LOG.info("addUser request received@@   {}", userDetailRequestDTO);
		UserDetail entity = new UserDetail();
		BeanUtils.copyProperties(userDetailRequestDTO, entity);
		
		entity.setCreated(new Date());
		entity.setUpdated(new Date());

		String createdByUser = userDetailRequestDTO.getCreatedByUser();
		UserDetail userDetail = userDetailRepository.findByEmail(createdByUser);
		//UserRole role = userDetail.getRole();

		/*
		 * if (!RoleTypes.Admin.name().equalsIgnoreCase(role.getName())) {
		 * entity.setDistributor(userDetail); }
		 */
		UserDetail savedEntitu = userDetailRepository.save(entity);
		LOG.info("Saved @@   {}", savedEntitu);
		return new ResponseEntity<Object>(savedEntitu.getId(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "Retrieves an entity by its id", notes = "Return Id of the record if saved correctly otherwise null", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@GetMapping(path = "/{id}", produces = { MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<Object> getUserDetail(@PathVariable Long id) {
		LOG.info("getUserDetail request received@@   {}", id);
		Optional<UserDetail> fetchedData = userDetailRepository.findById(id);
		if (fetchedData.isPresent()) {
			UserDetail entity = fetchedData.get();
			UserDetailResponseDTO dto = new UserDetailResponseDTO();
			BeanUtils.copyProperties(entity, dto);
			
			dto.add(linkTo(methodOn(UserDetailController.class).getUserDetail(entity.getId())).withSelfRel());
			Set<UserDetailResponseDTO> dealersDTOList = new HashSet<UserDetailResponseDTO>();
			Set<UserDetail> dealers = entity.getDealers();
			
			if (null != dealers) {
				dealers.forEach(dealer -> {
					UserDetailResponseDTO dealerDTO = new UserDetailResponseDTO();
					BeanUtils.copyProperties(dealer, dealerDTO);
					dealerDTO.setDistributorId(entity.getId());
					
					dealerDTO.add(linkTo(methodOn(UserDetailController.class).getUserDetail(dealer.getId())).withSelfRel());
					dealersDTOList.add(dealerDTO);

				});
			}

			return new ResponseEntity<Object>(dto, HttpStatus.OK);
		}
		return new ResponseEntity<Object>(HttpStatus.OK);
	}
	
	

}
