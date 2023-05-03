package com.ogive.oheo.dto.utils;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import com.ogive.oheo.controller.FileProcessingController;
import com.ogive.oheo.controller.VehicleSetupController;
import com.ogive.oheo.dto.AddressResponseDTO;
import com.ogive.oheo.dto.CityResponseDTO;
import com.ogive.oheo.dto.FileResponseDTO;
import com.ogive.oheo.dto.StateResponseDTO;
import com.ogive.oheo.dto.VehicleBodyTypeResponseDTO;
import com.ogive.oheo.dto.VehicleDetailResponseDTO;
import com.ogive.oheo.dto.VehicleTypeResponseDTO;
import com.ogive.oheo.dto.ZipcodeResponseDTO;
import com.ogive.oheo.dto.ZoneDetailResponseDTO;
import com.ogive.oheo.persistence.entities.Address;
import com.ogive.oheo.persistence.entities.City;
import com.ogive.oheo.persistence.entities.Images;
import com.ogive.oheo.persistence.entities.State;
import com.ogive.oheo.persistence.entities.VehicleBodyType;
import com.ogive.oheo.persistence.entities.VehicleDetail;
import com.ogive.oheo.persistence.entities.VehicleType;
import com.ogive.oheo.persistence.entities.Zipcode;
import com.ogive.oheo.persistence.entities.ZoneDetail;

public class CommonsUtil {

	private static final Logger LOG = LoggerFactory.getLogger(CommonsUtil.class);

	public static void entityToDTO(VehicleDetail entity, VehicleDetailResponseDTO dto) {
		BeanUtils.copyProperties(entity, dto);
		// dto.setStatus(entity.getStatus());
		dto.add(linkTo(methodOn(VehicleSetupController.class).getVehicleDetails(entity.getId())).withSelfRel());
		Address address = entity.getAddress();

		if (Objects.nonNull(address)) {
			AddressResponseDTO addressResponseDTO = new AddressResponseDTO();
			addressResponseDTO.setId(address.getId());

			City city = address.getCity();
			if (Objects.nonNull(city)) {
				CityResponseDTO cityResponseDTO = new CityResponseDTO();
				BeanUtils.copyProperties(city, cityResponseDTO);
				cityResponseDTO.setStateId(city.getState().getId());
				cityResponseDTO.setStateName(city.getState().getStateName());
				addressResponseDTO.setCity(cityResponseDTO);
			}
			State state = address.getState();
			if (Objects.nonNull(state)) {
				StateResponseDTO stateResponseDTO = new StateResponseDTO();
				BeanUtils.copyProperties(state, stateResponseDTO);
				stateResponseDTO.setCountryCode(state.getCountry().getCountryCode());
				addressResponseDTO.setState(stateResponseDTO);
			}

			Zipcode zipcode = address.getZipcode();
			if (Objects.nonNull(zipcode)) {
				ZipcodeResponseDTO zipcodeResponseDTO = new ZipcodeResponseDTO();
				BeanUtils.copyProperties(zipcode, zipcodeResponseDTO);
				zipcodeResponseDTO.setCityId(zipcode.getCity().getId());
				zipcodeResponseDTO.setCityName((zipcode.getCity().getName()));
				addressResponseDTO.setZipcode(zipcodeResponseDTO);
			}
			ZoneDetail zone = address.getZone();
			if (Objects.nonNull(zone)) {
				ZoneDetailResponseDTO zoneDetailResponseDTO = new ZoneDetailResponseDTO();
				BeanUtils.copyProperties(zone, zoneDetailResponseDTO);
				addressResponseDTO.setZone(zoneDetailResponseDTO);
			}
			dto.setAddress(addressResponseDTO);
		}
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
		// Image
		Set<Images> images = entity.getImages();
		List<FileResponseDTO> files = new ArrayList<>();

		if (!CollectionUtils.isEmpty(images)) {
			images.forEach(fileEntity -> {
				FileResponseDTO fileDTO = new FileResponseDTO();
				BeanUtils.copyProperties(fileEntity, fileDTO);
				fileDTO.add(
						linkTo(methodOn(FileProcessingController.class).readFile(fileEntity.getId())).withSelfRel());
				files.add(fileDTO);
			});
			dto.setImages(files);
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
}
