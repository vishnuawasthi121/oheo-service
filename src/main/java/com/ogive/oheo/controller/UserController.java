package com.ogive.oheo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ogive.oheo.dto.UserDTO;
import com.ogive.oheo.services.UserService;

/**
 * @author Vishnu Awasthi Vishnuawasthi121@gmail.com
 */

@RestController

@RequestMapping("/users")
public class UserController {

	private static final Logger LOG = LoggerFactory.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@PostMapping
	public ResponseEntity<Object> addUser(@RequestBody UserDTO userDTO) {
		LOG.info("addUser request received@@   {}", userDTO);
		Long id = userService.addUser(userDTO);
		return new ResponseEntity<Object>(id, HttpStatus.OK);

	}

}
