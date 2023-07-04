package com.ogive.oheo.controller;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(tags = "Orders")
@RestController
public class OrdersController {

	private static final Logger LOG = LoggerFactory.getLogger(OrdersController.class);

	@Transactional
	@ApiOperation(value = "Saves a given entity. Use the latest instance for further operations as the save operation might have changed the entity instance completely", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
	@PostMapping(path = "/orders", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> addOrders() {
		LOG.info("addOrders request received@@   {}");

		return new ResponseEntity<Object>(HttpStatus.OK);
	}

}
