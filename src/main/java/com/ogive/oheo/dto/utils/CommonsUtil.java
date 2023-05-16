package com.ogive.oheo.dto.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Random;

import org.apache.commons.text.CharacterPredicates;
import org.apache.commons.text.RandomStringGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import com.ogive.oheo.controller.VehicleSetupController;
import com.ogive.oheo.dto.VehicleBodyTypeResponseDTO;
import com.ogive.oheo.dto.VehicleDetailResponseDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleType;

public class CommonsUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CommonsUtil.class);

	public static void entityToDTO(VehicleDetail entity, VehicleDetailResponseDTO dto) {
		BeanUtils.copyProperties(entity, dto);
		dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleDetails(entity.getId())).withSelfRel());

		VehicleBodyType vehicleBodyType = entity.getVehicleBodyType();
		if (Objects.nonNull(vehicleBodyType)) {
			VehicleBodyTypeResponseDTO bodyTypeResponseDTO = new VehicleBodyTypeResponseDTO();
			BeanUtils.copyProperties(vehicleBodyType, bodyTypeResponseDTO);
			dto.setBodyType(bodyTypeResponseDTO);

		}
		VehicleType vehicleType = entity.getVehicleType();
		if (Objects.nonNull(vehicleType)) {
			VehicleTypeResponseDTO vehicleTypeResponseDTO = new VehicleTypeResponseDTO();
			BeanUtils.copyProperties(vehicleType, vehicleTypeResponseDTO);
			dto.setVehicleType(vehicleTypeResponseDTO);
		}
	}

	public static Date convertStringToDate(String dateStr, String pattern) {
		LOG.info("convertStringToDate   .. ");
		Date date = null;
		SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
		try {
			date = dateFormat.parse(dateStr);
		} catch (ParseException e) {
			LOG.error("Exception in date format..", e);
		}
		return date;

	}

	public static String generatePassword() {

		// Generates a 12 code point string, using only the letters a-z
		RandomStringGenerator generator = new RandomStringGenerator.Builder()  
                .withinRange('0', 'z')  
               // .filteredBy(CharacterPredicates.DIGITS)  
                .build();  
		return generator.generate(8);
	}

	
	  public static void main(String... args) {
	  
	//  System.out.println(generatePassword());
		  
		  Random random = new java.util.Random();
			String id = String.format("%04d", random.nextInt(10000));
			System.out.println(id);
	  }
	 
}
